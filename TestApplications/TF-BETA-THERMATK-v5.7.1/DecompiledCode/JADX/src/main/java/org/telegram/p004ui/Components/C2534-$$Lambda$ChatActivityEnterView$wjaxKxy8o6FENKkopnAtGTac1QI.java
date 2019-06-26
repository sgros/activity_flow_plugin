package org.telegram.p004ui.Components;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ChatActivityEnterView$wjaxKxy8o6FENKkopnAtGTac1QI */
public final /* synthetic */ class C2534-$$Lambda$ChatActivityEnterView$wjaxKxy8o6FENKkopnAtGTac1QI implements AnimatorUpdateListener {
    private final /* synthetic */ ChatActivityEnterView f$0;

    public /* synthetic */ C2534-$$Lambda$ChatActivityEnterView$wjaxKxy8o6FENKkopnAtGTac1QI(ChatActivityEnterView chatActivityEnterView) {
        this.f$0 = chatActivityEnterView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$checkStickresExpandHeight$18$ChatActivityEnterView(valueAnimator);
    }
}
