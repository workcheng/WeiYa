/**
 * @Copyright 0gmi
 * @Author Bane.Shi 2015年6月29日-下午1:22:17
 */
package com.workcheng.weiya.common.utils;

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
            log.error("error", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
        return properties;
    }

    public static String get(String filePath, String key) {
        Properties properties = PropertiesUtil.readProperties(filePath);
        return properties.getProperty(key);
    }

    public static String getMenuJson(WeiYaConfig weiYaConfig) {
        String menu = "'{'\"menu\":{0}'}'";
        return MessageFormat.format(menu, weiYaConfig.getMenuJson());
    }
}
