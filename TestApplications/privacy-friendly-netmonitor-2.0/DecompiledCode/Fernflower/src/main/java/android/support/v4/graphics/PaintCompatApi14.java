package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

class PaintCompatApi14 {
   private static final String EM_STRING = "m";
   private static final String TOFU_STRING = "\udb3f\udffd";
   private static final ThreadLocal sRectThreadLocal = new ThreadLocal();

   static boolean hasGlyph(@NonNull Paint var0, @NonNull String var1) {
      int var2 = var1.length();
      if (var2 == 1 && Character.isWhitespace(var1.charAt(0))) {
         return true;
      } else {
         float var3 = var0.measureText("\udb3f\udffd");
         float var4 = var0.measureText("m");
         float var5 = var0.measureText(var1);
         float var6 = 0.0F;
         if (var5 == 0.0F) {
            return false;
         } else {
            if (var1.codePointCount(0, var1.length()) > 1) {
               if (var5 > 2.0F * var4) {
                  return false;
               }

               int var8;
               for(int var7 = 0; var7 < var2; var7 = var8) {
                  var8 = Character.charCount(var1.codePointAt(var7)) + var7;
                  var6 += var0.measureText(var1, var7, var8);
               }

               if (var5 >= var6) {
                  return false;
               }
            }

            if (var5 != var3) {
               return true;
            } else {
               Pair var9 = obtainEmptyRects();
               var0.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)var9.first);
               var0.getTextBounds(var1, 0, var2, (Rect)var9.second);
               return ((Rect)var9.first).equals(var9.second) ^ true;
            }
         }
      }
   }

   private static Pair obtainEmptyRects() {
      Pair var0 = (Pair)sRectThreadLocal.get();
      if (var0 == null) {
         var0 = new Pair(new Rect(), new Rect());
         sRectThreadLocal.set(var0);
      } else {
         ((Rect)var0.first).setEmpty();
         ((Rect)var0.second).setEmpty();
      }

      return var0;
   }
}
