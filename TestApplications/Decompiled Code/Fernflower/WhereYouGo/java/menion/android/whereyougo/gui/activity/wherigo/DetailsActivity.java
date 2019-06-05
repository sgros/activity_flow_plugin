package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cz.matejcik.openwig.Action;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.Task;
import cz.matejcik.openwig.Thing;
import cz.matejcik.openwig.Zone;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.activity.MediaActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.guide.Guide;
import menion.android.whereyougo.maps.utils.MapDataProvider;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.UtilsFormat;

public class DetailsActivity extends MediaActivity implements IRefreshable, ILocationEventListener {
   private static final String TAG = "Details";
   public static EventTable et;
   private static final String[] taskStates = new String[]{Locale.getString(2131165231), Locale.getString(2131165204), Locale.getString(2131165201)};
   private TextView tvDescription;
   private TextView tvDistance;
   private TextView tvName;
   private TextView tvState;

   private void enableGuideOnEventTable() {
      Location var1 = extractLocation(et);
      if (var1 != null) {
         A.getGuidingContent().guideStart(new Guide(et.name, var1));
      } else {
         Logger.d("Details", "enableGuideOnEventTable(), waypoint 'null'");
      }

   }

   private static Location extractLocation(EventTable var0) {
      Location var2;
      if (var0 != null && var0.isLocated()) {
         Location var1 = new Location("Details");
         if (var0 instanceof Zone) {
            Zone var3 = (Zone)var0;
            var1.setLatitude(var3.nearestPoint.latitude);
            var1.setLongitude(var3.nearestPoint.longitude);
            var2 = var1;
         } else {
            var1.setLatitude(var0.position.latitude);
            var1.setLongitude(var0.position.longitude);
            var2 = var1;
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   private void setBottomMenu() {
      String var1 = null;
      String var2 = null;
      Object var3 = null;
      CustomDialog.OnClickListener var4 = null;
      CustomDialog.OnClickListener var5 = null;
      Object var6 = null;
      boolean var7 = et.isLocated();
      int var8 = 0;
      Vector var9 = null;
      if (et instanceof Thing) {
         Thing var10 = (Thing)et;
         int var11 = var10.visibleActions();
         var8 = Engine.instance.cartridge.visibleUniversalActions();
         Logger.d("Details", "actions:" + (var11 + var8));
         var9 = ListActionsActivity.getValidActions(var10);
         var8 = var9.size();
         Logger.d("Details", "validActions:" + var8);
      }

      Logger.d("Details", "setBottomMenu(), loc:" + et.isLocated() + ", et:" + et + ", act:" + var8);
      if (var7) {
         var1 = this.getString(2131165345);
         var4 = new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               try {
                  DetailsActivity.this.enableGuideOnEventTable();
                  MainActivity.callGudingScreen(DetailsActivity.this);
               } catch (Exception var4) {
                  Logger.w("Details", "btn01.click() - unknown problem");
               }

               return true;
            }
         };
         var2 = this.getString(2131165221);
         var5 = new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               MapDataProvider var4 = MapHelper.getMapDataProvider();
               var4.clear();
               var4.addAll();
               MainActivity.wui.showScreen(14, DetailsActivity.et);
               return true;
            }
         };
      }

