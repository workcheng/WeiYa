package com.zoe.weiya.comm.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * Created by zengjiyang on 2016/1/26.
 */
@Component("weiya.bean.factory")
public final class BeanFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        Collection<T> beans = getBeans(clazz);
        if (beans == null || beans.isEmpty())
            return null;

        T bean = null;
        for (T t : beans)
            bean = t;

        return bean;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        try {
            return (T) getApplicationContext().getBean(beanName);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(beanName, clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public static <T> Collection<T> getBeans(Class<T> clazz) {
        try {
            Map<String, T> map = getApplicationContext().getBeansOfType(clazz);
            return map.values();
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getBeanClass(String beanName) {
        return (Class<T>) getApplicationContext().getType(beanName);
    }

    public static Map<String, Object> getBeanWithAnnotation(Class<? extends Annotation> clazz) {
        return getApplicationContext().getBeansWithAnnotation(clazz);
    }

    public String[] getBeanNames() {
        return getApplicationContext().getBeanDefinitionNames();
    }
}
