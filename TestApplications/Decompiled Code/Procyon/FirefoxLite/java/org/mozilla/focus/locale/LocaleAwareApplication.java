// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.locale;

import android.content.Context;
import android.content.res.Configuration;
import android.app.Application;

public class LocaleAwareApplication extends Application
{
    private boolean mInBackground;
    
    public void onActivityPause() {
        this.mInBackground = true;
    }
    
    public void onActivityResume() {
        this.mInBackground = true;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.mInBackground) {
            super.onConfigurationChanged(configuration);
            return;
        }
        while (true) {
            try {
                LocaleManager.getInstance().correctLocale((Context)this, this.getResources(), configuration);
                super.onConfigurationChanged(configuration);
            }
            catch (IllegalStateException ex) {
                continue;
            }
            break;
        }
    }
    
    public void onCreate() {
        Locales.initializeLocale((Context)this);
        super.onCreate();
    }
}
