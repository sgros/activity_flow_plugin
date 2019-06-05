package org.mozilla.rocket.banner;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.rocket.banner.-$$Lambda$BasicViewHolder$9mY-ye4sx7Y5ekdaoixKpobbXSE */
public final /* synthetic */ class C0586-$$Lambda$BasicViewHolder$9mY-ye4sx7Y5ekdaoixKpobbXSE implements OnClickListener {
    private final /* synthetic */ BasicViewHolder f$0;
    private final /* synthetic */ BannerDAO f$1;

    public /* synthetic */ C0586-$$Lambda$BasicViewHolder$9mY-ye4sx7Y5ekdaoixKpobbXSE(BasicViewHolder basicViewHolder, BannerDAO bannerDAO) {
        this.f$0 = basicViewHolder;
        this.f$1 = bannerDAO;
    }

    public final void onClick(View view) {
        BasicViewHolder.lambda$onBindViewHolder$0(this.f$0, this.f$1, view);
    }
}
