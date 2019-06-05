// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import android.util.Log;

public abstract class Logger
{
    private static final int MAX_PREFIXED_TAG_LENGTH;
    private static Logger sLogger;
    
    static {
        MAX_PREFIXED_TAG_LENGTH = 23 - "WM-".length();
    }
    
    public Logger(final int n) {
    }
    
    public static Logger get() {
        synchronized (Logger.class) {
            if (Logger.sLogger == null) {
                Logger.sLogger = new LogcatLogger(3);
            }
            return Logger.sLogger;
        }
    }
    
    public static void setLogger(final Logger sLogger) {
        synchronized (Logger.class) {
            Logger.sLogger = sLogger;
        }
    }
    
    public static String tagWithPrefix(final String str) {
        final int length = str.length();
        final StringBuilder sb = new StringBuilder(23);
        sb.append("WM-");
        if (length >= Logger.MAX_PREFIXED_TAG_LENGTH) {
            sb.append(str.substring(0, Logger.MAX_PREFIXED_TAG_LENGTH));
        }
        else {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public abstract void debug(final String p0, final String p1, final Throwable... p2);
    
    public abstract void error(final String p0, final String p1, final Throwable... p2);
    
    public abstract void info(final String p0, final String p1, final Throwable... p2);
    
    public abstract void verbose(final String p0, final String p1, final Throwable... p2);
    
    public abstract void warning(final String p0, final String p1, final Throwable... p2);
    
    public static class LogcatLogger extends Logger
    {
        private int mLoggingLevel;
        
        public LogcatLogger(final int mLoggingLevel) {
            super(mLoggingLevel);
            this.mLoggingLevel = mLoggingLevel;
        }
        
        @Override
        public void debug(final String s, final String s2, final Throwable... array) {
            if (this.mLoggingLevel <= 3) {
                if (array != null && array.length >= 1) {
                    Log.d(s, s2, array[0]);
                }
                else {
                    Log.d(s, s2);
                }
            }
        }
        
        @Override
        public void error(final String s, final String s2, final Throwable... array) {
            if (this.mLoggingLevel <= 6) {
                if (array != null && array.length >= 1) {
                    Log.e(s, s2, array[0]);
                }
                else {
                    Log.e(s, s2);
                }
            }
        }
        
        @Override
        public void info(final String s, final String s2, final Throwable... array) {
            if (this.mLoggingLevel <= 4) {
                if (array != null && array.length >= 1) {
                    Log.i(s, s2, array[0]);
                }
                else {
                    Log.i(s, s2);
                }
            }
        }
        
        @Override
        public void verbose(final String s, final String s2, final Throwable... array) {
            if (this.mLoggingLevel <= 2) {
                if (array != null && array.length >= 1) {
                    Log.v(s, s2, array[0]);
                }
                else {
                    Log.v(s, s2);
                }
            }
        }
        
        @Override
        public void warning(final String s, final String s2, final Throwable... array) {
            if (this.mLoggingLevel <= 5) {
                if (array != null && array.length >= 1) {
                    Log.w(s, s2, array[0]);
                }
                else {
                    Log.w(s, s2);
                }
            }
        }
    }
}
