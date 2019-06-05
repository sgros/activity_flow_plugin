// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import android.graphics.drawable.Drawable;
import org.mozilla.focus.utils.DrawableUtils;
import android.view.View$OnClickListener;
import org.mozilla.rocket.content.data.ShoppingLink;
import kotlin.jvm.internal.Intrinsics;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;

public final class ShoppingLinkViewHolder extends ViewHolder
{
    private ImageView image;
    private TextView name;
    private View view;
    
    public ShoppingLinkViewHolder(final View view) {
        Intrinsics.checkParameterIsNotNull(view, "itemView");
        super(view);
        this.view = view.findViewById(2131296643);
        this.image = (ImageView)view.findViewById(2131296641);
        this.name = (TextView)view.findViewById(2131296642);
    }
    
    public final void bind(final ShoppingLink shoppingLink, final View$OnClickListener onClickListener) {
        Intrinsics.checkParameterIsNotNull(shoppingLink, "item");
        Intrinsics.checkParameterIsNotNull(onClickListener, "listener");
        final View view = this.view;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        final TextView name = this.name;
        if (name != null) {
            name.setText((CharSequence)shoppingLink.getName());
        }
        final View itemView = this.itemView;
        Intrinsics.checkExpressionValueIsNotNull(itemView, "itemView");
        final Drawable androidDrawable = DrawableUtils.getAndroidDrawable(itemView.getContext(), shoppingLink.getImage());
        if (androidDrawable != null) {
            final ImageView image = this.image;
            if (image != null) {
                image.setImageDrawable(androidDrawable);
            }
        }
    }
}
