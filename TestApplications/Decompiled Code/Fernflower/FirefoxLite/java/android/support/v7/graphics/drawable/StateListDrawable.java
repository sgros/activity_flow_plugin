package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.StateSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class StateListDrawable extends DrawableContainer {
   private boolean mMutated;
   private StateListDrawable.StateListState mStateListState;

   StateListDrawable() {
      this((StateListDrawable.StateListState)null, (Resources)null);
   }

   StateListDrawable(StateListDrawable.StateListState var1) {
      if (var1 != null) {
         this.setConstantState(var1);
      }

   }

   StateListDrawable(StateListDrawable.StateListState var1, Resources var2) {
      this.setConstantState(new StateListDrawable.StateListState(var1, this, var2));
      this.onStateChange(this.getState());
   }

   private void inflateChildElements(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      StateListDrawable.StateListState var6 = this.mStateListState;
      int var7 = var3.getDepth() + 1;

      while(true) {
         int var8 = var3.next();
         if (var8 == 1) {
            break;
         }

         int var9 = var3.getDepth();
         if (var9 < var7 && var8 == 3) {
            break;
         }

         if (var8 == 2 && var9 <= var7 && var3.getName().equals("item")) {
            TypedArray var10 = TypedArrayUtils.obtainAttributes(var2, var5, var4, R.styleable.StateListDrawableItem);
            Drawable var11 = null;
            var8 = var10.getResourceId(R.styleable.StateListDrawableItem_android_drawable, -1);
            if (var8 > 0) {
               var11 = AppCompatResources.getDrawable(var1, var8);
            }

            var10.recycle();
            int[] var12 = this.extractStateSet(var4);
            Drawable var14 = var11;
            if (var11 == null) {
               while(true) {
                  var8 = var3.next();
                  if (var8 != 4) {
                     if (var8 != 2) {
                        StringBuilder var13 = new StringBuilder();
                        var13.append(var3.getPositionDescription());
                        var13.append(": <item> tag requires a 'drawable' attribute or ");
                        var13.append("child tag defining a drawable");
                        throw new XmlPullParserException(var13.toString());
                     }

                     if (VERSION.SDK_INT >= 21) {
                        var14 = Drawable.createFromXmlInner(var2, var3, var4, var5);
                     } else {
                        var14 = Drawable.createFromXmlInner(var2, var3, var4);
                     }
                     break;
                  }
               }
            }

            var6.addStateSet(var12, var14);
         }
      }

   }

   private void updateStateFromTypedArray(TypedArray var1) {
      StateListDrawable.StateListState var2 = this.mStateListState;
      if (VERSION.SDK_INT >= 21) {
         var2.mChangingConfigurations |= var1.getChangingConfigurations();
      }

      var2.mVariablePadding = var1.getBoolean(R.styleable.StateListDrawable_android_variablePadding, var2.mVariablePadding);
      var2.mConstantSize = var1.getBoolean(R.styleable.StateListDrawable_android_constantSize, var2.mConstantSize);
      var2.mEnterFadeDuration = var1.getInt(R.styleable.StateListDrawable_android_enterFadeDuration, var2.mEnterFadeDuration);
      var2.mExitFadeDuration = var1.getInt(R.styleable.StateListDrawable_android_exitFadeDuration, var2.mExitFadeDuration);
      var2.mDither = var1.getBoolean(R.styleable.StateListDrawable_android_dither, var2.mDither);
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

   public void inflate(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var6 = TypedArrayUtils.obtainAttributes(var2, var5, var4, R.styleable.StateListDrawable);
      this.setVisible(var6.getBoolean(R.styleable.StateListDrawable_android_visible, true), true);
      this.updateStateFromTypedArray(var6);
      this.updateDensity(var2);
      var6.recycle();
      this.inflateChildElements(var1, var2, var3, var4, var5);
      this.onStateChange(this.getState());
   }

   public boolean isStateful() {
      return true;
   }

   public Drawable mutate() {
      if (!this.mMutated && super.mutate() == this) {
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
         int[][] var1 = new int[this.mStateSets.length][];

         for(int var2 = this.mStateSets.length - 1; var2 >= 0; --var2) {
            int[] var3;
            if (this.mStateSets[var2] != null) {
               var3 = (int[])this.mStateSets[var2].clone();
            } else {
               var3 = null;
            }

            var1[var2] = var3;
         }

         this.mStateSets = var1;
      }

      public Drawable newDrawable() {
         return new StateListDrawable(this, (Resources)null);
      }

      public Drawable newDrawable(Resources var1) {
         return new StateListDrawable(this, var1);
      }
   }
}
