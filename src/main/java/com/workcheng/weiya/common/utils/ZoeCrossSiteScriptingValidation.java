package com.workcheng.weiya.common.utils;

/**
 * Created by chenghui on 2017/1/9.
 */

/**
 * 过滤非法字符
 */

public class ZoeCrossSiteScriptingValidation {

    private static String[] str =
            new String[]
                    {
                            "<",
                            "&"
                    };

    public static int find(String arr, String[] str, int startIndex) {
        boolean flag = false;
        for (int i = 0; i < str.length; i++) {
            if (arr.indexOf(str[i], startIndex) != -1) {
                flag = true;
                return arr.indexOf(str[i], startIndex);
            }
        }
        if (flag == false) {
            return -1;
        }
        return -1;
    }


    public static boolean IsDangerousString(String s) {
        int matchIndex = 0;
        int startIndex = 0;
        while (true) {
            int num = find(s, str, startIndex);
            if (num < 0) {
                break;
            }
            if (num == s.length() - 1) {
                return false;
            }
            matchIndex = num;
            char c = s.charAt(num);
            if (c != '&') {
                if (c == '<' && (ZoeCrossSiteScriptingValidation.IsAtoZ(s.charAt(num + 1)) || s.charAt(num + 1) == '!' || s.charAt(num + 1) == '/' || s.charAt(num + 1) == '?')) {
                    return true;
                }
            } else {
                if (s.charAt(num + 1) == '#') {
                    return true;
                }
            }
            startIndex = num + 1;
        }
        return false;
    }

    private static boolean IsAtoZ(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static void main(String[] args) {
        String s = "acd<&《&";
        for (int i = 0; i < str.length; i++) {
            System.out.println(" i= " + find(s, str, 0));
        }
    }
}
