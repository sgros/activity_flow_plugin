package locus.api.android.objects;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.utils.UtilsBitmap;
import locus.api.objects.Storable;
import locus.api.objects.extra.ExtraStyle;
import locus.api.objects.extra.Waypoint;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class PackWaypoints extends Storable {
    private Bitmap mBitmap;
    private String mName;
    private ExtraStyle mStyle;
    private List<Waypoint> mWpts;

    public PackWaypoints() {
        this("");
    }

    public PackWaypoints(String uniqueName) {
        this.mName = uniqueName;
    }

    public PackWaypoints(byte[] data) throws IOException {
        super(data);
    }

    public String getName() {
        return this.mName;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public ExtraStyle getExtraStyle() {
        return this.mStyle;
    }

    public void setExtraStyle(ExtraStyle extraStyle) {
        this.mStyle = extraStyle;
    }

    public void addWaypoint(Waypoint wpt) {
        this.mWpts.add(wpt);
    }

    public List<Waypoint> getWaypoints() {
        return this.mWpts;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mName = dr.readString();
        if (dr.readBoolean()) {
            this.mStyle = new ExtraStyle(dr);
        }
        this.mBitmap = UtilsBitmap.readBitmap(dr);
        this.mWpts = dr.readListStorable(Waypoint.class);
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mName);
        if (this.mStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            dw.writeStorable(this.mStyle);
        }
        UtilsBitmap.writeBitmap(dw, this.mBitmap, CompressFormat.PNG);
        dw.writeListStorable(this.mWpts);
    }

    public void reset() {
        this.mName = null;
        this.mStyle = null;
        this.mWpts = new ArrayList();
    }
}
