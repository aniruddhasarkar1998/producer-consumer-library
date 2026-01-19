package org.parallelsuite.support.workflow.models.process;

import org.parallelsuite.support.workflow.executor.process.ProcessExecutor;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Process {

	private Integer counter = 0;

	private int nThreads;

	private String name;

	public Process(int nThreads, String name) {
		this.nThreads = nThreads;
		this.name = name + (++counter);
	}

	public int getNThreads() {
		return nThreads;
	}

	public String getName() {
		return name;
	}
}
