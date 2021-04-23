package com.cxj.link.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 提取exception信息
 *
 * @author caijian
 */
public class LogExceptionStackTrace {
    public LogExceptionStackTrace() {
    }

    public static Object erroStackTrace(Object obj) {
        if (obj instanceof Exception) {
            Exception eObj = (Exception) obj;
            StringWriter sw = null;
            PrintWriter pw = null;

            Object var5;
            try {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                String exceptionStack = "\r\n";
                eObj.printStackTrace(pw);
                exceptionStack = sw.toString();
                String var17 = exceptionStack;
                return var17;
            } catch (Exception var15) {
                var15.printStackTrace();
                var5 = obj;
            } finally {
                try {
                    pw.close();
                    sw.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }
            return var5;
        } else {
            return obj;
        }
    }

}
