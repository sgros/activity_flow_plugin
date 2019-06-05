package org.mozilla.rocket.content;

import android.graphics.drawable.Drawable;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.utils.DrawableUtils;
import org.mozilla.rocket.content.data.ShoppingLink;

/* compiled from: ShoppingLinkAdapter.kt */
public final class ShoppingLinkViewHolder extends ViewHolder {
    private ImageView image;
    private TextView name;
    private View view;

    public ShoppingLinkViewHolder(View view) {
        Intrinsics.checkParameterIsNotNull(view, "itemView");
        super(view);
        this.view = view.findViewById(C0427R.C0426id.shoppinglink_item);
        this.image = (ImageView) view.findViewById(C0427R.C0426id.shoppinglink_category_image);
        this.name = (TextView) view.findViewById(C0427R.C0426id.shoppinglink_category_text);
    }

    public final void bind(ShoppingLink shoppingLink, OnClickListener onClickListener) {
        Intrinsics.checkParameterIsNotNull(shoppingLink, "item");
        Intrinsics.checkParameterIsNotNull(onClickListener, "listener");
        View view = this.view;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        TextView textView = this.name;
        if (textView != null) {
            textView.setText(shoppingLink.getName());
        }
        View view2 = this.itemView;
        Intrinsics.checkExpressionValueIsNotNull(view2, "itemView");
        Drawable androidDrawable = DrawableUtils.getAndroidDrawable(view2.getContext(), shoppingLink.getImage());
        if (androidDrawable != null) {
            ImageView imageView = this.image;
            if (imageView != null) {
                imageView.setImageDrawable(androidDrawable);
            }
        }
    }
}
