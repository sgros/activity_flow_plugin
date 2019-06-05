// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.database.Cursor;
import android.content.Context;
import org.mozilla.focus.utils.FirebaseHelper;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.content.ContentValues;
import android.net.Uri;
import android.content.ContentProvider;

public class FirebaseHelperProvider extends ContentProvider
{
    public int delete(final Uri uri, final String s, final String[] array) {
        return 0;
    }
    
    public String getType(final Uri uri) {
        return null;
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        return null;
    }
    
    public boolean onCreate() {
        final Context context = this.getContext();
        FirebaseHelper.init(context, TelemetryWrapper.isTelemetryEnabled(context));
        return true;
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        return null;
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        return 0;
    }
}
