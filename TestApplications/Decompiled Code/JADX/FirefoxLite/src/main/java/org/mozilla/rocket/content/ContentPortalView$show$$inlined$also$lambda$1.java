package org.mozilla.rocket.content;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/* compiled from: ContentPortalView.kt */
public final class ContentPortalView$show$$inlined$also$lambda$1 implements AnimationListener {
    final /* synthetic */ ContentPortalView this$0;

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    ContentPortalView$show$$inlined$also$lambda$1(ContentPortalView contentPortalView) {
        this.this$0 = contentPortalView;
    }

    public void onAnimationEnd(Animation animation) {
        this.this$0.showInternal();
    }
}
