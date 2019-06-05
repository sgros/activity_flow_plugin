package org.mozilla.rocket.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;

class BasicViewHolder extends BannerViewHolder {
    private ImageView background = ((ImageView) this.itemView.findViewById(C0427R.C0426id.banner_background));
    private OnClickListener onClickListener;

    BasicViewHolder(ViewGroup viewGroup, OnClickListener onClickListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.banner0, viewGroup, false));
        this.onClickListener = onClickListener;
    }

    public void onBindViewHolder(Context context, BannerDAO bannerDAO) {
        super.onBindViewHolder(context, bannerDAO);
        try {
            Glide.with(context).load(bannerDAO.values.getString(0)).into(this.background);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.background.setOnClickListener(new C0586-$$Lambda$BasicViewHolder$9mY-ye4sx7Y5ekdaoixKpobbXSE(this, bannerDAO));
    }

    public static /* synthetic */ void lambda$onBindViewHolder$0(BasicViewHolder basicViewHolder, BannerDAO bannerDAO, View view) {
        basicViewHolder.sendClickBackgroundTelemetry();
        try {
            basicViewHolder.onClickListener.onClick(bannerDAO.values.getString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
