package com.grandata.www.grandc.java;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class DataWritable implements Writable {

  private int temperature;
  private int point;
  private double val;
  private double basevalue;

  public DataWritable() {
  }

  public void set(int temperature, int point, double val, double basevalue) {
      this.temperature = temperature;
      this.point = point;
      this.val = val;
      this.basevalue = basevalue;
  }

  public int getTemperature() {
    return temperature;
  }

  public int getPoint() {
    return point;
  }

  public double getVal() {
    return val;
  }

  public double getBasevalue() {
    return basevalue;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
      this.temperature = in.readInt();
      this.point = in.readInt();
      this.val = in.readDouble();
      this.basevalue = in.readDouble();
  }

  @Override
  public void write(DataOutput out) throws IOException {
      out.writeInt(temperature);
      out.writeInt(point);
      out.writeDouble(val);
      out.writeDouble(basevalue);
  }

  @Override
  public String toString() {
      return temperature + "\t" + point + "\t" + val + "\t" + basevalue;
  }
}
