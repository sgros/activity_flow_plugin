package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class ScrollSlidingTabStrip extends HorizontalScrollView {
   private boolean animateFromPosition;
   private int currentPosition;
   private LayoutParams defaultExpandLayoutParams;
   private LayoutParams defaultTabLayoutParams;
   private ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate delegate;
   private int dividerPadding = AndroidUtilities.dp(12.0F);
   private int indicatorColor = -10066330;
   private int indicatorHeight;
   private long lastAnimationTime;
   private int lastScrollX = 0;
   private float positionAnimationProgress;
   private Paint rectPaint;
   private int scrollOffset = AndroidUtilities.dp(52.0F);
   private boolean shouldExpand;
   private float startAnimationPosition;
   private int tabCount;
   private int tabPadding = AndroidUtilities.dp(24.0F);
   private LinearLayout tabsContainer;
   private int underlineColor = 436207616;
   private int underlineHeight = AndroidUtilities.dp(2.0F);

   public ScrollSlidingTabStrip(Context var1) {
      super(var1);
      this.setFillViewport(true);
      this.setWillNotDraw(false);
      this.setHorizontalScrollBarEnabled(false);
      this.tabsContainer = new LinearLayout(var1);
      this.tabsContainer.setOrientation(0);
      this.tabsContainer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
      this.addView(this.tabsContainer);
      this.rectPaint = new Paint();
      this.rectPaint.setAntiAlias(true);
      this.rectPaint.setStyle(Style.FILL);
      this.defaultTabLayoutParams = new LayoutParams(AndroidUtilities.dp(52.0F), -1);
      this.defaultExpandLayoutParams = new LayoutParams(0, -1, 1.0F);
   }

   private void scrollToChild(int var1) {
      if (this.tabCount != 0 && this.tabsContainer.getChildAt(var1) != null) {
         int var2 = this.tabsContainer.getChildAt(var1).getLeft();
         int var3 = var2;
         if (var1 > 0) {
            var3 = var2 - this.scrollOffset;
         }

         var1 = this.getScrollX();
         if (var3 != this.lastScrollX) {
            if (var3 < var1) {
               this.lastScrollX = var3;
               this.smoothScrollTo(this.lastScrollX, 0);
            } else if (this.scrollOffset + var3 > var1 + this.getWidth() - this.scrollOffset * 2) {
               this.lastScrollX = var3 - this.getWidth() + this.scrollOffset * 3;
               this.smoothScrollTo(this.lastScrollX, 0);
            }
         }
      }

   }

   public ImageView addIconTab(Drawable var1) {
      int var2 = this.tabCount++;
      ImageView var3 = new ImageView(this.getContext());
      boolean var4 = true;
      var3.setFocusable(true);
      var3.setImageDrawable(var1);
      var3.setScaleType(ScaleType.CENTER);
      var3.setOnClickListener(new _$$Lambda$ScrollSlidingTabStrip$_7_oyaI_rm6dAg9Sbg63YJVLh4k(this, var2));
      this.tabsContainer.addView(var3);
      if (var2 != this.currentPosition) {
         var4 = false;
      }

      var3.setSelected(var4);
      return var3;
   }

   public TextView addIconTabWithCounter(Drawable var1) {
      int var2 = this.tabCount++;
      FrameLayout var3 = new FrameLayout(this.getContext());
      var3.setFocusable(true);
      this.tabsContainer.addView(var3);
      ImageView var4 = new ImageView(this.getContext());
      var4.setImageDrawable(var1);
      var4.setScaleType(ScaleType.CENTER);
      var3.setOnClickListener(new _$$Lambda$ScrollSlidingTabStrip$mNTU5l0eITNwV9vJo7_FdGx3xIs(this, var2));
      var3.addView(var4, LayoutHelper.createFrame(-1, -1.0F));
      boolean var5;
      if (var2 == this.currentPosition) {
         var5 = true;
      } else {
         var5 = false;
      }

      var3.setSelected(var5);
      TextView var6 = new TextView(this.getContext());
      var6.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var6.setTextSize(1, 12.0F);
      var6.setTextColor(Theme.getColor("chat_emojiPanelBadgeText"));
      var6.setGravity(17);
      var6.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(9.0F), Theme.getColor("chat_emojiPanelBadgeBackground")));
      var6.setMinWidth(AndroidUtilities.dp(18.0F));
      var6.setPadding(AndroidUtilities.dp(5.0F), 0, AndroidUtilities.dp(5.0F), AndroidUtilities.dp(1.0F));
      var3.addView(var6, LayoutHelper.createFrame(-2, 18.0F, 51, 26.0F, 6.0F, 0.0F, 0.0F));
      return var6;
   }

   public View addStickerTab(TLObject var1, TLRPC.Document var2, Object var3) {
      int var4 = this.tabCount++;
      FrameLayout var5 = new FrameLayout(this.getContext());
      var5.setTag(var1);
      var5.setTag(2131230860, var3);
      var5.setTag(2131230858, var2);
      var5.setFocusable(true);
      var5.setOnClickListener(new _$$Lambda$ScrollSlidingTabStrip$VOvZofAy7NE0HRe6ap1qMeWlOr0(this, var4));
      this.tabsContainer.addView(var5);
      boolean var6;
      if (var4 == this.currentPosition) {
         var6 = true;
      } else {
         var6 = false;
      }

      var5.setSelected(var6);
      BackupImageView var7 = new BackupImageView(this.getContext());
      var7.setAspectFit(true);
      var5.addView(var7, LayoutHelper.createFrame(30, 30, 17));
      return var5;
   }

   public void addStickerTab(TLRPC.Chat var1) {
      int var2 = this.tabCount++;
      FrameLayout var3 = new FrameLayout(this.getContext());
      var3.setFocusable(true);
      var3.setOnClickListener(new _$$Lambda$ScrollSlidingTabStrip$CxqiesXBMvO9L7nEGHWQr5LTQDY(this, var2));
      this.tabsContainer.addView(var3);
      boolean var4;
      if (var2 == this.currentPosition) {
         var4 = true;
      } else {
         var4 = false;
      }

      var3.setSelected(var4);
      BackupImageView var5 = new BackupImageView(this.getContext());
      var5.setRoundRadius(AndroidUtilities.dp(15.0F));
      AvatarDrawable var6 = new AvatarDrawable();
      var6.setTextSize(AndroidUtilities.dp(14.0F));
      var6.setInfo(var1);
      var5.setImage((ImageLocation)ImageLocation.getForChat(var1, false), "50_50", (Drawable)var6, (Object)var1);
      var5.setAspectFit(true);
      var3.addView(var5, LayoutHelper.createFrame(30, 30, 17));
   }

   public void cancelPositionAnimation() {
      this.animateFromPosition = false;
      this.positionAnimationProgress = 1.0F;
   }

   public int getCurrentPosition() {
      return this.currentPosition;
   }

   // $FF: synthetic method
   public void lambda$addIconTab$1$ScrollSlidingTabStrip(int var1, View var2) {
      this.delegate.onPageSelected(var1);
   }

   // $FF: synthetic method
   public void lambda$addIconTabWithCounter$0$ScrollSlidingTabStrip(int var1, View var2) {
      this.delegate.onPageSelected(var1);
   }

   // $FF: synthetic method
   public void lambda$addStickerTab$2$ScrollSlidingTabStrip(int var1, View var2) {
      this.delegate.onPageSelected(var1);
   }

   // $FF: synthetic method
   public void lambda$addStickerTab$3$ScrollSlidingTabStrip(int var1, View var2) {
      this.delegate.onPageSelected(var1);
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (!this.isInEditMode() && this.tabCount != 0) {
         int var2 = this.getHeight();
         if (this.underlineHeight > 0) {
            this.rectPaint.setColor(this.underlineColor);
            var1.drawRect(0.0F, (float)(var2 - this.underlineHeight), (float)this.tabsContainer.getWidth(), (float)var2, this.rectPaint);
         }

         if (this.indicatorHeight >= 0) {
            View var3 = this.tabsContainer.getChildAt(this.currentPosition);
            float var4 = 0.0F;
            int var5;
            if (var3 != null) {
               var4 = (float)var3.getLeft();
               var5 = var3.getMeasuredWidth();
            } else {
               var5 = 0;
            }

            float var6 = var4;
            if (this.animateFromPosition) {
               long var7 = SystemClock.uptimeMillis();
               long var9 = this.lastAnimationTime;
               this.lastAnimationTime = var7;
               this.positionAnimationProgress += (float)(var7 - var9) / 150.0F;
               if (this.positionAnimationProgress >= 1.0F) {
                  this.positionAnimationProgress = 1.0F;
                  this.animateFromPosition = false;
               }

               var6 = this.startAnimationPosition;
               var6 += (var4 - var6) * CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.positionAnimationProgress);
               this.invalidate();
            }

            this.rectPaint.setColor(this.indicatorColor);
            int var11 = this.indicatorHeight;
            if (var11 == 0) {
               var1.drawRect(var6, 0.0F, var6 + (float)var5, (float)var2, this.rectPaint);
            } else {
               var1.drawRect(var6, (float)(var2 - var11), var6 + (float)var5, (float)var2, this.rectPaint);
            }
         }
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.setImages();
   }

   public void onPageScrolled(int var1, int var2) {
      int var3 = this.currentPosition;
      if (var3 != var1) {
         View var4 = this.tabsContainer.getChildAt(var3);
         if (var4 != null) {
            this.startAnimationPosition = (float)var4.getLeft();
            this.positionAnimationProgress = 0.0F;
            this.animateFromPosition = true;
            this.lastAnimationTime = SystemClock.uptimeMillis();
         } else {
            this.animateFromPosition = false;
         }

         this.currentPosition = var1;
         if (var1 < this.tabsContainer.getChildCount()) {
            this.positionAnimationProgress = 0.0F;

            for(var3 = 0; var3 < this.tabsContainer.getChildCount(); ++var3) {
               var4 = this.tabsContainer.getChildAt(var3);
               boolean var5;
               if (var3 == var1) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var4.setSelected(var5);
            }

            if (var2 == var1 && var1 > 1) {
               this.scrollToChild(var1 - 1);
            } else {
               this.scrollToChild(var1);
            }

            this.invalidate();
         }
      }
   }

   protected void onScrollChanged(int var1, int var2, int var3, int var4) {
      super.onScrollChanged(var1, var2, var3, var4);
      int var5 = AndroidUtilities.dp(52.0F);
      var4 = var3 / var5;
      var2 = var1 / var5;
      var3 = (int)Math.ceil((double)((float)this.getMeasuredWidth() / (float)var5)) + 1;
      var1 = Math.max(0, Math.min(var4, var2));

      for(var4 = Math.min(this.tabsContainer.getChildCount(), Math.max(var4, var2) + var3); var1 < var4; ++var1) {
         View var6 = this.tabsContainer.getChildAt(var1);
         if (var6 != null) {
            Object var7 = var6.getTag();
            Object var8 = var6.getTag(2131230860);
            TLRPC.Document var9 = (TLRPC.Document)var6.getTag(2131230858);
            ImageLocation var11;
            if (var7 instanceof TLRPC.Document) {
               var11 = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(var9.thumbs, 90), var9);
            } else {
               if (!(var7 instanceof TLRPC.PhotoSize)) {
                  continue;
               }

               var11 = ImageLocation.getForSticker((TLRPC.PhotoSize)var7, var9);
            }

            BackupImageView var10 = (BackupImageView)((FrameLayout)var6).getChildAt(0);
            if (var1 >= var2 && var1 < var2 + var3) {
               var10.setImage(var11, (String)null, "webp", (Drawable)null, var8);
            } else {
               var10.setImageDrawable((Drawable)null);
            }
         }
      }

   }

   public void removeTabs() {
      this.tabsContainer.removeAllViews();
      this.tabCount = 0;
      this.currentPosition = 0;
      this.animateFromPosition = false;
   }

   public void selectTab(int var1) {
      if (var1 >= 0 && var1 < this.tabCount) {
         this.tabsContainer.getChildAt(var1).performClick();
      }

   }

   public void setDelegate(ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate var1) {
      this.delegate = var1;
   }

   public void setImages() {
      int var1 = AndroidUtilities.dp(52.0F);
      int var2 = this.getScrollX() / var1;

      for(var1 = Math.min(this.tabsContainer.getChildCount(), (int)Math.ceil((double)((float)this.getMeasuredWidth() / (float)var1)) + var2 + 1); var2 < var1; ++var2) {
         View var3 = this.tabsContainer.getChildAt(var2);
         Object var4 = var3.getTag();
         Object var5 = var3.getTag(2131230860);
         TLRPC.Document var6 = (TLRPC.Document)var3.getTag(2131230858);
         ImageLocation var7;
         if (var4 instanceof TLRPC.Document) {
            var7 = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(var6.thumbs, 90), var6);
         } else {
            if (!(var4 instanceof TLRPC.PhotoSize)) {
               continue;
            }

            var7 = ImageLocation.getForSticker((TLRPC.PhotoSize)var4, var6);
         }

         ((BackupImageView)((FrameLayout)var3).getChildAt(0)).setImage(var7, (String)null, "webp", (Drawable)null, var5);
      }

   }

   public void setIndicatorColor(int var1) {
      this.indicatorColor = var1;
      this.invalidate();
   }

   public void setIndicatorHeight(int var1) {
      this.indicatorHeight = var1;
      this.invalidate();
   }

   public void setShouldExpand(boolean var1) {
      this.shouldExpand = var1;
      this.requestLayout();
   }

   public void setUnderlineColor(int var1) {
      this.underlineColor = var1;
      this.invalidate();
   }

   public void setUnderlineColorResource(int var1) {
      this.underlineColor = this.getResources().getColor(var1);
      this.invalidate();
   }

   public void setUnderlineHeight(int var1) {
      this.underlineHeight = var1;
      this.invalidate();
   }

   public void updateTabStyles() {
      for(int var1 = 0; var1 < this.tabCount; ++var1) {
         View var2 = this.tabsContainer.getChildAt(var1);
         if (this.shouldExpand) {
            var2.setLayoutParams(this.defaultExpandLayoutParams);
         } else {
            var2.setLayoutParams(this.defaultTabLayoutParams);
         }
      }

   }

   public interface ScrollSlidingTabStripDelegate {
      void onPageSelected(int var1);
   }
}
