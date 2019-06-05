// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.util.DiffUtil;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.content.data.ShoppingLink;
import android.support.v7.recyclerview.extensions.ListAdapter;

public final class ShoppingLinkAdapter extends ListAdapter<ShoppingLink, ShoppingLinkViewHolder>
{
    public static final Companion Companion;
    private final ContentPortalListener listener;
    
    static {
        Companion = new Companion(null);
    }
    
    public ShoppingLinkAdapter(final ContentPortalListener listener) {
        Intrinsics.checkParameterIsNotNull(listener, "listener");
        super(COMPARATOR.INSTANCE);
        this.listener = listener;
    }
    
    @Override
    public int getItemViewType(int n) {
        final int itemCount = this.getItemCount();
        final int n2 = 1;
        if (n == itemCount - 1) {
            n = n2;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        Intrinsics.checkParameterIsNotNull(recyclerView, "recyclerView");
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            gridLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new ShoppingLinkAdapter$onAttachedToRecyclerView.ShoppingLinkAdapter$onAttachedToRecyclerView$1(this, gridLayoutManager.getSpanCount()));
        }
    }
    
    public void onBindViewHolder(final ShoppingLinkViewHolder shoppingLinkViewHolder, final int n) {
        Intrinsics.checkParameterIsNotNull(shoppingLinkViewHolder, "holder");
        final ShoppingLink shoppingLink = ((ListAdapter<ShoppingLink, VH>)this).getItem(n);
        if (shoppingLink != null) {
            shoppingLinkViewHolder.bind(shoppingLink, (View$OnClickListener)new ShoppingLinkAdapter$onBindViewHolder.ShoppingLinkAdapter$onBindViewHolder$1(this, n, shoppingLink));
        }
    }
    
    public ShoppingLinkViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        final LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        ShoppingLinkViewHolder shoppingLinkViewHolder;
        if (n == 1) {
            final View inflate = from.inflate(2131492985, viewGroup, false);
            Intrinsics.checkExpressionValueIsNotNull(inflate, "v");
            shoppingLinkViewHolder = new ShoppingLinkViewHolder(inflate);
        }
        else {
            final View inflate2 = from.inflate(2131492984, viewGroup, false);
            Intrinsics.checkExpressionValueIsNotNull(inflate2, "v");
            shoppingLinkViewHolder = new ShoppingLinkViewHolder(inflate2);
        }
        return shoppingLinkViewHolder;
    }
    
    public static final class COMPARATOR extends ItemCallback<ShoppingLink>
    {
        public static final COMPARATOR INSTANCE;
        
        static {
            INSTANCE = new COMPARATOR();
        }
        
        private COMPARATOR() {
        }
        
        public boolean areContentsTheSame(final ShoppingLink shoppingLink, final ShoppingLink shoppingLink2) {
            Intrinsics.checkParameterIsNotNull(shoppingLink, "oldItem");
            Intrinsics.checkParameterIsNotNull(shoppingLink2, "newItem");
            return Intrinsics.areEqual(shoppingLink, shoppingLink2);
        }
        
        public boolean areItemsTheSame(final ShoppingLink shoppingLink, final ShoppingLink shoppingLink2) {
            Intrinsics.checkParameterIsNotNull(shoppingLink, "oldItem");
            Intrinsics.checkParameterIsNotNull(shoppingLink2, "newItem");
            return Intrinsics.areEqual(shoppingLink.getUrl(), shoppingLink2.getUrl());
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
