package com.grandata.www.grandc.home.workflow.controller;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/workflow")
public class WorkflowController {

	private static Logger logger = Logger.getLogger(WorkflowController.class);

	@RequestMapping(value = "/save", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject save(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/run", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject run(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject delete(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/runplan", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject runplan(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/kill", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject kill(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/rerun", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject rerun(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject list(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		// page
		JSONArray arrayList1 = new JSONArray();
		JSONObject json11 = new JSONObject();
		json11.put("pagecount", "100");
		json11.put("pageindex", "1");
		json11.put("pagesize", "10");
		arrayList1.add(json11);

		// list
		JSONArray arrayList2 = new JSONArray();
		JSONObject json22 = new JSONObject();
		json22.put("workflow", "hgr_1496828233756");
		json22.put("status", "KILLED");
		json22.put("progress", "100");
		json22.put("lastModifiedTime", "2017-06-08 10:47:27");
		json22.put("startTime", "2017-06-08 10:47:22");
		json22.put("createdTime", "2017-06-08 10:47:22");
		json22.put("endTime", "2017-06-08 10:47:27");
		json22.put("durationTime", "5s");
		arrayList2.add(json22);
		json22 = new JSONObject();
		json22.put("workflow", "hgr_1496828233756");
		json22.put("status", "SUCCEEDED");
		json22.put("progress", "100");
		json22.put("lastModifiedTime", "2017-06-08 10:46:33");
		json22.put("startTime", "2017-06-08 10:46:00");
		json22.put("createdTime", "2017-06-08 10:46:00");
		json22.put("endTime", "2017-06-08 10:46:33");
		json22.put("durationTime", "33s");
		arrayList2.add(json22);

		JSONObject json1 = new JSONObject();
		json1.put("page", arrayList1);
		json1.put("list", arrayList2);

		json.put("data", json1);

		return json;
	}

	@RequestMapping(value = "/action", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject action(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		// list
		JSONArray arrayList2 = new JSONArray();
		JSONObject json22 = new JSONObject();
		json22.put("id", "0000004-170608093516968-oozie-oozi-W@:start:");
		json22.put("name", "start");
		json22.put("type", "START:");
		json22.put("status", "OK");
		json22.put("externalId", "-");
		json22.put("startTime", "2017-06-08 10:47:22");
		json22.put("endTime", "2017-06-08 10:47:27");
		json22.put("errorCode", "");
		json22.put("errorMessage", "");
		json22.put("consoleUrl", "-");
		arrayList2.add(json22);
		json22 = new JSONObject();
		json22.put("id", "0000004-170608093516968-oozie-oozi-W@fork-1256");
		json22.put("name", "fork-1256");
		json22.put("type", "FORK:");
		json22.put("status", "OK");
		json22.put("externalId", "-");
		json22.put("startTime", "2017-06-08 10:47:22");
		json22.put("endTime", "2017-06-08 10:47:22");
		json22.put("errorCode", "");
		json22.put("errorMessage", "");
		json22.put("consoleUrl", "-");
		arrayList2.add(json22);
		json22 = new JSONObject();
		json22.put("id", "0000004-170608093516968-oozie-oozi-W@hive-2345");
		json22.put("name", "hive-2345");
		json22.put("type", "hive");
		json22.put("status", "KILLED");
		json22.put("externalId", "job_1496885424569_0013");
		json22.put("startTime", "2017-06-08 10:47:22");
		json22.put("endTime", "2017-06-08 10:47:22");
		json22.put("errorCode", "");
		json22.put("errorMessage", "");
		json22.put("consoleUrl",
				"http://grandata202:8088/proxy/application_1496885424569_0013/");
		arrayList2.add(json22);

		JSONObject json1 = new JSONObject();
		json1.put("list", arrayList2);

		json.put("data", json1);

		return json;
	}

	@RequestMapping(value = "/log", method = { RequestMethod.POST }, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject log(@RequestBody Map<String, String> paramMap)
			throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		// list
		JSONArray arrayList2 = new JSONArray();
		JSONObject json22 = new JSONObject();
		json22.put(
				"log",
				"2017-06-15 12:11:56,906 INFO org.apache.oozie.command.coord.CoordSubmitXCommand: SERVER[grandata203] USER[hgr] GROUP[-] TOKEN[] APP[hgr_1497499037952] JOB[0000002-170615094315395-oozie-oozi-C] ACTION[-] ENDED Coordinator Submit jobId=0000002-170615094315395-oozie-oozi-C");
		arrayList2.add(json22);
		JSONObject json1 = new JSONObject();
		json1.put("list", arrayList2);

		json.put("data", json1);

		return json;
	}

}
