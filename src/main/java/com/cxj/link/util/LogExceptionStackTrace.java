package com.cxj.link.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 提取exception信息
 *
 * @author caijian
 */
public class LogExceptionStackTrace {

    private LogExceptionStackTrace() {
    }

    public static Object erroStackTrace(Object object) {
        if (object instanceof Exception) {
            Exception exception = (Exception) object;

            Object value;

            try (StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw)) {

                String exceptionStack = "\r\n";
                exception.printStackTrace(pw);
                exceptionStack = sw.toString();
                String string = exceptionStack;
                return string;
            } catch (Exception ex) {
                ex.printStackTrace();
                value = object;
            }
            return value;
        } else {
            return object;
        }
    }

}
