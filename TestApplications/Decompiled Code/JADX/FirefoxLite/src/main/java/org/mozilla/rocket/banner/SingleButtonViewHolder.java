package org.mozilla.rocket.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import org.json.JSONException;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;

class SingleButtonViewHolder extends BannerViewHolder {
    private ViewGroup background = ((ViewGroup) this.itemView.findViewById(C0427R.C0426id.banner_background));
    private TextView button = ((TextView) this.itemView.findViewById(C0427R.C0426id.button));
    private OnClickListener onClickListener;

    /* renamed from: org.mozilla.rocket.banner.SingleButtonViewHolder$1 */
    class C07651 extends SimpleTarget<Drawable> {
        C07651() {
        }

        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
            SingleButtonViewHolder.this.background.setBackground(drawable);
        }
    }

    SingleButtonViewHolder(ViewGroup viewGroup, OnClickListener onClickListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.banner1, viewGroup, false));
        this.onClickListener = onClickListener;
    }

    public void onBindViewHolder(Context context, BannerDAO bannerDAO) {
        super.onBindViewHolder(context, bannerDAO);
        try {
            Glide.with(context).load(bannerDAO.values.getString(0)).into(new C07651());
            this.button.setText(bannerDAO.values.getString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.button.setOnClickListener(new C0589-$$Lambda$SingleButtonViewHolder$AVBmSa5cyayG5TiNQYmdowIvtqM(this, bannerDAO));
        this.background.setOnClickListener(new C0590-$$Lambda$SingleButtonViewHolder$C8bUwyLlkpWPMulUxUfmsIyZyD4(this));
    }

    public static /* synthetic */ void lambda$onBindViewHolder$0(SingleButtonViewHolder singleButtonViewHolder, BannerDAO bannerDAO, View view) {
        try {
            singleButtonViewHolder.sendClickItemTelemetry(0);
            singleButtonViewHolder.onClickListener.onClick(bannerDAO.values.getString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
