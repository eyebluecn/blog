package cn.eyeblue.blog.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 和参数处理有关的通用方法
 */
public class ValidationUtil {
    /**
     * 检查参数是否为空
     *
     * @param list the list
     * @return true, if successful
     */
    public static boolean checkParam(Object... list) {
        boolean res = true;

        for (Object obj : list) {
            //普通对象我们看看是不是为空，或者空字符串
            if (obj == null || "".equals(obj)) {
                res = false;
                break;
            }
            //如果是容器类的话，那么我们看看是否大小为0
            //String.class.isAssignableFrom(Object.class); false
            //Object.class.isAssignableFrom(String.class); true
            //Class1.isAssignableFrom(Class2);表示判断Class1是否是Class2的父类。
            else if (Collection.class.isAssignableFrom(obj.getClass())) {

                Collection c = (Collection) obj;

                if (c.size() == 0) {
                    res = false;
                }
                break;
            }
        }
        return res;
    }

    public static boolean isPhone(String str) {
        return str != null && str.matches("\\d{11}");
    }

    public static boolean isLocationWord(String str) {
        return str != null && str.matches("^[A-Za-z0-9_-]+$");
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    //判断是不是整形数组构成的。[from,to] 空字符串返回正确
    public static boolean isIntArrayString(String str, int from, int to) {
        if (str.trim().equals("")) {
            return true;
        }

        String[] strArr = str.split(" ");

        for (String s : strArr) {
            try {
                Integer i = Integer.parseInt(s);

                if (i < from || i > to) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;

    }


    //判断是否在某个范围内.[from,to]
    public static boolean inRange(int target, int from, int to) {
        return (target >= from && target <= to);
    }
}
