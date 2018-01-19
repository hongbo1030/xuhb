package com.grandata.www.grandc.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business {

  static void repairArray(Double in[]) {
    Double i_first = null;
    Double i_last = null;
    boolean flag = false;
    // 判断整个数组不全是null
    for (int i = 0; i < in.length; i++) {
      if (in[i] != null) {
        flag = true;
        break;
      }
    }

    if (flag) {

      for (int i = 0; i < in.length; i++) {
        if (in[i] == null) {
          // 从i开始往前找最近非空值
          i_first = null;
          // 从i开始往后找最近非空值
          i_last = null;
          for (int j = i - 1; j > 0; j--) {
            if (in[j] != null) {
              i_first = in[j];
              break;
            }
          }

          for (int j = i + 1; j < in.length; j++) {
            if (in[j] != null) {
              i_last = in[j];
              break;
            }
          }

          // 由于数组非空，此处i_first,i_last至少一个非空
          if (i_first == null && i_last != null) {
            in[i] = i_last;
          } else if (i_first != null && i_last == null) {
            in[i] = i_first;
          } else if (i_first != null && i_last != null) {
            in[i] = (i_first + i_last) / 2;
          }
        }
      }
    }

  }

  static Double[] linearSmooth7(Double in[], Double hard_max, Double hard_min) {
    Double[] out = in.clone();
    int N = in.length;

    int i;
    if (N < 7) {
      for (i = 0; i <= N - 1; i++) {
        out[i] = in[i];
      }
    } else {
      // 前3个点
      out[0] =
          (13.0 * in[0] + 10.0 * in[1] + 7.0 * in[2] + 4.0 * in[3] + in[4] - 2.0 * in[5] - 5.0 * in[6]) / 28.0;

      out[1] = (5.0 * in[0] + 4.0 * in[1] + 3 * in[2] + 2 * in[3] + in[4] - in[6]) / 14.0;

      out[2] =
          (7.0 * in[0] + 6.0 * in[1] + 5.0 * in[2] + 4.0 * in[3] + 3.0 * in[4] + 2.0 * in[5] + in[6]) / 28.0;

      // 中间
      for (i = 3; i <= N - 4; i++) {
        out[i] =
            (in[i - 3] + in[i - 2] + in[i - 1] + in[i] + in[i + 1] + in[i + 2] + in[i + 3]) / 7.0;
      }

      // 后3个点
      out[N - 3] =
          (7.0 * in[N - 1] + 6.0 * in[N - 2] + 5.0 * in[N - 3] + 4.0 * in[N - 4] + 3.0 * in[N - 5]
              + 2.0 * in[N - 6] + in[N - 7]) / 28.0;

      out[N - 2] =
          (5.0 * in[N - 1] + 4.0 * in[N - 2] + 3.0 * in[N - 3] + 2.0 * in[N - 4] + in[N - 5] - in[N - 7]) / 14.0;

      out[N - 1] =
          (13.0 * in[N - 1] + 10.0 * in[N - 2] + 7.0 * in[N - 3] + 4 * in[N - 4] + in[N - 5] - 2
              * in[N - 6] - 5 * in[N - 7]) / 28.0;
    }

    // 对前3点和后3点 增加 最大值，最小值限制
    for (i = 0; i < 3; i++) {
      // 前3点
      if (out[i] > hard_max) {
        out[i] = hard_max;
      } else if (out[i] < hard_min) {
        out[i] = hard_min;
      }

      // 后3点
      if (out[N - i + 1] > hard_max) {
        out[N - i + 1] = hard_max;
      } else if (out[N - i + 1] < hard_min) {
        out[N - i + 1] = hard_min;
      }
    }
    return out;
  }

  static Double[][] linearSmooth7(Double in[][], Double hard_max, Double hard_min) {
    int i;

    Double[][] out = in.clone();
    int i_outcount = in.length;
    if (i_outcount < 7) {
      return out;
    }
    int i_incount = in[0].length;
    if (i_incount < 7) {
      return out;
    }

    for (int x = 0; x < i_outcount; x++) {
      // 前3个点
      out[x][0] =
          (13.0 * in[x][0] + 10.0 * in[x][1] + 7.0 * in[x][2] + 4.0 * in[x][3] + in[x][4] - 2.0
              * in[x][5] - 5.0 * in[x][6]) / 28.0;

      out[x][1] =
          (5.0 * in[x][0] + 4.0 * in[x][1] + 3 * in[x][2] + 2 * in[x][3] + in[x][4] - in[x][6]) / 14.0;

      out[x][2] =
          (7.0 * in[x][0] + 6.0 * in[x][1] + 5.0 * in[x][2] + 4.0 * in[x][3] + 3.0 * in[x][4] + 2.0
              * in[x][5] + in[x][6]) / 28.0;

      // 中间
      for (i = 3; i <= i_incount - 4; i++) {
        out[x][i] =
            (in[x][i - 3] + in[x][i - 2] + in[x][i - 1] + in[x][i] + in[x][i + 1] + in[x][i + 2] + in[x][i + 3]) / 7.0;
      }

      // 后3个点
      out[x][i_incount - 3] =
          (7.0 * in[x][i_incount - 1] + 6.0 * in[x][i_incount - 2] + 5.0 * in[x][i_incount - 3]
              + 4.0 * in[x][i_incount - 4] + 3.0 * in[x][i_incount - 5] + 2.0
              * in[x][i_incount - 6] + in[x][i_incount - 7]) / 28.0;

      out[x][i_incount - 2] =
          (5.0 * in[x][i_incount - 1] + 4.0 * in[x][i_incount - 2] + 3.0 * in[x][i_incount - 3]
              + 2.0 * in[x][i_incount - 4] + in[x][i_incount - 5] - in[x][i_incount - 7]) / 14.0;

      out[x][i_incount - 1] =
          (13.0 * in[x][i_incount - 1] + 10.0 * in[x][i_incount - 2] + 7.0 * in[x][i_incount - 3]
              + 4 * in[x][i_incount - 4] + in[x][i_incount - 5] - 2 * in[x][i_incount - 6] - 5 * in[x][i_incount - 7]) / 28.0;


      // 对前3点和后3点 增加 最大值，最小值限制
      for (i = 0; i < 3; i++) {
        // 前3点
        if (out[x][i] > hard_max) {
          out[x][i] = hard_max;
        } else if (out[x][i] < hard_min) {
          out[x][i] = hard_min;
        }

        // 后3点
        if (out[x][i_incount - i - 1] > hard_max) {
          out[x][i_incount - i - 1] = hard_max;
        } else if (out[x][i_incount - i - 1] < hard_min) {
          out[x][i_incount - i - 1] = hard_min;
        }
      }
    }
    //
    for (int y = 0; y < i_incount; y++) {
      // 前3个点
      out[0][y] =
          (13.0 * in[0][y] + 10.0 * in[1][y] + 7.0 * in[2][y] + 4.0 * in[3][y] + in[4][y] - 2.0
              * in[5][y] - 5.0 * in[6][y]) / 28.0;

      out[1][y] =
          (5.0 * in[0][y] + 4.0 * in[1][y] + 3 * in[2][y] + 2 * in[3][y] + in[4][y] - in[6][y]) / 14.0;

      out[2][y] =
          (7.0 * in[0][y] + 6.0 * in[1][y] + 5.0 * in[2][y] + 4.0 * in[3][y] + 3.0 * in[4][y] + 2.0
              * in[5][y] + in[6][y]) / 28.0;

      // 中间
      for (i = 3; i <= i_outcount - 4; i++) {
        out[i][y] =
            (in[i - 3][y] + in[i - 2][y] + in[i - 1][y]
                + in[i][y] + in[i + 1][y] + in[i + 2][y] + in[i + 3][y]) / 7.0;
      }

      // 后3个点
      out[i_incount - 3][y] =
          (7.0 * in[i_outcount - 1][y] + 6.0 * in[i_incount - 2][y] + 5.0 * in[i_incount - 3][y]
              + 4.0 * in[i_incount - 4][y] + 3.0 * in[i_incount - 5][y] + 2.0
              * in[i_incount - 6][y] + in[i_incount - 7][y]) / 28.0;

      out[i_incount - 2][y] =
          (5.0 * in[i_incount - 1][y] + 4.0 * in[i_incount - 2][y] + 3.0 * in[i_incount - 3][y]
              + 2.0 * in[i_incount - 4][y] + in[i_incount - 5][y] - in[i_incount - 7][y]) / 14.0;

      out[i_incount - 1][y] =
          (13.0 * in[i_incount - 1][y] + 10.0 * in[i_incount - 2][y] + 7.0 * in[i_incount - 3][y]
              + 4 * in[i_incount - 4][y] + in[i_incount - 5][y] - 2 * in[i_incount - 6][y] - 5 * in[i_incount - 7][y]) / 28.0;


      // 对前3点和后3点 增加 最大值，最小值限制
      for (i = 0; i < 3; i++) {
        // 前3点
        if (out[i][y] > hard_max) {
          out[i][y] = hard_max;
        } else if (out[i][y] < hard_min) {
          out[i][y] = hard_min;
        }

        // 后3点
        if (out[i_incount - i - 1][y] > hard_max) {
          out[i_incount - i - 1][y] = hard_max;
        } else if (out[i_incount - i - 1][y] < hard_min) {
          out[i_incount - i - 1][y] = hard_min;
        }
      }
    }

    return out;

  }

  public static void main(String[] args) throws Exception {
    // 初始化
    Double hard_max = 2D; // 9D
    Double hard_min = -0.4D;
    Double basevalue = 1.2D;

    String file1 = "D:\\test1.txt"; //数据
    String file2 = "D:\\test2.txt"; //基准值

    // step1.原始数据在hive中处理成三维散点图再写到hdfs中由mr处理
    // 行业、地区、日期、时刻、值

    // 地区、日期、温度

    // step2.读取文件构造二维数组。暂时不考虑行业、地区
    // 稀疏矩阵(二维数组).行业(枚举)、地区、时刻(96)、温度(45)、值。去掉日期维度
    // Map<Integer[][], List<Double>> map = new HashMap<Integer[][], List<Double>>();

    // 存放基准值. 行业-基准值
    Map<String, Double> map_basevalue = new HashMap<String, Double>();
    BufferedReader br = new BufferedReader(new FileReader(file2));// 构造一个BufferedReader类来读取文件
    String line = null;
    while ((line = br.readLine()) != null) {// 使用readLine方法，一次读一行
      String[] data = line.split(",");
      // 取值根据基准值计算
      map_basevalue.put(data[0], Double.parseDouble(data[1]));
    }
    br.close();

    // 存放二维对象类型的list二维数组。此处需要加上 行业、地区、温度、基准值、{p1,p2,p3...p96}
    // {"行业_地区": {list}}}
    Map<String, List<Double>[][]> map_data = new HashMap<String, List<Double>[][]>();

    List<Double>[][] listArr = new ArrayList[96][45];
    /*
     * for (int x = 0; x < 45; x++) { for (int y = 0; y < 96; y++) { listArr[x][y] = new
     * ArrayList<Double>(); } }
     */

    br = new BufferedReader(new FileReader(file1));// 构造一个BufferedReader类来读取文件
    line = null;
    while ((line = br.readLine()) != null) {// 使用readLine方法，一次读一行
      String[] data = line.split(",");
      String key = data[0] + "@" + data[1];
      basevalue = map_basevalue.get(data[0]);
      if (map_data.containsKey(key)) {
        listArr = map_data.get(key);
        // 取值根据基准值计算
        listArr[Integer.parseInt(data[2])][Integer.parseInt(data[3])].add(new BigDecimal((Double
            .parseDouble(data[4]) - basevalue) / basevalue).setScale(4, BigDecimal.ROUND_HALF_UP)
            .doubleValue());
        map_data.put(key, listArr);
      } else {
        // 初始化
        for (int x = 0; x < 96; x++) {
          for (int y = 0; y < 45; y++) {
            listArr[x][y] = new ArrayList<>();
          }
        }

        //listArr[Integer.parseInt(data[2])][Integer.parseInt(data[3])] = new ArrayList<>();

        listArr[Integer.parseInt(data[2])][Integer.parseInt(data[3])].add(new BigDecimal((Double
            .parseDouble(data[4]) - basevalue) / basevalue).setScale(4, BigDecimal.ROUND_HALF_UP)
            .doubleValue());
        map_data.put(key, listArr);
      }

    }
    br.close();

    // 遍历 map_data
    for (Map.Entry<String, List<Double>[][]> entry : map_data.entrySet()) {
      // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      String key = entry.getKey();
      List<Double>[][] valueArr = entry.getValue();

      // 行业
      String trade = key.split("@")[0];
      String org = key.split("@")[1];
      if (trade.equals("C3004") || trade.equals("C3005") || trade.equals("C3006")) {
        hard_max = 9D;
      }

      // 每次循环的结果
      Double[][] result = new Double[96][45];


      // 此时加上不知道为何的判断逻辑
      // 每个点rate再计算. 每个点根据 (温度+-1)*(时刻+-1) 的9个点对应的值计算。一个点可能存在多个值
      List<Double> tmp = new ArrayList<Double>();
      for (int x = 0; x < 96; x++) {
        for (int y = 0; y < 45; y++) {

          // 二维数组转一维数组
          tmp = new ArrayList<Double>();
          // 需要判断是否存在
          if (x == 0) {
            // 缺少x-1
            if (y == 0) { // 4
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x + 1][y]);
              tmp.addAll(valueArr[x][y + 1]);
              tmp.addAll(valueArr[x + 1][y + 1]);
            } else if (y == 44) { // 4
              tmp.addAll(valueArr[x][y - 1]);
              tmp.addAll(valueArr[x + 1][y - 1]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x + 1][y]);
            } else { // 6
              tmp.addAll(valueArr[x][y - 1]);
              tmp.addAll(valueArr[x + 1][y - 1]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x + 1][y]);
              tmp.addAll(valueArr[x][y + 1]);
              tmp.addAll(valueArr[x + 1][y + 1]);
            }
          } else if (x == 95) {
            // 缺少x+1
            if (y == 0) { // 4
              tmp.addAll(valueArr[x - 1][y]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x - 1][y + 1]);
              tmp.addAll(valueArr[x][y + 1]);
            } else if (y == 44) { // 4
              tmp.addAll(valueArr[x - 1][y - 1]);
              tmp.addAll(valueArr[x][y - 1]);
              tmp.addAll(valueArr[x - 1][y]);
              tmp.addAll(valueArr[x][y]);
            } else { // 6
              tmp.addAll(valueArr[x - 1][y - 1]);
              tmp.addAll(valueArr[x][y - 1]);
              tmp.addAll(valueArr[x - 1][y]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x - 1][y + 1]);
              tmp.addAll(valueArr[x][y + 1]);
            }
          } else { //9
            if (y == 0) { // 6
              tmp.addAll(valueArr[x - 1][y]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x + 1][y]);
              tmp.addAll(valueArr[x - 1][y + 1]);
              tmp.addAll(valueArr[x][y + 1]);
              tmp.addAll(valueArr[x + 1][y + 1]);
            } else if (y == 44) { // 6
              tmp.addAll(valueArr[x - 1][y - 1]);
              tmp.addAll(valueArr[x][y - 1]);
              tmp.addAll(valueArr[x + 1][y - 1]);
              tmp.addAll(valueArr[x - 1][y]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x + 1][y]);
            } else {
              tmp.addAll(valueArr[x - 1][y - 1]);
              tmp.addAll(valueArr[x][y - 1]);
              tmp.addAll(valueArr[x + 1][y - 1]);
              tmp.addAll(valueArr[x - 1][y]);
              tmp.addAll(valueArr[x][y]);
              tmp.addAll(valueArr[x + 1][y]);
              tmp.addAll(valueArr[x - 1][y + 1]);
              tmp.addAll(valueArr[x][y + 1]);
              tmp.addAll(valueArr[x + 1][y + 1]);
            }
          }
          // ...
          Double d_max = Double.MIN_VALUE;
          Double d_min = Double.MAX_VALUE;
          Double d_sum = 0D;
          int i_count = 0;
          Double d_avg = 0D;

          if (tmp.size() < 1) {// 此种情况不计算
            d_avg = null;
          } else {
            // 遍历获取最大，最小值
            for (Double d : tmp) {
              if (d > d_max) {
                d_max = d;
              }
              if (d < d_min) {
                d_min = d;
              }
              d_sum = d_sum + d;
              i_count++;
            }

            // 计算平均值
            d_avg = d_sum / i_count;
            // 再次计算平均值
            if (d_avg > 0.2) {
              d_max = d_avg * 3;
              d_min = d_avg / 3;
            } else if (d_avg > 0.1) {
              d_max = d_avg + 0.1;
              d_min = d_avg - 0.1;
            } else if (d_avg > -0.1) {
              d_max = d_avg + 0.08;
              d_min = d_avg - 0.08;
            } else if (d_avg > -0.2) {
              d_max = d_avg + 0.1;
              d_min = d_avg - 0.1;
            } else {
              d_max = d_avg / 3;
              d_min = d_avg * 3;
            }

            // 再次遍历，去除远点
            d_sum = 0D;
            i_count = 0;
            for (Double d : tmp) {
              if (d <= d_max && d >= d_min) {
                if (d > d_max) {
                  d_max = d;
                }
                if (d < d_min) {
                  d_min = d;
                }
                d_sum = d_sum + d;
                i_count++;
              }
            }

            if (i_count >= 1) {
              d_avg = d_sum / i_count;

              if (d_avg > hard_max) {
                d_avg = hard_max;// 根据 行业不同,固定9或2
              } else if (d_avg < hard_min) {
                d_avg = hard_min;// 固定-0.4
              }
            } else {
              d_avg = null; // 此种情况不计算
            }

            result[x][y] = d_avg;
          }
        }
      } // end of for (int x = 0; x < 96; x++) {

      // 遍历result

/*       for(int x=0;x<result.length;x++) { for(int y=0;y<result[x].length;y++) {
       System.out.println("key="+key+","+x+","+y+"="+result[x][y]); } }*/


      // 阶段1结束。阶段2 七点曲面平滑拟合 (作用: 三维散点坐标拟合到平滑曲面)
      int i_outcount = result.length;
      //int i_incount = result[0].length;

      // 修复数组
      for (int x = 0; x < i_outcount; x++) {
        repairArray(result[x]);
      }
      // 七点曲面平滑拟合

      //时刻、温度、值
      Double[][] r = linearSmooth7(result, hard_max, hard_min);

      //输出结果. 行业、地区、时刻、温度、值
      System.out.println(String.format("%s %s %s %s %s", trade, org, 0, 0, r[0][0]));
/*      for(int x=0;x<r.length;x++) {
        for(int y=0;y<r[x].length;y++) {
          System.out.println(String.format("%s %s %s %s %s", trade, org, x, y, r[x][y]));
        }
      }*/
    }
  } // end if main
}
