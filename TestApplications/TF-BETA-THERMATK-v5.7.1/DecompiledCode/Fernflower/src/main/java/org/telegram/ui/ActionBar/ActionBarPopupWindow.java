package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.Keep;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.LayoutHelper;

public class ActionBarPopupWindow extends PopupWindow {
   private static final OnScrollChangedListener NOP;
   private static final boolean allowAnimation;
   private static DecelerateInterpolator decelerateInterpolator;
   private static Method layoutInScreenMethod;
   private static final Field superListenerField;
   private boolean animationEnabled;
   private int dismissAnimationDuration;
   private OnScrollChangedListener mSuperScrollListener;
   private ViewTreeObserver mViewTreeObserver;
   private AnimatorSet windowAnimatorSet;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 18) {
         var0 = true;
      } else {
         var0 = false;
      }

      allowAnimation = var0;
      decelerateInterpolator = new DecelerateInterpolator();
      Field var1 = null;

      label36: {
         boolean var10001;
         Field var2;
         try {
            var2 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
         } catch (NoSuchFieldException var4) {
            var10001 = false;
            break label36;
         }

         var1 = var2;

         try {
            var2.setAccessible(true);
         } catch (NoSuchFieldException var3) {
            var10001 = false;
            break label36;
         }

         var1 = var2;
      }

      superListenerField = var1;
      NOP = _$$Lambda$ActionBarPopupWindow$u1KuFqdl4RQdFf_yVDBUWk_fHAc.INSTANCE;
   }

   public ActionBarPopupWindow() {
      this.animationEnabled = allowAnimation;
      this.dismissAnimationDuration = 150;
      this.init();
   }

   public ActionBarPopupWindow(int var1, int var2) {
      super(var1, var2);
      this.animationEnabled = allowAnimation;
      this.dismissAnimationDuration = 150;
      this.init();
   }

   public ActionBarPopupWindow(Context var1) {
      super(var1);
      this.animationEnabled = allowAnimation;
      this.dismissAnimationDuration = 150;
      this.init();
   }

   public ActionBarPopupWindow(View var1) {
      super(var1);
      this.animationEnabled = allowAnimation;
      this.dismissAnimationDuration = 150;
      this.init();
   }

   public ActionBarPopupWindow(View var1, int var2, int var3) {
      super(var1, var2, var3);
      this.animationEnabled = allowAnimation;
      this.dismissAnimationDuration = 150;
      this.init();
   }

   public ActionBarPopupWindow(View var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
      this.animationEnabled = allowAnimation;
      this.dismissAnimationDuration = 150;
      this.init();
   }

   private void init() {
      Field var1 = superListenerField;
      if (var1 != null) {
         try {
            this.mSuperScrollListener = (OnScrollChangedListener)var1.get(this);
            superListenerField.set(this, NOP);
         } catch (Exception var2) {
            this.mSuperScrollListener = null;
         }
      }

   }

   // $FF: synthetic method
   static void lambda$static$0() {
   }

   private void registerListener(View var1) {
      if (this.mSuperScrollListener != null) {
         ViewTreeObserver var3;
         if (var1.getWindowToken() != null) {
            var3 = var1.getViewTreeObserver();
         } else {
            var3 = null;
         }

         ViewTreeObserver var2 = this.mViewTreeObserver;
         if (var3 != var2) {
            if (var2 != null && var2.isAlive()) {
               this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
            }

            this.mViewTreeObserver = var3;
            if (var3 != null) {
               var3.addOnScrollChangedListener(this.mSuperScrollListener);
            }
         }
      }

   }

   private void unregisterListener() {
      if (this.mSuperScrollListener != null) {
         ViewTreeObserver var1 = this.mViewTreeObserver;
         if (var1 != null) {
            if (var1.isAlive()) {
               this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
            }

            this.mViewTreeObserver = null;
         }
      }

   }

   public void dismiss() {
      this.dismiss(true);
   }

   public void dismiss(boolean var1) {
      this.setFocusable(false);
      if (this.animationEnabled && var1) {
         AnimatorSet var2 = this.windowAnimatorSet;
         if (var2 != null) {
            var2.cancel();
         }

         ActionBarPopupWindow.ActionBarPopupWindowLayout var3 = (ActionBarPopupWindow.ActionBarPopupWindowLayout)this.getContentView();
         this.windowAnimatorSet = new AnimatorSet();
         var2 = this.windowAnimatorSet;
         float var4;
         if (var3.showedFromBotton) {
            var4 = 5.0F;
         } else {
            var4 = -5.0F;
         }

         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, "translationY", new float[]{(float)AndroidUtilities.dp(var4)}), ObjectAnimator.ofFloat(var3, "alpha", new float[]{0.0F})});
         this.windowAnimatorSet.setDuration((long)this.dismissAnimationDuration);
         this.windowAnimatorSet.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator var1) {
               this.onAnimationEnd(var1);
            }

            public void onAnimationEnd(Animator var1) {
               ActionBarPopupWindow.this.windowAnimatorSet = null;
               ActionBarPopupWindow.this.setFocusable(false);

               try {
                  ActionBarPopupWindow.super.dismiss();
               } catch (Exception var2) {
               }

               ActionBarPopupWindow.this.unregisterListener();
            }

            public void onAnimationRepeat(Animator var1) {
            }

            public void onAnimationStart(Animator var1) {
            }
         });
         this.windowAnimatorSet.start();
      } else {
         try {
            super.dismiss();
         } catch (Exception var5) {
         }

         this.unregisterListener();
      }

   }

   public void setAnimationEnabled(boolean var1) {
      this.animationEnabled = var1;
   }

   public void setDismissAnimationDuration(int var1) {
      this.dismissAnimationDuration = var1;
   }

   public void setLayoutInScreen(boolean var1) {
      try {
         if (layoutInScreenMethod == null) {
            layoutInScreenMethod = PopupWindow.class.getDeclaredMethod("setLayoutInScreenEnabled", Boolean.TYPE);
            layoutInScreenMethod.setAccessible(true);
         }

         layoutInScreenMethod.invoke(this, true);
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   public void showAsDropDown(View var1, int var2, int var3) {
      try {
         super.showAsDropDown(var1, var2, var3);
         this.registerListener(var1);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   public void showAtLocation(View var1, int var2, int var3, int var4) {
      super.showAtLocation(var1, var2, var3, var4);
      this.unregisterListener();
   }

   public void startAnimation() {
      if (this.animationEnabled) {
         if (this.windowAnimatorSet != null) {
            return;
         }

         ActionBarPopupWindow.ActionBarPopupWindowLayout var1 = (ActionBarPopupWindow.ActionBarPopupWindowLayout)this.getContentView();
         var1.setTranslationY(0.0F);
         var1.setAlpha(1.0F);
         var1.setPivotX((float)var1.getMeasuredWidth());
         var1.setPivotY(0.0F);
         int var2 = var1.getItemsCount();
         var1.positions.clear();
         int var3 = 0;

         int var4;
         for(var4 = 0; var3 < var2; ++var3) {
            View var5 = var1.getItemAt(var3);
            if (var5.getVisibility() == 0) {
               var1.positions.put(var5, var4);
               var5.setAlpha(0.0F);
               ++var4;
            }
         }

         if (var1.showedFromBotton) {
            var1.lastStartedChild = var2 - 1;
         } else {
            var1.lastStartedChild = 0;
         }

         this.windowAnimatorSet = new AnimatorSet();
         this.windowAnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(var1, "backScaleY", new float[]{0.0F, 1.0F}), ObjectAnimator.ofInt(var1, "backAlpha", new int[]{0, 255})});
         this.windowAnimatorSet.setDuration((long)(var4 * 16 + 150));
         this.windowAnimatorSet.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator var1) {
               this.onAnimationEnd(var1);
            }

            public void onAnimationEnd(Animator var1) {
               ActionBarPopupWindow.this.windowAnimatorSet = null;
            }

            public void onAnimationRepeat(Animator var1) {
            }

            public void onAnimationStart(Animator var1) {
            }
         });
         this.windowAnimatorSet.start();
      }

   }

   public void update(View var1, int var2, int var3) {
      super.update(var1, var2, var3);
      this.registerListener(var1);
   }

   public void update(View var1, int var2, int var3, int var4, int var5) {
      super.update(var1, var2, var3, var4, var5);
      this.registerListener(var1);
   }

   public static class ActionBarPopupWindowLayout extends FrameLayout {
      private boolean animationEnabled;
      private int backAlpha = 255;
      private float backScaleX = 1.0F;
      private float backScaleY = 1.0F;
      protected Drawable backgroundDrawable;
      private int lastStartedChild = 0;
      protected LinearLayout linearLayout;
      private ActionBarPopupWindow.OnDispatchKeyEventListener mOnDispatchKeyEventListener;
      private HashMap positions;
      private ScrollView scrollView;
      private boolean showedFromBotton;

      public ActionBarPopupWindowLayout(Context var1) {
         super(var1);
         this.animationEnabled = ActionBarPopupWindow.allowAnimation;
         this.positions = new HashMap();
         this.backgroundDrawable = this.getResources().getDrawable(2131165777).mutate();
         this.backgroundDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuBackground"), Mode.MULTIPLY));
         this.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F));
         this.setWillNotDraw(false);

         try {
            ScrollView var2 = new ScrollView(var1);
            this.scrollView = var2;
            this.scrollView.setVerticalScrollBarEnabled(false);
            this.addView(this.scrollView, LayoutHelper.createFrame(-2, -2.0F));
         } catch (Throwable var3) {
            FileLog.e(var3);
         }

         this.linearLayout = new LinearLayout(var1);
         this.linearLayout.setOrientation(1);
         ScrollView var4 = this.scrollView;
         if (var4 != null) {
            var4.addView(this.linearLayout, new LayoutParams(-2, -2));
         } else {
            this.addView(this.linearLayout, LayoutHelper.createFrame(-2, -2.0F));
         }

      }

      private void startChildAnimation(View var1) {
         if (this.animationEnabled) {
            AnimatorSet var2 = new AnimatorSet();
            ObjectAnimator var3 = ObjectAnimator.ofFloat(var1, "alpha", new float[]{0.0F, 1.0F});
            float var4;
            if (this.showedFromBotton) {
               var4 = 6.0F;
            } else {
               var4 = -6.0F;
            }

            var2.playTogether(new Animator[]{var3, ObjectAnimator.ofFloat(var1, "translationY", new float[]{(float)AndroidUtilities.dp(var4), 0.0F})});
            var2.setDuration(180L);
            var2.setInterpolator(ActionBarPopupWindow.decelerateInterpolator);
            var2.start();
         }

      }

      public void addView(View var1) {
         this.linearLayout.addView(var1);
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         ActionBarPopupWindow.OnDispatchKeyEventListener var2 = this.mOnDispatchKeyEventListener;
         if (var2 != null) {
            var2.onDispatchKeyEvent(var1);
         }

         return super.dispatchKeyEvent(var1);
      }

      @Keep
      public int getBackAlpha() {
         return this.backAlpha;
      }

      public float getBackScaleX() {
         return this.backScaleX;
      }

      public float getBackScaleY() {
         return this.backScaleY;
      }

      public View getItemAt(int var1) {
         return this.linearLayout.getChildAt(var1);
      }

      public int getItemsCount() {
         return this.linearLayout.getChildCount();
      }

      protected void onDraw(Canvas var1) {
         Drawable var2 = this.backgroundDrawable;
         if (var2 != null) {
            var2.setAlpha(this.backAlpha);
            this.getMeasuredHeight();
            if (this.showedFromBotton) {
               this.backgroundDrawable.setBounds(0, (int)((float)this.getMeasuredHeight() * (1.0F - this.backScaleY)), (int)((float)this.getMeasuredWidth() * this.backScaleX), this.getMeasuredHeight());
            } else {
               this.backgroundDrawable.setBounds(0, 0, (int)((float)this.getMeasuredWidth() * this.backScaleX), (int)((float)this.getMeasuredHeight() * this.backScaleY));
            }

            this.backgroundDrawable.draw(var1);
         }

      }

      public void removeInnerViews() {
         this.linearLayout.removeAllViews();
      }

      public void scrollToTop() {
         ScrollView var1 = this.scrollView;
         if (var1 != null) {
            var1.scrollTo(0, 0);
         }

      }

      public void setAnimationEnabled(boolean var1) {
         this.animationEnabled = var1;
      }

      @Keep
      public void setBackAlpha(int var1) {
         this.backAlpha = var1;
      }

      @Keep
      public void setBackScaleX(float var1) {
         this.backScaleX = var1;
         this.invalidate();
      }

      @Keep
      public void setBackScaleY(float var1) {
         this.backScaleY = var1;
         if (this.animationEnabled) {
            int var2 = this.getItemsCount();

            int var3;
            for(var3 = 0; var3 < var2; ++var3) {
               this.getItemAt(var3).getVisibility();
            }

            int var4 = this.getMeasuredHeight() - AndroidUtilities.dp(16.0F);
            if (this.showedFromBotton) {
               for(var3 = this.lastStartedChild; var3 >= 0; --var3) {
                  View var5 = this.getItemAt(var3);
                  if (var5.getVisibility() == 0) {
                     Integer var6 = (Integer)this.positions.get(var5);
                     if (var6 != null && (float)(var4 - (var6 * AndroidUtilities.dp(48.0F) + AndroidUtilities.dp(32.0F))) > (float)var4 * var1) {
                        break;
                     }

                     this.lastStartedChild = var3 - 1;
                     this.startChildAnimation(var5);
                  }
               }
            } else {
               for(var3 = this.lastStartedChild; var3 < var2; ++var3) {
                  View var8 = this.getItemAt(var3);
                  if (var8.getVisibility() == 0) {
                     Integer var7 = (Integer)this.positions.get(var8);
                     if (var7 != null && (float)((var7 + 1) * AndroidUtilities.dp(48.0F) - AndroidUtilities.dp(24.0F)) > (float)var4 * var1) {
                        break;
                     }

                     this.lastStartedChild = var3 + 1;
                     this.startChildAnimation(var8);
                  }
               }
            }
         }

         this.invalidate();
      }

      public void setBackgroundDrawable(Drawable var1) {
         this.backgroundDrawable = var1;
      }

      public void setDispatchKeyEventListener(ActionBarPopupWindow.OnDispatchKeyEventListener var1) {
         this.mOnDispatchKeyEventListener = var1;
      }

      public void setShowedFromBotton(boolean var1) {
         this.showedFromBotton = var1;
      }
   }

   public interface OnDispatchKeyEventListener {
      void onDispatchKeyEvent(KeyEvent var1);
   }
}
