package menion.android.whereyougo.gui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import android.widget.AdapterView;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import java.io.File;
import java.util.ArrayList;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.preferences.PreviewPreference;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.StringToken;
import menion.android.whereyougo.utils.Utils;

public class XmlSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {
   private static final String TAG = "XmlSettingsActivity";
   private boolean needRestart;

   private Preference findPreference(int var1) {
      return this.findPreference(getKey(var1));
   }

   private static String getKey(int var0) {
      return Locale.getString(var0);
   }

   public void onActivityResult(int var1, int var2, Intent var3) {
      if (var1 == 2131165574 && var2 == -1 && var3 != null) {
         Uri var4 = (Uri)var3.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
         if (var4 != null) {
            Logger.d("XmlSettingsActivity", "uri:" + var4.toString());
            Preferences.setStringPreference(2131165574, 2);
            Preferences.setStringPreference(2131165575, var4.toString());
            Preferences.GUIDING_WAYPOINT_SOUND = Utils.parseInt((Object)2131165598);
            Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = var4.toString();
         }
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setTitle(2131165307);
      this.needRestart = false;
      switch(Preferences.APPEARANCE_FONT_SIZE) {
      case 1:
         this.setTheme(2131361803);
         break;
      case 2:
         this.setTheme(2131361802);
         break;
      case 3:
         this.setTheme(2131361801);
      }

      this.addPreferencesFromResource(2131034113);
      PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
      Preference var4 = this.findPreference(2131165591);
      this.getPreferenceScreen().removePreference(var4);
      this.findPreference(2131165582).setOnPreferenceClickListener(this);
      var4 = this.findPreference(2131165590);
      if (var4 != null) {
         var4.setOnPreferenceClickListener(this);
      }

      if (!Utils.isAndroid201OrMore()) {
         var4 = this.findPreference(2131165584);
         if (var4 != null) {
            var4.setEnabled(false);
         }
      }

      if (this.getIntent() != null && this.getIntent().hasExtra(this.getString(2131165592))) {
         Preference var2 = this.findPreference(2131165592);
         if (var2 != null) {
            PreferenceScreen var5 = this.getPreferenceScreen();

            for(int var3 = 0; var3 < var5.getPreferenceCount(); ++var3) {
               if (var5.getPreference(var3) == var2) {
                  this.getIntent().putExtra(this.getString(2131165592), false);
                  var5.onItemClick((AdapterView)null, (View)null, var3, 0L);
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
      } catch (Exception var2) {
         Logger.e(this.getLocalClassName(), "onDestroy()", var2);
      }

   }

   public boolean onPreferenceClick(Preference var1) {
      String var2 = var1.getKey();
      if (!var2.equals("")) {
         if (var2.equals(this.getString(2131165582))) {
            UtilsGUI.dialogDoItem(this, this.getText(2131165375), 2130837578, this.getText(2131165376), this.getString(2131165190), (OnClickListener)null, this.getString(2131165340), new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  FileChooserDialog var3 = new FileChooserDialog(XmlSettingsActivity.this);
                  var3.loadFolder(Preferences.GLOBAL_ROOT);
                  var3.setFolderMode(true);
                  var3.setCanCreateFiles(false);
                  var3.setShowCancelButton(true);
                  var3.addListener(new FileChooserDialog.OnFileSelectedListener() {
                     public void onFileSelected(Dialog var1, File var2) {
                        var1.dismiss();
                        if (((MainApplication)A.getApp()).setRoot(var2.getAbsolutePath())) {
                           ((PreviewPreference)XmlSettingsActivity.this.findPreference(2131165582)).setValue(FileSystem.ROOT);
                           MainActivity.refreshCartridges();
                        }

                     }

                     public void onFileSelected(Dialog var1, File var2, String var3) {
                        String var4 = var2.getAbsolutePath() + "/" + var3;
                        (new File(var4)).mkdir();
                        ((FileChooserDialog)var1).loadFolder(var4);
                     }
                  });
                  var3.show();
               }
            }, this.getString(2131165339), new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  if (((MainApplication)A.getApp()).setRoot((String)null)) {
                     ((PreviewPreference)XmlSettingsActivity.this.findPreference(2131165582)).setValue(FileSystem.ROOT);
                     MainActivity.refreshCartridges();
                  }

               }
            });
         } else if (var2.equals(this.getString(2131165590))) {
         }
      }

      return false;
   }

