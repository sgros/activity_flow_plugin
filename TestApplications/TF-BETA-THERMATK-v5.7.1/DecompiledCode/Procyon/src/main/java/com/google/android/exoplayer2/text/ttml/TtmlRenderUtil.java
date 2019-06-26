// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ttml;

import java.util.Map;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.AlignmentSpan$Standard;
import android.text.style.TypefaceSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.SpannableStringBuilder;

final class TtmlRenderUtil
{
    public static void applyStylesToSpan(final SpannableStringBuilder spannableStringBuilder, final int n, final int n2, final TtmlStyle ttmlStyle) {
        if (ttmlStyle.getStyle() != -1) {
            spannableStringBuilder.setSpan((Object)new StyleSpan(ttmlStyle.getStyle()), n, n2, 33);
        }
        if (ttmlStyle.isLinethrough()) {
            spannableStringBuilder.setSpan((Object)new StrikethroughSpan(), n, n2, 33);
        }
        if (ttmlStyle.isUnderline()) {
            spannableStringBuilder.setSpan((Object)new UnderlineSpan(), n, n2, 33);
        }
        if (ttmlStyle.hasFontColor()) {
            spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(ttmlStyle.getFontColor()), n, n2, 33);
        }
        if (ttmlStyle.hasBackgroundColor()) {
            spannableStringBuilder.setSpan((Object)new BackgroundColorSpan(ttmlStyle.getBackgroundColor()), n, n2, 33);
        }
        if (ttmlStyle.getFontFamily() != null) {
            spannableStringBuilder.setSpan((Object)new TypefaceSpan(ttmlStyle.getFontFamily()), n, n2, 33);
        }
        if (ttmlStyle.getTextAlign() != null) {
            spannableStringBuilder.setSpan((Object)new AlignmentSpan$Standard(ttmlStyle.getTextAlign()), n, n2, 33);
        }
        final int fontSizeUnit = ttmlStyle.getFontSizeUnit();
        if (fontSizeUnit != 1) {
            if (fontSizeUnit != 2) {
                if (fontSizeUnit == 3) {
                    spannableStringBuilder.setSpan((Object)new RelativeSizeSpan(ttmlStyle.getFontSize() / 100.0f), n, n2, 33);
                }
            }
            else {
                spannableStringBuilder.setSpan((Object)new RelativeSizeSpan(ttmlStyle.getFontSize()), n, n2, 33);
            }
        }
        else {
            spannableStringBuilder.setSpan((Object)new AbsoluteSizeSpan((int)ttmlStyle.getFontSize(), true), n, n2, 33);
        }
    }
    
    static String applyTextElementSpacePolicy(final String s) {
        return s.replaceAll("\r\n", "\n").replaceAll(" *\n *", "\n").replaceAll("\n", " ").replaceAll("[ \t\\x0B\f\r]+", " ");
    }
    
    static void endParagraph(final SpannableStringBuilder spannableStringBuilder) {
        int n;
        for (n = spannableStringBuilder.length() - 1; n >= 0 && spannableStringBuilder.charAt(n) == ' '; --n) {}
        if (n >= 0 && spannableStringBuilder.charAt(n) != '\n') {
            spannableStringBuilder.append('\n');
        }
    }
    
    public static TtmlStyle resolveStyle(TtmlStyle ttmlStyle, final String[] array, final Map<String, TtmlStyle> map) {
        if (ttmlStyle == null && array == null) {
            return null;
        }
        final int n = 0;
        int i = 0;
        if (ttmlStyle == null && array.length == 1) {
            return map.get(array[0]);
        }
        if (ttmlStyle == null && array.length > 1) {
            ttmlStyle = new TtmlStyle();
            while (i < array.length) {
                ttmlStyle.chain(map.get(array[i]));
                ++i;
            }
            return ttmlStyle;
        }
        if (ttmlStyle != null && array != null && array.length == 1) {
            ttmlStyle.chain(map.get(array[0]));
            return ttmlStyle;
        }
        if (ttmlStyle != null && array != null && array.length > 1) {
            for (int length = array.length, j = n; j < length; ++j) {
                ttmlStyle.chain(map.get(array[j]));
            }
        }
        return ttmlStyle;
    }
}
