package menion.android.whereyougo.gui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.VersionInfo;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.dialog.AboutDialog;
import menion.android.whereyougo.gui.dialog.ChooseCartridgeDialog;
import menion.android.whereyougo.gui.dialog.ChooseSavegameDialog;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.network.activity.DownloadCartridgeActivity;
import menion.android.whereyougo.openwig.WLocationService;
import menion.android.whereyougo.openwig.WSaveFile;
import menion.android.whereyougo.openwig.WSeekableFile;
import menion.android.whereyougo.openwig.WUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;

public class MainActivity extends CustomMainActivity {
   private static final String TAG = "Main";
   public static CartridgeFile cartridgeFile;
   private static Vector cartridgeFiles;
   public static String selectedFile;
   private static final WLocationService wLocationService = new WLocationService();
   public static final WUI wui = new WUI();

   static {
      wui.setOnSavingStarted(new Runnable() {
         public void run() {
            try {
               FileSystem.backupFile(MainActivity.getSaveFile());
            } catch (Exception var2) {
            }

         }
      });
   }

   public static boolean callGudingScreen(Activity var0) {
      var0.startActivity(new Intent(var0, GuidingActivity.class));
      return true;
   }

   @TargetApi(23)
   private void checkPermissions() {
      String[] var1 = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (ActivityCompat.checkSelfPermission(this, var1[var3]) != 0) {
            ActivityCompat.requestPermissions(this, var1, 0);
            break;
         }
      }

   }

   private void clickMap() {
      MapDataProvider var1 = MapHelper.getMapDataProvider();
      var1.clear();
      var1.addCartridges(cartridgeFiles);
      wui.showScreen(14, (EventTable)null);
   }

   private void clickStart() {
      if (this.isAnyCartridgeAvailable()) {
         ChooseCartridgeDialog var1 = new ChooseCartridgeDialog();
         var1.setParams(cartridgeFiles);
         this.getSupportFragmentManager().beginTransaction().add(var1, "DIALOG_TAG_CHOOSE_CARTRIDGE").commitAllowingStateLoss();
      }

   }

   public static File getLogFile() throws IOException {
      File var0;
      try {
         StringBuilder var1 = new StringBuilder();
         var0 = new File(var1.append(selectedFile.substring(0, selectedFile.length() - 3)).append("owl").toString());
      } catch (SecurityException var2) {
         Logger.e("Main", "getSyncFile()", (Exception)var2);
         var0 = null;
      }

      return var0;
   }

   public static File getSaveFile() throws IOException {
      File var0;
      try {
         StringBuilder var1 = new StringBuilder();
         var0 = new File(var1.append(selectedFile.substring(0, selectedFile.length() - 3)).append("ows").toString());
      } catch (SecurityException var2) {
         Logger.e("Main", "getSyncFile()", (Exception)var2);
         var0 = null;
      }

      return var0;
   }

   public static String getSelectedFile() {
      return selectedFile;
   }

   private boolean isAnyCartridgeAvailable() {
      boolean var1 = true;
      if (cartridgeFiles == null || cartridgeFiles.size() == 0) {
         UtilsGUI.showDialogInfo(this, this.getString(2131165346, new Object[]{FileSystem.ROOT, "WhereYouGo"}));
         var1 = false;
      }

      return var1;
   }

   private static void loadCartridge(OutputStream var0) {
      try {
         WUI.startProgressDialog();
         Engine.newInstance(cartridgeFile, var0, wui, wLocationService).start();
      } catch (Throwable var1) {
      }

   }

   public static void openCartridge(CartridgeFile var0) {
      CustomMainActivity var1 = A.getMain();
      if (var1 != null) {
         try {
            cartridgeFile = var0;
            selectedFile = cartridgeFile.filename;
            ChooseSavegameDialog var3 = ChooseSavegameDialog.newInstance(getSaveFile());
            var1.getSupportFragmentManager().beginTransaction().add(var3, "DIALOG_TAG_CHOOSE_SAVE_FILE").commitAllowingStateLoss();
         } catch (Exception var2) {
            Logger.e("Main", "onCreate()", var2);
         }
      }

   }

   private void openCartridge(File var1) {
      CartridgeFile var2 = null;
      CartridgeFile var3 = var2;

      Exception var10000;
      label76: {
         boolean var10001;
         label75: {
            label81: {
               label82: {
                  WSeekableFile var4;
                  try {
                     var4 = new WSeekableFile;
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label82;
                  }

                  var3 = var2;

                  try {
                     var4.<init>(var1);
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label82;
                  }

                  var3 = var2;

                  WSaveFile var5;
                  try {
                     var5 = new WSaveFile;
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label82;
                  }

                  var3 = var2;

                  try {
                     var5.<init>(var1);
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label82;
                  }

                  var3 = var2;

                  try {
                     var2 = CartridgeFile.read(var4, var5);
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label82;
                  }

                  if (var2 == null) {
                     return;
                  }

                  var3 = var2;

                  try {
                     var2.filename = var1.getAbsolutePath();
                     break label81;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               Exception var15 = var10000;

               try {
                  StringBuilder var16 = new StringBuilder();
                  Logger.w("Main", var16.append("openCartridge(), file:").append(var1).append(", e:").append(var15.toString()).toString());
                  ManagerNotify.toastShortMessage(Locale.getString(2131165214, var1.getName()));
                  break label75;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label76;
               }
            }

            var3 = var2;
         }

         try {
            openCartridge(var3);
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      Logger.e("Main", "onCreate()", var14);
   }

   public static void refreshCartridges() {
      StringBuilder var0 = (new StringBuilder()).append("refreshCartridges(), ");
      boolean var1;
      if (selectedFile == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Logger.w("Main", var0.append(var1).toString());
      File[] var11 = FileSystem.getFiles(FileSystem.ROOT, "gwc");
      cartridgeFiles = new Vector();
      ArrayList var2 = new ArrayList();
      if (var11 != null) {
         int var3 = var11.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File var5 = var11[var4];

            Exception var10000;
            label48: {
               CartridgeFile var13;
               boolean var10001;
               try {
                  WSeekableFile var6 = new WSeekableFile(var5);
                  WSaveFile var7 = new WSaveFile(var5);
                  var13 = CartridgeFile.read(var6, var7);
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label48;
               }

               if (var13 == null) {
                  continue;
               }

               try {
                  var13.filename = var5.getAbsolutePath();
                  Location var12 = new Location("Main");
                  var12.setLatitude(var13.latitude);
                  var12.setLongitude(var13.longitude);
                  Waypoint var8 = new Waypoint(var13.name, var12);
                  cartridgeFiles.add(var13);
                  var2.add(var8);
                  continue;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
               }
            }

            Exception var14 = var10000;
            Logger.w("Main", "refreshCartridge(), file:" + var5 + ", e:" + var14.toString());
            ManagerNotify.toastShortMessage(Locale.getString(2131165214, var5.getName()));
         }
      }

      if (var2.size() > 0) {
      }

   }

   private static void restoreCartridge(OutputStream var0) {
      try {
         WUI.startProgressDialog();
         Engine.newInstance(cartridgeFile, var0, wui, wLocationService).restore();
      } catch (Throwable var1) {
      }

   }

   public static void setSelectedFile(String var0) {
      selectedFile = var0;
   }

   public static void startSelectedCartridge(boolean param0) {
      // $FF: Couldn't be decompiled
   }

   protected void eventCreateLayout() {
      this.setContentView(2130903055);
      ((TextView)this.findViewById(2131492866)).setText("WhereYouGo");
      OnClickListener var1 = new OnClickListener() {
         public void onClick(View var1) {
            switch(var1.getId()) {
            case 2131492961:
               MainActivity.this.getSupportFragmentManager().beginTransaction().add(new AboutDialog(), "DIALOG_TAG_MAIN").commitAllowingStateLoss();
               break;
            case 2131492962:
               MainActivity.this.clickStart();
               break;
            case 2131492963:
               MainActivity.this.clickMap();
               break;
            case 2131492964:
               MainActivity.this.startActivity(new Intent(MainActivity.this, SatelliteActivity.class));
               break;
            case 2131492965:
               MainActivity.this.startActivity(new Intent(MainActivity.this, XmlSettingsActivity.class));
            }

         }
      };
      UtilsGUI.setButtons(this, new int[]{2131492962, 2131492963, 2131492964, 2131492965, 2131492961}, var1, (OnLongClickListener)null);
   }

   protected void eventDestroyApp() {
      ((NotificationManager)this.getSystemService("notification")).cancelAll();
   }

   protected void eventFirstInit() {
      VersionInfo.afterStartAction();
   }

   protected void eventRegisterOnly() {
   }

   protected void eventSecondInit() {
   }

   protected String getCloseAdditionalText() {
      return null;
   }

   protected int getCloseValue() {
      return 0;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (VERSION.SDK_INT >= 23) {
         this.checkPermissions();
      }

      Intent var4;
      if ("android.intent.action.VIEW".equals(this.getIntent().getAction())) {
         var4 = new Intent(this.getIntent());
         var4.setClass(this, DownloadCartridgeActivity.class);
         this.startActivity(var4);
         this.finish();
      } else if ("android.intent.action.SEND".equals(this.getIntent().getAction())) {
         try {
            Uri var2 = Uri.parse(this.getIntent().getStringExtra("android.intent.extra.TEXT"));
            if (var2.getQueryParameter("CGUID") == null) {
               Exception var5 = new Exception("Invalid URL");
               throw var5;
            }

            var4 = new Intent(this, DownloadCartridgeActivity.class);
            var4.setData(var2);
            this.startActivity(var4);
         } catch (Exception var3) {
            ManagerNotify.toastShortMessage(this, this.getString(2131165341));
         }

         this.finish();
      } else {
         String var6;
         if (this.getIntent() == null) {
            var6 = null;
         } else {
            var6 = this.getIntent().getStringExtra("cguid");
         }

         if (var6 != null) {
            File var7 = FileSystem.findFile(var6);
            if (var7 != null) {
               this.openCartridge(var7);
            }
         }
      }

   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(2131558400, var1);
      return true;
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2 = true;
      switch(var1.getItemId()) {
      case 2131492988:
         this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://geocaching.com/")));
         break;
      case 2131492989:
         this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://wherigo.com/")));
         break;
      case 2131492990:
         this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/biylda/WhereYouGo")));
      default:
         var2 = false;
      }

      return var2;
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      this.testFileSystem();
      if (Preferences.GPS || Preferences.GPS_START_AUTOMATICALLY) {
         LocationState.setGpsOn(this);
      }

   }

   public void onResume() {
      super.onResume();
      refreshCartridges();
   }
}
