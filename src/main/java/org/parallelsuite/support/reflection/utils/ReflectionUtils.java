package org.parallelsuite.support.reflection.utils;

import org.parallelsuite.support.workflow.pair.Pair;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReflectionUtils {

  public static Class<?> getArgumentType(Consumer<?> consumer) {
    Method[] acceptMethods = consumer.getClass().getDeclaredMethods();
    for (Method method: acceptMethods) {
      method.setAccessible(true);
      Class<?> parameterType = method.getParameterTypes()[0];
      if (!parameterType.equals(Object.class)) {
        return parameterType;
      }
    }
    return Object.class;
  }

  public static Class<?> getReturnType(Callable<?> callable) {
    Method[] callMethods = callable.getClass().getDeclaredMethods();
    for (Method method: callMethods) {
      method.setAccessible(true);
      Class<?> returnType = method.getReturnType();
      if (!returnType.equals(Object.class)) {
        return returnType;
      }
    }
    return Object.class;
  }

  public static Pair<Class<?>, Class<?>> getArgumentAndReturnType(Function<?, ?> function) {
    Method[] methods = function.getClass().getDeclaredMethods();
    Class<?> returnType = Object.class, argumentType = Object.class;
    for (Method method: methods) {
      method.setAccessible(true);
      if (!method.getName().equals("apply")) continue;
      Class<?> rType = method.getReturnType();
      Class<?> aType = method.getParameterTypes()[0];
      if (!rType.equals(Object.class)) {
        returnType = rType; argumentType = aType;
      }
    }
    return Pair.of(argumentType, returnType);
  }

}
