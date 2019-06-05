package com.google.zxing.pdf417.decoder;

import com.google.zxing.pdf417.PDF417Common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

final class BarcodeValue {
   private final Map values = new HashMap();

   public Integer getConfidence(int var1) {
      return (Integer)this.values.get(var1);
   }

   int[] getValue() {
      int var1 = -1;
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.values.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if ((Integer)var4.getValue() > var1) {
            var1 = (Integer)var4.getValue();
            var2.clear();
            var2.add(var4.getKey());
         } else if ((Integer)var4.getValue() == var1) {
            var2.add(var4.getKey());
         }
      }

      return PDF417Common.toIntArray(var2);
   }

   void setValue(int var1) {
      Integer var2 = (Integer)this.values.get(var1);
      Integer var3 = var2;
      if (var2 == null) {
         var3 = 0;
      }

      int var4 = var3;
      this.values.put(var1, var4 + 1);
   }
}
