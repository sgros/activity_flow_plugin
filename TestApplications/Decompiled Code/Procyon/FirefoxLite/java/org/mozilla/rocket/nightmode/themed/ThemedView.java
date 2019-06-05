// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.nightmode.themed;

import kotlin.jvm.internal.Intrinsics;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

public final class ThemedView extends View
{
    private boolean isNight;
    
    public ThemedView(final Context context, final AttributeSet set) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(set, "attrs");
        super(context, set);
    }
    
    public static final /* synthetic */ int[] access$mergeDrawableStates$s2666181(final int[] array, final int[] array2) {
        return View.mergeDrawableStates(array, array2);
    }
    
    public int[] onCreateDrawableState(final int n) {
        int[] array;
        if (this.isNight) {
            array = super.onCreateDrawableState(n + ThemedWidgetUtils.INSTANCE.getSTATE_NIGHT_MODE().length);
            access$mergeDrawableStates$s2666181(array, ThemedWidgetUtils.INSTANCE.getSTATE_NIGHT_MODE());
            Intrinsics.checkExpressionValueIsNotNull(array, "drawableState");
        }
        else {
            array = super.onCreateDrawableState(n);
            Intrinsics.checkExpressionValueIsNotNull(array, "super.onCreateDrawableState(extraSpace)");
        }
        return array;
    }
    
    public final void setNightMode(final boolean isNight) {
        if (this.isNight != isNight) {
            this.isNight = isNight;
            this.refreshDrawableState();
            this.invalidate();
        }
    }
}
