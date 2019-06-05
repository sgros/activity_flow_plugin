package org.mozilla.rocket.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import org.json.JSONException;

class SingleButtonViewHolder extends BannerViewHolder {
   private ViewGroup background;
   private TextView button;
   private OnClickListener onClickListener;

   SingleButtonViewHolder(ViewGroup var1, OnClickListener var2) {
      super(LayoutInflater.from(var1.getContext()).inflate(2131492900, var1, false));
      this.onClickListener = var2;
      this.background = (ViewGroup)this.itemView.findViewById(2131296305);
      this.button = (TextView)this.itemView.findViewById(2131296358);
   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$0(SingleButtonViewHolder var0, BannerDAO var1, View var2) {
      try {
         var0.sendClickItemTelemetry(0);
         var0.onClickListener.onClick(var1.values.getString(1));
      } catch (JSONException var3) {
         var3.printStackTrace();
      }

   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$1(SingleButtonViewHolder var0, View var1) {
      var0.sendClickBackgroundTelemetry();
   }

   public void onBindViewHolder(Context var1, BannerDAO var2) {
      super.onBindViewHolder(var1, var2);

      try {
         RequestBuilder var5 = Glide.with(var1).load(var2.values.getString(0));
         SimpleTarget var3 = new SimpleTarget() {
            public void onResourceReady(Drawable var1, Transition var2) {
               SingleButtonViewHolder.this.background.setBackground(var1);
            }
         };
         var5.into((Target)var3);
         this.button.setText(var2.values.getString(2));
      } catch (JSONException var4) {
         var4.printStackTrace();
      }

      this.button.setOnClickListener(new _$$Lambda$SingleButtonViewHolder$AVBmSa5cyayG5TiNQYmdowIvtqM(this, var2));
      this.background.setOnClickListener(new _$$Lambda$SingleButtonViewHolder$C8bUwyLlkpWPMulUxUfmsIyZyD4(this));
   }
}
