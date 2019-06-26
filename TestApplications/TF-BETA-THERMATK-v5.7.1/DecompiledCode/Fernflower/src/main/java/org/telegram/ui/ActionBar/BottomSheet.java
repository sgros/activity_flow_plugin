package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;

public class BottomSheet extends Dialog {
   private boolean allowCustomAnimation;
   private boolean allowDrawContent;
   private boolean allowNestedScroll;
   private boolean applyBottomPadding;
   private boolean applyTopPadding;
   protected ColorDrawable backDrawable;
   protected int backgroundPaddingLeft;
   protected int backgroundPaddingTop;
   protected BottomSheet.ContainerView container;
   protected ViewGroup containerView;
   protected int currentAccount;
   protected AnimatorSet currentSheetAnimation;
   private View customView;
   private BottomSheet.BottomSheetDelegateInterface delegate;
   private boolean dimBehind;
   private Runnable dismissRunnable;
   private boolean dismissed;
   private boolean focusable;
   protected boolean fullWidth;
   protected boolean isFullscreen;
   private int[] itemIcons;
   private ArrayList itemViews;
   private CharSequence[] items;
   private WindowInsets lastInsets;
   private int layoutCount;
   protected View nestedScrollChild;
   private OnClickListener onClickListener;
   private Drawable shadowDrawable;
   private boolean showWithoutAnimation;
   private Runnable startAnimationRunnable;
   private int tag;
   private CharSequence title;
   private TextView titleView;
   private int touchSlop;
   private boolean useFastDismiss;
   private boolean useHardwareLayer;

   public BottomSheet(Context var1, boolean var2, int var3) {
      super(var1, 2131624225);
      this.currentAccount = UserConfig.selectedAccount;
      this.allowDrawContent = true;
      this.useHardwareLayer = true;
      this.backDrawable = new ColorDrawable(-16777216);
      this.allowCustomAnimation = true;
      this.dimBehind = true;
      this.allowNestedScroll = true;
      this.applyTopPadding = true;
      this.applyBottomPadding = true;
      this.itemViews = new ArrayList();
      this.dismissRunnable = new _$$Lambda$wKJSb77Iz9CSKJu9VMkyxGvOd_c(this);
      if (VERSION.SDK_INT >= 21) {
         this.getWindow().addFlags(-2147417856);
      }

      this.touchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
      Rect var4 = new Rect();
      if (var3 == 0) {
         this.shadowDrawable = var1.getResources().getDrawable(2131165823).mutate();
      } else if (var3 == 1) {
         this.shadowDrawable = var1.getResources().getDrawable(2131165824).mutate();
      }

      this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogBackground"), Mode.MULTIPLY));
      this.shadowDrawable.getPadding(var4);
      this.backgroundPaddingLeft = var4.left;
      this.backgroundPaddingTop = var4.top;
      this.container = new BottomSheet.ContainerView(this.getContext()) {
         public boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = true;

            label20: {
               boolean var6;
               try {
                  if (!BottomSheet.this.allowDrawContent) {
                     break label20;
                  }

                  var6 = super.drawChild(var1, var2, var3);
               } catch (Exception var7) {
                  FileLog.e((Throwable)var7);
                  return true;
               }

               if (var6) {
                  return var5;
               }
            }

            var5 = false;
            return var5;
         }
      };
      this.container.setBackgroundDrawable(this.backDrawable);
      this.focusable = var2;
      if (VERSION.SDK_INT >= 21) {
         this.container.setFitsSystemWindows(true);
         this.container.setOnApplyWindowInsetsListener(new _$$Lambda$BottomSheet$IjDyKTRWpdCwBFc4MNcspRHUp7w(this));
         this.container.setSystemUiVisibility(1280);
      }

