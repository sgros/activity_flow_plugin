package com.google.android.exoplayer2.text.ssa;

import android.text.TextUtils;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SsaDecoder extends SimpleSubtitleDecoder {
   private static final Pattern SSA_TIMECODE_PATTERN = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)(?::|\\.)(\\d+)");
   private int formatEndIndex;
   private int formatKeyCount;
   private int formatStartIndex;
   private int formatTextIndex;
   private final boolean haveInitializationData;

   public SsaDecoder(List var1) {
      super("SsaDecoder");
      if (var1 != null && !var1.isEmpty()) {
         this.haveInitializationData = true;
         String var2 = Util.fromUtf8Bytes((byte[])var1.get(0));
         Assertions.checkArgument(var2.startsWith("Format: "));
         this.parseFormatLine(var2);
         this.parseHeader(new ParsableByteArray((byte[])var1.get(1)));
      } else {
         this.haveInitializationData = false;
      }

   }

   private void parseDialogueLine(String var1, List var2, LongArray var3) {
      StringBuilder var12;
      if (this.formatKeyCount == 0) {
         var12 = new StringBuilder();
         var12.append("Skipping dialogue line before complete format: ");
         var12.append(var1);
         Log.w("SsaDecoder", var12.toString());
      } else {
         String[] var4 = var1.substring(10).split(",", this.formatKeyCount);
         if (var4.length != this.formatKeyCount) {
            var12 = new StringBuilder();
            var12.append("Skipping dialogue line with fewer columns than format: ");
            var12.append(var1);
            Log.w("SsaDecoder", var12.toString());
         } else {
            long var5 = parseTimecodeUs(var4[this.formatStartIndex]);
            if (var5 == -9223372036854775807L) {
               var12 = new StringBuilder();
               var12.append("Skipping invalid timing: ");
               var12.append(var1);
               Log.w("SsaDecoder", var12.toString());
            } else {
               String var7 = var4[this.formatEndIndex];
               long var10;
               if (!var7.trim().isEmpty()) {
                  long var8 = parseTimecodeUs(var7);
                  var10 = var8;
                  if (var8 == -9223372036854775807L) {
                     var12 = new StringBuilder();
                     var12.append("Skipping invalid timing: ");
                     var12.append(var1);
                     Log.w("SsaDecoder", var12.toString());
                     return;
                  }
               } else {
                  var10 = -9223372036854775807L;
               }

               var2.add(new Cue(var4[this.formatTextIndex].replaceAll("\\{.*?\\}", "").replaceAll("\\\\N", "\n").replaceAll("\\\\n", "\n")));
               var3.add(var5);
               if (var10 != -9223372036854775807L) {
                  var2.add((Object)null);
                  var3.add(var10);
               }

            }
         }
      }
   }

   private void parseEventBody(ParsableByteArray var1, List var2, LongArray var3) {
      while(true) {
         String var4 = var1.readLine();
         if (var4 == null) {
            return;
         }

         if (!this.haveInitializationData && var4.startsWith("Format: ")) {
            this.parseFormatLine(var4);
         } else if (var4.startsWith("Dialogue: ")) {
            this.parseDialogueLine(var4, var2, var3);
         }
      }
   }

   private void parseFormatLine(String var1) {
      String[] var5 = TextUtils.split(var1.substring(8), ",");
      this.formatKeyCount = var5.length;
      this.formatStartIndex = -1;
      this.formatEndIndex = -1;
      this.formatTextIndex = -1;

      for(int var2 = 0; var2 < this.formatKeyCount; ++var2) {
         byte var6;
         label48: {
            String var3 = Util.toLowerInvariant(var5[var2].trim());
            int var4 = var3.hashCode();
            if (var4 != 100571) {
               if (var4 != 3556653) {
                  if (var4 == 109757538 && var3.equals("start")) {
                     var6 = 0;
                     break label48;
                  }
               } else if (var3.equals("text")) {
                  var6 = 2;
                  break label48;
               }
            } else if (var3.equals("end")) {
               var6 = 1;
               break label48;
            }

            var6 = -1;
         }

         if (var6 != 0) {
            if (var6 != 1) {
               if (var6 == 2) {
                  this.formatTextIndex = var2;
               }
            } else {
               this.formatEndIndex = var2;
            }
         } else {
            this.formatStartIndex = var2;
         }
      }

      if (this.formatStartIndex == -1 || this.formatEndIndex == -1 || this.formatTextIndex == -1) {
         this.formatKeyCount = 0;
      }

   }

   private void parseHeader(ParsableByteArray var1) {
      String var2;
      do {
         var2 = var1.readLine();
      } while(var2 != null && !var2.startsWith("[Events]"));

   }

   public static long parseTimecodeUs(String var0) {
      Matcher var1 = SSA_TIMECODE_PATTERN.matcher(var0);
      return !var1.matches() ? -9223372036854775807L : Long.parseLong(var1.group(1)) * 60L * 60L * 1000000L + Long.parseLong(var1.group(2)) * 60L * 1000000L + Long.parseLong(var1.group(3)) * 1000000L + Long.parseLong(var1.group(4)) * 10000L;
   }

   protected SsaSubtitle decode(byte[] var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      LongArray var5 = new LongArray();
      ParsableByteArray var6 = new ParsableByteArray(var1, var2);
      if (!this.haveInitializationData) {
         this.parseHeader(var6);
      }

      this.parseEventBody(var6, var4, var5);
      Cue[] var7 = new Cue[var4.size()];
      var4.toArray(var7);
      return new SsaSubtitle(var7, var5.toArray());
   }
}
