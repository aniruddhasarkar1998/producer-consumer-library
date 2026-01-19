package org.parallelsuite.support.workflow.executor.step;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.step.MultiProcessStep;
import org.parallelsuite.support.workflow.models.step.Step;

import java.util.concurrent.LinkedBlockingQueue;

public class StepExecutorFactory {

  public static <T, R> StepExecutor<T, R> getExecutor(Step step, MessageQueue<T> queueFrom, MessageQueue<R> queueTo) {
    if (step instanceof MultiProcessStep) {
      return new MultiProcessStepExecutor<>((MultiProcessStep<T, R>) step, queueFrom, queueTo);
    }
    return new StepExecutor<>(step, queueFrom, queueTo);
  }

}
