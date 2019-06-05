package com.journeyapps.barcodescanner;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;
import java.util.ArrayList;
import java.util.List;

public class Decoder implements ResultPointCallback {
   private List possibleResultPoints = new ArrayList();
   private Reader reader;

   public Decoder(Reader var1) {
      this.reader = var1;
   }

   protected Result decode(BinaryBitmap var1) {
      this.possibleResultPoints.clear();
      boolean var4 = false;

      Result var7;
      label72: {
         label73: {
            try {
               var4 = true;
               if (this.reader instanceof MultiFormatReader) {
                  var7 = ((MultiFormatReader)this.reader).decodeWithState(var1);
                  var4 = false;
                  break label72;
               }

               var7 = this.reader.decode(var1);
               var4 = false;
               break label73;
            } catch (Exception var5) {
               var4 = false;
            } finally {
               if (var4) {
                  this.reader.reset();
               }
            }

            var7 = null;
            this.reader.reset();
            return var7;
         }

         this.reader.reset();
         return var7;
      }

      this.reader.reset();
      return var7;
   }

   public Result decode(LuminanceSource var1) {
      return this.decode(this.toBitmap(var1));
   }

   public void foundPossibleResultPoint(ResultPoint var1) {
      this.possibleResultPoints.add(var1);
   }

   public List getPossibleResultPoints() {
      return new ArrayList(this.possibleResultPoints);
   }

   protected Reader getReader() {
      return this.reader;
   }

   protected BinaryBitmap toBitmap(LuminanceSource var1) {
      return new BinaryBitmap(new HybridBinarizer(var1));
   }
}
