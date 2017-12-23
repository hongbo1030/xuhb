package com.grandata.www.grandc.home.hadoop.controller;

import java.io.File;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.datanucleus.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.grandata.www.grandc.common.configuration.PropertiesConfUtil;
import com.grandata.www.grandc.common.util.HttpRequestUtils;

@Controller
@RequestMapping("/hdfs")
public class HDFSTestController {

	private static Logger logger = Logger.getLogger(HDFSTestController.class);

	private static String httpfsurl = PropertiesConfUtil.getInstance().getProperty("httpfsurl");
	private static String httpfsurl_datanode = PropertiesConfUtil.getInstance().getProperty("httpfsurl_datanode");

	@RequestMapping(value = "/put", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject put(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
		JSONObject json = new JSONObject();
		//解析参数
		String user = paramMap.get("tenantName");
		String path = paramMap.get("desPath");
		String localFile = paramMap.get("srcPath");
		String op = "CREATE";

		try{
		//判断本地文件必须存在
		if(new File(localFile).exists()) {
		  String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
	      JSONObject jsonResult = HttpRequestUtils.httpPutFile(url, localFile);

	      json.put("id", -1);
	        json.put("state", "COMPLETE");
	        json.put("errorCode", 0);
	        json.put("logDescription", jsonResult);
		} else {
		  json.put("id", -1);
	        json.put("state", "COMPLETE");
	        json.put("errorCode", -1);
	        json.put("logDescription", "Local file is not exists.");
		}
		}catch(Exception e) {
		  json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
		}

		return json;
	}

	@RequestMapping(value = "/putStream", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject putStream(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        //String str = Base64.encodeToString(mBuff,Base64.DEFAULT);
        byte[] bytes = Base64.decodeString(paramMap.get("bytes")).getBytes();
        String path = paramMap.get("desPath");
        String op = "CREATE";

        try{
          String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
          JSONObject jsonResult = HttpRequestUtils.httpPutBytes(url, bytes);

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

		return json;
	}

	@RequestMapping(value = "/copy", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject copy(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		JSONObject json = new JSONObject();
		json.put("id", -1);
		json.put("state", "COMPLETE");
		json.put("errorCode", 0);
		json.put("logDescription", "Operation succeeded.");

		return json;
	}

	@RequestMapping(value = "/mv", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject mv(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("srcPath");
        String destination = paramMap.get("desPath");
        String op = "RENAME";

        try{
          String url = String.format("%s%s?user.name=%s&op=%s&destination=%s", httpfsurl, path, user, op, destination);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
	}

	@RequestMapping(value = "/del", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject del(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("path");
        String force = paramMap.get("force");
        String op = "RENAME";
        boolean recursive = false; //只有空目录才能删除
        if(force.equals("1")) {
          recursive = true;
        }

        try{
          String url = String.format("%s%s?user.name=%s&op=%s&recursive=%s", httpfsurl, path, user, op, recursive);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
	}

	@RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject create(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("fspath");
        String fsname = paramMap.get("fsname");
        String type = paramMap.get("type");//D or F

        if(path.endsWith("/")) {
          path = path+fsname;
        } else {
          path = path+"/"+fsname;
        }

        try{
          JSONObject jsonResult = new JSONObject();
          if(type.equalsIgnoreCase("D")) {
            String op = "MKDIRS";
            String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
            jsonResult = HttpRequestUtils.httpPut(url);
          } else {
            //F
            String op = "CREATE";
            String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
            jsonResult = HttpRequestUtils.httpPut(url);
          }

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
	}

	@RequestMapping(value = "/rename", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject rename(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("fspath");
        String fsname = paramMap.get("fsname");
        String newfsname  = paramMap.get("newfsname ");
        String op = "RENAME";

        String destination= "";
        if(path.endsWith("/")) {
          path = path+fsname;
          destination = path+newfsname;
        } else {
          path = path+"/"+fsname;
          destination = path+"/"+newfsname;
        }

        try{
          String url = String.format("%s%s?user.name=%s&op=%s&destination=%s", httpfsurl, path, user, op, destination);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
	}

	@RequestMapping(value = "/grant", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject grant(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("fullname");
        int authorization = Integer.parseInt(paramMap.get("authorization"));
        String tenantName2 = paramMap.get("tenantName2");
        String op = "SETACL";

        String permission = Num2permission(authorization);

        try{
          //获取用户权限
          String remainPer = ""; //rwx
          // {"AclStatus":{"owner":"xuhb","group":"hdfs","stickyBit":false,"entries":["user:foo:rw-","user:foo3:rw-","group::r--"]}}
          String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);
          net.sf.json.JSONArray userPers = jsonResult.getJSONObject("AclStatus").getJSONArray("entries");
          for(int i=0;i<userPers.size();i++){
            String[] userPer = userPers.getString(i).split(":");
            if(userPer[0].equals("user") && userPer[1].equals("tenantName2")) {
              remainPer = userPer[2];
            }
          }

          //grant per = permission + remainPer
          StringBuffer setPer = new StringBuffer();
          if(permission.substring(0, 1).equals("r") || remainPer.substring(0, 1).equals("r")) {
            setPer.append("r");
          } else {
            setPer.append("-");
          }
          if(permission.substring(1, 2).equals("w") || remainPer.substring(1, 2).equals("w")) {
            setPer.append("w");
          } else {
            setPer.append("-");
          }
          if(permission.substring(2, 3).equals("x") || remainPer.substring(2, 3).equals("x")) {
            setPer.append("x");
          } else {
            setPer.append("-");
          }

          //aclspec必须包含user,group,other. aclspec=user:foo:rw-,user::rwx,group::---,other::---
          String aclspec = "user:"+tenantName2+":"+setPer.toString()+",user::rwx,group::---,other::---";

          url = String.format("%s%s/?user.name=%s&op=%s&aclspec=%s", httpfsurl, path, user, op, aclspec);
          jsonResult = HttpRequestUtils.httpPut(url);

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
	}

	@RequestMapping(value = "/revoke", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject revoke(@RequestBody Map<String, String> paramMap) throws Exception {
		logger.debug(JSONObject.fromObject(paramMap));

		//返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("fullname");
        int authorization = Integer.parseInt(paramMap.get("authorization"));
        String tenantName2 = paramMap.get("tenantName2");
        String op = "SETACL";

        String permission = Num2permission(authorization);

        try{
          //获取用户权限
          String remainPer = ""; //rwx
          // {"AclStatus":{"owner":"xuhb","group":"hdfs","stickyBit":false,"entries":["user:foo:rw-","user:foo3:rw-","group::r--"]}}
          String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);
          net.sf.json.JSONArray userPers = jsonResult.getJSONObject("AclStatus").getJSONArray("entries");
          for(int i=0;i<userPers.size();i++){
            String[] userPer = userPers.getString(i).split(":");
            if(userPer[0].equals("user") && userPer[1].equals("tenantName2")) {
              remainPer = userPer[2];
            }
          }

          //grant per = permission + remainPer
          StringBuffer setPer = new StringBuffer();
          if(permission.substring(0, 1).equals("r") && remainPer.substring(0, 1).equals("r")) {
            setPer.append("r");
          } else {
            setPer.append("-");
          }
          if(permission.substring(1, 2).equals("w") && remainPer.substring(1, 2).equals("w")) {
            setPer.append("w");
          } else {
            setPer.append("-");
          }
          if(permission.substring(2, 3).equals("x") && remainPer.substring(2, 3).equals("x")) {
            setPer.append("x");
          } else {
            setPer.append("-");
          }

          if(setPer.toString().equals("---")) {
            //删除全部权限
            op = "SETACL";
            url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
            jsonResult = HttpRequestUtils.httpPut(url);
          } else {
            //保留部分权限
            op = "REMOVEACL";
            //aclspec必须包含user,group,other. aclspec=user:foo:rw-,user::rwx,group::---,other::---
            String aclspec = "user:"+tenantName2+":"+setPer.toString()+",user::rwx,group::---,other::---";

            url = String.format("%s%s/?user.name=%s&op=%s&aclspec=%s", httpfsurl, path, user, op, aclspec);
            jsonResult = HttpRequestUtils.httpPut(url);
          }

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
	}

	@RequestMapping(value = "/setacl", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject setacl(@RequestBody Map<String, String> paramMap) throws Exception {
        logger.debug(JSONObject.fromObject(paramMap));

        //返回值
        JSONObject json = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("fullname");
        int authorization = Integer.parseInt(paramMap.get("authorization"));
        String tenantName2 = paramMap.get("tenantName2");
        String op = "SETACL";

        String permission = Num2permission(authorization);

        try{
          //aclspec必须包含user,group,other. aclspec=user:foo:rw-,user::rwx,group::---,other::---
          String aclspec = "user:"+tenantName2+":"+permission+",user::rwx,group::---,other::---";

          String url = String.format("%s%s/?user.name=%s&op=%s&aclspec=%s", httpfsurl, path, user, op, aclspec);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
    }

	@RequestMapping(value = "/liststatus", method = {RequestMethod.POST}, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject liststatus(@RequestBody Map<String, String> paramMap) throws Exception {
        logger.debug(JSONObject.fromObject(paramMap));

        //返回值
        JSONObject json = new JSONObject();
        JSONArray fileArray = new JSONArray();
        JSONObject fileJSON = new JSONObject();
        //解析参数
        String user = paramMap.get("tenantName");
        String path = paramMap.get("fullname");
        String op = "LISTSTATUS";
        if(!path.endsWith("/")) {
          path = path+"/";
        }

        try{
          String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
          JSONObject jsonResult = HttpRequestUtils.httpPut(url);

          //遍历处理结果
          JSONArray fileStatus=jsonResult.getJSONArray("FileStatus");
          for(int i=0;i<fileStatus.size();i++) {
            JSONObject fileStatu = fileStatus.getJSONObject(i);

            fileJSON.put("accessTime", fileStatu.getLong("accessTime"));
            fileJSON.put("blockSize", fileStatu.getLong("blockSize"));
            fileJSON.put("group", fileStatu.getString("group"));
            fileJSON.put("length", fileStatu.getLong("length"));
            fileJSON.put("modificationTime", fileStatu.getLong("modificationTime"));
            fileJSON.put("owner", fileStatu.getString("owner"));
            fileJSON.put("pathSuffix", fileStatu.getString("pathSuffix"));
            fileJSON.put("fullname", path+fileStatu.getString("pathSuffix"));
            fileJSON.put("permission", fileStatu.getString("permission"));
            fileJSON.put("type", fileStatu.getString("type").substring(0, 1));
            fileArray.add(fileJSON);
          }

          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", 0);
          json.put("logDescription", jsonResult);
        }catch(Exception e) {
          json.put("id", -1);
          json.put("state", "COMPLETE");
          json.put("errorCode", -1);
          json.put("logDescription", "Exception.");
        }

        return json;
    }

	public static String Num2permission (int i) {
	  StringBuffer permission = new StringBuffer();
	  //r
	  if((i&4)==1) {
	    permission.append("r");
	  } else {
	    permission.append("-");
	  }
	  //w
	  if((i&2)==1) {
	    permission.append("w");
      } else {
        permission.append("-");
      }
	  //x
	  if((i&1)==1) {
	    permission.append("x");
      } else {
        permission.append("-");
      }

	  return permission.toString();
	}

	public static int Permission2num (String permission) {
      int num = 0;

      if(!permission.substring(0, 1).equalsIgnoreCase("-")) {
        num = num+4;
      }
      if(!permission.substring(1, 2).equalsIgnoreCase("-")) {
        num = num+2;
      }
      if(!permission.substring(2, 3).equalsIgnoreCase("-")) {
        num = num+1;
      }

      return num;
    }

}
