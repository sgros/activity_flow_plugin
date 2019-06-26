package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
   private static final int[] ATTRS = new int[]{16843284};
   public static final int HORIZONTAL = 0;
   private static final String TAG = "DividerItem";
   public static final int VERTICAL = 1;
   private final Rect mBounds = new Rect();
   private Drawable mDivider;
   private int mOrientation;

   public DividerItemDecoration(Context var1, int var2) {
      TypedArray var3 = var1.obtainStyledAttributes(ATTRS);
      this.mDivider = var3.getDrawable(0);
      if (this.mDivider == null) {
         Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
      }

      var3.recycle();
      this.setOrientation(var2);
   }

   private void drawHorizontal(Canvas var1, RecyclerView var2) {
      var1.save();
      boolean var3 = var2.getClipToPadding();
      int var4 = 0;
      int var5;
      int var6;
      if (var3) {
         var5 = var2.getPaddingTop();
         var6 = var2.getHeight() - var2.getPaddingBottom();
         var1.clipRect(var2.getPaddingLeft(), var5, var2.getWidth() - var2.getPaddingRight(), var6);
      } else {
         var6 = var2.getHeight();
         var5 = 0;
      }

      for(int var7 = var2.getChildCount(); var4 < var7; ++var4) {
         View var8 = var2.getChildAt(var4);
         var2.getLayoutManager().getDecoratedBoundsWithMargins(var8, this.mBounds);
         int var9 = this.mBounds.right + Math.round(var8.getTranslationX());
         int var10 = this.mDivider.getIntrinsicWidth();
         this.mDivider.setBounds(var9 - var10, var5, var9, var6);
         this.mDivider.draw(var1);
      }

      var1.restore();
   }

   private void drawVertical(Canvas var1, RecyclerView var2) {
      var1.save();
      boolean var3 = var2.getClipToPadding();
      int var4 = 0;
      int var5;
      int var6;
      if (var3) {
         var5 = var2.getPaddingLeft();
         var6 = var2.getWidth() - var2.getPaddingRight();
         var1.clipRect(var5, var2.getPaddingTop(), var6, var2.getHeight() - var2.getPaddingBottom());
      } else {
         var6 = var2.getWidth();
         var5 = 0;
      }

      for(int var7 = var2.getChildCount(); var4 < var7; ++var4) {
         View var8 = var2.getChildAt(var4);
         var2.getDecoratedBoundsWithMargins(var8, this.mBounds);
         int var9 = this.mBounds.bottom + Math.round(var8.getTranslationY());
         int var10 = this.mDivider.getIntrinsicHeight();
         this.mDivider.setBounds(var5, var9 - var10, var6, var9);
         this.mDivider.draw(var1);
      }

      var1.restore();
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
      if (this.mDivider == null) {
         var1.set(0, 0, 0, 0);
      } else {
         if (this.mOrientation == 1) {
            var1.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
         } else {
            var1.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
         }

      }
   }

   public void onDraw(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
      if (var2.getLayoutManager() != null && this.mDivider != null) {
         if (this.mOrientation == 1) {
            this.drawVertical(var1, var2);
         } else {
            this.drawHorizontal(var1, var2);
         }

      }
   }

   public void setDrawable(@NonNull Drawable var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Drawable cannot be null.");
      } else {
         this.mDivider = var1;
      }
   }

   public void setOrientation(int var1) {
      if (var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
      } else {
         this.mOrientation = var1;
      }
   }
}
