package locus.api.objects.geocaching;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingTrackable extends Storable {
   private static final String TAG = GeocachingTrackable.class.getSimpleName();
   private String mCurrentOwner;
   private String mDetails;
   private String mGoal;
   private long mId;
   private String mImgUrl;
   private String mName;
   private String mOrigin;
   private String mOriginalOwner;
   private long mReleased;
   private String mSrcDetails;

   public GeocachingTrackable() {
   }

   public GeocachingTrackable(byte[] var1) throws IOException {
      super(var1);
   }

   public String getCurrentOwner() {
      return this.mCurrentOwner;
   }

   public String getDetails() {
      return this.mDetails;
   }

   public String getGoal() {
      return this.mGoal;
   }

   public long getId() {
      return this.mId;
   }

   public String getImgUrl() {
      return this.mImgUrl;
   }

   public String getName() {
      return this.mName;
   }

   public String getOrigin() {
      return this.mOrigin;
   }

   public String getOriginalOwner() {
      return this.mOriginalOwner;
   }

   public long getReleased() {
      return this.mReleased;
   }

   public String getSrcDetails() {
      return this.mSrcDetails;
   }

   public String getTbCode() {
      String var1;
      if (this.mSrcDetails != null && this.mSrcDetails.length() != 0) {
         if (this.mSrcDetails.startsWith("http://www.geocaching.com/track/details.aspx?tracker=")) {
            var1 = this.mSrcDetails.substring("http://www.geocaching.com/track/details.aspx?tracker=".length());
         } else if (this.mSrcDetails.startsWith("http://coord.info/")) {
            var1 = this.mSrcDetails.substring("http://coord.info/".length());
         } else {
            var1 = "";
         }
      } else {
         var1 = "";
      }

      return var1;
   }

   protected int getVersion() {
      return 1;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mName = var2.readString();
      this.mImgUrl = var2.readString();
      this.mSrcDetails = var2.readString();
      this.mOriginalOwner = var2.readString();
      this.mReleased = var2.readLong();
      this.mOrigin = var2.readString();
      this.mGoal = var2.readString();
      this.mDetails = var2.readString();
      if (var1 >= 1) {
         this.mId = var2.readLong();
         this.mCurrentOwner = var2.readString();
      }

   }

   public void reset() {
      this.mName = "";
      this.mImgUrl = "";
      this.mSrcDetails = "";
      this.mOriginalOwner = "";
      this.mReleased = 0L;
      this.mOrigin = "";
      this.mGoal = "";
      this.mDetails = "";
      this.mId = 0L;
      this.mCurrentOwner = "";
   }

   public void setCurrentOwner(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setCurrentOwner(), empty parameter");
         var2 = "";
      }

      this.mCurrentOwner = var2;
   }

   public void setDetails(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setDetails(), empty parameter");
         var2 = "";
      }

      this.mDetails = var2;
   }

   public void setGoal(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setGoal(), empty parameter");
         var2 = "";
      }

      this.mGoal = var2;
   }

   public void setId(long var1) {
      this.mId = var1;
   }

   public void setImgUrl(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setImgUrl(), empty parameter");
         var2 = "";
      }

      this.mImgUrl = var2;
   }

   public void setName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setName(), empty parameter");
         var2 = "";
      }

      this.mName = var2;
   }

   public void setOrigin(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setOrigin(), empty parameter");
         var2 = "";
      }

      this.mOrigin = var2;
   }

   public void setOriginalOwner(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setOriginalOwner(), empty parameter");
         var2 = "";
      }

      this.mOriginalOwner = var2;
   }

   public void setReleased(long var1) {
      this.mReleased = var1;
   }

   public void setSrcDetails(String var1) {
      String var2 = var1;
      if (var1 == null) {
         Logger.logD(TAG, "setSrcDetails(), empty parameter");
         var2 = "";
      }

      this.mSrcDetails = var2;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mName);
      var1.writeString(this.mImgUrl);
      var1.writeString(this.mSrcDetails);
      var1.writeString(this.mOriginalOwner);
      var1.writeLong(this.mReleased);
      var1.writeString(this.mOrigin);
      var1.writeString(this.mGoal);
      var1.writeString(this.mDetails);
      var1.writeLong(this.mId);
      var1.writeString(this.mCurrentOwner);
   }
}
