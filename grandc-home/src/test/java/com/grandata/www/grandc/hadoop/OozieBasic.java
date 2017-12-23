package com.grandata.www.grandc.hadoop;

import java.util.Properties;
import org.apache.oozie.client.AuthOozieClient;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;

public class OozieBasic {

  public static void oozieApi() throws OozieClientException, InterruptedException {
    // get a OozieClient for local Oozie
    OozieClient wc = new AuthOozieClient("http://grandata204:11000/oozie");

    // create a workflow job configuration and set the workflow application path
    Properties conf = wc.createConfiguration();
    //oozie.wf.application.path
    conf.setProperty(OozieClient.APP_PATH, "hdfs://zktest:8020/user/xuhb/examples/apps/map-reduce");


    // setting workflow parameters

    conf.setProperty("nameNode", "hdfs://zktest:8020");
    conf.setProperty("jobTracker", "localhost:8021");
    conf.setProperty("queueName", "default");
    conf.setProperty("examplesRoot", "examples");
    conf.setProperty("outputDir", "map-reduce");

    //conf.setProperty("inputDir", "/user/oozie/examples/input-data/text");
    //conf.setProperty("outputDir", "/user/oozie/examples/output-data/map-reduce");

    // submit and start the workflow job
    String jobId = wc.run(conf);
    System.out.println("Workflow job submitted");

    // wait until the workflow job finishes printing the status every 10 seconds
    while (wc.getJobInfo(jobId).getStatus() == WorkflowJob.Status.RUNNING) {
        System.out.println("Workflow job running ...");
        Thread.sleep(10 * 1000);
    }
/*
    --> PREP
    PREP --> RUNNING | KILLED
    RUNNING --> SUSPENDED | SUCCEEDED | KILLED | FAILED
    SUSPENDED --> RUNNING | KILLED
    */

    // print the final status of the workflow job
    System.out.println("Workflow job completed ...");
    System.out.println(wc.getJobInfo(jobId));
/*    //暂停
    wc.suspend(jobId);
    //恢复
    wc.resume(jobId);
    //kill
    wc.kill(jobId);*/
}

  public static void main(String[] args) throws OozieClientException, InterruptedException {
    System.setProperty("user.name", "xuhb");

    oozieApi();


  }
}
