package org.mozilla.rocket.nightmode.themed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ThemedRelativeLayout.kt */
public class ThemedRelativeLayout extends RelativeLayout {
    private boolean isNight;

    public ThemedRelativeLayout(Context context, AttributeSet attributeSet) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attributeSet, "attrs");
        super(context, attributeSet);
    }

    public ThemedRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attributeSet, "attrs");
        super(context, attributeSet, i);
    }

    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState;
        if (this.isNight) {
            onCreateDrawableState = super.onCreateDrawableState(i + ThemedWidgetUtils.INSTANCE.getSTATE_NIGHT_MODE().length);
            View.mergeDrawableStates(onCreateDrawableState, ThemedWidgetUtils.INSTANCE.getSTATE_NIGHT_MODE());
            Intrinsics.checkExpressionValueIsNotNull(onCreateDrawableState, "drawableState");
            return onCreateDrawableState;
        }
        onCreateDrawableState = super.onCreateDrawableState(i);
        Intrinsics.checkExpressionValueIsNotNull(onCreateDrawableState, "super.onCreateDrawableState(extraSpace)");
        return onCreateDrawableState;
    }

    public void setNightMode(boolean z) {
        if (this.isNight != z) {
            this.isNight = z;
            refreshDrawableState();
            invalidate();
        }
    }
}
