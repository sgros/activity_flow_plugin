package org.mozilla.focus.utils;

import android.content.Intent;
import android.util.Log;

public class SafeIntent {
    private static final String LOGTAG;
    private final Intent intent;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Gecko");
        stringBuilder.append(SafeIntent.class.getSimpleName());
        LOGTAG = stringBuilder.toString();
    }

    public SafeIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean getBooleanExtra(String str, boolean z) {
        try {
            return this.intent.getBooleanExtra(str, z);
        } catch (OutOfMemoryError unused) {
            Log.w(LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
            return z;
        } catch (RuntimeException e) {
            Log.w(LOGTAG, "Couldn't get intent extras.", e);
            return z;
        }
    }

    public String getStringExtra(String str) {
        try {
            return this.intent.getStringExtra(str);
        } catch (OutOfMemoryError unused) {
            Log.w(LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
            return null;
        } catch (RuntimeException e) {
            Log.w(LOGTAG, "Couldn't get intent extras.", e);
            return null;
        }
    }

    public CharSequence getCharSequenceExtra(String str) {
        try {
            return this.intent.getCharSequenceExtra(str);
        } catch (OutOfMemoryError unused) {
            Log.w(LOGTAG, "Couldn't get intent extras: OOM. Malformed?");
            return null;
        } catch (RuntimeException e) {
            Log.w(LOGTAG, "Couldn't get intent extras.", e);
            return null;
        }
    }

    public String getAction() {
        return this.intent.getAction();
    }

    public String getDataString() {
        try {
            return this.intent.getDataString();
        } catch (OutOfMemoryError unused) {
            Log.w(LOGTAG, "Couldn't get intent data string: OOM. Malformed?");
            return null;
        } catch (RuntimeException e) {
            Log.w(LOGTAG, "Couldn't get intent data string.", e);
            return null;
        }
    }
}
