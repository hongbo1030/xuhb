package com.grandata.www.grandc.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行业电量+温度模型
 * @author grandata
 *
 */
public class TradeElecWeatherModel {

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
    int array = 45;//温度

    String tradecode = "";
    String orgno = "";
    String energy_type = "";
    String factor_type = "";
    Double hard_max = 2D; // 9D
    Double hard_min = -0.9D;
    Double basevalue = 1D; //后面会修改

    String path = args[0]; //文件夹

    FileWriter fw = null;
    String createfile = "";
    if(path.endsWith("/")) {
      createfile = path+"createfile.txt";
    } else {
      createfile = path+"/"+"createfile.txt";
    }
    //写文件
    fw = new FileWriter(createfile);

    // 存放二维对象类型的list二维数组。此处需要加上  时间、行业、地区、energy_type、factor_type、基准值、上限、下限、温度、电量
    // {"行业@地区@energy_type@factor_type@上限@下限": {list}}}
    Map<String, List<Double>[]> map_data = new HashMap<String, List<Double>[]>();
    //温度(45)
    List<Double>[] listArr = new ArrayList[array];

    File dir = new File(path);
    File[] files = dir.listFiles();
    for (File file : files) {
      BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
      String line = null;
      while ((line = br.readLine()) != null) {// 使用readLine方法，一次读一行
        String[] data = line.split(",");
        String key = data[1] + "@" + data[2] + "@" + data[3] + "@" + data[4] + "@" + data[6] + "@" + data[7];
        basevalue = Double.valueOf(data[5]); // 基准值
        if (map_data.containsKey(key)) {
          listArr = map_data.get(key);
          // 取值根据基准值计算
          try {
            listArr[Integer.parseInt(data[8])].add(new BigDecimal(
                (Double.parseDouble(data[9]) - basevalue) / basevalue).setScale(4,
                BigDecimal.ROUND_HALF_UP).doubleValue());
          } catch (Exception e) {
            listArr[Integer.parseInt(data[8])].add(new BigDecimal((0 - basevalue) / basevalue)
                .setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
          }

          map_data.put(key, listArr);
        } else {
          // 初始化。温度
          for (int x = 0; x < array; x++) {
            listArr[x] = new ArrayList<>();
          }

          try {
            listArr[Integer.parseInt(data[8])].add(new BigDecimal(
                (Double.parseDouble(data[9]) - basevalue) / basevalue).setScale(4,
                BigDecimal.ROUND_HALF_UP).doubleValue());
          } catch (Exception e) {
            listArr[Integer.parseInt(data[8])].add(new BigDecimal((0 - basevalue) / basevalue)
                .setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
          }
          map_data.put(key, listArr);
        }
      }
      br.close();
    }

    // 遍历 map_data
    for (Map.Entry<String, List<Double>[]> entry : map_data.entrySet()) {
      // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      String key = entry.getKey();
      String[] keyArray = key.split("@", 6);
      List<Double>[] valueArr = entry.getValue();

      // 行业
      tradecode = keyArray[0];
      orgno = keyArray[1];
      energy_type = keyArray[2];
      factor_type = keyArray[3];
      hard_max = Double.parseDouble(keyArray[4]);
      hard_min = Double.parseDouble(keyArray[5]);

      // 每次循环的结果
      Double[] result = new Double[array];
      // 此时加上不知道为何的判断逻辑
      // 每个点rate再计算. 每个点根据 (湿度+-1)*(温度+-1) 的9个点对应的值计算。一个点可能存在多个值
      List<Double> tmp = new ArrayList<Double>();
      for (int x = 0; x < array; x++) {
          // 二维数组转一维数组
          tmp = new ArrayList<Double>();
          // 需要判断是否存在
          if (x == 0) {
            // 缺少x-1
            tmp.addAll(valueArr[x]);
            tmp.addAll(valueArr[x + 1]);
          } else if (x == array-1) {
            // 缺少x+1
            tmp.addAll(valueArr[x - 1]);
            tmp.addAll(valueArr[x]);
          } else { //3
            tmp.addAll(valueArr[x - 1]);
            tmp.addAll(valueArr[x]);
            tmp.addAll(valueArr[x + 1]);
          }

          // ...
          Double d_max = Double.MIN_VALUE;
          Double d_min = Double.MAX_VALUE;
          Double d_sum = 0D;
          int i_count = 0;
          Double d_avg = 0D;

          if (tmp.size() < 5) {// 此种情况不计算
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

            if (i_count >= 5) {
              d_avg = d_sum / i_count;

              if (d_avg > hard_max) {
                d_avg = hard_max;// 根据 行业不同,固定9或2
              } else if (d_avg < hard_min) {
                d_avg = hard_min;// 固定-0.4
              }
            } else {
              d_avg = null; // 此种情况不计算
            }

            result[x] = d_avg;
          }
      } // for (int x = 0; x < outArray; x++) {

      // 遍历result

      // 阶段1结束。阶段2 七点曲面平滑拟合 (作用: 三维散点坐标拟合到平滑曲面)
      // 修复数组
      repairArray(result);

      // 七点曲面平滑拟合

      //温度、电量
      Double[] r = linearSmooth7(result, hard_max, hard_min);

      //输出结果. 行业、地区、时刻、温度、值
      //System.out.println(String.format("%s %s %s %s %s", trade, org, 0, 0, r[0][0]));
/*      for(int x=0;x<r.length;x++) {
        for(int y=0;y<r[x].length;y++) {
          System.out.println(String.format("%s %s %s %s", trade, org, (x-1)*45+y, r[x][y]));
        }
      }*/

      //energy_type,factor_type,行业,地区,factor_id,value
      for(int x=0;x<r.length;x++) {
        fw.write(String.format("%s,%s,%s,%s,%s,%s"+System.lineSeparator(), energy_type, factor_type, tradecode, orgno, x+1, r[x]));
      }
    }

    fw.close();
  } // end if main
}