      this.backDrawable.setAlpha(0);
   }

   // $FF: synthetic method
   static int access$710(BottomSheet var0) {
      int var1 = var0.layoutCount--;
      return var1;
   }

   private void cancelSheetAnimation() {
      AnimatorSet var1 = this.currentSheetAnimation;
      if (var1 != null) {
         var1.cancel();
         this.currentSheetAnimation = null;
      }

   }

   // $FF: synthetic method
   static boolean lambda$onCreate$1(View var0, MotionEvent var1) {
      return true;
   }

   private void startOpenAnimation() {
      if (!this.dismissed) {
         this.containerView.setVisibility(0);
         if (!this.onCustomOpenAnimation()) {
            if (VERSION.SDK_INT >= 20 && this.useHardwareLayer) {
               this.container.setLayerType(2, (Paint)null);
            }

            ViewGroup var1 = this.containerView;
            var1.setTranslationY((float)var1.getMeasuredHeight());
            AnimatorSet var2 = new AnimatorSet();
            ObjectAnimator var3 = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{0.0F});
            ColorDrawable var5 = this.backDrawable;
            byte var4;
            if (this.dimBehind) {
               var4 = 51;
            } else {
               var4 = 0;
            }

            var2.playTogether(new Animator[]{var3, ObjectAnimator.ofInt(var5, "alpha", new int[]{var4})});
            var2.setDuration(400L);
            var2.setStartDelay(20L);
            var2.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            var2.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1) {
                  AnimatorSet var2 = BottomSheet.this.currentSheetAnimation;
                  if (var2 != null && var2.equals(var1)) {
                     BottomSheet.this.currentSheetAnimation = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  AnimatorSet var2 = BottomSheet.this.currentSheetAnimation;
                  if (var2 != null && var2.equals(var1)) {
                     BottomSheet var3 = BottomSheet.this;
                     var3.currentSheetAnimation = null;
                     if (var3.delegate != null) {
                        BottomSheet.this.delegate.onOpenAnimationEnd();
                     }

                     if (BottomSheet.this.useHardwareLayer) {
                        BottomSheet.this.container.setLayerType(0, (Paint)null);
                     }

                     var3 = BottomSheet.this;
                     if (var3.isFullscreen) {
                        LayoutParams var4 = var3.getWindow().getAttributes();
                        var4.flags &= -1025;
                        BottomSheet.this.getWindow().setAttributes(var4);
                     }
                  }

               }
            });
            var2.start();
            this.currentSheetAnimation = var2;
         }

      }
   }

   protected boolean canDismissWithSwipe() {
      return true;
   }

   protected boolean canDismissWithTouchOutside() {
      return true;
   }

   public void dismiss() {
      BottomSheet.BottomSheetDelegateInterface var1 = this.delegate;
      if (var1 == null || var1.canDismiss()) {
         if (!this.dismissed) {
            this.dismissed = true;
            this.cancelSheetAnimation();
            if (!this.allowCustomAnimation || !this.onCustomCloseAnimation()) {
               AnimatorSet var4 = new AnimatorSet();
               ViewGroup var2 = this.containerView;
               var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(var2, "translationY", new float[]{(float)(var2.getMeasuredHeight() + AndroidUtilities.dp(10.0F))}), ObjectAnimator.ofInt(this.backDrawable, "alpha", new int[]{0})});
               if (this.useFastDismiss) {
                  float var3 = (float)this.containerView.getMeasuredHeight();
                  var4.setDuration((long)Math.max(60, (int)((var3 - this.containerView.getTranslationY()) * 180.0F / var3)));
                  this.useFastDismiss = false;
               } else {
                  var4.setDuration(180L);
               }

               var4.setInterpolator(CubicBezierInterpolator.EASE_OUT);
               var4.addListener(new AnimatorListenerAdapter() {
                  // $FF: synthetic method
                  public void lambda$onAnimationEnd$0$BottomSheet$6() {
                     try {
                        BottomSheet.this.dismissInternal();
                     } catch (Exception var2) {
                        FileLog.e((Throwable)var2);
                     }

                  }

                  public void onAnimationCancel(Animator var1) {
                     AnimatorSet var2 = BottomSheet.this.currentSheetAnimation;
                     if (var2 != null && var2.equals(var1)) {
                        BottomSheet.this.currentSheetAnimation = null;
                     }

                  }

                  public void onAnimationEnd(Animator var1) {
                     AnimatorSet var2 = BottomSheet.this.currentSheetAnimation;
                     if (var2 != null && var2.equals(var1)) {
                        BottomSheet.this.currentSheetAnimation = null;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$BottomSheet$6$VTgE_oeIT2bQ5t_sdXAKcokhgP8(this));
                     }

                  }
               });
               var4.start();
               this.currentSheetAnimation = var4;
            }

         }
      }
   }

   public void dismissInternal() {
      try {
         super.dismiss();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   public void dismissWithButtonClick(final int var1) {
      if (!this.dismissed) {
         this.dismissed = true;
         this.cancelSheetAnimation();
         AnimatorSet var2 = new AnimatorSet();
         ViewGroup var3 = this.containerView;
         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, "translationY", new float[]{(float)(var3.getMeasuredHeight() + AndroidUtilities.dp(10.0F))}), ObjectAnimator.ofInt(this.backDrawable, "alpha", new int[]{0})});
         var2.setDuration(180L);
         var2.setInterpolator(CubicBezierInterpolator.EASE_OUT);
         var2.addListener(new AnimatorListenerAdapter() {
            // $FF: synthetic method
            public void lambda$onAnimationEnd$0$BottomSheet$5() {
               try {
                  BottomSheet.super.dismiss();
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

            }

            public void onAnimationCancel(Animator var1x) {
               AnimatorSet var2 = BottomSheet.this.currentSheetAnimation;
               if (var2 != null && var2.equals(var1x)) {
                  BottomSheet.this.currentSheetAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               AnimatorSet var2 = BottomSheet.this.currentSheetAnimation;
               if (var2 != null && var2.equals(var1x)) {
                  BottomSheet var3 = BottomSheet.this;
                  var3.currentSheetAnimation = null;
                  if (var3.onClickListener != null) {
                     BottomSheet.this.onClickListener.onClick(BottomSheet.this, var1);
                  }

                  AndroidUtilities.runOnUIThread(new _$$Lambda$BottomSheet$5$CikgvDyZEWn0favL4ZqbmH9PuGE(this));
               }

            }
         });
         var2.start();
         this.currentSheetAnimation = var2;
      }
   }

   public FrameLayout getContainer() {
      return this.container;
   }

   protected int getLeftInset() {
      WindowInsets var1 = this.lastInsets;
      return var1 != null && VERSION.SDK_INT >= 21 ? var1.getSystemWindowInsetLeft() : 0;
   }

   protected int getRightInset() {
      WindowInsets var1 = this.lastInsets;
      return var1 != null && VERSION.SDK_INT >= 21 ? var1.getSystemWindowInsetRight() : 0;
   }

   public ViewGroup getSheetContainer() {
      return this.containerView;
   }

   public int getTag() {
      return this.tag;
   }

   public TextView getTitleView() {
      return this.titleView;
   }

   public boolean isDismissed() {
      return this.dismissed;
   }

   // $FF: synthetic method
   public WindowInsets lambda$new$0$BottomSheet(View var1, WindowInsets var2) {
      this.lastInsets = var2;
      var1.requestLayout();
      return var2.consumeSystemWindowInsets();
   }

   // $FF: synthetic method
   public void lambda$onCreate$2$BottomSheet(View var1) {
      this.dismissWithButtonClick((Integer)var1.getTag());
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
   }

   public void onConfigurationChanged(Configuration var1) {
   }

   public void onContainerDraw(Canvas var1) {
   }

   protected boolean onContainerTouchEvent(MotionEvent var1) {
      return false;
   }

   protected void onContainerTranslationYChanged(float var1) {
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      Window var10 = this.getWindow();
      var10.setWindowAnimations(2131624098);
      this.setContentView(this.container, new android.view.ViewGroup.LayoutParams(-1, -1));
      int var3;
      int var4;
      int var7;
      if (this.containerView == null) {
         this.containerView = new FrameLayout(this.getContext()) {
            public boolean hasOverlappingRendering() {
               return false;
            }

            public void setTranslationY(float var1) {
               super.setTranslationY(var1);
               BottomSheet.this.onContainerTranslationYChanged(var1);
            }
         };
         this.containerView.setBackgroundDrawable(this.shadowDrawable);
         ViewGroup var2 = this.containerView;
         var3 = this.backgroundPaddingLeft;
         if (this.applyTopPadding) {
            var4 = AndroidUtilities.dp(8.0F);
         } else {
            var4 = 0;
         }

         int var5 = this.backgroundPaddingTop;
         int var6 = this.backgroundPaddingLeft;
         if (this.applyBottomPadding) {
            var7 = AndroidUtilities.dp(8.0F);
         } else {
            var7 = 0;
         }

         var2.setPadding(var3, var4 + var5 - 1, var6, var7);
      }

      this.containerView.setVisibility(4);
      this.container.addView(this.containerView, 0, LayoutHelper.createFrame(-1, -2, 80));
      byte var16;
      if (this.title != null) {
         this.titleView = new TextView(this.getContext());
         this.titleView.setLines(1);
         this.titleView.setSingleLine(true);
         this.titleView.setText(this.title);
         this.titleView.setTextColor(Theme.getColor("dialogTextGray2"));
         this.titleView.setTextSize(1, 16.0F);
         this.titleView.setEllipsize(TruncateAt.MIDDLE);
         this.titleView.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(16.0F), AndroidUtilities.dp(8.0F));
         this.titleView.setGravity(16);
         this.containerView.addView(this.titleView, LayoutHelper.createFrame(-1, 48.0F));
         this.titleView.setOnTouchListener(_$$Lambda$BottomSheet$bysjO3P7kPXgYfq_9zd4_H2r0_8.INSTANCE);
         var16 = 48;
      } else {
         var16 = 0;
      }

      View var11 = this.customView;
      if (var11 != null) {
         if (var11.getParent() != null) {
            ((ViewGroup)this.customView.getParent()).removeView(this.customView);
         }

         this.containerView.addView(this.customView, LayoutHelper.createFrame(-1, -2.0F, 51, 0.0F, (float)var16, 0.0F, 0.0F));
      } else if (this.items != null) {
         byte var14 = 0;
         var7 = var16;
         var4 = var14;

         while(true) {
            CharSequence[] var12 = this.items;
            if (var4 >= var12.length) {
               break;
            }

            if (var12[var4] != null) {
               BottomSheet.BottomSheetCell var8 = new BottomSheet.BottomSheetCell(this.getContext(), 0);
               CharSequence var9 = this.items[var4];
               int[] var13 = this.itemIcons;
               if (var13 != null) {
                  var3 = var13[var4];
               } else {
                  var3 = 0;
               }

               var8.setTextAndIcon(var9, var3);
               this.containerView.addView(var8, LayoutHelper.createFrame(-1, 48.0F, 51, 0.0F, (float)var7, 0.0F, 0.0F));
               var7 += 48;
               var8.setTag(var4);
               var8.setOnClickListener(new _$$Lambda$BottomSheet$6IWrsZfWA7fvlM9_8brqhUJi_uM(this));
               this.itemViews.add(var8);
            }

            ++var4;
         }
      }

      LayoutParams var15 = var10.getAttributes();
      var15.width = -1;
      var15.gravity = 51;
      var15.dimAmount = 0.0F;
      var15.flags &= -3;
      if (this.focusable) {
         var15.softInputMode = 16;
      } else {
         var15.flags |= 131072;
      }

      if (this.isFullscreen) {
         if (VERSION.SDK_INT >= 21) {
            var15.flags |= -2147417856;
         }

         var15.flags |= 1024;
         this.container.setSystemUiVisibility(1284);
      }

      var15.height = -1;
      if (VERSION.SDK_INT >= 28) {
         var15.layoutInDisplayCutoutMode = 1;
      }

      var10.setAttributes(var15);
   }

   protected boolean onCustomCloseAnimation() {
      return false;
   }

   protected boolean onCustomLayout(View var1, int var2, int var3, int var4, int var5) {
      return false;
   }

   protected boolean onCustomMeasure(View var1, int var2, int var3) {
      return false;
   }

   protected boolean onCustomOpenAnimation() {
      return false;
   }

   public void setAllowDrawContent(boolean var1) {
      if (this.allowDrawContent != var1) {
         this.allowDrawContent = var1;
         BottomSheet.ContainerView var2 = this.container;
         ColorDrawable var3;
         if (this.allowDrawContent) {
            var3 = this.backDrawable;
         } else {
            var3 = null;
         }

         var2.setBackgroundDrawable(var3);
         this.container.invalidate();
      }

   }

   public void setAllowNestedScroll(boolean var1) {
      this.allowNestedScroll = var1;
      if (!this.allowNestedScroll) {
         this.containerView.setTranslationY(0.0F);
      }

   }

   public void setApplyBottomPadding(boolean var1) {
      this.applyBottomPadding = var1;
   }

   public void setApplyTopPadding(boolean var1) {
      this.applyTopPadding = var1;
   }

   public void setBackgroundColor(int var1) {
      this.shadowDrawable.setColorFilter(var1, Mode.MULTIPLY);
   }

   public void setCustomView(View var1) {
      this.customView = var1;
   }

   public void setDelegate(BottomSheet.BottomSheetDelegateInterface var1) {
      this.delegate = var1;
   }

   public void setDimBehind(boolean var1) {
      this.dimBehind = var1;
   }

   public void setItemColor(int var1, int var2, int var3) {
      if (var1 >= 0 && var1 < this.itemViews.size()) {
         BottomSheet.BottomSheetCell var4 = (BottomSheet.BottomSheetCell)this.itemViews.get(var1);
         var4.textView.setTextColor(var2);
         var4.imageView.setColorFilter(new PorterDuffColorFilter(var3, Mode.MULTIPLY));
      }

   }

   public void setItemText(int var1, CharSequence var2) {
      if (var1 >= 0 && var1 < this.itemViews.size()) {
         ((BottomSheet.BottomSheetCell)this.itemViews.get(var1)).textView.setText(var2);
      }

   }

   public void setItems(CharSequence[] var1, int[] var2, OnClickListener var3) {
      this.items = var1;
      this.itemIcons = var2;
      this.onClickListener = var3;
   }

   public void setShowWithoutAnimation(boolean var1) {
      this.showWithoutAnimation = var1;
   }

   public void setTitle(CharSequence var1) {
      this.title = var1;
   }

   public void setTitleColor(int var1) {
      TextView var2 = this.titleView;
      if (var2 != null) {
         var2.setTextColor(var1);
      }
   }

   public void show() {
      super.show();
      if (this.focusable) {
         this.getWindow().setSoftInputMode(16);
      }

      byte var1 = 0;
      this.dismissed = false;
      this.cancelSheetAnimation();
      this.containerView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x + this.backgroundPaddingLeft * 2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
      if (this.showWithoutAnimation) {
         ColorDrawable var4 = this.backDrawable;
         if (this.dimBehind) {
            var1 = 51;
         }

         var4.setAlpha(var1);
         this.containerView.setTranslationY(0.0F);
      } else {
         this.backDrawable.setAlpha(0);
         if (VERSION.SDK_INT >= 18) {
            this.layoutCount = 2;
            ViewGroup var2 = this.containerView;
            var2.setTranslationY((float)var2.getMeasuredHeight());
            Runnable var3 = new Runnable() {
               public void run() {
                  if (BottomSheet.this.startAnimationRunnable == this && !BottomSheet.this.dismissed) {
                     BottomSheet.this.startAnimationRunnable = null;
                     BottomSheet.this.startOpenAnimation();
                  }

               }
            };
            this.startAnimationRunnable = var3;
            AndroidUtilities.runOnUIThread(var3, 150L);
         } else {
            this.startOpenAnimation();
         }

      }
   }

   public static class BottomSheetCell extends FrameLayout {
      private ImageView imageView;
      private TextView textView;

      public BottomSheetCell(Context var1, int var2) {
         super(var1);
         this.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.imageView = new ImageView(var1);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogIcon"), Mode.MULTIPLY));
         ImageView var3 = this.imageView;
         boolean var4 = LocaleController.isRTL;
         byte var5 = 5;
         byte var6;
         if (var4) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         this.addView(var3, LayoutHelper.createFrame(56, 48, var6 | 16));
         this.textView = new TextView(var1);
         this.textView.setLines(1);
         this.textView.setSingleLine(true);
         this.textView.setGravity(1);
         this.textView.setEllipsize(TruncateAt.END);
         if (var2 == 0) {
            this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
            this.textView.setTextSize(1, 16.0F);
            TextView var7 = this.textView;
            byte var8;
            if (LocaleController.isRTL) {
               var8 = var5;
            } else {
               var8 = 3;
            }

            this.addView(var7, LayoutHelper.createFrame(-2, -2, var8 | 16));
         } else if (var2 == 1) {
            this.textView.setGravity(17);
            this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
            this.textView.setTextSize(1, 14.0F);
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.addView(this.textView, LayoutHelper.createFrame(-1, -1.0F));
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
      }

      public void setGravity(int var1) {
         this.textView.setGravity(var1);
      }

      public void setTextAndIcon(CharSequence var1, int var2) {
         this.textView.setText(var1);
         float var3 = 16.0F;
         if (var2 != 0) {
            this.imageView.setImageResource(var2);
            this.imageView.setVisibility(0);
            TextView var5 = this.textView;
            float var4;
            if (LocaleController.isRTL) {
               var4 = 16.0F;
            } else {
               var4 = 72.0F;
            }

            var2 = AndroidUtilities.dp(var4);
            var4 = var3;
            if (LocaleController.isRTL) {
               var4 = 72.0F;
            }

            var5.setPadding(var2, 0, AndroidUtilities.dp(var4), 0);
         } else {
            this.imageView.setVisibility(4);
            this.textView.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(16.0F), 0);
         }

      }

      public void setTextColor(int var1) {
         this.textView.setTextColor(var1);
      }
   }

   public static class BottomSheetDelegate implements BottomSheet.BottomSheetDelegateInterface {
      public boolean canDismiss() {
         return true;
      }

      public void onOpenAnimationEnd() {
      }

      public void onOpenAnimationStart() {
      }
   }

   public interface BottomSheetDelegateInterface {
      boolean canDismiss();

      void onOpenAnimationEnd();

      void onOpenAnimationStart();
   }

   public static class Builder {
      private BottomSheet bottomSheet;

      public Builder(Context var1) {
         this.bottomSheet = new BottomSheet(var1, false, 0);
      }

      public Builder(Context var1, boolean var2) {
         this.bottomSheet = new BottomSheet(var1, var2, 0);
      }

      public Builder(Context var1, boolean var2, int var3) {
         this.bottomSheet = new BottomSheet(var1, var2, var3);
      }

      public BottomSheet create() {
         return this.bottomSheet;
      }

      public Runnable getDismissRunnable() {
         return this.bottomSheet.dismissRunnable;
      }

      public BottomSheet.Builder setApplyBottomPadding(boolean var1) {
         this.bottomSheet.applyBottomPadding = var1;
         return this;
      }

      public BottomSheet.Builder setApplyTopPadding(boolean var1) {
         this.bottomSheet.applyTopPadding = var1;
         return this;
      }

      public BottomSheet.Builder setCustomView(View var1) {
         this.bottomSheet.customView = var1;
         return this;
      }

      public BottomSheet.Builder setDelegate(BottomSheet.BottomSheetDelegate var1) {
         this.bottomSheet.setDelegate(var1);
         return this;
      }

      public BottomSheet setDimBehind(boolean var1) {
         this.bottomSheet.dimBehind = var1;
         return this.bottomSheet;
      }

      public BottomSheet.Builder setItems(CharSequence[] var1, OnClickListener var2) {
         this.bottomSheet.items = var1;
         this.bottomSheet.onClickListener = var2;
         return this;
      }

      public BottomSheet.Builder setItems(CharSequence[] var1, int[] var2, OnClickListener var3) {
         this.bottomSheet.items = var1;
         this.bottomSheet.itemIcons = var2;
         this.bottomSheet.onClickListener = var3;
         return this;
      }

      public BottomSheet.Builder setTag(int var1) {
         this.bottomSheet.tag = var1;
         return this;
      }

      public BottomSheet.Builder setTitle(CharSequence var1) {
         this.bottomSheet.title = var1;
         return this;
      }

      public BottomSheet setUseFullWidth(boolean var1) {
         BottomSheet var2 = this.bottomSheet;
         var2.fullWidth = var1;
         return var2;
      }

      public BottomSheet setUseFullscreen(boolean var1) {
         BottomSheet var2 = this.bottomSheet;
         var2.isFullscreen = var1;
         return var2;
      }

      public BottomSheet.Builder setUseHardwareLayer(boolean var1) {
         this.bottomSheet.useHardwareLayer = var1;
         return this;
      }

      public BottomSheet show() {
         this.bottomSheet.show();
         return this.bottomSheet;
      }
   }

   protected class ContainerView extends FrameLayout implements NestedScrollingParent {
      private AnimatorSet currentAnimation = null;
      private boolean maybeStartTracking = false;
      private NestedScrollingParentHelper nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
      private boolean startedTracking = false;
      private int startedTrackingPointerId = -1;
      private int startedTrackingX;
      private int startedTrackingY;
      private VelocityTracker velocityTracker = null;

      public ContainerView(Context var2) {
         super(var2);
      }

      private void cancelCurrentAnimation() {
         AnimatorSet var1 = this.currentAnimation;
         if (var1 != null) {
            var1.cancel();
            this.currentAnimation = null;
         }

      }

      private void checkDismiss(float var1, float var2) {
         float var3 = BottomSheet.this.containerView.getTranslationY();
         boolean var4;
         if ((var3 >= AndroidUtilities.getPixelsInCM(0.8F, false) || var2 >= 3500.0F && Math.abs(var2) >= Math.abs(var1)) && (var2 >= 0.0F || Math.abs(var2) < 3500.0F)) {
            var4 = false;
         } else {
            var4 = true;
         }

         if (!var4) {
            boolean var5 = BottomSheet.this.allowCustomAnimation;
            BottomSheet.this.allowCustomAnimation = false;
            BottomSheet.this.useFastDismiss = true;
            BottomSheet.this.dismiss();
            BottomSheet.this.allowCustomAnimation = var5;
         } else {
            this.currentAnimation = new AnimatorSet();
            this.currentAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(BottomSheet.this.containerView, "translationY", new float[]{0.0F})});
            this.currentAnimation.setDuration((long)((int)(var3 / AndroidUtilities.getPixelsInCM(0.8F, false) * 150.0F)));
            this.currentAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.currentAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (ContainerView.this.currentAnimation != null && ContainerView.this.currentAnimation.equals(var1)) {
                     ContainerView.this.currentAnimation = null;
                  }

               }
            });
            this.currentAnimation.start();
         }

      }

      public int getNestedScrollAxes() {
         return this.nestedScrollingParentHelper.getNestedScrollAxes();
      }

      public boolean hasOverlappingRendering() {
         return false;
      }

      protected void onDraw(Canvas var1) {
         super.onDraw(var1);
         BottomSheet.this.onContainerDraw(var1);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         return BottomSheet.this.canDismissWithSwipe() ? this.processTouchEvent(var1, true) : super.onInterceptTouchEvent(var1);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         BottomSheet.access$710(BottomSheet.this);
         BottomSheet var6 = BottomSheet.this;
         int var7;
         int var8;
         int var9;
         if (var6.containerView != null) {
            if (var6.lastInsets != null && VERSION.SDK_INT >= 21) {
               var2 += BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
               var7 = var4 - BottomSheet.this.lastInsets.getSystemWindowInsetRight();
               var4 = var2;
               var2 = var7;
            } else {
               var7 = var2;
               var2 = var4;
               var4 = var7;
            }

            var8 = var5 - var3 - BottomSheet.this.containerView.getMeasuredHeight();
            var9 = (var2 - var4 - BottomSheet.this.containerView.getMeasuredWidth()) / 2;
            var7 = var9;
            if (BottomSheet.this.lastInsets != null) {
               var7 = var9;
               if (VERSION.SDK_INT >= 21) {
                  var7 = var9 + BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
               }
            }

            ViewGroup var15 = BottomSheet.this.containerView;
            var15.layout(var7, var8, var15.getMeasuredWidth() + var7, BottomSheet.this.containerView.getMeasuredHeight() + var8);
            var7 = var4;
            var9 = var2;
         } else {
            var9 = var4;
            var7 = var2;
         }

         int var10 = this.getChildCount();

         for(var8 = 0; var8 < var10; ++var8) {
            View var16 = this.getChildAt(var8);
            if (var16.getVisibility() != 8) {
               BottomSheet var11 = BottomSheet.this;
               if (var16 != var11.containerView && !var11.onCustomLayout(var16, var7, var3, var9, var5)) {
                  android.widget.FrameLayout.LayoutParams var17 = (android.widget.FrameLayout.LayoutParams)var16.getLayoutParams();
                  int var12 = var16.getMeasuredWidth();
                  int var13 = var16.getMeasuredHeight();
                  var4 = var17.gravity;
                  var2 = var4;
                  if (var4 == -1) {
                     var2 = 51;
                  }

                  int var14;
                  label76: {
                     var14 = var2 & 112;
                     var2 = var2 & 7 & 7;
                     if (var2 != 1) {
                        if (var2 != 5) {
                           var2 = var17.leftMargin;
                           break label76;
                        }

                        var4 = var9 - var12;
                        var2 = var17.rightMargin;
                     } else {
                        var4 = (var9 - var7 - var12) / 2 + var17.leftMargin;
                        var2 = var17.rightMargin;
                     }

                     var2 = var4 - var2;
                  }

                  label70: {
                     if (var14 != 16) {
                        if (var14 == 48) {
                           var4 = var17.topMargin;
                           break label70;
                        }

                        if (var14 != 80) {
                           var4 = var17.topMargin;
                           break label70;
                        }

                        var14 = var5 - var3 - var13;
                        var4 = var17.bottomMargin;
                     } else {
                        var14 = (var5 - var3 - var13) / 2 + var17.topMargin;
                        var4 = var17.bottomMargin;
                     }

                     var4 = var14 - var4;
                  }

                  var14 = var2;
                  if (BottomSheet.this.lastInsets != null) {
                     var14 = var2;
                     if (VERSION.SDK_INT >= 21) {
                        var14 = var2 + BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
                     }
                  }

                  var16.layout(var14, var4, var12 + var14, var13 + var4);
               }
            }
         }

         if (BottomSheet.this.layoutCount == 0 && BottomSheet.this.startAnimationRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(BottomSheet.this.startAnimationRunnable);
            BottomSheet.this.startAnimationRunnable.run();
            BottomSheet.this.startAnimationRunnable = null;
         }

      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         var2 = MeasureSpec.getSize(var2);
         var1 = var2;
         if (BottomSheet.this.lastInsets != null) {
            var1 = var2;
            if (VERSION.SDK_INT >= 21) {
               var1 = var2 - BottomSheet.this.lastInsets.getSystemWindowInsetBottom();
            }
         }

         this.setMeasuredDimension(var3, var1);
         var2 = var3;
         if (BottomSheet.this.lastInsets != null) {
            var2 = var3;
            if (VERSION.SDK_INT >= 21) {
               var2 = var3 - (BottomSheet.this.lastInsets.getSystemWindowInsetRight() + BottomSheet.this.lastInsets.getSystemWindowInsetLeft());
            }
         }

         byte var4 = 0;
         boolean var8;
         if (var2 < var1) {
            var8 = true;
         } else {
            var8 = false;
         }

         BottomSheet var5 = BottomSheet.this;
         ViewGroup var6 = var5.containerView;
         if (var6 != null) {
            if (!var5.fullWidth) {
               if (AndroidUtilities.isTablet()) {
                  Point var9 = AndroidUtilities.displaySize;
                  var3 = MeasureSpec.makeMeasureSpec((int)((float)Math.min(var9.x, var9.y) * 0.8F) + BottomSheet.this.backgroundPaddingLeft * 2, 1073741824);
               } else {
                  if (var8) {
                     var3 = BottomSheet.this.backgroundPaddingLeft * 2 + var2;
                  } else {
                     var3 = (int)Math.max((float)var2 * 0.8F, (float)Math.min(AndroidUtilities.dp(480.0F), var2)) + BottomSheet.this.backgroundPaddingLeft * 2;
                  }

                  var3 = MeasureSpec.makeMeasureSpec(var3, 1073741824);
               }

               BottomSheet.this.containerView.measure(var3, MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
            } else {
               var6.measure(MeasureSpec.makeMeasureSpec(var5.backgroundPaddingLeft * 2 + var2, 1073741824), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
            }
         }

         int var7 = this.getChildCount();

         for(var3 = var4; var3 < var7; ++var3) {
            View var10 = this.getChildAt(var3);
            if (var10.getVisibility() != 8) {
               BottomSheet var11 = BottomSheet.this;
               if (var10 != var11.containerView && !var11.onCustomMeasure(var10, var2, var1)) {
                  this.measureChildWithMargins(var10, MeasureSpec.makeMeasureSpec(var2, 1073741824), 0, MeasureSpec.makeMeasureSpec(var1, 1073741824), 0);
               }
            }
         }

      }

      public boolean onNestedFling(View var1, float var2, float var3, boolean var4) {
         return false;
      }

      public boolean onNestedPreFling(View var1, float var2, float var3) {
         return false;
      }

      public void onNestedPreScroll(View var1, int var2, int var3, int[] var4) {
         if (!BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll) {
            this.cancelCurrentAnimation();
            float var5 = BottomSheet.this.containerView.getTranslationY();
            if (var5 > 0.0F && var3 > 0) {
               float var6 = var5 - (float)var3;
               var4[1] = var3;
               var5 = var6;
               if (var6 < 0.0F) {
                  var5 = 0.0F;
               }

               BottomSheet.this.containerView.setTranslationY(var5);
            }
         }

      }

      public void onNestedScroll(View var1, int var2, int var3, int var4, int var5) {
         if (!BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll) {
            this.cancelCurrentAnimation();
            if (var5 != 0) {
               float var6 = BottomSheet.this.containerView.getTranslationY() - (float)var5;
               float var7 = var6;
               if (var6 < 0.0F) {
                  var7 = 0.0F;
               }

               BottomSheet.this.containerView.setTranslationY(var7);
            }
         }

      }

      public void onNestedScrollAccepted(View var1, View var2, int var3) {
         this.nestedScrollingParentHelper.onNestedScrollAccepted(var1, var2, var3);
         if (!BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll) {
            this.cancelCurrentAnimation();
         }

      }

      public boolean onStartNestedScroll(View var1, View var2, int var3) {
         var2 = BottomSheet.this.nestedScrollChild;
         boolean var4;
         if ((var2 == null || var1 == var2) && !BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll && var3 == 2 && !BottomSheet.this.canDismissWithSwipe()) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      public void onStopNestedScroll(View var1) {
         this.nestedScrollingParentHelper.onStopNestedScroll(var1);
         if (!BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll) {
            BottomSheet.this.containerView.getTranslationY();
            this.checkDismiss(0.0F, 0.0F);
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         return this.processTouchEvent(var1, false);
      }

      boolean processTouchEvent(MotionEvent var1, boolean var2) {
         boolean var3 = BottomSheet.this.dismissed;
         boolean var4 = false;
         if (var3) {
            return false;
         } else if (BottomSheet.this.onContainerTouchEvent(var1)) {
            return true;
         } else {
            VelocityTracker var8;
            if (BottomSheet.this.canDismissWithTouchOutside() && var1 != null && (var1.getAction() == 0 || var1.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking && var1.getPointerCount() == 1) {
               this.startedTrackingX = (int)var1.getX();
               this.startedTrackingY = (int)var1.getY();
               if (this.startedTrackingY < BottomSheet.this.containerView.getTop() || this.startedTrackingX < BottomSheet.this.containerView.getLeft() || this.startedTrackingX > BottomSheet.this.containerView.getRight()) {
                  BottomSheet.this.dismiss();
                  return true;
               }

               this.startedTrackingPointerId = var1.getPointerId(0);
               this.maybeStartTracking = true;
               this.cancelCurrentAnimation();
               var8 = this.velocityTracker;
               if (var8 != null) {
                  var8.clear();
               }
            } else {
               float var5 = 0.0F;
               if (var1 != null && var1.getAction() == 2 && var1.getPointerId(0) == this.startedTrackingPointerId) {
                  if (this.velocityTracker == null) {
                     this.velocityTracker = VelocityTracker.obtain();
                  }

                  float var6 = (float)Math.abs((int)(var1.getX() - (float)this.startedTrackingX));
                  float var7 = (float)((int)var1.getY() - this.startedTrackingY);
                  this.velocityTracker.addMovement(var1);
                  if (this.maybeStartTracking && !this.startedTracking && var7 > 0.0F && var7 / 3.0F > Math.abs(var6) && Math.abs(var7) >= (float)BottomSheet.this.touchSlop) {
                     this.startedTrackingY = (int)var1.getY();
                     this.maybeStartTracking = false;
                     this.startedTracking = true;
                     this.requestDisallowInterceptTouchEvent(true);
                  } else if (this.startedTracking) {
                     var6 = BottomSheet.this.containerView.getTranslationY() + var7;
                     if (var6 >= 0.0F) {
                        var5 = var6;
                     }

                     BottomSheet.this.containerView.setTranslationY(var5);
                     this.startedTrackingY = (int)var1.getY();
                  }
               } else if (var1 == null || var1 != null && var1.getPointerId(0) == this.startedTrackingPointerId && (var1.getAction() == 3 || var1.getAction() == 1 || var1.getAction() == 6)) {
                  if (this.velocityTracker == null) {
                     this.velocityTracker = VelocityTracker.obtain();
                  }

                  this.velocityTracker.computeCurrentVelocity(1000);
                  var5 = BottomSheet.this.containerView.getTranslationY();
                  if (!this.startedTracking && var5 == 0.0F) {
                     this.maybeStartTracking = false;
                     this.startedTracking = false;
                  } else {
                     this.checkDismiss(this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                     this.startedTracking = false;
                  }

                  var8 = this.velocityTracker;
                  if (var8 != null) {
                     var8.recycle();
                     this.velocityTracker = null;
                  }

                  this.startedTrackingPointerId = -1;
               }
            }

            if ((var2 || !this.maybeStartTracking) && !this.startedTracking) {
               var2 = var4;
               if (BottomSheet.this.canDismissWithSwipe()) {
                  return var2;
               }
            }

            var2 = true;
            return var2;
         }
      }

      public void requestDisallowInterceptTouchEvent(boolean var1) {
         if (this.maybeStartTracking && !this.startedTracking) {
            this.onTouchEvent((MotionEvent)null);
         }

         super.requestDisallowInterceptTouchEvent(var1);
      }
   }
}
