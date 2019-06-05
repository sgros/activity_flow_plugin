// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.mapProvider.data;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.DataReaderBigEndian;
import java.io.IOException;
import locus.api.objects.Storable;

public class MapTileRequest extends Storable
{
    private double mMapSystemX1;
    private double mMapSystemX2;
    private double mMapSystemY1;
    private double mMapSystemY2;
    private int mTileX;
    private int mTileY;
    private int mTileZoom;
    
    public MapTileRequest() {
    }
    
    public MapTileRequest(final byte[] array) throws IOException {
        super(array);
    }
    
    public double getMapSystemX1() {
        return this.mMapSystemX1;
    }
    
    public double getMapSystemX2() {
        return this.mMapSystemX2;
    }
    
    public double getMapSystemY1() {
        return this.mMapSystemY1;
    }
    
    public double getMapSystemY2() {
        return this.mMapSystemY2;
    }
    
    public int getTileX() {
        return this.mTileX;
    }
    
    public int getTileY() {
        return this.mTileY;
    }
    
    public int getTileZoom() {
        return this.mTileZoom;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mTileX = dataReaderBigEndian.readInt();
        this.mTileY = dataReaderBigEndian.readInt();
        this.mTileZoom = dataReaderBigEndian.readInt();
        this.mMapSystemX1 = dataReaderBigEndian.readDouble();
        this.mMapSystemY1 = dataReaderBigEndian.readDouble();
        this.mMapSystemX2 = dataReaderBigEndian.readDouble();
        this.mMapSystemY2 = dataReaderBigEndian.readDouble();
    }
    
    @Override
    public void reset() {
        this.mTileX = -1;
        this.mTileY = -1;
        this.mTileZoom = -1;
        this.mMapSystemX1 = 0.0;
        this.mMapSystemY1 = 0.0;
        this.mMapSystemX2 = 0.0;
        this.mMapSystemY2 = 0.0;
    }
    
    public void setMapSystemX1(final double mMapSystemX1) {
        this.mMapSystemX1 = mMapSystemX1;
    }
    
    public void setMapSystemX2(final double mMapSystemX2) {
        this.mMapSystemX2 = mMapSystemX2;
    }
    
    public void setMapSystemY1(final double mMapSystemY1) {
        this.mMapSystemY1 = mMapSystemY1;
    }
    
    public void setMapSystemY2(final double mMapSystemY2) {
        this.mMapSystemY2 = mMapSystemY2;
    }
    
    public void setTileX(final int mTileX) {
        this.mTileX = mTileX;
    }
    
    public void setTileY(final int mTileY) {
        this.mTileY = mTileY;
    }
    
    public void setTileZoom(final int mTileZoom) {
        this.mTileZoom = mTileZoom;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.mTileX);
        dataWriterBigEndian.writeInt(this.mTileY);
        dataWriterBigEndian.writeInt(this.mTileZoom);
        dataWriterBigEndian.writeDouble(this.mMapSystemX1);
        dataWriterBigEndian.writeDouble(this.mMapSystemY1);
        dataWriterBigEndian.writeDouble(this.mMapSystemX2);
        dataWriterBigEndian.writeDouble(this.mMapSystemY2);
    }
}
