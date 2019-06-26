package androidx.core.view.inputmethod;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
   private static final String[] EMPTY_STRING_ARRAY = new String[0];

   public static String[] getContentMimeTypes(EditorInfo var0) {
      if (VERSION.SDK_INT >= 25) {
         String[] var3 = var0.contentMimeTypes;
         if (var3 == null) {
            var3 = EMPTY_STRING_ARRAY;
         }

         return var3;
      } else {
         Bundle var1 = var0.extras;
         if (var1 == null) {
            return EMPTY_STRING_ARRAY;
         } else {
            String[] var2 = var1.getStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
            String[] var4 = var2;
            if (var2 == null) {
               var4 = var0.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
            }

            if (var4 == null) {
               var4 = EMPTY_STRING_ARRAY;
            }

            return var4;
         }
      }
   }

   public static void setContentMimeTypes(EditorInfo var0, String[] var1) {
      if (VERSION.SDK_INT >= 25) {
         var0.contentMimeTypes = var1;
      } else {
         if (var0.extras == null) {
            var0.extras = new Bundle();
         }

         var0.extras.putStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", var1);
         var0.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", var1);
      }

   }
}
