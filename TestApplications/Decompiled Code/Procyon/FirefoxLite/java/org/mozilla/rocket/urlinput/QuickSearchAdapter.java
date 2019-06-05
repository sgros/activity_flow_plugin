// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import android.os.StrictMode$ThreadPolicy;
import android.view.View$OnClickListener;
import org.mozilla.icon.FavIconUtils;
import android.os.StrictMode;
import org.mozilla.focus.R;
import android.widget.ImageView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.util.DiffUtil;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import android.support.v7.recyclerview.extensions.ListAdapter;

public final class QuickSearchAdapter extends ListAdapter<QuickSearch, EngineViewHolder>
{
    private final Function1<QuickSearch, Unit> clickListener;
    
    public QuickSearchAdapter(final Function1<? super QuickSearch, Unit> clickListener) {
        Intrinsics.checkParameterIsNotNull(clickListener, "clickListener");
        super(new QuickSearchDiffCallback());
        this.clickListener = (Function1<QuickSearch, Unit>)clickListener;
    }
    
    public void onBindViewHolder(final EngineViewHolder engineViewHolder, final int n) {
        Intrinsics.checkParameterIsNotNull(engineViewHolder, "viewHolder");
        final QuickSearch item = ((ListAdapter<QuickSearch, VH>)this).getItem(n);
        Intrinsics.checkExpressionValueIsNotNull(item, "getItem(i)");
        engineViewHolder.bind(item, this.clickListener);
    }
    
    public EngineViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(2131493014, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "itemView");
        return new EngineViewHolder(inflate);
    }
    
    public static final class EngineViewHolder extends ViewHolder
    {
        private ImageView icon;
        
        public EngineViewHolder(final View view) {
            Intrinsics.checkParameterIsNotNull(view, "itemView");
            super(view);
            final ImageView icon = (ImageView)view.findViewById(R.id.quick_search_img);
            Intrinsics.checkExpressionValueIsNotNull(icon, "itemView.quick_search_img");
            this.icon = icon;
        }
        
        public final void bind(final QuickSearch quickSearch, final Function1<? super QuickSearch, Unit> function1) {
            Intrinsics.checkParameterIsNotNull(quickSearch, "item");
            Intrinsics.checkParameterIsNotNull(function1, "clickListener");
            final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            final View itemView = this.itemView;
            Intrinsics.checkExpressionValueIsNotNull(itemView, "itemView");
            this.icon.setImageBitmap(FavIconUtils.getBitmapFromUri(itemView.getContext(), quickSearch.getIcon()));
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
            this.itemView.setOnClickListener((View$OnClickListener)new QuickSearchAdapter$EngineViewHolder$bind.QuickSearchAdapter$EngineViewHolder$bind$1((Function1)function1, quickSearch));
        }
    }
}
