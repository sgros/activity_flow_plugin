// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge;

import android.preference.PreferenceManager;
import android.view.View;
import android.content.Context;
import android.webkit.WebView;
import android.os.Bundle;
import android.app.Activity;

public class InfoView extends Activity
{
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final WebView contentView = new WebView((Context)this);
        contentView.loadUrl("file:///android_asset/info.xml");
        this.setContentView((View)contentView);
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
