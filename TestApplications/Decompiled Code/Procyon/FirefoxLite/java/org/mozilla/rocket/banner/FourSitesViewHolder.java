// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.banner;

import org.json.JSONArray;
import com.bumptech.glide.RequestManager;
import android.view.View$OnClickListener;
import android.graphics.Bitmap;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import org.mozilla.rocket.glide.transformation.PorterDuffTransformation;
import org.mozilla.rocket.glide.transformation.ShrinkSizeTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import com.bumptech.glide.request.transition.Transition;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.Glide;
import android.content.Context;
import org.json.JSONException;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;

class FourSitesViewHolder extends BannerViewHolder
{
    private ViewGroup background;
    private ImageView[] icons;
    private OnClickListener onClickListener;
    private TextView[] textViews;
    
    FourSitesViewHolder(final ViewGroup viewGroup, final OnClickListener onClickListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(2131492901, viewGroup, false));
        this.onClickListener = onClickListener;
        this.background = (ViewGroup)this.itemView.findViewById(2131296305);
        this.icons = new ImageView[] { (ImageView)this.itemView.findViewById(2131296306), (ImageView)this.itemView.findViewById(2131296307), (ImageView)this.itemView.findViewById(2131296308), (ImageView)this.itemView.findViewById(2131296309) };
        this.textViews = new TextView[] { (TextView)this.itemView.findViewById(2131296310), (TextView)this.itemView.findViewById(2131296311), (TextView)this.itemView.findViewById(2131296312), (TextView)this.itemView.findViewById(2131296313) };
    }
    
    @Override
    public void onBindViewHolder(final Context context, final BannerDAO bannerDAO) {
        super.onBindViewHolder(context, bannerDAO);
        final int n = 0;
        int j;
        try {
            Glide.with(context).load(bannerDAO.values.getString(0)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(final Drawable background, final Transition<? super Drawable> transition) {
                    FourSitesViewHolder.this.background.setBackground(background);
                }
            });
            int n2;
            for (int i = 0; i < this.icons.length; i = n2) {
                final PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(context.getResources().getColor(2131099846), PorterDuff$Mode.DST_OVER);
                final RequestManager with = Glide.with(context);
                final JSONArray values = bannerDAO.values;
                n2 = i + 1;
                with.load(values.getString(n2)).apply(new RequestOptions().transforms(new ShrinkSizeTransformation(0.6200000047683716), new PorterDuffTransformation(porterDuffColorFilter), new CircleCrop())).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(final Drawable imageDrawable, final Transition<? super Drawable> transition) {
                        FourSitesViewHolder.this.icons[i].setImageDrawable(imageDrawable);
                        FourSitesViewHolder.this.icons[i].setVisibility(0);
                        FourSitesViewHolder.this.textViews[i].setVisibility(0);
                    }
                });
            }
            int n3 = 0;
            while (true) {
                j = n;
                if (n3 >= this.icons.length) {
                    break;
                }
                this.textViews[n3].setText((CharSequence)bannerDAO.values.getString(n3 + 9));
                ++n3;
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            j = n;
        }
        while (j < this.icons.length) {
            this.icons[j].setOnClickListener((View$OnClickListener)new _$$Lambda$FourSitesViewHolder$wlekmEwFW8wMEFcaf4IJ4wCdwHg(this, j, bannerDAO, j + 5));
            ++j;
        }
        this.background.setOnClickListener((View$OnClickListener)new _$$Lambda$FourSitesViewHolder$ue0qc_Qan64E80hyBP1XFfiyLJQ(this));
    }
}
