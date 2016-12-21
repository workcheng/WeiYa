/**
 * Created by chenghui on 2016/12/20.
 */
public class Test {
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

    private void method(){



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
}
