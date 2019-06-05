package org.mozilla.rocket.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.preference.PreferenceManager;
import java.util.HashSet;
import java.util.Iterator;
import org.mozilla.rocket.C0769R;

public class ThemeManager {
    private Context baseContext;
    private ThemeSet currentThemeSet = ThemeSet.Default;
    private boolean dirty = true;
    private HashSet<Themeable> subscribedThemeable = new HashSet(3);

    public interface ThemeHost {
        Context getApplicationContext();

        ThemeManager getThemeManager();
    }

    public enum ThemeSet {
        Default(C0769R.style.ThemeToyDefault),
        CatalinaBlue(C0769R.style.ThemeToy01),
        Gossamer(C0769R.style.ThemeToy02),
        BlueViolet(C0769R.style.ThemeToy03),
        CornflowerBlue(C0769R.style.ThemeToy04),
        Rocket(C0769R.style.ThemeToy05);
        
        final int style;

        private ThemeSet(int i) {
            this.style = i;
        }
    }

    public interface Themeable {
        void onThemeChanged();
    }

    public ThemeManager(ThemeHost themeHost) {
        this.baseContext = themeHost.getApplicationContext();
        this.currentThemeSet = loadCurrentTheme(getSharedPreferences(this.baseContext));
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void resetDefaultTheme() {
        setCurrentTheme(ThemeSet.Default);
        notifyThemeChange();
    }

    private void setCurrentTheme(ThemeSet themeSet) {
        saveCurrentTheme(getSharedPreferences(this.baseContext), themeSet);
        this.currentThemeSet = themeSet;
        this.dirty = true;
    }

    public static boolean shouldShowOnboarding(Context context) {
        return getSharedPreferences(context).getInt("pref_key_int_onboarding_version", 0) < 1;
    }

    public static void dismissOnboarding(Context context) {
        getSharedPreferences(context).edit().putInt("pref_key_int_onboarding_version", 1).apply();
    }

    public static String getCurrentThemeName(Context context) {
        return loadCurrentTheme(getSharedPreferences(context)).name();
    }

    private static ThemeSet loadCurrentTheme(SharedPreferences sharedPreferences) {
        try {
            return ThemeSet.valueOf(sharedPreferences.getString("pref_key_string_current_theme", ThemeSet.Default.name()));
        } catch (Exception unused) {
            ThemeSet themeSet = ThemeSet.Default;
            saveCurrentTheme(sharedPreferences, themeSet);
            return themeSet;
        }
    }

    private static void saveCurrentTheme(SharedPreferences sharedPreferences, ThemeSet themeSet) {
        sharedPreferences.edit().putString("pref_key_string_current_theme", themeSet.name()).apply();
    }

    private static ThemeSet findNextTheme(ThemeSet themeSet) {
        return ThemeSet.values()[(themeSet.ordinal() + 1) % ThemeSet.values().length];
    }

    public void applyCurrentTheme(Theme theme) {
        if (this.dirty) {
            this.dirty = false;
            theme.applyStyle(this.currentThemeSet.style, true);
        }
    }

    public ThemeSet toggleNextTheme() {
        ThemeSet findNextTheme = findNextTheme(loadCurrentTheme(getSharedPreferences(this.baseContext)));
        setCurrentTheme(findNextTheme);
        notifyThemeChange();
        return findNextTheme;
    }

    private void notifyThemeChange() {
        Iterator it = this.subscribedThemeable.iterator();
        while (it.hasNext()) {
            ((Themeable) it.next()).onThemeChanged();
        }
    }

    public void subscribeThemeChange(Themeable themeable) {
        this.subscribedThemeable.add(themeable);
    }

    public void unsubscribeThemeChange(Themeable themeable) {
        this.subscribedThemeable.remove(themeable);
    }
}
