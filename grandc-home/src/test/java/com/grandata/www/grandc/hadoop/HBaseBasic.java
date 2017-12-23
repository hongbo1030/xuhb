package com.grandata.www.grandc.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

public class HBaseBasic {
  //hbase  1.1.2
//声明静态配置
  static Configuration conf = null;
  static {
      conf = HBaseConfiguration.create();
      conf.set("hbase.zookeeper.property.clientPort", "2181");
      conf.set("hbase.zookeeper.quorum", "grandata201,grandata204,grandata205");
      //conf.set("hbase.master", "192.168.1.100:600000");

      //config.addResource("hbase-site.xml");
      //config.addResource("core-site.xml");

      //设置为hbase superuser. TODO
      System.setProperty("HADOOP_USER_NAME", "hbase");
  }

  //2.4.1. 创建HBase namespace
  public static void createdb(Connection conn, String dbname, String dscp) throws IOException {
    Admin admin = conn.getAdmin();

    NamespaceDescriptor descriptor = NamespaceDescriptor.create(dbname).build();
    admin.createNamespace(descriptor);

    admin.close();
  }

  //2.4.2. 删除HBase namespace
  public static void deletedb(Connection conn, String dbname) throws IOException {
    Admin admin = conn.getAdmin();

    admin.deleteNamespace(dbname);

    admin.close();
  }

  //2.4.3. 创建HBase表. 未找到描述字段如何写入hbase
  public static void creatTable(Connection conn, String dbname, String tblname, String dscp, JSONArray columns)
      throws IOException {
    Admin admin = conn.getAdmin();

    TableName tableNameObj = TableName.valueOf(dbname+":"+tblname);
    HTableDescriptor desc = new HTableDescriptor(tableNameObj);

    List<JSONObject> list = new ArrayList<JSONObject>();
    for (int i = 0; i < columns.size(); i++) {
      list.add(columns.getJSONObject(i));
    }
  //排序
  Collections.sort(list, new Comparator<JSONObject>() {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
      int num1 = o1.getInt("columnindex");
      int num2 = o2.getInt("columnindex");
      if(num1>num2){
        return 1;
    }else if(num1<num2){
        return -1;
    }else{
        return 0;
    }
    }
  });

  for(JSONObject family:list) {
    HColumnDescriptor c = new HColumnDescriptor(family.getString("columnname"));
    c.setTimeToLive(family.getInt("ttl")); //ttl,单位ms
    c.setMaxVersions(family.getInt("version"));//指定数据最大保存的版本个数。默认为3
      c.setCompressionType(Compression.Algorithm.valueOf(StringUtils.upperCase((family.getString("compress"))))); //压缩

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
  }

  //2.4.4. 删除HBase表
  public static void deletetbl(Connection conn, String dbname, String tblname) throws IOException {
    Admin admin = conn.getAdmin();

    TableName tableNameObj = TableName.valueOf(dbname+":"+tblname);
    admin.disableTable(tableNameObj);
    admin.deleteTable(tableNameObj);

    admin.close();
  }

  //2.4.5. 增加HBase列簇
  public static void addcolumn(Connection conn, String dbname, String tblname, String columnname, String compress, int ttl, int version, String dscp) throws IOException {
    Admin admin = conn.getAdmin();

    TableName tableName = TableName.valueOf(dbname+":"+tblname);
    //HTableDescriptor table  = admin.getTableDescriptor(tableName);
    HColumnDescriptor newfamily = new HColumnDescriptor(columnname);
    newfamily.setTimeToLive(ttl); //ttl,单位ms
    newfamily.setMaxVersions(version);//指定数据最大保存的版本个数。默认为3
    newfamily.setCompressionType(Compression.Algorithm.valueOf(StringUtils.upperCase(compress))); //压缩. LZO,GZ,NONE,SNAPPY,LZ4
    //desc.addFamily(newfamily);
    admin.addColumn(tableName, newfamily);

    admin.close();
  }

  //2.4.6. 修改HBase列簇信息
  public static void altercolumn(Connection conn, String dbname, String tblname, String columnname, String compress, int ttl, int version, String dscp) throws IOException {
    Admin admin = conn.getAdmin();

    TableName tableName = TableName.valueOf(dbname+":"+tblname);
    HTableDescriptor table  = admin.getTableDescriptor(tableName);
    HColumnDescriptor existfamily = new HColumnDescriptor(columnname);
    existfamily.setTimeToLive(ttl); //ttl,单位s
    existfamily.setMaxVersions(version);//指定数据最大保存的版本个数。默认为3
    existfamily.setCompressionType(Compression.Algorithm.valueOf(StringUtils.upperCase(compress))); //压缩. LZO,GZ,NONE,SNAPPY,LZ4
    table.modifyFamily(existfamily);
    admin.modifyTable(tableName, table);

    admin.close();
  }

  //2.4.7. 删除HBase列簇
  public static void deletecolumn(Connection conn, String dbname, String tblname, String columnname) throws IOException {
    Admin admin = conn.getAdmin();

    TableName tableName = TableName.valueOf(dbname+":"+tblname);
    admin.deleteColumn(tableName, columnname.getBytes("UTF-8"));

    admin.close();
  }

