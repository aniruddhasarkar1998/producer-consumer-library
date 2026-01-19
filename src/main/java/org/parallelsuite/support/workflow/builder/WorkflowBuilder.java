package org.parallelsuite.support.workflow.builder;

import org.parallelsuite.support.workflow.models.process.Process;
import org.parallelsuite.support.workflow.models.producer.ProducerOnly;
import org.parallelsuite.support.workflow.models.step.Step;
import org.parallelsuite.support.workflow.models.workflow.Workflow;
import org.parallelsuite.support.workflow.validation.WorkflowValidationUtils;

public class WorkflowBuilder {

  Workflow workflow = new Workflow();

  public WorkflowBuilder() {

  }

  public WorkflowBuilder addStep(Step step) {
    WorkflowValidationUtils.validateStepAdditionToWorkflow(workflow, step);
    workflow.addStep(step);
    return this;
  }

  public Workflow build() {
    Class<?> yieldType = null;
    Process lastProcess = workflow.getSteps().get(workflow.getSteps().size() - 1).getProcesses().get(0);
    if (lastProcess instanceof ProducerOnly) yieldType = ((ProducerOnly) lastProcess).getTypeToProduce();
    workflow.setYieldType(yieldType);
    return workflow;
  }
  
}
