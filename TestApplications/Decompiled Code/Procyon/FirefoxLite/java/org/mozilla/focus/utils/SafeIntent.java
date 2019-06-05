// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.util.Log;
import android.content.Intent;

public class SafeIntent
{
    private static final String LOGTAG;
    private final Intent intent;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append("Gecko");
        sb.append(SafeIntent.class.getSimpleName());
        LOGTAG = sb.toString();
    }
    
    public SafeIntent(final Intent intent) {
        this.intent = intent;
    }
    
    public String getAction() {
        return this.intent.getAction();
    }
    
    public boolean getBooleanExtra(final String s, final boolean b) {
        try {
            return this.intent.getBooleanExtra(s, b);
        }
        catch (RuntimeException ex) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent extras.", (Throwable)ex);
            return b;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
            return b;
        }
    }
    
    public CharSequence getCharSequenceExtra(final String s) {
        try {
            return this.intent.getCharSequenceExtra(s);
        }
        catch (RuntimeException ex) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent extras.", (Throwable)ex);
            return null;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
            return null;
        }
    }
    
    public String getDataString() {
        try {
            return this.intent.getDataString();
        }
        catch (RuntimeException ex) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent data string.", (Throwable)ex);
            return null;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent data string: OOM. Malformed?");
            return null;
        }
    }
    
    public String getStringExtra(String stringExtra) {
        try {
            stringExtra = this.intent.getStringExtra(stringExtra);
            return stringExtra;
        }
        catch (RuntimeException ex) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent extras.", (Throwable)ex);
            return null;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.w(SafeIntent.LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
            return null;
        }
    }
}
