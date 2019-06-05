package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class MultiFormatUPCEANReader extends OneDReader {
   private final UPCEANReader[] readers;

   public MultiFormatUPCEANReader(Map var1) {
      Collection var3;
      if (var1 == null) {
         var3 = null;
      } else {
         var3 = (Collection)var1.get(DecodeHintType.POSSIBLE_FORMATS);
      }

      ArrayList var2 = new ArrayList();
      if (var3 != null) {
         if (var3.contains(BarcodeFormat.EAN_13)) {
            var2.add(new EAN13Reader());
         } else if (var3.contains(BarcodeFormat.UPC_A)) {
            var2.add(new UPCAReader());
         }

         if (var3.contains(BarcodeFormat.EAN_8)) {
            var2.add(new EAN8Reader());
         }

         if (var3.contains(BarcodeFormat.UPC_E)) {
            var2.add(new UPCEReader());
         }
      }

      if (var2.isEmpty()) {
         var2.add(new EAN13Reader());
         var2.add(new EAN8Reader());
         var2.add(new UPCEReader());
      }

      this.readers = (UPCEANReader[])var2.toArray(new UPCEANReader[var2.size()]);
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException {
      boolean var4 = false;
      int[] var5 = UPCEANReader.findStartGuardPattern(var2);
      UPCEANReader[] var6 = this.readers;
      int var7 = var6.length;
      int var8 = 0;

      while(true) {
         if (var8 < var7) {
            UPCEANReader var9 = var6[var8];

            Result var15;
            try {
               var15 = var9.decodeRow(var1, var2, var5, var3);
            } catch (ReaderException var10) {
               ++var8;
               continue;
            }

            boolean var11;
            if (var15.getBarcodeFormat() == BarcodeFormat.EAN_13 && var15.getText().charAt(0) == '0') {
               var11 = true;
            } else {
               var11 = false;
            }

            Collection var12;
            if (var3 == null) {
               var12 = null;
            } else {
               var12 = (Collection)var3.get(DecodeHintType.POSSIBLE_FORMATS);
            }

            boolean var14;
            label36: {
               if (var12 != null) {
                  var14 = var4;
                  if (!var12.contains(BarcodeFormat.UPC_A)) {
                     break label36;
                  }
               }

               var14 = true;
            }

            Result var13;
            if (var11 && var14) {
               var13 = new Result(var15.getText().substring(1), var15.getRawBytes(), var15.getResultPoints(), BarcodeFormat.UPC_A);
               var13.putAllMetadata(var15.getResultMetadata());
            } else {
               var13 = var15;
            }

            return var13;
         }

         throw NotFoundException.getNotFoundInstance();
      }
   }

   public void reset() {
      UPCEANReader[] var1 = this.readers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset();
      }

   }
}
