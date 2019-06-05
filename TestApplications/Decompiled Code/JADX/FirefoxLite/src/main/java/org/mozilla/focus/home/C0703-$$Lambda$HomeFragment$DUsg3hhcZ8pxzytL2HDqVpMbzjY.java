package org.mozilla.focus.home;

import org.mozilla.focus.widget.FragmentListener.C0572-CC;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.rocket.banner.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY */
public final /* synthetic */ class C0703-$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY implements OnClickListener {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0703-$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void onClick(String str) {
        C0572-CC.notifyParent(this.f$0, TYPE.OPEN_URL_IN_NEW_TAB, str);
    }
}
