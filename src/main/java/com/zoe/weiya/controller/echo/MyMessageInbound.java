package com.zoe.weiya.controller.echo;

/**
 * Created by andy on 2017/1/4.
 */

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.responseModel.ZoeMessage;
import com.zoe.weiya.util.JacksonJsonUtil;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@SuppressWarnings("deprecation")
public class MyMessageInbound extends MessageInbound {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(MyMessageInbound.class);
    private ServletContext application;
    private Set<MyMessageInbound> connections = null;
    private WsOutbound wsOutbound;

    @SuppressWarnings("unchecked")
    public MyMessageInbound(ServletContext application) {
        this.application = application;
        connections = (Set<MyMessageInbound>) application.getAttribute("connections");
        if (connections == null) {
            connections = new CopyOnWriteArraySet<MyMessageInbound>();
        }
    }

    @Override
    protected void onOpen(WsOutbound outbound){
        log.info("Open Client.");
        this.wsOutbound = outbound;
        try {
            ZoeMessage zoeMessage = new ZoeMessage();
            zoeMessage.setContent("Hello World");
            zoeMessage.setHeadImgUrl("http://static.clewm.net/cli/images/beautify/logo/icon1.png");
            outbound.writeTextMessage(CharBuffer.wrap(JacksonJsonUtil.beanToJson(zoeMessage)));
        } catch (IOException e) {
            log.error("error",e);
            e.printStackTrace();
        }
        connections.add(this);
        application.setAttribute("connections", connections);
    }

    @Override
    protected void onClose(int status) {
        log.info("Close Client.");
        connections.remove(this);
        application.setAttribute("connections", connections);
    }

    @Override
    protected void onBinaryMessage(ByteBuffer message) throws IOException {
        log.error("error" ,message);
        throw new UnsupportedOperationException("message not supported.");
    }

    @Override
    protected void onTextMessage(CharBuffer message) throws IOException {
        log.info("Accept Message : " + message);
        Set<MyMessageInbound> connections =
                (Set<MyMessageInbound>) application.getAttribute("connections");
        for (MyMessageInbound myMessageInbound : connections){
            myMessageInbound.wsOutbound.writeTextMessage(message);
            myMessageInbound.wsOutbound.flush();
        }
    }

    public void sendMessage(CharBuffer message) throws IOException {
        CharBuffer buffer = CharBuffer.wrap(message);
        wsOutbound.writeTextMessage(buffer);
        wsOutbound.flush();
    }

}
