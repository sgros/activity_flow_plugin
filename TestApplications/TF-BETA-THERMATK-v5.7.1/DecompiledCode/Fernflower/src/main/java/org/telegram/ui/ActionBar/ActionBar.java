package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.FireworksEffect;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SnowflakesEffect;

public class ActionBar extends FrameLayout {
   public ActionBar.ActionBarMenuOnItemClick actionBarMenuOnItemClick;
   private ActionBarMenu actionMode;
   private AnimatorSet actionModeAnimation;
   private View actionModeExtraView;
   private View[] actionModeHidingViews;
   private View actionModeShowingView;
   private View actionModeTop;
   private View actionModeTranslationView;
   private boolean actionModeVisible;
   private boolean addToContainer;
   private boolean allowOverlayTitle;
   private ImageView backButtonImageView;
   private boolean castShadows;
   private boolean clipContent;
   private int extraHeight;
   private FireworksEffect fireworksEffect;
   private FontMetricsInt fontMetricsInt;
   private boolean ignoreLayoutRequest;
   private boolean interceptTouches;
   private boolean isBackOverlayVisible;
   protected boolean isSearchFieldVisible;
   protected int itemsActionModeBackgroundColor;
   protected int itemsActionModeColor;
   protected int itemsBackgroundColor;
   protected int itemsColor;
   private Runnable lastRunnable;
   private CharSequence lastTitle;
   private boolean manualStart;
   private ActionBarMenu menu;
   private boolean occupyStatusBar;
   protected BaseFragment parentFragment;
   private Rect rect;
   private SnowflakesEffect snowflakesEffect;
   private SimpleTextView subtitleTextView;
   private boolean supportsHolidayImage;
   private Runnable titleActionRunnable;
   private boolean titleOverlayShown;
   private int titleRightMargin;
   private SimpleTextView titleTextView;

   public ActionBar(Context var1) {
      super(var1);
      boolean var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.occupyStatusBar = var2;
      this.addToContainer = true;
      this.interceptTouches = true;
      this.castShadows = true;
      this.setOnClickListener(new _$$Lambda$ActionBar$ipLNSE_u7HuyPKHoD94Vr_WlQ_U(this));
   }

   private void createBackButtonImage() {
      if (this.backButtonImageView == null) {
         this.backButtonImageView = new ImageView(this.getContext());
         this.backButtonImageView.setScaleType(ScaleType.CENTER);
         this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
         int var1 = this.itemsColor;
         if (var1 != 0) {
            this.backButtonImageView.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
         }

         this.backButtonImageView.setPadding(AndroidUtilities.dp(1.0F), 0, 0, 0);
         this.addView(this.backButtonImageView, LayoutHelper.createFrame(54, 54, 51));
         this.backButtonImageView.setOnClickListener(new _$$Lambda$ActionBar$VS8eczH_GWXmmp1KP0J3EUbIWcg(this));
         this.backButtonImageView.setContentDescription(LocaleController.getString("AccDescrGoBack", 2131558435));
      }
   }

   private void createSubtitleTextView() {
      if (this.subtitleTextView == null) {
         this.subtitleTextView = new SimpleTextView(this.getContext());
         this.subtitleTextView.setGravity(3);
         this.subtitleTextView.setVisibility(8);
         this.subtitleTextView.setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
         this.addView(this.subtitleTextView, 0, LayoutHelper.createFrame(-2, -2, 51));
      }
   }

   private void createTitleTextView() {
      if (this.titleTextView == null) {
         this.titleTextView = new SimpleTextView(this.getContext());
         this.titleTextView.setGravity(3);
         this.titleTextView.setTextColor(Theme.getColor("actionBarDefaultTitle"));
         this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.addView(this.titleTextView, 0, LayoutHelper.createFrame(-2, -2, 51));
      }
   }

   public static int getCurrentActionBarHeight() {
      if (AndroidUtilities.isTablet()) {
         return AndroidUtilities.dp(64.0F);
      } else {
         Point var0 = AndroidUtilities.displaySize;
         return var0.x > var0.y ? AndroidUtilities.dp(48.0F) : AndroidUtilities.dp(56.0F);
      }
   }

   public void closeSearchField() {
      this.closeSearchField(true);
   }

   public void closeSearchField(boolean var1) {
      if (this.isSearchFieldVisible) {
         ActionBarMenu var2 = this.menu;
         if (var2 != null) {
            var2.closeSearchField(var1);
         }
      }

   }

   public ActionBarMenu createActionMode() {
      return this.createActionMode(true);
   }

