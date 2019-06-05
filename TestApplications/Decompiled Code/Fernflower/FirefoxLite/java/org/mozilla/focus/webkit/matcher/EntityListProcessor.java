package org.mozilla.focus.webkit.matcher;

import android.util.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.mozilla.focus.webkit.matcher.util.FocusString;

class EntityListProcessor {
   private final EntityList entityMap = new EntityList();

   private EntityListProcessor(JsonReader var1) throws IOException {
      var1.beginObject();

      while(var1.hasNext()) {
         var1.skipValue();
         this.handleSite(var1);
      }

      var1.endObject();
   }

   public static EntityList getEntityMapFromJSON(JsonReader var0) throws IOException {
      return (new EntityListProcessor(var0)).entityMap;
   }

   private void handleSite(JsonReader var1) throws IOException {
      var1.beginObject();
      Trie var2 = Trie.createRootNode();
      ArrayList var3 = new ArrayList();

      while(true) {
         while(var1.hasNext()) {
            String var4 = var1.nextName();
            if (var4.equals("properties")) {
               var1.beginArray();

               while(var1.hasNext()) {
                  var3.add(var1.nextString());
               }

               var1.endArray();
            } else if (var4.equals("resources")) {
               var1.beginArray();

               while(var1.hasNext()) {
                  var2.put(FocusString.create(var1.nextString()).reverse());
               }

               var1.endArray();
            }
         }

         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            FocusString var5 = FocusString.create((String)var6.next()).reverse();
            this.entityMap.putWhiteList(var5, var2);
         }

         var1.endObject();
         return;
      }
   }
}
