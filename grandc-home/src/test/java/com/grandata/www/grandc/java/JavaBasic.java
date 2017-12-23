package com.grandata.www.grandc.java;

import org.apache.hadoop.hbase.io.compress.Compression;

public class JavaBasic {
  public static void main(String[] args) {

/*    String dbname = "db1";
    String dscp = "这是db1";
    String location = "/a/b/c";
    String sql = String.format("CREATE DATABASE %s COMMENT %s LOCATION %s", dbname, dscp, location);
    System.out.println(sql);*/

    System.out.println(Compression.Algorithm.NONE.getName());
    System.out.println(Compression.Algorithm.valueOf("SNAPPY"));
  }
}
