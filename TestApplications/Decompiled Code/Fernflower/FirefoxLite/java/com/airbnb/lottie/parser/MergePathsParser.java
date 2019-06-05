package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.model.content.MergePaths;
import java.io.IOException;

class MergePathsParser {
   static MergePaths parse(JsonReader var0) throws IOException {
      String var1 = null;
      MergePaths.MergePathsMode var2 = null;

      while(var0.hasNext()) {
         String var3 = var0.nextName();
         byte var4 = -1;
         int var5 = var3.hashCode();
         if (var5 != 3488) {
            if (var5 == 3519 && var3.equals("nm")) {
               var4 = 0;
            }
         } else if (var3.equals("mm")) {
            var4 = 1;
         }

         switch(var4) {
         case 0:
            var1 = var0.nextString();
            break;
         case 1:
            var2 = MergePaths.MergePathsMode.forId(var0.nextInt());
            break;
         default:
            var0.skipValue();
         }
      }

      return new MergePaths(var1, var2);
   }
}
