// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import java.util.Arrays;
import java.util.ArrayList;
import locus.api.objects.Storable;
import locus.api.utils.Logger;
import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import java.util.List;
import locus.api.objects.GeoData;

public class Track extends GeoData
{
    private static final String TAG = "Track";
    List<Integer> breaks;
    private int mActivityType;
    private TrackStats mStats;
    private boolean mUseFolderStyle;
    List<Location> points;
    List<Waypoint> waypoints;
    
    public Track() {
    }
    
    public Track(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
    }
    
    public Track(final byte[] array) throws IOException {
        super(array);
    }
    
    public int getActivityType() {
        return this.mActivityType;
    }
    
    public List<Integer> getBreaks() {
        return this.breaks;
    }
    
    public byte[] getBreaksData() {
        try {
            final DataWriterBigEndian dataWriterBigEndian = new DataWriterBigEndian();
            for (int i = 0; i < this.breaks.size(); ++i) {
                dataWriterBigEndian.writeInt(this.breaks.get(i));
            }
            return dataWriterBigEndian.toByteArray();
        }
        catch (Exception ex) {
            Logger.logE("Track", "getBreaksData()", ex);
            return new byte[0];
        }
    }
    
    public Location getPoint(final int n) {
        return this.points.get(n);
    }
    
    public List<Location> getPoints() {
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
    
    public Waypoint getWaypoint(final int n) {
        return this.waypoints.get(n);
    }
    
    public List<Waypoint> getWaypoints() {
        return this.waypoints;
    }
    
    public boolean isUseFolderStyle() {
        return this.mUseFolderStyle;
    }
    
    public void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.id = dataReaderBigEndian.readLong();
        this.name = dataReaderBigEndian.readString();
        this.points = (List<Location>)dataReaderBigEndian.readListStorable(Location.class);
        final int int1 = dataReaderBigEndian.readInt();
        if (int1 > 0) {
            this.setBreaksData(dataReaderBigEndian.readBytes(int1));
        }
        this.waypoints = (List<Waypoint>)dataReaderBigEndian.readListStorable(Waypoint.class);
        this.readExtraData(dataReaderBigEndian);
        this.readStyles(dataReaderBigEndian);
        this.mStats.reset();
        this.mStats.setNumOfPoints(dataReaderBigEndian.readInt());
        this.mStats.setStartTime(dataReaderBigEndian.readLong());
        this.mStats.setStopTime(dataReaderBigEndian.readLong());
        this.mStats.setTotalLength(dataReaderBigEndian.readFloat());
        this.mStats.setTotalLengthMove(dataReaderBigEndian.readFloat());
        this.mStats.setTotalTime(dataReaderBigEndian.readLong());
        this.mStats.setTotalTimeMove(dataReaderBigEndian.readLong());
        this.mStats.setSpeedMax(dataReaderBigEndian.readFloat());
        this.mStats.setAltitudeMax(dataReaderBigEndian.readFloat());
        this.mStats.setAltitudeMin(dataReaderBigEndian.readFloat());
        this.mStats.setEleNeutralDistance(dataReaderBigEndian.readFloat());
        this.mStats.setEleNeutralHeight(dataReaderBigEndian.readFloat());
        this.mStats.setElePositiveDistance(dataReaderBigEndian.readFloat());
        this.mStats.setElePositiveHeight(dataReaderBigEndian.readFloat());
        this.mStats.setEleNegativeDistance(dataReaderBigEndian.readFloat());
        this.mStats.setEleNegativeHeight(dataReaderBigEndian.readFloat());
        this.mStats.setEleTotalAbsDistance(dataReaderBigEndian.readFloat());
        this.mStats.setEleTotalAbsHeight(dataReaderBigEndian.readFloat());
        if (n >= 1) {
            this.mUseFolderStyle = dataReaderBigEndian.readBoolean();
        }
        if (n >= 2) {
            this.timeCreated = dataReaderBigEndian.readLong();
        }
        if (n >= 3) {
            this.mStats = new TrackStats(dataReaderBigEndian);
        }
        if (n >= 4) {
            this.setReadWriteMode(ReadWriteMode.values()[dataReaderBigEndian.readInt()]);
        }
        if (n >= 5) {
            this.mActivityType = dataReaderBigEndian.readInt();
        }
    }
    
