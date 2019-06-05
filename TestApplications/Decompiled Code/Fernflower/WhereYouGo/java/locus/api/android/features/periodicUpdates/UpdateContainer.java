package locus.api.android.features.periodicUpdates;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.TrackStats;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class UpdateContainer extends Storable {
   public static final int GUIDE_TYPE_DISABLED = -1;
   public static final int GUIDE_TYPE_TRACK_GUIDE = 2;
   public static final int GUIDE_TYPE_TRACK_NAVIGATION = 3;
   public static final int GUIDE_TYPE_WAYPOINT = 1;
   protected float declination;
   protected float deviceBatteryTemperature;
   protected int deviceBatteryValue;
   protected boolean enabledMyLocation;
   protected int gpsSatsAll;
   protected int gpsSatsUsed;
   protected double guideDistFromStart;
   protected double guideDistToFinish;
   protected int guideNavPoint1Action;
   protected double guideNavPoint1Dist;
   protected Location guideNavPoint1Loc;
   protected String guideNavPoint1Name;
   protected long guideNavPoint1Time;
   protected int guideNavPoint2Action;
   protected double guideNavPoint2Dist;
   protected Location guideNavPoint2Loc;
   protected String guideNavPoint2Name;
   protected long guideNavPoint2Time;
   protected long guideTimeToFinish;
   protected int guideType;
   protected float guideWptAngle;
   protected float guideWptAzim;
   protected double guideWptDist;
   protected Location guideWptLoc;
   protected String guideWptName;
   protected long guideWptTime;
   protected boolean isUserTouching;
   protected Location locMapCenter;
   protected Location locMyLocation;
   protected Location mapBottomRight;
   protected float mapRotate;
   protected Location mapTopLeft;
   protected boolean mapVisible;
   protected int mapZoomLevel;
   protected boolean newMapCenter;
   protected boolean newMyLocation;
   protected boolean newZoomLevel;
   protected float orientCourse;
   protected float orientGpsAngle;
   protected float orientHeading;
   protected float orientHeadingOpposit;
   protected float orientPitch;
   protected float orientRoll;
   protected float slope;
   protected float speedVertical;
   protected boolean trackRecPaused;
   protected String trackRecProfileName;
   protected boolean trackRecRecording;
   protected TrackStats trackStats;

   private Location readLocation(DataReaderBigEndian var1) throws IOException {
      Location var6;
      if (!var1.readBoolean()) {
         var6 = null;
         return var6;
      } else {
         Object var5;
         try {
            var6 = (Location)var1.readStorable(Location.class);
            return var6;
         } catch (InstantiationException var2) {
            var5 = var2;
         } catch (IllegalAccessException var3) {
            var5 = var3;
         } catch (IOException var4) {
            var5 = var4;
         }

         throw new IOException(((Exception)var5).getMessage());
      }
   }

   private void writeLocation(DataWriterBigEndian var1, Location var2) throws IOException {
      if (var2 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeStorable(var2);
      }

   }

   public float getDeclination() {
      return this.declination;
   }

   public float getDeviceBatteryTemperature() {
      return this.deviceBatteryTemperature;
   }

   public int getDeviceBatteryValue() {
      return this.deviceBatteryValue;
   }

   public int getGpsSatsAll() {
      return this.gpsSatsAll;
   }

   public int getGpsSatsUsed() {
      return this.gpsSatsUsed;
   }

   public int getGuideType() {
      return this.guideType;
   }

   public UpdateContainer.GuideTypeTrack getGuideTypeTrack() {
      UpdateContainer.GuideTypeTrack var1 = null;
      if (this.guideType == 2 || this.guideType == 3) {
         var1 = new UpdateContainer.GuideTypeTrack();
      }

      return var1;
   }

   public UpdateContainer.GuideTypeWaypoint getGuideTypeWaypoint() {
      UpdateContainer.GuideTypeWaypoint var1 = null;
      if (this.guideType == 1) {
         var1 = new UpdateContainer.GuideTypeWaypoint();
      }

      return var1;
   }

   public Location getLocMapCenter() {
      return this.locMapCenter;
   }

   public Location getLocMyLocation() {
      return this.locMyLocation;
   }

   public Location getMapBottomRight() {
      return this.mapBottomRight;
   }

   public float getMapRotate() {
      return this.mapRotate;
   }

   public Location getMapTopLeft() {
      return this.mapTopLeft;
   }

   public int getMapZoomLevel() {
      return this.mapZoomLevel;
   }

   public float getOrientCourse() {
      return this.orientCourse;
   }

   public float getOrientGpsAngle() {
      return this.orientGpsAngle;
   }

   public float getOrientHeading() {
      return this.orientHeading;
   }

   public float getOrientHeadingOpposit() {
      return this.orientHeadingOpposit;
   }

   public float getOrientPitch() {
      return this.orientPitch;
   }

   public float getOrientRoll() {
      return this.orientRoll;
   }

   public float getSlope() {
      return this.slope;
   }

   public float getSpeedVertical() {
      return this.speedVertical;
   }

   public String getTrackRecProfileName() {
      return this.trackRecProfileName;
   }

   public TrackStats getTrackRecStats() {
      return this.trackStats;
   }

   protected int getVersion() {
      return 0;
   }

   public boolean isEnabledMyLocation() {
      return this.enabledMyLocation;
   }

   public boolean isGuideEnabled() {
      boolean var1;
      if (this.getGuideType() != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isMapVisible() {
      return this.mapVisible;
   }

   public boolean isNewMapCenter() {
      return this.newMapCenter;
   }

   public boolean isNewMyLocation() {
      return this.newMyLocation;
   }

   public boolean isNewZoomLevel() {
      return this.newZoomLevel;
   }

   public boolean isTrackRecPaused() {
      return this.trackRecPaused;
   }

   public boolean isTrackRecRecording() {
      return this.trackRecRecording;
   }

   public boolean isUserTouching() {
      return this.isUserTouching;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.newMyLocation = var2.readBoolean();
      this.newMapCenter = var2.readBoolean();
      this.newZoomLevel = var2.readBoolean();
      this.isUserTouching = var2.readBoolean();
      this.enabledMyLocation = var2.readBoolean();
      this.locMyLocation = this.readLocation(var2);
      this.gpsSatsUsed = var2.readInt();
      this.gpsSatsAll = var2.readInt();
      this.declination = var2.readFloat();
      this.orientHeading = var2.readFloat();
      this.orientHeadingOpposit = var2.readFloat();
      this.orientCourse = var2.readFloat();
      this.orientPitch = var2.readFloat();
      this.orientRoll = var2.readFloat();
      this.orientGpsAngle = var2.readFloat();
      this.speedVertical = var2.readFloat();
      this.slope = var2.readFloat();
      this.mapVisible = var2.readBoolean();
      this.mapRotate = var2.readFloat();
      this.locMapCenter = this.readLocation(var2);
      this.mapTopLeft = this.readLocation(var2);
      this.mapBottomRight = this.readLocation(var2);
      this.mapZoomLevel = var2.readInt();
      this.trackRecRecording = var2.readBoolean();
      this.trackRecPaused = var2.readBoolean();
      this.trackRecProfileName = var2.readString();
      if (var2.readBoolean()) {
         this.trackStats = new TrackStats();
         this.trackStats.read(var2);
      }

      this.guideType = var2.readInt();
      this.guideWptName = var2.readString();
      this.guideWptLoc = this.readLocation(var2);
      this.guideWptDist = var2.readDouble();
      this.guideWptAzim = var2.readFloat();
      this.guideWptAngle = var2.readFloat();
      this.guideWptTime = var2.readLong();
      this.guideDistFromStart = var2.readDouble();
      this.guideDistToFinish = var2.readDouble();
      this.guideTimeToFinish = var2.readLong();
      this.guideNavPoint1Name = var2.readString();
      this.guideNavPoint1Loc = this.readLocation(var2);
      this.guideNavPoint1Dist = var2.readDouble();
      this.guideNavPoint1Time = var2.readLong();
      this.guideNavPoint1Action = var2.readInt();
      this.guideNavPoint2Name = var2.readString();
      this.guideNavPoint2Loc = this.readLocation(var2);
      this.guideNavPoint2Dist = var2.readDouble();
      this.guideNavPoint2Time = var2.readLong();
      this.guideNavPoint2Action = var2.readInt();
      this.deviceBatteryValue = var2.readInt();
      this.deviceBatteryTemperature = var2.readFloat();
   }

   public void reset() {
      this.newMyLocation = false;
      this.newMapCenter = false;
      this.newZoomLevel = false;
      this.isUserTouching = false;
      this.enabledMyLocation = false;
      this.locMyLocation = null;
      this.gpsSatsUsed = 0;
      this.gpsSatsAll = 0;
      this.declination = 0.0F;
      this.orientHeading = 0.0F;
      this.orientHeadingOpposit = 0.0F;
      this.orientCourse = 0.0F;
      this.orientPitch = 0.0F;
      this.orientRoll = 0.0F;
      this.orientGpsAngle = 0.0F;
      this.speedVertical = 0.0F;
      this.slope = 0.0F;
      this.mapVisible = false;
      this.mapRotate = 0.0F;
      this.locMapCenter = null;
      this.mapTopLeft = null;
      this.mapBottomRight = null;
      this.mapZoomLevel = -1;
      this.trackRecRecording = false;
      this.trackRecPaused = false;
      this.trackRecProfileName = "";
      this.trackStats = null;
      this.guideType = -1;
      this.guideWptName = null;
      this.guideWptLoc = null;
      this.guideWptDist = 0.0D;
      this.guideWptAzim = 0.0F;
      this.guideWptAngle = 0.0F;
      this.guideWptTime = 0L;
      this.guideDistFromStart = 0.0D;
      this.guideDistToFinish = 0.0D;
      this.guideTimeToFinish = 0L;
      this.guideNavPoint1Name = "";
      this.guideNavPoint1Loc = null;
      this.guideNavPoint1Dist = 0.0D;
      this.guideNavPoint1Time = 0L;
      this.guideNavPoint1Action = 0;
      this.guideNavPoint2Name = "";
      this.guideNavPoint2Loc = null;
      this.guideNavPoint2Dist = 0.0D;
      this.guideNavPoint2Time = 0L;
      this.guideNavPoint2Action = 0;
      this.deviceBatteryValue = 0;
      this.deviceBatteryTemperature = 0.0F;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeBoolean(this.newMyLocation);
      var1.writeBoolean(this.newMapCenter);
      var1.writeBoolean(this.newZoomLevel);
      var1.writeBoolean(this.isUserTouching);
      var1.writeBoolean(this.enabledMyLocation);
      this.writeLocation(var1, this.locMyLocation);
      var1.writeInt(this.gpsSatsUsed);
      var1.writeInt(this.gpsSatsAll);
      var1.writeFloat(this.declination);
      var1.writeFloat(this.orientHeading);
      var1.writeFloat(this.orientHeadingOpposit);
      var1.writeFloat(this.orientCourse);
      var1.writeFloat(this.orientPitch);
      var1.writeFloat(this.orientRoll);
      var1.writeFloat(this.orientGpsAngle);
      var1.writeFloat(this.speedVertical);
      var1.writeFloat(this.slope);
      var1.writeBoolean(this.mapVisible);
      var1.writeFloat(this.mapRotate);
      this.writeLocation(var1, this.locMapCenter);
      this.writeLocation(var1, this.mapTopLeft);
      this.writeLocation(var1, this.mapBottomRight);
      var1.writeInt(this.mapZoomLevel);
      var1.writeBoolean(this.trackRecRecording);
      var1.writeBoolean(this.trackRecPaused);
      var1.writeString(this.trackRecProfileName);
      if (this.trackStats != null) {
         var1.writeBoolean(true);
         var1.writeStorable(this.trackStats);
      } else {
         var1.writeBoolean(false);
      }

      var1.writeInt(this.guideType);
      var1.writeString(this.guideWptName);
      this.writeLocation(var1, this.guideWptLoc);
      var1.writeDouble(this.guideWptDist);
      var1.writeFloat(this.guideWptAzim);
      var1.writeFloat(this.guideWptAngle);
      var1.writeLong(this.guideWptTime);
      var1.writeDouble(this.guideDistFromStart);
      var1.writeDouble(this.guideDistToFinish);
      var1.writeLong(this.guideTimeToFinish);
      var1.writeString(this.guideNavPoint1Name);
      this.writeLocation(var1, this.guideNavPoint1Loc);
      var1.writeDouble(this.guideNavPoint1Dist);
      var1.writeLong(this.guideNavPoint1Time);
      var1.writeInt(this.guideNavPoint1Action);
      var1.writeString(this.guideNavPoint2Name);
      this.writeLocation(var1, this.guideNavPoint2Loc);
      var1.writeDouble(this.guideNavPoint2Dist);
      var1.writeLong(this.guideNavPoint2Time);
      var1.writeInt(this.guideNavPoint2Action);
      var1.writeInt(this.deviceBatteryValue);
      var1.writeFloat(this.deviceBatteryTemperature);
   }

   private class GuideTypeBasic {
      private GuideTypeBasic() {
      }

      // $FF: synthetic method
      GuideTypeBasic(Object var2) {
         this();
      }

      public float getTargetAngle() {
         return UpdateContainer.this.guideWptAngle;
      }

      public float getTargetAzim() {
         return UpdateContainer.this.guideWptAzim;
      }

      public double getTargetDist() {
         return UpdateContainer.this.guideWptDist;
      }

      public Location getTargetLoc() {
         return UpdateContainer.this.guideWptLoc;
      }

      public String getTargetName() {
         return UpdateContainer.this.guideWptName;
      }

      public long getTargetTime() {
         return UpdateContainer.this.guideWptTime;
      }
   }

   public class GuideTypeTrack extends UpdateContainer.GuideTypeBasic {
      private GuideTypeTrack() {
         super(null);
      }

      // $FF: synthetic method
      GuideTypeTrack(Object var2) {
         this();
      }

      public double getDistFromStart() {
         return UpdateContainer.this.guideDistFromStart;
      }

      public double getDistToFinish() {
         return UpdateContainer.this.guideDistToFinish;
      }

      public int getNavPoint1Action() {
         return UpdateContainer.this.guideNavPoint1Action;
      }

      public double getNavPoint1Dist() {
         return UpdateContainer.this.guideNavPoint1Dist;
      }

      public Location getNavPoint1Loc() {
         return UpdateContainer.this.guideNavPoint1Loc;
      }

      public String getNavPoint1Name() {
         return UpdateContainer.this.guideNavPoint1Name;
      }

      public double getNavPoint1Time() {
         return (double)UpdateContainer.this.guideNavPoint1Time;
      }

      public int getNavPoint2Action() {
         return UpdateContainer.this.guideNavPoint2Action;
      }

      public double getNavPoint2Dist() {
         return UpdateContainer.this.guideNavPoint2Dist;
      }

      public Location getNavPoint2Loc() {
         return UpdateContainer.this.guideNavPoint2Loc;
      }

      public String getNavPoint2Name() {
         return UpdateContainer.this.guideNavPoint2Name;
      }

      public double getNavPoint2Time() {
         return (double)UpdateContainer.this.guideNavPoint2Time;
      }

      public long getTimeToFinish() {
         return UpdateContainer.this.guideTimeToFinish;
      }

      public boolean hasNavPoint1() {
         boolean var1;
         if (this.getNavPoint1Loc() != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean hasNavPoint2() {
         boolean var1;
         if (this.getNavPoint2Loc() != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public class GuideTypeWaypoint extends UpdateContainer.GuideTypeBasic {
      private GuideTypeWaypoint() {
         super(null);
      }

      // $FF: synthetic method
      GuideTypeWaypoint(Object var2) {
         this();
      }
   }
}
