package locus.api.objects.extra;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class TrackStats extends Storable {
   private float mAltitudeMax;
   private float mAltitudeMin;
   private int mCadenceMax;
   private double mCadenceNumber;
   private long mCadenceTime;
   private float mEleNegativeDistance;
   private float mEleNegativeHeight;
   private float mEleNeutralDistance;
   private float mEleNeutralHeight;
   private float mElePositiveDistance;
   private float mElePositiveHeight;
   private float mEleTotalAbsDistance;
   private float mEleTotalAbsHeight;
   private int mEnergy;
   private double mHeartRateBeats;
   private int mHeartRateMax;
   private long mHeartRateTime;
   private int mNumOfPoints;
   private int mNumOfStrides;
   private float mSpeedMax;
   private long mStartTime;
   private long mStopTime;
   private float mTotalLength;
   private float mTotalLengthMove;
   private long mTotalTime;
   private long mTotalTimeMove;

   public TrackStats() {
   }

   public TrackStats(DataReaderBigEndian var1) throws IOException {
      super(var1);
   }

   public TrackStats(byte[] var1) throws IOException {
      super(var1);
   }

   public void addCadenceMeasure(int var1, int var2, long var3) {
      double var5 = (double)var3 * 1.0D / 60000.0D;
      double var7 = (double)var2;
      this.mCadenceNumber += var7 * var5;
      this.mCadenceTime += var3;
      this.mCadenceMax = Math.max(this.mCadenceMax, var1);
   }

   public void addEleNegativeDistance(float var1) {
      this.mEleNegativeDistance += var1;
   }

   public void addEleNegativeHeight(float var1) {
      this.mEleNegativeHeight += var1;
   }

   public void addEleNeutralDistance(float var1) {
      this.mEleNeutralDistance += var1;
   }

   public void addEleNeutralHeight(float var1) {
      this.mEleNeutralHeight += var1;
   }

   public void addElePositiveDistance(float var1) {
      this.mElePositiveDistance += var1;
   }

   public void addElePositiveHeight(float var1) {
      this.mElePositiveHeight += var1;
   }

   public void addEleTotalAbsDistance(float var1) {
      this.mEleTotalAbsDistance += var1;
   }

   public void addEleTotalAbsHeight(float var1) {
      this.mEleTotalAbsHeight += var1;
   }

   public void addEnergy(int var1) {
      this.mEnergy += var1;
   }

   public void addHeartRateMeasure(int var1, int var2, long var3) {
      double var5 = (double)var3 * 1.0D / 60000.0D;
      double var7 = (double)var2;
      this.mHeartRateBeats += var7 * var5;
      this.mHeartRateTime += var3;
      this.mHeartRateMax = Math.max(this.mHeartRateMax, var1);
   }

   public void addTotalLength(float var1) {
      this.mTotalLength += var1;
   }

   public void addTotalLengthMove(float var1) {
      this.mTotalLengthMove += var1;
   }

   public void addTotalTime(long var1) {
      this.mTotalTime += Math.abs(var1);
   }

   public void addTotalTimeMove(long var1) {
      this.mTotalTimeMove += Math.abs(var1);
   }

   public void appendStatistics(TrackStats var1) {
      this.mNumOfPoints += var1.mNumOfPoints;
      this.mStartTime = Math.min(this.mStartTime, var1.mStartTime);
      this.mStopTime = Math.max(this.mStopTime, var1.mStopTime);
      this.mTotalLength += var1.mTotalLength;
      this.mTotalLengthMove += var1.mTotalLengthMove;
      this.mTotalTime += var1.mTotalTime;
      this.mTotalTimeMove += var1.mTotalTimeMove;
      this.mSpeedMax = Math.max(this.mSpeedMax, var1.mSpeedMax);
      this.mAltitudeMax = Math.max(this.mAltitudeMax, var1.mAltitudeMax);
      this.mAltitudeMin = Math.min(this.mAltitudeMin, var1.mAltitudeMin);
      this.mEleNeutralDistance += var1.mEleNeutralDistance;
      this.mEleNeutralHeight += var1.mEleNeutralHeight;
      this.mElePositiveDistance += var1.mElePositiveDistance;
      this.mElePositiveHeight += var1.mElePositiveHeight;
      this.mEleNegativeDistance += var1.mEleNegativeDistance;
      this.mEleNegativeHeight += var1.mEleNegativeHeight;
      this.mEleTotalAbsDistance += var1.mEleTotalAbsDistance;
      this.mEleTotalAbsHeight += var1.mEleTotalAbsHeight;
      this.mHeartRateBeats += var1.mHeartRateBeats;
      this.mHeartRateTime += var1.mHeartRateTime;
      this.mHeartRateMax = Math.max(this.mHeartRateMax, var1.mHeartRateMax);
      this.mCadenceNumber += var1.mCadenceNumber;
      this.mCadenceTime += var1.mCadenceTime;
      this.mCadenceMax = Math.max(this.mCadenceMax, var1.mCadenceMax);
      this.mEnergy += var1.mEnergy;
      this.mNumOfStrides += var1.mNumOfStrides;
   }

   public float getAltitudeMax() {
      return this.mAltitudeMax;
   }

   public float getAltitudeMin() {
      return this.mAltitudeMin;
   }

   public int getCadenceAverage() {
      int var3;
      if (this.mCadenceNumber > 0.0D && this.mCadenceTime > 0L) {
         double var1 = (double)this.mCadenceTime / 60000.0D;
         var3 = (int)(this.mCadenceNumber / var1);
      } else {
         var3 = 0;
      }

      return var3;
   }

   public int getCadenceMax() {
      return this.mCadenceMax;
   }

   public float getEleNegativeDistance() {
      return this.mEleNegativeDistance;
   }

   public float getEleNegativeHeight() {
      return this.mEleNegativeHeight;
   }

   public float getEleNeutralDistance() {
      return this.mEleNeutralDistance;
   }

   public float getEleNeutralHeight() {
      return this.mEleNeutralHeight;
   }

   public float getElePositiveDistance() {
      return this.mElePositiveDistance;
   }

   public float getElePositiveHeight() {
      return this.mElePositiveHeight;
   }

   public float getEleTotalAbsDistance() {
      return this.mEleTotalAbsDistance;
   }

   public float getEleTotalAbsHeight() {
      return this.mEleTotalAbsHeight;
   }

   public int getEnergy() {
      return this.mEnergy;
   }

   public int getHrmAverage() {
      int var3;
      if (this.mHeartRateBeats > 0.0D && this.mHeartRateTime > 0L) {
         double var1 = (double)this.mHeartRateTime / 60000.0D;
         var3 = (int)(this.mHeartRateBeats / var1);
      } else {
         var3 = 0;
      }

      return var3;
   }

   public int getHrmMax() {
      return this.mHeartRateMax;
   }

   public int getNumOfPoints() {
      return this.mNumOfPoints;
   }

   public int getNumOfStrides() {
      return this.mNumOfStrides;
   }

   public float getSpeedAverage(boolean var1) {
      float var2 = 0.0F;
      float var3 = (float)this.getTrackTime(var1) / 1000.0F;
      if (var3 > 0.0F) {
         var2 = (float)(this.getTrackLength(var1) / (double)var3);
      }

      return var2;
   }

   public float getSpeedMax() {
      return this.mSpeedMax;
   }

   public long getStartTime() {
      return this.mStartTime;
   }

   public long getStopTime() {
      return this.mStopTime;
   }

   public float getTotalLength() {
      return this.mTotalLength;
   }

   public float getTotalLengthMove() {
      return this.mTotalLengthMove;
   }

   public long getTotalTime() {
      return this.mTotalTime;
   }

   public long getTotalTimeMove() {
      return this.mTotalTimeMove;
   }

   public double getTrackLength(boolean var1) {
      double var2;
      if (var1) {
         var2 = (double)this.mTotalLengthMove;
      } else {
         var2 = (double)this.mTotalLength;
      }

      return var2;
   }

   public long getTrackTime(boolean var1) {
      long var2;
      if (var1) {
         var2 = this.mTotalTimeMove;
      } else {
         var2 = this.mTotalTime;
      }

      return var2;
   }

   protected int getVersion() {
      return 3;
   }

   public boolean hasElevationValues() {
      boolean var1;
      if (this.mAltitudeMin != Float.POSITIVE_INFINITY && (double)this.mAltitudeMin != 0.0D && this.mAltitudeMax != Float.NEGATIVE_INFINITY && (double)this.mAltitudeMax != 0.0D) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mNumOfPoints = var2.readInt();
      this.mStartTime = var2.readLong();
      this.mStopTime = var2.readLong();
      this.mTotalLength = var2.readFloat();
      this.mTotalLengthMove = var2.readFloat();
      this.mTotalTime = var2.readLong();
      this.mTotalTimeMove = var2.readLong();
      this.mSpeedMax = var2.readFloat();
      this.mAltitudeMax = var2.readFloat();
      this.mAltitudeMin = var2.readFloat();
      this.mEleNeutralDistance = var2.readFloat();
      this.mEleNeutralHeight = var2.readFloat();
      this.mElePositiveDistance = var2.readFloat();
      this.mElePositiveHeight = var2.readFloat();
      this.mEleNegativeDistance = var2.readFloat();
      this.mEleNegativeHeight = var2.readFloat();
      this.mEleTotalAbsDistance = var2.readFloat();
      this.mEleTotalAbsHeight = var2.readFloat();
      if (var1 >= 1) {
         this.mHeartRateBeats = (double)var2.readInt();
         this.mHeartRateTime = var2.readLong();
         this.mHeartRateMax = var2.readInt();
         this.mEnergy = var2.readInt();
      }

      if (var1 >= 2) {
         this.mHeartRateBeats = var2.readDouble();
         this.mCadenceNumber = var2.readDouble();
         this.mCadenceTime = var2.readLong();
         this.mCadenceMax = var2.readInt();
      }

      if (var1 >= 3) {
         this.mNumOfStrides = var2.readInt();
      }

   }

   public void reset() {
      this.mNumOfPoints = 0;
      this.mStartTime = -1L;
      this.mStopTime = -1L;
      this.resetStatistics();
   }

   public void resetStatistics() {
      this.mTotalLength = 0.0F;
      this.mTotalLengthMove = 0.0F;
      this.mTotalTime = 0L;
      this.mTotalTimeMove = 0L;
      this.mSpeedMax = 0.0F;
      this.mHeartRateBeats = 0.0D;
      this.mHeartRateTime = 0L;
      this.mHeartRateMax = 0;
      this.mCadenceNumber = 0.0D;
      this.mCadenceTime = 0L;
      this.mCadenceMax = 0;
      this.mEnergy = 0;
      this.mNumOfStrides = 0;
      this.resetStatisticsAltitude();
   }

   public void resetStatisticsAltitude() {
      this.mAltitudeMax = Float.NEGATIVE_INFINITY;
      this.mAltitudeMin = Float.POSITIVE_INFINITY;
      this.mEleNeutralDistance = 0.0F;
      this.mEleNeutralHeight = 0.0F;
      this.mElePositiveDistance = 0.0F;
      this.mElePositiveHeight = 0.0F;
      this.mEleNegativeDistance = 0.0F;
      this.mEleNegativeHeight = 0.0F;
      this.mEleTotalAbsDistance = 0.0F;
      this.mEleTotalAbsHeight = 0.0F;
   }

   public void setAltitudeMax(float var1) {
      this.mAltitudeMax = var1;
   }

   public void setAltitudeMin(float var1) {
      this.mAltitudeMin = var1;
   }

   public void setEleNegativeDistance(float var1) {
      this.mEleNegativeDistance = var1;
   }

   public void setEleNegativeHeight(float var1) {
      this.mEleNegativeHeight = var1;
   }

   public void setEleNeutralDistance(float var1) {
      this.mEleNeutralDistance = var1;
   }

   public void setEleNeutralHeight(float var1) {
      this.mEleNeutralHeight = var1;
   }

   public void setElePositiveDistance(float var1) {
      this.mElePositiveDistance = var1;
   }

   public void setElePositiveHeight(float var1) {
      this.mElePositiveHeight = var1;
   }

   public void setEleTotalAbsDistance(float var1) {
      this.mEleTotalAbsDistance = var1;
   }

   public void setEleTotalAbsHeight(float var1) {
      this.mEleTotalAbsHeight = var1;
   }

   public void setNumOfPoints(int var1) {
      this.mNumOfPoints = var1;
   }

   public void setNumOfStrides(int var1) {
      this.mNumOfStrides = var1;
   }

   public void setSpeedMax(float var1) {
      this.mSpeedMax = var1;
   }

   public void setStartTime(long var1) {
      this.mStartTime = var1;
   }

   public void setStopTime(long var1) {
      this.mStopTime = var1;
   }

   public void setTotalLength(float var1) {
      this.mTotalLength = var1;
   }

   public void setTotalLengthMove(float var1) {
      this.mTotalLengthMove = var1;
   }

   public void setTotalTime(long var1) {
      this.mTotalTime = Math.abs(var1);
   }

   public void setTotalTimeMove(long var1) {
      this.mTotalTimeMove = Math.abs(var1);
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.mNumOfPoints);
      var1.writeLong(this.mStartTime);
      var1.writeLong(this.mStopTime);
      var1.writeFloat(this.mTotalLength);
      var1.writeFloat(this.mTotalLengthMove);
      var1.writeLong(this.mTotalTime);
      var1.writeLong(this.mTotalTimeMove);
      var1.writeFloat(this.mSpeedMax);
      var1.writeFloat(this.mAltitudeMax);
      var1.writeFloat(this.mAltitudeMin);
      var1.writeFloat(this.mEleNeutralDistance);
      var1.writeFloat(this.mEleNeutralHeight);
      var1.writeFloat(this.mElePositiveDistance);
      var1.writeFloat(this.mElePositiveHeight);
      var1.writeFloat(this.mEleNegativeDistance);
      var1.writeFloat(this.mEleNegativeHeight);
      var1.writeFloat(this.mEleTotalAbsDistance);
      var1.writeFloat(this.mEleTotalAbsHeight);
      var1.writeInt((int)this.mHeartRateBeats);
      var1.writeLong(this.mHeartRateTime);
      var1.writeInt(this.mHeartRateMax);
      var1.writeInt(this.mEnergy);
      var1.writeDouble(this.mHeartRateBeats);
      var1.writeDouble(this.mCadenceNumber);
      var1.writeLong(this.mCadenceTime);
      var1.writeInt(this.mCadenceMax);
      var1.writeInt(this.mNumOfStrides);
   }
}
