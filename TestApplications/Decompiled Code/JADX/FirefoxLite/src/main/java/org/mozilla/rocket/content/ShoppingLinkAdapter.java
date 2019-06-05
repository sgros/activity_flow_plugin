package org.mozilla.rocket.content;

import android.support.p004v7.recyclerview.extensions.ListAdapter;
import android.support.p004v7.util.DiffUtil.ItemCallback;
import android.support.p004v7.widget.GridLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.data.ShoppingLink;

/* compiled from: ShoppingLinkAdapter.kt */
public final class ShoppingLinkAdapter extends ListAdapter<ShoppingLink, ShoppingLinkViewHolder> {
    public static final Companion Companion = new Companion();
    private final ContentPortalListener listener;

    /* compiled from: ShoppingLinkAdapter.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: ShoppingLinkAdapter.kt */
    public static final class COMPARATOR extends ItemCallback<ShoppingLink> {
        public static final COMPARATOR INSTANCE = new COMPARATOR();

        private COMPARATOR() {
        }

        public boolean areItemsTheSame(ShoppingLink shoppingLink, ShoppingLink shoppingLink2) {
            Intrinsics.checkParameterIsNotNull(shoppingLink, "oldItem");
            Intrinsics.checkParameterIsNotNull(shoppingLink2, "newItem");
            return Intrinsics.areEqual(shoppingLink.getUrl(), shoppingLink2.getUrl());
        }

        public boolean areContentsTheSame(ShoppingLink shoppingLink, ShoppingLink shoppingLink2) {
            Intrinsics.checkParameterIsNotNull(shoppingLink, "oldItem");
            Intrinsics.checkParameterIsNotNull(shoppingLink2, "newItem");
            return Intrinsics.areEqual(shoppingLink, shoppingLink2);
        }
    }

    public ShoppingLinkAdapter(ContentPortalListener contentPortalListener) {
        Intrinsics.checkParameterIsNotNull(contentPortalListener, "listener");
        super(COMPARATOR.INSTANCE);
        this.listener = contentPortalListener;
    }

    public ShoppingLinkViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        View inflate;
        if (i == 1) {
            inflate = from.inflate(C0769R.layout.item_shoppinglink_footer, viewGroup, false);
            Intrinsics.checkExpressionValueIsNotNull(inflate, "v");
            return new ShoppingLinkViewHolder(inflate);
        }
        inflate = from.inflate(C0769R.layout.item_shoppinglink, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "v");
        return new ShoppingLinkViewHolder(inflate);
    }

    public void onBindViewHolder(ShoppingLinkViewHolder shoppingLinkViewHolder, int i) {
        Intrinsics.checkParameterIsNotNull(shoppingLinkViewHolder, "holder");
        ShoppingLink shoppingLink = (ShoppingLink) getItem(i);
        if (shoppingLink != null) {
            shoppingLinkViewHolder.bind(shoppingLink, new ShoppingLinkAdapter$onBindViewHolder$1(this, i, shoppingLink));
        }
    }

    public int getItemViewType(int i) {
        return i == getItemCount() - 1 ? 1 : 0;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Intrinsics.checkParameterIsNotNull(recyclerView, "recyclerView");
        super.onAttachedToRecyclerView(recyclerView);
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new ShoppingLinkAdapter$onAttachedToRecyclerView$1(this, gridLayoutManager.getSpanCount()));
        }
    }
}
