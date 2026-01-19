package org.parallelsuite.support.workflow.executor;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.executor.step.StepExecutor;
import org.parallelsuite.support.workflow.executor.step.StepExecutorFactory;
import org.parallelsuite.support.workflow.models.queue.Message;
import org.parallelsuite.support.workflow.models.queue.MessageType;
import org.parallelsuite.support.workflow.models.workflow.Workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WorkflowExecutor {

  private Workflow workflow;

  private List<StepExecutor> stepExecutors = new ArrayList<>();

  public WorkflowExecutor(final Workflow workflow) {
    this.workflow = workflow;
    MessageQueue<?> queueFrom = new MessageQueue<>();
    queueFrom.put(new Message<>());
    queueFrom.put(new Message<>(MessageType.EOF));
    for (int i = 0; i < workflow.getSteps().size(); i++) {
      MessageQueue<?> queueTo = new MessageQueue<>();
      stepExecutors.add(StepExecutorFactory.getExecutor(workflow.getSteps().get(i), queueFrom, queueTo));
      queueFrom = queueTo;
    }
  }

  public void initiateThreadsAndExecute() {
    stepExecutors.forEach(StepExecutor::initiate);
    stepExecutors.forEach(StepExecutor::execute);
  }

  public void shutDown(Callable<Boolean> endCondition, long waitTimeInMillis) {
    try {
      boolean conditionSatisfied = endCondition.call();
      while (!conditionSatisfied) {
        conditionSatisfied = endCondition.call();
        Thread.sleep(waitTimeInMillis);
      }
      stepExecutors.forEach(StepExecutor::gracefulShutDown);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
