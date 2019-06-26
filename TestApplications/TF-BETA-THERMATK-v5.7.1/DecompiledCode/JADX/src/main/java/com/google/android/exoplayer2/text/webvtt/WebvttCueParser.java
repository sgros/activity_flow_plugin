package com.google.android.exoplayer2.text.webvtt;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.webvtt.WebvttCue.Builder;
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

    private static final class StartTag {
        private static final String[] NO_CLASSES = new String[0];
        public final String[] classes;
        public final String name;
        public final int position;
        public final String voice;

        private StartTag(String str, int i, String str2, String[] strArr) {
            this.position = i;
            this.name = str;
            this.voice = str2;
            this.classes = strArr;
        }

        public static StartTag buildStartTag(String str, int i) {
            str = str.trim();
            if (str.isEmpty()) {
                return null;
            }
            String str2;
            String trim;
            int indexOf = str.indexOf(" ");
            if (indexOf == -1) {
                str2 = "";
            } else {
                trim = str.substring(indexOf).trim();
                str = str.substring(0, indexOf);
                str2 = trim;
            }
            String[] split = Util.split(str, "\\.");
            trim = split[0];
            if (split.length > 1) {
                split = (String[]) Arrays.copyOfRange(split, 1, split.length);
            } else {
                split = NO_CLASSES;
            }
            return new StartTag(trim, i, str2, split);
        }

        public static StartTag buildWholeCueVirtualTag() {
            String str = "";
            return new StartTag(str, 0, str, new String[0]);
        }
    }

    private static final class StyleMatch implements Comparable<StyleMatch> {
        public final int score;
        public final WebvttCssStyle style;

        public StyleMatch(int i, WebvttCssStyle webvttCssStyle) {
            this.score = i;
            this.style = webvttCssStyle;
        }

        public int compareTo(StyleMatch styleMatch) {
            return this.score - styleMatch.score;
        }
    }

    public boolean parseCue(ParsableByteArray parsableByteArray, Builder builder, List<WebvttCssStyle> list) {
        String readLine = parsableByteArray.readLine();
        if (readLine == null) {
            return false;
        }
        Matcher matcher = CUE_HEADER_PATTERN.matcher(readLine);
        if (matcher.matches()) {
            return parseCue(null, matcher, parsableByteArray, builder, this.textBuilder, list);
        }
        String readLine2 = parsableByteArray.readLine();
        if (readLine2 == null) {
            return false;
        }
        Matcher matcher2 = CUE_HEADER_PATTERN.matcher(readLine2);
        if (!matcher2.matches()) {
            return false;
        }
        return parseCue(readLine.trim(), matcher2, parsableByteArray, builder, this.textBuilder, list);
    }

    static void parseCueSettingsList(String str, Builder builder) {
        String str2 = "WebvttCueParser";
        Matcher matcher = CUE_SETTING_PATTERN.matcher(str);
        while (matcher.find()) {
            String group = matcher.group(1);
            String group2 = matcher.group(2);
            try {
                if ("line".equals(group)) {
                    parseLineAttribute(group2, builder);
                } else if ("align".equals(group)) {
                    builder.setTextAlignment(parseTextAlignment(group2));
                } else if ("position".equals(group)) {
                    parsePositionAttribute(group2, builder);
                } else if ("size".equals(group)) {
                    builder.setWidth(WebvttParserUtil.parsePercentage(group2));
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown cue setting ");
                    stringBuilder.append(group);
                    stringBuilder.append(":");
                    stringBuilder.append(group2);
                    Log.m18w(str2, stringBuilder.toString());
                }
            } catch (NumberFormatException unused) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Skipping bad cue setting: ");
                stringBuilder2.append(matcher.group());
                Log.m18w(str2, stringBuilder2.toString());
            }
        }
    }

    static void parseCueText(String str, String str2, Builder builder, List<WebvttCssStyle> list) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ArrayDeque arrayDeque = new ArrayDeque();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < str2.length()) {
            char charAt = str2.charAt(i);
            if (charAt == '&') {
                i++;
                int indexOf = str2.indexOf(59, i);
                int indexOf2 = str2.indexOf(32, i);
                if (indexOf == -1) {
                    indexOf = indexOf2;
                } else if (indexOf2 != -1) {
                    indexOf = Math.min(indexOf, indexOf2);
                }
                if (indexOf != -1) {
                    applyEntity(str2.substring(i, indexOf), spannableStringBuilder);
                    if (indexOf == indexOf2) {
                        spannableStringBuilder.append(" ");
                    }
                    i = indexOf + 1;
                } else {
                    spannableStringBuilder.append(charAt);
                }
            } else if (charAt != '<') {
                spannableStringBuilder.append(charAt);
                i++;
            } else {
                int i2 = i + 1;
                if (i2 < str2.length()) {
                    int i3 = 1;
                    Object obj = str2.charAt(i2) == '/' ? 1 : null;
                    i2 = findEndOfTag(str2, i2);
                    int i4 = i2 - 2;
                    Object obj2 = str2.charAt(i4) == '/' ? 1 : null;
                    if (obj != null) {
                        i3 = 2;
                    }
                    i += i3;
                    if (obj2 == null) {
                        i4 = i2 - 1;
                    }
                    String substring = str2.substring(i, i4);
                    String tagName = getTagName(substring);
                    if (tagName != null && isSupportedTag(tagName)) {
                        if (obj != null) {
                            while (!arrayDeque.isEmpty()) {
                                StartTag startTag = (StartTag) arrayDeque.pop();
                                applySpansForTag(str, startTag, spannableStringBuilder, list, arrayList);
                                if (startTag.name.equals(tagName)) {
                                    break;
                                }
                            }
                        } else if (obj2 == null) {
                            arrayDeque.push(StartTag.buildStartTag(substring, spannableStringBuilder.length()));
                        }
                    }
                }
                i = i2;
            }
        }
        while (!arrayDeque.isEmpty()) {
            applySpansForTag(str, (StartTag) arrayDeque.pop(), spannableStringBuilder, list, arrayList);
        }
        applySpansForTag(str, StartTag.buildWholeCueVirtualTag(), spannableStringBuilder, list, arrayList);
        builder.setText(spannableStringBuilder);
    }

    private static boolean parseCue(String str, Matcher matcher, ParsableByteArray parsableByteArray, Builder builder, StringBuilder stringBuilder, List<WebvttCssStyle> list) {
        try {
            builder.setStartTime(WebvttParserUtil.parseTimestampUs(matcher.group(1)));
            builder.setEndTime(WebvttParserUtil.parseTimestampUs(matcher.group(2)));
            parseCueSettingsList(matcher.group(3), builder);
            stringBuilder.setLength(0);
            while (true) {
                String readLine = parsableByteArray.readLine();
                if (TextUtils.isEmpty(readLine)) {
                    parseCueText(str, stringBuilder.toString(), builder, list);
                    return true;
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(readLine.trim());
            }
        } catch (NumberFormatException unused) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Skipping cue with bad header: ");
            stringBuilder2.append(matcher.group());
            Log.m18w("WebvttCueParser", stringBuilder2.toString());
            return false;
        }
    }

    private static void parseLineAttribute(String str, Builder builder) throws NumberFormatException {
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            builder.setLineAnchor(parsePositionAnchor(str.substring(indexOf + 1)));
            str = str.substring(0, indexOf);
        } else {
            builder.setLineAnchor(Integer.MIN_VALUE);
        }
        if (str.endsWith("%")) {
            builder.setLine(WebvttParserUtil.parsePercentage(str));
            builder.setLineType(0);
            return;
        }
        int parseInt = Integer.parseInt(str);
        if (parseInt < 0) {
            parseInt--;
        }
        builder.setLine((float) parseInt);
        builder.setLineType(1);
    }

    private static void parsePositionAttribute(String str, Builder builder) throws NumberFormatException {
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            builder.setPositionAnchor(parsePositionAnchor(str.substring(indexOf + 1)));
            str = str.substring(0, indexOf);
        } else {
            builder.setPositionAnchor(Integer.MIN_VALUE);
        }
        builder.setPosition(WebvttParserUtil.parsePercentage(str));
    }

    private static int parsePositionAnchor(java.lang.String r5) {
        /*
        r0 = r5.hashCode();
        r1 = 0;
        r2 = 3;
        r3 = 2;
        r4 = 1;
        switch(r0) {
            case -1364013995: goto L_0x002a;
            case -1074341483: goto L_0x0020;
            case 100571: goto L_0x0016;
            case 109757538: goto L_0x000c;
            default: goto L_0x000b;
        };
    L_0x000b:
        goto L_0x0034;
    L_0x000c:
        r0 = "start";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0034;
    L_0x0014:
        r0 = 0;
        goto L_0x0035;
    L_0x0016:
        r0 = "end";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0034;
    L_0x001e:
        r0 = 3;
        goto L_0x0035;
    L_0x0020:
        r0 = "middle";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0034;
    L_0x0028:
        r0 = 2;
        goto L_0x0035;
    L_0x002a:
        r0 = "center";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0034;
    L_0x0032:
        r0 = 1;
        goto L_0x0035;
    L_0x0034:
        r0 = -1;
    L_0x0035:
        if (r0 == 0) goto L_0x0058;
    L_0x0037:
        if (r0 == r4) goto L_0x0057;
    L_0x0039:
        if (r0 == r3) goto L_0x0057;
    L_0x003b:
        if (r0 == r2) goto L_0x0056;
    L_0x003d:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Invalid anchor value: ";
        r0.append(r1);
        r0.append(r5);
        r5 = r0.toString();
        r0 = "WebvttCueParser";
        com.google.android.exoplayer2.util.Log.m18w(r0, r5);
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        return r5;
    L_0x0056:
        return r3;
    L_0x0057:
        return r4;
    L_0x0058:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.parsePositionAnchor(java.lang.String):int");
    }

    private static android.text.Layout.Alignment parseTextAlignment(java.lang.String r6) {
        /*
        r0 = r6.hashCode();
        r1 = 5;
        r2 = 4;
        r3 = 3;
        r4 = 2;
        r5 = 1;
        switch(r0) {
            case -1364013995: goto L_0x003f;
            case -1074341483: goto L_0x0035;
            case 100571: goto L_0x002b;
            case 3317767: goto L_0x0021;
            case 108511772: goto L_0x0017;
            case 109757538: goto L_0x000d;
            default: goto L_0x000c;
        };
    L_0x000c:
        goto L_0x0049;
    L_0x000d:
        r0 = "start";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0049;
    L_0x0015:
        r0 = 0;
        goto L_0x004a;
    L_0x0017:
        r0 = "right";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0049;
    L_0x001f:
        r0 = 5;
        goto L_0x004a;
    L_0x0021:
        r0 = "left";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0049;
    L_0x0029:
        r0 = 1;
        goto L_0x004a;
    L_0x002b:
        r0 = "end";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0049;
    L_0x0033:
        r0 = 4;
        goto L_0x004a;
    L_0x0035:
        r0 = "middle";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0049;
    L_0x003d:
        r0 = 3;
        goto L_0x004a;
    L_0x003f:
        r0 = "center";
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0049;
    L_0x0047:
        r0 = 2;
        goto L_0x004a;
    L_0x0049:
        r0 = -1;
    L_0x004a:
        if (r0 == 0) goto L_0x0074;
    L_0x004c:
        if (r0 == r5) goto L_0x0074;
    L_0x004e:
        if (r0 == r4) goto L_0x0071;
    L_0x0050:
        if (r0 == r3) goto L_0x0071;
    L_0x0052:
        if (r0 == r2) goto L_0x006e;
    L_0x0054:
        if (r0 == r1) goto L_0x006e;
    L_0x0056:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Invalid alignment value: ";
        r0.append(r1);
        r0.append(r6);
        r6 = r0.toString();
        r0 = "WebvttCueParser";
        com.google.android.exoplayer2.util.Log.m18w(r0, r6);
        r6 = 0;
        return r6;
    L_0x006e:
        r6 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        return r6;
    L_0x0071:
        r6 = android.text.Layout.Alignment.ALIGN_CENTER;
        return r6;
    L_0x0074:
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.parseTextAlignment(java.lang.String):android.text.Layout$Alignment");
    }

    private static int findEndOfTag(String str, int i) {
        i = str.indexOf(62, i);
        return i == -1 ? str.length() : i + 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079  */
    private static void applyEntity(java.lang.String r5, android.text.SpannableStringBuilder r6) {
        /*
        r0 = r5.hashCode();
        r1 = 3309; // 0xced float:4.637E-42 double:1.635E-320;
        r2 = 3;
        r3 = 2;
        r4 = 1;
        if (r0 == r1) goto L_0x0038;
    L_0x000b:
        r1 = 3464; // 0xd88 float:4.854E-42 double:1.7114E-320;
        if (r0 == r1) goto L_0x002e;
    L_0x000f:
        r1 = 96708; // 0x179c4 float:1.35517E-40 double:4.778E-319;
        if (r0 == r1) goto L_0x0024;
    L_0x0014:
        r1 = 3374865; // 0x337f11 float:4.729193E-39 double:1.667405E-317;
        if (r0 == r1) goto L_0x001a;
    L_0x0019:
        goto L_0x0042;
    L_0x001a:
        r0 = "nbsp";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0042;
    L_0x0022:
        r0 = 2;
        goto L_0x0043;
    L_0x0024:
        r0 = "amp";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0042;
    L_0x002c:
        r0 = 3;
        goto L_0x0043;
    L_0x002e:
        r0 = "lt";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0042;
    L_0x0036:
        r0 = 0;
        goto L_0x0043;
    L_0x0038:
        r0 = "gt";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x0042;
    L_0x0040:
        r0 = 1;
        goto L_0x0043;
    L_0x0042:
        r0 = -1;
    L_0x0043:
        if (r0 == 0) goto L_0x0079;
    L_0x0045:
        if (r0 == r4) goto L_0x0073;
    L_0x0047:
        if (r0 == r3) goto L_0x006d;
    L_0x0049:
        if (r0 == r2) goto L_0x0067;
    L_0x004b:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r0 = "ignoring unsupported entity: '&";
        r6.append(r0);
        r6.append(r5);
        r5 = ";'";
        r6.append(r5);
        r5 = r6.toString();
        r6 = "WebvttCueParser";
        com.google.android.exoplayer2.util.Log.m18w(r6, r5);
        goto L_0x007e;
    L_0x0067:
        r5 = 38;
        r6.append(r5);
        goto L_0x007e;
    L_0x006d:
        r5 = 32;
        r6.append(r5);
        goto L_0x007e;
    L_0x0073:
        r5 = 62;
        r6.append(r5);
        goto L_0x007e;
    L_0x0079:
        r5 = 60;
        r6.append(r5);
    L_0x007e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.applyEntity(java.lang.String, android.text.SpannableStringBuilder):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0063 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0063 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0063 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0063 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0063 A:{SKIP} */
    private static boolean isSupportedTag(java.lang.String r8) {
        /*
        r0 = r8.hashCode();
        r1 = 98;
        r2 = 0;
        r3 = 5;
        r4 = 4;
        r5 = 3;
        r6 = 2;
        r7 = 1;
        if (r0 == r1) goto L_0x0056;
    L_0x000e:
        r1 = 99;
        if (r0 == r1) goto L_0x004c;
    L_0x0012:
        r1 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        if (r0 == r1) goto L_0x0042;
    L_0x0016:
        r1 = 3314158; // 0x3291ee float:4.644125E-39 double:1.6374116E-317;
        if (r0 == r1) goto L_0x0038;
    L_0x001b:
        r1 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        if (r0 == r1) goto L_0x002e;
    L_0x001f:
        r1 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r0 == r1) goto L_0x0024;
    L_0x0023:
        goto L_0x0060;
    L_0x0024:
        r0 = "v";
        r8 = r8.equals(r0);
        if (r8 == 0) goto L_0x0060;
    L_0x002c:
        r8 = 5;
        goto L_0x0061;
    L_0x002e:
        r0 = "u";
        r8 = r8.equals(r0);
        if (r8 == 0) goto L_0x0060;
    L_0x0036:
        r8 = 4;
        goto L_0x0061;
    L_0x0038:
        r0 = "lang";
        r8 = r8.equals(r0);
        if (r8 == 0) goto L_0x0060;
    L_0x0040:
        r8 = 3;
        goto L_0x0061;
    L_0x0042:
        r0 = "i";
        r8 = r8.equals(r0);
        if (r8 == 0) goto L_0x0060;
    L_0x004a:
        r8 = 2;
        goto L_0x0061;
    L_0x004c:
        r0 = "c";
        r8 = r8.equals(r0);
        if (r8 == 0) goto L_0x0060;
    L_0x0054:
        r8 = 1;
        goto L_0x0061;
    L_0x0056:
        r0 = "b";
        r8 = r8.equals(r0);
        if (r8 == 0) goto L_0x0060;
    L_0x005e:
        r8 = 0;
        goto L_0x0061;
    L_0x0060:
        r8 = -1;
    L_0x0061:
        if (r8 == 0) goto L_0x006e;
    L_0x0063:
        if (r8 == r7) goto L_0x006e;
    L_0x0065:
        if (r8 == r6) goto L_0x006e;
    L_0x0067:
        if (r8 == r5) goto L_0x006e;
    L_0x0069:
        if (r8 == r4) goto L_0x006e;
    L_0x006b:
        if (r8 == r3) goto L_0x006e;
    L_0x006d:
        return r2;
    L_0x006e:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.isSupportedTag(java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0077 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009e A:{LOOP_END, LOOP:0: B:43:0x009c->B:44:0x009e} */
    private static void applySpansForTag(java.lang.String r8, com.google.android.exoplayer2.text.webvtt.WebvttCueParser.StartTag r9, android.text.SpannableStringBuilder r10, java.util.List<com.google.android.exoplayer2.text.webvtt.WebvttCssStyle> r11, java.util.List<com.google.android.exoplayer2.text.webvtt.WebvttCueParser.StyleMatch> r12) {
        /*
        r0 = r9.position;
        r1 = r10.length();
        r2 = r9.name;
        r3 = r2.hashCode();
        r4 = 0;
        r5 = 2;
        r6 = 1;
        if (r3 == 0) goto L_0x0067;
    L_0x0011:
        r7 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        if (r3 == r7) goto L_0x005d;
    L_0x0015:
        r7 = 3314158; // 0x3291ee float:4.644125E-39 double:1.6374116E-317;
        if (r3 == r7) goto L_0x0053;
    L_0x001a:
        r7 = 98;
        if (r3 == r7) goto L_0x0049;
    L_0x001e:
        r7 = 99;
        if (r3 == r7) goto L_0x003f;
    L_0x0022:
        r7 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        if (r3 == r7) goto L_0x0035;
    L_0x0026:
        r7 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r3 == r7) goto L_0x002b;
    L_0x002a:
        goto L_0x0071;
    L_0x002b:
        r3 = "v";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0033:
        r2 = 5;
        goto L_0x0072;
    L_0x0035:
        r3 = "u";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x003d:
        r2 = 2;
        goto L_0x0072;
    L_0x003f:
        r3 = "c";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0047:
        r2 = 3;
        goto L_0x0072;
    L_0x0049:
        r3 = "b";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0051:
        r2 = 0;
        goto L_0x0072;
    L_0x0053:
        r3 = "lang";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x005b:
        r2 = 4;
        goto L_0x0072;
    L_0x005d:
        r3 = "i";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0065:
        r2 = 1;
        goto L_0x0072;
    L_0x0067:
        r3 = "";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x006f:
        r2 = 6;
        goto L_0x0072;
    L_0x0071:
        r2 = -1;
    L_0x0072:
        r3 = 33;
        switch(r2) {
            case 0: goto L_0x008a;
            case 1: goto L_0x0081;
            case 2: goto L_0x0078;
            case 3: goto L_0x0092;
            case 4: goto L_0x0092;
            case 5: goto L_0x0092;
            case 6: goto L_0x0092;
            default: goto L_0x0077;
        };
    L_0x0077:
        return;
    L_0x0078:
        r2 = new android.text.style.UnderlineSpan;
        r2.<init>();
        r10.setSpan(r2, r0, r1, r3);
        goto L_0x0092;
    L_0x0081:
        r2 = new android.text.style.StyleSpan;
        r2.<init>(r5);
        r10.setSpan(r2, r0, r1, r3);
        goto L_0x0092;
    L_0x008a:
        r2 = new android.text.style.StyleSpan;
        r2.<init>(r6);
        r10.setSpan(r2, r0, r1, r3);
    L_0x0092:
        r12.clear();
        getApplicableStyles(r11, r8, r9, r12);
        r8 = r12.size();
    L_0x009c:
        if (r4 >= r8) goto L_0x00ac;
    L_0x009e:
        r9 = r12.get(r4);
        r9 = (com.google.android.exoplayer2.text.webvtt.WebvttCueParser.StyleMatch) r9;
        r9 = r9.style;
        applyStyleToText(r10, r9, r0, r1);
        r4 = r4 + 1;
        goto L_0x009c;
    L_0x00ac:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.applySpansForTag(java.lang.String, com.google.android.exoplayer2.text.webvtt.WebvttCueParser$StartTag, android.text.SpannableStringBuilder, java.util.List, java.util.List):void");
    }

    private static void applyStyleToText(SpannableStringBuilder spannableStringBuilder, WebvttCssStyle webvttCssStyle, int i, int i2) {
        if (webvttCssStyle != null) {
            if (webvttCssStyle.getStyle() != -1) {
                spannableStringBuilder.setSpan(new StyleSpan(webvttCssStyle.getStyle()), i, i2, 33);
            }
            if (webvttCssStyle.isLinethrough()) {
                spannableStringBuilder.setSpan(new StrikethroughSpan(), i, i2, 33);
            }
            if (webvttCssStyle.isUnderline()) {
                spannableStringBuilder.setSpan(new UnderlineSpan(), i, i2, 33);
            }
            if (webvttCssStyle.hasFontColor()) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(webvttCssStyle.getFontColor()), i, i2, 33);
            }
            if (webvttCssStyle.hasBackgroundColor()) {
                spannableStringBuilder.setSpan(new BackgroundColorSpan(webvttCssStyle.getBackgroundColor()), i, i2, 33);
            }
            if (webvttCssStyle.getFontFamily() != null) {
                spannableStringBuilder.setSpan(new TypefaceSpan(webvttCssStyle.getFontFamily()), i, i2, 33);
            }
            if (webvttCssStyle.getTextAlign() != null) {
                spannableStringBuilder.setSpan(new Standard(webvttCssStyle.getTextAlign()), i, i2, 33);
            }
            int fontSizeUnit = webvttCssStyle.getFontSizeUnit();
            if (fontSizeUnit == 1) {
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan((int) webvttCssStyle.getFontSize(), true), i, i2, 33);
            } else if (fontSizeUnit == 2) {
                spannableStringBuilder.setSpan(new RelativeSizeSpan(webvttCssStyle.getFontSize()), i, i2, 33);
            } else if (fontSizeUnit == 3) {
                spannableStringBuilder.setSpan(new RelativeSizeSpan(webvttCssStyle.getFontSize() / 100.0f), i, i2, 33);
            }
        }
    }

    private static String getTagName(String str) {
        str = str.trim();
        if (str.isEmpty()) {
            return null;
        }
        return Util.splitAtFirst(str, "[ \\.]")[0];
    }

    private static void getApplicableStyles(List<WebvttCssStyle> list, String str, StartTag startTag, List<StyleMatch> list2) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            WebvttCssStyle webvttCssStyle = (WebvttCssStyle) list.get(i);
            int specificityScore = webvttCssStyle.getSpecificityScore(str, startTag.name, startTag.classes, startTag.voice);
            if (specificityScore > 0) {
                list2.add(new StyleMatch(specificityScore, webvttCssStyle));
            }
        }
        Collections.sort(list2);
    }
}
