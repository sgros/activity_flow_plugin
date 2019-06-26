// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.util.ColorParser;
import java.util.regex.Matcher;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.regex.Pattern;

final class CssParser
{
    private static final Pattern VOICE_NAME_PATTERN;
    private final StringBuilder stringBuilder;
    private final ParsableByteArray styleInput;
    
    static {
        VOICE_NAME_PATTERN = Pattern.compile("\\[voice=\"([^\"]*)\"\\]");
    }
    
    public CssParser() {
        this.styleInput = new ParsableByteArray();
        this.stringBuilder = new StringBuilder();
    }
    
    private void applySelectorToStyle(final WebvttCssStyle webvttCssStyle, String s) {
        if ("".equals(s)) {
            return;
        }
        final int index = s.indexOf(91);
        String substring = s;
        if (index != -1) {
            final Matcher matcher = CssParser.VOICE_NAME_PATTERN.matcher(s.substring(index));
            if (matcher.matches()) {
                webvttCssStyle.setTargetVoice(matcher.group(1));
            }
            substring = s.substring(0, index);
        }
        final String[] split = Util.split(substring, "\\.");
        s = split[0];
        final int index2 = s.indexOf(35);
        if (index2 != -1) {
            webvttCssStyle.setTargetTagName(s.substring(0, index2));
            webvttCssStyle.setTargetId(s.substring(index2 + 1));
        }
        else {
            webvttCssStyle.setTargetTagName(s);
        }
        if (split.length > 1) {
            webvttCssStyle.setTargetClasses(Arrays.copyOfRange(split, 1, split.length));
        }
    }
    
