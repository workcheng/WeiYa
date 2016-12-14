package com.zoe.weiya.dao.test;

import com.zoe.weiya.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Created by andy on 2016/12/14.
 */
@Repository
public class TestDao extends BaseDaoImpl{

    public Object getTest() throws SQLException {
        return super.sqlSessionTemplate.selectOne("com.zoe.weiya.dao.mybatisMapper.test.TestDao.TestDaoMapper.getTest");
    }
}
