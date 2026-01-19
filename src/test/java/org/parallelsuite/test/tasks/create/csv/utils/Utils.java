package org.parallelsuite.test.tasks.create.csv.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Utils {

  public static void createCSVAndGenerateLegend(List<String> headers, List<List<String>> data, Integer index)
      throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String> legend = new HashMap<>();

    headers = headers.stream().map(header -> {
      String pcName = createColumnObjectName(header);
      if (!header.equals(pcName))
        legend.put(pcName, header);
      return pcName;
    }).collect(Collectors.toList());

    String csvFileName = String.format("src/test/resources/results/csvFile_%s.csv", index);
    // String csvFileName = "result.csv";

    stringBuilder.append(toString(headers, "\"")).append("\n");
    data.forEach(row -> stringBuilder.append(toString(row, "")).append("\n"));
    String csvString = stringBuilder.toString();
    File csvFile = new File(csvFileName);

    try (PrintWriter pw = new PrintWriter(csvFile)) {
      pw.print(csvString);
    } catch (Exception e) {
      throw new IOException("Error writing csv File. " + e.getMessage());
    }

    String legendString = legend.entrySet().stream().map(entry -> entry.getKey() + "->" + entry.getValue())
        .collect(Collectors.joining("\n"));
    File legendFile = new File(String.format("src/test/resources/legends/legend_%s.txt", index));

    if (legendFile.exists())
      return;
    try (PrintWriter pw = new PrintWriter(legendFile)) {
      pw.print(legendString);
    } catch (Exception e) {
      throw new IOException("Error writing legend File. " + e.getMessage());
    }
  }

  private static String toString(List<?> stringArray, String encloser) {
    return stringArray.stream().map(a -> String.format("%s%s%s", encloser, a.toString(), encloser))
        .collect(Collectors.joining(","));
  }

  public static List<List<String>> generateRandomData() {
    List<List<String>> data = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      List<String> row = new ArrayList<>();
      for (int j = 0; j < 1000; j++) {
        row.add(getRandomString(255));
      }
      data.add(row);
    }
    return data;
  }

  private static String getRandomString(int length) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      builder.append("a");
    }
    return "\"" + builder.toString() + "\"";
  }

  public static List<String> generateRandomHeaders() {
    List<String> headers = new ArrayList<>();
    List<Character> specialCharacters = List.of('@', '/', '+', '-', '=', ':', '~', '`', '!', '%', '^', '&', '*', '(',
        ')', '[', ']', '{', '}', '\'', ';', '?', ',', '<', '\\', '|', ' ', '.', '$', '#', '"');
    AtomicInteger columnCounter = new AtomicInteger(0);
    specialCharacters.forEach(character1 -> {
      specialCharacters.forEach(character2 -> {
        headers.add(new StringBuilder().append(character1).append("column").append(columnCounter.getAndIncrement())
            .append(character2).toString());
      });
    });
    for (int i = columnCounter.get(); i < 1000; i++) {
      headers.add("column" + columnCounter.getAndIncrement());
    }
    return headers;
  }

  private static String createColumnObjectName(String name) {
    StringBuilder buffer = new StringBuilder();
    int n = name.length();
    for (int i = 0; i < n; ++i) {
      char ch = name.charAt(i);
      switch (ch) {
        case '@':
        case '/':
        case '+':
        case '-':
        case '=':
        case ':':
        case '~':
        case '`':
        case '!':
        case '%':
        case '^':
        case '&':
        case '*':
        case '(':
        case ')':
        case '[':
        case ']':
        case '{':
        case '}':
        case '\'':
        case ';':
        case '?':
        case ',':
        case '<':
        case '>':
        case '\\':
        case '|':
        case ' ':
        case '.':
        case '$':
        case '#':
        case '"':
          buffer.append('_');
          break;
        default:
          if (i == 0) {
            if (ch == '@') { // If starts with '@' then replace with an underscore
              buffer.append('_');
            } else if ((ch >= '0') && (ch <= '9')) { // If starts with numeric then append an underscore.
              buffer.append('_');
              buffer.append(ch);
            } else {
              buffer.append(ch);
            }
          } else if (i == (n - 1)) {
            if (ch == '$' || ch == '#') { // If ends with '$' or '#' then replace with an underscore
              buffer.append('_');
            } else {
              buffer.append(ch);
            }
          } else {
            buffer.append(ch);
          }
      }
    }
    return buffer.toString();
  }
}
