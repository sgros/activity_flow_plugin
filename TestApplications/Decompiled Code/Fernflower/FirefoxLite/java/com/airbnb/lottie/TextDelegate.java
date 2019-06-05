package com.airbnb.lottie;

import java.util.Map;

public class TextDelegate {
   private boolean cacheText;
   private final Map stringMap;

   private String getText(String var1) {
      return var1;
   }

   public final String getTextInternal(String var1) {
      if (this.cacheText && this.stringMap.containsKey(var1)) {
         return (String)this.stringMap.get(var1);
      } else {
         String var2 = this.getText(var1);
         if (this.cacheText) {
            this.stringMap.put(var1, var2);
         }

         return var2;
      }
   }
}
