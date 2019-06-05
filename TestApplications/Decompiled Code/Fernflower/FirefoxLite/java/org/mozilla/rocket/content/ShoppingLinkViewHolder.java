package org.mozilla.rocket.content;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.DrawableUtils;
import org.mozilla.rocket.content.data.ShoppingLink;

public final class ShoppingLinkViewHolder extends RecyclerView.ViewHolder {
   private ImageView image;
   private TextView name;
   private View view;

   public ShoppingLinkViewHolder(View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "itemView");
      super(var1);
      this.view = var1.findViewById(2131296643);
      this.image = (ImageView)var1.findViewById(2131296641);
      this.name = (TextView)var1.findViewById(2131296642);
   }

   public final void bind(ShoppingLink var1, OnClickListener var2) {
      Intrinsics.checkParameterIsNotNull(var1, "item");
      Intrinsics.checkParameterIsNotNull(var2, "listener");
      View var3 = this.view;
      if (var3 != null) {
         var3.setOnClickListener(var2);
      }

      TextView var5 = this.name;
      if (var5 != null) {
         var5.setText((CharSequence)var1.getName());
      }

      View var6 = this.itemView;
      Intrinsics.checkExpressionValueIsNotNull(var6, "itemView");
      Drawable var4 = DrawableUtils.getAndroidDrawable(var6.getContext(), var1.getImage());
      if (var4 != null) {
         ImageView var7 = this.image;
         if (var7 != null) {
            var7.setImageDrawable(var4);
         }
      }

   }
}
