package org.mozilla.focus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class HtmlLoader {
   private static final byte[] pngHeader = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};

   public static String loadDrawableAsDataURI(Context var0, int var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("data:image/png;base64,");
      Bitmap var12 = DrawableUtils.getBitmap(DrawableUtils.loadAndTintDrawable(var0, var1, var2));
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      var12.compress(CompressFormat.PNG, 0, var4);
      ByteArrayInputStream var17 = new ByteArrayInputStream(var4.toByteArray());

      byte[] var13;
      boolean var10001;
      try {
         var13 = new byte[300];
      } catch (IOException var11) {
         var10001 = false;
         throw new IllegalStateException("Unable to load drawable data");
      }

      boolean var16 = false;

      IllegalStateException var14;
      label61:
      while(true) {
         int var5;
         try {
            var5 = var17.read(var13);
         } catch (IOException var8) {
            var10001 = false;
            throw new IllegalStateException("Unable to load drawable data");
         }

         if (var5 <= 0) {
            return var3.toString();
         }

         boolean var15 = var16;
         if (!var16) {
            if (var5 < 8) {
               try {
                  var14 = new IllegalStateException("Loaded drawable is improbably small");
                  throw var14;
               } catch (IOException var7) {
                  var10001 = false;
                  throw new IllegalStateException("Unable to load drawable data");
               }
            }

            var1 = 0;

            while(true) {
               try {
                  if (var1 >= pngHeader.length) {
                     break;
                  }

                  if (var13[var1] != pngHeader[var1]) {
                     break label61;
                  }
               } catch (IOException var10) {
                  var10001 = false;
                  throw new IllegalStateException("Unable to load drawable data");
               }

               ++var1;
            }

            var15 = true;
         }

         try {
            var3.append(Base64.encodeToString(var13, 0, var5, 0));
         } catch (IOException var9) {
            var10001 = false;
            throw new IllegalStateException("Unable to load drawable data");
         }

         var16 = var15;
      }

      try {
         var14 = new IllegalStateException("Invalid png detected");
         throw var14;
      } catch (IOException var6) {
         var10001 = false;
         throw new IllegalStateException("Unable to load drawable data");
      }
   }

   public static String loadPngAsDataURI(Context param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static String loadResourceFile(Context param0, int param1, Map param2) {
      // $FF: Couldn't be decompiled
   }
}
