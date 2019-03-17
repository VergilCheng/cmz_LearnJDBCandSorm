package sorm1.utils;

/**
 * 封装了字符串常用操作
 */
public class StringUtils {
    /**
     * 将目标字符串首字母变为大写
     *
     * @return
     */
    public static String firstChar2UpperCase(String string) {
        return string.toUpperCase().substring(0, 1) + string.substring(1);
    }

}
