package com.grandata.www.grandc.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * 定义一个AvgScore 求学生的平均值 要实现一个Tool 工具类，是为了初始化一个hadoop配置实例
 */
public class AvgScore {

  public static final Logger log = LoggerFactory.getLogger(AvgScore.class);


  public static class MyMap extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text mapOutputKey = new Text();
    private IntWritable intWritable = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException,
        InterruptedException {
      String stuInfo = value.toString();// 将输入的纯文本的数据转换成String
      System.out.println("studentInfo:" + stuInfo);
      log.info("MapSudentInfo:" + stuInfo);

      String[] tokenizer = (stuInfo).split(",");
      String name = tokenizer[0];// 学生姓名
      int score = Integer.parseInt(tokenizer[1]);// 学生成绩

      log.info("MapStu:" + name + " " + score);

      mapOutputKey.set(name);
      intWritable.set(score);

      context.write(mapOutputKey, intWritable);// 输出学生姓名和成绩
    }

  }

  public static class MyReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    private IntWritable intWritable = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      int count = 0;
      for (IntWritable value : values) {
        sum += value.get();// 计算总分
        count++;// 统计总科目
      }
      int avg = sum / count;
      intWritable.set(avg);
      context.write(key, intWritable);// 输出学生姓名和平均值
    }

  }


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    Job job = Job.getInstance(conf, "AvgScore");

    job.setJarByClass(AvgScore.class); // 设置运行jar中的class名称

    job.setMapperClass(MyMap.class);// 设置mapreduce中的mapper reducer combiner类
    job.setReducerClass(MyReduce.class);
    job.setCombinerClass(MyReduce.class);

    job.setOutputKeyClass(Text.class); // 设置输出结果键值对类型
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));// 设置mapreduce输入输出文件路径
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
