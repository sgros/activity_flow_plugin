package com.google.android.exoplayer2.text.ssa;

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

    public SsaDecoder(List<byte[]> list) {
        super("SsaDecoder");
        if (list == null || list.isEmpty()) {
            this.haveInitializationData = false;
            return;
        }
        this.haveInitializationData = true;
        String fromUtf8Bytes = Util.fromUtf8Bytes((byte[]) list.get(0));
        Assertions.checkArgument(fromUtf8Bytes.startsWith("Format: "));
        parseFormatLine(fromUtf8Bytes);
        parseHeader(new ParsableByteArray((byte[]) list.get(1)));
    }

    /* Access modifiers changed, original: protected */
    public SsaSubtitle decode(byte[] bArr, int i, boolean z) {
        ArrayList arrayList = new ArrayList();
        LongArray longArray = new LongArray();
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr, i);
        if (!this.haveInitializationData) {
            parseHeader(parsableByteArray);
        }
        parseEventBody(parsableByteArray, arrayList, longArray);
        Cue[] cueArr = new Cue[arrayList.size()];
        arrayList.toArray(cueArr);
        return new SsaSubtitle(cueArr, longArray.toArray());
    }

    private void parseHeader(ParsableByteArray parsableByteArray) {
        String readLine;
        do {
            readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
        } while (!readLine.startsWith("[Events]"));
    }

    private void parseEventBody(ParsableByteArray parsableByteArray, List<Cue> list, LongArray longArray) {
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
            if (!this.haveInitializationData && readLine.startsWith("Format: ")) {
                parseFormatLine(readLine);
            } else if (readLine.startsWith("Dialogue: ")) {
                parseDialogueLine(readLine, list, longArray);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005d  */
    private void parseFormatLine(java.lang.String r9) {
        /*
        r8 = this;
        r0 = 8;
        r9 = r9.substring(r0);
        r0 = ",";
        r9 = android.text.TextUtils.split(r9, r0);
        r0 = r9.length;
        r8.formatKeyCount = r0;
        r0 = -1;
        r8.formatStartIndex = r0;
        r8.formatEndIndex = r0;
        r8.formatTextIndex = r0;
        r1 = 0;
        r2 = 0;
    L_0x0018:
        r3 = r8.formatKeyCount;
        if (r2 >= r3) goto L_0x006d;
    L_0x001c:
        r3 = r9[r2];
        r3 = r3.trim();
        r3 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r3);
        r4 = r3.hashCode();
        r5 = 100571; // 0x188db float:1.4093E-40 double:4.96887E-319;
        r6 = 2;
        r7 = 1;
        if (r4 == r5) goto L_0x0050;
    L_0x0031:
        r5 = 3556653; // 0x36452d float:4.983932E-39 double:1.75722E-317;
        if (r4 == r5) goto L_0x0046;
    L_0x0036:
        r5 = 109757538; // 0x68ac462 float:5.219839E-35 double:5.4227429E-316;
        if (r4 == r5) goto L_0x003c;
    L_0x003b:
        goto L_0x005a;
    L_0x003c:
        r4 = "start";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x005a;
    L_0x0044:
        r3 = 0;
        goto L_0x005b;
    L_0x0046:
        r4 = "text";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x005a;
    L_0x004e:
        r3 = 2;
        goto L_0x005b;
    L_0x0050:
        r4 = "end";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x005a;
    L_0x0058:
        r3 = 1;
        goto L_0x005b;
    L_0x005a:
        r3 = -1;
    L_0x005b:
        if (r3 == 0) goto L_0x0068;
    L_0x005d:
        if (r3 == r7) goto L_0x0065;
    L_0x005f:
        if (r3 == r6) goto L_0x0062;
    L_0x0061:
        goto L_0x006a;
    L_0x0062:
        r8.formatTextIndex = r2;
        goto L_0x006a;
    L_0x0065:
        r8.formatEndIndex = r2;
        goto L_0x006a;
    L_0x0068:
        r8.formatStartIndex = r2;
    L_0x006a:
        r2 = r2 + 1;
        goto L_0x0018;
    L_0x006d:
        r9 = r8.formatStartIndex;
        if (r9 == r0) goto L_0x0079;
    L_0x0071:
        r9 = r8.formatEndIndex;
        if (r9 == r0) goto L_0x0079;
    L_0x0075:
        r9 = r8.formatTextIndex;
        if (r9 != r0) goto L_0x007b;
    L_0x0079:
        r8.formatKeyCount = r1;
    L_0x007b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.ssa.SsaDecoder.parseFormatLine(java.lang.String):void");
    }

    private void parseDialogueLine(String str, List<Cue> list, LongArray longArray) {
        String str2 = "SsaDecoder";
        StringBuilder stringBuilder;
        if (this.formatKeyCount == 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Skipping dialogue line before complete format: ");
            stringBuilder.append(str);
            Log.m18w(str2, stringBuilder.toString());
            return;
        }
        String[] split = str.substring(10).split(",", this.formatKeyCount);
        if (split.length != this.formatKeyCount) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Skipping dialogue line with fewer columns than format: ");
            stringBuilder.append(str);
            Log.m18w(str2, stringBuilder.toString());
            return;
        }
        long parseTimecodeUs = parseTimecodeUs(split[this.formatStartIndex]);
        String str3 = "Skipping invalid timing: ";
        if (parseTimecodeUs == -9223372036854775807L) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(str);
            Log.m18w(str2, stringBuilder.toString());
            return;
        }
        long j;
        String str4 = split[this.formatEndIndex];
        if (str4.trim().isEmpty()) {
            j = -9223372036854775807L;
        } else {
            j = parseTimecodeUs(str4);
            if (j == -9223372036854775807L) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(str);
                Log.m18w(str2, stringBuilder.toString());
                return;
            }
        }
        str = split[this.formatTextIndex].replaceAll("\\{.*?\\}", "");
        String str5 = "\n";
        list.add(new Cue(str.replaceAll("\\\\N", str5).replaceAll("\\\\n", str5)));
        longArray.add(parseTimecodeUs);
        if (j != -9223372036854775807L) {
            list.add(null);
            longArray.add(j);
        }
    }

    public static long parseTimecodeUs(String str) {
        Matcher matcher = SSA_TIMECODE_PATTERN.matcher(str);
        if (matcher.matches()) {
            return (((((Long.parseLong(matcher.group(1)) * 60) * 60) * 1000000) + ((Long.parseLong(matcher.group(2)) * 60) * 1000000)) + (Long.parseLong(matcher.group(3)) * 1000000)) + (Long.parseLong(matcher.group(4)) * 10000);
        }
        return -9223372036854775807L;
    }
}
