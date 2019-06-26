package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import androidx.collection.ArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable2Compat {
   private AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState mAnimatedVectorState;
   ArrayList mAnimationCallbacks;
   private AnimatorListener mAnimatorListener;
   private android.animation.ArgbEvaluator mArgbEvaluator;
   final Callback mCallback;
   private Context mContext;

   AnimatedVectorDrawableCompat() {
      this((Context)null, (AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState)null, (Resources)null);
   }

   private AnimatedVectorDrawableCompat(Context var1) {
      this(var1, (AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState)null, (Resources)null);
   }

   private AnimatedVectorDrawableCompat(Context var1, AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var2, Resources var3) {
      this.mArgbEvaluator = null;
      this.mAnimatorListener = null;
      this.mAnimationCallbacks = null;
      this.mCallback = new Callback() {
         public void invalidateDrawable(Drawable var1) {
            AnimatedVectorDrawableCompat.this.invalidateSelf();
         }

         public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
            AnimatedVectorDrawableCompat.this.scheduleSelf(var2, var3);
         }

         public void unscheduleDrawable(Drawable var1, Runnable var2) {
            AnimatedVectorDrawableCompat.this.unscheduleSelf(var2);
         }
      };
      this.mContext = var1;
      if (var2 != null) {
         this.mAnimatedVectorState = var2;
      } else {
         this.mAnimatedVectorState = new AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState(var1, var2, this.mCallback, var3);
      }

   }

   public static AnimatedVectorDrawableCompat createFromXmlInner(Context var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      AnimatedVectorDrawableCompat var5 = new AnimatedVectorDrawableCompat(var0);
      var5.inflate(var1, var2, var3, var4);
      return var5;
   }

   private void setupAnimatorsForTarget(String var1, Animator var2) {
      var2.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(var1));
      if (VERSION.SDK_INT < 21) {
         this.setupColorAnimator(var2);
      }

      AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var3 = this.mAnimatedVectorState;
      if (var3.mAnimators == null) {
         var3.mAnimators = new ArrayList();
         this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
      }

      this.mAnimatedVectorState.mAnimators.add(var2);
      this.mAnimatedVectorState.mTargetNameMap.put(var2, var1);
   }

   private void setupColorAnimator(Animator var1) {
      if (var1 instanceof AnimatorSet) {
         ArrayList var2 = ((AnimatorSet)var1).getChildAnimations();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               this.setupColorAnimator((Animator)var2.get(var3));
            }
         }
      }

      if (var1 instanceof ObjectAnimator) {
         ObjectAnimator var4 = (ObjectAnimator)var1;
         String var5 = var4.getPropertyName();
         if ("fillColor".equals(var5) || "strokeColor".equals(var5)) {
            if (this.mArgbEvaluator == null) {
               this.mArgbEvaluator = new android.animation.ArgbEvaluator();
            }

            var4.setEvaluator(this.mArgbEvaluator);
         }
      }

   }

   public void applyTheme(Theme var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         DrawableCompat.applyTheme(var2, var1);
      }

   }

   public boolean canApplyTheme() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? DrawableCompat.canApplyTheme(var1) : false;
   }

   public void draw(Canvas var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         var2.draw(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.draw(var1);
         if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.invalidateSelf();
         }

      }
   }

   public int getAlpha() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? DrawableCompat.getAlpha(var1) : this.mAnimatedVectorState.mVectorDrawable.getAlpha();
   }

   public int getChangingConfigurations() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? var1.getChangingConfigurations() : super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
   }

   public ConstantState getConstantState() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null && VERSION.SDK_INT >= 24 ? new AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState(var1.getConstantState()) : null;
   }

   public int getIntrinsicHeight() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? var1.getIntrinsicHeight() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
   }

   public int getIntrinsicWidth() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? var1.getIntrinsicWidth() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
   }

   public int getOpacity() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? var1.getOpacity() : this.mAnimatedVectorState.mVectorDrawable.getOpacity();
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3) throws XmlPullParserException, IOException {
      this.inflate(var1, var2, var3, (Theme)null);
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      Drawable var5 = super.mDelegateDrawable;
      if (var5 != null) {
         DrawableCompat.inflate(var5, var1, var2, var3, var4);
      } else {
         int var6 = var2.getEventType();

         for(int var7 = var2.getDepth(); var6 != 1 && (var2.getDepth() >= var7 + 1 || var6 != 3); var6 = var2.next()) {
            if (var6 == 2) {
               String var10 = var2.getName();
               if ("animated-vector".equals(var10)) {
                  TypedArray var8 = TypedArrayUtils.obtainAttributes(var1, var4, var3, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                  var6 = var8.getResourceId(0, 0);
                  if (var6 != 0) {
                     VectorDrawableCompat var11 = VectorDrawableCompat.create(var1, var6, var4);
                     var11.setAllowCaching(false);
                     var11.setCallback(this.mCallback);
                     VectorDrawableCompat var9 = this.mAnimatedVectorState.mVectorDrawable;
                     if (var9 != null) {
                        var9.setCallback((Callback)null);
                     }

                     this.mAnimatedVectorState.mVectorDrawable = var11;
                  }

                  var8.recycle();
               } else if ("target".equals(var10)) {
                  TypedArray var12 = var1.obtainAttributes(var3, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                  String var13 = var12.getString(0);
                  var6 = var12.getResourceId(1, 0);
                  if (var6 != 0) {
                     Context var14 = this.mContext;
                     if (var14 == null) {
                        var12.recycle();
                        throw new IllegalStateException("Context can't be null when inflating animators");
                     }

                     this.setupAnimatorsForTarget(var13, AnimatorInflaterCompat.loadAnimator(var14, var6));
                  }

                  var12.recycle();
               }
            }
         }

         this.mAnimatedVectorState.setupAnimatorSet();
      }
   }

   public boolean isAutoMirrored() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? DrawableCompat.isAutoMirrored(var1) : this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
   }

   public boolean isRunning() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? ((AnimatedVectorDrawable)var1).isRunning() : this.mAnimatedVectorState.mAnimatorSet.isRunning();
   }

   public boolean isStateful() {
      Drawable var1 = super.mDelegateDrawable;
      return var1 != null ? var1.isStateful() : this.mAnimatedVectorState.mVectorDrawable.isStateful();
   }

   public Drawable mutate() {
      Drawable var1 = super.mDelegateDrawable;
      if (var1 != null) {
         var1.mutate();
      }

      return this;
   }

   protected void onBoundsChange(Rect var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         var2.setBounds(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setBounds(var1);
      }
   }

   protected boolean onLevelChange(int var1) {
      Drawable var2 = super.mDelegateDrawable;
      return var2 != null ? var2.setLevel(var1) : this.mAnimatedVectorState.mVectorDrawable.setLevel(var1);
   }

   protected boolean onStateChange(int[] var1) {
      Drawable var2 = super.mDelegateDrawable;
      return var2 != null ? var2.setState(var1) : this.mAnimatedVectorState.mVectorDrawable.setState(var1);
   }

   public void setAlpha(int var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         var2.setAlpha(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setAlpha(var1);
      }
   }

   public void setAutoMirrored(boolean var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         DrawableCompat.setAutoMirrored(var2, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(var1);
      }
   }

   public void setColorFilter(ColorFilter var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         var2.setColorFilter(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setColorFilter(var1);
      }
   }

   public void setTint(int var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         DrawableCompat.setTint(var2, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTint(var1);
      }
   }

   public void setTintList(ColorStateList var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         DrawableCompat.setTintList(var2, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTintList(var1);
      }
   }

   public void setTintMode(Mode var1) {
      Drawable var2 = super.mDelegateDrawable;
      if (var2 != null) {
         DrawableCompat.setTintMode(var2, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTintMode(var1);
      }
   }

   public boolean setVisible(boolean var1, boolean var2) {
      Drawable var3 = super.mDelegateDrawable;
      if (var3 != null) {
         return var3.setVisible(var1, var2);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setVisible(var1, var2);
         return super.setVisible(var1, var2);
      }
   }

   public void start() {
      Drawable var1 = super.mDelegateDrawable;
      if (var1 != null) {
         ((AnimatedVectorDrawable)var1).start();
      } else if (!this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
         this.mAnimatedVectorState.mAnimatorSet.start();
         this.invalidateSelf();
      }
   }

   public void stop() {
      Drawable var1 = super.mDelegateDrawable;
      if (var1 != null) {
         ((AnimatedVectorDrawable)var1).stop();
      } else {
         this.mAnimatedVectorState.mAnimatorSet.end();
      }
   }

   private static class AnimatedVectorDrawableCompatState extends ConstantState {
      AnimatorSet mAnimatorSet;
      ArrayList mAnimators;
      int mChangingConfigurations;
      ArrayMap mTargetNameMap;
      VectorDrawableCompat mVectorDrawable;

      public AnimatedVectorDrawableCompatState(Context var1, AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var2, Callback var3, Resources var4) {
         if (var2 != null) {
            this.mChangingConfigurations = var2.mChangingConfigurations;
            VectorDrawableCompat var7 = var2.mVectorDrawable;
            int var5 = 0;
            if (var7 != null) {
               ConstantState var8 = var7.getConstantState();
               if (var4 != null) {
                  this.mVectorDrawable = (VectorDrawableCompat)var8.newDrawable(var4);
               } else {
                  this.mVectorDrawable = (VectorDrawableCompat)var8.newDrawable();
               }

               var7 = this.mVectorDrawable;
               var7.mutate();
               this.mVectorDrawable = (VectorDrawableCompat)var7;
               this.mVectorDrawable.setCallback(var3);
               this.mVectorDrawable.setBounds(var2.mVectorDrawable.getBounds());
               this.mVectorDrawable.setAllowCaching(false);
            }

            ArrayList var9 = var2.mAnimators;
            if (var9 != null) {
               int var6 = var9.size();
               this.mAnimators = new ArrayList(var6);

               for(this.mTargetNameMap = new ArrayMap(var6); var5 < var6; ++var5) {
                  Animator var11 = (Animator)var2.mAnimators.get(var5);
                  Animator var10 = var11.clone();
                  String var12 = (String)var2.mTargetNameMap.get(var11);
                  var10.setTarget(this.mVectorDrawable.getTargetByName(var12));
                  this.mAnimators.add(var10);
                  this.mTargetNameMap.put(var10, var12);
               }

               this.setupAnimatorSet();
            }
         }

      }

      public int getChangingConfigurations() {
         return this.mChangingConfigurations;
      }

      public Drawable newDrawable() {
         throw new IllegalStateException("No constant state support for SDK < 24.");
      }

      public Drawable newDrawable(Resources var1) {
         throw new IllegalStateException("No constant state support for SDK < 24.");
      }

      public void setupAnimatorSet() {
         if (this.mAnimatorSet == null) {
            this.mAnimatorSet = new AnimatorSet();
         }

         this.mAnimatorSet.playTogether(this.mAnimators);
      }
   }

   private static class AnimatedVectorDrawableDelegateState extends ConstantState {
      private final ConstantState mDelegateState;

      public AnimatedVectorDrawableDelegateState(ConstantState var1) {
         this.mDelegateState = var1;
      }

      public boolean canApplyTheme() {
         return this.mDelegateState.canApplyTheme();
      }

      public int getChangingConfigurations() {
         return this.mDelegateState.getChangingConfigurations();
      }

      public Drawable newDrawable() {
         AnimatedVectorDrawableCompat var1 = new AnimatedVectorDrawableCompat();
         var1.mDelegateDrawable = this.mDelegateState.newDrawable();
         var1.mDelegateDrawable.setCallback(var1.mCallback);
         return var1;
      }

      public Drawable newDrawable(Resources var1) {
         AnimatedVectorDrawableCompat var2 = new AnimatedVectorDrawableCompat();
         var2.mDelegateDrawable = this.mDelegateState.newDrawable(var1);
         var2.mDelegateDrawable.setCallback(var2.mCallback);
         return var2;
      }

      public Drawable newDrawable(Resources var1, Theme var2) {
         AnimatedVectorDrawableCompat var3 = new AnimatedVectorDrawableCompat();
         var3.mDelegateDrawable = this.mDelegateState.newDrawable(var1, var2);
         var3.mDelegateDrawable.setCallback(var3.mCallback);
         return var3;
      }
   }
}
