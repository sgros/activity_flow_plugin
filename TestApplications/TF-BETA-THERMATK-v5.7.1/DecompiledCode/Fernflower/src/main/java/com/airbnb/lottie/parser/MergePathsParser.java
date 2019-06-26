package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.content.MergePaths;
import java.io.IOException;

class MergePathsParser {
   static MergePaths parse(JsonReader var0) throws IOException {
      String var1 = null;
      MergePaths.MergePathsMode var2 = null;
      boolean var3 = false;

      while(var0.hasNext()) {
         String var4 = var0.nextName();
         byte var5 = -1;
         int var6 = var4.hashCode();
         if (var6 != 3324) {
            if (var6 != 3488) {
               if (var6 == 3519 && var4.equals("nm")) {
                  var5 = 0;
               }
            } else if (var4.equals("mm")) {
               var5 = 1;
            }
         } else if (var4.equals("hd")) {
            var5 = 2;
         }

         if (var5 != 0) {
            if (var5 != 1) {
               if (var5 != 2) {
                  var0.skipValue();
               } else {
                  var3 = var0.nextBoolean();
               }
            } else {
               var2 = MergePaths.MergePathsMode.forId(var0.nextInt());
            }
         } else {
            var1 = var0.nextString();
         }
      }

      return new MergePaths(var1, var2, var3);
   }
}
