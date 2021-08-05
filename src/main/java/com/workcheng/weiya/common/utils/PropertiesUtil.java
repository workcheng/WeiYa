/**
 * @Copyright 0gmi
 * @Author Bane.Shi 2015年6月29日-下午1:22:17
 */
package com.workcheng.weiya.common.utils;

import com.workcheng.weiya.common.dto.MyDate;
import com.workcheng.weiya.common.config.WeiYaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author Chenghui
 */
public class PropertiesUtil {
    private static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties readProperties(String filePath) {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = null;
        if (filePath.startsWith("classpath:")) {
            inputStream = loader.getResourceAsStream(filePath.substring(10));
        } else {
            inputStream = loader.getResourceAsStream(filePath);
        }
        try {
            assert inputStream != null;
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("error:{}", e.toString());
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                    loader = null;
                } catch (IOException e) {
                    log.error("error:{}", e.toString());
                    e.printStackTrace();
                }
            }

        }
        return properties;
    }

    public static String get(String filePath, String key) {
        Properties properties = PropertiesUtil.readProperties(filePath);
        String property = properties.getProperty(key);
        properties = null;
        return property;
    }

    public static int getServerNumber() {
        int defaultValue = 1;
        try {
            defaultValue = Integer.parseInt(get("algorithm/config.properties", "server.number"));
        } catch (Exception e) {
            log.error("System error", e);
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static MyDate getStartTime() {
        int startYear = 2017;
        int startMonth = 1;
        int startDay = 22;
        startYear = Integer.parseInt(get("static/static.properties", "start.year"));
        startMonth = Integer.parseInt(get("static/static.properties", "start.month"));
        startDay = Integer.parseInt(get("static/static.properties", "start.day"));
        return new MyDate(startYear, startMonth, startDay, null, null, null);
    }

    public static MyDate getStartTime(WeiYaConfig weiYaConfig) {
        int startYear = 2017;
        int startMonth = 1;
        int startDay = 22;
        startYear = Integer.parseInt(weiYaConfig.getStartYear());
        startMonth = Integer.parseInt(weiYaConfig.getStartMonth());
        startDay = Integer.parseInt(weiYaConfig.getStartDay());
        return new MyDate(startYear, startMonth, startDay, null, null, null);
    }

    public static int getNoonHour(WeiYaConfig weiYaConfig) {
        return Integer.parseInt(weiYaConfig.getNoon());
    }

    public static int getOffWorkHour(WeiYaConfig weiYaConfig) {
        return Integer.parseInt(weiYaConfig.getOffWork());
    }

    public static String getMenuJson() {
        String menu = "'{'\"menu\":{0}'}'";
        return MessageFormat.format(menu, get("static/static.properties", "menu.json"));
    }
}
