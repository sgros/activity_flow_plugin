package org.mozilla.focus.home;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mozilla.rocket.home.pinsite.PinViewWrapper;

class SiteViewHolder extends RecyclerView.ViewHolder {
   AppCompatImageView img;
   PinViewWrapper pinView;
   TextView text;

   public SiteViewHolder(View var1) {
      super(var1);
      this.img = (AppCompatImageView)var1.findViewById(2131296377);
      this.text = (TextView)var1.findViewById(2131296685);
      this.pinView = new PinViewWrapper((ViewGroup)var1.findViewById(2131296567));
   }
}
