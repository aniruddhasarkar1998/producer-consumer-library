package org.parallelsuite.test.task.data;

public class RandomHeaderGenerator implements Generator<String[]> {

	private final int numberOfHeaders;

	public RandomHeaderGenerator(int numberOfHeaders) {
		this.numberOfHeaders = numberOfHeaders;
	}

	@Override
	public String[] generate() {
		String[] headers = new String[numberOfHeaders];
		for (int i = 0; i < numberOfHeaders; i++) {
			headers[i] = "Header_" + i;
		}
		return headers;
	}

}
