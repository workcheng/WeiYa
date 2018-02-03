package com.zoe.weiya;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by andy on 2016/12/21.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:spring-servlet-common.xml"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractTestCase {

}