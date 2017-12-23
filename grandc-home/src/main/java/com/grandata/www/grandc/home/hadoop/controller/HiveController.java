package com.grandata.www.grandc.home.hadoop.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.grandata.www.grandc.common.configuration.PropertiesConfUtil;

@Controller
@RequestMapping("/hive")
public class HiveController {

  private static Logger logger = Logger.getLogger(HiveController.class);

  private static String driverName = PropertiesConfUtil.getInstance()
      .getProperty("hive.driverName");
  private static String url = PropertiesConfUtil.getInstance().getProperty("hive.url");
  private static String username = PropertiesConfUtil.getInstance().getProperty("hive.user");
  private static String password = PropertiesConfUtil.getInstance().getProperty("hive.password");

  static {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      logger.error("HiveController driverName error", e);
    }
  }

  @RequestMapping(value = "/exec", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject exec(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();
    JSONArray jsonResult = new JSONArray();

    //
    String user = paramMap.get("tenantName");
    String queueName = paramMap.get("queueName");
    String sql = paramMap.get("cmdStr");

    Connection conn = null;
    try {
      conn = getConnection(user);
      jsonResult = executeQuery(conn, sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    json.put("data", jsonResult);

    return json;
  }

  @RequestMapping(value = "/createdb", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject createdb(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");
    String location = paramMap.get("location");
    String dscp = paramMap.get("dscp");

    Connection conn = null;
    try {
      conn = getConnection(user);
      String sql =
          String
              .format("CREATE DATABASE %s COMMENT \"%s\" LOCATION \"%s\"", dbname, dscp, location);
      execute(conn, sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/deletedb", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject deletedb(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");

    Connection conn = null;
    try {
      conn = getConnection(user);
      String sql = String.format("DROP DATABASE %s RESTRICT", dbname);
      execute(conn, sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/createtbl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject createtbl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String location = paramMap.get("location");
    String stored = paramMap.get("stored");
    String field = paramMap.get("field");
    String compress = paramMap.get("compress");
    int type = Integer.parseInt(paramMap.get("type"));
    String dscp = paramMap.get("dscp");
    JSONArray columns = JSONArray.fromObject(paramMap.get("column"));

    Connection conn = null;
    try {
      conn = getConnection(user);

      // column: "columnname":"c1", "columnindex":"1", "partitioned":"2","dscp":"dscp",
      StringBuffer sql = new StringBuffer();
      if (type == 1) {
        sql.append("CREATE EXTERNAL TABLE ").append(dbname).append(".").append(tblname);
      } else {
        sql.append("CREATE TABLE ").append(dbname).append(".").append(tblname);
      }

      // partiton=1
      List<JSONObject> list1 = new ArrayList<JSONObject>();
      // partiton=2
      List<JSONObject> list2 = new ArrayList<JSONObject>();
      // 遍历 columns.组成两个排序的list
      for (int i = 0; i < columns.size(); i++) {
        JSONObject column = columns.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
        if (column.getInt("partitioned") == 1) {
          list1.add(column);
        } else {
          list2.add(column);
        }
      }
      // 排序
      Collections.sort(list1, new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
          int num1 = o1.getInt("columnindex");
          int num2 = o2.getInt("columnindex");
          if (num1 > num2) {
            return 1;
          } else if (num1 < num2) {
            return -1;
          } else {
            return 0;
          }
        }
      });


      sql.append("(");
      for (JSONObject column : list1) {
        sql.append(column.getString("columnname")).append(" ")
            .append(column.getString("columntype")).append(" COMMENT \"")
            .append(column.getString("dscp")).append("\",");
      }
      // 去掉末尾的,
      sql.deleteCharAt(sql.length() - 1).append(")");
      if (StringUtils.isNotBlank(dscp)) {
        sql.append(" COMMENT \"").append(dscp).append("\"");
      }

      // partition
      if (list2.size() > 0) {
        sql.append(" PARTITIONED BY (");
        for (JSONObject column : list2) {
          sql.append(column.getString("columnname")).append(" ")
              .append(column.getString("columntype")).append(" COMMENT \"")
              .append(column.getString("dscp")).append("\",");
        }
      }
      if (sql.toString().endsWith(",")) {
        sql.deleteCharAt(sql.length() - 1);
      }
      sql.append(")");


      // ROW FORMAT row_format
      if (StringUtils.isNotBlank(field)) {
        sql.append(" ROW FORMAT DELIMITED FIELDS TERMINATED BY \"").append(field).append("\"");
      }

      // STORED AS file_format
      if (StringUtils.isNotBlank(stored)) {
        sql.append(" STORED AS ").append(stored);
      }
      // LOCATION hdfs_path
      if (StringUtils.isNotBlank(location)) {
        sql.append(" LOCATION  \"").append(location).append("\"");
      }

      execute(conn, "use " + dbname, sql.toString());

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/altertbl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject altertbl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String newtblname = paramMap.get("newtblname");

    Connection conn = null;
    try {
      conn = getConnection(user);
      String sql = String.format("ALTER TABLE %s RENAME TO %s ", tblname, newtblname);
      execute(conn, "use " + dbname, sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/deletetbl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject deletetbl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");

    Connection conn = null;
    try {
      conn = getConnection(user);
      String sql = String.format("DROP TABLE IF EXISTS %s.%s", dbname, tblname);
      execute(conn, "use " + dbname, sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/addcolumn", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject addcolumn(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String columnname = paramMap.get("columnname");
    String columntype = paramMap.get("columntype");
    String dscp = paramMap.get("dscp");

    Connection conn = null;
    try {
      conn = getConnection(user);
      StringBuffer sql = new StringBuffer();
      sql.append("ALTER TABLE ").append(tblname).append(" ADD COLUMNS(").append(columnname)
          .append(" ").append(columntype).append(" COMMENT \"").append(dscp).append("\")");
      execute(conn, "use " + dbname, sql.toString());

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/renamecolumn", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject renamecolumn(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String columnname = paramMap.get("columnname");
    String columntype = paramMap.get("columntype");
    String dscp = paramMap.get("dscp");
    String newcolumnname = paramMap.get("newcolumnname ");

    Connection conn = null;
    try {
      conn = getConnection(user);
      StringBuffer sql = new StringBuffer();
      sql.append("ALTER TABLE ").append(tblname).append(" CHANGE ").append(columnname).append(" ")
          .append(newcolumnname).append(" ").append(columntype).append(" COMMENT \"").append(dscp)
          .append("\"");

      execute(conn, "use " + dbname, sql.toString());

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/grant", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject grant(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String fullname = paramMap.get("fullname");
    String auth = paramMap.get("auth");
    String tenantName2 = paramMap.get("tenantName2");

    Connection conn = null;
    try {
      conn = getConnection(user);

      // 获取用户权限
      Set<String> set = new TreeSet<String>();
      String sql = String.format("show grant user %s ON TABLE %s", tenantName2, fullname);
      JSONArray jsonA = executeQuery(conn, "set ROLE ADMIN", sql);
      // 遍历 jsonA. database,table,user 取出 grant。然后累加
      for (int i = 0; i < jsonA.size(); i++) {
        JSONObject jsonRight = jsonA.getJSONObject(i);
        if ((jsonRight.getString("database") + "." + jsonRight.getString("table")).equals(fullname)
            && jsonRight.getString("principal_name").equals(tenantName2)
            && jsonRight.getString("principal_type").equals("USER")
            && StringUtils.isBlank(jsonRight.getString("column"))
            && StringUtils.isBlank(jsonRight.getString("partition"))) {
          set.add(jsonRight.getString("privilege"));
        }
      }

      String[] s = auth.split(",");
      List<String> newauth = new ArrayList<String>();
      for (int i = 0; i < s.length; i++) {
        if (!set.contains(s[i])) {
          newauth.add(s[i]);
        }
      }
      // System.out.println(StringUtils.join(newauth.toArray(), ","));
      if (newauth.size() > 0) {
        sql =
            String.format("GRANT %s ON TABLE %s TO USER %s",
                StringUtils.join(newauth.toArray(), ","), fullname, tenantName2);
        execute(conn, sql);
      } else {
        sql = "";
      }
      // System.out.println("sql="+sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/revoke", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject revoke(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String fullname = paramMap.get("fullname");
    String auth = paramMap.get("auth");
    String tenantName2 = paramMap.get("tenantName2");

    Connection conn = null;
    try {
      conn = getConnection(user);

      Set<String> set = new TreeSet<String>();
      String sql = String.format("show grant user %s ON TABLE %s", tenantName2, fullname);
      JSONArray jsonA = executeQuery(conn, "set ROLE ADMIN", sql);
      // 遍历 jsonA. database,table,user 取出 grant。然后累加
      for (int i = 0; i < jsonA.size(); i++) {
        JSONObject jsonRight = jsonA.getJSONObject(i);
        if ((jsonRight.getString("database") + "." + jsonRight.getString("table")).equals(fullname)
            && jsonRight.getString("principal_name").equals(tenantName2)
            && jsonRight.getString("principal_type").equals("USER")
            && StringUtils.isBlank(jsonRight.getString("column"))
            && StringUtils.isBlank(jsonRight.getString("partition"))) {
          set.add(jsonRight.getString("privilege"));
        }
      }

      String[] s = auth.split(",");
      List<String> newauth = new ArrayList<String>();
      for (int i = 0; i < s.length; i++) {
        if (set.contains(s[i])) {
          newauth.add(s[i]);
        }
      }
      // System.out.println(StringUtils.join(newauth.toArray(), ","));

      if (newauth.size() > 0) {
        sql =
            String.format("REVOKE %s ON TABLE %s FROM USER %s",
                StringUtils.join(newauth.toArray(), ","), fullname, tenantName2);
        execute(conn, sql);
      } else {
        sql = "";
      }
      // System.out.println("sql="+sql);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  @RequestMapping(value = "/setacl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject setacl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    JSONObject json = new JSONObject();

    //
    String user = paramMap.get("tenantName");
    String fullname = paramMap.get("fullname");
    String auth = paramMap.get("auth");
    String tenantName2 = paramMap.get("tenantName2");

    Connection conn = null;
    try {
      conn = getConnection(user);

      // 获取用户权限
      Set<String> set = new TreeSet<String>();
      String sql = String.format("show grant user %s ON TABLE %s", tenantName2, fullname);
      JSONArray jsonA = executeQuery(conn, "set ROLE ADMIN", sql);
      // 遍历 jsonA. database,table,user 取出 grant。然后累加
      for (int i = 0; i < jsonA.size(); i++) {
        JSONObject jsonRight = jsonA.getJSONObject(i);
        if ((jsonRight.getString("database") + "." + jsonRight.getString("table")).equals(fullname)
            && jsonRight.getString("principal_name").equals(tenantName2)
            && jsonRight.getString("principal_type").equals("USER")
            && StringUtils.isBlank(jsonRight.getString("column"))
            && StringUtils.isBlank(jsonRight.getString("partition"))) {
          set.add(jsonRight.getString("privilege"));
        }
      }

      String[] s = auth.split(",");
      // revoke
      List<String> revokeauth = new ArrayList<String>();
      // grant
      List<String> grantauth = new ArrayList<String>();

      // 当前权限没有，需要授权。增加
      for (int i = 0; i < s.length; i++) {
        if (!set.contains(s[i])) {
          grantauth.add(s[i]);
        }
      }
      // 当前权限已有，不需要授权。删除
      for (int i = 0; i < set.size(); i++) {
        for (int j = 0; j < s.length; j++) {
          revokeauth.add(s[i]);
        }
      }
      // System.out.println(StringUtils.join(newauth.toArray(), ","));
      if (revokeauth.size() > 0) {
        sql =
            String.format("GRANT %s ON TABLE %s TO USER %s",
                StringUtils.join(revokeauth.toArray(), ","), fullname, tenantName2);
        execute(conn, sql);
      }
      if (grantauth.size() > 0) {
        sql =
            String.format("GRANT %s ON TABLE %s TO USER %s",
                StringUtils.join(grantauth.toArray(), ","), fullname, tenantName2);
        execute(conn, sql);
      }

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (SQLException e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("hive Connection close SQLException", e);
      }
    }

    return json;
  }

  // executeQuery
  public static JSONArray executeQuery(Connection conn, String sql) throws SQLException {
    return executeQuery(conn, "use default", sql);
  }

  public static JSONArray executeQuery(Connection conn, String preSql, String sql)
      throws SQLException {
    //
    JSONArray jsonA = new JSONArray();
    JSONObject json = new JSONObject();

    Statement stmt = conn.createStatement();
    stmt.execute(preSql);
    ResultSet res = stmt.executeQuery(sql);
    ResultSetMetaData resMeta = res.getMetaData();

    String[] metas = new String[resMeta.getColumnCount()];
    // 列
    for (int i = 1; i <= resMeta.getColumnCount(); i++) {
      metas[i - 1] = resMeta.getColumnName(i);
      resMeta.getColumnType(i);
      resMeta.getColumnTypeName(i);
    }

    while (res.next()) {
      json = new JSONObject();
      for (int j = 0; j < metas.length; j++) {
        // System.out.print(metas[j]+"="+res.getString(metas[j])+";");
        json.put(metas[j], res.getString(metas[j]));
      }
      jsonA.add(json);
      // System.out.println();
    }

    res.close();
    stmt.close();

    return jsonA;
  }

  // execute
  public static void execute(Connection conn, String sql) throws SQLException {
    execute(conn, "use default", sql);
  }

  // execute
  public static void execute(Connection conn, String preSql, String sql) throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.execute(preSql);
    stmt.execute(sql);
    stmt.close();
  }

  public static Connection getConnection() throws SQLException {
    return getConnection(username);
  }

  public static Connection getConnection(String user) throws SQLException {
    // replace "hive" here with the name of the user the queries should run as
    return DriverManager.getConnection(url, user, "");
  }

}
