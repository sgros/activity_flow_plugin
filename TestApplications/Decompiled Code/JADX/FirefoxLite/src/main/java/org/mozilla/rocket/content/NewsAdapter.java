package org.mozilla.rocket.content;

import android.support.p004v7.recyclerview.extensions.ListAdapter;
import android.support.p004v7.util.DiffUtil.ItemCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.rocket.C0769R;

/* compiled from: NewsAdapter.kt */
public final class NewsAdapter<T extends NewsItem> extends ListAdapter<NewsItem, NewsViewHolder<T>> {
    private final ContentPortalListener listener;

    /* compiled from: NewsAdapter.kt */
    public static final class COMPARATOR extends ItemCallback<NewsItem> {
        public static final COMPARATOR INSTANCE = new COMPARATOR();

        private COMPARATOR() {
        }

        public boolean areItemsTheSame(NewsItem newsItem, NewsItem newsItem2) {
            Intrinsics.checkParameterIsNotNull(newsItem, "oldItem");
            Intrinsics.checkParameterIsNotNull(newsItem2, "newItem");
            return Intrinsics.areEqual(newsItem.getId(), newsItem2.getId());
        }

        public boolean areContentsTheSame(NewsItem newsItem, NewsItem newsItem2) {
            Intrinsics.checkParameterIsNotNull(newsItem, "oldItem");
            Intrinsics.checkParameterIsNotNull(newsItem2, "newItem");
            return Intrinsics.areEqual(newsItem, newsItem2);
        }
    }

    public NewsAdapter(ContentPortalListener contentPortalListener) {
        Intrinsics.checkParameterIsNotNull(contentPortalListener, "listener");
        super(COMPARATOR.INSTANCE);
        this.listener = contentPortalListener;
    }

    public NewsViewHolder<T> onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_news, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "v");
        return new NewsViewHolder(inflate);
    }

    public void onBindViewHolder(NewsViewHolder<T> newsViewHolder, int i) {
        Intrinsics.checkParameterIsNotNull(newsViewHolder, "holder");
        NewsItem newsItem = (NewsItem) getItem(i);
        if (newsItem != null) {
            newsViewHolder.bind(newsItem, new NewsAdapter$onBindViewHolder$1(this, i, newsItem));
        }
    }
}
