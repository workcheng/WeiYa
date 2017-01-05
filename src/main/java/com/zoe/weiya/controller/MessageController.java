package com.zoe.weiya.controller;

import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.controller.echo.MyMessageInbound;
import com.zoe.weiya.model.User;
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
    public void sendMessage(@RequestBody List<String> openIds){
        for (int i=0; i<openIds.size(); i++){
            try {
                User userInfo = userService.get(openIds.get(i));
                if(null != userInfo){
                    wechatService.sendMessage(userInfo.getOpenId(),userInfo.getName(),0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("danmu")
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
