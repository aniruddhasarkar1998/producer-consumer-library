package org.parallelsuite.support.workflow.models.step;

import org.parallelsuite.support.reflection.utils.ReflectionUtils;
import org.parallelsuite.support.workflow.models.process.Process;
import org.parallelsuite.support.workflow.models.producer.ProducerOnly;
import org.parallelsuite.support.workflow.validation.WorkflowValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MultiProcessStep<T, R> extends Step implements ProducerOnly {

  private Function<List<T>, R> accumulator;

  private Class<?> typeToProduce;

  public MultiProcessStep(List<? extends Process> processes, Function<List<T>, R> accumulator, Class<?> typeToProduce) {
    super(processes);
    this.accumulator = accumulator;
    this.typeToProduce = typeToProduce;
  }

  public Function<List<T>, R> getAccumulator() {
    return accumulator;
  }

  @Override
  public Class<?> getTypeToProduce() {
    return typeToProduce;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder<T, R> extends Step.Builder {

    private List<Process> processes;

    private Function<List<T>, R> accumulator;

    private Class<?> typeToProduce;

    public Builder() {
      processes = new ArrayList<>();
    }

    public Builder addProcess(Process process) {
      WorkflowValidationUtils.validateProcessAdditionToStep(processes, process, true);
      processes.add(process);
      return this;
    }

    public Builder accumulator(Function<List<T>, R> accumulator) {
      this.accumulator = accumulator;
      this.typeToProduce = ReflectionUtils.getArgumentAndReturnType(accumulator).getValue();
      return this;
    }

    public Step build() {
      MultiProcessStep multiProcessStep = new MultiProcessStep<>(processes, accumulator, typeToProduce);
      WorkflowValidationUtils.validateMultiProcessStep(multiProcessStep);
      return multiProcessStep;
    }

  }

}
