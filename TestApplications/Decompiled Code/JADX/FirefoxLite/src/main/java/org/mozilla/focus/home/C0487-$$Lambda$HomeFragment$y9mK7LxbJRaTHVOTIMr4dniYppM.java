package org.mozilla.focus.home;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$y9mK7LxbJRaTHVOTIMr4dniYppM */
public final /* synthetic */ class C0487-$$Lambda$HomeFragment$y9mK7LxbJRaTHVOTIMr4dniYppM implements OnClickListener {
    private final /* synthetic */ HomeFragment f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C0487-$$Lambda$HomeFragment$y9mK7LxbJRaTHVOTIMr4dniYppM(HomeFragment homeFragment, String str) {
        this.f$0 = homeFragment;
        this.f$1 = str;
    }

    public final void onClick(View view) {
        HomeFragment.lambda$initFeatureSurveyViewIfNecessary$12(this.f$0, this.f$1, view);
    }
}
