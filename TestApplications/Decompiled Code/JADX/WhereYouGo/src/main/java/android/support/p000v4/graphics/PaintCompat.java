package android.support.p000v4.graphics;

import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

/* renamed from: android.support.v4.graphics.PaintCompat */
public final class PaintCompat {
    public static boolean hasGlyph(@NonNull Paint paint, @NonNull String string) {
        if (VERSION.SDK_INT >= 23) {
            return PaintCompatApi23.hasGlyph(paint, string);
        }
        return PaintCompatGingerbread.hasGlyph(paint, string);
    }

    private PaintCompat() {
    }
}
