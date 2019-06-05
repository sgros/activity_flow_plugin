package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.R;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetBehavior extends CoordinatorLayout.Behavior {
   int activePointerId;
   private BottomSheetBehavior.BottomSheetCallback callback;
   int collapsedOffset;
   private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         return var1.getLeft();
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         int var4 = BottomSheetBehavior.this.getExpandedOffset();
         if (BottomSheetBehavior.this.hideable) {
            var3 = BottomSheetBehavior.this.parentHeight;
         } else {
            var3 = BottomSheetBehavior.this.collapsedOffset;
         }

         return android.support.v4.math.MathUtils.clamp(var2, var4, var3);
      }

      public int getViewVerticalDragRange(View var1) {
         return BottomSheetBehavior.this.hideable ? BottomSheetBehavior.this.parentHeight : BottomSheetBehavior.this.collapsedOffset;
      }

      public void onViewDragStateChanged(int var1) {
         if (var1 == 1) {
            BottomSheetBehavior.this.setStateInternal(1);
         }

      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         BottomSheetBehavior.this.dispatchOnSlide(var3);
      }

      public void onViewReleased(View var1, float var2, float var3) {
         byte var4;
         int var5;
         label67: {
            label66: {
               label65: {
                  var4 = 4;
                  if (var3 < 0.0F) {
                     if (BottomSheetBehavior.this.fitToContents) {
                        var5 = BottomSheetBehavior.this.fitToContentsOffset;
                        break label66;
                     }

                     if (var1.getTop() > BottomSheetBehavior.this.halfExpandedOffset) {
                        var5 = BottomSheetBehavior.this.halfExpandedOffset;
                        break label65;
                     }
                  } else {
                     if (BottomSheetBehavior.this.hideable && BottomSheetBehavior.this.shouldHide(var1, var3) && (var1.getTop() > BottomSheetBehavior.this.collapsedOffset || Math.abs(var2) < Math.abs(var3))) {
                        var5 = BottomSheetBehavior.this.parentHeight;
                        var4 = 5;
                        break label67;
                     }

                     if (var3 != 0.0F && Math.abs(var2) <= Math.abs(var3)) {
                        var5 = BottomSheetBehavior.this.collapsedOffset;
                        break label67;
                     }

                     var5 = var1.getTop();
                     if (BottomSheetBehavior.this.fitToContents) {
                        if (Math.abs(var5 - BottomSheetBehavior.this.fitToContentsOffset) >= Math.abs(var5 - BottomSheetBehavior.this.collapsedOffset)) {
                           var5 = BottomSheetBehavior.this.collapsedOffset;
                           break label67;
                        }

                        var5 = BottomSheetBehavior.this.fitToContentsOffset;
                        break label66;
                     }

                     if (var5 >= BottomSheetBehavior.this.halfExpandedOffset) {
                        if (Math.abs(var5 - BottomSheetBehavior.this.halfExpandedOffset) >= Math.abs(var5 - BottomSheetBehavior.this.collapsedOffset)) {
                           var5 = BottomSheetBehavior.this.collapsedOffset;
                           break label67;
                        }

                        var5 = BottomSheetBehavior.this.halfExpandedOffset;
                        break label65;
                     }

                     if (var5 >= Math.abs(var5 - BottomSheetBehavior.this.collapsedOffset)) {
                        var5 = BottomSheetBehavior.this.halfExpandedOffset;
                        break label65;
                     }
                  }

                  var5 = 0;
                  break label66;
               }

               var4 = 6;
               break label67;
            }

            var4 = 3;
         }

         if (BottomSheetBehavior.this.viewDragHelper.settleCapturedViewAt(var1.getLeft(), var5)) {
            BottomSheetBehavior.this.setStateInternal(2);
            ViewCompat.postOnAnimation(var1, BottomSheetBehavior.this.new SettleRunnable(var1, var4));
         } else {
            BottomSheetBehavior.this.setStateInternal(var4);
         }

      }

      public boolean tryCaptureView(View var1, int var2) {
         int var3 = BottomSheetBehavior.this.state;
         boolean var4 = true;
         if (var3 == 1) {
            return false;
         } else if (BottomSheetBehavior.this.touchingScrollingChild) {
            return false;
         } else {
            if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == var2) {
               View var5 = (View)BottomSheetBehavior.this.nestedScrollingChildRef.get();
               if (var5 != null && var5.canScrollVertically(-1)) {
                  return false;
               }
            }

            if (BottomSheetBehavior.this.viewRef == null || BottomSheetBehavior.this.viewRef.get() != var1) {
               var4 = false;
            }

            return var4;
         }
      }
   };
   private boolean fitToContents = true;
   int fitToContentsOffset;
   int halfExpandedOffset;
   boolean hideable;
   private boolean ignoreEvents;
   private Map importantForAccessibilityMap;
   private int initialY;
   private int lastNestedScrollDy;
   private int lastPeekHeight;
   private float maximumVelocity;
   private boolean nestedScrolled;
   WeakReference nestedScrollingChildRef;
   int parentHeight;
   private int peekHeight;
   private boolean peekHeightAuto;
   private int peekHeightMin;
   private boolean skipCollapsed;
   int state = 4;
   boolean touchingScrollingChild;
   private VelocityTracker velocityTracker;
   ViewDragHelper viewDragHelper;
   WeakReference viewRef;

   public BottomSheetBehavior() {
   }

   public BottomSheetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.BottomSheetBehavior_Layout);
      TypedValue var4 = var3.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
      if (var4 != null && var4.data == -1) {
         this.setPeekHeight(var4.data);
      } else {
         this.setPeekHeight(var3.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
      }

      this.setHideable(var3.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
      this.setFitToContents(var3.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
      this.setSkipCollapsed(var3.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
      var3.recycle();
      this.maximumVelocity = (float)ViewConfiguration.get(var1).getScaledMaximumFlingVelocity();
   }

   private void calculateCollapsedOffset() {
      if (this.fitToContents) {
         this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
      } else {
         this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
      }

   }

   public static BottomSheetBehavior from(View var0) {
      LayoutParams var1 = var0.getLayoutParams();
      if (var1 instanceof CoordinatorLayout.LayoutParams) {
         CoordinatorLayout.Behavior var2 = ((CoordinatorLayout.LayoutParams)var1).getBehavior();
         if (var2 instanceof BottomSheetBehavior) {
            return (BottomSheetBehavior)var2;
         } else {
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
         }
      } else {
         throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
      }
   }

   private int getExpandedOffset() {
      int var1;
      if (this.fitToContents) {
         var1 = this.fitToContentsOffset;
      } else {
         var1 = 0;
      }

      return var1;
   }

   private float getYVelocity() {
      if (this.velocityTracker == null) {
         return 0.0F;
      } else {
         this.velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
         return this.velocityTracker.getYVelocity(this.activePointerId);
      }
   }

   private void reset() {
      this.activePointerId = -1;
      if (this.velocityTracker != null) {
         this.velocityTracker.recycle();
         this.velocityTracker = null;
      }

   }

   private void updateImportantForAccessibility(boolean var1) {
      if (this.viewRef != null) {
         ViewParent var2 = ((View)this.viewRef.get()).getParent();
         if (var2 instanceof CoordinatorLayout) {
            CoordinatorLayout var3 = (CoordinatorLayout)var2;
            int var4 = var3.getChildCount();
            if (VERSION.SDK_INT >= 16 && var1) {
               if (this.importantForAccessibilityMap != null) {
                  return;
               }

               this.importantForAccessibilityMap = new HashMap(var4);
            }

            for(int var5 = 0; var5 < var4; ++var5) {
               View var6 = var3.getChildAt(var5);
               if (var6 != this.viewRef.get()) {
                  if (!var1) {
                     if (this.importantForAccessibilityMap != null && this.importantForAccessibilityMap.containsKey(var6)) {
                        ViewCompat.setImportantForAccessibility(var6, (Integer)this.importantForAccessibilityMap.get(var6));
                     }
                  } else {
                     if (VERSION.SDK_INT >= 16) {
                        this.importantForAccessibilityMap.put(var6, var6.getImportantForAccessibility());
                     }

                     ViewCompat.setImportantForAccessibility(var6, 4);
                  }
               }
            }

            if (!var1) {
               this.importantForAccessibilityMap = null;
            }

         }
      }
   }

   void dispatchOnSlide(int var1) {
      View var2 = (View)this.viewRef.get();
      if (var2 != null && this.callback != null) {
         if (var1 > this.collapsedOffset) {
            this.callback.onSlide(var2, (float)(this.collapsedOffset - var1) / (float)(this.parentHeight - this.collapsedOffset));
         } else {
            this.callback.onSlide(var2, (float)(this.collapsedOffset - var1) / (float)(this.collapsedOffset - this.getExpandedOffset()));
         }
      }

   }

   View findScrollingChild(View var1) {
      if (ViewCompat.isNestedScrollingEnabled(var1)) {
         return var1;
      } else {
         if (var1 instanceof ViewGroup) {
            ViewGroup var2 = (ViewGroup)var1;
            int var3 = 0;

            for(int var4 = var2.getChildCount(); var3 < var4; ++var3) {
               var1 = this.findScrollingChild(var2.getChildAt(var3));
               if (var1 != null) {
                  return var1;
               }
            }
         }

         return null;
      }
   }

   public final int getPeekHeight() {
      int var1;
      if (this.peekHeightAuto) {
         var1 = -1;
      } else {
         var1 = this.peekHeight;
      }

      return var1;
   }

   public final int getState() {
      return this.state;
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      boolean var4 = var2.isShown();
      boolean var5 = false;
      if (!var4) {
         this.ignoreEvents = true;
         return false;
      } else {
         int var6 = var3.getActionMasked();
         if (var6 == 0) {
            this.reset();
         }

         if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
         }

         Object var7;
         label75: {
            this.velocityTracker.addMovement(var3);
            var7 = null;
            if (var6 != 3) {
               switch(var6) {
               case 0:
                  int var8 = (int)var3.getX();
                  this.initialY = (int)var3.getY();
                  View var9;
                  if (this.nestedScrollingChildRef != null) {
                     var9 = (View)this.nestedScrollingChildRef.get();
                  } else {
                     var9 = null;
                  }

                  if (var9 != null && var1.isPointInChildBounds(var9, var8, this.initialY)) {
                     this.activePointerId = var3.getPointerId(var3.getActionIndex());
                     this.touchingScrollingChild = true;
                  }

                  if (this.activePointerId == -1 && !var1.isPointInChildBounds(var2, var8, this.initialY)) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  this.ignoreEvents = var4;
                  break label75;
               case 1:
                  break;
               default:
                  break label75;
               }
            }

            this.touchingScrollingChild = false;
            this.activePointerId = -1;
            if (this.ignoreEvents) {
               this.ignoreEvents = false;
               return false;
            }
         }

         if (!this.ignoreEvents && this.viewDragHelper != null && this.viewDragHelper.shouldInterceptTouchEvent(var3)) {
            return true;
         } else {
            var2 = (View)var7;
            if (this.nestedScrollingChildRef != null) {
               var2 = (View)this.nestedScrollingChildRef.get();
            }

            var4 = var5;
            if (var6 == 2) {
               var4 = var5;
               if (var2 != null) {
                  var4 = var5;
                  if (!this.ignoreEvents) {
                     var4 = var5;
                     if (this.state != 1) {
                        var4 = var5;
                        if (!var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY())) {
                           var4 = var5;
                           if (this.viewDragHelper != null) {
                              var4 = var5;
                              if (Math.abs((float)this.initialY - var3.getY()) > (float)this.viewDragHelper.getTouchSlop()) {
                                 var4 = true;
                              }
                           }
                        }
                     }
                  }
               }
            }

            return var4;
         }
      }
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      if (ViewCompat.getFitsSystemWindows(var1) && !ViewCompat.getFitsSystemWindows(var2)) {
         var2.setFitsSystemWindows(true);
      }

      int var4 = var2.getTop();
      var1.onLayoutChild(var2, var3);
      this.parentHeight = var1.getHeight();
      if (this.peekHeightAuto) {
         if (this.peekHeightMin == 0) {
            this.peekHeightMin = var1.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
         }

         this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - var1.getWidth() * 9 / 16);
      } else {
         this.lastPeekHeight = this.peekHeight;
      }

      this.fitToContentsOffset = Math.max(0, this.parentHeight - var2.getHeight());
      this.halfExpandedOffset = this.parentHeight / 2;
      this.calculateCollapsedOffset();
      if (this.state == 3) {
         ViewCompat.offsetTopAndBottom(var2, this.getExpandedOffset());
      } else if (this.state == 6) {
         ViewCompat.offsetTopAndBottom(var2, this.halfExpandedOffset);
      } else if (this.hideable && this.state == 5) {
         ViewCompat.offsetTopAndBottom(var2, this.parentHeight);
      } else if (this.state == 4) {
         ViewCompat.offsetTopAndBottom(var2, this.collapsedOffset);
      } else if (this.state == 1 || this.state == 2) {
         ViewCompat.offsetTopAndBottom(var2, var4 - var2.getTop());
      }

      if (this.viewDragHelper == null) {
         this.viewDragHelper = ViewDragHelper.create(var1, this.dragCallback);
      }

      this.viewRef = new WeakReference(var2);
      this.nestedScrollingChildRef = new WeakReference(this.findScrollingChild(var2));
      return true;
   }

   public boolean onNestedPreFling(CoordinatorLayout var1, View var2, View var3, float var4, float var5) {
      boolean var6;
      if (var3 != this.nestedScrollingChildRef.get() || this.state == 3 && !super.onNestedPreFling(var1, var2, var3, var4, var5)) {
         var6 = false;
      } else {
         var6 = true;
      }

      return var6;
   }

   public void onNestedPreScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int[] var6, int var7) {
      if (var7 != 1) {
         if (var3 == (View)this.nestedScrollingChildRef.get()) {
            var7 = var2.getTop();
            var4 = var7 - var5;
            if (var5 > 0) {
               if (var4 < this.getExpandedOffset()) {
                  var6[1] = var7 - this.getExpandedOffset();
                  ViewCompat.offsetTopAndBottom(var2, -var6[1]);
                  this.setStateInternal(3);
               } else {
                  var6[1] = var5;
                  ViewCompat.offsetTopAndBottom(var2, -var5);
                  this.setStateInternal(1);
               }
            } else if (var5 < 0 && !var3.canScrollVertically(-1)) {
               if (var4 > this.collapsedOffset && !this.hideable) {
                  var6[1] = var7 - this.collapsedOffset;
                  ViewCompat.offsetTopAndBottom(var2, -var6[1]);
                  this.setStateInternal(4);
               } else {
                  var6[1] = var5;
                  ViewCompat.offsetTopAndBottom(var2, -var5);
                  this.setStateInternal(1);
               }
            }

            this.dispatchOnSlide(var2.getTop());
            this.lastNestedScrollDy = var5;
            this.nestedScrolled = true;
         }
      }
   }

   public void onRestoreInstanceState(CoordinatorLayout var1, View var2, Parcelable var3) {
      BottomSheetBehavior.SavedState var4 = (BottomSheetBehavior.SavedState)var3;
      super.onRestoreInstanceState(var1, var2, var4.getSuperState());
      if (var4.state != 1 && var4.state != 2) {
         this.state = var4.state;
      } else {
         this.state = 4;
      }

   }

   public Parcelable onSaveInstanceState(CoordinatorLayout var1, View var2) {
      return new BottomSheetBehavior.SavedState(super.onSaveInstanceState(var1, var2), this.state);
   }

   public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5, int var6) {
      boolean var7 = false;
      this.lastNestedScrollDy = 0;
      this.nestedScrolled = false;
      if ((var5 & 2) != 0) {
         var7 = true;
      }

      return var7;
   }

   public void onStopNestedScroll(CoordinatorLayout var1, View var2, View var3, int var4) {
      int var5 = var2.getTop();
      var4 = this.getExpandedOffset();
      byte var6 = 3;
      if (var5 == var4) {
         this.setStateInternal(3);
      } else if (var3 == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
         if (this.lastNestedScrollDy > 0) {
            var4 = this.getExpandedOffset();
         } else if (this.hideable && this.shouldHide(var2, this.getYVelocity())) {
            var4 = this.parentHeight;
            var6 = 5;
         } else {
            label66: {
               label49: {
                  if (this.lastNestedScrollDy == 0) {
                     var4 = var2.getTop();
                     if (this.fitToContents) {
                        if (Math.abs(var4 - this.fitToContentsOffset) < Math.abs(var4 - this.collapsedOffset)) {
                           var4 = this.fitToContentsOffset;
                           break label66;
                        }

                        var4 = this.collapsedOffset;
                     } else {
                        if (var4 < this.halfExpandedOffset) {
                           if (var4 < Math.abs(var4 - this.collapsedOffset)) {
                              var4 = 0;
                              break label66;
                           }

                           var4 = this.halfExpandedOffset;
                           break label49;
                        }

                        if (Math.abs(var4 - this.halfExpandedOffset) < Math.abs(var4 - this.collapsedOffset)) {
                           var4 = this.halfExpandedOffset;
                           break label49;
                        }

                        var4 = this.collapsedOffset;
                     }
                  } else {
                     var4 = this.collapsedOffset;
                  }

                  var6 = 4;
                  break label66;
               }

               var6 = 6;
            }
         }

         if (this.viewDragHelper.smoothSlideViewTo(var2, var2.getLeft(), var4)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(var2, new BottomSheetBehavior.SettleRunnable(var2, var6));
         } else {
            this.setStateInternal(var6);
         }

         this.nestedScrolled = false;
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (!var2.isShown()) {
         return false;
      } else {
         int var4 = var3.getActionMasked();
         if (this.state == 1 && var4 == 0) {
            return true;
         } else {
            if (this.viewDragHelper != null) {
               this.viewDragHelper.processTouchEvent(var3);
            }

            if (var4 == 0) {
               this.reset();
            }

            if (this.velocityTracker == null) {
               this.velocityTracker = VelocityTracker.obtain();
            }

            this.velocityTracker.addMovement(var3);
            if (var4 == 2 && !this.ignoreEvents && Math.abs((float)this.initialY - var3.getY()) > (float)this.viewDragHelper.getTouchSlop()) {
               this.viewDragHelper.captureChildView(var2, var3.getPointerId(var3.getActionIndex()));
            }

            return this.ignoreEvents ^ true;
         }
      }
   }

   public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback var1) {
      this.callback = var1;
   }

   public void setFitToContents(boolean var1) {
      if (this.fitToContents != var1) {
         this.fitToContents = var1;
         if (this.viewRef != null) {
            this.calculateCollapsedOffset();
         }

         int var2;
         if (this.fitToContents && this.state == 6) {
            var2 = 3;
         } else {
            var2 = this.state;
         }

         this.setStateInternal(var2);
      }
   }

   public void setHideable(boolean var1) {
      this.hideable = var1;
   }

   public final void setPeekHeight(int var1) {
      boolean var4;
      label36: {
         boolean var2 = true;
         if (var1 == -1) {
            if (!this.peekHeightAuto) {
               this.peekHeightAuto = true;
               var4 = var2;
               break label36;
            }
         } else if (this.peekHeightAuto || this.peekHeight != var1) {
            this.peekHeightAuto = false;
            this.peekHeight = Math.max(0, var1);
            this.collapsedOffset = this.parentHeight - var1;
            var4 = var2;
            break label36;
         }

         var4 = false;
      }

      if (var4 && this.state == 4 && this.viewRef != null) {
         View var3 = (View)this.viewRef.get();
         if (var3 != null) {
            var3.requestLayout();
         }
      }

   }

   public void setSkipCollapsed(boolean var1) {
      this.skipCollapsed = var1;
   }

   public final void setState(final int var1) {
      if (var1 != this.state) {
         if (this.viewRef == null) {
            if (var1 == 4 || var1 == 3 || var1 == 6 || this.hideable && var1 == 5) {
               this.state = var1;
            }

         } else {
            final View var2 = (View)this.viewRef.get();
            if (var2 != null) {
               ViewParent var3 = var2.getParent();
               if (var3 != null && var3.isLayoutRequested() && ViewCompat.isAttachedToWindow(var2)) {
                  var2.post(new Runnable() {
                     public void run() {
                        BottomSheetBehavior.this.startSettlingAnimation(var2, var1);
                     }
                  });
               } else {
                  this.startSettlingAnimation(var2, var1);
               }

            }
         }
      }
   }

   void setStateInternal(int var1) {
      if (this.state != var1) {
         this.state = var1;
         if (var1 != 6 && var1 != 3) {
            if (var1 == 5 || var1 == 4) {
               this.updateImportantForAccessibility(false);
            }
         } else {
            this.updateImportantForAccessibility(true);
         }

         View var2 = (View)this.viewRef.get();
         if (var2 != null && this.callback != null) {
            this.callback.onStateChanged(var2, var1);
         }

      }
   }

   boolean shouldHide(View var1, float var2) {
      boolean var3 = this.skipCollapsed;
      boolean var4 = true;
      if (var3) {
         return true;
      } else if (var1.getTop() < this.collapsedOffset) {
         return false;
      } else {
         if (Math.abs((float)var1.getTop() + var2 * 0.1F - (float)this.collapsedOffset) / (float)this.peekHeight <= 0.5F) {
            var4 = false;
         }

         return var4;
      }
   }

   void startSettlingAnimation(View var1, int var2) {
      int var3;
      if (var2 == 4) {
         var3 = this.collapsedOffset;
      } else if (var2 == 6) {
         var3 = this.halfExpandedOffset;
         if (this.fitToContents && var3 <= this.fitToContentsOffset) {
            var3 = this.fitToContentsOffset;
            var2 = 3;
         }
      } else if (var2 == 3) {
         var3 = this.getExpandedOffset();
      } else {
         if (!this.hideable || var2 != 5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Illegal state argument: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }

         var3 = this.parentHeight;
      }

      if (this.viewDragHelper.smoothSlideViewTo(var1, var1.getLeft(), var3)) {
         this.setStateInternal(2);
         ViewCompat.postOnAnimation(var1, new BottomSheetBehavior.SettleRunnable(var1, var2));
      } else {
         this.setStateInternal(var2);
      }

   }

   public abstract static class BottomSheetCallback {
      public abstract void onSlide(View var1, float var2);

      public abstract void onStateChanged(View var1, int var2);
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public BottomSheetBehavior.SavedState createFromParcel(Parcel var1) {
            return new BottomSheetBehavior.SavedState(var1, (ClassLoader)null);
         }

         public BottomSheetBehavior.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new BottomSheetBehavior.SavedState(var1, var2);
         }

         public BottomSheetBehavior.SavedState[] newArray(int var1) {
            return new BottomSheetBehavior.SavedState[var1];
         }
      };
      final int state;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.state = var1.readInt();
      }

      public SavedState(Parcelable var1, int var2) {
         super(var1);
         this.state = var2;
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.state);
      }
   }

   private class SettleRunnable implements Runnable {
      private final int targetState;
      private final View view;

      SettleRunnable(View var2, int var3) {
         this.view = var2;
         this.targetState = var3;
      }

      public void run() {
         if (BottomSheetBehavior.this.viewDragHelper != null && BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
            ViewCompat.postOnAnimation(this.view, this);
         } else {
            BottomSheetBehavior.this.setStateInternal(this.targetState);
         }

      }
   }
}
