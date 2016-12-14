package com.zoe.weiya.dao;

import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;

public class BaseDaoImpl {
  @Resource(name = "sqlSessionTemplate")
  protected  SqlSessionTemplate   sqlSessionTemplate;
}
