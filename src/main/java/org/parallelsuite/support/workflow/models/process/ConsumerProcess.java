package org.parallelsuite.support.workflow.models.process;

import org.parallelsuite.support.reflection.utils.ReflectionUtils;
import org.parallelsuite.support.workflow.models.consumer.ConsumerOnly;

import java.util.function.Consumer;

public class ConsumerProcess<T> extends Process implements ConsumerOnly {

  private Consumer<T> consumer;

  private Class<?> typeToConsume;

  public ConsumerProcess(int nThreads, Consumer<T> consumer) {
    super(nThreads, "consumer-");
    this.consumer = consumer;
    typeToConsume = ReflectionUtils.getArgumentType(consumer);
  }

  public Consumer<T> getConsumer() {
    return consumer;
  }

  @Override
  public Class<?> getTypeToConsume() {
    return typeToConsume;
  }
}
