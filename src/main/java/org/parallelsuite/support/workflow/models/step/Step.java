package org.parallelsuite.support.workflow.models.step;

import org.parallelsuite.support.workflow.executor.step.StepExecutor;
import org.parallelsuite.support.workflow.models.process.Process;
import org.parallelsuite.support.workflow.validation.WorkflowValidationUtils;

import java.util.ArrayList;
import java.util.List;

public class Step {

  private List<? extends Process> processes;

  public Step(List<? extends Process> processes) {
    this.processes = processes;
  }

  public List<? extends Process> getProcesses() {
    return processes;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private List<Process> processes;

    public Builder() {
      processes = new ArrayList<>();
    }

    public Builder addProcess(Process process) {
      WorkflowValidationUtils.validateProcessAdditionToStep(processes, process, false);
      processes.add(process);
      return this;
    }

    public Step build() {
      return new Step(processes);
    }

  }

}
