// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.util.DiffUtil;
import kotlin.jvm.internal.Intrinsics;
import android.support.v7.recyclerview.extensions.ListAdapter;
import org.mozilla.lite.partner.NewsItem;

public final class NewsAdapter<T extends NewsItem> extends ListAdapter<NewsItem, NewsViewHolder<T>>
{
    private final ContentPortalListener listener;
    
    public NewsAdapter(final ContentPortalListener listener) {
        Intrinsics.checkParameterIsNotNull(listener, "listener");
        super(COMPARATOR.INSTANCE);
        this.listener = listener;
    }
    
    public void onBindViewHolder(final NewsViewHolder<T> newsViewHolder, final int n) {
        Intrinsics.checkParameterIsNotNull(newsViewHolder, "holder");
        final NewsItem newsItem = this.getItem(n);
        if (newsItem != null) {
            newsViewHolder.bind(newsItem, (View$OnClickListener)new NewsAdapter$onBindViewHolder.NewsAdapter$onBindViewHolder$1(this, n, newsItem));
        }
    }
    
    public NewsViewHolder<T> onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(2131492979, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "v");
        return new NewsViewHolder<T>(inflate);
    }
    
    public static final class COMPARATOR extends ItemCallback<NewsItem>
    {
        public static final COMPARATOR INSTANCE;
        
        static {
            INSTANCE = new COMPARATOR();
        }
        
        private COMPARATOR() {
        }
        
        public boolean areContentsTheSame(final NewsItem newsItem, final NewsItem newsItem2) {
            Intrinsics.checkParameterIsNotNull(newsItem, "oldItem");
            Intrinsics.checkParameterIsNotNull(newsItem2, "newItem");
            return Intrinsics.areEqual(newsItem, newsItem2);
        }
        
        public boolean areItemsTheSame(final NewsItem newsItem, final NewsItem newsItem2) {
            Intrinsics.checkParameterIsNotNull(newsItem, "oldItem");
            Intrinsics.checkParameterIsNotNull(newsItem2, "newItem");
            return Intrinsics.areEqual(newsItem.getId(), newsItem2.getId());
        }
    }
}
