package android.support.v7.widget.helper;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class ItemTouchUIUtilImpl {
   static class Api21Impl extends ItemTouchUIUtilImpl.BaseImpl {
      private float findMaxElevation(RecyclerView var1, View var2) {
         int var3 = var1.getChildCount();
         float var4 = 0.0F;

         float var7;
         for(int var5 = 0; var5 < var3; var4 = var7) {
            View var6 = var1.getChildAt(var5);
            if (var6 == var2) {
               var7 = var4;
            } else {
               float var8 = ViewCompat.getElevation(var6);
               var7 = var4;
               if (var8 > var4) {
                  var7 = var8;
               }
            }

            ++var5;
         }

         return var4;
      }

      public void clearView(View var1) {
         Object var2 = var1.getTag(R.id.item_touch_helper_previous_elevation);
         if (var2 != null && var2 instanceof Float) {
            ViewCompat.setElevation(var1, (Float)var2);
         }

         var1.setTag(R.id.item_touch_helper_previous_elevation, (Object)null);
         super.clearView(var1);
      }

      public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
         if (var7 && var3.getTag(R.id.item_touch_helper_previous_elevation) == null) {
            float var8 = ViewCompat.getElevation(var3);
            ViewCompat.setElevation(var3, 1.0F + this.findMaxElevation(var2, var3));
            var3.setTag(R.id.item_touch_helper_previous_elevation, var8);
         }

         super.onDraw(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   static class BaseImpl implements ItemTouchUIUtil {
      public void clearView(View var1) {
         var1.setTranslationX(0.0F);
         var1.setTranslationY(0.0F);
      }

      public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
         var3.setTranslationX(var4);
         var3.setTranslationY(var5);
      }

      public void onDrawOver(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
      }

      public void onSelected(View var1) {
      }
   }
}
