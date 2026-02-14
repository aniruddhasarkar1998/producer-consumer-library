package org.parallelsuite.test;

import org.parallelsuite.support.workflow.executor.WorkflowExecutor;
import org.parallelsuite.support.workflow.builder.WorkflowBuilder;
import org.parallelsuite.support.workflow.models.process.CallableProcess;
import org.parallelsuite.support.workflow.models.process.ConsumerProcess;
import org.parallelsuite.support.workflow.models.step.MultiProcessStep;
import org.parallelsuite.support.workflow.models.step.Step;
import org.parallelsuite.support.workflow.models.workflow.Workflow;
import org.parallelsuite.test.task.TestUtils;
import org.parallelsuite.test.task.csv.builder.CSVDataModelBuilder;
import org.parallelsuite.test.task.csv.writer.CSVWriter;
import org.parallelsuite.test.task.data.RandomDataGenerator;
import org.parallelsuite.test.task.data.RandomHeaderGenerator;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;

public class CommonTest {

  public static final String RESULTS_DIR = "src/test/resources/results";
  private static final Integer NO_OF_FILES_TO_CREATE = 50;
  private Workflow workflow;

  @BeforeSuite
  public void preprocess() {
    TestUtils.emptyDirectory(RESULTS_DIR);
    TestUtils.createDirectory(RESULTS_DIR);
  }

  @Test
  public void testWorkflowBuilder() {
    WorkflowBuilder workflowBuilder = new WorkflowBuilder();

		int rows = 100;
		int cols = 10000;

		workflow = workflowBuilder.addStep(MultiProcessStep.builder()
				.addProcess(new CallableProcess<>(1, new RandomHeaderGenerator(10)::generate, NO_OF_FILES_TO_CREATE))
				.addProcess(new CallableProcess<>(1, new RandomDataGenerator(rows, cols)::generate, NO_OF_FILES_TO_CREATE))
				.accumulator(new CSVDataModelBuilder()).build()
		).addStep(
				Step.builder().addProcess(new ConsumerProcess<>(10, new CSVWriter()::write)).build()
		).build();

    Assert.assertTrue(workflow != null);
  }

  @Test(dependsOnMethods = "testWorkflowBuilder")
  public void testWorkflowExecutor() {
    WorkflowExecutor workflowExecutor = new WorkflowExecutor(workflow);
    workflowExecutor.initiateThreadsAndExecute();
    workflowExecutor.shutDown(() -> {
      int resultFiles = new File(RESULTS_DIR).list().length;
      return resultFiles == NO_OF_FILES_TO_CREATE;
    }, 500);
    validate();
  }

  private void validate() {
    int resultFiles = new File(RESULTS_DIR).list().length;
    Assert.assertTrue(resultFiles == NO_OF_FILES_TO_CREATE);
  }

  @AfterSuite
  public void postProcess() {
//    TestUtils.emptyDirectory(RESULTS_DIR);
  }

}
