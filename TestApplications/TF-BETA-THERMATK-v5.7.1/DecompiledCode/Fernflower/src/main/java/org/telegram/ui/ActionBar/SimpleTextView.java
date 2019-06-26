package org.telegram.ui.ActionBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;

public class SimpleTextView extends View implements Callback {
   private static final int DIST_BETWEEN_SCROLLING_TEXT = 16;
   private static final int PIXELS_PER_SECOND = 50;
   private static final int PIXELS_PER_SECOND_SLOW = 30;
   private static final int SCROLL_DELAY_MS = 500;
   private static final int SCROLL_SLOWDOWN_PX = 100;
   private int currentScrollDelay;
   private int drawablePadding = AndroidUtilities.dp(4.0F);
   private GradientDrawable fadeDrawable;
   private GradientDrawable fadeDrawableBack;
   private int gravity = 51;
   private long lastUpdateTime;
   private int lastWidth;
   private Layout layout;
   private Drawable leftDrawable;
   private int leftDrawableTopPadding;
   private int offsetX;
   private int offsetY;
   private Drawable rightDrawable;
   private int rightDrawableTopPadding;
   private boolean scrollNonFitText;
   private float scrollingOffset;
   private SpannableStringBuilder spannableStringBuilder;
   private CharSequence text;
   private boolean textDoesNotFit;
   private int textHeight;
   private TextPaint textPaint = new TextPaint(1);
   private int textWidth;
   private int totalWidth;
   private boolean wasLayout;

   public SimpleTextView(Context var1) {
      super(var1);
      this.setImportantForAccessibility(1);
   }

   private void calcOffset(int var1) {
      if (this.layout.getLineCount() > 0) {
         Layout var2 = this.layout;
         boolean var3 = false;
         this.textWidth = (int)Math.ceil((double)var2.getLineWidth(0));
         this.textHeight = this.layout.getLineBottom(0);
         if ((this.gravity & 112) == 16) {
            this.offsetY = (this.getMeasuredHeight() - this.textHeight) / 2;
         } else {
            this.offsetY = 0;
         }

         if ((this.gravity & 7) == 3) {
            this.offsetX = -((int)this.layout.getLineLeft(0));
         } else if (this.layout.getLineLeft(0) == 0.0F) {
            this.offsetX = var1 - this.textWidth;
         } else {
            this.offsetX = -AndroidUtilities.dp(8.0F);
         }

         this.offsetX += this.getPaddingLeft();
         if (this.textWidth > var1) {
            var3 = true;
         }

         this.textDoesNotFit = var3;
      }

   }

