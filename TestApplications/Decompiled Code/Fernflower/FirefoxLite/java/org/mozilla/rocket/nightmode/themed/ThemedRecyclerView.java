package org.mozilla.rocket.nightmode.themed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;

public class ThemedRecyclerView extends RecyclerView {
   private boolean isNight;

   public ThemedRecyclerView(Context var1, AttributeSet var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "attrs");
      super(var1, var2);
   }

   public ThemedRecyclerView(Context var1, AttributeSet var2, int var3) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "attrs");
      super(var1, var2, var3);
   }

   // $FF: synthetic method
   public static final int[] access$mergeDrawableStates$s2666181(int[] var0, int[] var1) {
      return View.mergeDrawableStates(var0, var1);
   }

   public final boolean isNightMode() {
      return this.isNight;
   }

   public int[] onCreateDrawableState(int var1) {
      int[] var2;
      if (this.isNight) {
         var2 = super.onCreateDrawableState(var1 + ThemedWidgetUtils.INSTANCE.getSTATE_NIGHT_MODE().length);
         access$mergeDrawableStates$s2666181(var2, ThemedWidgetUtils.INSTANCE.getSTATE_NIGHT_MODE());
         Intrinsics.checkExpressionValueIsNotNull(var2, "drawableState");
      } else {
         var2 = super.onCreateDrawableState(var1);
         Intrinsics.checkExpressionValueIsNotNull(var2, "super.onCreateDrawableState(extraSpace)");
      }

      return var2;
   }

   public void setNightMode(boolean var1) {
      if (this.isNight != var1) {
         this.isNight = var1;
         this.refreshDrawableState();
         this.invalidate();
      }

   }
}
