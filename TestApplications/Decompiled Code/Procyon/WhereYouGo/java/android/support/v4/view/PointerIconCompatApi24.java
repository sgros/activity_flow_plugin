// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.content.res.Resources;
import android.content.Context;
import android.view.PointerIcon;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(24)
@RequiresApi(24)
class PointerIconCompatApi24
{
    public static Object create(final Bitmap bitmap, final float n, final float n2) {
        return PointerIcon.create(bitmap, n, n2);
    }
    
    public static Object getSystemIcon(final Context context, final int n) {
        return PointerIcon.getSystemIcon(context, n);
    }
    
    public static Object load(final Resources resources, final int n) {
        return PointerIcon.load(resources, n);
    }
}
