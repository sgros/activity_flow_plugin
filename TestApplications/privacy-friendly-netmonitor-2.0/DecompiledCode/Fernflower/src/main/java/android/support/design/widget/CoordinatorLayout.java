package android.support.design.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.math.MathUtils;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2 {
   static final Class[] CONSTRUCTOR_PARAMS;
   static final int EVENT_NESTED_SCROLL = 1;
   static final int EVENT_PRE_DRAW = 0;
   static final int EVENT_VIEW_REMOVED = 2;
   static final String TAG = "CoordinatorLayout";
   static final Comparator TOP_SORTED_CHILDREN_COMPARATOR;
   private static final int TYPE_ON_INTERCEPT = 0;
   private static final int TYPE_ON_TOUCH = 1;
   static final String WIDGET_PACKAGE_NAME;
   static final ThreadLocal sConstructors;
   private static final Pools.Pool sRectPool;
   private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
   private View mBehaviorTouchView;
   private final DirectedAcyclicGraph mChildDag;
   private final List mDependencySortedChildren;
   private boolean mDisallowInterceptReset;
   private boolean mDrawStatusBarBackground;
   private boolean mIsAttachedToWindow;
   private int[] mKeylines;
   private WindowInsetsCompat mLastInsets;
   private boolean mNeedsPreDrawListener;
   private final NestedScrollingParentHelper mNestedScrollingParentHelper;
   private View mNestedScrollingTarget;
   OnHierarchyChangeListener mOnHierarchyChangeListener;
   private CoordinatorLayout.OnPreDrawListener mOnPreDrawListener;
   private Paint mScrimPaint;
   private Drawable mStatusBarBackground;
   private final List mTempDependenciesList;
   private final int[] mTempIntPair;
   private final List mTempList1;

   static {
      Package var0 = CoordinatorLayout.class.getPackage();
      String var1;
      if (var0 != null) {
         var1 = var0.getName();
      } else {
         var1 = null;
      }

      WIDGET_PACKAGE_NAME = var1;
      if (VERSION.SDK_INT >= 21) {
         TOP_SORTED_CHILDREN_COMPARATOR = new CoordinatorLayout.ViewElevationComparator();
      } else {
         TOP_SORTED_CHILDREN_COMPARATOR = null;
      }

      CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
      sConstructors = new ThreadLocal();
      sRectPool = new Pools.SynchronizedPool(12);
   }

   public CoordinatorLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CoordinatorLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public CoordinatorLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mDependencySortedChildren = new ArrayList();
      this.mChildDag = new DirectedAcyclicGraph();
      this.mTempList1 = new ArrayList();
      this.mTempDependenciesList = new ArrayList();
      this.mTempIntPair = new int[2];
      this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
      ThemeUtils.checkAppCompatTheme(var1);
      TypedArray var7 = var1.obtainStyledAttributes(var2, R.styleable.CoordinatorLayout, var3, R.style.Widget_Design_CoordinatorLayout);
      int var4 = R.styleable.CoordinatorLayout_keylines;
      var3 = 0;
      var4 = var7.getResourceId(var4, 0);
      if (var4 != 0) {
         Resources var6 = var1.getResources();
         this.mKeylines = var6.getIntArray(var4);
         float var5 = var6.getDisplayMetrics().density;

         for(var4 = this.mKeylines.length; var3 < var4; ++var3) {
            this.mKeylines[var3] = (int)((float)this.mKeylines[var3] * var5);
         }
      }

      this.mStatusBarBackground = var7.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
      var7.recycle();
      this.setupForInsets();
      super.setOnHierarchyChangeListener(new CoordinatorLayout.HierarchyChangeListener());
   }

   @NonNull
   private static Rect acquireTempRect() {
      Rect var0 = (Rect)sRectPool.acquire();
      Rect var1 = var0;
      if (var0 == null) {
         var1 = new Rect();
      }

      return var1;
   }

   private void constrainChildRect(CoordinatorLayout.LayoutParams var1, Rect var2, int var3, int var4) {
      int var5 = this.getWidth();
      int var6 = this.getHeight();
      var5 = Math.max(this.getPaddingLeft() + var1.leftMargin, Math.min(var2.left, var5 - this.getPaddingRight() - var3 - var1.rightMargin));
      var6 = Math.max(this.getPaddingTop() + var1.topMargin, Math.min(var2.top, var6 - this.getPaddingBottom() - var4 - var1.bottomMargin));
      var2.set(var5, var6, var3 + var5, var4 + var6);
   }

   private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat var1) {
      if (var1.isConsumed()) {
         return var1;
      } else {
         int var2 = 0;
         int var3 = this.getChildCount();

         WindowInsetsCompat var4;
         while(true) {
            var4 = var1;
            if (var2 >= var3) {
               break;
            }

            View var5 = this.getChildAt(var2);
            var4 = var1;
            if (ViewCompat.getFitsSystemWindows(var5)) {
               CoordinatorLayout.Behavior var6 = ((CoordinatorLayout.LayoutParams)var5.getLayoutParams()).getBehavior();
               var4 = var1;
               if (var6 != null) {
                  var1 = var6.onApplyWindowInsets(this, var5, var1);
                  var4 = var1;
                  if (var1.isConsumed()) {
                     var4 = var1;
                     break;
                  }
               }
            }

            ++var2;
            var1 = var4;
         }

         return var4;
      }
   }

   private void getDesiredAnchoredChildRectWithoutConstraints(View var1, int var2, Rect var3, Rect var4, CoordinatorLayout.LayoutParams var5, int var6, int var7) {
      int var8 = GravityCompat.getAbsoluteGravity(resolveAnchoredChildGravity(var5.gravity), var2);
      int var9 = GravityCompat.getAbsoluteGravity(resolveGravity(var5.anchorGravity), var2);
      int var10 = var8 & 7;
      int var11 = var8 & 112;
      var2 = var9 & 7;
      var9 &= 112;
      if (var2 != 1) {
         if (var2 != 5) {
            var2 = var3.left;
         } else {
            var2 = var3.right;
         }
      } else {
         var2 = var3.left + var3.width() / 2;
      }

      if (var9 != 16) {
         if (var9 != 80) {
            var9 = var3.top;
         } else {
            var9 = var3.bottom;
         }
      } else {
         var9 = var3.top + var3.height() / 2;
      }

      if (var10 != 1) {
         var8 = var2;
         if (var10 != 5) {
            var8 = var2 - var6;
         }
      } else {
         var8 = var2 - var6 / 2;
      }

      if (var11 != 16) {
         var2 = var9;
         if (var11 != 80) {
            var2 = var9 - var7;
         }
      } else {
         var2 = var9 - var7 / 2;
      }

      var4.set(var8, var2, var6 + var8, var7 + var2);
   }

   private int getKeyline(int var1) {
      StringBuilder var2;
      if (this.mKeylines == null) {
         var2 = new StringBuilder();
         var2.append("No keylines defined for ");
         var2.append(this);
         var2.append(" - attempted index lookup ");
         var2.append(var1);
         Log.e("CoordinatorLayout", var2.toString());
         return 0;
      } else if (var1 >= 0 && var1 < this.mKeylines.length) {
         return this.mKeylines[var1];
      } else {
         var2 = new StringBuilder();
         var2.append("Keyline index ");
         var2.append(var1);
         var2.append(" out of range for ");
         var2.append(this);
         Log.e("CoordinatorLayout", var2.toString());
         return 0;
      }
   }

   private void getTopSortedChildren(List var1) {
      var1.clear();
      boolean var2 = this.isChildrenDrawingOrderEnabled();
      int var3 = this.getChildCount();

      for(int var4 = var3 - 1; var4 >= 0; --var4) {
         int var5;
         if (var2) {
            var5 = this.getChildDrawingOrder(var3, var4);
         } else {
            var5 = var4;
         }

         var1.add(this.getChildAt(var5));
      }

      if (TOP_SORTED_CHILDREN_COMPARATOR != null) {
         Collections.sort(var1, TOP_SORTED_CHILDREN_COMPARATOR);
      }

   }

   private boolean hasDependencies(View var1) {
      return this.mChildDag.hasOutgoingEdges(var1);
   }

   private void layoutChild(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      Rect var4 = acquireTempRect();
      var4.set(this.getPaddingLeft() + var3.leftMargin, this.getPaddingTop() + var3.topMargin, this.getWidth() - this.getPaddingRight() - var3.rightMargin, this.getHeight() - this.getPaddingBottom() - var3.bottomMargin);
      if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this) && !ViewCompat.getFitsSystemWindows(var1)) {
         var4.left += this.mLastInsets.getSystemWindowInsetLeft();
         var4.top += this.mLastInsets.getSystemWindowInsetTop();
         var4.right -= this.mLastInsets.getSystemWindowInsetRight();
         var4.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
      }

      Rect var5 = acquireTempRect();
      GravityCompat.apply(resolveGravity(var3.gravity), var1.getMeasuredWidth(), var1.getMeasuredHeight(), var4, var5, var2);
      var1.layout(var5.left, var5.top, var5.right, var5.bottom);
      releaseTempRect(var4);
      releaseTempRect(var5);
   }

   private void layoutChildWithAnchor(View var1, View var2, int var3) {
      CoordinatorLayout.LayoutParams var4 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      Rect var8 = acquireTempRect();
      Rect var5 = acquireTempRect();

      try {
         this.getDescendantRect(var2, var8);
         this.getDesiredAnchoredChildRect(var1, var3, var8, var5);
         var1.layout(var5.left, var5.top, var5.right, var5.bottom);
      } finally {
         releaseTempRect(var8);
         releaseTempRect(var5);
      }

   }

   private void layoutChildWithKeyline(View var1, int var2, int var3) {
      CoordinatorLayout.LayoutParams var4 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      int var5 = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(var4.gravity), var3);
      int var6 = var5 & 7;
      int var7 = var5 & 112;
      int var8 = this.getWidth();
      int var9 = this.getHeight();
      int var10 = var1.getMeasuredWidth();
      int var11 = var1.getMeasuredHeight();
      var5 = var2;
      if (var3 == 1) {
         var5 = var8 - var2;
      }

      var2 = this.getKeyline(var5) - var10;
      var3 = 0;
      if (var6 != 1) {
         if (var6 == 5) {
            var2 += var10;
         }
      } else {
         var2 += var10 / 2;
      }

      if (var7 != 16) {
         if (var7 == 80) {
            var3 = 0 + var11;
         }
      } else {
         var3 = 0 + var11 / 2;
      }

      var2 = Math.max(this.getPaddingLeft() + var4.leftMargin, Math.min(var2, var8 - this.getPaddingRight() - var10 - var4.rightMargin));
      var3 = Math.max(this.getPaddingTop() + var4.topMargin, Math.min(var3, var9 - this.getPaddingBottom() - var11 - var4.bottomMargin));
      var1.layout(var2, var3, var10 + var2, var11 + var3);
   }

   private void offsetChildByInset(View var1, Rect var2, int var3) {
      if (ViewCompat.isLaidOut(var1)) {
         if (var1.getWidth() > 0 && var1.getHeight() > 0) {
            CoordinatorLayout.LayoutParams var4 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
            CoordinatorLayout.Behavior var5 = var4.getBehavior();
            Rect var6 = acquireTempRect();
            Rect var7 = acquireTempRect();
            var7.set(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom());
            if (var5 != null && var5.getInsetDodgeRect(this, var1, var6)) {
               if (!var7.contains(var6)) {
                  StringBuilder var11 = new StringBuilder();
                  var11.append("Rect should be within the child's bounds. Rect:");
                  var11.append(var6.toShortString());
                  var11.append(" | Bounds:");
                  var11.append(var7.toShortString());
                  throw new IllegalArgumentException(var11.toString());
               }
            } else {
               var6.set(var7);
            }

            releaseTempRect(var7);
            if (var6.isEmpty()) {
               releaseTempRect(var6);
            } else {
               int var8;
               boolean var12;
               label57: {
                  var8 = GravityCompat.getAbsoluteGravity(var4.dodgeInsetEdges, var3);
                  if ((var8 & 48) == 48) {
                     var3 = var6.top - var4.topMargin - var4.mInsetOffsetY;
                     if (var3 < var2.top) {
                        this.setInsetOffsetY(var1, var2.top - var3);
                        var12 = true;
                        break label57;
                     }
                  }

                  var12 = false;
               }

               boolean var9 = var12;
               if ((var8 & 80) == 80) {
                  int var10 = this.getHeight() - var6.bottom - var4.bottomMargin + var4.mInsetOffsetY;
                  var9 = var12;
                  if (var10 < var2.bottom) {
                     this.setInsetOffsetY(var1, var10 - var2.bottom);
                     var9 = true;
                  }
               }

               if (!var9) {
                  this.setInsetOffsetY(var1, 0);
               }

               label49: {
                  if ((var8 & 3) == 3) {
                     var3 = var6.left - var4.leftMargin - var4.mInsetOffsetX;
                     if (var3 < var2.left) {
                        this.setInsetOffsetX(var1, var2.left - var3);
                        var12 = true;
                        break label49;
                     }
                  }

                  var12 = false;
               }

               var9 = var12;
               if ((var8 & 5) == 5) {
                  var8 = this.getWidth() - var6.right - var4.rightMargin + var4.mInsetOffsetX;
                  var9 = var12;
                  if (var8 < var2.right) {
                     this.setInsetOffsetX(var1, var8 - var2.right);
                     var9 = true;
                  }
               }

               if (!var9) {
                  this.setInsetOffsetX(var1, 0);
               }

               releaseTempRect(var6);
            }
         }
      }
   }

   static CoordinatorLayout.Behavior parseBehavior(Context var0, AttributeSet var1, String var2) {
      if (TextUtils.isEmpty(var2)) {
         return null;
      } else {
         StringBuilder var3;
         String var15;
         if (var2.startsWith(".")) {
            var3 = new StringBuilder();
            var3.append(var0.getPackageName());
            var3.append(var2);
            var15 = var3.toString();
         } else if (var2.indexOf(46) >= 0) {
            var15 = var2;
         } else {
            var15 = var2;
            if (!TextUtils.isEmpty(WIDGET_PACKAGE_NAME)) {
               var3 = new StringBuilder();
               var3.append(WIDGET_PACKAGE_NAME);
               var3.append('.');
               var3.append(var2);
               var15 = var3.toString();
            }
         }

         Exception var10000;
         label57: {
            Map var4;
            boolean var10001;
            try {
               var4 = (Map)sConstructors.get();
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label57;
            }

            Object var14 = var4;
            if (var4 == null) {
               try {
                  var14 = new HashMap();
                  sConstructors.set(var14);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label57;
               }
            }

            Constructor var5;
            try {
               var5 = (Constructor)((Map)var14).get(var15);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label57;
            }

            Constructor var16 = var5;
            if (var5 == null) {
               try {
                  var16 = Class.forName(var15, true, var0.getClassLoader()).getConstructor(CONSTRUCTOR_PARAMS);
                  var16.setAccessible(true);
                  ((Map)var14).put(var15, var16);
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label57;
               }
            }

            try {
               CoordinatorLayout.Behavior var12 = (CoordinatorLayout.Behavior)var16.newInstance(var0, var1);
               return var12;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         Exception var11 = var10000;
         StringBuilder var13 = new StringBuilder();
         var13.append("Could not inflate Behavior subclass ");
         var13.append(var15);
         throw new RuntimeException(var13.toString(), var11);
      }
   }

   private boolean performIntercept(MotionEvent var1, int var2) {
      int var3 = var1.getActionMasked();
      List var4 = this.mTempList1;
      this.getTopSortedChildren(var4);
      int var5 = var4.size();
      byte var6 = 0;
      MotionEvent var8 = null;
      int var9 = var6;
      byte var10 = var6;
      byte var7 = var6;

      while(true) {
         var6 = var7;
         if (var9 >= var5) {
            break;
         }

         View var11 = (View)var4.get(var9);
         CoordinatorLayout.LayoutParams var12 = (CoordinatorLayout.LayoutParams)var11.getLayoutParams();
         CoordinatorLayout.Behavior var13 = var12.getBehavior();
         byte var14;
         MotionEvent var19;
         if ((var7 != 0 || var10 != 0) && var3 != 0) {
            var14 = var7;
            var6 = var10;
            var19 = var8;
            if (var13 != null) {
               var19 = var8;
               if (var8 == null) {
                  long var15 = SystemClock.uptimeMillis();
                  var19 = MotionEvent.obtain(var15, var15, 3, 0.0F, 0.0F, 0);
               }

               switch(var2) {
               case 0:
                  var13.onInterceptTouchEvent(this, var11, var19);
                  var14 = var7;
                  var6 = var10;
                  break;
               case 1:
                  var13.onTouchEvent(this, var11, var19);
                  var14 = var7;
                  var6 = var10;
                  break;
               default:
                  var14 = var7;
                  var6 = var10;
               }
            }
         } else {
            var10 = var7;
            if (var7 == 0) {
               var10 = var7;
               if (var13 != null) {
                  switch(var2) {
                  case 0:
                     var7 = var13.onInterceptTouchEvent(this, var11, var1);
                     break;
                  case 1:
                     var7 = var13.onTouchEvent(this, var11, var1);
                  }

                  var10 = var7;
                  if (var7 != 0) {
                     this.mBehaviorTouchView = var11;
                     var10 = var7;
                  }
               }
            }

            boolean var17 = var12.didBlockInteraction();
            boolean var18 = var12.isBlockingInteractionBelow(this, var11);
            if (var18 && !var17) {
               var7 = 1;
            } else {
               var7 = 0;
            }

            var14 = var10;
            var6 = var7;
            var19 = var8;
            if (var18) {
               var14 = var10;
               var6 = var7;
               var19 = var8;
               if (var7 == 0) {
                  var6 = var10;
                  break;
               }
            }
         }

         ++var9;
         var7 = var14;
         var10 = var6;
         var8 = var19;
      }

      var4.clear();
      return (boolean)var6;
   }

   private void prepareChildren() {
      this.mDependencySortedChildren.clear();
      this.mChildDag.clear();
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         CoordinatorLayout.LayoutParams var4 = this.getResolvedLayoutParams(var3);
         var4.findAnchorView(this, var3);
         this.mChildDag.addNode(var3);

         for(int var5 = 0; var5 < var1; ++var5) {
            if (var5 != var2) {
               View var6 = this.getChildAt(var5);
               if (var4.dependsOn(this, var3, var6)) {
                  if (!this.mChildDag.contains(var6)) {
                     this.mChildDag.addNode(var6);
                  }

                  this.mChildDag.addEdge(var6, var3);
               }
            }
         }
      }

      this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
      Collections.reverse(this.mDependencySortedChildren);
   }

   private static void releaseTempRect(@NonNull Rect var0) {
      var0.setEmpty();
      sRectPool.release(var0);
   }

   private void resetTouchBehaviors() {
      if (this.mBehaviorTouchView != null) {
         CoordinatorLayout.Behavior var1 = ((CoordinatorLayout.LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
         if (var1 != null) {
            long var2 = SystemClock.uptimeMillis();
            MotionEvent var4 = MotionEvent.obtain(var2, var2, 3, 0.0F, 0.0F, 0);
            var1.onTouchEvent(this, this.mBehaviorTouchView, var4);
            var4.recycle();
         }

         this.mBehaviorTouchView = null;
      }

      int var5 = this.getChildCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         ((CoordinatorLayout.LayoutParams)this.getChildAt(var6).getLayoutParams()).resetTouchBehaviorTracking();
      }

      this.mDisallowInterceptReset = false;
   }

   private static int resolveAnchoredChildGravity(int var0) {
      int var1 = var0;
      if (var0 == 0) {
         var1 = 17;
      }

      return var1;
   }

   private static int resolveGravity(int var0) {
      int var1 = var0;
      if ((var0 & 7) == 0) {
         var1 = var0 | 8388611;
      }

      var0 = var1;
      if ((var1 & 112) == 0) {
         var0 = var1 | 48;
      }

      return var0;
   }

   private static int resolveKeylineGravity(int var0) {
      int var1 = var0;
      if (var0 == 0) {
         var1 = 8388661;
      }

      return var1;
   }

   private void setInsetOffsetX(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var3.mInsetOffsetX != var2) {
         ViewCompat.offsetLeftAndRight(var1, var2 - var3.mInsetOffsetX);
         var3.mInsetOffsetX = var2;
      }

   }

   private void setInsetOffsetY(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var3.mInsetOffsetY != var2) {
         ViewCompat.offsetTopAndBottom(var1, var2 - var3.mInsetOffsetY);
         var3.mInsetOffsetY = var2;
      }

   }

   private void setupForInsets() {
      if (VERSION.SDK_INT >= 21) {
         if (ViewCompat.getFitsSystemWindows(this)) {
            if (this.mApplyWindowInsetsListener == null) {
               this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     return CoordinatorLayout.this.setWindowInsets(var2);
                  }
               };
            }

            ViewCompat.setOnApplyWindowInsetsListener(this, this.mApplyWindowInsetsListener);
            this.setSystemUiVisibility(1280);
         } else {
            ViewCompat.setOnApplyWindowInsetsListener(this, (OnApplyWindowInsetsListener)null);
         }

      }
   }

   void addPreDrawListener() {
      if (this.mIsAttachedToWindow) {
         if (this.mOnPreDrawListener == null) {
            this.mOnPreDrawListener = new CoordinatorLayout.OnPreDrawListener();
         }

         this.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
      }

      this.mNeedsPreDrawListener = true;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 instanceof CoordinatorLayout.LayoutParams && super.checkLayoutParams(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void dispatchDependentViewsChanged(View var1) {
      List var2 = this.mChildDag.getIncomingEdges(var1);
      if (var2 != null && !var2.isEmpty()) {
         for(int var3 = 0; var3 < var2.size(); ++var3) {
            View var4 = (View)var2.get(var3);
            CoordinatorLayout.Behavior var5 = ((CoordinatorLayout.LayoutParams)var4.getLayoutParams()).getBehavior();
            if (var5 != null) {
               var5.onDependentViewChanged(this, var4, var1);
            }
         }
      }

   }

   public boolean doViewsOverlap(View var1, View var2) {
      int var3 = var1.getVisibility();
      boolean var4 = false;
      if (var3 == 0 && var2.getVisibility() == 0) {
         Rect var5 = acquireTempRect();
         boolean var6;
         if (var1.getParent() != this) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.getChildRect(var1, var6, var5);
         Rect var20 = acquireTempRect();
         if (var2.getParent() != this) {
            var6 = true;
         } else {
            var6 = false;
         }

         this.getChildRect(var2, var6, var20);
         var6 = var4;

         label221: {
            int var7;
            label220: {
               Throwable var10000;
               label228: {
                  boolean var10001;
                  try {
                     if (var5.left > var20.right) {
                        break label221;
                     }
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label228;
                  }

                  var6 = var4;

                  try {
                     if (var5.top > var20.bottom) {
                        break label221;
                     }
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label228;
                  }

                  var6 = var4;

                  label211:
                  try {
                     if (var5.right < var20.left) {
                        break label221;
                     }

                     var7 = var5.bottom;
                     var3 = var20.top;
                     break label220;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label211;
                  }
               }

               Throwable var21 = var10000;
               releaseTempRect(var5);
               releaseTempRect(var20);
               throw var21;
            }

            var6 = var4;
            if (var7 >= var3) {
               var6 = true;
            }
         }

         releaseTempRect(var5);
         releaseTempRect(var20);
         return var6;
      } else {
         return false;
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
      if (var5.mBehavior != null) {
         float var6 = var5.mBehavior.getScrimOpacity(this, var2);
         if (var6 > 0.0F) {
            if (this.mScrimPaint == null) {
               this.mScrimPaint = new Paint();
            }

            this.mScrimPaint.setColor(var5.mBehavior.getScrimColor(this, var2));
            this.mScrimPaint.setAlpha(MathUtils.clamp(Math.round(255.0F * var6), 0, 255));
            int var7 = var1.save();
            if (var2.isOpaque()) {
               var1.clipRect((float)var2.getLeft(), (float)var2.getTop(), (float)var2.getRight(), (float)var2.getBottom(), Op.DIFFERENCE);
            }

            var1.drawRect((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - this.getPaddingBottom()), this.mScrimPaint);
            var1.restoreToCount(var7);
         }
      }

      return super.drawChild(var1, var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      int[] var1 = this.getDrawableState();
      Drawable var2 = this.mStatusBarBackground;
      boolean var3 = false;
      boolean var4 = var3;
      if (var2 != null) {
         var4 = var3;
         if (var2.isStateful()) {
            var4 = false | var2.setState(var1);
         }
      }

      if (var4) {
         this.invalidate();
      }

   }

   void ensurePreDrawListener() {
      int var1 = this.getChildCount();
      boolean var2 = false;
      int var3 = 0;

      boolean var4;
      while(true) {
         var4 = var2;
         if (var3 >= var1) {
            break;
         }

         if (this.hasDependencies(this.getChildAt(var3))) {
            var4 = true;
            break;
         }

         ++var3;
      }

      if (var4 != this.mNeedsPreDrawListener) {
         if (var4) {
            this.addPreDrawListener();
         } else {
            this.removePreDrawListener();
         }
      }

   }

   protected CoordinatorLayout.LayoutParams generateDefaultLayoutParams() {
      return new CoordinatorLayout.LayoutParams(-2, -2);
   }

   public CoordinatorLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new CoordinatorLayout.LayoutParams(this.getContext(), var1);
   }

   protected CoordinatorLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 instanceof CoordinatorLayout.LayoutParams) {
         return new CoordinatorLayout.LayoutParams((CoordinatorLayout.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new CoordinatorLayout.LayoutParams((MarginLayoutParams)var1) : new CoordinatorLayout.LayoutParams(var1);
      }
   }

   void getChildRect(View var1, boolean var2, Rect var3) {
      if (!var1.isLayoutRequested() && var1.getVisibility() != 8) {
         if (var2) {
            this.getDescendantRect(var1, var3);
         } else {
            var3.set(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom());
         }

      } else {
         var3.setEmpty();
      }
   }

   @NonNull
   public List getDependencies(@NonNull View var1) {
      List var2 = this.mChildDag.getOutgoingEdges(var1);
      this.mTempDependenciesList.clear();
      if (var2 != null) {
         this.mTempDependenciesList.addAll(var2);
      }

      return this.mTempDependenciesList;
   }

   @VisibleForTesting
   final List getDependencySortedChildren() {
      this.prepareChildren();
      return Collections.unmodifiableList(this.mDependencySortedChildren);
   }

   @NonNull
   public List getDependents(@NonNull View var1) {
      List var2 = this.mChildDag.getIncomingEdges(var1);
      this.mTempDependenciesList.clear();
      if (var2 != null) {
         this.mTempDependenciesList.addAll(var2);
      }

      return this.mTempDependenciesList;
   }

   void getDescendantRect(View var1, Rect var2) {
      ViewGroupUtils.getDescendantRect(this, var1, var2);
   }

   void getDesiredAnchoredChildRect(View var1, int var2, Rect var3, Rect var4) {
      CoordinatorLayout.LayoutParams var5 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      int var6 = var1.getMeasuredWidth();
      int var7 = var1.getMeasuredHeight();
      this.getDesiredAnchoredChildRectWithoutConstraints(var1, var2, var3, var4, var5, var6, var7);
      this.constrainChildRect(var5, var4, var6, var7);
   }

   void getLastChildRect(View var1, Rect var2) {
      var2.set(((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).getLastChildRect());
   }

   final WindowInsetsCompat getLastWindowInsets() {
      return this.mLastInsets;
   }

   public int getNestedScrollAxes() {
      return this.mNestedScrollingParentHelper.getNestedScrollAxes();
   }

   CoordinatorLayout.LayoutParams getResolvedLayoutParams(View var1) {
      CoordinatorLayout.LayoutParams var2 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (!var2.mBehaviorResolved) {
         Class var3 = var1.getClass();

         CoordinatorLayout.DefaultBehavior var4;
         CoordinatorLayout.DefaultBehavior var6;
         for(var6 = null; var3 != null; var6 = var4) {
            var4 = (CoordinatorLayout.DefaultBehavior)var3.getAnnotation(CoordinatorLayout.DefaultBehavior.class);
            var6 = var4;
            if (var4 != null) {
               break;
            }

            var3 = var3.getSuperclass();
         }

         if (var6 != null) {
            try {
               var2.setBehavior((CoordinatorLayout.Behavior)var6.value().getDeclaredConstructor().newInstance());
            } catch (Exception var5) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Default behavior class ");
               var7.append(var6.value().getName());
               var7.append(" could not be instantiated. Did you forget a default constructor?");
               Log.e("CoordinatorLayout", var7.toString(), var5);
            }
         }

         var2.mBehaviorResolved = true;
      }

      return var2;
   }

   @Nullable
   public Drawable getStatusBarBackground() {
      return this.mStatusBarBackground;
   }

   protected int getSuggestedMinimumHeight() {
      return Math.max(super.getSuggestedMinimumHeight(), this.getPaddingTop() + this.getPaddingBottom());
   }

   protected int getSuggestedMinimumWidth() {
      return Math.max(super.getSuggestedMinimumWidth(), this.getPaddingLeft() + this.getPaddingRight());
   }

   public boolean isPointInChildBounds(View var1, int var2, int var3) {
      Rect var4 = acquireTempRect();
      this.getDescendantRect(var1, var4);

      boolean var5;
      try {
         var5 = var4.contains(var2, var3);
      } finally {
         releaseTempRect(var4);
      }

      return var5;
   }

   void offsetChildToAnchor(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var3.mAnchorView != null) {
         Rect var4;
         Rect var5;
         Rect var6;
         int var8;
         int var9;
         boolean var11;
         label27: {
            var4 = acquireTempRect();
            var5 = acquireTempRect();
            var6 = acquireTempRect();
            this.getDescendantRect(var3.mAnchorView, var4);
            boolean var7 = false;
            this.getChildRect(var1, false, var5);
            var8 = var1.getMeasuredWidth();
            var9 = var1.getMeasuredHeight();
            this.getDesiredAnchoredChildRectWithoutConstraints(var1, var2, var4, var6, var3, var8, var9);
            if (var6.left == var5.left) {
               var11 = var7;
               if (var6.top == var5.top) {
                  break label27;
               }
            }

            var11 = true;
         }

         this.constrainChildRect(var3, var6, var8, var9);
         int var12 = var6.left - var5.left;
         var8 = var6.top - var5.top;
         if (var12 != 0) {
            ViewCompat.offsetLeftAndRight(var1, var12);
         }

         if (var8 != 0) {
            ViewCompat.offsetTopAndBottom(var1, var8);
         }

         if (var11) {
            CoordinatorLayout.Behavior var10 = var3.getBehavior();
            if (var10 != null) {
               var10.onDependentViewChanged(this, var1, var3.mAnchorView);
            }
         }

         releaseTempRect(var4);
         releaseTempRect(var5);
         releaseTempRect(var6);
      }

   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.resetTouchBehaviors();
      if (this.mNeedsPreDrawListener) {
         if (this.mOnPreDrawListener == null) {
            this.mOnPreDrawListener = new CoordinatorLayout.OnPreDrawListener();
         }

         this.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
      }

      if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows(this)) {
         ViewCompat.requestApplyInsets(this);
      }

      this.mIsAttachedToWindow = true;
   }

   final void onChildViewsChanged(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      int var3 = this.mDependencySortedChildren.size();
      Rect var4 = acquireTempRect();
      Rect var5 = acquireTempRect();
      Rect var6 = acquireTempRect();

      for(int var7 = 0; var7 < var3; ++var7) {
         View var8 = (View)this.mDependencySortedChildren.get(var7);
         CoordinatorLayout.LayoutParams var9 = (CoordinatorLayout.LayoutParams)var8.getLayoutParams();
         if (var1 != 0 || var8.getVisibility() != 8) {
            int var10;
            View var11;
            for(var10 = 0; var10 < var7; ++var10) {
               var11 = (View)this.mDependencySortedChildren.get(var10);
               if (var9.mAnchorDirectChild == var11) {
                  this.offsetChildToAnchor(var8, var2);
               }
            }

            this.getChildRect(var8, true, var5);
            if (var9.insetEdge != 0 && !var5.isEmpty()) {
               int var12 = GravityCompat.getAbsoluteGravity(var9.insetEdge, var2);
               var10 = var12 & 112;
               if (var10 != 48) {
                  if (var10 == 80) {
                     var4.bottom = Math.max(var4.bottom, this.getHeight() - var5.top);
                  }
               } else {
                  var4.top = Math.max(var4.top, var5.bottom);
               }

               var10 = var12 & 7;
               if (var10 != 3) {
                  if (var10 == 5) {
                     var4.right = Math.max(var4.right, this.getWidth() - var5.left);
                  }
               } else {
                  var4.left = Math.max(var4.left, var5.right);
               }
            }

            if (var9.dodgeInsetEdges != 0 && var8.getVisibility() == 0) {
               this.offsetChildByInset(var8, var4, var2);
            }

            if (var1 != 2) {
               this.getLastChildRect(var8, var6);
               if (var6.equals(var5)) {
                  continue;
               }

               this.recordLastChildRect(var8, var5);
            }

            for(var10 = var7 + 1; var10 < var3; ++var10) {
               var11 = (View)this.mDependencySortedChildren.get(var10);
               CoordinatorLayout.LayoutParams var13 = (CoordinatorLayout.LayoutParams)var11.getLayoutParams();
               CoordinatorLayout.Behavior var15 = var13.getBehavior();
               if (var15 != null && var15.layoutDependsOn(this, var11, var8)) {
                  if (var1 == 0 && var13.getChangedAfterNestedScroll()) {
                     var13.resetChangedAfterNestedScroll();
                  } else {
                     boolean var14;
                     if (var1 != 2) {
                        var14 = var15.onDependentViewChanged(this, var11, var8);
                     } else {
                        var15.onDependentViewRemoved(this, var11, var8);
                        var14 = true;
                     }

                     if (var1 == 1) {
                        var13.setChangedAfterNestedScroll(var14);
                     }
                  }
               }
            }
         }
      }

      releaseTempRect(var4);
      releaseTempRect(var5);
      releaseTempRect(var6);
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.resetTouchBehaviors();
      if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
         this.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
      }

      if (this.mNestedScrollingTarget != null) {
         this.onStopNestedScroll(this.mNestedScrollingTarget);
      }

      this.mIsAttachedToWindow = false;
   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
         int var2;
         if (this.mLastInsets != null) {
            var2 = this.mLastInsets.getSystemWindowInsetTop();
         } else {
            var2 = 0;
         }

         if (var2 > 0) {
            this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), var2);
            this.mStatusBarBackground.draw(var1);
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.resetTouchBehaviors();
      }

      boolean var3 = this.performIntercept(var1, 0);
      if (var2 == 1 || var2 == 3) {
         this.resetTouchBehaviors();
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var4 = ViewCompat.getLayoutDirection(this);
      var3 = this.mDependencySortedChildren.size();

      for(var2 = 0; var2 < var3; ++var2) {
         View var6 = (View)this.mDependencySortedChildren.get(var2);
         if (var6.getVisibility() != 8) {
            CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)var6.getLayoutParams()).getBehavior();
            if (var7 == null || !var7.onLayoutChild(this, var6, var4)) {
               this.onLayoutChild(var6, var4);
            }
         }
      }

   }

   public void onLayoutChild(View var1, int var2) {
      CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var1.getLayoutParams();
      if (var3.checkAnchorChanged()) {
         throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
      } else {
         if (var3.mAnchorView != null) {
            this.layoutChildWithAnchor(var1, var3.mAnchorView, var2);
         } else if (var3.keyline >= 0) {
            this.layoutChildWithKeyline(var1, var3.keyline, var2);
         } else {
            this.layoutChild(var1, var2);
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      this.prepareChildren();
      this.ensurePreDrawListener();
      int var3 = this.getPaddingLeft();
      int var4 = this.getPaddingTop();
      int var5 = this.getPaddingRight();
      int var6 = this.getPaddingBottom();
      int var7 = ViewCompat.getLayoutDirection(this);
      boolean var8;
      if (var7 == 1) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var9 = MeasureSpec.getMode(var1);
      int var10 = MeasureSpec.getSize(var1);
      int var11 = MeasureSpec.getMode(var2);
      int var12 = MeasureSpec.getSize(var2);
      int var13 = this.getSuggestedMinimumWidth();
      int var14 = this.getSuggestedMinimumHeight();
      boolean var15;
      if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this)) {
         var15 = true;
      } else {
         var15 = false;
      }

      int var16 = this.mDependencySortedChildren.size();
      int var17 = 0;

      for(int var18 = 0; var18 < var16; ++var18) {
         View var19 = (View)this.mDependencySortedChildren.get(var18);
         if (var19.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var20;
            int var21;
            int var22;
            label74: {
               var20 = (CoordinatorLayout.LayoutParams)var19.getLayoutParams();
               if (var20.keyline >= 0 && var9 != 0) {
                  var21 = this.getKeyline(var20.keyline);
                  var22 = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(var20.gravity), var7) & 7;
                  if (var22 == 3 && !var8 || var22 == 5 && var8) {
                     var21 = Math.max(0, var10 - var5 - var21);
                     break label74;
                  }

                  if (var22 == 5 && !var8 || var22 == 3 && var8) {
                     var21 = Math.max(0, var21 - var3);
                     break label74;
                  }
               }

               var21 = 0;
            }

            int var23 = var17;
            int var24 = var14;
            if (var15 && !ViewCompat.getFitsSystemWindows(var19)) {
               int var25 = this.mLastInsets.getSystemWindowInsetLeft();
               var14 = this.mLastInsets.getSystemWindowInsetRight();
               var17 = this.mLastInsets.getSystemWindowInsetTop();
               var22 = this.mLastInsets.getSystemWindowInsetBottom();
               var14 = MeasureSpec.makeMeasureSpec(var10 - (var25 + var14), var9);
               var22 = MeasureSpec.makeMeasureSpec(var12 - (var17 + var22), var11);
               var17 = var14;
               var14 = var22;
            } else {
               var17 = var1;
               var14 = var2;
            }

            CoordinatorLayout.Behavior var26 = var20.getBehavior();
            if (var26 == null || !var26.onMeasureChild(this, var19, var17, var21, var14, 0)) {
               this.onMeasureChild(var19, var17, var21, var14, 0);
            }

            var13 = Math.max(var13, var3 + var5 + var19.getMeasuredWidth() + var20.leftMargin + var20.rightMargin);
            var14 = Math.max(var24, var4 + var6 + var19.getMeasuredHeight() + var20.topMargin + var20.bottomMargin);
            var17 = View.combineMeasuredStates(var23, var19.getMeasuredState());
         }
      }

      this.setMeasuredDimension(View.resolveSizeAndState(var13, var1, -16777216 & var17), View.resolveSizeAndState(var14, var2, var17 << 16));
   }

   public void onMeasureChild(View var1, int var2, int var3, int var4, int var5) {
      this.measureChildWithMargins(var1, var2, var3, var4, var5);
   }

   public boolean onNestedFling(View var1, float var2, float var3, boolean var4) {
      int var5 = this.getChildCount();
      int var6 = 0;

      int var7;
      int var9;
      for(var7 = var6; var6 < var5; var7 = var9) {
         View var8 = this.getChildAt(var6);
         if (var8.getVisibility() == 8) {
            var9 = var7;
         } else {
            CoordinatorLayout.LayoutParams var10 = (CoordinatorLayout.LayoutParams)var8.getLayoutParams();
            if (!var10.isNestedScrollAccepted(0)) {
               var9 = var7;
            } else {
               CoordinatorLayout.Behavior var11 = var10.getBehavior();
               var9 = var7;
               if (var11 != null) {
                  var9 = var7 | var11.onNestedFling(this, var8, var1, var2, var3, var4);
               }
            }
         }

         ++var6;
      }

      if (var7 != 0) {
         this.onChildViewsChanged(1);
      }

      return (boolean)var7;
   }

   public boolean onNestedPreFling(View var1, float var2, float var3) {
      int var4 = this.getChildCount();
      int var5 = 0;

      int var6;
      int var8;
      for(var6 = var5; var5 < var4; var6 = var8) {
         View var7 = this.getChildAt(var5);
         if (var7.getVisibility() == 8) {
            var8 = var6;
         } else {
            CoordinatorLayout.LayoutParams var9 = (CoordinatorLayout.LayoutParams)var7.getLayoutParams();
            if (!var9.isNestedScrollAccepted(0)) {
               var8 = var6;
            } else {
               CoordinatorLayout.Behavior var10 = var9.getBehavior();
               var8 = var6;
               if (var10 != null) {
                  var8 = var6 | var10.onNestedPreFling(this, var7, var1, var2, var3);
               }
            }
         }

         ++var5;
      }

      return (boolean)var6;
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4) {
      this.onNestedPreScroll(var1, var2, var3, var4, 0);
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4, int var5) {
      int var6 = this.getChildCount();
      byte var7 = 0;
      int var10 = var7;
      int var11 = var7;

      int var8;
      for(int var12 = var7; var12 < var6; var10 = var8) {
         View var13 = this.getChildAt(var12);
         int var9;
         if (var13.getVisibility() == 8) {
            var9 = var11;
            var8 = var10;
         } else {
            CoordinatorLayout.LayoutParams var14 = (CoordinatorLayout.LayoutParams)var13.getLayoutParams();
            if (!var14.isNestedScrollAccepted(var5)) {
               var9 = var11;
               var8 = var10;
            } else {
               CoordinatorLayout.Behavior var16 = var14.getBehavior();
               var9 = var11;
               var8 = var10;
               if (var16 != null) {
                  int[] var15 = this.mTempIntPair;
                  this.mTempIntPair[1] = 0;
                  var15[0] = 0;
                  var16.onNestedPreScroll(this, var13, var1, var2, var3, this.mTempIntPair, var5);
                  if (var2 > 0) {
                     var8 = Math.max(var11, this.mTempIntPair[0]);
                  } else {
                     var8 = Math.min(var11, this.mTempIntPair[0]);
                  }

                  if (var3 > 0) {
                     var9 = Math.max(var10, this.mTempIntPair[1]);
                  } else {
                     var9 = Math.min(var10, this.mTempIntPair[1]);
                  }

                  var10 = var8;
                  var8 = var9;
                  var7 = 1;
                  var9 = var10;
               }
            }
         }

         ++var12;
         var11 = var9;
      }

      var4[0] = var11;
      var4[1] = var10;
      if (var7 != 0) {
         this.onChildViewsChanged(1);
      }

   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5) {
      this.onNestedScroll(var1, var2, var3, var4, var5, 0);
   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = this.getChildCount();
      boolean var8 = false;

      for(int var9 = 0; var9 < var7; ++var9) {
         View var10 = this.getChildAt(var9);
         if (var10.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var11 = (CoordinatorLayout.LayoutParams)var10.getLayoutParams();
            if (var11.isNestedScrollAccepted(var6)) {
               CoordinatorLayout.Behavior var12 = var11.getBehavior();
               if (var12 != null) {
                  var12.onNestedScroll(this, var10, var1, var2, var3, var4, var5, var6);
                  var8 = true;
               }
            }
         }
      }

      if (var8) {
         this.onChildViewsChanged(1);
      }

   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.onNestedScrollAccepted(var1, var2, var3, 0);
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3, int var4) {
      this.mNestedScrollingParentHelper.onNestedScrollAccepted(var1, var2, var3, var4);
      this.mNestedScrollingTarget = var2;
      int var5 = this.getChildCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = this.getChildAt(var6);
         CoordinatorLayout.LayoutParams var8 = (CoordinatorLayout.LayoutParams)var7.getLayoutParams();
         if (var8.isNestedScrollAccepted(var4)) {
            CoordinatorLayout.Behavior var9 = var8.getBehavior();
            if (var9 != null) {
               var9.onNestedScrollAccepted(this, var7, var1, var2, var3, var4);
            }
         }
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof CoordinatorLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         CoordinatorLayout.SavedState var8 = (CoordinatorLayout.SavedState)var1;
         super.onRestoreInstanceState(var8.getSuperState());
         SparseArray var2 = var8.behaviorStates;
         int var3 = 0;

         for(int var4 = this.getChildCount(); var3 < var4; ++var3) {
            View var5 = this.getChildAt(var3);
            int var6 = var5.getId();
            CoordinatorLayout.Behavior var9 = this.getResolvedLayoutParams(var5).getBehavior();
            if (var6 != -1 && var9 != null) {
               Parcelable var7 = (Parcelable)var2.get(var6);
               if (var7 != null) {
                  var9.onRestoreInstanceState(this, var5, var7);
               }
            }
         }

      }
   }

   protected Parcelable onSaveInstanceState() {
      CoordinatorLayout.SavedState var1 = new CoordinatorLayout.SavedState(super.onSaveInstanceState());
      SparseArray var2 = new SparseArray();
      int var3 = this.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = this.getChildAt(var4);
         int var6 = var5.getId();
         CoordinatorLayout.Behavior var7 = ((CoordinatorLayout.LayoutParams)var5.getLayoutParams()).getBehavior();
         if (var6 != -1 && var7 != null) {
            Parcelable var8 = var7.onSaveInstanceState(this, var5);
            if (var8 != null) {
               var2.append(var6, var8);
            }
         }
      }

      var1.behaviorStates = var2;
      return var1;
   }

   public boolean onStartNestedScroll(View var1, View var2, int var3) {
      return this.onStartNestedScroll(var1, var2, var3, 0);
   }

   public boolean onStartNestedScroll(View var1, View var2, int var3, int var4) {
      int var5 = this.getChildCount();
      int var6 = 0;

      int var7;
      for(var7 = var6; var6 < var5; ++var6) {
         View var8 = this.getChildAt(var6);
         if (var8.getVisibility() != 8) {
            CoordinatorLayout.LayoutParams var9 = (CoordinatorLayout.LayoutParams)var8.getLayoutParams();
            CoordinatorLayout.Behavior var10 = var9.getBehavior();
            if (var10 != null) {
               byte var11 = var10.onStartNestedScroll(this, var8, var1, var2, var3, var4);
               var9.setNestedScrollAccepted(var4, (boolean)var11);
               var7 |= var11;
            } else {
               var9.setNestedScrollAccepted(var4, false);
            }
         }
      }

      return (boolean)var7;
   }

   public void onStopNestedScroll(View var1) {
      this.onStopNestedScroll(var1, 0);
   }

   public void onStopNestedScroll(View var1, int var2) {
      this.mNestedScrollingParentHelper.onStopNestedScroll(var1, var2);
      int var3 = this.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = this.getChildAt(var4);
         CoordinatorLayout.LayoutParams var6 = (CoordinatorLayout.LayoutParams)var5.getLayoutParams();
         if (var6.isNestedScrollAccepted(var2)) {
            CoordinatorLayout.Behavior var7 = var6.getBehavior();
            if (var7 != null) {
               var7.onStopNestedScroll(this, var5, var1, var2);
            }

            var6.resetNestedScroll(var2);
            var6.resetChangedAfterNestedScroll();
         }
      }

      this.mNestedScrollingTarget = null;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2;
      View var3;
      boolean var5;
      boolean var6;
      boolean var7;
      label34: {
         var2 = var1.getActionMasked();
         var3 = this.mBehaviorTouchView;
         boolean var4 = false;
         if (var3 == null) {
            var5 = this.performIntercept(var1, 1);
            var6 = var5;
            var7 = var4;
            if (!var5) {
               break label34;
            }
         } else {
            var5 = false;
         }

         CoordinatorLayout.Behavior var11 = ((CoordinatorLayout.LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
         var6 = var5;
         var7 = var4;
         if (var11 != null) {
            var7 = var11.onTouchEvent(this, this.mBehaviorTouchView, var1);
            var6 = var5;
         }
      }

      View var8 = this.mBehaviorTouchView;
      var3 = null;
      if (var8 == null) {
         var5 = var7 | super.onTouchEvent(var1);
         var1 = var3;
      } else {
         var5 = var7;
         var1 = var3;
         if (var6) {
            long var9 = SystemClock.uptimeMillis();
            var1 = MotionEvent.obtain(var9, var9, 3, 0.0F, 0.0F, 0);
            super.onTouchEvent(var1);
            var5 = var7;
         }
      }

      if (var1 != null) {
         var1.recycle();
      }

      if (var2 == 1 || var2 == 3) {
         this.resetTouchBehaviors();
      }

      return var5;
   }

   void recordLastChildRect(View var1, Rect var2) {
      ((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).setLastChildRect(var2);
   }

   void removePreDrawListener() {
      if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null) {
         this.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
      }

      this.mNeedsPreDrawListener = false;
   }

   public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
      CoordinatorLayout.Behavior var4 = ((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).getBehavior();
      return var4 != null && var4.onRequestChildRectangleOnScreen(this, var1, var2, var3) ? true : super.requestChildRectangleOnScreen(var1, var2, var3);
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      super.requestDisallowInterceptTouchEvent(var1);
      if (var1 && !this.mDisallowInterceptReset) {
         this.resetTouchBehaviors();
         this.mDisallowInterceptReset = true;
      }

   }

   public void setFitsSystemWindows(boolean var1) {
      super.setFitsSystemWindows(var1);
      this.setupForInsets();
   }

   public void setOnHierarchyChangeListener(OnHierarchyChangeListener var1) {
      this.mOnHierarchyChangeListener = var1;
   }

   public void setStatusBarBackground(@Nullable Drawable var1) {
      if (this.mStatusBarBackground != var1) {
         Drawable var2 = this.mStatusBarBackground;
         Drawable var3 = null;
         if (var2 != null) {
            this.mStatusBarBackground.setCallback((Callback)null);
         }

         if (var1 != null) {
            var3 = var1.mutate();
         }

         this.mStatusBarBackground = var3;
         if (this.mStatusBarBackground != null) {
            if (this.mStatusBarBackground.isStateful()) {
               this.mStatusBarBackground.setState(this.getDrawableState());
            }

            DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection(this));
            var1 = this.mStatusBarBackground;
            boolean var4;
            if (this.getVisibility() == 0) {
               var4 = true;
            } else {
               var4 = false;
            }

            var1.setVisible(var4, false);
            this.mStatusBarBackground.setCallback(this);
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void setStatusBarBackgroundColor(@ColorInt int var1) {
      this.setStatusBarBackground(new ColorDrawable(var1));
   }

   public void setStatusBarBackgroundResource(@DrawableRes int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setStatusBarBackground(var2);
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.mStatusBarBackground != null && this.mStatusBarBackground.isVisible() != var2) {
         this.mStatusBarBackground.setVisible(var2, false);
      }

   }

   final WindowInsetsCompat setWindowInsets(WindowInsetsCompat var1) {
      WindowInsetsCompat var2 = var1;
      if (!ObjectsCompat.equals(this.mLastInsets, var1)) {
         this.mLastInsets = var1;
         boolean var3 = false;
         boolean var4;
         if (var1 != null && var1.getSystemWindowInsetTop() > 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.mDrawStatusBarBackground = var4;
         var4 = var3;
         if (!this.mDrawStatusBarBackground) {
            var4 = var3;
            if (this.getBackground() == null) {
               var4 = true;
            }
         }

         this.setWillNotDraw(var4);
         var2 = this.dispatchApplyWindowInsetsToBehaviors(var1);
         this.requestLayout();
      }

      return var2;
   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if (!super.verifyDrawable(var1) && var1 != this.mStatusBarBackground) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public abstract static class Behavior {
      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
      }

      public static Object getTag(View var0) {
         return ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).mBehaviorTag;
      }

      public static void setTag(View var0, Object var1) {
         ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).mBehaviorTag = var1;
      }

      public boolean blocksInteractionBelow(CoordinatorLayout var1, View var2) {
         boolean var3;
         if (this.getScrimOpacity(var1, var2) > 0.0F) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public boolean getInsetDodgeRect(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull Rect var3) {
         return false;
      }

      @ColorInt
      public int getScrimColor(CoordinatorLayout var1, View var2) {
         return -16777216;
      }

      @FloatRange(
         from = 0.0D,
         to = 1.0D
      )
      public float getScrimOpacity(CoordinatorLayout var1, View var2) {
         return 0.0F;
      }

      public boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3) {
         return false;
      }

      @NonNull
      public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout var1, View var2, WindowInsetsCompat var3) {
         return var3;
      }

      public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams var1) {
      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, View var2, View var3) {
         return false;
      }

      public void onDependentViewRemoved(CoordinatorLayout var1, View var2, View var3) {
      }

      public void onDetachedFromLayoutParams() {
      }

      public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         return false;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
         return false;
      }

      public boolean onMeasureChild(CoordinatorLayout var1, View var2, int var3, int var4, int var5, int var6) {
         return false;
      }

      public boolean onNestedFling(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, float var4, float var5, boolean var6) {
         return false;
      }

      public boolean onNestedPreFling(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, float var4, float var5) {
         return false;
      }

      @Deprecated
      public void onNestedPreScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, int var4, int var5, @NonNull int[] var6) {
      }

      public void onNestedPreScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, int var4, int var5, @NonNull int[] var6, int var7) {
         if (var7 == 0) {
            this.onNestedPreScroll(var1, var2, var3, var4, var5, var6);
         }

      }

      @Deprecated
      public void onNestedScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, int var4, int var5, int var6, int var7) {
      }

      public void onNestedScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, int var4, int var5, int var6, int var7, int var8) {
         if (var8 == 0) {
            this.onNestedScroll(var1, var2, var3, var4, var5, var6, var7);
         }

      }

      @Deprecated
      public void onNestedScrollAccepted(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, @NonNull View var4, int var5) {
      }

      public void onNestedScrollAccepted(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, @NonNull View var4, int var5, int var6) {
         if (var6 == 0) {
            this.onNestedScrollAccepted(var1, var2, var3, var4, var5);
         }

      }

      public boolean onRequestChildRectangleOnScreen(CoordinatorLayout var1, View var2, Rect var3, boolean var4) {
         return false;
      }

      public void onRestoreInstanceState(CoordinatorLayout var1, View var2, Parcelable var3) {
      }

      public Parcelable onSaveInstanceState(CoordinatorLayout var1, View var2) {
         return BaseSavedState.EMPTY_STATE;
      }

      @Deprecated
      public boolean onStartNestedScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, @NonNull View var4, int var5) {
         return false;
      }

      public boolean onStartNestedScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, @NonNull View var4, int var5, int var6) {
         return var6 == 0 ? this.onStartNestedScroll(var1, var2, var3, var4, var5) : false;
      }

      @Deprecated
      public void onStopNestedScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3) {
      }

      public void onStopNestedScroll(@NonNull CoordinatorLayout var1, @NonNull View var2, @NonNull View var3, int var4) {
         if (var4 == 0) {
            this.onStopNestedScroll(var1, var2, var3);
         }

      }

      public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         return false;
      }
   }

   @Retention(RetentionPolicy.RUNTIME)
   public @interface DefaultBehavior {
      Class value();
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface DispatchChangeEvent {
   }

   private class HierarchyChangeListener implements OnHierarchyChangeListener {
      HierarchyChangeListener() {
      }

      public void onChildViewAdded(View var1, View var2) {
         if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
            CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(var1, var2);
         }

      }

      public void onChildViewRemoved(View var1, View var2) {
         CoordinatorLayout.this.onChildViewsChanged(2);
         if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
            CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(var1, var2);
         }

      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      public int anchorGravity = 0;
      public int dodgeInsetEdges = 0;
      public int gravity = 0;
      public int insetEdge = 0;
      public int keyline = -1;
      View mAnchorDirectChild;
      int mAnchorId = -1;
      View mAnchorView;
      CoordinatorLayout.Behavior mBehavior;
      boolean mBehaviorResolved = false;
      Object mBehaviorTag;
      private boolean mDidAcceptNestedScrollNonTouch;
      private boolean mDidAcceptNestedScrollTouch;
      private boolean mDidBlockInteraction;
      private boolean mDidChangeAfterNestedScroll;
      int mInsetOffsetX;
      int mInsetOffsetY;
      final Rect mLastChildRect = new Rect();

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.CoordinatorLayout_Layout);
         this.gravity = var3.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
         this.mAnchorId = var3.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
         this.anchorGravity = var3.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
         this.keyline = var3.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
         this.insetEdge = var3.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
         this.dodgeInsetEdges = var3.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
         this.mBehaviorResolved = var3.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
         if (this.mBehaviorResolved) {
            this.mBehavior = CoordinatorLayout.parseBehavior(var1, var2, var3.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior));
         }

         var3.recycle();
         if (this.mBehavior != null) {
            this.mBehavior.onAttachedToLayoutParams(this);
         }

      }

      public LayoutParams(CoordinatorLayout.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      private void resolveAnchorView(View var1, CoordinatorLayout var2) {
         this.mAnchorView = var2.findViewById(this.mAnchorId);
         if (this.mAnchorView == null) {
            if (var2.isInEditMode()) {
               this.mAnchorDirectChild = null;
               this.mAnchorView = null;
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append("Could not find CoordinatorLayout descendant view with id ");
               var5.append(var2.getResources().getResourceName(this.mAnchorId));
               var5.append(" to anchor view ");
               var5.append(var1);
               throw new IllegalStateException(var5.toString());
            }
         } else if (this.mAnchorView == var2) {
            if (var2.isInEditMode()) {
               this.mAnchorDirectChild = null;
               this.mAnchorView = null;
            } else {
               throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
            }
         } else {
            View var3 = this.mAnchorView;

            for(ViewParent var4 = this.mAnchorView.getParent(); var4 != var2 && var4 != null; var4 = var4.getParent()) {
               if (var4 == var1) {
                  if (var2.isInEditMode()) {
                     this.mAnchorDirectChild = null;
                     this.mAnchorView = null;
                     return;
                  }

                  throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
               }

               if (var4 instanceof View) {
                  var3 = (View)var4;
               }
            }

            this.mAnchorDirectChild = var3;
         }
      }

      private boolean shouldDodge(View var1, int var2) {
         int var3 = GravityCompat.getAbsoluteGravity(((CoordinatorLayout.LayoutParams)var1.getLayoutParams()).insetEdge, var2);
         boolean var4;
         if (var3 != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, var2) & var3) == var3) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      private boolean verifyAnchorView(View var1, CoordinatorLayout var2) {
         if (this.mAnchorView.getId() != this.mAnchorId) {
            return false;
         } else {
            View var3 = this.mAnchorView;

            for(ViewParent var4 = this.mAnchorView.getParent(); var4 != var2; var4 = var4.getParent()) {
               if (var4 == null || var4 == var1) {
                  this.mAnchorDirectChild = null;
                  this.mAnchorView = null;
                  return false;
               }

               if (var4 instanceof View) {
                  var3 = (View)var4;
               }
            }

            this.mAnchorDirectChild = var3;
            return true;
         }
      }

      boolean checkAnchorChanged() {
         boolean var1;
         if (this.mAnchorView == null && this.mAnchorId != -1) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean dependsOn(CoordinatorLayout var1, View var2, View var3) {
         boolean var4;
         if (var3 == this.mAnchorDirectChild || this.shouldDodge(var3, ViewCompat.getLayoutDirection(var1)) || this.mBehavior != null && this.mBehavior.layoutDependsOn(var1, var2, var3)) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      boolean didBlockInteraction() {
         if (this.mBehavior == null) {
            this.mDidBlockInteraction = false;
         }

         return this.mDidBlockInteraction;
      }

      View findAnchorView(CoordinatorLayout var1, View var2) {
         if (this.mAnchorId == -1) {
            this.mAnchorDirectChild = null;
            this.mAnchorView = null;
            return null;
         } else {
            if (this.mAnchorView == null || !this.verifyAnchorView(var2, var1)) {
               this.resolveAnchorView(var2, var1);
            }

            return this.mAnchorView;
         }
      }

      @IdRes
      public int getAnchorId() {
         return this.mAnchorId;
      }

      @Nullable
      public CoordinatorLayout.Behavior getBehavior() {
         return this.mBehavior;
      }

      boolean getChangedAfterNestedScroll() {
         return this.mDidChangeAfterNestedScroll;
      }

      Rect getLastChildRect() {
         return this.mLastChildRect;
      }

      void invalidateAnchor() {
         this.mAnchorDirectChild = null;
         this.mAnchorView = null;
      }

      boolean isBlockingInteractionBelow(CoordinatorLayout var1, View var2) {
         if (this.mDidBlockInteraction) {
            return true;
         } else {
            boolean var3 = this.mDidBlockInteraction;
            boolean var4;
            if (this.mBehavior != null) {
               var4 = this.mBehavior.blocksInteractionBelow(var1, var2);
            } else {
               var4 = false;
            }

            var4 |= var3;
            this.mDidBlockInteraction = var4;
            return var4;
         }
      }

      boolean isNestedScrollAccepted(int var1) {
         switch(var1) {
         case 0:
            return this.mDidAcceptNestedScrollTouch;
         case 1:
            return this.mDidAcceptNestedScrollNonTouch;
         default:
            return false;
         }
      }

      void resetChangedAfterNestedScroll() {
         this.mDidChangeAfterNestedScroll = false;
      }

      void resetNestedScroll(int var1) {
         this.setNestedScrollAccepted(var1, false);
      }

      void resetTouchBehaviorTracking() {
         this.mDidBlockInteraction = false;
      }

      public void setAnchorId(@IdRes int var1) {
         this.invalidateAnchor();
         this.mAnchorId = var1;
      }

      public void setBehavior(@Nullable CoordinatorLayout.Behavior var1) {
         if (this.mBehavior != var1) {
            if (this.mBehavior != null) {
               this.mBehavior.onDetachedFromLayoutParams();
            }

            this.mBehavior = var1;
            this.mBehaviorTag = null;
            this.mBehaviorResolved = true;
            if (var1 != null) {
               var1.onAttachedToLayoutParams(this);
            }
         }

      }

      void setChangedAfterNestedScroll(boolean var1) {
         this.mDidChangeAfterNestedScroll = var1;
      }

      void setLastChildRect(Rect var1) {
         this.mLastChildRect.set(var1);
      }

      void setNestedScrollAccepted(int var1, boolean var2) {
         switch(var1) {
         case 0:
            this.mDidAcceptNestedScrollTouch = var2;
            break;
         case 1:
            this.mDidAcceptNestedScrollNonTouch = var2;
         }

      }
   }

   class OnPreDrawListener implements android.view.ViewTreeObserver.OnPreDrawListener {
      public boolean onPreDraw() {
         CoordinatorLayout.this.onChildViewsChanged(0);
         return true;
      }
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public CoordinatorLayout.SavedState createFromParcel(Parcel var1) {
            return new CoordinatorLayout.SavedState(var1, (ClassLoader)null);
         }

         public CoordinatorLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new CoordinatorLayout.SavedState(var1, var2);
         }

         public CoordinatorLayout.SavedState[] newArray(int var1) {
            return new CoordinatorLayout.SavedState[var1];
         }
      };
      SparseArray behaviorStates;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         int var3 = var1.readInt();
         int[] var4 = new int[var3];
         var1.readIntArray(var4);
         Parcelable[] var6 = var1.readParcelableArray(var2);
         this.behaviorStates = new SparseArray(var3);

         for(int var5 = 0; var5 < var3; ++var5) {
            this.behaviorStates.append(var4[var5], var6[var5]);
         }

      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         SparseArray var3 = this.behaviorStates;
         int var4 = 0;
         int var5;
         if (var3 != null) {
            var5 = this.behaviorStates.size();
         } else {
            var5 = 0;
         }

         var1.writeInt(var5);
         int[] var7 = new int[var5];

         Parcelable[] var6;
         for(var6 = new Parcelable[var5]; var4 < var5; ++var4) {
            var7[var4] = this.behaviorStates.keyAt(var4);
            var6[var4] = (Parcelable)this.behaviorStates.valueAt(var4);
         }

         var1.writeIntArray(var7);
         var1.writeParcelableArray(var6, var2);
      }
   }

   static class ViewElevationComparator implements Comparator {
      public int compare(View var1, View var2) {
         float var3 = ViewCompat.getZ(var1);
         float var4 = ViewCompat.getZ(var2);
         if (var3 > var4) {
            return -1;
         } else {
            return var3 < var4 ? 1 : 0;
         }
      }
   }
}
