package com.zoe.weiya.cache;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.cache.CacheManager;
import com.zoe.weiya.model.Cache;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by zhanghao on 2016/12/28.
 */
public class CacheTest {


    @Test
    public void test() throws Exception {
        System.out.println(CacheManager.getSimpleFlag("alksd"));
        CacheManager.putCache("abc", new Cache());
        CacheManager.putCache("def", new Cache());
        CacheManager.putCache("ccc", new Cache());
        CacheManager.clearOnly("");
        Cache c = new Cache();
        for (int i = 0; i < 10;
             i++) {
            CacheManager.putCache("" + i, c);
        }
        CacheManager.putCache("aaaaaaaa", c);
        CacheManager.putCache("abchcy;alskd", c);
        CacheManager.putCache("cccccccc", c);
        CacheManager.putCache("abcoqiwhcy", c);
        System.out.println("删除前的大小：" + CacheManager.getCacheSize());
        CacheManager.getCacheAllkey();
        CacheManager.clearAll("aaaa");
        System.out.println("删除后的大小：" + CacheManager.getCacheSize());
        CacheManager.getCacheAllkey();

    }

}
