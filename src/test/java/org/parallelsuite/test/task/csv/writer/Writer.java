package org.parallelsuite.test.task.csv.writer;

import org.parallelsuite.test.task.csv.model.DataModel;

public interface Writer<T extends DataModel> {

	void write(T dataModel);

}
