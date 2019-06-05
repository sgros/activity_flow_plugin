package org.mozilla.rocket.privately.browse;

import android.arch.lifecycle.Observer;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BrowserFragment.kt */
final class BrowserFragment$registerData$1<T> implements Observer<String> {
    final /* synthetic */ BrowserFragment this$0;

    BrowserFragment$registerData$1(BrowserFragment browserFragment) {
        this.this$0 = browserFragment;
    }

    public final void onChanged(String str) {
        if (str != null) {
            BrowserFragment browserFragment = this.this$0;
            Intrinsics.checkExpressionValueIsNotNull(str, "it");
            browserFragment.loadUrl(str, false, false, null);
        }
    }
}