      String var12 = var1;
      CustomDialog.OnClickListener var13 = var4;
      String var14 = var2;
      CustomDialog.OnClickListener var15 = var5;
      String var20 = (String)var3;
      CustomDialog.OnClickListener var16 = (CustomDialog.OnClickListener)var6;
      if (var8 > 0) {
         if (var7) {
            var20 = this.getString(2131165326, new Object[]{var8});
            var16 = new CustomDialog.OnClickListener() {
               public boolean onClick(CustomDialog var1, View var2, int var3) {
                  ListActionsActivity.reset((Thing)DetailsActivity.et);
                  MainActivity.wui.showScreen(12, DetailsActivity.et);
                  return true;
               }
            };
            var15 = var5;
            var14 = var2;
            var13 = var4;
            var12 = var1;
         } else if (var8 <= 3) {
            if (var8 > 0) {
               final Action var17 = (Action)var9.get(0);
               var1 = var17.text;
               var4 = new CustomDialog.OnClickListener() {
                  public boolean onClick(CustomDialog var1, View var2, int var3) {
                     ListActionsActivity.reset((Thing)DetailsActivity.et);
                     ListActionsActivity.callAction(var17);
                     return true;
                  }
               };
            }

            if (var8 > 1) {
               final Action var18 = (Action)var9.get(1);
               var2 = var18.text;
               var5 = new CustomDialog.OnClickListener() {
                  public boolean onClick(CustomDialog var1, View var2, int var3) {
                     ListActionsActivity.reset((Thing)DetailsActivity.et);
                     ListActionsActivity.callAction(var18);
                     return true;
                  }
               };
            }

            var12 = var1;
            var13 = var4;
            var14 = var2;
            var15 = var5;
            var20 = (String)var3;
            var16 = (CustomDialog.OnClickListener)var6;
            if (var8 > 2) {
               final Action var21 = (Action)var9.get(2);
               var20 = var21.text;
               var16 = new CustomDialog.OnClickListener() {
                  public boolean onClick(CustomDialog var1, View var2, int var3) {
                     ListActionsActivity.reset((Thing)DetailsActivity.et);
                     ListActionsActivity.callAction(var21);
                     return true;
                  }
               };
               var12 = var1;
               var13 = var4;
               var14 = var2;
               var15 = var5;
            }
         } else {
            var20 = this.getString(2131165326, new Object[]{var8});
            var16 = new CustomDialog.OnClickListener() {
               public boolean onClick(CustomDialog var1, View var2, int var3) {
                  ListActionsActivity.reset((Thing)DetailsActivity.et);
                  MainActivity.wui.showScreen(12, DetailsActivity.et);
                  return true;
               }
            };
            var12 = var1;
            var13 = var4;
            var14 = var2;
            var15 = var5;
         }
      }

      CustomDialog.setBottom(this, var12, var13, var14, var15, var20, var16);
      if (et instanceof Task) {
         Task var19 = (Task)et;
         this.tvState.setText(taskStates[var19.state()]);
      }

   }

   private void updateNavi() {
      if (et instanceof Zone) {
         Zone var1 = (Zone)et;
         String var2 = this.getString(2131165412);
         switch(var1.contain) {
         case 0:
            var2 = this.getString(2131165409);
            break;
         case 1:
            var2 = this.getString(2131165411);
            break;
         case 2:
            var2 = this.getString(2131165410);
         }

         this.tvState.setText(this.getString(2131165408, new Object[]{var2}));
         if (var1.contain == 2) {
            this.tvDistance.setText(this.getString(2131165407, new Object[]{this.getString(2131165410)}));
         } else {
            this.tvDistance.setText(this.getString(2131165407, new Object[]{UtilsFormat.formatDistance(var1.distance, false)}));
         }
      }

   }

   public String getName() {
      return "Details";
   }

   public int getPriority() {
      return 2;
   }

   public boolean isRequired() {
      return false;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() != null && Engine.instance != null) {
         this.setContentView(2130903052);
      } else {
         this.finish();
      }

   }

   public void onGpsStatusChanged(int var1, ArrayList var2) {
   }

   public void onLocationChanged(Location var1) {
      this.refresh();
   }

   public void onResume() {
      super.onResume();
      Logger.d("Details", "onResume(), et:" + et);
      if (et != null) {
         this.setTitle(et.name);
         this.tvName = (TextView)this.findViewById(2131492940);
         this.tvState = (TextView)this.findViewById(2131492941);
         this.tvDescription = (TextView)this.findViewById(2131492943);
         this.tvDistance = (TextView)this.findViewById(2131492942);
      } else {
         Logger.i("Details", "onCreate(), et == null, end!");
         this.finish();
      }

      this.refresh();
   }

   public void onStart() {
      super.onStart();
      if (et instanceof Zone) {
         LocationState.addLocationChangeListener(this);
      }

   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void onStop() {
      super.onStop();
      LocationState.removeLocationChangeListener(this);
   }

   public void refresh() {
      this.runOnUiThread(new Runnable() {
         public void run() {
            if (!DetailsActivity.this.stillValid()) {
               Logger.d("Details", "refresh(), not valid anymore");
               DetailsActivity.this.finish();
            } else {
               DetailsActivity.this.tvName.setText(DetailsActivity.et.name);
               DetailsActivity.this.tvDescription.setText(UtilsGUI.simpleHtml(DetailsActivity.et.description));
               Media var1 = (Media)DetailsActivity.et.table.rawget("Media");
               DetailsActivity.this.setMedia(var1);
               DetailsActivity.this.updateNavi();
               DetailsActivity.this.setBottomMenu();
            }

         }
      });
   }

   public boolean stillValid() {
      boolean var1;
      if (et != null) {
         if (et instanceof Thing) {
            var1 = ((Thing)et).visibleToPlayer();
         } else {
            var1 = et.isVisible();
         }
      } else {
         var1 = false;
      }

      return var1;
   }
}
