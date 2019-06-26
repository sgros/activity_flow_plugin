// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.util;

import android.util.Log;

public class AndroidLogger extends Logger
{
    String name;
    
    public AndroidLogger(final String name) {
        this.name = name;
    }
    
    @Override
    public void logDebug(final String str) {
        final StringBuilder sb = new StringBuilder(String.valueOf(this.name));
        sb.append(":");
        sb.append(str);
        Log.d("isoparser", sb.toString());
    }
}
