package com.zoe.weiya.controller;

import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.OnlyUser;
import com.zoe.weiya.model.responseModel.MealOrder;
import com.zoe.weiya.service.user.UserService;
import com.zoe.weiya.util.ZoeDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by chenghui on 2017/1/3.
 */
@RequestMapping("meal")
@RestController
public class MealController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "order", method = RequestMethod.GET)
    public Object getOrder(){
        Set<String> openIdSet = userService.getOpenIdSet();
        MealOrder mealOrder = new MealOrder();
        List<OnlyUser> userList = new ArrayList<>();
        if(null != openIdSet){
            Iterator<String> iterator = openIdSet.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                OnlyUser onlyUser = userService.get(next);
                if("1".equals(onlyUser.getOrder())){
                    userList.add(onlyUser);
                }
            }
        }
        mealOrder.setOrderUsers(userList);
        mealOrder.setOrderCount(userList.size());
        mealOrder.setNow(ZoeDateUtil.moment());
        return ZoeObject.success(mealOrder);
    }
}
