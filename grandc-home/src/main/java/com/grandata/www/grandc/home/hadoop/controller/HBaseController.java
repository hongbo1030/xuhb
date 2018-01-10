package com.grandata.www.grandc.home.hadoop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.security.access.AccessControlClient;
import org.apache.hadoop.hbase.security.access.Permission;
import org.apache.hadoop.hbase.security.access.UserPermission;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.grandata.www.grandc.common.configuration.PropertiesConfUtil;

@Controller
@RequestMapping("/hbase2")
public class HBaseController {

  private static Logger logger = Logger.getLogger(HBaseController.class);

  static Configuration conf = null;
  static {
    conf = HBaseConfiguration.create();
    conf.set("hbase.zookeeper.property.clientPort",
        PropertiesConfUtil.getInstance().getProperty("hbase.zookeeper.property.clientPort"));
    conf.set("hbase.zookeeper.quorum",
        PropertiesConfUtil.getInstance().getProperty("hbase.zookeeper.quorum"));
    conf.set("zookeeper.znode.parent",
        PropertiesConfUtil.getInstance().getProperty("zookeeper.znode.parent"));
    System.setProperty("HADOOP_USER_NAME",
        PropertiesConfUtil.getInstance().getProperty("hbase.superuser"));
  }

  @RequestMapping(value = "/createdb", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject createdb(@RequestBody Map<String, String> paramMap) {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");
    String dscp = paramMap.get("dscp");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      NamespaceDescriptor descriptor = NamespaceDescriptor.create(dbname).build();
      admin.createNamespace(descriptor);

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/deletedb", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject deletedb(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      admin.deleteNamespace(dbname);

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/createtbl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject createtbl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String dscp = paramMap.get("dscp");
    JSONArray columns = JSONArray.fromObject(paramMap.get("column"));

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      TableName tableNameObj = TableName.valueOf(dbname + ":" + tblname);
      HTableDescriptor desc = new HTableDescriptor(tableNameObj);

      List<JSONObject> list = new ArrayList<JSONObject>();
      for (int i = 0; i < columns.size(); i++) {
        list.add(columns.getJSONObject(i));
      }
      // 排序
      Collections.sort(list, new Comparator<JSONObject>() {
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

      for (JSONObject family : list) {
        HColumnDescriptor c = new HColumnDescriptor(family.getString("columnname"));
        c.setTimeToLive(family.getInt("ttl")); // ttl,单位ms
        c.setMaxVersions(family.getInt("version"));// 指定数据最大保存的版本个数。默认为3
        c.setCompressionType(Compression.Algorithm.valueOf(StringUtils.upperCase((family
            .getString("compress"))))); // 压缩

        desc.addFamily(c);
      }

      if (admin.tableExists(tableNameObj)) {
        System.out.println("table Exists!");
        System.exit(0);
      } else {
        admin.createTable(desc);
        System.out.println("create table Success!");
      }

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/deletetbl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject deletetbl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      TableName tableNameObj = TableName.valueOf(dbname + ":" + tblname);
      admin.disableTable(tableNameObj);
      admin.deleteTable(tableNameObj);

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/addcolumn", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject addcolumn(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String columnname = paramMap.get("columnname");
    String compress = paramMap.get("compress");
    int ttl = Integer.parseInt(paramMap.get("ttl"));
    int version = Integer.parseInt(paramMap.get("version"));
    String dscp = paramMap.get("dscp");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      TableName tableName = TableName.valueOf(dbname + ":" + tblname);
      // HTableDescriptor table = admin.getTableDescriptor(tableName);
      HColumnDescriptor newfamily = new HColumnDescriptor(columnname);
      newfamily.setTimeToLive(ttl); // ttl,单位ms
      newfamily.setMaxVersions(version);// 指定数据最大保存的版本个数。默认为3
      newfamily.setCompressionType(Compression.Algorithm.valueOf(StringUtils.upperCase(compress))); // 压缩.
                                                                                                    // LZO,GZ,NONE,SNAPPY,LZ4
      // desc.addFamily(newfamily);
      admin.addColumn(tableName, newfamily);

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/altercolumn", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject altercolumn(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String columnname = paramMap.get("columnname");
    String compress = paramMap.get("compress");
    int ttl = Integer.parseInt(paramMap.get("ttl"));
    int version = Integer.parseInt(paramMap.get("version"));
    String dscp = paramMap.get("dscp");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      TableName tableName = TableName.valueOf(dbname + ":" + tblname);
      HTableDescriptor table = admin.getTableDescriptor(tableName);
      HColumnDescriptor existfamily = new HColumnDescriptor(columnname);
      existfamily.setTimeToLive(ttl); // ttl,单位s
      existfamily.setMaxVersions(version);// 指定数据最大保存的版本个数。默认为3
      existfamily
          .setCompressionType(Compression.Algorithm.valueOf(StringUtils.upperCase(compress))); // 压缩.
                                                                                               // LZO,GZ,NONE,SNAPPY,LZ4
      table.modifyFamily(existfamily);
      admin.modifyTable(tableName, table);

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/deletecolumn", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject deletecolumn(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String dbname = paramMap.get("dbname");
    String tblname = paramMap.get("tblname");
    String columnname = paramMap.get("columnname");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);
      Admin admin = conn.getAdmin();

      TableName tableName = TableName.valueOf(dbname + ":" + tblname);
      admin.deleteColumn(tableName, columnname.getBytes("UTF-8"));

      admin.close();

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Exception e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/grant", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject grant(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String fullname = paramMap.get("fullname");
    String auth = paramMap.get("auth");
    String tenantName2 = paramMap.get("tenantName2");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);

      String dbname = fullname.split("\\.")[0];
      String tblname = fullname.split("\\.")[1];
      String newTblname = dbname + ":" + tblname;
      TableName tableName = TableName.valueOf(newTblname);

      List<String> actions = new ArrayList<String>(Arrays.asList(auth.split(",")));

      Permission.Action[] actions1 = fillActions(actions);

      // 获取用户已有权限
      Set<Permission.Action> set = new TreeSet<Permission.Action>();
      List<UserPermission> list = AccessControlClient.getUserPermissions(conn, newTblname);
      for (UserPermission userPerm : list) {
        if ((userPerm.getTableName()).getNameAsString().equals(newTblname)
            && Bytes.toString(userPerm.getUser()).equals(tenantName2)) {
          // 需要测试
          if (StringUtils.isBlank(Bytes.toString(userPerm.getFamily()))
              && StringUtils.isBlank(Bytes.toString(userPerm.getQualifier()))) {
            for (int i = 0; i < userPerm.getActions().length; i++) {
              set.add(userPerm.getActions()[i]);
            }
          }
        }
      }
      // 剩余权限=已有权限 + 新增权限
      for (int i = 0; i < actions1.length; i++) {
        if (!set.contains(actions1[i])) {
          set.add(actions1[i]);
        }
      }
      Permission.Action[] mergePermAction = set.toArray(new Permission.Action[] {});

      // grant(final Connection connection, final TableName tableName, final String userName, final
      // byte[] family, final byte[] qual, final Permission.Action... actions)
      AccessControlClient.grant(conn, tableName, tenantName2, null, null, mergePermAction);

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Throwable e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/revoke", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject revoke(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String fullname = paramMap.get("fullname");
    String auth = paramMap.get("auth");
    String tenantName2 = paramMap.get("tenantName2");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);

      // grant ‘bobsmith’, ‘RW’, ‘t1’, ‘f1’, ‘col1’
      String dbname = fullname.split("\\.")[0];
      String tblname = fullname.split("\\.")[1];
      String newTblname = dbname + ":" + tblname;
      TableName tableName = TableName.valueOf(newTblname);

      List<String> actions = new ArrayList<String>(Arrays.asList(auth.split(",")));
      Permission.Action[] actions1 = fillActions(actions);

      // 获取用户已有权限
      Set<Permission.Action> set = new TreeSet<Permission.Action>();
      List<UserPermission> list =
          AccessControlClient.getUserPermissions(conn, dbname + ":" + tblname);
      for (UserPermission userPerm : list) {
        if ((userPerm.getTableName()).getNameAsString().equals(newTblname)
            && Bytes.toString(userPerm.getUser()).equals(tenantName2)) {
          // 需要测试
          if (StringUtils.isBlank(Bytes.toString(userPerm.getFamily()))
              && StringUtils.isBlank(Bytes.toString(userPerm.getQualifier()))) {
            for (int i = 0; i < userPerm.getActions().length; i++) {
              set.add(userPerm.getActions()[i]);
            }
          }
        }
      }

      // 剩余权限=已有权限 - 删除权限
      for (int i = 0; i < actions1.length; i++) {
        if (set.contains(actions1[i])) {
          set.remove(actions1[i]);
        }
      }
      Permission.Action[] mergePermAction = set.toArray(new Permission.Action[] {});
      // 打印 existActions
      // System.out.println(Arrays.asList(set));

      if (mergePermAction.length == 0) {
        // 无剩余权限
        AccessControlClient.revoke(conn, tableName, tenantName2, null, null,
            Permission.Action.READ, Permission.Action.WRITE, Permission.Action.EXEC,
            Permission.Action.CREATE, Permission.Action.ADMIN);
      } else {
        // 有剩余权限
        // grant(final Connection connection, final TableName tableName, final String userName,
        // final byte[] family, final byte[] qual, final Permission.Action... actions)
        AccessControlClient.grant(conn, tableName, tenantName2, null, null, mergePermAction);
      }

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Throwable e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  @RequestMapping(value = "/setacl", method = {RequestMethod.POST},
      consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public JSONObject setacl(@RequestBody Map<String, String> paramMap) throws Exception {
    logger.debug(JSONObject.fromObject(paramMap));

    // 返回值
    JSONObject json = new JSONObject();
    // 解析参数
    String fullname = paramMap.get("fullname");
    String auth = paramMap.get("auth");
    String tenantName2 = paramMap.get("tenantName2");

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(conf);

      // grant ‘bobsmith’, ‘RW’, ‘t1’, ‘f1’, ‘col1’
      String dbname = fullname.split("\\.")[0];
      String tblname = fullname.split("\\.")[1];
      String newTblname = dbname + ":" + tblname;
      TableName tableName = TableName.valueOf(newTblname);

      List<String> actions = new ArrayList<String>(Arrays.asList(auth.split(",")));
      Permission.Action[] mergePermAction = fillActions(actions);

      if (mergePermAction.length == 0) {
        // 无剩余权限
        AccessControlClient.revoke(conn, tableName, tenantName2, null, null,
            Permission.Action.READ, Permission.Action.WRITE, Permission.Action.EXEC,
            Permission.Action.CREATE, Permission.Action.ADMIN);
      } else {
        // 有剩余权限
        // grant(final Connection connection, final TableName tableName, final String userName,
        // final byte[] family, final byte[] qual, final Permission.Action... actions)
        AccessControlClient.grant(conn, tableName, tenantName2, null, null, mergePermAction);
      }

      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", 0);
      json.put("logDescription", "Operation succeeded.");
    } catch (Throwable e) {
      json.put("id", -1);
      json.put("state", "COMPLETE");
      json.put("errorCode", -1);
      json.put("logDescription", e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  public static Permission.Action[] fillActions(List<String> actions) {
    Permission.Action[] actions1 = new Permission.Action[actions.size()];
    for (int i = 0; i < actions.size(); i++) {
      if ("read".equalsIgnoreCase(actions.get(i)) || "R".equalsIgnoreCase(actions.get(i))) {
        actions1[i] = Permission.Action.READ;
      }
      if ("write".equalsIgnoreCase(actions.get(i)) || "W".equalsIgnoreCase(actions.get(i))) {
        actions1[i] = Permission.Action.WRITE;
      }
      if ("exec".equalsIgnoreCase(actions.get(i)) || "E".equalsIgnoreCase(actions.get(i))) {
        actions1[i] = Permission.Action.EXEC;
      }
      if ("create".equalsIgnoreCase(actions.get(i)) || "C".equalsIgnoreCase(actions.get(i))) {
        actions1[i] = Permission.Action.CREATE;
      }
      if ("admin".equalsIgnoreCase(actions.get(i)) || "A".equalsIgnoreCase(actions.get(i))) {
        actions1[i] = Permission.Action.ADMIN;
      }
    }
    return actions1;
  }

}
