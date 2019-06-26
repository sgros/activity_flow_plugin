package com.google.android.exoplayer2.text.subrip;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SubripDecoder extends SimpleSubtitleDecoder {
   private static final Pattern SUBRIP_TAG_PATTERN = Pattern.compile("\\{\\\\.*?\\}");
   private static final Pattern SUBRIP_TIMING_LINE = Pattern.compile("\\s*((?:(\\d+):)?(\\d+):(\\d+),(\\d+))\\s*-->\\s*((?:(\\d+):)?(\\d+):(\\d+),(\\d+))?\\s*");
   private final ArrayList tags = new ArrayList();
   private final StringBuilder textBuilder = new StringBuilder();

   public SubripDecoder() {
      super("SubripDecoder");
   }

   private Cue buildCue(Spanned var1, String var2) {
      if (var2 == null) {
         return new Cue(var1);
      } else {
         byte var3;
         label118: {
            switch(var2.hashCode()) {
            case -685620710:
               if (var2.equals("{\\an1}")) {
                  var3 = 0;
                  break label118;
               }
               break;
            case -685620679:
               if (var2.equals("{\\an2}")) {
                  var3 = 6;
                  break label118;
               }
               break;
            case -685620648:
               if (var2.equals("{\\an3}")) {
                  var3 = 3;
                  break label118;
               }
               break;
            case -685620617:
               if (var2.equals("{\\an4}")) {
                  var3 = 1;
                  break label118;
               }
               break;
            case -685620586:
               if (var2.equals("{\\an5}")) {
                  var3 = 7;
                  break label118;
               }
               break;
            case -685620555:
               if (var2.equals("{\\an6}")) {
                  var3 = 4;
                  break label118;
               }
               break;
            case -685620524:
               if (var2.equals("{\\an7}")) {
                  var3 = 2;
                  break label118;
               }
               break;
            case -685620493:
               if (var2.equals("{\\an8}")) {
                  var3 = 8;
                  break label118;
               }
               break;
            case -685620462:
               if (var2.equals("{\\an9}")) {
                  var3 = 5;
                  break label118;
               }
            }

            var3 = -1;
         }

         byte var4;
         if (var3 != 0 && var3 != 1 && var3 != 2) {
            if (var3 != 3 && var3 != 4 && var3 != 5) {
               var4 = 1;
            } else {
               var4 = 2;
            }
         } else {
            var4 = 0;
         }

         label92: {
            switch(var2.hashCode()) {
            case -685620710:
               if (var2.equals("{\\an1}")) {
                  var3 = 0;
                  break label92;
               }
               break;
            case -685620679:
               if (var2.equals("{\\an2}")) {
                  var3 = 1;
                  break label92;
               }
               break;
            case -685620648:
               if (var2.equals("{\\an3}")) {
                  var3 = 2;
                  break label92;
               }
               break;
            case -685620617:
               if (var2.equals("{\\an4}")) {
                  var3 = 6;
                  break label92;
               }
               break;
            case -685620586:
               if (var2.equals("{\\an5}")) {
                  var3 = 7;
                  break label92;
               }
               break;
            case -685620555:
               if (var2.equals("{\\an6}")) {
                  var3 = 8;
                  break label92;
               }
               break;
            case -685620524:
               if (var2.equals("{\\an7}")) {
                  var3 = 3;
                  break label92;
               }
               break;
            case -685620493:
               if (var2.equals("{\\an8}")) {
                  var3 = 4;
                  break label92;
               }
               break;
            case -685620462:
               if (var2.equals("{\\an9}")) {
                  var3 = 5;
                  break label92;
               }
            }

            var3 = -1;
         }

         byte var5;
         if (var3 != 0 && var3 != 1 && var3 != 2) {
            if (var3 != 3 && var3 != 4 && var3 != 5) {
               var5 = 1;
            } else {
               var5 = 0;
            }
         } else {
            var5 = 2;
         }

         return new Cue(var1, (Alignment)null, getFractionalPositionForAnchorType(var5), 0, var5, getFractionalPositionForAnchorType(var4), var4, Float.MIN_VALUE);
      }
   }

   static float getFractionalPositionForAnchorType(int var0) {
      if (var0 != 0) {
         return var0 != 1 ? 0.92F : 0.5F;
      } else {
         return 0.08F;
      }
   }

   private static long parseTimecode(Matcher var0, int var1) {
      return (Long.parseLong(var0.group(var1 + 1)) * 60L * 60L * 1000L + Long.parseLong(var0.group(var1 + 2)) * 60L * 1000L + Long.parseLong(var0.group(var1 + 3)) * 1000L + Long.parseLong(var0.group(var1 + 4))) * 1000L;
   }

   private String processLine(String var1, ArrayList var2) {
      String var3 = var1.trim();
      StringBuilder var8 = new StringBuilder(var3);
      Matcher var9 = SUBRIP_TAG_PATTERN.matcher(var3);

      int var7;
      for(int var4 = 0; var9.find(); var4 += var7) {
         String var5 = var9.group();
         var2.add(var5);
         int var6 = var9.start() - var4;
         var7 = var5.length();
         var8.replace(var6, var6 + var7, "");
      }

      return var8.toString();
   }

   protected SubripSubtitle decode(byte[] var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      LongArray var5 = new LongArray();
      ParsableByteArray var6 = new ParsableByteArray(var1, var2);

      while(true) {
         String var10 = var6.readLine();
         if (var10 == null) {
            break;
         }

         if (var10.length() != 0) {
            StringBuilder var7;
            try {
               Integer.parseInt(var10);
            } catch (NumberFormatException var9) {
               var7 = new StringBuilder();
               var7.append("Skipping invalid index: ");
               var7.append(var10);
               Log.w("SubripDecoder", var7.toString());
               continue;
            }

            var10 = var6.readLine();
            if (var10 == null) {
               Log.w("SubripDecoder", "Unexpected end");
               break;
            }

            Matcher var13 = SUBRIP_TIMING_LINE.matcher(var10);
            if (!var13.matches()) {
               var7 = new StringBuilder();
               var7.append("Skipping invalid timing: ");
               var7.append(var10);
               Log.w("SubripDecoder", var7.toString());
            } else {
               boolean var11 = true;
               var5.add(parseTimecode(var13, 1));
               var3 = TextUtils.isEmpty(var13.group(6));
               int var8 = 0;
               if (!var3) {
                  var5.add(parseTimecode(var13, 6));
               } else {
                  var11 = false;
               }

               this.textBuilder.setLength(0);
               this.tags.clear();

               while(true) {
                  var10 = var6.readLine();
                  if (TextUtils.isEmpty(var10)) {
                     Spanned var14 = Html.fromHtml(this.textBuilder.toString());

                     while(true) {
                        if (var8 >= this.tags.size()) {
                           var10 = null;
                           break;
                        }

                        var10 = (String)this.tags.get(var8);
                        if (var10.matches("\\{\\\\an[1-9]\\}")) {
                           break;
                        }

                        ++var8;
                     }

                     var4.add(this.buildCue(var14, var10));
                     if (var11) {
                        var4.add((Object)null);
                     }
                     break;
                  }

                  if (this.textBuilder.length() > 0) {
                     this.textBuilder.append("<br>");
                  }

                  this.textBuilder.append(this.processLine(var10, this.tags));
               }
            }
         }
      }

      Cue[] var12 = new Cue[var4.size()];
      var4.toArray(var12);
      return new SubripSubtitle(var12, var5.toArray());
   }
}
