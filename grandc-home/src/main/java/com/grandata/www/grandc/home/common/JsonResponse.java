package com.grandata.www.grandc.home.common;

import net.sf.json.JSONObject;

public class JsonResponse {

  public static JSONObject SuccResponse() {
    return CommonResponse(-1, "COMPLETE", 0, "Operation succeeded.");
  }

  public static JSONObject FailResponse() {
    return FailResponse("Operation failed.");
  }

  public static JSONObject FailResponse(String logDescription) {
    return FailResponse(-1, logDescription);
  }

  public static JSONObject FailResponse(int errorCode, String logDescription) {
    return CommonResponse(-1, "FAILED", errorCode, logDescription);
  }

  public static JSONObject CommonResponse(int id, String state, int errorCode, String logDescription) {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("state", state);
    json.put("errorCode", errorCode);
    json.put("logDescription", logDescription);
    return json;
  }
}
