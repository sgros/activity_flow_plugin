package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import androidx.annotation.Keep;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;

public class EditTextBoldCursor extends EditText {
   private static Class editorClass;
   private static Method getVerticalOffsetMethod;
   private static Field mCursorDrawableField;
   private static Field mCursorDrawableResField;
   private static Field mEditor;
   private static Field mScrollYField;
   private static Field mShowCursorField;
   private int activeLineColor;
   private boolean allowDrawCursor = true;
   private boolean currentDrawHintAsHeader;
   private int cursorSize;
   private float cursorWidth = 2.0F;
   private Object editor;
   private StaticLayout errorLayout;
   private int errorLineColor;
   private TextPaint errorPaint;
   private CharSequence errorText;
   private boolean fixed;
   private GradientDrawable gradientDrawable;
   private float headerAnimationProgress;
   private int headerHintColor;
   private AnimatorSet headerTransformAnimation;
   private float hintAlpha = 1.0F;
   private int hintColor;
   private StaticLayout hintLayout;
   private boolean hintVisible = true;
   private int ignoreBottomCount;
   private int ignoreTopCount;
   private long lastUpdateTime;
   private int lineColor;
   private Paint linePaint;
   private float lineSpacingExtra;
   private float lineY;
   private OnPreDrawListener listenerFixer;
   private Drawable mCursorDrawable;
   private boolean nextSetTextAnimated;
   private android.graphics.Rect rect = new android.graphics.Rect();
   private int scrollY;
   private boolean supportRtlHint;
   private boolean transformHintToHeader;

   public EditTextBoldCursor(Context var1) {
      super(var1);
      if (VERSION.SDK_INT >= 26) {
         this.setImportantForAutofill(2);
      }

      this.init();
   }

