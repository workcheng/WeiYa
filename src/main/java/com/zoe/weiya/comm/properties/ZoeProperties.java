/**
 * @Copyright 0gmi
 * @Author Bane.Shi 2015年6月29日-下午1:22:17
 */
package com.zoe.weiya.comm.properties;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Chenghui
 *
 */
public class ZoeProperties {

  private static ZoeLogger log = ZoeLoggerFactory.getLogger(ZoeProperties.class);

  public static Properties readProperties(String filePath) throws Exception {
    Properties properties = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = null;
    if (filePath.startsWith("classpath:")) {
      inputStream = loader.getResourceAsStream(filePath.substring(10));
    } else {
      inputStream = loader.getResourceAsStream(filePath);
    }
    properties.load(inputStream);
    inputStream.close();
    return properties;
  }

  public static String get(String filePath, String key) throws Exception {
    Properties properties = ZoeProperties.readProperties(filePath);
    return properties.getProperty(key);
  }

  public static int getTaHoNumber() {
    int defalutValue = 10;
    try {
      defalutValue = Integer.valueOf(get("config/algorithm/config.properties", "joho.match.tahos.top"));
    } catch (Exception e) {
      log.error("System error", e);
    }
    return defalutValue;
  }

  public static int getJoHoNumber() {
    int defalutValue = 10;
    try {
      defalutValue = Integer.valueOf(get("config/algorithm/config.properties", "taho.match.johos.top"));
    } catch (Exception e) {
      log.error("System error", e);
    }
    return defalutValue;
  }

  public static int getServerNumber() {
    int defalutValue = 1;
    try {
      defalutValue = Integer.valueOf(get("config/algorithm/config.properties", "server.number"));
    } catch (Exception e) {
      log.error("System error", e);
    }
    return defalutValue;
  }

  public static float getPreferredEduLevel() {
    float defalutValue = 0.9f;
    try {
      defalutValue = Float
          .valueOf(get("config/algorithm/config.properties", "preferred.education.degree.level.weight"));
    } catch (Exception e) {
      log.error("System error", e);
    }
    return defalutValue;
  }

}
