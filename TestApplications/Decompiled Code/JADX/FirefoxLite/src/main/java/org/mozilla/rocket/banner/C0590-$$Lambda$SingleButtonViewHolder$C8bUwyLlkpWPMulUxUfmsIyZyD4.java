package org.mozilla.rocket.banner;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.rocket.banner.-$$Lambda$SingleButtonViewHolder$C8bUwyLlkpWPMulUxUfmsIyZyD4 */
public final /* synthetic */ class C0590-$$Lambda$SingleButtonViewHolder$C8bUwyLlkpWPMulUxUfmsIyZyD4 implements OnClickListener {
    private final /* synthetic */ SingleButtonViewHolder f$0;

    public /* synthetic */ C0590-$$Lambda$SingleButtonViewHolder$C8bUwyLlkpWPMulUxUfmsIyZyD4(SingleButtonViewHolder singleButtonViewHolder) {
        this.f$0 = singleButtonViewHolder;
    }

    public final void onClick(View view) {
        this.f$0.sendClickBackgroundTelemetry();
    }
}
