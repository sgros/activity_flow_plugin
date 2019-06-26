package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.StatsController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;

public class DataUsageActivity extends BaseFragment {
   private static final Interpolator interpolator;
   private boolean animatingForward;
   private boolean backAnimation;
   private Paint backgroundPaint = new Paint();
   private int maximumVelocity;
   private DataUsageActivity.ListAdapter mobileAdapter;
   private DataUsageActivity.ListAdapter roamingAdapter;
   private ScrollSlidingTextTabStrip scrollSlidingTextTabStrip;
   private AnimatorSet tabsAnimation;
   private boolean tabsAnimationInProgress;
   private DataUsageActivity.ViewPage[] viewPages = new DataUsageActivity.ViewPage[2];
   private DataUsageActivity.ListAdapter wifiAdapter;

   static {
      interpolator = _$$Lambda$DataUsageActivity$wsORmtBp3T6D_3i_RtGAVFVpGzs.INSTANCE;
   }

   // $FF: synthetic method
   static ActionBar access$1000(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1100(DataUsageActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$1200(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1300(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1400(DataUsageActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$1900(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2000(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static boolean access$202(DataUsageActivity var0, boolean var1) {
      var0.swipeBackEnabled = var1;
      return var1;
   }

   // $FF: synthetic method
   static ActionBarLayout access$2100(DataUsageActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static boolean access$2402(DataUsageActivity var0, boolean var1) {
      var0.swipeBackEnabled = var1;
      return var1;
   }

   // $FF: synthetic method
   static ActionBar access$2700(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$2800(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$3000(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$3100(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$3300(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3500(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3700(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3800(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3900(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$4000(DataUsageActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$600(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$700(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$800(DataUsageActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static float lambda$static$0(float var0) {
      --var0;
      return var0 * var0 * var0 * var0 * var0 + 1.0F;
   }

   private void setScrollY(float var1) {
      super.actionBar.setTranslationY(var1);
      int var2 = 0;

      while(true) {
         DataUsageActivity.ViewPage[] var3 = this.viewPages;
         if (var2 >= var3.length) {
            super.fragmentView.invalidate();
            return;
         }

         var3[var2].listView.setPinnedSectionOffsetY((int)var1);
         ++var2;
      }
   }

   private void switchToCurrentSelectedMode(boolean var1) {
      int var2 = 0;

      while(true) {
         DataUsageActivity.ViewPage[] var3 = this.viewPages;
         if (var2 >= var3.length) {
            RecyclerView.Adapter var4 = var3[var1].listView.getAdapter();
            this.viewPages[var1].listView.setPinnedHeaderShadowDrawable((Drawable)null);
            if (this.viewPages[var1].selectedType == 0) {
               if (var4 != this.mobileAdapter) {
                  this.viewPages[var1].listView.setAdapter(this.mobileAdapter);
               }
            } else if (this.viewPages[var1].selectedType == 1) {
               if (var4 != this.wifiAdapter) {
                  this.viewPages[var1].listView.setAdapter(this.wifiAdapter);
               }
            } else if (this.viewPages[var1].selectedType == 2 && var4 != this.roamingAdapter) {
               this.viewPages[var1].listView.setAdapter(this.roamingAdapter);
            }

            this.viewPages[var1].listView.setVisibility(0);
            if (super.actionBar.getTranslationY() != 0.0F) {
               this.viewPages[var1].layoutManager.scrollToPositionWithOffset(0, (int)super.actionBar.getTranslationY());
            }

            return;
         }

         var3[var2].listView.stopScroll();
         ++var2;
      }
   }

   private void updateTabs() {
      ScrollSlidingTextTabStrip var1 = this.scrollSlidingTextTabStrip;
      if (var1 != null) {
         var1.addTextTab(0, LocaleController.getString("NetworkUsageMobile", 2131559890));
         this.scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString("NetworkUsageWiFi", 2131559893));
         this.scrollSlidingTextTabStrip.addTextTab(2, LocaleController.getString("NetworkUsageRoaming", 2131559891));
         this.scrollSlidingTextTabStrip.setVisibility(0);
         super.actionBar.setExtraHeight(AndroidUtilities.dp(44.0F));
         int var2 = this.scrollSlidingTextTabStrip.getCurrentTabId();
         if (var2 >= 0) {
            this.viewPages[0].selectedType = var2;
         }

         this.scrollSlidingTextTabStrip.finishAddingTabs();
      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setTitle(LocaleController.getString("NetworkUsage", 2131559889));
      boolean var2 = AndroidUtilities.isTablet();
      boolean var3 = false;
      if (var2) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setExtraHeight(AndroidUtilities.dp(44.0F));
      super.actionBar.setAllowOverlayTitle(false);
      super.actionBar.setAddToContainer(false);
      super.actionBar.setClipContent(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               DataUsageActivity.this.finishFragment();
            }

         }
      });
      super.hasOwnBackground = true;
      this.mobileAdapter = new DataUsageActivity.ListAdapter(var1, 0);
      this.wifiAdapter = new DataUsageActivity.ListAdapter(var1, 1);
      this.roamingAdapter = new DataUsageActivity.ListAdapter(var1, 2);
      this.scrollSlidingTextTabStrip = new ScrollSlidingTextTabStrip(var1);
      this.scrollSlidingTextTabStrip.setUseSameWidth(true);
      super.actionBar.addView(this.scrollSlidingTextTabStrip, LayoutHelper.createFrame(-1, 44, 83));
      this.scrollSlidingTextTabStrip.setDelegate(new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() {
         public void onPageScrolled(float var1) {
            if (var1 != 1.0F || DataUsageActivity.this.viewPages[1].getVisibility() == 0) {
               if (DataUsageActivity.this.animatingForward) {
                  DataUsageActivity.this.viewPages[0].setTranslationX(-var1 * (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth());
                  DataUsageActivity.this.viewPages[1].setTranslationX((float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() - (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() * var1);
               } else {
                  DataUsageActivity.this.viewPages[0].setTranslationX((float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() * var1);
                  DataUsageActivity.this.viewPages[1].setTranslationX((float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() * var1 - (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth());
               }

               if (var1 == 1.0F) {
                  DataUsageActivity.ViewPage var2 = DataUsageActivity.this.viewPages[0];
                  DataUsageActivity.this.viewPages[0] = DataUsageActivity.this.viewPages[1];
                  DataUsageActivity.this.viewPages[1] = var2;
                  DataUsageActivity.this.viewPages[1].setVisibility(8);
               }

            }
         }

         public void onPageSelected(int var1, boolean var2) {
            if (DataUsageActivity.this.viewPages[0].selectedType != var1) {
               DataUsageActivity var3 = DataUsageActivity.this;
               boolean var4;
               if (var1 == var3.scrollSlidingTextTabStrip.getFirstTabId()) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               DataUsageActivity.access$202(var3, var4);
               DataUsageActivity.this.viewPages[1].selectedType = var1;
               DataUsageActivity.this.viewPages[1].setVisibility(0);
               DataUsageActivity.this.switchToCurrentSelectedMode(true);
               DataUsageActivity.this.animatingForward = var2;
            }
         }
      });
      this.maximumVelocity = ViewConfiguration.get(var1).getScaledMaximumFlingVelocity();
      FrameLayout var4 = new FrameLayout(var1) {
         private boolean globalIgnoreLayout;
         private boolean maybeStartTracking;
         private boolean startedTracking;
         private int startedTrackingPointerId;
         private int startedTrackingX;
         private int startedTrackingY;
         private VelocityTracker velocityTracker;

         private boolean prepareForMoving(MotionEvent var1, boolean var2) {
            int var3 = DataUsageActivity.this.scrollSlidingTextTabStrip.getNextPageId(var2);
            if (var3 < 0) {
               return false;
            } else {
               this.getParent().requestDisallowInterceptTouchEvent(true);
               this.maybeStartTracking = false;
               this.startedTracking = true;
               this.startedTrackingX = (int)var1.getX();
               DataUsageActivity.access$600(DataUsageActivity.this).setEnabled(false);
               DataUsageActivity.this.scrollSlidingTextTabStrip.setEnabled(false);
               DataUsageActivity.this.viewPages[1].selectedType = var3;
               DataUsageActivity.this.viewPages[1].setVisibility(0);
               DataUsageActivity.this.animatingForward = var2;
               DataUsageActivity.this.switchToCurrentSelectedMode(true);
               if (var2) {
                  DataUsageActivity.this.viewPages[1].setTranslationX((float)DataUsageActivity.this.viewPages[0].getMeasuredWidth());
               } else {
                  DataUsageActivity.this.viewPages[1].setTranslationX((float)(-DataUsageActivity.this.viewPages[0].getMeasuredWidth()));
               }

               return true;
            }
         }

         public boolean checkTabsAnimationInProgress() {
            if (!DataUsageActivity.this.tabsAnimationInProgress) {
               return false;
            } else {
               boolean var6;
               label35: {
                  boolean var1 = DataUsageActivity.this.backAnimation;
                  byte var2 = -1;
                  boolean var3 = true;
                  DataUsageActivity.ViewPage var4;
                  int var5;
                  if (var1) {
                     if (Math.abs(DataUsageActivity.this.viewPages[0].getTranslationX()) < 1.0F) {
                        DataUsageActivity.this.viewPages[0].setTranslationX(0.0F);
                        var4 = DataUsageActivity.this.viewPages[1];
                        var5 = DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                        if (DataUsageActivity.this.animatingForward) {
                           var2 = 1;
                        }

                        var4.setTranslationX((float)(var5 * var2));
                        var6 = var3;
                        break label35;
                     }
                  } else if (Math.abs(DataUsageActivity.this.viewPages[1].getTranslationX()) < 1.0F) {
                     var4 = DataUsageActivity.this.viewPages[0];
                     var5 = DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                     if (!DataUsageActivity.this.animatingForward) {
                        var2 = 1;
                     }

                     var4.setTranslationX((float)(var5 * var2));
                     DataUsageActivity.this.viewPages[1].setTranslationX(0.0F);
                     var6 = var3;
                     break label35;
                  }

                  var6 = false;
               }

               if (var6) {
                  if (DataUsageActivity.this.tabsAnimation != null) {
                     DataUsageActivity.this.tabsAnimation.cancel();
                     DataUsageActivity.this.tabsAnimation = null;
                  }

                  DataUsageActivity.this.tabsAnimationInProgress = false;
               }

               return DataUsageActivity.this.tabsAnimationInProgress;
            }
         }

         protected void dispatchDraw(Canvas var1) {
            super.dispatchDraw(var1);
            if (DataUsageActivity.access$1100(DataUsageActivity.this) != null) {
               DataUsageActivity.access$1400(DataUsageActivity.this).drawHeaderShadow(var1, DataUsageActivity.access$1200(DataUsageActivity.this).getMeasuredHeight() + (int)DataUsageActivity.access$1300(DataUsageActivity.this).getTranslationY());
            }

         }

         public void forceHasOverlappingRendering(boolean var1) {
            super.forceHasOverlappingRendering(var1);
         }

         protected void onDraw(Canvas var1) {
            DataUsageActivity.this.backgroundPaint.setColor(Theme.getColor("windowBackgroundGray"));
            var1.drawRect(0.0F, (float)DataUsageActivity.access$1900(DataUsageActivity.this).getMeasuredHeight() + DataUsageActivity.access$2000(DataUsageActivity.this).getTranslationY(), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), DataUsageActivity.this.backgroundPaint);
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!this.checkTabsAnimationInProgress() && !DataUsageActivity.this.scrollSlidingTextTabStrip.isAnimatingIndicator() && !this.onTouchEvent(var1)) {
               var2 = false;
            } else {
               var2 = true;
            }

            return var2;
         }

         protected void onMeasure(int var1, int var2) {
            this.setMeasuredDimension(MeasureSpec.getSize(var1), MeasureSpec.getSize(var2));
            this.measureChildWithMargins(DataUsageActivity.access$700(DataUsageActivity.this), var1, 0, var2, 0);
            int var3 = DataUsageActivity.access$800(DataUsageActivity.this).getMeasuredHeight();
            this.globalIgnoreLayout = true;
            byte var4 = 0;

            int var5;
            for(var5 = 0; var5 < DataUsageActivity.this.viewPages.length; ++var5) {
               if (DataUsageActivity.this.viewPages[var5] != null && DataUsageActivity.this.viewPages[var5].listView != null) {
                  DataUsageActivity.this.viewPages[var5].listView.setPadding(0, var3, 0, AndroidUtilities.dp(4.0F));
               }
            }

            this.globalIgnoreLayout = false;
            var3 = this.getChildCount();

            for(var5 = var4; var5 < var3; ++var5) {
               View var6 = this.getChildAt(var5);
               if (var6 != null && var6.getVisibility() != 8 && var6 != DataUsageActivity.access$1000(DataUsageActivity.this)) {
                  this.measureChildWithMargins(var6, var1, 0, var2, 0);
               }
            }

         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (!DataUsageActivity.access$2100(DataUsageActivity.this).checkTransitionAnimation() && !this.checkTabsAnimationInProgress()) {
               boolean var2 = true;
               VelocityTracker var11;
               if (var1 != null && var1.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                  this.startedTrackingPointerId = var1.getPointerId(0);
                  this.maybeStartTracking = true;
                  this.startedTrackingX = (int)var1.getX();
                  this.startedTrackingY = (int)var1.getY();
                  var11 = this.velocityTracker;
                  if (var11 != null) {
                     var11.clear();
                  }
               } else {
                  int var3;
                  int var4;
                  boolean var5;
                  float var6;
                  if (var1 != null && var1.getAction() == 2 && var1.getPointerId(0) == this.startedTrackingPointerId) {
                     if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                     }

                     var3 = (int)(var1.getX() - (float)this.startedTrackingX);
                     var4 = Math.abs((int)var1.getY() - this.startedTrackingY);
                     this.velocityTracker.addMovement(var1);
                     if (this.startedTracking && (DataUsageActivity.this.animatingForward && var3 > 0 || !DataUsageActivity.this.animatingForward && var3 < 0)) {
                        if (var3 < 0) {
                           var5 = true;
                        } else {
                           var5 = false;
                        }

                        if (!this.prepareForMoving(var1, var5)) {
                           this.maybeStartTracking = true;
                           this.startedTracking = false;
                        }
                     }

                     if (this.maybeStartTracking && !this.startedTracking) {
                        var6 = AndroidUtilities.getPixelsInCM(0.3F, true);
                        if ((float)Math.abs(var3) >= var6 && Math.abs(var3) / 3 > var4) {
                           if (var3 < 0) {
                              var5 = var2;
                           } else {
                              var5 = false;
                           }

                           this.prepareForMoving(var1, var5);
                        }
                     } else if (this.startedTracking) {
                        if (DataUsageActivity.this.animatingForward) {
                           DataUsageActivity.this.viewPages[0].setTranslationX((float)var3);
                           DataUsageActivity.this.viewPages[1].setTranslationX((float)(DataUsageActivity.this.viewPages[0].getMeasuredWidth() + var3));
                        } else {
                           DataUsageActivity.this.viewPages[0].setTranslationX((float)var3);
                           DataUsageActivity.this.viewPages[1].setTranslationX((float)(var3 - DataUsageActivity.this.viewPages[0].getMeasuredWidth()));
                        }

                        var6 = (float)Math.abs(var3) / (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                        DataUsageActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DataUsageActivity.this.viewPages[1].selectedType, var6);
                     }
                  } else if (var1 != null && var1.getPointerId(0) == this.startedTrackingPointerId && (var1.getAction() == 3 || var1.getAction() == 1 || var1.getAction() == 6)) {
                     if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                     }

                     this.velocityTracker.computeCurrentVelocity(1000, (float)DataUsageActivity.this.maximumVelocity);
                     float var7;
                     if (!this.startedTracking) {
                        var6 = this.velocityTracker.getXVelocity();
                        var7 = this.velocityTracker.getYVelocity();
                        if (Math.abs(var6) >= 3000.0F && Math.abs(var6) > Math.abs(var7)) {
                           if (var6 < 0.0F) {
                              var5 = true;
                           } else {
                              var5 = false;
                           }

                           this.prepareForMoving(var1, var5);
                        }
                     }

                     if (!this.startedTracking) {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                        DataUsageActivity.access$2800(DataUsageActivity.this).setEnabled(true);
                        DataUsageActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                     } else {
                        float var8 = DataUsageActivity.this.viewPages[0].getX();
                        DataUsageActivity.this.tabsAnimation = new AnimatorSet();
                        var7 = this.velocityTracker.getXVelocity();
                        var6 = this.velocityTracker.getYVelocity();
                        DataUsageActivity var10 = DataUsageActivity.this;
                        if (Math.abs(var8) >= (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() / 3.0F || Math.abs(var7) >= 3500.0F && Math.abs(var7) >= Math.abs(var6)) {
                           var5 = false;
                        } else {
                           var5 = true;
                        }

                        var10.backAnimation = var5;
                        if (DataUsageActivity.this.backAnimation) {
                           var6 = Math.abs(var8);
                           if (DataUsageActivity.this.animatingForward) {
                              DataUsageActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[]{(float)DataUsageActivity.this.viewPages[1].getMeasuredWidth()})});
                           } else {
                              DataUsageActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[]{(float)(-DataUsageActivity.this.viewPages[1].getMeasuredWidth())})});
                           }
                        } else {
                           var6 = (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth() - Math.abs(var8);
                           if (DataUsageActivity.this.animatingForward) {
                              DataUsageActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[]{(float)(-DataUsageActivity.this.viewPages[0].getMeasuredWidth())}), ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[]{0.0F})});
                           } else {
                              DataUsageActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[0], View.TRANSLATION_X, new float[]{(float)DataUsageActivity.this.viewPages[0].getMeasuredWidth()}), ObjectAnimator.ofFloat(DataUsageActivity.this.viewPages[1], View.TRANSLATION_X, new float[]{0.0F})});
                           }
                        }

                        DataUsageActivity.this.tabsAnimation.setInterpolator(DataUsageActivity.interpolator);
                        var3 = this.getMeasuredWidth();
                        var4 = var3 / 2;
                        float var9 = Math.min(1.0F, var6 * 1.0F / (float)var3);
                        var8 = (float)var4;
                        var9 = AndroidUtilities.distanceInfluenceForSnapDuration(var9);
                        var7 = Math.abs(var7);
                        if (var7 > 0.0F) {
                           var3 = Math.round(Math.abs((var8 + var9 * var8) / var7) * 1000.0F) * 4;
                        } else {
                           var3 = (int)((var6 / (float)this.getMeasuredWidth() + 1.0F) * 100.0F);
                        }

                        var3 = Math.max(150, Math.min(var3, 600));
                        DataUsageActivity.this.tabsAnimation.setDuration((long)var3);
                        DataUsageActivity.this.tabsAnimation.addListener(new AnimatorListenerAdapter() {
                           public void onAnimationEnd(Animator var1) {
                              DataUsageActivity.this.tabsAnimation = null;
                              if (DataUsageActivity.this.backAnimation) {
                                 DataUsageActivity.this.viewPages[1].setVisibility(8);
                              } else {
                                 DataUsageActivity.ViewPage var3 = DataUsageActivity.this.viewPages[0];
                                 DataUsageActivity.this.viewPages[0] = DataUsageActivity.this.viewPages[1];
                                 DataUsageActivity.this.viewPages[1] = var3;
                                 DataUsageActivity.this.viewPages[1].setVisibility(8);
                                 DataUsageActivity var4 = DataUsageActivity.this;
                                 boolean var2;
                                 if (var4.viewPages[0].selectedType == DataUsageActivity.this.scrollSlidingTextTabStrip.getFirstTabId()) {
                                    var2 = true;
                                 } else {
                                    var2 = false;
                                 }

                                 DataUsageActivity.access$2402(var4, var2);
                                 DataUsageActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DataUsageActivity.this.viewPages[0].selectedType, 1.0F);
                              }

                              DataUsageActivity.this.tabsAnimationInProgress = false;
                              maybeStartTracking = false;
                              startedTracking = false;
                              DataUsageActivity.access$2700(DataUsageActivity.this).setEnabled(true);
                              DataUsageActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                           }
                        });
                        DataUsageActivity.this.tabsAnimation.start();
                        DataUsageActivity.this.tabsAnimationInProgress = true;
                     }

                     var11 = this.velocityTracker;
                     if (var11 != null) {
                        var11.recycle();
                        this.velocityTracker = null;
                     }
                  }
               }

               return this.startedTracking;
            } else {
               return false;
            }
         }

         public void requestLayout() {
            if (!this.globalIgnoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.fragmentView = var4;
      var4.setWillNotDraw(false);
      int var5 = 0;
      int var6 = -1;
      int var7 = 0;

      while(true) {
         DataUsageActivity.ViewPage[] var8 = this.viewPages;
         if (var5 >= var8.length) {
            var4.addView(super.actionBar, LayoutHelper.createFrame(-1, -2.0F));
            this.updateTabs();
            this.switchToCurrentSelectedMode(false);
            if (this.scrollSlidingTextTabStrip.getCurrentTabId() == this.scrollSlidingTextTabStrip.getFirstTabId()) {
               var3 = true;
            }

            super.swipeBackEnabled = var3;
            return super.fragmentView;
         }

         int var9 = var6;
         int var10 = var7;
         if (var5 == 0) {
            var9 = var6;
            var10 = var7;
            if (var8[var5] != null) {
               var9 = var6;
               var10 = var7;
               if (var8[var5].layoutManager != null) {
                  label53: {
                     var9 = this.viewPages[var5].layoutManager.findFirstVisibleItemPosition();
                     if (var9 != this.viewPages[var5].layoutManager.getItemCount() - 1) {
                        RecyclerListView.Holder var13 = (RecyclerListView.Holder)this.viewPages[var5].listView.findViewHolderForAdapterPosition(var9);
                        if (var13 != null) {
                           var10 = var13.itemView.getTop();
                           break label53;
                        }
                     }

                     var9 = -1;
                     var10 = var7;
                  }
               }
            }
         }

         DataUsageActivity.ViewPage var11 = new DataUsageActivity.ViewPage(var1) {
            public void setTranslationX(float var1) {
               super.setTranslationX(var1);
               if (DataUsageActivity.this.tabsAnimationInProgress && DataUsageActivity.this.viewPages[0] == this) {
                  var1 = Math.abs(DataUsageActivity.this.viewPages[0].getTranslationX()) / (float)DataUsageActivity.this.viewPages[0].getMeasuredWidth();
                  DataUsageActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DataUsageActivity.this.viewPages[1].selectedType, var1);
               }

            }
         };
         var4.addView(var11, LayoutHelper.createFrame(-1, -1.0F));
         var8 = this.viewPages;
         var8[var5] = var11;
         var11 = var8[var5];
         LinearLayoutManager var14 = new LinearLayoutManager(var1, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
               return false;
            }
         };
         var11.layoutManager = var14;
         RecyclerListView var15 = new RecyclerListView(var1);
         this.viewPages[var5].listView = var15;
         this.viewPages[var5].listView.setItemAnimator((RecyclerView.ItemAnimator)null);
         this.viewPages[var5].listView.setClipToPadding(false);
         this.viewPages[var5].listView.setSectionsType(2);
         this.viewPages[var5].listView.setLayoutManager(var14);
         DataUsageActivity.ViewPage[] var12 = this.viewPages;
         var12[var5].addView(var12[var5].listView, LayoutHelper.createFrame(-1, -1.0F));
         this.viewPages[var5].listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$DataUsageActivity$NwaR7lgfeGPKETAnvYA4hx0PZOY(this, var15)));
         this.viewPages[var5].listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView var1, int var2) {
               if (var2 != 1) {
                  int var3 = (int)(-DataUsageActivity.access$3000(DataUsageActivity.this).getTranslationY());
                  var2 = ActionBar.getCurrentActionBarHeight();
                  if (var3 != 0 && var3 != var2) {
                     if (var3 < var2 / 2) {
                        DataUsageActivity.this.viewPages[0].listView.smoothScrollBy(0, -var3);
                     } else {
                        DataUsageActivity.this.viewPages[0].listView.smoothScrollBy(0, var2 - var3);
                     }
                  }
               }

            }

            public void onScrolled(RecyclerView var1, int var2, int var3) {
               if (var1 == DataUsageActivity.this.viewPages[0].listView) {
                  float var4 = DataUsageActivity.access$3100(DataUsageActivity.this).getTranslationY();
                  float var5 = var4 - (float)var3;
                  float var6;
                  if (var5 < (float)(-ActionBar.getCurrentActionBarHeight())) {
                     var6 = (float)(-ActionBar.getCurrentActionBarHeight());
                  } else {
                     var6 = var5;
                     if (var5 > 0.0F) {
                        var6 = 0.0F;
                     }
                  }

                  if (var6 != var4) {
                     DataUsageActivity.this.setScrollY(var6);
                  }
               }

            }
         });
         if (var5 == 0 && var9 != -1) {
            var14.scrollToPositionWithOffset(var9, var10);
         }

         if (var5 != 0) {
            this.viewPages[var5].setVisibility(8);
         }

         ++var5;
         var6 = var9;
         var7 = var10;
      }
   }

   public ThemeDescription[] getThemeDescriptions() {
      ArrayList var1 = new ArrayList();
      var1.add(new ThemeDescription(super.fragmentView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var1.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabActiveText"));
      var1.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabUnactiveText"));
      var1.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{TextView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabLine"));
      var1.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{this.scrollSlidingTextTabStrip.getSelectorDrawable()}, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabSelector"));
      int var2 = 0;

      while(true) {
         DataUsageActivity.ViewPage[] var3 = this.viewPages;
         if (var2 >= var3.length) {
            return (ThemeDescription[])var1.toArray(new ThemeDescription[0]);
         }

         var1.add(new ThemeDescription(var3[var2].listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
         RecyclerListView var5 = this.viewPages[var2].listView;
         Paint var4 = Theme.dividerPaint;
         var1.add(new ThemeDescription(var5, 0, new Class[]{View.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"));
         var1.add(new ThemeDescription(this.viewPages[var2].listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText2"));
         ++var2;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$2$DataUsageActivity(RecyclerListView var1, View var2, int var3) {
      if (this.getParentActivity() != null) {
         DataUsageActivity.ListAdapter var4 = (DataUsageActivity.ListAdapter)var1.getAdapter();
         if (var3 == var4.resetRow) {
            AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
            var7.setTitle(LocaleController.getString("ResetStatisticsAlertTitle", 2131560606));
            var7.setMessage(LocaleController.getString("ResetStatisticsAlert", 2131560605));
            var7.setPositiveButton(LocaleController.getString("Reset", 2131560583), new _$$Lambda$DataUsageActivity$ZhhoKKmFdkBYmiX0nsi78u9hycQ(this, var4));
            var7.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            AlertDialog var5 = var7.create();
            this.showDialog(var5);
            TextView var6 = (TextView)var5.getButton(-1);
            if (var6 != null) {
               var6.setTextColor(Theme.getColor("dialogTextRed2"));
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$1$DataUsageActivity(DataUsageActivity.ListAdapter var1, DialogInterface var2, int var3) {
      StatsController.getInstance(super.currentAccount).resetStats(var1.currentType);
      var1.notifyDataSetChanged();
   }

   public void onResume() {
      super.onResume();
      DataUsageActivity.ListAdapter var1 = this.mobileAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      var1 = this.wifiAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      var1 = this.roamingAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private int audiosBytesReceivedRow;
      private int audiosBytesSentRow;
      private int audiosReceivedRow;
      private int audiosSection2Row;
      private int audiosSectionRow;
      private int audiosSentRow;
      private int callsBytesReceivedRow;
      private int callsBytesSentRow;
      private int callsReceivedRow;
      private int callsSection2Row;
      private int callsSectionRow;
      private int callsSentRow;
      private int callsTotalTimeRow;
      private int currentType;
      private int filesBytesReceivedRow;
      private int filesBytesSentRow;
      private int filesReceivedRow;
      private int filesSection2Row;
      private int filesSectionRow;
      private int filesSentRow;
      private Context mContext;
      private int messagesBytesReceivedRow;
      private int messagesBytesSentRow;
      private int messagesReceivedRow;
      private int messagesSection2Row;
      private int messagesSectionRow;
      private int messagesSentRow;
      private int photosBytesReceivedRow;
      private int photosBytesSentRow;
      private int photosReceivedRow;
      private int photosSection2Row;
      private int photosSectionRow;
      private int photosSentRow;
      private int resetRow;
      private int resetSection2Row;
      private int rowCount;
      private int totalBytesReceivedRow;
      private int totalBytesSentRow;
      private int totalSection2Row;
      private int totalSectionRow;
      private int videosBytesReceivedRow;
      private int videosBytesSentRow;
      private int videosReceivedRow;
      private int videosSection2Row;
      private int videosSectionRow;
      private int videosSentRow;

      public ListAdapter(Context var2, int var3) {
         this.mContext = var2;
         this.currentType = var3;
         this.rowCount = 0;
         var3 = this.rowCount++;
         this.photosSectionRow = var3;
         var3 = this.rowCount++;
         this.photosSentRow = var3;
         var3 = this.rowCount++;
         this.photosReceivedRow = var3;
         var3 = this.rowCount++;
         this.photosBytesSentRow = var3;
         var3 = this.rowCount++;
         this.photosBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.photosSection2Row = var3;
         var3 = this.rowCount++;
         this.videosSectionRow = var3;
         var3 = this.rowCount++;
         this.videosSentRow = var3;
         var3 = this.rowCount++;
         this.videosReceivedRow = var3;
         var3 = this.rowCount++;
         this.videosBytesSentRow = var3;
         var3 = this.rowCount++;
         this.videosBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.videosSection2Row = var3;
         var3 = this.rowCount++;
         this.audiosSectionRow = var3;
         var3 = this.rowCount++;
         this.audiosSentRow = var3;
         var3 = this.rowCount++;
         this.audiosReceivedRow = var3;
         var3 = this.rowCount++;
         this.audiosBytesSentRow = var3;
         var3 = this.rowCount++;
         this.audiosBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.audiosSection2Row = var3;
         var3 = this.rowCount++;
         this.filesSectionRow = var3;
         var3 = this.rowCount++;
         this.filesSentRow = var3;
         var3 = this.rowCount++;
         this.filesReceivedRow = var3;
         var3 = this.rowCount++;
         this.filesBytesSentRow = var3;
         var3 = this.rowCount++;
         this.filesBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.filesSection2Row = var3;
         var3 = this.rowCount++;
         this.callsSectionRow = var3;
         var3 = this.rowCount++;
         this.callsSentRow = var3;
         var3 = this.rowCount++;
         this.callsReceivedRow = var3;
         var3 = this.rowCount++;
         this.callsBytesSentRow = var3;
         var3 = this.rowCount++;
         this.callsBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.callsTotalTimeRow = var3;
         var3 = this.rowCount++;
         this.callsSection2Row = var3;
         var3 = this.rowCount++;
         this.messagesSectionRow = var3;
         this.messagesSentRow = -1;
         this.messagesReceivedRow = -1;
         var3 = this.rowCount++;
         this.messagesBytesSentRow = var3;
         var3 = this.rowCount++;
         this.messagesBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.messagesSection2Row = var3;
         var3 = this.rowCount++;
         this.totalSectionRow = var3;
         var3 = this.rowCount++;
         this.totalBytesSentRow = var3;
         var3 = this.rowCount++;
         this.totalBytesReceivedRow = var3;
         var3 = this.rowCount++;
         this.totalSection2Row = var3;
         var3 = this.rowCount++;
         this.resetRow = var3;
         var3 = this.rowCount++;
         this.resetSection2Row = var3;
      }

      public int getItemCount() {
         return this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == this.resetSection2Row) {
            return 3;
         } else if (var1 != this.callsSection2Row && var1 != this.filesSection2Row && var1 != this.audiosSection2Row && var1 != this.videosSection2Row && var1 != this.photosSection2Row && var1 != this.messagesSection2Row && var1 != this.totalSection2Row) {
            return var1 != this.totalSectionRow && var1 != this.callsSectionRow && var1 != this.filesSectionRow && var1 != this.audiosSectionRow && var1 != this.videosSectionRow && var1 != this.photosSectionRow && var1 != this.messagesSectionRow ? 1 : 2;
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getAdapterPosition() == this.resetRow) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            boolean var4 = false;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     TextInfoPrivacyCell var8 = (TextInfoPrivacyCell)var1.itemView;
                     var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                     var8.setText(LocaleController.formatString("NetworkUsageSince", 2131559892, LocaleController.getInstance().formatterStats.format(StatsController.getInstance(DataUsageActivity.access$4000(DataUsageActivity.this)).getResetStatsDate(this.currentType))));
                  }
               } else {
                  HeaderCell var9 = (HeaderCell)var1.itemView;
                  if (var2 == this.totalSectionRow) {
                     var9.setText(LocaleController.getString("TotalDataUsage", 2131560915));
                  } else if (var2 == this.callsSectionRow) {
                     var9.setText(LocaleController.getString("CallsDataUsage", 2131558889));
                  } else if (var2 == this.filesSectionRow) {
                     var9.setText(LocaleController.getString("FilesDataUsage", 2131559483));
                  } else if (var2 == this.audiosSectionRow) {
                     var9.setText(LocaleController.getString("LocalAudioCache", 2131559770));
                  } else if (var2 == this.videosSectionRow) {
                     var9.setText(LocaleController.getString("LocalVideoCache", 2131559779));
                  } else if (var2 == this.photosSectionRow) {
                     var9.setText(LocaleController.getString("LocalPhotoCache", 2131559778));
                  } else if (var2 == this.messagesSectionRow) {
                     var9.setText(LocaleController.getString("MessagesDataUsage", 2131559855));
                  }
               }
            } else {
               TextSettingsCell var5 = (TextSettingsCell)var1.itemView;
               if (var2 == this.resetRow) {
                  var5.setTag("windowBackgroundWhiteRedText2");
                  var5.setText(LocaleController.getString("ResetStatistics", 2131560604), false);
                  var5.setTextColor(Theme.getColor("windowBackgroundWhiteRedText2"));
               } else {
                  var5.setTag("windowBackgroundWhiteBlackText");
                  var5.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  byte var11;
                  if (var2 != this.callsSentRow && var2 != this.callsReceivedRow && var2 != this.callsBytesSentRow && var2 != this.callsBytesReceivedRow) {
                     if (var2 != this.messagesSentRow && var2 != this.messagesReceivedRow && var2 != this.messagesBytesSentRow && var2 != this.messagesBytesReceivedRow) {
                        if (var2 != this.photosSentRow && var2 != this.photosReceivedRow && var2 != this.photosBytesSentRow && var2 != this.photosBytesReceivedRow) {
                           if (var2 != this.audiosSentRow && var2 != this.audiosReceivedRow && var2 != this.audiosBytesSentRow && var2 != this.audiosBytesReceivedRow) {
                              if (var2 != this.videosSentRow && var2 != this.videosReceivedRow && var2 != this.videosBytesSentRow && var2 != this.videosBytesReceivedRow) {
                                 if (var2 != this.filesSentRow && var2 != this.filesReceivedRow && var2 != this.filesBytesSentRow && var2 != this.filesBytesReceivedRow) {
                                    var11 = 6;
                                 } else {
                                    var11 = 5;
                                 }
                              } else {
                                 var11 = 2;
                              }
                           } else {
                              var11 = 3;
                           }
                        } else {
                           var11 = 4;
                        }
                     } else {
                        var11 = 1;
                     }
                  } else {
                     var11 = 0;
                  }

                  if (var2 == this.callsSentRow) {
                     var5.setTextAndValue(LocaleController.getString("OutgoingCalls", 2131560133), String.format("%d", StatsController.getInstance(DataUsageActivity.access$3300(DataUsageActivity.this)).getSentItemsCount(this.currentType, var11)), true);
                  } else if (var2 == this.callsReceivedRow) {
                     var5.setTextAndValue(LocaleController.getString("IncomingCalls", 2131559662), String.format("%d", StatsController.getInstance(DataUsageActivity.access$3400(DataUsageActivity.this)).getRecivedItemsCount(this.currentType, var11)), true);
                  } else {
                     String var10;
                     if (var2 == this.callsTotalTimeRow) {
                        var3 = StatsController.getInstance(DataUsageActivity.access$3500(DataUsageActivity.this)).getCallsTotalTime(this.currentType);
                        var2 = var3 / 3600;
                        int var6 = var3 - var2 * 3600;
                        var3 = var6 / 60;
                        var6 -= var3 * 60;
                        if (var2 != 0) {
                           var10 = String.format("%d:%02d:%02d", var2, var3, var6);
                        } else {
                           var10 = String.format("%d:%02d", var3, var6);
                        }

                        var5.setTextAndValue(LocaleController.getString("CallsTotalTime", 2131558890), var10, false);
                     } else if (var2 != this.messagesSentRow && var2 != this.photosSentRow && var2 != this.videosSentRow && var2 != this.audiosSentRow && var2 != this.filesSentRow) {
                        if (var2 != this.messagesReceivedRow && var2 != this.photosReceivedRow && var2 != this.videosReceivedRow && var2 != this.audiosReceivedRow && var2 != this.filesReceivedRow) {
                           if (var2 != this.messagesBytesSentRow && var2 != this.photosBytesSentRow && var2 != this.videosBytesSentRow && var2 != this.audiosBytesSentRow && var2 != this.filesBytesSentRow && var2 != this.callsBytesSentRow && var2 != this.totalBytesSentRow) {
                              if (var2 == this.messagesBytesReceivedRow || var2 == this.photosBytesReceivedRow || var2 == this.videosBytesReceivedRow || var2 == this.audiosBytesReceivedRow || var2 == this.filesBytesReceivedRow || var2 == this.callsBytesReceivedRow || var2 == this.totalBytesReceivedRow) {
                                 String var7 = LocaleController.getString("BytesReceived", 2131558864);
                                 var10 = AndroidUtilities.formatFileSize(StatsController.getInstance(DataUsageActivity.access$3900(DataUsageActivity.this)).getReceivedBytesCount(this.currentType, var11));
                                 if (var2 != this.totalBytesReceivedRow) {
                                    var4 = true;
                                 }

                                 var5.setTextAndValue(var7, var10, var4);
                              }
                           } else {
                              var5.setTextAndValue(LocaleController.getString("BytesSent", 2131558865), AndroidUtilities.formatFileSize(StatsController.getInstance(DataUsageActivity.access$3800(DataUsageActivity.this)).getSentBytesCount(this.currentType, var11)), true);
                           }
                        } else {
                           var5.setTextAndValue(LocaleController.getString("CountReceived", 2131559165), String.format("%d", StatsController.getInstance(DataUsageActivity.access$3700(DataUsageActivity.this)).getRecivedItemsCount(this.currentType, var11)), true);
                        }
                     } else {
                        var5.setTextAndValue(LocaleController.getString("CountSent", 2131559166), String.format("%d", StatsController.getInstance(DataUsageActivity.access$3600(DataUsageActivity.this)).getSentItemsCount(this.currentType, var11)), true);
                     }
                  }
               }
            }
         } else if (var2 == this.resetSection2Row) {
            var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
         } else {
            var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     var3 = null;
                  } else {
                     var3 = new TextInfoPrivacyCell(this.mContext);
                  }
               } else {
                  var3 = new HeaderCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextSettingsCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new ShadowSectionCell(this.mContext);
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class ViewPage extends FrameLayout {
      private LinearLayoutManager layoutManager;
      private DataUsageActivity.ListAdapter listAdapter;
      private RecyclerListView listView;
      private int selectedType;

      public ViewPage(Context var2) {
         super(var2);
      }
   }
}
