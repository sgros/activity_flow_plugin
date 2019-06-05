package locus.api.android.utils;

import android.database.Cursor;
import android.database.MatrixCursor;
import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

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

   public static LocusInfo create(Cursor var0) {
      LocusInfo var1;
      if (var0 != null && var0.getCount() != 0) {
         LocusInfo var2 = new LocusInfo();
         int var3 = 0;

         while(true) {
            var1 = var2;
            if (var3 >= var0.getCount()) {
               break;
            }

            var0.moveToPosition(var3);
            String var6 = var0.getString(0);
            byte var4 = -1;
            switch(var6.hashCode()) {
            case -2032206226:
               if (var6.equals("unitsFormatEnergy")) {
                  var4 = 16;
               }
               break;
            case -1839855924:
               if (var6.equals("unitsFormatLength")) {
                  var4 = 17;
               }
               break;
            case -1731815379:
               if (var6.equals("unitsFormatAngle")) {
                  var4 = 14;
               }
               break;
            case -1715243771:
               if (var6.equals("unitsFormatSlope")) {
                  var4 = 18;
               }
               break;
            case -1715134559:
               if (var6.equals("unitsFormatSpeed")) {
                  var4 = 19;
               }
               break;
            case -1525084578:
               if (var6.equals("unitsFormatWeight")) {
                  var4 = 21;
               }
               break;
            case -1395163857:
               if (var6.equals("rootDirMapItems")) {
                  var4 = 6;
               }
               break;
            case -1014597298:
               if (var6.equals("unitsFormatTemperature")) {
                  var4 = 20;
               }
               break;
            case -821754387:
               if (var6.equals("rootDirBackup")) {
                  var4 = 3;
               }
               break;
            case -714234913:
               if (var6.equals("rootDirExport")) {
                  var4 = 4;
               }
               break;
            case -655943070:
               if (var6.equals("gcOwnerName")) {
                  var4 = 12;
               }
               break;
            case -610050573:
               if (var6.equals("unitsFormatArea")) {
                  var4 = 15;
               }
               break;
            case 229871074:
               if (var6.equals("rootDirMapsPersonal")) {
                  var4 = 8;
               }
               break;
            case 401853871:
               if (var6.equals("periodicUpdates")) {
                  var4 = 11;
               }
               break;
            case 908759025:
               if (var6.equals("packageName")) {
                  var4 = 0;
               }
               break;
            case 971005237:
               if (var6.equals("isRunning")) {
                  var4 = 1;
               }
               break;
            case 1128020473:
               if (var6.equals("rootDirGeocaching")) {
                  var4 = 5;
               }
               break;
            case 1141743221:
               if (var6.equals("rootDirMapsOnline")) {
                  var4 = 7;
               }
               break;
            case 1333578085:
               if (var6.equals("rootDirMapsVector")) {
                  var4 = 9;
               }
               break;
            case 1380075595:
               if (var6.equals("rootDir")) {
                  var4 = 2;
               }
               break;
            case 1525134600:
               if (var6.equals("unitsFormatAltitude")) {
                  var4 = 13;
               }
               break;
            case 1546035203:
               if (var6.equals("rootDirSrtm")) {
                  var4 = 10;
               }
            }

            boolean var5;
            switch(var4) {
            case 0:
               var2.mPackageName = var0.getString(1);
               break;
            case 1:
               if (var0.getInt(1) == 1) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var2.mIsRunning = var5;
               break;
            case 2:
               var2.mRootDir = var0.getString(1);
               break;
            case 3:
               var2.mRootDirBackup = var0.getString(1);
               break;
            case 4:
               var2.mRootDirExport = var0.getString(1);
               break;
            case 5:
               var2.mRootDirGeocaching = var0.getString(1);
               break;
            case 6:
               var2.mRootDirMapItems = var0.getString(1);
               break;
            case 7:
               var2.mRootDirMapsOnline = var0.getString(1);
               break;
            case 8:
               var2.mRootDirMapsPersonal = var0.getString(1);
               break;
            case 9:
               var2.mRootDirMapsVector = var0.getString(1);
               break;
            case 10:
               var2.mRootDirSrtm = var0.getString(1);
               break;
            case 11:
               if (var0.getInt(1) == 1) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var2.mPeriodicUpdatesEnabled = var5;
               break;
            case 12:
               var2.mGcOwnerName = var0.getString(1);
               break;
            case 13:
               var2.mUnitsFormatAltitude = var0.getInt(1);
               break;
            case 14:
               var2.mUnitsFormatAngle = var0.getInt(1);
               break;
            case 15:
               var2.mUnitsFormatArea = var0.getInt(1);
               break;
            case 16:
               var2.mUnitsFormatEnergy = var0.getInt(1);
               break;
            case 17:
               var2.mUnitsFormatLength = var0.getInt(1);
               break;
            case 18:
               var2.mUnitsFormatSlope = var0.getInt(1);
               break;
            case 19:
               var2.mUnitsFormatSpeed = var0.getInt(1);
               break;
            case 20:
               var2.mUnitsFormatTemperature = var0.getInt(1);
               break;
            case 21:
               var2.mUnitsFormatWeight = var0.getInt(1);
            }

            ++var3;
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   protected Cursor create() {
      MatrixCursor var1 = new MatrixCursor(new String[]{"key", "value"});
      var1.addRow(new Object[]{"packageName", this.mPackageName});
      String var2;
      if (this.mIsRunning) {
         var2 = "1";
      } else {
         var2 = "0";
      }

      var1.addRow(new Object[]{"isRunning", var2});
      var1.addRow(new Object[]{"rootDir", this.mRootDir});
      var1.addRow(new Object[]{"rootDirBackup", this.mRootDirBackup});
      var1.addRow(new Object[]{"rootDirExport", this.mRootDirExport});
      var1.addRow(new Object[]{"rootDirGeocaching", this.mRootDirGeocaching});
      var1.addRow(new Object[]{"rootDirMapItems", this.mRootDirMapItems});
      var1.addRow(new Object[]{"rootDirMapsOnline", this.mRootDirMapsOnline});
      var1.addRow(new Object[]{"rootDirMapsPersonal", this.mRootDirMapsPersonal});
      var1.addRow(new Object[]{"rootDirMapsVector", this.mRootDirMapsVector});
      var1.addRow(new Object[]{"rootDirSrtm", this.mRootDirSrtm});
      if (this.mPeriodicUpdatesEnabled) {
         var2 = "1";
      } else {
         var2 = "0";
      }

      var1.addRow(new Object[]{"periodicUpdates", var2});
      var1.addRow(new Object[]{"gcOwnerName", this.mGcOwnerName});
      var1.addRow(new Object[]{"unitsFormatAltitude", this.mUnitsFormatAltitude});
      var1.addRow(new Object[]{"unitsFormatAngle", this.mUnitsFormatAngle});
      var1.addRow(new Object[]{"unitsFormatArea", this.mUnitsFormatArea});
      var1.addRow(new Object[]{"unitsFormatEnergy", this.mUnitsFormatEnergy});
      var1.addRow(new Object[]{"unitsFormatLength", this.mUnitsFormatLength});
      var1.addRow(new Object[]{"unitsFormatSlope", this.mUnitsFormatSlope});
      var1.addRow(new Object[]{"unitsFormatSpeed", this.mUnitsFormatSpeed});
      var1.addRow(new Object[]{"unitsFormatTemperature", this.mUnitsFormatTemperature});
      var1.addRow(new Object[]{"unitsFormatWeight", this.mUnitsFormatWeight});
      return var1;
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

   protected int getVersion() {
      return 1;
   }

   public boolean isPeriodicUpdatesEnabled() {
      return this.mPeriodicUpdatesEnabled;
   }

   public boolean isRunning() {
      return this.mIsRunning;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mPackageName = var2.readString();
      this.mIsRunning = var2.readBoolean();
      this.mRootDir = var2.readString();
      this.mRootDirBackup = var2.readString();
      this.mRootDirExport = var2.readString();
      this.mRootDirGeocaching = var2.readString();
      this.mRootDirMapItems = var2.readString();
      this.mRootDirMapsOnline = var2.readString();
      this.mRootDirMapsPersonal = var2.readString();
      this.mRootDirMapsVector = var2.readString();
      this.mRootDirSrtm = var2.readString();
      this.mPeriodicUpdatesEnabled = var2.readBoolean();
      this.mGcOwnerName = var2.readString();
      this.mUnitsFormatAltitude = var2.readInt();
      this.mUnitsFormatAngle = var2.readInt();
      this.mUnitsFormatArea = var2.readInt();
      this.mUnitsFormatLength = var2.readInt();
      this.mUnitsFormatSpeed = var2.readInt();
      this.mUnitsFormatTemperature = var2.readInt();
      if (var1 >= 1) {
         this.mUnitsFormatEnergy = var2.readInt();
         this.mUnitsFormatSlope = var2.readInt();
         this.mUnitsFormatWeight = var2.readInt();
      }

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

   protected void setGcOwnerName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mGcOwnerName = var2;
   }

   protected void setPackageName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mPackageName = var2;
   }

   protected void setPeriodicUpdatesEnabled(boolean var1) {
      this.mPeriodicUpdatesEnabled = var1;
   }

   protected void setRootDirBackup(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirBackup = var2;
   }

   protected void setRootDirExport(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirExport = var2;
   }

   protected void setRootDirGeocaching(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirGeocaching = var2;
   }

   protected void setRootDirMapItems(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirMapItems = var2;
   }

   protected void setRootDirMapsOnline(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirMapsOnline = var2;
   }

   protected void setRootDirMapsPersonal(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirMapsPersonal = var2;
   }

   protected void setRootDirMapsVector(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirMapsVector = var2;
   }

   protected void setRootDirSrtm(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDirSrtm = var2;
   }

   protected void setRootDirectory(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mRootDir = var2;
   }

   protected void setRunning(boolean var1) {
      this.mIsRunning = var1;
   }

   protected void setUnitsFormatAltitude(int var1) {
      this.mUnitsFormatAltitude = var1;
   }

   protected void setUnitsFormatAngle(int var1) {
      this.mUnitsFormatAngle = var1;
   }

   protected void setUnitsFormatArea(int var1) {
      this.mUnitsFormatArea = var1;
   }

   protected void setUnitsFormatEnergy(int var1) {
      this.mUnitsFormatEnergy = var1;
   }

   protected void setUnitsFormatLength(int var1) {
      this.mUnitsFormatLength = var1;
   }

   protected void setUnitsFormatSlope(int var1) {
      this.mUnitsFormatSlope = var1;
   }

   protected void setUnitsFormatSpeed(int var1) {
      this.mUnitsFormatSpeed = var1;
   }

   protected void setUnitsFormatTemperature(int var1) {
      this.mUnitsFormatTemperature = var1;
   }

   protected void setUnitsFormatWeight(int var1) {
      this.mUnitsFormatWeight = var1;
   }

   public String toString() {
      return locus.api.utils.Utils.toString(this);
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mPackageName);
      var1.writeBoolean(this.mIsRunning);
      var1.writeString(this.mRootDir);
      var1.writeString(this.mRootDirBackup);
      var1.writeString(this.mRootDirExport);
      var1.writeString(this.mRootDirGeocaching);
      var1.writeString(this.mRootDirMapItems);
      var1.writeString(this.mRootDirMapsOnline);
      var1.writeString(this.mRootDirMapsPersonal);
      var1.writeString(this.mRootDirMapsVector);
      var1.writeString(this.mRootDirSrtm);
      var1.writeBoolean(this.mPeriodicUpdatesEnabled);
      var1.writeString(this.mGcOwnerName);
      var1.writeInt(this.mUnitsFormatAltitude);
      var1.writeInt(this.mUnitsFormatAngle);
      var1.writeInt(this.mUnitsFormatArea);
      var1.writeInt(this.mUnitsFormatLength);
      var1.writeInt(this.mUnitsFormatSpeed);
      var1.writeInt(this.mUnitsFormatTemperature);
      var1.writeInt(this.mUnitsFormatEnergy);
      var1.writeInt(this.mUnitsFormatSlope);
      var1.writeInt(this.mUnitsFormatWeight);
   }
}
