// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.support.annotation.NonNull;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.Pair;
import android.support.annotation.RequiresApi;

@RequiresApi(9)
class PaintCompatGingerbread
{
    private static final String TOFU_STRING = "\udb3f\udffd";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal;
    
    static {
        sRectThreadLocal = new ThreadLocal<Pair<Rect, Rect>>();
    }
    
    static boolean hasGlyph(@NonNull final Paint paint, @NonNull final String s) {
        final boolean b = false;
        final int length = s.length();
        boolean b2;
        if (length == 1 && Character.isWhitespace(s.charAt(0))) {
            b2 = true;
        }
        else {
            final float measureText = paint.measureText("\udb3f\udffd");
            final float measureText2 = paint.measureText(s);
            b2 = b;
            if (measureText2 != 0.0f) {
                if (s.codePointCount(0, s.length()) > 1) {
                    b2 = b;
                    if (measureText2 > 2.0f * measureText) {
                        return b2;
                    }
                    float n = 0.0f;
                    int charCount;
                    for (int i = 0; i < length; i += charCount) {
                        charCount = Character.charCount(s.codePointAt(i));
                        n += paint.measureText(s, i, i + charCount);
                    }
                    b2 = b;
                    if (measureText2 >= n) {
                        return b2;
                    }
                }
                if (measureText2 != measureText) {
                    b2 = true;
                }
                else {
                    final Pair<Rect, Rect> obtainEmptyRects = obtainEmptyRects();
                    paint.getTextBounds("\udb3f\udffd", 0, "\udb3f\udffd".length(), (Rect)obtainEmptyRects.first);
                    paint.getTextBounds(s, 0, length, (Rect)obtainEmptyRects.second);
                    b2 = !obtainEmptyRects.first.equals((Object)obtainEmptyRects.second);
                }
            }
        }
        return b2;
    }
    
    private static Pair<Rect, Rect> obtainEmptyRects() {
        Pair<Rect, Rect> value = PaintCompatGingerbread.sRectThreadLocal.get();
        if (value == null) {
            value = new Pair<Rect, Rect>(new Rect(), new Rect());
            PaintCompatGingerbread.sRectThreadLocal.set(value);
        }
        else {
            value.first.setEmpty();
            value.second.setEmpty();
        }
        return value;
    }
}
