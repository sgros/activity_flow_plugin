// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.theme;

import android.content.res.Resources$Theme;
import java.util.Iterator;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import java.util.HashSet;
import android.content.Context;

public class ThemeManager
{
    private Context baseContext;
    private ThemeSet currentThemeSet;
    private boolean dirty;
    private HashSet<Themeable> subscribedThemeable;
    
    public ThemeManager(final ThemeHost themeHost) {
        this.currentThemeSet = ThemeSet.Default;
        this.subscribedThemeable = new HashSet<Themeable>(3);
        this.dirty = true;
        this.baseContext = themeHost.getApplicationContext();
        this.currentThemeSet = loadCurrentTheme(getSharedPreferences(this.baseContext));
    }
    
    public static void dismissOnboarding(final Context context) {
        getSharedPreferences(context).edit().putInt("pref_key_int_onboarding_version", 1).apply();
    }
    
    private static ThemeSet findNextTheme(final ThemeSet set) {
        return ThemeSet.values()[(set.ordinal() + 1) % ThemeSet.values().length];
    }
    
    public static String getCurrentThemeName(final Context context) {
        return loadCurrentTheme(getSharedPreferences(context)).name();
    }
    
    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    private static ThemeSet loadCurrentTheme(SharedPreferences value) {
        final String string = value.getString("pref_key_string_current_theme", ThemeSet.Default.name());
        try {
            value = (SharedPreferences)ThemeSet.valueOf(string);
        }
        catch (Exception ex) {
            final Object default1 = ThemeSet.Default;
            saveCurrentTheme(value, (ThemeSet)default1);
            value = (SharedPreferences)default1;
        }
        return (ThemeSet)value;
    }
    
    private void notifyThemeChange() {
        final Iterator<Themeable> iterator = this.subscribedThemeable.iterator();
        while (iterator.hasNext()) {
            iterator.next().onThemeChanged();
        }
    }
    
    private static void saveCurrentTheme(final SharedPreferences sharedPreferences, final ThemeSet set) {
        sharedPreferences.edit().putString("pref_key_string_current_theme", set.name()).apply();
    }
    
    private void setCurrentTheme(final ThemeSet currentThemeSet) {
        saveCurrentTheme(getSharedPreferences(this.baseContext), currentThemeSet);
        this.currentThemeSet = currentThemeSet;
        this.dirty = true;
    }
    
    public static boolean shouldShowOnboarding(final Context context) {
        final int int1 = getSharedPreferences(context).getInt("pref_key_int_onboarding_version", 0);
        boolean b = true;
        if (int1 >= 1) {
            b = false;
        }
        return b;
    }
    
    public void applyCurrentTheme(final Resources$Theme resources$Theme) {
        if (this.dirty) {
            this.dirty = false;
            resources$Theme.applyStyle(this.currentThemeSet.style, true);
        }
    }
    
    public void resetDefaultTheme() {
        this.setCurrentTheme(ThemeSet.Default);
        this.notifyThemeChange();
    }
    
    public void subscribeThemeChange(final Themeable e) {
        this.subscribedThemeable.add(e);
    }
    
    public ThemeSet toggleNextTheme() {
        final ThemeSet nextTheme = findNextTheme(loadCurrentTheme(getSharedPreferences(this.baseContext)));
        this.setCurrentTheme(nextTheme);
        this.notifyThemeChange();
        return nextTheme;
    }
    
    public void unsubscribeThemeChange(final Themeable o) {
        this.subscribedThemeable.remove(o);
    }
    
    public interface ThemeHost
    {
        Context getApplicationContext();
        
        ThemeManager getThemeManager();
    }
    
    public enum ThemeSet
    {
        BlueViolet(2131820989), 
        CatalinaBlue(2131820987), 
        CornflowerBlue(2131820990), 
        Default(2131820992), 
        Gossamer(2131820988), 
        Rocket(2131820991);
        
        final int style;
        
        private ThemeSet(final int style) {
            this.style = style;
        }
    }
    
    public interface Themeable
    {
        void onThemeChanged();
    }
}
