package com.grandata.www.grandc.home.hadoop.controller;

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
@RequestMapping("/hdfs")
public class HDFSTestController {

  private static Logger logger = Logger.getLogger(HDFSTestController.class);

  @RequestMapping(value = "/put", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject put(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/putStream", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject putStream(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/copy", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject copy(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/mv", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject mv(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/del", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject del(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/create", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject create(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/rename", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject rename(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/grant", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject grant(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/revoke", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject revoke(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/setacl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject setacl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

  @RequestMapping(value = "/liststatus", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject liststatus(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    String path = paramMap.get("fullname");
    String op = "LISTSTATUS";
    if(!path.endsWith("/")) {
      path = path+"/";
    }

    JSONObject data = new JSONObject();
    JSONArray fileStatus = new JSONArray();
    JSONObject file = new JSONObject();

    //
    file.put("accessTime", 1320171722771L);
    file.put("blockSize", 33554432L);
    file.put("group", "supergroup");
    file.put("length", 24930L);
    file.put("modificationTime", 1320171722771L);
    file.put("owner", "webuser");
    file.put("pathSuffix", "a.patch");
    file.put("fullname", path+"a.patch");
    file.put("permission", "644");
    file.put("type", "F");
    fileStatus.add(file);

    file.put("accessTime", 0L);
    file.put("blockSize", 0L);
    file.put("group", "supergroup");
    file.put("length", 0L);
    file.put("modificationTime", 1320895981256L);
    file.put("owner", "szetszwo");
    file.put("pathSuffix", "bar");
    file.put("fullname", path+"bar");
    file.put("permission", "711");
    file.put("type", "D");
    fileStatus.add(file);

    data.put("FileStatus", fileStatus);
    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    json.put("data", data);
    return json;
  }

  @RequestMapping(value = "/setpermission", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject setpermission(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("state", "COMPLETE");
    json.put("errorCode", 0);
    json.put("logDescription", "Operation succeeded.");
    return json;
  }

}
