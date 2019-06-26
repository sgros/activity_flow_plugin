package menion.android.whereyougo.guide;

import android.net.Uri;
import java.util.Timer;
import java.util.TimerTask;
import menion.android.whereyougo.audio.AudioClip;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;

public class Guide implements IGuide {
   private static final String TAG = "WaypointGuide";
   private boolean alreadyBeeped;
   private AudioClip audioBeep;
   private float azimuth;
   private float distance;
   private int id;
   private long lastSonarCall;
   private final Location location;
   private final String name;

   public Guide(String var1, Location var2) {
      this.name = var1;
      this.location = var2;
      this.alreadyBeeped = false;
      this.lastSonarCall = 0L;

      try {
         AudioClip var4 = new AudioClip(A.getApp(), 2131099648);
         this.audioBeep = var4;
      } catch (Exception var3) {
         Logger.e("WaypointGuide", "Guide(2131099648), e:" + var3.toString());
      }

   }

   private long getSonarTimeout(double var1) {
      long var3;
      if (var1 < (double)Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE) {
         var3 = (long)(1000.0D * var1 / 33.0D);
      } else {
         var3 = Long.MAX_VALUE;
      }

      return var3;
   }

   public void actualizeState(Location var1) {
      this.azimuth = var1.bearingTo(this.location);
      this.distance = var1.distanceTo(this.location);
   }

   public float getAzimuthToTaget() {
      return this.azimuth;
   }

   public float getDistanceToTarget() {
      return this.distance;
   }

   public int getId() {
      return this.id;
   }

   public Location getTargetLocation() {
      return this.location;
   }

   public String getTargetName() {
      return this.name;
   }

   public long getTimeToTarget() {
      long var1;
      if ((double)LocationState.getLocation().getSpeed() > 1.0D) {
         var1 = (long)(this.getDistanceToTarget() / LocationState.getLocation().getSpeed() * 1000.0F);
      } else {
         var1 = 0L;
      }

      return var1;
   }

   public void manageDistanceSoundsBeeping(double var1) {
      try {
         switch(Preferences.GUIDING_WAYPOINT_SOUND) {
         case 0:
            long var4 = System.currentTimeMillis();
            float var6 = (float)this.getSonarTimeout(var1);
            if ((float)(var4 - this.lastSonarCall) > var6) {
               this.lastSonarCall = var4;
               this.playSingleBeep();
            }
            break;
         case 1:
            if (var1 < (double)Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE && !this.alreadyBeeped) {
               this.playSingleBeep();
               this.alreadyBeeped = true;
            }
            break;
         case 2:
            if (var1 < (double)Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE && !this.alreadyBeeped) {
               this.playCustomSound();
               this.alreadyBeeped = true;
            }
         }
      } catch (Exception var7) {
         Logger.e("WaypointGuide", "manageDistanceSounds(" + var1 + "), e:" + var7.toString());
      }

   }

   protected void playCustomSound() {
      String var1 = Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI;

      try {
         final AudioClip var2 = new AudioClip(A.getApp(), Uri.parse(var1));
         var2.play();
         Timer var3 = new Timer();
         TimerTask var4 = new TimerTask() {
            public void run() {
               AudioClip.destroyAudio(var2);
            }
         };
         var3.schedule(var4, 5000L);
      } catch (Exception var5) {
         Logger.e("WaypointGuide", "playCustomSound(" + var1 + "), e:" + var5.toString());
      }

   }

   protected void playSingleBeep() {
      if (this.audioBeep != null) {
         this.audioBeep.play();
      }

   }
}
