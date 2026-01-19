package org.parallelsuite.test.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class TestUtils {

  public static void emptyDirectory(String directoryPath) {
    File directory = new File(directoryPath);
    if (directory.isDirectory()) {
      Arrays.stream(directory.listFiles()).forEach(file -> emptyDirectory(file.getPath()));
    }
    delete(directory);
  }

  private static void delete(File file) {
    try {
      Files.deleteIfExists(file.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createDirectory(String path) {
    try {
      Files.createDirectory(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