    @Override
    public void reset() {
        this.id = -1L;
        this.name = "";
        this.points = new ArrayList<Location>();
        this.breaks = new ArrayList<Integer>();
        this.waypoints = new ArrayList<Waypoint>();
        this.extraData = null;
        this.styleNormal = null;
        this.styleHighlight = null;
        this.mUseFolderStyle = true;
        this.timeCreated = System.currentTimeMillis();
        this.mStats = new TrackStats();
        this.setReadWriteMode(ReadWriteMode.READ_WRITE);
        this.mActivityType = 0;
    }
    
    public void setActivityType(final int mActivityType) {
        this.mActivityType = mActivityType;
    }
    
    public void setBreaksData(final byte[] array) {
        if (array != null && array.length != 0) {
            try {
                final DataReaderBigEndian dataReaderBigEndian = new DataReaderBigEndian(array);
                this.breaks.clear();
                while (dataReaderBigEndian.available() > 0) {
                    this.breaks.add(dataReaderBigEndian.readInt());
                }
            }
            catch (Exception ex) {
                Logger.logE("Track", "setBreaksData()", ex);
                this.breaks.clear();
            }
        }
    }
    
    public boolean setPoints(final List<Location> points) {
        boolean b;
        if (points == null) {
            Logger.logW("Track", "setPoints(), cannot be null!");
            b = false;
        }
        else {
            this.points = points;
            b = true;
        }
        return b;
    }
    
    public void setStats(final TrackStats mStats) {
        if (mStats == null) {
            throw new NullPointerException("setTrackStats(), parameter cannot be null");
        }
        this.mStats = mStats;
    }
    
    public void setStats(final byte[] a) {
        try {
            this.setStats(new TrackStats(a));
        }
        catch (Exception ex) {
            Logger.logE("Track", "setStats(" + Arrays.toString(a) + ")", ex);
            this.setStats(new TrackStats());
        }
    }
    
    public void setUseFolderStyle(final boolean mUseFolderStyle) {
        this.mUseFolderStyle = mUseFolderStyle;
    }
    
    public boolean setWaypoints(final List<Waypoint> waypoints) {
        boolean b;
        if (waypoints == null) {
            Logger.logW("Track", "setWaypoints(), cannot be null!");
            b = false;
        }
        else {
            this.waypoints = waypoints;
            b = true;
        }
        return b;
    }
    
    public void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.id);
        dataWriterBigEndian.writeString(this.name);
        dataWriterBigEndian.writeListStorable(this.points);
        final byte[] breaksData = this.getBreaksData();
        dataWriterBigEndian.writeInt(breaksData.length);
        if (breaksData.length > 0) {
            dataWriterBigEndian.write(breaksData);
        }
        dataWriterBigEndian.writeListStorable(this.waypoints);
        this.writeExtraData(dataWriterBigEndian);
        this.writeStyles(dataWriterBigEndian);
        dataWriterBigEndian.writeInt(0);
        dataWriterBigEndian.writeLong(0L);
        dataWriterBigEndian.writeLong(0L);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeLong(0L);
        dataWriterBigEndian.writeLong(0L);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeFloat(0.0f);
        dataWriterBigEndian.writeBoolean(this.mUseFolderStyle);
        dataWriterBigEndian.writeLong(this.timeCreated);
        dataWriterBigEndian.writeStorable(this.mStats);
        dataWriterBigEndian.writeInt(this.getReadWriteMode().ordinal());
        dataWriterBigEndian.writeInt(this.mActivityType);
    }
}
