package org.mozilla.rocket.banner;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.rocket.banner.-$$Lambda$SingleButtonViewHolder$AVBmSa5cyayG5TiNQYmdowIvtqM */
public final /* synthetic */ class C0589-$$Lambda$SingleButtonViewHolder$AVBmSa5cyayG5TiNQYmdowIvtqM implements OnClickListener {
    private final /* synthetic */ SingleButtonViewHolder f$0;
    private final /* synthetic */ BannerDAO f$1;

    public /* synthetic */ C0589-$$Lambda$SingleButtonViewHolder$AVBmSa5cyayG5TiNQYmdowIvtqM(SingleButtonViewHolder singleButtonViewHolder, BannerDAO bannerDAO) {
        this.f$0 = singleButtonViewHolder;
        this.f$1 = bannerDAO;
    }

    public final void onClick(View view) {
        SingleButtonViewHolder.lambda$onBindViewHolder$0(this.f$0, this.f$1, view);
    }
}
