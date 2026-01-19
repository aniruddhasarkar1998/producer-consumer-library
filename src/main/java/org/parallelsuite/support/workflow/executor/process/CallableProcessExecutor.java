package org.parallelsuite.support.workflow.executor.process;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.process.CallableProcess;
import org.parallelsuite.support.workflow.models.queue.Message;
import org.parallelsuite.support.workflow.models.queue.MessageType;
import org.parallelsuite.support.workflow.models.status.ExecutionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.parallelsuite.support.workflow.executor.utils.WorkflowExecutionUtils.completeAllFutures;
import static org.parallelsuite.support.workflow.executor.utils.WorkflowExecutionUtils.gracefulShutdown;

public class CallableProcessExecutor<R> implements ProcessExecutor<R> {

  private MessageQueue<R> queue;

  private CallableProcess<R> process;

  private ExecutorService executorService;

  private ExecutionStatus status;

  private List<Future> futures = new ArrayList<>();

  public CallableProcessExecutor(CallableProcess<R> process, MessageQueue<R> queue) {
    this.queue = queue;
    this.process = process;
    status = ExecutionStatus.INITIALIZED;
  }

  @Override
  public void execute(Object argument) {
    status = ExecutionStatus.RUNNING;
    for (int i = 0; i < process.getIterations(); i++) {
      futures.add(executorService.submit(() -> {
        R result;
        try {
          result = process.getCallable().call();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        queue.put(new Message<>(result));
        return result;
      }));
    }
    System.out.println(futures.size());
    executorService.execute(() -> {
      completeAllFutures(futures);
      queue.put(new Message<>(MessageType.EOF));
    });
  }

  @Override
  public Optional<MessageQueue> getQueue() {
    return Optional.of(queue);
  }

  @Override
  public void initiate() {
    executorService = Executors.newFixedThreadPool(process.getNThreads());
    status = ExecutionStatus.STARTED;
  }

  @Override
  public void shutDown() {
    executorService.shutdown();
    status = ExecutionStatus.STOPPED;
  }

  @Override
  public ExecutionStatus getStatus() {
    return status;
  }

}
