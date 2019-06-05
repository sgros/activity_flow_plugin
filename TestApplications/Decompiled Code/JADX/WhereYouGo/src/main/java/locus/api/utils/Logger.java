package locus.api.utils;

public class Logger {
    private static ILogger logger;

    public interface ILogger {
        void logD(String str, String str2);

        void logE(String str, String str2);

        void logE(String str, String str2, Exception exception);

        void logI(String str, String str2);

        void logW(String str, String str2);
    }

    public static void registerLogger(ILogger logger) {
        logger = logger;
    }

    public static void logI(String tag, String msg) {
        if (logger == null) {
            System.out.println(tag + " - " + msg);
        } else {
            logger.logI(tag, msg);
        }
    }

    public static void logD(String tag, String msg) {
        if (logger == null) {
            System.out.println(tag + " - " + msg);
        } else {
            logger.logD(tag, msg);
        }
    }

    public static void logW(String tag, String msg) {
        if (logger == null) {
            System.out.println(tag + " - " + msg);
        } else {
            logger.logW(tag, msg);
        }
    }

    public static void logE(String tag, String msg) {
        if (logger == null) {
            System.err.println(tag + " - " + msg);
        } else {
            logger.logE(tag, msg);
        }
    }

    public static void logE(String tag, String msg, Exception e) {
        if (logger == null) {
            System.err.println(tag + " - " + msg + ", e:" + e.getMessage());
            e.printStackTrace();
            return;
        }
        logger.logE(tag, msg, e);
    }
}
