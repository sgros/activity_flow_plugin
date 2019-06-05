package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;

public class Barrier extends ConstraintHelper {
   private android.support.constraint.solver.widgets.Barrier mBarrier;
   private int mIndicatedType = 0;
   private int mResolvedType = 0;

   public Barrier(Context var1) {
      super(var1);
      super.setVisibility(8);
   }

   public int getType() {
      return this.mIndicatedType;
   }

   protected void init(AttributeSet var1) {
      super.init(var1);
      this.mBarrier = new android.support.constraint.solver.widgets.Barrier();
      if (var1 != null) {
         TypedArray var5 = this.getContext().obtainStyledAttributes(var1, R.styleable.ConstraintLayout_Layout);
         int var2 = var5.getIndexCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = var5.getIndex(var3);
            if (var4 == R.styleable.ConstraintLayout_Layout_barrierDirection) {
               this.setType(var5.getInt(var4, 0));
            } else if (var4 == R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
               this.mBarrier.setAllowsGoneWidget(var5.getBoolean(var4, true));
            }
         }
      }

      this.mHelperWidget = this.mBarrier;
      this.validateParams();
   }

   public void setType(int var1) {
      this.mIndicatedType = var1;
      this.mResolvedType = var1;
      if (VERSION.SDK_INT < 17) {
         if (this.mIndicatedType == 5) {
            this.mResolvedType = 0;
         } else if (this.mIndicatedType == 6) {
            this.mResolvedType = 1;
         }
      } else {
         boolean var2;
         if (1 == this.getResources().getConfiguration().getLayoutDirection()) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            if (this.mIndicatedType == 5) {
               this.mResolvedType = 1;
            } else if (this.mIndicatedType == 6) {
               this.mResolvedType = 0;
            }
         } else if (this.mIndicatedType == 5) {
            this.mResolvedType = 0;
         } else if (this.mIndicatedType == 6) {
            this.mResolvedType = 1;
         }
      }

      this.mBarrier.setBarrierType(this.mResolvedType);
   }
}
