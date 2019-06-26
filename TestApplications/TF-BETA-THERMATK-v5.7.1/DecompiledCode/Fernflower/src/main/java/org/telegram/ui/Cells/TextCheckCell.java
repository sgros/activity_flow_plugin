package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Switch;

public class TextCheckCell extends FrameLayout {
   public static final Property ANIMATION_PROGRESS = new AnimationProperties.FloatProperty("animationProgress") {
      public Float get(TextCheckCell var1) {
         return var1.animationProgress;
      }

      public void setValue(TextCheckCell var1, float var2) {
         var1.setAnimationProgress(var2);
         var1.invalidate();
      }
   };
   private int animatedColorBackground;
   private Paint animationPaint;
   private float animationProgress;
   private ObjectAnimator animator;
   private Switch checkBox;
   private boolean drawCheckRipple;
   private int height;
   private boolean isMultiline;
   private float lastTouchX;
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public TextCheckCell(Context var1) {
      this(var1, 21);
   }

   public TextCheckCell(Context var1, int var2) {
      this(var1, var2, false);
   }

   public TextCheckCell(Context var1, int var2, boolean var3) {
      super(var1);
      this.height = 50;
      this.textView = new TextView(var1);
      TextView var4 = this.textView;
      String var5;
      if (var3) {
         var5 = "dialogTextBlack";
      } else {
         var5 = "windowBackgroundWhiteBlackText";
      }

      var4.setTextColor(Theme.getColor(var5));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var13 = this.textView;
      boolean var6 = LocaleController.isRTL;
      byte var7 = 5;
      byte var8;
      if (var6) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      var13.setGravity(var8 | 16);
      this.textView.setEllipsize(TruncateAt.END);
      var13 = this.textView;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 70.0F;
      } else {
         var9 = (float)var2;
      }

      float var10;
      if (LocaleController.isRTL) {
         var10 = (float)var2;
      } else {
         var10 = 70.0F;
      }

      this.addView(var13, LayoutHelper.createFrame(-1, -1.0F, var8 | 48, var9, 0.0F, var10, 0.0F));
      this.valueTextView = new TextView(var1);
      var4 = this.valueTextView;
      if (var3) {
         var5 = "dialogIcon";
      } else {
         var5 = "windowBackgroundWhiteGrayText2";
      }

