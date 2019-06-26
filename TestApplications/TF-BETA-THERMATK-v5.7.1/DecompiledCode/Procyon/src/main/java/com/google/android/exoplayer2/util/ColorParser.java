// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.regex.Matcher;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.Map;

public final class ColorParser
{
    private static final Map<String, Integer> COLOR_MAP;
    private static final String RGB = "rgb";
    private static final String RGBA = "rgba";
    private static final Pattern RGBA_PATTERN_FLOAT_ALPHA;
    private static final Pattern RGBA_PATTERN_INT_ALPHA;
    private static final Pattern RGB_PATTERN;
    
    static {
        RGB_PATTERN = Pattern.compile("^rgb\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)$");
        RGBA_PATTERN_INT_ALPHA = Pattern.compile("^rgba\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)$");
        RGBA_PATTERN_FLOAT_ALPHA = Pattern.compile("^rgba\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3}),(\\d*\\.?\\d*?)\\)$");
        (COLOR_MAP = new HashMap<String, Integer>()).put("aliceblue", -984833);
        ColorParser.COLOR_MAP.put("antiquewhite", -332841);
        final Map<String, Integer> color_MAP = ColorParser.COLOR_MAP;
        final Integer value = -16711681;
        color_MAP.put("aqua", value);
        ColorParser.COLOR_MAP.put("aquamarine", -8388652);
        ColorParser.COLOR_MAP.put("azure", -983041);
        ColorParser.COLOR_MAP.put("beige", -657956);
        ColorParser.COLOR_MAP.put("bisque", -6972);
        ColorParser.COLOR_MAP.put("black", -16777216);
        ColorParser.COLOR_MAP.put("blanchedalmond", -5171);
        ColorParser.COLOR_MAP.put("blue", -16776961);
        ColorParser.COLOR_MAP.put("blueviolet", -7722014);
        ColorParser.COLOR_MAP.put("brown", -5952982);
        ColorParser.COLOR_MAP.put("burlywood", -2180985);
        ColorParser.COLOR_MAP.put("cadetblue", -10510688);
        ColorParser.COLOR_MAP.put("chartreuse", -8388864);
        ColorParser.COLOR_MAP.put("chocolate", -2987746);
        ColorParser.COLOR_MAP.put("coral", -32944);
        ColorParser.COLOR_MAP.put("cornflowerblue", -10185235);
        ColorParser.COLOR_MAP.put("cornsilk", -1828);
        ColorParser.COLOR_MAP.put("crimson", -2354116);
        ColorParser.COLOR_MAP.put("cyan", value);
        ColorParser.COLOR_MAP.put("darkblue", -16777077);
        ColorParser.COLOR_MAP.put("darkcyan", -16741493);
        ColorParser.COLOR_MAP.put("darkgoldenrod", -4684277);
        final Map<String, Integer> color_MAP2 = ColorParser.COLOR_MAP;
        final Integer value2 = -5658199;
        color_MAP2.put("darkgray", value2);
        ColorParser.COLOR_MAP.put("darkgreen", -16751616);
        ColorParser.COLOR_MAP.put("darkgrey", value2);
        ColorParser.COLOR_MAP.put("darkkhaki", -4343957);
        ColorParser.COLOR_MAP.put("darkmagenta", -7667573);
        ColorParser.COLOR_MAP.put("darkolivegreen", -11179217);
        ColorParser.COLOR_MAP.put("darkorange", -29696);
        ColorParser.COLOR_MAP.put("darkorchid", -6737204);
        ColorParser.COLOR_MAP.put("darkred", -7667712);
        ColorParser.COLOR_MAP.put("darksalmon", -1468806);
        ColorParser.COLOR_MAP.put("darkseagreen", -7357297);
        ColorParser.COLOR_MAP.put("darkslateblue", -12042869);
        final Map<String, Integer> color_MAP3 = ColorParser.COLOR_MAP;
        final Integer value3 = -13676721;
        color_MAP3.put("darkslategray", value3);
        ColorParser.COLOR_MAP.put("darkslategrey", value3);
        ColorParser.COLOR_MAP.put("darkturquoise", -16724271);
        ColorParser.COLOR_MAP.put("darkviolet", -7077677);
        ColorParser.COLOR_MAP.put("deeppink", -60269);
        ColorParser.COLOR_MAP.put("deepskyblue", -16728065);
        final Map<String, Integer> color_MAP4 = ColorParser.COLOR_MAP;
        final Integer value4 = -9868951;
        color_MAP4.put("dimgray", value4);
        ColorParser.COLOR_MAP.put("dimgrey", value4);
        ColorParser.COLOR_MAP.put("dodgerblue", -14774017);
        ColorParser.COLOR_MAP.put("firebrick", -5103070);
        ColorParser.COLOR_MAP.put("floralwhite", -1296);
        ColorParser.COLOR_MAP.put("forestgreen", -14513374);
        final Map<String, Integer> color_MAP5 = ColorParser.COLOR_MAP;
        final Integer value5 = -65281;
        color_MAP5.put("fuchsia", value5);
        ColorParser.COLOR_MAP.put("gainsboro", -2302756);
        ColorParser.COLOR_MAP.put("ghostwhite", -460545);
        ColorParser.COLOR_MAP.put("gold", -10496);
        ColorParser.COLOR_MAP.put("goldenrod", -2448096);
        final Map<String, Integer> color_MAP6 = ColorParser.COLOR_MAP;
        final Integer value6 = -8355712;
        color_MAP6.put("gray", value6);
        ColorParser.COLOR_MAP.put("green", -16744448);
        ColorParser.COLOR_MAP.put("greenyellow", -5374161);
        ColorParser.COLOR_MAP.put("grey", value6);
        ColorParser.COLOR_MAP.put("honeydew", -983056);
        ColorParser.COLOR_MAP.put("hotpink", -38476);
        ColorParser.COLOR_MAP.put("indianred", -3318692);
        ColorParser.COLOR_MAP.put("indigo", -11861886);
        ColorParser.COLOR_MAP.put("ivory", -16);
        ColorParser.COLOR_MAP.put("khaki", -989556);
        ColorParser.COLOR_MAP.put("lavender", -1644806);
        ColorParser.COLOR_MAP.put("lavenderblush", -3851);
        ColorParser.COLOR_MAP.put("lawngreen", -8586240);
        ColorParser.COLOR_MAP.put("lemonchiffon", -1331);
        ColorParser.COLOR_MAP.put("lightblue", -5383962);
        ColorParser.COLOR_MAP.put("lightcoral", -1015680);
        ColorParser.COLOR_MAP.put("lightcyan", -2031617);
        ColorParser.COLOR_MAP.put("lightgoldenrodyellow", -329006);
        final Map<String, Integer> color_MAP7 = ColorParser.COLOR_MAP;
        final Integer value7 = -2894893;
        color_MAP7.put("lightgray", value7);
        ColorParser.COLOR_MAP.put("lightgreen", -7278960);
        ColorParser.COLOR_MAP.put("lightgrey", value7);
        ColorParser.COLOR_MAP.put("lightpink", -18751);
        ColorParser.COLOR_MAP.put("lightsalmon", -24454);
        ColorParser.COLOR_MAP.put("lightseagreen", -14634326);
        ColorParser.COLOR_MAP.put("lightskyblue", -7876870);
        ColorParser.COLOR_MAP.put("lightslategray", -8943463);
        ColorParser.COLOR_MAP.put("lightslategrey", -8943463);
        ColorParser.COLOR_MAP.put("lightsteelblue", -5192482);
        ColorParser.COLOR_MAP.put("lightyellow", -32);
        ColorParser.COLOR_MAP.put("lime", -16711936);
        ColorParser.COLOR_MAP.put("limegreen", -13447886);
        ColorParser.COLOR_MAP.put("linen", -331546);
        ColorParser.COLOR_MAP.put("magenta", value5);
        ColorParser.COLOR_MAP.put("maroon", -8388608);
        ColorParser.COLOR_MAP.put("mediumaquamarine", -10039894);
        ColorParser.COLOR_MAP.put("mediumblue", -16777011);
        ColorParser.COLOR_MAP.put("mediumorchid", -4565549);
        ColorParser.COLOR_MAP.put("mediumpurple", -7114533);
        ColorParser.COLOR_MAP.put("mediumseagreen", -12799119);
        ColorParser.COLOR_MAP.put("mediumslateblue", -8689426);
        ColorParser.COLOR_MAP.put("mediumspringgreen", -16713062);
        ColorParser.COLOR_MAP.put("mediumturquoise", -12004916);
        ColorParser.COLOR_MAP.put("mediumvioletred", -3730043);
        ColorParser.COLOR_MAP.put("midnightblue", -15132304);
        ColorParser.COLOR_MAP.put("mintcream", -655366);
        ColorParser.COLOR_MAP.put("mistyrose", -6943);
        ColorParser.COLOR_MAP.put("moccasin", -6987);
        ColorParser.COLOR_MAP.put("navajowhite", -8531);
        ColorParser.COLOR_MAP.put("navy", -16777088);
        ColorParser.COLOR_MAP.put("oldlace", -133658);
        ColorParser.COLOR_MAP.put("olive", -8355840);
        ColorParser.COLOR_MAP.put("olivedrab", -9728477);
        ColorParser.COLOR_MAP.put("orange", -23296);
        ColorParser.COLOR_MAP.put("orangered", -47872);
        ColorParser.COLOR_MAP.put("orchid", -2461482);
        ColorParser.COLOR_MAP.put("palegoldenrod", -1120086);
        ColorParser.COLOR_MAP.put("palegreen", -6751336);
        ColorParser.COLOR_MAP.put("paleturquoise", -5247250);
        ColorParser.COLOR_MAP.put("palevioletred", -2396013);
        ColorParser.COLOR_MAP.put("papayawhip", -4139);
        ColorParser.COLOR_MAP.put("peachpuff", -9543);
        ColorParser.COLOR_MAP.put("peru", -3308225);
        ColorParser.COLOR_MAP.put("pink", -16181);
        ColorParser.COLOR_MAP.put("plum", -2252579);
        ColorParser.COLOR_MAP.put("powderblue", -5185306);
        ColorParser.COLOR_MAP.put("purple", -8388480);
        ColorParser.COLOR_MAP.put("rebeccapurple", -10079335);
        ColorParser.COLOR_MAP.put("red", -65536);
        ColorParser.COLOR_MAP.put("rosybrown", -4419697);
        ColorParser.COLOR_MAP.put("royalblue", -12490271);
        ColorParser.COLOR_MAP.put("saddlebrown", -7650029);
        ColorParser.COLOR_MAP.put("salmon", -360334);
        ColorParser.COLOR_MAP.put("sandybrown", -744352);
        ColorParser.COLOR_MAP.put("seagreen", -13726889);
        ColorParser.COLOR_MAP.put("seashell", -2578);
        ColorParser.COLOR_MAP.put("sienna", -6270419);
        ColorParser.COLOR_MAP.put("silver", -4144960);
        ColorParser.COLOR_MAP.put("skyblue", -7876885);
        ColorParser.COLOR_MAP.put("slateblue", -9807155);
        ColorParser.COLOR_MAP.put("slategray", -9404272);
        ColorParser.COLOR_MAP.put("slategrey", -9404272);
        ColorParser.COLOR_MAP.put("snow", -1286);
        ColorParser.COLOR_MAP.put("springgreen", -16711809);
        ColorParser.COLOR_MAP.put("steelblue", -12156236);
        ColorParser.COLOR_MAP.put("tan", -2968436);
        ColorParser.COLOR_MAP.put("teal", -16744320);
        ColorParser.COLOR_MAP.put("thistle", -2572328);
        ColorParser.COLOR_MAP.put("tomato", -40121);
        ColorParser.COLOR_MAP.put("transparent", 0);
        ColorParser.COLOR_MAP.put("turquoise", -12525360);
        ColorParser.COLOR_MAP.put("violet", -1146130);
        ColorParser.COLOR_MAP.put("wheat", -663885);
        ColorParser.COLOR_MAP.put("white", -1);
        ColorParser.COLOR_MAP.put("whitesmoke", -657931);
        ColorParser.COLOR_MAP.put("yellow", -256);
        ColorParser.COLOR_MAP.put("yellowgreen", -6632142);
    }
    
