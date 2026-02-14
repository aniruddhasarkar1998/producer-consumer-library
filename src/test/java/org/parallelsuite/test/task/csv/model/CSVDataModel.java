package org.parallelsuite.test.task.csv.model;

import org.parallelsuite.test.task.csv.writer.CSVWriter;

import java.io.File;

public class CSVDataModel implements DataModel {

	private final String filePath;

	private final String[] headers;

	private final String[][] dataFrame;

	public CSVDataModel(String filePath, String[] headers, String[][] dataFrame) {
		this.filePath = filePath;
		this.headers = headers;
		this.dataFrame = dataFrame;
	}

	public String getFilePath() {
		return filePath;
	}

	public String[] getHeaders() {
		return headers;
	}

	public String[][] getDataFrame() {
		return dataFrame;
	}

	private static class Builder {

		private String filePath;

		private String[] headers;

		private String[][] dataFrame;

		public Builder filePath(String filePath) {
			if (!new File(filePath).exists()) {
				throw new IllegalArgumentException("File path does not exist: " + filePath);
			}
			this.filePath = filePath;
			return this;
		}

		public Builder headers(String[] headers) {
			if (headers.length == 0) {
				throw new IllegalArgumentException("Headers cannot be empty");
			}
			this.headers = headers;
			return this;
		}

		public Builder data(String[][] dataFrame) {
			if (dataFrame.length == 0) {
				throw new IllegalArgumentException("Data Frame cannot be empty");
			}
			if (dataFrame[0].length == 0) {
				throw new IllegalArgumentException("Data Frame cannot have empty rows");
			}
			if (headers == null) {
				throw new IllegalStateException("Headers must be set before setting Data Frame");
			}
			if (headers.length != dataFrame[0].length) {
				throw new IllegalArgumentException("Headers length must match Data Frame row length");
			}
			this.dataFrame = dataFrame;
			return this;
		}

		public CSVDataModel build() {
			return new CSVDataModel(filePath, headers, dataFrame);
		}
	}

}
