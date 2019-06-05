package org.mozilla.focus.locale;

import android.app.Application;
import android.content.res.Configuration;

public class LocaleAwareApplication extends Application {
    private boolean mInBackground;

    public void onCreate() {
        Locales.initializeLocale(this);
        super.onCreate();
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mInBackground) {
            super.onConfigurationChanged(configuration);
            return;
        }
        try {
            LocaleManager.getInstance().correctLocale(this, getResources(), configuration);
        } catch (IllegalStateException unused) {
        }
        super.onConfigurationChanged(configuration);
    }

    public void onActivityPause() {
        this.mInBackground = true;
    }

    public void onActivityResume() {
        this.mInBackground = true;
    }
}
