package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VCardResultParser extends ResultParser {
   private static final Pattern BEGIN_VCARD = Pattern.compile("BEGIN:VCARD", 2);
   private static final Pattern COMMA = Pattern.compile(",");
   private static final Pattern CR_LF_SPACE_TAB = Pattern.compile("\r\n[ \t]");
   private static final Pattern EQUALS = Pattern.compile("=");
   private static final Pattern NEWLINE_ESCAPE = Pattern.compile("\\\\[nN]");
   private static final Pattern SEMICOLON = Pattern.compile(";");
   private static final Pattern SEMICOLON_OR_COMMA = Pattern.compile("[;,]");
   private static final Pattern UNESCAPED_SEMICOLONS = Pattern.compile("(?<!\\\\);+");
   private static final Pattern VCARD_ESCAPES = Pattern.compile("\\\\([,;\\\\])");
   private static final Pattern VCARD_LIKE_DATE = Pattern.compile("\\d{4}-?\\d{2}-?\\d{2}");

   private static String decodeQuotedPrintable(CharSequence var0, String var1) {
      int var2 = var0.length();
      StringBuilder var3 = new StringBuilder(var2);
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();

      int var7;
      for(int var5 = 0; var5 < var2; var5 = var7 + 1) {
         char var6 = var0.charAt(var5);
         var7 = var5;
         switch(var6) {
         case '\n':
         case '\r':
            break;
         case '=':
            var7 = var5;
            if (var5 < var2 - 2) {
               char var8 = var0.charAt(var5 + 1);
               var7 = var5;
               if (var8 != '\r') {
                  var7 = var5;
                  if (var8 != '\n') {
                     var6 = var0.charAt(var5 + 2);
                     var7 = parseHexDigit(var8);
                     int var9 = parseHexDigit(var6);
                     if (var7 >= 0 && var9 >= 0) {
                        var4.write((var7 << 4) + var9);
                     }

                     var7 = var5 + 2;
                  }
               }
            }
            break;
         default:
            maybeAppendFragment(var4, var1, var3);
            var3.append(var6);
            var7 = var5;
         }
      }

      maybeAppendFragment(var4, var1, var3);
      return var3.toString();
   }

   private static void formatNames(Iterable var0) {
      if (var0 != null) {
         Iterator var7 = var0.iterator();

         while(var7.hasNext()) {
            List var1 = (List)var7.next();
            String var2 = (String)var1.get(0);
            String[] var3 = new String[5];
            int var4 = 0;

            int var5;
            int var6;
            for(var5 = 0; var5 < 4; var4 = var6 + 1) {
               var6 = var2.indexOf(59, var4);
               if (var6 < 0) {
                  break;
               }

               var3[var5] = var2.substring(var4, var6);
               ++var5;
            }

            var3[var5] = var2.substring(var4);
            StringBuilder var8 = new StringBuilder(100);
            maybeAppendComponent(var3, 3, var8);
            maybeAppendComponent(var3, 1, var8);
            maybeAppendComponent(var3, 2, var8);
            maybeAppendComponent(var3, 0, var8);
            maybeAppendComponent(var3, 4, var8);
            var1.set(0, var8.toString().trim());
         }
      }

   }

   private static boolean isLikeVCardDate(CharSequence var0) {
      boolean var1;
      if (var0 != null && !VCARD_LIKE_DATE.matcher(var0).matches()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   static List matchSingleVCardPrefixedField(CharSequence var0, String var1, boolean var2, boolean var3) {
      List var4 = matchVCardPrefixedField(var0, var1, var2, var3);
      if (var4 != null && !var4.isEmpty()) {
         var4 = (List)var4.get(0);
      } else {
         var4 = null;
      }

      return var4;
   }

   static List matchVCardPrefixedField(CharSequence var0, String var1, boolean var2, boolean var3) {
      ArrayList var4 = null;
      int var5 = 0;
      int var6 = var1.length();

      while(var5 < var6) {
         Matcher var7 = Pattern.compile("(?:^|\n)" + var0 + "(?:;([^:]*))?:", 2).matcher(var1);
         int var8 = var5;
         if (var5 > 0) {
            var8 = var5 - 1;
         }

         if (!var7.find(var8)) {
            break;
         }

         int var9 = var7.end(0);
         String var10 = var7.group(1);
         ArrayList var11 = null;
         ArrayList var12 = null;
         boolean var20 = false;
         boolean var18 = false;
         String var13 = null;
         String var19 = null;
         int var15;
         if (var10 != null) {
            String[] var21 = SEMICOLON.split(var10);
            int var14 = var21.length;
            var15 = 0;

            while(true) {
               var11 = var12;
               var20 = var18;
               var13 = var19;
               if (var15 >= var14) {
                  break;
               }

               var13 = var21[var15];
               var11 = var12;
               if (var12 == null) {
                  var11 = new ArrayList(1);
               }

               var11.add(var13);
               String[] var22 = EQUALS.split(var13, 2);
               var20 = var18;
               var13 = var19;
               if (var22.length > 1) {
                  String var16 = var22[0];
                  String var23 = var22[1];
                  if ("ENCODING".equalsIgnoreCase(var16) && "QUOTED-PRINTABLE".equalsIgnoreCase(var23)) {
                     var20 = true;
                     var13 = var19;
                  } else {
                     var20 = var18;
                     var13 = var19;
                     if ("CHARSET".equalsIgnoreCase(var16)) {
                        var13 = var23;
                        var20 = var18;
                     }
                  }
               }

               ++var15;
               var12 = var11;
               var18 = var20;
               var19 = var13;
            }
         }

         var5 = var9;

         while(true) {
            var15 = var1.indexOf(10, var5);
            if (var15 < 0) {
               break;
            }

            if (var15 >= var1.length() - 1 || var1.charAt(var15 + 1) != ' ' && var1.charAt(var15 + 1) != '\t') {
               if (!var20 || (var15 <= 0 || var1.charAt(var15 - 1) != '=') && (var15 < 2 || var1.charAt(var15 - 2) != '=')) {
                  break;
               }

               var5 = var15 + 1;
            } else {
               var5 = var15 + 2;
            }
         }

         if (var15 < 0) {
            var5 = var6;
         } else if (var15 > var9) {
            var12 = var4;
            if (var4 == null) {
               var12 = new ArrayList(1);
            }

            var5 = var15;
            if (var15 > 0) {
               var5 = var15;
               if (var1.charAt(var15 - 1) == '\r') {
                  var5 = var15 - 1;
               }
            }

            String var17 = var1.substring(var9, var5);
            var19 = var17;
            if (var2) {
               var19 = var17.trim();
            }

            if (var20) {
               var17 = decodeQuotedPrintable(var19, var13);
               var19 = var17;
               if (var3) {
                  var19 = UNESCAPED_SEMICOLONS.matcher(var17).replaceAll("\n").trim();
               }
            } else {
               var17 = var19;
               if (var3) {
                  var17 = UNESCAPED_SEMICOLONS.matcher(var19).replaceAll("\n").trim();
               }

               var19 = CR_LF_SPACE_TAB.matcher(var17).replaceAll("");
               var19 = NEWLINE_ESCAPE.matcher(var19).replaceAll("\n");
               var19 = VCARD_ESCAPES.matcher(var19).replaceAll("$1");
            }

            if (var11 == null) {
               var11 = new ArrayList(1);
               var11.add(var19);
               var12.add(var11);
            } else {
               var11.add(0, var19);
               var12.add(var11);
            }

            ++var5;
            var4 = var12;
         } else {
            var5 = var15 + 1;
         }
      }

      return var4;
   }

   private static void maybeAppendComponent(String[] var0, int var1, StringBuilder var2) {
      if (var0[var1] != null && !var0[var1].isEmpty()) {
         if (var2.length() > 0) {
            var2.append(' ');
         }

         var2.append(var0[var1]);
      }

   }

   private static void maybeAppendFragment(ByteArrayOutputStream var0, String var1, StringBuilder var2) {
      if (var0.size() > 0) {
         byte[] var3 = var0.toByteArray();
         if (var1 == null) {
            var1 = new String(var3, Charset.forName("UTF-8"));
         } else {
            label23: {
               String var4;
               try {
                  var4 = new String(var3, var1);
               } catch (UnsupportedEncodingException var5) {
                  var1 = new String(var3, Charset.forName("UTF-8"));
                  break label23;
               }

               var1 = var4;
            }
         }

         var0.reset();
         var2.append(var1);
      }

   }

   private static String toPrimaryValue(List var0) {
      String var1;
      if (var0 != null && !var0.isEmpty()) {
         var1 = (String)var0.get(0);
      } else {
         var1 = null;
      }

      return var1;
   }

   private static String[] toPrimaryValues(Collection var0) {
      String[] var4;
      if (var0 != null && !var0.isEmpty()) {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var3 = (String)((List)var2.next()).get(0);
            if (var3 != null && !var3.isEmpty()) {
               var1.add(var3);
            }
         }

         var4 = (String[])var1.toArray(new String[var0.size()]);
      } else {
         var4 = null;
      }

      return var4;
   }

   private static String[] toTypes(Collection var0) {
      String[] var8;
      if (var0 != null && !var0.isEmpty()) {
         ArrayList var1 = new ArrayList(var0.size());

         String var6;
         for(Iterator var2 = var0.iterator(); var2.hasNext(); var1.add(var6)) {
            List var3 = (List)var2.next();
            Object var4 = null;
            int var5 = 1;

            while(true) {
               var6 = (String)var4;
               if (var5 >= var3.size()) {
                  break;
               }

               var6 = (String)var3.get(var5);
               int var7 = var6.indexOf(61);
               if (var7 < 0) {
                  break;
               }

               if ("TYPE".equalsIgnoreCase(var6.substring(0, var7))) {
                  var6 = var6.substring(var7 + 1);
                  break;
               }

               ++var5;
            }
         }

         var8 = (String[])var1.toArray(new String[var0.size()]);
      } else {
         var8 = null;
      }

      return var8;
   }

   public AddressBookParsedResult parse(Result var1) {
      String var2 = getMassagedText(var1);
      Matcher var14 = BEGIN_VCARD.matcher(var2);
      AddressBookParsedResult var15;
      if (var14.find() && var14.start() == 0) {
         List var16 = matchVCardPrefixedField("FN", var2, true, false);
         List var3 = var16;
         if (var16 == null) {
            var3 = matchVCardPrefixedField("N", var2, true, false);
            formatNames(var3);
         }

         var16 = matchSingleVCardPrefixedField("NICKNAME", var2, true, false);
         String[] var4;
         if (var16 == null) {
            var4 = null;
         } else {
            var4 = COMMA.split((CharSequence)var16.get(0));
         }

         List var5 = matchVCardPrefixedField("TEL", var2, true, false);
         List var6 = matchVCardPrefixedField("EMAIL", var2, true, false);
         List var7 = matchSingleVCardPrefixedField("NOTE", var2, false, false);
         List var8 = matchVCardPrefixedField("ADR", var2, true, true);
         List var9 = matchSingleVCardPrefixedField("ORG", var2, true, true);
         var16 = matchSingleVCardPrefixedField("BDAY", var2, true, false);
         List var10 = var16;
         if (var16 != null) {
            var10 = var16;
            if (!isLikeVCardDate((CharSequence)var16.get(0))) {
               var10 = null;
            }
         }

         List var11 = matchSingleVCardPrefixedField("TITLE", var2, true, false);
         List var12 = matchVCardPrefixedField("URL", var2, true, false);
         List var13 = matchSingleVCardPrefixedField("IMPP", var2, true, false);
         var16 = matchSingleVCardPrefixedField("GEO", var2, true, false);
         String[] var18;
         if (var16 == null) {
            var18 = null;
         } else {
            var18 = SEMICOLON_OR_COMMA.split((CharSequence)var16.get(0));
         }

         String[] var17 = var18;
         if (var18 != null) {
            var17 = var18;
            if (var18.length != 2) {
               var17 = null;
            }
         }

         var15 = new AddressBookParsedResult(toPrimaryValues(var3), var4, (String)null, toPrimaryValues(var5), toTypes(var5), toPrimaryValues(var6), toTypes(var6), toPrimaryValue(var13), toPrimaryValue(var7), toPrimaryValues(var8), toTypes(var8), toPrimaryValue(var9), toPrimaryValue(var10), toPrimaryValue(var11), toPrimaryValues(var12), var17);
      } else {
         var15 = null;
      }

      return var15;
   }
}
