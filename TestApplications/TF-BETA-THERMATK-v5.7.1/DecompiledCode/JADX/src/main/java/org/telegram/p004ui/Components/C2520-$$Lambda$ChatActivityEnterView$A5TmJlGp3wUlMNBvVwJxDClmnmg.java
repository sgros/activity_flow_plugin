package org.telegram.p004ui.Components;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg */
public final /* synthetic */ class C2520-$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg implements AnimatorUpdateListener {
    private final /* synthetic */ ChatActivityEnterView f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C2520-$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg(ChatActivityEnterView chatActivityEnterView, int i) {
        this.f$0 = chatActivityEnterView;
        this.f$1 = i;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setStickersExpanded$21$ChatActivityEnterView(this.f$1, valueAnimator);
    }
}
