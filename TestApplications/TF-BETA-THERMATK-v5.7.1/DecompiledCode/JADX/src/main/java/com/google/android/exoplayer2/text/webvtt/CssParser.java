package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CssParser {
    private static final Pattern VOICE_NAME_PATTERN = Pattern.compile("\\[voice=\"([^\"]*)\"\\]");
    private final StringBuilder stringBuilder = new StringBuilder();
    private final ParsableByteArray styleInput = new ParsableByteArray();

    public WebvttCssStyle parseBlock(ParsableByteArray parsableByteArray) {
        this.stringBuilder.setLength(0);
        int position = parsableByteArray.getPosition();
        skipStyleBlock(parsableByteArray);
        this.styleInput.reset(parsableByteArray.data, parsableByteArray.getPosition());
        this.styleInput.setPosition(position);
        String parseSelector = parseSelector(this.styleInput, this.stringBuilder);
        if (parseSelector == null) {
            return null;
        }
        if (!"{".equals(parseNextToken(this.styleInput, this.stringBuilder))) {
            return null;
        }
        String str;
        WebvttCssStyle webvttCssStyle = new WebvttCssStyle();
        applySelectorToStyle(webvttCssStyle, parseSelector);
        Object obj = null;
        Object obj2 = null;
        while (true) {
            str = "}";
            if (obj2 != null) {
                break;
            }
            int position2 = this.styleInput.getPosition();
            obj = parseNextToken(this.styleInput, this.stringBuilder);
            Object obj3 = (obj == null || str.equals(obj)) ? 1 : null;
            if (obj3 == null) {
                this.styleInput.setPosition(position2);
                parseStyleDeclaration(this.styleInput, webvttCssStyle, this.stringBuilder);
            }
            obj2 = obj3;
        }
        if (str.equals(obj)) {
            return webvttCssStyle;
        }
        return null;
    }

    private static String parseSelector(ParsableByteArray parsableByteArray, StringBuilder stringBuilder) {
        skipWhitespaceAndComments(parsableByteArray);
        if (parsableByteArray.bytesLeft() < 5) {
            return null;
        }
        if (!"::cue".equals(parsableByteArray.readString(5))) {
            return null;
        }
        int position = parsableByteArray.getPosition();
        String parseNextToken = parseNextToken(parsableByteArray, stringBuilder);
        if (parseNextToken == null) {
            return null;
        }
        if ("{".equals(parseNextToken)) {
            parsableByteArray.setPosition(position);
            return "";
        }
        String readCueTarget = "(".equals(parseNextToken) ? readCueTarget(parsableByteArray) : null;
        String parseNextToken2 = parseNextToken(parsableByteArray, stringBuilder);
        if (!")".equals(parseNextToken2) || parseNextToken2 == null) {
            return null;
        }
        return readCueTarget;
    }

    private static String readCueTarget(ParsableByteArray parsableByteArray) {
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        Object obj = null;
        while (position < limit && obj == null) {
            int i = position + 1;
            obj = ((char) parsableByteArray.data[position]) == ')' ? 1 : null;
            position = i;
        }
        return parsableByteArray.readString((position - 1) - parsableByteArray.getPosition()).trim();
    }

    private static void parseStyleDeclaration(ParsableByteArray parsableByteArray, WebvttCssStyle webvttCssStyle, StringBuilder stringBuilder) {
        skipWhitespaceAndComments(parsableByteArray);
        String parseIdentifier = parseIdentifier(parsableByteArray, stringBuilder);
        String str = "";
        if (!str.equals(parseIdentifier)) {
            if (":".equals(parseNextToken(parsableByteArray, stringBuilder))) {
                skipWhitespaceAndComments(parsableByteArray);
                String parsePropertyValue = parsePropertyValue(parsableByteArray, stringBuilder);
                if (!(parsePropertyValue == null || str.equals(parsePropertyValue))) {
                    int position = parsableByteArray.getPosition();
                    String parseNextToken = parseNextToken(parsableByteArray, stringBuilder);
                    if (!";".equals(parseNextToken)) {
                        if ("}".equals(parseNextToken)) {
                            parsableByteArray.setPosition(position);
                        }
                    }
                    if ("color".equals(parseIdentifier)) {
                        webvttCssStyle.setFontColor(ColorParser.parseCssColor(parsePropertyValue));
                    } else if ("background-color".equals(parseIdentifier)) {
                        webvttCssStyle.setBackgroundColor(ColorParser.parseCssColor(parsePropertyValue));
                    } else if ("text-decoration".equals(parseIdentifier)) {
                        if ("underline".equals(parsePropertyValue)) {
                            webvttCssStyle.setUnderline(true);
                        }
                    } else if ("font-family".equals(parseIdentifier)) {
                        webvttCssStyle.setFontFamily(parsePropertyValue);
                    } else if ("font-weight".equals(parseIdentifier)) {
                        if ("bold".equals(parsePropertyValue)) {
                            webvttCssStyle.setBold(true);
                        }
                    } else if ("font-style".equals(parseIdentifier) && "italic".equals(parsePropertyValue)) {
                        webvttCssStyle.setItalic(true);
                    }
                }
            }
        }
    }

    static void skipWhitespaceAndComments(ParsableByteArray parsableByteArray) {
        while (true) {
            Object obj = 1;
            while (parsableByteArray.bytesLeft() > 0 && obj != null) {
                if (!maybeSkipWhitespace(parsableByteArray)) {
                    if (!maybeSkipComment(parsableByteArray)) {
                        obj = null;
                    }
                }
            }
            return;
        }
    }

    static String parseNextToken(ParsableByteArray parsableByteArray, StringBuilder stringBuilder) {
        skipWhitespaceAndComments(parsableByteArray);
        if (parsableByteArray.bytesLeft() == 0) {
            return null;
        }
        String parseIdentifier = parseIdentifier(parsableByteArray, stringBuilder);
        String str = "";
        if (!str.equals(parseIdentifier)) {
            return parseIdentifier;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append((char) parsableByteArray.readUnsignedByte());
        return stringBuilder.toString();
    }

    private static boolean maybeSkipWhitespace(ParsableByteArray parsableByteArray) {
        char peekCharAtPosition = peekCharAtPosition(parsableByteArray, parsableByteArray.getPosition());
        if (peekCharAtPosition != 9 && peekCharAtPosition != 10 && peekCharAtPosition != 12 && peekCharAtPosition != 13 && peekCharAtPosition != ' ') {
            return false;
        }
        parsableByteArray.skipBytes(1);
        return true;
    }

    static void skipStyleBlock(ParsableByteArray parsableByteArray) {
        do {
        } while (!TextUtils.isEmpty(parsableByteArray.readLine()));
    }

    private static char peekCharAtPosition(ParsableByteArray parsableByteArray, int i) {
        return (char) parsableByteArray.data[i];
    }

    private static String parsePropertyValue(ParsableByteArray parsableByteArray, StringBuilder stringBuilder) {
        StringBuilder stringBuilder2 = new StringBuilder();
        Object obj = null;
        while (obj == null) {
            int position = parsableByteArray.getPosition();
            String parseNextToken = parseNextToken(parsableByteArray, stringBuilder);
            if (parseNextToken == null) {
                return null;
            }
            if ("}".equals(parseNextToken) || ";".equals(parseNextToken)) {
                parsableByteArray.setPosition(position);
                obj = 1;
            } else {
                stringBuilder2.append(parseNextToken);
            }
        }
        return stringBuilder2.toString();
    }

    private static boolean maybeSkipComment(ParsableByteArray parsableByteArray) {
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        byte[] bArr = parsableByteArray.data;
        if (position + 2 <= limit) {
            int i = position + 1;
            if (bArr[position] == (byte) 47) {
                position = i + 1;
                if (bArr[i] == (byte) 42) {
                    while (true) {
                        i = position + 1;
                        if (i >= limit) {
                            parsableByteArray.skipBytes(limit - parsableByteArray.getPosition());
                            return true;
                        } else if (((char) bArr[position]) == '*' && ((char) bArr[i]) == '/') {
                            position = i + 1;
                            limit = position;
                        } else {
                            position = i;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static String parseIdentifier(ParsableByteArray parsableByteArray, StringBuilder stringBuilder) {
        int i = 0;
        stringBuilder.setLength(0);
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        while (position < limit && i == 0) {
            char c = (char) parsableByteArray.data[position];
            if ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && !((c >= '0' && c <= '9') || c == '#' || c == '-' || c == '.' || c == '_'))) {
                i = 1;
            } else {
                position++;
                stringBuilder.append(c);
            }
        }
        parsableByteArray.skipBytes(position - parsableByteArray.getPosition());
        return stringBuilder.toString();
    }

    private void applySelectorToStyle(WebvttCssStyle webvttCssStyle, String str) {
        if (!"".equals(str)) {
            int indexOf = str.indexOf(91);
            if (indexOf != -1) {
                Matcher matcher = VOICE_NAME_PATTERN.matcher(str.substring(indexOf));
                if (matcher.matches()) {
                    webvttCssStyle.setTargetVoice(matcher.group(1));
                }
                str = str.substring(0, indexOf);
            }
            String[] split = Util.split(str, "\\.");
            String str2 = split[0];
            int indexOf2 = str2.indexOf(35);
            if (indexOf2 != -1) {
                webvttCssStyle.setTargetTagName(str2.substring(0, indexOf2));
                webvttCssStyle.setTargetId(str2.substring(indexOf2 + 1));
            } else {
                webvttCssStyle.setTargetTagName(str2);
            }
            if (split.length > 1) {
                webvttCssStyle.setTargetClasses((String[]) Arrays.copyOfRange(split, 1, split.length));
            }
        }
    }
}
