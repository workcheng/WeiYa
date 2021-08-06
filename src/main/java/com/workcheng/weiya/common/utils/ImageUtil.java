package com.workcheng.weiya.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 *
 * @author chenghui
 * @date 2017/1/17
 */

@Slf4j
public class ImageUtil {

    /**
     * 针对高度与宽度进行等比缩放
     *
     * @param img
     * @param maxSize 要缩放到的尺寸
     * @param type    1:高度与宽度的最大值为maxSize进行等比缩放 , 2:高度与宽度的最小值为maxSize进行等比缩放
     * @return
     */
    private static Image getScaledImage(BufferedImage img, int maxSize, int type) {
        int w0 = img.getWidth();
        int h0 = img.getHeight();
        int w = w0;
        int h = h0;
        if (type == 1) {
            w = w0 > h0 ? maxSize : (maxSize * w0 / h0);
            h = w0 > h0 ? (maxSize * h0 / w0) : maxSize;
        } else if (type == 2) {
            w = w0 > h0 ? (maxSize * w0 / h0) : maxSize;
            h = w0 > h0 ? maxSize : (maxSize * h0 / w0);
        }
        Image image = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        //在适当的位置画出
        return result;
    }

    /**
     * 先按最小宽高为size等比例绽放, 然后图像居中抠出直径为size的圆形图像
     *
     * @param img
     * @param size
     * @return
     */
    private static BufferedImage getRoundedImage(BufferedImage img, int size) {
        return getRoundedImage(img, size, size / 2, 2);
    }

    /**
     * 先按最小宽高为size等比例绽放, 然后图像居中抠出半径为radius的圆形图像
     *
     * @param img
     * @param size   要缩放到的尺寸
     * @param radius 圆角半径
     * @param type   1:高度与宽度的最大值为maxSize进行等比缩放 , 2:高度与宽度的最小值为maxSize进行等比缩放
     * @return
     */
    private static BufferedImage getRoundedImage(BufferedImage img, int size, int radius, int type) {
        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        //先按最小宽高为size等比例绽放, 然后图像居中抠出直径为size的圆形图像
        Image fixedImg = getScaledImage(img, size, type);
        g.drawImage(fixedImg, (size - fixedImg.getWidth(null)) / 2, (size - fixedImg.getHeight(null)) / 2, null);
        //在适当的位置画出
        //圆角
        if (radius > 0) {
            RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, size, size, radius * 2, radius * 2);
            Area clear = new Area(new Rectangle(0, 0, size, size));
            clear.subtract(new Area(round));
            g.setComposite(AlphaComposite.Clear);
            //抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fill(clear);
            g.dispose();
        }
        return result;
    }

    /**
     * 使用删除alpha值的方式去掉图像的alpha通道
     *
     * @param $image
     * @return
     */
    protected static BufferedImage get24BitImage(BufferedImage $image) {
        int __w = $image.getWidth();
        int __h = $image.getHeight();
        int[] __imgARGB = getRGBs($image.getRGB(0, 0, __w, __h, null, 0, __w));
        BufferedImage __newImg = new BufferedImage(__w, __h, BufferedImage.TYPE_INT_RGB);
        __newImg.setRGB(0, 0, __w, __h, __imgARGB, 0, __w);
        return __newImg;
    }

    /**
     * 使用绘制的方式去掉图像的alpha值
     *
     * @param $image
     * @param $bgColor
     * @return
     */
    protected static BufferedImage get24BitImage(BufferedImage $image, Color $bgColor) {
        int $w = $image.getWidth();
        int $h = $image.getHeight();
        BufferedImage img = new BufferedImage($w, $h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor($bgColor);
        g.fillRect(0, 0, $w, $h);
        g.drawRenderedImage($image, null);
        g.dispose();
        return img;
    }

    /**
     * 将32位色彩转换成24位色彩（丢弃Alpha通道）
     *
     * @param $argb
     * @return
     */
    public static int[] getRGBs(int[] $argb) {
        int[] __rgbs = new int[$argb.length];
        for (int i = 0; i < $argb.length; i++) {
            __rgbs[i] = $argb[i] & 0xFFFFFF;
        }
        return __rgbs;
    }


    public static void toPNG(File img, File save, int size, int radius) throws IOException {
        ImageIO.write(getRoundedImage(ImageIO.read(img), size, radius, 2), "PNG", save);
        //默认无圆角
    }

    public static ByteArrayOutputStream toPNG(URL url, int size, int radius) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(getRoundedImage(ImageIO.read(url), size, radius, 2), "PNG", os);
//        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return os;
    }

    public static void toPNG(URL url, OutputStream os, int size, int radius) throws IOException {
        BufferedImage roundedImage = getRoundedImage(ImageIO.read(url), size, radius, 2);
        ImageIO.write(roundedImage, "PNG", os);
        os.close();
    }

    public static void toPNG(URL url, OutputStream os, int size, int radius, StringRedisTemplate stringRedisTemplate) throws IOException {
        if (getFromTemp(os, url, stringRedisTemplate)) {
            log.info("从缓存获取, url:{}", url.getPath());
            return;
        }
        BufferedImage roundedImage = getRoundedImage(ImageIO.read(url), size, radius, 2);
        createTempAndOutPut(os, url, roundedImage, stringRedisTemplate);
    }

    public static void main(String[] args) throws IOException {
        File img = new File("e:\\Users\\rocky\\Desktop\\0\\IMG_0404.PNG");
        File save = new File("e:\\Users\\rocky\\Desktop\\0\\zz.jpg");
//        toJPG(img, save, 250, 100);
    }

    private static void createTempAndOutPut(OutputStream os, URL url, BufferedImage roundedImage, StringRedisTemplate stringRedisTemplate) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(roundedImage,  "PNG", baos);
            final byte[] bytes = baos.toByteArray();
            final String encodeToString = Base64Utils.encodeToString(bytes);
            stringRedisTemplate.opsForValue().set(getTempPrefix(url.getPath()), encodeToString);
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            log.error("create image temp error", e);
            ImageIO.write(roundedImage, "PNG", os);
            os.close();
        }
    }

    private static boolean getFromTemp(OutputStream os, URL url, StringRedisTemplate stringRedisTemplate) {
        try {
            final String s = stringRedisTemplate.opsForValue().get(getTempPrefix(url.getPath()));
            if (StringUtils.isNotEmpty(s)) {
                final byte[] bytes = Base64Utils.decodeFromString(s);
                os.write(bytes);
                os.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("get image from temp error", e);
            return false;
        }
    }

    private static String getTempPrefix(String key) {
        String tempPrefix = "headImage";
        return tempPrefix + "_" + key;
    }
}

