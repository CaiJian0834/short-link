package com.cxj.link.util;

import java.util.Stack;

/**
 * 进制转换
 *
 * @author caijian
 */
public class MathUtils {

    private static char[] charSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 将10进制转化为62进制
     *
     * @param number
     * @return
     */
    public static String _10_to_62(Long number) {
        Long rest = Math.abs(number);
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder(0);
        while (rest != 0) {
            int i = new Long((rest - (rest / 62) * 62)).intValue();
            stack.add(charSet[i]);
            rest = rest / 62;
        }
        for (; !stack.isEmpty(); ) {
            result.append(stack.pop());
        }

        return result.toString();

    }
}