   public ActionBarMenu createActionMode(boolean var1) {
      ActionBarMenu var2 = this.actionMode;
      if (var2 != null) {
         return var2;
      } else {
         this.actionMode = new ActionBarMenu(this.getContext(), this);
         var2 = this.actionMode;
         var2.isActionMode = true;
         var2.setBackgroundColor(Theme.getColor("actionBarActionModeDefault"));
         this.addView(this.actionMode, this.indexOfChild(this.backButtonImageView));
         var2 = this.actionMode;
         int var3;
         if (this.occupyStatusBar) {
            var3 = AndroidUtilities.statusBarHeight;
         } else {
            var3 = 0;
         }

         var2.setPadding(0, var3, 0, 0);
         LayoutParams var4 = (LayoutParams)this.actionMode.getLayoutParams();
         var4.height = -1;
         var4.width = -1;
         var4.bottomMargin = this.extraHeight;
         var4.gravity = 5;
         this.actionMode.setLayoutParams(var4);
         this.actionMode.setVisibility(4);
         if (this.occupyStatusBar && var1 && this.actionModeTop == null) {
            this.actionModeTop = new View(this.getContext());
            this.actionModeTop.setBackgroundColor(Theme.getColor("actionBarActionModeDefaultTop"));
            this.addView(this.actionModeTop);
            var4 = (LayoutParams)this.actionModeTop.getLayoutParams();
            var4.height = AndroidUtilities.statusBarHeight;
            var4.width = -1;
            var4.gravity = 51;
            this.actionModeTop.setLayoutParams(var4);
            this.actionModeTop.setVisibility(4);
         }

         return this.actionMode;
      }
   }

