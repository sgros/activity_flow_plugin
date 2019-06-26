// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ssa;

import java.util.ArrayList;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.Subtitle;
import java.util.regex.Matcher;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class SsaDecoder extends SimpleSubtitleDecoder
{
    private static final Pattern SSA_TIMECODE_PATTERN;
    private int formatEndIndex;
    private int formatKeyCount;
    private int formatStartIndex;
    private int formatTextIndex;
    private final boolean haveInitializationData;
    
    static {
        SSA_TIMECODE_PATTERN = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)(?::|\\.)(\\d+)");
    }
    
    public SsaDecoder(final List<byte[]> list) {
        super("SsaDecoder");
        if (list != null && !list.isEmpty()) {
            this.haveInitializationData = true;
            final String fromUtf8Bytes = Util.fromUtf8Bytes(list.get(0));
            Assertions.checkArgument(fromUtf8Bytes.startsWith("Format: "));
            this.parseFormatLine(fromUtf8Bytes);
            this.parseHeader(new ParsableByteArray(list.get(1)));
        }
        else {
            this.haveInitializationData = false;
        }
    }
    
    private void parseDialogueLine(final String s, final List<Cue> list, final LongArray longArray) {
        if (this.formatKeyCount == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Skipping dialogue line before complete format: ");
            sb.append(s);
            Log.w("SsaDecoder", sb.toString());
            return;
        }
        final String[] split = s.substring(10).split(",", this.formatKeyCount);
        if (split.length != this.formatKeyCount) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Skipping dialogue line with fewer columns than format: ");
            sb2.append(s);
            Log.w("SsaDecoder", sb2.toString());
            return;
        }
        final long timecodeUs = parseTimecodeUs(split[this.formatStartIndex]);
        if (timecodeUs == -9223372036854775807L) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Skipping invalid timing: ");
            sb3.append(s);
            Log.w("SsaDecoder", sb3.toString());
            return;
        }
        final String s2 = split[this.formatEndIndex];
        long timecodeUs2;
        if (!s2.trim().isEmpty()) {
            if ((timecodeUs2 = parseTimecodeUs(s2)) == -9223372036854775807L) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Skipping invalid timing: ");
                sb4.append(s);
                Log.w("SsaDecoder", sb4.toString());
                return;
            }
        }
        else {
            timecodeUs2 = -9223372036854775807L;
        }
        list.add(new Cue(split[this.formatTextIndex].replaceAll("\\{.*?\\}", "").replaceAll("\\\\N", "\n").replaceAll("\\\\n", "\n")));
        longArray.add(timecodeUs);
        if (timecodeUs2 != -9223372036854775807L) {
            list.add(null);
            longArray.add(timecodeUs2);
        }
    }
    
    private void parseEventBody(final ParsableByteArray parsableByteArray, final List<Cue> list, final LongArray longArray) {
        while (true) {
            final String line = parsableByteArray.readLine();
            if (line == null) {
                break;
            }
            if (!this.haveInitializationData && line.startsWith("Format: ")) {
                this.parseFormatLine(line);
            }
            else {
                if (!line.startsWith("Dialogue: ")) {
                    continue;
                }
                this.parseDialogueLine(line, list, longArray);
            }
        }
    }
    
    private void parseFormatLine(final String s) {
        final String[] split = TextUtils.split(s.substring(8), ",");
        this.formatKeyCount = split.length;
        this.formatStartIndex = -1;
        this.formatEndIndex = -1;
        this.formatTextIndex = -1;
        for (int i = 0; i < this.formatKeyCount; ++i) {
            final String lowerInvariant = Util.toLowerInvariant(split[i].trim());
            final int hashCode = lowerInvariant.hashCode();
            int n = 0;
            Label_0131: {
                if (hashCode != 100571) {
                    if (hashCode != 3556653) {
                        if (hashCode == 109757538) {
                            if (lowerInvariant.equals("start")) {
                                n = 0;
                                break Label_0131;
                            }
                        }
                    }
                    else if (lowerInvariant.equals("text")) {
                        n = 2;
                        break Label_0131;
                    }
                }
                else if (lowerInvariant.equals("end")) {
                    n = 1;
                    break Label_0131;
                }
                n = -1;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n == 2) {
                        this.formatTextIndex = i;
                    }
                }
                else {
                    this.formatEndIndex = i;
                }
            }
            else {
                this.formatStartIndex = i;
            }
        }
        if (this.formatStartIndex == -1 || this.formatEndIndex == -1 || this.formatTextIndex == -1) {
            this.formatKeyCount = 0;
        }
    }
    
    private void parseHeader(final ParsableByteArray parsableByteArray) {
        String line;
        do {
            line = parsableByteArray.readLine();
        } while (line != null && !line.startsWith("[Events]"));
    }
    
    public static long parseTimecodeUs(final String input) {
        final Matcher matcher = SsaDecoder.SSA_TIMECODE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            return -9223372036854775807L;
        }
        return Long.parseLong(matcher.group(1)) * 60L * 60L * 1000000L + Long.parseLong(matcher.group(2)) * 60L * 1000000L + Long.parseLong(matcher.group(3)) * 1000000L + Long.parseLong(matcher.group(4)) * 10000L;
    }
    
    @Override
    protected SsaSubtitle decode(final byte[] array, final int n, final boolean b) {
        final ArrayList<Cue> list = new ArrayList<Cue>();
        final LongArray longArray = new LongArray();
        final ParsableByteArray parsableByteArray = new ParsableByteArray(array, n);
        if (!this.haveInitializationData) {
            this.parseHeader(parsableByteArray);
        }
        this.parseEventBody(parsableByteArray, list, longArray);
        final Cue[] a = new Cue[list.size()];
        list.toArray(a);
        return new SsaSubtitle(a, longArray.toArray());
    }
}
