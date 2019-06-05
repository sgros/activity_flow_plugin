package com.google.zxing.oned;

import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;

final class UPCEANExtensionSupport {
   private static final int[] EXTENSION_START_PATTERN = new int[]{1, 1, 2};
   private final UPCEANExtension5Support fiveSupport = new UPCEANExtension5Support();
   private final UPCEANExtension2Support twoSupport = new UPCEANExtension2Support();

   Result decodeRow(int var1, BitArray var2, int var3) throws NotFoundException {
      int[] var4 = UPCEANReader.findGuardPattern(var2, var3, false, EXTENSION_START_PATTERN);

      Result var5;
      Result var7;
      try {
         var5 = this.fiveSupport.decodeRow(var1, var2, var4);
      } catch (ReaderException var6) {
         var7 = this.twoSupport.decodeRow(var1, var2, var4);
         return var7;
      }

      var7 = var5;
      return var7;
   }
}
