package org.mozilla.rocket.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import org.json.JSONException;

class BasicViewHolder extends BannerViewHolder {
   private ImageView background;
   private OnClickListener onClickListener;

   BasicViewHolder(ViewGroup var1, OnClickListener var2) {
      super(LayoutInflater.from(var1.getContext()).inflate(2131492899, var1, false));
      this.onClickListener = var2;
      this.background = (ImageView)this.itemView.findViewById(2131296305);
   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$0(BasicViewHolder var0, BannerDAO var1, View var2) {
      var0.sendClickBackgroundTelemetry();

      try {
         var0.onClickListener.onClick(var1.values.getString(1));
      } catch (JSONException var3) {
         var3.printStackTrace();
      }

   }

   public void onBindViewHolder(Context var1, BannerDAO var2) {
      super.onBindViewHolder(var1, var2);

      try {
         Glide.with(var1).load(var2.values.getString(0)).into(this.background);
      } catch (JSONException var3) {
         var3.printStackTrace();
      }

      this.background.setOnClickListener(new _$$Lambda$BasicViewHolder$9mY_ye4sx7Y5ekdaoixKpobbXSE(this, var2));
   }
}
