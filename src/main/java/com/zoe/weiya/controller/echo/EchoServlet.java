package com.zoe.weiya.controller.echo;

/**
 * Created by andy on 2017/1/4.
 */

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("deprecation")
// 处理WebSocket的Servlet需要继承自WebSocketServlet
public class EchoServlet extends WebSocketServlet {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(EchoServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    public StreamInbound createWebSocketInbound(String subProtocol,HttpServletRequest request) {
        ServletContext application = this.getServletContext();
        log.info("EchoServlet");
        return new MyMessageInbound(application);
    }

}