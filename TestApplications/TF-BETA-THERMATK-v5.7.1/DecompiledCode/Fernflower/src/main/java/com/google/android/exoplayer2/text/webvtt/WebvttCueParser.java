package com.google.android.exoplayer2.text.webvtt;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.text.style.AlignmentSpan.Standard;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttCueParser {
   public static final Pattern CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
   private static final Pattern CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");
   private final StringBuilder textBuilder = new StringBuilder();

   private static void applyEntity(String var0, SpannableStringBuilder var1) {
      byte var4;
      label42: {
         int var2 = var0.hashCode();
         if (var2 != 3309) {
            if (var2 != 3464) {
               if (var2 != 96708) {
                  if (var2 == 3374865 && var0.equals("nbsp")) {
                     var4 = 2;
                     break label42;
                  }
               } else if (var0.equals("amp")) {
                  var4 = 3;
                  break label42;
               }
            } else if (var0.equals("lt")) {
               var4 = 0;
               break label42;
            }
         } else if (var0.equals("gt")) {
            var4 = 1;
            break label42;
         }

         var4 = -1;
      }

      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 != 3) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("ignoring unsupported entity: '&");
                  var3.append(var0);
                  var3.append(";'");
                  Log.w("WebvttCueParser", var3.toString());
               } else {
                  var1.append('&');
               }
            } else {
               var1.append(' ');
            }
         } else {
            var1.append('>');
         }
      } else {
         var1.append('<');
      }

   }

   private static void applySpansForTag(String var0, WebvttCueParser.StartTag var1, SpannableStringBuilder var2, List var3, List var4) {
      int var5;
      int var6;
      int var8;
      byte var9;
      byte var11;
      label61: {
         var5 = var1.position;
         var6 = var2.length();
         String var7 = var1.name;
         var8 = var7.hashCode();
         var9 = 0;
         if (var8 != 0) {
            if (var8 != 105) {
               if (var8 != 3314158) {
                  if (var8 != 98) {
                     if (var8 != 99) {
                        if (var8 != 117) {
                           if (var8 == 118 && var7.equals("v")) {
                              var11 = 5;
                              break label61;
                           }
                        } else if (var7.equals("u")) {
                           var11 = 2;
                           break label61;
                        }
                     } else if (var7.equals("c")) {
                        var11 = 3;
                        break label61;
                     }
                  } else if (var7.equals("b")) {
                     var11 = 0;
                     break label61;
                  }
               } else if (var7.equals("lang")) {
                  var11 = 4;
                  break label61;
               }
            } else if (var7.equals("i")) {
               var11 = 1;
               break label61;
            }
         } else if (var7.equals("")) {
            var11 = 6;
            break label61;
         }

         var11 = -1;
      }

      switch(var11) {
      case 0:
         var2.setSpan(new StyleSpan(1), var5, var6, 33);
         break;
      case 1:
         var2.setSpan(new StyleSpan(2), var5, var6, 33);
         break;
      case 2:
         var2.setSpan(new UnderlineSpan(), var5, var6, 33);
      case 3:
      case 4:
      case 5:
      case 6:
         break;
      default:
         return;
      }

      var4.clear();
      getApplicableStyles(var3, var0, var1, var4);
      int var10 = var4.size();

      for(var8 = var9; var8 < var10; ++var8) {
         applyStyleToText(var2, ((WebvttCueParser.StyleMatch)var4.get(var8)).style, var5, var6);
      }

   }

   private static void applyStyleToText(SpannableStringBuilder var0, WebvttCssStyle var1, int var2, int var3) {
      if (var1 != null) {
         if (var1.getStyle() != -1) {
            var0.setSpan(new StyleSpan(var1.getStyle()), var2, var3, 33);
         }

         if (var1.isLinethrough()) {
            var0.setSpan(new StrikethroughSpan(), var2, var3, 33);
         }

         if (var1.isUnderline()) {
            var0.setSpan(new UnderlineSpan(), var2, var3, 33);
         }

         if (var1.hasFontColor()) {
            var0.setSpan(new ForegroundColorSpan(var1.getFontColor()), var2, var3, 33);
         }

         if (var1.hasBackgroundColor()) {
            var0.setSpan(new BackgroundColorSpan(var1.getBackgroundColor()), var2, var3, 33);
         }

         if (var1.getFontFamily() != null) {
            var0.setSpan(new TypefaceSpan(var1.getFontFamily()), var2, var3, 33);
         }

         if (var1.getTextAlign() != null) {
            var0.setSpan(new Standard(var1.getTextAlign()), var2, var3, 33);
         }

         int var4 = var1.getFontSizeUnit();
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 == 3) {
                  var0.setSpan(new RelativeSizeSpan(var1.getFontSize() / 100.0F), var2, var3, 33);
               }
            } else {
               var0.setSpan(new RelativeSizeSpan(var1.getFontSize()), var2, var3, 33);
            }
         } else {
            var0.setSpan(new AbsoluteSizeSpan((int)var1.getFontSize(), true), var2, var3, 33);
         }

      }
   }

   private static int findEndOfTag(String var0, int var1) {
      var1 = var0.indexOf(62, var1);
      if (var1 == -1) {
         var1 = var0.length();
      } else {
         ++var1;
      }

      return var1;
   }

   private static void getApplicableStyles(List var0, String var1, WebvttCueParser.StartTag var2, List var3) {
      int var4 = var0.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         WebvttCssStyle var6 = (WebvttCssStyle)var0.get(var5);
         int var7 = var6.getSpecificityScore(var1, var2.name, var2.classes, var2.voice);
         if (var7 > 0) {
            var3.add(new WebvttCueParser.StyleMatch(var7, var6));
         }
      }

      Collections.sort(var3);
   }

   private static String getTagName(String var0) {
      var0 = var0.trim();
      return var0.isEmpty() ? null : Util.splitAtFirst(var0, "[ \\.]")[0];
   }

   private static boolean isSupportedTag(String var0) {
      int var1 = var0.hashCode();
      byte var2;
      if (var1 != 98) {
         if (var1 != 99) {
            if (var1 != 105) {
               if (var1 != 3314158) {
                  if (var1 != 117) {
                     if (var1 == 118 && var0.equals("v")) {
                        var2 = 5;
                        return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
                     }
                  } else if (var0.equals("u")) {
                     var2 = 4;
                     return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
                  }
               } else if (var0.equals("lang")) {
                  var2 = 3;
                  return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
               }
            } else if (var0.equals("i")) {
               var2 = 2;
               return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
            }
         } else if (var0.equals("c")) {
            var2 = 1;
            return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
         }
      } else if (var0.equals("b")) {
         var2 = 0;
         return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
      }

      var2 = -1;
      return var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 5;
   }

   private static boolean parseCue(String var0, Matcher var1, ParsableByteArray var2, WebvttCue.Builder var3, StringBuilder var4, List var5) {
      try {
         var3.setStartTime(WebvttParserUtil.parseTimestampUs(var1.group(1)));
         var3.setEndTime(WebvttParserUtil.parseTimestampUs(var1.group(2)));
      } catch (NumberFormatException var6) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Skipping cue with bad header: ");
         var7.append(var1.group());
         Log.w("WebvttCueParser", var7.toString());
         return false;
      }

      parseCueSettingsList(var1.group(3), var3);
      var4.setLength(0);

      while(true) {
         String var8 = var2.readLine();
         if (TextUtils.isEmpty(var8)) {
            parseCueText(var0, var4.toString(), var3, var5);
            return true;
         }

         if (var4.length() > 0) {
            var4.append("\n");
         }

         var4.append(var8.trim());
      }
   }

   static void parseCueSettingsList(String var0, WebvttCue.Builder var1) {
      Matcher var6 = CUE_SETTING_PATTERN.matcher(var0);

      while(var6.find()) {
         String var2 = var6.group(1);
         String var3 = var6.group(2);

         StringBuilder var4;
         try {
            if ("line".equals(var2)) {
               parseLineAttribute(var3, var1);
            } else if ("align".equals(var2)) {
               var1.setTextAlignment(parseTextAlignment(var3));
            } else if ("position".equals(var2)) {
               parsePositionAttribute(var3, var1);
            } else if ("size".equals(var2)) {
               var1.setWidth(WebvttParserUtil.parsePercentage(var3));
            } else {
               var4 = new StringBuilder();
               var4.append("Unknown cue setting ");
               var4.append(var2);
               var4.append(":");
               var4.append(var3);
               Log.w("WebvttCueParser", var4.toString());
            }
         } catch (NumberFormatException var5) {
            var4 = new StringBuilder();
            var4.append("Skipping bad cue setting: ");
            var4.append(var6.group());
            Log.w("WebvttCueParser", var4.toString());
         }
      }

   }

   static void parseCueText(String var0, String var1, WebvttCue.Builder var2, List var3) {
      SpannableStringBuilder var4 = new SpannableStringBuilder();
      ArrayDeque var5 = new ArrayDeque();
      ArrayList var6 = new ArrayList();
      int var7 = 0;

      while(true) {
         while(true) {
            label67:
            while(var7 < var1.length()) {
               char var8 = var1.charAt(var7);
               int var9;
               if (var8 != '&') {
                  if (var8 != '<') {
                     var4.append(var8);
                     ++var7;
                  } else {
                     var9 = var7 + 1;
                     if (var9 >= var1.length()) {
                        var7 = var9;
                     } else {
                        char var17 = var1.charAt(var9);
                        byte var11 = 1;
                        boolean var18;
                        if (var17 == '/') {
                           var18 = true;
                        } else {
                           var18 = false;
                        }

                        int var12 = findEndOfTag(var1, var9);
                        int var13 = var12 - 2;
                        boolean var16;
                        if (var1.charAt(var13) == '/') {
                           var16 = true;
                        } else {
                           var16 = false;
                        }

                        if (var18) {
                           var11 = 2;
                        }

                        if (!var16) {
                           var13 = var12 - 1;
                        }

                        String var14 = var1.substring(var7 + var11, var13);
                        String var15 = getTagName(var14);
                        var7 = var12;
                        if (var15 != null) {
                           if (!isSupportedTag(var15)) {
                              var7 = var12;
                           } else if (!var18) {
                              var7 = var12;
                              if (!var16) {
                                 var5.push(WebvttCueParser.StartTag.buildStartTag(var14, var4.length()));
                                 var7 = var12;
                              }
                           } else {
                              while(!var5.isEmpty()) {
                                 WebvttCueParser.StartTag var19 = (WebvttCueParser.StartTag)var5.pop();
                                 applySpansForTag(var0, var19, var4, var3, var6);
                                 if (var19.name.equals(var15)) {
                                    var7 = var12;
                                    continue label67;
                                 }
                              }

                              var7 = var12;
                           }
                        }
                     }
                  }
               } else {
                  var9 = var7 + 1;
                  var7 = var1.indexOf(59, var9);
                  int var10 = var1.indexOf(32, var9);
                  if (var7 == -1) {
                     var7 = var10;
                  } else if (var10 != -1) {
                     var7 = Math.min(var7, var10);
                  }

                  if (var7 != -1) {
                     applyEntity(var1.substring(var9, var7), var4);
                     if (var7 == var10) {
                        var4.append(" ");
                     }

                     ++var7;
                  } else {
                     var4.append(var8);
                     var7 = var9;
                  }
               }
            }

            while(!var5.isEmpty()) {
               applySpansForTag(var0, (WebvttCueParser.StartTag)var5.pop(), var4, var3, var6);
            }

            applySpansForTag(var0, WebvttCueParser.StartTag.buildWholeCueVirtualTag(), var4, var3, var6);
            var2.setText(var4);
            return;
         }
      }
   }

   private static void parseLineAttribute(String var0, WebvttCue.Builder var1) throws NumberFormatException {
      int var2 = var0.indexOf(44);
      if (var2 != -1) {
         var1.setLineAnchor(parsePositionAnchor(var0.substring(var2 + 1)));
         var0 = var0.substring(0, var2);
      } else {
         var1.setLineAnchor(Integer.MIN_VALUE);
      }

      if (var0.endsWith("%")) {
         var1.setLine(WebvttParserUtil.parsePercentage(var0));
         var1.setLineType(0);
      } else {
         int var3 = Integer.parseInt(var0);
         var2 = var3;
         if (var3 < 0) {
            var2 = var3 - 1;
         }

         var1.setLine((float)var2);
         var1.setLineType(1);
      }

   }

   private static int parsePositionAnchor(String var0) {
      byte var1;
      label38: {
         switch(var0.hashCode()) {
         case -1364013995:
            if (var0.equals("center")) {
               var1 = 1;
               break label38;
            }
            break;
         case -1074341483:
            if (var0.equals("middle")) {
               var1 = 2;
               break label38;
            }
            break;
         case 100571:
            if (var0.equals("end")) {
               var1 = 3;
               break label38;
            }
            break;
         case 109757538:
            if (var0.equals("start")) {
               var1 = 0;
               break label38;
            }
         }

         var1 = -1;
      }

      if (var1 != 0) {
         if (var1 != 1 && var1 != 2) {
            if (var1 != 3) {
               StringBuilder var2 = new StringBuilder();
               var2.append("Invalid anchor value: ");
               var2.append(var0);
               Log.w("WebvttCueParser", var2.toString());
               return Integer.MIN_VALUE;
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      } else {
         return 0;
      }
   }

   private static void parsePositionAttribute(String var0, WebvttCue.Builder var1) throws NumberFormatException {
      int var2 = var0.indexOf(44);
      if (var2 != -1) {
         var1.setPositionAnchor(parsePositionAnchor(var0.substring(var2 + 1)));
         var0 = var0.substring(0, var2);
      } else {
         var1.setPositionAnchor(Integer.MIN_VALUE);
      }

      var1.setPosition(WebvttParserUtil.parsePercentage(var0));
   }

   private static Alignment parseTextAlignment(String var0) {
      byte var1;
      label48: {
         switch(var0.hashCode()) {
         case -1364013995:
            if (var0.equals("center")) {
               var1 = 2;
               break label48;
            }
            break;
         case -1074341483:
            if (var0.equals("middle")) {
               var1 = 3;
               break label48;
            }
            break;
         case 100571:
            if (var0.equals("end")) {
               var1 = 4;
               break label48;
            }
            break;
         case 3317767:
            if (var0.equals("left")) {
               var1 = 1;
               break label48;
            }
            break;
         case 108511772:
            if (var0.equals("right")) {
               var1 = 5;
               break label48;
            }
            break;
         case 109757538:
            if (var0.equals("start")) {
               var1 = 0;
               break label48;
            }
         }

         var1 = -1;
      }

      if (var1 != 0 && var1 != 1) {
         if (var1 != 2 && var1 != 3) {
            if (var1 != 4 && var1 != 5) {
               StringBuilder var2 = new StringBuilder();
               var2.append("Invalid alignment value: ");
               var2.append(var0);
               Log.w("WebvttCueParser", var2.toString());
               return null;
            } else {
               return Alignment.ALIGN_OPPOSITE;
            }
         } else {
            return Alignment.ALIGN_CENTER;
         }
      } else {
         return Alignment.ALIGN_NORMAL;
      }
   }

   public boolean parseCue(ParsableByteArray var1, WebvttCue.Builder var2, List var3) {
      String var4 = var1.readLine();
      if (var4 == null) {
         return false;
      } else {
         Matcher var5 = CUE_HEADER_PATTERN.matcher(var4);
         if (var5.matches()) {
            return parseCue((String)null, var5, var1, var2, this.textBuilder, var3);
         } else {
            String var6 = var1.readLine();
            if (var6 == null) {
               return false;
            } else {
               var5 = CUE_HEADER_PATTERN.matcher(var6);
               return var5.matches() ? parseCue(var4.trim(), var5, var1, var2, this.textBuilder, var3) : false;
            }
         }
      }
   }

   private static final class StartTag {
      private static final String[] NO_CLASSES = new String[0];
      public final String[] classes;
      public final String name;
      public final int position;
      public final String voice;

      private StartTag(String var1, int var2, String var3, String[] var4) {
         this.position = var2;
         this.name = var1;
         this.voice = var3;
         this.classes = var4;
      }

      public static WebvttCueParser.StartTag buildStartTag(String var0, int var1) {
         String var2 = var0.trim();
         if (var2.isEmpty()) {
            return null;
         } else {
            int var3 = var2.indexOf(" ");
            if (var3 == -1) {
               var0 = "";
            } else {
               var0 = var2.substring(var3).trim();
               var2 = var2.substring(0, var3);
            }

            String[] var5 = Util.split(var2, "\\.");
            String var4 = var5[0];
            if (var5.length > 1) {
               var5 = (String[])Arrays.copyOfRange(var5, 1, var5.length);
            } else {
               var5 = NO_CLASSES;
            }

            return new WebvttCueParser.StartTag(var4, var1, var0, var5);
         }
      }

      public static WebvttCueParser.StartTag buildWholeCueVirtualTag() {
         return new WebvttCueParser.StartTag("", 0, "", new String[0]);
      }
   }

   private static final class StyleMatch implements Comparable {
      public final int score;
      public final WebvttCssStyle style;

      public StyleMatch(int var1, WebvttCssStyle var2) {
         this.score = var1;
         this.style = var2;
      }

      public int compareTo(WebvttCueParser.StyleMatch var1) {
         return this.score - var1.score;
      }
   }
}
