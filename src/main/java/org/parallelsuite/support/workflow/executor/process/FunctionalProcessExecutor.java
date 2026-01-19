package org.parallelsuite.support.workflow.executor.process;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.process.FunctionalProcess;
import org.parallelsuite.support.workflow.models.status.ExecutionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.parallelsuite.support.workflow.executor.utils.WorkflowExecutionUtils.gracefulShutdown;

public class FunctionalProcessExecutor<T, R> implements ProcessExecutor<T> {

  private FunctionalProcess<T, R> process;

  private ExecutorService executorService;

  private MessageQueue<R> queue;

  private ExecutionStatus status;

  private List<Future> futures = new ArrayList<>();

  public FunctionalProcessExecutor(FunctionalProcess<T, R> process, MessageQueue<R> queue) {
    this.process = process;
    this.queue = queue;
    status = ExecutionStatus.INITIALIZED;
  }

  @Override
  public void execute(T argument) {
    futures.add(executorService.submit(() -> process.getFunction().apply(argument)));
    status = ExecutionStatus.RUNNING;
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
    gracefulShutdown(executorService, futures);
    status = ExecutionStatus.STOPPED;
  }

  @Override
  public ExecutionStatus getStatus() {
    return status;
  }

}
