package org.parallelsuite.support.workflow.executor.queue;

import org.parallelsuite.support.workflow.models.queue.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue<T> {

  private LinkedBlockingQueue<Message<T>> queue = new LinkedBlockingQueue<>();

  public void put(Message<T> message) {
    try {
      queue.put(message);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Message<T> take() {
    try {
      return queue.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Message<T> peek() {
    return queue.peek();
  }

  public int getSize() {
    return queue.size();
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }
}
