package com.zoe.weiya.service.user;

import com.zoe.weiya.AbstractTestCase;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.uuid.ZoeUUID;
import com.zoe.weiya.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2016/12/21.
 */
public class UserServiceTest extends AbstractTestCase {
//public class UserServiceTest {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(UserServiceTest.class);
    @Autowired UserService userService;

    @Test
    public void save() throws Exception {
        User user = new User();
        user.setOpenId("oChiIs03wk41iBRrAHV-Kv8a2jeg0");
        user.setOrder(1);
        userService.save(user);
    }

    private String[] nameList = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
    };
    private String getRandomName(){
        return nameList[Integer.valueOf(String.valueOf(Math.round(Math.random() * 25)))] + nameList[Integer.valueOf(String.valueOf(Math.round(Math.random() * 25)))] + nameList[Integer.valueOf(String.valueOf(Math.round(Math.random() * 25)))];
    }
    @Test
    public void saveInSet() throws Exception {
            User u = new User();
            u.setOpenId(ZoeUUID.random());
            log.info("info:"+userService.saveInSet(u.getOpenId()));
            log.info("size:"+userService.isMember(u.getOpenId()));
    }

    @Test
    public void getUserSet() throws Exception {
        log.info("info:"+ userService.getOpenIdSet());
    }

    @Test
    public void testGetSignUser() throws Exception {
        List<User> signUser = userService.randomUsers();
        log.info("info:"+ signUser.size());
    }

    @Test
    public void testSave() throws Exception {
        for(int i=0; i<=1500; i++){
            User u = new User();
            String randomName = getRandomName();
            u.setName(randomName);
            u.setOpenId(ZoeUUID.random());
            //56 640 728 750 748 1242
            String headUrl="http://fpoimg.com/{0}x{1}?text={2}";
            int length = (Integer.valueOf(String.valueOf(Math.round(Math.random() * 25))) + 1) * 30;
           /* if(length < 100){
                length = length * 15;
            }
            double height;
            if(length > 666){
                height = length * 0.9;
            }else {
                height = length * 1.5;
            }*/
            int height = (int)(length * 1.5);
            String formatUrl = MessageFormat.format(headUrl, String.valueOf(length).replace(",",""), String.valueOf(height).replace(",",""), randomName);
            log.info(formatUrl);
            u.setHeadImgUrl(formatUrl);
            userService.save(u);
        }
    }

    @Test
    public void testGetRandomUser() throws Exception {
//        List<String> randomUser = userService.getRandomOpenIds(5);
//        log.info(randomUser.toString());
    }

    @Test
    public void testGetRandomUser1() throws Exception {
        ArrayList list = new ArrayList<String>(){
            {
                add("0");
                add("1");
                add("2");
                add("3");
                add("4");
                add("5");
            }
        };
        list.remove(1);
        System.out.println(list.toString());
    }
    @Test
    public void testOrderMealUserList() throws Exception {
        log.info(userService.orderMealUserList().toString());
    }
}