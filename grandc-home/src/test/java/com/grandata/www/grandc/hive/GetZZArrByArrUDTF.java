package com.grandata.www.grandc.hive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

public class GetZZArrByArrUDTF extends GenericUDTF {

  @Override
  public void close() throws HiveException {
    // TODO Auto-generated method stub
  }

  @Override
  public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
    if (args.length != 1) {
      throw new UDFArgumentLengthException("ExplodeMap takes only one argument");
    }
    if (args[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
      throw new UDFArgumentException("ExplodeMap takes string as a parameter");
    }

    ArrayList<String> fieldNames = new ArrayList<String>();
    ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
    fieldNames.add("starttime");
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
    fieldNames.add("endtime");
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
    fieldNames.add("avgrate");
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

    return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
  }

  @Override
  public void process(Object[] args) throws HiveException {
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String input = args[0].toString();
    String[] split = input.split("\\|");

    Double contract_cap = Double.parseDouble(split[0]);
    Date date_date = null;
    try {
      date_date = df1.parse(split[1]);
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
    //
    List<String> p = Arrays.asList(split[2].split(","));
    // init
    ArrayList<Object[]> results = new ArrayList<Object[]>(); //结果
    String starttime = "";
    String endtime = "";
    String avgrate = "";
    Double maxvalue = Double.MIN_VALUE;

    for (int i = 0; i <= p.size() - 8;) {
      try {
        if (Double.parseDouble(p.get(i)) > contract_cap*0.8 && Double.parseDouble(p.get(i + 1)) > contract_cap*0.8
            && Double.parseDouble(p.get(i + 2)) > contract_cap*0.8 && Double.parseDouble(p.get(i + 3)) > contract_cap*0.8
            && Double.parseDouble(p.get(i + 4)) > contract_cap*0.8 && Double.parseDouble(p.get(i + 5)) > contract_cap*0.8
            && Double.parseDouble(p.get(i + 6)) > contract_cap*0.8 && Double.parseDouble(p.get(i + 7)) > contract_cap*0.8
            && (Double.parseDouble(p.get(i)) <= contract_cap || Double.parseDouble(p.get(i + 1)) <= contract_cap
                || Double.parseDouble(p.get(i + 2)) <= contract_cap || Double.parseDouble(p.get(i + 3)) <= contract_cap
                || Double.parseDouble(p.get(i + 4)) <= contract_cap || Double.parseDouble(p.get(i + 5)) <= contract_cap
                || Double.parseDouble(p.get(i + 6)) <= contract_cap || Double.parseDouble(p.get(i + 7)) <= contract_cap)) {
          starttime = df.format(date_date.getTime() + (i * 24 * 60 * 60 * 1000L) / 96);
          endtime = df.format(date_date.getTime() + ((i + 8) * 24 * 60 * 60 * 1000L) / 96);

          Double[] list = {Double.parseDouble(p.get(i)), Double.parseDouble(p.get(i + 1)), Double.parseDouble(p.get(i + 2)), Double.parseDouble(p.get(i + 3))
              ,Double.parseDouble(p.get(i+4)), Double.parseDouble(p.get(i + 5)), Double.parseDouble(p.get(i + 6)), Double.parseDouble(p.get(i + 7))}; // 定义一维数组

          for (int j = 0; j < list.length; j++) { // 开始循环一维数组
            if (maxvalue < list[j]) { // 循环判断数组元素
              maxvalue = list[j]; // 赋值给num，然后再次循环
            }
          }
          avgrate = String.valueOf(maxvalue/contract_cap);
          String[] result = {starttime, endtime, avgrate};
          //forward(result);
          results.add(result);
          i = i + 8;
        } else {
          i++;
        }
      } catch (Exception e) {
        continue;
      }
    }

    //输出
    for(Object[] o:results) {
      forward(o);
    }

  }

  public static void main(String[] args) throws HiveException {
    GetZZArrByArrUDTF getArrByArrUDTF = new GetZZArrByArrUDTF();

    String list = "0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D"
        +",0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D,0.9D";

    Object[] arg = new Object[3];
    arg[0] = 1D;
    arg[1]="2015-01-01";
    arg[2]=list;

    ObjectInspector[] inputOI = {PrimitiveObjectInspectorFactory.javaStringObjectInspector};
    getArrByArrUDTF.initialize(inputOI);

    getArrByArrUDTF.process(arg);

  }

}
