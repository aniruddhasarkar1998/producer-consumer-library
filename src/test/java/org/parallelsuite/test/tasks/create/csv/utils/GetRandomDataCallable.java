package org.parallelsuite.test.tasks.create.csv.utils;

import java.util.List;
import java.util.concurrent.Callable;

public class GetRandomDataCallable implements Callable<List<List<String>>> {
  @Override
  public List<List<String>> call() throws Exception {
    return Utils.generateRandomData();
  }
}
