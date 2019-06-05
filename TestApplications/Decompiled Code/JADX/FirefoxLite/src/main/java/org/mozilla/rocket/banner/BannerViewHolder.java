package org.mozilla.rocket.banner;

import android.content.Context;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import org.mozilla.focus.telemetry.TelemetryWrapper;

public abstract class BannerViewHolder extends ViewHolder {
    /* renamed from: id */
    protected String f68id;

    BannerViewHolder(View view) {
        super(view);
    }

    public void onBindViewHolder(Context context, BannerDAO bannerDAO) {
        this.f68id = bannerDAO.f53id;
    }

    /* Access modifiers changed, original: protected|final */
    public final void sendClickItemTelemetry(int i) {
        if (this.f68id != null) {
            TelemetryWrapper.clickBannerItem(this.f68id, i);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void sendClickBackgroundTelemetry() {
        if (this.f68id != null) {
            TelemetryWrapper.clickBannerBackground(this.f68id);
        }
    }

    public final String getId() {
        return this.f68id;
    }
}
