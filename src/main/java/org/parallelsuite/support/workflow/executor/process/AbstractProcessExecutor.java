package org.parallelsuite.support.workflow.executor.process;

import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.process.Process;
import org.parallelsuite.support.workflow.models.queue.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public abstract class AbstractProcessExecutor<T> implements ProcessExecutor<T> {

  private static final ConcurrentHashMap<String, List<Future>> futuresMap = new ConcurrentHashMap<>();

  private static final ConcurrentHashMap<String, MessageQueue<?>> queueMap = new ConcurrentHashMap<>();

  public void addFuture(String processName, Future future) {
    futuresMap.computeIfAbsent(processName, k -> new ArrayList<>()).add(future);
  }

  public List<Future> getFutures(String processName) {
    return futuresMap.get(processName);
  }

  public boolean containsFutures(String processName) {
    if (futuresMap.containsKey(processName)) return !futuresMap.get(processName).isEmpty();
    else return false;
  }

  public synchronized void waitForFutures(Process process) {
    while (!containsFutures(process.getName())) {}
  }

  public void putMessage(String processName, Message<?> message) {

  }

  public Message<?> takeMessage(String processName) {
    return null;
  }

}
