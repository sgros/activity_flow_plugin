package locus.api.objects.geocaching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingLog extends Storable {
   public static final int CACHE_LOG_TYPE_ANNOUNCEMENT = 9;
   public static final int CACHE_LOG_TYPE_ARCHIVE = 16;
   public static final int CACHE_LOG_TYPE_ATTENDED = 11;
   public static final int CACHE_LOG_TYPE_ENABLE_LISTING = 6;
   public static final int CACHE_LOG_TYPE_FOUND = 0;
   public static final int CACHE_LOG_TYPE_NEEDS_ARCHIVED = 13;
   public static final int CACHE_LOG_TYPE_NEEDS_MAINTENANCE = 3;
   public static final int CACHE_LOG_TYPE_NOT_FOUND = 1;
   public static final int CACHE_LOG_TYPE_OWNER_MAINTENANCE = 4;
   public static final int CACHE_LOG_TYPE_PERMANENTLY_ARCHIVED = 18;
   public static final int CACHE_LOG_TYPE_POST_REVIEWER_NOTE = 12;
   public static final int CACHE_LOG_TYPE_PUBLISH_LISTING = 5;
   public static final int CACHE_LOG_TYPE_RETRACT_LISTING = 15;
   public static final int CACHE_LOG_TYPE_TEMPORARILY_DISABLE_LISTING = 7;
   public static final int CACHE_LOG_TYPE_UNARCHIVE = 17;
   public static final int CACHE_LOG_TYPE_UNKNOWN = -1;
   public static final int CACHE_LOG_TYPE_UPDATE_COORDINATES = 8;
   public static final int CACHE_LOG_TYPE_WEBCAM_PHOTO_TAKEN = 14;
   public static final int CACHE_LOG_TYPE_WILL_ATTEND = 10;
   public static final int CACHE_LOG_TYPE_WRITE_NOTE = 2;
   public static final long FINDERS_ID_UNDEFINED = 0L;
   private static final String TAG = "GeocachingLog";
   private double mCooLat;
   private double mCooLon;
   private long mDate;
   private String mFinder;
   private int mFindersFound;
   private long mFindersId;
   private long mId;
   private List mImages;
   private String mLogText;
   private int mType;

   public void addImage(GeocachingImage var1) {
      this.mImages.add(var1);
   }

   public double getCooLat() {
      return this.mCooLat;
   }

   public double getCooLon() {
      return this.mCooLon;
   }

   public long getDate() {
      return this.mDate;
   }

   public String getFinder() {
      return this.mFinder;
   }

   public int getFindersFound() {
      return this.mFindersFound;
   }

   public long getFindersId() {
      return this.mFindersId;
   }

   public long getId() {
      return this.mId;
   }

   public Iterator getImages() {
      return this.mImages.iterator();
   }

   public String getLogText() {
      return this.mLogText;
   }

   public int getType() {
      return this.mType;
   }

   protected int getVersion() {
      return 2;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readLong();
      this.mType = var2.readInt();
      this.mDate = var2.readLong();
      this.mFinder = var2.readString();
      this.mFindersFound = var2.readInt();
      this.mLogText = var2.readString();
      if (var1 >= 1) {
         this.mImages = var2.readListStorable(GeocachingImage.class);
      }

      if (var1 >= 2) {
         this.mFindersId = var2.readLong();
         this.mCooLon = var2.readDouble();
         this.mCooLat = var2.readDouble();
      }

   }

   public void reset() {
      this.mId = 0L;
      this.mType = -1;
      this.mDate = 0L;
      this.mFinder = "";
      this.mFindersFound = 0;
      this.mLogText = "";
      this.mImages = new ArrayList();
      this.mFindersId = 0L;
      this.mCooLon = 0.0D;
      this.mCooLat = 0.0D;
   }

   public void setCooLat(double var1) {
      this.mCooLat = var1;
   }

   public void setCooLon(double var1) {
      this.mCooLon = var1;
   }

   public void setDate(long var1) {
      this.mDate = var1;
   }

   public void setFinder(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingLog", "setFinder(), empty parameter");
         var2 = "";
      }

      this.mFinder = var2;
   }

   public void setFindersFound(int var1) {
      this.mFindersFound = var1;
   }

   public void setFindersId(long var1) {
      this.mFindersId = var1;
   }

   public void setId(long var1) {
      this.mId = var1;
   }

   public void setLogText(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD("GeocachingLog", "setLogText(), empty parameter");
         var2 = "";
      }

      this.mLogText = var2;
   }

   public void setType(int var1) {
      this.mType = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.mId);
      var1.writeInt(this.mType);
      var1.writeLong(this.mDate);
      var1.writeString(this.mFinder);
      var1.writeInt(this.mFindersFound);
      var1.writeString(this.mLogText);
      var1.writeListStorable(this.mImages);
      var1.writeLong(this.mFindersId);
      var1.writeDouble(this.mCooLon);
      var1.writeDouble(this.mCooLat);
   }
}
