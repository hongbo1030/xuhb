package com.grandata.www.grandc.hadoop;

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
import java.util.Set;
import java.util.TreeSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

public class HiveBase {
  //hive 1.2.1
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";

  //executeQuery
  public static void executeQuery(Connection conn, String sql) throws SQLException {
    executeQuery(conn, "use default", sql);
  }

  public static JSONArray executeQuery(Connection conn, String preSql, String sql) throws SQLException {
    //
    JSONArray jsonA = new JSONArray();
    JSONObject json = new JSONObject();

    Statement stmt = conn.createStatement();
    stmt.execute(preSql);
    ResultSet res = stmt.executeQuery(sql);
    ResultSetMetaData resMeta = res.getMetaData();

    String[] metas = new String[resMeta.getColumnCount()];
    //列
    for(int i=1;i<=resMeta.getColumnCount();i++) {
      metas[i-1]=resMeta.getColumnName(i);
      resMeta.getColumnType(i);
      resMeta.getColumnTypeName(i);
    }

    while (res.next()) {
      json = new JSONObject();
      for(int j=0;j<metas.length;j++) {
        System.out.print(metas[j]+"="+res.getString(metas[j])+";");
        json.put(metas[j], res.getString(metas[j]));
      }
      jsonA.add(json);
      System.out.println();
    }

    res.close();
    stmt.close();

    return jsonA;
  }

  //execute
  public static void execute(Connection conn, String sql) throws SQLException {
    execute(conn, "default", sql);
  }

