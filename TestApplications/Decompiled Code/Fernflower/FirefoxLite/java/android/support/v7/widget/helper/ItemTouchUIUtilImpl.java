package android.support.v7.widget.helper;

import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class ItemTouchUIUtilImpl implements ItemTouchUIUtil {
   static final ItemTouchUIUtil INSTANCE = new ItemTouchUIUtilImpl();

   private static float findMaxElevation(RecyclerView var0, View var1) {
      int var2 = var0.getChildCount();
      float var3 = 0.0F;

      float var6;
      for(int var4 = 0; var4 < var2; var3 = var6) {
         View var5 = var0.getChildAt(var4);
         if (var5 == var1) {
            var6 = var3;
         } else {
            float var7 = ViewCompat.getElevation(var5);
            var6 = var3;
            if (var7 > var3) {
               var6 = var7;
            }
         }

         ++var4;
      }

      return var3;
   }

   public void clearView(View var1) {
      if (VERSION.SDK_INT >= 21) {
         Object var2 = var1.getTag(R.id.item_touch_helper_previous_elevation);
         if (var2 != null && var2 instanceof Float) {
            ViewCompat.setElevation(var1, (Float)var2);
         }

         var1.setTag(R.id.item_touch_helper_previous_elevation, (Object)null);
      }

      var1.setTranslationX(0.0F);
      var1.setTranslationY(0.0F);
   }

   public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
      if (VERSION.SDK_INT >= 21 && var7 && var3.getTag(R.id.item_touch_helper_previous_elevation) == null) {
         float var8 = ViewCompat.getElevation(var3);
         ViewCompat.setElevation(var3, findMaxElevation(var2, var3) + 1.0F);
         var3.setTag(R.id.item_touch_helper_previous_elevation, var8);
      }

      var3.setTranslationX(var4);
      var3.setTranslationY(var5);
   }

   public void onDrawOver(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
   }

   public void onSelected(View var1) {
   }
}
