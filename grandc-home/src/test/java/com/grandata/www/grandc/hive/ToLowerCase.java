package com.grandata.www.grandc.hive;

import java.util.List;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class ToLowerCase extends UDF {

    public Text evaluate(final Text s) {
        if (s == null) { return null; }
        return new Text(s.toString().toLowerCase());
    }

    public Text evaluate(Text p1, List<Integer> lp1, List<Integer> p3) {
      return new Text("");
    }
}
