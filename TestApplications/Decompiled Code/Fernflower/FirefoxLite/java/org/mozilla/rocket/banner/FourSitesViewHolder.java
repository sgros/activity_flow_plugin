package org.mozilla.rocket.banner;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import org.json.JSONArray;
import org.json.JSONException;
import org.mozilla.rocket.glide.transformation.PorterDuffTransformation;
import org.mozilla.rocket.glide.transformation.ShrinkSizeTransformation;

class FourSitesViewHolder extends BannerViewHolder {
   private ViewGroup background;
   private ImageView[] icons;
   private OnClickListener onClickListener;
   private TextView[] textViews;

   FourSitesViewHolder(ViewGroup var1, OnClickListener var2) {
      super(LayoutInflater.from(var1.getContext()).inflate(2131492901, var1, false));
      this.onClickListener = var2;
      this.background = (ViewGroup)this.itemView.findViewById(2131296305);
      this.icons = new ImageView[]{(ImageView)this.itemView.findViewById(2131296306), (ImageView)this.itemView.findViewById(2131296307), (ImageView)this.itemView.findViewById(2131296308), (ImageView)this.itemView.findViewById(2131296309)};
      this.textViews = new TextView[]{(TextView)this.itemView.findViewById(2131296310), (TextView)this.itemView.findViewById(2131296311), (TextView)this.itemView.findViewById(2131296312), (TextView)this.itemView.findViewById(2131296313)};
   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$0(FourSitesViewHolder var0, int var1, BannerDAO var2, int var3, View var4) {
      try {
         var0.sendClickItemTelemetry(var1);
         var0.onClickListener.onClick(var2.values.getString(var3));
      } catch (JSONException var5) {
         var5.printStackTrace();
      }

   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$1(FourSitesViewHolder var0, View var1) {
      var0.sendClickBackgroundTelemetry();
   }

   public void onBindViewHolder(Context var1, BannerDAO var2) {
      super.onBindViewHolder(var1, var2);
      byte var3 = 0;

      final int var6;
      label59: {
         JSONException var10000;
         label63: {
            boolean var10001;
            try {
               RequestBuilder var4 = Glide.with(var1).load(var2.values.getString(0));
               SimpleTarget var5 = new SimpleTarget() {
                  public void onResourceReady(Drawable var1, Transition var2) {
                     FourSitesViewHolder.this.background.setBackground(var1);
                  }
               };
               var4.into((Target)var5);
            } catch (JSONException var14) {
               var10000 = var14;
               var10001 = false;
               break label63;
            }

            var6 = 0;

            int var8;
            while(true) {
               RequestManager var7;
               PorterDuffColorFilter var16;
               JSONArray var19;
               try {
                  if (var6 >= this.icons.length) {
                     break;
                  }

                  var16 = new PorterDuffColorFilter(var1.getResources().getColor(2131099846), Mode.DST_OVER);
                  var7 = Glide.with(var1);
                  var19 = var2.values;
               } catch (JSONException var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label63;
               }

               var8 = var6 + 1;

               try {
                  RequestBuilder var9 = var7.load(var19.getString(var8));
                  RequestOptions var20 = new RequestOptions();
                  ShrinkSizeTransformation var10 = new ShrinkSizeTransformation(0.6200000047683716D);
                  PorterDuffTransformation var22 = new PorterDuffTransformation(var16);
                  CircleCrop var17 = new CircleCrop();
                  RequestBuilder var21 = var9.apply(var20.transforms(var10, var22, var17));
                  SimpleTarget var18 = new SimpleTarget() {
                     public void onResourceReady(Drawable var1, Transition var2) {
                        FourSitesViewHolder.this.icons[var6].setImageDrawable(var1);
                        FourSitesViewHolder.this.icons[var6].setVisibility(0);
                        FourSitesViewHolder.this.textViews[var6].setVisibility(0);
                     }
                  };
                  var21.into((Target)var18);
               } catch (JSONException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label63;
               }

               var6 = var8;
            }

            var8 = 0;

            while(true) {
               var6 = var3;

               try {
                  if (var8 >= this.icons.length) {
                     break label59;
                  }

                  this.textViews[var8].setText(var2.values.getString(var8 + 9));
               } catch (JSONException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               ++var8;
            }
         }

         JSONException var15 = var10000;
         var15.printStackTrace();
      }

      for(var6 = var3; var6 < this.icons.length; ++var6) {
         this.icons[var6].setOnClickListener(new _$$Lambda$FourSitesViewHolder$wlekmEwFW8wMEFcaf4IJ4wCdwHg(this, var6, var2, var6 + 5));
      }

      this.background.setOnClickListener(new _$$Lambda$FourSitesViewHolder$ue0qc_Qan64E80hyBP1XFfiyLJQ(this));
   }
}
