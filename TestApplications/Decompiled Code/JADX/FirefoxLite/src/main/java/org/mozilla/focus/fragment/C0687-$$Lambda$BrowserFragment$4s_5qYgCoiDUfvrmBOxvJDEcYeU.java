package org.mozilla.focus.fragment;

import android.view.View;
import org.mozilla.rocket.download.DownloadIndicatorIntroViewHelper.OnViewInflated;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$BrowserFragment$4s_5qYgCoiDUfvrmBOxvJDEcYeU */
public final /* synthetic */ class C0687-$$Lambda$BrowserFragment$4s_5qYgCoiDUfvrmBOxvJDEcYeU implements OnViewInflated {
    private final /* synthetic */ BrowserFragment f$0;

    public /* synthetic */ C0687-$$Lambda$BrowserFragment$4s_5qYgCoiDUfvrmBOxvJDEcYeU(BrowserFragment browserFragment) {
        this.f$0 = browserFragment;
    }

    public final void onInflated(View view) {
        this.f$0.downloadIndicatorIntro = view;
    }
}
