package androidx.work;

import android.util.Log;

public abstract class Logger {
    private static final int MAX_PREFIXED_TAG_LENGTH = (23 - "WM-".length());
    private static Logger sLogger;

    public static class LogcatLogger extends Logger {
        private int mLoggingLevel;

        public LogcatLogger(int i) {
            super(i);
            this.mLoggingLevel = i;
        }

        public void verbose(String str, String str2, Throwable... thArr) {
            if (this.mLoggingLevel > 2) {
                return;
            }
            if (thArr == null || thArr.length < 1) {
                Log.v(str, str2);
            } else {
                Log.v(str, str2, thArr[0]);
            }
        }

        public void debug(String str, String str2, Throwable... thArr) {
            if (this.mLoggingLevel > 3) {
                return;
            }
            if (thArr == null || thArr.length < 1) {
                Log.d(str, str2);
            } else {
                Log.d(str, str2, thArr[0]);
            }
        }

        public void info(String str, String str2, Throwable... thArr) {
            if (this.mLoggingLevel > 4) {
                return;
            }
            if (thArr == null || thArr.length < 1) {
                Log.i(str, str2);
            } else {
                Log.i(str, str2, thArr[0]);
            }
        }

        public void warning(String str, String str2, Throwable... thArr) {
            if (this.mLoggingLevel > 5) {
                return;
            }
            if (thArr == null || thArr.length < 1) {
                Log.w(str, str2);
            } else {
                Log.w(str, str2, thArr[0]);
            }
        }

        public void error(String str, String str2, Throwable... thArr) {
            if (this.mLoggingLevel > 6) {
                return;
            }
            if (thArr == null || thArr.length < 1) {
                Log.e(str, str2);
            } else {
                Log.e(str, str2, thArr[0]);
            }
        }
    }

    public abstract void debug(String str, String str2, Throwable... thArr);

    public abstract void error(String str, String str2, Throwable... thArr);

    public abstract void info(String str, String str2, Throwable... thArr);

    public abstract void verbose(String str, String str2, Throwable... thArr);

    public abstract void warning(String str, String str2, Throwable... thArr);

    public static synchronized void setLogger(Logger logger) {
        synchronized (Logger.class) {
            sLogger = logger;
        }
    }

    public static String tagWithPrefix(String str) {
        int length = str.length();
        StringBuilder stringBuilder = new StringBuilder(23);
        stringBuilder.append("WM-");
        if (length >= MAX_PREFIXED_TAG_LENGTH) {
            stringBuilder.append(str.substring(0, MAX_PREFIXED_TAG_LENGTH));
        } else {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static synchronized Logger get() {
        Logger logger;
        synchronized (Logger.class) {
            if (sLogger == null) {
                sLogger = new LogcatLogger(3);
            }
            logger = sLogger;
        }
        return logger;
    }

    public Logger(int i) {
    }
}
