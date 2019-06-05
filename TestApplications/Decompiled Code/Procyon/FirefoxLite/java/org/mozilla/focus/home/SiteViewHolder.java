// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import org.mozilla.rocket.home.pinsite.PinViewWrapper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;

class SiteViewHolder extends ViewHolder
{
    AppCompatImageView img;
    PinViewWrapper pinView;
    TextView text;
    
    public SiteViewHolder(final View view) {
        super(view);
        this.img = (AppCompatImageView)view.findViewById(2131296377);
        this.text = (TextView)view.findViewById(2131296685);
        this.pinView = new PinViewWrapper((ViewGroup)view.findViewById(2131296567));
    }
}
