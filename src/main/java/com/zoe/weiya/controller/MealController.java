package com.zoe.weiya.controller;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.exception.InternalException;
import com.zoe.weiya.comm.exception.NotStartException;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.User;
import com.zoe.weiya.model.responseModel.MealOrder;
import com.zoe.weiya.service.user.UserService;
import com.zoe.weiya.util.ZoeDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        try {
            MealOrder mealOrder = new MealOrder();
            List<User> userList = userService.orderMealUserList();
            mealOrder.setOrderUsers(userList);
            mealOrder.setOrderCount(userList.size());
            mealOrder.setNow(ZoeDateUtil.moment());
            return ZoeObject.success(mealOrder);
        } catch (NotStartException e) {
            return ZoeObject.failure(ZoeErrorCode.NOT_START);
        } catch (InternalException e) {
            return ZoeObject.failure(e.getMessage());
        }
    }
}
