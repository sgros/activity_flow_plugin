// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Utils;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import android.database.MatrixCursor;
import android.database.Cursor;
import locus.api.objects.Storable;

public class LocusInfo extends Storable
{
    private static final String VALUE_GEOCACHING_OWNER_NAME = "gcOwnerName";
    private static final String VALUE_IS_RUNNING = "isRunning";
    private static final String VALUE_PACKAGE_NAME = "packageName";
    private static final String VALUE_PERIODIC_UPDATES = "periodicUpdates";
    private static final String VALUE_ROOT_DIR = "rootDir";
    private static final String VALUE_ROOT_DIR_BACKUP = "rootDirBackup";
    private static final String VALUE_ROOT_DIR_EXPORT = "rootDirExport";
    private static final String VALUE_ROOT_DIR_GEOCACHING = "rootDirGeocaching";
    private static final String VALUE_ROOT_DIR_MAPS_ONLINE = "rootDirMapsOnline";
    private static final String VALUE_ROOT_DIR_MAPS_PERSONAL = "rootDirMapsPersonal";
    private static final String VALUE_ROOT_DIR_MAPS_VECTOR = "rootDirMapsVector";
    private static final String VALUE_ROOT_DIR_MAP_ITEMS = "rootDirMapItems";
    private static final String VALUE_ROOT_DIR_SRTM = "rootDirSrtm";
    private static final String VALUE_UNITS_FORMAT_ALTITUDE = "unitsFormatAltitude";
    private static final String VALUE_UNITS_FORMAT_ANGLE = "unitsFormatAngle";
    private static final String VALUE_UNITS_FORMAT_AREA = "unitsFormatArea";
    private static final String VALUE_UNITS_FORMAT_ENERGY = "unitsFormatEnergy";
    private static final String VALUE_UNITS_FORMAT_LENGTH = "unitsFormatLength";
    private static final String VALUE_UNITS_FORMAT_SLOPE = "unitsFormatSlope";
    private static final String VALUE_UNITS_FORMAT_SPEED = "unitsFormatSpeed";
    private static final String VALUE_UNITS_FORMAT_TEMPERATURE = "unitsFormatTemperature";
    private static final String VALUE_UNITS_FORMAT_WEIGHT = "unitsFormatWeight";
    private String mGcOwnerName;
    private boolean mIsRunning;
    private String mPackageName;
    private boolean mPeriodicUpdatesEnabled;
    private String mRootDir;
    private String mRootDirBackup;
    private String mRootDirExport;
    private String mRootDirGeocaching;
    private String mRootDirMapItems;
    private String mRootDirMapsOnline;
    private String mRootDirMapsPersonal;
    private String mRootDirMapsVector;
    private String mRootDirSrtm;
    private int mUnitsFormatAltitude;
    private int mUnitsFormatAngle;
    private int mUnitsFormatArea;
    private int mUnitsFormatEnergy;
    private int mUnitsFormatLength;
    private int mUnitsFormatSlope;
    private int mUnitsFormatSpeed;
    private int mUnitsFormatTemperature;
    private int mUnitsFormatWeight;
    
    protected LocusInfo() {
    }
    
