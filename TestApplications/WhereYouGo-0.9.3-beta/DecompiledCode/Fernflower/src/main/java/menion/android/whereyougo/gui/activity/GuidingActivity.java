package menion.android.whereyougo.gui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.orientation.IOrientationEventListener;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.gui.view.CompassView;
import menion.android.whereyougo.guide.Guide;
import menion.android.whereyougo.guide.IGuide;
import menion.android.whereyougo.guide.IGuideEventListener;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.UtilsFormat;

public class GuidingActivity extends CustomActivity implements IGuideEventListener, IOrientationEventListener, IRefreshable {
   private float azimuthToTarget;
   private float mAzimuth;
   private float mPitch;
   private float mRoll;
   private TextView viewAcc;
   private TextView viewAlt;
   private CompassView viewCompass;
   private TextView viewLat;
   private TextView viewLon;
   private TextView viewName;
   private TextView viewProvider;
   private TextView viewSpeed;
   private TextView viewTimeToTarget;

   private void repaint() {
      this.viewCompass.moveAngles(this.azimuthToTarget, this.mAzimuth, this.mPitch, this.mRoll);
   }

   public void guideStart() {
   }

   public void guideStop() {
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() == null) {
         this.finish();
      } else {
         this.setContentView(2130903053);
         this.mAzimuth = 0.0F;
         this.mPitch = 0.0F;
         this.mRoll = 0.0F;
         this.azimuthToTarget = 0.0F;
         this.viewCompass = new CompassView(this);
         ((LinearLayout)this.findViewById(2131492945)).addView(this.viewCompass, -1, -1);
         this.viewName = (TextView)this.findViewById(2131492951);
         this.viewProvider = (TextView)this.findViewById(2131492949);
         this.viewAlt = (TextView)this.findViewById(2131492953);
         this.viewSpeed = (TextView)this.findViewById(2131492954);
         this.viewAcc = (TextView)this.findViewById(2131492955);
         this.viewLat = (TextView)this.findViewById(2131492948);
         this.viewLon = (TextView)this.findViewById(2131492950);
         this.viewTimeToTarget = (TextView)this.findViewById(2131492956);
         this.onOrientationChanged(this.mAzimuth, this.mPitch, this.mRoll);
      }

   }

   public void onOrientationChanged(float var1, float var2, float var3) {
      Location var4 = LocationState.getLocation();
      this.mAzimuth = var1;
      this.mPitch = var2;
      this.mRoll = var3;
      String var5 = var4.getProvider();
      byte var6 = -1;
      switch(var5.hashCode()) {
      case 102570:
         if (var5.equals("gps")) {
            var6 = 0;
         }
         break;
      case 1843485230:
         if (var5.equals("network")) {
            var6 = 1;
         }
      }

      switch(var6) {
      case 0:
         var5 = this.getString(2131165393);
         break;
      case 1:
         var5 = this.getString(2131165394);
         break;
      default:
         var5 = this.getString(2131165395);
      }

      this.viewProvider.setText(var5);
      this.viewLat.setText(UtilsFormat.formatLatitude(var4.getLatitude()));
      this.viewLon.setText(UtilsFormat.formatLongitude(var4.getLongitude()));
      this.viewAlt.setText(UtilsFormat.formatAltitude(var4.getAltitude(), true));
      this.viewAcc.setText(UtilsFormat.formatDistance((double)var4.getAccuracy(), false));
      this.viewSpeed.setText(UtilsFormat.formatSpeed((double)var4.getSpeed(), false));
      this.repaint();
   }

   public void onStart() {
      super.onStart();
      A.getGuidingContent().addGuidingListener(this);
      A.getRotator().addListener(this);
   }

   public void onStop() {
      super.onStop();
      A.getGuidingContent().removeGuidingListener(this);
      A.getRotator().removeListener(this);
   }

   public void receiveGuideEvent(IGuide var1, String var2, float var3, double var4) {
      this.viewName.setText(var2);
      this.azimuthToTarget = var3;
      this.viewCompass.setDistance(var4);
      if (LocationState.getLocation().getSpeed() > 1.0F) {
         this.viewTimeToTarget.setText(UtilsFormat.formatTime(true, (long)(var4 / (double)LocationState.getLocation().getSpeed()) * 1000L));
      } else {
         this.viewTimeToTarget.setText(UtilsFormat.formatTime(true, 0L));
      }

      this.repaint();
   }

   public void refresh() {
      this.runOnUiThread(new Runnable() {
         public void run() {
            EventTable var1 = DetailsActivity.et;
            if (var1 != null && var1.isLocated() && var1.isVisible() && A.getGuidingContent() != null) {
               Location var2 = A.getGuidingContent().getTargetLocation();
               Location var3 = UtilsWherigo.extractLocation(var1);
               if (var3 != null && !var3.equals(var2)) {
                  A.getGuidingContent().guideStart(new Guide(var1.name, var3));
               }
            }

         }
      });
   }

   public void trackGuideCallRecalculate() {
   }
}
