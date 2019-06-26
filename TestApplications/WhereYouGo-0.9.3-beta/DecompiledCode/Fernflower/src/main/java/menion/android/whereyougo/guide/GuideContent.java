package menion.android.whereyougo.guide;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;

public class GuideContent implements ILocationEventListener {
   private static final String TAG = "NavigationContent";
   private final ArrayList listeners = new ArrayList();
   private float mAzimuthToTarget;
   private float mDistanceToTarget;
   private IGuide mGuide;
   private Location mLocation;
   private String mTargetName;

   public void addGuidingListener(IGuideEventListener var1) {
      this.listeners.add(var1);
      this.onLocationChanged(LocationState.getLocation());
   }

   public IGuide getGuide() {
      return this.mGuide;
   }

   public String getName() {
      return "NavigationContent";
   }

   public int getPriority() {
      return 3;
   }

   public Location getTargetLocation() {
      Location var1;
      if (this.mGuide == null) {
         var1 = null;
      } else {
         var1 = this.mGuide.getTargetLocation();
      }

      return var1;
   }

   public void guideStart(IGuide var1) {
      this.mGuide = var1;
      LocationState.addLocationChangeListener(this);
      this.onLocationChanged(LocationState.getLocation());
      (new Thread(new Runnable() {
         public void run() {
            while(true) {
               try {
                  if (GuideContent.this.mGuide != null) {
                     if (Preferences.GUIDING_SOUNDS) {
                        GuideContent.this.mGuide.manageDistanceSoundsBeeping((double)GuideContent.this.mDistanceToTarget);
                     }

                     Thread.sleep(100L);
                     continue;
                  }
               } catch (Exception var2) {
                  Logger.e("NavigationContent", "guideStart(" + GuideContent.this.mGuide + ")", var2);
               }

               return;
            }
         }
      })).start();
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((IGuideEventListener)var2.next()).guideStart();
      }

   }

   public void guideStop() {
      this.mGuide = null;
      LocationState.removeLocationChangeListener(this);
      this.onLocationChanged(LocationState.getLocation());
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         ((IGuideEventListener)var1.next()).guideStop();
      }

   }

   public boolean isGuiding() {
      boolean var1;
      if (this.getTargetLocation() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isRequired() {
      return Preferences.GUIDING_GPS_REQUIRED;
   }

   public void onGpsStatusChanged(int var1, ArrayList var2) {
   }

   public void onLocationChanged(Location var1) {
      if (this.mGuide != null && var1 != null) {
         this.mGuide.actualizeState(var1);
         this.mTargetName = this.mGuide.getTargetName();
         this.mAzimuthToTarget = this.mGuide.getAzimuthToTaget();
         this.mDistanceToTarget = this.mGuide.getDistanceToTarget();
         this.mLocation = var1;
      } else {
         this.mTargetName = null;
         this.mAzimuthToTarget = 0.0F;
         this.mDistanceToTarget = 0.0F;
      }

      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((IGuideEventListener)var2.next()).receiveGuideEvent(this.mGuide, this.mTargetName, this.mAzimuthToTarget, (double)this.mDistanceToTarget);
      }

   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void removeGuidingListener(IGuideEventListener var1) {
      this.listeners.remove(var1);
   }

   protected void trackGuideCallRecalculate() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         ((IGuideEventListener)var1.next()).trackGuideCallRecalculate();
      }

   }
}
