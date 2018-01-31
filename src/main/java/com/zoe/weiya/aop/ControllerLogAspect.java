package com.zoe.weiya.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Binary Wang
 *
 */
@Aspect
@Component
public class ControllerLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("within(com.github.binarywang.demo.spring..*.controller..*)")
    public void inController() {
    }

    @Pointcut("execution(public * com.github.binarywang.demo.spring..*.controller..*.*(..))")
    public void controller() {
    }

    @Before("inController()")
    public void writeBeforeLog(JoinPoint jp) {
        this.debugInController(jp, "Start");
    }

    @After("inController()")
    public void writeAfterLog(JoinPoint jp) {
        this.debugInController(jp, "End");
    }

    private void debugInController(JoinPoint jp, String msg) {
        String userName = getLoginUserName();

        this.logger.debug("\n【{}】{}.{}() {} ", userName,
            jp.getTarget()
            .getClass().getSimpleName(), jp.getSignature().getName(), msg);
    }

    private static String getLoginUserName() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }

        return "Anonymous";
    }

    @Before("controller()")
    public void writeParams(JoinPoint jp) {
        String[] names = ((CodeSignature) jp.getSignature())
            .getParameterNames();
        Object[] args = jp.getArgs();

        if (ArrayUtils.isEmpty(names)) {
            return;
        }

        StringBuilder sb = new StringBuilder("Arguments: ");
        for (int i = 0; i < names.length; i++) {
            sb.append(names[i] + " = " + args[i] + ",");
        }

        debugInController(jp, sb.toString());
    }

}
