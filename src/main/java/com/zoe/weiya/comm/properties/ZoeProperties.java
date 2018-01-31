/**
 * @Copyright 0gmi
 * @Author Bane.Shi 2015年6月29日-下午1:22:17
 */
package com.zoe.weiya.comm.properties;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.ZoeDate;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author Chenghui
 *
 */
public class ZoeProperties {

  private static ZoeLogger log = ZoeLoggerFactory.getLogger(ZoeProperties.class);

  public static Properties readProperties(String filePath)  {
    Properties properties = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = null;
    if (filePath.startsWith("classpath:")) {
      inputStream = loader.getResourceAsStream(filePath.substring(10));
    } else {
      inputStream = loader.getResourceAsStream(filePath);
    }
    try {
      properties.load(inputStream);
    } catch (IOException e) {
        log.error("error",e.toString());
        e.printStackTrace();
    }finally {
      if(null != inputStream){
        try {
            inputStream.close();
            if(null != loader){
                loader = null;
            }
        } catch (IOException e) {
            log.error("error",e.toString());
            e.printStackTrace();
        }
      }

    }
    return properties;
  }

  public static String get(String filePath, String key) {
    Properties properties = ZoeProperties.readProperties(filePath);
      String property = properties.getProperty(key);
      properties = null;
      return property;
  }

  public static int getServerNumber() {
    int defaultValue = 1;
    try {
      defaultValue = Integer.valueOf(get("algorithm/config.properties", "server.number"));
    } catch (Exception e) {
        log.error("System error", e);
        e.printStackTrace();
    }
    return defaultValue;
  }

    public static ZoeDate getStartTime(){
        int startYear = 2017;
        int startMonth = 1;
        int startDay = 22;
        startYear = Integer.valueOf(get("static/static.properties", "start.year"));
        startMonth = Integer.valueOf(get("static/static.properties", "start.month"));
        startDay = Integer.valueOf(get("static/static.properties", "start.day"));
        ZoeDate zoeDate = new ZoeDate(startYear,startMonth,startDay,null,null,null);
        return zoeDate;
    }

    public static int getNoonHour(){
        return Integer.valueOf(get("static/static.properties", "zoe.date.noon"));
    }
    public static int getOffWorkHour(){
        return Integer.valueOf(get("static/static.properties", "zoe.date.off.work"));
    }
    public static String getMenuJson(){
        String menu = "'{'\"menu\":{0}'}'";
        return MessageFormat.format(menu,get("static/static.properties", "menu.json"));
    }
}
