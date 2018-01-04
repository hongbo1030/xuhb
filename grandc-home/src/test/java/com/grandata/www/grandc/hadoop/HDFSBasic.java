package com.grandata.www.grandc.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import com.grandata.www.grandc.common.util.HttpRequestUtils;

public class HDFSBasic {

  public static String httpfsurl = "http://192.168.10.205:14000/webhdfs/v1";

  // 创建目录
  public static void mkdir(String user, String op, String path) {
    // curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=MKDIRS[&permission=<OCTAL>]"
    String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 创建文件
  // step1: 请求 namenode(active或standby均可) 返回307, redirect Location
  // step2: 请求step1返回的Location。其实是datanode地址
  public static void create(String user, String op, String path) {
    // step1. curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=CREATE[&permission=<OCTAL>]"
    // step2. "http://grandata205:50075/webhdfs/v1/user/xuhb/c47.txt?op=CREATE&user.name=hdfs&namenoderpcaddress=zktest&createflag=&createparent=true&overwrite=false"
    String url = String.format("%s%s?user.name=%s&op=%s", "http://192.168.10.205:50070/webhdfs/v1", path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 重命名文件。可能支持移动文件
  public static void rename(String user, String op, String path, String destination) {
    // curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=RENAME&destination=<PATH>"
    String url = String.format("%s%s?user.name=%s&op=%s&destination=%s", httpfsurl, path, user, op, destination);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 删除文件或目录
  public static void delete(String user, String op, String path) {
    delete(user, op, path, false);
  }

  //recursive=<true |false>.recursive=false 只有空目录才能删除
  public static void delete(String user, String op, String path, boolean recursive) {
    // curl -i -X DELETE "http://<host>:<port>/webhdfs/v1/<path>?op=DELETE[&recursive=<true |false>]"
    String url = String.format("%s%s?user.name=%s&op=%s&recursive=%s", httpfsurl, path, user, op, recursive);
    JSONObject jsonResult = HttpRequestUtils.httpDelete(url);
    System.out.println(jsonResult);
  }

  //查看列表
  public static void liststatus(String user, String op, String path) {
    // curl -i "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=LISTSTATUS"
    String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpGet(url);
    System.out.println(jsonResult);
  }

  // 不知道如何调用。只看出是分页
  public static void liststatus_batch(String user, String op, String path, String startAfter) {
    // curl -i "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=LISTSTATUS_BATCH&startAfter=<CHILD>"
    String url =
        String.format("%s%s?user.name=%s&op=%s&startAfter=%s", httpfsurl, path, user, op, startAfter);
    JSONObject jsonResult = HttpRequestUtils.httpGet(url);
    System.out.println(jsonResult);
  }

  // 获取目录大小。不知道是实际大小还是多副本大小，需要测试
  public static void getcontentsummary(String user, String op, String path) {
    // curl -i "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=GETCONTENTSUMMARY"
    String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpGet(url);
    System.out.println(jsonResult);
  }

  // check file
  public static void getfilechecksum(String user, String op, String path) {
    // curl -i "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=GETFILECHECKSUM"
    String url = String.format("%s%s?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpGet(url);
    System.out.println(jsonResult);
  }

  // 获取用户在hdfs的家路径
  public static void gethomedirectory(String user, String op) {
    // curl -i "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=GETHOMEDIRECTORY"
    String url = String.format("%s/?user.name=%s&op=%s", httpfsurl, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpGet(url);
    System.out.println(jsonResult);
  }

  // 设置目录或文件的 owner,group
  public static void setowner(String user, String op, String path, String owner, String group) {
    // curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=SETOWNER[&owner=<USER>][&group=<GROUP>]"
    String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
    if (StringUtils.isNotBlank(owner)) {
      url = url + "&owner=" + owner;
    }
    if (StringUtils.isNotBlank(group)) {
      url = url + "&group=" + group;
    }
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  //设置目录或文件的rwx权限
  public static void setpermission(String user, String op, String path, String permission) {
    // curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=SETPERMISSION[&permission=<OCTAL>]"
    String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
    if (StringUtils.isNotBlank(permission)) {
      url = url + "&permission=" + permission;
    }
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 获取目录或文件的acl权限
  public static void getaclstatus(String user, String op, String path) {
    // curl -i "http://192.168.10.205:14000/webhdfs/v1/user/xuhb/a1.txt/?user.name=hdfs&op=GETACLSTATUS"
    // {"AclStatus":{"owner":"xuhb","group":"hdfs","stickyBit":false,"entries":["user:foo:rw-","user:foo3:rw-","group::r--"]}}
    String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 设置目录或文件的acl权限. aclspec必须包含user,group,other
  public static void setaclstatus(String user, String op, String path, String aclspec) {
    // curl -i -X PUT "http://192.168.10.205:14000/webhdfs/v1/user/xuhb/a1.txt?user.name=hdfs&op=SETACL&aclspec=user:foo:rw-,user::rwx,group::---,other::---"
    // 1.getfacl.需要先获取 entries.aclspec 中遍历user
    String url = String.format("%s%s/?user.name=%s&op=%s&aclspec=%s", httpfsurl, path, user, op, aclspec);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 移除默认acl。未发现作用
  public static void removedefaultacl(String user, String op, String path) {
    // curl -i -X PUT "http://192.168.10.205:14000/webhdfs/v1/user/xuhb/a1.txt?user.name=hdfs&op=REMOVEDEFAULTACL"
    String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 删除目录或文件的全部acl
  public static void removeacl(String user, String op, String path) {
    // curl -i -X PUT "http://192.168.10.205:14000/webhdfs/v1/user/xuhb/a1.txt?user.name=hdfs&op=REMOVEACL"
    String url = String.format("%s%s/?user.name=%s&op=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  // 删除目录或文件的某个用户的acl。aclspec中user的权限不管写什么，都会把该用户的全部acl权限删除
  public static void removeaclentries(String user, String op, String path, String aclspec) {
    // curl -i -X PUT "http://192.168.10.205:14000/webhdfs/v1/user/xuhb/a1.txt?user.name=hdfs&op=REMOVEACLENTRIES&aclspec=user:foo:rwx"
    String url = String.format("%s%s/?user.name=%s&op=%s&aclspec=%s", httpfsurl, path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  //上传文件
  public static void putfile(String user, String op, String path, String localFile) {
    // step1. curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=CREATE[&permission=<OCTAL>]"
    // step2. curl -i -X PUT -T <file> "http://grandata205:50075/webhdfs/v1/user/xuhb/c47.txt?op=CREATE&user.name=hdfs&namenoderpcaddress=zktest&createflag=&createparent=true&overwrite=false"
    //String url = String.format("%s%s?namenoderpcaddress=zktest&createflag=&createparent=true&overwrite=false&user.name=%s&op=%s", "http://192.168.10.205:50075/webhdfs/v1", path, user, op);
    String url = String.format("%s%s?user.name=%s&op=%s", "http://192.168.10.205:50070/webhdfs/v1", path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPutFile(url, localFile);
    System.out.println(jsonResult);
  }

  public static void putfile2(String user, String local, String remote) {
      Configuration conf = new Configuration();
      //conf.addResource("core-site.xml");
      //conf.addResource("hdfs-site.xml");
      conf.addResource("C:/Users/ThinkPad/Downloads/hdfs-site.xml");
      conf.addResource("C:/Users/ThinkPad/Downloads/core-site.xml");
      FileSystem fs = null;
      try {
        URI uri = URI.create("hdfs://zktest");
        fs = FileSystem.get(uri, conf, user);
        fs.copyFromLocalFile(new Path(local), new Path(remote));
        System.out.println("copy from: " + local + " to " + remote);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        try {
          fs.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
  }

  //流拷贝
  public static void putbyte(String user, String op, String path, byte[] bytes) throws IOException {
    // step1. curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=CREATE[&permission=<OCTAL>]"
    // step2. curl -i -X PUT -T <file> "http://grandata205:50075/webhdfs/v1/user/xuhb/c47.txt?op=CREATE&user.name=hdfs&namenoderpcaddress=zktest&createflag=&createparent=true&overwrite=false"
    String url = String.format("%s%s?namenoderpcaddress=zktest&createflag=&createparent=true&overwrite=false&user.name=%s&op=%s", "http://192.168.10.205:50075/webhdfs/v1", path, user, op);
    JSONObject jsonResult = HttpRequestUtils.httpPutBytes(url, bytes);
    System.out.println(jsonResult);
  }


  //source会被删除
  public static void concat(String user, String op, String path, String source) {
    // curl -i -X POST "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=CONCAT&sources=<PATHS>"
    String url = String.format("%s%s/?user.name=%s&op=%s&sources=%s", httpfsurl, path, user, op, source);
    JSONObject jsonResult = HttpRequestUtils.httpPost(url);
    System.out.println(jsonResult);
  }

  //复制文件： hdfs没有直接的api实现
  public static void copy(String user, String op, String path, String source) {
    //step1. download
    //step2. upload
    String local = "D:\\1.txt";
    Configuration conf = new Configuration();
    conf.addResource("core-site.xml");
    conf.addResource("hdfs-site.xml");
    FileSystem fs = null;
    try {
      fs = FileSystem.get(new URI(conf.get("fs.defaultFS")), conf, user);
      fs.copyToLocalFile(false, new Path(source), new Path(local), true);
      //fs.copyToLocalFile(new Path(source), new Path(local));
      fs.copyFromLocalFile(new Path(local), new Path(path));
      System.out.println("copy from: " + source + " to " + path);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } finally {
      try {
        fs.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //CREATESYMLINK 调用失败
  public static void createsymlink(String user, String op, String path, String destination) {
    // curl -i -X PUT "http://<HOST>:<PORT>/webhdfs/v1/<PATH>?op=CREATESYMLINK&destination=<PATH>[&createParent=<true |false>]"
    String url = String.format("%s%s/?user.name=%s&op=%s&destination=%s", httpfsurl, path, user, op, destination);
    JSONObject jsonResult = HttpRequestUtils.httpPut(url);
    System.out.println(jsonResult);
  }

  //java process
  public static String readConsole(String cmd, Boolean isPrettify) throws IOException {
    StringBuffer cmdout = new StringBuffer();
    System.out.println("执行命令：" + cmd);
    Process process = Runtime.getRuntime().exec(cmd);     //执行一个系统命令
    InputStream fis = process.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line = null;
    if (isPrettify == null || isPrettify) {
            while ((line = br.readLine()) != null) {
                    cmdout.append(line);
            }
    } else {
            while ((line = br.readLine()) != null) {
                    cmdout.append(line).append(System.getProperty("line.separator"));
            }
    }
    System.out.println("执行系统命令后的结果为：\n" + cmdout.toString());
    return cmdout.toString().trim();
  }

  //string to bytes
  public static byte[] strToByteArray(String str) {
    if (str == null) {
        return null;
    }
    byte[] byteArray = str.getBytes();
    return byteArray;
}

  //bytes to string
  public static String byteArrayToStr(byte[] byteArray) {
    if (byteArray == null) {
        return null;
    }
    String str = new String(byteArray);
    return str;
}

  public static void main(String[] args) {
    //mkdir("hdfs", "MKDIRS", "/user/xuhb/d");
    //create("hdfs", "CREATE", "/user/xuhb/a311.txt");
    //rename("hdfs", "RENAME", "/user/xuhb/c", "/user/xuhb/c1" );
    //delete("xuhb", "DELETE", "/user/xuhb/d", false );
    //liststatus("hdfs", "LISTSTATUS", "/");
    //liststatus_batch("hdfs", "LISTSTATUS_BATCH", "/", "apps");  //�д�
    //getcontentsummary("hdfs", "GETCONTENTSUMMARY", "/user/xuhb");
    //getfilechecksum("hdfs", "GETFILECHECKSUM", "/user/xuhb/data.txt");
    //gethomedirectory("abc1", "GETHOMEDIRECTORY");
    //setowner("hdfs", "SETOWNER", "/user/xuhb/a2.txt", "xuhb", null);
    //setpermission("xuhb", "SETPERMISSION", "/user/xuhb/a2.txt", "700");

    //getaclstatus("xuhb", "GETACLSTATUS", "/user/xuhb/a2.txt");
    //setaclstatus("xuhb", "SETACL", "/user/xuhb/a2.txt", "user:foo:rw-,user::rwx,group::---,other::---");
    //removedefaultacl("xuhb", "REMOVEDEFAULTACL", "/user/xuhb/a2.txt");
    //removeacl("xuhb", "REMOVEACL", "/user/xuhb/a2.txt");
    //removeaclentries("xuhb", "REMOVEACL", "/user/xuhb/a2.txt", "user:foo:rwx");

    //putfile("hdfs", "CREATE", "/user/xuhb/c12.txt", "D:\\中文.txt");
    //putbyte("hdfs", "CREATE", "/user/xuhb/c11.txt", "中文123".getBytes());
    putfile2("xuhb", "D:\\中文.txt", "/user/xuhb/c13.txt");

    //concat("hdfs", "CONCAT", "/user/xuhb/c12.txt", "/user/xuhb/c8.txt");
    //copy("hdfs", "COPY", "/user/xuhb/c9.txt", "/user/xuhb/c12.txt");

    //createsymlink("hdfs", "CREATESYMLINK", "/user/xuhb/c12.txt", "/user/xuhb/l12.txt");
  }
}
