package locus.api.objects.geocaching;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingWaypoint extends Storable {
   public static final String CACHE_WAYPOINT_TYPE_FINAL = "Final Location";
   public static final String CACHE_WAYPOINT_TYPE_PARKING = "Parking Area";
   public static final String CACHE_WAYPOINT_TYPE_PHYSICAL_STAGE = "Physical Stage";
   @Deprecated
   public static final String CACHE_WAYPOINT_TYPE_QUESTION = "Question to Answer";
   public static final String CACHE_WAYPOINT_TYPE_REFERENCE = "Reference Point";
   @Deprecated
   public static final String CACHE_WAYPOINT_TYPE_STAGES = "Stages of a Multicache";
   public static final String CACHE_WAYPOINT_TYPE_TRAILHEAD = "Trailhead";
   public static final String CACHE_WAYPOINT_TYPE_VIRTUAL_STAGE = "Virtual Stage";
   private static final String TAG = "GeocachingWaypoint";
   private String mCode;
   private String mDesc;
   private boolean mDescModified;
   private double mLat;
   private double mLon;
   private String mName;
   private String mType;
   private String mTypeImagePath;

   public String getCode() {
      return this.mCode;
   }

   public String getDesc() {
      return this.mDesc;
   }

   public double getLat() {
      return this.mLat;
   }

   public double getLon() {
      return this.mLon;
   }

   public String getName() {
      return this.mName;
   }

   public String getType() {
      return this.mType;
   }

   public String getTypeImagePath() {
      return this.mTypeImagePath;
   }

   protected int getVersion() {
      return 1;
   }

   public boolean isDescModified() {
      return this.mDescModified;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mCode = var2.readString();
      this.mName = var2.readString();
      this.mDesc = var2.readString();
      this.mType = var2.readString();
      this.mTypeImagePath = var2.readString();
      this.mLon = var2.readDouble();
      this.mLat = var2.readDouble();
      if (var1 >= 1) {
         this.mDescModified = var2.readBoolean();
      }

   }

   public void reset() {
      this.mCode = "";
      this.mName = "";
      this.mDesc = "";
      this.mType = "";
      this.mTypeImagePath = "";
      this.mLon = 0.0D;
      this.mLat = 0.0D;
      this.mDescModified = false;
   }

   public void setCode(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingWaypoint", "setCode(), empty parameter");
         var2 = "";
      }

      this.mCode = var2;
   }

   public void setDesc(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingWaypoint", "setDesc(), empty parameter");
         var2 = "";
      }

      this.mDesc = var2;
   }

   public void setDescModified(boolean var1) {
      this.mDescModified = var1;
   }

   public void setLat(double var1) {
      this.mLat = var1;
   }

   public void setLon(double var1) {
      this.mLon = var1;
   }

   public void setName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingWaypoint", "setName(), empty parameter");
         var2 = "";
      }

      this.mName = var2;
   }

   public void setType(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingWaypoint", "setType(), empty parameter");
         var2 = "";
      }

      var1 = var2;
      if (var2.toLowerCase().startsWith("waypoint|")) {
         var1 = var2.substring("waypoint|".length());
      }

      this.mType = var1;
   }

   public void setTypeImagePath(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingWaypoint", "setTypeImagePath(), empty parameter");
         var2 = "";
      }

      this.mTypeImagePath = var2;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mCode);
      var1.writeString(this.mName);
      var1.writeString(this.mDesc);
      var1.writeString(this.mType);
      var1.writeString(this.mTypeImagePath);
      var1.writeDouble(this.mLon);
      var1.writeDouble(this.mLat);
      var1.writeBoolean(this.mDescModified);
   }
}
