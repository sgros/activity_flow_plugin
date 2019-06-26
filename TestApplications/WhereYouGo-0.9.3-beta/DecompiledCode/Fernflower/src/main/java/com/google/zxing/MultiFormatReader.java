package com.google.zxing;

import com.google.zxing.aztec.AztecReader;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.maxicode.MaxiCodeReader;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class MultiFormatReader implements Reader {
   private Map hints;
   private Reader[] readers;

   private Result decodeInternal(BinaryBitmap var1) throws NotFoundException {
      if (this.readers != null) {
         Reader[] var2 = this.readers;
         int var3 = var2.length;
         int var4 = 0;

         while(var4 < var3) {
            Reader var5 = var2[var4];

            try {
               Result var7 = var5.decode(var1, this.hints);
               return var7;
            } catch (ReaderException var6) {
               ++var4;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException {
      this.setHints((Map)null);
      return this.decodeInternal(var1);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException {
      this.setHints(var2);
      return this.decodeInternal(var1);
   }

   public Result decodeWithState(BinaryBitmap var1) throws NotFoundException {
      if (this.readers == null) {
         this.setHints((Map)null);
      }

      return this.decodeInternal(var1);
   }

   public void reset() {
      if (this.readers != null) {
         Reader[] var1 = this.readers;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1[var3].reset();
         }
      }

   }

   public void setHints(Map var1) {
      boolean var2 = false;
      this.hints = var1;
      boolean var3;
      if (var1 != null && var1.containsKey(DecodeHintType.TRY_HARDER)) {
         var3 = true;
      } else {
         var3 = false;
      }

      Collection var4;
      if (var1 == null) {
         var4 = null;
      } else {
         var4 = (Collection)var1.get(DecodeHintType.POSSIBLE_FORMATS);
      }

      ArrayList var5 = new ArrayList();
      if (var4 != null) {
         if (var4.contains(BarcodeFormat.UPC_A) || var4.contains(BarcodeFormat.UPC_E) || var4.contains(BarcodeFormat.EAN_13) || var4.contains(BarcodeFormat.EAN_8) || var4.contains(BarcodeFormat.CODABAR) || var4.contains(BarcodeFormat.CODE_39) || var4.contains(BarcodeFormat.CODE_93) || var4.contains(BarcodeFormat.CODE_128) || var4.contains(BarcodeFormat.ITF) || var4.contains(BarcodeFormat.RSS_14) || var4.contains(BarcodeFormat.RSS_EXPANDED)) {
            var2 = true;
         }

         if (var2 && !var3) {
            var5.add(new MultiFormatOneDReader(var1));
         }

         if (var4.contains(BarcodeFormat.QR_CODE)) {
            var5.add(new QRCodeReader());
         }

         if (var4.contains(BarcodeFormat.DATA_MATRIX)) {
            var5.add(new DataMatrixReader());
         }

         if (var4.contains(BarcodeFormat.AZTEC)) {
            var5.add(new AztecReader());
         }

         if (var4.contains(BarcodeFormat.PDF_417)) {
            var5.add(new PDF417Reader());
         }

         if (var4.contains(BarcodeFormat.MAXICODE)) {
            var5.add(new MaxiCodeReader());
         }

         if (var2 && var3) {
            var5.add(new MultiFormatOneDReader(var1));
         }
      }

      if (var5.isEmpty()) {
         if (!var3) {
            var5.add(new MultiFormatOneDReader(var1));
         }

         var5.add(new QRCodeReader());
         var5.add(new DataMatrixReader());
         var5.add(new AztecReader());
         var5.add(new PDF417Reader());
         var5.add(new MaxiCodeReader());
         if (var3) {
            var5.add(new MultiFormatOneDReader(var1));
         }
      }

      this.readers = (Reader[])var5.toArray(new Reader[var5.size()]);
   }
}
