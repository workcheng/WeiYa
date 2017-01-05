package com.zoe.weiya.controller.echo;

/**
 * Created by andy on 2017/1/4.
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/*import org.dxlab.danmu.domain.Info;
import org.dxlab.danmu.service.IInfoService;
import org.dxlab.danmu.service.impl.CoreServiceImpl;
import org.dxlab.danmu.service.impl.InfoServiceImpl;
import org.dxlab.danmu.util.MessageUtil;
import org.dxlab.danmu.util.SignUtil;
import org.dxlab.danmu.util.wx.message.resp.TextMessage;
import org.dxlab.danmu.websocket.MyMessageInbound;*/


/**
 * @author wdj
 * 核心请求处理servlet
 * */
public class CoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final HashMap<Integer,String> color = new HashMap<Integer,String>(){//弹幕颜色
        private static final long serialVersionUID = 1L;
        {
            put(1,"red");
            put(2,"white");
            put(3,"green");
            put(4,"blue");
            put(5,"yellow");
        }
    };

    public CoreServlet() {
        super();
    }

    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }


    /*
     * 确认请求来自微信服务器
     * */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        /*if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }*/
        out.close();
        out = null;
    }

    /*
     *处理微信服务器发来的信息
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        try {
            //解析微信发送来的XML
//            Map<String, String> requestMap = MessageUtil.parseXml(request);
            Map<String, String> requestMap = null;
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 消息文本
            String content = requestMap.get("Content").trim();

/*            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            textMessage.setContent(content);

            CoreServiceImpl coreService = new CoreServiceImpl();*/
            String message =  requestMap.get("Content");
            String respMessage = null;

            //保存弹幕信息
            /*Info info = new Info();
            info.setOpenid(fromUserName);
//              System.out.println(user.getWuid());
            info.setInfo(message);
            IInfoService iis = new InfoServiceImpl();
            iis.add(info);*/
            System.out.println(message);
            if(message.contains("+")){//添加需要被和谐的词汇
                String[] str = message.split("[+]");
                if(str.length>1 && str[0].equals("1")){
//                    CoreServiceImpl.hexie.put(CoreServiceImpl.hexie.size()+1, str[1]);
                }
            }
            String danmuText = "{ \"text\":\"加油\",\"color\":\"white\",\"size\":\"1\",\"position\":\"0\",\"time\":";
            //随机选取发送的弹幕的类型和颜色
/*            int ranNum = coreService.getRandomNum();
            int position = coreService.getPosition();
            if(ranNum<10){
                danmuText = "{ \"text\":\""+message+"\",\"color\":\""+color.get(ranNum/5)+"\",\"size\":\""+position+"\",\"position\":\"1\",\"time\":";
            }else if(ranNum<20){
                danmuText = "{ \"text\":\""+message+"\",\"color\":\""+color.get(ranNum/5)+"\",\"size\":\""+position+"\",\"position\":\"2\",\"time\":";
            }else{
                danmuText = "{ \"text\":\""+message+"\",\"color\":\""+color.get(ranNum/5)+"\",\"size\":\""+position+"\",\"position\":\"0\",\"time\":";
            }*/
            // 调用核心业务类接收消息、处理消息
            /*respMessage = coreService.processRequest(textMessage);
            if(coreService.isHeXie(message)){
                broadcast(danmuText);//将微信消息组装的弹幕格式的消息传入websocket通道
            }else{
                textMessage.setContent("呀，您发送的弹幕可能包含不和谐的词语呢╮(╯_╰)╭");
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }*/
            // 响应消息
            PrintWriter out = response.getWriter();

            out.print(respMessage);
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void init() throws ServletException {
        // Put your code here
    }

    @SuppressWarnings("deprecation")
    private void broadcast(String message) {//将消息传入websocket通道中
        ServletContext application=this.getServletContext();
        @SuppressWarnings("unchecked")
        Set<MyMessageInbound> connections =
                (Set<MyMessageInbound>)application.getAttribute("connections");
        if(connections == null){
            return;
        }

        for (MyMessageInbound connection : connections) {
            try {
                CharBuffer buffer = CharBuffer.wrap(message);
                connection.getWsOutbound().writeTextMessage(buffer);
            } catch (IOException ignore) {
                // Ignore
            }
        }
    }

}