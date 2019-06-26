package locus.api.android.features.mapProvider.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class MapConfigLayer extends Storable {
   private List mCalPoints;
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

   public MapConfigLayer(byte[] var1) throws IOException {
      super(var1);
   }

   public void addCalibrationPoint(double var1, double var3, double var5, double var7) {
      this.addCalibrationPoint(new MapConfigLayer.CalibrationPoint(var1, var3, var5, var7));
   }

   public void addCalibrationPoint(MapConfigLayer.CalibrationPoint var1) {
      this.mCalPoints.add(var1);
   }

   public List getCalibrationPoints() {
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

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mName = var2.readString();
      this.mDescription = var2.readString();
      this.mTileSizeX = var2.readInt();
      this.mTileSizeY = var2.readInt();
      this.mXmax = var2.readLong();
      this.mYmax = var2.readLong();
      this.mZoom = var2.readInt();
      this.mProjEpsg = var2.readInt();
      int var3 = var2.readInt();

      for(var1 = 0; var1 < var3; ++var1) {
         this.addCalibrationPoint(var2.readDouble(), var2.readDouble(), var2.readDouble(), var2.readDouble());
      }

   }

   public void reset() {
      this.mName = "";
      this.mDescription = "";
      this.mTileSizeX = 0;
      this.mTileSizeY = 0;
      this.mXmax = 0L;
      this.mYmax = 0L;
      this.mZoom = -1;
      this.mProjEpsg = 0;
      this.mCalPoints = new ArrayList();
   }

   public void setDescription(String var1) {
      this.mDescription = var1;
   }

   public void setName(String var1) {
      this.mName = var1;
   }

   public void setProjEpsg(int var1) {
      this.mProjEpsg = var1;
   }

   public void setTileSizeX(int var1) {
      this.mTileSizeX = var1;
   }

   public void setTileSizeY(int var1) {
      this.mTileSizeY = var1;
   }

   public void setXmax(long var1) {
      this.mXmax = var1;
   }

   public void setYmax(long var1) {
      this.mYmax = var1;
   }

   public void setZoom(int var1) {
      this.mZoom = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mName);
      var1.writeString(this.mDescription);
      var1.writeInt(this.mTileSizeX);
      var1.writeInt(this.mTileSizeY);
      var1.writeLong(this.mXmax);
      var1.writeLong(this.mYmax);
      var1.writeInt(this.mZoom);
      var1.writeInt(this.mProjEpsg);
      var1.writeInt(this.mCalPoints.size());

      for(int var2 = 0; var2 < this.mCalPoints.size(); ++var2) {
         MapConfigLayer.CalibrationPoint var3 = (MapConfigLayer.CalibrationPoint)this.mCalPoints.get(var2);
         var1.writeDouble(var3.x);
         var1.writeDouble(var3.y);
         var1.writeDouble(var3.lat);
         var1.writeDouble(var3.lon);
      }

   }

   public static class CalibrationPoint {
      public double lat;
      public double lon;
      public double x;
      public double y;

      public CalibrationPoint() {
         this.x = 0.0D;
         this.y = 0.0D;
         this.lat = 0.0D;
         this.lon = 0.0D;
      }

      public CalibrationPoint(double var1, double var3, double var5, double var7) {
         this();
         this.x = var1;
         this.y = var3;
         this.lat = var5;
         this.lon = var7;
      }
   }
}