  //2.4.8. 表级授权
  public static void grant(Connection conn, String fullname, String auth, String tenantName2) throws Throwable {
    //grant ‘bobsmith’, ‘RW’, ‘t1’, ‘f1’, ‘col1’
    String dbname = fullname.split("\\.")[0];
    String tblname = fullname.split("\\.")[1];
    String newTblname = dbname+":"+tblname;
    TableName tableName = TableName.valueOf(newTblname);

    List<String> actions = new ArrayList<String>(Arrays.asList(auth.split(",")));

    Permission.Action[] actions1 = fillActions(actions);

    //获取用户已有权限
    Set<Permission.Action> set = new TreeSet<Permission.Action>();
    List<UserPermission> list = AccessControlClient.getUserPermissions(conn, newTblname);
    for(UserPermission userPerm:list) {
      if( (userPerm.getTableName()).getNameAsString().equals(newTblname) && Bytes.toString(userPerm.getUser()).equals(tenantName2)) {
        //需要测试
if(StringUtils.isBlank(Bytes.toString(userPerm.getFamily())) && StringUtils.isBlank(Bytes.toString(userPerm.getQualifier())) ) {
  for (int i = 0; i < userPerm.getActions().length; i++) {
    set.add(userPerm.getActions()[i]);
  }
}
      }
    }
    //剩余权限=已有权限 + 新增权限
    for (int i = 0; i < actions1.length; i++) {
      if(!set.contains(actions1[i])) {
        set.add(actions1[i]);
      }
  }
    Permission.Action[] mergePermAction = set.toArray(new Permission.Action[] {});

    //grant(final Connection connection, final TableName tableName, final String userName, final byte[] family, final byte[] qual, final Permission.Action... actions)
    AccessControlClient.grant(conn, tableName, tenantName2, null, null, mergePermAction);
  }

  //2.4.9. 取消表级授权
  //revoke
  // revoke ‘bobsmith’, ‘t1’, ‘f1’, ‘col1’
  public static void revoke(Connection conn, String fullname, String auth, String tenantName2) throws Throwable {
    //grant ‘bobsmith’, ‘RW’, ‘t1’, ‘f1’, ‘col1’
    String dbname = fullname.split("\\.")[0];
    String tblname = fullname.split("\\.")[1];
    String newTblname = dbname+":"+tblname;
    TableName tableName = TableName.valueOf(newTblname);

    List<String> actions = new ArrayList<String>(Arrays.asList(auth.split(",")));
    Permission.Action[] actions1 = fillActions(actions);

    //获取用户已有权限
    Set<Permission.Action> set = new TreeSet<Permission.Action>();
    List<UserPermission> list = AccessControlClient.getUserPermissions(conn, dbname+":"+tblname);
    for(UserPermission userPerm:list) {
      if( (userPerm.getTableName()).getNameAsString().equals(newTblname) && Bytes.toString(userPerm.getUser()).equals(tenantName2)) {
        //需要测试
if(StringUtils.isBlank(Bytes.toString(userPerm.getFamily())) && StringUtils.isBlank(Bytes.toString(userPerm.getQualifier())) ) {
  for (int i = 0; i < userPerm.getActions().length; i++) {
    set.add(userPerm.getActions()[i]);
  }
}
      }
    }

    //剩余权限=已有权限 - 删除权限
    for (int i = 0; i < actions1.length; i++) {
      if(set.contains(actions1[i])) {
        set.remove(actions1[i]);
      }
  }
    Permission.Action[] mergePermAction = set.toArray(new Permission.Action[] {});
    //打印 existActions
    System.out.println(Arrays.asList(set));

    if(mergePermAction.length==0) {
      //无剩余权限
      AccessControlClient.revoke(conn, tableName, tenantName2, null, null, Permission.Action.READ, Permission.Action.WRITE, Permission.Action.EXEC, Permission.Action.CREATE, Permission.Action.ADMIN);
    } else {
      //有剩余权限
      //grant(final Connection connection, final TableName tableName, final String userName, final byte[] family, final byte[] qual, final Permission.Action... actions)
      AccessControlClient.grant(conn, tableName, tenantName2, null, null, mergePermAction);
    }
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

  public static void main(String[] args) throws Throwable {
    Connection conn = ConnectionFactory.createConnection(conf);

    //
    String dbname = "hbasetest";
    String tblname = "t1";
    String dscp = "dscp dscp";
    String fullname = dbname+"."+tblname;
    String tenantName2 = "xuhb";
    String auth = "R,W,E,C,A";
    String columnname = "f1";
    String compress="snappy";
    int ttl = 86400;
    int version = 3;

    JSONArray columns = new JSONArray();
    JSONObject column = new JSONObject();

    column = new JSONObject();
    column.put("columnindex", 1);
    column.put("columnname", "a1");
    column.put("compress", compress);
    column.put("ttl", ttl);
    column.put("version", version);
    columns.add(column);

    column = new JSONObject();
    column.put("columnindex", 1);
    column.put("columnname", "a2");
    column.put("compress", compress);
    column.put("ttl", ttl);
    column.put("version", version);
    columns.add(column);


    //createdb(conn, dbname, dscp);
    //deletedb(conn, dbname);
    //creatTable(conn, dbname, tblname, dscp, columns);
    //deletetbl(conn, dbname, tblname);
    //addcolumn(conn, dbname, tblname, columnname, "NONE", ttl, version, dscp);
    //altercolumn(conn, dbname, tblname, columnname, compress, 100, 3, dscp);
    //deletecolumn(conn, dbname, tblname, columnname);
    //grant(conn, fullname, "W", tenantName2);
    revoke(conn, fullname, "E,C", tenantName2);


    conn.close();
  }
}
