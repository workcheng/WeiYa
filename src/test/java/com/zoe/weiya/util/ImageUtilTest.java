package com.zoe.weiya.util;

import org.junit.Test;

import java.io.File;

/**
 * Created by chenghui on 2017/1/17.
 */
public class ImageUtilTest {

    @Test
    public void testToPNG() throws Exception {
        ImageUtil.toPNG(new File("E:\\fixfoxdownload\\test.jpg"),new File("E:\\fixfoxdownload\\test1.jpg"),250,250);
    }
}