// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.support.annotation.NonNull;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.Pair;

class PaintCompatApi14
{
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "\udb3f\udffd";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal;
    
    static {
        sRectThreadLocal = new ThreadLocal<Pair<Rect, Rect>>();
    }
    
    static boolean hasGlyph(@NonNull final Paint paint, @NonNull final String s) {
        final int length = s.length();
        if (length == 1 && Character.isWhitespace(s.charAt(0))) {
            return true;
        }
        final float measureText = paint.measureText("\udb3f\udffd");
        final float measureText2 = paint.measureText("m");
        final float measureText3 = paint.measureText(s);
        float n = 0.0f;
        if (measureText3 == 0.0f) {
            return false;
        }
        if (s.codePointCount(0, s.length()) > 1) {
            if (measureText3 > 2.0f * measureText2) {
                return false;
            }
            int n2;
            for (int i = 0; i < length; i = n2) {
                n2 = Character.charCount(s.codePointAt(i)) + i;
                n += paint.measureText(s, i, n2);
            }
            if (measureText3 >= n) {
                return false;
            }
        }
        if (measureText3 != measureText) {
            return true;
        }
        final Pair<Rect, Rect> obtainEmptyRects = obtainEmptyRects();
        paint.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)obtainEmptyRects.first);
        paint.getTextBounds(s, 0, length, (Rect)obtainEmptyRects.second);
        return obtainEmptyRects.first.equals((Object)obtainEmptyRects.second) ^ true;
    }
    
    private static Pair<Rect, Rect> obtainEmptyRects() {
        Pair<Rect, Rect> value = PaintCompatApi14.sRectThreadLocal.get();
        if (value == null) {
            value = new Pair<Rect, Rect>(new Rect(), new Rect());
            PaintCompatApi14.sRectThreadLocal.set(value);
        }
        else {
            value.first.setEmpty();
            value.second.setEmpty();
        }
        return value;
    }
}
