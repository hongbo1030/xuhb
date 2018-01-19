package com.grandata.www.grandc.hive;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class GetClusterNoByArrUDF extends UDF {

  public Text evaluate(String p, List<String> lp) {
    // p:p0,p1,p2...p23
    // lp:clusterno,p0,p1,p2...p23
    // init
    Double min_distance = Double.MAX_VALUE;
    Double distance = 0D;
    String r_clusterno = "";
    if (lp.size() == 0) {
      // 如果聚类目标是空，那么返回null
      return null;
    } else if (lp.size() == 0) {
      // 如果聚类目标只有一个，那么返回这一个的clusterno
      return new Text(lp.get(0).split(",")[0]);
    } else {
      String[] s = p.split(",");
      for (String c : lp) {
        String[] cluster = c.split(",");
        distance = 0D;
        // 循环
        for (int j = 0; j < s.length; j++) {
          distance =
              distance
                  + Math.pow((Double.parseDouble(cluster[j + 1]) - Double.parseDouble(s[j])), 2);
        }
        if (min_distance > distance) {
          min_distance = distance;
          r_clusterno = cluster[0];
        }
      }

      return new Text(r_clusterno);
    }
  }

  public static void main(String[] args) {
    String p = "1.5,2.2,3.2";
    List<String> lp = new ArrayList<String>();
    String c = "";

    c = "c1,1,2,3";
    lp.add(c);

    c = "c2,1.2,2.2,3.2";
    lp.add(c);

    c = "c3,1.3,2.3,3.3";
    lp.add(c);
    GetClusterNoByArrUDF getClusterNoByArrUDF = new GetClusterNoByArrUDF();

    System.out.println(getClusterNoByArrUDF.evaluate(p, lp));
  }
}
