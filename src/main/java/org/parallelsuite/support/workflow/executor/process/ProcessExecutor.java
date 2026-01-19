package org.parallelsuite.support.workflow.executor.process;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.status.ExecutionStatus;

import java.util.Optional;

public interface ProcessExecutor<T> {
  void execute(T argument);

  Optional<MessageQueue> getQueue();

  void initiate();

  void shutDown();

  ExecutionStatus getStatus();

}
