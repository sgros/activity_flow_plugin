package org.mozilla.rocket.home.pinsite;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class PinViewWrapper {
   public static final PinViewWrapper.Companion Companion = new PinViewWrapper.Companion((DefaultConstructorMarker)null);
   private final ViewGroup view;

   public PinViewWrapper(ViewGroup var1) {
      Intrinsics.checkParameterIsNotNull(var1, "view");
      super();
      this.view = var1;
   }

   public final void setPinColor(int var1) {
      ViewGroup var2 = this.view;
      Drawable var3 = this.view.getBackground();
      Object var4 = null;
      if (var3 != null) {
         var3 = this.tint(var3, var1);
      } else {
         var3 = null;
      }

      var2.setBackground(var3);
      int var5 = -1;
      if (ColorUtils.calculateContrast(var1, -1) < 1.5D) {
         var5 = -16777216;
      }

      View var7 = this.view.findViewById(2131296566);
      Intrinsics.checkExpressionValueIsNotNull(var7, "iconView");
      Drawable var6 = var7.getBackground();
      var3 = (Drawable)var4;
      if (var6 != null) {
         var3 = this.tint(var6, var5);
      }

      var7.setBackground(var3);
   }

   public final void setVisibility(int var1) {
      this.view.setVisibility(var1);
   }

   public final Drawable tint(Drawable var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
      var1 = DrawableCompat.wrap(var1).mutate();
      DrawableCompat.setTint(var1, var2);
      Intrinsics.checkExpressionValueIsNotNull(var1, "DrawableCompat.wrap(this…nt(this, color)\n        }");
      return var1;
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
