package com.google.zxing.integration.android;

public final class IntentResult {
   private final String barcodeImagePath;
   private final String contents;
   private final String errorCorrectionLevel;
   private final String formatName;
   private final Integer orientation;
   private final byte[] rawBytes;

   IntentResult() {
      this((String)null, (String)null, (byte[])null, (Integer)null, (String)null, (String)null);
   }

   IntentResult(String var1, String var2, byte[] var3, Integer var4, String var5, String var6) {
      this.contents = var1;
      this.formatName = var2;
      this.rawBytes = var3;
      this.orientation = var4;
      this.errorCorrectionLevel = var5;
      this.barcodeImagePath = var6;
   }

   public String getBarcodeImagePath() {
      return this.barcodeImagePath;
   }

   public String getContents() {
      return this.contents;
   }

   public String getErrorCorrectionLevel() {
      return this.errorCorrectionLevel;
   }

   public String getFormatName() {
      return this.formatName;
   }

   public Integer getOrientation() {
      return this.orientation;
   }

   public byte[] getRawBytes() {
      return this.rawBytes;
   }

   public String toString() {
      int var1;
      if (this.rawBytes == null) {
         var1 = 0;
      } else {
         var1 = this.rawBytes.length;
      }

      return "Format: " + this.formatName + '\n' + "Contents: " + this.contents + '\n' + "Raw bytes: (" + var1 + " bytes)\nOrientation: " + this.orientation + '\n' + "EC level: " + this.errorCorrectionLevel + '\n' + "Barcode image: " + this.barcodeImagePath + '\n';
   }
}
