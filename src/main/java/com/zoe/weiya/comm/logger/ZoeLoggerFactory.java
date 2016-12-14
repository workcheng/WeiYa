package com.zoe.weiya.comm.logger;

import org.slf4j.LoggerFactory;

/**
 * 
 * @author Chenghui
 * @date It wrap slf4j factory to create our log class.
 */
public class ZoeLoggerFactory {

  public static ZoeLogger getLogger(Class<?> clazz) {
    ZoeLogger logger = new ZoeLogger(LoggerFactory.getLogger(clazz));
    return logger;
  }
}
