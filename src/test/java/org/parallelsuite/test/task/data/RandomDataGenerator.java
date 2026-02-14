package org.parallelsuite.test.task.data;

public class RandomDataGenerator implements Generator<String[][]> {

	private final int numberOfRows;
	private final int numberOfColumns;

	public RandomDataGenerator(int numberOfRows, int numberOfColumns) {
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
	}

	@Override
	public String[][] generate() {
		String[][] data = new String[numberOfRows][numberOfColumns];
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				data[i][j] = "Data_" + i + "_" + j;
			}
		}
		return data;
	}
}
