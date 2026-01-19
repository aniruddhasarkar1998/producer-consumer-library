package org.parallelsuite.support.workflow.executor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class WorkflowExecutionUtils {

  public static void gracefulShutdown(ExecutorService executorService, List<Future>... futureGroups) {
    for (List<Future> futures : futureGroups)
      completeAllFutures(futures);
    executorService.shutdown();
  }

  public static void gracefulShutdown(ExecutorService executorService, Future... futures) {
    for (Future future: futures) {
      if (future != null) completeFuture(future);
    }
    executorService.shutdown();
  }

  public static List<?> completeAllFutures(List<Future> futures) {
    List results = new ArrayList<>();
    for (Future<?> future : futures) {
      results.add(completeFuture(future));
    }
    return results;
  }

  private static Object completeFuture(Future<?> future) {
    try {
      return future.get();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

}
