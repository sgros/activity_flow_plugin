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

    public TrackStats(DataReaderBigEndian dr) throws IOException {
        super(dr);
    }

    public TrackStats(byte[] data) throws IOException {
        super(data);
    }

    public int getNumOfPoints() {
        return this.mNumOfPoints;
    }

    public void setNumOfPoints(int numOfPoints) {
        this.mNumOfPoints = numOfPoints;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public long getStopTime() {
        return this.mStopTime;
    }

    public void setStopTime(long stopTime) {
        this.mStopTime = stopTime;
    }

    public float getTotalLength() {
        return this.mTotalLength;
    }

    public void setTotalLength(float totalLength) {
        this.mTotalLength = totalLength;
    }

    public void addTotalLength(float add) {
        this.mTotalLength += add;
    }

    public float getTotalLengthMove() {
        return this.mTotalLengthMove;
    }

    public void setTotalLengthMove(float totalLengthMove) {
        this.mTotalLengthMove = totalLengthMove;
    }

    public void addTotalLengthMove(float add) {
        this.mTotalLengthMove += add;
    }

    public long getTotalTime() {
        return this.mTotalTime;
    }

    public void setTotalTime(long totalTime) {
        this.mTotalTime = Math.abs(totalTime);
    }

    public void addTotalTime(long add) {
        this.mTotalTime += Math.abs(add);
    }

    public long getTotalTimeMove() {
        return this.mTotalTimeMove;
    }

    public void setTotalTimeMove(long totalTimeMove) {
        this.mTotalTimeMove = Math.abs(totalTimeMove);
    }

    public void addTotalTimeMove(long add) {
        this.mTotalTimeMove += Math.abs(add);
    }

    public float getSpeedMax() {
        return this.mSpeedMax;
    }

    public void setSpeedMax(float speedMax) {
        this.mSpeedMax = speedMax;
    }

    public float getAltitudeMax() {
        return this.mAltitudeMax;
    }

    public void setAltitudeMax(float altitudeMax) {
        this.mAltitudeMax = altitudeMax;
    }

    public float getAltitudeMin() {
        return this.mAltitudeMin;
    }

    public void setAltitudeMin(float altitudeMin) {
        this.mAltitudeMin = altitudeMin;
    }

    public float getEleNeutralDistance() {
        return this.mEleNeutralDistance;
    }

    public void setEleNeutralDistance(float eleNeutralDistance) {
        this.mEleNeutralDistance = eleNeutralDistance;
    }

    public void addEleNeutralDistance(float add) {
        this.mEleNeutralDistance += add;
    }

    public float getEleNeutralHeight() {
        return this.mEleNeutralHeight;
    }

    public void setEleNeutralHeight(float eleNeutralHeight) {
        this.mEleNeutralHeight = eleNeutralHeight;
    }

    public void addEleNeutralHeight(float add) {
        this.mEleNeutralHeight += add;
    }

    public float getElePositiveDistance() {
        return this.mElePositiveDistance;
    }

    public void setElePositiveDistance(float elePositiveDistance) {
        this.mElePositiveDistance = elePositiveDistance;
    }

    public void addElePositiveDistance(float add) {
        this.mElePositiveDistance += add;
    }

    public float getElePositiveHeight() {
        return this.mElePositiveHeight;
    }

    public void setElePositiveHeight(float elePositiveHeight) {
        this.mElePositiveHeight = elePositiveHeight;
    }

    public void addElePositiveHeight(float add) {
        this.mElePositiveHeight += add;
    }

    public float getEleNegativeDistance() {
        return this.mEleNegativeDistance;
    }

    public void setEleNegativeDistance(float eleNegativeDistance) {
        this.mEleNegativeDistance = eleNegativeDistance;
    }

    public void addEleNegativeDistance(float add) {
        this.mEleNegativeDistance += add;
    }

    public float getEleNegativeHeight() {
        return this.mEleNegativeHeight;
    }

    public void setEleNegativeHeight(float eleNegativeHeight) {
        this.mEleNegativeHeight = eleNegativeHeight;
    }

    public void addEleNegativeHeight(float add) {
        this.mEleNegativeHeight += add;
    }

    public float getEleTotalAbsDistance() {
        return this.mEleTotalAbsDistance;
    }

    public void setEleTotalAbsDistance(float eleTotalAbsDistance) {
        this.mEleTotalAbsDistance = eleTotalAbsDistance;
    }

    public void addEleTotalAbsDistance(float add) {
        this.mEleTotalAbsDistance += add;
    }

    public float getEleTotalAbsHeight() {
        return this.mEleTotalAbsHeight;
    }

    public void setEleTotalAbsHeight(float eleTotalAbsHeight) {
        this.mEleTotalAbsHeight = eleTotalAbsHeight;
    }

    public void addEleTotalAbsHeight(float add) {
        this.mEleTotalAbsHeight += add;
    }

    public int getHrmAverage() {
        if (this.mHeartRateBeats <= 0.0d || this.mHeartRateTime <= 0) {
            return 0;
        }
        return (int) (this.mHeartRateBeats / (((double) this.mHeartRateTime) / 60000.0d));
    }

    public int getHrmMax() {
        return this.mHeartRateMax;
    }

    public void addHeartRateMeasure(int hrmMeasured, int hrmAvgSegment, long measureTime) {
        this.mHeartRateBeats += ((double) hrmAvgSegment) * ((((double) measureTime) * 1.0d) / 60000.0d);
        this.mHeartRateTime += measureTime;
        this.mHeartRateMax = Math.max(this.mHeartRateMax, hrmMeasured);
    }

    public int getCadenceAverage() {
        if (this.mCadenceNumber <= 0.0d || this.mCadenceTime <= 0) {
            return 0;
        }
        return (int) (this.mCadenceNumber / (((double) this.mCadenceTime) / 60000.0d));
    }

    public int getCadenceMax() {
        return this.mCadenceMax;
    }

    public void addCadenceMeasure(int revMeasured, int revAvgSegment, long measureTime) {
        this.mCadenceNumber += ((double) revAvgSegment) * ((((double) measureTime) * 1.0d) / 60000.0d);
        this.mCadenceTime += measureTime;
        this.mCadenceMax = Math.max(this.mCadenceMax, revMeasured);
    }

    public int getEnergy() {
        return this.mEnergy;
    }

    public void addEnergy(int energy) {
        this.mEnergy += energy;
    }

    public int getNumOfStrides() {
        return this.mNumOfStrides;
    }

    public void setNumOfStrides(int numOfStrides) {
        this.mNumOfStrides = numOfStrides;
    }

    public double getTrackLength(boolean onlyWithMove) {
        if (onlyWithMove) {
            return (double) this.mTotalLengthMove;
        }
        return (double) this.mTotalLength;
    }

    public long getTrackTime(boolean onlyWithMove) {
        if (onlyWithMove) {
            return this.mTotalTimeMove;
        }
        return this.mTotalTime;
    }

    public float getSpeedAverage(boolean onlyWithMove) {
        float trackTime = ((float) getTrackTime(onlyWithMove)) / 1000.0f;
        if (trackTime > 0.0f) {
            return (float) (getTrackLength(onlyWithMove) / ((double) trackTime));
        }
        return 0.0f;
    }

    public boolean hasElevationValues() {
        return (this.mAltitudeMin == Float.POSITIVE_INFINITY || ((double) this.mAltitudeMin) == 0.0d || this.mAltitudeMax == Float.NEGATIVE_INFINITY || ((double) this.mAltitudeMax) == 0.0d) ? false : true;
    }

    public void resetStatistics() {
        this.mTotalLength = 0.0f;
        this.mTotalLengthMove = 0.0f;
        this.mTotalTime = 0;
        this.mTotalTimeMove = 0;
        this.mSpeedMax = 0.0f;
        this.mHeartRateBeats = 0.0d;
        this.mHeartRateTime = 0;
        this.mHeartRateMax = 0;
        this.mCadenceNumber = 0.0d;
        this.mCadenceTime = 0;
        this.mCadenceMax = 0;
        this.mEnergy = 0;
        this.mNumOfStrides = 0;
        resetStatisticsAltitude();
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

    public void appendStatistics(TrackStats stats) {
        this.mNumOfPoints += stats.mNumOfPoints;
        this.mStartTime = Math.min(this.mStartTime, stats.mStartTime);
        this.mStopTime = Math.max(this.mStopTime, stats.mStopTime);
        this.mTotalLength += stats.mTotalLength;
        this.mTotalLengthMove += stats.mTotalLengthMove;
        this.mTotalTime += stats.mTotalTime;
        this.mTotalTimeMove += stats.mTotalTimeMove;
        this.mSpeedMax = Math.max(this.mSpeedMax, stats.mSpeedMax);
        this.mAltitudeMax = Math.max(this.mAltitudeMax, stats.mAltitudeMax);
        this.mAltitudeMin = Math.min(this.mAltitudeMin, stats.mAltitudeMin);
        this.mEleNeutralDistance += stats.mEleNeutralDistance;
        this.mEleNeutralHeight += stats.mEleNeutralHeight;
        this.mElePositiveDistance += stats.mElePositiveDistance;
        this.mElePositiveHeight += stats.mElePositiveHeight;
        this.mEleNegativeDistance += stats.mEleNegativeDistance;
        this.mEleNegativeHeight += stats.mEleNegativeHeight;
        this.mEleTotalAbsDistance += stats.mEleTotalAbsDistance;
        this.mEleTotalAbsHeight += stats.mEleTotalAbsHeight;
        this.mHeartRateBeats += stats.mHeartRateBeats;
        this.mHeartRateTime += stats.mHeartRateTime;
        this.mHeartRateMax = Math.max(this.mHeartRateMax, stats.mHeartRateMax);
        this.mCadenceNumber += stats.mCadenceNumber;
        this.mCadenceTime += stats.mCadenceTime;
        this.mCadenceMax = Math.max(this.mCadenceMax, stats.mCadenceMax);
        this.mEnergy += stats.mEnergy;
        this.mNumOfStrides += stats.mNumOfStrides;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 3;
    }

    public void reset() {
        this.mNumOfPoints = 0;
        this.mStartTime = -1;
        this.mStopTime = -1;
        resetStatistics();
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mNumOfPoints = dr.readInt();
        this.mStartTime = dr.readLong();
        this.mStopTime = dr.readLong();
        this.mTotalLength = dr.readFloat();
        this.mTotalLengthMove = dr.readFloat();
        this.mTotalTime = dr.readLong();
        this.mTotalTimeMove = dr.readLong();
        this.mSpeedMax = dr.readFloat();
        this.mAltitudeMax = dr.readFloat();
        this.mAltitudeMin = dr.readFloat();
        this.mEleNeutralDistance = dr.readFloat();
        this.mEleNeutralHeight = dr.readFloat();
        this.mElePositiveDistance = dr.readFloat();
        this.mElePositiveHeight = dr.readFloat();
        this.mEleNegativeDistance = dr.readFloat();
        this.mEleNegativeHeight = dr.readFloat();
        this.mEleTotalAbsDistance = dr.readFloat();
        this.mEleTotalAbsHeight = dr.readFloat();
        if (version >= 1) {
            this.mHeartRateBeats = (double) dr.readInt();
            this.mHeartRateTime = dr.readLong();
            this.mHeartRateMax = dr.readInt();
            this.mEnergy = dr.readInt();
        }
        if (version >= 2) {
            this.mHeartRateBeats = dr.readDouble();
            this.mCadenceNumber = dr.readDouble();
            this.mCadenceTime = dr.readLong();
            this.mCadenceMax = dr.readInt();
        }
        if (version >= 3) {
            this.mNumOfStrides = dr.readInt();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(this.mNumOfPoints);
        dw.writeLong(this.mStartTime);
        dw.writeLong(this.mStopTime);
        dw.writeFloat(this.mTotalLength);
        dw.writeFloat(this.mTotalLengthMove);
        dw.writeLong(this.mTotalTime);
        dw.writeLong(this.mTotalTimeMove);
        dw.writeFloat(this.mSpeedMax);
        dw.writeFloat(this.mAltitudeMax);
        dw.writeFloat(this.mAltitudeMin);
        dw.writeFloat(this.mEleNeutralDistance);
        dw.writeFloat(this.mEleNeutralHeight);
        dw.writeFloat(this.mElePositiveDistance);
        dw.writeFloat(this.mElePositiveHeight);
        dw.writeFloat(this.mEleNegativeDistance);
        dw.writeFloat(this.mEleNegativeHeight);
        dw.writeFloat(this.mEleTotalAbsDistance);
        dw.writeFloat(this.mEleTotalAbsHeight);
        dw.writeInt((int) this.mHeartRateBeats);
        dw.writeLong(this.mHeartRateTime);
        dw.writeInt(this.mHeartRateMax);
        dw.writeInt(this.mEnergy);
        dw.writeDouble(this.mHeartRateBeats);
        dw.writeDouble(this.mCadenceNumber);
        dw.writeLong(this.mCadenceTime);
        dw.writeInt(this.mCadenceMax);
        dw.writeInt(this.mNumOfStrides);
    }
}