    public static LocusInfo create(final Cursor cursor) {
        LocusInfo locusInfo;
        if (cursor == null || cursor.getCount() == 0) {
            locusInfo = null;
        }
        else {
            final LocusInfo locusInfo2 = new LocusInfo();
            int n = 0;
            while (true) {
                locusInfo = locusInfo2;
                if (n >= cursor.getCount()) {
                    break;
                }
                cursor.moveToPosition(n);
                final String string = cursor.getString(0);
                switch (string) {
                    case "packageName": {
                        locusInfo2.mPackageName = cursor.getString(1);
                        break;
                    }
                    case "isRunning": {
                        locusInfo2.mIsRunning = (cursor.getInt(1) == 1);
                        break;
                    }
                    case "rootDir": {
                        locusInfo2.mRootDir = cursor.getString(1);
                        break;
                    }
                    case "rootDirBackup": {
                        locusInfo2.mRootDirBackup = cursor.getString(1);
                        break;
                    }
                    case "rootDirExport": {
                        locusInfo2.mRootDirExport = cursor.getString(1);
                        break;
                    }
                    case "rootDirGeocaching": {
                        locusInfo2.mRootDirGeocaching = cursor.getString(1);
                        break;
                    }
                    case "rootDirMapItems": {
                        locusInfo2.mRootDirMapItems = cursor.getString(1);
                        break;
                    }
                    case "rootDirMapsOnline": {
                        locusInfo2.mRootDirMapsOnline = cursor.getString(1);
                        break;
                    }
                    case "rootDirMapsPersonal": {
                        locusInfo2.mRootDirMapsPersonal = cursor.getString(1);
                        break;
                    }
                    case "rootDirMapsVector": {
                        locusInfo2.mRootDirMapsVector = cursor.getString(1);
                        break;
                    }
                    case "rootDirSrtm": {
                        locusInfo2.mRootDirSrtm = cursor.getString(1);
                        break;
                    }
                    case "periodicUpdates": {
                        locusInfo2.mPeriodicUpdatesEnabled = (cursor.getInt(1) == 1);
                        break;
                    }
                    case "gcOwnerName": {
                        locusInfo2.mGcOwnerName = cursor.getString(1);
                        break;
                    }
                    case "unitsFormatAltitude": {
                        locusInfo2.mUnitsFormatAltitude = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatAngle": {
                        locusInfo2.mUnitsFormatAngle = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatArea": {
                        locusInfo2.mUnitsFormatArea = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatEnergy": {
                        locusInfo2.mUnitsFormatEnergy = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatLength": {
                        locusInfo2.mUnitsFormatLength = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatSlope": {
                        locusInfo2.mUnitsFormatSlope = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatSpeed": {
                        locusInfo2.mUnitsFormatSpeed = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatTemperature": {
                        locusInfo2.mUnitsFormatTemperature = cursor.getInt(1);
                        break;
                    }
                    case "unitsFormatWeight": {
                        locusInfo2.mUnitsFormatWeight = cursor.getInt(1);
                        break;
                    }
                }
                ++n;
            }
        }
        return locusInfo;
    }
    
    protected Cursor create() {
        final MatrixCursor matrixCursor = new MatrixCursor(new String[] { "key", "value" });
        matrixCursor.addRow(new Object[] { "packageName", this.mPackageName });
        String s;
        if (this.mIsRunning) {
            s = "1";
        }
        else {
            s = "0";
        }
        matrixCursor.addRow(new Object[] { "isRunning", s });
        matrixCursor.addRow(new Object[] { "rootDir", this.mRootDir });
        matrixCursor.addRow(new Object[] { "rootDirBackup", this.mRootDirBackup });
        matrixCursor.addRow(new Object[] { "rootDirExport", this.mRootDirExport });
        matrixCursor.addRow(new Object[] { "rootDirGeocaching", this.mRootDirGeocaching });
        matrixCursor.addRow(new Object[] { "rootDirMapItems", this.mRootDirMapItems });
        matrixCursor.addRow(new Object[] { "rootDirMapsOnline", this.mRootDirMapsOnline });
        matrixCursor.addRow(new Object[] { "rootDirMapsPersonal", this.mRootDirMapsPersonal });
        matrixCursor.addRow(new Object[] { "rootDirMapsVector", this.mRootDirMapsVector });
        matrixCursor.addRow(new Object[] { "rootDirSrtm", this.mRootDirSrtm });
        String s2;
        if (this.mPeriodicUpdatesEnabled) {
            s2 = "1";
        }
        else {
            s2 = "0";
        }
        matrixCursor.addRow(new Object[] { "periodicUpdates", s2 });
        matrixCursor.addRow(new Object[] { "gcOwnerName", this.mGcOwnerName });
        matrixCursor.addRow(new Object[] { "unitsFormatAltitude", this.mUnitsFormatAltitude });
        matrixCursor.addRow(new Object[] { "unitsFormatAngle", this.mUnitsFormatAngle });
        matrixCursor.addRow(new Object[] { "unitsFormatArea", this.mUnitsFormatArea });
        matrixCursor.addRow(new Object[] { "unitsFormatEnergy", this.mUnitsFormatEnergy });
        matrixCursor.addRow(new Object[] { "unitsFormatLength", this.mUnitsFormatLength });
        matrixCursor.addRow(new Object[] { "unitsFormatSlope", this.mUnitsFormatSlope });
        matrixCursor.addRow(new Object[] { "unitsFormatSpeed", this.mUnitsFormatSpeed });
        matrixCursor.addRow(new Object[] { "unitsFormatTemperature", this.mUnitsFormatTemperature });
        matrixCursor.addRow(new Object[] { "unitsFormatWeight", this.mUnitsFormatWeight });
        return (Cursor)matrixCursor;
    }
    
