// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.graphics.Paint;

public final class PaintCompat
{
    private PaintCompat() {
    }
    
    public static boolean hasGlyph(@NonNull final Paint paint, @NonNull final String s) {
        boolean b;
        if (Build$VERSION.SDK_INT >= 23) {
            b = PaintCompatApi23.hasGlyph(paint, s);
        }
        else {
            b = PaintCompatGingerbread.hasGlyph(paint, s);
        }
        return b;
    }
}
