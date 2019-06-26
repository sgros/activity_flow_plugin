package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CssParser {
   private static final Pattern VOICE_NAME_PATTERN = Pattern.compile("\\[voice=\"([^\"]*)\"\\]");
   private final StringBuilder stringBuilder = new StringBuilder();
   private final ParsableByteArray styleInput = new ParsableByteArray();

   public CssParser() {
   }

   private void applySelectorToStyle(WebvttCssStyle var1, String var2) {
      if (!"".equals(var2)) {
         int var3 = var2.indexOf(91);
         String var4 = var2;
         if (var3 != -1) {
            Matcher var5 = VOICE_NAME_PATTERN.matcher(var2.substring(var3));
            if (var5.matches()) {
               var1.setTargetVoice(var5.group(1));
            }

            var4 = var2.substring(0, var3);
         }

         String[] var6 = Util.split(var4, "\\.");
         var2 = var6[0];
         var3 = var2.indexOf(35);
         if (var3 != -1) {
            var1.setTargetTagName(var2.substring(0, var3));
            var1.setTargetId(var2.substring(var3 + 1));
         } else {
            var1.setTargetTagName(var2);
         }

         if (var6.length > 1) {
            var1.setTargetClasses((String[])Arrays.copyOfRange(var6, 1, var6.length));
         }

      }
   }

   private static boolean maybeSkipComment(ParsableByteArray var0) {
      int var1 = var0.getPosition();
      int var2 = var0.limit();
      byte[] var3 = var0.data;
      if (var1 + 2 <= var2) {
         int var4 = var1 + 1;
         if (var3[var1] == 47) {
            var1 = var4 + 1;
            if (var3[var4] == 42) {
               while(true) {
                  while(true) {
                     var4 = var1 + 1;
                     if (var4 >= var2) {
                        var0.skipBytes(var2 - var0.getPosition());
                        return true;
                     }

                     if ((char)var3[var1] == '*' && (char)var3[var4] == '/') {
                        var1 = var4 + 1;
                        var2 = var1;
                     } else {
                        var1 = var4;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private static boolean maybeSkipWhitespace(ParsableByteArray var0) {
      char var1 = peekCharAtPosition(var0, var0.getPosition());
      if (var1 != '\t' && var1 != '\n' && var1 != '\f' && var1 != '\r' && var1 != ' ') {
         return false;
      } else {
         var0.skipBytes(1);
         return true;
      }
   }

   private static String parseIdentifier(ParsableByteArray var0, StringBuilder var1) {
      boolean var2 = false;
      var1.setLength(0);
      int var3 = var0.getPosition();
      int var4 = var0.limit();

      while(var3 < var4 && !var2) {
         char var5 = (char)var0.data[var3];
         if ((var5 < 'A' || var5 > 'Z') && (var5 < 'a' || var5 > 'z') && (var5 < '0' || var5 > '9') && var5 != '#' && var5 != '-' && var5 != '.' && var5 != '_') {
            var2 = true;
         } else {
            ++var3;
            var1.append(var5);
         }
      }

      var0.skipBytes(var3 - var0.getPosition());
      return var1.toString();
   }

   static String parseNextToken(ParsableByteArray var0, StringBuilder var1) {
      skipWhitespaceAndComments(var0);
      if (var0.bytesLeft() == 0) {
         return null;
      } else {
         String var2 = parseIdentifier(var0, var1);
         if (!"".equals(var2)) {
            return var2;
         } else {
            var1 = new StringBuilder();
            var1.append("");
            var1.append((char)var0.readUnsignedByte());
            return var1.toString();
         }
      }
   }

   private static String parsePropertyValue(ParsableByteArray var0, StringBuilder var1) {
      StringBuilder var2 = new StringBuilder();
      boolean var3 = false;

      while(true) {
         while(!var3) {
            int var4 = var0.getPosition();
            String var5 = parseNextToken(var0, var1);
            if (var5 == null) {
               return null;
            }

            if (!"}".equals(var5) && !";".equals(var5)) {
               var2.append(var5);
            } else {
               var0.setPosition(var4);
               var3 = true;
            }
         }

         return var2.toString();
      }
   }

   private static String parseSelector(ParsableByteArray var0, StringBuilder var1) {
      skipWhitespaceAndComments(var0);
      if (var0.bytesLeft() < 5) {
         return null;
      } else if (!"::cue".equals(var0.readString(5))) {
         return null;
      } else {
         int var2 = var0.getPosition();
         String var3 = parseNextToken(var0, var1);
         if (var3 == null) {
            return null;
         } else if ("{".equals(var3)) {
            var0.setPosition(var2);
            return "";
         } else {
            if ("(".equals(var3)) {
               var3 = readCueTarget(var0);
            } else {
               var3 = null;
            }

            String var4 = parseNextToken(var0, var1);
            return ")".equals(var4) && var4 != null ? var3 : null;
         }
      }
   }

   private static void parseStyleDeclaration(ParsableByteArray var0, WebvttCssStyle var1, StringBuilder var2) {
      skipWhitespaceAndComments(var0);
      String var3 = parseIdentifier(var0, var2);
      if (!"".equals(var3)) {
         if (":".equals(parseNextToken(var0, var2))) {
            skipWhitespaceAndComments(var0);
            String var4 = parsePropertyValue(var0, var2);
            if (var4 != null && !"".equals(var4)) {
               int var5 = var0.getPosition();
               String var6 = parseNextToken(var0, var2);
               if (!";".equals(var6)) {
                  if (!"}".equals(var6)) {
                     return;
                  }

                  var0.setPosition(var5);
               }

               if ("color".equals(var3)) {
                  var1.setFontColor(ColorParser.parseCssColor(var4));
               } else if ("background-color".equals(var3)) {
                  var1.setBackgroundColor(ColorParser.parseCssColor(var4));
               } else if ("text-decoration".equals(var3)) {
                  if ("underline".equals(var4)) {
                     var1.setUnderline(true);
                  }
               } else if ("font-family".equals(var3)) {
                  var1.setFontFamily(var4);
               } else if ("font-weight".equals(var3)) {
                  if ("bold".equals(var4)) {
                     var1.setBold(true);
                  }
               } else if ("font-style".equals(var3) && "italic".equals(var4)) {
                  var1.setItalic(true);
               }
            }

         }
      }
   }

   private static char peekCharAtPosition(ParsableByteArray var0, int var1) {
      return (char)var0.data[var1];
   }

   private static String readCueTarget(ParsableByteArray var0) {
      int var1 = var0.getPosition();
      int var2 = var0.limit();

      for(boolean var3 = false; var1 < var2 && !var3; ++var1) {
         if ((char)var0.data[var1] == ')') {
            var3 = true;
         } else {
            var3 = false;
         }
      }

      return var0.readString(var1 - 1 - var0.getPosition()).trim();
   }

   static void skipStyleBlock(ParsableByteArray var0) {
      while(!TextUtils.isEmpty(var0.readLine())) {
      }

   }

   static void skipWhitespaceAndComments(ParsableByteArray var0) {
      label24:
      while(true) {
         for(boolean var1 = true; var0.bytesLeft() > 0 && var1; var1 = false) {
            if (maybeSkipWhitespace(var0) || maybeSkipComment(var0)) {
               continue label24;
            }
         }

         return;
      }
   }

   public WebvttCssStyle parseBlock(ParsableByteArray var1) {
      this.stringBuilder.setLength(0);
      int var2 = var1.getPosition();
      skipStyleBlock(var1);
      this.styleInput.reset(var1.data, var1.getPosition());
      this.styleInput.setPosition(var2);
      String var3 = parseSelector(this.styleInput, this.stringBuilder);
      Object var4 = null;
      WebvttCssStyle var7 = (WebvttCssStyle)var4;
      if (var3 != null) {
         if (!"{".equals(parseNextToken(this.styleInput, this.stringBuilder))) {
            var7 = (WebvttCssStyle)var4;
         } else {
            WebvttCssStyle var5 = new WebvttCssStyle();
            this.applySelectorToStyle(var5, var3);
            var3 = null;
            boolean var8 = false;

            while(!var8) {
               int var6 = this.styleInput.getPosition();
               var3 = parseNextToken(this.styleInput, this.stringBuilder);
               if (var3 != null && !"}".equals(var3)) {
                  var8 = false;
               } else {
                  var8 = true;
               }

               if (!var8) {
                  this.styleInput.setPosition(var6);
                  parseStyleDeclaration(this.styleInput, var5, this.stringBuilder);
               }
            }

            var7 = (WebvttCssStyle)var4;
            if ("}".equals(var3)) {
               var7 = var5;
            }
         }
      }

      return var7;
   }
}
