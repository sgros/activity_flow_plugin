package org.mozilla.rocket.nightmode.themed;

import android.content.Context;
import android.support.p004v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ThemedTextView.kt */
public final class ThemedTextView extends AppCompatTextView {
    private boolean isNight;

    public ThemedTextView(Context context, AttributeSet attributeSet) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attributeSet, "attrs");
        super(context, attributeSet);
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

    public final void setNightMode(boolean z) {
        if (this.isNight != z) {
            this.isNight = z;
            refreshDrawableState();
            invalidate();
        }
    }
}
