// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

import android.util.Log;
import com.airbnb.lottie.L;
import java.util.HashSet;
import java.util.Set;
import com.airbnb.lottie.LottieLogger;

public class LogcatLogger implements LottieLogger
{
    private static final Set<String> loggedMessages;
    
    static {
        loggedMessages = new HashSet<String>();
    }
    
    @Override
    public void debug(final String s) {
        this.debug(s, null);
    }
    
    public void debug(final String s, final Throwable t) {
        if (L.DBG) {
            Log.d("LOTTIE", s, t);
        }
    }
    
    @Override
    public void warning(final String s) {
        this.warning(s, null);
    }
    
    @Override
    public void warning(final String s, final Throwable t) {
        if (LogcatLogger.loggedMessages.contains(s)) {
            return;
        }
        Log.w("LOTTIE", s, t);
        LogcatLogger.loggedMessages.add(s);
    }
}
