package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LineProgressView;
import org.telegram.ui.Components.RadialProgressView;

public class AlertDialog extends Dialog implements Callback {
   private Rect backgroundPaddings = new Rect();
   protected FrameLayout buttonsLayout;
   private boolean canCacnel = true;
   private AlertDialog cancelDialog;
   private ScrollView contentScrollView;
   private int currentProgress;
   private View customView;
   private int customViewOffset = 20;
   private boolean dismissDialogByButtons = true;
   private Runnable dismissRunnable = new _$$Lambda$H9iyBEO4Zihg11d8XSg_qvJnAGk(this);
   private int[] itemIcons;
   private ArrayList itemViews = new ArrayList();
   private CharSequence[] items;
   private int lastScreenWidth;
   private LineProgressView lineProgressView;
   private TextView lineProgressViewPercent;
   private CharSequence message;
   private TextView messageTextView;
   private boolean messageTextViewClickable = true;
   private OnClickListener negativeButtonListener;
   private CharSequence negativeButtonText;
   private OnClickListener neutralButtonListener;
   private CharSequence neutralButtonText;
   private OnClickListener onBackButtonListener;
   private OnCancelListener onCancelListener;
   private OnClickListener onClickListener;
   private OnDismissListener onDismissListener;
   private OnScrollChangedListener onScrollChangedListener;
   private OnClickListener positiveButtonListener;
   private CharSequence positiveButtonText;
   private FrameLayout progressViewContainer;
   private int progressViewStyle;
   private TextView progressViewTextView;
   private LinearLayout scrollContainer;
   private CharSequence secondTitle;
   private TextView secondTitleTextView;
   private BitmapDrawable[] shadow = new BitmapDrawable[2];
   private AnimatorSet[] shadowAnimation = new AnimatorSet[2];
   private Drawable shadowDrawable;
   private boolean[] shadowVisibility = new boolean[2];
   private CharSequence subtitle;
   private TextView subtitleTextView;
   private CharSequence title;
   private FrameLayout titleContainer;
   private TextView titleTextView;
   private int topBackgroundColor;
   private Drawable topDrawable;
   private int topHeight = 132;
   private ImageView topImageView;
   private int topResId;

   public AlertDialog(Context var1, int var2) {
      super(var1, 2131624225);
      if (var2 != 3) {
         this.shadowDrawable = var1.getResources().getDrawable(2131165776).mutate();
         this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(this.getThemeColor("dialogBackground"), Mode.MULTIPLY));
         this.shadowDrawable.getPadding(this.backgroundPaddings);
      }

