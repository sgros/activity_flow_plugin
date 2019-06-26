package locus.api.android.utils;

import android.database.Cursor;
import android.database.MatrixCursor;
import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Utils;

public class LocusInfo extends Storable {
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

    public String getPackageName() {
        return this.mPackageName;
    }

    /* Access modifiers changed, original: protected */
    public void setPackageName(String packageName) {
        if (packageName == null) {
            packageName = "";
        }
        this.mPackageName = packageName;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    /* Access modifiers changed, original: protected */
    public void setRunning(boolean isRunning) {
        this.mIsRunning = isRunning;
    }

    public String getRootDirectory() {
        return this.mRootDir;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirectory(String rootDirectory) {
        if (rootDirectory == null) {
            rootDirectory = "";
        }
        this.mRootDir = rootDirectory;
    }

    public String getRootDirBackup() {
        return this.mRootDirBackup;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirBackup(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirBackup = dir;
    }

    public String getRootDirExport() {
        return this.mRootDirExport;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirExport(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirExport = dir;
    }

    public String getRootDirGeocaching() {
        return this.mRootDirGeocaching;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirGeocaching(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirGeocaching = dir;
    }

    public String getRootDirMapItems() {
        return this.mRootDirMapItems;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirMapItems(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirMapItems = dir;
    }

    public String getRootDirMapsOnline() {
        return this.mRootDirMapsOnline;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirMapsOnline(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirMapsOnline = dir;
    }

    public String getRootDirMapsPersonal() {
        return this.mRootDirMapsPersonal;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirMapsPersonal(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirMapsPersonal = dir;
    }

    public String getRootDirMapsVector() {
        return this.mRootDirMapsVector;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirMapsVector(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirMapsVector = dir;
    }

    public String getRootDirSrtm() {
        return this.mRootDirSrtm;
    }

    /* Access modifiers changed, original: protected */
    public void setRootDirSrtm(String dir) {
        if (dir == null) {
            dir = "";
        }
        this.mRootDirSrtm = dir;
    }

    public boolean isPeriodicUpdatesEnabled() {
        return this.mPeriodicUpdatesEnabled;
    }

    /* Access modifiers changed, original: protected */
    public void setPeriodicUpdatesEnabled(boolean enabled) {
        this.mPeriodicUpdatesEnabled = enabled;
    }

    public String getGcOwnerName() {
        return this.mGcOwnerName;
    }

    /* Access modifiers changed, original: protected */
    public void setGcOwnerName(String gcOwnerName) {
        if (gcOwnerName == null) {
            gcOwnerName = "";
        }
        this.mGcOwnerName = gcOwnerName;
    }

    public int getUnitsFormatAltitude() {
        return this.mUnitsFormatAltitude;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatAltitude(int format) {
        this.mUnitsFormatAltitude = format;
    }

    public int getUnitsFormatAngle() {
        return this.mUnitsFormatAngle;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatAngle(int format) {
        this.mUnitsFormatAngle = format;
    }

    public int getUnitsFormatArea() {
        return this.mUnitsFormatArea;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatArea(int format) {
        this.mUnitsFormatArea = format;
    }

    public int getUnitsFormatEnergy() {
        return this.mUnitsFormatEnergy;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatEnergy(int format) {
        this.mUnitsFormatEnergy = format;
    }

    public int getUnitsFormatLength() {
        return this.mUnitsFormatLength;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatLength(int format) {
        this.mUnitsFormatLength = format;
    }

    public int getUnitsFormatSlope() {
        return this.mUnitsFormatSlope;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatSlope(int format) {
        this.mUnitsFormatSlope = format;
    }

    public int getUnitsFormatSpeed() {
        return this.mUnitsFormatSpeed;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatSpeed(int format) {
        this.mUnitsFormatSpeed = format;
    }

    public int getUnitsFormatTemperature() {
        return this.mUnitsFormatTemperature;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatTemperature(int format) {
        this.mUnitsFormatTemperature = format;
    }

    public int getUnitsFormatWeight() {
        return this.mUnitsFormatWeight;
    }

    /* Access modifiers changed, original: protected */
    public void setUnitsFormatWeight(int format) {
        this.mUnitsFormatWeight = format;
    }

    public String toString() {
        return Utils.toString(this);
    }

    public static LocusInfo create(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        LocusInfo info = new LocusInfo();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String key = cursor.getString(0);
            int i2 = -1;
            switch (key.hashCode()) {
                case -2032206226:
                    if (key.equals(VALUE_UNITS_FORMAT_ENERGY)) {
                        i2 = 16;
                        break;
                    }
                    break;
                case -1839855924:
                    if (key.equals(VALUE_UNITS_FORMAT_LENGTH)) {
                        i2 = 17;
                        break;
                    }
                    break;
                case -1731815379:
                    if (key.equals(VALUE_UNITS_FORMAT_ANGLE)) {
                        i2 = 14;
                        break;
                    }
                    break;
                case -1715243771:
                    if (key.equals(VALUE_UNITS_FORMAT_SLOPE)) {
                        i2 = 18;
                        break;
                    }
                    break;
                case -1715134559:
                    if (key.equals(VALUE_UNITS_FORMAT_SPEED)) {
                        i2 = 19;
                        break;
                    }
                    break;
                case -1525084578:
                    if (key.equals(VALUE_UNITS_FORMAT_WEIGHT)) {
                        i2 = 21;
                        break;
                    }
                    break;
                case -1395163857:
                    if (key.equals(VALUE_ROOT_DIR_MAP_ITEMS)) {
                        i2 = 6;
                        break;
                    }
                    break;
                case -1014597298:
                    if (key.equals(VALUE_UNITS_FORMAT_TEMPERATURE)) {
                        i2 = 20;
                        break;
                    }
                    break;
                case -821754387:
                    if (key.equals(VALUE_ROOT_DIR_BACKUP)) {
                        i2 = 3;
                        break;
                    }
                    break;
                case -714234913:
                    if (key.equals(VALUE_ROOT_DIR_EXPORT)) {
                        i2 = 4;
                        break;
                    }
                    break;
                case -655943070:
                    if (key.equals(VALUE_GEOCACHING_OWNER_NAME)) {
                        i2 = 12;
                        break;
                    }
                    break;
                case -610050573:
                    if (key.equals(VALUE_UNITS_FORMAT_AREA)) {
                        i2 = 15;
                        break;
                    }
                    break;
                case 229871074:
                    if (key.equals(VALUE_ROOT_DIR_MAPS_PERSONAL)) {
                        i2 = 8;
                        break;
                    }
                    break;
                case 401853871:
                    if (key.equals(VALUE_PERIODIC_UPDATES)) {
                        i2 = 11;
                        break;
                    }
                    break;
                case 908759025:
                    if (key.equals(VALUE_PACKAGE_NAME)) {
                        i2 = 0;
                        break;
                    }
                    break;
                case 971005237:
                    if (key.equals(VALUE_IS_RUNNING)) {
                        i2 = 1;
                        break;
                    }
                    break;
                case 1128020473:
                    if (key.equals(VALUE_ROOT_DIR_GEOCACHING)) {
                        i2 = 5;
                        break;
                    }
                    break;
                case 1141743221:
                    if (key.equals(VALUE_ROOT_DIR_MAPS_ONLINE)) {
                        i2 = 7;
                        break;
                    }
                    break;
                case 1333578085:
                    if (key.equals(VALUE_ROOT_DIR_MAPS_VECTOR)) {
                        i2 = 9;
                        break;
                    }
                    break;
                case 1380075595:
                    if (key.equals(VALUE_ROOT_DIR)) {
                        i2 = 2;
                        break;
                    }
                    break;
                case 1525134600:
                    if (key.equals(VALUE_UNITS_FORMAT_ALTITUDE)) {
                        i2 = 13;
                        break;
                    }
                    break;
                case 1546035203:
                    if (key.equals(VALUE_ROOT_DIR_SRTM)) {
                        i2 = 10;
                        break;
                    }
                    break;
            }
            switch (i2) {
                case 0:
                    info.mPackageName = cursor.getString(1);
                    break;
                case 1:
                    info.mIsRunning = cursor.getInt(1) == 1;
                    break;
                case 2:
                    info.mRootDir = cursor.getString(1);
                    break;
                case 3:
                    info.mRootDirBackup = cursor.getString(1);
                    break;
                case 4:
                    info.mRootDirExport = cursor.getString(1);
                    break;
                case 5:
                    info.mRootDirGeocaching = cursor.getString(1);
                    break;
                case 6:
                    info.mRootDirMapItems = cursor.getString(1);
                    break;
                case 7:
                    info.mRootDirMapsOnline = cursor.getString(1);
                    break;
                case 8:
                    info.mRootDirMapsPersonal = cursor.getString(1);
                    break;
                case 9:
                    info.mRootDirMapsVector = cursor.getString(1);
                    break;
                case 10:
                    info.mRootDirSrtm = cursor.getString(1);
                    break;
                case 11:
                    info.mPeriodicUpdatesEnabled = cursor.getInt(1) == 1;
                    break;
                case 12:
                    info.mGcOwnerName = cursor.getString(1);
                    break;
                case 13:
                    info.mUnitsFormatAltitude = cursor.getInt(1);
                    break;
                case 14:
                    info.mUnitsFormatAngle = cursor.getInt(1);
                    break;
                case 15:
                    info.mUnitsFormatArea = cursor.getInt(1);
                    break;
                case 16:
                    info.mUnitsFormatEnergy = cursor.getInt(1);
                    break;
                case 17:
                    info.mUnitsFormatLength = cursor.getInt(1);
                    break;
                case 18:
                    info.mUnitsFormatSlope = cursor.getInt(1);
                    break;
                case 19:
                    info.mUnitsFormatSpeed = cursor.getInt(1);
                    break;
                case 20:
                    info.mUnitsFormatTemperature = cursor.getInt(1);
                    break;
                case 21:
                    info.mUnitsFormatWeight = cursor.getInt(1);
                    break;
                default:
                    break;
            }
        }
        return info;
    }

    /* Access modifiers changed, original: protected */
    public Cursor create() {
        MatrixCursor c = new MatrixCursor(new String[]{"key", "value"});
        c.addRow(new Object[]{VALUE_PACKAGE_NAME, this.mPackageName});
        Object[] objArr = new Object[2];
        objArr[0] = VALUE_IS_RUNNING;
        objArr[1] = this.mIsRunning ? "1" : "0";
        c.addRow(objArr);
        c.addRow(new Object[]{VALUE_ROOT_DIR, this.mRootDir});
        c.addRow(new Object[]{VALUE_ROOT_DIR_BACKUP, this.mRootDirBackup});
        c.addRow(new Object[]{VALUE_ROOT_DIR_EXPORT, this.mRootDirExport});
        c.addRow(new Object[]{VALUE_ROOT_DIR_GEOCACHING, this.mRootDirGeocaching});
        c.addRow(new Object[]{VALUE_ROOT_DIR_MAP_ITEMS, this.mRootDirMapItems});
        c.addRow(new Object[]{VALUE_ROOT_DIR_MAPS_ONLINE, this.mRootDirMapsOnline});
        c.addRow(new Object[]{VALUE_ROOT_DIR_MAPS_PERSONAL, this.mRootDirMapsPersonal});
        c.addRow(new Object[]{VALUE_ROOT_DIR_MAPS_VECTOR, this.mRootDirMapsVector});
        c.addRow(new Object[]{VALUE_ROOT_DIR_SRTM, this.mRootDirSrtm});
        objArr = new Object[2];
        objArr[0] = VALUE_PERIODIC_UPDATES;
        objArr[1] = this.mPeriodicUpdatesEnabled ? "1" : "0";
        c.addRow(objArr);
        c.addRow(new Object[]{VALUE_GEOCACHING_OWNER_NAME, this.mGcOwnerName});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_ALTITUDE, Integer.valueOf(this.mUnitsFormatAltitude)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_ANGLE, Integer.valueOf(this.mUnitsFormatAngle)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_AREA, Integer.valueOf(this.mUnitsFormatArea)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_ENERGY, Integer.valueOf(this.mUnitsFormatEnergy)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_LENGTH, Integer.valueOf(this.mUnitsFormatLength)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_SLOPE, Integer.valueOf(this.mUnitsFormatSlope)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_SPEED, Integer.valueOf(this.mUnitsFormatSpeed)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_TEMPERATURE, Integer.valueOf(this.mUnitsFormatTemperature)});
        c.addRow(new Object[]{VALUE_UNITS_FORMAT_WEIGHT, Integer.valueOf(this.mUnitsFormatWeight)});
        return c;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 1;
    }

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

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mPackageName = dr.readString();
        this.mIsRunning = dr.readBoolean();
        this.mRootDir = dr.readString();
        this.mRootDirBackup = dr.readString();
        this.mRootDirExport = dr.readString();
        this.mRootDirGeocaching = dr.readString();
        this.mRootDirMapItems = dr.readString();
        this.mRootDirMapsOnline = dr.readString();
        this.mRootDirMapsPersonal = dr.readString();
        this.mRootDirMapsVector = dr.readString();
        this.mRootDirSrtm = dr.readString();
        this.mPeriodicUpdatesEnabled = dr.readBoolean();
        this.mGcOwnerName = dr.readString();
        this.mUnitsFormatAltitude = dr.readInt();
        this.mUnitsFormatAngle = dr.readInt();
        this.mUnitsFormatArea = dr.readInt();
        this.mUnitsFormatLength = dr.readInt();
        this.mUnitsFormatSpeed = dr.readInt();
        this.mUnitsFormatTemperature = dr.readInt();
        if (version >= 1) {
            this.mUnitsFormatEnergy = dr.readInt();
            this.mUnitsFormatSlope = dr.readInt();
            this.mUnitsFormatWeight = dr.readInt();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mPackageName);
        dw.writeBoolean(this.mIsRunning);
        dw.writeString(this.mRootDir);
        dw.writeString(this.mRootDirBackup);
        dw.writeString(this.mRootDirExport);
        dw.writeString(this.mRootDirGeocaching);
        dw.writeString(this.mRootDirMapItems);
        dw.writeString(this.mRootDirMapsOnline);
        dw.writeString(this.mRootDirMapsPersonal);
        dw.writeString(this.mRootDirMapsVector);
        dw.writeString(this.mRootDirSrtm);
        dw.writeBoolean(this.mPeriodicUpdatesEnabled);
        dw.writeString(this.mGcOwnerName);
        dw.writeInt(this.mUnitsFormatAltitude);
        dw.writeInt(this.mUnitsFormatAngle);
        dw.writeInt(this.mUnitsFormatArea);
        dw.writeInt(this.mUnitsFormatLength);
        dw.writeInt(this.mUnitsFormatSpeed);
        dw.writeInt(this.mUnitsFormatTemperature);
        dw.writeInt(this.mUnitsFormatEnergy);
        dw.writeInt(this.mUnitsFormatSlope);
        dw.writeInt(this.mUnitsFormatWeight);
    }
}
