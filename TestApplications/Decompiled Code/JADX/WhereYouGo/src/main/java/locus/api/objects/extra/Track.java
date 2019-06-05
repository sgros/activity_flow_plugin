package locus.api.objects.extra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import locus.api.objects.GeoData;
import locus.api.objects.GeoData.ReadWriteMode;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class Track extends GeoData {
    private static final String TAG = "Track";
    List<Integer> breaks;
    private int mActivityType;
    private TrackStats mStats;
    private boolean mUseFolderStyle;
    List<Location> points;
    List<Waypoint> waypoints;

    public Track(DataReaderBigEndian dr) throws IOException {
        super(dr);
    }

    public Track(byte[] data) throws IOException {
        super(data);
    }

    public Location getPoint(int index) {
        return (Location) this.points.get(index);
    }

    public int getPointsCount() {
        return this.points.size();
    }

    public List<Location> getPoints() {
        return this.points;
    }

    public boolean setPoints(List<Location> points) {
        if (points == null) {
            Logger.logW(TAG, "setPoints(), cannot be null!");
            return false;
        }
        this.points = points;
        return true;
    }

    public List<Integer> getBreaks() {
        return this.breaks;
    }

    public byte[] getBreaksData() {
        try {
            DataWriterBigEndian dw = new DataWriterBigEndian();
            for (int i = 0; i < this.breaks.size(); i++) {
                dw.writeInt(((Integer) this.breaks.get(i)).intValue());
            }
            return dw.toByteArray();
        } catch (Exception e) {
            Logger.logE(TAG, "getBreaksData()", e);
            return new byte[0];
        }
    }

    public void setBreaksData(byte[] data) {
        if (data != null && data.length != 0) {
            try {
                DataReaderBigEndian dr = new DataReaderBigEndian(data);
                this.breaks.clear();
                while (dr.available() > 0) {
                    this.breaks.add(Integer.valueOf(dr.readInt()));
                }
            } catch (Exception e) {
                Logger.logE(TAG, "setBreaksData()", e);
                this.breaks.clear();
            }
        }
    }

    public Waypoint getWaypoint(int index) {
        return (Waypoint) this.waypoints.get(index);
    }

    public List<Waypoint> getWaypoints() {
        return this.waypoints;
    }

    public boolean setWaypoints(List<Waypoint> wpts) {
        if (wpts == null) {
            Logger.logW(TAG, "setWaypoints(), cannot be null!");
            return false;
        }
        this.waypoints = wpts;
        return true;
    }

    public boolean isUseFolderStyle() {
        return this.mUseFolderStyle;
    }

    public void setUseFolderStyle(boolean useFolderStyle) {
        this.mUseFolderStyle = useFolderStyle;
    }

    public int getActivityType() {
        return this.mActivityType;
    }

    public void setActivityType(int activityType) {
        this.mActivityType = activityType;
    }

    public TrackStats getStats() {
        return this.mStats;
    }

    public void setStats(TrackStats stats) {
        if (stats == null) {
            throw new NullPointerException("setTrackStats(), parameter cannot be null");
        }
        this.mStats = stats;
    }

    public void setStats(byte[] data) {
        try {
            setStats(new TrackStats(data));
        } catch (Exception e) {
            Logger.logE(TAG, "setStats(" + Arrays.toString(data) + ")", e);
            setStats(new TrackStats());
        }
    }

    public int getVersion() {
        return 5;
    }

    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.f80id = dr.readLong();
        this.name = dr.readString();
        this.points = dr.readListStorable(Location.class);
        int breaksSize = dr.readInt();
        if (breaksSize > 0) {
            setBreaksData(dr.readBytes(breaksSize));
        }
        this.waypoints = dr.readListStorable(Waypoint.class);
        readExtraData(dr);
        readStyles(dr);
        this.mStats.reset();
        this.mStats.setNumOfPoints(dr.readInt());
        this.mStats.setStartTime(dr.readLong());
        this.mStats.setStopTime(dr.readLong());
        this.mStats.setTotalLength(dr.readFloat());
        this.mStats.setTotalLengthMove(dr.readFloat());
        this.mStats.setTotalTime(dr.readLong());
        this.mStats.setTotalTimeMove(dr.readLong());
        this.mStats.setSpeedMax(dr.readFloat());
        this.mStats.setAltitudeMax(dr.readFloat());
        this.mStats.setAltitudeMin(dr.readFloat());
        this.mStats.setEleNeutralDistance(dr.readFloat());
        this.mStats.setEleNeutralHeight(dr.readFloat());
        this.mStats.setElePositiveDistance(dr.readFloat());
        this.mStats.setElePositiveHeight(dr.readFloat());
        this.mStats.setEleNegativeDistance(dr.readFloat());
        this.mStats.setEleNegativeHeight(dr.readFloat());
        this.mStats.setEleTotalAbsDistance(dr.readFloat());
        this.mStats.setEleTotalAbsHeight(dr.readFloat());
        if (version >= 1) {
            this.mUseFolderStyle = dr.readBoolean();
        }
        if (version >= 2) {
            this.timeCreated = dr.readLong();
        }
        if (version >= 3) {
            this.mStats = new TrackStats(dr);
        }
        if (version >= 4) {
            setReadWriteMode(ReadWriteMode.values()[dr.readInt()]);
        }
        if (version >= 5) {
            this.mActivityType = dr.readInt();
        }
    }

    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.f80id);
        dw.writeString(this.name);
        dw.writeListStorable(this.points);
        byte[] breaksData = getBreaksData();
        dw.writeInt(breaksData.length);
        if (breaksData.length > 0) {
            dw.write(breaksData);
        }
        dw.writeListStorable(this.waypoints);
        writeExtraData(dw);
        writeStyles(dw);
        dw.writeInt(0);
        dw.writeLong(0);
        dw.writeLong(0);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeLong(0);
        dw.writeLong(0);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeFloat(0.0f);
        dw.writeBoolean(this.mUseFolderStyle);
        dw.writeLong(this.timeCreated);
        dw.writeStorable(this.mStats);
        dw.writeInt(getReadWriteMode().ordinal());
        dw.writeInt(this.mActivityType);
    }

    public void reset() {
        this.f80id = -1;
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
        setReadWriteMode(ReadWriteMode.READ_WRITE);
        this.mActivityType = 0;
    }
}
