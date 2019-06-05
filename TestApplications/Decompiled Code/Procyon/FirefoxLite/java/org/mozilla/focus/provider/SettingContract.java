// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.net.Uri;

public final class SettingContract
{
    private static final Uri AUTHORITY_URI;
    public static final SettingContract INSTANCE;
    
    static {
        INSTANCE = new SettingContract();
        AUTHORITY_URI = Uri.parse("content://org.mozilla.rocket.provider.settingprovider");
    }
    
    private SettingContract() {
    }
    
    public final Uri getAUTHORITY_URI() {
        return SettingContract.AUTHORITY_URI;
    }
}
