package locus.api.android.features.periodicUpdates;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.TrackStats;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class UpdateContainer extends Storable {
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

    private class GuideTypeBasic {
        private GuideTypeBasic() {
        }

        public String getTargetName() {
            return UpdateContainer.this.guideWptName;
        }

        public Location getTargetLoc() {
            return UpdateContainer.this.guideWptLoc;
        }

        public double getTargetDist() {
            return UpdateContainer.this.guideWptDist;
        }

        public float getTargetAzim() {
            return UpdateContainer.this.guideWptAzim;
        }

        public float getTargetAngle() {
            return UpdateContainer.this.guideWptAngle;
        }

        public long getTargetTime() {
            return UpdateContainer.this.guideWptTime;
        }
    }

    public class GuideTypeTrack extends GuideTypeBasic {
        public /* bridge */ /* synthetic */ float getTargetAngle() {
            return super.getTargetAngle();
        }

        public /* bridge */ /* synthetic */ float getTargetAzim() {
            return super.getTargetAzim();
        }

        public /* bridge */ /* synthetic */ double getTargetDist() {
            return super.getTargetDist();
        }

        public /* bridge */ /* synthetic */ Location getTargetLoc() {
            return super.getTargetLoc();
        }

        public /* bridge */ /* synthetic */ String getTargetName() {
            return super.getTargetName();
        }

        public /* bridge */ /* synthetic */ long getTargetTime() {
            return super.getTargetTime();
        }

        private GuideTypeTrack() {
            super();
        }

        public double getDistFromStart() {
            return UpdateContainer.this.guideDistFromStart;
        }

        public double getDistToFinish() {
            return UpdateContainer.this.guideDistToFinish;
        }

        public long getTimeToFinish() {
            return UpdateContainer.this.guideTimeToFinish;
        }

        public boolean hasNavPoint1() {
            return getNavPoint1Loc() != null;
        }

        public String getNavPoint1Name() {
            return UpdateContainer.this.guideNavPoint1Name;
        }

        public Location getNavPoint1Loc() {
            return UpdateContainer.this.guideNavPoint1Loc;
        }

        public double getNavPoint1Dist() {
            return UpdateContainer.this.guideNavPoint1Dist;
        }

        public double getNavPoint1Time() {
            return (double) UpdateContainer.this.guideNavPoint1Time;
        }

        public int getNavPoint1Action() {
            return UpdateContainer.this.guideNavPoint1Action;
        }

        public boolean hasNavPoint2() {
            return getNavPoint2Loc() != null;
        }

        public String getNavPoint2Name() {
            return UpdateContainer.this.guideNavPoint2Name;
        }

        public Location getNavPoint2Loc() {
            return UpdateContainer.this.guideNavPoint2Loc;
        }

        public double getNavPoint2Dist() {
            return UpdateContainer.this.guideNavPoint2Dist;
        }

        public double getNavPoint2Time() {
            return (double) UpdateContainer.this.guideNavPoint2Time;
        }

        public int getNavPoint2Action() {
            return UpdateContainer.this.guideNavPoint2Action;
        }
    }

    public class GuideTypeWaypoint extends GuideTypeBasic {
        public /* bridge */ /* synthetic */ float getTargetAngle() {
            return super.getTargetAngle();
        }

        public /* bridge */ /* synthetic */ float getTargetAzim() {
            return super.getTargetAzim();
        }

        public /* bridge */ /* synthetic */ double getTargetDist() {
            return super.getTargetDist();
        }

        public /* bridge */ /* synthetic */ Location getTargetLoc() {
            return super.getTargetLoc();
        }

        public /* bridge */ /* synthetic */ String getTargetName() {
            return super.getTargetName();
        }

        public /* bridge */ /* synthetic */ long getTargetTime() {
            return super.getTargetTime();
        }

        private GuideTypeWaypoint() {
            super();
        }
    }

    public boolean isNewMyLocation() {
        return this.newMyLocation;
    }

    public boolean isNewMapCenter() {
        return this.newMapCenter;
    }

    public boolean isNewZoomLevel() {
        return this.newZoomLevel;
    }

    public boolean isUserTouching() {
        return this.isUserTouching;
    }

    public boolean isEnabledMyLocation() {
        return this.enabledMyLocation;
    }

    public Location getLocMyLocation() {
        return this.locMyLocation;
    }

    public int getGpsSatsUsed() {
        return this.gpsSatsUsed;
    }

    public int getGpsSatsAll() {
        return this.gpsSatsAll;
    }

    public float getDeclination() {
        return this.declination;
    }

    public float getOrientHeading() {
        return this.orientHeading;
    }

    public float getOrientHeadingOpposit() {
        return this.orientHeadingOpposit;
    }

    public float getOrientCourse() {
        return this.orientCourse;
    }

    public float getOrientPitch() {
        return this.orientPitch;
    }

    public float getOrientRoll() {
        return this.orientRoll;
    }

    public float getOrientGpsAngle() {
        return this.orientGpsAngle;
    }

    public float getSpeedVertical() {
        return this.speedVertical;
    }

    public float getSlope() {
        return this.slope;
    }

    public boolean isMapVisible() {
        return this.mapVisible;
    }

    public float getMapRotate() {
        return this.mapRotate;
    }

    public Location getLocMapCenter() {
        return this.locMapCenter;
    }

    public Location getMapTopLeft() {
        return this.mapTopLeft;
    }

    public Location getMapBottomRight() {
        return this.mapBottomRight;
    }

    public int getMapZoomLevel() {
        return this.mapZoomLevel;
    }

    public boolean isTrackRecRecording() {
        return this.trackRecRecording;
    }

    public boolean isTrackRecPaused() {
        return this.trackRecPaused;
    }

    public String getTrackRecProfileName() {
        return this.trackRecProfileName;
    }

    public TrackStats getTrackRecStats() {
        return this.trackStats;
    }

    public boolean isGuideEnabled() {
        return getGuideType() != -1;
    }

    public int getGuideType() {
        return this.guideType;
    }

    public GuideTypeWaypoint getGuideTypeWaypoint() {
        if (this.guideType != 1) {
            return null;
        }
        return new GuideTypeWaypoint();
    }

    public GuideTypeTrack getGuideTypeTrack() {
        if (this.guideType == 2 || this.guideType == 3) {
            return new GuideTypeTrack();
        }
        return null;
    }

    public int getDeviceBatteryValue() {
        return this.deviceBatteryValue;
    }

    public float getDeviceBatteryTemperature() {
        return this.deviceBatteryTemperature;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

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
        this.guideWptDist = 0.0d;
        this.guideWptAzim = 0.0f;
        this.guideWptAngle = 0.0f;
        this.guideWptTime = 0;
        this.guideDistFromStart = 0.0d;
        this.guideDistToFinish = 0.0d;
        this.guideTimeToFinish = 0;
        this.guideNavPoint1Name = "";
        this.guideNavPoint1Loc = null;
        this.guideNavPoint1Dist = 0.0d;
        this.guideNavPoint1Time = 0;
        this.guideNavPoint1Action = 0;
        this.guideNavPoint2Name = "";
        this.guideNavPoint2Loc = null;
        this.guideNavPoint2Dist = 0.0d;
        this.guideNavPoint2Time = 0;
        this.guideNavPoint2Action = 0;
        this.deviceBatteryValue = 0;
        this.deviceBatteryTemperature = 0.0f;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.newMyLocation = dr.readBoolean();
        this.newMapCenter = dr.readBoolean();
        this.newZoomLevel = dr.readBoolean();
        this.isUserTouching = dr.readBoolean();
        this.enabledMyLocation = dr.readBoolean();
        this.locMyLocation = readLocation(dr);
        this.gpsSatsUsed = dr.readInt();
        this.gpsSatsAll = dr.readInt();
        this.declination = dr.readFloat();
        this.orientHeading = dr.readFloat();
        this.orientHeadingOpposit = dr.readFloat();
        this.orientCourse = dr.readFloat();
        this.orientPitch = dr.readFloat();
        this.orientRoll = dr.readFloat();
        this.orientGpsAngle = dr.readFloat();
        this.speedVertical = dr.readFloat();
        this.slope = dr.readFloat();
        this.mapVisible = dr.readBoolean();
        this.mapRotate = dr.readFloat();
        this.locMapCenter = readLocation(dr);
        this.mapTopLeft = readLocation(dr);
        this.mapBottomRight = readLocation(dr);
        this.mapZoomLevel = dr.readInt();
        this.trackRecRecording = dr.readBoolean();
        this.trackRecPaused = dr.readBoolean();
        this.trackRecProfileName = dr.readString();
        if (dr.readBoolean()) {
            this.trackStats = new TrackStats();
            this.trackStats.read(dr);
        }
        this.guideType = dr.readInt();
        this.guideWptName = dr.readString();
        this.guideWptLoc = readLocation(dr);
        this.guideWptDist = dr.readDouble();
        this.guideWptAzim = dr.readFloat();
        this.guideWptAngle = dr.readFloat();
        this.guideWptTime = dr.readLong();
        this.guideDistFromStart = dr.readDouble();
        this.guideDistToFinish = dr.readDouble();
        this.guideTimeToFinish = dr.readLong();
        this.guideNavPoint1Name = dr.readString();
        this.guideNavPoint1Loc = readLocation(dr);
        this.guideNavPoint1Dist = dr.readDouble();
        this.guideNavPoint1Time = dr.readLong();
        this.guideNavPoint1Action = dr.readInt();
        this.guideNavPoint2Name = dr.readString();
        this.guideNavPoint2Loc = readLocation(dr);
        this.guideNavPoint2Dist = dr.readDouble();
        this.guideNavPoint2Time = dr.readLong();
        this.guideNavPoint2Action = dr.readInt();
        this.deviceBatteryValue = dr.readInt();
        this.deviceBatteryTemperature = dr.readFloat();
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeBoolean(this.newMyLocation);
        dw.writeBoolean(this.newMapCenter);
        dw.writeBoolean(this.newZoomLevel);
        dw.writeBoolean(this.isUserTouching);
        dw.writeBoolean(this.enabledMyLocation);
        writeLocation(dw, this.locMyLocation);
        dw.writeInt(this.gpsSatsUsed);
        dw.writeInt(this.gpsSatsAll);
        dw.writeFloat(this.declination);
        dw.writeFloat(this.orientHeading);
        dw.writeFloat(this.orientHeadingOpposit);
        dw.writeFloat(this.orientCourse);
        dw.writeFloat(this.orientPitch);
        dw.writeFloat(this.orientRoll);
        dw.writeFloat(this.orientGpsAngle);
        dw.writeFloat(this.speedVertical);
        dw.writeFloat(this.slope);
        dw.writeBoolean(this.mapVisible);
        dw.writeFloat(this.mapRotate);
        writeLocation(dw, this.locMapCenter);
        writeLocation(dw, this.mapTopLeft);
        writeLocation(dw, this.mapBottomRight);
        dw.writeInt(this.mapZoomLevel);
        dw.writeBoolean(this.trackRecRecording);
        dw.writeBoolean(this.trackRecPaused);
        dw.writeString(this.trackRecProfileName);
        if (this.trackStats != null) {
            dw.writeBoolean(true);
            dw.writeStorable(this.trackStats);
        } else {
            dw.writeBoolean(false);
        }
        dw.writeInt(this.guideType);
        dw.writeString(this.guideWptName);
        writeLocation(dw, this.guideWptLoc);
        dw.writeDouble(this.guideWptDist);
        dw.writeFloat(this.guideWptAzim);
        dw.writeFloat(this.guideWptAngle);
        dw.writeLong(this.guideWptTime);
        dw.writeDouble(this.guideDistFromStart);
        dw.writeDouble(this.guideDistToFinish);
        dw.writeLong(this.guideTimeToFinish);
        dw.writeString(this.guideNavPoint1Name);
        writeLocation(dw, this.guideNavPoint1Loc);
        dw.writeDouble(this.guideNavPoint1Dist);
        dw.writeLong(this.guideNavPoint1Time);
        dw.writeInt(this.guideNavPoint1Action);
        dw.writeString(this.guideNavPoint2Name);
        writeLocation(dw, this.guideNavPoint2Loc);
        dw.writeDouble(this.guideNavPoint2Dist);
        dw.writeLong(this.guideNavPoint2Time);
        dw.writeInt(this.guideNavPoint2Action);
        dw.writeInt(this.deviceBatteryValue);
        dw.writeFloat(this.deviceBatteryTemperature);
    }

    private Location readLocation(DataReaderBigEndian dr) throws IOException {
        if (!dr.readBoolean()) {
            return null;
        }
        try {
            return (Location) dr.readStorable(Location.class);
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void writeLocation(DataWriterBigEndian dw, Location loc) throws IOException {
        if (loc == null) {
            dw.writeBoolean(false);
            return;
        }
        dw.writeBoolean(true);
        dw.writeStorable(loc);
    }
}
