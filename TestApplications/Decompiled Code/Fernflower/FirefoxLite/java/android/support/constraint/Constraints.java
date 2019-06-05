package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class Constraints extends ViewGroup {
   ConstraintSet myConstraintSet;

   protected Constraints.LayoutParams generateDefaultLayoutParams() {
      return new Constraints.LayoutParams(-2, -2);
   }

   public Constraints.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new Constraints.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new ConstraintLayout.LayoutParams(var1);
   }

   public ConstraintSet getConstraintSet() {
      if (this.myConstraintSet == null) {
         this.myConstraintSet = new ConstraintSet();
      }

      this.myConstraintSet.clone(this);
      return this.myConstraintSet;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
   }

   public static class LayoutParams extends ConstraintLayout.LayoutParams {
      public float alpha = 1.0F;
      public boolean applyElevation;
      public float elevation;
      public float rotation;
      public float rotationX;
      public float rotationY;
      public float scaleX;
      public float scaleY;
      public float transformPivotX;
      public float transformPivotY;
      public float translationX;
      public float translationY;
      public float translationZ;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.applyElevation = false;
         this.elevation = 0.0F;
         this.rotation = 0.0F;
         this.rotationX = 0.0F;
         this.rotationY = 0.0F;
         this.scaleX = 1.0F;
         this.scaleY = 1.0F;
         this.transformPivotX = 0.0F;
         this.transformPivotY = 0.0F;
         this.translationX = 0.0F;
         this.translationY = 0.0F;
         this.translationZ = 0.0F;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         int var3 = 0;
         this.applyElevation = false;
         this.elevation = 0.0F;
         this.rotation = 0.0F;
         this.rotationX = 0.0F;
         this.rotationY = 0.0F;
         this.scaleX = 1.0F;
         this.scaleY = 1.0F;
         this.transformPivotX = 0.0F;
         this.transformPivotY = 0.0F;
         this.translationX = 0.0F;
         this.translationY = 0.0F;
         this.translationZ = 0.0F;
         TypedArray var6 = var1.obtainStyledAttributes(var2, R.styleable.ConstraintSet);

         for(int var4 = var6.getIndexCount(); var3 < var4; ++var3) {
            int var5 = var6.getIndex(var3);
            if (var5 == R.styleable.ConstraintSet_android_alpha) {
               this.alpha = var6.getFloat(var5, this.alpha);
            } else if (var5 == R.styleable.ConstraintSet_android_elevation) {
               this.elevation = var6.getFloat(var5, this.elevation);
               this.applyElevation = true;
            } else if (var5 == R.styleable.ConstraintSet_android_rotationX) {
               this.rotationX = var6.getFloat(var5, this.rotationX);
            } else if (var5 == R.styleable.ConstraintSet_android_rotationY) {
               this.rotationY = var6.getFloat(var5, this.rotationY);
            } else if (var5 == R.styleable.ConstraintSet_android_rotation) {
               this.rotation = var6.getFloat(var5, this.rotation);
            } else if (var5 == R.styleable.ConstraintSet_android_scaleX) {
               this.scaleX = var6.getFloat(var5, this.scaleX);
            } else if (var5 == R.styleable.ConstraintSet_android_scaleY) {
               this.scaleY = var6.getFloat(var5, this.scaleY);
            } else if (var5 == R.styleable.ConstraintSet_android_transformPivotX) {
               this.transformPivotX = var6.getFloat(var5, this.transformPivotX);
            } else if (var5 == R.styleable.ConstraintSet_android_transformPivotY) {
               this.transformPivotY = var6.getFloat(var5, this.transformPivotY);
            } else if (var5 == R.styleable.ConstraintSet_android_translationX) {
               this.translationX = var6.getFloat(var5, this.translationX);
            } else if (var5 == R.styleable.ConstraintSet_android_translationY) {
               this.translationY = var6.getFloat(var5, this.translationY);
            } else if (var5 == R.styleable.ConstraintSet_android_translationZ) {
               this.translationX = var6.getFloat(var5, this.translationZ);
            }
         }

      }
   }
}
