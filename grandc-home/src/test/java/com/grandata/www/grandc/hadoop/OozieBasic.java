package com.grandata.www.grandc.hadoop;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Properties;
import java.util.TimeZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.oozie.client.AuthOozieClient;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.dom4j.DocumentHelper;
import com.grandata.www.grandc.common.util.HttpRequestUtils;

public class OozieBasic {



  private static String nameNode = "hdfs://zktest";
  private static String jobTracker = "localhost:8021";
  private static String queueName = "default";
  private static String workspace = "/user/xuhb/oozie-test/workspaces/";

  public static void oozieApi() throws OozieClientException, InterruptedException {
    // get a OozieClient for local Oozie
    OozieClient wc = new AuthOozieClient("http://grandata204:11000/oozie");

    // create a workflow job configuration and set the workflow application path
    Properties conf = wc.createConfiguration();
    //oozie.wf.application.path
    conf.setProperty(OozieClient.APP_PATH, "hdfs://zktest:8020/user/xuhb/examples/apps/hive");


    // setting workflow parameters
    conf.setProperty("nameNode", "hdfs://zktest");
    conf.setProperty("jobTracker", "localhost:8021");
    conf.setProperty("queueName", "default");
    conf.setProperty("examplesRoot", "examples");
    conf.setProperty("oozie.use.system.libpath", "true");

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

  //生成xml
  public static void xmlExample(String file, JSONObject obj) {
    org.dom4j.Document document=DocumentHelper.createDocument();

    org.dom4j.Element root=document.addElement("rss"); //创建根节点
    root.addAttribute("version", "2.0");  //为根结点设置属性
    org.dom4j.Element channel=root.addElement("channel");  //创建子节点
    org.dom4j.Element title=channel.addElement("title");    //创建子节点
    title.setText("新闻国内");  //设置结点文本内容

    org.dom4j.io.XMLWriter writer;
    try {
      writer = new org.dom4j.io.XMLWriter(new FileOutputStream(file), org.dom4j.io.OutputFormat.createPrettyPrint());
      writer.setEscapeText(false);//字符是否转义,默认true
      writer.write(document);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void createWorkFlow(String filePath, JSONObject obj) {
    //
    if(!filePath.endsWith("/")) {
      filePath = filePath+"/";
    }
    String wfXml = filePath+"workflow.xml";
    String wfText = filePath+"job_wf.properties";
    String endname = "";

    org.dom4j.Document document=DocumentHelper.createDocument();

    //解析obj
    org.dom4j.Element root=document.addElement("workflow-app", "uri:oozie:workflow:0.2"); //创建根节点
    root.addAttribute("name", "test1");  //为根结点设置属性
    //root.addAttribute("xmlns", "uri:oozie:workflow:0.5");

    for(int i=0;i<obj.getJSONArray("workflow").size();i++) {
      JSONObject nodeJson = obj.getJSONArray("workflow").getJSONObject(i);

      String nodetype=nodeJson.getString("type");
      //判断fork. ,{"currentnode":"fork-1256","type":"fork","nextnode":["hive-2345","fs-2ef5"]}
      if(nodetype.equals("fork")) {
        org.dom4j.Element fork=root.addElement("fork");  //创建子节点
        fork.addAttribute("name", nodeJson.getString("currentnode"));

        for(int j=0;j<nodeJson.getJSONArray("nextnode").size();j++) {
          org.dom4j.Element path=fork.addElement("path");  //创建子节点
          path.addAttribute("start", nodeJson.getJSONArray("nextnode").getString(j));
        }
      }

      //判断join. ,{"currentnode":"join-2345","type":"join","nextnode":["end"]}
      else if(nodetype.equals("join")) {
        org.dom4j.Element join=root.addElement("join");  //创建子节点
        join.addAttribute("name", nodeJson.getString("currentnode"));
        join.addAttribute("to", nodeJson.getJSONArray("nextnode").getString(0));
      } else if(nodetype.equals("start")) {
        org.dom4j.Element start=root.addElement("start");  //创建子节点
        start.addAttribute("to", nodeJson.getJSONArray("nextnode").getString(0));
      } else if(nodetype.equals("end")) {
        //先放到临时变量中，最后再追加进去。
        endname = nodeJson.getString("currentnode");
      } else {

      org.dom4j.Element action=root.addElement("action");  //创建子节点
      action.addAttribute("name", nodeJson.getString("currentnode"));

      org.dom4j.Element type = null;    //创建子节点
      //增加if判断
      if(nodetype.equals("hive")) {
        type=action.addElement("hive", "uri:oozie:hive-action:0.2");  //为根结点设置属性
      }
      org.dom4j.Element job_tracker=type.addElement("job-tracker");  //创建子节点
      job_tracker.setText("${jobTracker}"); //配置
      org.dom4j.Element name_node=type.addElement("name-node");  //创建子节点
      name_node.setText("${nameNode}"); //配置

      //此处按照 prepare, configuration, script, param 顺序解析。
      for(int j=0;j<nodeJson.getJSONArray("action").size();j++) {
        JSONObject actionJson = nodeJson.getJSONArray("action").getJSONObject(j);
        Iterator it = actionJson.keys();
        String key = it.next().toString();

        //prepare
        if(key.equals("prepare")) {
          org.dom4j.Element prepare=type.addElement("prepare");  //创建子节点
          for(int x=0;x<actionJson.getJSONArray(key).size();x++) {
          Iterator it2 = actionJson.getJSONArray(key).getJSONObject(x).keys();
          while(it2.hasNext()) {
            //mkdir,delete
            String key2 = it2.next().toString();
            String value2 = actionJson.getJSONArray(key).getJSONObject(x).getString(key2);

            org.dom4j.Element dir=prepare.addElement(key2);  //创建子节点
            dir.addAttribute("path", value2);  //为根结点设置属性
          }
          }
        }
      }

      for(int j=0;j<nodeJson.getJSONArray("action").size();j++) {
        JSONObject actionJson = nodeJson.getJSONArray("action").getJSONObject(j);
        Iterator it = actionJson.keys();
        String key = it.next().toString();
        if(key.equals("property")) {
          org.dom4j.Element configuration=type.addElement("configuration");  //创建子节点
          org.dom4j.Element property=configuration.addElement("property");  //创建子节点
          for(int x=0;x<actionJson.getJSONArray(key).size();x++){
            JSONObject propertyJson = actionJson.getJSONArray(key).getJSONObject(x);
            org.dom4j.Element name=property.addElement("name");  //创建子节点
            name.setText(propertyJson.getString("name"));
            org.dom4j.Element value=property.addElement("value");  //创建子节点
            value.setText(propertyJson.getString("value"));
          }
        }
      }

      for(int j=0;j<nodeJson.getJSONArray("action").size();j++) {
        JSONObject actionJson = nodeJson.getJSONArray("action").getJSONObject(j);
        Iterator it = actionJson.keys();
        String key = it.next().toString();
        if(key.equals("script")) {
          org.dom4j.Element script=type.addElement("script");  //创建子节点
          script.setText(actionJson.getString(key)); //配置
        }
      }

      for(int j=0;j<nodeJson.getJSONArray("action").size();j++) {
        JSONObject actionJson = nodeJson.getJSONArray("action").getJSONObject(j);
        Iterator it = actionJson.keys();
        String key = it.next().toString();
        if(key.equals("param")) {
          for(int x=0;x<actionJson.getJSONArray(key).size();x++){
            org.dom4j.Element param=type.addElement("param");  //创建子节点
            param.setText(actionJson.getJSONArray(key).getString(x));
          }
        }
      }

      //ok
      org.dom4j.Element ok=action.addElement("ok");    //创建子节点
      ok.addAttribute("to", nodeJson.getJSONArray("nextnode").getString(0));  //为根结点设置属性

      //固定增加 error
      org.dom4j.Element error=action.addElement("error");    //创建子节点
      error.addAttribute("to", "Kill");  //为根结点设置属性
      }
    }

    //固定增加 Kill 节点
    org.dom4j.Element kill=root.addElement("kill");  //创建子节点
    kill.addAttribute("name", "Kill");  //为根结点设置属性

    org.dom4j.Element end=root.addElement("end");  //创建子节点
    end.addAttribute("name", endname);

    org.dom4j.Element message=kill.addElement("message");  //创建子节点
    message.setText("Action failed, error message[${wf:errorMessage(wf:lastErrorNode())}]");

    //xml
    org.dom4j.io.XMLWriter writer = null;
    try {
      writer = new org.dom4j.io.XMLWriter(new FileOutputStream(wfXml), org.dom4j.io.OutputFormat.createPrettyPrint());
      writer.setEscapeText(false);//字符是否转义,默认true
      writer.write(document);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //properties
    FileWriter fw = null;
    try {
      fw = new FileWriter(wfText);
      fw.write("nameNode="+nameNode+System.lineSeparator());
      fw.write("jobTracker="+jobTracker+System.lineSeparator());
      fw.write("queueName="+obj.getString("queueName")+System.lineSeparator());
      fw.write("oozie.use.system.libpath=true"+System.lineSeparator());
      fw.write("oozie.wf.application.path="+obj.getString("remoteFilePath")+System.lineSeparator());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public static void createCoordinator(String filePath, JSONObject obj) {
    //
      if(!filePath.endsWith("/")) {
        filePath = filePath+"/";
      }
      String coorXml = filePath+"coordinator.xml";
      String coorText = filePath+"job_coor.properties";

    org.dom4j.Document document=DocumentHelper.createDocument();

    //解析obj
    org.dom4j.Element root=document.addElement("coordinator-app", "uri:oozie:coordinator:0.2"); //创建根节点
    root.addAttribute("name", obj.getString("workflowname"));  //为根结点设置属性
    root.addAttribute("frequency", obj.getString("frequency"));
    root.addAttribute("start", "${start_date}");
    root.addAttribute("end", "${end_date}");
    root.addAttribute("timezone", "Asia/Shanghai");

    //controls
    org.dom4j.Element controls=root.addElement("controls");  //创建子节点
    org.dom4j.Element concurrency=controls.addElement("concurrency");
    concurrency.setText("1");
    org.dom4j.Element execution=controls.addElement("execution");
    execution.setText("FIFO");
    org.dom4j.Element timeout=controls.addElement("timeout");
    timeout.setText("30"); //单位分钟。默认值为-1，表示永远不超时，如果为0 则总是超时。

    //action
    org.dom4j.Element action=root.addElement("action");  //创建子节点
    org.dom4j.Element workflow=action.addElement("workflow");
    org.dom4j.Element app_path=workflow.addElement("app-path");
    app_path.setText("${wf_application_path}");
    //

    org.dom4j.Element configuration=workflow.addElement("configuration");
    //nameNode,jobTracker,queueName,oozie.use.system.libpath
    org.dom4j.Element property=configuration.addElement("property");
    org.dom4j.Element name=property.addElement("name");
    name.setText("nameNode");
    org.dom4j.Element value=property.addElement("value");
    value.setText("${nameNode}");

    org.dom4j.Element property2=configuration.addElement("property");
    org.dom4j.Element name2=property2.addElement("name");
    name2.setText("jobTracker");
    org.dom4j.Element value2=property2.addElement("value");
    value2.setText("${jobTracker}");

    org.dom4j.Element property3=configuration.addElement("property");
    org.dom4j.Element name3=property3.addElement("name");
    name3.setText("queueName");
    org.dom4j.Element value3=property3.addElement("value");
    value3.setText("${queueName}"); //增加 queueName在此处解析

    org.dom4j.Element property4=configuration.addElement("property");
    org.dom4j.Element name4=property4.addElement("name");
    name4.setText("oozie.use.system.libpath");
    org.dom4j.Element value4=property4.addElement("value");
    value4.setText("true");

    org.dom4j.io.XMLWriter writer = null;
    try {
      writer = new org.dom4j.io.XMLWriter(new FileOutputStream(coorXml), org.dom4j.io.OutputFormat.createPrettyPrint());
      writer.setEscapeText(false);//字符是否转义,默认true
      writer.write(document);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    //properties
    FileWriter fw = null;
    try {
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //小写的mm表示的是分钟
      SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); //UTC时间
      df1.setTimeZone(TimeZone.getTimeZone("UTC"));
      String start_date=df1.format(sdf.parse(obj.getString("starttime")));
      String end_date=df1.format(sdf.parse(obj.getString("endtime")));

      fw = new FileWriter(coorText);
      fw.write("nameNode="+nameNode+System.lineSeparator());
      fw.write("jobTracker="+jobTracker+System.lineSeparator());
      fw.write("queueName="+obj.getString("queueName")+System.lineSeparator());
      fw.write("oozie.use.system.libpath=true"+System.lineSeparator());
      //增加
      fw.write("wf_application_path="+obj.getString("remoteFilePath")+"/workflow.xml"+System.lineSeparator());
      fw.write("start_date="+start_date+System.lineSeparator());
      fw.write("end_date="+end_date+System.lineSeparator());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } finally {
      try {
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static JSONObject createJson() {
    JSONObject obj = new JSONObject();
    JSONObject wf = createSingleJson();

    obj.put("tenantName", "tenantName2");
    obj.put("key", "");
    obj.put("workflowname", "wf_name1");
    obj.put("starttime", "2017-11-20 21:58:51");
    obj.put("endtime", "2020-11-20 21:58:56");
    obj.put("frequency", "0 * * * *");

    obj.put("workflow", wf.get("workflow"));

    return obj;
  }

  //生成json
  public static JSONObject createMutiJson() {
    JSONObject obj = new JSONObject();

    JSONArray workflowArray = new JSONArray();
    JSONObject workflowJson = new JSONObject();
    JSONArray nextnodeArray = new JSONArray();
    JSONArray actionArray = new JSONArray();
    JSONObject actionJson = new JSONObject();
    JSONArray paramArray = new JSONArray();
    JSONArray propertyArray = new JSONArray();
    JSONObject propertyJson = new JSONObject();
    //
    JSONArray prepareArray = new JSONArray();
    JSONObject prepareJson = new JSONObject();

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    workflowJson.put("currentnode", "start");
    workflowJson.put("type", "start");
    nextnodeArray.add("fork-1256");
    workflowJson.put("nextnode", nextnodeArray);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    workflowJson.put("currentnode", "fork-1256");
    workflowJson.put("type", "fork");
    nextnodeArray.add("hive-2345");
    nextnodeArray.add("hive-2ef5");
    workflowJson.put("nextnode", nextnodeArray);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    actionArray = new JSONArray();
    workflowJson.put("currentnode", "hive-2345");
    workflowJson.put("type", "hive");
    nextnodeArray.add("join-2e45");
    workflowJson.put("nextnode", nextnodeArray);
    actionJson = new JSONObject();
    actionJson.put("script", "script.q");
    actionArray.add(actionJson);
    paramArray = new JSONArray();
    paramArray.add("INPUT=/user/${wf:user()}/examples/input-data/table");
    paramArray.add("OUTPUT=/user/${wf:user()}/examples/output-data/hive11");
    actionJson = new JSONObject();
    actionJson.put("param", paramArray);
    actionArray.add(actionJson);
    propertyArray = new JSONArray();
    propertyJson = new JSONObject();
    propertyJson.put("name", "mapred.job.queue.name");
    propertyJson.put("value", "${queueName}");
    propertyArray.add(propertyJson);

    actionJson = new JSONObject();
    actionJson.put("property", propertyArray);
    actionArray.add(actionJson);



    workflowJson.put("action", actionArray);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    actionArray = new JSONArray();
    workflowJson.put("currentnode", "hive-2ef5");
    workflowJson.put("type", "hive");
    nextnodeArray.add("join-2e45");
    workflowJson.put("nextnode", nextnodeArray);
    actionJson = new JSONObject();
    actionJson.put("script", "script.q");
    actionArray.add(actionJson);
    paramArray = new JSONArray();
    paramArray.add("INPUT=/user/${wf:user()}/examples/input-data/table");
    paramArray.add("OUTPUT=/user/${wf:user()}/examples/output-data/hive22");
    actionJson = new JSONObject();
    actionJson.put("param", paramArray);
    propertyArray = new JSONArray();
    propertyJson = new JSONObject();
    propertyJson.put("name", "mapred.job.queue.name");
    propertyJson.put("value", "${queueName}");
    propertyArray.add(propertyJson);
    actionJson = new JSONObject();
    actionJson.put("property", propertyArray);
    actionArray.add(actionJson);
    workflowJson.put("action", actionArray);
    actionArray.add(actionJson);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    workflowJson.put("currentnode", "join-2e45");
    workflowJson.put("type", "join");
    nextnodeArray.add("end");
    workflowJson.put("nextnode", nextnodeArray);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    workflowJson.put("currentnode", "end");
    workflowJson.put("type", "end");
    workflowJson.put("nextnode", nextnodeArray);
    workflowArray.add(workflowJson);

    obj.put("workflow", workflowArray);

    return obj;
  }

  public static JSONObject createSingleJson() {
    JSONObject obj = new JSONObject();

    JSONArray workflowArray = new JSONArray();
    JSONObject workflowJson = new JSONObject();
    JSONArray nextnodeArray = new JSONArray();
    JSONArray actionArray = new JSONArray();
    JSONObject actionJson = new JSONObject();
    JSONArray paramArray = new JSONArray();
    JSONArray propertyArray = new JSONArray();
    JSONObject propertyJson = new JSONObject();
    //
    JSONArray prepareArray = new JSONArray();
    JSONObject prepareJson = new JSONObject();

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    workflowJson.put("currentnode", "start");
    workflowJson.put("type", "start");
    nextnodeArray.add("hive-2345");
    workflowJson.put("nextnode", nextnodeArray);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    actionArray = new JSONArray();
    workflowJson.put("currentnode", "hive-2345");
    workflowJson.put("type", "hive");
    nextnodeArray.add("end");
    workflowJson.put("nextnode", nextnodeArray);
    actionJson = new JSONObject();
    actionJson.put("script", "script.q");
    actionArray.add(actionJson);
    paramArray = new JSONArray();
    paramArray.add("INPUT=/user/${wf:user()}/examples/input-data/table");
    paramArray.add("OUTPUT=/user/${wf:user()}/examples/output-data/hive11");
    actionJson = new JSONObject();
    actionJson.put("param", paramArray);
    actionArray.add(actionJson);
    propertyArray = new JSONArray();
    propertyJson = new JSONObject();
    propertyJson.put("name", "mapred.job.queue.name");
    propertyJson.put("value", "${queueName}");
    propertyArray.add(propertyJson);
    actionJson = new JSONObject();
    actionJson.put("property", propertyArray);
    actionArray.add(actionJson);

    prepareArray = new JSONArray();
    prepareJson = new JSONObject();
    prepareJson.put("delete", "${nameNode}/user/${wf:user()}/examples/output-data/hive");
    prepareArray.add(prepareJson);
    prepareJson = new JSONObject();
    prepareJson.put("mkdir", "${nameNode}/user/${wf:user()}/examples/output-data");
    prepareArray.add(prepareJson);
    actionJson = new JSONObject();
    actionJson.put("prepare", prepareArray);
    actionArray.add(actionJson);

    workflowJson.put("action", actionArray);
    workflowArray.add(workflowJson);

    obj = new JSONObject();
    workflowJson = new JSONObject();
    nextnodeArray = new JSONArray();
    workflowJson.put("currentnode", "end");
    workflowJson.put("type", "end");
    workflowJson.put("nextnode", nextnodeArray);
    workflowArray.add(workflowJson);

    obj.put("workflow", workflowArray);

    return obj;
  }

  public static void putfile(String user, String op, String path, String localFile) {
    String url = String.format("%s%s?user.name=%s&op=%s", "http://192.168.10.205:50070/webhdfs/v1", path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPutFile(url, localFile);
    System.out.println(jsonResult);
  }

  public static void putfile(String user, String local, String remote) {
    putfile("xuhb", "CREATE", remote+"/job_wf.properties", local+"/job_wf.properties");
    putfile("xuhb", "CREATE", remote+"/workflow.xml", local+"/workflow.xml");
    putfile("xuhb", "CREATE", remote+"/job_coor.properties", local+"/job_coor.properties");
    putfile("xuhb", "CREATE", remote+"/coordinator.xml", local+"/coordinator.xml");
  }


  public static void main(String[] args) throws OozieClientException, InterruptedException {
    System.setProperty("user.name", "hdfs");


    String filePath = "D:\\grandata\\test";
    String remoteFilePath = workspace+"hue-oozie-"+System.currentTimeMillis();
    JSONObject obj = new JSONObject();
    obj = createJson();// 模拟请求数据。暂时不支持配置dataset;暂时不支持job.properties中变量
    obj.put("queueName", queueName); //queue
    obj.put("remoteFilePath", remoteFilePath); //当前时刻用于生成唯一目录

    //step1. 本地生成workflow.xml及对应的job_wf.properties.  json -> xml
    createWorkFlow(filePath, obj);

    //step2. 本地生成 coordinator.xml及对应的job_coor.properties. 时间
    createCoordinator(filePath, obj);

    //step3. 文件上传到hdfs(workflow.xml,job_wf.properties,coordinator.xml,job_coor.properties)
    putfile("xuhb", filePath, remoteFilePath);

    //step4. oozie api调用提交任务
    //oozieApi();


  }
}
