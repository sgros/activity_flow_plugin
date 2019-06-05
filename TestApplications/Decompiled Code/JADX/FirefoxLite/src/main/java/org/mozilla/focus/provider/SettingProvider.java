package org.mozilla.focus.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SettingProvider.kt */
public final class SettingProvider extends ContentProvider {
    public static final Companion Companion = new Companion();
    private static final UriMatcher uriMatcher = new UriMatcher(-1);

    /* compiled from: SettingProvider.kt */
    private static final class BundleCursor extends MatrixCursor {
        private Bundle bundle;

        public BundleCursor(Bundle bundle) {
            super(new String[0], 0);
            this.bundle = bundle;
        }

        public Bundle getExtras() {
            return this.bundle;
        }

        public Bundle respond(Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(bundle, "extras");
            this.bundle = bundle;
            return this.bundle;
        }
    }

    /* compiled from: SettingProvider.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public boolean onCreate() {
        return true;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }

    public String getType(Uri uri) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        String str3 = (String) null;
        str = "";
        if (strArr2 != null) {
            str3 = strArr2[0];
            str = strArr2[1];
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle bundle = new Bundle();
        switch (uriMatcher.match(uri)) {
            case 1:
                bundle.putFloat("key", defaultSharedPreferences.getFloat(str3, Float.parseFloat(str)));
                break;
            case 2:
                bundle.putBoolean("key", defaultSharedPreferences.getBoolean(str3, Boolean.parseBoolean(str)));
                break;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown uri：");
                stringBuilder.append(uri);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
        return new BundleCursor(bundle);
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        throw new UnsupportedOperationException("Not supported");
    }

    static {
        uriMatcher.addURI("org.mozilla.rocket.provider.settingprovider", "getFloat", 1);
        uriMatcher.addURI("org.mozilla.rocket.provider.settingprovider", "getBoolean", 2);
    }
}