      this.progressViewStyle = var2;
   }

   private boolean canTextInput(View var1) {
      if (var1.onCheckIsTextEditor()) {
         return true;
      } else if (!(var1 instanceof ViewGroup)) {
         return false;
      } else {
         ViewGroup var4 = (ViewGroup)var1;
         int var2 = var4.getChildCount();

         int var3;
         do {
            if (var2 <= 0) {
               return false;
            }

            var3 = var2 - 1;
            var2 = var3;
         } while(!this.canTextInput(var4.getChildAt(var3)));

         return true;
      }
   }

   private void runShadowAnimation(final int var1, boolean var2) {
      if (var2 && !this.shadowVisibility[var1] || !var2 && this.shadowVisibility[var1]) {
         this.shadowVisibility[var1] = var2;
         AnimatorSet[] var3 = this.shadowAnimation;
         if (var3[var1] != null) {
            var3[var1].cancel();
         }

         this.shadowAnimation[var1] = new AnimatorSet();
         BitmapDrawable[] var4 = this.shadow;
         if (var4[var1] != null) {
            AnimatorSet var7 = this.shadowAnimation[var1];
            BitmapDrawable var8 = var4[var1];
            short var5;
            if (var2) {
               var5 = 255;
            } else {
               var5 = 0;
            }

            var7.playTogether(new Animator[]{ObjectAnimator.ofInt(var8, "alpha", new int[]{var5})});
         }

         this.shadowAnimation[var1].setDuration(150L);
         this.shadowAnimation[var1].addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (AlertDialog.this.shadowAnimation[var1] != null && AlertDialog.this.shadowAnimation[var1].equals(var1x)) {
                  AlertDialog.this.shadowAnimation[var1] = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (AlertDialog.this.shadowAnimation[var1] != null && AlertDialog.this.shadowAnimation[var1].equals(var1x)) {
                  AlertDialog.this.shadowAnimation[var1] = null;
               }

            }
         });

         try {
            this.shadowAnimation[var1].start();
         } catch (Exception var6) {
            FileLog.e((Throwable)var6);
         }
      }

   }

   private void showCancelAlert() {
      if (this.canCacnel && this.cancelDialog == null) {
         AlertDialog.Builder var1 = new AlertDialog.Builder(this.getContext());
         var1.setTitle(LocaleController.getString("AppName", 2131558635));
         var1.setMessage(LocaleController.getString("StopLoading", 2131560827));
         var1.setPositiveButton(LocaleController.getString("WaitMore", 2131561101), (OnClickListener)null);
         var1.setNegativeButton(LocaleController.getString("Stop", 2131560820), new _$$Lambda$AlertDialog$1zFp_sikyCYaQ1aMdMAPeCc_86g(this));
         var1.setOnDismissListener(new _$$Lambda$AlertDialog$_jIiJ2tOQUco_X_BniQD16XJ3QE(this));
         this.cancelDialog = var1.show();
      }

   }

   private void updateLineProgressTextView() {
      this.lineProgressViewPercent.setText(String.format("%d%%", this.currentProgress));
   }

   public void dismiss() {
      AlertDialog var1 = this.cancelDialog;
      if (var1 != null) {
         var1.dismiss();
      }

      try {
         super.dismiss();
      } catch (Throwable var2) {
      }

   }

   public View getButton(int var1) {
      FrameLayout var2 = this.buttonsLayout;
      return var2 != null ? var2.findViewWithTag(var1) : null;
   }

   protected int getThemeColor(String var1) {
      return Theme.getColor(var1);
   }

   public void invalidateDrawable(Drawable var1) {
      this.contentScrollView.invalidate();
      this.scrollContainer.invalidate();
   }

   // $FF: synthetic method
   public void lambda$onCreate$0$AlertDialog(View var1) {
      OnClickListener var2 = this.onClickListener;
      if (var2 != null) {
         var2.onClick(this, (Integer)var1.getTag());
      }

      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$onCreate$1$AlertDialog(View var1) {
      OnClickListener var2 = this.positiveButtonListener;
      if (var2 != null) {
         var2.onClick(this, -1);
      }

      if (this.dismissDialogByButtons) {
         this.dismiss();
      }

   }

   // $FF: synthetic method
   public void lambda$onCreate$2$AlertDialog(View var1) {
      OnClickListener var2 = this.negativeButtonListener;
      if (var2 != null) {
         var2.onClick(this, -2);
      }

      if (this.dismissDialogByButtons) {
         this.cancel();
      }

   }

   // $FF: synthetic method
   public void lambda$onCreate$3$AlertDialog(View var1) {
      OnClickListener var2 = this.neutralButtonListener;
      if (var2 != null) {
         var2.onClick(this, -2);
      }

      if (this.dismissDialogByButtons) {
         this.dismiss();
      }

   }

   // $FF: synthetic method
   public void lambda$showCancelAlert$4$AlertDialog(DialogInterface var1, int var2) {
      OnCancelListener var3 = this.onCancelListener;
      if (var3 != null) {
         var3.onCancel(this);
      }

      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$showCancelAlert$5$AlertDialog(DialogInterface var1) {
      this.cancelDialog = null;
   }

   public void onBackPressed() {
      super.onBackPressed();
      OnClickListener var1 = this.onBackButtonListener;
      if (var1 != null) {
         var1.onClick(this, -2);
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      LinearLayout var10 = new LinearLayout(this.getContext()) {
         private boolean inLayout;

         public boolean hasOverlappingRendering() {
            return false;
         }

         // $FF: synthetic method
         public void lambda$onLayout$1$AlertDialog$1() {
            AlertDialog var1 = AlertDialog.this;
            TextView var2 = var1.titleTextView;
            boolean var3 = false;
            boolean var4;
            if (var2 != null && AlertDialog.this.contentScrollView.getScrollY() > AlertDialog.this.scrollContainer.getTop()) {
               var4 = true;
            } else {
               var4 = false;
            }

            var1.runShadowAnimation(0, var4);
            var1 = AlertDialog.this;
            var4 = var3;
            if (var1.buttonsLayout != null) {
               var4 = var3;
               if (var1.contentScrollView.getScrollY() + AlertDialog.this.contentScrollView.getHeight() < AlertDialog.this.scrollContainer.getBottom()) {
                  var4 = true;
               }
            }

            var1.runShadowAnimation(1, var4);
            AlertDialog.this.contentScrollView.invalidate();
         }

         // $FF: synthetic method
         public void lambda$onMeasure$0$AlertDialog$1() {
            AlertDialog.this.lastScreenWidth = AndroidUtilities.displaySize.x;
            int var1 = AndroidUtilities.displaySize.x;
            int var2 = AndroidUtilities.dp(56.0F);
            int var3;
            if (AndroidUtilities.isTablet()) {
               if (AndroidUtilities.isSmallTablet()) {
                  var3 = AndroidUtilities.dp(446.0F);
               } else {
                  var3 = AndroidUtilities.dp(496.0F);
               }
            } else {
               var3 = AndroidUtilities.dp(356.0F);
            }

            Window var4 = AlertDialog.this.getWindow();
            LayoutParams var5 = new LayoutParams();
            var5.copyFrom(var4.getAttributes());
            var5.width = Math.min(var3, var1 - var2) + AlertDialog.this.backgroundPaddings.left + AlertDialog.this.backgroundPaddings.right;
            var4.setAttributes(var5);
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (AlertDialog.this.progressViewStyle == 3) {
               AlertDialog.this.showCancelAlert();
               return false;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (AlertDialog.this.progressViewStyle == 3) {
               var2 = (var4 - var2 - AlertDialog.this.progressViewContainer.getMeasuredWidth()) / 2;
               var3 = (var5 - var3 - AlertDialog.this.progressViewContainer.getMeasuredHeight()) / 2;
               AlertDialog.this.progressViewContainer.layout(var2, var3, AlertDialog.this.progressViewContainer.getMeasuredWidth() + var2, AlertDialog.this.progressViewContainer.getMeasuredHeight() + var3);
            } else if (AlertDialog.this.contentScrollView != null) {
               if (AlertDialog.this.onScrollChangedListener == null) {
                  AlertDialog.this.onScrollChangedListener = new _$$Lambda$AlertDialog$1$vvKcenyzvRwmFgV39QOFVkx4krI(this);
                  AlertDialog.this.contentScrollView.getViewTreeObserver().addOnScrollChangedListener(AlertDialog.this.onScrollChangedListener);
               }

               AlertDialog.this.onScrollChangedListener.onScrollChanged();
            }

         }

         protected void onMeasure(int var1, int var2) {
            if (AlertDialog.this.progressViewStyle == 3) {
               AlertDialog.this.progressViewContainer.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0F), 1073741824));
               this.setMeasuredDimension(MeasureSpec.getSize(var1), MeasureSpec.getSize(var2));
            } else {
               this.inLayout = true;
               int var3 = MeasureSpec.getSize(var1);
               int var4 = MeasureSpec.getSize(var2) - this.getPaddingTop() - this.getPaddingBottom();
               int var5 = var3 - this.getPaddingLeft() - this.getPaddingRight();
               int var6 = MeasureSpec.makeMeasureSpec(var5 - AndroidUtilities.dp(48.0F), 1073741824);
               int var7 = MeasureSpec.makeMeasureSpec(var5, 1073741824);
               FrameLayout var8 = AlertDialog.this.buttonsLayout;
               int var9;
               android.widget.LinearLayout.LayoutParams var11;
               if (var8 == null) {
                  var9 = var4;
               } else {
                  var9 = var8.getChildCount();

                  for(var1 = 0; var1 < var9; ++var1) {
                     View var10 = AlertDialog.this.buttonsLayout.getChildAt(var1);
                     if (var10 instanceof TextView) {
                        ((TextView)var10).setMaxWidth(AndroidUtilities.dp((float)((var5 - AndroidUtilities.dp(24.0F)) / 2)));
                     }
                  }

                  AlertDialog.this.buttonsLayout.measure(var7, var2);
                  var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.buttonsLayout.getLayoutParams();
                  var9 = var4 - (AlertDialog.this.buttonsLayout.getMeasuredHeight() + var11.bottomMargin + var11.topMargin);
               }

               if (AlertDialog.this.secondTitleTextView != null) {
                  AlertDialog.this.secondTitleTextView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var6), Integer.MIN_VALUE), var2);
               }

               if (AlertDialog.this.titleTextView != null) {
                  if (AlertDialog.this.secondTitleTextView != null) {
                     AlertDialog.this.titleTextView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var6) - AlertDialog.this.secondTitleTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0F), 1073741824), var2);
                  } else {
                     AlertDialog.this.titleTextView.measure(var6, var2);
                  }
               }

               var1 = var9;
               if (AlertDialog.this.titleContainer != null) {
                  AlertDialog.this.titleContainer.measure(var6, var2);
                  var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.titleContainer.getLayoutParams();
                  var1 = var9 - (AlertDialog.this.titleContainer.getMeasuredHeight() + var11.bottomMargin + var11.topMargin);
               }

               var9 = var1;
               if (AlertDialog.this.subtitleTextView != null) {
                  AlertDialog.this.subtitleTextView.measure(var6, var2);
                  var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.subtitleTextView.getLayoutParams();
                  var9 = var1 - (AlertDialog.this.subtitleTextView.getMeasuredHeight() + var11.bottomMargin + var11.topMargin);
               }

               var1 = var9;
               if (AlertDialog.this.topImageView != null) {
                  AlertDialog.this.topImageView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)AlertDialog.this.topHeight), 1073741824));
                  var1 = var9 - (AlertDialog.this.topImageView.getMeasuredHeight() - AndroidUtilities.dp(8.0F));
               }

               if (AlertDialog.this.progressViewStyle == 0) {
                  var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.contentScrollView.getLayoutParams();
                  if (AlertDialog.this.customView != null) {
                     if (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8 && AlertDialog.this.items == null) {
                        var2 = AndroidUtilities.dp(16.0F);
                     } else {
                        var2 = 0;
                     }

                     var11.topMargin = var2;
                     if (AlertDialog.this.buttonsLayout == null) {
                        var2 = AndroidUtilities.dp(8.0F);
                     } else {
                        var2 = 0;
                     }

                     var11.bottomMargin = var2;
                  } else if (AlertDialog.this.items != null) {
                     if (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8) {
                        var2 = AndroidUtilities.dp(8.0F);
                     } else {
                        var2 = 0;
                     }

                     var11.topMargin = var2;
                     var11.bottomMargin = AndroidUtilities.dp(8.0F);
                  } else if (AlertDialog.this.messageTextView.getVisibility() == 0) {
                     if (AlertDialog.this.titleTextView == null) {
                        var2 = AndroidUtilities.dp(19.0F);
                     } else {
                        var2 = 0;
                     }

                     var11.topMargin = var2;
                     var11.bottomMargin = AndroidUtilities.dp(20.0F);
                  }

                  var1 -= var11.bottomMargin + var11.topMargin;
                  AlertDialog.this.contentScrollView.measure(var7, MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
                  var1 -= AlertDialog.this.contentScrollView.getMeasuredHeight();
               } else {
                  label95: {
                     if (AlertDialog.this.progressViewContainer != null) {
                        AlertDialog.this.progressViewContainer.measure(var6, MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
                        var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.progressViewContainer.getLayoutParams();
                        var9 = AlertDialog.this.progressViewContainer.getMeasuredHeight() + var11.bottomMargin;
                        var2 = var11.topMargin;
                     } else {
                        var2 = var1;
                        if (AlertDialog.this.messageTextView == null) {
                           break label95;
                        }

                        AlertDialog.this.messageTextView.measure(var6, MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
                        var2 = var1;
                        if (AlertDialog.this.messageTextView.getVisibility() == 8) {
                           break label95;
                        }

                        var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.messageTextView.getLayoutParams();
                        var9 = AlertDialog.this.messageTextView.getMeasuredHeight() + var11.bottomMargin;
                        var2 = var11.topMargin;
                     }

                     var2 = var1 - (var9 + var2);
                  }

                  var1 = var2;
                  if (AlertDialog.this.lineProgressView != null) {
                     AlertDialog.this.lineProgressView.measure(var6, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0F), 1073741824));
                     var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.lineProgressView.getLayoutParams();
                     var1 = var2 - (AlertDialog.this.lineProgressView.getMeasuredHeight() + var11.bottomMargin + var11.topMargin);
                     AlertDialog.this.lineProgressViewPercent.measure(var6, MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
                     var11 = (android.widget.LinearLayout.LayoutParams)AlertDialog.this.lineProgressViewPercent.getLayoutParams();
                     var1 -= AlertDialog.this.lineProgressViewPercent.getMeasuredHeight() + var11.bottomMargin + var11.topMargin;
                  }
               }

               this.setMeasuredDimension(var3, var4 - var1 + this.getPaddingTop() + this.getPaddingBottom());
               this.inLayout = false;
               if (AlertDialog.this.lastScreenWidth != AndroidUtilities.displaySize.x) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$AlertDialog$1$2il1lPevBw8X_3FhfSjXOpGmbaM(this));
               }
            }

         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (AlertDialog.this.progressViewStyle == 3) {
               AlertDialog.this.showCancelAlert();
               return false;
            } else {
               return super.onTouchEvent(var1);
            }
         }

         public void requestLayout() {
            if (!this.inLayout) {
               super.requestLayout();
            }
         }
      };
      var10.setOrientation(1);
      if (this.progressViewStyle == 3) {
         var10.setBackgroundDrawable((Drawable)null);
      } else {
         var10.setBackgroundDrawable(this.shadowDrawable);
      }

      boolean var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = true;
      } else {
         var2 = false;
      }

      var10.setFitsSystemWindows(var2);
      this.setContentView(var10);
      boolean var3;
      if (this.positiveButtonText == null && this.negativeButtonText == null && this.neutralButtonText == null) {
         var3 = false;
      } else {
         var3 = true;
      }

      if (this.topResId != 0 || this.topDrawable != null) {
         this.topImageView = new ImageView(this.getContext());
         Drawable var4 = this.topDrawable;
         if (var4 != null) {
            this.topImageView.setImageDrawable(var4);
         } else {
            this.topImageView.setImageResource(this.topResId);
         }

         this.topImageView.setScaleType(ScaleType.CENTER);
         this.topImageView.setBackgroundDrawable(this.getContext().getResources().getDrawable(2131165778));
         this.topImageView.getBackground().setColorFilter(new PorterDuffColorFilter(this.topBackgroundColor, Mode.MULTIPLY));
         this.topImageView.setPadding(0, 0, 0, 0);
         var10.addView(this.topImageView, LayoutHelper.createLinear(-1, this.topHeight, 51, -8, -8, 0, 0));
      }

      byte var5;
      FrameLayout var6;
      byte var7;
      TextView var14;
      if (this.title != null) {
         this.titleContainer = new FrameLayout(this.getContext());
         var10.addView(this.titleContainer, LayoutHelper.createLinear(-2, -2, 24.0F, 0.0F, 24.0F, 0.0F));
         this.titleTextView = new TextView(this.getContext());
         this.titleTextView.setText(this.title);
         this.titleTextView.setTextColor(this.getThemeColor("dialogTextBlack"));
         this.titleTextView.setTextSize(1, 20.0F);
         this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var14 = this.titleTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var14.setGravity(var5 | 48);
         var6 = this.titleContainer;
         var14 = this.titleTextView;
         if (LocaleController.isRTL) {
            var7 = 5;
         } else {
            var7 = 3;
         }

         if (this.subtitle != null) {
            var5 = 2;
         } else if (this.items != null) {
            var5 = 14;
         } else {
            var5 = 10;
         }

         var6.addView(var14, LayoutHelper.createFrame(-2, -2.0F, var7 | 48, 0.0F, 19.0F, 0.0F, (float)var5));
      }

      FrameLayout var15;
      TextView var16;
      if (this.secondTitle != null && this.title != null) {
         this.secondTitleTextView = new TextView(this.getContext());
         this.secondTitleTextView.setText(this.secondTitle);
         this.secondTitleTextView.setTextColor(this.getThemeColor("dialogTextGray3"));
         this.secondTitleTextView.setTextSize(1, 18.0F);
         var14 = this.secondTitleTextView;
         if (LocaleController.isRTL) {
            var5 = 3;
         } else {
            var5 = 5;
         }

         var14.setGravity(var5 | 48);
         var15 = this.titleContainer;
         var16 = this.secondTitleTextView;
         if (LocaleController.isRTL) {
            var5 = 3;
         } else {
            var5 = 5;
         }

         var15.addView(var16, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, 0.0F, 21.0F, 0.0F, 0.0F));
      }

      if (this.subtitle != null) {
         this.subtitleTextView = new TextView(this.getContext());
         this.subtitleTextView.setText(this.subtitle);
         this.subtitleTextView.setTextColor(this.getThemeColor("dialogIcon"));
         this.subtitleTextView.setTextSize(1, 14.0F);
         var14 = this.subtitleTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var14.setGravity(var5 | 48);
         var14 = this.subtitleTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         if (this.items != null) {
            var7 = 14;
         } else {
            var7 = 10;
         }

         var10.addView(var14, LayoutHelper.createLinear(-2, -2, var5 | 48, 24, 0, 24, var7));
      }

      if (this.progressViewStyle == 0) {
         this.shadow[0] = (BitmapDrawable)this.getContext().getResources().getDrawable(2131165407).mutate();
         this.shadow[1] = (BitmapDrawable)this.getContext().getResources().getDrawable(2131165408).mutate();
         this.shadow[0].setAlpha(0);
         this.shadow[1].setAlpha(0);
         this.shadow[0].setCallback(this);
         this.shadow[1].setCallback(this);
         this.contentScrollView = new ScrollView(this.getContext()) {
            protected boolean drawChild(Canvas var1, View var2, long var3) {
               boolean var5 = super.drawChild(var1, var2, var3);
               if (AlertDialog.this.shadow[0].getPaint().getAlpha() != 0) {
                  AlertDialog.this.shadow[0].setBounds(0, this.getScrollY(), this.getMeasuredWidth(), this.getScrollY() + AndroidUtilities.dp(3.0F));
                  AlertDialog.this.shadow[0].draw(var1);
               }

               if (AlertDialog.this.shadow[1].getPaint().getAlpha() != 0) {
                  AlertDialog.this.shadow[1].setBounds(0, this.getScrollY() + this.getMeasuredHeight() - AndroidUtilities.dp(3.0F), this.getMeasuredWidth(), this.getScrollY() + this.getMeasuredHeight());
                  AlertDialog.this.shadow[1].draw(var1);
               }

               return var5;
            }
         };
         this.contentScrollView.setVerticalScrollBarEnabled(false);
         AndroidUtilities.setScrollViewEdgeEffectColor(this.contentScrollView, this.getThemeColor("dialogScrollGlow"));
         var10.addView(this.contentScrollView, LayoutHelper.createLinear(-1, -2, 0.0F, 0.0F, 0.0F, 0.0F));
         this.scrollContainer = new LinearLayout(this.getContext());
         this.scrollContainer.setOrientation(1);
         this.contentScrollView.addView(this.scrollContainer, new android.widget.FrameLayout.LayoutParams(-1, -2));
      }

      this.messageTextView = new TextView(this.getContext());
      this.messageTextView.setTextColor(this.getThemeColor("dialogTextBlack"));
      this.messageTextView.setTextSize(1, 16.0F);
      this.messageTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
      this.messageTextView.setLinkTextColor(this.getThemeColor("dialogTextLink"));
      if (!this.messageTextViewClickable) {
         this.messageTextView.setClickable(false);
         this.messageTextView.setEnabled(false);
      }

      var14 = this.messageTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var14.setGravity(var5 | 48);
      int var27 = this.progressViewStyle;
      RadialProgressView var17;
      int var26;
      if (var27 == 1) {
         this.progressViewContainer = new FrameLayout(this.getContext());
         var15 = this.progressViewContainer;
         if (this.title == null) {
            var5 = 24;
         } else {
            var5 = 0;
         }

         var10.addView(var15, LayoutHelper.createLinear(-1, 44, 51, 23, var5, 23, 24));
         var17 = new RadialProgressView(this.getContext());
         var17.setProgressColor(this.getThemeColor("dialogProgressCircle"));
         var6 = this.progressViewContainer;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.addView(var17, LayoutHelper.createFrame(44, 44, var5 | 48));
         this.messageTextView.setLines(1);
         this.messageTextView.setEllipsize(TruncateAt.END);
         var15 = this.progressViewContainer;
         var16 = this.messageTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0;
         } else {
            var7 = 62;
         }

         float var8 = (float)var7;
         if (LocaleController.isRTL) {
            var7 = 62;
         } else {
            var7 = 0;
         }

         var15.addView(var16, LayoutHelper.createFrame(-2, -2.0F, var5 | 16, var8, 0.0F, (float)var7, 0.0F));
      } else if (var27 == 2) {
         var14 = this.messageTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         if (this.title == null) {
            var7 = 19;
         } else {
            var7 = 0;
         }

         var10.addView(var14, LayoutHelper.createLinear(-2, -2, var5 | 48, 24, var7, 24, 20));
         this.lineProgressView = new LineProgressView(this.getContext());
         this.lineProgressView.setProgress((float)this.currentProgress / 100.0F, false);
         this.lineProgressView.setProgressColor(this.getThemeColor("dialogLineProgress"));
         this.lineProgressView.setBackColor(this.getThemeColor("dialogLineProgressBackground"));
         var10.addView(this.lineProgressView, LayoutHelper.createLinear(-1, 4, 19, 24, 0, 24, 0));
         this.lineProgressViewPercent = new TextView(this.getContext());
         this.lineProgressViewPercent.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var14 = this.lineProgressViewPercent;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var14.setGravity(var5 | 48);
         this.lineProgressViewPercent.setTextColor(this.getThemeColor("dialogTextGray2"));
         this.lineProgressViewPercent.setTextSize(1, 14.0F);
         var14 = this.lineProgressViewPercent;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var10.addView(var14, LayoutHelper.createLinear(-2, -2, var5 | 48, 23, 4, 23, 24));
         this.updateLineProgressTextView();
      } else if (var27 == 3) {
         this.setCanceledOnTouchOutside(false);
         this.setCancelable(false);
         this.progressViewContainer = new FrameLayout(this.getContext());
         this.progressViewContainer.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0F), Theme.getColor("dialog_inlineProgressBackground")));
         var10.addView(this.progressViewContainer, LayoutHelper.createLinear(86, 86, 17));
         var17 = new RadialProgressView(this.getContext());
         var17.setProgressColor(this.getThemeColor("dialog_inlineProgress"));
         this.progressViewContainer.addView(var17, LayoutHelper.createLinear(86, 86));
      } else {
         LinearLayout var21 = this.scrollContainer;
         var16 = this.messageTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         if (this.customView == null && this.items == null) {
            var26 = 0;
         } else {
            var26 = this.customViewOffset;
         }

         var21.addView(var16, LayoutHelper.createLinear(-2, -2, var5 | 48, 24, 0, 24, var26));
      }

      if (!TextUtils.isEmpty(this.message)) {
         this.messageTextView.setText(this.message);
         this.messageTextView.setVisibility(0);
      } else {
         this.messageTextView.setVisibility(8);
      }

      if (this.items != null) {
         var27 = 0;

         while(true) {
            CharSequence[] var22 = this.items;
            if (var27 >= var22.length) {
               break;
            }

            if (var22[var27] != null) {
               AlertDialog.AlertDialogCell var23 = new AlertDialog.AlertDialogCell(this.getContext());
               CharSequence var18 = this.items[var27];
               int[] var9 = this.itemIcons;
               if (var9 != null) {
                  var26 = var9[var27];
               } else {
                  var26 = 0;
               }

               var23.setTextAndIcon(var18, var26);
               var23.setTag(var27);
               this.itemViews.add(var23);
               this.scrollContainer.addView(var23, LayoutHelper.createLinear(-1, 50));
               var23.setOnClickListener(new _$$Lambda$AlertDialog$iC26x2guh9hO2NrF8CCJRy6v4_w(this));
            }

            ++var27;
         }
      }

      View var24 = this.customView;
      if (var24 != null) {
         if (var24.getParent() != null) {
            ((ViewGroup)this.customView.getParent()).removeView(this.customView);
         }

         this.scrollContainer.addView(this.customView, LayoutHelper.createLinear(-1, -2));
      }

      if (var3) {
         this.buttonsLayout = new FrameLayout(this.getContext()) {
            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               var5 = this.getChildCount();
               int var6 = var4 - var2;
               View var7 = null;

               View var10;
               for(var2 = 0; var2 < var5; var7 = var10) {
                  View var8 = this.getChildAt(var2);
                  Integer var9 = (Integer)var8.getTag();
                  if (var9 != null) {
                     if (var9 == -1) {
                        if (LocaleController.isRTL) {
                           var8.layout(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + var8.getMeasuredWidth(), this.getPaddingTop() + var8.getMeasuredHeight());
                        } else {
                           var8.layout(var6 - this.getPaddingRight() - var8.getMeasuredWidth(), this.getPaddingTop(), var6 - this.getPaddingRight(), this.getPaddingTop() + var8.getMeasuredHeight());
                        }

                        var10 = var8;
                     } else if (var9 == -2) {
                        if (LocaleController.isRTL) {
                           var4 = this.getPaddingLeft();
                           var3 = var4;
                           if (var7 != null) {
                              var3 = var4 + var7.getMeasuredWidth() + AndroidUtilities.dp(8.0F);
                           }

                           var8.layout(var3, this.getPaddingTop(), var8.getMeasuredWidth() + var3, this.getPaddingTop() + var8.getMeasuredHeight());
                           var10 = var7;
                        } else {
                           var4 = var6 - this.getPaddingRight() - var8.getMeasuredWidth();
                           var3 = var4;
                           if (var7 != null) {
                              var3 = var4 - (var7.getMeasuredWidth() + AndroidUtilities.dp(8.0F));
                           }

                           var8.layout(var3, this.getPaddingTop(), var8.getMeasuredWidth() + var3, this.getPaddingTop() + var8.getMeasuredHeight());
                           var10 = var7;
                        }
                     } else {
                        var10 = var7;
                        if (var9 == -3) {
                           if (LocaleController.isRTL) {
                              var8.layout(var6 - this.getPaddingRight() - var8.getMeasuredWidth(), this.getPaddingTop(), var6 - this.getPaddingRight(), this.getPaddingTop() + var8.getMeasuredHeight());
                              var10 = var7;
                           } else {
                              var8.layout(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + var8.getMeasuredWidth(), this.getPaddingTop() + var8.getMeasuredHeight());
                              var10 = var7;
                           }
                        }
                     }
                  } else {
                     int var11 = var8.getMeasuredWidth();
                     int var12 = var8.getMeasuredHeight();
                     if (var7 != null) {
                        var3 = var7.getLeft() + (var7.getMeasuredWidth() - var11) / 2;
                        var4 = var7.getTop() + (var7.getMeasuredHeight() - var12) / 2;
                     } else {
                        var3 = 0;
                        var4 = 0;
                     }

                     var8.layout(var3, var4, var11 + var3, var12 + var4);
                     var10 = var7;
                  }

                  ++var2;
               }

            }

            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, var2);
               int var3 = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
               int var4 = this.getChildCount();
               var1 = 0;

               int var5;
               View var6;
               for(var5 = 0; var1 < var4; var5 = var2) {
                  var6 = this.getChildAt(var1);
                  var2 = var5;
                  if (var6 instanceof TextView) {
                     var2 = var5;
                     if (var6.getTag() != null) {
                        var2 = var5 + var6.getMeasuredWidth();
                     }
                  }

                  ++var1;
               }

               if (var5 > var3) {
                  View var7 = this.findViewWithTag(-2);
                  var6 = this.findViewWithTag(-3);
                  if (var7 != null && var6 != null) {
                     if (var7.getMeasuredWidth() < var6.getMeasuredWidth()) {
                        var6.measure(MeasureSpec.makeMeasureSpec(var6.getMeasuredWidth() - (var5 - var3), 1073741824), MeasureSpec.makeMeasureSpec(var6.getMeasuredHeight(), 1073741824));
                     } else {
                        var7.measure(MeasureSpec.makeMeasureSpec(var7.getMeasuredWidth() - (var5 - var3), 1073741824), MeasureSpec.makeMeasureSpec(var7.getMeasuredHeight(), 1073741824));
                     }
                  }
               }

            }
         };
         this.buttonsLayout.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F));
         var10.addView(this.buttonsLayout, LayoutHelper.createLinear(-1, 52));
         TextView var11;
         if (this.positiveButtonText != null) {
            var11 = new TextView(this.getContext()) {
               public void setEnabled(boolean var1) {
                  super.setEnabled(var1);
                  float var2;
                  if (var1) {
                     var2 = 1.0F;
                  } else {
                     var2 = 0.5F;
                  }

                  this.setAlpha(var2);
               }

               public void setTextColor(int var1) {
                  super.setTextColor(var1);
                  this.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(var1));
               }
            };
            var11.setMinWidth(AndroidUtilities.dp(64.0F));
            var11.setTag(-1);
            var11.setTextSize(1, 14.0F);
            var11.setTextColor(this.getThemeColor("dialogButton"));
            var11.setGravity(17);
            var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var11.setText(this.positiveButtonText.toString().toUpperCase());
            var11.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(this.getThemeColor("dialogButton")));
            var11.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
            this.buttonsLayout.addView(var11, LayoutHelper.createFrame(-2, 36, 53));
            var11.setOnClickListener(new _$$Lambda$AlertDialog$rp49coWDdM6PFZnr9_LTptCU2Ag(this));
         }

         if (this.negativeButtonText != null) {
            var11 = new TextView(this.getContext()) {
               public void setEnabled(boolean var1) {
                  super.setEnabled(var1);
                  float var2;
                  if (var1) {
                     var2 = 1.0F;
                  } else {
                     var2 = 0.5F;
                  }

                  this.setAlpha(var2);
               }

               public void setTextColor(int var1) {
                  super.setTextColor(var1);
                  this.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(var1));
               }
            };
            var11.setMinWidth(AndroidUtilities.dp(64.0F));
            var11.setTag(-2);
            var11.setTextSize(1, 14.0F);
            var11.setTextColor(this.getThemeColor("dialogButton"));
            var11.setGravity(17);
            var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var11.setEllipsize(TruncateAt.END);
            var11.setSingleLine(true);
            var11.setText(this.negativeButtonText.toString().toUpperCase());
            var11.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(this.getThemeColor("dialogButton")));
            var11.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
            this.buttonsLayout.addView(var11, LayoutHelper.createFrame(-2, 36, 53));
            var11.setOnClickListener(new _$$Lambda$AlertDialog$35svlhUpH_M074FLkhJN8iyIwmw(this));
         }

         if (this.neutralButtonText != null) {
            var11 = new TextView(this.getContext()) {
               public void setEnabled(boolean var1) {
                  super.setEnabled(var1);
                  float var2;
                  if (var1) {
                     var2 = 1.0F;
                  } else {
                     var2 = 0.5F;
                  }

                  this.setAlpha(var2);
               }

               public void setTextColor(int var1) {
                  super.setTextColor(var1);
                  this.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(var1));
               }
            };
            var11.setMinWidth(AndroidUtilities.dp(64.0F));
            var11.setTag(-3);
            var11.setTextSize(1, 14.0F);
            var11.setTextColor(this.getThemeColor("dialogButton"));
            var11.setGravity(17);
            var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var11.setEllipsize(TruncateAt.END);
            var11.setSingleLine(true);
            var11.setText(this.neutralButtonText.toString().toUpperCase());
            var11.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(this.getThemeColor("dialogButton")));
            var11.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
            this.buttonsLayout.addView(var11, LayoutHelper.createFrame(-2, 36, 51));
            var11.setOnClickListener(new _$$Lambda$AlertDialog$hCRmQxFHC_EIDauULvRdmfnSEuE(this));
         }
      }

      Window var25 = this.getWindow();
      LayoutParams var12 = new LayoutParams();
      var12.copyFrom(var25.getAttributes());
      if (this.progressViewStyle == 3) {
         var12.width = -1;
      } else {
         var12.dimAmount = 0.6F;
         var12.flags |= 2;
         int var13 = AndroidUtilities.displaySize.x;
         this.lastScreenWidth = var13;
         var26 = AndroidUtilities.dp(48.0F);
         if (AndroidUtilities.isTablet()) {
            if (AndroidUtilities.isSmallTablet()) {
               var27 = AndroidUtilities.dp(446.0F);
            } else {
               var27 = AndroidUtilities.dp(496.0F);
            }
         } else {
            var27 = AndroidUtilities.dp(356.0F);
         }

         var27 = Math.min(var27, var13 - var26);
         Rect var19 = this.backgroundPaddings;
         var12.width = var27 + var19.left + var19.right;
      }

      View var20 = this.customView;
      if (var20 != null && this.canTextInput(var20)) {
         var12.softInputMode = 4;
      } else {
         var12.flags |= 131072;
      }

      if (VERSION.SDK_INT >= 28) {
         var12.layoutInDisplayCutoutMode = 0;
      }

      var25.setAttributes(var12);
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      ScrollView var5 = this.contentScrollView;
      if (var5 != null) {
         var5.postDelayed(var2, var3);
      }

   }

   public void setButton(int var1, CharSequence var2, OnClickListener var3) {
      if (var1 != -3) {
         if (var1 != -2) {
            if (var1 == -1) {
               this.positiveButtonText = var2;
               this.positiveButtonListener = var3;
            }
         } else {
            this.negativeButtonText = var2;
            this.negativeButtonListener = var3;
         }
      } else {
         this.neutralButtonText = var2;
         this.neutralButtonListener = var3;
      }

   }

   public void setCanCacnel(boolean var1) {
      this.canCacnel = var1;
   }

   public void setCanceledOnTouchOutside(boolean var1) {
      super.setCanceledOnTouchOutside(var1);
   }

   public void setDismissDialogByButtons(boolean var1) {
      this.dismissDialogByButtons = var1;
   }

   public void setItemColor(int var1, int var2, int var3) {
      if (var1 >= 0 && var1 < this.itemViews.size()) {
         AlertDialog.AlertDialogCell var4 = (AlertDialog.AlertDialogCell)this.itemViews.get(var1);
         var4.textView.setTextColor(var2);
         var4.imageView.setColorFilter(new PorterDuffColorFilter(var3, Mode.MULTIPLY));
      }

   }

   public void setMessage(CharSequence var1) {
      this.message = var1;
      if (this.messageTextView != null) {
         if (!TextUtils.isEmpty(this.message)) {
            this.messageTextView.setText(this.message);
            this.messageTextView.setVisibility(0);
         } else {
            this.messageTextView.setVisibility(8);
         }
      }

   }

   public void setMessageTextViewClickable(boolean var1) {
      this.messageTextViewClickable = var1;
   }

   public void setNegativeButton(CharSequence var1, OnClickListener var2) {
      this.negativeButtonText = var1;
      this.negativeButtonListener = var2;
   }

   public void setNeutralButton(CharSequence var1, OnClickListener var2) {
      this.neutralButtonText = var1;
      this.neutralButtonListener = var2;
   }

   public void setOnCancelListener(OnCancelListener var1) {
      this.onCancelListener = var1;
      super.setOnCancelListener(var1);
   }

   public void setPositiveButton(CharSequence var1, OnClickListener var2) {
      this.positiveButtonText = var1;
      this.positiveButtonListener = var2;
   }

   public void setPositiveButtonListener(OnClickListener var1) {
      this.positiveButtonListener = var1;
   }

   public void setProgress(int var1) {
      this.currentProgress = var1;
      LineProgressView var2 = this.lineProgressView;
      if (var2 != null) {
         var2.setProgress((float)var1 / 100.0F, true);
         this.updateLineProgressTextView();
      }

   }

   public void setProgressStyle(int var1) {
      this.progressViewStyle = var1;
   }

   public void setSecondTitle(CharSequence var1) {
      this.secondTitle = var1;
   }

   public void setTitle(CharSequence var1) {
      this.title = var1;
      TextView var2 = this.titleTextView;
      if (var2 != null) {
         var2.setText(var1);
      }

   }

   public void setTopHeight(int var1) {
      this.topHeight = var1;
   }

   public void setTopImage(int var1, int var2) {
      this.topResId = var1;
      this.topBackgroundColor = var2;
   }

   public void setTopImage(Drawable var1, int var2) {
      this.topDrawable = var1;
      this.topBackgroundColor = var2;
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      ScrollView var3 = this.contentScrollView;
      if (var3 != null) {
         var3.removeCallbacks(var2);
      }

   }

   public static class AlertDialogCell extends FrameLayout {
      private ImageView imageView;
      private TextView textView;

      public AlertDialogCell(Context var1) {
         super(var1);
         this.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
         this.setPadding(AndroidUtilities.dp(23.0F), 0, AndroidUtilities.dp(23.0F), 0);
         this.imageView = new ImageView(var1);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogIcon"), Mode.MULTIPLY));
         ImageView var2 = this.imageView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var2, LayoutHelper.createFrame(-2, 40, var5 | 16));
         this.textView = new TextView(var1);
         this.textView.setLines(1);
         this.textView.setSingleLine(true);
         this.textView.setGravity(1);
         this.textView.setEllipsize(TruncateAt.END);
         this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
         this.textView.setTextSize(1, 16.0F);
         TextView var6 = this.textView;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createFrame(-2, -2, var5 | 16));
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
      }

      public void setGravity(int var1) {
         this.textView.setGravity(var1);
      }

      public void setTextAndIcon(CharSequence var1, int var2) {
         this.textView.setText(var1);
         if (var2 != 0) {
            this.imageView.setImageResource(var2);
            this.imageView.setVisibility(0);
            TextView var4 = this.textView;
            if (LocaleController.isRTL) {
               var2 = 0;
            } else {
               var2 = AndroidUtilities.dp(56.0F);
            }

            int var3;
            if (LocaleController.isRTL) {
               var3 = AndroidUtilities.dp(56.0F);
            } else {
               var3 = 0;
            }

            var4.setPadding(var2, 0, var3, 0);
         } else {
            this.imageView.setVisibility(4);
            this.textView.setPadding(0, 0, 0, 0);
         }

      }

      public void setTextColor(int var1) {
         this.textView.setTextColor(var1);
      }
   }

   public static class Builder {
      private AlertDialog alertDialog;

      public Builder(Context var1) {
         this.alertDialog = new AlertDialog(var1, 0);
      }

      public Builder(Context var1, int var2) {
         this.alertDialog = new AlertDialog(var1, var2);
      }

      protected Builder(AlertDialog var1) {
         this.alertDialog = var1;
      }

      public AlertDialog create() {
         return this.alertDialog;
      }

      public Context getContext() {
         return this.alertDialog.getContext();
      }

      public Runnable getDismissRunnable() {
         return this.alertDialog.dismissRunnable;
      }

      public AlertDialog.Builder setCustomViewOffset(int var1) {
         this.alertDialog.customViewOffset = var1;
         return this;
      }

      public AlertDialog.Builder setItems(CharSequence[] var1, OnClickListener var2) {
         this.alertDialog.items = var1;
         this.alertDialog.onClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setItems(CharSequence[] var1, int[] var2, OnClickListener var3) {
         this.alertDialog.items = var1;
         this.alertDialog.itemIcons = var2;
         this.alertDialog.onClickListener = var3;
         return this;
      }

      public AlertDialog.Builder setMessage(CharSequence var1) {
         this.alertDialog.message = var1;
         return this;
      }

      public AlertDialog.Builder setMessageTextViewClickable(boolean var1) {
         this.alertDialog.messageTextViewClickable = var1;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(CharSequence var1, OnClickListener var2) {
         this.alertDialog.negativeButtonText = var1;
         this.alertDialog.negativeButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNeutralButton(CharSequence var1, OnClickListener var2) {
         this.alertDialog.neutralButtonText = var1;
         this.alertDialog.neutralButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setOnBackButtonListener(OnClickListener var1) {
         this.alertDialog.onBackButtonListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnCancelListener(OnCancelListener var1) {
         this.alertDialog.setOnCancelListener(var1);
         return this;
      }

      public AlertDialog.Builder setOnDismissListener(OnDismissListener var1) {
         this.alertDialog.setOnDismissListener(var1);
         return this;
      }

      public AlertDialog.Builder setPositiveButton(CharSequence var1, OnClickListener var2) {
         this.alertDialog.positiveButtonText = var1;
         this.alertDialog.positiveButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setSubtitle(CharSequence var1) {
         this.alertDialog.subtitle = var1;
         return this;
      }

      public AlertDialog.Builder setTitle(CharSequence var1) {
         this.alertDialog.title = var1;
         return this;
      }

      public AlertDialog.Builder setTopImage(int var1, int var2) {
         this.alertDialog.topResId = var1;
         this.alertDialog.topBackgroundColor = var2;
         return this;
      }

      public AlertDialog.Builder setTopImage(Drawable var1, int var2) {
         this.alertDialog.topDrawable = var1;
         this.alertDialog.topBackgroundColor = var2;
         return this;
      }

      public AlertDialog.Builder setView(View var1) {
         this.alertDialog.customView = var1;
         return this;
      }

      public AlertDialog show() {
         this.alertDialog.show();
         return this.alertDialog;
      }
   }
}
