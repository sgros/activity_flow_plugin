// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.os.Build$VERSION;
import android.view.ViewGroup;

class ViewGroupUtils
{
    static ViewGroupOverlayImpl getOverlay(final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 18) {
            return new ViewGroupOverlayApi18(viewGroup);
        }
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }
    
    static void suppressLayout(final ViewGroup viewGroup, final boolean b) {
        if (Build$VERSION.SDK_INT >= 18) {
            ViewGroupUtilsApi18.suppressLayout(viewGroup, b);
        }
        else {
            ViewGroupUtilsApi14.suppressLayout(viewGroup, b);
        }
    }
}
