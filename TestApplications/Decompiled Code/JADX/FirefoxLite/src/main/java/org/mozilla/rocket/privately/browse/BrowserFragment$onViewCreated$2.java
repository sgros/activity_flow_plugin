package org.mozilla.rocket.privately.browse;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: BrowserFragment.kt */
final class BrowserFragment$onViewCreated$2 implements OnClickListener {
    final /* synthetic */ BrowserFragment this$0;

    BrowserFragment$onViewCreated$2(BrowserFragment browserFragment) {
        this.this$0 = browserFragment;
    }

    public final void onClick(View view) {
        this.this$0.onModeClicked();
    }
}