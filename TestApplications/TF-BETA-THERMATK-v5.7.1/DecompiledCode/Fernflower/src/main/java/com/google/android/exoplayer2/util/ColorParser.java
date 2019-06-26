package com.google.android.exoplayer2.util;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorParser {
   private static final Map COLOR_MAP = new HashMap();
   private static final String RGB = "rgb";
   private static final String RGBA = "rgba";
   private static final Pattern RGBA_PATTERN_FLOAT_ALPHA = Pattern.compile("^rgba\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3}),(\\d*\\.?\\d*?)\\)$");
   private static final Pattern RGBA_PATTERN_INT_ALPHA = Pattern.compile("^rgba\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)$");
   private static final Pattern RGB_PATTERN = Pattern.compile("^rgb\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)$");

   static {
      COLOR_MAP.put("aliceblue", -984833);
      COLOR_MAP.put("antiquewhite", -332841);
      Map var0 = COLOR_MAP;
      Integer var1 = -16711681;
      var0.put("aqua", var1);
      COLOR_MAP.put("aquamarine", -8388652);
      COLOR_MAP.put("azure", -983041);
      COLOR_MAP.put("beige", -657956);
      COLOR_MAP.put("bisque", -6972);
      COLOR_MAP.put("black", -16777216);
      COLOR_MAP.put("blanchedalmond", -5171);
      COLOR_MAP.put("blue", -16776961);
      COLOR_MAP.put("blueviolet", -7722014);
      COLOR_MAP.put("brown", -5952982);
      COLOR_MAP.put("burlywood", -2180985);
      COLOR_MAP.put("cadetblue", -10510688);
      COLOR_MAP.put("chartreuse", -8388864);
      COLOR_MAP.put("chocolate", -2987746);
      COLOR_MAP.put("coral", -32944);
      COLOR_MAP.put("cornflowerblue", -10185235);
      COLOR_MAP.put("cornsilk", -1828);
      COLOR_MAP.put("crimson", -2354116);
      COLOR_MAP.put("cyan", var1);
      COLOR_MAP.put("darkblue", -16777077);
      COLOR_MAP.put("darkcyan", -16741493);
      COLOR_MAP.put("darkgoldenrod", -4684277);
      var0 = COLOR_MAP;
      var1 = -5658199;
      var0.put("darkgray", var1);
      COLOR_MAP.put("darkgreen", -16751616);
      COLOR_MAP.put("darkgrey", var1);
      COLOR_MAP.put("darkkhaki", -4343957);
      COLOR_MAP.put("darkmagenta", -7667573);
      COLOR_MAP.put("darkolivegreen", -11179217);
      COLOR_MAP.put("darkorange", -29696);
      COLOR_MAP.put("darkorchid", -6737204);
      COLOR_MAP.put("darkred", -7667712);
      COLOR_MAP.put("darksalmon", -1468806);
      COLOR_MAP.put("darkseagreen", -7357297);
      COLOR_MAP.put("darkslateblue", -12042869);
      var0 = COLOR_MAP;
      var1 = -13676721;
      var0.put("darkslategray", var1);
      COLOR_MAP.put("darkslategrey", var1);
      COLOR_MAP.put("darkturquoise", -16724271);
      COLOR_MAP.put("darkviolet", -7077677);
      COLOR_MAP.put("deeppink", -60269);
      COLOR_MAP.put("deepskyblue", -16728065);
      var0 = COLOR_MAP;
      var1 = -9868951;
      var0.put("dimgray", var1);
      COLOR_MAP.put("dimgrey", var1);
      COLOR_MAP.put("dodgerblue", -14774017);
      COLOR_MAP.put("firebrick", -5103070);
      COLOR_MAP.put("floralwhite", -1296);
      COLOR_MAP.put("forestgreen", -14513374);
      var0 = COLOR_MAP;
      var1 = -65281;
      var0.put("fuchsia", var1);
      COLOR_MAP.put("gainsboro", -2302756);
      COLOR_MAP.put("ghostwhite", -460545);
      COLOR_MAP.put("gold", -10496);
      COLOR_MAP.put("goldenrod", -2448096);
      Map var2 = COLOR_MAP;
      Integer var3 = -8355712;
      var2.put("gray", var3);
      COLOR_MAP.put("green", -16744448);
      COLOR_MAP.put("greenyellow", -5374161);
      COLOR_MAP.put("grey", var3);
      COLOR_MAP.put("honeydew", -983056);
      COLOR_MAP.put("hotpink", -38476);
      COLOR_MAP.put("indianred", -3318692);
      COLOR_MAP.put("indigo", -11861886);
      COLOR_MAP.put("ivory", -16);
      COLOR_MAP.put("khaki", -989556);
      COLOR_MAP.put("lavender", -1644806);
      COLOR_MAP.put("lavenderblush", -3851);
      COLOR_MAP.put("lawngreen", -8586240);
      COLOR_MAP.put("lemonchiffon", -1331);
      COLOR_MAP.put("lightblue", -5383962);
      COLOR_MAP.put("lightcoral", -1015680);
      COLOR_MAP.put("lightcyan", -2031617);
      COLOR_MAP.put("lightgoldenrodyellow", -329006);
      var2 = COLOR_MAP;
      var3 = -2894893;
      var2.put("lightgray", var3);
      COLOR_MAP.put("lightgreen", -7278960);
      COLOR_MAP.put("lightgrey", var3);
      COLOR_MAP.put("lightpink", -18751);
      COLOR_MAP.put("lightsalmon", -24454);
      COLOR_MAP.put("lightseagreen", -14634326);
      COLOR_MAP.put("lightskyblue", -7876870);
      COLOR_MAP.put("lightslategray", -8943463);
      COLOR_MAP.put("lightslategrey", -8943463);
      COLOR_MAP.put("lightsteelblue", -5192482);
      COLOR_MAP.put("lightyellow", -32);
      COLOR_MAP.put("lime", -16711936);
      COLOR_MAP.put("limegreen", -13447886);
      COLOR_MAP.put("linen", -331546);
      COLOR_MAP.put("magenta", var1);
      COLOR_MAP.put("maroon", -8388608);
      COLOR_MAP.put("mediumaquamarine", -10039894);
      COLOR_MAP.put("mediumblue", -16777011);
      COLOR_MAP.put("mediumorchid", -4565549);
      COLOR_MAP.put("mediumpurple", -7114533);
      COLOR_MAP.put("mediumseagreen", -12799119);
      COLOR_MAP.put("mediumslateblue", -8689426);
      COLOR_MAP.put("mediumspringgreen", -16713062);
      COLOR_MAP.put("mediumturquoise", -12004916);
      COLOR_MAP.put("mediumvioletred", -3730043);
      COLOR_MAP.put("midnightblue", -15132304);
      COLOR_MAP.put("mintcream", -655366);
      COLOR_MAP.put("mistyrose", -6943);
      COLOR_MAP.put("moccasin", -6987);
      COLOR_MAP.put("navajowhite", -8531);
      COLOR_MAP.put("navy", -16777088);
      COLOR_MAP.put("oldlace", -133658);
      COLOR_MAP.put("olive", -8355840);
      COLOR_MAP.put("olivedrab", -9728477);
      COLOR_MAP.put("orange", -23296);
      COLOR_MAP.put("orangered", -47872);
      COLOR_MAP.put("orchid", -2461482);
      COLOR_MAP.put("palegoldenrod", -1120086);
      COLOR_MAP.put("palegreen", -6751336);
      COLOR_MAP.put("paleturquoise", -5247250);
      COLOR_MAP.put("palevioletred", -2396013);
      COLOR_MAP.put("papayawhip", -4139);
      COLOR_MAP.put("peachpuff", -9543);
      COLOR_MAP.put("peru", -3308225);
      COLOR_MAP.put("pink", -16181);
      COLOR_MAP.put("plum", -2252579);
      COLOR_MAP.put("powderblue", -5185306);
      COLOR_MAP.put("purple", -8388480);
      COLOR_MAP.put("rebeccapurple", -10079335);
      COLOR_MAP.put("red", -65536);
      COLOR_MAP.put("rosybrown", -4419697);
      COLOR_MAP.put("royalblue", -12490271);
      COLOR_MAP.put("saddlebrown", -7650029);
      COLOR_MAP.put("salmon", -360334);
      COLOR_MAP.put("sandybrown", -744352);
      COLOR_MAP.put("seagreen", -13726889);
      COLOR_MAP.put("seashell", -2578);
      COLOR_MAP.put("sienna", -6270419);
      COLOR_MAP.put("silver", -4144960);
      COLOR_MAP.put("skyblue", -7876885);
      COLOR_MAP.put("slateblue", -9807155);
      COLOR_MAP.put("slategray", -9404272);
      COLOR_MAP.put("slategrey", -9404272);
      COLOR_MAP.put("snow", -1286);
      COLOR_MAP.put("springgreen", -16711809);
      COLOR_MAP.put("steelblue", -12156236);
      COLOR_MAP.put("tan", -2968436);
      COLOR_MAP.put("teal", -16744320);
      COLOR_MAP.put("thistle", -2572328);
      COLOR_MAP.put("tomato", -40121);
      COLOR_MAP.put("transparent", 0);
      COLOR_MAP.put("turquoise", -12525360);
      COLOR_MAP.put("violet", -1146130);
      COLOR_MAP.put("wheat", -663885);
      COLOR_MAP.put("white", -1);
      COLOR_MAP.put("whitesmoke", -657931);
      COLOR_MAP.put("yellow", -256);
      COLOR_MAP.put("yellowgreen", -6632142);
   }

   private ColorParser() {
   }

   private static int argb(int var0, int var1, int var2, int var3) {
      return var0 << 24 | var1 << 16 | var2 << 8 | var3;
   }

   private static int parseColorInternal(String var0, boolean var1) {
      Assertions.checkArgument(TextUtils.isEmpty(var0) ^ true);
      String var2 = var0.replace(" ", "");
      int var3;
      if (var2.charAt(0) == '#') {
         var3 = (int)Long.parseLong(var2.substring(1), 16);
         if (var2.length() == 7) {
            var3 |= -16777216;
         } else {
            if (var2.length() != 9) {
               throw new IllegalArgumentException();
            }

            var3 = (var3 & 255) << 24 | var3 >>> 8;
         }

         return var3;
      } else {
         Matcher var5;
         if (var2.startsWith("rgba")) {
            Pattern var4;
            if (var1) {
               var4 = RGBA_PATTERN_FLOAT_ALPHA;
            } else {
               var4 = RGBA_PATTERN_INT_ALPHA;
            }

            var5 = var4.matcher(var2);
            if (var5.matches()) {
               if (var1) {
                  var3 = (int)(Float.parseFloat(var5.group(4)) * 255.0F);
               } else {
                  var3 = Integer.parseInt(var5.group(4), 10);
               }

               return argb(var3, Integer.parseInt(var5.group(1), 10), Integer.parseInt(var5.group(2), 10), Integer.parseInt(var5.group(3), 10));
            }
         } else if (var2.startsWith("rgb")) {
            var5 = RGB_PATTERN.matcher(var2);
            if (var5.matches()) {
               return rgb(Integer.parseInt(var5.group(1), 10), Integer.parseInt(var5.group(2), 10), Integer.parseInt(var5.group(3), 10));
            }
         } else {
            Integer var6 = (Integer)COLOR_MAP.get(Util.toLowerInvariant(var2));
            if (var6 != null) {
               return var6;
            }
         }

         throw new IllegalArgumentException();
      }
   }

   public static int parseCssColor(String var0) {
      return parseColorInternal(var0, true);
   }

   public static int parseTtmlColor(String var0) {
      return parseColorInternal(var0, false);
   }

   private static int rgb(int var0, int var1, int var2) {
      return argb(255, var0, var1, var2);
   }
}
