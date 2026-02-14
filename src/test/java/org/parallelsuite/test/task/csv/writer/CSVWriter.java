package org.parallelsuite.test.task.csv.writer;

import org.parallelsuite.test.task.csv.model.CSVDataModel;

import java.io.File;

public class CSVWriter implements Writer<CSVDataModel> {

	@Override
	public void write(CSVDataModel dataModel) {
		try {
			java.io.FileWriter csvWriter = new java.io.FileWriter(dataModel.getFilePath());
			csvWriter.append(String.join(",", dataModel.getHeaders()));
			csvWriter.append("\n");
			for (String[] row : dataModel.getDataFrame()) {
				csvWriter.append(String.join(",", row));
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (Exception e) {
			throw new RuntimeException("Error writing CSV file: " + e.getMessage());
		}
	}

}
