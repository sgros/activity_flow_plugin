package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttParserUtil {
    private static final Pattern COMMENT = Pattern.compile("^NOTE(( |\t).*)?$");

    public static void validateWebvttHeaderLine(ParsableByteArray parsableByteArray) throws ParserException {
        int position = parsableByteArray.getPosition();
        if (!isWebvttHeaderLine(parsableByteArray)) {
            parsableByteArray.setPosition(position);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected WEBVTT. Got ");
            stringBuilder.append(parsableByteArray.readLine());
            throw new ParserException(stringBuilder.toString());
        }
    }

    public static boolean isWebvttHeaderLine(ParsableByteArray parsableByteArray) {
        String readLine = parsableByteArray.readLine();
        return readLine != null && readLine.startsWith("WEBVTT");
    }

    public static long parseTimestampUs(String str) throws NumberFormatException {
        String[] splitAtFirst = Util.splitAtFirst(str, "\\.");
        int i = 0;
        String[] split = Util.split(splitAtFirst[0], ":");
        long j = 0;
        while (i < split.length) {
            j = (j * 60) + Long.parseLong(split[i]);
            i++;
        }
        j *= 1000;
        if (splitAtFirst.length == 2) {
            j += Long.parseLong(splitAtFirst[1]);
        }
        return j * 1000;
    }

    public static float parsePercentage(String str) throws NumberFormatException {
        if (str.endsWith("%")) {
            return Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0f;
        }
        throw new NumberFormatException("Percentages must end with %");
    }

    public static Matcher findNextCueHeader(ParsableByteArray parsableByteArray) {
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return null;
            }
            if (COMMENT.matcher(readLine).matches()) {
                while (true) {
                    readLine = parsableByteArray.readLine();
                    if (readLine == null || readLine.isEmpty()) {
                        break;
                    }
                }
            } else {
                Matcher matcher = WebvttCueParser.CUE_HEADER_PATTERN.matcher(readLine);
                if (matcher.matches()) {
                    return matcher;
                }
            }
        }
    }
}
