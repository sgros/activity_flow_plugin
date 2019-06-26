// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import java.util.Arrays;
import android.text.Layout$Alignment;
import java.util.ArrayList;
import java.util.ArrayDeque;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.regex.Matcher;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.AlignmentSpan$Standard;
import android.text.style.TypefaceSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import java.util.List;
import com.google.android.exoplayer2.util.Log;
import android.text.SpannableStringBuilder;
import java.util.regex.Pattern;

public final class WebvttCueParser
{
    public static final Pattern CUE_HEADER_PATTERN;
    private static final Pattern CUE_SETTING_PATTERN;
    private final StringBuilder textBuilder;
    
    static {
        CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
        CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");
    }
    
    public WebvttCueParser() {
        this.textBuilder = new StringBuilder();
    }
    
    private static void applyEntity(final String str, final SpannableStringBuilder spannableStringBuilder) {
        final int hashCode = str.hashCode();
        int n = 0;
        Label_0092: {
            if (hashCode != 3309) {
                if (hashCode != 3464) {
                    if (hashCode != 96708) {
                        if (hashCode == 3374865) {
                            if (str.equals("nbsp")) {
                                n = 2;
                                break Label_0092;
                            }
                        }
                    }
                    else if (str.equals("amp")) {
                        n = 3;
                        break Label_0092;
                    }
                }
                else if (str.equals("lt")) {
                    n = 0;
                    break Label_0092;
                }
            }
            else if (str.equals("gt")) {
                n = 1;
                break Label_0092;
            }
            n = -1;
        }
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("ignoring unsupported entity: '&");
                        sb.append(str);
                        sb.append(";'");
                        Log.w("WebvttCueParser", sb.toString());
                    }
                    else {
                        spannableStringBuilder.append('&');
                    }
                }
                else {
                    spannableStringBuilder.append(' ');
                }
            }
            else {
                spannableStringBuilder.append('>');
            }
        }
        else {
            spannableStringBuilder.append('<');
        }
    }
    
    private static void applySpansForTag(final String s, final StartTag startTag, final SpannableStringBuilder spannableStringBuilder, final List<WebvttCssStyle> list, final List<StyleMatch> list2) {
        final int position = startTag.position;
        final int length = spannableStringBuilder.length();
        final String name = startTag.name;
        final int hashCode = name.hashCode();
        final int n = 0;
        int n2 = 0;
        Label_0194: {
            if (hashCode != 0) {
                if (hashCode != 105) {
                    if (hashCode != 3314158) {
                        if (hashCode != 98) {
                            if (hashCode != 99) {
                                if (hashCode != 117) {
                                    if (hashCode == 118) {
                                        if (name.equals("v")) {
                                            n2 = 5;
                                            break Label_0194;
                                        }
                                    }
                                }
                                else if (name.equals("u")) {
                                    n2 = 2;
                                    break Label_0194;
                                }
                            }
                            else if (name.equals("c")) {
                                n2 = 3;
                                break Label_0194;
                            }
                        }
                        else if (name.equals("b")) {
                            n2 = 0;
                            break Label_0194;
                        }
                    }
                    else if (name.equals("lang")) {
                        n2 = 4;
                        break Label_0194;
                    }
                }
                else if (name.equals("i")) {
                    n2 = 1;
                    break Label_0194;
                }
            }
            else if (name.equals("")) {
                n2 = 6;
                break Label_0194;
            }
            n2 = -1;
        }
        while (true) {
            switch (n2) {
                default: {}
                case 3:
                case 4:
                case 5:
                case 6: {
                    list2.clear();
                    getApplicableStyles(list, s, startTag, list2);
                    for (int size = list2.size(), i = n; i < size; ++i) {
                        applyStyleToText(spannableStringBuilder, list2.get(i).style, position, length);
                    }
                }
                case 2: {
                    spannableStringBuilder.setSpan((Object)new UnderlineSpan(), position, length, 33);
                    continue;
                }
                case 1: {
                    spannableStringBuilder.setSpan((Object)new StyleSpan(2), position, length, 33);
                    continue;
                }
                case 0: {
                    spannableStringBuilder.setSpan((Object)new StyleSpan(1), position, length, 33);
                    continue;
                }
            }
            break;
        }
    }
    
    private static void applyStyleToText(final SpannableStringBuilder spannableStringBuilder, final WebvttCssStyle webvttCssStyle, final int n, final int n2) {
        if (webvttCssStyle == null) {
            return;
        }
        if (webvttCssStyle.getStyle() != -1) {
            spannableStringBuilder.setSpan((Object)new StyleSpan(webvttCssStyle.getStyle()), n, n2, 33);
        }
        if (webvttCssStyle.isLinethrough()) {
            spannableStringBuilder.setSpan((Object)new StrikethroughSpan(), n, n2, 33);
        }
        if (webvttCssStyle.isUnderline()) {
            spannableStringBuilder.setSpan((Object)new UnderlineSpan(), n, n2, 33);
        }
        if (webvttCssStyle.hasFontColor()) {
            spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(webvttCssStyle.getFontColor()), n, n2, 33);
        }
        if (webvttCssStyle.hasBackgroundColor()) {
            spannableStringBuilder.setSpan((Object)new BackgroundColorSpan(webvttCssStyle.getBackgroundColor()), n, n2, 33);
        }
        if (webvttCssStyle.getFontFamily() != null) {
            spannableStringBuilder.setSpan((Object)new TypefaceSpan(webvttCssStyle.getFontFamily()), n, n2, 33);
        }
        if (webvttCssStyle.getTextAlign() != null) {
            spannableStringBuilder.setSpan((Object)new AlignmentSpan$Standard(webvttCssStyle.getTextAlign()), n, n2, 33);
        }
        final int fontSizeUnit = webvttCssStyle.getFontSizeUnit();
        if (fontSizeUnit != 1) {
            if (fontSizeUnit != 2) {
                if (fontSizeUnit == 3) {
                    spannableStringBuilder.setSpan((Object)new RelativeSizeSpan(webvttCssStyle.getFontSize() / 100.0f), n, n2, 33);
                }
            }
            else {
                spannableStringBuilder.setSpan((Object)new RelativeSizeSpan(webvttCssStyle.getFontSize()), n, n2, 33);
            }
        }
        else {
            spannableStringBuilder.setSpan((Object)new AbsoluteSizeSpan((int)webvttCssStyle.getFontSize(), true), n, n2, 33);
        }
    }
    
    private static int findEndOfTag(final String s, int fromIndex) {
        fromIndex = s.indexOf(62, fromIndex);
        if (fromIndex == -1) {
            fromIndex = s.length();
        }
        else {
            ++fromIndex;
        }
        return fromIndex;
    }
    
    private static void getApplicableStyles(final List<WebvttCssStyle> list, final String s, final StartTag startTag, final List<StyleMatch> list2) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            final WebvttCssStyle webvttCssStyle = list.get(i);
            final int specificityScore = webvttCssStyle.getSpecificityScore(s, startTag.name, startTag.classes, startTag.voice);
            if (specificityScore > 0) {
                list2.add(new StyleMatch(specificityScore, webvttCssStyle));
            }
        }
        Collections.sort((List<Comparable>)list2);
    }
    
    private static String getTagName(String trim) {
        trim = trim.trim();
        if (trim.isEmpty()) {
            return null;
        }
        return Util.splitAtFirst(trim, "[ \\.]")[0];
    }
    
    private static boolean isSupportedTag(final String s) {
        final int hashCode = s.hashCode();
        if (hashCode != 98) {
            if (hashCode != 99) {
                if (hashCode != 105) {
                    if (hashCode != 3314158) {
                        if (hashCode != 117) {
                            if (hashCode == 118) {
                                if (s.equals("v")) {
                                    final int n = 5;
                                    return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
                                }
                            }
                        }
                        else if (s.equals("u")) {
                            final int n = 4;
                            return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
                        }
                    }
                    else if (s.equals("lang")) {
                        final int n = 3;
                        return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
                    }
                }
                else if (s.equals("i")) {
                    final int n = 2;
                    return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
                }
            }
            else if (s.equals("c")) {
                final int n = 1;
                return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
            }
        }
        else if (s.equals("b")) {
            final int n = 0;
            return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
        }
        final int n = -1;
        return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5;
    }
    
    private static boolean parseCue(final String s, Matcher line, final ParsableByteArray parsableByteArray, final WebvttCue.Builder builder, final StringBuilder sb, final List<WebvttCssStyle> list) {
        try {
            builder.setStartTime(WebvttParserUtil.parseTimestampUs(line.group(1)));
            builder.setEndTime(WebvttParserUtil.parseTimestampUs(line.group(2)));
            parseCueSettingsList(line.group(3), builder);
            sb.setLength(0);
            while (true) {
                line = (Matcher)parsableByteArray.readLine();
                if (TextUtils.isEmpty((CharSequence)line)) {
                    break;
                }
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(((String)line).trim());
            }
            parseCueText(s, sb.toString(), builder, list);
            return true;
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Skipping cue with bad header: ");
            sb2.append(line.group());
            Log.w("WebvttCueParser", sb2.toString());
            return false;
        }
    }
    
    static void parseCueSettingsList(String matcher, final WebvttCue.Builder builder) {
        matcher = (String)WebvttCueParser.CUE_SETTING_PATTERN.matcher(matcher);
        while (((Matcher)matcher).find()) {
            final String group = ((Matcher)matcher).group(1);
            final String group2 = ((Matcher)matcher).group(2);
            try {
                if ("line".equals(group)) {
                    parseLineAttribute(group2, builder);
                }
                else if ("align".equals(group)) {
                    builder.setTextAlignment(parseTextAlignment(group2));
                }
                else if ("position".equals(group)) {
                    parsePositionAttribute(group2, builder);
                }
                else if ("size".equals(group)) {
                    builder.setWidth(WebvttParserUtil.parsePercentage(group2));
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown cue setting ");
                    sb.append(group);
                    sb.append(":");
                    sb.append(group2);
                    Log.w("WebvttCueParser", sb.toString());
                }
            }
            catch (NumberFormatException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Skipping bad cue setting: ");
                sb2.append(((Matcher)matcher).group());
                Log.w("WebvttCueParser", sb2.toString());
            }
        }
    }
    
    static void parseCueText(final String s, final String s2, final WebvttCue.Builder builder, final List<WebvttCssStyle> list) {
        final SpannableStringBuilder text = new SpannableStringBuilder();
        final ArrayDeque<StartTag> arrayDeque = new ArrayDeque<StartTag>();
        final ArrayList<StyleMatch> list2 = new ArrayList<StyleMatch>();
        int i = 0;
        while (i < s2.length()) {
            final char char1 = s2.charAt(i);
            if (char1 != '&') {
                if (char1 != '<') {
                    text.append(char1);
                    ++i;
                }
                else {
                    final int index = i + 1;
                    if (index >= s2.length()) {
                        i = index;
                    }
                    else {
                        final char char2 = s2.charAt(index);
                        int n = 1;
                        final boolean b = char2 == '/';
                        final int endOfTag = findEndOfTag(s2, index);
                        int n2 = endOfTag - 2;
                        final boolean b2 = s2.charAt(n2) == '/';
                        if (b) {
                            n = 2;
                        }
                        if (!b2) {
                            n2 = endOfTag - 1;
                        }
                        final String substring = s2.substring(i + n, n2);
                        final String tagName = getTagName(substring);
                        i = endOfTag;
                        if (tagName == null) {
                            continue;
                        }
                        if (!isSupportedTag(tagName)) {
                            i = endOfTag;
                        }
                        else if (b) {
                            while (!arrayDeque.isEmpty()) {
                                final StartTag startTag = arrayDeque.pop();
                                applySpansForTag(s, startTag, text, list, list2);
                                if (startTag.name.equals(tagName)) {
                                    i = endOfTag;
                                    continue Label_0313;
                                }
                            }
                            i = endOfTag;
                        }
                        else {
                            i = endOfTag;
                            if (b2) {
                                continue;
                            }
                            arrayDeque.push(StartTag.buildStartTag(substring, text.length()));
                            i = endOfTag;
                        }
                    }
                    Label_0313:;
                }
            }
            else {
                final int beginIndex = i + 1;
                i = s2.indexOf(59, beginIndex);
                final int index2 = s2.indexOf(32, beginIndex);
                if (i == -1) {
                    i = index2;
                }
                else if (index2 != -1) {
                    i = Math.min(i, index2);
                }
                if (i != -1) {
                    applyEntity(s2.substring(beginIndex, i), text);
                    if (i == index2) {
                        text.append((CharSequence)" ");
                    }
                    ++i;
                }
                else {
                    text.append(char1);
                    i = beginIndex;
                }
            }
        }
        while (!arrayDeque.isEmpty()) {
            applySpansForTag(s, arrayDeque.pop(), text, list, list2);
        }
        applySpansForTag(s, StartTag.buildWholeCueVirtualTag(), text, list, list2);
        builder.setText(text);
    }
    
    private static void parseLineAttribute(String substring, final WebvttCue.Builder builder) throws NumberFormatException {
        final int index = substring.indexOf(44);
        if (index != -1) {
            builder.setLineAnchor(parsePositionAnchor(substring.substring(index + 1)));
            substring = substring.substring(0, index);
        }
        else {
            builder.setLineAnchor(Integer.MIN_VALUE);
        }
        if (substring.endsWith("%")) {
            builder.setLine(WebvttParserUtil.parsePercentage(substring));
            builder.setLineType(0);
        }
        else {
            final int int1 = Integer.parseInt(substring);
            int n;
            if ((n = int1) < 0) {
                n = int1 - 1;
            }
            builder.setLine((float)n);
            builder.setLineType(1);
        }
    }
    
    private static int parsePositionAnchor(final String str) {
        int n = 0;
        Label_0113: {
            switch (str.hashCode()) {
                case 109757538: {
                    if (str.equals("start")) {
                        n = 0;
                        break Label_0113;
                    }
                    break;
                }
                case 100571: {
                    if (str.equals("end")) {
                        n = 3;
                        break Label_0113;
                    }
                    break;
                }
                case -1074341483: {
                    if (str.equals("middle")) {
                        n = 2;
                        break Label_0113;
                    }
                    break;
                }
                case -1364013995: {
                    if (str.equals("center")) {
                        n = 1;
                        break Label_0113;
                    }
                    break;
                }
            }
            n = -1;
        }
        if (n == 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        if (n != 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid anchor value: ");
            sb.append(str);
            Log.w("WebvttCueParser", sb.toString());
            return Integer.MIN_VALUE;
        }
        return 2;
    }
    
    private static void parsePositionAttribute(String substring, final WebvttCue.Builder builder) throws NumberFormatException {
        final int index = substring.indexOf(44);
        if (index != -1) {
            builder.setPositionAnchor(parsePositionAnchor(substring.substring(index + 1)));
            substring = substring.substring(0, index);
        }
        else {
            builder.setPositionAnchor(Integer.MIN_VALUE);
        }
        builder.setPosition(WebvttParserUtil.parsePercentage(substring));
    }
    
    private static Layout$Alignment parseTextAlignment(final String str) {
        int n = 0;
        Label_0159: {
            switch (str.hashCode()) {
                case 109757538: {
                    if (str.equals("start")) {
                        n = 0;
                        break Label_0159;
                    }
                    break;
                }
                case 108511772: {
                    if (str.equals("right")) {
                        n = 5;
                        break Label_0159;
                    }
                    break;
                }
                case 3317767: {
                    if (str.equals("left")) {
                        n = 1;
                        break Label_0159;
                    }
                    break;
                }
                case 100571: {
                    if (str.equals("end")) {
                        n = 4;
                        break Label_0159;
                    }
                    break;
                }
                case -1074341483: {
                    if (str.equals("middle")) {
                        n = 3;
                        break Label_0159;
                    }
                    break;
                }
                case -1364013995: {
                    if (str.equals("center")) {
                        n = 2;
                        break Label_0159;
                    }
                    break;
                }
            }
            n = -1;
        }
        if (n == 0 || n == 1) {
            return Layout$Alignment.ALIGN_NORMAL;
        }
        if (n == 2 || n == 3) {
            return Layout$Alignment.ALIGN_CENTER;
        }
        if (n != 4 && n != 5) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid alignment value: ");
            sb.append(str);
            Log.w("WebvttCueParser", sb.toString());
            return null;
        }
        return Layout$Alignment.ALIGN_OPPOSITE;
    }
    
    public boolean parseCue(final ParsableByteArray parsableByteArray, final WebvttCue.Builder builder, final List<WebvttCssStyle> list) {
        final String line = parsableByteArray.readLine();
        if (line == null) {
            return false;
        }
        final Matcher matcher = WebvttCueParser.CUE_HEADER_PATTERN.matcher(line);
        if (matcher.matches()) {
            return parseCue(null, matcher, parsableByteArray, builder, this.textBuilder, list);
        }
        final String line2 = parsableByteArray.readLine();
        if (line2 == null) {
            return false;
        }
        final Matcher matcher2 = WebvttCueParser.CUE_HEADER_PATTERN.matcher(line2);
        return matcher2.matches() && parseCue(line.trim(), matcher2, parsableByteArray, builder, this.textBuilder, list);
    }
    
    private static final class StartTag
    {
        private static final String[] NO_CLASSES;
        public final String[] classes;
        public final String name;
        public final int position;
        public final String voice;
        
        static {
            NO_CLASSES = new String[0];
        }
        
        private StartTag(final String name, final int position, final String voice, final String[] classes) {
            this.position = position;
            this.name = name;
            this.voice = voice;
            this.classes = classes;
        }
        
        public static StartTag buildStartTag(String trim, final int n) {
            String s = trim.trim();
            if (s.isEmpty()) {
                return null;
            }
            final int index = s.indexOf(" ");
            if (index == -1) {
                trim = "";
            }
            else {
                trim = s.substring(index).trim();
                s = s.substring(0, index);
            }
            final String[] split = Util.split(s, "\\.");
            final String s2 = split[0];
            String[] no_CLASSES;
            if (split.length > 1) {
                no_CLASSES = Arrays.copyOfRange(split, 1, split.length);
            }
            else {
                no_CLASSES = StartTag.NO_CLASSES;
            }
            return new StartTag(s2, n, trim, no_CLASSES);
        }
        
        public static StartTag buildWholeCueVirtualTag() {
            return new StartTag("", 0, "", new String[0]);
        }
    }
    
    private static final class StyleMatch implements Comparable<StyleMatch>
    {
        public final int score;
        public final WebvttCssStyle style;
        
        public StyleMatch(final int score, final WebvttCssStyle style) {
            this.score = score;
            this.style = style;
        }
        
        @Override
        public int compareTo(final StyleMatch styleMatch) {
            return this.score - styleMatch.score;
        }
    }
}
