package com.googlecode.mp4parser.util;

import android.util.Log;

public class AndroidLogger extends Logger {
    String name;

    public AndroidLogger(String str) {
        this.name = str;
    }

    public void logDebug(String str) {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.name));
        stringBuilder.append(":");
        stringBuilder.append(str);
        Log.d("isoparser", stringBuilder.toString());
    }
}
