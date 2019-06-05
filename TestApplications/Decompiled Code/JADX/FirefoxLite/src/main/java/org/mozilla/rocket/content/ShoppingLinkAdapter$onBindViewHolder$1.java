package org.mozilla.rocket.content;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.content.data.ShoppingLink;

/* compiled from: ShoppingLinkAdapter.kt */
final class ShoppingLinkAdapter$onBindViewHolder$1 implements OnClickListener {
    final /* synthetic */ ShoppingLink $item;
    final /* synthetic */ int $position;
    final /* synthetic */ ShoppingLinkAdapter this$0;

    ShoppingLinkAdapter$onBindViewHolder$1(ShoppingLinkAdapter shoppingLinkAdapter, int i, ShoppingLink shoppingLink) {
        this.this$0 = shoppingLinkAdapter;
        this.$position = i;
        this.$item = shoppingLink;
    }

    public final void onClick(View view) {
        TelemetryWrapper.INSTANCE.clickOnEcItem(String.valueOf(this.$position), this.$item.getSource(), this.$item.getName());
        this.this$0.listener.onItemClicked(this.$item.getUrl());
    }
}
