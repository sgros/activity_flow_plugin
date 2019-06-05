// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.view.MenuItem;
import android.annotation.TargetApi;
import android.app.Activity;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.Context;
import android.preference.PreferenceManager;
import android.preference.ListPreference;
import android.util.Log;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceChangeListener;

public class SettingsActivity extends BaseActivity
{
    private static Preference$OnPreferenceChangeListener sBindPreferenceSummaryToValueListener;
    
    static {
        SettingsActivity.sBindPreferenceSummaryToValueListener = (Preference$OnPreferenceChangeListener)new Preference$OnPreferenceChangeListener() {
            public boolean onPreferenceChange(final Preference preference, final Object o) {
                final String string = o.toString();
                Log.d("NetMonitor", string);
                if (preference instanceof ListPreference) {
                    final ListPreference listPreference = (ListPreference)preference;
                    final int indexOfValue = listPreference.findIndexOfValue(string);
                    CharSequence summary;
                    if (indexOfValue >= 0) {
                        summary = listPreference.getEntries()[indexOfValue];
                    }
                    else {
                        summary = null;
                    }
                    preference.setSummary(summary);
                }
                else {
                    preference.setSummary((CharSequence)string);
                }
                return true;
            }
        };
    }
    
    private static void bindPreferenceSummaryToValue(final Preference preference) {
        preference.setOnPreferenceChangeListener(SettingsActivity.sBindPreferenceSummaryToValueListener);
        SettingsActivity.sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, (Object)PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }
    
    private static boolean isXLargeTablet(final Context context) {
        return (context.getResources().getConfiguration().screenLayout & 0xF) >= 4;
    }
    
    @Override
    protected int getNavigationDrawerID() {
        return 2131296410;
    }
    
    protected boolean isValidFragment(final String s) {
        return PreferenceFragment.class.getName().equals(s) || GeneralPreferenceFragment.class.getName().equals(s);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        RunStore.setContext(this);
        this.setContentView(2131427368);
        this.overridePendingTransition(0, 0);
    }
    
    @TargetApi(11)
    public static class GeneralPreferenceFragment extends PreferenceFragment
    {
        public void onCreate(final Bundle bundle) {
            super.onCreate(bundle);
            this.addPreferencesFromResource(2131820544);
        }
        
        public boolean onOptionsItemSelected(final MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                this.startActivity(new Intent((Context)this.getActivity(), (Class)SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }
}
