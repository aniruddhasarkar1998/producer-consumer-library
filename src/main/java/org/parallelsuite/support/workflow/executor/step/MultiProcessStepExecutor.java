package org.parallelsuite.support.workflow.executor.step;

import org.parallelsuite.support.workflow.executor.process.ProcessExecutor;
import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.models.queue.Message;
import org.parallelsuite.support.workflow.models.queue.MessageType;
import org.parallelsuite.support.workflow.models.step.MultiProcessStep;
import org.parallelsuite.support.workflow.models.step.Step;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MultiProcessStepExecutor<T, R> extends StepExecutor<T, R> {

  MultiProcessStep<T, R> multiProcessStep;

  public MultiProcessStepExecutor(MultiProcessStep<T, R> step, MessageQueue<T> queueFrom, MessageQueue<R> queueTo) {
    super(step, queueFrom, queueTo);
    multiProcessStep = step;
  }

  /**
   * @apiNote This method is supposed to take from individual queues in which the ProcessExecutors produce message,
   * use the accumulator to create the message to be put in the single output queue. For steps with a single Process
   * the implicit accumulator does nothing. For Steps with multiple Processes the accumulator is invoked to create
   * the single message to be produced in the output queue.
   */
  @Override
  public <U, V> Message<V> createMessageToProduce(List<ProcessExecutor<U>> processExecutors, Step step) {
    List<MessageQueue<U>> queues = processExecutors.stream()
        .map(processExecutor -> (MessageQueue<U>) processExecutor.getQueue().orElse(null)).filter(a -> a != null)
        .collect(Collectors.toList());

    while (queues.stream().anyMatch(queue -> queue.isEmpty())) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    List<Message<U>> messages = queues.stream().map(MessageQueue::take).collect(Collectors.toList());
    if (messages.get(0).getType().equals(MessageType.EOF)) {
      return new Message<>(MessageType.EOF);
    } else {
      V result = ((MultiProcessStep<U, V>) step).getAccumulator()
          .apply(messages.stream().map(m -> m.getData()).collect(Collectors.toList()));
      return new Message<>(result);
    }
  }

}