   private boolean createLayout(int var1) {
      if (this.text != null) {
         label80: {
            int var2 = var1;

            boolean var10001;
            try {
               if (this.leftDrawable != null) {
                  var2 = var1 - this.leftDrawable.getIntrinsicWidth() - this.drawablePadding;
               }
            } catch (Exception var13) {
               var10001 = false;
               break label80;
            }

            var1 = var2;

            try {
               if (this.rightDrawable != null) {
                  var1 = var2 - this.rightDrawable.getIntrinsicWidth() - this.drawablePadding;
               }
            } catch (Exception var12) {
               var10001 = false;
               break label80;
            }

            CharSequence var3;
            label65: {
               try {
                  if (this.scrollNonFitText) {
                     var3 = this.text;
                     break label65;
                  }
               } catch (Exception var11) {
                  var10001 = false;
                  break label80;
               }

               try {
                  var3 = TextUtils.ellipsize(this.text, this.textPaint, (float)var1, TruncateAt.END);
               } catch (Exception var10) {
                  var10001 = false;
                  break label80;
               }
            }

            StaticLayout var4;
            int var5;
            TextPaint var6;
            label81: {
               try {
                  var4 = new StaticLayout;
                  var5 = var3.length();
                  var6 = this.textPaint;
                  if (this.scrollNonFitText) {
                     var2 = AndroidUtilities.dp(2000.0F);
                     break label81;
                  }
               } catch (Exception var9) {
                  var10001 = false;
                  break label80;
               }

               try {
                  var2 = AndroidUtilities.dp(8.0F) + var1;
               } catch (Exception var8) {
                  var10001 = false;
                  break label80;
               }
            }

            try {
               var4.<init>(var3, 0, var5, var6, var2, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               this.layout = var4;
               this.calcOffset(var1);
            } catch (Exception var7) {
               var10001 = false;
            }
         }
      } else {
         this.layout = null;
         this.textWidth = 0;
         this.textHeight = 0;
      }

      this.invalidate();
      return true;
   }

   private boolean recreateLayoutMaybe() {
      if (this.wasLayout && this.getMeasuredHeight() != 0) {
         return this.createLayout(this.getMeasuredWidth());
      } else {
         this.requestLayout();
         return true;
      }
   }

   private void updateScrollAnimation() {
      if (this.scrollNonFitText && (this.textDoesNotFit || this.scrollingOffset != 0.0F)) {
         long var1 = SystemClock.uptimeMillis();
         long var3 = var1 - this.lastUpdateTime;
         long var5 = var3;
         if (var3 > 17L) {
            var5 = 17L;
         }

         int var7 = this.currentScrollDelay;
         if (var7 > 0) {
            this.currentScrollDelay = (int)((long)var7 - var5);
         } else {
            var7 = this.totalWidth + AndroidUtilities.dp(16.0F);
            float var8 = this.scrollingOffset;
            float var9 = (float)AndroidUtilities.dp(100.0F);
            float var10 = 50.0F;
            if (var8 < var9) {
               var10 = this.scrollingOffset / (float)AndroidUtilities.dp(100.0F) * 20.0F + 30.0F;
            } else if (this.scrollingOffset >= (float)(var7 - AndroidUtilities.dp(100.0F))) {
               var10 = 50.0F - (this.scrollingOffset - (float)(var7 - AndroidUtilities.dp(100.0F))) / (float)AndroidUtilities.dp(100.0F) * 20.0F;
            }

            this.scrollingOffset += (float)var5 / 1000.0F * (float)AndroidUtilities.dp(var10);
            this.lastUpdateTime = var1;
            if (this.scrollingOffset > (float)var7) {
               this.scrollingOffset = 0.0F;
               this.currentScrollDelay = 500;
            }
         }

         this.invalidate();
      }

   }

   public Paint getPaint() {
      return this.textPaint;
   }

   public Drawable getRightDrawable() {
      return this.rightDrawable;
   }

   public int getSideDrawablesSize() {
      Drawable var1 = this.leftDrawable;
      int var2 = 0;
      if (var1 != null) {
         var2 = 0 + var1.getIntrinsicWidth() + this.drawablePadding;
      }

      var1 = this.rightDrawable;
      int var3 = var2;
      if (var1 != null) {
         var3 = var2 + var1.getIntrinsicWidth() + this.drawablePadding;
      }

      return var3;
   }

   public CharSequence getText() {
      CharSequence var1 = this.text;
      Object var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      return (CharSequence)var2;
   }

   public int getTextHeight() {
      return this.textHeight;
   }

   public TextPaint getTextPaint() {
      return this.textPaint;
   }

   public int getTextStartX() {
      Layout var1 = this.layout;
      byte var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         Drawable var4 = this.leftDrawable;
         int var3 = var2;
         if (var4 != null) {
            var3 = var2;
            if ((this.gravity & 7) == 3) {
               var3 = 0 + this.drawablePadding + var4.getIntrinsicWidth();
            }
         }

         return (int)this.getX() + this.offsetX + var3;
      }
   }

   public int getTextStartY() {
      return this.layout == null ? 0 : (int)this.getY();
   }

