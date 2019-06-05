package org.mozilla.focus.home;

import android.content.Context;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4 */
public final /* synthetic */ class C0704-$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4 implements OnRootConfigLoadedListener {
    private final /* synthetic */ HomeFragment f$0;
    private final /* synthetic */ Context f$1;

    public /* synthetic */ C0704-$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4(HomeFragment homeFragment, Context context) {
        this.f$0 = homeFragment;
        this.f$1 = context;
    }

    public final void onRootConfigLoaded(String[] strArr) {
        HomeFragment.lambda$initBanner$0(this.f$0, this.f$1, strArr);
    }
}
