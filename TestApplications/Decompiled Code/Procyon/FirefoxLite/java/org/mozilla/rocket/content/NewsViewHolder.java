// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import com.bumptech.glide.Glide;
import android.text.format.DateUtils;
import android.view.View$OnClickListener;
import kotlin.jvm.internal.Intrinsics;
import android.graphics.Bitmap;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.Transformation;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import android.support.v7.widget.RecyclerView;
import org.mozilla.lite.partner.NewsItem;

public final class NewsViewHolder<T extends NewsItem> extends ViewHolder
{
    public static final Companion Companion;
    private static RequestOptions requestOptions;
    private TextView headline;
    private ImageView image;
    private TextView source;
    private TextView time;
    private View view;
    
    static {
        Companion = new Companion(null);
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        NewsViewHolder.requestOptions = requestOptions;
    }
    
    public NewsViewHolder(final View view) {
        Intrinsics.checkParameterIsNotNull(view, "itemView");
        super(view);
        this.view = view.findViewById(2131296538);
        this.headline = (TextView)view.findViewById(2131296539);
        this.source = (TextView)view.findViewById(2131296541);
        this.time = (TextView)view.findViewById(2131296542);
        this.image = (ImageView)view.findViewById(2131296540);
    }
    
    public final void bind(final NewsItem newsItem, final View$OnClickListener onClickListener) {
        Intrinsics.checkParameterIsNotNull(newsItem, "item");
        Intrinsics.checkParameterIsNotNull(onClickListener, "listener");
        final View view = this.view;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        final TextView headline = this.headline;
        if (headline != null) {
            headline.setText((CharSequence)newsItem.getTitle());
        }
        final String partner = newsItem.getPartner();
        final TextView source = this.source;
        if (source != null) {
            source.setText((CharSequence)partner);
        }
        final TextView time = this.time;
        if (time != null) {
            time.setText(DateUtils.getRelativeTimeSpanString(newsItem.getTime(), System.currentTimeMillis(), 60000L));
        }
        final String imageUrl = newsItem.getImageUrl();
        if (imageUrl != null && this.image != null) {
            final View itemView = this.itemView;
            Intrinsics.checkExpressionValueIsNotNull(itemView, "itemView");
            Glide.with(itemView.getContext()).load(imageUrl).apply(NewsViewHolder.requestOptions).into(this.image);
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
