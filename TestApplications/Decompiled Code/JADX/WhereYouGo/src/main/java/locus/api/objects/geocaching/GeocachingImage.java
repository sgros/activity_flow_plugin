package locus.api.objects.geocaching;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeocachingImage extends Storable {
    private static final String TAG = "GeocachingImage";
    private String mDescription;
    private String mName;
    private String mThumbUrl;
    private String mUrl;

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

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        if (description == null) {
            Logger.logD(TAG, "setDescription(), empty parameter");
            description = "";
        }
        this.mDescription = description;
    }

    public String getThumbUrl() {
        return this.mThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        if (thumbUrl == null) {
            Logger.logD(TAG, "setThumbUrl(), empty parameter");
            thumbUrl = "";
        }
        this.mThumbUrl = thumbUrl;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        if (url == null) {
            Logger.logD(TAG, "setUrl(), empty parameter");
            url = "";
        }
        this.mUrl = url;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mName = dr.readString();
        this.mDescription = dr.readString();
        this.mThumbUrl = dr.readString();
        this.mUrl = dr.readString();
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mName);
        dw.writeString(this.mDescription);
        dw.writeString(this.mThumbUrl);
        dw.writeString(this.mUrl);
    }

    public void reset() {
        this.mName = "";
        this.mDescription = "";
        this.mThumbUrl = "";
        this.mUrl = "";
    }
}
