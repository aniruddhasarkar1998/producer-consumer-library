package org.parallelsuite.support.workflow.validation;

import org.parallelsuite.support.workflow.models.consumer.ConsumerOnly;
import org.parallelsuite.support.workflow.models.process.CallableProcess;
import org.parallelsuite.support.workflow.models.process.ConsumerProcess;
import org.parallelsuite.support.workflow.models.process.Process;
import org.parallelsuite.support.workflow.models.producer.ProducerOnly;
import org.parallelsuite.support.workflow.models.step.MultiProcessStep;
import org.parallelsuite.support.workflow.models.step.Step;
import org.parallelsuite.support.workflow.models.workflow.Workflow;

import java.util.List;

public class WorkflowValidationUtils {

  /**
   * Validates if a Step can be added to a Workflow.
   * This method checks the following conditions:
   * 1. If the Workflow is empty, the first Process in the Step must be a CallableProcess.
   * 2. If the Workflow is not empty, the first Process in the Step must not be a CallableProcess.
   * 3. The type produced by the last Process of the previous Step must be assignable to the type consumed by the first Process of the new Step.
   * If the last Process of the previous Step is a ConsumerProcess, no new Process can be added after it.
   * @param workflow
   * @param step
   */
  public static void validateStepAdditionToWorkflow(Workflow workflow, Step step) {
    List<Step> previousSteps = workflow.getSteps();

    if (previousSteps.isEmpty() && !step.getProcesses().get(0).getClass().equals(CallableProcess.class)) {
      throw new IllegalArgumentException("First process needs to have Callable method");
    } else if (!previousSteps.isEmpty()) {
      if (step.getProcesses().get(0).getClass().equals(CallableProcess.class)) {
        throw new IllegalArgumentException("Cannot add a CallableProcess after the beginning");
      }

      Process previousFirstProcess = previousSteps.get(previousSteps.size() - 1).getProcesses().get(0);
      if (previousFirstProcess.getClass().equals(ConsumerProcess.class)) {
        throw new IllegalArgumentException("Cannot add a new Process after a ConsumerProcess");
      }

      Step previousStep = previousSteps.get(previousSteps.size() - 1);

      ProducerOnly producerOnly = previousStep instanceof MultiProcessStep ? (MultiProcessStep<?, ?>) previousStep :
          (ProducerOnly) previousFirstProcess;
      ConsumerOnly consumerOnly = (ConsumerOnly) step.getProcesses().get(0);

      if (!consumerOnly.getTypeToConsume().isAssignableFrom(producerOnly.getTypeToProduce())) {
        throw new IllegalArgumentException(String.format(
            "The type returned is in the last Process (%s) is not assignable to type accepted in this Process (%s)",
            producerOnly.getTypeToProduce().getName(), consumerOnly.getTypeToConsume().getName()
        ));
      }
    }
  }

  private static boolean synchronousWithLastProcess(ProducerOnly previousProcess, ConsumerOnly currentProcess) {
    return currentProcess.getTypeToConsume().isAssignableFrom(previousProcess.getTypeToProduce());
  }

  public static void validateProcessAdditionToStep(List<Process> processes, Process process, boolean isMultiProcess) {
    if (!processes.isEmpty() && !isMultiProcess) {
      throw new IllegalArgumentException("Cannot add more than on Process to a Step. Please use a MultiProcessStep");
    }
    if (!processes.isEmpty() && !processes.get(processes.size() - 1).getClass().equals(process.getClass())) {
      throw new IllegalArgumentException(
          "Cannot add a Process of a type different from the previous Process in the same Step");
    }
  }

  public static void validateMultiProcessStep(MultiProcessStep multiProcessStep) {
    if (multiProcessStep.getAccumulator() == null) {
      throw new IllegalStateException("For MultiProcessStep process please add an accumulator");
    }
  }
}
