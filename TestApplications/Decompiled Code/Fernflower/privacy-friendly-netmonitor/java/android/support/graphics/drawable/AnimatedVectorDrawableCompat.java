package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable2Compat {
   private static final String ANIMATED_VECTOR = "animated-vector";
   private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
   private static final String LOGTAG = "AnimatedVDCompat";
   private static final String TARGET = "target";
   private AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState mAnimatedVectorState;
   private ArrayList mAnimationCallbacks;
   private AnimatorListener mAnimatorListener;
   private android.animation.ArgbEvaluator mArgbEvaluator;
   AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
   final Callback mCallback;
   private Context mContext;

   AnimatedVectorDrawableCompat() {
      this((Context)null, (AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState)null, (Resources)null);
   }

   private AnimatedVectorDrawableCompat(@Nullable Context var1) {
      this(var1, (AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState)null, (Resources)null);
   }

   private AnimatedVectorDrawableCompat(@Nullable Context var1, @Nullable AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var2, @Nullable Resources var3) {
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

   public static void clearAnimationCallbacks(Drawable var0) {
      if (var0 != null && var0 instanceof Animatable) {
         if (VERSION.SDK_INT >= 24) {
            ((AnimatedVectorDrawable)var0).clearAnimationCallbacks();
         } else {
            ((AnimatedVectorDrawableCompat)var0).clearAnimationCallbacks();
         }

      }
   }

   @Nullable
   public static AnimatedVectorDrawableCompat create(@NonNull Context var0, @DrawableRes int var1) {
      if (VERSION.SDK_INT >= 24) {
         AnimatedVectorDrawableCompat var16 = new AnimatedVectorDrawableCompat(var0);
         var16.mDelegateDrawable = ResourcesCompat.getDrawable(var0.getResources(), var1, var0.getTheme());
         var16.mDelegateDrawable.setCallback(var16.mCallback);
         var16.mCachedConstantStateDelegate = new AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState(var16.mDelegateDrawable.getConstantState());
         return var16;
      } else {
         Resources var2 = var0.getResources();

         XmlPullParserException var12;
         XmlPullParserException var17;
         label67: {
            IOException var10000;
            label53: {
               AttributeSet var3;
               XmlResourceParser var15;
               boolean var10001;
               try {
                  var15 = var2.getXml(var1);
                  var3 = Xml.asAttributeSet(var15);
               } catch (XmlPullParserException var10) {
                  var17 = var10;
                  var10001 = false;
                  break label67;
               } catch (IOException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label53;
               }

               while(true) {
                  try {
                     var1 = var15.next();
                  } catch (XmlPullParserException var8) {
                     var17 = var8;
                     var10001 = false;
                     break label67;
                  } catch (IOException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break;
                  }

                  if (var1 == 2 || var1 == 1) {
                     if (var1 != 2) {
                        try {
                           var12 = new XmlPullParserException("No start tag found");
                           throw var12;
                        } catch (XmlPullParserException var4) {
                           var17 = var4;
                           var10001 = false;
                           break label67;
                        } catch (IOException var5) {
                           var10000 = var5;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           AnimatedVectorDrawableCompat var14 = createFromXmlInner(var0, var0.getResources(), var15, var3, var0.getTheme());
                           return var14;
                        } catch (XmlPullParserException var6) {
                           var17 = var6;
                           var10001 = false;
                           break label67;
                        } catch (IOException var7) {
                           var10000 = var7;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }
            }

            IOException var13 = var10000;
            Log.e("AnimatedVDCompat", "parser error", var13);
            return null;
         }

         var12 = var17;
         Log.e("AnimatedVDCompat", "parser error", var12);
         return null;
      }
   }

   public static AnimatedVectorDrawableCompat createFromXmlInner(Context var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      AnimatedVectorDrawableCompat var5 = new AnimatedVectorDrawableCompat(var0);
      var5.inflate(var1, var2, var3, var4);
      return var5;
   }

   public static void registerAnimationCallback(Drawable var0, Animatable2Compat.AnimationCallback var1) {
      if (var0 != null && var1 != null) {
         if (var0 instanceof Animatable) {
            if (VERSION.SDK_INT >= 24) {
               registerPlatformCallback((AnimatedVectorDrawable)var0, var1);
            } else {
               ((AnimatedVectorDrawableCompat)var0).registerAnimationCallback(var1);
            }

         }
      }
   }

   @RequiresApi(23)
   private static void registerPlatformCallback(@NonNull AnimatedVectorDrawable var0, @NonNull Animatable2Compat.AnimationCallback var1) {
      var0.registerAnimationCallback(var1.getPlatformCallback());
   }

   private void removeAnimatorSetListener() {
      if (this.mAnimatorListener != null) {
         this.mAnimatedVectorState.mAnimatorSet.removeListener(this.mAnimatorListener);
         this.mAnimatorListener = null;
      }

   }

   private void setupAnimatorsForTarget(String var1, Animator var2) {
      var2.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(var1));
      if (VERSION.SDK_INT < 21) {
         this.setupColorAnimator(var2);
      }

      if (this.mAnimatedVectorState.mAnimators == null) {
         this.mAnimatedVectorState.mAnimators = new ArrayList();
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
         ObjectAnimator var5 = (ObjectAnimator)var1;
         String var4 = var5.getPropertyName();
         if ("fillColor".equals(var4) || "strokeColor".equals(var4)) {
            if (this.mArgbEvaluator == null) {
               this.mArgbEvaluator = new android.animation.ArgbEvaluator();
            }

            var5.setEvaluator(this.mArgbEvaluator);
         }
      }

   }

   public static boolean unregisterAnimationCallback(Drawable var0, Animatable2Compat.AnimationCallback var1) {
      if (var0 != null && var1 != null) {
         if (!(var0 instanceof Animatable)) {
            return false;
         } else {
            return VERSION.SDK_INT >= 24 ? unregisterPlatformCallback((AnimatedVectorDrawable)var0, var1) : ((AnimatedVectorDrawableCompat)var0).unregisterAnimationCallback(var1);
         }
      } else {
         return false;
      }
   }

   @RequiresApi(23)
   private static boolean unregisterPlatformCallback(AnimatedVectorDrawable var0, Animatable2Compat.AnimationCallback var1) {
      return var0.unregisterAnimationCallback(var1.getPlatformCallback());
   }

   public void applyTheme(Theme var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.applyTheme(this.mDelegateDrawable, var1);
      }
   }

   public boolean canApplyTheme() {
      return this.mDelegateDrawable != null ? DrawableCompat.canApplyTheme(this.mDelegateDrawable) : false;
   }

   public void clearAnimationCallbacks() {
      if (this.mDelegateDrawable != null) {
         ((AnimatedVectorDrawable)this.mDelegateDrawable).clearAnimationCallbacks();
      } else {
         this.removeAnimatorSetListener();
         if (this.mAnimationCallbacks != null) {
            this.mAnimationCallbacks.clear();
         }
      }
   }

   public void draw(Canvas var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.draw(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.draw(var1);
         if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.invalidateSelf();
         }

      }
   }

   public int getAlpha() {
      return this.mDelegateDrawable != null ? DrawableCompat.getAlpha(this.mDelegateDrawable) : this.mAnimatedVectorState.mVectorDrawable.getAlpha();
   }

   public int getChangingConfigurations() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getChangingConfigurations() : super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
   }

   public ConstantState getConstantState() {
      return this.mDelegateDrawable != null && VERSION.SDK_INT >= 24 ? new AnimatedVectorDrawableCompat.AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState()) : null;
   }

   public int getIntrinsicHeight() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getIntrinsicHeight() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
   }

   public int getIntrinsicWidth() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getIntrinsicWidth() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
   }

   public int getOpacity() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getOpacity() : this.mAnimatedVectorState.mVectorDrawable.getOpacity();
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3) throws XmlPullParserException, IOException {
      this.inflate(var1, var2, var3, (Theme)null);
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.inflate(this.mDelegateDrawable, var1, var2, var3, var4);
      } else {
         int var5 = var2.getEventType();

         for(int var6 = var2.getDepth(); var5 != 1 && (var2.getDepth() >= var6 + 1 || var5 != 3); var5 = var2.next()) {
            if (var5 == 2) {
               String var7 = var2.getName();
               TypedArray var9;
               if ("animated-vector".equals(var7)) {
                  var9 = TypedArrayUtils.obtainAttributes(var1, var4, var3, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                  var5 = var9.getResourceId(0, 0);
                  if (var5 != 0) {
                     VectorDrawableCompat var8 = VectorDrawableCompat.create(var1, var5, var4);
                     var8.setAllowCaching(false);
                     var8.setCallback(this.mCallback);
                     if (this.mAnimatedVectorState.mVectorDrawable != null) {
                        this.mAnimatedVectorState.mVectorDrawable.setCallback((Callback)null);
                     }

                     this.mAnimatedVectorState.mVectorDrawable = var8;
                  }

                  var9.recycle();
               } else if ("target".equals(var7)) {
                  var9 = var1.obtainAttributes(var3, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                  String var10 = var9.getString(0);
                  var5 = var9.getResourceId(1, 0);
                  if (var5 != 0) {
                     if (this.mContext == null) {
                        var9.recycle();
                        throw new IllegalStateException("Context can't be null when inflating animators");
                     }

                     this.setupAnimatorsForTarget(var10, AnimatorInflaterCompat.loadAnimator(this.mContext, var5));
                  }

                  var9.recycle();
               }
            }
         }

         this.mAnimatedVectorState.setupAnimatorSet();
      }
   }

   public boolean isAutoMirrored() {
      return this.mDelegateDrawable != null ? DrawableCompat.isAutoMirrored(this.mDelegateDrawable) : this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
   }

   public boolean isRunning() {
      return this.mDelegateDrawable != null ? ((AnimatedVectorDrawable)this.mDelegateDrawable).isRunning() : this.mAnimatedVectorState.mAnimatorSet.isRunning();
   }

   public boolean isStateful() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.isStateful() : this.mAnimatedVectorState.mVectorDrawable.isStateful();
   }

   public Drawable mutate() {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.mutate();
      }

      return this;
   }

   protected void onBoundsChange(Rect var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setBounds(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setBounds(var1);
      }
   }

   protected boolean onLevelChange(int var1) {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.setLevel(var1) : this.mAnimatedVectorState.mVectorDrawable.setLevel(var1);
   }

   protected boolean onStateChange(int[] var1) {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.setState(var1) : this.mAnimatedVectorState.mVectorDrawable.setState(var1);
   }

   public void registerAnimationCallback(@NonNull Animatable2Compat.AnimationCallback var1) {
      if (this.mDelegateDrawable != null) {
         registerPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, var1);
      } else if (var1 != null) {
         if (this.mAnimationCallbacks == null) {
            this.mAnimationCallbacks = new ArrayList();
         }

         if (!this.mAnimationCallbacks.contains(var1)) {
            this.mAnimationCallbacks.add(var1);
            if (this.mAnimatorListener == null) {
               this.mAnimatorListener = new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     ArrayList var4 = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                     int var2 = var4.size();

                     for(int var3 = 0; var3 < var2; ++var3) {
                        ((Animatable2Compat.AnimationCallback)var4.get(var3)).onAnimationEnd(AnimatedVectorDrawableCompat.this);
                     }

                  }

                  public void onAnimationStart(Animator var1) {
                     ArrayList var4 = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                     int var2 = var4.size();

                     for(int var3 = 0; var3 < var2; ++var3) {
                        ((Animatable2Compat.AnimationCallback)var4.get(var3)).onAnimationStart(AnimatedVectorDrawableCompat.this);
                     }

                  }
               };
            }

            this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
         }
      }
   }

   public void setAlpha(int var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setAlpha(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setAlpha(var1);
      }
   }

   public void setAutoMirrored(boolean var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setAutoMirrored(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(var1);
      }
   }

   public void setColorFilter(ColorFilter var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setColorFilter(var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setColorFilter(var1);
      }
   }

   public void setTint(int var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setTint(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTint(var1);
      }
   }

   public void setTintList(ColorStateList var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setTintList(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTintList(var1);
      }
   }

   public void setTintMode(Mode var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setTintMode(this.mDelegateDrawable, var1);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setTintMode(var1);
      }
   }

   public boolean setVisible(boolean var1, boolean var2) {
      if (this.mDelegateDrawable != null) {
         return this.mDelegateDrawable.setVisible(var1, var2);
      } else {
         this.mAnimatedVectorState.mVectorDrawable.setVisible(var1, var2);
         return super.setVisible(var1, var2);
      }
   }

   public void start() {
      if (this.mDelegateDrawable != null) {
         ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
      } else if (!this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
         this.mAnimatedVectorState.mAnimatorSet.start();
         this.invalidateSelf();
      }
   }

   public void stop() {
      if (this.mDelegateDrawable != null) {
         ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
      } else {
         this.mAnimatedVectorState.mAnimatorSet.end();
      }
   }

   public boolean unregisterAnimationCallback(@NonNull Animatable2Compat.AnimationCallback var1) {
      if (this.mDelegateDrawable != null) {
         unregisterPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, var1);
      }

      if (this.mAnimationCallbacks != null && var1 != null) {
         boolean var2 = this.mAnimationCallbacks.remove(var1);
         if (this.mAnimationCallbacks.size() == 0) {
            this.removeAnimatorSetListener();
         }

         return var2;
      } else {
         return false;
      }
   }

   private static class AnimatedVectorDrawableCompatState extends ConstantState {
      AnimatorSet mAnimatorSet;
      private ArrayList mAnimators;
      int mChangingConfigurations;
      ArrayMap mTargetNameMap;
      VectorDrawableCompat mVectorDrawable;

      public AnimatedVectorDrawableCompatState(Context var1, AnimatedVectorDrawableCompat.AnimatedVectorDrawableCompatState var2, Callback var3, Resources var4) {
         if (var2 != null) {
            this.mChangingConfigurations = var2.mChangingConfigurations;
            VectorDrawableCompat var7 = var2.mVectorDrawable;
            int var5 = 0;
            if (var7 != null) {
               ConstantState var8 = var2.mVectorDrawable.getConstantState();
               if (var4 != null) {
                  this.mVectorDrawable = (VectorDrawableCompat)var8.newDrawable(var4);
               } else {
                  this.mVectorDrawable = (VectorDrawableCompat)var8.newDrawable();
               }

               this.mVectorDrawable = (VectorDrawableCompat)this.mVectorDrawable.mutate();
               this.mVectorDrawable.setCallback(var3);
               this.mVectorDrawable.setBounds(var2.mVectorDrawable.getBounds());
               this.mVectorDrawable.setAllowCaching(false);
            }

            if (var2.mAnimators != null) {
               int var6 = var2.mAnimators.size();
               this.mAnimators = new ArrayList(var6);

               for(this.mTargetNameMap = new ArrayMap(var6); var5 < var6; ++var5) {
                  Animator var10 = (Animator)var2.mAnimators.get(var5);
                  Animator var9 = var10.clone();
                  String var11 = (String)var2.mTargetNameMap.get(var10);
                  var9.setTarget(this.mVectorDrawable.getTargetByName(var11));
                  this.mAnimators.add(var9);
                  this.mTargetNameMap.put(var9, var11);
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

   @RequiresApi(24)
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