// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.database.MatrixCursor;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.database.Cursor;
import android.content.ContentValues;
import kotlin.jvm.internal.Intrinsics;
import android.net.Uri;
import android.content.UriMatcher;
import android.content.ContentProvider;

public final class SettingProvider extends ContentProvider
{
    public static final Companion Companion;
    private static final UriMatcher uriMatcher;
    
    static {
        Companion = new Companion(null);
        (uriMatcher = new UriMatcher(-1)).addURI("org.mozilla.rocket.provider.settingprovider", "getFloat", 1);
        SettingProvider.uriMatcher.addURI("org.mozilla.rocket.provider.settingprovider", "getBoolean", 2);
    }
    
    public int delete(final Uri uri, final String s, final String[] array) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }
    
    public String getType(final Uri uri) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }
    
    public boolean onCreate() {
        return true;
    }
    
    public Cursor query(final Uri obj, final String[] array, String s, final String[] array2, final String s2) {
        Intrinsics.checkParameterIsNotNull(obj, "uri");
        s = null;
        String s3 = "";
        if (array2 != null) {
            s = array2[0];
            s3 = array2[1];
        }
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        final Bundle bundle = new Bundle();
        switch (SettingProvider.uriMatcher.match(obj)) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown uri\uff1a");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 2: {
                bundle.putBoolean("key", defaultSharedPreferences.getBoolean(s, Boolean.parseBoolean(s3)));
                break;
            }
            case 1: {
                bundle.putFloat("key", defaultSharedPreferences.getFloat(s, Float.parseFloat(s3)));
                break;
            }
        }
        return (Cursor)new BundleCursor(bundle);
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }
    
    private static final class BundleCursor extends MatrixCursor
    {
        private Bundle bundle;
        
        public BundleCursor(final Bundle bundle) {
            super(new String[0], 0);
            this.bundle = bundle;
        }
        
        public Bundle getExtras() {
            return this.bundle;
        }
        
        public Bundle respond(final Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(bundle, "extras");
            return this.bundle = bundle;
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
