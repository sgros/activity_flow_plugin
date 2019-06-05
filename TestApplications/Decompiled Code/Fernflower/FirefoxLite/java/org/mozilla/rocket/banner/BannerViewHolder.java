package org.mozilla.rocket.banner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.mozilla.focus.telemetry.TelemetryWrapper;

public abstract class BannerViewHolder extends RecyclerView.ViewHolder {
   protected String id;

   BannerViewHolder(View var1) {
      super(var1);
   }

   public final String getId() {
      return this.id;
   }

   public void onBindViewHolder(Context var1, BannerDAO var2) {
      this.id = var2.id;
   }

   protected final void sendClickBackgroundTelemetry() {
      if (this.id != null) {
         TelemetryWrapper.clickBannerBackground(this.id);
      }

   }

   protected final void sendClickItemTelemetry(int var1) {
      if (this.id != null) {
         TelemetryWrapper.clickBannerItem(this.id, var1);
      }

   }
}
