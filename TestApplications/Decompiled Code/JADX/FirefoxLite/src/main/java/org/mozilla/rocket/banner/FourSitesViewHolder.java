package org.mozilla.rocket.banner;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import org.json.JSONException;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.glide.transformation.PorterDuffTransformation;
import org.mozilla.rocket.glide.transformation.ShrinkSizeTransformation;

class FourSitesViewHolder extends BannerViewHolder {
    private ViewGroup background = ((ViewGroup) this.itemView.findViewById(C0427R.C0426id.banner_background));
    private ImageView[] icons = new ImageView[]{(ImageView) this.itemView.findViewById(C0427R.C0426id.banner_icon_1), (ImageView) this.itemView.findViewById(C0427R.C0426id.banner_icon_2), (ImageView) this.itemView.findViewById(C0427R.C0426id.banner_icon_3), (ImageView) this.itemView.findViewById(C0427R.C0426id.banner_icon_4)};
    private OnClickListener onClickListener;
    private TextView[] textViews = new TextView[]{(TextView) this.itemView.findViewById(C0427R.C0426id.banner_label_1), (TextView) this.itemView.findViewById(C0427R.C0426id.banner_label_2), (TextView) this.itemView.findViewById(C0427R.C0426id.banner_label_3), (TextView) this.itemView.findViewById(C0427R.C0426id.banner_label_4)};

    /* renamed from: org.mozilla.rocket.banner.FourSitesViewHolder$1 */
    class C07631 extends SimpleTarget<Drawable> {
        C07631() {
        }

        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
            FourSitesViewHolder.this.background.setBackground(drawable);
        }
    }

    FourSitesViewHolder(ViewGroup viewGroup, OnClickListener onClickListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.banner2, viewGroup, false));
        this.onClickListener = onClickListener;
    }

    public void onBindViewHolder(Context context, BannerDAO bannerDAO) {
        super.onBindViewHolder(context, bannerDAO);
        int i = 0;
        try {
            Glide.with(context).load(bannerDAO.values.getString(0)).into(new C07631());
            int i2 = 0;
            while (i2 < this.icons.length) {
                PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(context.getResources().getColor(C0769R.color.paletteWhite100), Mode.DST_OVER);
                int i3 = i2 + 1;
                Glide.with(context).load(bannerDAO.values.getString(i3)).apply(new RequestOptions().transforms(new ShrinkSizeTransformation(0.6200000047683716d), new PorterDuffTransformation(porterDuffColorFilter), new CircleCrop())).into(new SimpleTarget<Drawable>() {
                    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                        FourSitesViewHolder.this.icons[i2].setImageDrawable(drawable);
                        FourSitesViewHolder.this.icons[i2].setVisibility(0);
                        FourSitesViewHolder.this.textViews[i2].setVisibility(0);
                    }
                });
                i2 = i3;
            }
            for (int i4 = 0; i4 < this.icons.length; i4++) {
                this.textViews[i4].setText(bannerDAO.values.getString(i4 + 9));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        while (i < this.icons.length) {
            this.icons[i].setOnClickListener(new C0588-$$Lambda$FourSitesViewHolder$wlekmEwFW8wMEFcaf4IJ4wCdwHg(this, i, bannerDAO, i + 5));
            i++;
        }
        this.background.setOnClickListener(new C0587-$$Lambda$FourSitesViewHolder$ue0qc_Qan64E80hyBP1XFfiyLJQ(this));
    }

    public static /* synthetic */ void lambda$onBindViewHolder$0(FourSitesViewHolder fourSitesViewHolder, int i, BannerDAO bannerDAO, int i2, View view) {
        try {
            fourSitesViewHolder.sendClickItemTelemetry(i);
            fourSitesViewHolder.onClickListener.onClick(bannerDAO.values.getString(i2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
