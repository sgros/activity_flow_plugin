package androidx.appcompat.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.StateSet;

class StateListDrawable extends DrawableContainer {
   private boolean mMutated;
   private StateListDrawable.StateListState mStateListState;

   StateListDrawable(StateListDrawable.StateListState var1) {
      if (var1 != null) {
         this.setConstantState(var1);
      }

   }

   StateListDrawable(StateListDrawable.StateListState var1, Resources var2) {
      this.setConstantState(new StateListDrawable.StateListState(var1, this, var2));
      this.onStateChange(this.getState());
   }

   public void applyTheme(Theme var1) {
      super.applyTheme(var1);
      this.onStateChange(this.getState());
   }

   StateListDrawable.StateListState cloneConstantState() {
      return new StateListDrawable.StateListState(this.mStateListState, this, (Resources)null);
   }

   int[] extractStateSet(AttributeSet var1) {
      int var2 = var1.getAttributeCount();
      int[] var3 = new int[var2];
      int var4 = 0;

      int var5;
      int var7;
      for(var5 = 0; var4 < var2; var5 = var7) {
         int var6 = var1.getAttributeNameResource(var4);
         var7 = var5;
         if (var6 != 0) {
            var7 = var5;
            if (var6 != 16842960) {
               var7 = var5;
               if (var6 != 16843161) {
                  if (var1.getAttributeBooleanValue(var4, false)) {
                     var7 = var6;
                  } else {
                     var7 = -var6;
                  }

                  var3[var5] = var7;
                  var7 = var5 + 1;
               }
            }
         }

         ++var4;
      }

      return StateSet.trimStateSet(var3, var5);
   }

   public boolean isStateful() {
      return true;
   }

   public Drawable mutate() {
      if (!this.mMutated) {
         super.mutate();
         this.mStateListState.mutate();
         this.mMutated = true;
      }

      return this;
   }

   protected boolean onStateChange(int[] var1) {
      boolean var2 = super.onStateChange(var1);
      int var3 = this.mStateListState.indexOfStateSet(var1);
      int var4 = var3;
      if (var3 < 0) {
         var4 = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
      }

      if (!this.selectDrawable(var4) && !var2) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected void setConstantState(DrawableContainer.DrawableContainerState var1) {
      super.setConstantState(var1);
      if (var1 instanceof StateListDrawable.StateListState) {
         this.mStateListState = (StateListDrawable.StateListState)var1;
      }

   }

   static class StateListState extends DrawableContainer.DrawableContainerState {
      int[][] mStateSets;

      StateListState(StateListDrawable.StateListState var1, StateListDrawable var2, Resources var3) {
         super(var1, var2, var3);
         if (var1 != null) {
            this.mStateSets = var1.mStateSets;
         } else {
            this.mStateSets = new int[this.getCapacity()][];
         }

      }

      int addStateSet(int[] var1, Drawable var2) {
         int var3 = this.addChild(var2);
         this.mStateSets[var3] = var1;
         return var3;
      }

      public void growArray(int var1, int var2) {
         super.growArray(var1, var2);
         int[][] var3 = new int[var2][];
         System.arraycopy(this.mStateSets, 0, var3, 0, var1);
         this.mStateSets = var3;
      }

      int indexOfStateSet(int[] var1) {
         int[][] var2 = this.mStateSets;
         int var3 = this.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            if (StateSet.stateSetMatches(var2[var4], var1)) {
               return var4;
            }
         }

         return -1;
      }

      void mutate() {
         int[][] var1 = this.mStateSets;
         int[][] var2 = new int[var1.length][];

         for(int var3 = var1.length - 1; var3 >= 0; --var3) {
            var1 = this.mStateSets;
            int[] var4;
            if (var1[var3] != null) {
               var4 = (int[])var1[var3].clone();
            } else {
               var4 = null;
            }

            var2[var3] = var4;
         }

         this.mStateSets = var2;
      }

      public Drawable newDrawable() {
         return new StateListDrawable(this, (Resources)null);
      }

      public Drawable newDrawable(Resources var1) {
         return new StateListDrawable(this, var1);
      }
   }
}
