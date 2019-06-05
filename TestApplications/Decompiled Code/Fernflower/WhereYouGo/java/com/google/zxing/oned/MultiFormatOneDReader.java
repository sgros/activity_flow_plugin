package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class MultiFormatOneDReader extends OneDReader {
   private final OneDReader[] readers;

   public MultiFormatOneDReader(Map var1) {
      Collection var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = (Collection)var1.get(DecodeHintType.POSSIBLE_FORMATS);
      }

      boolean var3;
      if (var1 != null && var1.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      ArrayList var4 = new ArrayList();
      if (var2 != null) {
         if (var2.contains(BarcodeFormat.EAN_13) || var2.contains(BarcodeFormat.UPC_A) || var2.contains(BarcodeFormat.EAN_8) || var2.contains(BarcodeFormat.UPC_E)) {
            var4.add(new MultiFormatUPCEANReader(var1));
         }

         if (var2.contains(BarcodeFormat.CODE_39)) {
            var4.add(new Code39Reader(var3));
         }

         if (var2.contains(BarcodeFormat.CODE_93)) {
            var4.add(new Code93Reader());
         }

         if (var2.contains(BarcodeFormat.CODE_128)) {
            var4.add(new Code128Reader());
         }

         if (var2.contains(BarcodeFormat.ITF)) {
            var4.add(new ITFReader());
         }

         if (var2.contains(BarcodeFormat.CODABAR)) {
            var4.add(new CodaBarReader());
         }

         if (var2.contains(BarcodeFormat.RSS_14)) {
            var4.add(new RSS14Reader());
         }

         if (var2.contains(BarcodeFormat.RSS_EXPANDED)) {
            var4.add(new RSSExpandedReader());
         }
      }

      if (var4.isEmpty()) {
         var4.add(new MultiFormatUPCEANReader(var1));
         var4.add(new Code39Reader());
         var4.add(new CodaBarReader());
         var4.add(new Code93Reader());
         var4.add(new Code128Reader());
         var4.add(new ITFReader());
         var4.add(new RSS14Reader());
         var4.add(new RSSExpandedReader());
      }

      this.readers = (OneDReader[])var4.toArray(new OneDReader[var4.size()]);
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException {
      OneDReader[] var4 = this.readers;
      int var5 = var4.length;
      int var6 = 0;

      while(var6 < var5) {
         OneDReader var7 = var4[var6];

         try {
            Result var9 = var7.decodeRow(var1, var2, var3);
            return var9;
         } catch (ReaderException var8) {
            ++var6;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public void reset() {
      OneDReader[] var1 = this.readers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3].reset();
      }

   }
}
