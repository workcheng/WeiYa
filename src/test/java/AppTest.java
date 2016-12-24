import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chenghui on 2016/12/23.
 */
public class AppTest extends AbstractTestCase {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(AppTest.class);

    @Autowired
    WxMpServiceImpl wxMpService;

    @org.junit.Test
    public void test() throws Exception{
//        log.info(wxMpService.getQrcodeService().qrCodePictureUrl("test"));
//        System.out.println("=="+wxMpService.getQrcodeService().qrCodePictureUrl("test"));
        // 临时ticket
//        WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(scene, expire_seconds);
    }
}
