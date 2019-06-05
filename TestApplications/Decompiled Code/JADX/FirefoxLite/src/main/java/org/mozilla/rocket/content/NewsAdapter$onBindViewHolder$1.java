package org.mozilla.rocket.content;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.lite.partner.NewsItem;

/* compiled from: NewsAdapter.kt */
final class NewsAdapter$onBindViewHolder$1 implements OnClickListener {
    final /* synthetic */ NewsItem $item;
    final /* synthetic */ int $position;
    final /* synthetic */ NewsAdapter this$0;

    NewsAdapter$onBindViewHolder$1(NewsAdapter newsAdapter, int i, NewsItem newsItem) {
        this.this$0 = newsAdapter;
        this.$position = i;
        this.$item = newsItem;
    }

    public final void onClick(View view) {
        TelemetryWrapper.INSTANCE.clickOnNewsItem(String.valueOf(this.$position), this.$item.getSource(), this.$item.getPartner(), this.$item.getCategory(), this.$item.getSubcategory());
        this.this$0.listener.onItemClicked(this.$item.getNewsUrl());
    }
}