   public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
      if (!var2.equals("X")) {
         if (Preferences.comparePreferenceKey(var2, 2131165569)) {
            Preferences.APPEARANCE_FONT_SIZE = Utils.parseInt(var1.getString(var2, (String)null));
         } else if (Preferences.comparePreferenceKey(var2, 2131165551)) {
            Preferences.APPEARANCE_FULLSCREEN = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else if (Preferences.comparePreferenceKey(var2, 2131165552)) {
            Preferences.GPS = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else if (Preferences.comparePreferenceKey(var2, 2131165555)) {
            Preferences.GPS_START_AUTOMATICALLY = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else if (Preferences.comparePreferenceKey(var2, 2131165572)) {
            Preferences.GPS_ALTITUDE_CORRECTION = Utils.parseDouble(var1.getString(var2, (String)null));
         } else if (Preferences.comparePreferenceKey(var2, 2131165553)) {
            Preferences.GPS_BEEP_ON_GPS_FIX = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else if (Preferences.comparePreferenceKey(var2, 2131165554)) {
            Preferences.GPS_DISABLE_WHEN_HIDE = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else if (Preferences.comparePreferenceKey(var2, 2131165556)) {
            Preferences.GUIDING_SOUNDS = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else if (Preferences.comparePreferenceKey(var2, 2131165557)) {
            Preferences.GUIDING_GPS_REQUIRED = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
         } else {
            int var3;
            if (Preferences.comparePreferenceKey(var2, 2131165574)) {
               var3 = Utils.parseInt(var1.getString(var2, (String)null));
               if (var3 == 2) {
                  Intent var6 = new Intent("android.intent.action.PICK");
                  var6.setType("audio/*");
                  Intent var5 = var6;
                  if (!Utils.isIntentAvailable(var6)) {
                     var5 = new Intent("android.intent.action.RINGTONE_PICKER");
                  }

                  this.startActivityForResult(var5, 2131165574);
               } else {
                  Preferences.GUIDING_WAYPOINT_SOUND = var3;
               }
            } else if (Preferences.comparePreferenceKey(var2, 2131165576)) {
               var3 = Utils.parseInt(var1.getString(var2, (String)null));
               if (var3 > 0) {
                  Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE = var3;
               } else {
                  ManagerNotify.toastShortMessage(2131165215);
               }
            } else if (Preferences.comparePreferenceKey(var2, 2131165577)) {
               Preferences.GUIDING_ZONE_NAVIGATION_POINT = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165558)) {
               Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
               A.getRotator().manageSensors();
            } else if (Preferences.comparePreferenceKey(var2, 2131165578)) {
               var3 = Utils.parseInt(var1.getString(var2, (String)null));
               if (var3 > 0) {
                  Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = var3;
               } else {
                  ManagerNotify.toastShortMessage(2131165215);
               }
            } else if (Preferences.comparePreferenceKey(var2, 2131165562)) {
               Preferences.SENSOR_HARDWARE_COMPASS = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
               A.getRotator().manageSensors();
            } else if (Preferences.comparePreferenceKey(var2, 2131165579)) {
               Preferences.APPEARANCE_HIGHLIGHT = Utils.parseInt(var1.getString(var2, (String)null));
               PreferenceValues.enableWakeLock();
            } else if (Preferences.comparePreferenceKey(var2, 2131165559)) {
               Preferences.APPEARANCE_IMAGE_STRETCH = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
            } else if (Preferences.comparePreferenceKey(var2, 2131165580)) {
               String var7 = var1.getString(var2, "");
               ArrayList var4 = StringToken.parse(var7, "_");
               Configuration var8 = this.getBaseContext().getResources().getConfiguration();
               java.util.Locale var9;
               if ("default".equals(var7)) {
                  var9 = java.util.Locale.getDefault();
               } else if (var4.size() == 1) {
                  var9 = new java.util.Locale(var7);
               } else if (var4.size() == 2) {
                  var9 = new java.util.Locale((String)var4.get(0), (String)var4.get(1));
               } else {
                  var9 = var8.locale;
               }

               if (!var8.locale.getLanguage().equals(var9.getLanguage())) {
                  var8.locale = var9;
                  this.getBaseContext().getResources().updateConfiguration(var8, this.getBaseContext().getResources().getDisplayMetrics());
                  this.needRestart = true;
               }
            } else if (Preferences.comparePreferenceKey(var2, 2131165581)) {
               Preferences.GLOBAL_MAP_PROVIDER = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165560)) {
               Preferences.GLOBAL_SAVEGAME_AUTO = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
            } else if (Preferences.comparePreferenceKey(var2, 2131165583)) {
               var3 = Utils.parseInt(var1.getString(var2, (String)null));
               if (var3 >= 0) {
                  Preferences.GLOBAL_SAVEGAME_SLOTS = var3;
               } else {
                  ManagerNotify.toastShortMessage(2131165215);
               }
            } else if (Preferences.comparePreferenceKey(var2, 2131165550)) {
               Preferences.GLOBAL_DOUBLE_CLICK = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
            } else if (Preferences.comparePreferenceKey(var2, 2131165571)) {
               Preferences.GC_USERNAME = var1.getString(var2, (String)null);
            } else if (Preferences.comparePreferenceKey(var2, 2131165570)) {
               Preferences.GC_PASSWORD = var1.getString(var2, (String)null);
            } else if (Preferences.comparePreferenceKey(var2, 2131165561)) {
               Preferences.SENSOR_BEARING_TRUE = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
            } else if (Preferences.comparePreferenceKey(var2, 2131165584)) {
               Preferences.SENSOR_ORIENT_FILTER = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165563)) {
               Preferences.APPEARANCE_STATUSBAR = Utils.parseBoolean((Object)var1.getBoolean(var2, false));
            } else if (Preferences.comparePreferenceKey(var2, 2131165585)) {
               Preferences.FORMAT_ALTITUDE = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165586)) {
               Preferences.FORMAT_ANGLE = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165587)) {
               Preferences.FORMAT_COO_LATLON = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165588)) {
               Preferences.FORMAT_LENGTH = Utils.parseInt(var1.getString(var2, (String)null));
            } else if (Preferences.comparePreferenceKey(var2, 2131165589)) {
               Preferences.FORMAT_SPEED = Utils.parseInt(var1.getString(var2, (String)null));
            }
         }
      }

   }
}
