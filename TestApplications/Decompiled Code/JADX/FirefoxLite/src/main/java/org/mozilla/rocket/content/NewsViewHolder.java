package org.mozilla.rocket.content;

import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.lite.partner.NewsItem;

/* compiled from: NewsAdapter.kt */
public final class NewsViewHolder<T extends NewsItem> extends ViewHolder {
    public static final Companion Companion = new Companion();
    private static RequestOptions requestOptions;
    private TextView headline;
    private ImageView image;
    private TextView source;
    private TextView time;
    private View view;

    /* compiled from: NewsAdapter.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public NewsViewHolder(View view) {
        Intrinsics.checkParameterIsNotNull(view, "itemView");
        super(view);
        this.view = view.findViewById(C0427R.C0426id.news_item);
        this.headline = (TextView) view.findViewById(C0427R.C0426id.news_item_headline);
        this.source = (TextView) view.findViewById(C0427R.C0426id.news_item_source);
        this.time = (TextView) view.findViewById(C0427R.C0426id.news_item_time);
        this.image = (ImageView) view.findViewById(C0427R.C0426id.news_item_image);
    }

    static {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        requestOptions = requestOptions;
    }

    public final void bind(NewsItem newsItem, OnClickListener onClickListener) {
        Intrinsics.checkParameterIsNotNull(newsItem, "item");
        Intrinsics.checkParameterIsNotNull(onClickListener, "listener");
        View view = this.view;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        TextView textView = this.headline;
        if (textView != null) {
            textView.setText(newsItem.getTitle());
        }
        String partner = newsItem.getPartner();
        TextView textView2 = this.source;
        if (textView2 != null) {
            textView2.setText(partner);
        }
        textView = this.time;
        if (textView != null) {
            textView.setText(DateUtils.getRelativeTimeSpanString(newsItem.getTime(), System.currentTimeMillis(), 60000));
        }
        String imageUrl = newsItem.getImageUrl();
        if (imageUrl != null && this.image != null) {
            View view2 = this.itemView;
            Intrinsics.checkExpressionValueIsNotNull(view2, "itemView");
            Glide.with(view2.getContext()).load(imageUrl).apply(requestOptions).into(this.image);
        }
    }
}
