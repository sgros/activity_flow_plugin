package org.mozilla.focus.urlinput;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import org.mozilla.focus.utils.ViewUtils;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$onCreateView$3 implements OnFocusChangeListener {
    final /* synthetic */ UrlInputFragment this$0;

    UrlInputFragment$onCreateView$3(UrlInputFragment urlInputFragment) {
        this.this$0 = urlInputFragment;
    }

    public final void onFocusChange(View view, boolean z) {
        if (z) {
            ViewUtils.showKeyboard(UrlInputFragment.access$getUrlView$p(this.this$0));
        } else {
            ViewUtils.hideKeyboard(UrlInputFragment.access$getUrlView$p(this.this$0));
        }
    }
}
