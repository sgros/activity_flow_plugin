package org.mozilla.rocket.privately.home;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/* compiled from: PrivateHomeFragment.kt */
public final class PrivateHomeFragment$onCreateAnimation$1 implements AnimationListener {
    final /* synthetic */ PrivateHomeFragment this$0;

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    PrivateHomeFragment$onCreateAnimation$1(PrivateHomeFragment privateHomeFragment) {
        this.this$0 = privateHomeFragment;
    }

    public void onAnimationEnd(Animation animation) {
        this.this$0.animatePrivateHome();
    }
}
