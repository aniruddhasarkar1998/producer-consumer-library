package org.parallelsuite.test.tasks.create.csv.utils;

import org.parallelsuite.test.tasks.create.csv.utils.Utils;

import java.util.List;
import java.util.concurrent.Callable;

public class GetRandomHeadersCallable implements Callable<List<String>> {
  @Override
  public List<String> call() throws Exception {
    return Utils.generateRandomHeaders();
  }
}
