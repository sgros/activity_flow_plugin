package menion.android.whereyougo.gui.activity.wherigo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Player;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.SatelliteActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;

public class MainMenuActivity extends CustomActivity implements IRefreshable {
   private static final int DOUBLE_PRESS_HK_BACK_PERIOD = 666;
   private static final String TAG = "CartridgeMainMenu";
   private long lastPressedTime = 0L;
   private OnItemClickListener listClick;
   private MenuItem saveGameMainMenuItem;

   private String getVisibleCartridgeThingsDescription() {
      String var1 = null;
      Vector var2 = Engine.instance.cartridge.zones;

      String var5;
      for(int var3 = 0; var3 < var2.size(); var1 = var5) {
         String var4 = this.getVisibleThingsDescription((Zone)var2.elementAt(var3));
         var5 = var1;
         if (var4 != null) {
            if (var1 == null) {
               var5 = "";
            } else {
               var5 = var1 + ", ";
            }

            var5 = var5 + var4;
         }

         ++var3;
      }

      return var1;
   }

   private String getVisiblePlayerThingsDescription() {
      Player var1 = Engine.instance.player;
      String var2 = null;
      Object var3 = null;

      while(true) {
         Object var4 = var1.inventory.next(var3);
         if (var4 == null) {
            return var2;
         }

         Object var5 = var1.inventory.rawget(var4);
         var3 = var4;
         if (var5 instanceof Thing) {
            var3 = var4;
            if (((Thing)var5).isVisible()) {
               String var6;
               if (var2 == null) {
                  var6 = "";
               } else {
                  var6 = var2 + ", ";
               }

               var2 = var6 + ((Thing)var5).name;
               var3 = var4;
            }
         }
      }
   }

   private int getVisibleTasksCount() {
      int var1 = 0;

      int var3;
      for(int var2 = 0; var2 < Engine.instance.cartridge.tasks.size(); var1 = var3) {
         var3 = var1;
         if (((Task)Engine.instance.cartridge.tasks.elementAt(var2)).isVisible()) {
            var3 = var1 + 1;
         }

         ++var2;
      }

      return var1;
   }

   private String getVisibleTasksDescription() {
      String var1 = null;

      String var4;
      for(int var2 = 0; var2 < Engine.instance.cartridge.tasks.size(); var1 = var4) {
         Task var3 = (Task)Engine.instance.cartridge.tasks.elementAt(var2);
         var4 = var1;
         if (var3.isVisible()) {
            if (var1 == null) {
               var4 = "";
            } else {
               var4 = var1 + ", ";
            }

            var4 = var4 + var3.name;
         }

         ++var2;
      }

      return var1;
   }

   private String getVisibleThingsDescription(Zone var1) {
      String var2 = null;
      String var6;
      if (!var1.showThings()) {
         var6 = null;
      } else {
         Object var3 = null;

         while(true) {
            Object var4 = var1.inventory.next(var3);
            if (var4 == null) {
               var6 = var2;
               break;
            }

            Object var5 = var1.inventory.rawget(var4);
            var3 = var4;
            if (!(var5 instanceof Player)) {
               var3 = var4;
               if (var5 instanceof Thing) {
                  var3 = var4;
                  if (((Thing)var5).isVisible()) {
                     if (var2 == null) {
                        var2 = "";
                     } else {
                        var2 = var2 + ", ";
                     }

                     var2 = var2 + ((Thing)var5).name;
                     var3 = var4;
                  }
               }
            }
         }
      }

      return var6;
   }