   private void checkHeaderVisibility(boolean var1) {
      boolean var2;
      if (!this.transformHintToHeader || !this.isFocused() && this.getText().length() <= 0) {
         var2 = false;
      } else {
         var2 = true;
      }

      if (this.currentDrawHintAsHeader != var2) {
         AnimatorSet var3 = this.headerTransformAnimation;
         if (var3 != null) {
            var3.cancel();
            this.headerTransformAnimation = null;
         }

         this.currentDrawHintAsHeader = var2;
         float var4 = 1.0F;
         if (var1) {
            this.headerTransformAnimation = new AnimatorSet();
            var3 = this.headerTransformAnimation;
            if (!var2) {
               var4 = 0.0F;
            }

            var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "headerAnimationProgress", new float[]{var4})});
            this.headerTransformAnimation.setDuration(200L);
            this.headerTransformAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.headerTransformAnimation.start();
         } else {
            if (!var2) {
               var4 = 0.0F;
            }

            this.headerAnimationProgress = var4;
         }

         this.invalidate();
      }

   }

   @SuppressLint({"PrivateApi"})
   private void init() {
      this.linePaint = new Paint();
      this.errorPaint = new TextPaint(1);
      this.errorPaint.setTextSize((float)AndroidUtilities.dp(11.0F));
      if (VERSION.SDK_INT >= 26) {
         this.setImportantForAutofill(2);
      }

      try {
         if (mScrollYField == null) {
            mScrollYField = View.class.getDeclaredField("mScrollY");
            mScrollYField.setAccessible(true);
         }
      } catch (Throwable var4) {
      }

      try {
         if (mCursorDrawableResField == null) {
            mCursorDrawableResField = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableResField.setAccessible(true);
         }
      } catch (Throwable var3) {
      }

      label55: {
         Throwable var10000;
         label61: {
            boolean var10001;
            label52: {
               try {
                  if (editorClass != null) {
                     break label55;
                  }

                  mEditor = TextView.class.getDeclaredField("mEditor");
                  mEditor.setAccessible(true);
                  editorClass = Class.forName("android.widget.Editor");
                  mShowCursorField = editorClass.getDeclaredField("mShowCursor");
                  mShowCursorField.setAccessible(true);
                  if (VERSION.SDK_INT >= 28) {
                     break label52;
                  }
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label61;
               }

               try {
                  mCursorDrawableField = editorClass.getDeclaredField("mCursorDrawable");
                  mCursorDrawableField.setAccessible(true);
               } catch (Throwable var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label61;
               }
            }

            try {
               getVerticalOffsetMethod = TextView.class.getDeclaredMethod("getVerticalOffset", Boolean.TYPE);
               getVerticalOffsetMethod.setAccessible(true);
               mShowCursorField = editorClass.getDeclaredField("mShowCursor");
               mShowCursorField.setAccessible(true);
               getVerticalOffsetMethod = TextView.class.getDeclaredMethod("getVerticalOffset", Boolean.TYPE);
               getVerticalOffsetMethod.setAccessible(true);
               break label55;
            } catch (Throwable var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         Throwable var1 = var10000;
         FileLog.e(var1);
      }

      try {
         GradientDrawable var8 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-11230757, -11230757});
         this.gradientDrawable = var8;
         this.editor = mEditor.get(this);
         if (mCursorDrawableField != null) {
            mCursorDrawableResField.set(this, 2131165378);
         }
      } catch (Throwable var2) {
      }

      this.cursorSize = AndroidUtilities.dp(24.0F);
   }

   @SuppressLint({"PrivateApi"})
   public void fixHandleView(boolean var1) {
      if (var1) {
         this.fixed = false;
      } else if (!this.fixed) {
         try {
            if (editorClass == null) {
               editorClass = Class.forName("android.widget.Editor");
               mEditor = TextView.class.getDeclaredField("mEditor");
               mEditor.setAccessible(true);
               this.editor = mEditor.get(this);
            }

            if (this.listenerFixer == null) {
               Method var2 = editorClass.getDeclaredMethod("getPositionListener");
               var2.setAccessible(true);
               this.listenerFixer = (OnPreDrawListener)var2.invoke(this.editor);
            }

            OnPreDrawListener var5 = this.listenerFixer;
            var5.getClass();
            _$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY var3 = new _$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY(var5);
            AndroidUtilities.runOnUIThread(var3, 500L);
         } catch (Throwable var4) {
         }

         this.fixed = true;
      }

   }

   @TargetApi(26)
   public int getAutofillType() {
      return 0;
   }

   public StaticLayout getErrorLayout(int var1) {
      return TextUtils.isEmpty(this.errorText) ? null : new StaticLayout(this.errorText, this.errorPaint, var1, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
   }

   public int getExtendedPaddingBottom() {
      int var1 = this.ignoreBottomCount;
      if (var1 != 0) {
         this.ignoreBottomCount = var1 - 1;
         var1 = this.scrollY;
         if (var1 != Integer.MAX_VALUE) {
            var1 = -var1;
         } else {
            var1 = 0;
         }

         return var1;
      } else {
         return super.getExtendedPaddingBottom();
      }
   }

   public int getExtendedPaddingTop() {
      int var1 = this.ignoreTopCount;
      if (var1 != 0) {
         this.ignoreTopCount = var1 - 1;
         return 0;
      } else {
         return super.getExtendedPaddingTop();
      }
   }

   @Keep
   public float getHeaderAnimationProgress() {
      return this.headerAnimationProgress;
   }

   public float getLineY() {
      return this.lineY;
   }

   public boolean hasErrorText() {
      return TextUtils.isEmpty(this.errorText) ^ true;
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.getExtendedPaddingTop();
      this.scrollY = Integer.MAX_VALUE;

      try {
         this.scrollY = mScrollYField.getInt(this);
         mScrollYField.set(this, 0);
      } catch (Exception var25) {
      }

      this.ignoreTopCount = 1;
      this.ignoreBottomCount = 1;
      var1.save();
      var1.translate(0.0F, (float)var2);

      try {
         super.onDraw(var1);
      } catch (Exception var24) {
      }

      var2 = this.scrollY;
      if (var2 != Integer.MAX_VALUE) {
         try {
            mScrollYField.set(this, var2);
         } catch (Exception var23) {
         }
      }

      var1.restore();
      long var8;
      int var10;
      if ((this.length() == 0 || this.transformHintToHeader) && this.hintLayout != null && (this.hintVisible || this.hintAlpha != 0.0F)) {
         if (this.hintVisible && this.hintAlpha != 1.0F || !this.hintVisible && this.hintAlpha != 0.0F) {
            long var4;
            label199: {
               var4 = System.currentTimeMillis();
               long var6 = var4 - this.lastUpdateTime;
               if (var6 >= 0L) {
                  var8 = var6;
                  if (var6 <= 17L) {
                     break label199;
                  }
               }

               var8 = 17L;
            }

            this.lastUpdateTime = var4;
            if (this.hintVisible) {
               this.hintAlpha += (float)var8 / 150.0F;
               if (this.hintAlpha > 1.0F) {
                  this.hintAlpha = 1.0F;
               }
            } else {
               this.hintAlpha -= (float)var8 / 150.0F;
               if (this.hintAlpha < 0.0F) {
                  this.hintAlpha = 0.0F;
               }
            }

            this.invalidate();
         }

         var10 = this.getPaint().getColor();
         var1.save();
         float var11 = this.hintLayout.getLineLeft(0);
         float var12 = this.hintLayout.getLineWidth(0);
         if (var11 != 0.0F) {
            var2 = (int)((float)0 - var11);
         } else {
            var2 = 0;
         }

         float var13;
         if (this.supportRtlHint && LocaleController.isRTL) {
            var13 = (float)this.getMeasuredWidth();
            var1.translate((float)(var2 + this.getScrollX()) + (var13 - var12), this.lineY - (float)this.hintLayout.getHeight() - (float)AndroidUtilities.dp(6.0F));
         } else {
            var1.translate((float)(var2 + this.getScrollX()), this.lineY - (float)this.hintLayout.getHeight() - (float)AndroidUtilities.dp(6.0F));
         }

         if (!this.transformHintToHeader) {
            this.getPaint().setColor(this.hintColor);
            this.getPaint().setAlpha((int)(this.hintAlpha * 255.0F * ((float)Color.alpha(this.hintColor) / 255.0F)));
         } else {
            var13 = 1.0F - this.headerAnimationProgress * 0.3F;
            float var14 = (float)(-AndroidUtilities.dp(22.0F));
            float var15 = this.headerAnimationProgress;
            var2 = Color.red(this.headerHintColor);
            int var16 = Color.green(this.headerHintColor);
            int var17 = Color.blue(this.headerHintColor);
            int var18 = Color.alpha(this.headerHintColor);
            int var19 = Color.red(this.hintColor);
            int var20 = Color.green(this.hintColor);
            int var21 = Color.blue(this.hintColor);
            int var22 = Color.alpha(this.hintColor);
            if (this.supportRtlHint && LocaleController.isRTL) {
               var11 += var12;
               var1.translate(var11 - var11 * var13, 0.0F);
            } else if (var11 != 0.0F) {
               var1.translate(var11 * (1.0F - var13), 0.0F);
            }

            var1.scale(var13, var13);
            var1.translate(0.0F, var14 * var15);
            TextPaint var3 = this.getPaint();
            var11 = (float)var22;
            var13 = (float)(var18 - var22);
            var15 = this.headerAnimationProgress;
            var3.setColor(Color.argb((int)(var11 + var13 * var15), (int)((float)var19 + (float)(var2 - var19) * var15), (int)((float)var20 + (float)(var16 - var20) * var15), (int)((float)var21 + (float)(var17 - var21) * var15)));
         }

         this.hintLayout.draw(var1);
         this.getPaint().setColor(var10);
         var1.restore();
      }

      label225: {
         boolean var10001;
         label172: {
            try {
               if (!this.allowDrawCursor || mShowCursorField == null) {
                  break label225;
               }

               if (this.mCursorDrawable != null) {
                  break label172;
               }

               if (VERSION.SDK_INT >= 28) {
                  this.mCursorDrawable = (Drawable)mCursorDrawableField.get(this.editor);
                  break label172;
               }
            } catch (Throwable var33) {
               var10001 = false;
               break label225;
            }

            try {
               this.mCursorDrawable = ((Drawable[])mCursorDrawableField.get(this.editor))[0];
            } catch (Throwable var32) {
               var10001 = false;
               break label225;
            }
         }

         try {
            if (this.mCursorDrawable == null) {
               return;
            }
         } catch (Throwable var31) {
            var10001 = false;
            break label225;
         }

         boolean var36;
         label156: {
            label155: {
               try {
                  var8 = mShowCursorField.getLong(this.editor);
                  if ((SystemClock.uptimeMillis() - var8) % 1000L < 500L && this.isFocused()) {
                     break label155;
                  }
               } catch (Throwable var30) {
                  var10001 = false;
                  break label225;
               }

               var36 = false;
               break label156;
            }

            var36 = true;
         }

         if (var36) {
            label235: {
               label143: {
                  try {
                     var1.save();
                     if ((this.getGravity() & 112) != 48) {
                        var2 = (Integer)getVerticalOffsetMethod.invoke(this, true);
                        break label143;
                     }
                  } catch (Throwable var29) {
                     var10001 = false;
                     break label235;
                  }

                  var2 = 0;
               }

               label134: {
                  android.graphics.Rect var35;
                  try {
                     var1.translate((float)this.getPaddingLeft(), (float)(this.getExtendedPaddingTop() + var2));
                     Layout var34 = this.getLayout();
                     var10 = var34.getLineForOffset(this.getSelectionStart());
                     var2 = var34.getLineCount();
                     var35 = this.mCursorDrawable.getBounds();
                     this.rect.left = var35.left;
                     this.rect.right = var35.left + AndroidUtilities.dp(this.cursorWidth);
                     this.rect.bottom = var35.bottom;
                     this.rect.top = var35.top;
                     if (this.lineSpacingExtra == 0.0F) {
                        break label134;
                     }
                  } catch (Throwable var28) {
                     var10001 = false;
                     break label235;
                  }

                  if (var10 < var2 - 1) {
                     try {
                        var35 = this.rect;
                        var35.bottom = (int)((float)var35.bottom - this.lineSpacingExtra);
                     } catch (Throwable var27) {
                        var10001 = false;
                        break label235;
                     }
                  }
               }

               try {
                  this.rect.top = this.rect.centerY() - this.cursorSize / 2;
                  this.rect.bottom = this.rect.top + this.cursorSize;
                  this.gradientDrawable.setBounds(this.rect);
                  this.gradientDrawable.draw(var1);
                  var1.restore();
               } catch (Throwable var26) {
                  var10001 = false;
               }
            }
         }
      }

      if (this.lineColor != 0 && this.hintLayout != null) {
         if (!TextUtils.isEmpty(this.errorText)) {
            this.linePaint.setColor(this.errorLineColor);
            var2 = AndroidUtilities.dp(2.0F);
         } else if (this.isFocused()) {
            this.linePaint.setColor(this.activeLineColor);
            var2 = AndroidUtilities.dp(2.0F);
         } else {
            this.linePaint.setColor(this.lineColor);
            var2 = AndroidUtilities.dp(1.0F);
         }

         var1.drawRect((float)this.getScrollX(), (float)((int)this.lineY), (float)(this.getScrollX() + this.getMeasuredWidth()), this.lineY + (float)var2, this.linePaint);
      }

   }

   protected void onFocusChanged(boolean var1, int var2, android.graphics.Rect var3) {
      super.onFocusChanged(var1, var2, var3);
      this.checkHeaderVisibility(true);
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.EditText");
      StaticLayout var2 = this.hintLayout;
      if (var2 != null) {
         var1.setContentDescription(var2.getText());
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.hintLayout != null) {
         this.lineY = (float)(this.getMeasuredHeight() - this.hintLayout.getHeight()) / 2.0F + (float)this.hintLayout.getHeight() + (float)AndroidUtilities.dp(6.0F);
      }

   }

   public boolean requestFocus(int var1, android.graphics.Rect var2) {
      return super.requestFocus(var1, var2);
   }

   public void setAllowDrawCursor(boolean var1) {
      this.allowDrawCursor = var1;
      this.invalidate();
   }

   public void setCursorColor(int var1) {
      this.gradientDrawable.setColor(var1);
      this.invalidate();
   }

   public void setCursorSize(int var1) {
      this.cursorSize = var1;
   }

   public void setCursorWidth(float var1) {
      this.cursorWidth = var1;
   }

   public void setErrorLineColor(int var1) {
      this.errorLineColor = var1;
      this.errorPaint.setColor(this.errorLineColor);
      this.invalidate();
   }

   public void setErrorText(CharSequence var1) {
      if (!TextUtils.equals(var1, this.errorText)) {
         this.errorText = var1;
         this.requestLayout();
      }
   }

   @Keep
   public void setHeaderAnimationProgress(float var1) {
      this.headerAnimationProgress = var1;
      this.invalidate();
   }

   public void setHeaderHintColor(int var1) {
      this.headerHintColor = var1;
      this.invalidate();
   }

   public void setHintColor(int var1) {
      this.hintColor = var1;
      this.invalidate();
   }

   public void setHintText(String var1) {
      this.hintLayout = new StaticLayout(var1, this.getPaint(), AndroidUtilities.dp(1000.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
   }

   public void setHintVisible(boolean var1) {
      if (this.hintVisible != var1) {
         this.lastUpdateTime = System.currentTimeMillis();
         this.hintVisible = var1;
         this.invalidate();
      }
   }

   public void setLineColors(int var1, int var2, int var3) {
      this.lineColor = var1;
      this.activeLineColor = var2;
      this.errorLineColor = var3;
      this.errorPaint.setColor(this.errorLineColor);
      this.invalidate();
   }

   public void setLineSpacing(float var1, float var2) {
      super.setLineSpacing(var1, var2);
      this.lineSpacingExtra = var1;
   }

   public void setNextSetTextAnimated(boolean var1) {
      this.nextSetTextAnimated = var1;
   }

   public void setSupportRtlHint(boolean var1) {
      this.supportRtlHint = var1;
   }

   public void setText(CharSequence var1, BufferType var2) {
      super.setText(var1, var2);
      this.checkHeaderVisibility(this.nextSetTextAnimated);
      this.nextSetTextAnimated = false;
   }

   public void setTransformHintToHeader(boolean var1) {
      if (this.transformHintToHeader != var1) {
         this.transformHintToHeader = var1;
         AnimatorSet var2 = this.headerTransformAnimation;
         if (var2 != null) {
            var2.cancel();
            this.headerTransformAnimation = null;
         }

      }
   }
}
