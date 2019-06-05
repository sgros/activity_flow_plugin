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

   public MapTileRequest() {
   }

   public MapTileRequest(byte[] var1) throws IOException {
      super(var1);
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

   protected int getVersion() {
      return 0;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mTileX = var2.readInt();
      this.mTileY = var2.readInt();
      this.mTileZoom = var2.readInt();
      this.mMapSystemX1 = var2.readDouble();
      this.mMapSystemY1 = var2.readDouble();
      this.mMapSystemX2 = var2.readDouble();
      this.mMapSystemY2 = var2.readDouble();
   }

   public void reset() {
      this.mTileX = -1;
      this.mTileY = -1;
      this.mTileZoom = -1;
      this.mMapSystemX1 = 0.0D;
      this.mMapSystemY1 = 0.0D;
      this.mMapSystemX2 = 0.0D;
      this.mMapSystemY2 = 0.0D;
   }

   public void setMapSystemX1(double var1) {
      this.mMapSystemX1 = var1;
   }

   public void setMapSystemX2(double var1) {
      this.mMapSystemX2 = var1;
   }

   public void setMapSystemY1(double var1) {
      this.mMapSystemY1 = var1;
   }

   public void setMapSystemY2(double var1) {
      this.mMapSystemY2 = var1;
   }

   public void setTileX(int var1) {
      this.mTileX = var1;
   }

   public void setTileY(int var1) {
      this.mTileY = var1;
   }

   public void setTileZoom(int var1) {
      this.mTileZoom = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.mTileX);
      var1.writeInt(this.mTileY);
      var1.writeInt(this.mTileZoom);
      var1.writeDouble(this.mMapSystemX1);
      var1.writeDouble(this.mMapSystemY1);
      var1.writeDouble(this.mMapSystemX2);
      var1.writeDouble(this.mMapSystemY2);
   }
}
