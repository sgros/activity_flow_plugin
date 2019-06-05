package menion.android.whereyougo.gui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import java.io.File;
import java.util.ArrayList;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.preferences.PreviewPreference;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.StringToken;
import menion.android.whereyougo.utils.Utils;
import p003ar.com.daidalos.afiledialog.FileChooserDialog;
import p003ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

public class XmlSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {
    private static final String TAG = "XmlSettingsActivity";
    private boolean needRestart;

    /* renamed from: menion.android.whereyougo.gui.activity.XmlSettingsActivity$1 */
    class C02691 implements OnClickListener {

        /* renamed from: menion.android.whereyougo.gui.activity.XmlSettingsActivity$1$1 */
        class C02701 implements OnFileSelectedListener {
            C02701() {
            }

            public void onFileSelected(Dialog source, File folder) {
                source.dismiss();
                if (((MainApplication) C0322A.getApp()).setRoot(folder.getAbsolutePath())) {
                    ((PreviewPreference) XmlSettingsActivity.this.findPreference(C0254R.string.pref_KEY_S_ROOT)).setValue(FileSystem.ROOT);
                    MainActivity.refreshCartridges();
                }
            }

            public void onFileSelected(Dialog source, File folder, String name) {
                String newFolder = folder.getAbsolutePath() + "/" + name;
                new File(newFolder).mkdir();
                ((FileChooserDialog) source).loadFolder(newFolder);
            }
        }

        C02691() {
        }

        public void onClick(DialogInterface dialog, int which) {
            FileChooserDialog selectDialog = new FileChooserDialog(XmlSettingsActivity.this);
            selectDialog.loadFolder(Preferences.GLOBAL_ROOT);
            selectDialog.setFolderMode(true);
            selectDialog.setCanCreateFiles(false);
            selectDialog.setShowCancelButton(true);
            selectDialog.addListener(new C02701());
            selectDialog.show();
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.XmlSettingsActivity$2 */
    class C02712 implements OnClickListener {
        C02712() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (((MainApplication) C0322A.getApp()).setRoot(null)) {
                ((PreviewPreference) XmlSettingsActivity.this.findPreference(C0254R.string.pref_KEY_S_ROOT)).setValue(FileSystem.ROOT);
                MainActivity.refreshCartridges();
            }
        }
    }

    private static String getKey(int prefKeyId) {
        return Locale.getString(prefKeyId);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(C0254R.string.settings);
        this.needRestart = false;
        switch (Preferences.APPEARANCE_FONT_SIZE) {
            case 1:
                setTheme(C0254R.style.FontSizeSmall);
                break;
            case 2:
                setTheme(C0254R.style.FontSizeMedium);
                break;
            case 3:
                setTheme(C0254R.style.FontSizeLarge);
                break;
        }
        addPreferencesFromResource(C0254R.xml.whereyougo_preferences);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        getPreferenceScreen().removePreference(findPreference(C0254R.string.pref_KEY_X_HIDDEN_PREFERENCES));
        findPreference(C0254R.string.pref_KEY_S_ROOT).setOnPreferenceClickListener(this);
        Preference preferenceAbout = findPreference(C0254R.string.pref_KEY_X_ABOUT);
        if (preferenceAbout != null) {
            preferenceAbout.setOnPreferenceClickListener(this);
        }
        if (!Utils.isAndroid201OrMore()) {
            Preference prefSensorFilter = findPreference(C0254R.string.pref_KEY_S_SENSORS_ORIENT_FILTER);
            if (prefSensorFilter != null) {
                prefSensorFilter.setEnabled(false);
            }
        }
        if (getIntent() != null && getIntent().hasExtra(getString(C0254R.string.pref_KEY_X_LOGIN_PREFERENCES))) {
            Preference preferenceLogin = findPreference(C0254R.string.pref_KEY_X_LOGIN_PREFERENCES);
            if (preferenceLogin != null) {
                PreferenceScreen screen = getPreferenceScreen();
                for (int i = 0; i < screen.getPreferenceCount(); i++) {
                    if (screen.getPreference(i) == preferenceLogin) {
                        getIntent().putExtra(getString(C0254R.string.pref_KEY_X_LOGIN_PREFERENCES), false);
                        screen.onItemClick(null, null, i, 0);
                        return;
                    }
                }
            }
        }
    }

