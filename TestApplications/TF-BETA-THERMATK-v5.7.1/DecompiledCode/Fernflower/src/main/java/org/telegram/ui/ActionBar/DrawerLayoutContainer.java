package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class DrawerLayoutContainer extends FrameLayout {
   private static final int MIN_DRAWER_MARGIN = 64;
   private boolean allowDrawContent = true;
   private boolean allowOpenDrawer;
   private Paint backgroundPaint = new Paint();
   private boolean beginTrackingSent;
   private int behindKeyboardColor;
   private AnimatorSet currentAnimation;
   private ViewGroup drawerLayout;
   private boolean drawerOpened;
   private float drawerPosition;
   private boolean hasCutout;
   private boolean inLayout;
   private Object lastInsets;
   private boolean maybeStartTracking;
   private int minDrawerMargin;
   private int paddingTop;
   private ActionBarLayout parentActionBarLayout;
   private Rect rect = new Rect();
   private float scrimOpacity;
   private Paint scrimPaint = new Paint();
   private Drawable shadowLeft;
   private boolean startedTracking;
   private int startedTrackingPointerId;
   private int startedTrackingX;
   private int startedTrackingY;
   private VelocityTracker velocityTracker;

   public DrawerLayoutContainer(Context var1) {
      super(var1);
      this.minDrawerMargin = (int)(AndroidUtilities.density * 64.0F + 0.5F);
      this.setDescendantFocusability(262144);
      this.setFocusableInTouchMode(true);
      if (VERSION.SDK_INT >= 21) {
         this.setFitsSystemWindows(true);
         this.setOnApplyWindowInsetsListener(new _$$Lambda$DrawerLayoutContainer$dOsUXLZuN_il_QGSmGuPct7OsoA(this));
         this.setSystemUiVisibility(1280);
      }

      this.shadowLeft = this.getResources().getDrawable(2131165599);
   }

   @SuppressLint({"NewApi"})
   private void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3, boolean var4) {
      WindowInsets var5 = (WindowInsets)var2;
      byte var6 = 0;
      WindowInsets var7;
      if (var3 == 3) {
         var7 = var5.replaceSystemWindowInsets(var5.getSystemWindowInsetLeft(), var5.getSystemWindowInsetTop(), 0, var5.getSystemWindowInsetBottom());
      } else {
         var7 = var5;
         if (var3 == 5) {
            var7 = var5.replaceSystemWindowInsets(0, var5.getSystemWindowInsetTop(), var5.getSystemWindowInsetRight(), var5.getSystemWindowInsetBottom());
         }
      }

      var1.leftMargin = var7.getSystemWindowInsetLeft();
      if (var4) {
         var3 = var6;
      } else {
         var3 = var7.getSystemWindowInsetTop();
      }

      var1.topMargin = var3;
      var1.rightMargin = var7.getSystemWindowInsetRight();
      var1.bottomMargin = var7.getSystemWindowInsetBottom();
   }

   @SuppressLint({"NewApi"})
   private void dispatchChildInsets(View var1, Object var2, int var3) {
      WindowInsets var4 = (WindowInsets)var2;
      WindowInsets var5;
      if (var3 == 3) {
         var5 = var4.replaceSystemWindowInsets(var4.getSystemWindowInsetLeft(), var4.getSystemWindowInsetTop(), 0, var4.getSystemWindowInsetBottom());
      } else {
         var5 = var4;
         if (var3 == 5) {
            var5 = var4.replaceSystemWindowInsets(0, var4.getSystemWindowInsetTop(), var4.getSystemWindowInsetRight(), var4.getSystemWindowInsetBottom());
         }
      }

      var1.dispatchApplyWindowInsets(var5);
   }

   private float getScrimOpacity() {
      return this.scrimOpacity;
   }

   private int getTopInset(Object var1) {
      int var2 = VERSION.SDK_INT;
      byte var3 = 0;
      int var4 = var3;
      if (var2 >= 21) {
         var4 = var3;
         if (var1 != null) {
            var4 = ((WindowInsets)var1).getSystemWindowInsetTop();
         }
      }

      return var4;
   }

   private void onDrawerAnimationEnd(boolean var1) {
      this.startedTracking = false;
      this.currentAnimation = null;
      this.drawerOpened = var1;
      if (!var1) {
         ViewGroup var2 = this.drawerLayout;
         if (var2 instanceof ListView) {
            ((ListView)var2).setSelectionFromTop(0, 0);
         }
      }

      if (VERSION.SDK_INT >= 19) {
         for(int var3 = 0; var3 < this.getChildCount(); ++var3) {
            View var5 = this.getChildAt(var3);
            if (var5 != this.drawerLayout) {
               byte var4;
               if (var1) {
                  var4 = 4;
               } else {
                  var4 = 0;
               }

               var5.setImportantForAccessibility(var4);
            }
         }
      }

      this.sendAccessibilityEvent(32);
   }

   private void prepareForDrawerOpen(MotionEvent var1) {
      this.maybeStartTracking = false;
      this.startedTracking = true;
      if (var1 != null) {
         this.startedTrackingX = (int)var1.getX();
      }

      this.beginTrackingSent = false;
   }

   private void setScrimOpacity(float var1) {
      this.scrimOpacity = var1;
      this.invalidate();
   }

   public void cancelCurrentAnimation() {
      AnimatorSet var1 = this.currentAnimation;
      if (var1 != null) {
         var1.cancel();
         this.currentAnimation = null;
      }

   }

   public void closeDrawer(boolean var1) {
      this.cancelCurrentAnimation();
      AnimatorSet var2 = new AnimatorSet();
      var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "drawerPosition", new float[]{0.0F})});
      var2.setInterpolator(new DecelerateInterpolator());
      if (var1) {
         var2.setDuration((long)Math.max((int)(200.0F / (float)this.drawerLayout.getMeasuredWidth() * this.drawerPosition), 50));
      } else {
         var2.setDuration(300L);
      }

      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            DrawerLayoutContainer.this.onDrawerAnimationEnd(false);
         }
      });
      var2.start();
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = this.allowDrawContent;
      int var6 = 0;
      if (!var5) {
         return false;
      } else {
         int var7 = this.getHeight();
         boolean var8;
         if (var2 != this.drawerLayout) {
            var8 = true;
         } else {
            var8 = false;
         }

         int var9 = this.getWidth();
         int var10 = var1.save();
         int var13;
         if (var8) {
            int var11 = this.getChildCount();
            var6 = 0;
            int var12 = 0;

            int var15;
            for(var13 = 0; var6 < var11; var13 = var15) {
               View var14 = this.getChildAt(var6);
               var15 = var13;
               if (var14.getVisibility() == 0) {
                  var15 = var13;
                  if (var14 != this.drawerLayout) {
                     var15 = var6;
                  }
               }

               var13 = var12;
               if (var14 != var2) {
                  var13 = var12;
                  if (var14.getVisibility() == 0) {
                     var13 = var12;
                     if (var14 == this.drawerLayout) {
                        if (var14.getHeight() < var7) {
                           var13 = var12;
                        } else {
                           var13 = (int)var14.getX();
                           int var16 = var14.getMeasuredWidth() + var13;
                           var13 = var12;
                           if (var16 > var12) {
                              var13 = var16;
                           }
                        }
                     }
                  }
               }

               ++var6;
               var12 = var13;
            }

            if (var12 != 0) {
               var1.clipRect(var12, 0, var9, this.getHeight());
            }

            var6 = var12;
         } else {
            var13 = 0;
         }

         var5 = super.drawChild(var1, var2, var3);
         var1.restoreToCount(var10);
         if (this.scrimOpacity > 0.0F && var8) {
            if (this.indexOfChild(var2) == var13) {
               this.scrimPaint.setColor((int)(this.scrimOpacity * 153.0F) << 24);
               var1.drawRect((float)var6, 0.0F, (float)var9, (float)this.getHeight(), this.scrimPaint);
            }
         } else if (this.shadowLeft != null) {
            float var17 = Math.max(0.0F, Math.min(this.drawerPosition / (float)AndroidUtilities.dp(20.0F), 1.0F));
            if (var17 != 0.0F) {
               this.shadowLeft.setBounds((int)this.drawerPosition, var2.getTop(), (int)this.drawerPosition + this.shadowLeft.getIntrinsicWidth(), var2.getBottom());
               this.shadowLeft.setAlpha((int)(var17 * 255.0F));
               this.shadowLeft.draw(var1);
            }
         }

         return var5;
      }
   }

   public View getDrawerLayout() {
      return this.drawerLayout;
   }

   public float getDrawerPosition() {
      return this.drawerPosition;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public boolean isDrawerOpened() {
      return this.drawerOpened;
   }

   // $FF: synthetic method
   public WindowInsets lambda$new$0$DrawerLayoutContainer(View var1, WindowInsets var2) {
      DrawerLayoutContainer var6 = (DrawerLayoutContainer)var1;
      if (AndroidUtilities.statusBarHeight != var2.getSystemWindowInsetTop()) {
         var6.requestLayout();
      }

      AndroidUtilities.statusBarHeight = var2.getSystemWindowInsetTop();
      this.lastInsets = var2;
      int var3 = var2.getSystemWindowInsetTop();
      boolean var4 = true;
      boolean var5;
      if (var3 <= 0 && this.getBackground() == null) {
         var5 = true;
      } else {
         var5 = false;
      }

      var6.setWillNotDraw(var5);
      if (VERSION.SDK_INT >= 28) {
         DisplayCutout var7 = var2.getDisplayCutout();
         if (var7 != null && var7.getBoundingRects().size() != 0) {
            var5 = var4;
         } else {
            var5 = false;
         }

         this.hasCutout = var5;
      }

      this.invalidate();
      return var2.consumeSystemWindowInsets();
   }

   public void moveDrawerByX(float var1) {
      this.setDrawerPosition(this.drawerPosition + var1);
   }

   protected void onDraw(Canvas var1) {
      if (VERSION.SDK_INT >= 21) {
         Object var2 = this.lastInsets;
         if (var2 != null) {
            WindowInsets var4 = (WindowInsets)var2;
            int var3 = var4.getSystemWindowInsetBottom();
            if (var3 > 0) {
               this.backgroundPaint.setColor(this.behindKeyboardColor);
               var1.drawRect(0.0F, (float)(this.getMeasuredHeight() - var3), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.backgroundPaint);
            }

            if (this.hasCutout) {
               this.backgroundPaint.setColor(-16777216);
               var3 = var4.getSystemWindowInsetLeft();
               if (var3 != 0) {
                  var1.drawRect(0.0F, 0.0F, (float)var3, (float)this.getMeasuredHeight(), this.backgroundPaint);
               }

               var3 = var4.getSystemWindowInsetRight();
               if (var3 != 0) {
                  var1.drawRect((float)var3, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.backgroundPaint);
               }
            }
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2;
      if (!this.parentActionBarLayout.checkTransitionAnimation() && !this.onTouchEvent(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.inLayout = true;
      var3 = this.getChildCount();

      for(var2 = 0; var2 < var3; ++var2) {
         View var6 = this.getChildAt(var2);
         if (var6.getVisibility() != 8) {
            LayoutParams var7 = (LayoutParams)var6.getLayoutParams();
            if (BuildVars.DEBUG_VERSION) {
               if (this.drawerLayout != var6) {
                  var6.layout(var7.leftMargin, var7.topMargin + this.getPaddingTop(), var7.leftMargin + var6.getMeasuredWidth(), var7.topMargin + var6.getMeasuredHeight() + this.getPaddingTop());
               } else {
                  var6.layout(-var6.getMeasuredWidth(), var7.topMargin + this.getPaddingTop(), 0, var7.topMargin + var6.getMeasuredHeight() + this.getPaddingTop());
               }
            } else {
               try {
                  if (this.drawerLayout != var6) {
                     var6.layout(var7.leftMargin, var7.topMargin + this.getPaddingTop(), var7.leftMargin + var6.getMeasuredWidth(), var7.topMargin + var6.getMeasuredHeight() + this.getPaddingTop());
                  } else {
                     var6.layout(-var6.getMeasuredWidth(), var7.topMargin + this.getPaddingTop(), 0, var7.topMargin + var6.getMeasuredHeight() + this.getPaddingTop());
                  }
               } catch (Exception var8) {
                  FileLog.e((Throwable)var8);
               }
            }
         }
      }

      this.inLayout = false;
   }

   @SuppressLint({"NewApi"})
   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      int var4 = MeasureSpec.getSize(var2);
      this.setMeasuredDimension(var3, var4);
      int var5 = var4;
      if (VERSION.SDK_INT < 21) {
         this.inLayout = true;
         if (var4 == AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight) {
            if (this.getLayoutParams() instanceof MarginLayoutParams) {
               this.setPadding(0, AndroidUtilities.statusBarHeight, 0, 0);
            }

            var5 = AndroidUtilities.displaySize.y;
         } else {
            var5 = var4;
            if (this.getLayoutParams() instanceof MarginLayoutParams) {
               this.setPadding(0, 0, 0, 0);
               var5 = var4;
            }
         }

         this.inLayout = false;
      }

      boolean var13;
      if (this.lastInsets != null && VERSION.SDK_INT >= 21) {
         var13 = true;
      } else {
         var13 = false;
      }

      int var6 = this.getChildCount();

      for(int var7 = 0; var7 < var6; ++var7) {
         View var8 = this.getChildAt(var7);
         if (var8.getVisibility() != 8) {
            LayoutParams var9 = (LayoutParams)var8.getLayoutParams();
            if (var13) {
               if (var8.getFitsSystemWindows()) {
                  this.dispatchChildInsets(var8, this.lastInsets, var9.gravity);
               } else if (var8.getTag() == null) {
                  Object var10 = this.lastInsets;
                  int var11 = var9.gravity;
                  boolean var12;
                  if (VERSION.SDK_INT >= 21) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  this.applyMarginInsets(var9, var10, var11, var12);
               }
            }

            if (this.drawerLayout != var8) {
               var8.measure(MeasureSpec.makeMeasureSpec(var3 - var9.leftMargin - var9.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec(var5 - var9.topMargin - var9.bottomMargin, 1073741824));
            } else {
               var8.setPadding(0, 0, 0, 0);
               var8.measure(FrameLayout.getChildMeasureSpec(var1, this.minDrawerMargin + var9.leftMargin + var9.rightMargin, var9.width), FrameLayout.getChildMeasureSpec(var2, var9.topMargin + var9.bottomMargin, var9.height));
            }
         }
      }

   }

   public boolean onRequestSendAccessibilityEvent(View var1, AccessibilityEvent var2) {
      return this.drawerOpened && var1 != this.drawerLayout ? false : super.onRequestSendAccessibilityEvent(var1, var2);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.parentActionBarLayout.checkTransitionAnimation()) {
         boolean var2 = this.drawerOpened;
         boolean var3 = true;
         boolean var4 = true;
         if (var2 && var1 != null && var1.getX() > this.drawerPosition && !this.startedTracking) {
            if (var1.getAction() == 1) {
               this.closeDrawer(false);
            }

            return true;
         } else {
            if (this.allowOpenDrawer && this.parentActionBarLayout.fragmentsStack.size() == 1) {
               VelocityTracker var8;
               if (var1 != null && (var1.getAction() == 0 || var1.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking) {
                  this.parentActionBarLayout.getHitRect(this.rect);
                  this.startedTrackingX = (int)var1.getX();
                  this.startedTrackingY = (int)var1.getY();
                  if (this.rect.contains(this.startedTrackingX, this.startedTrackingY)) {
                     this.startedTrackingPointerId = var1.getPointerId(0);
                     this.maybeStartTracking = true;
                     this.cancelCurrentAnimation();
                     var8 = this.velocityTracker;
                     if (var8 != null) {
                        var8.clear();
                     }
                  }
               } else {
                  float var5;
                  float var6;
                  if (var1 != null && var1.getAction() == 2 && var1.getPointerId(0) == this.startedTrackingPointerId) {
                     if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                     }

                     var5 = (float)((int)(var1.getX() - (float)this.startedTrackingX));
                     var6 = (float)Math.abs((int)var1.getY() - this.startedTrackingY);
                     this.velocityTracker.addMovement(var1);
                     if (!this.maybeStartTracking || this.startedTracking || (var5 <= 0.0F || var5 / 3.0F <= Math.abs(var6) || Math.abs(var5) < AndroidUtilities.getPixelsInCM(0.2F, true)) && (!this.drawerOpened || var5 >= 0.0F || Math.abs(var5) < Math.abs(var6) || Math.abs(var5) < AndroidUtilities.getPixelsInCM(0.4F, true))) {
                        if (this.startedTracking) {
                           if (!this.beginTrackingSent) {
                              if (((Activity)this.getContext()).getCurrentFocus() != null) {
                                 AndroidUtilities.hideKeyboard(((Activity)this.getContext()).getCurrentFocus());
                              }

                              this.beginTrackingSent = true;
                           }

                           this.moveDrawerByX(var5);
                           this.startedTrackingX = (int)var1.getX();
                        }
                     } else {
                        this.prepareForDrawerOpen(var1);
                        this.startedTrackingX = (int)var1.getX();
                        this.requestDisallowInterceptTouchEvent(true);
                     }
                  } else if (var1 == null || var1 != null && var1.getPointerId(0) == this.startedTrackingPointerId && (var1.getAction() == 3 || var1.getAction() == 1 || var1.getAction() == 6)) {
                     if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                     }

                     label183: {
                        this.velocityTracker.computeCurrentVelocity(1000);
                        if (!this.startedTracking) {
                           var5 = this.drawerPosition;
                           if (var5 == 0.0F || var5 == (float)this.drawerLayout.getMeasuredWidth()) {
                              break label183;
                           }
                        }

                        var6 = this.velocityTracker.getXVelocity();
                        var5 = this.velocityTracker.getYVelocity();
                        boolean var7;
                        if ((this.drawerPosition >= (float)this.drawerLayout.getMeasuredWidth() / 2.0F || var6 >= 3500.0F && Math.abs(var6) >= Math.abs(var5)) && (var6 >= 0.0F || Math.abs(var6) < 3500.0F)) {
                           var7 = false;
                        } else {
                           var7 = true;
                        }

                        if (!var7) {
                           if (this.drawerOpened || Math.abs(var6) < 3500.0F) {
                              var4 = false;
                           }

                           this.openDrawer(var4);
                        } else {
                           if (this.drawerOpened && Math.abs(var6) >= 3500.0F) {
                              var4 = var3;
                           } else {
                              var4 = false;
                           }

                           this.closeDrawer(var4);
                        }
                     }

                     this.startedTracking = false;
                     this.maybeStartTracking = false;
                     var8 = this.velocityTracker;
                     if (var8 != null) {
                        var8.recycle();
                        this.velocityTracker = null;
                     }
                  }
               }
            }

            return this.startedTracking;
         }
      } else {
         return false;
      }
   }

   public void openDrawer(boolean var1) {
      if (this.allowOpenDrawer) {
         if (AndroidUtilities.isTablet()) {
            ActionBarLayout var2 = this.parentActionBarLayout;
            if (var2 != null) {
               Activity var3 = var2.parentActivity;
               if (var3 != null) {
                  AndroidUtilities.hideKeyboard(var3.getCurrentFocus());
               }
            }
         }

         this.cancelCurrentAnimation();
         AnimatorSet var4 = new AnimatorSet();
         var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "drawerPosition", new float[]{(float)this.drawerLayout.getMeasuredWidth()})});
         var4.setInterpolator(new DecelerateInterpolator());
         if (var1) {
            var4.setDuration((long)Math.max((int)(200.0F / (float)this.drawerLayout.getMeasuredWidth() * ((float)this.drawerLayout.getMeasuredWidth() - this.drawerPosition)), 50));
         } else {
            var4.setDuration(300L);
         }

         var4.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               DrawerLayoutContainer.this.onDrawerAnimationEnd(true);
            }
         });
         var4.start();
         this.currentAnimation = var4;
      }
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      if (this.maybeStartTracking && !this.startedTracking) {
         this.onTouchEvent((MotionEvent)null);
      }

      super.requestDisallowInterceptTouchEvent(var1);
   }

   public void requestLayout() {
      if (!this.inLayout) {
         super.requestLayout();
      }

   }

   public void setAllowDrawContent(boolean var1) {
      if (this.allowDrawContent != var1) {
         this.allowDrawContent = var1;
         this.invalidate();
      }

   }

   public void setAllowOpenDrawer(boolean var1, boolean var2) {
      this.allowOpenDrawer = var1;
      if (!this.allowOpenDrawer && this.drawerPosition != 0.0F) {
         if (!var2) {
            this.setDrawerPosition(0.0F);
            this.onDrawerAnimationEnd(false);
         } else {
            this.closeDrawer(true);
         }
      }

   }

   public void setBehindKeyboardColor(int var1) {
      this.behindKeyboardColor = var1;
      this.invalidate();
   }

   public void setDrawerLayout(ViewGroup var1) {
      this.drawerLayout = var1;
      this.addView(this.drawerLayout);
      if (VERSION.SDK_INT >= 21) {
         this.drawerLayout.setFitsSystemWindows(true);
      }

   }

   @Keep
   public void setDrawerPosition(float var1) {
      this.drawerPosition = var1;
      if (this.drawerPosition > (float)this.drawerLayout.getMeasuredWidth()) {
         this.drawerPosition = (float)this.drawerLayout.getMeasuredWidth();
      } else if (this.drawerPosition < 0.0F) {
         this.drawerPosition = 0.0F;
      }

      this.drawerLayout.setTranslationX(this.drawerPosition);
      byte var2;
      if (this.drawerPosition > 0.0F) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      if (this.drawerLayout.getVisibility() != var2) {
         this.drawerLayout.setVisibility(var2);
      }

      this.setScrimOpacity(this.drawerPosition / (float)this.drawerLayout.getMeasuredWidth());
   }

   public void setParentActionBarLayout(ActionBarLayout var1) {
      this.parentActionBarLayout = var1;
   }
}
