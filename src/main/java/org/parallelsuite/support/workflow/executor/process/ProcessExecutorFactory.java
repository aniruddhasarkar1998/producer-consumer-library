package org.parallelsuite.support.workflow.executor.process;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.process.CallableProcess;
import org.parallelsuite.support.workflow.models.process.ConsumerProcess;
import org.parallelsuite.support.workflow.models.process.FunctionalProcess;
import org.parallelsuite.support.workflow.models.process.Process;

import java.util.concurrent.LinkedBlockingQueue;

public class ProcessExecutorFactory {

  public static <T, R> ProcessExecutor getProcessExecutor(Process process, MessageQueue<R> queueTo) {
    Class<? extends Process> processType = process.getClass();
    if (processType.equals(CallableProcess.class)) {
      return new CallableProcessExecutor<R>((CallableProcess<R>) process, queueTo);
    } else if (processType.equals(FunctionalProcess.class)) {
      return new FunctionalProcessExecutor<T, R>((FunctionalProcess<T, R>) process, queueTo);
    } else {
      return new ConsumerProcessExecutor<T>((ConsumerProcess<T>) process);
    }
  }

}