    private static boolean maybeSkipComment(final ParsableByteArray parsableByteArray) {
        final int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        final byte[] data = parsableByteArray.data;
        if (position + 2 <= limit) {
            final int n = position + 1;
            if (data[position] == 47) {
                int n2 = n + 1;
                if (data[n] == 42) {
                    while (true) {
                        final int n3 = n2 + 1;
                        if (n3 >= limit) {
                            break;
                        }
                        if ((char)data[n2] == '*' && (char)data[n3] == '/') {
                            n2 = (limit = n3 + 1);
                        }
                        else {
                            n2 = n3;
                        }
                    }
                    parsableByteArray.skipBytes(limit - parsableByteArray.getPosition());
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean maybeSkipWhitespace(final ParsableByteArray parsableByteArray) {
        final char peekCharAtPosition = peekCharAtPosition(parsableByteArray, parsableByteArray.getPosition());
        if (peekCharAtPosition != '\t' && peekCharAtPosition != '\n' && peekCharAtPosition != '\f' && peekCharAtPosition != '\r' && peekCharAtPosition != ' ') {
            return false;
        }
        parsableByteArray.skipBytes(1);
        return true;
    }
    
    private static String parseIdentifier(final ParsableByteArray parsableByteArray, final StringBuilder sb) {
        int n = 0;
        sb.setLength(0);
        int position = parsableByteArray.getPosition();
        while (position < parsableByteArray.limit() && n == 0) {
            final char c = (char)parsableByteArray.data[position];
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') && (c < '0' || c > '9') && c != '#' && c != '-' && c != '.' && c != '_') {
                n = 1;
            }
            else {
                ++position;
                sb.append(c);
            }
        }
        parsableByteArray.skipBytes(position - parsableByteArray.getPosition());
        return sb.toString();
    }
    
    static String parseNextToken(final ParsableByteArray parsableByteArray, StringBuilder sb) {
        skipWhitespaceAndComments(parsableByteArray);
        if (parsableByteArray.bytesLeft() == 0) {
            return null;
        }
        final String identifier = parseIdentifier(parsableByteArray, sb);
        if (!"".equals(identifier)) {
            return identifier;
        }
        sb = new StringBuilder();
        sb.append("");
        sb.append((char)parsableByteArray.readUnsignedByte());
        return sb.toString();
    }
    
    private static String parsePropertyValue(final ParsableByteArray parsableByteArray, final StringBuilder sb) {
        final StringBuilder sb2 = new StringBuilder();
        int i = 0;
        while (i == 0) {
            final int position = parsableByteArray.getPosition();
            final String nextToken = parseNextToken(parsableByteArray, sb);
            if (nextToken == null) {
                return null;
            }
            if (!"}".equals(nextToken) && !";".equals(nextToken)) {
                sb2.append(nextToken);
            }
            else {
                parsableByteArray.setPosition(position);
                i = 1;
            }
        }
        return sb2.toString();
    }
    
    private static String parseSelector(final ParsableByteArray parsableByteArray, final StringBuilder sb) {
        skipWhitespaceAndComments(parsableByteArray);
        if (parsableByteArray.bytesLeft() < 5) {
            return null;
        }
        if (!"::cue".equals(parsableByteArray.readString(5))) {
            return null;
        }
        final int position = parsableByteArray.getPosition();
        final String nextToken = parseNextToken(parsableByteArray, sb);
        if (nextToken == null) {
            return null;
        }
        if ("{".equals(nextToken)) {
            parsableByteArray.setPosition(position);
            return "";
        }
        String cueTarget;
        if ("(".equals(nextToken)) {
            cueTarget = readCueTarget(parsableByteArray);
        }
        else {
            cueTarget = null;
        }
        final String nextToken2 = parseNextToken(parsableByteArray, sb);
        if (")".equals(nextToken2) && nextToken2 != null) {
            return cueTarget;
        }
        return null;
    }
    
    private static void parseStyleDeclaration(final ParsableByteArray parsableByteArray, final WebvttCssStyle webvttCssStyle, final StringBuilder sb) {
        skipWhitespaceAndComments(parsableByteArray);
        final String identifier = parseIdentifier(parsableByteArray, sb);
        if ("".equals(identifier)) {
            return;
        }
        if (!":".equals(parseNextToken(parsableByteArray, sb))) {
            return;
        }
        skipWhitespaceAndComments(parsableByteArray);
        final String propertyValue = parsePropertyValue(parsableByteArray, sb);
        if (propertyValue != null) {
            if (!"".equals(propertyValue)) {
                final int position = parsableByteArray.getPosition();
                final String nextToken = parseNextToken(parsableByteArray, sb);
                if (!";".equals(nextToken)) {
                    if (!"}".equals(nextToken)) {
                        return;
                    }
                    parsableByteArray.setPosition(position);
                }
                if ("color".equals(identifier)) {
                    webvttCssStyle.setFontColor(ColorParser.parseCssColor(propertyValue));
                }
                else if ("background-color".equals(identifier)) {
                    webvttCssStyle.setBackgroundColor(ColorParser.parseCssColor(propertyValue));
                }
                else if ("text-decoration".equals(identifier)) {
                    if ("underline".equals(propertyValue)) {
                        webvttCssStyle.setUnderline(true);
                    }
                }
                else if ("font-family".equals(identifier)) {
                    webvttCssStyle.setFontFamily(propertyValue);
                }
                else if ("font-weight".equals(identifier)) {
                    if ("bold".equals(propertyValue)) {
                        webvttCssStyle.setBold(true);
                    }
                }
                else if ("font-style".equals(identifier) && "italic".equals(propertyValue)) {
                    webvttCssStyle.setItalic(true);
                }
            }
        }
    }
    
    private static char peekCharAtPosition(final ParsableByteArray parsableByteArray, final int n) {
        return (char)parsableByteArray.data[n];
    }
    
    private static String readCueTarget(final ParsableByteArray parsableByteArray) {
        int position = parsableByteArray.getPosition();
        for (int limit = parsableByteArray.limit(), n = 0; position < limit && n == 0; ++position) {
            if ((char)parsableByteArray.data[position] == ')') {
                n = 1;
            }
            else {
                n = 0;
            }
        }
        return parsableByteArray.readString(position - 1 - parsableByteArray.getPosition()).trim();
    }
    
    static void skipStyleBlock(final ParsableByteArray parsableByteArray) {
        while (!TextUtils.isEmpty((CharSequence)parsableByteArray.readLine())) {}
    }
    
    static void skipWhitespaceAndComments(final ParsableByteArray parsableByteArray) {
    Label_0000:
        while (true) {
            for (int n = 1; parsableByteArray.bytesLeft() > 0 && n != 0; n = 0) {
                if (maybeSkipWhitespace(parsableByteArray)) {
                    continue Label_0000;
                }
                if (maybeSkipComment(parsableByteArray)) {
                    continue Label_0000;
                }
            }
            break;
        }
    }
    
    public WebvttCssStyle parseBlock(final ParsableByteArray parsableByteArray) {
        this.stringBuilder.setLength(0);
        final int position = parsableByteArray.getPosition();
        skipStyleBlock(parsableByteArray);
        this.styleInput.reset(parsableByteArray.data, parsableByteArray.getPosition());
        this.styleInput.setPosition(position);
        final String selector = parseSelector(this.styleInput, this.stringBuilder);
        WebvttCssStyle webvttCssStyle2;
        final WebvttCssStyle webvttCssStyle = webvttCssStyle2 = null;
        if (selector != null) {
            if (!"{".equals(parseNextToken(this.styleInput, this.stringBuilder))) {
                webvttCssStyle2 = webvttCssStyle;
            }
            else {
                final WebvttCssStyle webvttCssStyle3 = new WebvttCssStyle();
                this.applySelectorToStyle(webvttCssStyle3, selector);
                Object nextToken = null;
                int i = 0;
                while (i == 0) {
                    final int position2 = this.styleInput.getPosition();
                    nextToken = parseNextToken(this.styleInput, this.stringBuilder);
                    if (nextToken != null && !"}".equals(nextToken)) {
                        i = 0;
                    }
                    else {
                        i = 1;
                    }
                    if (i == 0) {
                        this.styleInput.setPosition(position2);
                        parseStyleDeclaration(this.styleInput, webvttCssStyle3, this.stringBuilder);
                    }
                }
                webvttCssStyle2 = webvttCssStyle;
                if ("}".equals(nextToken)) {
                    webvttCssStyle2 = webvttCssStyle3;
                }
            }
        }
        return webvttCssStyle2;
    }
}
