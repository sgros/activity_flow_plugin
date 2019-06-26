// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity;

import android.content.res.Configuration;
import java.util.ArrayList;
import menion.android.whereyougo.utils.StringToken;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.ManagerNotify;
import android.content.SharedPreferences;
import android.app.Activity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.preferences.PreviewPreference;
import menion.android.whereyougo.MainApplication;
import java.io.File;
import android.app.Dialog;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import menion.android.whereyougo.utils.A;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.AdapterView;
import android.content.Context;
import android.preference.PreferenceManager;
import android.os.Bundle;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;
import android.net.Uri;
import android.content.Intent;
import menion.android.whereyougo.preferences.Locale;
import android.preference.Preference;
import android.preference.Preference$OnPreferenceClickListener;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class XmlSettingsActivity extends PreferenceActivity implements SharedPreferences$OnSharedPreferenceChangeListener, Preference$OnPreferenceClickListener
{
    private static final String TAG = "XmlSettingsActivity";
    private boolean needRestart;
    
    private Preference findPreference(final int n) {
        return this.findPreference((CharSequence)getKey(n));
    }
    
    private static String getKey(final int n) {
        return Locale.getString(n);
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 2131165574 && n2 == -1 && intent != null) {
            final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri != null) {
                Logger.d("XmlSettingsActivity", "uri:" + uri.toString());
                Preferences.setStringPreference(2131165574, 2);
                Preferences.setStringPreference(2131165575, uri.toString());
                Preferences.GUIDING_WAYPOINT_SOUND = Utils.parseInt(2131165598);
                Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = uri.toString();
            }
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setTitle(2131165307);
        this.needRestart = false;
        switch (Preferences.APPEARANCE_FONT_SIZE) {
            case 1: {
                this.setTheme(2131361803);
                break;
            }
            case 2: {
                this.setTheme(2131361802);
                break;
            }
            case 3: {
                this.setTheme(2131361801);
                break;
            }
        }
        this.addPreferencesFromResource(2131034113);
        PreferenceManager.getDefaultSharedPreferences((Context)this).registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        this.getPreferenceScreen().removePreference(this.findPreference(2131165591));
        this.findPreference(2131165582).setOnPreferenceClickListener((Preference$OnPreferenceClickListener)this);
        final Preference preference = this.findPreference(2131165590);
        if (preference != null) {
            preference.setOnPreferenceClickListener((Preference$OnPreferenceClickListener)this);
        }
        if (!Utils.isAndroid201OrMore()) {
            final Preference preference2 = this.findPreference(2131165584);
            if (preference2 != null) {
                preference2.setEnabled(false);
            }
        }
        if (this.getIntent() != null && this.getIntent().hasExtra(this.getString(2131165592))) {
            final Preference preference3 = this.findPreference(2131165592);
            if (preference3 != null) {
                final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
                for (int i = 0; i < preferenceScreen.getPreferenceCount(); ++i) {
                    if (preferenceScreen.getPreference(i) == preference3) {
                        this.getIntent().putExtra(this.getString(2131165592), false);
                        preferenceScreen.onItemClick((AdapterView)null, (View)null, i, 0L);
                        break;
                    }
                }
            }
        }
    }
    
    public void onDestroy() {
        try {
            super.onDestroy();
            if (this.needRestart) {
                A.getMain().showDialogFinish(2);
            }
        }
        catch (Exception ex) {
            Logger.e(this.getLocalClassName(), "onDestroy()", ex);
        }
    }
    
    public boolean onPreferenceClick(final Preference preference) {
        final String key = preference.getKey();
        if (!key.equals("")) {
            if (key.equals(this.getString(2131165582))) {
                UtilsGUI.dialogDoItem((Activity)this, this.getText(2131165375), 2130837578, this.getText(2131165376), this.getString(2131165190), null, this.getString(2131165340), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final FileChooserDialog fileChooserDialog = new FileChooserDialog((Context)XmlSettingsActivity.this);
                        fileChooserDialog.loadFolder(Preferences.GLOBAL_ROOT);
                        fileChooserDialog.setFolderMode(true);
                        fileChooserDialog.setCanCreateFiles(false);
                        fileChooserDialog.setShowCancelButton(true);
                        fileChooserDialog.addListener((FileChooserDialog.OnFileSelectedListener)new FileChooserDialog.OnFileSelectedListener() {
                            @Override
                            public void onFileSelected(final Dialog dialog, final File file) {
                                dialog.dismiss();
                                if (((MainApplication)A.getApp()).setRoot(file.getAbsolutePath())) {
                                    ((PreviewPreference)XmlSettingsActivity.this.findPreference(2131165582)).setValue(FileSystem.ROOT);
                                    MainActivity.refreshCartridges();
                                }
                            }
                            
                            @Override
                            public void onFileSelected(final Dialog dialog, final File file, final String str) {
                                final String string = file.getAbsolutePath() + "/" + str;
                                new File(string).mkdir();
                                ((FileChooserDialog)dialog).loadFolder(string);
                            }
                        });
                        fileChooserDialog.show();
                    }
                }, this.getString(2131165339), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        if (((MainApplication)A.getApp()).setRoot(null)) {
                            ((PreviewPreference)XmlSettingsActivity.this.findPreference(2131165582)).setValue(FileSystem.ROOT);
                            MainActivity.refreshCartridges();
                        }
                    }
                });
            }
            else if (key.equals(this.getString(2131165590))) {}
        }
        return false;
    }
    
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        if (!s.equals("X")) {
            if (Preferences.comparePreferenceKey(s, 2131165569)) {
                Preferences.APPEARANCE_FONT_SIZE = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165551)) {
                Preferences.APPEARANCE_FULLSCREEN = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165552)) {
                Preferences.GPS = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165555)) {
                Preferences.GPS_START_AUTOMATICALLY = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165572)) {
                Preferences.GPS_ALTITUDE_CORRECTION = Utils.parseDouble(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165553)) {
                Preferences.GPS_BEEP_ON_GPS_FIX = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165554)) {
                Preferences.GPS_DISABLE_WHEN_HIDE = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165556)) {
                Preferences.GUIDING_SOUNDS = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165557)) {
                Preferences.GUIDING_GPS_REQUIRED = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165574)) {
                final int int1 = Utils.parseInt(sharedPreferences.getString(s, (String)null));
                if (int1 == 2) {
                    final Intent intent = new Intent("android.intent.action.PICK");
                    intent.setType("audio/*");
                    Intent intent2 = intent;
                    if (!Utils.isIntentAvailable(intent)) {
                        intent2 = new Intent("android.intent.action.RINGTONE_PICKER");
                    }
                    this.startActivityForResult(intent2, 2131165574);
                }
                else {
                    Preferences.GUIDING_WAYPOINT_SOUND = int1;
                }
            }
            else if (Preferences.comparePreferenceKey(s, 2131165576)) {
                final int int2 = Utils.parseInt(sharedPreferences.getString(s, (String)null));
                if (int2 > 0) {
                    Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE = int2;
                }
                else {
                    ManagerNotify.toastShortMessage(2131165215);
                }
            }
            else if (Preferences.comparePreferenceKey(s, 2131165577)) {
                Preferences.GUIDING_ZONE_NAVIGATION_POINT = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165558)) {
                Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
                A.getRotator().manageSensors();
            }
            else if (Preferences.comparePreferenceKey(s, 2131165578)) {
                final int int3 = Utils.parseInt(sharedPreferences.getString(s, (String)null));
                if (int3 > 0) {
                    Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = int3;
                }
                else {
                    ManagerNotify.toastShortMessage(2131165215);
                }
            }
            else if (Preferences.comparePreferenceKey(s, 2131165562)) {
                Preferences.SENSOR_HARDWARE_COMPASS = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
                A.getRotator().manageSensors();
            }
            else if (Preferences.comparePreferenceKey(s, 2131165579)) {
                Preferences.APPEARANCE_HIGHLIGHT = Utils.parseInt(sharedPreferences.getString(s, (String)null));
                PreferenceValues.enableWakeLock();
            }
            else if (Preferences.comparePreferenceKey(s, 2131165559)) {
                Preferences.APPEARANCE_IMAGE_STRETCH = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165580)) {
                final String string = sharedPreferences.getString(s, "");
                final ArrayList<String> parse = StringToken.parse(string, "_");
                final Configuration configuration = this.getBaseContext().getResources().getConfiguration();
                java.util.Locale locale;
                if ("default".equals(string)) {
                    locale = java.util.Locale.getDefault();
                }
                else if (parse.size() == 1) {
                    locale = new java.util.Locale(string);
                }
                else if (parse.size() == 2) {
                    locale = new java.util.Locale(parse.get(0), parse.get(1));
                }
                else {
                    locale = configuration.locale;
                }
                if (!configuration.locale.getLanguage().equals(locale.getLanguage())) {
                    configuration.locale = locale;
                    this.getBaseContext().getResources().updateConfiguration(configuration, this.getBaseContext().getResources().getDisplayMetrics());
                    this.needRestart = true;
                }
            }
            else if (Preferences.comparePreferenceKey(s, 2131165581)) {
                Preferences.GLOBAL_MAP_PROVIDER = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165560)) {
                Preferences.GLOBAL_SAVEGAME_AUTO = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165583)) {
                final int int4 = Utils.parseInt(sharedPreferences.getString(s, (String)null));
                if (int4 >= 0) {
                    Preferences.GLOBAL_SAVEGAME_SLOTS = int4;
                }
                else {
                    ManagerNotify.toastShortMessage(2131165215);
                }
            }
            else if (Preferences.comparePreferenceKey(s, 2131165550)) {
                Preferences.GLOBAL_DOUBLE_CLICK = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165571)) {
                Preferences.GC_USERNAME = sharedPreferences.getString(s, (String)null);
            }
            else if (Preferences.comparePreferenceKey(s, 2131165570)) {
                Preferences.GC_PASSWORD = sharedPreferences.getString(s, (String)null);
            }
            else if (Preferences.comparePreferenceKey(s, 2131165561)) {
                Preferences.SENSOR_BEARING_TRUE = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165584)) {
                Preferences.SENSOR_ORIENT_FILTER = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165563)) {
                Preferences.APPEARANCE_STATUSBAR = Utils.parseBoolean(sharedPreferences.getBoolean(s, false));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165585)) {
                Preferences.FORMAT_ALTITUDE = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165586)) {
                Preferences.FORMAT_ANGLE = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165587)) {
                Preferences.FORMAT_COO_LATLON = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165588)) {
                Preferences.FORMAT_LENGTH = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
            else if (Preferences.comparePreferenceKey(s, 2131165589)) {
                Preferences.FORMAT_SPEED = Utils.parseInt(sharedPreferences.getString(s, (String)null));
            }
        }
    }
}