    private ColorParser() {
    }
    
    private static int argb(final int n, final int n2, final int n3, final int n4) {
        return n << 24 | n2 << 16 | n3 << 8 | n4;
    }
    
    private static int parseColorInternal(final String s, final boolean b) {
        Assertions.checkArgument(TextUtils.isEmpty((CharSequence)s) ^ true);
        final String replace = s.replace(" ", "");
        if (replace.charAt(0) == '#') {
            final int n = (int)Long.parseLong(replace.substring(1), 16);
            int n2;
            if (replace.length() == 7) {
                n2 = (0xFF000000 | n);
            }
            else {
                if (replace.length() != 9) {
                    throw new IllegalArgumentException();
                }
                n2 = ((n & 0xFF) << 24 | n >>> 8);
            }
            return n2;
        }
        if (replace.startsWith("rgba")) {
            Pattern pattern;
            if (b) {
                pattern = ColorParser.RGBA_PATTERN_FLOAT_ALPHA;
            }
            else {
                pattern = ColorParser.RGBA_PATTERN_INT_ALPHA;
            }
            final Matcher matcher = pattern.matcher(replace);
            if (matcher.matches()) {
                int int1;
                if (b) {
                    int1 = (int)(Float.parseFloat(matcher.group(4)) * 255.0f);
                }
                else {
                    int1 = Integer.parseInt(matcher.group(4), 10);
                }
                return argb(int1, Integer.parseInt(matcher.group(1), 10), Integer.parseInt(matcher.group(2), 10), Integer.parseInt(matcher.group(3), 10));
            }
        }
        else if (replace.startsWith("rgb")) {
            final Matcher matcher2 = ColorParser.RGB_PATTERN.matcher(replace);
            if (matcher2.matches()) {
                return rgb(Integer.parseInt(matcher2.group(1), 10), Integer.parseInt(matcher2.group(2), 10), Integer.parseInt(matcher2.group(3), 10));
            }
        }
        else {
            final Integer n3 = ColorParser.COLOR_MAP.get(Util.toLowerInvariant(replace));
            if (n3 != null) {
                return n3;
            }
        }
        throw new IllegalArgumentException();
    }
    
    public static int parseCssColor(final String s) {
        return parseColorInternal(s, true);
    }
    
    public static int parseTtmlColor(final String s) {
        return parseColorInternal(s, false);
    }
    
    private static int rgb(final int n, final int n2, final int n3) {
        return argb(255, n, n2, n3);
    }
}
