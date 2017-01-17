import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.zoe.weiya.model.User;
import redis.clients.jedis.Jedis;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by chenghui on 2016/12/20.
 */
public class Test{


    public static void main(String[] args) {
//        "latitude":24.479834,"longitude":118.089425,

//        latitude	:	24.479834        longitude	:	118.089425
        double lat1 = 24.479834;
        double lng1 = 118.089425;
        double lat2 = 24.49037;
        double lng2 = 118.1744;
        double jl_jd = 102834.74258026089786013677476285;
        double jl_wd = 111712.69150641055729984301412873;
        double b = Math.abs((lat1 - lat2) * jl_jd);
        double a = Math.abs((lng1 - lng2) * jl_wd);
//        System.out.println(Math.sqrt((a * a + b * b)));
        System.out.println(GetDistance(lat1,lng1,lat2,lng2));
    }

    @org.junit.Test
    public void method(){
        System.out.println(Long.valueOf(1));
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    private static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double EARTH_RADIUS = 6378.137;

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    @org.junit.Test
    public void test() throws Exception {
        Jedis jedis = new Jedis("localhost");
        jedis.hset("lotteryPerson", "ID001", "张三");
        jedis.hset("lotteryPerson", "ID002", "李四");
        jedis.hset("lotteryPerson", "ID003", "王五");
    }

    @org.junit.Test
    public void testUser(){
        User user = new User();
        System.out.println(user.getSignDate());
    }

    @org.junit.Test
    public void images() throws Exception{
        /*URL url = new URL("http://www.google.com/intl/en_ALL/images/logo.gif");
        BufferedImage image = ImageIO.read(url);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "gif", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());*/
        URL img = new  URL("http://img233.ph.126.net/m6sweVwiTs1-VNNz6Pj5xg==/1365153637048312514.jpg");
        InputStream in = img.openStream();

//        接着把输入流转为BufferedImage：

        JPEGImageDecoder decoderFile = JPEGCodec.createJPEGDecoder(in);
        BufferedImage image = decoderFile.decodeAsBufferedImage();

//        如果根据这个图片对象，重新draw了一个新的bufferedImage以后，怎么才能获得它的byte数组呢？

//        通过ImageIO对象进行操作：
/*        OutputStream bos = null;
        boolean jpg = ImageIO.write(image, "jpg", bos);*/
//        InputStream inputStream = ClassLoader.getSystemResourceAsStream("mm.jpg");
        BufferedImage bufferedImage = setBorderRadius(image, 1);
        /*ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "gif", os);*/
        // 输出为文件
        ImageIO.write(bufferedImage, "JPEG", new File("E:/fixfoxdownload/"
                + "test.jpg"));
    }

    public static BufferedImage setBorderRadius(BufferedImage srcImage, int radius){
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, width, height, radius, radius);
        g2d.setComposite(AlphaComposite.SrcIn);
        g2d.drawImage(srcImage, 0, 0, width, height, null);
        return image;
    }
}
