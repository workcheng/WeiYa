package com.zoe.weiya.comm.redis;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.OnlyUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chenghui on 2016/12/26.
 */
public class ZoeRedisTempleteTest extends AbstractTestCase{
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(ZoeRedisTempleteTest.class);
    @Autowired
    ZoeRedisTemplate zoeRedisTemplate;

    @Test
    public void test() throws Exception{
        /*String key = "111";
        com.zoe.weiya.model.OnlyUser onlyUser = new OnlyUser();
        onlyUser.setName("test");
        zoeRedisTemplete.setValue(key,onlyUser);*/
        OnlyUser value = (OnlyUser) zoeRedisTemplate.getValue("111");
        if(value != null){
            log.info("info:"+ value.getName());
        }else{
            log.error("error:"+value);
        }
//        zoeRedisTemplete.move(key);
    }

}