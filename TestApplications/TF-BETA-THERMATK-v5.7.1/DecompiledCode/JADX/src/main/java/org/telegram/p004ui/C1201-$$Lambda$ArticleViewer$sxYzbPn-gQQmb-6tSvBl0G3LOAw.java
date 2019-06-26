package org.telegram.p004ui;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$sxYzbPn-gQQmb-6tSvBl0G3LOAw */
public final /* synthetic */ class C1201-$$Lambda$ArticleViewer$sxYzbPn-gQQmb-6tSvBl0G3LOAw implements AnimatorUpdateListener {
    private final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ C1201-$$Lambda$ArticleViewer$sxYzbPn-gQQmb-6tSvBl0G3LOAw(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$checkScrollAnimated$21$ArticleViewer(valueAnimator);
    }
}
