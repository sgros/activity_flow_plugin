// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.activity;

import org.mozilla.focus.locale.LocaleAwareApplication;
import org.mozilla.focus.locale.Locales;
import android.os.Bundle;
import org.mozilla.focus.locale.LocaleManager;
import android.content.res.Configuration;
import android.content.Intent;
import android.view.WindowManager$LayoutParams;
import android.view.Window;
import android.content.Context;
import org.mozilla.focus.utils.Settings;
import android.support.v4.view.ViewCompat;
import android.support.v4.text.TextUtilsCompat;
import android.view.View;
import java.util.Locale;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity
{
    private volatile Locale mLastLocale;
    
    public static void setLayoutDirection(final View view, final Locale locale) {
        if (TextUtilsCompat.getLayoutDirectionFromLocale(locale) != 1) {
            ViewCompat.setLayoutDirection(view, 0);
        }
        else {
            ViewCompat.setLayoutDirection(view, 1);
        }
    }
    
    private void updateScreenBrightness() {
        final Settings instance = Settings.getInstance((Context)this);
        float nightModeBrightnessValue = instance.getNightModeBrightnessValue();
        final Window window = this.getWindow();
        if (window != null) {
            final WindowManager$LayoutParams attributes = window.getAttributes();
            if (!instance.isNightModeEnable()) {
                nightModeBrightnessValue = -1.0f;
            }
            attributes.screenBrightness = nightModeBrightnessValue;
            window.setAttributes(attributes);
        }
    }
    
    public abstract void applyLocale();
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        this.onConfigurationChanged(this.getResources().getConfiguration());
        if (n2 == 1) {
            this.applyLocale();
        }
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        final LocaleManager instance = LocaleManager.getInstance();
        instance.correctLocale((Context)this, this.getResources(), this.getResources().getConfiguration());
        final Locale onSystemConfigurationChanged = instance.onSystemConfigurationChanged((Context)this, this.getResources(), configuration, this.mLastLocale);
        if (onSystemConfigurationChanged != null) {
            this.mLastLocale = onSystemConfigurationChanged;
            LocaleManager.getInstance().updateConfiguration((Context)this, onSystemConfigurationChanged);
            this.applyLocale();
            setLayoutDirection(this.getWindow().getDecorView(), onSystemConfigurationChanged);
        }
        super.onConfigurationChanged(configuration);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        Locales.initializeLocale((Context)this);
        this.mLastLocale = LocaleManager.getInstance().getCurrentLocale(this.getApplicationContext());
        LocaleManager.getInstance().updateConfiguration((Context)this, this.mLastLocale);
        super.onCreate(bundle);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        ((LocaleAwareApplication)this.getApplicationContext()).onActivityPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.updateScreenBrightness();
        ((LocaleAwareApplication)this.getApplicationContext()).onActivityResume();
    }
    
    public void openPreferences() {
        this.startActivityForResult(new Intent((Context)this, (Class)SettingsActivity.class), 0);
    }
}
