package menion.android.whereyougo.gui.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.Point2D;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.geo.orientation.Orientation;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.view.Satellite2DView;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;

public class SatelliteActivity extends CustomActivity implements ILocationEventListener {
   private static final String TAG = "SatelliteScreen";
   private ToggleButton buttonGps;
   private Satellite2DView satelliteView;
   private final ArrayList satellites = new ArrayList();

   private void createLayout() {
      LinearLayout var1 = (LinearLayout)this.findViewById(2131492970);
      var1.removeAllViews();
      this.satelliteView = new Satellite2DView(this, this.satellites);
      var1.addView(this.satelliteView, -1, -1);
      if (Utils.isAndroid30OrMore()) {
         this.findViewById(2131492978).setBackgroundColor(-2236963);
      }

      this.buttonGps = (ToggleButton)this.findViewById(2131492982);
      this.buttonGps.setChecked(LocationState.isActuallyHardwareGpsOn());
      this.buttonGps.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            if (!var2) {
               LocationState.setGpsOff(SatelliteActivity.this);
               SatelliteActivity.this.satellites.clear();
               SatelliteActivity.this.satelliteView.invalidate();
            } else {
               LocationState.setGpsOn(SatelliteActivity.this);
            }

            SatelliteActivity.this.onGpsStatusChanged(0, (ArrayList)null);
            PreferenceValues.enableWakeLock();
         }
      });
      ToggleButton var2 = (ToggleButton)this.findViewById(2131492983);
      var2.setChecked(Preferences.SENSOR_HARDWARE_COMPASS);
      var2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            ManagerNotify.toastLongMessage(2131165276);
            Preferences.SENSOR_HARDWARE_COMPASS = var2;
            Preferences.setPreference(2131165562, Preferences.SENSOR_HARDWARE_COMPASS);
            A.getRotator().manageSensors();
         }
      });
   }

   private Point2D.Int setSatellites(ArrayList var1) {
      ArrayList var2 = this.satellites;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label323: {
         Point2D.Int var3;
         try {
            var3 = new Point2D.Int();
            this.satellites.clear();
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label323;
         }

         if (var1 != null) {
            int var4 = 0;

            while(true) {
               SatellitePosition var5;
               try {
                  if (var4 >= var1.size()) {
                     break;
                  }

                  var5 = (SatellitePosition)var1.get(var4);
                  if (var5.isFixed()) {
                     ++var3.x;
                  }
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label323;
               }

               try {
                  ++var3.y;
                  this.satellites.add(var5);
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label323;
               }

               ++var4;
            }
         }

         label304:
         try {
            return var3;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label304;
         }
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            continue;
         }
      }
   }

   public String getName() {
      return "SatelliteScreen";
   }

   public int getPriority() {
      return 2;
   }

   public boolean isRequired() {
      return false;
   }

   public void notifyGpsDisable() {
      this.buttonGps.setChecked(false);
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903058);
      this.createLayout();
   }

   public void onGpsStatusChanged(int var1, ArrayList var2) {
      try {
         Point2D.Int var3 = this.setSatellites(var2);
         this.satelliteView.invalidate();
         TextView var4 = (TextView)this.findViewById(2131492979);
         StringBuilder var5 = new StringBuilder();
         var4.setText(var5.append(var3.x).append(" | ").append(var3.y).toString());
      } catch (Exception var6) {
         Logger.e("SatelliteScreen", "onGpsStatusChanged(" + var1 + ", " + var2 + "), e:" + var6.toString());
      }

   }

   public void onLocationChanged(final Location var1) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            String var1x = var1.getProvider();
            byte var2 = -1;
            switch(var1x.hashCode()) {
            case 102570:
               if (var1x.equals("gps")) {
                  var2 = 0;
               }
               break;
            case 1843485230:
               if (var1x.equals("network")) {
                  var2 = 1;
               }
            }

            switch(var2) {
            case 0:
               var1x = SatelliteActivity.this.getString(2131165393);
               break;
            case 1:
               var1x = SatelliteActivity.this.getString(2131165394);
               break;
            default:
               var1x = SatelliteActivity.this.getString(2131165395);
            }

            ((TextView)SatelliteActivity.this.findViewById(2131492973)).setText(var1x);
            ((TextView)SatelliteActivity.this.findViewById(2131492972)).setText(UtilsFormat.formatLatitude(var1.getLatitude()));
            ((TextView)SatelliteActivity.this.findViewById(2131492974)).setText(UtilsFormat.formatLongitude(var1.getLongitude()));
            ((TextView)SatelliteActivity.this.findViewById(2131492975)).setText(UtilsFormat.formatAltitude(var1.getAltitude(), true));
            ((TextView)SatelliteActivity.this.findViewById(2131492977)).setText(UtilsFormat.formatDistance((double)var1.getAccuracy(), false));
            ((TextView)SatelliteActivity.this.findViewById(2131492976)).setText(UtilsFormat.formatSpeed((double)var1.getSpeed(), false));
            ((TextView)SatelliteActivity.this.findViewById(2131492980)).setText(UtilsFormat.formatAngle((double)Orientation.getDeclination()));
            long var3 = LocationState.getLastFixTime();
            if (var3 > 0L) {
               ((TextView)SatelliteActivity.this.findViewById(2131492981)).setText(UtilsFormat.formatTime(var3));
            } else {
               ((TextView)SatelliteActivity.this.findViewById(2131492981)).setText("~");
            }

         }
      });
   }

   protected void onResume() {
      super.onResume();
      this.onLocationChanged(LocationState.getLocation());
      this.onGpsStatusChanged(0, (ArrayList)null);
   }

   public void onStart() {
      super.onStart();
      LocationState.addLocationChangeListener(this);
      if (this.buttonGps.isChecked() && !LocationState.isActuallyHardwareGpsOn()) {
         this.notifyGpsDisable();
      }

   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void onStop() {
      super.onStop();
      LocationState.removeLocationChangeListener(this);
   }
}
