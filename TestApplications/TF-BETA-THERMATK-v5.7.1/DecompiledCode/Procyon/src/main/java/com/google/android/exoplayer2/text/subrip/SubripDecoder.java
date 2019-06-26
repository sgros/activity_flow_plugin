// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.subrip;

import android.text.Html;
import android.text.TextUtils;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.Subtitle;
import java.util.regex.Matcher;
import android.text.Layout$Alignment;
import com.google.android.exoplayer2.text.Cue;
import android.text.Spanned;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class SubripDecoder extends SimpleSubtitleDecoder
{
    private static final Pattern SUBRIP_TAG_PATTERN;
    private static final Pattern SUBRIP_TIMING_LINE;
    private final ArrayList<String> tags;
    private final StringBuilder textBuilder;
    
    static {
        SUBRIP_TIMING_LINE = Pattern.compile("\\s*((?:(\\d+):)?(\\d+):(\\d+),(\\d+))\\s*-->\\s*((?:(\\d+):)?(\\d+):(\\d+),(\\d+))?\\s*");
        SUBRIP_TAG_PATTERN = Pattern.compile("\\{\\\\.*?\\}");
    }
    
    public SubripDecoder() {
        super("SubripDecoder");
        this.textBuilder = new StringBuilder();
        this.tags = new ArrayList<String>();
    }
    
    private Cue buildCue(final Spanned spanned, final String s) {
        if (s == null) {
            return new Cue((CharSequence)spanned);
        }
        int n = 0;
        Label_0234: {
            switch (s.hashCode()) {
                case -685620462: {
                    if (s.equals("{\\an9}")) {
                        n = 5;
                        break Label_0234;
                    }
                    break;
                }
                case -685620493: {
                    if (s.equals("{\\an8}")) {
                        n = 8;
                        break Label_0234;
                    }
                    break;
                }
                case -685620524: {
                    if (s.equals("{\\an7}")) {
                        n = 2;
                        break Label_0234;
                    }
                    break;
                }
                case -685620555: {
                    if (s.equals("{\\an6}")) {
                        n = 4;
                        break Label_0234;
                    }
                    break;
                }
                case -685620586: {
                    if (s.equals("{\\an5}")) {
                        n = 7;
                        break Label_0234;
                    }
                    break;
                }
                case -685620617: {
                    if (s.equals("{\\an4}")) {
                        n = 1;
                        break Label_0234;
                    }
                    break;
                }
                case -685620648: {
                    if (s.equals("{\\an3}")) {
                        n = 3;
                        break Label_0234;
                    }
                    break;
                }
                case -685620679: {
                    if (s.equals("{\\an2}")) {
                        n = 6;
                        break Label_0234;
                    }
                    break;
                }
                case -685620710: {
                    if (s.equals("{\\an1}")) {
                        n = 0;
                        break Label_0234;
                    }
                    break;
                }
            }
            n = -1;
        }
        int n2;
        if (n != 0 && n != 1 && n != 2) {
            if (n != 3 && n != 4 && n != 5) {
                n2 = 1;
            }
            else {
                n2 = 2;
            }
        }
        else {
            n2 = 0;
        }
        int n3 = 0;
        Label_0498: {
            switch (s.hashCode()) {
                case -685620462: {
                    if (s.equals("{\\an9}")) {
                        n3 = 5;
                        break Label_0498;
                    }
                    break;
                }
                case -685620493: {
                    if (s.equals("{\\an8}")) {
                        n3 = 4;
                        break Label_0498;
                    }
                    break;
                }
                case -685620524: {
                    if (s.equals("{\\an7}")) {
                        n3 = 3;
                        break Label_0498;
                    }
                    break;
                }
                case -685620555: {
                    if (s.equals("{\\an6}")) {
                        n3 = 8;
                        break Label_0498;
                    }
                    break;
                }
                case -685620586: {
                    if (s.equals("{\\an5}")) {
                        n3 = 7;
                        break Label_0498;
                    }
                    break;
                }
                case -685620617: {
                    if (s.equals("{\\an4}")) {
                        n3 = 6;
                        break Label_0498;
                    }
                    break;
                }
                case -685620648: {
                    if (s.equals("{\\an3}")) {
                        n3 = 2;
                        break Label_0498;
                    }
                    break;
                }
                case -685620679: {
                    if (s.equals("{\\an2}")) {
                        n3 = 1;
                        break Label_0498;
                    }
                    break;
                }
                case -685620710: {
                    if (s.equals("{\\an1}")) {
                        n3 = 0;
                        break Label_0498;
                    }
                    break;
                }
            }
            n3 = -1;
        }
        int n4;
        if (n3 != 0 && n3 != 1 && n3 != 2) {
            if (n3 != 3 && n3 != 4 && n3 != 5) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
        }
        else {
            n4 = 2;
        }
        return new Cue((CharSequence)spanned, null, getFractionalPositionForAnchorType(n4), 0, n4, getFractionalPositionForAnchorType(n2), n2, Float.MIN_VALUE);
    }
    
    static float getFractionalPositionForAnchorType(final int n) {
        if (n == 0) {
            return 0.08f;
        }
        if (n != 1) {
            return 0.92f;
        }
        return 0.5f;
    }
    
    private static long parseTimecode(final Matcher matcher, final int n) {
        return (Long.parseLong(matcher.group(n + 1)) * 60L * 60L * 1000L + Long.parseLong(matcher.group(n + 2)) * 60L * 1000L + Long.parseLong(matcher.group(n + 3)) * 1000L + Long.parseLong(matcher.group(n + 4))) * 1000L;
    }
    
    private String processLine(final String s, final ArrayList<String> list) {
        final String trim = s.trim();
        final StringBuilder sb = new StringBuilder(trim);
        final Matcher matcher = SubripDecoder.SUBRIP_TAG_PATTERN.matcher(trim);
        int n = 0;
        while (matcher.find()) {
            final String group = matcher.group();
            list.add(group);
            final int start = matcher.start() - n;
            final int length = group.length();
            sb.replace(start, start + length, "");
            n += length;
        }
        return sb.toString();
    }
    
    @Override
    protected SubripSubtitle decode(byte[] array, int n, final boolean b) {
        final ArrayList<Cue> list = new ArrayList<Cue>();
        final LongArray longArray = new LongArray();
        final ParsableByteArray parsableByteArray = new ParsableByteArray(array, n);
    Label_0029:
        while (true) {
            array = (byte[])(Object)parsableByteArray.readLine();
            if (array == null) {
                break;
            }
            if (((String)(Object)array).length() == 0) {
                continue;
            }
            try {
                Integer.parseInt((String)(Object)array);
                array = (byte[])(Object)parsableByteArray.readLine();
                if (array == null) {
                    Log.w("SubripDecoder", "Unexpected end");
                    break;
                }
                final Matcher matcher = SubripDecoder.SUBRIP_TIMING_LINE.matcher((CharSequence)(Object)array);
                if (matcher.matches()) {
                    n = 1;
                    longArray.add(parseTimecode(matcher, 1));
                    final boolean empty = TextUtils.isEmpty((CharSequence)matcher.group(6));
                    int i = 0;
                    if (!empty) {
                        longArray.add(parseTimecode(matcher, 6));
                    }
                    else {
                        n = 0;
                    }
                    this.textBuilder.setLength(0);
                    this.tags.clear();
                    while (true) {
                        array = (byte[])(Object)parsableByteArray.readLine();
                        if (TextUtils.isEmpty((CharSequence)(Object)array)) {
                            break;
                        }
                        if (this.textBuilder.length() > 0) {
                            this.textBuilder.append("<br>");
                        }
                        this.textBuilder.append(this.processLine((String)(Object)array, this.tags));
                    }
                    final Spanned fromHtml = Html.fromHtml(this.textBuilder.toString());
                    while (true) {
                        while (i < this.tags.size()) {
                            array = (byte[])(Object)this.tags.get(i);
                            if (((String)(Object)array).matches("\\{\\\\an[1-9]\\}")) {
                                list.add(this.buildCue(fromHtml, (String)(Object)array));
                                if (n != 0) {
                                    list.add(null);
                                    continue Label_0029;
                                }
                                continue Label_0029;
                            }
                            else {
                                ++i;
                            }
                        }
                        array = null;
                        continue;
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Skipping invalid timing: ");
                sb.append((String)(Object)array);
                Log.w("SubripDecoder", sb.toString());
            }
            catch (NumberFormatException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Skipping invalid index: ");
                sb2.append((String)(Object)array);
                Log.w("SubripDecoder", sb2.toString());
            }
        }
        final Cue[] a = new Cue[list.size()];
        list.toArray(a);
        return new SubripSubtitle(a, longArray.toArray());
    }
}
