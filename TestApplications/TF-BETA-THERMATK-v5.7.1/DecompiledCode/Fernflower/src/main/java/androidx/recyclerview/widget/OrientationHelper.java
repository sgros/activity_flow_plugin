package androidx.recyclerview.widget;

import android.graphics.Rect;
import android.view.View;

public abstract class OrientationHelper {
   private int mLastTotalSpace;
   protected final RecyclerView.LayoutManager mLayoutManager;
   final Rect mTmpRect;

   private OrientationHelper(RecyclerView.LayoutManager var1) {
      this.mLastTotalSpace = Integer.MIN_VALUE;
      this.mTmpRect = new Rect();
      this.mLayoutManager = var1;
   }

   // $FF: synthetic method
   OrientationHelper(RecyclerView.LayoutManager var1, Object var2) {
      this(var1);
   }

   public static OrientationHelper createHorizontalHelper(RecyclerView.LayoutManager var0) {
      return new OrientationHelper(var0) {
         public int getDecoratedEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedRight(var1) + var2.rightMargin;
         }

         public int getDecoratedMeasurement(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedMeasuredWidth(var1) + var2.leftMargin + var2.rightMargin;
         }

         public int getDecoratedMeasurementInOther(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedMeasuredHeight(var1) + var2.topMargin + var2.bottomMargin;
         }

         public int getDecoratedStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedLeft(var1) - var2.leftMargin;
         }

         public int getEnd() {
            return super.mLayoutManager.getWidth();
         }

         public int getEndAfterPadding() {
            return super.mLayoutManager.getWidth() - super.mLayoutManager.getPaddingRight();
         }

         public int getEndPadding() {
            return super.mLayoutManager.getPaddingRight();
         }

         public int getMode() {
            return super.mLayoutManager.getWidthMode();
         }

         public int getModeInOther() {
            return super.mLayoutManager.getHeightMode();
         }

         public int getStartAfterPadding() {
            return super.mLayoutManager.getPaddingLeft();
         }

         public int getTotalSpace() {
            return super.mLayoutManager.getWidth() - super.mLayoutManager.getPaddingLeft() - super.mLayoutManager.getPaddingRight();
         }

         public int getTransformedEndWithDecoration(View var1) {
            super.mLayoutManager.getTransformedBoundingBox(var1, true, super.mTmpRect);
            return super.mTmpRect.right;
         }

         public int getTransformedStartWithDecoration(View var1) {
            super.mLayoutManager.getTransformedBoundingBox(var1, true, super.mTmpRect);
            return super.mTmpRect.left;
         }

         public void offsetChildren(int var1) {
            super.mLayoutManager.offsetChildrenHorizontal(var1);
         }
      };
   }

   public static OrientationHelper createOrientationHelper(RecyclerView.LayoutManager var0, int var1) {
      if (var1 != 0) {
         if (var1 == 1) {
            return createVerticalHelper(var0);
         } else {
            throw new IllegalArgumentException("invalid orientation");
         }
      } else {
         return createHorizontalHelper(var0);
      }
   }

   public static OrientationHelper createVerticalHelper(RecyclerView.LayoutManager var0) {
      return new OrientationHelper(var0) {
         public int getDecoratedEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedBottom(var1) + var2.bottomMargin;
         }

         public int getDecoratedMeasurement(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedMeasuredHeight(var1) + var2.topMargin + var2.bottomMargin;
         }

         public int getDecoratedMeasurementInOther(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedMeasuredWidth(var1) + var2.leftMargin + var2.rightMargin;
         }

         public int getDecoratedStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return super.mLayoutManager.getDecoratedTop(var1) - var2.topMargin;
         }

         public int getEnd() {
            return super.mLayoutManager.getHeight();
         }

         public int getEndAfterPadding() {
            return super.mLayoutManager.getHeight() - super.mLayoutManager.getPaddingBottom();
         }

         public int getEndPadding() {
            return super.mLayoutManager.getPaddingBottom();
         }

         public int getMode() {
            return super.mLayoutManager.getHeightMode();
         }

         public int getModeInOther() {
            return super.mLayoutManager.getWidthMode();
         }

         public int getStartAfterPadding() {
            return super.mLayoutManager.getPaddingTop();
         }

         public int getTotalSpace() {
            return super.mLayoutManager.getHeight() - super.mLayoutManager.getPaddingTop() - super.mLayoutManager.getPaddingBottom();
         }

         public int getTransformedEndWithDecoration(View var1) {
            super.mLayoutManager.getTransformedBoundingBox(var1, true, super.mTmpRect);
            return super.mTmpRect.bottom;
         }

         public int getTransformedStartWithDecoration(View var1) {
            super.mLayoutManager.getTransformedBoundingBox(var1, true, super.mTmpRect);
            return super.mTmpRect.top;
         }

         public void offsetChildren(int var1) {
            super.mLayoutManager.offsetChildrenVertical(var1);
         }
      };
   }

   public abstract int getDecoratedEnd(View var1);

   public abstract int getDecoratedMeasurement(View var1);

   public abstract int getDecoratedMeasurementInOther(View var1);

   public abstract int getDecoratedStart(View var1);

   public abstract int getEnd();

   public abstract int getEndAfterPadding();

   public abstract int getEndPadding();

   public abstract int getMode();

   public abstract int getModeInOther();

   public abstract int getStartAfterPadding();

   public abstract int getTotalSpace();

   public int getTotalSpaceChange() {
      int var1;
      if (Integer.MIN_VALUE == this.mLastTotalSpace) {
         var1 = 0;
      } else {
         var1 = this.getTotalSpace() - this.mLastTotalSpace;
      }

      return var1;
   }

   public abstract int getTransformedEndWithDecoration(View var1);

   public abstract int getTransformedStartWithDecoration(View var1);

   public abstract void offsetChildren(int var1);

   public void onLayoutComplete() {
      this.mLastTotalSpace = this.getTotalSpace();
   }
}
