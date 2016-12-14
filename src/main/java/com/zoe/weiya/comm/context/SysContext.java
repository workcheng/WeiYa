package com.zoe.weiya.comm.context;

/**
 * 
 * @author Chenghui
 * @date It is a class to take care of the some context in threadlocal.
 */
public class SysContext {
  private static ThreadLocal<String> trackingId = new ThreadLocal<String>();

  public static void setTrackingId(String value) {
    trackingId.set(value);
  }

  public static String getTrackingId() {
    return trackingId.get();
  }
}
