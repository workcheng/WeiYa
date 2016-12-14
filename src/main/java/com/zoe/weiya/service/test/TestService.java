package com.zoe.weiya.service.test;

import com.zoe.weiya.dao.test.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Created by andy on 2016/12/14.
 */
@Service
public class TestService {
    @Autowired
    private TestDao testDao;

    public Object getTest() throws SQLException {
        return testDao.getTest();
    }
}
