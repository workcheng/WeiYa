package com.zoe.weiya;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by andy on 2016/12/21.
 */
@ContextConfiguration(locations = {"classpath:config/spring/applicationContext.xml","classpath:config/spring/applicationRabbitMqClient.xml","classpath:config/springmvc-servlet.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractTestCase {

}