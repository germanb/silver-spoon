package com.gbringas.silver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    static void logError(String message, Object... args) {
        System.err.println(ANSI_RED + getPrefix() + String.format(message, args) + ANSI_RESET);
    }

    static void logInfo(String message, Object... args) {
        System.out.println(ANSI_GREEN + getPrefix() + String.format(message, args) + ANSI_RESET);
    }

    static void logDebug(boolean debug, String message, Object... args) {
        if (debug) System.out.println(getPrefix() + String.format(message, args));
    }

    private static String getPrefix() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length < 4) return "[BOGUS]";
        String className = stackTrace[3].getClassName();
        String methodName = stackTrace[3].getMethodName();
        className = className.replaceAll("[a-z\\.]", "");
        String timestamp = DATE_FORMAT.get().format(new Date());
        return String.format("%s [%s.%s] ", timestamp, className, methodName);
    }
}
