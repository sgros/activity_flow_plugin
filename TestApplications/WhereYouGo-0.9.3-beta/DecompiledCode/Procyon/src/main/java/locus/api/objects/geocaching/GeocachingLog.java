// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.geocaching;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import java.util.ArrayList;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import java.util.Iterator;
import java.util.List;
import locus.api.objects.Storable;

public class GeocachingLog extends Storable
{
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
    public static final long FINDERS_ID_UNDEFINED = 0L;
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
    
    public void addImage(final GeocachingImage geocachingImage) {
        this.mImages.add(geocachingImage);
    }
    
    public double getCooLat() {
        return this.mCooLat;
    }
    
    public double getCooLon() {
        return this.mCooLon;
    }
    
    public long getDate() {
        return this.mDate;
    }
    
    public String getFinder() {
        return this.mFinder;
    }
    
    public int getFindersFound() {
        return this.mFindersFound;
    }
    
    public long getFindersId() {
        return this.mFindersId;
    }
    
    public long getId() {
        return this.mId;
    }
    
    public Iterator<GeocachingImage> getImages() {
        return this.mImages.iterator();
    }
    
    public String getLogText() {
        return this.mLogText;
    }
    
    public int getType() {
        return this.mType;
    }
    
    @Override
    protected int getVersion() {
        return 2;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mId = dataReaderBigEndian.readLong();
        this.mType = dataReaderBigEndian.readInt();
        this.mDate = dataReaderBigEndian.readLong();
        this.mFinder = dataReaderBigEndian.readString();
        this.mFindersFound = dataReaderBigEndian.readInt();
        this.mLogText = dataReaderBigEndian.readString();
        if (n >= 1) {
            this.mImages = (List<GeocachingImage>)dataReaderBigEndian.readListStorable(GeocachingImage.class);
        }
        if (n >= 2) {
            this.mFindersId = dataReaderBigEndian.readLong();
            this.mCooLon = dataReaderBigEndian.readDouble();
            this.mCooLat = dataReaderBigEndian.readDouble();
        }
    }
    
    @Override
    public void reset() {
        this.mId = 0L;
        this.mType = -1;
        this.mDate = 0L;
        this.mFinder = "";
        this.mFindersFound = 0;
        this.mLogText = "";
        this.mImages = new ArrayList<GeocachingImage>();
        this.mFindersId = 0L;
        this.mCooLon = 0.0;
        this.mCooLat = 0.0;
    }
    
    public void setCooLat(final double mCooLat) {
        this.mCooLat = mCooLat;
    }
    
    public void setCooLon(final double mCooLon) {
        this.mCooLon = mCooLon;
    }
    
    public void setDate(final long mDate) {
        this.mDate = mDate;
    }
    
    public void setFinder(final String s) {
        String mFinder = s;
        if (s == null) {
            Logger.logD("GeocachingLog", "setFinder(), empty parameter");
            mFinder = "";
        }
        this.mFinder = mFinder;
    }
    
    public void setFindersFound(final int mFindersFound) {
        this.mFindersFound = mFindersFound;
    }
    
    public void setFindersId(final long mFindersId) {
        this.mFindersId = mFindersId;
    }
    
    public void setId(final long mId) {
        this.mId = mId;
    }
    
    public void setLogText(final String s) {
        String mLogText = s;
        if (s == null) {
            Logger.logD("GeocachingLog", "setLogText(), empty parameter");
            mLogText = "";
        }
        this.mLogText = mLogText;
    }
    
    public void setType(final int mType) {
        this.mType = mType;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.mId);
        dataWriterBigEndian.writeInt(this.mType);
        dataWriterBigEndian.writeLong(this.mDate);
        dataWriterBigEndian.writeString(this.mFinder);
        dataWriterBigEndian.writeInt(this.mFindersFound);
        dataWriterBigEndian.writeString(this.mLogText);
        dataWriterBigEndian.writeListStorable(this.mImages);
        dataWriterBigEndian.writeLong(this.mFindersId);
        dataWriterBigEndian.writeDouble(this.mCooLon);
        dataWriterBigEndian.writeDouble(this.mCooLat);
    }
}
