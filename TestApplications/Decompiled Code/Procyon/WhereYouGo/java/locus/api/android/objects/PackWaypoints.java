// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.objects;

import android.graphics.Bitmap$CompressFormat;
import locus.api.utils.DataWriterBigEndian;
import java.util.ArrayList;
import locus.api.android.utils.UtilsBitmap;
import locus.api.utils.DataReaderBigEndian;
import java.io.IOException;
import locus.api.objects.extra.Waypoint;
import java.util.List;
import locus.api.objects.extra.ExtraStyle;
import android.graphics.Bitmap;
import locus.api.objects.Storable;

public class PackWaypoints extends Storable
{
    private Bitmap mBitmap;
    private String mName;
    private ExtraStyle mStyle;
    private List<Waypoint> mWpts;
    
    public PackWaypoints() {
        this("");
    }
    
    public PackWaypoints(final String mName) {
        this.mName = mName;
    }
    
    public PackWaypoints(final byte[] array) throws IOException {
        super(array);
    }
    
    public void addWaypoint(final Waypoint waypoint) {
        this.mWpts.add(waypoint);
    }
    
    public Bitmap getBitmap() {
        return this.mBitmap;
    }
    
    public ExtraStyle getExtraStyle() {
        return this.mStyle;
    }
    
    public String getName() {
        return this.mName;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    public List<Waypoint> getWaypoints() {
        return this.mWpts;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mName = dataReaderBigEndian.readString();
        if (dataReaderBigEndian.readBoolean()) {
            this.mStyle = new ExtraStyle(dataReaderBigEndian);
        }
        this.mBitmap = UtilsBitmap.readBitmap(dataReaderBigEndian);
        this.mWpts = (List<Waypoint>)dataReaderBigEndian.readListStorable(Waypoint.class);
    }
    
    @Override
    public void reset() {
        this.mName = null;
        this.mStyle = null;
        this.mWpts = new ArrayList<Waypoint>();
    }
    
    public void setBitmap(final Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
    
    public void setExtraStyle(final ExtraStyle mStyle) {
        this.mStyle = mStyle;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mName);
        if (this.mStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeStorable(this.mStyle);
        }
        UtilsBitmap.writeBitmap(dataWriterBigEndian, this.mBitmap, Bitmap$CompressFormat.PNG);
        dataWriterBigEndian.writeListStorable(this.mWpts);
    }
}
