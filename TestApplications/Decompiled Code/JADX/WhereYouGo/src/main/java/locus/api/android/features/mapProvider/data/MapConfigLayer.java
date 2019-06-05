package locus.api.android.features.mapProvider.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class MapConfigLayer extends Storable {
    private List<CalibrationPoint> mCalPoints;
    private String mDescription;
    private String mName;
    private int mProjEpsg;
    private int mTileSizeX;
    private int mTileSizeY;
    private long mXmax;
    private long mYmax;
    private int mZoom;

    public static class CalibrationPoint {
        public double lat;
        public double lon;
        /* renamed from: x */
        public double f42x;
        /* renamed from: y */
        public double f43y;

        public CalibrationPoint() {
            this.f42x = 0.0d;
            this.f43y = 0.0d;
            this.lat = 0.0d;
            this.lon = 0.0d;
        }

        public CalibrationPoint(double x, double y, double lat, double lon) {
            this();
            this.f42x = x;
            this.f43y = y;
            this.lat = lat;
            this.lon = lon;
        }
    }

    public MapConfigLayer(byte[] data) throws IOException {
        super(data);
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getTileSizeX() {
        return this.mTileSizeX;
    }

    public void setTileSizeX(int tileSizeX) {
        this.mTileSizeX = tileSizeX;
    }

    public int getTileSizeY() {
        return this.mTileSizeY;
    }

    public void setTileSizeY(int tileSizeY) {
        this.mTileSizeY = tileSizeY;
    }

    public long getXmax() {
        return this.mXmax;
    }

    public void setXmax(long xmax) {
        this.mXmax = xmax;
    }

    public long getYmax() {
        return this.mYmax;
    }

    public void setYmax(long ymax) {
        this.mYmax = ymax;
    }

    public int getZoom() {
        return this.mZoom;
    }

    public void setZoom(int zoom) {
        this.mZoom = zoom;
    }

    public int getProjEpsg() {
        return this.mProjEpsg;
    }

    public void setProjEpsg(int projEpsg) {
        this.mProjEpsg = projEpsg;
    }

    public void addCalibrationPoint(double x, double y, double lat, double lon) {
        addCalibrationPoint(new CalibrationPoint(x, y, lat, lon));
    }

    public void addCalibrationPoint(CalibrationPoint cp) {
        this.mCalPoints.add(cp);
    }

    public List<CalibrationPoint> getCalibrationPoints() {
        return this.mCalPoints;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    public void reset() {
        this.mName = "";
        this.mDescription = "";
        this.mTileSizeX = 0;
        this.mTileSizeY = 0;
        this.mXmax = 0;
        this.mYmax = 0;
        this.mZoom = -1;
        this.mProjEpsg = 0;
        this.mCalPoints = new ArrayList();
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mName = dr.readString();
        this.mDescription = dr.readString();
        this.mTileSizeX = dr.readInt();
        this.mTileSizeY = dr.readInt();
        this.mXmax = dr.readLong();
        this.mYmax = dr.readLong();
        this.mZoom = dr.readInt();
        this.mProjEpsg = dr.readInt();
        int count = dr.readInt();
        for (int i = 0; i < count; i++) {
            addCalibrationPoint(dr.readDouble(), dr.readDouble(), dr.readDouble(), dr.readDouble());
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mName);
        dw.writeString(this.mDescription);
        dw.writeInt(this.mTileSizeX);
        dw.writeInt(this.mTileSizeY);
        dw.writeLong(this.mXmax);
        dw.writeLong(this.mYmax);
        dw.writeInt(this.mZoom);
        dw.writeInt(this.mProjEpsg);
        dw.writeInt(this.mCalPoints.size());
        for (int i = 0; i < this.mCalPoints.size(); i++) {
            CalibrationPoint cal = (CalibrationPoint) this.mCalPoints.get(i);
            dw.writeDouble(cal.f42x);
            dw.writeDouble(cal.f43y);
            dw.writeDouble(cal.lat);
            dw.writeDouble(cal.lon);
        }
    }
}
