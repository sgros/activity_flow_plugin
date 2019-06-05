package org.mozilla.rocket.urlinput;

import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.p004v7.recyclerview.extensions.ListAdapter;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.C0769R;

/* compiled from: QuickSearchAdapter.kt */
public final class QuickSearchAdapter extends ListAdapter<QuickSearch, EngineViewHolder> {
    private final Function1<QuickSearch, Unit> clickListener;

    /* compiled from: QuickSearchAdapter.kt */
    public static final class EngineViewHolder extends ViewHolder {
        private ImageView icon;

        public EngineViewHolder(View view) {
            Intrinsics.checkParameterIsNotNull(view, "itemView");
            super(view);
            ImageView imageView = (ImageView) view.findViewById(C0427R.C0426id.quick_search_img);
            Intrinsics.checkExpressionValueIsNotNull(imageView, "itemView.quick_search_img");
            this.icon = imageView;
        }

        public final void bind(QuickSearch quickSearch, Function1<? super QuickSearch, Unit> function1) {
            Intrinsics.checkParameterIsNotNull(quickSearch, "item");
            Intrinsics.checkParameterIsNotNull(function1, "clickListener");
            ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            View view = this.itemView;
            Intrinsics.checkExpressionValueIsNotNull(view, "itemView");
            this.icon.setImageBitmap(FavIconUtils.getBitmapFromUri(view.getContext(), quickSearch.getIcon()));
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
            this.itemView.setOnClickListener(new QuickSearchAdapter$EngineViewHolder$bind$1(function1, quickSearch));
        }
    }

    public QuickSearchAdapter(Function1<? super QuickSearch, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "clickListener");
        super(new QuickSearchDiffCallback());
        this.clickListener = function1;
    }

    public EngineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.quick_search_item, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "itemView");
        return new EngineViewHolder(inflate);
    }

    public void onBindViewHolder(EngineViewHolder engineViewHolder, int i) {
        Intrinsics.checkParameterIsNotNull(engineViewHolder, "viewHolder");
        Object item = getItem(i);
        Intrinsics.checkExpressionValueIsNotNull(item, "getItem(i)");
        engineViewHolder.bind((QuickSearch) item, this.clickListener);
    }
}
