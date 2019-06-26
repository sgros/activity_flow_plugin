package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;

public class ActionBarLayout extends FrameLayout {
   private static Drawable headerShadowDrawable;
   private static Drawable layerShadowDrawable;
   private static Paint scrimPaint;
   private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
   private int[][] animateEndColors = new int[2][];
   private Theme.ThemeInfo animateSetThemeAfterAnimation;
   private boolean animateSetThemeNightAfterAnimation;
   private int[][] animateStartColors = new int[2][];
   private boolean animateThemeAfterAnimation;
   protected boolean animationInProgress;
   private float animationProgress;
   private Runnable animationRunnable;
   private View backgroundView;
   private boolean beginTrackingSent;
   private ActionBarLayout.LinearLayoutContainer containerView;
   private ActionBarLayout.LinearLayoutContainer containerViewBack;
   private ActionBar currentActionBar;
   private AnimatorSet currentAnimation;
   private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1.5F);
   private Runnable delayedOpenAnimationRunnable;
   private ActionBarLayout.ActionBarLayoutDelegate delegate;
   private DrawerLayoutContainer drawerLayoutContainer;
   public ArrayList fragmentsStack;
   private boolean inActionMode;
   private boolean inPreviewMode;
   public float innerTranslationX;
   private long lastFrameTime;
   private boolean maybeStartTracking;
   private Runnable onCloseAnimationEndRunnable;
   private Runnable onOpenAnimationEndRunnable;
   private Runnable overlayAction;
   protected Activity parentActivity;
   private ColorDrawable previewBackgroundDrawable;
   private boolean rebuildAfterAnimation;
   private boolean rebuildLastAfterAnimation;
   private boolean removeActionBarExtraHeight;
   private boolean showLastAfterAnimation;
   protected boolean startedTracking;
   private int startedTrackingPointerId;
   private int startedTrackingX;
   private int startedTrackingY;
   private float themeAnimationValue;
   private ThemeDescription.ThemeDescriptionDelegate[] themeAnimatorDelegate = new ThemeDescription.ThemeDescriptionDelegate[2];
   private ThemeDescription[][] themeAnimatorDescriptions = new ThemeDescription[2][];
   private AnimatorSet themeAnimatorSet;
   private String titleOverlayText;
   private int titleOverlayTextId;
   private boolean transitionAnimationInProgress;
   private boolean transitionAnimationPreviewMode;
   private long transitionAnimationStartTime;
   private boolean useAlphaAnimations;
   private VelocityTracker velocityTracker;
   private Runnable waitingForKeyboardCloseRunnable;

   public ActionBarLayout(Context var1) {
      super(var1);
      this.parentActivity = (Activity)var1;
      if (layerShadowDrawable == null) {
         layerShadowDrawable = this.getResources().getDrawable(2131165521);
         headerShadowDrawable = this.getResources().getDrawable(2131165407).mutate();
         scrimPaint = new Paint();
      }

   }

   private void checkNeedRebuild() {
      if (this.rebuildAfterAnimation) {
         this.rebuildAllFragmentViews(this.rebuildLastAfterAnimation, this.showLastAfterAnimation);
         this.rebuildAfterAnimation = false;
      } else if (this.animateThemeAfterAnimation) {
         this.animateThemedValues(this.animateSetThemeAfterAnimation, this.animateSetThemeNightAfterAnimation);
         this.animateSetThemeAfterAnimation = null;
         this.animateThemeAfterAnimation = false;
      }

   }

   private void closeLastFragmentInternalRemoveOld(BaseFragment var1) {
      var1.onPause();
      var1.onFragmentDestroy();
      var1.setParentLayout((ActionBarLayout)null);
      this.fragmentsStack.remove(var1);
      this.containerViewBack.setVisibility(8);
      this.bringChildToFront(this.containerView);
   }

   // $FF: synthetic method
   static void lambda$presentFragment$0(BaseFragment var0) {
      var0.onTransitionAnimationEnd(true, false);
      var0.onBecomeFullyVisible();
   }

   private void onAnimationEndCheck(boolean var1) {
      this.onCloseAnimationEnd();
      this.onOpenAnimationEnd();
      Runnable var2 = this.waitingForKeyboardCloseRunnable;
      if (var2 != null) {
         AndroidUtilities.cancelRunOnUIThread(var2);
         this.waitingForKeyboardCloseRunnable = null;
      }

      AnimatorSet var3 = this.currentAnimation;
      if (var3 != null) {
         if (var1) {
            var3.cancel();
         }

         this.currentAnimation = null;
      }

      var2 = this.animationRunnable;
      if (var2 != null) {
         AndroidUtilities.cancelRunOnUIThread(var2);
         this.animationRunnable = null;
      }

      this.setAlpha(1.0F);
      this.containerView.setAlpha(1.0F);
      this.containerView.setScaleX(1.0F);
      this.containerView.setScaleY(1.0F);
      this.containerViewBack.setAlpha(1.0F);
      this.containerViewBack.setScaleX(1.0F);
      this.containerViewBack.setScaleY(1.0F);
   }

   private void onCloseAnimationEnd() {
      if (this.transitionAnimationInProgress) {
         Runnable var1 = this.onCloseAnimationEndRunnable;
         if (var1 != null) {
            this.transitionAnimationInProgress = false;
            this.transitionAnimationPreviewMode = false;
            this.transitionAnimationStartTime = 0L;
            var1.run();
            this.onCloseAnimationEndRunnable = null;
            this.checkNeedRebuild();
         }
      }

   }

   private void onOpenAnimationEnd() {
      if (this.transitionAnimationInProgress) {
         Runnable var1 = this.onOpenAnimationEndRunnable;
         if (var1 != null) {
            this.transitionAnimationInProgress = false;
            this.transitionAnimationPreviewMode = false;
            this.transitionAnimationStartTime = 0L;
            var1.run();
            this.onOpenAnimationEndRunnable = null;
            this.checkNeedRebuild();
         }
      }

   }

   private void onSlideAnimationEnd(boolean var1) {
      ArrayList var2;
      BaseFragment var4;
      if (!var1) {
         if (this.fragmentsStack.size() < 2) {
            return;
         }

         var2 = this.fragmentsStack;
         var4 = (BaseFragment)var2.get(var2.size() - 1);
         var4.onPause();
         var4.onFragmentDestroy();
         var4.setParentLayout((ActionBarLayout)null);
         var2 = this.fragmentsStack;
         var2.remove(var2.size() - 1);
         ActionBarLayout.LinearLayoutContainer var5 = this.containerView;
         this.containerView = this.containerViewBack;
         this.containerViewBack = var5;
         this.bringChildToFront(this.containerView);
         var2 = this.fragmentsStack;
         var4 = (BaseFragment)var2.get(var2.size() - 1);
         this.currentActionBar = var4.actionBar;
         var4.onResume();
         var4.onBecomeFullyVisible();
      } else if (this.fragmentsStack.size() >= 2) {
         var2 = this.fragmentsStack;
         var4 = (BaseFragment)var2.get(var2.size() - 2);
         var4.onPause();
         View var3 = var4.fragmentView;
         ViewGroup var6;
         if (var3 != null) {
            var6 = (ViewGroup)var3.getParent();
            if (var6 != null) {
               var4.onRemoveFromParent();
               var6.removeView(var4.fragmentView);
            }
         }

         ActionBar var7 = var4.actionBar;
         if (var7 != null && var7.getAddToContainer()) {
            var6 = (ViewGroup)var4.actionBar.getParent();
            if (var6 != null) {
               var6.removeView(var4.actionBar);
            }
         }
      }

      this.containerViewBack.setVisibility(8);
      this.startedTracking = false;
      this.animationInProgress = false;
      this.containerView.setTranslationX(0.0F);
      this.containerViewBack.setTranslationX(0.0F);
      this.setInnerTranslationX(0.0F);
   }

   private void prepareForMoving(MotionEvent var1) {
      this.maybeStartTracking = false;
      this.startedTracking = true;
      this.startedTrackingX = (int)var1.getX();
      this.containerViewBack.setVisibility(0);
      this.beginTrackingSent = false;
      ArrayList var4 = this.fragmentsStack;
      BaseFragment var2 = (BaseFragment)var4.get(var4.size() - 2);
      View var3 = var2.fragmentView;
      View var5 = var3;
      if (var3 == null) {
         var5 = var2.createView(this.parentActivity);
      }

      ViewGroup var6 = (ViewGroup)var5.getParent();
      if (var6 != null) {
         var2.onRemoveFromParent();
         var6.removeView(var5);
      }

      ActionBar var7 = var2.actionBar;
      if (var7 != null && var7.getAddToContainer()) {
         var6 = (ViewGroup)var2.actionBar.getParent();
         if (var6 != null) {
            var6.removeView(var2.actionBar);
         }

         if (this.removeActionBarExtraHeight) {
            var2.actionBar.setOccupyStatusBar(false);
         }

         this.containerViewBack.addView(var2.actionBar);
         var2.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
      }

      this.containerViewBack.addView(var5);
      LayoutParams var8 = (LayoutParams)var5.getLayoutParams();
      var8.width = -1;
      var8.height = -1;
      var8.leftMargin = 0;
      var8.rightMargin = 0;
      var8.bottomMargin = 0;
      var8.topMargin = 0;
      var5.setLayoutParams(var8);
      if (!var2.hasOwnBackground && var5.getBackground() == null) {
         var5.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      }

      var2.onResume();
   }

   private void presentFragmentInternalRemoveOld(boolean var1, BaseFragment var2) {
      if (var2 != null) {
         var2.onBecomeFullyHidden();
         var2.onPause();
         if (var1) {
            var2.onFragmentDestroy();
            var2.setParentLayout((ActionBarLayout)null);
            this.fragmentsStack.remove(var2);
         } else {
            View var3 = var2.fragmentView;
            ViewGroup var4;
            if (var3 != null) {
               var4 = (ViewGroup)var3.getParent();
               if (var4 != null) {
                  var2.onRemoveFromParent();
                  var4.removeView(var2.fragmentView);
               }
            }

            ActionBar var5 = var2.actionBar;
            if (var5 != null && var5.getAddToContainer()) {
               var4 = (ViewGroup)var2.actionBar.getParent();
               if (var4 != null) {
                  var4.removeView(var2.actionBar);
               }
            }
         }

         this.containerViewBack.setVisibility(8);
      }
   }

   private void removeFragmentFromStackInternal(BaseFragment var1) {
      var1.onPause();
      var1.onFragmentDestroy();
      var1.setParentLayout((ActionBarLayout)null);
      this.fragmentsStack.remove(var1);
   }

   private void startLayoutAnimation(final boolean var1, final boolean var2, final boolean var3) {
      if (var2) {
         this.animationProgress = 0.0F;
         this.lastFrameTime = System.nanoTime() / 1000000L;
      }

      Runnable var4 = new Runnable() {
         public void run() {
            if (ActionBarLayout.this.animationRunnable == this) {
               ActionBarLayout.this.animationRunnable = null;
               if (var2) {
                  ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
               }

               long var1x = System.nanoTime() / 1000000L;
               long var3x = var1x - ActionBarLayout.this.lastFrameTime;
               long var5 = var3x;
               if (var3x > 18L) {
                  var5 = 18L;
               }

               ActionBarLayout.this.lastFrameTime = var1x;
               ActionBarLayout var7 = ActionBarLayout.this;
               var7.animationProgress = var7.animationProgress + (float)var5 / 150.0F;
               if (ActionBarLayout.this.animationProgress > 1.0F) {
                  ActionBarLayout.this.animationProgress = 1.0F;
               }

               float var8 = ActionBarLayout.this.decelerateInterpolator.getInterpolation(ActionBarLayout.this.animationProgress);
               float var9;
               ActionBarLayout.LinearLayoutContainer var10;
               if (var1) {
                  ActionBarLayout.this.containerView.setAlpha(var8);
                  if (var3) {
                     var10 = ActionBarLayout.this.containerView;
                     var9 = 0.1F * var8 + 0.9F;
                     var10.setScaleX(var9);
                     ActionBarLayout.this.containerView.setScaleY(var9);
                     ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int)(128.0F * var8));
                     Theme.moveUpDrawable.setAlpha((int)(var8 * 255.0F));
                     ActionBarLayout.this.containerView.invalidate();
                     ActionBarLayout.this.invalidate();
                  } else {
                     ActionBarLayout.this.containerView.setTranslationX((float)AndroidUtilities.dp(48.0F) * (1.0F - var8));
                  }
               } else {
                  var10 = ActionBarLayout.this.containerViewBack;
                  var9 = 1.0F - var8;
                  var10.setAlpha(var9);
                  if (var3) {
                     var10 = ActionBarLayout.this.containerViewBack;
                     var8 = 0.1F * var9 + 0.9F;
                     var10.setScaleX(var8);
                     ActionBarLayout.this.containerViewBack.setScaleY(var8);
                     ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int)(128.0F * var9));
                     Theme.moveUpDrawable.setAlpha((int)(var9 * 255.0F));
                     ActionBarLayout.this.containerView.invalidate();
                     ActionBarLayout.this.invalidate();
                  } else {
                     ActionBarLayout.this.containerViewBack.setTranslationX((float)AndroidUtilities.dp(48.0F) * var8);
                  }
               }

               if (ActionBarLayout.this.animationProgress < 1.0F) {
                  ActionBarLayout.this.startLayoutAnimation(var1, false, var3);
               } else {
                  ActionBarLayout.this.onAnimationEndCheck(false);
               }

            }
         }
      };
      this.animationRunnable = var4;
      AndroidUtilities.runOnUIThread(var4);
   }

   public boolean addFragmentToStack(BaseFragment var1) {
      return this.addFragmentToStack(var1, -1);
   }

   public boolean addFragmentToStack(BaseFragment var1, int var2) {
      ActionBarLayout.ActionBarLayoutDelegate var3 = this.delegate;
      if ((var3 == null || var3.needAddFragmentToStack(var1, this)) && var1.onFragmentCreate()) {
         var1.setParentLayout(this);
         if (var2 == -1) {
            if (!this.fragmentsStack.isEmpty()) {
               ArrayList var5 = this.fragmentsStack;
               BaseFragment var6 = (BaseFragment)var5.get(var5.size() - 1);
               var6.onPause();
               ActionBar var4 = var6.actionBar;
               ViewGroup var7;
               if (var4 != null && var4.getAddToContainer()) {
                  var7 = (ViewGroup)var6.actionBar.getParent();
                  if (var7 != null) {
                     var7.removeView(var6.actionBar);
                  }
               }

               View var8 = var6.fragmentView;
               if (var8 != null) {
                  var7 = (ViewGroup)var8.getParent();
                  if (var7 != null) {
                     var6.onRemoveFromParent();
                     var7.removeView(var6.fragmentView);
                  }
               }
            }

            this.fragmentsStack.add(var1);
         } else {
            this.fragmentsStack.add(var2, var1);
         }

         return true;
      } else {
         return false;
      }
   }

   public void animateThemedValues(Theme.ThemeInfo var1, boolean var2) {
      if (!this.transitionAnimationInProgress && !this.startedTracking) {
         AnimatorSet var3 = this.themeAnimatorSet;
         if (var3 != null) {
            var3.cancel();
            this.themeAnimatorSet = null;
         }

         int var4 = 0;

         boolean var5;
         int var14;
         label92:
         for(var5 = false; var4 < 2; ++var4) {
            BaseFragment var9;
            if (var4 == 0) {
               var9 = this.getLastFragment();
            } else {
               if (!this.inPreviewMode && !this.transitionAnimationPreviewMode || this.fragmentsStack.size() <= 1) {
                  this.themeAnimatorDescriptions[var4] = null;
                  this.animateStartColors[var4] = null;
                  this.animateEndColors[var4] = null;
                  this.themeAnimatorDelegate[var4] = null;
                  continue;
               }

               ArrayList var10 = this.fragmentsStack;
               var9 = (BaseFragment)var10.get(var10.size() - 2);
            }

            if (var9 != null) {
               this.themeAnimatorDescriptions[var4] = var9.getThemeDescriptions();
               this.animateStartColors[var4] = new int[this.themeAnimatorDescriptions[var4].length];
               var14 = 0;

               while(true) {
                  ThemeDescription[][] var12 = this.themeAnimatorDescriptions;
                  if (var14 >= var12[var4].length) {
                     if (var4 == 0) {
                        Theme.applyTheme(var1, var2);
                     }

                     this.animateEndColors[var4] = new int[this.themeAnimatorDescriptions[var4].length];
                     var14 = 0;

                     while(true) {
                        var12 = this.themeAnimatorDescriptions;
                        if (var14 >= var12[var4].length) {
                           var5 = true;
                           continue label92;
                        }

                        this.animateEndColors[var4][var14] = var12[var4][var14].getSetColor();
                        ++var14;
                     }
                  }

                  this.animateStartColors[var4][var14] = var12[var4][var14].getSetColor();
                  ThemeDescription.ThemeDescriptionDelegate var6 = this.themeAnimatorDescriptions[var4][var14].setDelegateDisabled();
                  ThemeDescription.ThemeDescriptionDelegate[] var13 = this.themeAnimatorDelegate;
                  if (var13[var4] == null && var6 != null) {
                     var13[var4] = var6;
                  }

                  ++var14;
               }
            }
         }

         if (var5) {
            this.themeAnimatorSet = new AnimatorSet();
            this.themeAnimatorSet.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1) {
                  if (var1.equals(ActionBarLayout.this.themeAnimatorSet)) {
                     for(int var2 = 0; var2 < 2; ++var2) {
                        ActionBarLayout.this.themeAnimatorDescriptions[var2] = null;
                        ActionBarLayout.this.animateStartColors[var2] = null;
                        ActionBarLayout.this.animateEndColors[var2] = null;
                        ActionBarLayout.this.themeAnimatorDelegate[var2] = null;
                     }

                     Theme.setAnimatingColor(false);
                     ActionBarLayout.this.themeAnimatorSet = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(ActionBarLayout.this.themeAnimatorSet)) {
                     for(int var2 = 0; var2 < 2; ++var2) {
                        ActionBarLayout.this.themeAnimatorDescriptions[var2] = null;
                        ActionBarLayout.this.animateStartColors[var2] = null;
                        ActionBarLayout.this.animateEndColors[var2] = null;
                        ActionBarLayout.this.themeAnimatorDelegate[var2] = null;
                     }

                     Theme.setAnimatingColor(false);
                     ActionBarLayout.this.themeAnimatorSet = null;
                  }

               }
            });
            int var7 = this.fragmentsStack.size();
            byte var11;
            if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
               var11 = 1;
            } else {
               var11 = 2;
            }

            for(var14 = 0; var14 < var7 - var11; ++var14) {
               BaseFragment var8 = (BaseFragment)this.fragmentsStack.get(var14);
               var8.clearViews();
               var8.setParentLayout(this);
            }

            Theme.setAnimatingColor(true);
            this.themeAnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "themeAnimationValue", new float[]{0.0F, 1.0F})});
            this.themeAnimatorSet.setDuration(200L);
            this.themeAnimatorSet.start();
         }

      } else {
         this.animateThemeAfterAnimation = true;
         this.animateSetThemeAfterAnimation = var1;
         this.animateSetThemeNightAfterAnimation = var2;
      }
   }

   public boolean checkTransitionAnimation() {
      if (this.transitionAnimationPreviewMode) {
         return false;
      } else {
         if (this.transitionAnimationInProgress && this.transitionAnimationStartTime < System.currentTimeMillis() - 1500L) {
            this.onAnimationEndCheck(true);
         }

         return this.transitionAnimationInProgress;
      }
   }

   public void closeLastFragment(boolean var1) {
      ActionBarLayout.ActionBarLayoutDelegate var2 = this.delegate;
      if ((var2 == null || var2.needCloseLastFragment(this)) && !this.checkTransitionAnimation() && !this.fragmentsStack.isEmpty()) {
         if (this.parentActivity.getCurrentFocus() != null) {
            AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
         }

         this.setInnerTranslationX(0.0F);
         boolean var3;
         if (this.inPreviewMode || this.transitionAnimationPreviewMode || var1 && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            var3 = true;
         } else {
            var3 = false;
         }

         ArrayList var10 = this.fragmentsStack;
         BaseFragment var4 = (BaseFragment)var10.get(var10.size() - 1);
         int var5 = this.fragmentsStack.size();
         Object var6 = null;
         BaseFragment var11;
         if (var5 > 1) {
            var10 = this.fragmentsStack;
            var11 = (BaseFragment)var10.get(var10.size() - 2);
         } else {
            var11 = null;
         }

         View var14;
         if (var11 != null) {
            ActionBarLayout.LinearLayoutContainer var7 = this.containerView;
            this.containerView = this.containerViewBack;
            this.containerViewBack = var7;
            var11.setParentLayout(this);
            View var8 = var11.fragmentView;
            var14 = var8;
            if (var8 == null) {
               var14 = var11.createView(this.parentActivity);
            }

            if (!this.inPreviewMode) {
               this.containerView.setVisibility(0);
               ActionBar var15 = var11.actionBar;
               ViewGroup var16;
               if (var15 != null && var15.getAddToContainer()) {
                  if (this.removeActionBarExtraHeight) {
                     var11.actionBar.setOccupyStatusBar(false);
                  }

                  var16 = (ViewGroup)var11.actionBar.getParent();
                  if (var16 != null) {
                     var16.removeView(var11.actionBar);
                  }

                  this.containerView.addView(var11.actionBar);
                  var11.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
               }

               var16 = (ViewGroup)var14.getParent();
               if (var16 != null) {
                  var11.onRemoveFromParent();

                  try {
                     var16.removeView(var14);
                  } catch (Exception var9) {
                     FileLog.e((Throwable)var9);
                  }
               }

               this.containerView.addView(var14);
               LayoutParams var17 = (LayoutParams)var14.getLayoutParams();
               var17.width = -1;
               var17.height = -1;
               var17.leftMargin = 0;
               var17.rightMargin = 0;
               var17.bottomMargin = 0;
               var17.topMargin = 0;
               var14.setLayoutParams(var17);
            }

            var11.onTransitionAnimationStart(true, true);
            var4.onTransitionAnimationStart(false, false);
            var11.onResume();
            this.currentActionBar = var11.actionBar;
            if (!var11.hasOwnBackground && var14.getBackground() == null) {
               var14.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }

            if (!var3) {
               this.closeLastFragmentInternalRemoveOld(var4);
            }

            if (var3) {
               this.transitionAnimationStartTime = System.currentTimeMillis();
               this.transitionAnimationInProgress = true;
               this.onCloseAnimationEndRunnable = new _$$Lambda$ActionBarLayout$pBlWjaMhbocc2CQiKAJuI1kS_Ds(this, var4, var11);
               AnimatorSet var12 = (AnimatorSet)var6;
               if (!this.inPreviewMode) {
                  var12 = (AnimatorSet)var6;
                  if (!this.transitionAnimationPreviewMode) {
                     var12 = var4.onCustomTransitionAnimation(false, new _$$Lambda$ActionBarLayout$r7PgXBy38d_b4XaDN8FoveXk1BE(this));
                  }
               }

               if (var12 == null) {
                  if (!this.containerView.isKeyboardVisible && !this.containerViewBack.isKeyboardVisible) {
                     if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
                        var1 = false;
                     } else {
                        var1 = true;
                     }

                     this.startLayoutAnimation(false, true, var1);
                  } else {
                     this.waitingForKeyboardCloseRunnable = new Runnable() {
                        public void run() {
                           if (ActionBarLayout.this.waitingForKeyboardCloseRunnable == this) {
                              ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                              ActionBarLayout.this.startLayoutAnimation(false, true, false);
                           }
                        }
                     };
                     AndroidUtilities.runOnUIThread(this.waitingForKeyboardCloseRunnable, 200L);
                  }
               } else {
                  this.currentAnimation = var12;
               }
            } else {
               var4.onTransitionAnimationEnd(false, false);
               var11.onTransitionAnimationEnd(true, true);
               var11.onBecomeFullyVisible();
            }
         } else if (this.useAlphaAnimations) {
            this.transitionAnimationStartTime = System.currentTimeMillis();
            this.transitionAnimationInProgress = true;
            this.onCloseAnimationEndRunnable = new _$$Lambda$ActionBarLayout$S9HTTIgsI9OBg6Q7_NNccIiX628(this, var4);
            var10 = new ArrayList();
            var10.add(ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0F, 0.0F}));
            var14 = this.backgroundView;
            if (var14 != null) {
               var10.add(ObjectAnimator.ofFloat(var14, "alpha", new float[]{1.0F, 0.0F}));
            }

            this.currentAnimation = new AnimatorSet();
            this.currentAnimation.playTogether(var10);
            this.currentAnimation.setInterpolator(this.accelerateDecelerateInterpolator);
            this.currentAnimation.setDuration(200L);
            this.currentAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  ActionBarLayout.this.onAnimationEndCheck(false);
               }

               public void onAnimationStart(Animator var1) {
                  ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
               }
            });
            this.currentAnimation.start();
         } else {
            this.removeFragmentFromStackInternal(var4);
            this.setVisibility(8);
            View var13 = this.backgroundView;
            if (var13 != null) {
               var13.setVisibility(8);
            }
         }
      }

   }

   public void dismissDialogs() {
      if (!this.fragmentsStack.isEmpty()) {
         ArrayList var1 = this.fragmentsStack;
         ((BaseFragment)var1.get(var1.size() - 1)).dismissCurrentDialig();
      }

   }

   public boolean dispatchKeyEventPreIme(KeyEvent var1) {
      if (var1 != null && var1.getKeyCode() == 4) {
         int var2 = var1.getAction();
         boolean var3 = true;
         if (var2 == 1) {
            ActionBarLayout.ActionBarLayoutDelegate var4 = this.delegate;
            boolean var5;
            if (var4 != null) {
               var5 = var3;
               if (var4.onPreIme()) {
                  return var5;
               }
            }

            if (super.dispatchKeyEventPreIme(var1)) {
               var5 = var3;
            } else {
               var5 = false;
            }

            return var5;
         }
      }

      return super.dispatchKeyEventPreIme(var1);
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      int var5 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
      int var6 = (int)this.innerTranslationX + this.getPaddingRight();
      int var7 = this.getPaddingLeft();
      int var8 = this.getPaddingLeft() + var5;
      int var9;
      if (var2 == this.containerViewBack) {
         var9 = var6;
      } else {
         var9 = var8;
         if (var2 == this.containerView) {
            var7 = var6;
            var9 = var8;
         }
      }

      int var10 = var1.save();
      boolean var11 = this.transitionAnimationInProgress;
      byte var16 = 0;
      if (!var11 && !this.inPreviewMode) {
         var1.clipRect(var7, 0, var9, this.getHeight());
      }

      float var14;
      float var15;
      if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
         ActionBarLayout.LinearLayoutContainer var12 = this.containerView;
         if (var2 == var12) {
            View var17 = var12.getChildAt(0);
            if (var17 != null) {
               this.previewBackgroundDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
               this.previewBackgroundDrawable.draw(var1);
               int var13 = (this.getMeasuredWidth() - AndroidUtilities.dp(24.0F)) / 2;
               var14 = (float)var17.getTop();
               var15 = this.containerView.getTranslationY();
               if (VERSION.SDK_INT < 21) {
                  var16 = 20;
               }

               var8 = (int)(var14 + var15 - (float)AndroidUtilities.dp((float)(var16 + 12)));
               Theme.moveUpDrawable.setBounds(var13, var8, AndroidUtilities.dp(24.0F) + var13, AndroidUtilities.dp(24.0F) + var8);
               Theme.moveUpDrawable.draw(var1);
            }
         }
      }

      var11 = super.drawChild(var1, var2, var3);
      var1.restoreToCount(var10);
      if (var6 != 0) {
         if (var2 == this.containerView) {
            var14 = Math.max(0.0F, Math.min((float)(var5 - var6) / (float)AndroidUtilities.dp(20.0F), 1.0F));
            Drawable var18 = layerShadowDrawable;
            var18.setBounds(var6 - var18.getIntrinsicWidth(), var2.getTop(), var6, var2.getBottom());
            layerShadowDrawable.setAlpha((int)(var14 * 255.0F));
            layerShadowDrawable.draw(var1);
         } else if (var2 == this.containerViewBack) {
            var15 = Math.min(0.8F, (float)(var5 - var6) / (float)var5);
            var14 = var15;
            if (var15 < 0.0F) {
               var14 = 0.0F;
            }

            scrimPaint.setColor((int)(var14 * 153.0F) << 24);
            var1.drawRect((float)var7, 0.0F, (float)var9, (float)this.getHeight(), scrimPaint);
         }
      }

      return var11;
   }

   public void drawHeaderShadow(Canvas var1, int var2) {
      Drawable var3 = headerShadowDrawable;
      if (var3 != null) {
         var3.setBounds(0, var2, this.getMeasuredWidth(), headerShadowDrawable.getIntrinsicHeight() + var2);
         headerShadowDrawable.draw(var1);
      }

   }

   public boolean extendActionMode(Menu var1) {
      boolean var2 = this.fragmentsStack.isEmpty();
      boolean var3 = true;
      if (!var2) {
         ArrayList var4 = this.fragmentsStack;
         if (((BaseFragment)var4.get(var4.size() - 1)).extendActionMode(var1)) {
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   public void finishPreviewFragment() {
      if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
         this.closeLastFragment(true);
      }
   }

   public DrawerLayoutContainer getDrawerLayoutContainer() {
      return this.drawerLayoutContainer;
   }

   @Keep
   public float getInnerTranslationX() {
      return this.innerTranslationX;
   }

   public BaseFragment getLastFragment() {
      if (this.fragmentsStack.isEmpty()) {
         return null;
      } else {
         ArrayList var1 = this.fragmentsStack;
         return (BaseFragment)var1.get(var1.size() - 1);
      }
   }

   @Keep
   public float getThemeAnimationValue() {
      return this.themeAnimationValue;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void init(ArrayList var1) {
      this.fragmentsStack = var1;
      this.containerViewBack = new ActionBarLayout.LinearLayoutContainer(this.parentActivity);
      this.addView(this.containerViewBack);
      android.widget.FrameLayout.LayoutParams var2 = (android.widget.FrameLayout.LayoutParams)this.containerViewBack.getLayoutParams();
      var2.width = -1;
      var2.height = -1;
      var2.gravity = 51;
      this.containerViewBack.setLayoutParams(var2);
      this.containerView = new ActionBarLayout.LinearLayoutContainer(this.parentActivity);
      this.addView(this.containerView);
      var2 = (android.widget.FrameLayout.LayoutParams)this.containerView.getLayoutParams();
      var2.width = -1;
      var2.height = -1;
      var2.gravity = 51;
      this.containerView.setLayoutParams(var2);
      Iterator var3 = this.fragmentsStack.iterator();

      while(var3.hasNext()) {
         ((BaseFragment)var3.next()).setParentLayout(this);
      }

   }

   public boolean isInPreviewMode() {
      boolean var1;
      if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$closeLastFragment$3$ActionBarLayout(BaseFragment var1, BaseFragment var2) {
      if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
         this.containerViewBack.setTranslationX(0.0F);
      } else {
         this.containerViewBack.setScaleX(1.0F);
         this.containerViewBack.setScaleY(1.0F);
         this.inPreviewMode = false;
         this.transitionAnimationPreviewMode = false;
      }

      this.closeLastFragmentInternalRemoveOld(var1);
      var1.onTransitionAnimationEnd(false, false);
      var2.onTransitionAnimationEnd(true, true);
      var2.onBecomeFullyVisible();
   }

   // $FF: synthetic method
   public void lambda$closeLastFragment$4$ActionBarLayout() {
      this.onAnimationEndCheck(false);
   }

   // $FF: synthetic method
   public void lambda$closeLastFragment$5$ActionBarLayout(BaseFragment var1) {
      this.removeFragmentFromStackInternal(var1);
      this.setVisibility(8);
      View var2 = this.backgroundView;
      if (var2 != null) {
         var2.setVisibility(8);
      }

      DrawerLayoutContainer var3 = this.drawerLayoutContainer;
      if (var3 != null) {
         var3.setAllowOpenDrawer(true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$presentFragment$1$ActionBarLayout(boolean var1, boolean var2, BaseFragment var3, BaseFragment var4) {
      if (var1) {
         this.inPreviewMode = true;
         this.transitionAnimationPreviewMode = false;
         this.containerView.setScaleX(1.0F);
         this.containerView.setScaleY(1.0F);
      } else {
         this.presentFragmentInternalRemoveOld(var2, var3);
         this.containerView.setTranslationX(0.0F);
      }

      var4.onTransitionAnimationEnd(true, false);
      var4.onBecomeFullyVisible();
   }

   // $FF: synthetic method
   public void lambda$presentFragment$2$ActionBarLayout() {
      this.onAnimationEndCheck(false);
   }

   public void movePreviewFragment(float var1) {
      if (this.inPreviewMode && !this.transitionAnimationPreviewMode) {
         float var2 = this.containerView.getTranslationY();
         var1 = -var1;
         float var3 = 0.0F;
         if (var1 > 0.0F) {
            var1 = var3;
         } else if (var1 < (float)(-AndroidUtilities.dp(60.0F))) {
            this.inPreviewMode = false;
            ArrayList var4 = this.fragmentsStack;
            BaseFragment var7 = (BaseFragment)var4.get(var4.size() - 2);
            ArrayList var5 = this.fragmentsStack;
            BaseFragment var9 = (BaseFragment)var5.get(var5.size() - 1);
            if (VERSION.SDK_INT >= 21) {
               var9.fragmentView.setOutlineProvider((ViewOutlineProvider)null);
               var9.fragmentView.setClipToOutline(false);
            }

            LayoutParams var6 = (LayoutParams)var9.fragmentView.getLayoutParams();
            var6.leftMargin = 0;
            var6.rightMargin = 0;
            var6.bottomMargin = 0;
            var6.topMargin = 0;
            var9.fragmentView.setLayoutParams(var6);
            this.presentFragmentInternalRemoveOld(false, var7);
            AnimatorSet var8 = new AnimatorSet();
            var8.playTogether(new Animator[]{ObjectAnimator.ofFloat(var9.fragmentView, "scaleX", new float[]{1.0F, 1.05F, 1.0F}), ObjectAnimator.ofFloat(var9.fragmentView, "scaleY", new float[]{1.0F, 1.05F, 1.0F})});
            var8.setDuration(200L);
            var8.setInterpolator(new CubicBezierInterpolator(0.42D, 0.0D, 0.58D, 1.0D));
            var8.start();
            this.performHapticFeedback(3);
            var9.setInPreviewMode(false);
            var1 = var3;
         }

         if (var2 != var1) {
            this.containerView.setTranslationY(var1);
            this.invalidate();
         }
      }

   }

   public void onActionModeFinished(Object var1) {
      ActionBar var2 = this.currentActionBar;
      if (var2 != null) {
         var2.setVisibility(0);
      }

      this.inActionMode = false;
   }

   public void onActionModeStarted(Object var1) {
      ActionBar var2 = this.currentActionBar;
      if (var2 != null) {
         var2.setVisibility(8);
      }

      this.inActionMode = true;
   }

   public void onBackPressed() {
      if (!this.transitionAnimationPreviewMode && !this.startedTracking && !this.checkTransitionAnimation() && !this.fragmentsStack.isEmpty()) {
         if (!this.currentActionBar.isActionModeShowed()) {
            ActionBar var1 = this.currentActionBar;
            if (var1 != null && var1.isSearchFieldVisible) {
               var1.closeSearchField();
               return;
            }
         }

         ArrayList var2 = this.fragmentsStack;
         if (((BaseFragment)var2.get(var2.size() - 1)).onBackPressed() && !this.fragmentsStack.isEmpty()) {
            this.closeLastFragment(true);
         }
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (!this.fragmentsStack.isEmpty()) {
         ArrayList var2 = this.fragmentsStack;
         BaseFragment var3 = (BaseFragment)var2.get(var2.size() - 1);
         var3.onConfigurationChanged(var1);
         Dialog var4 = var3.visibleDialog;
         if (var4 instanceof BottomSheet) {
            ((BottomSheet)var4).onConfigurationChanged(var1);
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2;
      if (!this.animationInProgress && !this.checkTransitionAnimation() && !this.onTouchEvent(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      if (var1 == 82 && !this.checkTransitionAnimation() && !this.startedTracking) {
         ActionBar var3 = this.currentActionBar;
         if (var3 != null) {
            var3.onMenuButtonPressed();
         }
      }

      return super.onKeyUp(var1, var2);
   }

   public void onLowMemory() {
      Iterator var1 = this.fragmentsStack.iterator();

      while(var1.hasNext()) {
         ((BaseFragment)var1.next()).onLowMemory();
      }

   }

   public void onPause() {
      if (!this.fragmentsStack.isEmpty()) {
         ArrayList var1 = this.fragmentsStack;
         ((BaseFragment)var1.get(var1.size() - 1)).onPause();
      }

   }

   public void onResume() {
      if (this.transitionAnimationInProgress) {
         AnimatorSet var1 = this.currentAnimation;
         if (var1 != null) {
            var1.cancel();
            this.currentAnimation = null;
         }

         if (this.onCloseAnimationEndRunnable != null) {
            this.onCloseAnimationEnd();
         } else if (this.onOpenAnimationEndRunnable != null) {
            this.onOpenAnimationEnd();
         }
      }

      if (!this.fragmentsStack.isEmpty()) {
         ArrayList var2 = this.fragmentsStack;
         ((BaseFragment)var2.get(var2.size() - 1)).onResume();
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.checkTransitionAnimation() && !this.inActionMode && !this.animationInProgress) {
         if (this.fragmentsStack.size() > 1) {
            ArrayList var2;
            VelocityTracker var10;
            if (var1 != null && var1.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
               var2 = this.fragmentsStack;
               if (!((BaseFragment)var2.get(var2.size() - 1)).swipeBackEnabled) {
                  return false;
               }

               this.startedTrackingPointerId = var1.getPointerId(0);
               this.maybeStartTracking = true;
               this.startedTrackingX = (int)var1.getX();
               this.startedTrackingY = (int)var1.getY();
               var10 = this.velocityTracker;
               if (var10 != null) {
                  var10.clear();
               }
            } else {
               float var5;
               ActionBarLayout.LinearLayoutContainer var9;
               if (var1 != null && var1.getAction() == 2 && var1.getPointerId(0) == this.startedTrackingPointerId) {
                  if (this.velocityTracker == null) {
                     this.velocityTracker = VelocityTracker.obtain();
                  }

                  int var3 = Math.max(0, (int)(var1.getX() - (float)this.startedTrackingX));
                  int var4 = Math.abs((int)var1.getY() - this.startedTrackingY);
                  this.velocityTracker.addMovement(var1);
                  if (this.maybeStartTracking && !this.startedTracking && (float)var3 >= AndroidUtilities.getPixelsInCM(0.4F, true) && Math.abs(var3) / 3 > var4) {
                     var2 = this.fragmentsStack;
                     if (((BaseFragment)var2.get(var2.size() - 1)).canBeginSlide()) {
                        this.prepareForMoving(var1);
                     } else {
                        this.maybeStartTracking = false;
                     }
                  } else if (this.startedTracking) {
                     if (!this.beginTrackingSent) {
                        if (this.parentActivity.getCurrentFocus() != null) {
                           AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                        }

                        ArrayList var12 = this.fragmentsStack;
                        ((BaseFragment)var12.get(var12.size() - 1)).onBeginSlide();
                        this.beginTrackingSent = true;
                     }

                     var9 = this.containerView;
                     var5 = (float)var3;
                     var9.setTranslationX(var5);
                     this.setInnerTranslationX(var5);
                  }
               } else if (var1 == null || var1.getPointerId(0) != this.startedTrackingPointerId || var1.getAction() != 3 && var1.getAction() != 1 && var1.getAction() != 6) {
                  if (var1 == null) {
                     this.maybeStartTracking = false;
                     this.startedTracking = false;
                     var10 = this.velocityTracker;
                     if (var10 != null) {
                        var10.recycle();
                        this.velocityTracker = null;
                     }
                  }
               } else {
                  if (this.velocityTracker == null) {
                     this.velocityTracker = VelocityTracker.obtain();
                  }

                  this.velocityTracker.computeCurrentVelocity(1000);
                  var2 = this.fragmentsStack;
                  BaseFragment var11 = (BaseFragment)var2.get(var2.size() - 1);
                  float var6;
                  if (!this.inPreviewMode && !this.transitionAnimationPreviewMode && !this.startedTracking && var11.swipeBackEnabled) {
                     var6 = this.velocityTracker.getXVelocity();
                     var5 = this.velocityTracker.getYVelocity();
                     if (var6 >= 3500.0F && var6 > Math.abs(var5) && var11.canBeginSlide()) {
                        this.prepareForMoving(var1);
                        if (!this.beginTrackingSent) {
                           if (((Activity)this.getContext()).getCurrentFocus() != null) {
                              AndroidUtilities.hideKeyboard(((Activity)this.getContext()).getCurrentFocus());
                           }

                           this.beginTrackingSent = true;
                        }
                     }
                  }

                  if (!this.startedTracking) {
                     this.maybeStartTracking = false;
                     this.startedTracking = false;
                  } else {
                     var5 = this.containerView.getX();
                     AnimatorSet var13 = new AnimatorSet();
                     var6 = this.velocityTracker.getXVelocity();
                     float var7 = this.velocityTracker.getYVelocity();
                     final boolean var8;
                     if (var5 >= (float)this.containerView.getMeasuredWidth() / 3.0F || var6 >= 3500.0F && var6 >= var7) {
                        var8 = false;
                     } else {
                        var8 = true;
                     }

                     if (!var8) {
                        var5 = (float)this.containerView.getMeasuredWidth() - var5;
                        var9 = this.containerView;
                        var13.playTogether(new Animator[]{ObjectAnimator.ofFloat(var9, "translationX", new float[]{(float)var9.getMeasuredWidth()}), ObjectAnimator.ofFloat(this, "innerTranslationX", new float[]{(float)this.containerView.getMeasuredWidth()})});
                     } else {
                        var13.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{0.0F}), ObjectAnimator.ofFloat(this, "innerTranslationX", new float[]{0.0F})});
                     }

                     var13.setDuration((long)Math.max((int)(200.0F / (float)this.containerView.getMeasuredWidth() * var5), 50));
                     var13.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1) {
                           ActionBarLayout.this.onSlideAnimationEnd(var8);
                        }
                     });
                     var13.start();
                     this.animationInProgress = true;
                  }

                  var10 = this.velocityTracker;
                  if (var10 != null) {
                     var10.recycle();
                     this.velocityTracker = null;
                  }
               }
            }
         }

         return this.startedTracking;
      } else {
         return false;
      }
   }

   public boolean presentFragment(BaseFragment var1) {
      return this.presentFragment(var1, false, false, true, false);
   }

   public boolean presentFragment(BaseFragment var1, boolean var2) {
      return this.presentFragment(var1, var2, false, true, false);
   }

   public boolean presentFragment(BaseFragment var1, boolean var2, boolean var3, boolean var4, final boolean var5) {
      if (!this.checkTransitionAnimation()) {
         ActionBarLayout.ActionBarLayoutDelegate var6 = this.delegate;
         if ((var6 == null || !var4 || var6.needPresentFragment(var1, var2, var3, this)) && var1.onFragmentCreate()) {
            var1.setInPreviewMode(var5);
            if (this.parentActivity.getCurrentFocus() != null) {
               AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
            }

            boolean var7;
            if (var5 || !var3 && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
               var7 = true;
            } else {
               var7 = false;
            }

            ArrayList var12;
            BaseFragment var13;
            if (!this.fragmentsStack.isEmpty()) {
               var12 = this.fragmentsStack;
               var13 = (BaseFragment)var12.get(var12.size() - 1);
            } else {
               var13 = null;
            }

            var1.setParentLayout(this);
            View var8 = var1.fragmentView;
            View var9;
            if (var8 == null) {
               var9 = var1.createView(this.parentActivity);
            } else {
               ViewGroup var10 = (ViewGroup)var8.getParent();
               var9 = var8;
               if (var10 != null) {
                  var1.onRemoveFromParent();
                  var10.removeView(var8);
                  var9 = var8;
               }
            }

            ActionBar var15 = var1.actionBar;
            if (var15 != null && var15.getAddToContainer()) {
               if (this.removeActionBarExtraHeight) {
                  var1.actionBar.setOccupyStatusBar(false);
               }

               ViewGroup var16 = (ViewGroup)var1.actionBar.getParent();
               if (var16 != null) {
                  var16.removeView(var1.actionBar);
               }

               this.containerViewBack.addView(var1.actionBar);
               var1.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
            }

            this.containerViewBack.addView(var9);
            LayoutParams var17 = (LayoutParams)var9.getLayoutParams();
            var17.width = -1;
            var17.height = -1;
            if (var5) {
               int var11 = AndroidUtilities.dp(8.0F);
               var17.leftMargin = var11;
               var17.rightMargin = var11;
               var11 = AndroidUtilities.dp(46.0F);
               var17.bottomMargin = var11;
               var17.topMargin = var11;
               var17.topMargin += AndroidUtilities.statusBarHeight;
            } else {
               var17.leftMargin = 0;
               var17.rightMargin = 0;
               var17.bottomMargin = 0;
               var17.topMargin = 0;
            }

            var9.setLayoutParams(var17);
            this.fragmentsStack.add(var1);
            var1.onResume();
            this.currentActionBar = var1.actionBar;
            if (!var1.hasOwnBackground && var9.getBackground() == null) {
               var9.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }

            ActionBarLayout.LinearLayoutContainer var18 = this.containerView;
            this.containerView = this.containerViewBack;
            this.containerViewBack = var18;
            this.containerView.setVisibility(0);
            this.setInnerTranslationX(0.0F);
            this.containerView.setTranslationY(0.0F);
            if (var5) {
               if (VERSION.SDK_INT >= 21) {
                  var9.setOutlineProvider(new ViewOutlineProvider() {
                     @TargetApi(21)
                     public void getOutline(View var1, Outline var2) {
                        var2.setRoundRect(0, AndroidUtilities.statusBarHeight, var1.getMeasuredWidth(), var1.getMeasuredHeight(), (float)AndroidUtilities.dp(6.0F));
                     }
                  });
                  var9.setClipToOutline(true);
                  var9.setElevation((float)AndroidUtilities.dp(4.0F));
               }

               if (this.previewBackgroundDrawable == null) {
                  this.previewBackgroundDrawable = new ColorDrawable(Integer.MIN_VALUE);
               }

               this.previewBackgroundDrawable.setAlpha(0);
               Theme.moveUpDrawable.setAlpha(0);
            }

            this.bringChildToFront(this.containerView);
            if (!var7) {
               this.presentFragmentInternalRemoveOld(var2, var13);
               var9 = this.backgroundView;
               if (var9 != null) {
                  var9.setVisibility(0);
               }
            }

            if (!var7 && !var5) {
               View var19 = this.backgroundView;
               if (var19 != null) {
                  var19.setAlpha(1.0F);
                  this.backgroundView.setVisibility(0);
               }

               var1.onTransitionAnimationStart(true, false);
               var1.onTransitionAnimationEnd(true, false);
               var1.onBecomeFullyVisible();
            } else if (this.useAlphaAnimations && this.fragmentsStack.size() == 1) {
               this.presentFragmentInternalRemoveOld(var2, var13);
               this.transitionAnimationStartTime = System.currentTimeMillis();
               this.transitionAnimationInProgress = true;
               this.onOpenAnimationEndRunnable = new _$$Lambda$ActionBarLayout$pYh6HgDiwfydlsh9Xn7223Bd_IA(var1);
               var12 = new ArrayList();
               var12.add(ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0F, 1.0F}));
               var9 = this.backgroundView;
               if (var9 != null) {
                  var9.setVisibility(0);
                  var12.add(ObjectAnimator.ofFloat(this.backgroundView, "alpha", new float[]{0.0F, 1.0F}));
               }

               var1.onTransitionAnimationStart(true, false);
               this.currentAnimation = new AnimatorSet();
               this.currentAnimation.playTogether(var12);
               this.currentAnimation.setInterpolator(this.accelerateDecelerateInterpolator);
               this.currentAnimation.setDuration(200L);
               this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     ActionBarLayout.this.onAnimationEndCheck(false);
                  }
               });
               this.currentAnimation.start();
            } else {
               this.transitionAnimationPreviewMode = var5;
               this.transitionAnimationStartTime = System.currentTimeMillis();
               this.transitionAnimationInProgress = true;
               this.onOpenAnimationEndRunnable = new _$$Lambda$ActionBarLayout$gS41pICx_migujhqTs_lczeRv1Y(this, var5, var2, var13, var1);
               var1.onTransitionAnimationStart(true, false);
               AnimatorSet var14;
               if (!var5) {
                  var14 = var1.onCustomTransitionAnimation(true, new _$$Lambda$ActionBarLayout$ZVBs3Yp413UBaNwwGzpjbW7oZTc(this));
               } else {
                  var14 = null;
               }

               if (var14 == null) {
                  this.containerView.setAlpha(0.0F);
                  if (var5) {
                     this.containerView.setTranslationX(0.0F);
                     this.containerView.setScaleX(0.9F);
                     this.containerView.setScaleY(0.9F);
                  } else {
                     this.containerView.setTranslationX(48.0F);
                     this.containerView.setScaleX(1.0F);
                     this.containerView.setScaleY(1.0F);
                  }

                  if (!this.containerView.isKeyboardVisible && !this.containerViewBack.isKeyboardVisible) {
                     if (var1.needDelayOpenAnimation()) {
                        this.delayedOpenAnimationRunnable = new Runnable() {
                           public void run() {
                              if (ActionBarLayout.this.delayedOpenAnimationRunnable == this) {
                                 ActionBarLayout.this.delayedOpenAnimationRunnable = null;
                                 ActionBarLayout.this.startLayoutAnimation(true, true, var5);
                              }
                           }
                        };
                        AndroidUtilities.runOnUIThread(this.delayedOpenAnimationRunnable, 200L);
                     } else {
                        this.startLayoutAnimation(true, true, var5);
                     }
                  } else {
                     this.waitingForKeyboardCloseRunnable = new Runnable() {
                        public void run() {
                           if (ActionBarLayout.this.waitingForKeyboardCloseRunnable == this) {
                              ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                              ActionBarLayout.this.startLayoutAnimation(true, true, var5);
                           }
                        }
                     };
                     AndroidUtilities.runOnUIThread(this.waitingForKeyboardCloseRunnable, 200L);
                  }
               } else {
                  this.containerView.setAlpha(1.0F);
                  this.containerView.setTranslationX(0.0F);
                  this.currentAnimation = var14;
               }
            }

            return true;
         }
      }

      return false;
   }

   public boolean presentFragmentAsPreview(BaseFragment var1) {
      return this.presentFragment(var1, false, false, true, true);
   }

   public void rebuildAllFragmentViews(boolean var1, boolean var2) {
      if (!this.transitionAnimationInProgress && !this.startedTracking) {
         int var3 = this.fragmentsStack.size();
         int var4 = var3;
         if (!var1) {
            var4 = var3 - 1;
         }

         var3 = var4;
         if (this.inPreviewMode) {
            var3 = var4 - 1;
         }

         for(var4 = 0; var4 < var3; ++var4) {
            ((BaseFragment)this.fragmentsStack.get(var4)).clearViews();
            ((BaseFragment)this.fragmentsStack.get(var4)).setParentLayout(this);
         }

         ActionBarLayout.ActionBarLayoutDelegate var5 = this.delegate;
         if (var5 != null) {
            var5.onRebuildAllFragments(this, var1);
         }

         if (var2) {
            this.showLastFragment();
         }

      } else {
         this.rebuildAfterAnimation = true;
         this.rebuildLastAfterAnimation = var1;
         this.showLastAfterAnimation = var2;
      }
   }

   public void removeAllFragments() {
      while(this.fragmentsStack.size() > 0) {
         this.removeFragmentFromStackInternal((BaseFragment)this.fragmentsStack.get(0));
      }

   }

   public void removeFragmentFromStack(int var1) {
      if (var1 < this.fragmentsStack.size()) {
         this.removeFragmentFromStackInternal((BaseFragment)this.fragmentsStack.get(var1));
      }
   }

   public void removeFragmentFromStack(BaseFragment var1) {
      if (this.useAlphaAnimations && this.fragmentsStack.size() == 1 && AndroidUtilities.isTablet()) {
         this.closeLastFragment(true);
      } else {
         this.removeFragmentFromStackInternal(var1);
      }

   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      this.onTouchEvent((MotionEvent)null);
      super.requestDisallowInterceptTouchEvent(var1);
   }

   public void resumeDelayedFragmentAnimation() {
      Runnable var1 = this.delayedOpenAnimationRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.delayedOpenAnimationRunnable.run();
         this.delayedOpenAnimationRunnable = null;
      }
   }

   public void setBackgroundView(View var1) {
      this.backgroundView = var1;
   }

   public void setDelegate(ActionBarLayout.ActionBarLayoutDelegate var1) {
      this.delegate = var1;
   }

   public void setDrawerLayoutContainer(DrawerLayoutContainer var1) {
      this.drawerLayoutContainer = var1;
   }

   @Keep
   public void setInnerTranslationX(float var1) {
      this.innerTranslationX = var1;
      this.invalidate();
   }

   public void setRemoveActionBarExtraHeight(boolean var1) {
      this.removeActionBarExtraHeight = var1;
   }

   @Keep
   public void setThemeAnimationValue(float var1) {
      this.themeAnimationValue = var1;

      for(int var2 = 0; var2 < 2; ++var2) {
         if (this.themeAnimatorDescriptions[var2] != null) {
            for(int var3 = 0; var3 < this.themeAnimatorDescriptions[var2].length; ++var3) {
               int var4 = Color.red(this.animateEndColors[var2][var3]);
               int var5 = Color.green(this.animateEndColors[var2][var3]);
               int var6 = Color.blue(this.animateEndColors[var2][var3]);
               int var7 = Color.alpha(this.animateEndColors[var2][var3]);
               int var8 = Color.red(this.animateStartColors[var2][var3]);
               int var9 = Color.green(this.animateStartColors[var2][var3]);
               int var10 = Color.blue(this.animateStartColors[var2][var3]);
               int var11 = Color.alpha(this.animateStartColors[var2][var3]);
               var10 = Color.argb(Math.min(255, (int)((float)var11 + (float)(var7 - var11) * var1)), Math.min(255, (int)((float)var8 + (float)(var4 - var8) * var1)), Math.min(255, (int)((float)var9 + (float)(var5 - var9) * var1)), Math.min(255, (int)((float)var10 + (float)(var6 - var10) * var1)));
               Theme.setAnimatedColor(this.themeAnimatorDescriptions[var2][var3].getCurrentKey(), var10);
               this.themeAnimatorDescriptions[var2][var3].setColor(var10, false, false);
            }

            ThemeDescription.ThemeDescriptionDelegate[] var12 = this.themeAnimatorDelegate;
            if (var12[var2] != null) {
               var12[var2].didSetColor();
            }
         }
      }

   }

   public void setTitleOverlayText(String var1, int var2, Runnable var3) {
      this.titleOverlayText = var1;
      this.titleOverlayTextId = var2;
      this.overlayAction = var3;

      for(var2 = 0; var2 < this.fragmentsStack.size(); ++var2) {
         ActionBar var4 = ((BaseFragment)this.fragmentsStack.get(var2)).actionBar;
         if (var4 != null) {
            var4.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, var3);
         }
      }

   }

   public void setUseAlphaAnimations(boolean var1) {
      this.useAlphaAnimations = var1;
   }

   public void showLastFragment() {
      if (!this.fragmentsStack.isEmpty()) {
         ActionBar var3;
         ViewGroup var8;
         View var9;
         for(int var1 = 0; var1 < this.fragmentsStack.size() - 1; ++var1) {
            BaseFragment var2 = (BaseFragment)this.fragmentsStack.get(var1);
            var3 = var2.actionBar;
            if (var3 != null && var3.getAddToContainer()) {
               var8 = (ViewGroup)var2.actionBar.getParent();
               if (var8 != null) {
                  var8.removeView(var2.actionBar);
               }
            }

            var9 = var2.fragmentView;
            if (var9 != null) {
               var8 = (ViewGroup)var9.getParent();
               if (var8 != null) {
                  var2.onPause();
                  var2.onRemoveFromParent();
                  var8.removeView(var2.fragmentView);
               }
            }
         }

         ArrayList var6 = this.fragmentsStack;
         BaseFragment var4 = (BaseFragment)var6.get(var6.size() - 1);
         var4.setParentLayout(this);
         var9 = var4.fragmentView;
         View var7;
         if (var9 == null) {
            var7 = var4.createView(this.parentActivity);
         } else {
            ViewGroup var5 = (ViewGroup)var9.getParent();
            var7 = var9;
            if (var5 != null) {
               var4.onRemoveFromParent();
               var5.removeView(var9);
               var7 = var9;
            }
         }

         var3 = var4.actionBar;
         if (var3 != null && var3.getAddToContainer()) {
            if (this.removeActionBarExtraHeight) {
               var4.actionBar.setOccupyStatusBar(false);
            }

            var8 = (ViewGroup)var4.actionBar.getParent();
            if (var8 != null) {
               var8.removeView(var4.actionBar);
            }

            this.containerView.addView(var4.actionBar);
            var4.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
         }

         this.containerView.addView(var7, LayoutHelper.createLinear(-1, -1));
         var4.onResume();
         this.currentActionBar = var4.actionBar;
         if (!var4.hasOwnBackground && var7.getBackground() == null) {
            var7.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

      }
   }

   public void startActivityForResult(Intent var1, int var2) {
      Activity var3 = this.parentActivity;
      if (var3 != null) {
         if (this.transitionAnimationInProgress) {
            AnimatorSet var4 = this.currentAnimation;
            if (var4 != null) {
               var4.cancel();
               this.currentAnimation = null;
            }

            if (this.onCloseAnimationEndRunnable != null) {
               this.onCloseAnimationEnd();
            } else if (this.onOpenAnimationEndRunnable != null) {
               this.onOpenAnimationEnd();
            }

            this.containerView.invalidate();
            if (var1 != null) {
               this.parentActivity.startActivityForResult(var1, var2);
            }
         } else if (var1 != null) {
            var3.startActivityForResult(var1, var2);
         }

      }
   }

   public interface ActionBarLayoutDelegate {
      boolean needAddFragmentToStack(BaseFragment var1, ActionBarLayout var2);

      boolean needCloseLastFragment(ActionBarLayout var1);

      boolean needPresentFragment(BaseFragment var1, boolean var2, boolean var3, ActionBarLayout var4);

      boolean onPreIme();

      void onRebuildAllFragments(ActionBarLayout var1, boolean var2);
   }

   public class LinearLayoutContainer extends LinearLayout {
      private boolean isKeyboardVisible;
      private Rect rect = new Rect();

      public LinearLayoutContainer(Context var2) {
         super(var2);
         this.setOrientation(1);
      }

      public boolean dispatchTouchEvent(MotionEvent var1) {
         boolean var2 = ActionBarLayout.this.inPreviewMode;
         boolean var3 = false;
         if (!var2 && !ActionBarLayout.this.transitionAnimationPreviewMode || var1.getActionMasked() != 0 && var1.getActionMasked() != 5) {
            Throwable var10000;
            label60: {
               boolean var10001;
               label53: {
                  try {
                     if (!ActionBarLayout.this.inPreviewMode) {
                        break label53;
                     }
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label60;
                  }

                  var2 = var3;

                  try {
                     if (this == ActionBarLayout.this.containerView) {
                        return var2;
                     }
                  } catch (Throwable var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label60;
                  }
               }

               boolean var4;
               try {
                  var4 = super.dispatchTouchEvent(var1);
               } catch (Throwable var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label60;
               }

               var2 = var3;
               if (var4) {
                  var2 = true;
               }

               return var2;
            }

            Throwable var8 = var10000;
            FileLog.e(var8);
         }

         return false;
      }

      protected boolean drawChild(Canvas var1, View var2, long var3) {
         if (var2 instanceof ActionBar) {
            return super.drawChild(var1, var2, var3);
         } else {
            int var5 = this.getChildCount();
            int var6 = 0;

            while(true) {
               if (var6 < var5) {
                  View var7 = this.getChildAt(var6);
                  if (var7 == var2 || !(var7 instanceof ActionBar) || var7.getVisibility() != 0) {
                     ++var6;
                     continue;
                  }

                  if (((ActionBar)var7).getCastShadows()) {
                     var6 = var7.getMeasuredHeight();
                     break;
                  }
               }

               var6 = 0;
               break;
            }

            boolean var8 = super.drawChild(var1, var2, var3);
            if (var6 != 0 && ActionBarLayout.headerShadowDrawable != null) {
               ActionBarLayout.headerShadowDrawable.setBounds(0, var6, this.getMeasuredWidth(), ActionBarLayout.headerShadowDrawable.getIntrinsicHeight() + var6);
               ActionBarLayout.headerShadowDrawable.draw(var1);
            }

            return var8;
         }
      }

      public boolean hasOverlappingRendering() {
         return VERSION.SDK_INT >= 28;
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         View var6 = this.getRootView();
         this.getWindowVisibleDisplayFrame(this.rect);
         var3 = var6.getHeight();
         var2 = this.rect.top;
         var1 = false;
         if (var2 != 0) {
            var2 = AndroidUtilities.statusBarHeight;
         } else {
            var2 = 0;
         }

         var4 = AndroidUtilities.getViewInset(var6);
         Rect var7 = this.rect;
         if (var3 - var2 - var4 - (var7.bottom - var7.top) > 0) {
            var1 = true;
         }

         this.isKeyboardVisible = var1;
         if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != null && !ActionBarLayout.this.containerView.isKeyboardVisible && !ActionBarLayout.this.containerViewBack.isKeyboardVisible) {
            AndroidUtilities.cancelRunOnUIThread(ActionBarLayout.this.waitingForKeyboardCloseRunnable);
            ActionBarLayout.this.waitingForKeyboardCloseRunnable.run();
            ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
         }

      }
   }
}
