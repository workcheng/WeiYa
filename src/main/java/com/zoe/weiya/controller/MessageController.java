package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.controller.echo.MyMessageInbound;
import com.zoe.weiya.model.LuckyUser;
import com.zoe.weiya.service.message.WechatService;
import com.zoe.weiya.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Set;

/**
 * Created by andy on 2016/12/30.
 */
@RequestMapping("message")
@RestController
public class MessageController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(MessageController.class);
    @Autowired
    WechatService wechatService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object sendMessage(@RequestBody List<LuckyUser> users){
        try {
            wechatService.sendMessage(users);
            return ZoeObject.success();
        } catch (Exception e) {
            log.error("error",e);
            return ZoeObject.failure(e.toString());
        }
    }

    @RequestMapping(value = "danmu", method = RequestMethod.GET)
    public void sendDanmuMessage(@RequestParam String message, HttpServletRequest request){//将消息传入websocket通道中
            ServletContext application = request.getServletContext();
            Set<MyMessageInbound> connections =
                    (Set<MyMessageInbound>)application.getAttribute("connections");
            if(connections == null){
                return;
            }

            for (MyMessageInbound connection : connections) {
                try {
                    CharBuffer buffer = CharBuffer.wrap(message);
                    connection.getWsOutbound().writeTextMessage(buffer);
                } catch (IOException e) {
                    log.error("error",e);
                    e.printStackTrace();
                }
            }
    }
}
