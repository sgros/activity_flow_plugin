package org.mozilla.focus.utils;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class MimeUtils {
   private static final Pattern audioPattern = Pattern.compile("^audio/[0-9,a-z,A-Z,-,*]+?$");
   private static final Pattern imgPattern = Pattern.compile("^image/[0-9,a-z,A-Z,-,*]+?$");
   private static final Pattern textPattern = Pattern.compile("^text/[0-9,a-z,A-Z,-,*]+?$");
   private static final Pattern videoPattern = Pattern.compile("^video/[0-9,a-z,A-Z,-,*]+?$");

   public static boolean isImage(String var0) {
      boolean var1;
      if (!TextUtils.isEmpty(var0) && imgPattern.matcher(var0).find()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
