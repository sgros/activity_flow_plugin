package org.mozilla.rocket.banner;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.rocket.banner.-$$Lambda$FourSitesViewHolder$ue0qc_Qan64E80hyBP1XFfiyLJQ */
public final /* synthetic */ class C0587-$$Lambda$FourSitesViewHolder$ue0qc_Qan64E80hyBP1XFfiyLJQ implements OnClickListener {
    private final /* synthetic */ FourSitesViewHolder f$0;

    public /* synthetic */ C0587-$$Lambda$FourSitesViewHolder$ue0qc_Qan64E80hyBP1XFfiyLJQ(FourSitesViewHolder fourSitesViewHolder) {
        this.f$0 = fourSitesViewHolder;
    }

    public final void onClick(View view) {
        this.f$0.sendClickBackgroundTelemetry();
    }
}
