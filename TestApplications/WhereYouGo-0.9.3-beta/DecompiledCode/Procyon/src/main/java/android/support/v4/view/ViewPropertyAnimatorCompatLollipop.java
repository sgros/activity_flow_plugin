// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.View;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(21)
@RequiresApi(21)
class ViewPropertyAnimatorCompatLollipop
{
    public static void translationZ(final View view, final float n) {
        view.animate().translationZ(n);
    }
    
    public static void translationZBy(final View view, final float n) {
        view.animate().translationZBy(n);
    }
    
    public static void z(final View view, final float n) {
        view.animate().z(n);
    }
    
    public static void zBy(final View view, final float n) {
        view.animate().zBy(n);
    }
}
