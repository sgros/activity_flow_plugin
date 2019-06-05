// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.periodicUpdates;

import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.extra.TrackStats;
import locus.api.objects.extra.Location;
import locus.api.objects.Storable;

public class UpdateContainer extends Storable
{
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
    
    private Location readLocation(DataReaderBigEndian ex) throws IOException {
        if (!((DataReaderBigEndian)ex).readBoolean()) {
            ex = null;
        }
        else {
            try {
                ex = (IllegalAccessException)((DataReaderBigEndian)ex).readStorable(Location.class);
            }
            catch (InstantiationException ex2) {}
            catch (IllegalAccessException ex) {
                goto Label_0025;
            }
            catch (IOException ex) {
                goto Label_0025;
            }
        }
        return (Location)ex;
    }
    
    private void writeLocation(final DataWriterBigEndian dataWriterBigEndian, final Location location) throws IOException {
        if (location == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeStorable(location);
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
    
    public GuideTypeTrack getGuideTypeTrack() {
        GuideTypeTrack guideTypeTrack = null;
        if (this.guideType == 2 || this.guideType == 3) {
            guideTypeTrack = new GuideTypeTrack();
        }
        return guideTypeTrack;
    }
    
    public GuideTypeWaypoint getGuideTypeWaypoint() {
        GuideTypeWaypoint guideTypeWaypoint = null;
        if (this.guideType == 1) {
            guideTypeWaypoint = new GuideTypeWaypoint();
        }
        return guideTypeWaypoint;
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
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    public boolean isEnabledMyLocation() {
        return this.enabledMyLocation;
    }
    
    public boolean isGuideEnabled() {
        return this.getGuideType() != -1;
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
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.newMyLocation = dataReaderBigEndian.readBoolean();
        this.newMapCenter = dataReaderBigEndian.readBoolean();
        this.newZoomLevel = dataReaderBigEndian.readBoolean();
        this.isUserTouching = dataReaderBigEndian.readBoolean();
        this.enabledMyLocation = dataReaderBigEndian.readBoolean();
        this.locMyLocation = this.readLocation(dataReaderBigEndian);
        this.gpsSatsUsed = dataReaderBigEndian.readInt();
        this.gpsSatsAll = dataReaderBigEndian.readInt();
        this.declination = dataReaderBigEndian.readFloat();
        this.orientHeading = dataReaderBigEndian.readFloat();
        this.orientHeadingOpposit = dataReaderBigEndian.readFloat();
        this.orientCourse = dataReaderBigEndian.readFloat();
        this.orientPitch = dataReaderBigEndian.readFloat();
        this.orientRoll = dataReaderBigEndian.readFloat();
        this.orientGpsAngle = dataReaderBigEndian.readFloat();
        this.speedVertical = dataReaderBigEndian.readFloat();
        this.slope = dataReaderBigEndian.readFloat();
        this.mapVisible = dataReaderBigEndian.readBoolean();
        this.mapRotate = dataReaderBigEndian.readFloat();
        this.locMapCenter = this.readLocation(dataReaderBigEndian);
        this.mapTopLeft = this.readLocation(dataReaderBigEndian);
        this.mapBottomRight = this.readLocation(dataReaderBigEndian);
        this.mapZoomLevel = dataReaderBigEndian.readInt();
        this.trackRecRecording = dataReaderBigEndian.readBoolean();
        this.trackRecPaused = dataReaderBigEndian.readBoolean();
        this.trackRecProfileName = dataReaderBigEndian.readString();
        if (dataReaderBigEndian.readBoolean()) {
            (this.trackStats = new TrackStats()).read(dataReaderBigEndian);
        }
        this.guideType = dataReaderBigEndian.readInt();
        this.guideWptName = dataReaderBigEndian.readString();
        this.guideWptLoc = this.readLocation(dataReaderBigEndian);
        this.guideWptDist = dataReaderBigEndian.readDouble();
        this.guideWptAzim = dataReaderBigEndian.readFloat();
        this.guideWptAngle = dataReaderBigEndian.readFloat();
        this.guideWptTime = dataReaderBigEndian.readLong();
        this.guideDistFromStart = dataReaderBigEndian.readDouble();
        this.guideDistToFinish = dataReaderBigEndian.readDouble();
        this.guideTimeToFinish = dataReaderBigEndian.readLong();
        this.guideNavPoint1Name = dataReaderBigEndian.readString();
        this.guideNavPoint1Loc = this.readLocation(dataReaderBigEndian);
        this.guideNavPoint1Dist = dataReaderBigEndian.readDouble();
        this.guideNavPoint1Time = dataReaderBigEndian.readLong();
        this.guideNavPoint1Action = dataReaderBigEndian.readInt();
        this.guideNavPoint2Name = dataReaderBigEndian.readString();
        this.guideNavPoint2Loc = this.readLocation(dataReaderBigEndian);
        this.guideNavPoint2Dist = dataReaderBigEndian.readDouble();
        this.guideNavPoint2Time = dataReaderBigEndian.readLong();
        this.guideNavPoint2Action = dataReaderBigEndian.readInt();
        this.deviceBatteryValue = dataReaderBigEndian.readInt();
        this.deviceBatteryTemperature = dataReaderBigEndian.readFloat();
    }
    
    @Override
    public void reset() {
        this.newMyLocation = false;
        this.newMapCenter = false;
        this.newZoomLevel = false;
        this.isUserTouching = false;
        this.enabledMyLocation = false;
        this.locMyLocation = null;
        this.gpsSatsUsed = 0;
        this.gpsSatsAll = 0;
        this.declination = 0.0f;
        this.orientHeading = 0.0f;
        this.orientHeadingOpposit = 0.0f;
        this.orientCourse = 0.0f;
        this.orientPitch = 0.0f;
        this.orientRoll = 0.0f;
        this.orientGpsAngle = 0.0f;
        this.speedVertical = 0.0f;
        this.slope = 0.0f;
        this.mapVisible = false;
        this.mapRotate = 0.0f;
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
        this.guideWptDist = 0.0;
        this.guideWptAzim = 0.0f;
        this.guideWptAngle = 0.0f;
        this.guideWptTime = 0L;
        this.guideDistFromStart = 0.0;
        this.guideDistToFinish = 0.0;
        this.guideTimeToFinish = 0L;
        this.guideNavPoint1Name = "";
        this.guideNavPoint1Loc = null;
        this.guideNavPoint1Dist = 0.0;
        this.guideNavPoint1Time = 0L;
        this.guideNavPoint1Action = 0;
        this.guideNavPoint2Name = "";
        this.guideNavPoint2Loc = null;
        this.guideNavPoint2Dist = 0.0;
        this.guideNavPoint2Time = 0L;
        this.guideNavPoint2Action = 0;
        this.deviceBatteryValue = 0;
        this.deviceBatteryTemperature = 0.0f;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeBoolean(this.newMyLocation);
        dataWriterBigEndian.writeBoolean(this.newMapCenter);
        dataWriterBigEndian.writeBoolean(this.newZoomLevel);
        dataWriterBigEndian.writeBoolean(this.isUserTouching);
        dataWriterBigEndian.writeBoolean(this.enabledMyLocation);
        this.writeLocation(dataWriterBigEndian, this.locMyLocation);
        dataWriterBigEndian.writeInt(this.gpsSatsUsed);
        dataWriterBigEndian.writeInt(this.gpsSatsAll);
        dataWriterBigEndian.writeFloat(this.declination);
        dataWriterBigEndian.writeFloat(this.orientHeading);
        dataWriterBigEndian.writeFloat(this.orientHeadingOpposit);
        dataWriterBigEndian.writeFloat(this.orientCourse);
        dataWriterBigEndian.writeFloat(this.orientPitch);
        dataWriterBigEndian.writeFloat(this.orientRoll);
        dataWriterBigEndian.writeFloat(this.orientGpsAngle);
        dataWriterBigEndian.writeFloat(this.speedVertical);
        dataWriterBigEndian.writeFloat(this.slope);
        dataWriterBigEndian.writeBoolean(this.mapVisible);
        dataWriterBigEndian.writeFloat(this.mapRotate);
        this.writeLocation(dataWriterBigEndian, this.locMapCenter);
        this.writeLocation(dataWriterBigEndian, this.mapTopLeft);
        this.writeLocation(dataWriterBigEndian, this.mapBottomRight);
        dataWriterBigEndian.writeInt(this.mapZoomLevel);
        dataWriterBigEndian.writeBoolean(this.trackRecRecording);
        dataWriterBigEndian.writeBoolean(this.trackRecPaused);
        dataWriterBigEndian.writeString(this.trackRecProfileName);
        if (this.trackStats != null) {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeStorable(this.trackStats);
        }
        else {
            dataWriterBigEndian.writeBoolean(false);
        }
        dataWriterBigEndian.writeInt(this.guideType);
        dataWriterBigEndian.writeString(this.guideWptName);
        this.writeLocation(dataWriterBigEndian, this.guideWptLoc);
        dataWriterBigEndian.writeDouble(this.guideWptDist);
        dataWriterBigEndian.writeFloat(this.guideWptAzim);
        dataWriterBigEndian.writeFloat(this.guideWptAngle);
        dataWriterBigEndian.writeLong(this.guideWptTime);
        dataWriterBigEndian.writeDouble(this.guideDistFromStart);
        dataWriterBigEndian.writeDouble(this.guideDistToFinish);
        dataWriterBigEndian.writeLong(this.guideTimeToFinish);
        dataWriterBigEndian.writeString(this.guideNavPoint1Name);
        this.writeLocation(dataWriterBigEndian, this.guideNavPoint1Loc);
        dataWriterBigEndian.writeDouble(this.guideNavPoint1Dist);
        dataWriterBigEndian.writeLong(this.guideNavPoint1Time);
        dataWriterBigEndian.writeInt(this.guideNavPoint1Action);
        dataWriterBigEndian.writeString(this.guideNavPoint2Name);
        this.writeLocation(dataWriterBigEndian, this.guideNavPoint2Loc);
        dataWriterBigEndian.writeDouble(this.guideNavPoint2Dist);
        dataWriterBigEndian.writeLong(this.guideNavPoint2Time);
        dataWriterBigEndian.writeInt(this.guideNavPoint2Action);
        dataWriterBigEndian.writeInt(this.deviceBatteryValue);
        dataWriterBigEndian.writeFloat(this.deviceBatteryTemperature);
    }
    
    private class GuideTypeBasic
    {
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
    
    public class GuideTypeTrack extends GuideTypeBasic
    {
        private GuideTypeTrack() {
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
            return this.getNavPoint1Loc() != null;
        }
        
        public boolean hasNavPoint2() {
            return this.getNavPoint2Loc() != null;
        }
    }
    
    public class GuideTypeWaypoint extends GuideTypeBasic
    {
        private GuideTypeWaypoint() {
        }
    }
}
