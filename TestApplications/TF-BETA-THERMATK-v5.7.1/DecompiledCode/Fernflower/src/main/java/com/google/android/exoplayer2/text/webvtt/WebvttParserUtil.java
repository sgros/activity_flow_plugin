package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttParserUtil {
   private static final Pattern COMMENT = Pattern.compile("^NOTE(( |\t).*)?$");

   public static Matcher findNextCueHeader(ParsableByteArray var0) {
      label22:
      while(true) {
         String var1 = var0.readLine();
         if (var1 != null) {
            if (COMMENT.matcher(var1).matches()) {
               while(true) {
                  var1 = var0.readLine();
                  if (var1 == null || var1.isEmpty()) {
                     continue label22;
                  }
               }
            }

            Matcher var2 = WebvttCueParser.CUE_HEADER_PATTERN.matcher(var1);
            if (!var2.matches()) {
               continue;
            }

            return var2;
         }

         return null;
      }
   }

   public static boolean isWebvttHeaderLine(ParsableByteArray var0) {
      String var2 = var0.readLine();
      boolean var1;
      if (var2 != null && var2.startsWith("WEBVTT")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static float parsePercentage(String var0) throws NumberFormatException {
      if (var0.endsWith("%")) {
         return Float.parseFloat(var0.substring(0, var0.length() - 1)) / 100.0F;
      } else {
         throw new NumberFormatException("Percentages must end with %");
      }
   }

   public static long parseTimestampUs(String var0) throws NumberFormatException {
      String[] var1 = Util.splitAtFirst(var0, "\\.");
      int var2 = 0;
      String[] var8 = Util.split(var1[0], ":");
      int var3 = var8.length;

      long var4;
      for(var4 = 0L; var2 < var3; ++var2) {
         var4 = var4 * 60L + Long.parseLong(var8[var2]);
      }

      long var6 = var4 * 1000L;
      var4 = var6;
      if (var1.length == 2) {
         var4 = var6 + Long.parseLong(var1[1]);
      }

      return var4 * 1000L;
   }

   public static void validateWebvttHeaderLine(ParsableByteArray var0) throws ParserException {
      int var1 = var0.getPosition();
      if (!isWebvttHeaderLine(var0)) {
         var0.setPosition(var1);
         StringBuilder var2 = new StringBuilder();
         var2.append("Expected WEBVTT. Got ");
         var2.append(var0.readLine());
         throw new ParserException(var2.toString());
      }
   }
}
