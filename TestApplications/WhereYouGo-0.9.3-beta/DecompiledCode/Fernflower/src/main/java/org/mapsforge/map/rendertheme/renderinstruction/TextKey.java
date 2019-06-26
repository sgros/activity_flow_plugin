package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;

final class TextKey {
   private static final String KEY_ELEVATION = "ele";
   private static final String KEY_HOUSENUMBER = "addr:housenumber";
   private static final String KEY_NAME = "name";
   private static final String KEY_REF = "ref";
   private static final TextKey TEXT_KEY_ELEVATION = new TextKey("ele");
   private static final TextKey TEXT_KEY_HOUSENUMBER = new TextKey("addr:housenumber");
   private static final TextKey TEXT_KEY_NAME = new TextKey("name");
   private static final TextKey TEXT_KEY_REF = new TextKey("ref");
   private final String key;

   private TextKey(String var1) {
      this.key = var1;
   }

   static TextKey getInstance(String var0) {
      TextKey var1;
      if ("ele".equals(var0)) {
         var1 = TEXT_KEY_ELEVATION;
      } else if ("addr:housenumber".equals(var0)) {
         var1 = TEXT_KEY_HOUSENUMBER;
      } else if ("name".equals(var0)) {
         var1 = TEXT_KEY_NAME;
      } else {
         if (!"ref".equals(var0)) {
            throw new IllegalArgumentException("invalid key: " + var0);
         }

         var1 = TEXT_KEY_REF;
      }

      return var1;
   }

   String getValue(List var1) {
      int var2 = 0;
      int var3 = var1.size();

      String var4;
      while(true) {
         if (var2 >= var3) {
            var4 = null;
            break;
         }

         if (this.key.equals(((Tag)var1.get(var2)).key)) {
            var4 = ((Tag)var1.get(var2)).value;
            break;
         }

         ++var2;
      }

      return var4;
   }
}
