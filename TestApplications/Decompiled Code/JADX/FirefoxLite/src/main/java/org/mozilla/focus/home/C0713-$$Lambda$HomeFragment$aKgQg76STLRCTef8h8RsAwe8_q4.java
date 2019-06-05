package org.mozilla.focus.home;

import android.arch.lifecycle.Observer;
import org.mozilla.rocket.download.DownloadIndicatorViewModel.Status;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4 */
public final /* synthetic */ class C0713-$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4 implements Observer {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0713-$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void onChanged(Object obj) {
        HomeFragment.lambda$onCreateView$5(this.f$0, (Status) obj);
    }
}
