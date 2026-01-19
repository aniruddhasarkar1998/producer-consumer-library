package org.parallelsuite.support.workflow.executor.step;

import org.parallelsuite.support.workflow.executor.process.ConsumerProcessExecutor;
import org.parallelsuite.support.workflow.executor.process.ProcessExecutor;
import org.parallelsuite.support.workflow.executor.process.ProcessExecutorFactory;
import org.parallelsuite.support.workflow.executor.queue.MessageQueue;
import org.parallelsuite.support.workflow.executor.utils.WorkflowExecutionUtils;
import org.parallelsuite.support.workflow.models.queue.Message;
import org.parallelsuite.support.workflow.models.queue.MessageType;
import org.parallelsuite.support.workflow.models.status.ExecutionStatus;
import org.parallelsuite.support.workflow.models.step.Step;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class StepExecutor<T, R> {

	private Step step;

	private MessageQueue<R> queueTo;

	private MessageQueue<T> queueFrom;

	private ExecutorService executorService;

	private List<ProcessExecutor<T>> processExecutors;

	private volatile boolean receivedEOF = false;

	public StepExecutor(Step step, MessageQueue<T> queueFrom, MessageQueue<R> queueTo) {
		this.step = step;
		this.queueTo = queueTo;
		this.queueFrom = queueFrom;
		processExecutors = Collections.unmodifiableList(step.getProcesses().stream()
				.map(process -> (ProcessExecutor<T>) ProcessExecutorFactory.getProcessExecutor(process, new MessageQueue<>()))
				.collect(Collectors.toList()));
	}

	public void initiate() {
		processExecutors.forEach(ProcessExecutor::initiate);
		executorService = Executors.newFixedThreadPool(2);
	}

	public void execute() {
		executorService.execute(() -> {
			if (queueFrom != null) {
				while (true) {
					if (queueFrom.isEmpty())
						continue;
					Message<T> receivedMessage = queueFrom.take();
					if (receivedMessage.getType().equals(MessageType.EOF)) {
						receivedEOF = true;
						break;
					}
					processExecutors.forEach(processExecutor -> processExecutor.execute(receivedMessage.getData()));
				}
			}
		});

		if (!processExecutors.get(0).getClass().equals(ConsumerProcessExecutor.class)) {
			executorService.execute(() -> {
				while (true) {
					Message<R> messageToProduce = (Message<R>) createMessageToProduce(processExecutors, step);
					queueTo.put(messageToProduce);
					if (messageToProduce.getType().equals(MessageType.EOF)) {
						processExecutors.forEach(ProcessExecutor::shutDown);
						break;
					}
				}
			});
		} else {
			executorService.execute(() -> {
				while (!receivedEOF) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				processExecutors.forEach(ProcessExecutor::shutDown);
			});
		}
	}

	/**
	 * @param processExecutors List of ProcessExecutors
	 * @return consolidated message to produce to a single queue
	 * @apiNote This method is supposed to take from individual queues in which the ProcessExecutors produce message,
	 * use the accumulator to create the message to be put in the single output queue. For steps with a single Process
	 * the implicit accumulator does nothing. For Steps with multiple Processes the accumulator is invoked to create
	 * the single message to be produced in the output queue.
	 */
	protected <U, V> Message<V> createMessageToProduce(List<ProcessExecutor<U>> processExecutors, Step step) {
		Optional<MessageQueue> optional = processExecutors.get(0).getQueue();
		if (optional.isEmpty()) {
			return new Message<>(MessageType.EOF);
		} else {
			MessageQueue<V> queue = optional.get();
			while (queue.isEmpty()) {
			}
			Message<V> message = queue.take();
			return message;
		}
	}

	public void gracefulShutDown() {
		processExecutors.forEach(ProcessExecutor::shutDown);
		executorService.shutdown();
	}

}
