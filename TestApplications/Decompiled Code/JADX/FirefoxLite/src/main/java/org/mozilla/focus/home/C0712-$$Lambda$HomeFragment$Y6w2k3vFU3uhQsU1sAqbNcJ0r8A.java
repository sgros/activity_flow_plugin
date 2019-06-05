package org.mozilla.focus.home;

import android.arch.lifecycle.Observer;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A */
public final /* synthetic */ class C0712-$$Lambda$HomeFragment$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A implements Observer {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0712-$$Lambda$HomeFragment$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void onChanged(Object obj) {
        this.f$0.setUpBannerFromConfig((String[]) obj);
    }
}
