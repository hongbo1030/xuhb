package com.grandata.www.grandc.home.hadoop.controller;

import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hbasetest")
public class HBaseTestController {

	private static Logger logger = Logger.getLogger(HBaseTestController.class);

	@RequestMapping(value = "/createdb", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject createdb(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/deletedb", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject deletedb(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/createtbl", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject createtbl(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/deletetbl", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject deletetbl(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/addcolumn", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject addcolumn(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/altercolumn", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject altercolumn(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/deletecolumn", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject deletecolumn(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/grant", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject grant(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/revoke", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject revoke(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

}
