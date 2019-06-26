// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.preferences;

import android.content.Context;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class EditPreferences extends PreferenceActivity
{
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.addPreferencesFromResource(2131034112);
    }
    
    protected void onResume() {
        super.onResume();
        if (PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("fullscreen", false)) {
            this.getWindow().addFlags(1024);
            this.getWindow().clearFlags(2048);
        }
        else {
            this.getWindow().clearFlags(1024);
            this.getWindow().addFlags(2048);
        }
    }
}
