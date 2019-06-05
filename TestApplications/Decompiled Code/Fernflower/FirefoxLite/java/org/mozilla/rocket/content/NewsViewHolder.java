package org.mozilla.rocket.content;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;

public final class NewsViewHolder extends RecyclerView.ViewHolder {
   public static final NewsViewHolder.Companion Companion = new NewsViewHolder.Companion((DefaultConstructorMarker)null);
   private static RequestOptions requestOptions;
   private TextView headline;
   private ImageView image;
   private TextView source;
   private TextView time;
   private View view;

   static {
      RequestOptions var0 = new RequestOptions();
      var0.transforms((Transformation)(new CenterCrop()), (Transformation)(new RoundedCorners(16)));
      requestOptions = var0;
   }

   public NewsViewHolder(View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "itemView");
      super(var1);
      this.view = var1.findViewById(2131296538);
      this.headline = (TextView)var1.findViewById(2131296539);
      this.source = (TextView)var1.findViewById(2131296541);
      this.time = (TextView)var1.findViewById(2131296542);
      this.image = (ImageView)var1.findViewById(2131296540);
   }

   public final void bind(NewsItem var1, OnClickListener var2) {
      Intrinsics.checkParameterIsNotNull(var1, "item");
      Intrinsics.checkParameterIsNotNull(var2, "listener");
      View var3 = this.view;
      if (var3 != null) {
         var3.setOnClickListener(var2);
      }

      TextView var5 = this.headline;
      if (var5 != null) {
         var5.setText((CharSequence)var1.getTitle());
      }

      String var6 = var1.getPartner();
      var5 = this.source;
      if (var5 != null) {
         var5.setText((CharSequence)var6);
      }

      var5 = this.time;
      if (var5 != null) {
         var5.setText(DateUtils.getRelativeTimeSpanString(var1.getTime(), System.currentTimeMillis(), 60000L));
      }

      String var4 = var1.getImageUrl();
      if (var4 != null && this.image != null) {
         View var7 = this.itemView;
         Intrinsics.checkExpressionValueIsNotNull(var7, "itemView");
         Glide.with(var7.getContext()).load(var4).apply(requestOptions).into(this.image);
      }

   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
