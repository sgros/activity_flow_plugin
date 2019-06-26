package com.google.android.exoplayer2.text.subrip;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
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
    private final ArrayList<String> tags = new ArrayList();
    private final StringBuilder textBuilder = new StringBuilder();

    static float getFractionalPositionForAnchorType(int i) {
        return i != 0 ? i != 1 ? 0.92f : 0.5f : 0.08f;
    }

    public SubripDecoder() {
        super("SubripDecoder");
    }

    /* Access modifiers changed, original: protected */
    public SubripSubtitle decode(byte[] bArr, int i, boolean z) {
        String str = "SubripDecoder";
        ArrayList arrayList = new ArrayList();
        LongArray longArray = new LongArray();
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr, i);
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                break;
            } else if (readLine.length() != 0) {
                StringBuilder stringBuilder;
                try {
                    Integer.parseInt(readLine);
                    readLine = parsableByteArray.readLine();
                    if (readLine == null) {
                        Log.m18w(str, "Unexpected end");
                        break;
                    }
                    Matcher matcher = SUBRIP_TIMING_LINE.matcher(readLine);
                    if (matcher.matches()) {
                        String str2;
                        int i2 = 1;
                        longArray.add(parseTimecode(matcher, 1));
                        int i3 = 0;
                        if (TextUtils.isEmpty(matcher.group(6))) {
                            i2 = 0;
                        } else {
                            longArray.add(parseTimecode(matcher, 6));
                        }
                        this.textBuilder.setLength(0);
                        this.tags.clear();
                        while (true) {
                            String readLine2 = parsableByteArray.readLine();
                            if (TextUtils.isEmpty(readLine2)) {
                                break;
                            }
                            if (this.textBuilder.length() > 0) {
                                this.textBuilder.append("<br>");
                            }
                            this.textBuilder.append(processLine(readLine2, this.tags));
                        }
                        Spanned fromHtml = Html.fromHtml(this.textBuilder.toString());
                        while (i3 < this.tags.size()) {
                            str2 = (String) this.tags.get(i3);
                            if (str2.matches("\\{\\\\an[1-9]\\}")) {
                                break;
                            }
                            i3++;
                        }
                        str2 = null;
                        arrayList.add(buildCue(fromHtml, str2));
                        if (i2 != 0) {
                            arrayList.add(null);
                        }
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Skipping invalid timing: ");
                        stringBuilder.append(readLine);
                        Log.m18w(str, stringBuilder.toString());
                    }
                } catch (NumberFormatException unused) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Skipping invalid index: ");
                    stringBuilder.append(readLine);
                    Log.m18w(str, stringBuilder.toString());
                }
            }
        }
        Cue[] cueArr = new Cue[arrayList.size()];
        arrayList.toArray(cueArr);
        return new SubripSubtitle(cueArr, longArray.toArray());
    }

    private String processLine(String str, ArrayList<String> arrayList) {
        str = str.trim();
        StringBuilder stringBuilder = new StringBuilder(str);
        Matcher matcher = SUBRIP_TAG_PATTERN.matcher(str);
        int i = 0;
        while (matcher.find()) {
            String group = matcher.group();
            arrayList.add(group);
            int start = matcher.start() - i;
            int length = group.length();
            stringBuilder.replace(start, start + length, "");
            i += length;
        }
        return stringBuilder.toString();
    }

    private com.google.android.exoplayer2.text.Cue buildCue(android.text.Spanned r18, java.lang.String r19) {
        /*
        r17 = this;
        r0 = r19;
        if (r0 != 0) goto L_0x000c;
    L_0x0004:
        r0 = new com.google.android.exoplayer2.text.Cue;
        r2 = r18;
        r0.<init>(r2);
        return r0;
    L_0x000c:
        r2 = r18;
        r1 = r19.hashCode();
        r3 = "{\\an8}";
        r4 = "{\\an7}";
        r5 = "{\\an6}";
        r6 = "{\\an5}";
        r7 = "{\\an4}";
        r8 = "{\\an3}";
        r9 = "{\\an2}";
        r10 = "{\\an1}";
        r13 = 5;
        r14 = 4;
        r15 = 3;
        r11 = 2;
        r12 = 1;
        switch(r1) {
            case -685620710: goto L_0x006e;
            case -685620679: goto L_0x0066;
            case -685620648: goto L_0x005e;
            case -685620617: goto L_0x0056;
            case -685620586: goto L_0x004e;
            case -685620555: goto L_0x0046;
            case -685620524: goto L_0x003e;
            case -685620493: goto L_0x0035;
            case -685620462: goto L_0x002b;
            default: goto L_0x002a;
        };
    L_0x002a:
        goto L_0x0076;
    L_0x002b:
        r1 = "{\\an9}";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x0076;
    L_0x0033:
        r1 = 5;
        goto L_0x0077;
    L_0x0035:
        r1 = r0.equals(r3);
        if (r1 == 0) goto L_0x0076;
    L_0x003b:
        r1 = 8;
        goto L_0x0077;
    L_0x003e:
        r1 = r0.equals(r4);
        if (r1 == 0) goto L_0x0076;
    L_0x0044:
        r1 = 2;
        goto L_0x0077;
    L_0x0046:
        r1 = r0.equals(r5);
        if (r1 == 0) goto L_0x0076;
    L_0x004c:
        r1 = 4;
        goto L_0x0077;
    L_0x004e:
        r1 = r0.equals(r6);
        if (r1 == 0) goto L_0x0076;
    L_0x0054:
        r1 = 7;
        goto L_0x0077;
    L_0x0056:
        r1 = r0.equals(r7);
        if (r1 == 0) goto L_0x0076;
    L_0x005c:
        r1 = 1;
        goto L_0x0077;
    L_0x005e:
        r1 = r0.equals(r8);
        if (r1 == 0) goto L_0x0076;
    L_0x0064:
        r1 = 3;
        goto L_0x0077;
    L_0x0066:
        r1 = r0.equals(r9);
        if (r1 == 0) goto L_0x0076;
    L_0x006c:
        r1 = 6;
        goto L_0x0077;
    L_0x006e:
        r1 = r0.equals(r10);
        if (r1 == 0) goto L_0x0076;
    L_0x0074:
        r1 = 0;
        goto L_0x0077;
    L_0x0076:
        r1 = -1;
    L_0x0077:
        if (r1 == 0) goto L_0x0089;
    L_0x0079:
        if (r1 == r12) goto L_0x0089;
    L_0x007b:
        if (r1 == r11) goto L_0x0089;
    L_0x007d:
        if (r1 == r15) goto L_0x0086;
    L_0x007f:
        if (r1 == r14) goto L_0x0086;
    L_0x0081:
        if (r1 == r13) goto L_0x0086;
    L_0x0083:
        r16 = 1;
        goto L_0x008b;
    L_0x0086:
        r16 = 2;
        goto L_0x008b;
    L_0x0089:
        r16 = 0;
    L_0x008b:
        r1 = r19.hashCode();
        switch(r1) {
            case -685620710: goto L_0x00d6;
            case -685620679: goto L_0x00ce;
            case -685620648: goto L_0x00c6;
            case -685620617: goto L_0x00be;
            case -685620586: goto L_0x00b6;
            case -685620555: goto L_0x00ad;
            case -685620524: goto L_0x00a5;
            case -685620493: goto L_0x009d;
            case -685620462: goto L_0x0093;
            default: goto L_0x0092;
        };
    L_0x0092:
        goto L_0x00de;
    L_0x0093:
        r1 = "{\\an9}";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x00de;
    L_0x009b:
        r0 = 5;
        goto L_0x00df;
    L_0x009d:
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x00de;
    L_0x00a3:
        r0 = 4;
        goto L_0x00df;
    L_0x00a5:
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x00de;
    L_0x00ab:
        r0 = 3;
        goto L_0x00df;
    L_0x00ad:
        r0 = r0.equals(r5);
        if (r0 == 0) goto L_0x00de;
    L_0x00b3:
        r0 = 8;
        goto L_0x00df;
    L_0x00b6:
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x00de;
    L_0x00bc:
        r0 = 7;
        goto L_0x00df;
    L_0x00be:
        r0 = r0.equals(r7);
        if (r0 == 0) goto L_0x00de;
    L_0x00c4:
        r0 = 6;
        goto L_0x00df;
    L_0x00c6:
        r0 = r0.equals(r8);
        if (r0 == 0) goto L_0x00de;
    L_0x00cc:
        r0 = 2;
        goto L_0x00df;
    L_0x00ce:
        r0 = r0.equals(r9);
        if (r0 == 0) goto L_0x00de;
    L_0x00d4:
        r0 = 1;
        goto L_0x00df;
    L_0x00d6:
        r0 = r0.equals(r10);
        if (r0 == 0) goto L_0x00de;
    L_0x00dc:
        r0 = 0;
        goto L_0x00df;
    L_0x00de:
        r0 = -1;
    L_0x00df:
        if (r0 == 0) goto L_0x00ef;
    L_0x00e1:
        if (r0 == r12) goto L_0x00ef;
    L_0x00e3:
        if (r0 == r11) goto L_0x00ef;
    L_0x00e5:
        if (r0 == r15) goto L_0x00ed;
    L_0x00e7:
        if (r0 == r14) goto L_0x00ed;
    L_0x00e9:
        if (r0 == r13) goto L_0x00ed;
    L_0x00eb:
        r6 = 1;
        goto L_0x00f0;
    L_0x00ed:
        r6 = 0;
        goto L_0x00f0;
    L_0x00ef:
        r6 = 2;
    L_0x00f0:
        r0 = new com.google.android.exoplayer2.text.Cue;
        r3 = 0;
        r4 = getFractionalPositionForAnchorType(r6);
        r5 = 0;
        r7 = getFractionalPositionForAnchorType(r16);
        r9 = 1;
        r1 = r0;
        r2 = r18;
        r8 = r16;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.subrip.SubripDecoder.buildCue(android.text.Spanned, java.lang.String):com.google.android.exoplayer2.text.Cue");
    }

    private static long parseTimecode(Matcher matcher, int i) {
        return ((((((Long.parseLong(matcher.group(i + 1)) * 60) * 60) * 1000) + ((Long.parseLong(matcher.group(i + 2)) * 60) * 1000)) + (Long.parseLong(matcher.group(i + 3)) * 1000)) + Long.parseLong(matcher.group(i + 4))) * 1000;
    }
}
