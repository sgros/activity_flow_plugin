// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.Storable;

public class TrackStats extends Storable
{
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
    
    public TrackStats(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
    }
    
    public TrackStats(final byte[] array) throws IOException {
        super(array);
    }
    
    public void addCadenceMeasure(final int b, final int n, final long n2) {
        this.mCadenceNumber += n * (n2 * 1.0 / 60000.0);
        this.mCadenceTime += n2;
        this.mCadenceMax = Math.max(this.mCadenceMax, b);
    }
    
    public void addEleNegativeDistance(final float n) {
        this.mEleNegativeDistance += n;
    }
    
    public void addEleNegativeHeight(final float n) {
        this.mEleNegativeHeight += n;
    }
    
    public void addEleNeutralDistance(final float n) {
        this.mEleNeutralDistance += n;
    }
    
    public void addEleNeutralHeight(final float n) {
        this.mEleNeutralHeight += n;
    }
    
    public void addElePositiveDistance(final float n) {
        this.mElePositiveDistance += n;
    }
    
    public void addElePositiveHeight(final float n) {
        this.mElePositiveHeight += n;
    }
    
    public void addEleTotalAbsDistance(final float n) {
        this.mEleTotalAbsDistance += n;
    }
    
    public void addEleTotalAbsHeight(final float n) {
        this.mEleTotalAbsHeight += n;
    }
    
    public void addEnergy(final int n) {
        this.mEnergy += n;
    }
    
    public void addHeartRateMeasure(final int b, final int n, final long n2) {
        this.mHeartRateBeats += n * (n2 * 1.0 / 60000.0);
        this.mHeartRateTime += n2;
        this.mHeartRateMax = Math.max(this.mHeartRateMax, b);
    }
    
    public void addTotalLength(final float n) {
        this.mTotalLength += n;
    }
    
    public void addTotalLengthMove(final float n) {
        this.mTotalLengthMove += n;
    }
    
    public void addTotalTime(final long a) {
        this.mTotalTime += Math.abs(a);
    }
    
    public void addTotalTimeMove(final long a) {
        this.mTotalTimeMove += Math.abs(a);
    }
    
    public void appendStatistics(final TrackStats trackStats) {
        this.mNumOfPoints += trackStats.mNumOfPoints;
        this.mStartTime = Math.min(this.mStartTime, trackStats.mStartTime);
        this.mStopTime = Math.max(this.mStopTime, trackStats.mStopTime);
        this.mTotalLength += trackStats.mTotalLength;
        this.mTotalLengthMove += trackStats.mTotalLengthMove;
        this.mTotalTime += trackStats.mTotalTime;
        this.mTotalTimeMove += trackStats.mTotalTimeMove;
        this.mSpeedMax = Math.max(this.mSpeedMax, trackStats.mSpeedMax);
        this.mAltitudeMax = Math.max(this.mAltitudeMax, trackStats.mAltitudeMax);
        this.mAltitudeMin = Math.min(this.mAltitudeMin, trackStats.mAltitudeMin);
        this.mEleNeutralDistance += trackStats.mEleNeutralDistance;
        this.mEleNeutralHeight += trackStats.mEleNeutralHeight;
        this.mElePositiveDistance += trackStats.mElePositiveDistance;
        this.mElePositiveHeight += trackStats.mElePositiveHeight;
        this.mEleNegativeDistance += trackStats.mEleNegativeDistance;
        this.mEleNegativeHeight += trackStats.mEleNegativeHeight;
        this.mEleTotalAbsDistance += trackStats.mEleTotalAbsDistance;
        this.mEleTotalAbsHeight += trackStats.mEleTotalAbsHeight;
        this.mHeartRateBeats += trackStats.mHeartRateBeats;
        this.mHeartRateTime += trackStats.mHeartRateTime;
        this.mHeartRateMax = Math.max(this.mHeartRateMax, trackStats.mHeartRateMax);
        this.mCadenceNumber += trackStats.mCadenceNumber;
        this.mCadenceTime += trackStats.mCadenceTime;
        this.mCadenceMax = Math.max(this.mCadenceMax, trackStats.mCadenceMax);
        this.mEnergy += trackStats.mEnergy;
        this.mNumOfStrides += trackStats.mNumOfStrides;
    }
    
    public float getAltitudeMax() {
        return this.mAltitudeMax;
    }
    
    public float getAltitudeMin() {
        return this.mAltitudeMin;
    }
    