      var4.setTextColor(Theme.getColor(var5));
      this.valueTextView.setTextSize(1, 13.0F);
      var13 = this.valueTextView;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      var13.setGravity(var8);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setPadding(0, 0, 0, 0);
      this.valueTextView.setEllipsize(TruncateAt.END);
      var13 = this.valueTextView;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 64.0F;
      } else {
         var9 = (float)var2;
      }

      if (LocaleController.isRTL) {
         var10 = (float)var2;
      } else {
         var10 = 64.0F;
      }

      this.addView(var13, LayoutHelper.createFrame(-2, -2.0F, var8 | 48, var9, 36.0F, var10, 0.0F));
      this.checkBox = new Switch(var1);
      this.checkBox.setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
      Switch var11 = this.checkBox;
      byte var12 = var7;
      if (LocaleController.isRTL) {
         var12 = 3;
      }

      this.addView(var11, LayoutHelper.createFrame(37, 20.0F, var12 | 16, 22.0F, 0.0F, 22.0F, 0.0F));
      this.setClipChildren(false);
   }

   private void setAnimationProgress(float var1) {
      this.animationProgress = var1;
      var1 = Math.max(this.lastTouchX, (float)this.getMeasuredWidth() - this.lastTouchX);
      float var2 = (float)AndroidUtilities.dp(40.0F);
      float var3 = this.lastTouchX;
      int var4 = this.getMeasuredHeight() / 2;
      float var5 = this.animationProgress;
      this.checkBox.setOverrideColorProgress(var3, (float)var4, (var1 + var2) * var5);
   }

   public boolean isChecked() {
      return this.checkBox.isChecked();
   }

   protected void onDraw(Canvas var1) {
      float var3;
      float var4;
      int var5;
      if (this.animatedColorBackground != 0) {
         float var2 = Math.max(this.lastTouchX, (float)this.getMeasuredWidth() - this.lastTouchX);
         var3 = (float)AndroidUtilities.dp(40.0F);
         var4 = this.lastTouchX;
         var5 = this.getMeasuredHeight() / 2;
         float var6 = this.animationProgress;
         var1.drawCircle(var4, (float)var5, (var2 + var3) * var6, this.animationPaint);
      }

      if (this.needDivider) {
         if (LocaleController.isRTL) {
            var3 = 0.0F;
         } else {
            var3 = (float)AndroidUtilities.dp(20.0F);
         }

         var4 = (float)(this.getMeasuredHeight() - 1);
         int var7 = this.getMeasuredWidth();
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var3, var4, (float)(var7 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.Switch");
      var1.setCheckable(true);
      var1.setChecked(this.checkBox.isChecked());
      int var2;
      String var3;
      if (this.checkBox.isChecked()) {
         var2 = 2131560080;
         var3 = "NotificationsOn";
      } else {
         var2 = 2131560078;
         var3 = "NotificationsOff";
      }

      var1.setContentDescription(LocaleController.getString(var3, var2));
   }

   protected void onMeasure(int var1, int var2) {
      if (this.isMultiline) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      } else {
         var1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
         float var3;
         if (this.valueTextView.getVisibility() == 0) {
            var3 = 64.0F;
         } else {
            var3 = (float)this.height;
         }

         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(var3) + this.needDivider, 1073741824));
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.lastTouchX = var1.getX();
      return super.onTouchEvent(var1);
   }

   public void setBackgroundColor(int var1) {
      this.clearAnimation();
      this.animatedColorBackground = 0;
      super.setBackgroundColor(var1);
   }

   public void setBackgroundColorAnimated(boolean var1, int var2) {
      ObjectAnimator var3 = this.animator;
      if (var3 != null) {
         var3.cancel();
         this.animator = null;
      }

      int var4 = this.animatedColorBackground;
      if (var4 != 0) {
         this.setBackgroundColor(var4);
      }

      Paint var5 = this.animationPaint;
      byte var7 = 1;
      if (var5 == null) {
         this.animationPaint = new Paint(1);
      }

      Switch var6 = this.checkBox;
      if (!var1) {
         var7 = 2;
      }

      var6.setOverrideColor(var7);
      this.animatedColorBackground = var2;
      this.animationPaint.setColor(this.animatedColorBackground);
      this.animationProgress = 0.0F;
      this.animator = ObjectAnimator.ofFloat(this, ANIMATION_PROGRESS, new float[]{0.0F, 1.0F});
      this.animator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            TextCheckCell var2 = TextCheckCell.this;
            var2.setBackgroundColor(var2.animatedColorBackground);
            TextCheckCell.this.animatedColorBackground = 0;
            TextCheckCell.this.invalidate();
         }
      });
      this.animator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
      this.animator.setDuration(240L).start();
   }

   public void setChecked(boolean var1) {
      this.checkBox.setChecked(var1, true);
   }

   public void setColors(String var1, String var2, String var3, String var4, String var5) {
      this.textView.setTextColor(Theme.getColor(var1));
      this.checkBox.setColors(var2, var3, var4, var5);
      this.textView.setTag(var1);
   }

   public void setDrawCheckRipple(boolean var1) {
      this.drawCheckRipple = var1;
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      super.setEnabled(var1);
      float var3 = 1.0F;
      float var5;
      if (var2 != null) {
         TextView var4 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var5}));
         Switch var8 = this.checkBox;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var8, "alpha", new float[]{var5}));
         if (this.valueTextView.getVisibility() == 0) {
            var4 = this.valueTextView;
            if (!var1) {
               var3 = 0.5F;
            }

            var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var3}));
         }
      } else {
         TextView var6 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var6.setAlpha(var5);
         Switch var7 = this.checkBox;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var7.setAlpha(var5);
         if (this.valueTextView.getVisibility() == 0) {
            var6 = this.valueTextView;
            if (!var1) {
               var3 = 0.5F;
            }

            var6.setAlpha(var3);
         }
      }

   }

   public void setHeight(int var1) {
      this.height = var1;
   }

   public void setPressed(boolean var1) {
      if (this.drawCheckRipple) {
         this.checkBox.setDrawRipple(var1);
      }

      super.setPressed(var1);
   }

   public void setTextAndCheck(String var1, boolean var2, boolean var3) {
      this.textView.setText(var1);
      this.isMultiline = false;
      this.checkBox.setChecked(var2, false);
      this.needDivider = var3;
      this.valueTextView.setVisibility(8);
      LayoutParams var4 = (LayoutParams)this.textView.getLayoutParams();
      var4.height = -1;
      var4.topMargin = 0;
      this.textView.setLayoutParams(var4);
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextAndValueAndCheck(String var1, String var2, boolean var3, boolean var4, boolean var5) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.checkBox.setChecked(var3, false);
      this.needDivider = var5;
      this.valueTextView.setVisibility(0);
      this.isMultiline = var4;
      if (var4) {
         this.valueTextView.setLines(0);
         this.valueTextView.setMaxLines(0);
         this.valueTextView.setSingleLine(false);
         this.valueTextView.setEllipsize((TruncateAt)null);
         this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(11.0F));
      } else {
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setEllipsize(TruncateAt.END);
         this.valueTextView.setPadding(0, 0, 0, 0);
      }

      LayoutParams var6 = (LayoutParams)this.textView.getLayoutParams();
      var6.height = -2;
      var6.topMargin = AndroidUtilities.dp(10.0F);
      this.textView.setLayoutParams(var6);
      this.setWillNotDraw(true ^ var5);
   }

   public void setTypeface(Typeface var1) {
      this.textView.setTypeface(var1);
   }
}
