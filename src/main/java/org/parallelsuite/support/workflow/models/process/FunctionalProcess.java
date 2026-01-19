package org.parallelsuite.support.workflow.models.process;

import org.parallelsuite.support.reflection.utils.ReflectionUtils;
import org.parallelsuite.support.workflow.models.consumer.ConsumerOnly;
import org.parallelsuite.support.workflow.models.producer.ProducerOnly;
import org.parallelsuite.support.workflow.pair.Pair;

import java.util.function.Function;

public class FunctionalProcess<T, R> extends Process implements ProducerOnly, ConsumerOnly {

  private Function<T, R> function;

  private Class<?> typeToConsume;

  private Class<?> typeToProduce;

  public Function<T, R> getFunction() {
    return function;
  }

  protected FunctionalProcess(int nThreads, Function<T, R> function) {
    super(nThreads, "function-");
    this.function = function;
    Pair<Class<?>, Class<?>> pair = ReflectionUtils.getArgumentAndReturnType(function);
    typeToConsume = pair.getKey();
    typeToProduce = pair.getValue();
  }

  @Override
  public Class<?> getTypeToConsume() {
    return typeToConsume;
  }

  @Override
  public Class<?> getTypeToProduce() {
    return typeToProduce;
  }
}
