package com.grandata.www.grandc.java;

import java.io.IOException;
import java.text.ParseException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JavaBasic {
  public static void main(String[] args) throws IOException, ParseException {

/*    String dbname = "db1";
    String dscp = "这是db1";
    String location = "/a/b/c";
    String sql = String.format("CREATE DATABASE %s COMMENT %s LOCATION %s", dbname, dscp, location);
    System.out.println(sql);*/

/*    System.out.println(Compression.Algorithm.NONE.getName());
    System.out.println(Compression.Algorithm.valueOf("SNAPPY"));*/

    /*SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    System.out.println("foo:" + foo.format(new Date()));

    Calendar gc = GregorianCalendar.getInstance();
    System.out.println("gc.getTime():" + gc.getTime());
    System.out.println("gc.getTimeInMillis():" + new Date(gc.getTimeInMillis()));

    // 当前系统默认时区的时间：
    Calendar calendar = new GregorianCalendar();
    System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
    System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    // 美国洛杉矶时区
    TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
    // 时区转换
    calendar.setTimeZone(tz);
    System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
    System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    Date time = new Date();

    // 1、取得本地时间：
    java.util.Calendar cal = java.util.Calendar.getInstance();

    // 2、取得时间偏移量：
    int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

    // 3、取得夏令时差：
    int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

    // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

    // 之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。

    System.out.println("UTC:" + new Date(cal.getTimeInMillis()));

    Calendar calendar1 = Calendar.getInstance();
    TimeZone tztz = TimeZone.getTimeZone("GMT");
    calendar1.setTimeZone(tztz);
    System.out.println(calendar.getTime());
    System.out.println(calendar.getTimeInMillis());

    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    // df.setTimeZone(TimeZone.getTimeZone("UTC"));
    // System.out.println(df.parse("2014-08-23T09:20:05Z").toString());

    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    Date t = new Date();
    System.out.println(df1.format(t));
    System.out.println(df1.format(cal.getTime()) + "***********");
    df1.setTimeZone(TimeZone.getTimeZone("UTC"));
    System.out.println(df1.format(t));
    System.out.println("-----------");
    System.out.println(df1.format(df1.parse("2014-08-27T18:02Z")) + "***********");
    System.out.println("2014-08-27T18:02:59.676Z");*/

/*    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date date_date = null;
    try {
      date_date = df1.parse("2015-01-01");
    } catch (ParseException e1) {
      e1.printStackTrace();
    }

    for(int i=0;i<96;i++) {
      String starttime = df.format(date_date.getTime() + (i * 24 * 60 * 60 * 1000L) / 96);
      System.out.println( starttime);
    }*/

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

    System.out.println("json="+json);
  }
}
