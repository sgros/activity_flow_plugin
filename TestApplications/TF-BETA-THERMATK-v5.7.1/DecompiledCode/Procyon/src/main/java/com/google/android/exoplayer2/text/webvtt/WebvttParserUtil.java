// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Util;
import java.util.regex.Matcher;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.regex.Pattern;

public final class WebvttParserUtil
{
    private static final Pattern COMMENT;
    
    static {
        COMMENT = Pattern.compile("^NOTE(( |\t).*)?$");
    }
    
    public static Matcher findNextCueHeader(final ParsableByteArray parsableByteArray) {
        while (true) {
            final String line = parsableByteArray.readLine();
            if (line == null) {
                return null;
            }
            if (WebvttParserUtil.COMMENT.matcher(line).matches()) {
                String line2;
                do {
                    line2 = parsableByteArray.readLine();
                } while (line2 != null && !line2.isEmpty());
            }
            else {
                final Matcher matcher = WebvttCueParser.CUE_HEADER_PATTERN.matcher(line);
                if (matcher.matches()) {
                    return matcher;
                }
                continue;
            }
        }
    }
    
    public static boolean isWebvttHeaderLine(final ParsableByteArray parsableByteArray) {
        final String line = parsableByteArray.readLine();
        return line != null && line.startsWith("WEBVTT");
    }
    
    public static float parsePercentage(final String s) throws NumberFormatException {
        if (s.endsWith("%")) {
            return Float.parseFloat(s.substring(0, s.length() - 1)) / 100.0f;
        }
        throw new NumberFormatException("Percentages must end with %");
    }
    
    public static long parseTimestampUs(final String s) throws NumberFormatException {
        final String[] splitAtFirst = Util.splitAtFirst(s, "\\.");
        int i = 0;
        final String[] split = Util.split(splitAtFirst[0], ":");
        final int length = split.length;
        long n = 0L;
        while (i < length) {
            n = n * 60L + Long.parseLong(split[i]);
            ++i;
        }
        long n2 = n * 1000L;
        if (splitAtFirst.length == 2) {
            n2 += Long.parseLong(splitAtFirst[1]);
        }
        return n2 * 1000L;
    }
    
    public static void validateWebvttHeaderLine(final ParsableByteArray parsableByteArray) throws ParserException {
        final int position = parsableByteArray.getPosition();
        if (isWebvttHeaderLine(parsableByteArray)) {
            return;
        }
        parsableByteArray.setPosition(position);
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected WEBVTT. Got ");
        sb.append(parsableByteArray.readLine());
        throw new ParserException(sb.toString());
    }
}
