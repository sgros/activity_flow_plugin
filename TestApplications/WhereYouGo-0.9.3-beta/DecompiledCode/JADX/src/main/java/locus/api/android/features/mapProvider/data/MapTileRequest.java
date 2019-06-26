package locus.api.android.features.mapProvider.data;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class MapTileRequest extends Storable {
    private double mMapSystemX1;
    private double mMapSystemX2;
    private double mMapSystemY1;
    private double mMapSystemY2;
    private int mTileX;
    private int mTileY;
    private int mTileZoom;

    public MapTileRequest(byte[] data) throws IOException {
        super(data);
    }

    public int getTileX() {
        return this.mTileX;
    }

    public void setTileX(int tileX) {
        this.mTileX = tileX;
    }

    public int getTileY() {
        return this.mTileY;
    }

    public void setTileY(int tileY) {
        this.mTileY = tileY;
    }

    public int getTileZoom() {
        return this.mTileZoom;
    }

    public void setTileZoom(int tileZoom) {
        this.mTileZoom = tileZoom;
    }

    public double getMapSystemX1() {
        return this.mMapSystemX1;
    }

    public void setMapSystemX1(double mapSystemX1) {
        this.mMapSystemX1 = mapSystemX1;
    }

    public double getMapSystemY1() {
        return this.mMapSystemY1;
    }

    public void setMapSystemY1(double mapSystemY1) {
        this.mMapSystemY1 = mapSystemY1;
    }

    public double getMapSystemX2() {
        return this.mMapSystemX2;
    }

    public void setMapSystemX2(double mapSystemX2) {
        this.mMapSystemX2 = mapSystemX2;
    }

    public double getMapSystemY2() {
        return this.mMapSystemY2;
    }

    public void setMapSystemY2(double mapSystemY2) {
        this.mMapSystemY2 = mapSystemY2;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    public void reset() {
        this.mTileX = -1;
        this.mTileY = -1;
        this.mTileZoom = -1;
        this.mMapSystemX1 = 0.0d;
        this.mMapSystemY1 = 0.0d;
        this.mMapSystemX2 = 0.0d;
        this.mMapSystemY2 = 0.0d;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mTileX = dr.readInt();
        this.mTileY = dr.readInt();
        this.mTileZoom = dr.readInt();
        this.mMapSystemX1 = dr.readDouble();
        this.mMapSystemY1 = dr.readDouble();
        this.mMapSystemX2 = dr.readDouble();
        this.mMapSystemY2 = dr.readDouble();
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(this.mTileX);
        dw.writeInt(this.mTileY);
        dw.writeInt(this.mTileZoom);
        dw.writeDouble(this.mMapSystemX1);
        dw.writeDouble(this.mMapSystemY1);
        dw.writeDouble(this.mMapSystemX2);
        dw.writeDouble(this.mMapSystemY2);
    }
}
