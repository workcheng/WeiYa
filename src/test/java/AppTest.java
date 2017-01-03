import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.User;
import com.zoe.weiya.service.user.UserService;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by chenghui on 2016/12/23.
 */
public class AppTest  extends AbstractTestCase{
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(AppTest.class);
    @Autowired
    UserService userService;

    @org.junit.Test
    public void test() throws Exception{
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
//        config.setAppId("wx3b2f04cd718a6f57"); // 设置微信公众号的appid
        config.setAppId("wx5149e1919bfc8aee"); // 设置微信公众号的appid
        config.setToken("andy"); // 设置微信公众号的token
        config.setAesKey("..."); // 设置微信公众号的EncodingAESKey

        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);

        // 用户的openid在下面地址获得
        // https://mp.weixin.qq.com/debug/cgi-bin/apiinfo?t=index&type=用户管理&form=获取关注者列表接口%20/user/get
        /*String openid = "oChiIs25opLJaN61eKr1jx8Ir2oA";
        WxMpKefuMessage message = WxMpKefuMessage.TEXT().toUser(openid).content("Hello World").build();
        wxService.getKefuService().sendKefuMessage(message);*/

        WxMpUserList wxMpUserList = wxService.getUserService().userList(null);
        if(null != wxMpUserList){
            List<String> openIds = wxMpUserList.getOpenIds();
            if(null != openIds){
                for(int i=0; i<openIds.size(); i++){
                    WxMpUser wxMpUser = wxService.getUserService().userInfo(openIds.get(i));
                    if(null != wxMpUser){
                        User u = new User();
                        u.setName(wxMpUser.getNickname());
                        u.setOpenId(wxMpUser.getOpenId());
                        u.setHeadImgUrl(wxMpUser.getHeadImgUrl());
                        userService.save(u);
                        log.info(wxMpUser.toString());
                    }
                }
            }
        }
    }
}
