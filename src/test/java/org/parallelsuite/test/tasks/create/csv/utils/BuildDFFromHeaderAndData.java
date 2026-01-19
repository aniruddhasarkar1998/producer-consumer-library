package org.parallelsuite.test.tasks.create.csv.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BuildDFFromHeaderAndData implements Function<List, List> {

  @Override
  public List apply(List list) {
    List<List<String>> result = new ArrayList<>();
    result.add((List<String>) list.get(0));
    if (list.get(1) == null) {
      System.out.println("caught");
    }
    for (List<String> row: (List<List>) list.get(1)) {
      result.add(row);
    }
    return result;
  }

}
