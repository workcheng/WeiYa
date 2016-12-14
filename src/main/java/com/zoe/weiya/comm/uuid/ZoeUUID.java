/**
 * @Copyright 0gmi
 * @Author Bane.Shi 2015年5月22日-下午5:04:54
 */
package com.zoe.weiya.comm.uuid;

import java.util.UUID;
/**
 * @author ChengHui
 *
 */
public abstract class ZoeUUID {
  public static String random() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  public static String random(String prefix) {
    return prefix + UUID.randomUUID().toString().replaceAll("-", "");
  }
}