   public int getTextWidth() {
      return this.textWidth;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void invalidateDrawable(Drawable var1) {
      Drawable var2 = this.leftDrawable;
      if (var1 == var2) {
         this.invalidate(var2.getBounds());
      } else {
         var2 = this.rightDrawable;
         if (var1 == var2) {
            this.invalidate(var2.getBounds());
         }
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.wasLayout = false;
   }

   protected void onDraw(Canvas var1) {
      this.totalWidth = this.textWidth;
      Drawable var2 = this.leftDrawable;
      int var3;
      int var4;
      if (var2 != null) {
         var3 = (int)(-this.scrollingOffset);
         var4 = (this.textHeight - var2.getIntrinsicHeight()) / 2 + this.leftDrawableTopPadding;
         var2 = this.leftDrawable;
         var2.setBounds(var3, var4, var2.getIntrinsicWidth() + var3, this.leftDrawable.getIntrinsicHeight() + var4);
         this.leftDrawable.draw(var1);
         if ((this.gravity & 7) == 3) {
            var3 = this.drawablePadding + this.leftDrawable.getIntrinsicWidth() + 0;
         } else {
            var3 = 0;
         }

         this.totalWidth += this.drawablePadding + this.leftDrawable.getIntrinsicWidth();
      } else {
         var3 = 0;
      }

      var2 = this.rightDrawable;
      int var5;
      if (var2 != null) {
         var4 = this.textWidth + var3 + this.drawablePadding + (int)(-this.scrollingOffset);
         var5 = (this.textHeight - var2.getIntrinsicHeight()) / 2 + this.rightDrawableTopPadding;
         var2 = this.rightDrawable;
         var2.setBounds(var4, var5, var2.getIntrinsicWidth() + var4, this.rightDrawable.getIntrinsicHeight() + var5);
         this.rightDrawable.draw(var1);
         this.totalWidth += this.drawablePadding + this.rightDrawable.getIntrinsicWidth();
      }

      var4 = this.totalWidth + AndroidUtilities.dp(16.0F);
      float var6 = this.scrollingOffset;
      if (var6 != 0.0F) {
         var2 = this.leftDrawable;
         int var7;
         if (var2 != null) {
            var7 = (int)(-var6) + var4;
            var5 = (this.textHeight - var2.getIntrinsicHeight()) / 2 + this.leftDrawableTopPadding;
            var2 = this.leftDrawable;
            var2.setBounds(var7, var5, var2.getIntrinsicWidth() + var7, this.leftDrawable.getIntrinsicHeight() + var5);
            this.leftDrawable.draw(var1);
         }

         var2 = this.rightDrawable;
         if (var2 != null) {
            var5 = this.textWidth + var3 + this.drawablePadding + (int)(-this.scrollingOffset) + var4;
            var7 = (this.textHeight - var2.getIntrinsicHeight()) / 2 + this.rightDrawableTopPadding;
            var2 = this.rightDrawable;
            var2.setBounds(var5, var7, var2.getIntrinsicWidth() + var5, this.rightDrawable.getIntrinsicHeight() + var7);
            this.rightDrawable.draw(var1);
         }
      }

      if (this.layout != null) {
         if (this.offsetX + var3 != 0 || this.offsetY != 0 || this.scrollingOffset != 0.0F) {
            var1.save();
            var1.translate((float)(this.offsetX + var3) - this.scrollingOffset, (float)this.offsetY);
            var6 = this.scrollingOffset;
         }

         this.layout.draw(var1);
         if (this.scrollingOffset != 0.0F) {
            var1.translate((float)var4, 0.0F);
            this.layout.draw(var1);
         }

         if (this.offsetX + var3 != 0 || this.offsetY != 0 || this.scrollingOffset != 0.0F) {
            var1.restore();
         }

         if (this.scrollNonFitText && (this.textDoesNotFit || this.scrollingOffset != 0.0F)) {
            if (this.scrollingOffset < (float)AndroidUtilities.dp(10.0F)) {
               this.fadeDrawable.setAlpha((int)(this.scrollingOffset / (float)AndroidUtilities.dp(10.0F) * 255.0F));
            } else if (this.scrollingOffset > (float)(this.totalWidth + AndroidUtilities.dp(16.0F) - AndroidUtilities.dp(10.0F))) {
               var6 = this.scrollingOffset;
               float var8 = (float)(this.totalWidth + AndroidUtilities.dp(16.0F) - AndroidUtilities.dp(10.0F));
               this.fadeDrawable.setAlpha((int)((1.0F - (var6 - var8) / (float)AndroidUtilities.dp(10.0F)) * 255.0F));
            } else {
               this.fadeDrawable.setAlpha(255);
            }

            this.fadeDrawable.setBounds(0, 0, AndroidUtilities.dp(6.0F), this.getMeasuredHeight());
            this.fadeDrawable.draw(var1);
            this.fadeDrawableBack.setBounds(this.getMeasuredWidth() - AndroidUtilities.dp(6.0F), 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            this.fadeDrawableBack.draw(var1);
         }

         this.updateScrollAnimation();
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setVisibleToUser(true);
      var1.setClassName("android.widget.TextView");
      var1.setText(this.text);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.wasLayout = true;
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      var1 = MeasureSpec.getSize(var2);
      int var4 = this.lastWidth;
      int var5 = AndroidUtilities.displaySize.x;
      if (var4 != var5) {
         this.lastWidth = var5;
         this.scrollingOffset = 0.0F;
         this.currentScrollDelay = 500;
      }

      this.createLayout(var3 - this.getPaddingLeft() - this.getPaddingRight());
      if (MeasureSpec.getMode(var2) != 1073741824) {
         var1 = this.textHeight;
      }

      this.setMeasuredDimension(var3, var1);
   }

   public void setBackgroundColor(int var1) {
      if (this.scrollNonFitText) {
         GradientDrawable var2 = this.fadeDrawable;
         if (var2 != null) {
            var2.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
            this.fadeDrawableBack.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
         }
      } else {
         super.setBackgroundColor(var1);
      }

   }

   public void setDrawablePadding(int var1) {
      if (this.drawablePadding != var1) {
         this.drawablePadding = var1;
         if (!this.recreateLayoutMaybe()) {
            this.invalidate();
         }

      }
   }

   public void setGravity(int var1) {
      this.gravity = var1;
   }

   public void setLeftDrawable(int var1) {
      Drawable var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = this.getContext().getResources().getDrawable(var1);
      }

      this.setLeftDrawable(var2);
   }

   public void setLeftDrawable(Drawable var1) {
      Drawable var2 = this.leftDrawable;
      if (var2 != var1) {
         if (var2 != null) {
            var2.setCallback((Callback)null);
         }

         this.leftDrawable = var1;
         if (var1 != null) {
            var1.setCallback(this);
         }

         if (!this.recreateLayoutMaybe()) {
            this.invalidate();
         }

      }
   }

   public void setLeftDrawableTopPadding(int var1) {
      this.leftDrawableTopPadding = var1;
   }

   public void setLinkTextColor(int var1) {
      this.textPaint.linkColor = var1;
      this.invalidate();
   }

   public void setRightDrawable(int var1) {
      Drawable var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = this.getContext().getResources().getDrawable(var1);
      }

      this.setRightDrawable(var2);
   }

   public void setRightDrawable(Drawable var1) {
      Drawable var2 = this.rightDrawable;
      if (var2 != var1) {
         if (var2 != null) {
            var2.setCallback((Callback)null);
         }

         this.rightDrawable = var1;
         if (var1 != null) {
            var1.setCallback(this);
         }

         if (!this.recreateLayoutMaybe()) {
            this.invalidate();
         }

      }
   }

   public void setRightDrawableTopPadding(int var1) {
      this.rightDrawableTopPadding = var1;
   }

   public void setScrollNonFitText(boolean var1) {
      if (this.scrollNonFitText != var1) {
         this.scrollNonFitText = var1;
         if (this.scrollNonFitText) {
            this.fadeDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{-1, 0});
            this.fadeDrawableBack = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{0, -1});
         }

         this.requestLayout();
      }
   }

   public void setSideDrawablesColor(int var1) {
      Theme.setDrawableColor(this.rightDrawable, var1);
      Theme.setDrawableColor(this.leftDrawable, var1);
   }

   public boolean setText(CharSequence var1) {
      return this.setText(var1, false);
   }

   public boolean setText(CharSequence var1, boolean var2) {
      if (this.text != null || var1 != null) {
         if (!var2) {
            CharSequence var3 = this.text;
            if (var3 != null && var3.equals(var1)) {
               return false;
            }
         }

         this.text = var1;
         this.scrollingOffset = 0.0F;
         this.currentScrollDelay = 500;
         this.recreateLayoutMaybe();
         return true;
      } else {
         return false;
      }
   }

   public void setTextColor(int var1) {
      this.textPaint.setColor(var1);
      this.invalidate();
   }

   public void setTextSize(int var1) {
      float var2 = (float)AndroidUtilities.dp((float)var1);
      if (var2 != this.textPaint.getTextSize()) {
         this.textPaint.setTextSize(var2);
         if (!this.recreateLayoutMaybe()) {
            this.invalidate();
         }

      }
   }

   public void setTypeface(Typeface var1) {
      this.textPaint.setTypeface(var1);
   }
}
