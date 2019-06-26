// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

import com.airbnb.lottie.LottieLogger;

public class Logger
{
    private static LottieLogger INSTANCE;
    
    static {
        Logger.INSTANCE = new LogcatLogger();
    }
    
    public static void debug(final String s) {
        Logger.INSTANCE.debug(s);
    }
    
    public static void warning(final String s) {
        Logger.INSTANCE.warning(s);
    }
    
    public static void warning(final String s, final Throwable t) {
        Logger.INSTANCE.warning(s, t);
    }
}
