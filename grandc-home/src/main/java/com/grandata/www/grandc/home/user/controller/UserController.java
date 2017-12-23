package com.grandata.www.grandc.home.user.controller;

import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.grandata.www.grandc.common.exception.CustomerException;
import com.grandata.www.grandc.home.common.JsonResponse;
import com.grandata.www.grandc.home.user.service.IUserService;

/**
 * 返回错误码  10xx
 * @author
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
    private IUserService userservice;

	@RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject create(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//init
		JSONObject json = new JSONObject();

	    String username = paramMap.get("tenantName1");
		String password = paramMap.get("key1");
		try{
			userservice.create(username, password);
			json = JsonResponse.SuccResponse();
		} catch(CustomerException e) {
		  logger.error("UserController create CustomerException:"+ e.getErrormsg());
		  json = JsonResponse.FailResponse(e.getErrorcode(), e.getErrormsg());
		} catch(Exception e) {
		  logger.error("UserController create Exception", e);
		  json = JsonResponse.FailResponse(e.getMessage());
		}

		return json;
	}

	@RequestMapping(value = "/modifypwd", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject modifypwd(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//init
        JSONObject json = new JSONObject();

        String username = paramMap.get("tenantName1");
        String password = paramMap.get("key1");
        try{
            userservice.modifypwd(username, password);
            json = JsonResponse.SuccResponse();
        } catch(CustomerException e) {
          logger.error("UserController modifypwd CustomerException:"+ e.getErrormsg());
          json = JsonResponse.FailResponse(e.getErrorcode(), e.getErrormsg());
        } catch(Exception e) {
          logger.error("UserController modifypwd Exception", e);
          json = JsonResponse.FailResponse(e.getMessage());
        }

        return json;
	}

	@RequestMapping(value = "/modifyselfpwd", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject modifyselfpwd(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//init
        JSONObject json = new JSONObject();

        String username = paramMap.get("tenantName");
        String password = paramMap.get("newkey");
        try{
            userservice.modifypwd(username, password);
            json = JsonResponse.SuccResponse();
        } catch(CustomerException e) {
          logger.error("UserController modifyselfpwd CustomerException:"+ e.getErrormsg());
          json = JsonResponse.FailResponse(e.getErrorcode(), e.getErrormsg());
        } catch(Exception e) {
          logger.error("UserController modifyselfpwd Exception", e);
          json = JsonResponse.FailResponse(e.getMessage());
        }

        return json;
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject delete(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//init
        JSONObject json = new JSONObject();

        String username = paramMap.get("tenantName1");
        try{
            userservice.delete(username);
            json = JsonResponse.SuccResponse();
        } catch(CustomerException e) {
          logger.error("UserController delete CustomerException:"+ e.getErrormsg());
          json = JsonResponse.FailResponse(e.getErrorcode(), e.getErrormsg());
        } catch(Exception e) {
          logger.error("UserController delete Exception", e);
          json = JsonResponse.FailResponse(e.getMessage());
        }

        return json;
	}

}
