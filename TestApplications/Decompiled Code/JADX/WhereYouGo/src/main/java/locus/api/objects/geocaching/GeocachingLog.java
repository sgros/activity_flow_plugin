package locus.api.objects.geocaching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingLog extends Storable {
    public static final int CACHE_LOG_TYPE_ANNOUNCEMENT = 9;
    public static final int CACHE_LOG_TYPE_ARCHIVE = 16;
    public static final int CACHE_LOG_TYPE_ATTENDED = 11;
    public static final int CACHE_LOG_TYPE_ENABLE_LISTING = 6;
    public static final int CACHE_LOG_TYPE_FOUND = 0;
    public static final int CACHE_LOG_TYPE_NEEDS_ARCHIVED = 13;
    public static final int CACHE_LOG_TYPE_NEEDS_MAINTENANCE = 3;
    public static final int CACHE_LOG_TYPE_NOT_FOUND = 1;
    public static final int CACHE_LOG_TYPE_OWNER_MAINTENANCE = 4;
    public static final int CACHE_LOG_TYPE_PERMANENTLY_ARCHIVED = 18;
    public static final int CACHE_LOG_TYPE_POST_REVIEWER_NOTE = 12;
    public static final int CACHE_LOG_TYPE_PUBLISH_LISTING = 5;
    public static final int CACHE_LOG_TYPE_RETRACT_LISTING = 15;
    public static final int CACHE_LOG_TYPE_TEMPORARILY_DISABLE_LISTING = 7;
    public static final int CACHE_LOG_TYPE_UNARCHIVE = 17;
    public static final int CACHE_LOG_TYPE_UNKNOWN = -1;
    public static final int CACHE_LOG_TYPE_UPDATE_COORDINATES = 8;
    public static final int CACHE_LOG_TYPE_WEBCAM_PHOTO_TAKEN = 14;
    public static final int CACHE_LOG_TYPE_WILL_ATTEND = 10;
    public static final int CACHE_LOG_TYPE_WRITE_NOTE = 2;
    public static final long FINDERS_ID_UNDEFINED = 0;
    private static final String TAG = "GeocachingLog";
    private double mCooLat;
    private double mCooLon;
    private long mDate;
    private String mFinder;
    private int mFindersFound;
    private long mFindersId;
    private long mId;
    private List<GeocachingImage> mImages;
    private String mLogText;
    private int mType;

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public long getDate() {
        return this.mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public String getFinder() {
        return this.mFinder;
    }

    public void setFinder(String finder) {
        if (finder == null) {
            Logger.logD(TAG, "setFinder(), empty parameter");
            finder = "";
        }
        this.mFinder = finder;
    }

    public long getFindersId() {
        return this.mFindersId;
    }

    public void setFindersId(long finderId) {
        this.mFindersId = finderId;
    }

    public int getFindersFound() {
        return this.mFindersFound;
    }

    public void setFindersFound(int finderFound) {
        this.mFindersFound = finderFound;
    }

    public String getLogText() {
        return this.mLogText;
    }

    public void setLogText(String logText) {
        if (logText == null) {
            Logger.logD(TAG, "setLogText(), empty parameter");
            logText = "";
        }
        this.mLogText = logText;
    }

    public void addImage(GeocachingImage image) {
        this.mImages.add(image);
    }

    public Iterator<GeocachingImage> getImages() {
        return this.mImages.iterator();
    }

    public double getCooLon() {
        return this.mCooLon;
    }

    public void setCooLon(double lon) {
        this.mCooLon = lon;
    }

    public double getCooLat() {
        return this.mCooLat;
    }

    public void setCooLat(double lat) {
        this.mCooLat = lat;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 2;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readLong();
        this.mType = dr.readInt();
        this.mDate = dr.readLong();
        this.mFinder = dr.readString();
        this.mFindersFound = dr.readInt();
        this.mLogText = dr.readString();
        if (version >= 1) {
            this.mImages = dr.readListStorable(GeocachingImage.class);
        }
        if (version >= 2) {
            this.mFindersId = dr.readLong();
            this.mCooLon = dr.readDouble();
            this.mCooLat = dr.readDouble();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.mId);
        dw.writeInt(this.mType);
        dw.writeLong(this.mDate);
        dw.writeString(this.mFinder);
        dw.writeInt(this.mFindersFound);
        dw.writeString(this.mLogText);
        dw.writeListStorable(this.mImages);
        dw.writeLong(this.mFindersId);
        dw.writeDouble(this.mCooLon);
        dw.writeDouble(this.mCooLat);
    }

    public void reset() {
        this.mId = 0;
        this.mType = -1;
        this.mDate = 0;
        this.mFinder = "";
        this.mFindersFound = 0;
        this.mLogText = "";
        this.mImages = new ArrayList();
        this.mFindersId = 0;
        this.mCooLon = 0.0d;
        this.mCooLat = 0.0d;
    }
}