   private String getVisibleZonesDescription() {
      String var1 = null;
      Vector var2 = Engine.instance.cartridge.zones;

      String var5;
      for(int var3 = 0; var3 < var2.size(); var1 = var5) {
         Zone var4 = (Zone)var2.get(var3);
         var5 = var1;
         if (var4.isVisible()) {
            if (var1 == null) {
               var5 = "";
            } else {
               var5 = var1 + ", ";
            }

            var1 = var5 + var4.name;
            var5 = var1;
            if (var4.contains(Engine.instance.player)) {
               var5 = var1 + String.format(" (%s)", this.getString(2131165410));
            }
         }

         ++var3;
      }

      return var1;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() != null && Engine.instance != null) {
         this.setContentView(2130903042);
         this.listClick = new OnItemClickListener() {
            public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
               Logger.d("CartridgeMainMenu", "onItemClick:" + var3);
               switch(var3) {
               case 0:
                  if (Engine.instance.cartridge.visibleZones() >= 1) {
                     MainActivity.wui.showScreen(4, (EventTable)null);
                  }
                  break;
               case 1:
                  if (Engine.instance.cartridge.visibleThings() >= 1) {
                     MainActivity.wui.showScreen(3, (EventTable)null);
                  }
                  break;
               case 2:
                  if (Engine.instance.player.visibleThings() >= 1) {
                     MainActivity.wui.showScreen(2, (EventTable)null);
                  }
                  break;
               case 3:
                  if (MainMenuActivity.this.getVisibleTasksCount() > 0) {
                     MainActivity.wui.showScreen(5, (EventTable)null);
                  }
               }

            }
         };
         CustomDialog.setTitle(this, Engine.instance.cartridge.name, (Bitmap)null, Integer.MIN_VALUE, (CustomDialog.OnClickListener)null);
         CustomDialog.OnClickListener var2;
         String var3;
         if (Preferences.GLOBAL_SAVEGAME_SLOTS > 0) {
            var3 = this.getString(2131165403);
            var2 = new CustomDialog.OnClickListener() {
               public boolean onClick(CustomDialog var1, View var2, int var3) {
                  MainMenuActivity.this.openOptionsMenu();
                  return true;
               }
            };
         } else {
            var3 = this.getString(2131165399);
            var2 = new CustomDialog.OnClickListener() {
               public boolean onClick(CustomDialog var1, View var2, int var3) {
                  MainActivity.wui.setOnSavingFinished(new Runnable() {
                     public void run() {
                        ManagerNotify.toastShortMessage(MainMenuActivity.this, MainMenuActivity.this.getString(2131165401));
                        MainActivity.wui.setOnSavingFinished((Runnable)null);
                     }
                  });
                  (MainMenuActivity.this.new SaveGame()).execute(new Void[0]);
                  return true;
               }
            };
         }