    public String getGcOwnerName() {
        return this.mGcOwnerName;
    }
    
    public String getPackageName() {
        return this.mPackageName;
    }
    
    public String getRootDirBackup() {
        return this.mRootDirBackup;
    }
    
    public String getRootDirExport() {
        return this.mRootDirExport;
    }
    
    public String getRootDirGeocaching() {
        return this.mRootDirGeocaching;
    }
    
    public String getRootDirMapItems() {
        return this.mRootDirMapItems;
    }
    
    public String getRootDirMapsOnline() {
        return this.mRootDirMapsOnline;
    }
    
    public String getRootDirMapsPersonal() {
        return this.mRootDirMapsPersonal;
    }
    
    public String getRootDirMapsVector() {
        return this.mRootDirMapsVector;
    }
    
    public String getRootDirSrtm() {
        return this.mRootDirSrtm;
    }
    
    public String getRootDirectory() {
        return this.mRootDir;
    }
    
    public int getUnitsFormatAltitude() {
        return this.mUnitsFormatAltitude;
    }
    
    public int getUnitsFormatAngle() {
        return this.mUnitsFormatAngle;
    }
    
    public int getUnitsFormatArea() {
        return this.mUnitsFormatArea;
    }
    
    public int getUnitsFormatEnergy() {
        return this.mUnitsFormatEnergy;
    }
    
    public int getUnitsFormatLength() {
        return this.mUnitsFormatLength;
    }
    
    public int getUnitsFormatSlope() {
        return this.mUnitsFormatSlope;
    }
    
    public int getUnitsFormatSpeed() {
        return this.mUnitsFormatSpeed;
    }
    
    public int getUnitsFormatTemperature() {
        return this.mUnitsFormatTemperature;
    }
    
    public int getUnitsFormatWeight() {
        return this.mUnitsFormatWeight;
    }
    
    @Override
    protected int getVersion() {
        return 1;
    }
    
    public boolean isPeriodicUpdatesEnabled() {
        return this.mPeriodicUpdatesEnabled;
    }
    
