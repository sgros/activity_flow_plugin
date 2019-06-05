package org.mozilla.focus.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.p001v4.text.TextUtilsCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p004v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import java.util.Locale;
import org.mozilla.focus.locale.LocaleAwareApplication;
import org.mozilla.focus.locale.LocaleManager;
import org.mozilla.focus.locale.Locales;
import org.mozilla.focus.utils.Settings;

public abstract class BaseActivity extends AppCompatActivity {
    private volatile Locale mLastLocale;

    public abstract void applyLocale();

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        Locales.initializeLocale(this);
        this.mLastLocale = LocaleManager.getInstance().getCurrentLocale(getApplicationContext());
        LocaleManager.getInstance().updateConfiguration(this, this.mLastLocale);
        super.onCreate(bundle);
    }

    public void onConfigurationChanged(Configuration configuration) {
        LocaleManager instance = LocaleManager.getInstance();
        instance.correctLocale(this, getResources(), getResources().getConfiguration());
        Locale onSystemConfigurationChanged = instance.onSystemConfigurationChanged(this, getResources(), configuration, this.mLastLocale);
        if (onSystemConfigurationChanged != null) {
            this.mLastLocale = onSystemConfigurationChanged;
            LocaleManager.getInstance().updateConfiguration(this, onSystemConfigurationChanged);
            applyLocale();
            setLayoutDirection(getWindow().getDecorView(), onSystemConfigurationChanged);
        }
        super.onConfigurationChanged(configuration);
    }

    public static void setLayoutDirection(View view, Locale locale) {
        if (TextUtilsCompat.getLayoutDirectionFromLocale(locale) != 1) {
            ViewCompat.setLayoutDirection(view, 0);
        } else {
            ViewCompat.setLayoutDirection(view, 1);
        }
    }

    public void openPreferences() {
        startActivityForResult(new Intent(this, SettingsActivity.class), 0);
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        onConfigurationChanged(getResources().getConfiguration());
        if (i2 == 1) {
            applyLocale();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        updateScreenBrightness();
        ((LocaleAwareApplication) getApplicationContext()).onActivityResume();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        ((LocaleAwareApplication) getApplicationContext()).onActivityPause();
    }

    private void updateScreenBrightness() {
        Settings instance = Settings.getInstance(this);
        float nightModeBrightnessValue = instance.getNightModeBrightnessValue();
        Window window = getWindow();
        if (window != null) {
            LayoutParams attributes = window.getAttributes();
            if (!instance.isNightModeEnable()) {
                nightModeBrightnessValue = -1.0f;
            }
            attributes.screenBrightness = nightModeBrightnessValue;
            window.setAttributes(attributes);
        }
    }
}
