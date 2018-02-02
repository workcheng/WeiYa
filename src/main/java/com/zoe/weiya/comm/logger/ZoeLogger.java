package com.zoe.weiya.comm.logger;

import com.zoe.weiya.comm.context.SysContext;
import org.slf4j.Logger;

/**
 * 
 * @author Chenghui
 * @date It wrap a logger class to append trackingID into the log file.
 */

public class ZoeLogger {

  private Logger log;

  ZoeLogger(Logger log) {
    this.log = log;
  }

  public void error(String message, Object e) {
    log.error(buildTrackingIdInMessage(message), e);
  }

  public void error(String message) {
    log.error(buildTrackingIdInMessage(message));
  }

  public void info(String message) {
    log.info(buildTrackingIdInMessage(message));
  }

  public void debug(String message) {
    log.debug(buildTrackingIdInMessage(message));
  }

  private String buildTrackingIdInMessage(String message) {
    return "[TrackingId = " + SysContext.getTrackingId() + "] " + message;
  }
}
