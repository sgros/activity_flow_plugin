// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class AdjustReferrerReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        final String stringExtra = intent.getStringExtra("referrer");
        if (stringExtra == null) {
            return;
        }
        String decode;
        try {
            decode = URLDecoder.decode(stringExtra, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            decode = "malformed";
        }
        Adjust.getDefaultInstance().sendReferrer(decode);
    }
}
