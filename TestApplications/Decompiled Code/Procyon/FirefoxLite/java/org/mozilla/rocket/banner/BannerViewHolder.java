// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.banner;

import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.content.Context;
import android.view.View;
import android.support.v7.widget.RecyclerView;

public abstract class BannerViewHolder extends ViewHolder
{
    protected String id;
    
    BannerViewHolder(final View view) {
        super(view);
    }
    
    public final String getId() {
        return this.id;
    }
    
    public void onBindViewHolder(final Context context, final BannerDAO bannerDAO) {
        this.id = bannerDAO.id;
    }
    
    protected final void sendClickBackgroundTelemetry() {
        if (this.id != null) {
            TelemetryWrapper.clickBannerBackground(this.id);
        }
    }
    
    protected final void sendClickItemTelemetry(final int n) {
        if (this.id != null) {
            TelemetryWrapper.clickBannerItem(this.id, n);
        }
    }
}
