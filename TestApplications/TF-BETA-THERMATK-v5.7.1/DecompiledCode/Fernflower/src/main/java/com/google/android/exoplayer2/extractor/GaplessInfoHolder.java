package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GaplessInfoHolder {
   private static final Pattern GAPLESS_COMMENT_PATTERN = Pattern.compile("^ [0-9a-fA-F]{8} ([0-9a-fA-F]{8}) ([0-9a-fA-F]{8})");
   public int encoderDelay = -1;
   public int encoderPadding = -1;

   private boolean setFromComment(String var1) {
      Matcher var6 = GAPLESS_COMMENT_PATTERN.matcher(var1);
      if (var6.find()) {
         boolean var10001;
         int var2;
         int var3;
         try {
            var2 = Integer.parseInt(var6.group(1), 16);
            var3 = Integer.parseInt(var6.group(2), 16);
         } catch (NumberFormatException var5) {
            var10001 = false;
            return false;
         }

         if (var2 > 0 || var3 > 0) {
            try {
               this.encoderDelay = var2;
               this.encoderPadding = var3;
               return true;
            } catch (NumberFormatException var4) {
               var10001 = false;
            }
         }
      }

      return false;
   }

   public boolean hasGaplessInfo() {
      boolean var1;
      if (this.encoderDelay != -1 && this.encoderPadding != -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean setFromMetadata(Metadata var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         Metadata.Entry var3 = var1.get(var2);
         if (var3 instanceof CommentFrame) {
            CommentFrame var4 = (CommentFrame)var3;
            if ("iTunSMPB".equals(var4.description) && this.setFromComment(var4.text)) {
               return true;
            }
         } else if (var3 instanceof InternalFrame) {
            InternalFrame var5 = (InternalFrame)var3;
            if ("com.apple.iTunes".equals(var5.domain) && "iTunSMPB".equals(var5.description) && this.setFromComment(var5.text)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean setFromXingHeaderValue(int var1) {
      int var2 = var1 >> 12;
      var1 &= 4095;
      if (var2 <= 0 && var1 <= 0) {
         return false;
      } else {
         this.encoderDelay = var2;
         this.encoderPadding = var1;
         return true;
      }
   }
}
