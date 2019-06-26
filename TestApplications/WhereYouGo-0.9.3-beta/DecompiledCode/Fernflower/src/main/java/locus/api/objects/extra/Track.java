package locus.api.objects.extra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import locus.api.objects.GeoData;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class Track extends GeoData {
   private static final String TAG = "Track";
   List breaks;
   private int mActivityType;
   private TrackStats mStats;
   private boolean mUseFolderStyle;
   List points;
   List waypoints;

   public Track() {
   }

   public Track(DataReaderBigEndian var1) throws IOException {
      super(var1);
   }

   public Track(byte[] var1) throws IOException {
      super(var1);
   }

   public int getActivityType() {
      return this.mActivityType;
   }

   public List getBreaks() {
      return this.breaks;
   }

   public byte[] getBreaksData() {
      // $FF: Couldn't be decompiled
   }

   public Location getPoint(int var1) {
      return (Location)this.points.get(var1);
   }

   public List getPoints() {
      return this.points;
   }

   public int getPointsCount() {
      return this.points.size();
   }

   public TrackStats getStats() {
      return this.mStats;
   }

   public int getVersion() {
      return 5;
   }

   public Waypoint getWaypoint(int var1) {
      return (Waypoint)this.waypoints.get(var1);
   }

   public List getWaypoints() {
      return this.waypoints;
   }

   public boolean isUseFolderStyle() {
      return this.mUseFolderStyle;
   }

   public void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.id = var2.readLong();
      this.name = var2.readString();
      this.points = var2.readListStorable(Location.class);
      int var3 = var2.readInt();
      if (var3 > 0) {
         this.setBreaksData(var2.readBytes(var3));
      }

      this.waypoints = var2.readListStorable(Waypoint.class);
      this.readExtraData(var2);
      this.readStyles(var2);
      this.mStats.reset();
      this.mStats.setNumOfPoints(var2.readInt());
      this.mStats.setStartTime(var2.readLong());
      this.mStats.setStopTime(var2.readLong());
      this.mStats.setTotalLength(var2.readFloat());
      this.mStats.setTotalLengthMove(var2.readFloat());
      this.mStats.setTotalTime(var2.readLong());
      this.mStats.setTotalTimeMove(var2.readLong());
      this.mStats.setSpeedMax(var2.readFloat());
      this.mStats.setAltitudeMax(var2.readFloat());
      this.mStats.setAltitudeMin(var2.readFloat());
      this.mStats.setEleNeutralDistance(var2.readFloat());
      this.mStats.setEleNeutralHeight(var2.readFloat());
      this.mStats.setElePositiveDistance(var2.readFloat());
      this.mStats.setElePositiveHeight(var2.readFloat());
      this.mStats.setEleNegativeDistance(var2.readFloat());
      this.mStats.setEleNegativeHeight(var2.readFloat());
      this.mStats.setEleTotalAbsDistance(var2.readFloat());
      this.mStats.setEleTotalAbsHeight(var2.readFloat());
      if (var1 >= 1) {
         this.mUseFolderStyle = var2.readBoolean();
      }

      if (var1 >= 2) {
         this.timeCreated = var2.readLong();
      }

      if (var1 >= 3) {
         this.mStats = new TrackStats(var2);
      }

      if (var1 >= 4) {
         this.setReadWriteMode(GeoData.ReadWriteMode.values()[var2.readInt()]);
      }

      if (var1 >= 5) {
         this.mActivityType = var2.readInt();
      }

   }

   public void reset() {
      this.id = -1L;
      this.name = "";
      this.points = new ArrayList();
      this.breaks = new ArrayList();
      this.waypoints = new ArrayList();
      this.extraData = null;
      this.styleNormal = null;
      this.styleHighlight = null;
      this.mUseFolderStyle = true;
      this.timeCreated = System.currentTimeMillis();
      this.mStats = new TrackStats();
      this.setReadWriteMode(GeoData.ReadWriteMode.READ_WRITE);
      this.mActivityType = 0;
   }

   public void setActivityType(int var1) {
      this.mActivityType = var1;
   }

   public void setBreaksData(byte[] var1) {
      if (var1 != null && var1.length != 0) {
         Exception var10000;
         label26: {
            boolean var10001;
            DataReaderBigEndian var2;
            try {
               var2 = new DataReaderBigEndian(var1);
               this.breaks.clear();
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label26;
            }

            while(true) {
               try {
                  if (var2.available() <= 0) {
                     return;
                  }

                  this.breaks.add(var2.readInt());
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
                  break;
               }
            }
         }

         Exception var5 = var10000;
         Logger.logE("Track", "setBreaksData()", var5);
         this.breaks.clear();
      }

   }

   public boolean setPoints(List var1) {
      boolean var2;
      if (var1 == null) {
         Logger.logW("Track", "setPoints(), cannot be null!");
         var2 = false;
      } else {
         this.points = var1;
         var2 = true;
      }

      return var2;
   }

   public void setStats(TrackStats var1) {
      if (var1 == null) {
         throw new NullPointerException("setTrackStats(), parameter cannot be null");
      } else {
         this.mStats = var1;
      }
   }

   public void setStats(byte[] var1) {
      try {
         TrackStats var2 = new TrackStats(var1);
         this.setStats(var2);
      } catch (Exception var3) {
         Logger.logE("Track", "setStats(" + Arrays.toString(var1) + ")", var3);
         this.setStats(new TrackStats());
      }

   }

   public void setUseFolderStyle(boolean var1) {
      this.mUseFolderStyle = var1;
   }

   public boolean setWaypoints(List var1) {
      boolean var2;
      if (var1 == null) {
         Logger.logW("Track", "setWaypoints(), cannot be null!");
         var2 = false;
      } else {
         this.waypoints = var1;
         var2 = true;
      }

      return var2;
   }

   public void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.id);
      var1.writeString(this.name);
      var1.writeListStorable(this.points);
      byte[] var2 = this.getBreaksData();
      var1.writeInt(var2.length);
      if (var2.length > 0) {
         var1.write(var2);
      }

      var1.writeListStorable(this.waypoints);
      this.writeExtraData(var1);
      this.writeStyles(var1);
      var1.writeInt(0);
      var1.writeLong(0L);
      var1.writeLong(0L);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeLong(0L);
      var1.writeLong(0L);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeFloat(0.0F);
      var1.writeBoolean(this.mUseFolderStyle);
      var1.writeLong(this.timeCreated);
      var1.writeStorable(this.mStats);
      var1.writeInt(this.getReadWriteMode().ordinal());
      var1.writeInt(this.mActivityType);
   }
}
