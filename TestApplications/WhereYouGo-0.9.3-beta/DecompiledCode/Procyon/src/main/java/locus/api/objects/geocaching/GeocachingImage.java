// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.geocaching;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.Storable;

public class GeocachingImage extends Storable
{
    private static final String TAG = "GeocachingImage";
    private String mDescription;
    private String mName;
    private String mThumbUrl;
    private String mUrl;
    
    public String getDescription() {
        return this.mDescription;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getThumbUrl() {
        return this.mThumbUrl;
    }
    
    public String getUrl() {
        return this.mUrl;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mName = dataReaderBigEndian.readString();
        this.mDescription = dataReaderBigEndian.readString();
        this.mThumbUrl = dataReaderBigEndian.readString();
        this.mUrl = dataReaderBigEndian.readString();
    }
    
    @Override
    public void reset() {
        this.mName = "";
        this.mDescription = "";
        this.mThumbUrl = "";
        this.mUrl = "";
    }
    
    public void setDescription(final String s) {
        String mDescription = s;
        if (s == null) {
            Logger.logD("GeocachingImage", "setDescription(), empty parameter");
            mDescription = "";
        }
        this.mDescription = mDescription;
    }
    
    public void setName(final String s) {
        String mName = s;
        if (s == null) {
            Logger.logD("GeocachingImage", "setName(), empty parameter");
            mName = "";
        }
        this.mName = mName;
    }
    
    public void setThumbUrl(final String s) {
        String mThumbUrl = s;
        if (s == null) {
            Logger.logD("GeocachingImage", "setThumbUrl(), empty parameter");
            mThumbUrl = "";
        }
        this.mThumbUrl = mThumbUrl;
    }
    
    public void setUrl(final String s) {
        String mUrl = s;
        if (s == null) {
            Logger.logD("GeocachingImage", "setUrl(), empty parameter");
            mUrl = "";
        }
        this.mUrl = mUrl;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mName);
        dataWriterBigEndian.writeString(this.mDescription);
        dataWriterBigEndian.writeString(this.mThumbUrl);
        dataWriterBigEndian.writeString(this.mUrl);
    }
}
