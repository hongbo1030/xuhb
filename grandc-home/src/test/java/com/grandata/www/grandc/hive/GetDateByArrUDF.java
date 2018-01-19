package com.grandata.www.grandc.hive;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class GetDateByArrUDF extends UDF {

  public Text evaluate(List<Double> lp, int start, int end, Double basevalue) {
    // lp:p0,p1,p2...p23
    // init
    int r_label = -1;
    boolean flag = false;

    for (int i = start; i < end; i++) {
      if (lp.get(i) > basevalue) {
        if (i == 0 || lp.get(i) > lp.get(i - 1) * 2) {
          // 上升
          for (int j = i + 1; j < end + 1; j++) {
            if (lp.get(i) / 2 > lp.get(j)) {
              // 下降
              r_label = i;
              flag = true;
              break;
            }
          }
        }
      }
      if (flag) {
        break;
      }
    }

    return new Text(String.valueOf(r_label));

  }

  public static void main(String[] args) {
    List<Double> lp = new ArrayList<Double>();
    Double c = 0D;

    c = 0D;
    lp.add(c);

    c = 0.6D;
    lp.add(c);

    c = 2D;
    lp.add(c);
    c = 0.6D;
    lp.add(c);
    c = 0.2D;
    lp.add(c);

    GetDateByArrUDF getDateByArrUDF = new GetDateByArrUDF();

    System.out.println(getDateByArrUDF.evaluate(lp, 1, 3, 0.3));
  }
}
