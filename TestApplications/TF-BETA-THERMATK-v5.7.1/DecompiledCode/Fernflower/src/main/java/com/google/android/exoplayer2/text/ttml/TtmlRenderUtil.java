package com.google.android.exoplayer2.text.ttml;

import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.text.style.AlignmentSpan.Standard;
import java.util.Map;

final class TtmlRenderUtil {
   public static void applyStylesToSpan(SpannableStringBuilder var0, int var1, int var2, TtmlStyle var3) {
      if (var3.getStyle() != -1) {
         var0.setSpan(new StyleSpan(var3.getStyle()), var1, var2, 33);
      }

      if (var3.isLinethrough()) {
         var0.setSpan(new StrikethroughSpan(), var1, var2, 33);
      }

      if (var3.isUnderline()) {
         var0.setSpan(new UnderlineSpan(), var1, var2, 33);
      }

      if (var3.hasFontColor()) {
         var0.setSpan(new ForegroundColorSpan(var3.getFontColor()), var1, var2, 33);
      }

      if (var3.hasBackgroundColor()) {
         var0.setSpan(new BackgroundColorSpan(var3.getBackgroundColor()), var1, var2, 33);
      }

      if (var3.getFontFamily() != null) {
         var0.setSpan(new TypefaceSpan(var3.getFontFamily()), var1, var2, 33);
      }

      if (var3.getTextAlign() != null) {
         var0.setSpan(new Standard(var3.getTextAlign()), var1, var2, 33);
      }

      int var4 = var3.getFontSizeUnit();
      if (var4 != 1) {
         if (var4 != 2) {
            if (var4 == 3) {
               var0.setSpan(new RelativeSizeSpan(var3.getFontSize() / 100.0F), var1, var2, 33);
            }
         } else {
            var0.setSpan(new RelativeSizeSpan(var3.getFontSize()), var1, var2, 33);
         }
      } else {
         var0.setSpan(new AbsoluteSizeSpan((int)var3.getFontSize(), true), var1, var2, 33);
      }

   }

   static String applyTextElementSpacePolicy(String var0) {
      return var0.replaceAll("\r\n", "\n").replaceAll(" *\n *", "\n").replaceAll("\n", " ").replaceAll("[ \t\\x0B\f\r]+", " ");
   }

   static void endParagraph(SpannableStringBuilder var0) {
      int var1;
      for(var1 = var0.length() - 1; var1 >= 0 && var0.charAt(var1) == ' '; --var1) {
      }

      if (var1 >= 0 && var0.charAt(var1) != '\n') {
         var0.append('\n');
      }

   }

   public static TtmlStyle resolveStyle(TtmlStyle var0, String[] var1, Map var2) {
      if (var0 == null && var1 == null) {
         return null;
      } else {
         byte var3 = 0;
         int var4 = 0;
         if (var0 == null && var1.length == 1) {
            return (TtmlStyle)var2.get(var1[0]);
         } else if (var0 == null && var1.length > 1) {
            var0 = new TtmlStyle();

            for(int var6 = var1.length; var4 < var6; ++var4) {
               var0.chain((TtmlStyle)var2.get(var1[var4]));
            }

            return var0;
         } else if (var0 != null && var1 != null && var1.length == 1) {
            var0.chain((TtmlStyle)var2.get(var1[0]));
            return var0;
         } else {
            if (var0 != null && var1 != null && var1.length > 1) {
               int var5 = var1.length;

               for(var4 = var3; var4 < var5; ++var4) {
                  var0.chain((TtmlStyle)var2.get(var1[var4]));
               }
            }

            return var0;
         }
      }
   }
}
