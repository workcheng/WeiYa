package com.zoe.weiya.controller;

import com.zoe.weiya.comm.constant.ZoeErrorCode;
import com.zoe.weiya.comm.logger.ZoeLogger;
import com.zoe.weiya.comm.logger.ZoeLoggerFactory;
import com.zoe.weiya.comm.response.ZoeObject;
import com.zoe.weiya.model.Area;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andy on 2016/12/18.
 */
@RequestMapping("sign")
@RestController
public class SignController {
    private static final ZoeLogger log = ZoeLoggerFactory.getLogger(SignController.class);

    @RequestMapping(value = "if_over_area",method = RequestMethod.POST)
    public Object ifOverArea(Area area){
        //TODO 判断位置范围，返回是否在这个范围当中
        if(true){
            return ZoeObject.success(ZoeErrorCode.SUCCESS);
        }else{
            return ZoeObject.failure(ZoeErrorCode.ERROR_WECHAT);
        }
    }
}
