package org.mozilla.rocket.privately.home;

import android.arch.lifecycle.Observer;

/* compiled from: PrivateHomeFragment.kt */
final class PrivateHomeFragment$observeViewModel$$inlined$apply$lambda$1<T> implements Observer<Boolean> {
    final /* synthetic */ PrivateHomeFragment this$0;

    PrivateHomeFragment$observeViewModel$$inlined$apply$lambda$1(PrivateHomeFragment privateHomeFragment) {
        this.this$0 = privateHomeFragment;
    }

    public final void onChanged(Boolean bool) {
        if (bool != null) {
            this.this$0.onUrlInputScreenVisible(bool.booleanValue());
        }
    }
}
