// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.geocaching;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.DataReaderBigEndian;
import java.io.IOException;
import locus.api.objects.Storable;

public class GeocachingTrackable extends Storable
{
    private static final String TAG;
    private String mCurrentOwner;
    private String mDetails;
    private String mGoal;
    private long mId;
    private String mImgUrl;
    private String mName;
    private String mOrigin;
    private String mOriginalOwner;
    private long mReleased;
    private String mSrcDetails;
    
    static {
        TAG = GeocachingTrackable.class.getSimpleName();
    }
    
    public GeocachingTrackable() {
    }
    
    public GeocachingTrackable(final byte[] array) throws IOException {
        super(array);
    }
    
    public String getCurrentOwner() {
        return this.mCurrentOwner;
    }
    
    public String getDetails() {
        return this.mDetails;
    }
    
    public String getGoal() {
        return this.mGoal;
    }
    
    public long getId() {
        return this.mId;
    }
    
    public String getImgUrl() {
        return this.mImgUrl;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getOrigin() {
        return this.mOrigin;
    }
    
    public String getOriginalOwner() {
        return this.mOriginalOwner;
    }
    
    public long getReleased() {
        return this.mReleased;
    }
    
    public String getSrcDetails() {
        return this.mSrcDetails;
    }
    
    public String getTbCode() {
        String s;
        if (this.mSrcDetails == null || this.mSrcDetails.length() == 0) {
            s = "";
        }
        else if (this.mSrcDetails.startsWith("http://www.geocaching.com/track/details.aspx?tracker=")) {
            s = this.mSrcDetails.substring("http://www.geocaching.com/track/details.aspx?tracker=".length());
        }
        else if (this.mSrcDetails.startsWith("http://coord.info/")) {
            s = this.mSrcDetails.substring("http://coord.info/".length());
        }
        else {
            s = "";
        }
        return s;
    }
    
    @Override
    protected int getVersion() {
        return 1;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mName = dataReaderBigEndian.readString();
        this.mImgUrl = dataReaderBigEndian.readString();
        this.mSrcDetails = dataReaderBigEndian.readString();
        this.mOriginalOwner = dataReaderBigEndian.readString();
        this.mReleased = dataReaderBigEndian.readLong();
        this.mOrigin = dataReaderBigEndian.readString();
        this.mGoal = dataReaderBigEndian.readString();
        this.mDetails = dataReaderBigEndian.readString();
        if (n >= 1) {
            this.mId = dataReaderBigEndian.readLong();
            this.mCurrentOwner = dataReaderBigEndian.readString();
        }
    }
    
    @Override
    public void reset() {
        this.mName = "";
        this.mImgUrl = "";
        this.mSrcDetails = "";
        this.mOriginalOwner = "";
        this.mReleased = 0L;
        this.mOrigin = "";
        this.mGoal = "";
        this.mDetails = "";
        this.mId = 0L;
        this.mCurrentOwner = "";
    }
    
    public void setCurrentOwner(final String s) {
        String mCurrentOwner = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setCurrentOwner(), empty parameter");
            mCurrentOwner = "";
        }
        this.mCurrentOwner = mCurrentOwner;
    }
    
    public void setDetails(final String s) {
        String mDetails = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setDetails(), empty parameter");
            mDetails = "";
        }
        this.mDetails = mDetails;
    }
    
    public void setGoal(final String s) {
        String mGoal = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setGoal(), empty parameter");
            mGoal = "";
        }
        this.mGoal = mGoal;
    }
    
    public void setId(final long mId) {
        this.mId = mId;
    }
    
    public void setImgUrl(final String s) {
        String mImgUrl = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setImgUrl(), empty parameter");
            mImgUrl = "";
        }
        this.mImgUrl = mImgUrl;
    }
    
    public void setName(final String s) {
        String mName = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setName(), empty parameter");
            mName = "";
        }
        this.mName = mName;
    }
    
    public void setOrigin(final String s) {
        String mOrigin = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setOrigin(), empty parameter");
            mOrigin = "";
        }
        this.mOrigin = mOrigin;
    }
    
    public void setOriginalOwner(final String s) {
        String mOriginalOwner = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setOriginalOwner(), empty parameter");
            mOriginalOwner = "";
        }
        this.mOriginalOwner = mOriginalOwner;
    }
    
    public void setReleased(final long mReleased) {
        this.mReleased = mReleased;
    }
    
    public void setSrcDetails(final String s) {
        String mSrcDetails = s;
        if (s == null) {
            Logger.logD(GeocachingTrackable.TAG, "setSrcDetails(), empty parameter");
            mSrcDetails = "";
        }
        this.mSrcDetails = mSrcDetails;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mName);
        dataWriterBigEndian.writeString(this.mImgUrl);
        dataWriterBigEndian.writeString(this.mSrcDetails);
        dataWriterBigEndian.writeString(this.mOriginalOwner);
        dataWriterBigEndian.writeLong(this.mReleased);
        dataWriterBigEndian.writeString(this.mOrigin);
        dataWriterBigEndian.writeString(this.mGoal);
        dataWriterBigEndian.writeString(this.mDetails);
        dataWriterBigEndian.writeLong(this.mId);
        dataWriterBigEndian.writeString(this.mCurrentOwner);
    }
}
