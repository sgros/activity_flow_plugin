package com.journeyapps.barcodescanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.BitmapFactory.Options;
import com.google.zxing.PlanarYUVLuminanceSource;
import java.io.ByteArrayOutputStream;

public class SourceData {
   private Rect cropRect;
   private byte[] data;
   private int dataHeight;
   private int dataWidth;
   private int imageFormat;
   private int rotation;

   public SourceData(byte[] var1, int var2, int var3, int var4, int var5) {
      this.data = var1;
      this.dataWidth = var2;
      this.dataHeight = var3;
      this.rotation = var5;
      this.imageFormat = var4;
      if (var2 * var3 > var1.length) {
         throw new IllegalArgumentException("Image data does not match the resolution. " + var2 + "x" + var3 + " > " + var1.length);
      }
   }

   private Bitmap getBitmap(Rect var1, int var2) {
      Rect var3 = var1;
      if (this.isRotated()) {
         var3 = new Rect(var1.top, var1.left, var1.bottom, var1.right);
      }

      YuvImage var5 = new YuvImage(this.data, this.imageFormat, this.dataWidth, this.dataHeight, (int[])null);
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      var5.compressToJpeg(var3, 90, var4);
      byte[] var9 = var4.toByteArray();
      Options var6 = new Options();
      var6.inSampleSize = var2;
      Bitmap var10 = BitmapFactory.decodeByteArray(var9, 0, var9.length, var6);
      Bitmap var7 = var10;
      if (this.rotation != 0) {
         Matrix var8 = new Matrix();
         var8.postRotate((float)this.rotation);
         var7 = Bitmap.createBitmap(var10, 0, 0, var10.getWidth(), var10.getHeight(), var8, false);
      }

      return var7;
   }

   public static byte[] rotate180(byte[] var0, int var1, int var2) {
      int var3 = var1 * var2;
      byte[] var4 = new byte[var3];
      var2 = var3 - 1;

      for(var1 = 0; var1 < var3; ++var1) {
         var4[var2] = (byte)var0[var1];
         --var2;
      }

      return var4;
   }

   public static byte[] rotateCCW(byte[] var0, int var1, int var2) {
      int var3 = var1 * var2;
      byte[] var4 = new byte[var3];
      int var5 = var3 - 1;

      for(var3 = 0; var3 < var1; ++var3) {
         for(int var6 = var2 - 1; var6 >= 0; --var6) {
            var4[var5] = (byte)var0[var6 * var1 + var3];
            --var5;
         }
      }

      return var4;
   }

   public static byte[] rotateCW(byte[] var0, int var1, int var2) {
      byte[] var3 = new byte[var1 * var2];
      int var4 = 0;

      for(int var5 = 0; var5 < var1; ++var5) {
         for(int var6 = var2 - 1; var6 >= 0; --var6) {
            var3[var4] = (byte)var0[var6 * var1 + var5];
            ++var4;
         }
      }

      return var3;
   }

   public static byte[] rotateCameraPreview(int var0, byte[] var1, int var2, int var3) {
      byte[] var4 = var1;
      switch(var0) {
      case 0:
         break;
      case 90:
         var4 = rotateCW(var1, var2, var3);
         break;
      case 180:
         var4 = rotate180(var1, var2, var3);
         break;
      case 270:
         var4 = rotateCCW(var1, var2, var3);
         break;
      default:
         var4 = var1;
      }

      return var4;
   }

   public PlanarYUVLuminanceSource createSource() {
      byte[] var1 = rotateCameraPreview(this.rotation, this.data, this.dataWidth, this.dataHeight);
      PlanarYUVLuminanceSource var2;
      if (this.isRotated()) {
         var2 = new PlanarYUVLuminanceSource(var1, this.dataHeight, this.dataWidth, this.cropRect.left, this.cropRect.top, this.cropRect.width(), this.cropRect.height(), false);
      } else {
         var2 = new PlanarYUVLuminanceSource(var1, this.dataWidth, this.dataHeight, this.cropRect.left, this.cropRect.top, this.cropRect.width(), this.cropRect.height(), false);
      }

      return var2;
   }

   public Bitmap getBitmap() {
      return this.getBitmap(1);
   }

   public Bitmap getBitmap(int var1) {
      return this.getBitmap(this.cropRect, var1);
   }

   public Rect getCropRect() {
      return this.cropRect;
   }

   public byte[] getData() {
      return this.data;
   }

   public int getDataHeight() {
      return this.dataHeight;
   }

   public int getDataWidth() {
      return this.dataWidth;
   }

   public int getImageFormat() {
      return this.imageFormat;
   }

   public boolean isRotated() {
      boolean var1;
      if (this.rotation % 180 != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setCropRect(Rect var1) {
      this.cropRect = var1;
   }
}
