package com.zoe.weiya.comm.redis;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * Created by andy on 2016/12/29.
 */
public class ZoeRedisTemplateTest extends AbstractTestCase{
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(ZoeRedisTemplateTest.class);
    private String key = "key";
    @Autowired ZoeRedisTemplate zoeRedisTemplate;

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

    @Test
    public void getZSetOperations() throws Exception {
        Long size = zoeRedisTemplate.getZSetOperations().size(key);
        User user = new User();
        user.setName("chenghui");
        user.setOpenId("123456789");
        log.info("info:"+zoeRedisTemplate.getZSetOperations().add(key,user,size));
    }

    @Test
    public void getZSetOperationsScan() throws Exception {
        ScanOptions scanOptions = ScanOptions.scanOptions().match("123456789").build();
        Cursor<ZSetOperations.TypedTuple<Object>> cursor = zoeRedisTemplate.getZSetOperations().scan(key, scanOptions);
        log.info("=====");
        while (cursor.hasNext()){
            log.info(">>>>>>");
            ZSetOperations.TypedTuple<Object> next = cursor.open().next();
            User value = (User) next.getValue();
            log.info("info:"+ value);
        }
        log.info("=====");
    }

    @Test
    public void getSetOperations() throws Exception {
        ScanOptions scanOptions = ScanOptions.scanOptions().match("110").build();
        log.info("info"+zoeRedisTemplate.getSetOperations().scan("",scanOptions));
    }

    @Test
    public void putSetOperations() throws Exception {
        zoeRedisTemplate.getSetOperations().add(key,"");
    }

}