package org.parallelsuite.test.tasks.create.csv;

import org.parallelsuite.test.tasks.create.csv.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreateCSVConsumer implements Consumer<List<List<String>>> {

  int index = 0;

  @Override
  public void accept(List<List<String>> rows) {
    List<String> headers = rows.get(0);
    try {
      Utils.createCSVAndGenerateLegend(headers, rows.subList(1, rows.size()), ++index);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
