package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;

public final class QRCode {
   public static final int NUM_MASK_PATTERNS = 8;
   private ErrorCorrectionLevel ecLevel;
   private int maskPattern = -1;
   private ByteMatrix matrix;
   private Mode mode;
   private Version version;

   public static boolean isValidMaskPattern(int var0) {
      boolean var1;
      if (var0 >= 0 && var0 < 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public ErrorCorrectionLevel getECLevel() {
      return this.ecLevel;
   }

   public int getMaskPattern() {
      return this.maskPattern;
   }

   public ByteMatrix getMatrix() {
      return this.matrix;
   }

   public Mode getMode() {
      return this.mode;
   }

   public Version getVersion() {
      return this.version;
   }

   public void setECLevel(ErrorCorrectionLevel var1) {
      this.ecLevel = var1;
   }

   public void setMaskPattern(int var1) {
      this.maskPattern = var1;
   }

   public void setMatrix(ByteMatrix var1) {
      this.matrix = var1;
   }

   public void setMode(Mode var1) {
      this.mode = var1;
   }

   public void setVersion(Version var1) {
      this.version = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(200);
      var1.append("<<\n");
      var1.append(" mode: ");
      var1.append(this.mode);
      var1.append("\n ecLevel: ");
      var1.append(this.ecLevel);
      var1.append("\n version: ");
      var1.append(this.version);
      var1.append("\n maskPattern: ");
      var1.append(this.maskPattern);
      if (this.matrix == null) {
         var1.append("\n matrix: null\n");
      } else {
         var1.append("\n matrix:\n");
         var1.append(this.matrix);
      }

      var1.append(">>\n");
      return var1.toString();
   }
}