    public void onDestroy() {
        try {
            super.onDestroy();
            if (this.needRestart) {
                C0322A.getMain().showDialogFinish(2);
            }
        } catch (Exception e) {
            Logger.m22e(getLocalClassName(), "onDestroy()", e);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals("")) {
            return false;
        }
        if (key.equals(getString(C0254R.string.pref_KEY_S_ROOT))) {
            UtilsGUI.dialogDoItem(this, getText(C0254R.string.pref_root), C0254R.C0252drawable.var_empty, getText(C0254R.string.pref_root_desc), getString(C0254R.string.cancel), null, getString(C0254R.string.folder_select), new C02691(), getString(C0254R.string.folder_default), new C02712());
            return false;
        }
        if (key.equals(getString(C0254R.string.pref_KEY_X_ABOUT))) {
        }
        return false;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals("X")) {
            int value;
            if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_FONT_SIZE)) {
                Preferences.APPEARANCE_FONT_SIZE = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_FULLSCREEN)) {
                Preferences.APPEARANCE_FULLSCREEN = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_GPS)) {
                Preferences.GPS = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_GPS_START_AUTOMATICALLY)) {
                Preferences.GPS_START_AUTOMATICALLY = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_GPS_ALTITUDE_MANUAL_CORRECTION)) {
                Preferences.GPS_ALTITUDE_CORRECTION = Utils.parseDouble(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_GPS_BEEP_ON_GPS_FIX)) {
                Preferences.GPS_BEEP_ON_GPS_FIX = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_GPS_DISABLE_WHEN_HIDE)) {
                Preferences.GPS_DISABLE_WHEN_HIDE = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_GUIDING_COMPASS_SOUNDS)) {
                Preferences.GUIDING_SOUNDS = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_GUIDING_GPS_REQUIRED)) {
                Preferences.GUIDING_GPS_REQUIRED = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND)) {
                int result = Utils.parseInt(sharedPreferences.getString(key, null));
                if (result == 2) {
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setType("audio/*");
                    if (!Utils.isIntentAvailable(intent)) {
                        intent = new Intent("android.intent.action.RINGTONE_PICKER");
                    }
                    startActivityForResult(intent, C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND);
                    return;
                }
                Preferences.GUIDING_WAYPOINT_SOUND = result;
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND_DISTANCE)) {
                value = Utils.parseInt(sharedPreferences.getString(key, null));
                if (value > 0) {
                    Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE = value;
                } else {
                    ManagerNotify.toastShortMessage((int) C0254R.string.invalid_value);
                }
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_GUIDING_ZONE_POINT)) {
                Preferences.GUIDING_ZONE_NAVIGATION_POINT = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_HARDWARE_COMPASS_AUTO_CHANGE)) {
                Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
                C0322A.getRotator().manageSensors();
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_HARDWARE_COMPASS_AUTO_CHANGE_VALUE)) {
                value = Utils.parseInt(sharedPreferences.getString(key, null));
                if (value > 0) {
                    Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = value;
                } else {
                    ManagerNotify.toastShortMessage((int) C0254R.string.invalid_value);
                }
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_SENSOR_HARDWARE_COMPASS)) {
                Preferences.SENSOR_HARDWARE_COMPASS = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
                C0322A.getRotator().manageSensors();
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_HIGHLIGHT)) {
                Preferences.APPEARANCE_HIGHLIGHT = Utils.parseInt(sharedPreferences.getString(key, null));
                PreferenceValues.enableWakeLock();
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_IMAGE_STRETCH)) {
                Preferences.APPEARANCE_IMAGE_STRETCH = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_LANGUAGE)) {
                java.util.Locale locale;
                String lang = sharedPreferences.getString(key, "");
                ArrayList<String> loc = StringToken.parse(lang, "_");
                Configuration config = getBaseContext().getResources().getConfiguration();
                if ("default".equals(lang)) {
                    locale = java.util.Locale.getDefault();
                } else if (loc.size() == 1) {
                    locale = new java.util.Locale(lang);
                } else if (loc.size() == 2) {
                    locale = new java.util.Locale((String) loc.get(0), (String) loc.get(1));
                } else {
                    locale = config.locale;
                }
                if (!config.locale.getLanguage().equals(locale.getLanguage())) {
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    this.needRestart = true;
                }
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_MAP_PROVIDER)) {
                Preferences.GLOBAL_MAP_PROVIDER = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_SAVEGAME_AUTO)) {
                Preferences.GLOBAL_SAVEGAME_AUTO = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_SAVEGAME_SLOTS)) {
                value = Utils.parseInt(sharedPreferences.getString(key, null));
                if (value >= 0) {
                    Preferences.GLOBAL_SAVEGAME_SLOTS = value;
                } else {
                    ManagerNotify.toastShortMessage((int) C0254R.string.invalid_value);
                }
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_DOUBLE_CLICK)) {
                Preferences.GLOBAL_DOUBLE_CLICK = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_GC_USERNAME)) {
                Preferences.GC_USERNAME = sharedPreferences.getString(key, null);
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_GC_PASSWORD)) {
                Preferences.GC_PASSWORD = sharedPreferences.getString(key, null);
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_SENSORS_BEARING_TRUE)) {
                Preferences.SENSOR_BEARING_TRUE = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_SENSORS_ORIENT_FILTER)) {
                Preferences.SENSOR_ORIENT_FILTER = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_B_STATUSBAR)) {
                Preferences.APPEARANCE_STATUSBAR = Utils.parseBoolean(Boolean.valueOf(sharedPreferences.getBoolean(key, false)));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_UNITS_ALTITUDE)) {
                Preferences.FORMAT_ALTITUDE = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_UNITS_ANGLE)) {
                Preferences.FORMAT_ANGLE = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_UNITS_COO_LATLON)) {
                Preferences.FORMAT_COO_LATLON = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_UNITS_LENGTH)) {
                Preferences.FORMAT_LENGTH = Utils.parseInt(sharedPreferences.getString(key, null));
            } else if (Preferences.comparePreferenceKey(key, C0254R.string.pref_KEY_S_UNITS_SPEED)) {
                Preferences.FORMAT_SPEED = Utils.parseInt(sharedPreferences.getString(key, null));
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND && resultCode == -1 && data != null) {
            Uri uri = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri != null) {
                Logger.m20d(TAG, "uri:" + uri.toString());
                Preferences.setStringPreference(C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND, Integer.valueOf(2));
                Preferences.setStringPreference(C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI, uri.toString());
                Preferences.GUIDING_WAYPOINT_SOUND = Utils.parseInt(Integer.valueOf(C0254R.string.pref_VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND));
                Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = uri.toString();
            }
        }
    }

    private Preference findPreference(int keyId) {
        return findPreference(getKey(keyId));
    }
}