         CustomDialog.setBottom(this, this.getString(2131165205), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               Intent var4 = new Intent(MainMenuActivity.this, SatelliteActivity.class);
               MainMenuActivity.this.startActivity(var4);
               return true;
            }
         }, this.getString(2131165221), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               MapDataProvider var4 = MapHelper.getMapDataProvider();
               var4.clear();
               var4.addAll();
               MainActivity.wui.showScreen(14, (EventTable)null);
               return true;
            }
         }, var3, var2);
      } else {
         this.finish();
      }

   }

   public boolean onCreateOptionsMenu(Menu var1) {
      boolean var2 = true;
      super.onCreateOptionsMenu(var1);

      boolean var6;
      Exception var10000;
      label69: {
         File var3;
         String var4;
         boolean var10001;
         label70: {
            try {
               var3 = MainActivity.getSaveFile();
               if (var3.exists()) {
                  var4 = String.format("%s: %s", this.getString(2131165397), UtilsFormat.formatDatetime(var3.lastModified()));
                  break label70;
               }
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label69;
            }

            try {
               var4 = String.format("%s", this.getString(2131165397));
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label69;
            }
         }

         try {
            this.saveGameMainMenuItem = var1.add(0, 0, Preferences.GLOBAL_SAVEGAME_SLOTS, var4);
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label69;
         }

         int var5 = 1;

         while(true) {
            var6 = var2;

            label72: {
               try {
                  if (var5 > Preferences.GLOBAL_SAVEGAME_SLOTS) {
                     return var6;
                  }

                  StringBuilder var15 = new StringBuilder();
                  File var7 = new File(var15.append(var3.getAbsolutePath()).append(".").append(var5).toString());
                  if (var7.exists()) {
                     var4 = String.format("%s %d: %s", this.getString(2131165402), var5, UtilsFormat.formatDatetime(var7.lastModified()));
                     break label72;
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break;
               }

               try {
                  var4 = String.format("%s %d", this.getString(2131165402), var5);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }

            try {
               var1.add(0, var5, Preferences.GLOBAL_SAVEGAME_SLOTS - var5, var4);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            ++var5;
         }
      }

      Exception var14 = var10000;
      var14.printStackTrace();
      var6 = false;
      return var6;
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = true;
      Logger.d("CartridgeMainMenu", "onKeyDown(" + var1 + ", " + var2 + ")");
      boolean var4;
      if (var2.getKeyCode() == 4) {
         if (Preferences.GLOBAL_DOUBLE_CLICK && var2.getDownTime() - this.lastPressedTime >= 666L) {
            this.lastPressedTime = var2.getDownTime();
            ManagerNotify.toastShortMessage(2131165344);
            var4 = var3;
         } else {
            UtilsGUI.showDialogQuestion(this, 2131165306, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  (MainMenuActivity.this.new SaveGameOnExit()).execute(new Void[0]);
                  MainActivity.selectedFile = null;
                  DetailsActivity.et = null;
               }
            }, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  Engine.kill();
                  MainActivity.selectedFile = null;
                  DetailsActivity.et = null;
                  MainMenuActivity.this.finish();
               }
            }, (OnClickListener)null);
            var4 = var3;
         }
      } else {
         var4 = var3;
         if (var2.getKeyCode() != 84) {
            var4 = var3;
            if (!super.onKeyDown(var1, var2)) {
               var4 = false;
            }
         }
      }

      return var4;
   }

   public boolean onOptionsItemSelected(final MenuItem var1) {
      boolean var2 = false;
      if (var1.getGroupId() == 0) {
         if (var1.getItemId() == 0) {
            MainActivity.wui.setOnSavingFinished(new Runnable() {
               public void run() {
                  try {
                     File var1x = MainActivity.getSaveFile();
                     String var3 = String.format("%s: %s", MainMenuActivity.this.getString(2131165397), UtilsFormat.formatDatetime(var1x.lastModified()));
                     var1.setTitle(var3);
                     ManagerNotify.toastShortMessage(MainMenuActivity.this, MainMenuActivity.this.getString(2131165401));
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }

                  MainActivity.wui.setOnSavingFinished((Runnable)null);
               }
            });
         } else {
            MainActivity.wui.setOnSavingFinished(new Runnable() {
               public void run() {
                  try {
                     File var1x = MainActivity.getSaveFile();
                     StringBuilder var3 = new StringBuilder();
                     File var2 = new File(var3.append(var1x.getAbsolutePath()).append(".").append(var1.getItemId()).toString());
                     FileSystem.copyFile(var1x, var2);
                     String var6 = String.format("%s: %s", MainMenuActivity.this.getString(2131165397), UtilsFormat.formatDatetime(var1x.lastModified()));
                     MainMenuActivity.this.saveGameMainMenuItem.setTitle(var6);
                     String var5 = String.format("%s %d: %s", MainMenuActivity.this.getString(2131165402), var1.getItemId(), UtilsFormat.formatDatetime(var2.lastModified()));
                     var1.setTitle(var5);
                     var5 = String.format("%s %d\n%s", MainMenuActivity.this.getString(2131165402), var1.getItemId(), MainMenuActivity.this.getText(2131165401));
                     ManagerNotify.toastShortMessage(MainMenuActivity.this, var5);
                  } catch (Exception var4) {
                     var4.printStackTrace();
                  }

                  MainActivity.wui.setOnSavingFinished((Runnable)null);
               }
            });
         }

         (new MainMenuActivity.SaveGame()).execute(new Void[0]);
         var2 = true;
      }

      return var2;
   }

   public void onResume() {
      super.onResume();
      this.refresh();
   }

   public void refresh() {
      this.runOnUiThread(new Runnable() {
         public void run() {
            if (A.getMain() != null && Engine.instance != null && Engine.instance.cartridge != null) {
               ArrayList var1 = new ArrayList();
               var1.add(new DataInfo(MainMenuActivity.this.getString(2131165219) + " (" + Engine.instance.cartridge.visibleZones() + ")", MainMenuActivity.this.getVisibleZonesDescription(), 2130837556));
               var1.add(new DataInfo(MainMenuActivity.this.getString(2131165321) + " (" + Engine.instance.cartridge.visibleThings() + ")", MainMenuActivity.this.getVisibleCartridgeThingsDescription(), 2130837557));
               var1.add(new DataInfo(MainMenuActivity.this.getString(2131165216) + " (" + Engine.instance.player.visibleThings() + ")", MainMenuActivity.this.getVisiblePlayerThingsDescription(), 2130837555));
               var1.add(new DataInfo(MainMenuActivity.this.getString(2131165310) + " (" + Engine.instance.cartridge.visibleTasks() + ")", MainMenuActivity.this.getVisibleTasksDescription(), 2130837558));
               ListView var2 = new ListView(MainMenuActivity.this);
               IconedListAdapter var3 = new IconedListAdapter(MainMenuActivity.this, var1, var2);
               var3.setMinHeight((int)Utils.getDpPixels(70.0F));
               var3.setTextView02Visible(0, true);
               var2.setAdapter(var3);
               var2.setOnItemClickListener(MainMenuActivity.this.listClick);
               CustomDialog.setContent(MainMenuActivity.this, var2, 0, true, false);
            }

         }
      });
   }

   private class SaveGame extends menion.android.whereyougo.gui.SaveGame {
      public SaveGame() {
         super(MainMenuActivity.this);
      }
   }

   private class SaveGameOnExit extends MainMenuActivity.SaveGame {
      private SaveGameOnExit() {
         super();
      }

      // $FF: synthetic method
      SaveGameOnExit(Object var2) {
         this();
      }

      protected void onPostExecute(Void var1) {
         super.onPostExecute(var1);
         Engine.kill();
         MainMenuActivity.this.finish();
      }
   }
}
