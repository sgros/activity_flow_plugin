package org.mozilla.rocket.banner;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.rocket.banner.-$$Lambda$FourSitesViewHolder$wlekmEwFW8wMEFcaf4IJ4wCdwHg */
public final /* synthetic */ class C0588-$$Lambda$FourSitesViewHolder$wlekmEwFW8wMEFcaf4IJ4wCdwHg implements OnClickListener {
    private final /* synthetic */ FourSitesViewHolder f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ BannerDAO f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0588-$$Lambda$FourSitesViewHolder$wlekmEwFW8wMEFcaf4IJ4wCdwHg(FourSitesViewHolder fourSitesViewHolder, int i, BannerDAO bannerDAO, int i2) {
        this.f$0 = fourSitesViewHolder;
        this.f$1 = i;
        this.f$2 = bannerDAO;
        this.f$3 = i2;
    }

    public final void onClick(View view) {
        FourSitesViewHolder.lambda$onBindViewHolder$0(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
