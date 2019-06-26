package locus.api.objects.geocaching;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingWaypoint extends Storable {
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

    public void setCode(String code) {
        if (code == null) {
            Logger.logD(TAG, "setCode(), empty parameter");
            code = "";
        }
        this.mCode = code;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        if (name == null) {
            Logger.logD(TAG, "setName(), empty parameter");
            name = "";
        }
        this.mName = name;
    }

    public String getDesc() {
        return this.mDesc;
    }

    public void setDesc(String desc) {
        if (desc == null) {
            Logger.logD(TAG, "setDesc(), empty parameter");
            desc = "";
        }
        this.mDesc = desc;
    }

    public boolean isDescModified() {
        return this.mDescModified;
    }

    public void setDescModified(boolean modified) {
        this.mDescModified = modified;
    }

    public String getTypeImagePath() {
        return this.mTypeImagePath;
    }

    public void setTypeImagePath(String typeImagePath) {
        if (typeImagePath == null) {
            Logger.logD(TAG, "setTypeImagePath(), empty parameter");
            typeImagePath = "";
        }
        this.mTypeImagePath = typeImagePath;
    }

    public double getLon() {
        return this.mLon;
    }

    public void setLon(double lon) {
        this.mLon = lon;
    }

    public double getLat() {
        return this.mLat;
    }

    public void setLat(double lat) {
        this.mLat = lat;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        if (type == null) {
            Logger.logD(TAG, "setType(), empty parameter");
            type = "";
        }
        if (type.toLowerCase().startsWith("waypoint|")) {
            type = type.substring("waypoint|".length());
        }
        this.mType = type;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 1;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mCode = dr.readString();
        this.mName = dr.readString();
        this.mDesc = dr.readString();
        this.mType = dr.readString();
        this.mTypeImagePath = dr.readString();
        this.mLon = dr.readDouble();
        this.mLat = dr.readDouble();
        if (version >= 1) {
            this.mDescModified = dr.readBoolean();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mCode);
        dw.writeString(this.mName);
        dw.writeString(this.mDesc);
        dw.writeString(this.mType);
        dw.writeString(this.mTypeImagePath);
        dw.writeDouble(this.mLon);
        dw.writeDouble(this.mLat);
        dw.writeBoolean(this.mDescModified);
    }

    public void reset() {
        this.mCode = "";
        this.mName = "";
        this.mDesc = "";
        this.mType = "";
        this.mTypeImagePath = "";
        this.mLon = 0.0d;
        this.mLat = 0.0d;
        this.mDescModified = false;
    }
}
