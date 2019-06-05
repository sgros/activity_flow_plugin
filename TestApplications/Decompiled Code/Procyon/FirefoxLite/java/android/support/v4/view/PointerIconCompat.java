// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.PointerIcon;
import android.os.Build$VERSION;
import android.content.Context;

public final class PointerIconCompat
{
    private Object mPointerIcon;
    
    private PointerIconCompat(final Object mPointerIcon) {
        this.mPointerIcon = mPointerIcon;
    }
    
    public static PointerIconCompat getSystemIcon(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return new PointerIconCompat(PointerIcon.getSystemIcon(context, n));
        }
        return new PointerIconCompat(null);
    }
    
    public Object getPointerIcon() {
        return this.mPointerIcon;
    }
}
