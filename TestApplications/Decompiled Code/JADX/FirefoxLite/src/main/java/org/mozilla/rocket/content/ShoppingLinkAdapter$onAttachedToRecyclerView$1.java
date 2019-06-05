package org.mozilla.rocket.content;

import android.support.p004v7.widget.GridLayoutManager.SpanSizeLookup;

/* compiled from: ShoppingLinkAdapter.kt */
public final class ShoppingLinkAdapter$onAttachedToRecyclerView$1 extends SpanSizeLookup {
    final /* synthetic */ int $cacheSpanCount;
    final /* synthetic */ ShoppingLinkAdapter this$0;

    ShoppingLinkAdapter$onAttachedToRecyclerView$1(ShoppingLinkAdapter shoppingLinkAdapter, int i) {
        this.this$0 = shoppingLinkAdapter;
        this.$cacheSpanCount = i;
    }

    public int getSpanSize(int i) {
        if (this.this$0.getItemViewType(i) == 1) {
            return this.$cacheSpanCount;
        }
        return 1;
    }
}
