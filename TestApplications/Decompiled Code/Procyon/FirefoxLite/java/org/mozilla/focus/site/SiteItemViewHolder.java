// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.site;

import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.support.v7.widget.RecyclerView;

public class SiteItemViewHolder extends ViewHolder
{
    public FrameLayout btnMore;
    public ImageView imgFav;
    public ViewGroup rootView;
    public TextView textMain;
    public TextView textSecondary;
    
    public SiteItemViewHolder(final View view) {
        super(view);
        this.rootView = (ViewGroup)view.findViewById(2131296460);
        this.imgFav = (ImageView)view.findViewById(2131296459);
        this.textMain = (TextView)view.findViewById(2131296461);
        this.textSecondary = (TextView)view.findViewById(2131296462);
        this.btnMore = (FrameLayout)view.findViewById(2131296457);
    }
}
