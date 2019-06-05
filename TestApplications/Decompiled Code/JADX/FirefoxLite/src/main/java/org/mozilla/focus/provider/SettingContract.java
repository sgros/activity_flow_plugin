package org.mozilla.focus.provider;

import android.net.Uri;

/* compiled from: SettingContract.kt */
public final class SettingContract {
    private static final Uri AUTHORITY_URI = Uri.parse("content://org.mozilla.rocket.provider.settingprovider");
    public static final SettingContract INSTANCE = new SettingContract();

    private SettingContract() {
    }

    public final Uri getAUTHORITY_URI() {
        return AUTHORITY_URI;
    }
}
