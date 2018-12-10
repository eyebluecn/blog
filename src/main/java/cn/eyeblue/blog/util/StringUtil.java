package cn.eyeblue.blog.util;

import com.google.common.base.CaseFormat;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Random;
import java.util.UUID;

/**
 * 关于字符串处理的通用方法。
 */
public class StringUtil extends StringUtils {

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

    //生成32位的UUID，即将uuid的'-'去掉
    public static String new32BitsUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String new32BitsUuid(@NonNull String uuid) {
        return uuid.replace("-", "");
    }

    //将一个32为的字符串转变回uuid
    public static String recoverUuid(@NonNull String bits32) {
        if (bits32.length() != 32) {
            throw new RuntimeException("入参非32位" + bits32);
        }

        //1ca00b2d 3016 4e22 b150 277828cffd36
        //1ca00b2d-3016-4e22-b150-277828cffd36
        return format("{}-{}-{}-{}-{}",
                bits32.substring(0, 8),
                bits32.substring(8, 12),
                bits32.substring(12, 16),
                bits32.substring(16, 20),
                bits32.substring(20, 32)
        );
    }

    //将阿拉伯数字转成中文。
    public static String numberToChinese(int num) {

        String[] chnNumChar = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] chnUnitSection = new String[]{"", "万", "亿", "万亿", "亿亿"};
        String[] chnUnitChar = new String[]{"", "十", "百", "千"};

        String strIns = "";
        String chnStr = "";
        int unitPos = 0;
        boolean zero = true;
        while (num > 0) {
            int v = num % 10;
            if (v == 0) {
                if (!zero) {
                    zero = true;
                    chnStr = chnNumChar[v] + chnStr;
                }
            } else {
                zero = false;
                strIns = chnNumChar[v];
                strIns += chnUnitChar[unitPos];
                chnStr = strIns + chnStr;
            }
            unitPos++;
            num = num / 10;
        }
        return chnStr;

    }

    //数字转成ABCD
    public static String numberToCapital(int num) {
        if (num < 0 || num > 'Z') {
            return "null";
        }
        char res = (char) ('A' + num);
        return String.valueOf(res);
    }


    //把一个字符串转成下划线的大写格式
    public static String toUpperUnderscore(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
    }

    //类似于sl4j的字符串格式化.使用 {} 做占位符。
    public static String format(String messagePattern, Object... arguments) {
        return MessageFormatter.arrayFormat(messagePattern, arguments).getMessage();
    }

}
