package org.parallelsuite.test.task.csv.builder;

import org.parallelsuite.test.CommonTest;
import org.parallelsuite.test.task.csv.model.CSVDataModel;

import java.util.List;
import java.util.function.Function;

public class CSVDataModelBuilder implements Function<List, CSVDataModel> {

	private static int FILE_COUNTER = 0;

	@Override
	public CSVDataModel apply(List list) {
		String[] headers = (String[]) list.get(0);
		String[][] dataframe = (String[][]) list.get(1);
		return new CSVDataModel(CommonTest.RESULTS_DIR + "/file" + (++FILE_COUNTER) + ".csv", headers, dataframe);
	}
}
