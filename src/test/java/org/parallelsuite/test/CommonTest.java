package org.parallelsuite.test;

import org.parallelsuite.support.workflow.executor.WorkflowExecutor;
import org.parallelsuite.support.workflow.models.process.ConsumerProcess;
import org.parallelsuite.support.workflow.models.process.CallableProcess;
import org.parallelsuite.support.workflow.builder.WorkflowBuilder;
import org.parallelsuite.support.workflow.models.step.MultiProcessStep;
import org.parallelsuite.support.workflow.models.step.Step;
import org.parallelsuite.support.workflow.models.workflow.Workflow;
import org.parallelsuite.test.tasks.TestUtils;
import org.parallelsuite.test.tasks.create.csv.CreateCSVConsumer;
import org.parallelsuite.test.tasks.create.csv.utils.GetRandomHeadersCallable;
import org.parallelsuite.test.tasks.create.csv.utils.BuildDFFromHeaderAndData;
import org.parallelsuite.test.tasks.create.csv.utils.GetRandomDataCallable;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;

public class CommonTest {

  private static final String RESULTS_DIR = "src/test/resources/results";
  private static final String LEGENDS_DIR = "src/test/resources/legends";
  private static final Integer NO_OF_FILES_TO_CREATE = 20;
  private Workflow workflow;

  @BeforeSuite
  public void preprocess() {
    TestUtils.emptyDirectory(RESULTS_DIR);
    TestUtils.emptyDirectory(LEGENDS_DIR);
    TestUtils.createDirectory(RESULTS_DIR);
    TestUtils.createDirectory(LEGENDS_DIR);
  }

  @Test
  public void testWorkflowBuilder() {
    WorkflowBuilder workflowBuilder = new WorkflowBuilder();
    workflow = workflowBuilder.addStep(MultiProcessStep.builder()
        .addProcess(new CallableProcess(1, new GetRandomHeadersCallable(), NO_OF_FILES_TO_CREATE))
        .addProcess(new CallableProcess(1, new GetRandomDataCallable(), NO_OF_FILES_TO_CREATE))
        .accumulator(new BuildDFFromHeaderAndData()).build()
    ).addStep(Step.builder()
        .addProcess(new ConsumerProcess<>(1, new CreateCSVConsumer())).build()
    ).build();
    Assert.assertTrue(workflow != null);
  }

  @Test(dependsOnMethods = "testWorkflowBuilder")
  public void testWorkflowExecutor() {
    WorkflowExecutor workflowExecutor = new WorkflowExecutor(workflow);
    workflowExecutor.initiateThreadsAndExecute();
    workflowExecutor.shutDown(() -> {
      int resultFiles = new File(RESULTS_DIR).list().length;
      int legendFiles = new File(LEGENDS_DIR).list().length;
      System.out.println(resultFiles + " " + legendFiles);
      return resultFiles == legendFiles && resultFiles == NO_OF_FILES_TO_CREATE;
    }, 500);
    validate();
  }

  private void validate() {
    int resultFiles = new File(RESULTS_DIR).list().length;
    int legendFiles = new File(LEGENDS_DIR).list().length;
    System.out.println(resultFiles + " " + legendFiles);
    Assert.assertTrue(resultFiles == NO_OF_FILES_TO_CREATE);
    Assert.assertTrue(legendFiles == NO_OF_FILES_TO_CREATE);
  }

  @AfterSuite
  public void postProcess() {
//    TestUtils.emptyDirectory(RESULTS_DIR);
//    TestUtils.emptyDirectory(LEGENDS_DIR);
  }

}
