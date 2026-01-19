package org.parallelsuite.support.workflow.models.process;

import org.parallelsuite.support.reflection.utils.ReflectionUtils;
import org.parallelsuite.support.workflow.models.producer.ProducerOnly;

import java.util.concurrent.Callable;

public class CallableProcess<R> extends Process implements ProducerOnly {

  private Callable<R> callable;

  private Class<?> typeToProduce;

  private int iterations;

  public CallableProcess(int nThreads, Callable<R> callable, int iterations) {
    super(nThreads, "callable-");
    this.callable = callable;
    this.iterations = iterations;
    typeToProduce = ReflectionUtils.getReturnType(callable);
  }

  public Callable<R> getCallable() {
    return callable;
  }

  public int getIterations() {
    return iterations;
  }

  @Override
  public Class<?> getTypeToProduce() {
    return typeToProduce;
  }

}
