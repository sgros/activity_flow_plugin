package locus.api.objects.geocaching;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingTrackable extends Storable {
    private static final String TAG = GeocachingTrackable.class.getSimpleName();
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

    public GeocachingTrackable(byte[] data) throws IOException {
        super(data);
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
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

    public String getImgUrl() {
        return this.mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        if (imgUrl == null) {
            Logger.logD(TAG, "setImgUrl(), empty parameter");
            imgUrl = "";
        }
        this.mImgUrl = imgUrl;
    }

    public String getSrcDetails() {
        return this.mSrcDetails;
    }

    public void setSrcDetails(String srcDetails) {
        if (srcDetails == null) {
            Logger.logD(TAG, "setSrcDetails(), empty parameter");
            srcDetails = "";
        }
        this.mSrcDetails = srcDetails;
    }

    public String getOriginalOwner() {
        return this.mOriginalOwner;
    }

    public void setOriginalOwner(String originalOwner) {
        if (originalOwner == null) {
            Logger.logD(TAG, "setOriginalOwner(), empty parameter");
            originalOwner = "";
        }
        this.mOriginalOwner = originalOwner;
    }

    public String getCurrentOwner() {
        return this.mCurrentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        if (currentOwner == null) {
            Logger.logD(TAG, "setCurrentOwner(), empty parameter");
            currentOwner = "";
        }
        this.mCurrentOwner = currentOwner;
    }

    public long getReleased() {
        return this.mReleased;
    }

    public void setReleased(long released) {
        this.mReleased = released;
    }

    public String getOrigin() {
        return this.mOrigin;
    }

    public void setOrigin(String origin) {
        if (origin == null) {
            Logger.logD(TAG, "setOrigin(), empty parameter");
            origin = "";
        }
        this.mOrigin = origin;
    }

    public String getGoal() {
        return this.mGoal;
    }

    public void setGoal(String goal) {
        if (goal == null) {
            Logger.logD(TAG, "setGoal(), empty parameter");
            goal = "";
        }
        this.mGoal = goal;
    }

    public String getDetails() {
        return this.mDetails;
    }

    public void setDetails(String details) {
        if (details == null) {
            Logger.logD(TAG, "setDetails(), empty parameter");
            details = "";
        }
        this.mDetails = details;
    }

    public String getTbCode() {
        if (this.mSrcDetails == null || this.mSrcDetails.length() == 0) {
            return "";
        }
        if (this.mSrcDetails.startsWith("http://www.geocaching.com/track/details.aspx?tracker=")) {
            return this.mSrcDetails.substring("http://www.geocaching.com/track/details.aspx?tracker=".length());
        }
        if (this.mSrcDetails.startsWith("http://coord.info/")) {
            return this.mSrcDetails.substring("http://coord.info/".length());
        }
        return "";
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 1;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mName = dr.readString();
        this.mImgUrl = dr.readString();
        this.mSrcDetails = dr.readString();
        this.mOriginalOwner = dr.readString();
        this.mReleased = dr.readLong();
        this.mOrigin = dr.readString();
        this.mGoal = dr.readString();
        this.mDetails = dr.readString();
        if (version >= 1) {
            this.mId = dr.readLong();
            this.mCurrentOwner = dr.readString();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mName);
        dw.writeString(this.mImgUrl);
        dw.writeString(this.mSrcDetails);
        dw.writeString(this.mOriginalOwner);
        dw.writeLong(this.mReleased);
        dw.writeString(this.mOrigin);
        dw.writeString(this.mGoal);
        dw.writeString(this.mDetails);
        dw.writeLong(this.mId);
        dw.writeString(this.mCurrentOwner);
    }

    public void reset() {
        this.mName = "";
        this.mImgUrl = "";
        this.mSrcDetails = "";
        this.mOriginalOwner = "";
        this.mReleased = 0;
        this.mOrigin = "";
        this.mGoal = "";
        this.mDetails = "";
        this.mId = 0;
        this.mCurrentOwner = "";
    }
}
