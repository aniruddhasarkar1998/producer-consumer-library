package org.parallelsuite.support.workflow.models.queue;

public class Message<T> {

  private T data;

  private MessageType type;

  public Message(MessageType type) {
    if (type != MessageType.EOF)
      throw new IllegalArgumentException("Use correct constructor for non-EOF messages");
    this.type = type;
  }

  public Message(T data) {
    this.data = data;
    this.type = MessageType.DATA;
  }

  public Message() {
    type = MessageType.EMPTY;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }
}
