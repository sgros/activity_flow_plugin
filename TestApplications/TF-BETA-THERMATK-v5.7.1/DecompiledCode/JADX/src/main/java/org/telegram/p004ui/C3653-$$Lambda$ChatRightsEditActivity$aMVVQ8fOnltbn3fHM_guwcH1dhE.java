package org.telegram.p004ui;

import android.content.Context;
import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatRightsEditActivity$aMVVQ8fOnltbn3fHM_guwcH1dhE */
public final /* synthetic */ class C3653-$$Lambda$ChatRightsEditActivity$aMVVQ8fOnltbn3fHM_guwcH1dhE implements OnItemClickListener {
    private final /* synthetic */ ChatRightsEditActivity f$0;
    private final /* synthetic */ Context f$1;

    public /* synthetic */ C3653-$$Lambda$ChatRightsEditActivity$aMVVQ8fOnltbn3fHM_guwcH1dhE(ChatRightsEditActivity chatRightsEditActivity, Context context) {
        this.f$0 = chatRightsEditActivity;
        this.f$1 = context;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$6$ChatRightsEditActivity(this.f$1, view, i);
    }
}
