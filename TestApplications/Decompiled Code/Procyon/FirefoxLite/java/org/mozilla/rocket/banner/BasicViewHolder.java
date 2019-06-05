// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.banner;

import android.view.View$OnClickListener;
import com.bumptech.glide.Glide;
import android.content.Context;
import org.json.JSONException;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

class BasicViewHolder extends BannerViewHolder
{
    private ImageView background;
    private OnClickListener onClickListener;
    
    BasicViewHolder(final ViewGroup viewGroup, final OnClickListener onClickListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(2131492899, viewGroup, false));
        this.onClickListener = onClickListener;
        this.background = (ImageView)this.itemView.findViewById(2131296305);
    }
    
    @Override
    public void onBindViewHolder(final Context context, final BannerDAO bannerDAO) {
        super.onBindViewHolder(context, bannerDAO);
        try {
            Glide.with(context).load(bannerDAO.values.getString(0)).into(this.background);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        this.background.setOnClickListener((View$OnClickListener)new _$$Lambda$BasicViewHolder$9mY_ye4sx7Y5ekdaoixKpobbXSE(this, bannerDAO));
    }
}
