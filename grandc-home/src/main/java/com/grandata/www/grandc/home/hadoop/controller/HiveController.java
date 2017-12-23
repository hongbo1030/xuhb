package com.grandata.www.grandc.home.hadoop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hive")
public class HiveController {

	private static Logger logger = Logger.getLogger(HiveController.class);

	@RequestMapping(value = "/list", method = {RequestMethod.GET}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject list(HttpServletRequest request) throws Exception {
		logger.debug(request.getQueryString());
		String name = request.getParameter("name");
		logger.debug(name);

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/exec", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject exec(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		//colList
		JSONArray arrayList1 = new JSONArray();
		JSONObject json11 = new JSONObject();
		json11.put("colName", "id");
		json11.put("colType", "INT");
		arrayList1.add(json11);

		JSONObject json12 = new JSONObject();
		json12.put("colName", "name");
		json12.put("colType", "String");
		arrayList1.add(json12);

		JSONObject json13 = new JSONObject();
		json13.put("colName", "date");
		json13.put("colType", "String");
		arrayList1.add(json13);

		//valList
		JSONArray arrayList2 = new JSONArray();
		JSONObject json22 = new JSONObject();
		json22.put("id", "1001");
		json22.put("name", "Tom");
		json22.put("data", "2015-04-25");
		arrayList2.add(json22);
		json22 = new JSONObject();
		json22.put("id", "1002");
		json22.put("name", "mary");
		json22.put("data", "2015-04-25");
		arrayList2.add(json22);
		json22 = new JSONObject();
		json22.put("id", "1003");
		json22.put("name", "jack");
		json22.put("data", "2015-04-25");
		arrayList2.add(json22);

		JSONObject json1 = new JSONObject();
		json1.put("colList", arrayList1);
		json1.put("valList", arrayList2);

		json.put("data", json1);

		return json;
	}

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

	@RequestMapping(value = "/altertbl", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject altertbl(@RequestBody Map<String, String> paramMap) throws Exception {
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

	@RequestMapping(value = "/renamecolumn", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject renamecolumn(@RequestBody Map<String, String> paramMap) throws Exception {
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
