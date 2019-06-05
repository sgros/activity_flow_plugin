package org.mozilla.focus.tabs.tabtray;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.tabs.tabtray.-$$Lambda$TabTrayFragment$Dt-zQsvhR7gYSWlSVgXL4-cs9S0 */
public final /* synthetic */ class C0519-$$Lambda$TabTrayFragment$Dt-zQsvhR7gYSWlSVgXL4-cs9S0 implements OnClickListener {
    private final /* synthetic */ TabTrayFragment f$0;

    public /* synthetic */ C0519-$$Lambda$TabTrayFragment$Dt-zQsvhR7gYSWlSVgXL4-cs9S0(TabTrayFragment tabTrayFragment) {
        this.f$0 = tabTrayFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        TabTrayFragment.lambda$onCloseAllTabsClicked$6(this.f$0, dialogInterface, i);
    }
}
