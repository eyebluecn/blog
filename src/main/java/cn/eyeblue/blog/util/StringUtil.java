package cn.eyeblue.blog.util;

import java.util.Random;

/**
 * 关于字符串处理的通用方法。
 */
public class StringUtil {

    /**
     * 获取长度为n的字符串
     *
     * @param n 字符串长度
     * @return 一个长度为n的随机字符串
     */
    public static String randomString(int n) {
        String rawString = "abcdefghijklmnopqrstuvwxyz0123456789";
        int rawLength = rawString.length();
        String res = "";
        for (int i = 0; i < n; i++) {
            int pos = new Random().nextInt(rawLength);
            res += rawString.charAt(pos);
        }
        return res;
    }


    /*
    名词变复数归纳总结
    1.一般情况下，在名词后加“s”或“es”.
    2.以s,sh,ch,x结尾的名字，在名词后直接加“es”.
    3.以o结尾的名字，有两种情况：
        1）有生命的名词，在名词后加“es”.
        如：tomato-tomatoes potato-potatoes
        2)无生命的名字，在名字后加“s”.
        如：photo-photos radio-radios
        注意：使用java一律采用加“s”的策略
    4.以辅音字母+y结尾的名词,将y改变为i,再加-es.
        元音字母+y结尾的名词则直接加s
     */
    public static String toPlural(String singular) {

        if (singular == null || singular.equals("")) {
            return singular;
        }
        int length = singular.length();
        //一个字母的直接加个s.
        if (length == 1) {
            return singular + "s";
        }

        char lastChar = singular.charAt(length - 1);
        char lastSecondChar = singular.charAt(length - 2);
        if (lastChar == 's' || lastChar == 'x' || (lastChar == 'h' && (lastSecondChar == 's' || lastSecondChar == 'c'))) {
            return singular + "es";
        } else if (lastChar == 'y' && (lastSecondChar != 'a' && lastSecondChar != 'e' && lastSecondChar != 'i' && lastSecondChar != 'o' && lastSecondChar != 'u')) {
            return singular.substring(0, length - 1) + "ies";
        } else {
            return singular + "s";
        }

    }
}
