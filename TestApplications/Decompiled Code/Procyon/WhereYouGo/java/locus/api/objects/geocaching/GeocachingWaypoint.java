// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.geocaching;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.Storable;

public class GeocachingWaypoint extends Storable
{
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
    
    @Override
    protected int getVersion() {
        return 1;
    }
    
    public boolean isDescModified() {
        return this.mDescModified;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mCode = dataReaderBigEndian.readString();
        this.mName = dataReaderBigEndian.readString();
        this.mDesc = dataReaderBigEndian.readString();
        this.mType = dataReaderBigEndian.readString();
        this.mTypeImagePath = dataReaderBigEndian.readString();
        this.mLon = dataReaderBigEndian.readDouble();
        this.mLat = dataReaderBigEndian.readDouble();
        if (n >= 1) {
            this.mDescModified = dataReaderBigEndian.readBoolean();
        }
    }
    
    @Override
    public void reset() {
        this.mCode = "";
        this.mName = "";
        this.mDesc = "";
        this.mType = "";
        this.mTypeImagePath = "";
        this.mLon = 0.0;
        this.mLat = 0.0;
        this.mDescModified = false;
    }
    
    public void setCode(final String s) {
        String mCode = s;
        if (s == null) {
            Logger.logD("GeocachingWaypoint", "setCode(), empty parameter");
            mCode = "";
        }
        this.mCode = mCode;
    }
    
    public void setDesc(final String s) {
        String mDesc = s;
        if (s == null) {
            Logger.logD("GeocachingWaypoint", "setDesc(), empty parameter");
            mDesc = "";
        }
        this.mDesc = mDesc;
    }
    
    public void setDescModified(final boolean mDescModified) {
        this.mDescModified = mDescModified;
    }
    
    public void setLat(final double mLat) {
        this.mLat = mLat;
    }
    
    public void setLon(final double mLon) {
        this.mLon = mLon;
    }
    
    public void setName(final String s) {
        String mName = s;
        if (s == null) {
            Logger.logD("GeocachingWaypoint", "setName(), empty parameter");
            mName = "";
        }
        this.mName = mName;
    }
    
    public void setType(String substring) {
        String s = substring;
        if (substring == null) {
            Logger.logD("GeocachingWaypoint", "setType(), empty parameter");
            s = "";
        }
        substring = s;
        if (s.toLowerCase().startsWith("waypoint|")) {
            substring = s.substring("waypoint|".length());
        }
        this.mType = substring;
    }
    
    public void setTypeImagePath(final String s) {
        String mTypeImagePath = s;
        if (s == null) {
            Logger.logD("GeocachingWaypoint", "setTypeImagePath(), empty parameter");
            mTypeImagePath = "";
        }
        this.mTypeImagePath = mTypeImagePath;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mCode);
        dataWriterBigEndian.writeString(this.mName);
        dataWriterBigEndian.writeString(this.mDesc);
        dataWriterBigEndian.writeString(this.mType);
        dataWriterBigEndian.writeString(this.mTypeImagePath);
        dataWriterBigEndian.writeDouble(this.mLon);
        dataWriterBigEndian.writeDouble(this.mLat);
        dataWriterBigEndian.writeBoolean(this.mDescModified);
    }
}