    public boolean isRunning() {
        return this.mIsRunning;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mPackageName = dataReaderBigEndian.readString();
        this.mIsRunning = dataReaderBigEndian.readBoolean();
        this.mRootDir = dataReaderBigEndian.readString();
        this.mRootDirBackup = dataReaderBigEndian.readString();
        this.mRootDirExport = dataReaderBigEndian.readString();
        this.mRootDirGeocaching = dataReaderBigEndian.readString();
        this.mRootDirMapItems = dataReaderBigEndian.readString();
        this.mRootDirMapsOnline = dataReaderBigEndian.readString();
        this.mRootDirMapsPersonal = dataReaderBigEndian.readString();
        this.mRootDirMapsVector = dataReaderBigEndian.readString();
        this.mRootDirSrtm = dataReaderBigEndian.readString();
        this.mPeriodicUpdatesEnabled = dataReaderBigEndian.readBoolean();
        this.mGcOwnerName = dataReaderBigEndian.readString();
        this.mUnitsFormatAltitude = dataReaderBigEndian.readInt();
        this.mUnitsFormatAngle = dataReaderBigEndian.readInt();
        this.mUnitsFormatArea = dataReaderBigEndian.readInt();
        this.mUnitsFormatLength = dataReaderBigEndian.readInt();
        this.mUnitsFormatSpeed = dataReaderBigEndian.readInt();
        this.mUnitsFormatTemperature = dataReaderBigEndian.readInt();
        if (n >= 1) {
            this.mUnitsFormatEnergy = dataReaderBigEndian.readInt();
            this.mUnitsFormatSlope = dataReaderBigEndian.readInt();
            this.mUnitsFormatWeight = dataReaderBigEndian.readInt();
        }
    }
    
    @Override
    public void reset() {
        this.mPackageName = "";
        this.mIsRunning = false;
        this.mRootDir = "";
        this.mRootDirBackup = "";
        this.mRootDirExport = "";
        this.mRootDirGeocaching = "";
        this.mRootDirMapItems = "";
        this.mRootDirMapsOnline = "";
        this.mRootDirMapsPersonal = "";
        this.mRootDirMapsVector = "";
        this.mRootDirSrtm = "";
        this.mPeriodicUpdatesEnabled = false;
        this.mGcOwnerName = "";
        this.mUnitsFormatAltitude = -1;
        this.mUnitsFormatAngle = -1;
        this.mUnitsFormatArea = -1;
        this.mUnitsFormatLength = -1;
        this.mUnitsFormatSpeed = -1;
        this.mUnitsFormatTemperature = -1;
        this.mUnitsFormatEnergy = -1;
        this.mUnitsFormatSlope = -1;
        this.mUnitsFormatWeight = -1;
    }
    
    protected void setGcOwnerName(final String s) {
        String mGcOwnerName = s;
        if (s == null) {
            mGcOwnerName = "";
        }
        this.mGcOwnerName = mGcOwnerName;
    }
    
    protected void setPackageName(final String s) {
        String mPackageName = s;
        if (s == null) {
            mPackageName = "";
        }
        this.mPackageName = mPackageName;
    }
    
    protected void setPeriodicUpdatesEnabled(final boolean mPeriodicUpdatesEnabled) {
        this.mPeriodicUpdatesEnabled = mPeriodicUpdatesEnabled;
    }
    
    protected void setRootDirBackup(final String s) {
        String mRootDirBackup = s;
        if (s == null) {
            mRootDirBackup = "";
        }
        this.mRootDirBackup = mRootDirBackup;
    }
    
    protected void setRootDirExport(final String s) {
        String mRootDirExport = s;
        if (s == null) {
            mRootDirExport = "";
        }
        this.mRootDirExport = mRootDirExport;
    }
    
    protected void setRootDirGeocaching(final String s) {
        String mRootDirGeocaching = s;
        if (s == null) {
            mRootDirGeocaching = "";
        }
        this.mRootDirGeocaching = mRootDirGeocaching;
    }
    
    protected void setRootDirMapItems(final String s) {
        String mRootDirMapItems = s;
        if (s == null) {
            mRootDirMapItems = "";
        }
        this.mRootDirMapItems = mRootDirMapItems;
    }
    
    protected void setRootDirMapsOnline(final String s) {
        String mRootDirMapsOnline = s;
        if (s == null) {
            mRootDirMapsOnline = "";
        }
        this.mRootDirMapsOnline = mRootDirMapsOnline;
    }
    
    protected void setRootDirMapsPersonal(final String s) {
        String mRootDirMapsPersonal = s;
        if (s == null) {
            mRootDirMapsPersonal = "";
        }
        this.mRootDirMapsPersonal = mRootDirMapsPersonal;
    }
    
