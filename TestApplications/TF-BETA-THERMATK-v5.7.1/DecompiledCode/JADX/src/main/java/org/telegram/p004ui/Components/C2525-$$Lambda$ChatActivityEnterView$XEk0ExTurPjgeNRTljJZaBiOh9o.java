package org.telegram.p004ui.Components;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ChatActivityEnterView$XEk0ExTurPjgeNRTljJZaBiOh9o */
public final /* synthetic */ class C2525-$$Lambda$ChatActivityEnterView$XEk0ExTurPjgeNRTljJZaBiOh9o implements AnimatorUpdateListener {
    private final /* synthetic */ ChatActivityEnterView f$0;

    public /* synthetic */ C2525-$$Lambda$ChatActivityEnterView$XEk0ExTurPjgeNRTljJZaBiOh9o(ChatActivityEnterView chatActivityEnterView) {
        this.f$0 = chatActivityEnterView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$checkStickresExpandHeight$19$ChatActivityEnterView(valueAnimator);
    }
}
