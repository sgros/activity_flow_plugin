package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;

@RequiresApi(9)
class PaintCompatGingerbread {
   private static final String TOFU_STRING = "\udb3f\udffd";
   private static final ThreadLocal sRectThreadLocal = new ThreadLocal();

   static boolean hasGlyph(@NonNull Paint var0, @NonNull String var1) {
      boolean var2 = false;
      int var3 = var1.length();
      boolean var4;
      if (var3 == 1 && Character.isWhitespace(var1.charAt(0))) {
         var4 = true;
      } else {
         float var5 = var0.measureText("\udb3f\udffd");
         float var6 = var0.measureText(var1);
         var4 = var2;
         if (var6 != 0.0F) {
            if (var1.codePointCount(0, var1.length()) > 1) {
               var4 = var2;
               if (var6 > 2.0F * var5) {
                  return var4;
               }

               float var7 = 0.0F;

               int var9;
               for(int var8 = 0; var8 < var3; var8 += var9) {
                  var9 = Character.charCount(var1.codePointAt(var8));
                  var7 += var0.measureText(var1, var8, var8 + var9);
               }

               var4 = var2;
               if (var6 >= var7) {
                  return var4;
               }
            }

            if (var6 != var5) {
               var4 = true;
            } else {
               Pair var10 = obtainEmptyRects();
               var0.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)var10.first);
               var0.getTextBounds(var1, 0, var3, (Rect)var10.second);
               if (!((Rect)var10.first).equals(var10.second)) {
                  var4 = true;
               } else {
                  var4 = false;
               }
            }
         }
      }

      return var4;
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