   public ActionBarMenu createMenu() {
      ActionBarMenu var1 = this.menu;
      if (var1 != null) {
         return var1;
      } else {
         this.menu = new ActionBarMenu(this.getContext(), this);
         this.addView(this.menu, 0, LayoutHelper.createFrame(-2, -1, 5));
         return this.menu;
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5;
      if (!this.clipContent || var2 != this.titleTextView && var2 != this.subtitleTextView && var2 != this.actionMode && var2 != this.menu && var2 != this.backButtonImageView) {
         var5 = false;
      } else {
         var5 = true;
      }

      int var7;
      if (var5) {
         var1.save();
         float var6 = -this.getTranslationY();
         if (this.occupyStatusBar) {
            var7 = AndroidUtilities.statusBarHeight;
         } else {
            var7 = 0;
         }

         var1.clipRect(0.0F, var6 + (float)var7, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
      }

      boolean var8 = super.drawChild(var1, var2, var3);
      if (this.supportsHolidayImage && !this.titleOverlayShown && !LocaleController.isRTL && var2 == this.titleTextView) {
         Drawable var11 = Theme.getCurrentHolidayDrawable();
         if (var11 != null) {
            TextPaint var9 = this.titleTextView.getTextPaint();
            var9.getFontMetricsInt(this.fontMetricsInt);
            var9.getTextBounds((String)this.titleTextView.getText(), 0, 1, this.rect);
            var7 = this.titleTextView.getTextStartX() + Theme.getCurrentHolidayDrawableXOffset() + (this.rect.width() - (var11.getIntrinsicWidth() + Theme.getCurrentHolidayDrawableXOffset())) / 2;
            int var10 = this.titleTextView.getTextStartY() + Theme.getCurrentHolidayDrawableYOffset() + (int)Math.ceil((double)((float)(this.titleTextView.getTextHeight() - this.rect.height()) / 2.0F));
            var11.setBounds(var7, var10 - var11.getIntrinsicHeight(), var11.getIntrinsicWidth() + var7, var10);
            var11.draw(var1);
            if (Theme.canStartHolidayAnimation()) {
               if (this.snowflakesEffect == null) {
                  this.snowflakesEffect = new SnowflakesEffect();
               }
            } else if (!this.manualStart && this.snowflakesEffect != null) {
               this.snowflakesEffect = null;
            }

            SnowflakesEffect var12 = this.snowflakesEffect;
            if (var12 != null) {
               var12.onDraw(this, var1);
            } else {
               FireworksEffect var13 = this.fireworksEffect;
               if (var13 != null) {
                  var13.onDraw(this, var1);
               }
            }
         }
      }

      if (var5) {
         var1.restore();
      }

      return var8;
   }

   public ActionBar.ActionBarMenuOnItemClick getActionBarMenuOnItemClick() {
      return this.actionBarMenuOnItemClick;
   }

   public boolean getAddToContainer() {
      return this.addToContainer;
   }

   public View getBackButton() {
      return this.backButtonImageView;
   }

   public boolean getCastShadows() {
      return this.castShadows;
   }

   public boolean getOccupyStatusBar() {
      return this.occupyStatusBar;
   }

   public String getSubtitle() {
      SimpleTextView var1 = this.subtitleTextView;
      return var1 == null ? null : var1.getText().toString();
   }

   public SimpleTextView getSubtitleTextView() {
      return this.subtitleTextView;
   }

   public String getTitle() {
      SimpleTextView var1 = this.titleTextView;
      return var1 == null ? null : var1.getText().toString();
   }

   public SimpleTextView getTitleTextView() {
      return this.titleTextView;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void hideActionMode() {
      ActionBarMenu var1 = this.actionMode;
      if (var1 != null && this.actionModeVisible) {
         var1.hideAllPopupMenus();
         this.actionModeVisible = false;
         ArrayList var4 = new ArrayList();
         var4.add(ObjectAnimator.ofFloat(this.actionMode, View.ALPHA, new float[]{0.0F}));
         if (this.actionModeHidingViews != null) {
            int var2 = 0;

            while(true) {
               View[] var3 = this.actionModeHidingViews;
               if (var2 >= var3.length) {
                  break;
               }

               if (var3 != null) {
                  var3[var2].setVisibility(0);
                  var4.add(ObjectAnimator.ofFloat(this.actionModeHidingViews[var2], View.ALPHA, new float[]{1.0F}));
               }

               ++var2;
            }
         }

         View var8 = this.actionModeTranslationView;
         if (var8 != null) {
            var4.add(ObjectAnimator.ofFloat(var8, View.TRANSLATION_Y, new float[]{0.0F}));
            this.actionModeTranslationView = null;
         }

         var8 = this.actionModeShowingView;
         if (var8 != null) {
            var4.add(ObjectAnimator.ofFloat(var8, View.ALPHA, new float[]{0.0F}));
         }

         if (this.occupyStatusBar) {
            var8 = this.actionModeTop;
            if (var8 != null) {
               var4.add(ObjectAnimator.ofFloat(var8, View.ALPHA, new float[]{0.0F}));
            }
         }

         AnimatorSet var9 = this.actionModeAnimation;
         if (var9 != null) {
            var9.cancel();
         }

         this.actionModeAnimation = new AnimatorSet();
         this.actionModeAnimation.playTogether(var4);
         this.actionModeAnimation.setDuration(200L);
         this.actionModeAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(var1)) {
                  ActionBar.this.actionModeAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(var1)) {
                  ActionBar.this.actionModeAnimation = null;
                  ActionBar.this.actionMode.setVisibility(4);
                  if (ActionBar.this.occupyStatusBar && ActionBar.this.actionModeTop != null) {
                     ActionBar.this.actionModeTop.setVisibility(4);
                  }

                  if (ActionBar.this.actionModeExtraView != null) {
                     ActionBar.this.actionModeExtraView.setVisibility(4);
                  }
               }

            }
         });
         this.actionModeAnimation.start();
         if (!this.isSearchFieldVisible) {
            SimpleTextView var5 = this.titleTextView;
            if (var5 != null) {
               var5.setVisibility(0);
            }

            var5 = this.subtitleTextView;
            if (var5 != null && !TextUtils.isEmpty(var5.getText())) {
               this.subtitleTextView.setVisibility(0);
            }
         }

         var1 = this.menu;
         if (var1 != null) {
            var1.setVisibility(0);
         }

         ImageView var6 = this.backButtonImageView;
         if (var6 != null) {
            Drawable var7 = var6.getDrawable();
            if (var7 instanceof BackDrawable) {
               ((BackDrawable)var7).setRotation(0.0F, true);
            }

            this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
         }
      }

   }

   public boolean isActionModeShowed() {
      boolean var1;
      if (this.actionMode != null && this.actionModeVisible) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isSearchFieldVisible() {
      return this.isSearchFieldVisible;
   }

   // $FF: synthetic method
   public void lambda$createBackButtonImage$1$ActionBar(View var1) {
      if (!this.actionModeVisible && this.isSearchFieldVisible) {
         this.closeSearchField();
      } else {
         ActionBar.ActionBarMenuOnItemClick var2 = this.actionBarMenuOnItemClick;
         if (var2 != null) {
            var2.onItemClick(-1);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$new$0$ActionBar(View var1) {
      if (!this.isSearchFieldVisible()) {
         Runnable var2 = this.titleActionRunnable;
         if (var2 != null) {
            var2.run();
         }

      }
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      if (this.supportsHolidayImage && !this.titleOverlayShown && !LocaleController.isRTL && var1.getAction() == 0) {
         Drawable var2 = Theme.getCurrentHolidayDrawable();
         if (var2 != null && var2.getBounds().contains((int)var1.getX(), (int)var1.getY())) {
            this.manualStart = true;
            if (this.snowflakesEffect == null) {
               this.fireworksEffect = null;
               this.snowflakesEffect = new SnowflakesEffect();
               this.titleTextView.invalidate();
               this.invalidate();
            } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
               this.snowflakesEffect = null;
               this.fireworksEffect = new FireworksEffect();
               this.titleTextView.invalidate();
               this.invalidate();
            }
         }
      }

      return super.onInterceptTouchEvent(var1);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var1 = this.occupyStatusBar;
      byte var6 = 0;
      int var7;
      if (var1) {
         var7 = AndroidUtilities.statusBarHeight;
      } else {
         var7 = 0;
      }

      ImageView var8 = this.backButtonImageView;
      float var9;
      int var10;
      if (var8 != null && var8.getVisibility() != 8) {
         var8 = this.backButtonImageView;
         var8.layout(0, var7, var8.getMeasuredWidth(), this.backButtonImageView.getMeasuredHeight() + var7);
         if (AndroidUtilities.isTablet()) {
            var9 = 80.0F;
         } else {
            var9 = 72.0F;
         }

         var10 = AndroidUtilities.dp(var9);
      } else {
         if (AndroidUtilities.isTablet()) {
            var9 = 26.0F;
         } else {
            var9 = 18.0F;
         }

         var10 = AndroidUtilities.dp(var9);
      }

      ActionBarMenu var17 = this.menu;
      int var11;
      if (var17 != null && var17.getVisibility() != 8) {
         if (this.isSearchFieldVisible) {
            if (AndroidUtilities.isTablet()) {
               var9 = 74.0F;
            } else {
               var9 = 66.0F;
            }

            var11 = AndroidUtilities.dp(var9);
         } else {
            var11 = var4 - var2 - this.menu.getMeasuredWidth();
         }

         var17 = this.menu;
         var17.layout(var11, var7, var17.getMeasuredWidth() + var11, this.menu.getMeasuredHeight() + var7);
      }

      SimpleTextView var18 = this.titleTextView;
      if (var18 != null && var18.getVisibility() != 8) {
         var18 = this.subtitleTextView;
         if (var18 != null && var18.getVisibility() != 8) {
            var11 = (getCurrentActionBarHeight() / 2 - this.titleTextView.getTextHeight()) / 2;
            if (!AndroidUtilities.isTablet() && this.getResources().getConfiguration().orientation == 2) {
               var9 = 2.0F;
            } else {
               var9 = 3.0F;
            }

            var11 += AndroidUtilities.dp(var9);
         } else {
            var11 = (getCurrentActionBarHeight() - this.titleTextView.getTextHeight()) / 2;
         }

         var18 = this.titleTextView;
         var11 += var7;
         var18.layout(var10, var11, var18.getMeasuredWidth() + var10, this.titleTextView.getTextHeight() + var11);
      }

      var18 = this.subtitleTextView;
      int var12;
      int var13;
      if (var18 != null && var18.getVisibility() != 8) {
         var12 = getCurrentActionBarHeight() / 2;
         var11 = (getCurrentActionBarHeight() / 2 - this.subtitleTextView.getTextHeight()) / 2;
         if (!AndroidUtilities.isTablet()) {
            var13 = this.getResources().getConfiguration().orientation;
         }

         var13 = AndroidUtilities.dp(1.0F);
         var18 = this.subtitleTextView;
         var7 += var12 + var11 - var13;
         var18.layout(var10, var7, var18.getMeasuredWidth() + var10, this.subtitleTextView.getTextHeight() + var7);
      }

      var12 = this.getChildCount();

      for(var10 = var6; var10 < var12; ++var10) {
         View var19 = this.getChildAt(var10);
         if (var19.getVisibility() != 8 && var19 != this.titleTextView && var19 != this.subtitleTextView && var19 != this.menu && var19 != this.backButtonImageView) {
            LayoutParams var14 = (LayoutParams)var19.getLayoutParams();
            var13 = var19.getMeasuredWidth();
            int var15 = var19.getMeasuredHeight();
            var11 = var14.gravity;
            var7 = var11;
            if (var11 == -1) {
               var7 = 51;
            }

            int var16;
            label111: {
               var16 = var7 & 112;
               var7 = var7 & 7 & 7;
               if (var7 != 1) {
                  if (var7 != 5) {
                     var11 = var14.leftMargin;
                     break label111;
                  }

                  var7 = var4 - var13;
                  var11 = var14.rightMargin;
               } else {
                  var7 = (var4 - var2 - var13) / 2 + var14.leftMargin;
                  var11 = var14.rightMargin;
               }

               var11 = var7 - var11;
            }

            label105: {
               if (var16 != 16) {
                  if (var16 == 48) {
                     var7 = var14.topMargin;
                     break label105;
                  }

                  if (var16 != 80) {
                     var7 = var14.topMargin;
                     break label105;
                  }

                  var7 = var5 - var3 - var15;
                  var16 = var14.bottomMargin;
               } else {
                  var7 = (var5 - var3 - var15) / 2 + var14.topMargin;
                  var16 = var14.bottomMargin;
               }

               var7 -= var16;
            }

            var19.layout(var11, var7, var13 + var11, var15 + var7);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      MeasureSpec.getSize(var2);
      int var4 = getCurrentActionBarHeight();
      int var5 = MeasureSpec.makeMeasureSpec(var4, 1073741824);
      this.ignoreLayoutRequest = true;
      View var6 = this.actionModeTop;
      if (var6 != null) {
         ((LayoutParams)var6.getLayoutParams()).height = AndroidUtilities.statusBarHeight;
      }

      ActionBarMenu var10 = this.actionMode;
      byte var7 = 0;
      if (var10 != null) {
         if (this.occupyStatusBar) {
            var2 = AndroidUtilities.statusBarHeight;
         } else {
            var2 = 0;
         }

         var10.setPadding(0, var2, 0, 0);
      }

      this.ignoreLayoutRequest = false;
      if (this.occupyStatusBar) {
         var2 = AndroidUtilities.statusBarHeight;
      } else {
         var2 = 0;
      }

      this.setMeasuredDimension(var3, var4 + var2 + this.extraHeight);
      ImageView var12 = this.backButtonImageView;
      float var8;
      if (var12 != null && var12.getVisibility() != 8) {
         this.backButtonImageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(54.0F), 1073741824), var5);
         if (AndroidUtilities.isTablet()) {
            var8 = 80.0F;
         } else {
            var8 = 72.0F;
         }

         var2 = AndroidUtilities.dp(var8);
      } else {
         if (AndroidUtilities.isTablet()) {
            var8 = 26.0F;
         } else {
            var8 = 18.0F;
         }

         var2 = AndroidUtilities.dp(var8);
      }

      var10 = this.menu;
      if (var10 != null && var10.getVisibility() != 8) {
         if (this.isSearchFieldVisible) {
            if (AndroidUtilities.isTablet()) {
               var8 = 74.0F;
            } else {
               var8 = 66.0F;
            }

            var4 = MeasureSpec.makeMeasureSpec(var3 - AndroidUtilities.dp(var8), 1073741824);
         } else {
            var4 = MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE);
         }

         this.menu.measure(var4, var5);
      }

      label173: {
         SimpleTextView var13 = this.titleTextView;
         if (var13 == null || var13.getVisibility() == 8) {
            var13 = this.subtitleTextView;
            if (var13 == null || var13.getVisibility() == 8) {
               break label173;
            }
         }

         var10 = this.menu;
         if (var10 != null) {
            var4 = var10.getMeasuredWidth();
         } else {
            var4 = 0;
         }

         label163: {
            var3 = var3 - var4 - AndroidUtilities.dp(16.0F) - var2 - this.titleRightMargin;
            var13 = this.titleTextView;
            byte var9 = 14;
            byte var11 = 18;
            if (var13 != null && var13.getVisibility() != 8) {
               var13 = this.subtitleTextView;
               if (var13 != null && var13.getVisibility() != 8) {
                  var13 = this.titleTextView;
                  if (AndroidUtilities.isTablet()) {
                     var11 = 20;
                  }

                  var13.setTextSize(var11);
                  var13 = this.subtitleTextView;
                  if (AndroidUtilities.isTablet()) {
                     var9 = 16;
                  }

                  var13.setTextSize(var9);
                  break label163;
               }
            }

            var13 = this.titleTextView;
            if (var13 != null && var13.getVisibility() != 8) {
               var13 = this.titleTextView;
               if (AndroidUtilities.isTablet() || this.getResources().getConfiguration().orientation != 2) {
                  var11 = 20;
               }

               var13.setTextSize(var11);
            }

            var13 = this.subtitleTextView;
            if (var13 != null && var13.getVisibility() != 8) {
               var13 = this.subtitleTextView;
               if (AndroidUtilities.isTablet() || this.getResources().getConfiguration().orientation != 2) {
                  var9 = 16;
               }

               var13.setTextSize(var9);
            }
         }

         var13 = this.titleTextView;
         if (var13 != null && var13.getVisibility() != 8) {
            this.titleTextView.measure(MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), Integer.MIN_VALUE));
         }

         var13 = this.subtitleTextView;
         if (var13 != null && var13.getVisibility() != 8) {
            this.subtitleTextView.measure(MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), Integer.MIN_VALUE));
         }
      }

      var4 = this.getChildCount();

      for(var2 = var7; var2 < var4; ++var2) {
         var6 = this.getChildAt(var2);
         if (var6.getVisibility() != 8 && var6 != this.titleTextView && var6 != this.subtitleTextView && var6 != this.menu && var6 != this.backButtonImageView) {
            this.measureChildWithMargins(var6, var1, 0, MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824), 0);
         }
      }

   }

   public void onMenuButtonPressed() {
      if (!this.isActionModeShowed()) {
         ActionBarMenu var1 = this.menu;
         if (var1 != null) {
            var1.onMenuButtonPressed();
         }

      }
   }

   protected void onPause() {
      ActionBarMenu var1 = this.menu;
      if (var1 != null) {
         var1.hideAllPopupMenus();
      }

   }

   protected void onSearchFieldVisibilityChanged(boolean var1) {
      this.isSearchFieldVisible = var1;
      SimpleTextView var2 = this.titleTextView;
      byte var3 = 4;
      byte var4;
      if (var2 != null) {
         if (var1) {
            var4 = 4;
         } else {
            var4 = 0;
         }

         var2.setVisibility(var4);
      }

      var2 = this.subtitleTextView;
      if (var2 != null && !TextUtils.isEmpty(var2.getText())) {
         var2 = this.subtitleTextView;
         if (var1) {
            var4 = var3;
         } else {
            var4 = 0;
         }

         var2.setVisibility(var4);
      }

      Drawable var6 = this.backButtonImageView.getDrawable();
      if (var6 instanceof MenuDrawable) {
         MenuDrawable var7 = (MenuDrawable)var6;
         var7.setRotateToBack(true);
         float var5;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         var7.setRotation(var5, true);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2;
      if (!super.onTouchEvent(var1) && !this.interceptTouches) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void openSearchField(String var1, boolean var2) {
      ActionBarMenu var3 = this.menu;
      if (var3 != null && var1 != null) {
         var3.openSearchField(this.isSearchFieldVisible ^ true, var1, var2);
      }

   }

   public void requestLayout() {
      if (!this.ignoreLayoutRequest) {
         super.requestLayout();
      }
   }

   public void setActionBarMenuOnItemClick(ActionBar.ActionBarMenuOnItemClick var1) {
      this.actionBarMenuOnItemClick = var1;
   }

   public void setActionModeColor(int var1) {
      ActionBarMenu var2 = this.actionMode;
      if (var2 != null) {
         var2.setBackgroundColor(var1);
      }

   }

   public void setActionModeTopColor(int var1) {
      View var2 = this.actionModeTop;
      if (var2 != null) {
         var2.setBackgroundColor(var1);
      }

   }

   public void setAddToContainer(boolean var1) {
      this.addToContainer = var1;
   }

   public void setAllowOverlayTitle(boolean var1) {
      this.allowOverlayTitle = var1;
   }

   public void setBackButtonContentDescription(CharSequence var1) {
      ImageView var2 = this.backButtonImageView;
      if (var2 != null) {
         var2.setContentDescription(var1);
      }

   }

   public void setBackButtonDrawable(Drawable var1) {
      if (this.backButtonImageView == null) {
         this.createBackButtonImage();
      }

      ImageView var2 = this.backButtonImageView;
      byte var3;
      if (var1 == null) {
         var3 = 8;
      } else {
         var3 = 0;
      }

      var2.setVisibility(var3);
      this.backButtonImageView.setImageDrawable(var1);
      if (var1 instanceof BackDrawable) {
         BackDrawable var5 = (BackDrawable)var1;
         float var4;
         if (this.isActionModeShowed()) {
            var4 = 1.0F;
         } else {
            var4 = 0.0F;
         }

         var5.setRotation(var4, false);
         var5.setRotatedColor(this.itemsActionModeColor);
         var5.setColor(this.itemsColor);
      }

   }

   public void setBackButtonImage(int var1) {
      if (this.backButtonImageView == null) {
         this.createBackButtonImage();
      }

      ImageView var2 = this.backButtonImageView;
      byte var3;
      if (var1 == 0) {
         var3 = 8;
      } else {
         var3 = 0;
      }

      var2.setVisibility(var3);
      this.backButtonImageView.setImageResource(var1);
   }

   public void setCastShadows(boolean var1) {
      this.castShadows = var1;
   }

   public void setClipContent(boolean var1) {
      this.clipContent = var1;
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      ImageView var2 = this.backButtonImageView;
      if (var2 != null) {
         var2.setEnabled(var1);
      }

      ActionBarMenu var3 = this.menu;
      if (var3 != null) {
         var3.setEnabled(var1);
      }

      var3 = this.actionMode;
      if (var3 != null) {
         var3.setEnabled(var1);
      }

   }

   public void setExtraHeight(int var1) {
      this.extraHeight = var1;
      ActionBarMenu var2 = this.actionMode;
      if (var2 != null) {
         LayoutParams var3 = (LayoutParams)var2.getLayoutParams();
         var3.bottomMargin = this.extraHeight;
         this.actionMode.setLayoutParams(var3);
      }

   }

   public void setInterceptTouches(boolean var1) {
      this.interceptTouches = var1;
   }

   public void setItemsBackgroundColor(int var1, boolean var2) {
      ImageView var3;
      ActionBarMenu var4;
      if (var2) {
         this.itemsActionModeBackgroundColor = var1;
         if (this.actionModeVisible) {
            var3 = this.backButtonImageView;
            if (var3 != null) {
               var3.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsActionModeBackgroundColor));
            }
         }

         var4 = this.actionMode;
         if (var4 != null) {
            var4.updateItemsBackgroundColor();
         }
      } else {
         this.itemsBackgroundColor = var1;
         var3 = this.backButtonImageView;
         if (var3 != null) {
            var3.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
         }

         var4 = this.menu;
         if (var4 != null) {
            var4.updateItemsBackgroundColor();
         }
      }

   }

   public void setItemsColor(int var1, boolean var2) {
      ActionBarMenu var3;
      ImageView var5;
      Drawable var6;
      if (var2) {
         this.itemsActionModeColor = var1;
         var3 = this.actionMode;
         if (var3 != null) {
            var3.updateItemsColor();
         }

         var5 = this.backButtonImageView;
         if (var5 != null) {
            var6 = var5.getDrawable();
            if (var6 instanceof BackDrawable) {
               ((BackDrawable)var6).setRotatedColor(var1);
            }
         }
      } else {
         this.itemsColor = var1;
         var5 = this.backButtonImageView;
         if (var5 != null) {
            int var4 = this.itemsColor;
            if (var4 != 0) {
               var5.setColorFilter(new PorterDuffColorFilter(var4, Mode.MULTIPLY));
               var6 = this.backButtonImageView.getDrawable();
               if (var6 instanceof BackDrawable) {
                  ((BackDrawable)var6).setColor(var1);
               }
            }
         }

         var3 = this.menu;
         if (var3 != null) {
            var3.updateItemsColor();
         }
      }

   }

   public void setOccupyStatusBar(boolean var1) {
      this.occupyStatusBar = var1;
      ActionBarMenu var2 = this.actionMode;
      if (var2 != null) {
         int var3;
         if (this.occupyStatusBar) {
            var3 = AndroidUtilities.statusBarHeight;
         } else {
            var3 = 0;
         }

         var2.setPadding(0, var3, 0, 0);
      }

   }

   public void setPopupBackgroundColor(int var1) {
      ActionBarMenu var2 = this.menu;
      if (var2 != null) {
         var2.redrawPopup(var1);
      }

   }

   public void setPopupItemsColor(int var1, boolean var2) {
      ActionBarMenu var3 = this.menu;
      if (var3 != null) {
         var3.setPopupItemsColor(var1, var2);
      }

   }

   public void setSearchTextColor(int var1, boolean var2) {
      ActionBarMenu var3 = this.menu;
      if (var3 != null) {
         var3.setSearchTextColor(var1, var2);
      }

   }

   public void setSubtitle(CharSequence var1) {
      if (var1 != null && this.subtitleTextView == null) {
         this.createSubtitleTextView();
      }

      SimpleTextView var2 = this.subtitleTextView;
      if (var2 != null) {
         byte var3;
         if (!TextUtils.isEmpty(var1) && !this.isSearchFieldVisible) {
            var3 = 0;
         } else {
            var3 = 8;
         }

         var2.setVisibility(var3);
         this.subtitleTextView.setText(var1);
      }

   }

   public void setSubtitleColor(int var1) {
      if (this.subtitleTextView == null) {
         this.createSubtitleTextView();
      }

      this.subtitleTextView.setTextColor(var1);
   }

   public void setSupportsHolidayImage(boolean var1) {
      this.supportsHolidayImage = var1;
      if (this.supportsHolidayImage) {
         this.fontMetricsInt = new FontMetricsInt();
         this.rect = new Rect();
      }

      this.invalidate();
   }

   public void setTitle(CharSequence var1) {
      if (var1 != null && this.titleTextView == null) {
         this.createTitleTextView();
      }

      SimpleTextView var2 = this.titleTextView;
      if (var2 != null) {
         this.lastTitle = var1;
         byte var3;
         if (var1 != null && !this.isSearchFieldVisible) {
            var3 = 0;
         } else {
            var3 = 4;
         }

         var2.setVisibility(var3);
         this.titleTextView.setText(var1);
      }

   }

   public void setTitleActionRunnable(Runnable var1) {
      this.titleActionRunnable = var1;
      this.lastRunnable = var1;
   }

   public void setTitleColor(int var1) {
      if (this.titleTextView == null) {
         this.createTitleTextView();
      }

      this.titleTextView.setTextColor(var1);
   }

   public void setTitleOverlayText(String var1, int var2, Runnable var3) {
      if (this.allowOverlayTitle && this.parentFragment.parentLayout != null) {
         Object var4;
         if (var1 != null) {
            var4 = LocaleController.getString(var1, var2);
         } else {
            var4 = this.lastTitle;
         }

         if (var4 != null && this.titleTextView == null) {
            this.createTitleTextView();
         }

         if (this.titleTextView != null) {
            byte var7 = 0;
            boolean var5;
            if (var1 != null) {
               var5 = true;
            } else {
               var5 = false;
            }

            this.titleOverlayShown = var5;
            if (this.supportsHolidayImage) {
               this.titleTextView.invalidate();
               this.invalidate();
            }

            SimpleTextView var6 = this.titleTextView;
            if (var4 == null || this.isSearchFieldVisible) {
               var7 = 4;
            }

            var6.setVisibility(var7);
            this.titleTextView.setText((CharSequence)var4);
         }

         if (var3 == null) {
            var3 = this.lastRunnable;
         }

         this.titleActionRunnable = var3;
      }

   }

   public void setTitleRightMargin(int var1) {
      this.titleRightMargin = var1;
   }

   public void setTranslationY(float var1) {
      super.setTranslationY(var1);
      if (this.clipContent) {
         this.invalidate();
      }

   }

   public void showActionMode() {
      this.showActionMode((View)null, (View)null, (View[])null, (boolean[])null, (View)null, 0);
   }

   public void showActionMode(View var1, View var2, View[] var3, final boolean[] var4, View var5, int var6) {
      if (this.actionMode != null && !this.actionModeVisible) {
         this.actionModeVisible = true;
         ArrayList var7 = new ArrayList();
         var7.add(ObjectAnimator.ofFloat(this.actionMode, View.ALPHA, new float[]{0.0F, 1.0F}));
         if (var3 != null) {
            for(int var8 = 0; var8 < var3.length; ++var8) {
               if (var3[var8] != null) {
                  var7.add(ObjectAnimator.ofFloat(var3[var8], View.ALPHA, new float[]{1.0F, 0.0F}));
               }
            }
         }

         if (var2 != null) {
            var7.add(ObjectAnimator.ofFloat(var2, View.ALPHA, new float[]{0.0F, 1.0F}));
         }

         if (var5 != null) {
            var7.add(ObjectAnimator.ofFloat(var5, View.TRANSLATION_Y, new float[]{(float)var6}));
            this.actionModeTranslationView = var5;
         }

         this.actionModeExtraView = var1;
         this.actionModeShowingView = var2;
         this.actionModeHidingViews = var3;
         if (this.occupyStatusBar) {
            var1 = this.actionModeTop;
            if (var1 != null) {
               var7.add(ObjectAnimator.ofFloat(var1, View.ALPHA, new float[]{0.0F, 1.0F}));
            }
         }

         AnimatorSet var9 = this.actionModeAnimation;
         if (var9 != null) {
            var9.cancel();
         }

         this.actionModeAnimation = new AnimatorSet();
         this.actionModeAnimation.playTogether(var7);
         this.actionModeAnimation.setDuration(200L);
         this.actionModeAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(var1)) {
                  ActionBar.this.actionModeAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(var1)) {
                  ActionBar.this.actionModeAnimation = null;
                  if (ActionBar.this.titleTextView != null) {
                     ActionBar.this.titleTextView.setVisibility(4);
                  }

                  if (ActionBar.this.subtitleTextView != null && !TextUtils.isEmpty(ActionBar.this.subtitleTextView.getText())) {
                     ActionBar.this.subtitleTextView.setVisibility(4);
                  }

                  if (ActionBar.this.menu != null) {
                     ActionBar.this.menu.setVisibility(4);
                  }

                  if (ActionBar.this.actionModeHidingViews != null) {
                     for(int var2 = 0; var2 < ActionBar.this.actionModeHidingViews.length; ++var2) {
                        if (ActionBar.this.actionModeHidingViews[var2] != null) {
                           boolean[] var3 = var4;
                           if (var3 == null || var2 >= var3.length || var3[var2]) {
                              ActionBar.this.actionModeHidingViews[var2].setVisibility(4);
                           }
                        }
                     }
                  }
               }

            }

            public void onAnimationStart(Animator var1) {
               ActionBar.this.actionMode.setVisibility(0);
               if (ActionBar.this.occupyStatusBar && ActionBar.this.actionModeTop != null) {
                  ActionBar.this.actionModeTop.setVisibility(0);
               }

            }
         });
         this.actionModeAnimation.start();
         ImageView var10 = this.backButtonImageView;
         if (var10 != null) {
            Drawable var11 = var10.getDrawable();
            if (var11 instanceof BackDrawable) {
               ((BackDrawable)var11).setRotation(1.0F, true);
            }

            this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsActionModeBackgroundColor));
         }
      }

   }

   public void showActionModeTop() {
      if (this.occupyStatusBar && this.actionModeTop == null) {
         this.actionModeTop = new View(this.getContext());
         this.actionModeTop.setBackgroundColor(Theme.getColor("actionBarActionModeDefaultTop"));
         this.addView(this.actionModeTop);
         LayoutParams var1 = (LayoutParams)this.actionModeTop.getLayoutParams();
         var1.height = AndroidUtilities.statusBarHeight;
         var1.width = -1;
         var1.gravity = 51;
         this.actionModeTop.setLayoutParams(var1);
      }

   }

   public static class ActionBarMenuOnItemClick {
      public boolean canOpenMenu() {
         return true;
      }

      public void onItemClick(int var1) {
      }
   }
}
