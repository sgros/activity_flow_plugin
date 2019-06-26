package com.google.zxing;

import java.util.EnumMap;
import java.util.Map;

public final class Result {
   private final BarcodeFormat format;
   private final int numBits;
   private final byte[] rawBytes;
   private Map resultMetadata;
   private ResultPoint[] resultPoints;
   private final String text;
   private final long timestamp;

   public Result(String var1, byte[] var2, int var3, ResultPoint[] var4, BarcodeFormat var5, long var6) {
      this.text = var1;
      this.rawBytes = var2;
      this.numBits = var3;
      this.resultPoints = var4;
      this.format = var5;
      this.resultMetadata = null;
      this.timestamp = var6;
   }

   public Result(String var1, byte[] var2, ResultPoint[] var3, BarcodeFormat var4) {
      this(var1, var2, var3, var4, System.currentTimeMillis());
   }

   public Result(String var1, byte[] var2, ResultPoint[] var3, BarcodeFormat var4, long var5) {
      int var7;
      if (var2 == null) {
         var7 = 0;
      } else {
         var7 = var2.length * 8;
      }

      this(var1, var2, var7, var3, var4, var5);
   }

   public void addResultPoints(ResultPoint[] var1) {
      ResultPoint[] var2 = this.resultPoints;
      if (var2 == null) {
         this.resultPoints = var1;
      } else if (var1 != null && var1.length > 0) {
         ResultPoint[] var3 = new ResultPoint[var2.length + var1.length];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         System.arraycopy(var1, 0, var3, var2.length, var1.length);
         this.resultPoints = var3;
      }

   }

   public BarcodeFormat getBarcodeFormat() {
      return this.format;
   }

   public int getNumBits() {
      return this.numBits;
   }

   public byte[] getRawBytes() {
      return this.rawBytes;
   }

   public Map getResultMetadata() {
      return this.resultMetadata;
   }

   public ResultPoint[] getResultPoints() {
      return this.resultPoints;
   }

   public String getText() {
      return this.text;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void putAllMetadata(Map var1) {
      if (var1 != null) {
         if (this.resultMetadata == null) {
            this.resultMetadata = var1;
         } else {
            this.resultMetadata.putAll(var1);
         }
      }

   }

   public void putMetadata(ResultMetadataType var1, Object var2) {
      if (this.resultMetadata == null) {
         this.resultMetadata = new EnumMap(ResultMetadataType.class);
      }

      this.resultMetadata.put(var1, var2);
   }

   public String toString() {
      return this.text;
   }
}
