package org.parallelsuite.support.workflow.models.workflow;

import org.parallelsuite.support.workflow.models.step.Step;

import java.util.ArrayList;
import java.util.List;

public class Workflow {

  private List<Step> steps = new ArrayList<>();
  private Class<?> yieldType;

  public List<Step> getSteps() {
    return steps;
  }

  public void addStep(Step step) {
    steps.add(step);
  }

  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  public Class<?> getYieldType() {
    return yieldType;
  }

  public void setYieldType(Class<?> yieldType) {
    this.yieldType = yieldType;
  }
}