    protected void setRootDirMapsVector(final String s) {
        String mRootDirMapsVector = s;
        if (s == null) {
            mRootDirMapsVector = "";
        }
        this.mRootDirMapsVector = mRootDirMapsVector;
    }
    
    protected void setRootDirSrtm(final String s) {
        String mRootDirSrtm = s;
        if (s == null) {
            mRootDirSrtm = "";
        }
        this.mRootDirSrtm = mRootDirSrtm;
    }
    
    protected void setRootDirectory(final String s) {
        String mRootDir = s;
        if (s == null) {
            mRootDir = "";
        }
        this.mRootDir = mRootDir;
    }
    
    protected void setRunning(final boolean mIsRunning) {
        this.mIsRunning = mIsRunning;
    }
    
    protected void setUnitsFormatAltitude(final int mUnitsFormatAltitude) {
        this.mUnitsFormatAltitude = mUnitsFormatAltitude;
    }
    
    protected void setUnitsFormatAngle(final int mUnitsFormatAngle) {
        this.mUnitsFormatAngle = mUnitsFormatAngle;
    }
    
    protected void setUnitsFormatArea(final int mUnitsFormatArea) {
        this.mUnitsFormatArea = mUnitsFormatArea;
    }
    
    protected void setUnitsFormatEnergy(final int mUnitsFormatEnergy) {
        this.mUnitsFormatEnergy = mUnitsFormatEnergy;
    }
    
    protected void setUnitsFormatLength(final int mUnitsFormatLength) {
        this.mUnitsFormatLength = mUnitsFormatLength;
    }
    
    protected void setUnitsFormatSlope(final int mUnitsFormatSlope) {
        this.mUnitsFormatSlope = mUnitsFormatSlope;
    }
    
    protected void setUnitsFormatSpeed(final int mUnitsFormatSpeed) {
        this.mUnitsFormatSpeed = mUnitsFormatSpeed;
    }
    
    protected void setUnitsFormatTemperature(final int mUnitsFormatTemperature) {
        this.mUnitsFormatTemperature = mUnitsFormatTemperature;
    }
    
    protected void setUnitsFormatWeight(final int mUnitsFormatWeight) {
        this.mUnitsFormatWeight = mUnitsFormatWeight;
    }
    
    @Override
    public String toString() {
        return Utils.toString(this);
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mPackageName);
        dataWriterBigEndian.writeBoolean(this.mIsRunning);
        dataWriterBigEndian.writeString(this.mRootDir);
        dataWriterBigEndian.writeString(this.mRootDirBackup);
        dataWriterBigEndian.writeString(this.mRootDirExport);
        dataWriterBigEndian.writeString(this.mRootDirGeocaching);
        dataWriterBigEndian.writeString(this.mRootDirMapItems);
        dataWriterBigEndian.writeString(this.mRootDirMapsOnline);
        dataWriterBigEndian.writeString(this.mRootDirMapsPersonal);
        dataWriterBigEndian.writeString(this.mRootDirMapsVector);
        dataWriterBigEndian.writeString(this.mRootDirSrtm);
        dataWriterBigEndian.writeBoolean(this.mPeriodicUpdatesEnabled);
        dataWriterBigEndian.writeString(this.mGcOwnerName);
        dataWriterBigEndian.writeInt(this.mUnitsFormatAltitude);
        dataWriterBigEndian.writeInt(this.mUnitsFormatAngle);
        dataWriterBigEndian.writeInt(this.mUnitsFormatArea);
        dataWriterBigEndian.writeInt(this.mUnitsFormatLength);
        dataWriterBigEndian.writeInt(this.mUnitsFormatSpeed);
        dataWriterBigEndian.writeInt(this.mUnitsFormatTemperature);
        dataWriterBigEndian.writeInt(this.mUnitsFormatEnergy);
        dataWriterBigEndian.writeInt(this.mUnitsFormatSlope);
        dataWriterBigEndian.writeInt(this.mUnitsFormatWeight);
    }
}