  //execute
  public static void execute(Connection conn, String database, String sql) throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.execute("use "+database);
    stmt.execute(sql);
    stmt.close();
  }


  //以下是根据接口拼接语句
  //2.3.2. 创建Hive库
  public static String createdb(String dbname, String location, String dscp) {
/*    CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
      [COMMENT database_comment]
          [LOCATION hdfs_path]
          [WITH DBPROPERTIES (property_name=property_value, ...)];*/

      String sql = String.format("CREATE DATABASE %s COMMENT \"%s\" LOCATION \"%s\"", dbname, dscp, location);
      System.out.println("sql="+sql);
      return sql;
  }

  //2.3.3. 删除Hive库
  public static String deletedb(String dbname) {
    //DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];
      String sql = String.format("DROP DATABASE %s RESTRICT", dbname);
      System.out.println("sql="+sql);
      return sql;
  }
  public static String deletedb(String dbname, boolean force) {
    //DROP (DATABASE|SCHEMA) [IF EXISTS] database_name [RESTRICT|CASCADE];
    String sql = String.format("DROP DATABASE %s", dbname);
    if(force) {
      sql = sql+" CASCADE";
    } else {
      sql = sql+" RESTRICT";
    }
      System.out.println("sql="+sql);
      return sql;
  }

  //2.3.4. 创建Hive表
  public static String createtbl(String dbname, String tblname, String location, String stored, String field, String compress, int type, String dscp, JSONArray columns) {
/*    CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [db_name.]table_name    -- (Note: TEMPORARY available in Hive 0.14.0 and later)
      [(col_name data_type [COMMENT col_comment], ... [constraint_specification])]
      [COMMENT table_comment]
      [PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]
      [CLUSTERED BY (col_name, col_name, ...) [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
      [SKEWED BY (col_name, col_name, ...)                  -- (Note: Available in Hive 0.10.0 and later)]
         ON ((col_value, col_value, ...), (col_value, col_value, ...), ...)
         [STORED AS DIRECTORIES]
      [
       [ROW FORMAT row_format] 
       [STORED AS file_format]
         | STORED BY 'storage.handler.class.name' [WITH SERDEPROPERTIES (...)]  -- (Note: Available in Hive 0.6.0 and later)
      ]
      [LOCATION hdfs_path]
      [TBLPROPERTIES (property_name=property_value, ...)]   -- (Note: Available in Hive 0.6.0 and later)
      [AS select_statement];   -- (Note: Available in Hive 0.5.0 and later; not supported for external tables)
     
    CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [db_name.]table_name
      LIKE existing_table_or_view_name
      [LOCATION hdfs_path];
     
    data_type
      : primitive_type
      | array_type
      | map_type
      | struct_type
      | union_type  -- (Note: Available in Hive 0.7.0 and later)
     
    primitive_type
      : TINYINT
      | SMALLINT
      | INT
      | BIGINT
      | BOOLEAN
      | FLOAT
      | DOUBLE
      | DOUBLE PRECISION -- (Note: Available in Hive 2.2.0 and later)
      | STRING
      | BINARY      -- (Note: Available in Hive 0.8.0 and later)
      | TIMESTAMP   -- (Note: Available in Hive 0.8.0 and later)
      | DECIMAL     -- (Note: Available in Hive 0.11.0 and later)
      | DECIMAL(precision, scale)  -- (Note: Available in Hive 0.13.0 and later)
      | DATE        -- (Note: Available in Hive 0.12.0 and later)
      | VARCHAR     -- (Note: Available in Hive 0.12.0 and later)
      | CHAR        -- (Note: Available in Hive 0.13.0 and later)
     
    array_type
      : ARRAY < data_type >
     
    map_type
      : MAP < primitive_type, data_type >
     
    struct_type
      : STRUCT < col_name : data_type [COMMENT col_comment], ...>
     
    union_type
       : UNIONTYPE < data_type, data_type, ... >  -- (Note: Available in Hive 0.7.0 and later)
     
    row_format
      : DELIMITED [FIELDS TERMINATED BY char [ESCAPED BY char]] [COLLECTION ITEMS TERMINATED BY char]
            [MAP KEYS TERMINATED BY char] [LINES TERMINATED BY char]
            [NULL DEFINED AS char]   -- (Note: Available in Hive 0.13 and later)
      | SERDE serde_name [WITH SERDEPROPERTIES (property_name=property_value, property_name=property_value, ...)]
     
    file_format:
      : SEQUENCEFILE
      | TEXTFILE    -- (Default, depending on hive.default.fileformat configuration)
      | RCFILE      -- (Note: Available in Hive 0.6.0 and later)
      | ORC         -- (Note: Available in Hive 0.11.0 and later)
      | PARQUET     -- (Note: Available in Hive 0.13.0 and later)
      | AVRO        -- (Note: Available in Hive 0.14.0 and later)
      | INPUTFORMAT input_format_classname OUTPUTFORMAT output_format_classname
     
    constraint_specification:
      : [, PRIMARY KEY (col_name, ...) DISABLE NOVALIDATE ]
        [, CONSTRAINT constraint_name FOREIGN KEY (col_name, ...) REFERENCES table_name(col_name, ...) DISABLE NOVALIDATE */

    //column: "columnname":"c1", "columnindex":"1", "partitioned":"2","dscp":"dscp",
    StringBuffer sql = new StringBuffer();
    if(type==1) {
      sql.append("CREATE EXTERNAL TABLE ").append(dbname).append(".").append(tblname);
    } else {
      sql.append("CREATE TABLE ").append(dbname).append(".").append(tblname);
    }

  //partiton=1
    List<JSONObject> list1 = new ArrayList<JSONObject>();
    //partiton=2
    List<JSONObject> list2 = new ArrayList<JSONObject>();
    //遍历 columns.组成两个排序的list
    for(int i=0;i<columns.size();i++) {
      JSONObject json = columns.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
      if(json.getInt("partitioned")==1) {
        list1.add(json);
      } else {
        list2.add(json);
      }
    }
    //排序
    Collections.sort(list1, new Comparator<JSONObject>() {
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

    System.out.println(list1.toString()); // [68.9, 92.8, 105, 168.61, 242, 317]
    System.out.println(list2.toString());

    sql.append("(");
    for(JSONObject json:list1) {
      sql.append(json.getString("columnname")).append(" ").append(json.getString("columntype")).append(" COMMENT \"").append(json.getString("dscp")).append("\",");
    }
    //去掉末尾的,
    sql.deleteCharAt(sql.length() - 1).append(")");
    if(StringUtils.isNotBlank(dscp)) {
      sql.append(" COMMENT \"").append(dscp).append("\"");
    }

    //partition
    if(list2.size()>0) {
      sql.append(" PARTITIONED BY (");
      for(JSONObject json:list2) {
        sql.append(json.getString("columnname")).append(" ").append(json.getString("columntype")).append(" COMMENT \"").append(json.getString("dscp")).append("\",");
      }
    }
    if(sql.toString().endsWith(",")) {
      System.out.println("1");
      sql.deleteCharAt(sql.length() - 1).append(")");
    } else {
      System.out.println("2");
    }

    //ROW FORMAT row_format
    if(StringUtils.isNotBlank(field)) {
      sql.append(" ROW FORMAT DELIMITED FIELDS TERMINATED BY \"").append(field).append("\"");
    }

    //STORED AS file_format
    if(StringUtils.isNotBlank(stored)) {
      sql.append(" STORED AS ").append(stored);
    }
    //LOCATION hdfs_path
    if(StringUtils.isNotBlank(location)) {
      sql.append(" LOCATION  \"").append(location).append("\"");
    }

    //TBLPROPERTIES ('orc.compress'='ZLIB') .TODO

    System.out.println("sql="+sql.toString());

    return sql.toString();
  }

  //2.3.5. 修改Hive表名
  public static String altertbl(String dbname, String tblname, String newtblname) {
    //ALTER TABLE table_name RENAME TO new_table_name;
    //use <dbname>
    String sql = String.format("ALTER TABLE %s RENAME TO %s ", tblname, newtblname);
    System.out.println("sql="+sql);
    return sql;
  }

  //2.3.6. 删除Hive表
  public static String deletetbl(String dbname, String tblname) {
    //DROP TABLE [IF EXISTS] table_name [PURGE];
    String sql = String.format("DROP TABLE IF EXISTS %s.%s", dbname, tblname);
    System.out.println("sql="+sql);
    return sql;
  }

  //2.3.7. 增加Hive字段
  public static String addcolumn(String dbname, String tblname, String columnname, String columntype, String dscp) {
    /*
     ALTER TABLE table_name
  [PARTITION partition_spec]                 -- (Note: Hive 0.14.0 and later)
  ADD|REPLACE COLUMNS (col_name data_type [COMMENT col_comment], ...)
  [CASCADE|RESTRICT]                         -- (Note: Hive 1.1.0 and later)
     */
    //use <dbname>
    StringBuffer sql = new StringBuffer();
    sql.append("ALTER TABLE ").append(tblname).append(" ADD COLUMNS(").append(columnname).append(" ").append(columntype).append(" COMMENT \"").append(dscp).append("\")");
    System.out.println("sql="+sql.toString());
    return sql.toString();
  }

  //2.3.8. 修改Hive字段
  public static String renamecolumn(String dbname, String tblname, String columnname, String newcolumnname,String columntype,String dscp ) {
    StringBuffer sql = new StringBuffer();

      sql.append("ALTER TABLE ").append(tblname).append(" CHANGE ").append(columnname).append(" ").append(newcolumnname).append(" ").append(columntype).append(" COMMENT \"").append(dscp).append("\"");

    System.out.println("sql="+sql.toString());
    return sql.toString();
  }

  //2.3.9. 表级授权. auth=INSERT | SELECT | UPDATE | DELETE | ALL
  public static String grant(Connection conn, String fullname, String auth, String tenantName2) throws SQLException {
    /*
    GRANT
    priv_type [, priv_type ] ...
    ON table_or_view_name
    TO principal_specification [, principal_specification] ...
    [WITH GRANT OPTION];*/

    //获取用户权限
    Set<String> set = new TreeSet<String>();
    String sql = String.format("show grant user %s ON TABLE %s", tenantName2, fullname);
    JSONArray jsonA = executeQuery(conn, "set ROLE ADMIN", sql);
    //遍历 jsonA. database,table,user 取出 grant。然后累加
    for(int i=0;i<jsonA.size();i++){
      JSONObject json = jsonA.getJSONObject(i);
      if( (json.getString("database")+"."+json.getString("table")).equals(fullname) && json.getString("principal_name").equals(tenantName2)
          && json.getString("principal_type").equals("USER") && StringUtils.isBlank(json.getString("column")) && StringUtils.isBlank(json.getString("partition"))) {
        set.add(json.getString("privilege"));
      }
    }

    String[] s = auth.split(",");
    List<String> newauth = new ArrayList<String>();
    for(int i=0;i<s.length;i++) {
      if(!set.contains(s[i])) {
        newauth.add(s[i]);
      }
    }
    System.out.println(StringUtils.join(newauth.toArray(), ","));
    if(newauth.size() > 0 ) {
      sql = String.format("GRANT %s ON TABLE %s TO USER %s", StringUtils.join(newauth.toArray(), ","), fullname, tenantName2);
    } else {
      sql = "";
    }
    System.out.println("sql="+sql);
    return sql;
  }

  //2.3.10. 取消表级授权
  public static String revoke(Connection conn, String fullname, String auth, String tenantName2) throws SQLException {
    /*    REVOKE [GRANT OPTION FOR]
        priv_type [, priv_type ] ...
        ON table_or_view_name
        FROM principal_specification [, principal_specification] ... ;

    principal_specification
      : USER user
      | ROLE role

    priv_type
      : INSERT | SELECT | UPDATE | DELETE | ALL*/

    Set<String> set = new TreeSet<String>();
    String sql = String.format("show grant user %s ON TABLE %s", tenantName2, fullname);
    JSONArray jsonA = executeQuery(conn, "set ROLE ADMIN", sql);
    //遍历 jsonA. database,table,user 取出 grant。然后累加
    for(int i=0;i<jsonA.size();i++){
      JSONObject json = jsonA.getJSONObject(i);
      if( (json.getString("database")+"."+json.getString("table")).equals(fullname) && json.getString("principal_name").equals(tenantName2)
          && json.getString("principal_type").equals("USER") && StringUtils.isBlank(json.getString("column")) && StringUtils.isBlank(json.getString("partition"))) {
        set.add(json.getString("privilege"));
      }
    }

    String[] s = auth.split(",");
    List<String> newauth = new ArrayList<String>();
    for(int i=0;i<s.length;i++) {
      if(set.contains(s[i])) {
        newauth.add(s[i]);
      }
    }
    System.out.println(StringUtils.join(newauth.toArray(), ","));

    if(newauth.size() > 0 ) {
      sql = String.format("REVOKE %s ON TABLE %s FROM USER %s", StringUtils.join(newauth.toArray(), ","), fullname, tenantName2);
    } else {
      sql = "";
    }
    System.out.println("sql="+sql);
    return sql;
  }

  /**
   * @param args
   * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
      try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    //replace "hive" here with the name of the user the queries should run as
    Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.10.204:10000/default", "hive", "");

/*    executeQuery(conn, "show tables");
    executeQuery(conn, "select count(1) from test");
    executeQuery(conn, "select id from test");
    execute(conn, "create table t2(id int, name String)");
    execute(conn, "drop table t2");*/

    //execute(conn, "create database test");


    JSONArray columns = new JSONArray();
    JSONObject column = new JSONObject();

    column = new JSONObject();
    column.put("columnname", "c1");
    column.put("columnindex", 1);
    column.put("partitioned", 1);
    column.put("columntype", "String");
    column.put("dscp", "dscp c1");
    columns.add(column);

    column = new JSONObject();
    column.put("columnname", "c2");
    column.put("columnindex", 2);
    column.put("partitioned", 1);
    column.put("columntype", "int");
    column.put("dscp", "dscp c2");
    columns.add(column);

    column = new JSONObject();
    column.put("columnname", "p1");
    column.put("columnindex", 1);
    column.put("partitioned", 2);
    column.put("columntype", "String");
    column.put("dscp", "dscp p1");
    columns.add(column);

    column = new JSONObject();
    column.put("columnname", "p2");
    column.put("columnindex", 2);
    column.put("partitioned", 2);
    column.put("columntype", "int");
    column.put("dscp", "dscp p2");
    columns.add(column);
    System.out.println("columns="+columns+";");

    String dbname = "test1";
    String tblname = "t2";
    String newtblname = "t2_new";
    String columnname = "c3";
    String columntype = "int";
    String dscp = "dscp dscp";
    String newcolumnname = "c1_new";
    String fullname = dbname+"."+tblname;
    String tenantName2 = "xuhb";
    String auth = "INSERT,SELECT,UPDATE,DELETE";

    String sql = "";

    //sql = createdb("test1", "/user/hive/test1", "这是一个测试库test1");
    //sql = deletedb("test1", true);
    //sql = createtbl("test1", tblname, "/user/hive/test1/t2", "textfile", ",", "NONE", 1, "t1", columns);

    //sql = altertbl(dbname, tblname, newtblname);
    //sql = deletetbl(dbname, newtblname);
    //sql = addcolumn(dbname, tblname, columnname, columntype, dscp);
    //sql = renamecolumn(dbname, tblname, columnname, newcolumnname,columntype,dscp, 1 );
    //SET ROLE ADMIN; TODO.  在hive-site.xml,hiveserver2-site.xml中都配置 hive.users.in.admin.role=hive,这样hive用户登录hiveserver2就可以使用该命令
    //sql = grant(conn, fullname, "INSERT", "xuhb2");
    //sql = revoke(conn, fullname, "INSERT,SELECT", tenantName2);

    if(StringUtils.isNotBlank(sql)) {
      execute(conn, dbname, sql);
    }


    conn.close();
    /*Statement stmt = conn.createStatement();
    String tableName = "testHiveDriverTable";
    stmt.execute("drop table if exists " + tableName);
    stmt.execute("create table " + tableName + " (key int, value string)");
    // show tables
    String sql = "show tables '" + tableName + "'";
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
    if (res.next()) {
      System.out.println(res.getString(1));
    }
       // describe table
    sql = "describe " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(res.getString(1) + "\t" + res.getString(2));
    }

    // load data into table
    // NOTE: filepath has to be local to the hive server
    // NOTE: /tmp/a.txt is a ctrl-A separated file with two fields per line
    String filepath = "/tmp/a.txt";
    sql = "load data local inpath '" + filepath + "' into table " + tableName;
    System.out.println("Running: " + sql);
    stmt.execute(sql);

    // select * query
    sql = "select * from " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
    }

    // regular hive query
    sql = "select count(1) from " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(res.getString(1));
    }

    //关闭
    stmt.close();
    res.close();*/


  }
}