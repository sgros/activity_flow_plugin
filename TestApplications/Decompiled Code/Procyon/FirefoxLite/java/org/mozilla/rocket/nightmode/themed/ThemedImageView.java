// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.nightmode.themed;

import android.view.View;
import android.util.AttributeSet;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public class ThemedImageView extends AppCompatImageView
{
    private boolean isNight;
    
    public ThemedImageView(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context);
    }
    
    public ThemedImageView(final Context context, final AttributeSet set) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(set, "attrs");
        super(context, set);
    }
    
    public ThemedImageView(final Context context, final AttributeSet set, final int n) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(set, "attrs");
        super(context, set, n);
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
    
    public void setNightMode(final boolean isNight) {
        if (this.isNight != isNight) {
            this.isNight = isNight;
            this.refreshDrawableState();
            this.invalidate();
        }
    }
}
