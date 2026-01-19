package org.parallelsuite.support.workflow.executor.process;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.process.ConsumerProcess;
import org.parallelsuite.support.workflow.models.status.ExecutionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.parallelsuite.support.workflow.executor.utils.WorkflowExecutionUtils.gracefulShutdown;

public class ConsumerProcessExecutor<T> implements ProcessExecutor<T> {

  private ConsumerProcess<T> process;

  private ExecutorService executorService;

  private ExecutionStatus status;

  private T argument;

  private List<Future> futures = new ArrayList<>();

  public ConsumerProcessExecutor(ConsumerProcess<T> process) {
    this.process = process;
    status = ExecutionStatus.INITIALIZED;
  }

  @Override
  public void execute(T argument) {
    status = ExecutionStatus.RUNNING;
    futures.add(executorService.submit(() -> {
      process.getConsumer().accept(argument);
    }));
  }

  @Override
  public Optional<MessageQueue> getQueue() {
    return Optional.empty();
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
