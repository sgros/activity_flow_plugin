// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.banner;

import android.view.View$OnClickListener;
import com.bumptech.glide.request.transition.Transition;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.Glide;
import android.content.Context;
import org.json.JSONException;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.ViewGroup;

class SingleButtonViewHolder extends BannerViewHolder
{
    private ViewGroup background;
    private TextView button;
    private OnClickListener onClickListener;
    
    SingleButtonViewHolder(final ViewGroup viewGroup, final OnClickListener onClickListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(2131492900, viewGroup, false));
        this.onClickListener = onClickListener;
        this.background = (ViewGroup)this.itemView.findViewById(2131296305);
        this.button = (TextView)this.itemView.findViewById(2131296358);
    }
    
    @Override
    public void onBindViewHolder(final Context context, final BannerDAO bannerDAO) {
        super.onBindViewHolder(context, bannerDAO);
        try {
            Glide.with(context).load(bannerDAO.values.getString(0)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(final Drawable background, final Transition<? super Drawable> transition) {
                    SingleButtonViewHolder.this.background.setBackground(background);
                }
            });
            this.button.setText((CharSequence)bannerDAO.values.getString(2));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        this.button.setOnClickListener((View$OnClickListener)new _$$Lambda$SingleButtonViewHolder$AVBmSa5cyayG5TiNQYmdowIvtqM(this, bannerDAO));
        this.background.setOnClickListener((View$OnClickListener)new _$$Lambda$SingleButtonViewHolder$C8bUwyLlkpWPMulUxUfmsIyZyD4(this));
    }
}
