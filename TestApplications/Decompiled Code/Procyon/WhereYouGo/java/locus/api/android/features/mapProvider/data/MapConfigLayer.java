// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.mapProvider.data;

import locus.api.utils.DataWriterBigEndian;
import java.util.ArrayList;
import locus.api.utils.DataReaderBigEndian;
import java.io.IOException;
import java.util.List;
import locus.api.objects.Storable;

public class MapConfigLayer extends Storable
{
    private List<CalibrationPoint> mCalPoints;
    private String mDescription;
    private String mName;
    private int mProjEpsg;
    private int mTileSizeX;
    private int mTileSizeY;
    private long mXmax;
    private long mYmax;
    private int mZoom;
    
    public MapConfigLayer() {
    }
    
    public MapConfigLayer(final byte[] array) throws IOException {
        super(array);
    }
    
    public void addCalibrationPoint(final double n, final double n2, final double n3, final double n4) {
        this.addCalibrationPoint(new CalibrationPoint(n, n2, n3, n4));
    }
    
    public void addCalibrationPoint(final CalibrationPoint calibrationPoint) {
        this.mCalPoints.add(calibrationPoint);
    }
    
    public List<CalibrationPoint> getCalibrationPoints() {
        return this.mCalPoints;
    }
    
    public String getDescription() {
        return this.mDescription;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public int getProjEpsg() {
        return this.mProjEpsg;
    }
    
    public int getTileSizeX() {
        return this.mTileSizeX;
    }
    
    public int getTileSizeY() {
        return this.mTileSizeY;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    public long getXmax() {
        return this.mXmax;
    }
    
    public long getYmax() {
        return this.mYmax;
    }
    
    public int getZoom() {
        return this.mZoom;
    }
    
    @Override
    protected void readObject(int i, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mName = dataReaderBigEndian.readString();
        this.mDescription = dataReaderBigEndian.readString();
        this.mTileSizeX = dataReaderBigEndian.readInt();
        this.mTileSizeY = dataReaderBigEndian.readInt();
        this.mXmax = dataReaderBigEndian.readLong();
        this.mYmax = dataReaderBigEndian.readLong();
        this.mZoom = dataReaderBigEndian.readInt();
        this.mProjEpsg = dataReaderBigEndian.readInt();
        int int1;
        for (int1 = dataReaderBigEndian.readInt(), i = 0; i < int1; ++i) {
            this.addCalibrationPoint(dataReaderBigEndian.readDouble(), dataReaderBigEndian.readDouble(), dataReaderBigEndian.readDouble(), dataReaderBigEndian.readDouble());
        }
    }
    
    @Override
    public void reset() {
        this.mName = "";
        this.mDescription = "";
        this.mTileSizeX = 0;
        this.mTileSizeY = 0;
        this.mXmax = 0L;
        this.mYmax = 0L;
        this.mZoom = -1;
        this.mProjEpsg = 0;
        this.mCalPoints = new ArrayList<CalibrationPoint>();
    }
    
    public void setDescription(final String mDescription) {
        this.mDescription = mDescription;
    }
    
    public void setName(final String mName) {
        this.mName = mName;
    }
    
    public void setProjEpsg(final int mProjEpsg) {
        this.mProjEpsg = mProjEpsg;
    }
    
    public void setTileSizeX(final int mTileSizeX) {
        this.mTileSizeX = mTileSizeX;
    }
    
    public void setTileSizeY(final int mTileSizeY) {
        this.mTileSizeY = mTileSizeY;
    }
    
    public void setXmax(final long mXmax) {
        this.mXmax = mXmax;
    }
    
    public void setYmax(final long mYmax) {
        this.mYmax = mYmax;
    }
    
    public void setZoom(final int mZoom) {
        this.mZoom = mZoom;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mName);
        dataWriterBigEndian.writeString(this.mDescription);
        dataWriterBigEndian.writeInt(this.mTileSizeX);
        dataWriterBigEndian.writeInt(this.mTileSizeY);
        dataWriterBigEndian.writeLong(this.mXmax);
        dataWriterBigEndian.writeLong(this.mYmax);
        dataWriterBigEndian.writeInt(this.mZoom);
        dataWriterBigEndian.writeInt(this.mProjEpsg);
        dataWriterBigEndian.writeInt(this.mCalPoints.size());
        for (int i = 0; i < this.mCalPoints.size(); ++i) {
            final CalibrationPoint calibrationPoint = this.mCalPoints.get(i);
            dataWriterBigEndian.writeDouble(calibrationPoint.x);
            dataWriterBigEndian.writeDouble(calibrationPoint.y);
            dataWriterBigEndian.writeDouble(calibrationPoint.lat);
            dataWriterBigEndian.writeDouble(calibrationPoint.lon);
        }
    }
    
    public static class CalibrationPoint
    {
        public double lat;
        public double lon;
        public double x;
        public double y;
        
        public CalibrationPoint() {
            this.x = 0.0;
            this.y = 0.0;
            this.lat = 0.0;
            this.lon = 0.0;
        }
        
        public CalibrationPoint(final double x, final double y, final double lat, final double lon) {
            this();
            this.x = x;
            this.y = y;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