    public int getCadenceAverage() {
        int n;
        if (this.mCadenceNumber > 0.0 && this.mCadenceTime > 0L) {
            n = (int)(this.mCadenceNumber / (this.mCadenceTime / 60000.0));
        }
        else {
            n = 0;
        }
        return n;
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
        int n;
        if (this.mHeartRateBeats > 0.0 && this.mHeartRateTime > 0L) {
            n = (int)(this.mHeartRateBeats / (this.mHeartRateTime / 60000.0));
        }
        else {
            n = 0;
        }
        return n;
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
    
    public float getSpeedAverage(final boolean b) {
        float n = 0.0f;
        final float n2 = this.getTrackTime(b) / 1000.0f;
        if (n2 > 0.0f) {
            n = (float)(this.getTrackLength(b) / n2);
        }
        return n;
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
    
    public double getTrackLength(final boolean b) {
        double n;
        if (b) {
            n = this.mTotalLengthMove;
        }
        else {
            n = this.mTotalLength;
        }
        return n;
    }
    
    public long getTrackTime(final boolean b) {
        long n;
        if (b) {
            n = this.mTotalTimeMove;
        }
        else {
            n = this.mTotalTime;
        }
        return n;
    }
    
    @Override
    protected int getVersion() {
        return 3;
    }
    
    public boolean hasElevationValues() {
        return this.mAltitudeMin != Float.POSITIVE_INFINITY && this.mAltitudeMin != 0.0 && this.mAltitudeMax != Float.NEGATIVE_INFINITY && this.mAltitudeMax != 0.0;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mNumOfPoints = dataReaderBigEndian.readInt();
        this.mStartTime = dataReaderBigEndian.readLong();
        this.mStopTime = dataReaderBigEndian.readLong();
        this.mTotalLength = dataReaderBigEndian.readFloat();
        this.mTotalLengthMove = dataReaderBigEndian.readFloat();
        this.mTotalTime = dataReaderBigEndian.readLong();
        this.mTotalTimeMove = dataReaderBigEndian.readLong();
        this.mSpeedMax = dataReaderBigEndian.readFloat();
        this.mAltitudeMax = dataReaderBigEndian.readFloat();
        this.mAltitudeMin = dataReaderBigEndian.readFloat();
        this.mEleNeutralDistance = dataReaderBigEndian.readFloat();
        this.mEleNeutralHeight = dataReaderBigEndian.readFloat();
        this.mElePositiveDistance = dataReaderBigEndian.readFloat();
        this.mElePositiveHeight = dataReaderBigEndian.readFloat();
        this.mEleNegativeDistance = dataReaderBigEndian.readFloat();
        this.mEleNegativeHeight = dataReaderBigEndian.readFloat();
        this.mEleTotalAbsDistance = dataReaderBigEndian.readFloat();
        this.mEleTotalAbsHeight = dataReaderBigEndian.readFloat();
        if (n >= 1) {
            this.mHeartRateBeats = dataReaderBigEndian.readInt();
            this.mHeartRateTime = dataReaderBigEndian.readLong();
            this.mHeartRateMax = dataReaderBigEndian.readInt();
            this.mEnergy = dataReaderBigEndian.readInt();
        }
        if (n >= 2) {
            this.mHeartRateBeats = dataReaderBigEndian.readDouble();
            this.mCadenceNumber = dataReaderBigEndian.readDouble();
            this.mCadenceTime = dataReaderBigEndian.readLong();
            this.mCadenceMax = dataReaderBigEndian.readInt();
        }
        if (n >= 3) {
            this.mNumOfStrides = dataReaderBigEndian.readInt();
        }
    }
    
    @Override
    public void reset() {
        this.mNumOfPoints = 0;
        this.mStartTime = -1L;
        this.mStopTime = -1L;
        this.resetStatistics();
    }
    
    public void resetStatistics() {
        this.mTotalLength = 0.0f;
        this.mTotalLengthMove = 0.0f;
        this.mTotalTime = 0L;
        this.mTotalTimeMove = 0L;
        this.mSpeedMax = 0.0f;
        this.mHeartRateBeats = 0.0;
        this.mHeartRateTime = 0L;
        this.mHeartRateMax = 0;
        this.mCadenceNumber = 0.0;
        this.mCadenceTime = 0L;
        this.mCadenceMax = 0;
        this.mEnergy = 0;
        this.mNumOfStrides = 0;
        this.resetStatisticsAltitude();
    }
    
    public void resetStatisticsAltitude() {
        this.mAltitudeMax = Float.NEGATIVE_INFINITY;
        this.mAltitudeMin = Float.POSITIVE_INFINITY;
        this.mEleNeutralDistance = 0.0f;
        this.mEleNeutralHeight = 0.0f;
        this.mElePositiveDistance = 0.0f;
        this.mElePositiveHeight = 0.0f;
        this.mEleNegativeDistance = 0.0f;
        this.mEleNegativeHeight = 0.0f;
        this.mEleTotalAbsDistance = 0.0f;
        this.mEleTotalAbsHeight = 0.0f;
    }
    
    public void setAltitudeMax(final float mAltitudeMax) {
        this.mAltitudeMax = mAltitudeMax;
    }
    
    public void setAltitudeMin(final float mAltitudeMin) {
        this.mAltitudeMin = mAltitudeMin;
    }
    
    public void setEleNegativeDistance(final float mEleNegativeDistance) {
        this.mEleNegativeDistance = mEleNegativeDistance;
    }
    
    public void setEleNegativeHeight(final float mEleNegativeHeight) {
        this.mEleNegativeHeight = mEleNegativeHeight;
    }
    
    public void setEleNeutralDistance(final float mEleNeutralDistance) {
        this.mEleNeutralDistance = mEleNeutralDistance;
    }
    
    public void setEleNeutralHeight(final float mEleNeutralHeight) {
        this.mEleNeutralHeight = mEleNeutralHeight;
    }
    
    public void setElePositiveDistance(final float mElePositiveDistance) {
        this.mElePositiveDistance = mElePositiveDistance;
    }
    
    public void setElePositiveHeight(final float mElePositiveHeight) {
        this.mElePositiveHeight = mElePositiveHeight;
    }
    
    public void setEleTotalAbsDistance(final float mEleTotalAbsDistance) {
        this.mEleTotalAbsDistance = mEleTotalAbsDistance;
    }
    
    public void setEleTotalAbsHeight(final float mEleTotalAbsHeight) {
        this.mEleTotalAbsHeight = mEleTotalAbsHeight;
    }
    
    public void setNumOfPoints(final int mNumOfPoints) {
        this.mNumOfPoints = mNumOfPoints;
    }
    
    public void setNumOfStrides(final int mNumOfStrides) {
        this.mNumOfStrides = mNumOfStrides;
    }
    
    public void setSpeedMax(final float mSpeedMax) {
        this.mSpeedMax = mSpeedMax;
    }
    
    public void setStartTime(final long mStartTime) {
        this.mStartTime = mStartTime;
    }
    
    public void setStopTime(final long mStopTime) {
        this.mStopTime = mStopTime;
    }
    
    public void setTotalLength(final float mTotalLength) {
        this.mTotalLength = mTotalLength;
    }
    
    public void setTotalLengthMove(final float mTotalLengthMove) {
        this.mTotalLengthMove = mTotalLengthMove;
    }
    
    public void setTotalTime(final long a) {
        this.mTotalTime = Math.abs(a);
    }
    
    public void setTotalTimeMove(final long a) {
        this.mTotalTimeMove = Math.abs(a);
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.mNumOfPoints);
        dataWriterBigEndian.writeLong(this.mStartTime);
        dataWriterBigEndian.writeLong(this.mStopTime);
        dataWriterBigEndian.writeFloat(this.mTotalLength);
        dataWriterBigEndian.writeFloat(this.mTotalLengthMove);
        dataWriterBigEndian.writeLong(this.mTotalTime);
        dataWriterBigEndian.writeLong(this.mTotalTimeMove);
        dataWriterBigEndian.writeFloat(this.mSpeedMax);
        dataWriterBigEndian.writeFloat(this.mAltitudeMax);
        dataWriterBigEndian.writeFloat(this.mAltitudeMin);
        dataWriterBigEndian.writeFloat(this.mEleNeutralDistance);
        dataWriterBigEndian.writeFloat(this.mEleNeutralHeight);
        dataWriterBigEndian.writeFloat(this.mElePositiveDistance);
        dataWriterBigEndian.writeFloat(this.mElePositiveHeight);
        dataWriterBigEndian.writeFloat(this.mEleNegativeDistance);
        dataWriterBigEndian.writeFloat(this.mEleNegativeHeight);
        dataWriterBigEndian.writeFloat(this.mEleTotalAbsDistance);
        dataWriterBigEndian.writeFloat(this.mEleTotalAbsHeight);
        dataWriterBigEndian.writeInt((int)this.mHeartRateBeats);
        dataWriterBigEndian.writeLong(this.mHeartRateTime);
        dataWriterBigEndian.writeInt(this.mHeartRateMax);
        dataWriterBigEndian.writeInt(this.mEnergy);
        dataWriterBigEndian.writeDouble(this.mHeartRateBeats);
        dataWriterBigEndian.writeDouble(this.mCadenceNumber);
        dataWriterBigEndian.writeLong(this.mCadenceTime);
        dataWriterBigEndian.writeInt(this.mCadenceMax);
        dataWriterBigEndian.writeInt(this.mNumOfStrides);
    }
}
