package com.journeyapps.barcodescanner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import java.util.Map;

public class BarcodeResult {
   private static final float PREVIEW_DOT_WIDTH = 10.0F;
   private static final float PREVIEW_LINE_WIDTH = 4.0F;
   protected Result mResult;
   private final int mScaleFactor = 2;
   protected SourceData sourceData;

   public BarcodeResult(Result var1, SourceData var2) {
      this.mResult = var1;
      this.sourceData = var2;
   }

   private static void drawLine(Canvas var0, Paint var1, ResultPoint var2, ResultPoint var3, int var4) {
      if (var2 != null && var3 != null) {
         var0.drawLine(var2.getX() / (float)var4, var2.getY() / (float)var4, var3.getX() / (float)var4, var3.getY() / (float)var4, var1);
      }

   }

   public BarcodeFormat getBarcodeFormat() {
      return this.mResult.getBarcodeFormat();
   }

   public Bitmap getBitmap() {
      return this.sourceData.getBitmap(2);
   }

   public int getBitmapScaleFactor() {
      return 2;
   }

   public Bitmap getBitmapWithResultPoints(int var1) {
      byte var2 = 0;
      Bitmap var3 = this.getBitmap();
      ResultPoint[] var5 = this.mResult.getResultPoints();
      Bitmap var6 = var3;
      if (var5 != null) {
         var6 = var3;
         if (var5.length > 0) {
            var6 = var3;
            if (var3 != null) {
               Bitmap var4 = Bitmap.createBitmap(var3.getWidth(), var3.getHeight(), Config.ARGB_8888);
               Canvas var7 = new Canvas(var4);
               var7.drawBitmap(var3, 0.0F, 0.0F, (Paint)null);
               Paint var9 = new Paint();
               var9.setColor(var1);
               if (var5.length == 2) {
                  var9.setStrokeWidth(4.0F);
                  drawLine(var7, var9, var5[0], var5[1], 2);
                  var6 = var4;
               } else if (var5.length == 4 && (this.mResult.getBarcodeFormat() == BarcodeFormat.UPC_A || this.mResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                  drawLine(var7, var9, var5[0], var5[1], 2);
                  drawLine(var7, var9, var5[2], var5[3], 2);
                  var6 = var4;
               } else {
                  var9.setStrokeWidth(10.0F);
                  int var8 = var5.length;
                  var1 = var2;

                  while(true) {
                     var6 = var4;
                     if (var1 >= var8) {
                        break;
                     }

                     ResultPoint var10 = var5[var1];
                     if (var10 != null) {
                        var7.drawPoint(var10.getX() / 2.0F, var10.getY() / 2.0F, var9);
                     }

                     ++var1;
                  }
               }
            }
         }
      }

      return var6;
   }

   public byte[] getRawBytes() {
      return this.mResult.getRawBytes();
   }

   public Result getResult() {
      return this.mResult;
   }

   public Map getResultMetadata() {
      return this.mResult.getResultMetadata();
   }

   public ResultPoint[] getResultPoints() {
      return this.mResult.getResultPoints();
   }

   public String getText() {
      return this.mResult.getText();
   }

   public long getTimestamp() {
      return this.mResult.getTimestamp();
   }

   public String toString() {
      return this.mResult.getText();
   }
}
