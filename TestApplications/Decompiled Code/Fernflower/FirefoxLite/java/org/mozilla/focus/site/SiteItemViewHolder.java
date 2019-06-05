package org.mozilla.focus.site;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteItemViewHolder extends RecyclerView.ViewHolder {
   public FrameLayout btnMore;
   public ImageView imgFav;
   public ViewGroup rootView;
   public TextView textMain;
   public TextView textSecondary;

   public SiteItemViewHolder(View var1) {
      super(var1);
      this.rootView = (ViewGroup)var1.findViewById(2131296460);
      this.imgFav = (ImageView)var1.findViewById(2131296459);
      this.textMain = (TextView)var1.findViewById(2131296461);
      this.textSecondary = (TextView)var1.findViewById(2131296462);
      this.btnMore = (FrameLayout)var1.findViewById(2131296457);
   }
}
