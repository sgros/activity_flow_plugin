package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2, NestedScrollingChild3 {
   static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
   static final boolean ALLOW_THREAD_GAP_WORK;
   private static final int[] CLIP_TO_PADDING_ATTR = new int[]{16842987};
   static final boolean DEBUG = false;
   static final int DEFAULT_ORIENTATION = 1;
   static final boolean DISPATCH_TEMP_DETACH = false;
   private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
   static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
   static final long FOREVER_NS = Long.MAX_VALUE;
   public static final int HORIZONTAL = 0;
   private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
   private static final int INVALID_POINTER = -1;
   public static final int INVALID_TYPE = -1;
   private static final Class[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
   static final int MAX_SCROLL_DURATION = 2000;
   private static final int[] NESTED_SCROLLING_ATTRS = new int[]{16843830};
   public static final long NO_ID = -1L;
   public static final int NO_POSITION = -1;
   static final boolean POST_UPDATES_ON_ANIMATION;
   public static final int SCROLL_STATE_DRAGGING = 1;
   public static final int SCROLL_STATE_IDLE = 0;
   public static final int SCROLL_STATE_SETTLING = 2;
   static final String TAG = "RecyclerView";
   public static final int TOUCH_SLOP_DEFAULT = 0;
   public static final int TOUCH_SLOP_PAGING = 1;
   static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
   static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
   private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
   static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
   private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
   private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
   static final String TRACE_PREFETCH_TAG = "RV Prefetch";
   static final String TRACE_SCROLL_TAG = "RV Scroll";
   static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
   static final boolean VERBOSE_TRACING = false;
   public static final int VERTICAL = 1;
   static final Interpolator sQuinticInterpolator;
   private int bottomGlowOffset;
   private int glowColor;
   RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
   private final AccessibilityManager mAccessibilityManager;
   RecyclerView.Adapter mAdapter;
   AdapterHelper mAdapterHelper;
   boolean mAdapterUpdateDuringMeasure;
   private EdgeEffect mBottomGlow;
   private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback;
   ChildHelper mChildHelper;
   boolean mClipToPadding;
   boolean mDataSetHasChangedAfterLayout;
   boolean mDispatchItemsChangedEvent;
   private int mDispatchScrollCounter;
   private int mEatenAccessibilityChangeFlags;
   private RecyclerView.EdgeEffectFactory mEdgeEffectFactory;
   boolean mEnableFastScroller;
   boolean mFirstLayoutComplete;
   GapWorker mGapWorker;
   boolean mHasFixedSize;
   private boolean mIgnoreMotionEventTillDown;
   private int mInitialTouchX;
   private int mInitialTouchY;
   private int mInterceptRequestLayoutDepth;
   private RecyclerView.OnItemTouchListener mInterceptingOnItemTouchListener;
   boolean mIsAttached;
   RecyclerView.ItemAnimator mItemAnimator;
   private RecyclerView.ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
   private Runnable mItemAnimatorRunner;
   final ArrayList mItemDecorations;
   boolean mItemsAddedOrRemoved;
   boolean mItemsChanged;
   private int mLastTouchX;
   private int mLastTouchY;
   RecyclerView.LayoutManager mLayout;
   private int mLayoutOrScrollCounter;
   boolean mLayoutSuppressed;
   boolean mLayoutWasDefered;
   private EdgeEffect mLeftGlow;
   private final int mMaxFlingVelocity;
   private final int mMinFlingVelocity;
   private final int[] mMinMaxLayoutPositions;
   private final int[] mNestedOffsets;
   private final RecyclerView.RecyclerViewDataObserver mObserver;
   private List mOnChildAttachStateListeners;
   private RecyclerView.OnFlingListener mOnFlingListener;
   private final ArrayList mOnItemTouchListeners;
   final List mPendingAccessibilityImportanceChange;
   private RecyclerView.SavedState mPendingSavedState;
   boolean mPostedAnimatorRunner;
   GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
   private boolean mPreserveFocusAfterLayout;
   final RecyclerView.Recycler mRecycler;
   RecyclerView.RecyclerListener mRecyclerListener;
   final int[] mReusableIntPair;
   private EdgeEffect mRightGlow;
   private float mScaledHorizontalScrollFactor;
   private float mScaledVerticalScrollFactor;
   private RecyclerView.OnScrollListener mScrollListener;
   private List mScrollListeners;
   private final int[] mScrollOffset;
   private int mScrollPointerId;
   private int mScrollState;
   private NestedScrollingChildHelper mScrollingChildHelper;
   final RecyclerView.State mState;
   final Rect mTempRect;
   private final Rect mTempRect2;
   final RectF mTempRectF;
   private EdgeEffect mTopGlow;
   private int mTouchSlop;
   final Runnable mUpdateChildViewsRunnable;
   private VelocityTracker mVelocityTracker;
   final RecyclerView.ViewFlinger mViewFlinger;
   private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
   final ViewInfoStore mViewInfoStore;
   private int topGlowOffset;

   static {
      int var0 = VERSION.SDK_INT;
      boolean var1;
      if (var0 != 18 && var0 != 19 && var0 != 20) {
         var1 = false;
      } else {
         var1 = true;
      }

      FORCE_INVALIDATE_DISPLAY_LIST = var1;
      if (VERSION.SDK_INT >= 23) {
         var1 = true;
      } else {
         var1 = false;
      }

      ALLOW_SIZE_IN_UNSPECIFIED_SPEC = var1;
      if (VERSION.SDK_INT >= 16) {
         var1 = true;
      } else {
         var1 = false;
      }

      POST_UPDATES_ON_ANIMATION = var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = true;
      } else {
         var1 = false;
      }

      ALLOW_THREAD_GAP_WORK = var1;
      if (VERSION.SDK_INT <= 15) {
         var1 = true;
      } else {
         var1 = false;
      }

      FORCE_ABS_FOCUS_SEARCH_DIRECTION = var1;
      if (VERSION.SDK_INT <= 15) {
         var1 = true;
      } else {
         var1 = false;
      }

      IGNORE_DETACHED_FOCUSED_CHILD = var1;
      Class var2 = Integer.TYPE;
      LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, var2, var2};
      sQuinticInterpolator = new Interpolator() {
         public float getInterpolation(float var1) {
            --var1;
            return var1 * var1 * var1 * var1 * var1 + 1.0F;
         }
      };
   }

   public RecyclerView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public RecyclerView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public RecyclerView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mObserver = new RecyclerView.RecyclerViewDataObserver();
      this.mRecycler = new RecyclerView.Recycler();
      this.mViewInfoStore = new ViewInfoStore();
      this.mUpdateChildViewsRunnable = new Runnable() {
         public void run() {
            RecyclerView var1 = RecyclerView.this;
            if (var1.mFirstLayoutComplete && !var1.isLayoutRequested()) {
               var1 = RecyclerView.this;
               if (!var1.mIsAttached) {
                  var1.requestLayout();
                  return;
               }

               if (var1.mLayoutSuppressed) {
                  var1.mLayoutWasDefered = true;
                  return;
               }

               var1.consumePendingUpdateOperations();
            }

         }
      };
      this.mTempRect = new Rect();
      this.mTempRect2 = new Rect();
      this.mTempRectF = new RectF();
      this.mItemDecorations = new ArrayList();
      this.mOnItemTouchListeners = new ArrayList();
      boolean var4 = false;
      this.mInterceptRequestLayoutDepth = 0;
      this.mDataSetHasChangedAfterLayout = false;
      this.mDispatchItemsChangedEvent = false;
      this.mLayoutOrScrollCounter = 0;
      this.mDispatchScrollCounter = 0;
      this.mEdgeEffectFactory = new RecyclerView.EdgeEffectFactory();
      this.mItemAnimator = new DefaultItemAnimator();
      this.mScrollState = 0;
      this.mScrollPointerId = -1;
      this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
      this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
      this.mPreserveFocusAfterLayout = true;
      this.mViewFlinger = new RecyclerView.ViewFlinger();
      GapWorker.LayoutPrefetchRegistryImpl var5;
      if (ALLOW_THREAD_GAP_WORK) {
         var5 = new GapWorker.LayoutPrefetchRegistryImpl();
      } else {
         var5 = null;
      }

      this.mPrefetchRegistry = var5;
      this.mState = new RecyclerView.State();
      this.mItemsAddedOrRemoved = false;
      this.mItemsChanged = false;
      this.mItemAnimatorListener = new RecyclerView.ItemAnimatorRestoreListener();
      this.mPostedAnimatorRunner = false;
      this.mMinMaxLayoutPositions = new int[2];
      this.mScrollOffset = new int[2];
      this.mNestedOffsets = new int[2];
      this.topGlowOffset = 0;
      this.bottomGlowOffset = 0;
      this.glowColor = 0;
      this.mReusableIntPair = new int[2];
      this.mPendingAccessibilityImportanceChange = new ArrayList();
      this.mItemAnimatorRunner = new Runnable() {
         public void run() {
            RecyclerView.ItemAnimator var1 = RecyclerView.this.mItemAnimator;
            if (var1 != null) {
               var1.runPendingAnimations();
            }

            RecyclerView.this.mPostedAnimatorRunner = false;
         }
      };
      this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
         public void processAppeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
            RecyclerView.this.animateAppearance(var1, var2, var3);
         }

         public void processDisappeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
            RecyclerView.this.mRecycler.unscrapView(var1);
            RecyclerView.this.animateDisappearance(var1, var2, var3);
         }

         public void processPersistent(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
            var1.setIsRecyclable(false);
            RecyclerView var4 = RecyclerView.this;
            if (var4.mDataSetHasChangedAfterLayout) {
               if (var4.mItemAnimator.animateChange(var1, var1, var2, var3)) {
                  RecyclerView.this.postAnimationRunner();
               }
            } else if (var4.mItemAnimator.animatePersistence(var1, var2, var3)) {
               RecyclerView.this.postAnimationRunner();
            }

         }

         public void unused(RecyclerView.ViewHolder var1) {
            RecyclerView var2 = RecyclerView.this;
            var2.mLayout.removeAndRecycleView(var1.itemView, var2.mRecycler);
         }
      };
      if (var2 != null) {
         TypedArray var6 = var1.obtainStyledAttributes(var2, CLIP_TO_PADDING_ATTR, var3, 0);
         this.mClipToPadding = var6.getBoolean(0, true);
         var6.recycle();
      } else {
         this.mClipToPadding = true;
      }

      this.setScrollContainer(true);
      this.setFocusableInTouchMode(true);
      ViewConfiguration var7 = ViewConfiguration.get(var1);
      this.mTouchSlop = var7.getScaledTouchSlop();
      this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(var7, var1);
      this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(var7, var1);
      this.mMinFlingVelocity = var7.getScaledMinimumFlingVelocity();
      this.mMaxFlingVelocity = var7.getScaledMaximumFlingVelocity();
      if (this.getOverScrollMode() == 2) {
         var4 = true;
      }

      this.setWillNotDraw(var4);
      this.mItemAnimator.setListener(this.mItemAnimatorListener);
      this.initAdapterManager();
      this.initChildrenHelper();
      this.initAutofill();
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
      this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
      this.setDescendantFocusability(262144);
      this.setNestedScrollingEnabled(true);
   }

   private void addAnimatingView(RecyclerView.ViewHolder var1) {
      View var2 = var1.itemView;
      boolean var3;
      if (var2.getParent() == this) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.mRecycler.unscrapView(this.getChildViewHolder(var2));
      if (var1.isTmpDetached()) {
         this.mChildHelper.attachViewToParent(var2, -1, var2.getLayoutParams(), true);
      } else if (!var3) {
         this.mChildHelper.addView(var2, true);
      } else {
         this.mChildHelper.hide(var2);
      }

   }

   private void animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, RecyclerView.ItemAnimator.ItemHolderInfo var3, RecyclerView.ItemAnimator.ItemHolderInfo var4, boolean var5, boolean var6) {
      var1.setIsRecyclable(false);
      if (var5) {
         this.addAnimatingView(var1);
      }

      if (var1 != var2) {
         if (var6) {
            this.addAnimatingView(var2);
         }

         var1.mShadowedHolder = var2;
         this.addAnimatingView(var1);
         this.mRecycler.unscrapView(var1);
         var2.setIsRecyclable(false);
         var2.mShadowingHolder = var1;
      }

      if (this.mItemAnimator.animateChange(var1, var2, var3, var4)) {
         this.postAnimationRunner();
      }

   }

   private void cancelScroll() {
      this.resetScroll();
      this.setScrollState(0);
   }

   static void clearNestedRecyclerViewIfNotNested(RecyclerView.ViewHolder var0) {
      WeakReference var1 = var0.mNestedRecyclerView;
      if (var1 != null) {
         View var2 = (View)var1.get();

         while(var2 != null) {
            if (var2 == var0.itemView) {
               return;
            }

            ViewParent var3 = var2.getParent();
            if (var3 instanceof View) {
               var2 = (View)var3;
            } else {
               var2 = null;
            }
         }

         var0.mNestedRecyclerView = null;
      }

   }

   private void createLayoutManager(Context param1, String param2, AttributeSet param3, int param4, int param5) {
      // $FF: Couldn't be decompiled
   }

   private boolean didChildRangeChange(int var1, int var2) {
      this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
      int[] var3 = this.mMinMaxLayoutPositions;
      boolean var4 = false;
      if (var3[0] != var1 || var3[1] != var2) {
         var4 = true;
      }

      return var4;
   }

   private void dispatchContentChangedIfNecessary() {
      int var1 = this.mEatenAccessibilityChangeFlags;
      this.mEatenAccessibilityChangeFlags = 0;
      if (var1 != 0 && this.isAccessibilityEnabled()) {
         AccessibilityEvent var2 = AccessibilityEvent.obtain();
         var2.setEventType(2048);
         AccessibilityEventCompat.setContentChangeTypes(var2, var1);
         this.sendAccessibilityEventUnchecked(var2);
      }

   }

   private void dispatchLayoutStep1() {
      RecyclerView.State var1 = this.mState;
      boolean var2 = true;
      var1.assertLayoutStep(1);
      this.fillRemainingScrollValues(this.mState);
      this.mState.mIsMeasuring = false;
      this.startInterceptRequestLayout();
      this.mViewInfoStore.clear();
      this.onEnterLayoutOrScroll();
      this.processAdapterUpdatesAndSetAnimationFlags();
      this.saveFocusInfo();
      var1 = this.mState;
      if (!var1.mRunSimpleAnimations || !this.mItemsChanged) {
         var2 = false;
      }

      var1.mTrackOldChangeHolders = var2;
      this.mItemsChanged = false;
      this.mItemsAddedOrRemoved = false;
      var1 = this.mState;
      var1.mInPreLayout = var1.mRunPredictiveAnimations;
      var1.mItemCount = this.mAdapter.getItemCount();
      this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
      int var3;
      int var4;
      RecyclerView.ViewHolder var5;
      RecyclerView.ItemAnimator.ItemHolderInfo var9;
      if (this.mState.mRunSimpleAnimations) {
         var3 = this.mChildHelper.getChildCount();

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = getChildViewHolderInt(this.mChildHelper.getChildAt(var4));
            if (!var5.shouldIgnore() && (!var5.isInvalid() || this.mAdapter.hasStableIds())) {
               var9 = this.mItemAnimator.recordPreLayoutInformation(this.mState, var5, RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(var5), var5.getUnmodifiedPayloads());
               this.mViewInfoStore.addToPreLayout(var5, var9);
               if (this.mState.mTrackOldChangeHolders && var5.isUpdated() && !var5.isRemoved() && !var5.shouldIgnore() && !var5.isInvalid()) {
                  long var6 = this.getChangedHolderKey(var5);
                  this.mViewInfoStore.addToOldChangeHolders(var6, var5);
               }
            }
         }
      }

      if (this.mState.mRunPredictiveAnimations) {
         this.saveOldPositions();
         var1 = this.mState;
         var2 = var1.mStructureChanged;
         var1.mStructureChanged = false;
         this.mLayout.onLayoutChildren(this.mRecycler, var1);
         this.mState.mStructureChanged = var2;

         for(var4 = 0; var4 < this.mChildHelper.getChildCount(); ++var4) {
            var5 = getChildViewHolderInt(this.mChildHelper.getChildAt(var4));
            if (!var5.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(var5)) {
               int var8 = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(var5);
               var2 = var5.hasAnyOfTheFlags(8192);
               var3 = var8;
               if (!var2) {
                  var3 = var8 | 4096;
               }

               var9 = this.mItemAnimator.recordPreLayoutInformation(this.mState, var5, var3, var5.getUnmodifiedPayloads());
               if (var2) {
                  this.recordAnimationInfoIfBouncedHiddenView(var5, var9);
               } else {
                  this.mViewInfoStore.addToAppearedInPreLayoutHolders(var5, var9);
               }
            }
         }

         this.clearOldPositions();
      } else {
         this.clearOldPositions();
      }

      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
      this.mState.mLayoutStep = 2;
   }

   private void dispatchLayoutStep2() {
      this.startInterceptRequestLayout();
      this.onEnterLayoutOrScroll();
      this.mState.assertLayoutStep(6);
      this.mAdapterHelper.consumeUpdatesInOnePass();
      this.mState.mItemCount = this.mAdapter.getItemCount();
      RecyclerView.State var1 = this.mState;
      var1.mDeletedInvisibleItemCountSincePreviousLayout = 0;
      var1.mInPreLayout = false;
      this.mLayout.onLayoutChildren(this.mRecycler, var1);
      var1 = this.mState;
      var1.mStructureChanged = false;
      this.mPendingSavedState = null;
      boolean var2;
      if (var1.mRunSimpleAnimations && this.mItemAnimator != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var1.mRunSimpleAnimations = var2;
      this.mState.mLayoutStep = 4;
      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
   }

   private void dispatchLayoutStep3() {
      this.mState.assertLayoutStep(4);
      this.startInterceptRequestLayout();
      this.onEnterLayoutOrScroll();
      RecyclerView.State var1 = this.mState;
      var1.mLayoutStep = 1;
      if (var1.mRunSimpleAnimations) {
         for(int var2 = this.mChildHelper.getChildCount() - 1; var2 >= 0; --var2) {
            RecyclerView.ViewHolder var10 = getChildViewHolderInt(this.mChildHelper.getChildAt(var2));
            if (!var10.shouldIgnore()) {
               long var3 = this.getChangedHolderKey(var10);
               RecyclerView.ItemAnimator.ItemHolderInfo var5 = this.mItemAnimator.recordPostLayoutInformation(this.mState, var10);
               RecyclerView.ViewHolder var6 = this.mViewInfoStore.getFromOldChangeHolders(var3);
               if (var6 != null && !var6.shouldIgnore()) {
                  boolean var7 = this.mViewInfoStore.isDisappearing(var6);
                  boolean var8 = this.mViewInfoStore.isDisappearing(var10);
                  if (var7 && var6 == var10) {
                     this.mViewInfoStore.addToPostLayout(var10, var5);
                  } else {
                     RecyclerView.ItemAnimator.ItemHolderInfo var9 = this.mViewInfoStore.popFromPreLayout(var6);
                     this.mViewInfoStore.addToPostLayout(var10, var5);
                     var5 = this.mViewInfoStore.popFromPostLayout(var10);
                     if (var9 == null) {
                        this.handleMissingPreInfoForChangeError(var3, var10, var6);
                     } else {
                        this.animateChange(var6, var10, var9, var5, var7, var8);
                     }
                  }
               } else {
                  this.mViewInfoStore.addToPostLayout(var10, var5);
               }
            }
         }

         this.mViewInfoStore.process(this.mViewInfoProcessCallback);
      }

      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      var1 = this.mState;
      var1.mPreviousLayoutItemCount = var1.mItemCount;
      this.mDataSetHasChangedAfterLayout = false;
      this.mDispatchItemsChangedEvent = false;
      var1.mRunSimpleAnimations = false;
      var1.mRunPredictiveAnimations = false;
      this.mLayout.mRequestedSimpleAnimations = false;
      ArrayList var11 = this.mRecycler.mChangedScrap;
      if (var11 != null) {
         var11.clear();
      }

      RecyclerView.LayoutManager var12 = this.mLayout;
      if (var12.mPrefetchMaxObservedInInitialPrefetch) {
         var12.mPrefetchMaxCountObserved = 0;
         var12.mPrefetchMaxObservedInInitialPrefetch = false;
         this.mRecycler.updateViewCacheSize();
      }

      this.mLayout.onLayoutCompleted(this.mState);
      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
      this.mViewInfoStore.clear();
      int[] var13 = this.mMinMaxLayoutPositions;
      if (this.didChildRangeChange(var13[0], var13[1])) {
         this.dispatchOnScrolled(0, 0);
      }

      this.recoverFocusFromState();
      this.resetFocusInfo();
   }

   private boolean dispatchToOnItemTouchListeners(MotionEvent var1) {
      RecyclerView.OnItemTouchListener var2 = this.mInterceptingOnItemTouchListener;
      if (var2 == null) {
         return var1.getAction() == 0 ? false : this.findInterceptingOnItemTouchListener(var1);
      } else {
         var2.onTouchEvent(this, var1);
         int var3 = var1.getAction();
         if (var3 == 3 || var3 == 1) {
            this.mInterceptingOnItemTouchListener = null;
         }

         return true;
      }
   }

   private boolean findInterceptingOnItemTouchListener(MotionEvent var1) {
      int var2 = var1.getAction();
      int var3 = this.mOnItemTouchListeners.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         RecyclerView.OnItemTouchListener var5 = (RecyclerView.OnItemTouchListener)this.mOnItemTouchListeners.get(var4);
         if (var5.onInterceptTouchEvent(this, var1) && var2 != 3) {
            this.mInterceptingOnItemTouchListener = var5;
            return true;
         }
      }

      return false;
   }

   private void findMinMaxChildLayoutPositions(int[] var1) {
      int var2 = this.mChildHelper.getChildCount();
      if (var2 == 0) {
         var1[0] = -1;
         var1[1] = -1;
      } else {
         int var3 = 0;
         int var4 = Integer.MAX_VALUE;

         int var5;
         int var7;
         for(var5 = Integer.MIN_VALUE; var3 < var2; var5 = var7) {
            RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getChildAt(var3));
            if (var6.shouldIgnore()) {
               var7 = var5;
            } else {
               int var8 = var6.getLayoutPosition();
               int var9 = var4;
               if (var8 < var4) {
                  var9 = var8;
               }

               var4 = var9;
               var7 = var5;
               if (var8 > var5) {
                  var7 = var8;
                  var4 = var9;
               }
            }

            ++var3;
         }

         var1[0] = var4;
         var1[1] = var5;
      }
   }

   static RecyclerView findNestedRecyclerView(View var0) {
      if (!(var0 instanceof ViewGroup)) {
         return null;
      } else if (var0 instanceof RecyclerView) {
         return (RecyclerView)var0;
      } else {
         ViewGroup var1 = (ViewGroup)var0;
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            RecyclerView var4 = findNestedRecyclerView(var1.getChildAt(var3));
            if (var4 != null) {
               return var4;
            }
         }

         return null;
      }
   }

   private View findNextViewToFocus() {
      int var1 = this.mState.mFocusedItemPosition;
      if (var1 == -1) {
         var1 = 0;
      }

      int var2 = this.mState.getItemCount();

      RecyclerView.ViewHolder var4;
      for(int var3 = var1; var3 < var2; ++var3) {
         var4 = this.findViewHolderForAdapterPosition(var3);
         if (var4 == null) {
            break;
         }

         if (var4.itemView.hasFocusable()) {
            return var4.itemView;
         }
      }

      for(var1 = Math.min(var2, var1) - 1; var1 >= 0; --var1) {
         var4 = this.findViewHolderForAdapterPosition(var1);
         if (var4 == null) {
            return null;
         }

         if (var4.itemView.hasFocusable()) {
            return var4.itemView;
         }
      }

      return null;
   }

   static RecyclerView.ViewHolder getChildViewHolderInt(View var0) {
      return var0 == null ? null : ((RecyclerView.LayoutParams)var0.getLayoutParams()).mViewHolder;
   }

   static void getDecoratedBoundsWithMarginsInt(View var0, Rect var1) {
      RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var0.getLayoutParams();
      Rect var3 = var2.mDecorInsets;
      var1.set(var0.getLeft() - var3.left - var2.leftMargin, var0.getTop() - var3.top - var2.topMargin, var0.getRight() + var3.right + var2.rightMargin, var0.getBottom() + var3.bottom + var2.bottomMargin);
   }

   private int getDeepestFocusedViewWithId(View var1) {
      int var2 = var1.getId();

      while(!var1.isFocused() && var1 instanceof ViewGroup && var1.hasFocus()) {
         View var3 = ((ViewGroup)var1).getFocusedChild();
         var1 = var3;
         if (var3.getId() != -1) {
            var2 = var3.getId();
            var1 = var3;
         }
      }

      return var2;
   }

   private String getFullClassName(Context var1, String var2) {
      if (var2.charAt(0) == '.') {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getPackageName());
         var3.append(var2);
         return var3.toString();
      } else if (var2.contains(".")) {
         return var2;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(RecyclerView.class.getPackage().getName());
         var4.append('.');
         var4.append(var2);
         return var4.toString();
      }
   }

   private NestedScrollingChildHelper getScrollingChildHelper() {
      if (this.mScrollingChildHelper == null) {
         this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
      }

      return this.mScrollingChildHelper;
   }

   private void handleMissingPreInfoForChangeError(long var1, RecyclerView.ViewHolder var3, RecyclerView.ViewHolder var4) {
      int var5 = this.mChildHelper.getChildCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         RecyclerView.ViewHolder var7 = getChildViewHolderInt(this.mChildHelper.getChildAt(var6));
         if (var7 != var3 && this.getChangedHolderKey(var7) == var1) {
            RecyclerView.Adapter var8 = this.mAdapter;
            StringBuilder var9;
            if (var8 != null && var8.hasStableIds()) {
               var9 = new StringBuilder();
               var9.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
               var9.append(var7);
               var9.append(" \n View Holder 2:");
               var9.append(var3);
               var9.append(this.exceptionLabel());
               throw new IllegalStateException(var9.toString());
            }

            var9 = new StringBuilder();
            var9.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
            var9.append(var7);
            var9.append(" \n View Holder 2:");
            var9.append(var3);
            var9.append(this.exceptionLabel());
            throw new IllegalStateException(var9.toString());
         }
      }

      StringBuilder var10 = new StringBuilder();
      var10.append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
      var10.append(var4);
      var10.append(" cannot be found but it is necessary for ");
      var10.append(var3);
      var10.append(this.exceptionLabel());
      Log.e("RecyclerView", var10.toString());
   }

   private boolean hasUpdatedView() {
      int var1 = this.mChildHelper.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getChildAt(var2));
         if (var3 != null && !var3.shouldIgnore() && var3.isUpdated()) {
            return true;
         }
      }

      return false;
   }

   @SuppressLint({"InlinedApi"})
   private void initAutofill() {
      if (ViewCompat.getImportantForAutofill(this) == 0) {
         ViewCompat.setImportantForAutofill(this, 8);
      }

   }

   private void initChildrenHelper() {
      this.mChildHelper = new ChildHelper(new ChildHelper.Callback() {
         public void addView(View var1, int var2) {
            RecyclerView.this.addView(var1, var2);
            RecyclerView.this.dispatchChildAttached(var1);
         }

         public void attachViewToParent(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
            RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var1);
            if (var4 != null) {
               if (!var4.isTmpDetached() && !var4.shouldIgnore()) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Called attach on a child which is not detached: ");
                  var5.append(var4);
                  var5.append(RecyclerView.this.exceptionLabel());
                  throw new IllegalArgumentException(var5.toString());
               }

               var4.clearTmpDetachFlag();
            }

            RecyclerView.this.attachViewToParent(var1, var2, var3);
         }

         public void detachViewFromParent(int var1) {
            View var2 = this.getChildAt(var1);
            if (var2 != null) {
               RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var2);
               if (var4 != null) {
                  if (var4.isTmpDetached() && !var4.shouldIgnore()) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("called detach on an already detached child ");
                     var3.append(var4);
                     var3.append(RecyclerView.this.exceptionLabel());
                     throw new IllegalArgumentException(var3.toString());
                  }

                  var4.addFlags(256);
               }
            }

            RecyclerView.this.detachViewFromParent(var1);
         }

         public View getChildAt(int var1) {
            return RecyclerView.this.getChildAt(var1);
         }

         public int getChildCount() {
            return RecyclerView.this.getChildCount();
         }

         public RecyclerView.ViewHolder getChildViewHolder(View var1) {
            return RecyclerView.getChildViewHolderInt(var1);
         }

         public int indexOfChild(View var1) {
            return RecyclerView.this.indexOfChild(var1);
         }

         public void onEnteredHiddenState(View var1) {
            RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
            if (var2 != null) {
               var2.onEnteredHiddenState(RecyclerView.this);
            }

         }

         public void onLeftHiddenState(View var1) {
            RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
            if (var2 != null) {
               var2.onLeftHiddenState(RecyclerView.this);
            }

         }

         public void removeAllViews() {
            int var1 = this.getChildCount();

            for(int var2 = 0; var2 < var1; ++var2) {
               View var3 = this.getChildAt(var2);
               RecyclerView.this.dispatchChildDetached(var3);
               var3.clearAnimation();
            }

            RecyclerView.this.removeAllViews();
         }

         public void removeViewAt(int var1) {
            View var2 = RecyclerView.this.getChildAt(var1);
            if (var2 != null) {
               RecyclerView.this.dispatchChildDetached(var2);
               var2.clearAnimation();
            }

            RecyclerView.this.removeViewAt(var1);
         }
      });
   }

   private boolean isPreferredNextFocus(View var1, View var2, int var3) {
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = var8;
      if (var2 != null) {
         if (var2 == this) {
            var10 = var8;
         } else {
            if (this.findContainingItemView(var2) == null) {
               return false;
            }

            if (var1 == null) {
               return true;
            }

            if (this.findContainingItemView(var1) == null) {
               return true;
            }

            this.mTempRect.set(0, 0, var1.getWidth(), var1.getHeight());
            this.mTempRect2.set(0, 0, var2.getWidth(), var2.getHeight());
            this.offsetDescendantRectToMyCoords(var1, this.mTempRect);
            this.offsetDescendantRectToMyCoords(var2, this.mTempRect2);
            int var11 = this.mLayout.getLayoutDirection();
            byte var12 = -1;
            byte var13;
            if (var11 == 1) {
               var13 = -1;
            } else {
               var13 = 1;
            }

            Rect var16 = this.mTempRect;
            var11 = var16.left;
            int var14 = this.mTempRect2.left;
            byte var18;
            if ((var11 < var14 || var16.right <= var14) && this.mTempRect.right < this.mTempRect2.right) {
               var18 = 1;
            } else {
               var16 = this.mTempRect;
               var14 = var16.right;
               var11 = this.mTempRect2.right;
               if ((var14 > var11 || var16.left >= var11) && this.mTempRect.left > this.mTempRect2.left) {
                  var18 = -1;
               } else {
                  var18 = 0;
               }
            }

            var16 = this.mTempRect;
            var14 = var16.top;
            int var15 = this.mTempRect2.top;
            if ((var14 < var15 || var16.bottom <= var15) && this.mTempRect.bottom < this.mTempRect2.bottom) {
               var12 = 1;
            } else {
               var16 = this.mTempRect;
               var15 = var16.bottom;
               var14 = this.mTempRect2.bottom;
               if (var15 <= var14 && var16.top < var14 || this.mTempRect.top <= this.mTempRect2.top) {
                  var12 = 0;
               }
            }

            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 17) {
                     if (var3 != 33) {
                        if (var3 != 66) {
                           if (var3 == 130) {
                              var10 = var9;
                              if (var12 > 0) {
                                 var10 = true;
                              }

                              return var10;
                           }

                           StringBuilder var17 = new StringBuilder();
                           var17.append("Invalid direction: ");
                           var17.append(var3);
                           var17.append(this.exceptionLabel());
                           throw new IllegalArgumentException(var17.toString());
                        }

                        var10 = var4;
                        if (var18 > 0) {
                           var10 = true;
                        }

                        return var10;
                     }

                     var10 = var5;
                     if (var12 < 0) {
                        var10 = true;
                     }

                     return var10;
                  }

                  var10 = var6;
                  if (var18 < 0) {
                     var10 = true;
                  }

                  return var10;
               }

               if (var12 <= 0) {
                  var10 = var7;
                  if (var12 != 0) {
                     return var10;
                  }

                  var10 = var7;
                  if (var18 * var13 < 0) {
                     return var10;
                  }
               }

               var10 = true;
               return var10;
            }

            if (var12 >= 0) {
               var10 = var8;
               if (var12 != 0) {
                  return var10;
               }

               var10 = var8;
               if (var18 * var13 > 0) {
                  return var10;
               }
            }

            var10 = true;
         }
      }

      return var10;
   }

   private void onPointerUp(MotionEvent var1) {
      int var2 = var1.getActionIndex();
      if (var1.getPointerId(var2) == this.mScrollPointerId) {
         byte var4;
         if (var2 == 0) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         this.mScrollPointerId = var1.getPointerId(var4);
         int var3 = (int)(var1.getX(var4) + 0.5F);
         this.mLastTouchX = var3;
         this.mInitialTouchX = var3;
         var2 = (int)(var1.getY(var4) + 0.5F);
         this.mLastTouchY = var2;
         this.mInitialTouchY = var2;
      }

   }

   private boolean predictiveItemAnimationsEnabled() {
      boolean var1;
      if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void processAdapterUpdatesAndSetAnimationFlags() {
      if (this.mDataSetHasChangedAfterLayout) {
         this.mAdapterHelper.reset();
         if (this.mDispatchItemsChangedEvent) {
            this.mLayout.onItemsChanged(this);
         }
      }

      if (this.predictiveItemAnimationsEnabled()) {
         this.mAdapterHelper.preProcess();
      } else {
         this.mAdapterHelper.consumeUpdatesInOnePass();
      }

      boolean var1 = this.mItemsAddedOrRemoved;
      boolean var2 = false;
      boolean var3;
      if (!var1 && !this.mItemsChanged) {
         var3 = false;
      } else {
         var3 = true;
      }

      RecyclerView.State var4 = this.mState;
      if (!this.mFirstLayoutComplete || this.mItemAnimator == null || !this.mDataSetHasChangedAfterLayout && !var3 && !this.mLayout.mRequestedSimpleAnimations || this.mDataSetHasChangedAfterLayout && !this.mAdapter.hasStableIds()) {
         var1 = false;
      } else {
         var1 = true;
      }

      var4.mRunSimpleAnimations = var1;
      var4 = this.mState;
      var1 = var2;
      if (var4.mRunSimpleAnimations) {
         var1 = var2;
         if (var3) {
            var1 = var2;
            if (!this.mDataSetHasChangedAfterLayout) {
               var1 = var2;
               if (this.predictiveItemAnimationsEnabled()) {
                  var1 = true;
               }
            }
         }
      }

      var4.mRunPredictiveAnimations = var1;
   }

   private void pullGlows(float var1, float var2, float var3, float var4) {
      boolean var5;
      boolean var6;
      label32: {
         var5 = true;
         if (var2 < 0.0F) {
            this.ensureLeftGlow();
            EdgeEffectCompat.onPull(this.mLeftGlow, -var2 / (float)this.getWidth(), 1.0F - var3 / (float)this.getHeight());
         } else {
            if (var2 <= 0.0F) {
               var6 = false;
               break label32;
            }

            this.ensureRightGlow();
            EdgeEffectCompat.onPull(this.mRightGlow, var2 / (float)this.getWidth(), var3 / (float)this.getHeight());
         }

         var6 = true;
      }

      if (var4 < 0.0F) {
         this.ensureTopGlow();
         EdgeEffectCompat.onPull(this.mTopGlow, -var4 / (float)this.getHeight(), var1 / (float)this.getWidth());
         var6 = var5;
      } else if (var4 > 0.0F) {
         this.ensureBottomGlow();
         EdgeEffectCompat.onPull(this.mBottomGlow, var4 / (float)this.getHeight(), 1.0F - var1 / (float)this.getWidth());
         var6 = var5;
      }

      if (var6 || var2 != 0.0F || var4 != 0.0F) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   private void recoverFocusFromState() {
      if (this.mPreserveFocusAfterLayout && this.mAdapter != null && this.hasFocus() && this.getDescendantFocusability() != 393216 && (this.getDescendantFocusability() != 131072 || !this.isFocused())) {
         View var1;
         if (!this.isFocused()) {
            var1 = this.getFocusedChild();
            if (IGNORE_DETACHED_FOCUSED_CHILD && (var1.getParent() == null || !var1.hasFocus())) {
               if (this.mChildHelper.getChildCount() == 0) {
                  this.requestFocus();
                  return;
               }
            } else if (!this.mChildHelper.isHidden(var1)) {
               return;
            }
         }

         long var2 = this.mState.mFocusedItemId;
         View var4 = null;
         RecyclerView.ViewHolder var6;
         if (var2 != -1L && this.mAdapter.hasStableIds()) {
            var6 = this.findViewHolderForItemId(this.mState.mFocusedItemId);
         } else {
            var6 = null;
         }

         if (var6 != null && !this.mChildHelper.isHidden(var6.itemView) && var6.itemView.hasFocusable()) {
            var1 = var6.itemView;
         } else {
            var1 = var4;
            if (this.mChildHelper.getChildCount() > 0) {
               var1 = this.findNextViewToFocus();
            }
         }

         if (var1 != null) {
            int var5 = this.mState.mFocusedSubChildId;
            if ((long)var5 != -1L) {
               var4 = var1.findViewById(var5);
               if (var4 != null && var4.isFocusable()) {
                  var1 = var4;
               }
            }

            var1.requestFocus();
         }
      }

   }

   private void releaseGlows() {
      EdgeEffect var1 = this.mLeftGlow;
      boolean var2;
      if (var1 != null) {
         var1.onRelease();
         var2 = this.mLeftGlow.isFinished();
      } else {
         var2 = false;
      }

      var1 = this.mTopGlow;
      boolean var3 = var2;
      if (var1 != null) {
         var1.onRelease();
         var3 = var2 | this.mTopGlow.isFinished();
      }

      var1 = this.mRightGlow;
      var2 = var3;
      if (var1 != null) {
         var1.onRelease();
         var2 = var3 | this.mRightGlow.isFinished();
      }

      var1 = this.mBottomGlow;
      var3 = var2;
      if (var1 != null) {
         var1.onRelease();
         var3 = var2 | this.mBottomGlow.isFinished();
      }

      if (var3) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   private void requestChildOnScreen(View var1, View var2) {
      View var3;
      if (var2 != null) {
         var3 = var2;
      } else {
         var3 = var1;
      }

      this.mTempRect.set(0, 0, var3.getWidth(), var3.getHeight());
      android.view.ViewGroup.LayoutParams var7 = var3.getLayoutParams();
      Rect var4;
      if (var7 instanceof RecyclerView.LayoutParams) {
         RecyclerView.LayoutParams var8 = (RecyclerView.LayoutParams)var7;
         if (!var8.mInsetsDirty) {
            var4 = var8.mDecorInsets;
            Rect var9 = this.mTempRect;
            var9.left -= var4.left;
            var9.right += var4.right;
            var9.top -= var4.top;
            var9.bottom += var4.bottom;
         }
      }

      if (var2 != null) {
         this.offsetDescendantRectToMyCoords(var2, this.mTempRect);
         this.offsetRectIntoDescendantCoords(var1, this.mTempRect);
      }

      RecyclerView.LayoutManager var10 = this.mLayout;
      var4 = this.mTempRect;
      boolean var5 = this.mFirstLayoutComplete;
      boolean var6;
      if (var2 == null) {
         var6 = true;
      } else {
         var6 = false;
      }

      var10.requestChildRectangleOnScreen(this, var1, var4, var5 ^ true, var6);
   }

   private void resetFocusInfo() {
      RecyclerView.State var1 = this.mState;
      var1.mFocusedItemId = -1L;
      var1.mFocusedItemPosition = -1;
      var1.mFocusedSubChildId = -1;
   }

   private void resetScroll() {
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 != null) {
         var1.clear();
      }

      this.stopNestedScroll(0);
      this.releaseGlows();
   }

   private void saveFocusInfo() {
      boolean var1 = this.mPreserveFocusAfterLayout;
      RecyclerView.State var2 = null;
      View var3;
      if (var1 && this.hasFocus() && this.mAdapter != null) {
         var3 = this.getFocusedChild();
      } else {
         var3 = null;
      }

      RecyclerView.ViewHolder var7;
      if (var3 == null) {
         var7 = var2;
      } else {
         var7 = this.findContainingViewHolder(var3);
      }

      if (var7 == null) {
         this.resetFocusInfo();
      } else {
         var2 = this.mState;
         long var4;
         if (this.mAdapter.hasStableIds()) {
            var4 = var7.getItemId();
         } else {
            var4 = -1L;
         }

         var2.mFocusedItemId = var4;
         var2 = this.mState;
         int var6;
         if (this.mDataSetHasChangedAfterLayout) {
            var6 = -1;
         } else if (var7.isRemoved()) {
            var6 = var7.mOldPosition;
         } else {
            var6 = var7.getAdapterPosition();
         }

         var2.mFocusedItemPosition = var6;
         this.mState.mFocusedSubChildId = this.getDeepestFocusedViewWithId(var7.itemView);
      }

   }

   private void setAdapterInternal(RecyclerView.Adapter var1, boolean var2, boolean var3) {
      RecyclerView.Adapter var4 = this.mAdapter;
      if (var4 != null) {
         var4.unregisterAdapterDataObserver(this.mObserver);
         this.mAdapter.onDetachedFromRecyclerView(this);
      }

      if (!var2 || var3) {
         this.removeAndRecycleViews();
      }

      this.mAdapterHelper.reset();
      var4 = this.mAdapter;
      this.mAdapter = var1;
      if (var1 != null) {
         var1.registerAdapterDataObserver(this.mObserver);
         var1.onAttachedToRecyclerView(this);
      }

      RecyclerView.LayoutManager var5 = this.mLayout;
      if (var5 != null) {
         var5.onAdapterChanged(var4, this.mAdapter);
      }

      this.mRecycler.onAdapterChanged(var4, this.mAdapter, var2);
      this.mState.mStructureChanged = true;
   }

   private void stopScrollersInternal() {
      this.mViewFlinger.stop();
      RecyclerView.LayoutManager var1 = this.mLayout;
      if (var1 != null) {
         var1.stopSmoothScroller();
      }

   }

   void absorbGlows(int var1, int var2) {
      if (var1 < 0) {
         this.ensureLeftGlow();
         if (this.mLeftGlow.isFinished()) {
            this.mLeftGlow.onAbsorb(-var1);
         }
      } else if (var1 > 0) {
         this.ensureRightGlow();
         if (this.mRightGlow.isFinished()) {
            this.mRightGlow.onAbsorb(var1);
         }
      }

      if (var2 < 0) {
         this.ensureTopGlow();
         if (this.mTopGlow.isFinished()) {
            this.mTopGlow.onAbsorb(-var2);
         }
      } else if (var2 > 0) {
         this.ensureBottomGlow();
         if (this.mBottomGlow.isFinished()) {
            this.mBottomGlow.onAbsorb(var2);
         }
      }

      if (var1 != 0 || var2 != 0) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      RecyclerView.LayoutManager var4 = this.mLayout;
      if (var4 == null || !var4.onAddFocusables(this, var1, var2, var3)) {
         super.addFocusables(var1, var2, var3);
      }

   }

   public void addItemDecoration(RecyclerView.ItemDecoration var1) {
      this.addItemDecoration(var1, -1);
   }

   public void addItemDecoration(RecyclerView.ItemDecoration var1, int var2) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      if (var3 != null) {
         var3.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
      }

      if (this.mItemDecorations.isEmpty()) {
         this.setWillNotDraw(false);
      }

      if (var2 < 0) {
         this.mItemDecorations.add(var1);
      } else {
         this.mItemDecorations.add(var2, var1);
      }

      this.markItemDecorInsetsDirty();
      this.requestLayout();
   }

   public void addOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener var1) {
      if (this.mOnChildAttachStateListeners == null) {
         this.mOnChildAttachStateListeners = new ArrayList();
      }

      this.mOnChildAttachStateListeners.add(var1);
   }

   public void addOnItemTouchListener(RecyclerView.OnItemTouchListener var1) {
      this.mOnItemTouchListeners.add(var1);
   }

   public void addOnScrollListener(RecyclerView.OnScrollListener var1) {
      if (this.mScrollListeners == null) {
         this.mScrollListeners = new ArrayList();
      }

      this.mScrollListeners.add(var1);
   }

   void animateAppearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      var1.setIsRecyclable(false);
      if (this.mItemAnimator.animateAppearance(var1, var2, var3)) {
         this.postAnimationRunner();
      }

   }

   void animateDisappearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      this.addAnimatingView(var1);
      var1.setIsRecyclable(false);
      if (this.mItemAnimator.animateDisappearance(var1, var2, var3)) {
         this.postAnimationRunner();
      }

   }

   void applyEdgeEffectColor(EdgeEffect var1) {
      if (var1 != null && VERSION.SDK_INT >= 21) {
         int var2 = this.glowColor;
         if (var2 != 0) {
            var1.setColor(var2);
         }
      }

   }

   void assertInLayoutOrScroll(String var1) {
      if (!this.isComputingLayout()) {
         if (var1 == null) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Cannot call this method unless RecyclerView is computing a layout or scrolling");
            var3.append(this.exceptionLabel());
            throw new IllegalStateException(var3.toString());
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append(var1);
            var2.append(this.exceptionLabel());
            throw new IllegalStateException(var2.toString());
         }
      }
   }

   void assertNotInLayoutOrScroll(String var1) {
      StringBuilder var2;
      if (this.isComputingLayout()) {
         if (var1 == null) {
            var2 = new StringBuilder();
            var2.append("Cannot call this method while RecyclerView is computing a layout or scrolling");
            var2.append(this.exceptionLabel());
            throw new IllegalStateException(var2.toString());
         } else {
            throw new IllegalStateException(var1);
         }
      } else {
         if (this.mDispatchScrollCounter > 0) {
            var2 = new StringBuilder();
            var2.append("");
            var2.append(this.exceptionLabel());
            Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException(var2.toString()));
         }

      }
   }

   boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1) {
      RecyclerView.ItemAnimator var2 = this.mItemAnimator;
      boolean var3;
      if (var2 != null && !var2.canReuseUpdatedViewHolder(var1, var1.getUnmodifiedPayloads())) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 instanceof RecyclerView.LayoutParams && this.mLayout.checkLayoutParams((RecyclerView.LayoutParams)var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void clearOldPositions() {
      int var1 = this.mChildHelper.getUnfilteredChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var2));
         if (!var3.shouldIgnore()) {
            var3.clearOldPosition();
         }
      }

      this.mRecycler.clearOldPositions();
   }

   public void clearOnChildAttachStateChangeListeners() {
      List var1 = this.mOnChildAttachStateListeners;
      if (var1 != null) {
         var1.clear();
      }

   }

   public void clearOnScrollListeners() {
      List var1 = this.mScrollListeners;
      if (var1 != null) {
         var1.clear();
      }

   }

   public int computeHorizontalScrollExtent() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         if (var1.canScrollHorizontally()) {
            var2 = this.mLayout.computeHorizontalScrollExtent(this.mState);
         }

         return var2;
      }
   }

   public int computeHorizontalScrollOffset() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         if (var1.canScrollHorizontally()) {
            var2 = this.mLayout.computeHorizontalScrollOffset(this.mState);
         }

         return var2;
      }
   }

   public int computeHorizontalScrollRange() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         if (var1.canScrollHorizontally()) {
            var2 = this.mLayout.computeHorizontalScrollRange(this.mState);
         }

         return var2;
      }
   }

   public int computeVerticalScrollExtent() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         if (var1.canScrollVertically()) {
            var2 = this.mLayout.computeVerticalScrollExtent(this.mState);
         }

         return var2;
      }
   }

   public int computeVerticalScrollOffset() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         if (var1.canScrollVertically()) {
            var2 = this.mLayout.computeVerticalScrollOffset(this.mState);
         }

         return var2;
      }
   }

   public int computeVerticalScrollRange() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      int var2 = 0;
      if (var1 == null) {
         return 0;
      } else {
         if (var1.canScrollVertically()) {
            var2 = this.mLayout.computeVerticalScrollRange(this.mState);
         }

         return var2;
      }
   }

   void considerReleasingGlowsOnScroll(int var1, int var2) {
      EdgeEffect var3 = this.mLeftGlow;
      boolean var4;
      if (var3 != null && !var3.isFinished() && var1 > 0) {
         this.mLeftGlow.onRelease();
         var4 = this.mLeftGlow.isFinished();
      } else {
         var4 = false;
      }

      var3 = this.mRightGlow;
      boolean var5 = var4;
      if (var3 != null) {
         var5 = var4;
         if (!var3.isFinished()) {
            var5 = var4;
            if (var1 < 0) {
               this.mRightGlow.onRelease();
               var5 = var4 | this.mRightGlow.isFinished();
            }
         }
      }

      var3 = this.mTopGlow;
      var4 = var5;
      if (var3 != null) {
         var4 = var5;
         if (!var3.isFinished()) {
            var4 = var5;
            if (var2 > 0) {
               this.mTopGlow.onRelease();
               var4 = var5 | this.mTopGlow.isFinished();
            }
         }
      }

      var3 = this.mBottomGlow;
      var5 = var4;
      if (var3 != null) {
         var5 = var4;
         if (!var3.isFinished()) {
            var5 = var4;
            if (var2 < 0) {
               this.mBottomGlow.onRelease();
               var5 = var4 | this.mBottomGlow.isFinished();
            }
         }
      }

      if (var5) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void consumePendingUpdateOperations() {
      if (this.mFirstLayoutComplete && !this.mDataSetHasChangedAfterLayout) {
         if (this.mAdapterHelper.hasPendingUpdates()) {
            if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
               TraceCompat.beginSection("RV PartialInvalidate");
               this.startInterceptRequestLayout();
               this.onEnterLayoutOrScroll();
               this.mAdapterHelper.preProcess();
               if (!this.mLayoutWasDefered) {
                  if (this.hasUpdatedView()) {
                     this.dispatchLayout();
                  } else {
                     this.mAdapterHelper.consumePostponedUpdates();
                  }
               }

               this.stopInterceptRequestLayout(true);
               this.onExitLayoutOrScroll();
               TraceCompat.endSection();
            } else if (this.mAdapterHelper.hasPendingUpdates()) {
               TraceCompat.beginSection("RV FullInvalidate");
               this.dispatchLayout();
               TraceCompat.endSection();
            }

         }
      } else {
         TraceCompat.beginSection("RV FullInvalidate");
         this.dispatchLayout();
         TraceCompat.endSection();
      }
   }

   void defaultOnMeasure(int var1, int var2) {
      this.setMeasuredDimension(RecyclerView.LayoutManager.chooseSize(var1, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth(this)), RecyclerView.LayoutManager.chooseSize(var2, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight(this)));
   }

   void dispatchChildAttached(View var1) {
      RecyclerView.ViewHolder var2 = getChildViewHolderInt(var1);
      this.onChildAttachedToWindow(var1);
      RecyclerView.Adapter var3 = this.mAdapter;
      if (var3 != null && var2 != null) {
         var3.onViewAttachedToWindow(var2);
      }

      List var5 = this.mOnChildAttachStateListeners;
      if (var5 != null) {
         for(int var4 = var5.size() - 1; var4 >= 0; --var4) {
            ((RecyclerView.OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(var4)).onChildViewAttachedToWindow(var1);
         }
      }

   }

   void dispatchChildDetached(View var1) {
      RecyclerView.ViewHolder var2 = getChildViewHolderInt(var1);
      this.onChildDetachedFromWindow(var1);
      RecyclerView.Adapter var3 = this.mAdapter;
      if (var3 != null && var2 != null) {
         var3.onViewDetachedFromWindow(var2);
      }

      List var5 = this.mOnChildAttachStateListeners;
      if (var5 != null) {
         for(int var4 = var5.size() - 1; var4 >= 0; --var4) {
            ((RecyclerView.OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(var4)).onChildViewDetachedFromWindow(var1);
         }
      }

   }

   void dispatchLayout() {
      if (this.mAdapter == null) {
         Log.e("RecyclerView", "No adapter attached; skipping layout");
      } else if (this.mLayout == null) {
         Log.e("RecyclerView", "No layout manager attached; skipping layout");
      } else {
         RecyclerView.State var1 = this.mState;
         var1.mIsMeasuring = false;
         if (var1.mLayoutStep == 1) {
            this.dispatchLayoutStep1();
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
         } else if (!this.mAdapterHelper.hasUpdates() && this.mLayout.getWidth() == this.getWidth() && this.mLayout.getHeight() == this.getHeight()) {
            this.mLayout.setExactMeasureSpecsFrom(this);
         } else {
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
         }

         this.dispatchLayoutStep3();
      }
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      return this.getScrollingChildHelper().dispatchNestedFling(var1, var2, var3);
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      return this.getScrollingChildHelper().dispatchNestedPreFling(var1, var2);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.getScrollingChildHelper().dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4, int var5) {
      return this.getScrollingChildHelper().dispatchNestedPreScroll(var1, var2, var3, var4, var5);
   }

   public final void dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6, int[] var7) {
      this.getScrollingChildHelper().dispatchNestedScroll(var1, var2, var3, var4, var5, var6, var7);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.getScrollingChildHelper().dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6) {
      return this.getScrollingChildHelper().dispatchNestedScroll(var1, var2, var3, var4, var5, var6);
   }

   void dispatchOnScrollStateChanged(int var1) {
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.onScrollStateChanged(var1);
      }

      this.onScrollStateChanged(var1);
      RecyclerView.OnScrollListener var4 = this.mScrollListener;
      if (var4 != null) {
         var4.onScrollStateChanged(this, var1);
      }

      List var5 = this.mScrollListeners;
      if (var5 != null) {
         for(int var3 = var5.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.OnScrollListener)this.mScrollListeners.get(var3)).onScrollStateChanged(this, var1);
         }
      }

   }

   void dispatchOnScrolled(int var1, int var2) {
      ++this.mDispatchScrollCounter;
      int var3 = this.getScrollX();
      int var4 = this.getScrollY();
      this.onScrollChanged(var3, var4, var3, var4);
      this.onScrolled(var1, var2);
      RecyclerView.OnScrollListener var5 = this.mScrollListener;
      if (var5 != null) {
         var5.onScrolled(this, var1, var2);
      }

      List var6 = this.mScrollListeners;
      if (var6 != null) {
         for(var4 = var6.size() - 1; var4 >= 0; --var4) {
            ((RecyclerView.OnScrollListener)this.mScrollListeners.get(var4)).onScrolled(this, var1, var2);
         }
      }

      --this.mDispatchScrollCounter;
   }

   void dispatchPendingImportantForAccessibilityChanges() {
      for(int var1 = this.mPendingAccessibilityImportanceChange.size() - 1; var1 >= 0; --var1) {
         RecyclerView.ViewHolder var2 = (RecyclerView.ViewHolder)this.mPendingAccessibilityImportanceChange.get(var1);
         if (var2.itemView.getParent() == this && !var2.shouldIgnore()) {
            int var3 = var2.mPendingAccessibilityState;
            if (var3 != -1) {
               ViewCompat.setImportantForAccessibility(var2.itemView, var3);
               var2.mPendingAccessibilityState = -1;
            }
         }
      }

      this.mPendingAccessibilityImportanceChange.clear();
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      this.onPopulateAccessibilityEvent(var1);
      return true;
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.dispatchThawSelfOnly(var1);
   }

   protected void dispatchSaveInstanceState(SparseArray var1) {
      this.dispatchFreezeSelfOnly(var1);
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      int var2 = this.mItemDecorations.size();
      boolean var3 = false;

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         ((RecyclerView.ItemDecoration)this.mItemDecorations.get(var4)).onDrawOver(var1, this, this.mState);
      }

      EdgeEffect var5 = this.mLeftGlow;
      int var6;
      boolean var8;
      if (var5 != null && !var5.isFinished()) {
         var6 = var1.save();
         if (this.mClipToPadding) {
            var4 = this.getPaddingBottom();
         } else {
            var4 = 0;
         }

         var1.rotate(270.0F);
         var1.translate((float)(-this.getHeight() + var4), 0.0F);
         var5 = this.mLeftGlow;
         if (var5 != null && var5.draw(var1)) {
            var8 = true;
         } else {
            var8 = false;
         }

         var1.restoreToCount(var6);
      } else {
         var8 = false;
      }

      var5 = this.mTopGlow;
      boolean var9 = var8;
      if (var5 != null) {
         var9 = var8;
         if (!var5.isFinished()) {
            var6 = var1.save();
            if (this.mClipToPadding) {
               var1.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            }

            var1.translate(0.0F, (float)this.topGlowOffset);
            var5 = this.mTopGlow;
            if (var5 != null && var5.draw(var1)) {
               var9 = true;
            } else {
               var9 = false;
            }

            var9 |= var8;
            var1.restoreToCount(var6);
         }
      }

      var5 = this.mRightGlow;
      var8 = var9;
      if (var5 != null) {
         var8 = var9;
         if (!var5.isFinished()) {
            var6 = var1.save();
            int var7 = this.getWidth();
            if (this.mClipToPadding) {
               var2 = this.getPaddingTop();
            } else {
               var2 = 0;
            }

            var1.rotate(90.0F);
            var1.translate((float)(-var2), (float)(-var7));
            var5 = this.mRightGlow;
            if (var5 != null && var5.draw(var1)) {
               var8 = true;
            } else {
               var8 = false;
            }

            var8 |= var9;
            var1.restoreToCount(var6);
         }
      }

      var5 = this.mBottomGlow;
      if (var5 != null && !var5.isFinished()) {
         var6 = var1.save();
         var1.rotate(180.0F);
         if (this.mClipToPadding) {
            var1.translate((float)(-this.getWidth() + this.getPaddingRight()), (float)(-this.getHeight() + this.getPaddingBottom()));
         } else {
            var1.translate((float)(-this.getWidth()), (float)(-this.getHeight() + this.bottomGlowOffset));
         }

         var5 = this.mBottomGlow;
         var9 = var3;
         if (var5 != null) {
            var9 = var3;
            if (var5.draw(var1)) {
               var9 = true;
            }
         }

         var9 |= var8;
         var1.restoreToCount(var6);
      } else {
         var9 = var8;
      }

      var8 = var9;
      if (!var9) {
         var8 = var9;
         if (this.mItemAnimator != null) {
            var8 = var9;
            if (this.mItemDecorations.size() > 0) {
               var8 = var9;
               if (this.mItemAnimator.isRunning()) {
                  var8 = true;
               }
            }
         }
      }

      if (var8) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public boolean drawChild(Canvas var1, View var2, long var3) {
      return super.drawChild(var1, var2, var3);
   }

   void ensureBottomGlow() {
      if (this.mBottomGlow == null) {
         this.mBottomGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
         if (this.mClipToPadding) {
            this.mBottomGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
         } else {
            this.mBottomGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
         }

         this.applyEdgeEffectColor(this.mBottomGlow);
      }
   }

   void ensureLeftGlow() {
      if (this.mLeftGlow == null) {
         this.mLeftGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
         if (this.mClipToPadding) {
            this.mLeftGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
         } else {
            this.mLeftGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
         }

         this.applyEdgeEffectColor(this.mLeftGlow);
      }
   }

   void ensureRightGlow() {
      if (this.mRightGlow == null) {
         this.mRightGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
         if (this.mClipToPadding) {
            this.mRightGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
         } else {
            this.mRightGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
         }

         this.applyEdgeEffectColor(this.mRightGlow);
      }
   }

   void ensureTopGlow() {
      if (this.mTopGlow == null) {
         this.mTopGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
         if (this.mClipToPadding) {
            this.mTopGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
         } else {
            this.mTopGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
         }

         this.applyEdgeEffectColor(this.mTopGlow);
      }
   }

   String exceptionLabel() {
      StringBuilder var1 = new StringBuilder();
      var1.append(" ");
      var1.append(super.toString());
      var1.append(", adapter:");
      var1.append(this.mAdapter);
      var1.append(", layout:");
      var1.append(this.mLayout);
      var1.append(", context:");
      var1.append(this.getContext());
      return var1.toString();
   }

   final void fillRemainingScrollValues(RecyclerView.State var1) {
      if (this.getScrollState() == 2) {
         OverScroller var2 = this.mViewFlinger.mOverScroller;
         var1.mRemainingScrollHorizontal = var2.getFinalX() - var2.getCurrX();
         var1.mRemainingScrollVertical = var2.getFinalY() - var2.getCurrY();
      } else {
         var1.mRemainingScrollHorizontal = 0;
         var1.mRemainingScrollVertical = 0;
      }

   }

   public View findChildViewUnder(float var1, float var2) {
      for(int var3 = this.mChildHelper.getChildCount() - 1; var3 >= 0; --var3) {
         View var4 = this.mChildHelper.getChildAt(var3);
         float var5 = var4.getTranslationX();
         float var6 = var4.getTranslationY();
         if (var1 >= (float)var4.getLeft() + var5 && var1 <= (float)var4.getRight() + var5 && var2 >= (float)var4.getTop() + var6 && var2 <= (float)var4.getBottom() + var6) {
            return var4;
         }
      }

      return null;
   }

   public View findContainingItemView(View var1) {
      ViewParent var2;
      for(var2 = var1.getParent(); var2 != null && var2 != this && var2 instanceof View; var2 = var1.getParent()) {
         var1 = (View)var2;
      }

      if (var2 != this) {
         var1 = null;
      }

      return var1;
   }

   public RecyclerView.ViewHolder findContainingViewHolder(View var1) {
      var1 = this.findContainingItemView(var1);
      RecyclerView.ViewHolder var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = this.getChildViewHolder(var1);
      }

      return var2;
   }

   public RecyclerView.ViewHolder findViewHolderForAdapterPosition(int var1) {
      boolean var2 = this.mDataSetHasChangedAfterLayout;
      RecyclerView.ViewHolder var3 = null;
      if (var2) {
         return null;
      } else {
         int var4 = this.mChildHelper.getUnfilteredChildCount();

         RecyclerView.ViewHolder var7;
         for(int var5 = 0; var5 < var4; var3 = var7) {
            RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var5));
            var7 = var3;
            if (var6 != null) {
               var7 = var3;
               if (!var6.isRemoved()) {
                  var7 = var3;
                  if (this.getAdapterPositionFor(var6) == var1) {
                     if (!this.mChildHelper.isHidden(var6.itemView)) {
                        return var6;
                     }

                     var7 = var6;
                  }
               }
            }

            ++var5;
         }

         return var3;
      }
   }

   public RecyclerView.ViewHolder findViewHolderForItemId(long var1) {
      RecyclerView.Adapter var3 = this.mAdapter;
      RecyclerView.ViewHolder var4 = null;
      RecyclerView.ViewHolder var5 = null;
      RecyclerView.ViewHolder var6 = var4;
      if (var3 != null) {
         if (!var3.hasStableIds()) {
            var6 = var4;
         } else {
            int var7 = this.mChildHelper.getUnfilteredChildCount();
            int var8 = 0;

            while(true) {
               var6 = var5;
               if (var8 >= var7) {
                  break;
               }

               var4 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var8));
               var6 = var5;
               if (var4 != null) {
                  var6 = var5;
                  if (!var4.isRemoved()) {
                     var6 = var5;
                     if (var4.getItemId() == var1) {
                        if (!this.mChildHelper.isHidden(var4.itemView)) {
                           return var4;
                        }

                        var6 = var4;
                     }
                  }
               }

               ++var8;
               var5 = var6;
            }
         }
      }

      return var6;
   }

   public RecyclerView.ViewHolder findViewHolderForLayoutPosition(int var1) {
      return this.findViewHolderForPosition(var1, false);
   }

   @Deprecated
   public RecyclerView.ViewHolder findViewHolderForPosition(int var1) {
      return this.findViewHolderForPosition(var1, false);
   }

   RecyclerView.ViewHolder findViewHolderForPosition(int var1, boolean var2) {
      int var3 = this.mChildHelper.getUnfilteredChildCount();
      RecyclerView.ViewHolder var4 = null;

      RecyclerView.ViewHolder var7;
      for(int var5 = 0; var5 < var3; var4 = var7) {
         RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var5));
         var7 = var4;
         if (var6 != null) {
            var7 = var4;
            if (!var6.isRemoved()) {
               label37: {
                  if (var2) {
                     if (var6.mPosition != var1) {
                        var7 = var4;
                        break label37;
                     }
                  } else if (var6.getLayoutPosition() != var1) {
                     var7 = var4;
                     break label37;
                  }

                  if (!this.mChildHelper.isHidden(var6.itemView)) {
                     return var6;
                  }

                  var7 = var6;
               }
            }
         }

         ++var5;
      }

      return var4;
   }

   public boolean fling(int var1, int var2) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      byte var4 = 0;
      if (var3 == null) {
         Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
         return false;
      } else if (this.mLayoutSuppressed) {
         return false;
      } else {
         boolean var5;
         boolean var6;
         int var7;
         label60: {
            var5 = var3.canScrollHorizontally();
            var6 = this.mLayout.canScrollVertically();
            if (var5) {
               var7 = var1;
               if (Math.abs(var1) >= this.mMinFlingVelocity) {
                  break label60;
               }
            }

            var7 = 0;
         }

         int var8;
         label55: {
            if (var6) {
               var8 = var2;
               if (Math.abs(var2) >= this.mMinFlingVelocity) {
                  break label55;
               }
            }

            var8 = 0;
         }

         if (var7 == 0 && var8 == 0) {
            return false;
         } else {
            float var9 = (float)var7;
            float var10 = (float)var8;
            if (!this.dispatchNestedPreFling(var9, var10)) {
               boolean var11;
               if (!var5 && !var6) {
                  var11 = false;
               } else {
                  var11 = true;
               }

               this.dispatchNestedFling(var9, var10, var11);
               RecyclerView.OnFlingListener var13 = this.mOnFlingListener;
               if (var13 != null && var13.onFling(var7, var8)) {
                  return true;
               }

               if (var11) {
                  byte var12 = var4;
                  if (var5) {
                     var12 = 1;
                  }

                  var2 = var12;
                  if (var6) {
                     var2 = var12 | 2;
                  }

                  this.startNestedScroll(var2, 1);
                  var1 = this.mMaxFlingVelocity;
                  var1 = Math.max(-var1, Math.min(var7, var1));
                  var2 = this.mMaxFlingVelocity;
                  var2 = Math.max(-var2, Math.min(var8, var2));
                  this.mViewFlinger.fling(var1, var2);
                  return true;
               }
            }

            return false;
         }
      }
   }

   public View focusSearch(View var1, int var2) {
      View var3 = this.mLayout.onInterceptFocusSearch(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         boolean var4;
         if (this.mAdapter != null && this.mLayout != null && !this.isComputingLayout() && !this.mLayoutSuppressed) {
            var4 = true;
         } else {
            var4 = false;
         }

         FocusFinder var8 = FocusFinder.getInstance();
         if (var4 && (var2 == 2 || var2 == 1)) {
            boolean var6;
            if (this.mLayout.canScrollVertically()) {
               short var5;
               if (var2 == 2) {
                  var5 = 130;
               } else {
                  var5 = 33;
               }

               if (var8.findNextFocus(this, var1, var5) == null) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var4 = var6;
               if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                  var2 = var5;
                  var4 = var6;
               }
            } else {
               var4 = false;
            }

            boolean var7 = var4;
            int var9 = var2;
            if (!var4) {
               var7 = var4;
               var9 = var2;
               if (this.mLayout.canScrollHorizontally()) {
                  if (this.mLayout.getLayoutDirection() == 1) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  boolean var10;
                  if (var2 == 2) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  byte var11;
                  if (var4 ^ var10) {
                     var11 = 66;
                  } else {
                     var11 = 17;
                  }

                  if (var8.findNextFocus(this, var1, var11) == null) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var7 = var6;
                  var9 = var2;
                  if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                     var9 = var11;
                     var7 = var6;
                  }
               }
            }

            if (var7) {
               this.consumePendingUpdateOperations();
               if (this.findContainingItemView(var1) == null) {
                  return null;
               }

               this.startInterceptRequestLayout();
               this.mLayout.onFocusSearchFailed(var1, var9, this.mRecycler, this.mState);
               this.stopInterceptRequestLayout(false);
            }

            var3 = var8.findNextFocus(this, var1, var9);
            var2 = var9;
         } else {
            var3 = var8.findNextFocus(this, var1, var2);
            if (var3 == null && var4) {
               this.consumePendingUpdateOperations();
               if (this.findContainingItemView(var1) == null) {
                  return null;
               }

               this.startInterceptRequestLayout();
               var3 = this.mLayout.onFocusSearchFailed(var1, var2, this.mRecycler, this.mState);
               this.stopInterceptRequestLayout(false);
            }
         }

         if (var3 != null && !var3.hasFocusable()) {
            if (this.getFocusedChild() == null) {
               return super.focusSearch(var1, var2);
            } else {
               this.requestChildOnScreen(var3, (View)null);
               return var1;
            }
         } else {
            if (!this.isPreferredNextFocus(var1, var3, var2)) {
               var3 = super.focusSearch(var1, var2);
            }

            return var3;
         }
      }
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      if (var1 != null) {
         return var1.generateDefaultLayoutParams();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("RecyclerView has no LayoutManager");
         var2.append(this.exceptionLabel());
         throw new IllegalStateException(var2.toString());
      }
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         return var2.generateLayoutParams(this.getContext(), var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("RecyclerView has no LayoutManager");
         var3.append(this.exceptionLabel());
         throw new IllegalStateException(var3.toString());
      }
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         return var2.generateLayoutParams(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("RecyclerView has no LayoutManager");
         var3.append(this.exceptionLabel());
         throw new IllegalStateException(var3.toString());
      }
   }

   public CharSequence getAccessibilityClassName() {
      return "androidx.recyclerview.widget.RecyclerView";
   }

   public RecyclerView.Adapter getAdapter() {
      return this.mAdapter;
   }

   int getAdapterPositionFor(RecyclerView.ViewHolder var1) {
      return !var1.hasAnyOfTheFlags(524) && var1.isBound() ? this.mAdapterHelper.applyPendingUpdatesToPosition(var1.mPosition) : -1;
   }

   public View getAttachedScrapChildAt(int var1) {
      return var1 >= 0 && var1 < this.mRecycler.mAttachedScrap.size() ? this.mRecycler.getScrapViewAt(var1) : null;
   }

   public int getAttachedScrapChildCount() {
      return this.mRecycler.getScrapCount();
   }

   public int getBaseline() {
      RecyclerView.LayoutManager var1 = this.mLayout;
      return var1 != null ? var1.getBaseline() : super.getBaseline();
   }

   public View getCachedChildAt(int var1) {
      return var1 >= 0 && var1 < this.mRecycler.mCachedViews.size() ? ((RecyclerView.ViewHolder)this.mRecycler.mCachedViews.get(var1)).itemView : null;
   }

   public int getCachedChildCount() {
      return this.mRecycler.mCachedViews.size();
   }

   long getChangedHolderKey(RecyclerView.ViewHolder var1) {
      long var2;
      if (this.mAdapter.hasStableIds()) {
         var2 = var1.getItemId();
      } else {
         var2 = (long)var1.mPosition;
      }

      return var2;
   }

   public int getChildAdapterPosition(View var1) {
      RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
      int var2;
      if (var3 != null) {
         var2 = var3.getAdapterPosition();
      } else {
         var2 = -1;
      }

      return var2;
   }

   protected int getChildDrawingOrder(int var1, int var2) {
      RecyclerView.ChildDrawingOrderCallback var3 = this.mChildDrawingOrderCallback;
      return var3 == null ? super.getChildDrawingOrder(var1, var2) : var3.onGetChildDrawingOrder(var1, var2);
   }

   public long getChildItemId(View var1) {
      RecyclerView.Adapter var2 = this.mAdapter;
      long var3 = -1L;
      long var5 = var3;
      if (var2 != null) {
         if (!var2.hasStableIds()) {
            var5 = var3;
         } else {
            RecyclerView.ViewHolder var7 = getChildViewHolderInt(var1);
            var5 = var3;
            if (var7 != null) {
               var5 = var7.getItemId();
            }
         }
      }

      return var5;
   }

   public int getChildLayoutPosition(View var1) {
      RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
      int var2;
      if (var3 != null) {
         var2 = var3.getLayoutPosition();
      } else {
         var2 = -1;
      }

      return var2;
   }

   @Deprecated
   public int getChildPosition(View var1) {
      return this.getChildAdapterPosition(var1);
   }

   public RecyclerView.ViewHolder getChildViewHolder(View var1) {
      ViewParent var2 = var1.getParent();
      if (var2 != null && var2 != this) {
         StringBuilder var3 = new StringBuilder();
         var3.append("View ");
         var3.append(var1);
         var3.append(" is not a direct child of ");
         var3.append(this);
         throw new IllegalArgumentException(var3.toString());
      } else {
         return getChildViewHolderInt(var1);
      }
   }

   public boolean getClipToPadding() {
      return this.mClipToPadding;
   }

   public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
      return this.mAccessibilityDelegate;
   }

   public void getDecoratedBoundsWithMargins(View var1, Rect var2) {
      getDecoratedBoundsWithMarginsInt(var1, var2);
   }

   public RecyclerView.EdgeEffectFactory getEdgeEffectFactory() {
      return this.mEdgeEffectFactory;
   }

   public View getHiddenChildAt(int var1) {
      return this.mChildHelper.getHiddenChildAt(var1);
   }

   public int getHiddenChildCount() {
      return this.mChildHelper.getHiddenChildCount();
   }

   public RecyclerView.ItemAnimator getItemAnimator() {
      return this.mItemAnimator;
   }

   Rect getItemDecorInsetsForChild(View var1) {
      RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
      if (!var2.mInsetsDirty) {
         return var2.mDecorInsets;
      } else if (this.mState.isPreLayout() && (var2.isItemChanged() || var2.isViewInvalid())) {
         return var2.mDecorInsets;
      } else {
         Rect var3 = var2.mDecorInsets;
         var3.set(0, 0, 0, 0);
         int var4 = this.mItemDecorations.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            this.mTempRect.set(0, 0, 0, 0);
            ((RecyclerView.ItemDecoration)this.mItemDecorations.get(var5)).getItemOffsets(this.mTempRect, var1, this, this.mState);
            int var6 = var3.left;
            Rect var7 = this.mTempRect;
            var3.left = var6 + var7.left;
            var3.top += var7.top;
            var3.right += var7.right;
            var3.bottom += var7.bottom;
         }

         var2.mInsetsDirty = false;
         return var3;
      }
   }

   public RecyclerView.ItemDecoration getItemDecorationAt(int var1) {
      int var2 = this.getItemDecorationCount();
      if (var1 >= 0 && var1 < var2) {
         return (RecyclerView.ItemDecoration)this.mItemDecorations.get(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" is an invalid index for size ");
         var3.append(var2);
         throw new IndexOutOfBoundsException(var3.toString());
      }
   }

   public int getItemDecorationCount() {
      return this.mItemDecorations.size();
   }

   public RecyclerView.LayoutManager getLayoutManager() {
      return this.mLayout;
   }

   public int getMaxFlingVelocity() {
      return this.mMaxFlingVelocity;
   }

   public int getMinFlingVelocity() {
      return this.mMinFlingVelocity;
   }

   long getNanoTime() {
      return ALLOW_THREAD_GAP_WORK ? System.nanoTime() : 0L;
   }

   public RecyclerView.OnFlingListener getOnFlingListener() {
      return this.mOnFlingListener;
   }

   public boolean getPreserveFocusAfterLayout() {
      return this.mPreserveFocusAfterLayout;
   }

   public RecyclerView.RecycledViewPool getRecycledViewPool() {
      return this.mRecycler.getRecycledViewPool();
   }

   public int getScrollState() {
      return this.mScrollState;
   }

   public boolean hasFixedSize() {
      return this.mHasFixedSize;
   }

   public boolean hasNestedScrollingParent() {
      return this.getScrollingChildHelper().hasNestedScrollingParent();
   }

   public boolean hasNestedScrollingParent(int var1) {
      return this.getScrollingChildHelper().hasNestedScrollingParent(var1);
   }

   public boolean hasPendingAdapterUpdates() {
      boolean var1;
      if (this.mFirstLayoutComplete && !this.mDataSetHasChangedAfterLayout && !this.mAdapterHelper.hasPendingUpdates()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   void initAdapterManager() {
      this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
         void dispatchUpdate(AdapterHelper.UpdateOp var1) {
            int var2 = var1.cmd;
            RecyclerView var3;
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 4) {
                     if (var2 == 8) {
                        var3 = RecyclerView.this;
                        var3.mLayout.onItemsMoved(var3, var1.positionStart, var1.itemCount, 1);
                     }
                  } else {
                     var3 = RecyclerView.this;
                     var3.mLayout.onItemsUpdated(var3, var1.positionStart, var1.itemCount, var1.payload);
                  }
               } else {
                  var3 = RecyclerView.this;
                  var3.mLayout.onItemsRemoved(var3, var1.positionStart, var1.itemCount);
               }
            } else {
               var3 = RecyclerView.this;
               var3.mLayout.onItemsAdded(var3, var1.positionStart, var1.itemCount);
            }

         }

         public RecyclerView.ViewHolder findViewHolder(int var1) {
            RecyclerView.ViewHolder var2 = RecyclerView.this.findViewHolderForPosition(var1, true);
            if (var2 == null) {
               return null;
            } else {
               return RecyclerView.this.mChildHelper.isHidden(var2.itemView) ? null : var2;
            }
         }

         public void markViewHoldersUpdated(int var1, int var2, Object var3) {
            RecyclerView.this.viewRangeUpdate(var1, var2, var3);
            RecyclerView.this.mItemsChanged = true;
         }

         public void offsetPositionsForAdd(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForInsert(var1, var2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
         }

         public void offsetPositionsForMove(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForMove(var1, var2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
         }

         public void offsetPositionsForRemovingInvisible(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForRemove(var1, var2, true);
            RecyclerView var3 = RecyclerView.this;
            var3.mItemsAddedOrRemoved = true;
            RecyclerView.State var4 = var3.mState;
            var4.mDeletedInvisibleItemCountSincePreviousLayout += var2;
         }

         public void offsetPositionsForRemovingLaidOutOrNewView(int var1, int var2) {
            RecyclerView.this.offsetPositionRecordsForRemove(var1, var2, false);
            RecyclerView.this.mItemsAddedOrRemoved = true;
         }

         public void onDispatchFirstPass(AdapterHelper.UpdateOp var1) {
            this.dispatchUpdate(var1);
         }

         public void onDispatchSecondPass(AdapterHelper.UpdateOp var1) {
            this.dispatchUpdate(var1);
         }
      });
   }

   void initFastScroller(StateListDrawable var1, Drawable var2, StateListDrawable var3, Drawable var4) {
      if (var1 != null && var2 != null && var3 != null && var4 != null) {
         this.getContext().getResources();
         new FastScroller(this, var1, var2, var3, var4, AndroidUtilities.dp(8.0F), AndroidUtilities.dp(50.0F), 0);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Trying to set fast scroller without both required drawables.");
         var5.append(this.exceptionLabel());
         throw new IllegalArgumentException(var5.toString());
      }
   }

   void invalidateGlows() {
      this.mBottomGlow = null;
      this.mTopGlow = null;
      this.mRightGlow = null;
      this.mLeftGlow = null;
   }

   public void invalidateItemDecorations() {
      if (this.mItemDecorations.size() != 0) {
         RecyclerView.LayoutManager var1 = this.mLayout;
         if (var1 != null) {
            var1.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
         }

         this.markItemDecorInsetsDirty();
         this.requestLayout();
      }
   }

   boolean isAccessibilityEnabled() {
      AccessibilityManager var1 = this.mAccessibilityManager;
      boolean var2;
      if (var1 != null && var1.isEnabled()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isAnimating() {
      RecyclerView.ItemAnimator var1 = this.mItemAnimator;
      boolean var2;
      if (var1 != null && var1.isRunning()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isAttachedToWindow() {
      return this.mIsAttached;
   }

   public boolean isComputingLayout() {
      boolean var1;
      if (this.mLayoutOrScrollCounter > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @Deprecated
   public boolean isLayoutFrozen() {
      return this.isLayoutSuppressed();
   }

   public final boolean isLayoutSuppressed() {
      return this.mLayoutSuppressed;
   }

   public boolean isNestedScrollingEnabled() {
      return this.getScrollingChildHelper().isNestedScrollingEnabled();
   }

   void jumpToPositionForSmoothScroller(int var1) {
      if (this.mLayout != null) {
         this.setScrollState(2);
         this.mLayout.scrollToPosition(var1);
         this.awakenScrollBars();
      }
   }

   void markItemDecorInsetsDirty() {
      int var1 = this.mChildHelper.getUnfilteredChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         ((RecyclerView.LayoutParams)this.mChildHelper.getUnfilteredChildAt(var2).getLayoutParams()).mInsetsDirty = true;
      }

      this.mRecycler.markItemDecorInsetsDirty();
   }

   void markKnownViewsInvalid() {
      int var1 = this.mChildHelper.getUnfilteredChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var2));
         if (var3 != null && !var3.shouldIgnore()) {
            var3.addFlags(6);
         }
      }

      this.markItemDecorInsetsDirty();
      this.mRecycler.markKnownViewsInvalid();
   }

   public void offsetChildrenHorizontal(int var1) {
      int var2 = this.mChildHelper.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.mChildHelper.getChildAt(var3).offsetLeftAndRight(var1);
      }

   }

   public void offsetChildrenVertical(int var1) {
      int var2 = this.mChildHelper.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.mChildHelper.getChildAt(var3).offsetTopAndBottom(var1);
      }

   }

   void offsetPositionRecordsForInsert(int var1, int var2) {
      int var3 = this.mChildHelper.getUnfilteredChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         RecyclerView.ViewHolder var5 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var4));
         if (var5 != null && !var5.shouldIgnore() && var5.mPosition >= var1) {
            var5.offsetPosition(var2, false);
            this.mState.mStructureChanged = true;
         }
      }

      this.mRecycler.offsetPositionRecordsForInsert(var1, var2);
      this.requestLayout();
   }

   void offsetPositionRecordsForMove(int var1, int var2) {
      int var3 = this.mChildHelper.getUnfilteredChildCount();
      int var4;
      int var5;
      byte var6;
      if (var1 < var2) {
         var4 = var1;
         var5 = var2;
         var6 = -1;
      } else {
         var5 = var1;
         var4 = var2;
         var6 = 1;
      }

      for(int var7 = 0; var7 < var3; ++var7) {
         RecyclerView.ViewHolder var8 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var7));
         if (var8 != null) {
            int var9 = var8.mPosition;
            if (var9 >= var4 && var9 <= var5) {
               if (var9 == var1) {
                  var8.offsetPosition(var2 - var1, false);
               } else {
                  var8.offsetPosition(var6, false);
               }

               this.mState.mStructureChanged = true;
            }
         }
      }

      this.mRecycler.offsetPositionRecordsForMove(var1, var2);
      this.requestLayout();
   }

   void offsetPositionRecordsForRemove(int var1, int var2, boolean var3) {
      int var4 = this.mChildHelper.getUnfilteredChildCount();

      for(int var5 = 0; var5 < var4; ++var5) {
         RecyclerView.ViewHolder var6 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var5));
         if (var6 != null && !var6.shouldIgnore()) {
            int var7 = var6.mPosition;
            if (var7 >= var1 + var2) {
               var6.offsetPosition(-var2, var3);
               this.mState.mStructureChanged = true;
            } else if (var7 >= var1) {
               var6.flagRemovedAndOffsetPosition(var1 - 1, -var2, var3);
               this.mState.mStructureChanged = true;
            }
         }
      }

      this.mRecycler.offsetPositionRecordsForRemove(var1, var2, var3);
      this.requestLayout();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mLayoutOrScrollCounter = 0;
      boolean var1 = true;
      this.mIsAttached = true;
      if (!this.mFirstLayoutComplete || this.isLayoutRequested()) {
         var1 = false;
      }

      this.mFirstLayoutComplete = var1;
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.dispatchAttachedToWindow(this);
      }

      this.mPostedAnimatorRunner = false;
      if (ALLOW_THREAD_GAP_WORK) {
         this.mGapWorker = (GapWorker)GapWorker.sGapWorker.get();
         if (this.mGapWorker == null) {
            float var3;
            label27: {
               this.mGapWorker = new GapWorker();
               Display var4 = ViewCompat.getDisplay(this);
               if (!this.isInEditMode() && var4 != null) {
                  var3 = var4.getRefreshRate();
                  if (var3 >= 30.0F) {
                     break label27;
                  }
               }

               var3 = 60.0F;
            }

            GapWorker var5 = this.mGapWorker;
            var5.mFrameIntervalNs = (long)(1.0E9F / var3);
            GapWorker.sGapWorker.set(var5);
         }

         this.mGapWorker.add(this);
      }

   }

   public void onChildAttachedToWindow(View var1) {
   }

   public void onChildDetachedFromWindow(View var1) {
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      RecyclerView.ItemAnimator var1 = this.mItemAnimator;
      if (var1 != null) {
         var1.endAnimations();
      }

      this.stopScroll();
      this.mIsAttached = false;
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.dispatchDetachedFromWindow(this, this.mRecycler);
      }

      this.mPendingAccessibilityImportanceChange.clear();
      this.removeCallbacks(this.mItemAnimatorRunner);
      this.mViewInfoStore.onDetach();
      if (ALLOW_THREAD_GAP_WORK) {
         GapWorker var3 = this.mGapWorker;
         if (var3 != null) {
            var3.remove(this);
            this.mGapWorker = null;
         }
      }

   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      int var2 = this.mItemDecorations.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((RecyclerView.ItemDecoration)this.mItemDecorations.get(var3)).onDraw(var1, this, this.mState);
      }

   }

   void onEnterLayoutOrScroll() {
      ++this.mLayoutOrScrollCounter;
   }

   void onExitLayoutOrScroll() {
      this.onExitLayoutOrScroll(true);
   }

   void onExitLayoutOrScroll(boolean var1) {
      --this.mLayoutOrScrollCounter;
      if (this.mLayoutOrScrollCounter < 1) {
         this.mLayoutOrScrollCounter = 0;
         if (var1) {
            this.dispatchContentChangedIfNecessary();
            this.dispatchPendingImportantForAccessibilityChanges();
         }
      }

   }

   public boolean onGenericMotionEvent(MotionEvent var1) {
      if (this.mLayout == null) {
         return false;
      } else if (this.mLayoutSuppressed) {
         return false;
      } else {
         if (var1.getAction() == 8) {
            float var2;
            float var3;
            label42: {
               if ((var1.getSource() & 2) != 0) {
                  if (this.mLayout.canScrollVertically()) {
                     var2 = -var1.getAxisValue(9);
                  } else {
                     var2 = 0.0F;
                  }

                  var3 = var2;
                  if (this.mLayout.canScrollHorizontally()) {
                     float var4 = var1.getAxisValue(10);
                     var3 = var2;
                     var2 = var4;
                     break label42;
                  }
               } else {
                  label40: {
                     if ((var1.getSource() & 4194304) != 0) {
                        var2 = var1.getAxisValue(26);
                        if (this.mLayout.canScrollVertically()) {
                           var3 = -var2;
                           break label40;
                        }

                        if (this.mLayout.canScrollHorizontally()) {
                           var3 = 0.0F;
                           break label42;
                        }
                     }

                     var3 = 0.0F;
                  }
               }

               var2 = 0.0F;
            }

            if (var3 != 0.0F || var2 != 0.0F) {
               this.scrollByInternal((int)(var2 * this.mScaledHorizontalScrollFactor), (int)(var3 * this.mScaledVerticalScrollFactor), var1);
            }
         }

         return false;
      }
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2 = this.mLayoutSuppressed;
      boolean var3 = false;
      if (var2) {
         return false;
      } else {
         this.mInterceptingOnItemTouchListener = null;
         if (this.findInterceptingOnItemTouchListener(var1)) {
            this.cancelScroll();
            return true;
         } else {
            RecyclerView.LayoutManager var4 = this.mLayout;
            if (var4 == null) {
               return false;
            } else {
               boolean var5 = var4.canScrollHorizontally();
               var2 = this.mLayout.canScrollVertically();
               if (this.mVelocityTracker == null) {
                  this.mVelocityTracker = VelocityTracker.obtain();
               }

               this.mVelocityTracker.addMovement(var1);
               int var6 = var1.getActionMasked();
               int var7 = var1.getActionIndex();
               if (var6 != 0) {
                  if (var6 != 1) {
                     if (var6 != 2) {
                        if (var6 != 3) {
                           if (var6 != 5) {
                              if (var6 == 6) {
                                 this.onPointerUp(var1);
                              }
                           } else {
                              this.mScrollPointerId = var1.getPointerId(var7);
                              var6 = (int)(var1.getX(var7) + 0.5F);
                              this.mLastTouchX = var6;
                              this.mInitialTouchX = var6;
                              var7 = (int)(var1.getY(var7) + 0.5F);
                              this.mLastTouchY = var7;
                              this.mInitialTouchY = var7;
                           }
                        } else {
                           this.cancelScroll();
                        }
                     } else {
                        var6 = var1.findPointerIndex(this.mScrollPointerId);
                        if (var6 < 0) {
                           StringBuilder var10 = new StringBuilder();
                           var10.append("Error processing scroll; pointer index for id ");
                           var10.append(this.mScrollPointerId);
                           var10.append(" not found. Did any MotionEvents get skipped?");
                           Log.e("RecyclerView", var10.toString());
                           return false;
                        }

                        var7 = (int)(var1.getX(var6) + 0.5F);
                        int var8 = (int)(var1.getY(var6) + 0.5F);
                        if (this.mScrollState != 1) {
                           var6 = this.mInitialTouchX;
                           int var9 = this.mInitialTouchY;
                           boolean var13;
                           if (var5 && Math.abs(var7 - var6) > this.mTouchSlop) {
                              this.mLastTouchX = var7;
                              var13 = true;
                           } else {
                              var13 = false;
                           }

                           boolean var12 = var13;
                           if (var2) {
                              var12 = var13;
                              if (Math.abs(var8 - var9) > this.mTouchSlop) {
                                 this.mLastTouchY = var8;
                                 var12 = true;
                              }
                           }

                           if (var12) {
                              this.setScrollState(1);
                           }
                        }
                     }
                  } else {
                     this.mVelocityTracker.clear();
                     this.stopNestedScroll(0);
                  }
               } else {
                  if (this.mIgnoreMotionEventTillDown) {
                     this.mIgnoreMotionEventTillDown = false;
                  }

                  this.mScrollPointerId = var1.getPointerId(0);
                  var7 = (int)(var1.getX() + 0.5F);
                  this.mLastTouchX = var7;
                  this.mInitialTouchX = var7;
                  var7 = (int)(var1.getY() + 0.5F);
                  this.mLastTouchY = var7;
                  this.mInitialTouchY = var7;
                  if (this.mScrollState == 2) {
                     this.getParent().requestDisallowInterceptTouchEvent(true);
                     this.setScrollState(1);
                     this.stopNestedScroll(1);
                  }

                  int[] var11 = this.mNestedOffsets;
                  var11[1] = 0;
                  var11[0] = 0;
                  byte var14;
                  if (var5) {
                     var14 = 1;
                  } else {
                     var14 = 0;
                  }

                  var6 = var14;
                  if (var2) {
                     var6 = var14 | 2;
                  }

                  this.startNestedScroll(var6, 0);
               }

               if (this.mScrollState == 1) {
                  var3 = true;
               }

               return var3;
            }
         }
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      TraceCompat.beginSection("RV OnLayout");
      this.dispatchLayout();
      TraceCompat.endSection();
      this.mFirstLayoutComplete = true;
   }

   protected void onMeasure(int var1, int var2) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      if (var3 == null) {
         this.defaultOnMeasure(var1, var2);
      } else {
         boolean var4 = var3.isAutoMeasureEnabled();
         boolean var5 = false;
         if (var4) {
            int var6 = MeasureSpec.getMode(var1);
            int var7 = MeasureSpec.getMode(var2);
            this.mLayout.onMeasure(this.mRecycler, this.mState, var1, var2);
            boolean var8 = var5;
            if (var6 == 1073741824) {
               var8 = var5;
               if (var7 == 1073741824) {
                  var8 = true;
               }
            }

            if (var8 || this.mAdapter == null) {
               return;
            }

            if (this.mState.mLayoutStep == 1) {
               this.dispatchLayoutStep1();
            }

            this.mLayout.setMeasureSpecs(var1, var2);
            this.mState.mIsMeasuring = true;
            this.dispatchLayoutStep2();
            this.mLayout.setMeasuredDimensionFromChildren(var1, var2);
            if (this.mLayout.shouldMeasureTwice()) {
               this.mLayout.setMeasureSpecs(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
               this.mState.mIsMeasuring = true;
               this.dispatchLayoutStep2();
               this.mLayout.setMeasuredDimensionFromChildren(var1, var2);
            }
         } else {
            if (this.mHasFixedSize) {
               this.mLayout.onMeasure(this.mRecycler, this.mState, var1, var2);
               return;
            }

            if (this.mAdapterUpdateDuringMeasure) {
               this.startInterceptRequestLayout();
               this.onEnterLayoutOrScroll();
               this.processAdapterUpdatesAndSetAnimationFlags();
               this.onExitLayoutOrScroll();
               RecyclerView.State var9 = this.mState;
               if (var9.mRunPredictiveAnimations) {
                  var9.mInPreLayout = true;
               } else {
                  this.mAdapterHelper.consumeUpdatesInOnePass();
                  this.mState.mInPreLayout = false;
               }

               this.mAdapterUpdateDuringMeasure = false;
               this.stopInterceptRequestLayout(false);
            } else if (this.mState.mRunPredictiveAnimations) {
               this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight());
               return;
            }

            RecyclerView.Adapter var10 = this.mAdapter;
            if (var10 != null) {
               this.mState.mItemCount = var10.getItemCount();
            } else {
               this.mState.mItemCount = 0;
            }

            this.startInterceptRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, var1, var2);
            this.stopInterceptRequestLayout(false);
            this.mState.mInPreLayout = false;
         }

      }
   }

   protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
      return this.isComputingLayout() ? false : super.onRequestFocusInDescendants(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof RecyclerView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         this.mPendingSavedState = (RecyclerView.SavedState)var1;
         super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
         RecyclerView.LayoutManager var2 = this.mLayout;
         if (var2 != null) {
            var1 = this.mPendingSavedState.mLayoutState;
            if (var1 != null) {
               var2.onRestoreInstanceState(var1);
            }
         }

      }
   }

   protected Parcelable onSaveInstanceState() {
      RecyclerView.SavedState var1 = new RecyclerView.SavedState(super.onSaveInstanceState());
      RecyclerView.SavedState var2 = this.mPendingSavedState;
      if (var2 != null) {
         var1.copyFrom(var2);
      } else {
         RecyclerView.LayoutManager var3 = this.mLayout;
         if (var3 != null) {
            var1.mLayoutState = var3.onSaveInstanceState();
         } else {
            var1.mLayoutState = null;
         }
      }

      return var1;
   }

   public void onScrollStateChanged(int var1) {
   }

   public void onScrolled(int var1, int var2) {
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3 || var2 != var4) {
         this.invalidateGlows();
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2 = this.mLayoutSuppressed;
      boolean var3 = false;
      if (!var2 && !this.mIgnoreMotionEventTillDown) {
         if (this.dispatchToOnItemTouchListeners(var1)) {
            this.cancelScroll();
            return true;
         } else {
            RecyclerView.LayoutManager var4 = this.mLayout;
            if (var4 == null) {
               return false;
            } else {
               var2 = var4.canScrollHorizontally();
               boolean var5 = this.mLayout.canScrollVertically();
               if (this.mVelocityTracker == null) {
                  this.mVelocityTracker = VelocityTracker.obtain();
               }

               MotionEvent var20 = MotionEvent.obtain(var1);
               int var6 = var1.getActionMasked();
               int var7 = var1.getActionIndex();
               int[] var8;
               if (var6 == 0) {
                  var8 = this.mNestedOffsets;
                  var8[1] = 0;
                  var8[0] = 0;
               }

               var8 = this.mNestedOffsets;
               var20.offsetLocation((float)var8[0], (float)var8[1]);
               boolean var21;
               if (var6 != 0) {
                  if (var6 != 1) {
                     if (var6 != 2) {
                        if (var6 != 3) {
                           if (var6 != 5) {
                              if (var6 != 6) {
                                 var21 = var3;
                              } else {
                                 this.onPointerUp(var1);
                                 var21 = var3;
                              }
                           } else {
                              this.mScrollPointerId = var1.getPointerId(var7);
                              var6 = (int)(var1.getX(var7) + 0.5F);
                              this.mLastTouchX = var6;
                              this.mInitialTouchX = var6;
                              var7 = (int)(var1.getY(var7) + 0.5F);
                              this.mLastTouchY = var7;
                              this.mInitialTouchY = var7;
                              var21 = var3;
                           }
                        } else {
                           this.cancelScroll();
                           var21 = var3;
                        }
                     } else {
                        var7 = var1.findPointerIndex(this.mScrollPointerId);
                        if (var7 < 0) {
                           StringBuilder var19 = new StringBuilder();
                           var19.append("Error processing scroll; pointer index for id ");
                           var19.append(this.mScrollPointerId);
                           var19.append(" not found. Did any MotionEvents get skipped?");
                           Log.e("RecyclerView", var19.toString());
                           return false;
                        }

                        int var9 = (int)(var1.getX(var7) + 0.5F);
                        int var10 = (int)(var1.getY(var7) + 0.5F);
                        int var11 = this.mLastTouchX - var9;
                        int var12 = this.mLastTouchY - var10;
                        int[] var18 = this.mReusableIntPair;
                        var18[0] = 0;
                        var18[1] = 0;
                        var6 = var11;
                        var7 = var12;
                        if (this.dispatchNestedPreScroll(var11, var12, var18, this.mScrollOffset, 0)) {
                           var18 = this.mReusableIntPair;
                           var6 = var11 - var18[0];
                           var7 = var12 - var18[1];
                           var18 = this.mScrollOffset;
                           var20.offsetLocation((float)var18[0], (float)var18[1]);
                           var8 = this.mNestedOffsets;
                           var12 = var8[0];
                           var18 = this.mScrollOffset;
                           var8[0] = var12 + var18[0];
                           var8[1] += var18[1];
                        }

                        var12 = var6;
                        var11 = var7;
                        if (this.mScrollState != 1) {
                           boolean var22;
                           label128: {
                              if (var2) {
                                 var11 = Math.abs(var6);
                                 var12 = this.mTouchSlop;
                                 if (var11 > var12) {
                                    if (var6 > 0) {
                                       var6 -= var12;
                                    } else {
                                       var6 += var12;
                                    }

                                    var22 = true;
                                    break label128;
                                 }
                              }

                              var22 = false;
                           }

                           boolean var13 = var22;
                           int var14 = var7;
                           if (var5) {
                              int var15 = Math.abs(var7);
                              var11 = this.mTouchSlop;
                              var13 = var22;
                              var14 = var7;
                              if (var15 > var11) {
                                 if (var7 > 0) {
                                    var14 = var7 - var11;
                                 } else {
                                    var14 = var7 + var11;
                                 }

                                 var13 = true;
                              }
                           }

                           var12 = var6;
                           var11 = var14;
                           if (var13) {
                              this.setScrollState(1);
                              var11 = var14;
                              var12 = var6;
                           }
                        }

                        var21 = var3;
                        if (this.mScrollState == 1) {
                           var18 = this.mScrollOffset;
                           this.mLastTouchX = var9 - var18[0];
                           this.mLastTouchY = var10 - var18[1];
                           if (var2) {
                              var7 = var12;
                           } else {
                              var7 = 0;
                           }

                           if (var5) {
                              var6 = var11;
                           } else {
                              var6 = 0;
                           }

                           if (this.scrollByInternal(var7, var6, var20)) {
                              this.getParent().requestDisallowInterceptTouchEvent(true);
                           }

                           var21 = var3;
                           if (this.mGapWorker != null) {
                              label119: {
                                 if (var12 == 0) {
                                    var21 = var3;
                                    if (var11 == 0) {
                                       break label119;
                                    }
                                 }

                                 this.mGapWorker.postFromTraversal(this, var12, var11);
                                 var21 = var3;
                              }
                           }
                        }
                     }
                  } else {
                     this.mVelocityTracker.addMovement(var20);
                     this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                     float var16;
                     if (var2) {
                        var16 = -this.mVelocityTracker.getXVelocity(this.mScrollPointerId);
                     } else {
                        var16 = 0.0F;
                     }

                     float var17;
                     if (var5) {
                        var17 = -this.mVelocityTracker.getYVelocity(this.mScrollPointerId);
                     } else {
                        var17 = 0.0F;
                     }

                     if (var16 == 0.0F && var17 == 0.0F || !this.fling((int)var16, (int)var17)) {
                        this.setScrollState(0);
                     }

                     this.resetScroll();
                     var21 = true;
                  }
               } else {
                  this.mScrollPointerId = var1.getPointerId(0);
                  var7 = (int)(var1.getX() + 0.5F);
                  this.mLastTouchX = var7;
                  this.mInitialTouchX = var7;
                  var7 = (int)(var1.getY() + 0.5F);
                  this.mLastTouchY = var7;
                  this.mInitialTouchY = var7;
                  byte var23;
                  if (var2) {
                     var23 = 1;
                  } else {
                     var23 = 0;
                  }

                  var6 = var23;
                  if (var5) {
                     var6 = var23 | 2;
                  }

                  this.startNestedScroll(var6, 0);
                  var21 = var3;
               }

               if (!var21) {
                  this.mVelocityTracker.addMovement(var20);
               }

               var20.recycle();
               return true;
            }
         }
      } else {
         return false;
      }
   }

   void postAnimationRunner() {
      if (!this.mPostedAnimatorRunner && this.mIsAttached) {
         ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
         this.mPostedAnimatorRunner = true;
      }

   }

   void processDataSetCompletelyChanged(boolean var1) {
      this.mDispatchItemsChangedEvent |= var1;
      this.mDataSetHasChangedAfterLayout = true;
      this.markKnownViewsInvalid();
   }

   void recordAnimationInfoIfBouncedHiddenView(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      var1.setFlags(0, 8192);
      if (this.mState.mTrackOldChangeHolders && var1.isUpdated() && !var1.isRemoved() && !var1.shouldIgnore()) {
         long var3 = this.getChangedHolderKey(var1);
         this.mViewInfoStore.addToOldChangeHolders(var3, var1);
      }

      this.mViewInfoStore.addToPreLayout(var1, var2);
   }

   void removeAndRecycleViews() {
      RecyclerView.ItemAnimator var1 = this.mItemAnimator;
      if (var1 != null) {
         var1.endAnimations();
      }

      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.removeAndRecycleAllViews(this.mRecycler);
         this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      }

      this.mRecycler.clear();
   }

   boolean removeAnimatingView(View var1) {
      this.startInterceptRequestLayout();
      boolean var2 = this.mChildHelper.removeViewIfHidden(var1);
      if (var2) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
         this.mRecycler.unscrapView(var3);
         this.mRecycler.recycleViewHolderInternal(var3);
      }

      this.stopInterceptRequestLayout(var2 ^ true);
      return var2;
   }

   protected void removeDetachedView(View var1, boolean var2) {
      RecyclerView.ViewHolder var3 = getChildViewHolderInt(var1);
      if (var3 != null) {
         if (var3.isTmpDetached()) {
            var3.clearTmpDetachFlag();
         } else if (!var3.shouldIgnore()) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
            var4.append(var3);
            var4.append(this.exceptionLabel());
            throw new IllegalArgumentException(var4.toString());
         }
      }

      var1.clearAnimation();
      this.dispatchChildDetached(var1);
      super.removeDetachedView(var1, var2);
   }

   public void removeItemDecoration(RecyclerView.ItemDecoration var1) {
      RecyclerView.LayoutManager var2 = this.mLayout;
      if (var2 != null) {
         var2.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
      }

      this.mItemDecorations.remove(var1);
      if (this.mItemDecorations.isEmpty()) {
         boolean var3;
         if (this.getOverScrollMode() == 2) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.setWillNotDraw(var3);
      }

      this.markItemDecorInsetsDirty();
      this.requestLayout();
   }

   public void removeItemDecorationAt(int var1) {
      int var2 = this.getItemDecorationCount();
      if (var1 >= 0 && var1 < var2) {
         this.removeItemDecoration(this.getItemDecorationAt(var1));
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" is an invalid index for size ");
         var3.append(var2);
         throw new IndexOutOfBoundsException(var3.toString());
      }
   }

   public void removeOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener var1) {
      List var2 = this.mOnChildAttachStateListeners;
      if (var2 != null) {
         var2.remove(var1);
      }
   }

   public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener var1) {
      this.mOnItemTouchListeners.remove(var1);
      if (this.mInterceptingOnItemTouchListener == var1) {
         this.mInterceptingOnItemTouchListener = null;
      }

   }

   public void removeOnScrollListener(RecyclerView.OnScrollListener var1) {
      List var2 = this.mScrollListeners;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   void repositionShadowingViews() {
      int var1 = this.mChildHelper.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.mChildHelper.getChildAt(var2);
         RecyclerView.ViewHolder var4 = this.getChildViewHolder(var3);
         if (var4 != null) {
            var4 = var4.mShadowingHolder;
            if (var4 != null) {
               View var7 = var4.itemView;
               int var5 = var3.getLeft();
               int var6 = var3.getTop();
               if (var5 != var7.getLeft() || var6 != var7.getTop()) {
                  var7.layout(var5, var6, var7.getWidth() + var5, var7.getHeight() + var6);
               }
            }
         }
      }

   }

   public void requestChildFocus(View var1, View var2) {
      if (!this.mLayout.onRequestChildFocus(this, this.mState, var1, var2) && var2 != null) {
         this.requestChildOnScreen(var1, var2);
      }

      super.requestChildFocus(var1, var2);
   }

   public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
      return this.mLayout.requestChildRectangleOnScreen(this, var1, var2, var3);
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      int var2 = this.mOnItemTouchListeners.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((RecyclerView.OnItemTouchListener)this.mOnItemTouchListeners.get(var3)).onRequestDisallowInterceptTouchEvent(var1);
      }

      super.requestDisallowInterceptTouchEvent(var1);
   }

   public void requestLayout() {
      if (this.mInterceptRequestLayoutDepth == 0 && !this.mLayoutSuppressed) {
         super.requestLayout();
      } else {
         this.mLayoutWasDefered = true;
      }

   }

   void saveOldPositions() {
      int var1 = this.mChildHelper.getUnfilteredChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         RecyclerView.ViewHolder var3 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(var2));
         if (!var3.shouldIgnore()) {
            var3.saveOldPosition();
         }
      }

   }

   public void scrollBy(int var1, int var2) {
      RecyclerView.LayoutManager var3 = this.mLayout;
      if (var3 == null) {
         Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      } else if (!this.mLayoutSuppressed) {
         boolean var4 = var3.canScrollHorizontally();
         boolean var5 = this.mLayout.canScrollVertically();
         if (var4 || var5) {
            if (!var4) {
               var1 = 0;
            }

            if (!var5) {
               var2 = 0;
            }

            this.scrollByInternal(var1, var2, (MotionEvent)null);
         }

      }
   }

   boolean scrollByInternal(int var1, int var2, MotionEvent var3) {
      this.consumePendingUpdateOperations();
      RecyclerView.Adapter var4 = this.mAdapter;
      boolean var5 = true;
      int var6;
      int var7;
      int var8;
      int var9;
      int[] var15;
      if (var4 != null) {
         var15 = this.mReusableIntPair;
         var15[0] = 0;
         var15[1] = 0;
         this.scrollStep(var1, var2, var15);
         var15 = this.mReusableIntPair;
         var6 = var15[0];
         var7 = var15[1];
         var8 = var7;
         var9 = var6;
         var6 = var1 - var6;
         var7 = var2 - var7;
      } else {
         var8 = 0;
         var9 = 0;
         var6 = 0;
         var7 = 0;
      }

      if (!this.mItemDecorations.isEmpty()) {
         this.invalidate();
      }

      var15 = this.mReusableIntPair;
      var15[0] = 0;
      var15[1] = 0;
      this.dispatchNestedScroll(var9, var8, var6, var7, this.mScrollOffset, 0, var15);
      var15 = this.mReusableIntPair;
      int var10 = var15[0];
      int var11 = var15[1];
      int var12 = this.mLastTouchX;
      var15 = this.mScrollOffset;
      this.mLastTouchX = var12 - var15[0];
      this.mLastTouchY -= var15[1];
      if (var3 != null) {
         var3.offsetLocation((float)var15[0], (float)var15[1]);
      }

      int[] var13 = this.mNestedOffsets;
      var12 = var13[0];
      var15 = this.mScrollOffset;
      var13[0] = var12 + var15[0];
      var13[1] += var15[1];
      if (this.getOverScrollMode() != 2) {
         if (var3 != null && !MotionEventCompat.isFromSource(var3, 8194)) {
            this.pullGlows(var3.getX(), (float)(var6 - var10), var3.getY(), (float)(var7 - var11));
         }

         this.considerReleasingGlowsOnScroll(var1, var2);
      }

      if (var9 != 0 || var8 != 0) {
         this.dispatchOnScrolled(var9, var8);
      }

      if (!this.awakenScrollBars()) {
         this.invalidate();
      }

      boolean var14 = var5;
      if (var9 == 0) {
         if (var8 != 0) {
            var14 = var5;
         } else {
            var14 = false;
         }
      }

      return var14;
   }

   void scrollStep(int var1, int var2, int[] var3) {
      this.startInterceptRequestLayout();
      this.onEnterLayoutOrScroll();
      TraceCompat.beginSection("RV Scroll");
      this.fillRemainingScrollValues(this.mState);
      if (var1 != 0) {
         var1 = this.mLayout.scrollHorizontallyBy(var1, this.mRecycler, this.mState);
      } else {
         var1 = 0;
      }

      if (var2 != 0) {
         var2 = this.mLayout.scrollVerticallyBy(var2, this.mRecycler, this.mState);
      } else {
         var2 = 0;
      }

      TraceCompat.endSection();
      this.repositionShadowingViews();
      this.onExitLayoutOrScroll();
      this.stopInterceptRequestLayout(false);
      if (var3 != null) {
         var3[0] = var1;
         var3[1] = var2;
      }

   }

   public void scrollTo(int var1, int var2) {
      Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
   }

   public void scrollToPosition(int var1) {
      if (!this.mLayoutSuppressed) {
         this.stopScroll();
         RecyclerView.LayoutManager var2 = this.mLayout;
         if (var2 == null) {
            Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
         } else {
            var2.scrollToPosition(var1);
            this.awakenScrollBars();
         }
      }
   }

   public void sendAccessibilityEventUnchecked(AccessibilityEvent var1) {
      if (!this.shouldDeferAccessibilityEvent(var1)) {
         super.sendAccessibilityEventUnchecked(var1);
      }
   }

   public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate var1) {
      this.mAccessibilityDelegate = var1;
      ViewCompat.setAccessibilityDelegate(this, this.mAccessibilityDelegate);
   }

   public void setAdapter(RecyclerView.Adapter var1) {
      this.setLayoutFrozen(false);
      this.setAdapterInternal(var1, false, true);
      this.processDataSetCompletelyChanged(false);
      this.requestLayout();
   }

   public void setBottomGlowOffset(int var1) {
      this.bottomGlowOffset = var1;
   }

   public void setChildDrawingOrderCallback(RecyclerView.ChildDrawingOrderCallback var1) {
      if (var1 != this.mChildDrawingOrderCallback) {
         this.mChildDrawingOrderCallback = var1;
         boolean var2;
         if (this.mChildDrawingOrderCallback != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.setChildrenDrawingOrderEnabled(var2);
      }
   }

   boolean setChildImportantForAccessibilityInternal(RecyclerView.ViewHolder var1, int var2) {
      if (this.isComputingLayout()) {
         var1.mPendingAccessibilityState = var2;
         this.mPendingAccessibilityImportanceChange.add(var1);
         return false;
      } else {
         ViewCompat.setImportantForAccessibility(var1.itemView, var2);
         return true;
      }
   }

   public void setClipToPadding(boolean var1) {
      if (var1 != this.mClipToPadding) {
         this.invalidateGlows();
      }

      this.mClipToPadding = var1;
      super.setClipToPadding(var1);
      if (this.mFirstLayoutComplete) {
         this.requestLayout();
      }

   }

   public void setEdgeEffectFactory(RecyclerView.EdgeEffectFactory var1) {
      Preconditions.checkNotNull(var1);
      this.mEdgeEffectFactory = var1;
      this.invalidateGlows();
   }

   public void setGlowColor(int var1) {
      this.glowColor = var1;
   }

   public void setHasFixedSize(boolean var1) {
      this.mHasFixedSize = var1;
   }

   public void setItemAnimator(RecyclerView.ItemAnimator var1) {
      RecyclerView.ItemAnimator var2 = this.mItemAnimator;
      if (var2 != null) {
         var2.endAnimations();
         this.mItemAnimator.setListener((RecyclerView.ItemAnimator.ItemAnimatorListener)null);
      }

      this.mItemAnimator = var1;
      var1 = this.mItemAnimator;
      if (var1 != null) {
         var1.setListener(this.mItemAnimatorListener);
      }

   }

   public void setItemViewCacheSize(int var1) {
      this.mRecycler.setViewCacheSize(var1);
   }

   @Deprecated
   public void setLayoutFrozen(boolean var1) {
      this.suppressLayout(var1);
   }

   public void setLayoutManager(RecyclerView.LayoutManager var1) {
      if (var1 != this.mLayout) {
         this.stopScroll();
         if (this.mLayout != null) {
            RecyclerView.ItemAnimator var2 = this.mItemAnimator;
            if (var2 != null) {
               var2.endAnimations();
            }

            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            this.mRecycler.clear();
            if (this.mIsAttached) {
               this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
            }

            this.mLayout.setRecyclerView((RecyclerView)null);
            this.mLayout = null;
         } else {
            this.mRecycler.clear();
         }

         this.mChildHelper.removeAllViewsUnfiltered();
         this.mLayout = var1;
         if (var1 != null) {
            if (var1.mRecyclerView != null) {
               StringBuilder var3 = new StringBuilder();
               var3.append("LayoutManager ");
               var3.append(var1);
               var3.append(" is already attached to a RecyclerView:");
               var3.append(var1.mRecyclerView.exceptionLabel());
               throw new IllegalArgumentException(var3.toString());
            }

            this.mLayout.setRecyclerView(this);
            if (this.mIsAttached) {
               this.mLayout.dispatchAttachedToWindow(this);
            }
         }

         this.mRecycler.updateViewCacheSize();
         this.requestLayout();
      }
   }

   @Deprecated
   public void setLayoutTransition(LayoutTransition var1) {
      if (var1 == null) {
         super.setLayoutTransition((LayoutTransition)null);
      } else {
         throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
      }
   }

   public void setNestedScrollingEnabled(boolean var1) {
      this.getScrollingChildHelper().setNestedScrollingEnabled(var1);
   }

   public void setOnFlingListener(RecyclerView.OnFlingListener var1) {
      this.mOnFlingListener = var1;
   }

   @Deprecated
   public void setOnScrollListener(RecyclerView.OnScrollListener var1) {
      this.mScrollListener = var1;
   }

   public void setPreserveFocusAfterLayout(boolean var1) {
      this.mPreserveFocusAfterLayout = var1;
   }

   public void setRecycledViewPool(RecyclerView.RecycledViewPool var1) {
      this.mRecycler.setRecycledViewPool(var1);
   }

   public void setRecyclerListener(RecyclerView.RecyclerListener var1) {
      this.mRecyclerListener = var1;
   }

   void setScrollState(int var1) {
      if (var1 != this.mScrollState) {
         this.mScrollState = var1;
         if (var1 != 2) {
            this.stopScrollersInternal();
         }

         this.dispatchOnScrollStateChanged(var1);
      }
   }

   public void setScrollingTouchSlop(int var1) {
      ViewConfiguration var2 = ViewConfiguration.get(this.getContext());
      if (var1 != 0) {
         if (var1 == 1) {
            this.mTouchSlop = var2.getScaledPagingTouchSlop();
            return;
         }

         StringBuilder var3 = new StringBuilder();
         var3.append("setScrollingTouchSlop(): bad argument constant ");
         var3.append(var1);
         var3.append("; using default value");
         Log.w("RecyclerView", var3.toString());
      }

      this.mTouchSlop = var2.getScaledTouchSlop();
   }

   public void setTopGlowOffset(int var1) {
      this.topGlowOffset = var1;
   }

   public void setViewCacheExtension(RecyclerView.ViewCacheExtension var1) {
      this.mRecycler.setViewCacheExtension(var1);
   }

   boolean shouldDeferAccessibilityEvent(AccessibilityEvent var1) {
      if (this.isComputingLayout()) {
         int var2;
         if (var1 != null) {
            var2 = AccessibilityEventCompat.getContentChangeTypes(var1);
         } else {
            var2 = 0;
         }

         int var3 = var2;
         if (var2 == 0) {
            var3 = 0;
         }

         this.mEatenAccessibilityChangeFlags |= var3;
         return true;
      } else {
         return false;
      }
   }

   public void smoothScrollBy(int var1, int var2) {
      this.smoothScrollBy(var1, var2, (Interpolator)null);
   }

   public void smoothScrollBy(int var1, int var2, Interpolator var3) {
      RecyclerView.LayoutManager var4 = this.mLayout;
      if (var4 == null) {
         Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      } else if (!this.mLayoutSuppressed) {
         if (!var4.canScrollHorizontally()) {
            var1 = 0;
         }

         if (!this.mLayout.canScrollVertically()) {
            var2 = 0;
         }

         if (var1 != 0 || var2 != 0) {
            this.mViewFlinger.smoothScrollBy(var1, var2, Integer.MIN_VALUE, var3);
         }

      }
   }

   public void smoothScrollToPosition(int var1) {
      if (!this.mLayoutSuppressed) {
         RecyclerView.LayoutManager var2 = this.mLayout;
         if (var2 == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
         } else {
            var2.smoothScrollToPosition(this, this.mState, var1);
         }
      }
   }

   void startInterceptRequestLayout() {
      ++this.mInterceptRequestLayoutDepth;
      if (this.mInterceptRequestLayoutDepth == 1 && !this.mLayoutSuppressed) {
         this.mLayoutWasDefered = false;
      }

   }

   public boolean startNestedScroll(int var1) {
      return this.getScrollingChildHelper().startNestedScroll(var1);
   }

   public boolean startNestedScroll(int var1, int var2) {
      return this.getScrollingChildHelper().startNestedScroll(var1, var2);
   }

   void stopInterceptRequestLayout(boolean var1) {
      if (this.mInterceptRequestLayoutDepth < 1) {
         this.mInterceptRequestLayoutDepth = 1;
      }

      if (!var1 && !this.mLayoutSuppressed) {
         this.mLayoutWasDefered = false;
      }

      if (this.mInterceptRequestLayoutDepth == 1) {
         if (var1 && this.mLayoutWasDefered && !this.mLayoutSuppressed && this.mLayout != null && this.mAdapter != null) {
            this.dispatchLayout();
         }

         if (!this.mLayoutSuppressed) {
            this.mLayoutWasDefered = false;
         }
      }

      --this.mInterceptRequestLayoutDepth;
   }

   public void stopNestedScroll() {
      this.getScrollingChildHelper().stopNestedScroll();
   }

   public void stopNestedScroll(int var1) {
      this.getScrollingChildHelper().stopNestedScroll(var1);
   }

   public void stopScroll() {
      this.setScrollState(0);
      this.stopScrollersInternal();
   }

   public final void suppressLayout(boolean var1) {
      if (var1 != this.mLayoutSuppressed) {
         this.assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
         if (!var1) {
            this.mLayoutSuppressed = false;
            if (this.mLayoutWasDefered && this.mLayout != null && this.mAdapter != null) {
               this.requestLayout();
            }

            this.mLayoutWasDefered = false;
         } else {
            long var2 = SystemClock.uptimeMillis();
            this.onTouchEvent(MotionEvent.obtain(var2, var2, 3, 0.0F, 0.0F, 0));
            this.mLayoutSuppressed = true;
            this.mIgnoreMotionEventTillDown = true;
            this.stopScroll();
         }
      }

   }

   public void swapAdapter(RecyclerView.Adapter var1, boolean var2) {
      this.setLayoutFrozen(false);
      this.setAdapterInternal(var1, true, var2);
      this.processDataSetCompletelyChanged(true);
      this.requestLayout();
   }

   void viewRangeUpdate(int var1, int var2, Object var3) {
      int var4 = this.mChildHelper.getUnfilteredChildCount();

      for(int var5 = 0; var5 < var4; ++var5) {
         View var6 = this.mChildHelper.getUnfilteredChildAt(var5);
         RecyclerView.ViewHolder var7 = getChildViewHolderInt(var6);
         if (var7 != null && !var7.shouldIgnore()) {
            int var8 = var7.mPosition;
            if (var8 >= var1 && var8 < var1 + var2) {
               var7.addFlags(2);
               var7.addChangePayload(var3);
               ((RecyclerView.LayoutParams)var6.getLayoutParams()).mInsetsDirty = true;
            }
         }
      }

      this.mRecycler.viewRangeUpdate(var1, var2);
   }

   public abstract static class Adapter {
      private boolean mHasStableIds = false;
      private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();

      public final void bindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         var1.mPosition = var2;
         if (this.hasStableIds()) {
            var1.mItemId = this.getItemId(var2);
         }

         var1.setFlags(1, 519);
         TraceCompat.beginSection("RV OnBindView");
         this.onBindViewHolder(var1, var2, var1.getUnmodifiedPayloads());
         var1.clearPayload();
         android.view.ViewGroup.LayoutParams var3 = var1.itemView.getLayoutParams();
         if (var3 instanceof RecyclerView.LayoutParams) {
            ((RecyclerView.LayoutParams)var3).mInsetsDirty = true;
         }

         TraceCompat.endSection();
      }

      public final RecyclerView.ViewHolder createViewHolder(ViewGroup var1, int var2) {
         RecyclerView.ViewHolder var5;
         try {
            TraceCompat.beginSection("RV CreateView");
            var5 = this.onCreateViewHolder(var1, var2);
            if (var5.itemView.getParent() != null) {
               IllegalStateException var6 = new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
               throw var6;
            }

            var5.mItemViewType = var2;
         } finally {
            TraceCompat.endSection();
         }

         return var5;
      }

      public abstract int getItemCount();

      public long getItemId(int var1) {
         return -1L;
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public final boolean hasObservers() {
         return this.mObservable.hasObservers();
      }

      public final boolean hasStableIds() {
         return this.mHasStableIds;
      }

      public void notifyDataSetChanged() {
         this.mObservable.notifyChanged();
      }

      public void notifyItemChanged(int var1) {
         this.mObservable.notifyItemRangeChanged(var1, 1);
      }

      public void notifyItemChanged(int var1, Object var2) {
         this.mObservable.notifyItemRangeChanged(var1, 1, var2);
      }

      public void notifyItemInserted(int var1) {
         this.mObservable.notifyItemRangeInserted(var1, 1);
      }

      public void notifyItemMoved(int var1, int var2) {
         this.mObservable.notifyItemMoved(var1, var2);
      }

      public void notifyItemRangeChanged(int var1, int var2) {
         this.mObservable.notifyItemRangeChanged(var1, var2);
      }

      public void notifyItemRangeChanged(int var1, int var2, Object var3) {
         this.mObservable.notifyItemRangeChanged(var1, var2, var3);
      }

      public void notifyItemRangeInserted(int var1, int var2) {
         this.mObservable.notifyItemRangeInserted(var1, var2);
      }

      public void notifyItemRangeRemoved(int var1, int var2) {
         this.mObservable.notifyItemRangeRemoved(var1, var2);
      }

      public void notifyItemRemoved(int var1) {
         this.mObservable.notifyItemRangeRemoved(var1, 1);
      }

      public void onAttachedToRecyclerView(RecyclerView var1) {
      }

      public abstract void onBindViewHolder(RecyclerView.ViewHolder var1, int var2);

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2, List var3) {
         this.onBindViewHolder(var1, var2);
      }

      public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2);

      public void onDetachedFromRecyclerView(RecyclerView var1) {
      }

      public boolean onFailedToRecycleView(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
      }

      public void onViewDetachedFromWindow(RecyclerView.ViewHolder var1) {
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
      }

      public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver var1) {
         this.mObservable.registerObserver(var1);
      }

      public void setHasStableIds(boolean var1) {
         if (!this.hasObservers()) {
            this.mHasStableIds = var1;
         } else {
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
         }
      }

      public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver var1) {
         this.mObservable.unregisterObserver(var1);
      }
   }

   static class AdapterDataObservable extends Observable {
      public boolean hasObservers() {
         return super.mObservers.isEmpty() ^ true;
      }

      public void notifyChanged() {
         for(int var1 = super.mObservers.size() - 1; var1 >= 0; --var1) {
            ((RecyclerView.AdapterDataObserver)super.mObservers.get(var1)).onChanged();
         }

      }

      public void notifyItemMoved(int var1, int var2) {
         for(int var3 = super.mObservers.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.AdapterDataObserver)super.mObservers.get(var3)).onItemRangeMoved(var1, var2, 1);
         }

      }

      public void notifyItemRangeChanged(int var1, int var2) {
         this.notifyItemRangeChanged(var1, var2, (Object)null);
      }

      public void notifyItemRangeChanged(int var1, int var2, Object var3) {
         for(int var4 = super.mObservers.size() - 1; var4 >= 0; --var4) {
            ((RecyclerView.AdapterDataObserver)super.mObservers.get(var4)).onItemRangeChanged(var1, var2, var3);
         }

      }

      public void notifyItemRangeInserted(int var1, int var2) {
         for(int var3 = super.mObservers.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.AdapterDataObserver)super.mObservers.get(var3)).onItemRangeInserted(var1, var2);
         }

      }

      public void notifyItemRangeRemoved(int var1, int var2) {
         for(int var3 = super.mObservers.size() - 1; var3 >= 0; --var3) {
            ((RecyclerView.AdapterDataObserver)super.mObservers.get(var3)).onItemRangeRemoved(var1, var2);
         }

      }
   }

   public abstract static class AdapterDataObserver {
      public void onChanged() {
      }

      public void onItemRangeChanged(int var1, int var2) {
      }

      public void onItemRangeChanged(int var1, int var2, Object var3) {
         this.onItemRangeChanged(var1, var2);
      }

      public void onItemRangeInserted(int var1, int var2) {
      }

      public void onItemRangeMoved(int var1, int var2, int var3) {
      }

      public void onItemRangeRemoved(int var1, int var2) {
      }
   }

   public interface ChildDrawingOrderCallback {
      int onGetChildDrawingOrder(int var1, int var2);
   }

   public static class EdgeEffectFactory {
      protected EdgeEffect createEdgeEffect(RecyclerView var1, int var2) {
         return new EdgeEffect(var1.getContext());
      }
   }

   public abstract static class ItemAnimator {
      public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
      public static final int FLAG_CHANGED = 2;
      public static final int FLAG_INVALIDATED = 4;
      public static final int FLAG_MOVED = 2048;
      public static final int FLAG_REMOVED = 8;
      private long mAddDuration = 120L;
      private long mChangeDuration = 250L;
      private ArrayList mFinishedListeners = new ArrayList();
      private RecyclerView.ItemAnimator.ItemAnimatorListener mListener = null;
      private long mMoveDuration = 250L;
      private long mRemoveDuration = 120L;

      static int buildAdapterChangeFlagsForAnimations(RecyclerView.ViewHolder var0) {
         int var1 = var0.mFlags & 14;
         if (var0.isInvalid()) {
            return 4;
         } else {
            int var2 = var1;
            if ((var1 & 4) == 0) {
               int var3 = var0.getOldPosition();
               int var4 = var0.getAdapterPosition();
               var2 = var1;
               if (var3 != -1) {
                  var2 = var1;
                  if (var4 != -1) {
                     var2 = var1;
                     if (var3 != var4) {
                        var2 = var1 | 2048;
                     }
                  }
               }
            }

            return var2;
         }
      }

      public abstract boolean animateAppearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      public abstract boolean animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, RecyclerView.ItemAnimator.ItemHolderInfo var3, RecyclerView.ItemAnimator.ItemHolderInfo var4);

      public abstract boolean animateDisappearance(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      public abstract boolean animatePersistence(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      public abstract boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1);

      public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder var1, List var2) {
         return this.canReuseUpdatedViewHolder(var1);
      }

      public final void dispatchAnimationFinished(RecyclerView.ViewHolder var1) {
         this.onAnimationFinished(var1);
         RecyclerView.ItemAnimator.ItemAnimatorListener var2 = this.mListener;
         if (var2 != null) {
            var2.onAnimationFinished(var1);
         }

      }

      public final void dispatchAnimationStarted(RecyclerView.ViewHolder var1) {
         this.onAnimationStarted(var1);
      }

      public final void dispatchAnimationsFinished() {
         int var1 = this.mFinishedListeners.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ((RecyclerView.ItemAnimator.ItemAnimatorFinishedListener)this.mFinishedListeners.get(var2)).onAnimationsFinished();
         }

         this.mFinishedListeners.clear();
      }

      public abstract void endAnimation(RecyclerView.ViewHolder var1);

      public abstract void endAnimations();

      public long getAddDuration() {
         return this.mAddDuration;
      }

      public long getChangeDuration() {
         return this.mChangeDuration;
      }

      public long getMoveDuration() {
         return this.mMoveDuration;
      }

      public long getRemoveDuration() {
         return this.mRemoveDuration;
      }

      public abstract boolean isRunning();

      public final boolean isRunning(RecyclerView.ItemAnimator.ItemAnimatorFinishedListener var1) {
         boolean var2 = this.isRunning();
         if (var1 != null) {
            if (!var2) {
               var1.onAnimationsFinished();
            } else {
               this.mFinishedListeners.add(var1);
            }
         }

         return var2;
      }

      public RecyclerView.ItemAnimator.ItemHolderInfo obtainHolderInfo() {
         return new RecyclerView.ItemAnimator.ItemHolderInfo();
      }

      public void onAnimationFinished(RecyclerView.ViewHolder var1) {
      }

      public void onAnimationStarted(RecyclerView.ViewHolder var1) {
      }

      public RecyclerView.ItemAnimator.ItemHolderInfo recordPostLayoutInformation(RecyclerView.State var1, RecyclerView.ViewHolder var2) {
         RecyclerView.ItemAnimator.ItemHolderInfo var3 = this.obtainHolderInfo();
         var3.setFrom(var2);
         return var3;
      }

      public RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation(RecyclerView.State var1, RecyclerView.ViewHolder var2, int var3, List var4) {
         RecyclerView.ItemAnimator.ItemHolderInfo var5 = this.obtainHolderInfo();
         var5.setFrom(var2);
         return var5;
      }

      public abstract void runPendingAnimations();

      public void setAddDuration(long var1) {
         this.mAddDuration = var1;
      }

      public void setChangeDuration(long var1) {
         this.mChangeDuration = var1;
      }

      void setListener(RecyclerView.ItemAnimator.ItemAnimatorListener var1) {
         this.mListener = var1;
      }

      public void setMoveDuration(long var1) {
         this.mMoveDuration = var1;
      }

      public void setRemoveDuration(long var1) {
         this.mRemoveDuration = var1;
      }

      public interface ItemAnimatorFinishedListener {
         void onAnimationsFinished();
      }

      interface ItemAnimatorListener {
         void onAnimationFinished(RecyclerView.ViewHolder var1);
      }

      public static class ItemHolderInfo {
         public int bottom;
         public int left;
         public int right;
         public int top;

         public RecyclerView.ItemAnimator.ItemHolderInfo setFrom(RecyclerView.ViewHolder var1) {
            this.setFrom(var1, 0);
            return this;
         }

         public RecyclerView.ItemAnimator.ItemHolderInfo setFrom(RecyclerView.ViewHolder var1, int var2) {
            View var3 = var1.itemView;
            this.left = var3.getLeft();
            this.top = var3.getTop();
            this.right = var3.getRight();
            this.bottom = var3.getBottom();
            return this;
         }
      }
   }

   private class ItemAnimatorRestoreListener implements RecyclerView.ItemAnimator.ItemAnimatorListener {
      ItemAnimatorRestoreListener() {
      }

      public void onAnimationFinished(RecyclerView.ViewHolder var1) {
         var1.setIsRecyclable(true);
         if (var1.mShadowedHolder != null && var1.mShadowingHolder == null) {
            var1.mShadowedHolder = null;
         }

         var1.mShadowingHolder = null;
         if (!var1.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(var1.itemView) && var1.isTmpDetached()) {
            RecyclerView.this.removeDetachedView(var1.itemView, false);
         }

      }
   }

   public abstract static class ItemDecoration {
      @Deprecated
      public void getItemOffsets(Rect var1, int var2, RecyclerView var3) {
         var1.set(0, 0, 0, 0);
      }

      public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
         this.getItemOffsets(var1, ((RecyclerView.LayoutParams)var2.getLayoutParams()).getViewLayoutPosition(), var3);
      }

      @Deprecated
      public void onDraw(Canvas var1, RecyclerView var2) {
      }

      public void onDraw(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
         this.onDraw(var1, var2);
      }

      @Deprecated
      public void onDrawOver(Canvas var1, RecyclerView var2) {
      }

      public void onDrawOver(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
         this.onDrawOver(var1, var2);
      }
   }

   public abstract static class LayoutManager {
      boolean mAutoMeasure;
      ChildHelper mChildHelper;
      private int mHeight;
      private int mHeightMode;
      ViewBoundsCheck mHorizontalBoundCheck;
      private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback = new ViewBoundsCheck.Callback() {
         public View getChildAt(int var1) {
            return LayoutManager.this.getChildAt(var1);
         }

         public int getChildEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedRight(var1) + var2.rightMargin;
         }

         public int getChildStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedLeft(var1) - var2.leftMargin;
         }

         public int getParentEnd() {
            return LayoutManager.this.getWidth() - LayoutManager.this.getPaddingRight();
         }

         public int getParentStart() {
            return LayoutManager.this.getPaddingLeft();
         }
      };
      boolean mIsAttachedToWindow;
      private boolean mItemPrefetchEnabled;
      private boolean mMeasurementCacheEnabled;
      int mPrefetchMaxCountObserved;
      boolean mPrefetchMaxObservedInInitialPrefetch;
      RecyclerView mRecyclerView;
      boolean mRequestedSimpleAnimations;
      RecyclerView.SmoothScroller mSmoothScroller;
      ViewBoundsCheck mVerticalBoundCheck;
      private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback = new ViewBoundsCheck.Callback() {
         public View getChildAt(int var1) {
            return LayoutManager.this.getChildAt(var1);
         }

         public int getChildEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedBottom(var1) + var2.bottomMargin;
         }

         public int getChildStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return LayoutManager.this.getDecoratedTop(var1) - var2.topMargin;
         }

         public int getParentEnd() {
            return LayoutManager.this.getHeight() - LayoutManager.this.getPaddingBottom();
         }

         public int getParentStart() {
            return LayoutManager.this.getPaddingTop();
         }
      };
      private int mWidth;
      private int mWidthMode;

      public LayoutManager() {
         this.mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
         this.mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
         this.mRequestedSimpleAnimations = false;
         this.mIsAttachedToWindow = false;
         this.mAutoMeasure = false;
         this.mMeasurementCacheEnabled = true;
         this.mItemPrefetchEnabled = true;
      }

      private void addViewInt(View var1, int var2, boolean var3) {
         RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var1);
         if (!var3 && !var4.isRemoved()) {
            this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(var4);
         } else {
            this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(var4);
         }

         RecyclerView.LayoutParams var5 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         if (!var4.wasReturnedFromScrap() && !var4.isScrap()) {
            if (var1.getParent() == this.mRecyclerView) {
               int var6 = this.mChildHelper.indexOfChild(var1);
               int var7 = var2;
               if (var2 == -1) {
                  var7 = this.mChildHelper.getChildCount();
               }

               if (var6 == -1) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
                  var8.append(this.mRecyclerView.indexOfChild(var1));
                  var8.append(this.mRecyclerView.exceptionLabel());
                  throw new IllegalStateException(var8.toString());
               }

               if (var6 != var7) {
                  this.mRecyclerView.mLayout.moveView(var6, var7);
               }
            } else {
               this.mChildHelper.addView(var1, var2, false);
               var5.mInsetsDirty = true;
               RecyclerView.SmoothScroller var9 = this.mSmoothScroller;
               if (var9 != null && var9.isRunning()) {
                  this.mSmoothScroller.onChildAttachedToWindow(var1);
               }
            }
         } else {
            if (var4.isScrap()) {
               var4.unScrap();
            } else {
               var4.clearReturnedFromScrapFlag();
            }

            this.mChildHelper.attachViewToParent(var1, var2, var1.getLayoutParams(), false);
         }

         if (var5.mPendingInvalidate) {
            var4.itemView.invalidate();
            var5.mPendingInvalidate = false;
         }

      }

      public static int chooseSize(int var0, int var1, int var2) {
         int var3 = MeasureSpec.getMode(var0);
         var0 = MeasureSpec.getSize(var0);
         if (var3 != Integer.MIN_VALUE) {
            if (var3 != 1073741824) {
               var0 = Math.max(var1, var2);
            }

            return var0;
         } else {
            return Math.min(var0, Math.max(var1, var2));
         }
      }

      private void detachViewInternal(int var1, View var2) {
         this.mChildHelper.detachViewFromParent(var1);
      }

      public static int getChildMeasureSpec(int var0, int var1, int var2, int var3, boolean var4) {
         byte var5;
         label66: {
            var5 = 0;
            int var6 = Math.max(0, var0 - var2);
            if (var4) {
               if (var3 < 0) {
                  if (var3 == -1) {
                     if (var1 == Integer.MIN_VALUE || var1 != 0 && var1 == 1073741824) {
                        var0 = var6;
                     } else {
                        var1 = 0;
                        var0 = 0;
                     }

                     var2 = var0;
                     var0 = var1;
                     return MeasureSpec.makeMeasureSpec(var2, var0);
                  }
                  break label66;
               }
            } else if (var3 < 0) {
               if (var3 == -1) {
                  var0 = var1;
                  var2 = var6;
                  return MeasureSpec.makeMeasureSpec(var2, var0);
               }

               if (var3 == -2) {
                  if (var1 != Integer.MIN_VALUE) {
                     var2 = var6;
                     var0 = var5;
                     if (var1 != 1073741824) {
                        return MeasureSpec.makeMeasureSpec(var2, var0);
                     }
                  }

                  var0 = Integer.MIN_VALUE;
                  var2 = var6;
                  return MeasureSpec.makeMeasureSpec(var2, var0);
               }
               break label66;
            }

            var2 = var3;
            var0 = 1073741824;
            return MeasureSpec.makeMeasureSpec(var2, var0);
         }

         var2 = 0;
         var0 = var5;
         return MeasureSpec.makeMeasureSpec(var2, var0);
      }

      @Deprecated
      public static int getChildMeasureSpec(int var0, int var1, int var2, boolean var3) {
         byte var4;
         label35: {
            label24: {
               var4 = 0;
               var0 = Math.max(0, var0 - var1);
               if (var3) {
                  if (var2 < 0) {
                     break label35;
                  }
               } else if (var2 < 0) {
                  if (var2 != -1) {
                     if (var2 == -2) {
                        var1 = Integer.MIN_VALUE;
                        return MeasureSpec.makeMeasureSpec(var0, var1);
                     }
                     break label35;
                  }
                  break label24;
               }

               var0 = var2;
            }

            var1 = 1073741824;
            return MeasureSpec.makeMeasureSpec(var0, var1);
         }

         var0 = 0;
         var1 = var4;
         return MeasureSpec.makeMeasureSpec(var0, var1);
      }

      private int[] getChildRectangleOnScreenScrollAmount(View var1, Rect var2) {
         int var3 = this.getPaddingLeft();
         int var4 = this.getPaddingTop();
         int var5 = this.getWidth();
         int var6 = this.getPaddingRight();
         int var7 = this.getHeight();
         int var8 = this.getPaddingBottom();
         int var9 = var1.getLeft() + var2.left - var1.getScrollX();
         int var10 = var1.getTop() + var2.top - var1.getScrollY();
         int var11 = var2.width();
         int var12 = var2.height();
         int var13 = var9 - var3;
         var3 = Math.min(0, var13);
         int var14 = var10 - var4;
         var4 = Math.min(0, var14);
         var5 = var11 + var9 - (var5 - var6);
         var9 = Math.max(0, var5);
         var10 = Math.max(0, var12 + var10 - (var7 - var8));
         if (this.getLayoutDirection() == 1) {
            if (var9 != 0) {
               var3 = var9;
            } else {
               var3 = Math.max(var3, var5);
            }
         } else if (var3 == 0) {
            var3 = Math.min(var13, var9);
         }

         if (var4 == 0) {
            var4 = Math.min(var14, var10);
         }

         return new int[]{var3, var4};
      }

      private boolean isFocusedChildVisibleAfterScrolling(RecyclerView var1, int var2, int var3) {
         View var4 = var1.getFocusedChild();
         if (var4 == null) {
            return false;
         } else {
            int var5 = this.getPaddingLeft();
            int var6 = this.getPaddingTop();
            int var7 = this.getWidth();
            int var8 = this.getPaddingRight();
            int var9 = this.getHeight();
            int var10 = this.getPaddingBottom();
            Rect var11 = this.mRecyclerView.mTempRect;
            this.getDecoratedBoundsWithMargins(var4, var11);
            return var11.left - var2 < var7 - var8 && var11.right - var2 > var5 && var11.top - var3 < var9 - var10 && var11.bottom - var3 > var6;
         }
      }

      private static boolean isMeasurementUpToDate(int var0, int var1, int var2) {
         int var3 = MeasureSpec.getMode(var1);
         var1 = MeasureSpec.getSize(var1);
         boolean var4 = false;
         boolean var5 = false;
         if (var2 > 0 && var0 != var2) {
            return false;
         } else if (var3 != Integer.MIN_VALUE) {
            if (var3 != 0) {
               if (var3 != 1073741824) {
                  return false;
               } else {
                  if (var1 == var0) {
                     var5 = true;
                  }

                  return var5;
               }
            } else {
               return true;
            }
         } else {
            var5 = var4;
            if (var1 >= var0) {
               var5 = true;
            }

            return var5;
         }
      }

      private void scrapOrRecycleView(RecyclerView.Recycler var1, int var2, View var3) {
         RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var3);
         if (!var4.shouldIgnore()) {
            if (var4.isInvalid() && !var4.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
               this.removeViewAt(var2);
               var1.recycleViewHolderInternal(var4);
            } else {
               this.detachViewAt(var2);
               var1.scrapView(var3);
               this.mRecyclerView.mViewInfoStore.onViewDetached(var4);
            }

         }
      }

      public void addDisappearingView(View var1) {
         this.addDisappearingView(var1, -1);
      }

      public void addDisappearingView(View var1, int var2) {
         this.addViewInt(var1, var2, true);
      }

      public void addView(View var1) {
         this.addView(var1, -1);
      }

      public void addView(View var1, int var2) {
         this.addViewInt(var1, var2, false);
      }

      public void assertInLayoutOrScroll(String var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.assertInLayoutOrScroll(var1);
         }

      }

      public void assertNotInLayoutOrScroll(String var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.assertNotInLayoutOrScroll(var1);
         }

      }

      public void attachView(View var1) {
         this.attachView(var1, -1);
      }

      public void attachView(View var1, int var2) {
         this.attachView(var1, var2, (RecyclerView.LayoutParams)var1.getLayoutParams());
      }

      public void attachView(View var1, int var2, RecyclerView.LayoutParams var3) {
         RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var1);
         if (var4.isRemoved()) {
            this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(var4);
         } else {
            this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(var4);
         }

         this.mChildHelper.attachViewToParent(var1, var2, var3, var4.isRemoved());
      }

      public void calculateItemDecorationsForChild(View var1, Rect var2) {
         RecyclerView var3 = this.mRecyclerView;
         if (var3 == null) {
            var2.set(0, 0, 0, 0);
         } else {
            var2.set(var3.getItemDecorInsetsForChild(var1));
         }
      }

      public boolean canScrollHorizontally() {
         return false;
      }

      public boolean canScrollVertically() {
         return false;
      }

      public boolean checkLayoutParams(RecyclerView.LayoutParams var1) {
         boolean var2;
         if (var1 != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void collectAdjacentPrefetchPositions(int var1, int var2, RecyclerView.State var3, RecyclerView.LayoutManager.LayoutPrefetchRegistry var4) {
      }

      public void collectInitialPrefetchPositions(int var1, RecyclerView.LayoutManager.LayoutPrefetchRegistry var2) {
      }

      public int computeHorizontalScrollExtent(RecyclerView.State var1) {
         return 0;
      }

      public int computeHorizontalScrollOffset(RecyclerView.State var1) {
         return 0;
      }

      public int computeHorizontalScrollRange(RecyclerView.State var1) {
         return 0;
      }

      public int computeVerticalScrollExtent(RecyclerView.State var1) {
         return 0;
      }

      public int computeVerticalScrollOffset(RecyclerView.State var1) {
         return 0;
      }

      public int computeVerticalScrollRange(RecyclerView.State var1) {
         return 0;
      }

      public void detachAndScrapAttachedViews(RecyclerView.Recycler var1) {
         for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
            this.scrapOrRecycleView(var1, var2, this.getChildAt(var2));
         }

      }

      public void detachAndScrapView(View var1, RecyclerView.Recycler var2) {
         this.scrapOrRecycleView(var2, this.mChildHelper.indexOfChild(var1), var1);
      }

      public void detachAndScrapViewAt(int var1, RecyclerView.Recycler var2) {
         this.scrapOrRecycleView(var2, var1, this.getChildAt(var1));
      }

      public void detachView(View var1) {
         int var2 = this.mChildHelper.indexOfChild(var1);
         if (var2 >= 0) {
            this.detachViewInternal(var2, var1);
         }

      }

      public void detachViewAt(int var1) {
         this.detachViewInternal(var1, this.getChildAt(var1));
      }

      void dispatchAttachedToWindow(RecyclerView var1) {
         this.mIsAttachedToWindow = true;
         this.onAttachedToWindow(var1);
      }

      void dispatchDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
         this.mIsAttachedToWindow = false;
         this.onDetachedFromWindow(var1, var2);
      }

      public void endAnimation(View var1) {
         RecyclerView.ItemAnimator var2 = this.mRecyclerView.mItemAnimator;
         if (var2 != null) {
            var2.endAnimation(RecyclerView.getChildViewHolderInt(var1));
         }

      }

      public View findContainingItemView(View var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 == null) {
            return null;
         } else {
            var1 = var2.findContainingItemView(var1);
            if (var1 == null) {
               return null;
            } else {
               return this.mChildHelper.isHidden(var1) ? null : var1;
            }
         }
      }

      public View findViewByPosition(int var1) {
         int var2 = this.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.getChildAt(var3);
            RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var4);
            if (var5 != null && var5.getLayoutPosition() == var1 && !var5.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !var5.isRemoved())) {
               return var4;
            }
         }

         return null;
      }

      public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();

      public RecyclerView.LayoutParams generateLayoutParams(Context var1, AttributeSet var2) {
         return new RecyclerView.LayoutParams(var1, var2);
      }

      public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
         if (var1 instanceof RecyclerView.LayoutParams) {
            return new RecyclerView.LayoutParams((RecyclerView.LayoutParams)var1);
         } else {
            return var1 instanceof MarginLayoutParams ? new RecyclerView.LayoutParams((MarginLayoutParams)var1) : new RecyclerView.LayoutParams(var1);
         }
      }

      public int getBaseline() {
         return -1;
      }

      public int getBottomDecorationHeight(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.bottom;
      }

      public View getChildAt(int var1) {
         ChildHelper var2 = this.mChildHelper;
         View var3;
         if (var2 != null) {
            var3 = var2.getChildAt(var1);
         } else {
            var3 = null;
         }

         return var3;
      }

      public int getChildCount() {
         ChildHelper var1 = this.mChildHelper;
         int var2;
         if (var1 != null) {
            var2 = var1.getChildCount();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public boolean getClipToPadding() {
         RecyclerView var1 = this.mRecyclerView;
         boolean var2;
         if (var1 != null && var1.mClipToPadding) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public int getColumnCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
         RecyclerView var5 = this.mRecyclerView;
         byte var3 = 1;
         int var4 = var3;
         if (var5 != null) {
            if (var5.mAdapter == null) {
               var4 = var3;
            } else {
               var4 = var3;
               if (this.canScrollHorizontally()) {
                  var4 = this.mRecyclerView.mAdapter.getItemCount();
               }
            }
         }

         return var4;
      }

      public int getDecoratedBottom(View var1) {
         return var1.getBottom() + this.getBottomDecorationHeight(var1);
      }

      public void getDecoratedBoundsWithMargins(View var1, Rect var2) {
         RecyclerView.getDecoratedBoundsWithMarginsInt(var1, var2);
      }

      public int getDecoratedLeft(View var1) {
         return var1.getLeft() - this.getLeftDecorationWidth(var1);
      }

      public int getDecoratedMeasuredHeight(View var1) {
         Rect var2 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
         return var1.getMeasuredHeight() + var2.top + var2.bottom;
      }

      public int getDecoratedMeasuredWidth(View var1) {
         Rect var2 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
         return var1.getMeasuredWidth() + var2.left + var2.right;
      }

      public int getDecoratedRight(View var1) {
         return var1.getRight() + this.getRightDecorationWidth(var1);
      }

      public int getDecoratedTop(View var1) {
         return var1.getTop() - this.getTopDecorationHeight(var1);
      }

      public View getFocusedChild() {
         RecyclerView var1 = this.mRecyclerView;
         if (var1 == null) {
            return null;
         } else {
            View var2 = var1.getFocusedChild();
            return var2 != null && !this.mChildHelper.isHidden(var2) ? var2 : null;
         }
      }

      public int getHeight() {
         return this.mHeight;
      }

      public int getHeightMode() {
         return this.mHeightMode;
      }

      public int getItemCount() {
         RecyclerView var1 = this.mRecyclerView;
         RecyclerView.Adapter var3;
         if (var1 != null) {
            var3 = var1.getAdapter();
         } else {
            var3 = null;
         }

         int var2;
         if (var3 != null) {
            var2 = var3.getItemCount();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getItemViewType(View var1) {
         return RecyclerView.getChildViewHolderInt(var1).getItemViewType();
      }

      public int getLayoutDirection() {
         return ViewCompat.getLayoutDirection(this.mRecyclerView);
      }

      public int getLeftDecorationWidth(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.left;
      }

      public int getMinimumHeight() {
         return ViewCompat.getMinimumHeight(this.mRecyclerView);
      }

      public int getMinimumWidth() {
         return ViewCompat.getMinimumWidth(this.mRecyclerView);
      }

      public int getPaddingBottom() {
         RecyclerView var1 = this.mRecyclerView;
         int var2;
         if (var1 != null) {
            var2 = var1.getPaddingBottom();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getPaddingEnd() {
         RecyclerView var1 = this.mRecyclerView;
         int var2;
         if (var1 != null) {
            var2 = ViewCompat.getPaddingEnd(var1);
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getPaddingLeft() {
         RecyclerView var1 = this.mRecyclerView;
         int var2;
         if (var1 != null) {
            var2 = var1.getPaddingLeft();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getPaddingRight() {
         RecyclerView var1 = this.mRecyclerView;
         int var2;
         if (var1 != null) {
            var2 = var1.getPaddingRight();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getPaddingStart() {
         RecyclerView var1 = this.mRecyclerView;
         int var2;
         if (var1 != null) {
            var2 = ViewCompat.getPaddingStart(var1);
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getPaddingTop() {
         RecyclerView var1 = this.mRecyclerView;
         int var2;
         if (var1 != null) {
            var2 = var1.getPaddingTop();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getPosition(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).getViewLayoutPosition();
      }

      public int getRightDecorationWidth(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.right;
      }

      public int getRowCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
         RecyclerView var5 = this.mRecyclerView;
         byte var3 = 1;
         int var4 = var3;
         if (var5 != null) {
            if (var5.mAdapter == null) {
               var4 = var3;
            } else {
               var4 = var3;
               if (this.canScrollVertically()) {
                  var4 = this.mRecyclerView.mAdapter.getItemCount();
               }
            }
         }

         return var4;
      }

      public int getSelectionModeForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
         return 0;
      }

      public int getTopDecorationHeight(View var1) {
         return ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets.top;
      }

      public void getTransformedBoundingBox(View var1, boolean var2, Rect var3) {
         if (var2) {
            Rect var4 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
            var3.set(-var4.left, -var4.top, var1.getWidth() + var4.right, var1.getHeight() + var4.bottom);
         } else {
            var3.set(0, 0, var1.getWidth(), var1.getHeight());
         }

         if (this.mRecyclerView != null) {
            Matrix var6 = var1.getMatrix();
            if (var6 != null && !var6.isIdentity()) {
               RectF var5 = this.mRecyclerView.mTempRectF;
               var5.set(var3);
               var6.mapRect(var5);
               var3.set((int)Math.floor((double)var5.left), (int)Math.floor((double)var5.top), (int)Math.ceil((double)var5.right), (int)Math.ceil((double)var5.bottom));
            }
         }

         var3.offset(var1.getLeft(), var1.getTop());
      }

      public int getWidth() {
         return this.mWidth;
      }

      public int getWidthMode() {
         return this.mWidthMode;
      }

      boolean hasFlexibleChildInBothOrientations() {
         int var1 = this.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            android.view.ViewGroup.LayoutParams var3 = this.getChildAt(var2).getLayoutParams();
            if (var3.width < 0 && var3.height < 0) {
               return true;
            }
         }

         return false;
      }

      public boolean hasFocus() {
         RecyclerView var1 = this.mRecyclerView;
         boolean var2;
         if (var1 != null && var1.hasFocus()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void ignoreView(View var1) {
         ViewParent var2 = var1.getParent();
         RecyclerView var3 = this.mRecyclerView;
         if (var2 == var3 && var3.indexOfChild(var1) != -1) {
            RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var1);
            var5.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder(var5);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("View should be fully attached to be ignored");
            var4.append(this.mRecyclerView.exceptionLabel());
            throw new IllegalArgumentException(var4.toString());
         }
      }

      public boolean isAttachedToWindow() {
         return this.mIsAttachedToWindow;
      }

      public boolean isAutoMeasureEnabled() {
         return this.mAutoMeasure;
      }

      public boolean isFocused() {
         RecyclerView var1 = this.mRecyclerView;
         boolean var2;
         if (var1 != null && var1.isFocused()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public final boolean isItemPrefetchEnabled() {
         return this.mItemPrefetchEnabled;
      }

      public boolean isLayoutHierarchical(RecyclerView.Recycler var1, RecyclerView.State var2) {
         return false;
      }

      public boolean isMeasurementCacheEnabled() {
         return this.mMeasurementCacheEnabled;
      }

      public boolean isSmoothScrolling() {
         RecyclerView.SmoothScroller var1 = this.mSmoothScroller;
         boolean var2;
         if (var1 != null && var1.isRunning()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean isViewPartiallyVisible(View var1, boolean var2, boolean var3) {
         if (this.mHorizontalBoundCheck.isViewWithinBoundFlags(var1, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(var1, 24579)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var2 ? var3 : var3 ^ true;
      }

      public void layoutDecorated(View var1, int var2, int var3, int var4, int var5) {
         Rect var6 = ((RecyclerView.LayoutParams)var1.getLayoutParams()).mDecorInsets;
         var1.layout(var2 + var6.left, var3 + var6.top, var4 - var6.right, var5 - var6.bottom);
      }

      public void layoutDecoratedWithMargins(View var1, int var2, int var3, int var4, int var5) {
         RecyclerView.LayoutParams var6 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         Rect var7 = var6.mDecorInsets;
         var1.layout(var2 + var7.left + var6.leftMargin, var3 + var7.top + var6.topMargin, var4 - var7.right - var6.rightMargin, var5 - var7.bottom - var6.bottomMargin);
      }

      public void measureChild(View var1, int var2, int var3) {
         RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         Rect var5 = this.mRecyclerView.getItemDecorInsetsForChild(var1);
         int var6 = var5.left;
         int var7 = var5.right;
         int var8 = var5.top;
         int var9 = var5.bottom;
         var2 = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + var2 + var6 + var7, var4.width, this.canScrollHorizontally());
         var3 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + var3 + var8 + var9, var4.height, this.canScrollVertically());
         if (this.shouldMeasureChild(var1, var2, var3, var4)) {
            var1.measure(var2, var3);
         }

      }

      public void measureChildWithMargins(View var1, int var2, int var3) {
         RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         Rect var5 = this.mRecyclerView.getItemDecorInsetsForChild(var1);
         int var6 = var5.left;
         int var7 = var5.right;
         int var8 = var5.top;
         int var9 = var5.bottom;
         var2 = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + var4.leftMargin + var4.rightMargin + var2 + var6 + var7, var4.width, this.canScrollHorizontally());
         var3 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + var4.topMargin + var4.bottomMargin + var3 + var8 + var9, var4.height, this.canScrollVertically());
         if (this.shouldMeasureChild(var1, var2, var3, var4)) {
            var1.measure(var2, var3);
         }

      }

      public void moveView(int var1, int var2) {
         View var3 = this.getChildAt(var1);
         if (var3 != null) {
            this.detachViewAt(var1);
            this.attachView(var3, var2);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Cannot move a child from non-existing index:");
            var4.append(var1);
            var4.append(this.mRecyclerView.toString());
            throw new IllegalArgumentException(var4.toString());
         }
      }

      public void offsetChildrenHorizontal(int var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.offsetChildrenHorizontal(var1);
         }

      }

      public void offsetChildrenVertical(int var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            var2.offsetChildrenVertical(var1);
         }

      }

      public void onAdapterChanged(RecyclerView.Adapter var1, RecyclerView.Adapter var2) {
      }

      public boolean onAddFocusables(RecyclerView var1, ArrayList var2, int var3, int var4) {
         return false;
      }

      public void onAttachedToWindow(RecyclerView var1) {
      }

      @Deprecated
      public void onDetachedFromWindow(RecyclerView var1) {
      }

      public void onDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
         this.onDetachedFromWindow(var1);
      }

      public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
         return null;
      }

      public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
         RecyclerView var2 = this.mRecyclerView;
         this.onInitializeAccessibilityEvent(var2.mRecycler, var2.mState, var1);
      }

      public void onInitializeAccessibilityEvent(RecyclerView.Recycler var1, RecyclerView.State var2, AccessibilityEvent var3) {
         RecyclerView var6 = this.mRecyclerView;
         if (var6 != null && var3 != null) {
            boolean var4 = true;
            boolean var5 = var4;
            if (!var6.canScrollVertically(1)) {
               var5 = var4;
               if (!this.mRecyclerView.canScrollVertically(-1)) {
                  var5 = var4;
                  if (!this.mRecyclerView.canScrollHorizontally(-1)) {
                     if (this.mRecyclerView.canScrollHorizontally(1)) {
                        var5 = var4;
                     } else {
                        var5 = false;
                     }
                  }
               }
            }

            var3.setScrollable(var5);
            RecyclerView.Adapter var7 = this.mRecyclerView.mAdapter;
            if (var7 != null) {
               var3.setItemCount(var7.getItemCount());
            }
         }

      }

      void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat var1) {
         RecyclerView var2 = this.mRecyclerView;
         this.onInitializeAccessibilityNodeInfo(var2.mRecycler, var2.mState, var1);
      }

      public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler var1, RecyclerView.State var2, AccessibilityNodeInfoCompat var3) {
         if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
            var3.addAction(8192);
            var3.setScrollable(true);
         }

         if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
            var3.addAction(4096);
            var3.setScrollable(true);
         }

         var3.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(this.getRowCountForAccessibility(var1, var2), this.getColumnCountForAccessibility(var1, var2), this.isLayoutHierarchical(var1, var2), this.getSelectionModeForAccessibility(var1, var2)));
      }

      void onInitializeAccessibilityNodeInfoForItem(View var1, AccessibilityNodeInfoCompat var2) {
         RecyclerView.ViewHolder var3 = RecyclerView.getChildViewHolderInt(var1);
         if (var3 != null && !var3.isRemoved() && !this.mChildHelper.isHidden(var3.itemView)) {
            RecyclerView var4 = this.mRecyclerView;
            this.onInitializeAccessibilityNodeInfoForItem(var4.mRecycler, var4.mState, var1, var2);
         }

      }

      public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
         int var5;
         if (this.canScrollVertically()) {
            var5 = this.getPosition(var3);
         } else {
            var5 = 0;
         }

         int var6;
         if (this.canScrollHorizontally()) {
            var6 = this.getPosition(var3);
         } else {
            var6 = 0;
         }

         var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var5, 1, var6, 1, false, false));
      }

      public View onInterceptFocusSearch(View var1, int var2) {
         return null;
      }

      public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      }

      public void onItemsChanged(RecyclerView var1) {
      }

      public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      }

      public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      }

      public void onItemsUpdated(RecyclerView var1, int var2, int var3) {
      }

      public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
         this.onItemsUpdated(var1, var2, var3);
      }

      public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
         Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
      }

      public void onLayoutCompleted(RecyclerView.State var1) {
      }

      public void onMeasure(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4) {
         this.mRecyclerView.defaultOnMeasure(var3, var4);
      }

      @Deprecated
      public boolean onRequestChildFocus(RecyclerView var1, View var2, View var3) {
         boolean var4;
         if (!this.isSmoothScrolling() && !var1.isComputingLayout()) {
            var4 = false;
         } else {
            var4 = true;
         }

         return var4;
      }

      public boolean onRequestChildFocus(RecyclerView var1, RecyclerView.State var2, View var3, View var4) {
         return this.onRequestChildFocus(var1, var3, var4);
      }

      public void onRestoreInstanceState(Parcelable var1) {
      }

      public Parcelable onSaveInstanceState() {
         return null;
      }

      public void onScrollStateChanged(int var1) {
      }

      void onSmoothScrollerStopped(RecyclerView.SmoothScroller var1) {
         if (this.mSmoothScroller == var1) {
            this.mSmoothScroller = null;
         }

      }

      boolean performAccessibilityAction(int var1, Bundle var2) {
         RecyclerView var3 = this.mRecyclerView;
         return this.performAccessibilityAction(var3.mRecycler, var3.mState, var1, var2);
      }

      public boolean performAccessibilityAction(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, Bundle var4) {
         RecyclerView var7 = this.mRecyclerView;
         if (var7 == null) {
            return false;
         } else {
            int var5;
            label37: {
               if (var3 != 4096) {
                  if (var3 != 8192) {
                     var5 = 0;
                  } else {
                     if (var7.canScrollVertically(-1)) {
                        var3 = -(this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
                     } else {
                        var3 = 0;
                     }

                     var5 = var3;
                     if (this.mRecyclerView.canScrollHorizontally(-1)) {
                        var5 = -(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
                        break label37;
                     }
                  }
               } else {
                  if (var7.canScrollVertically(1)) {
                     var3 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                  } else {
                     var3 = 0;
                  }

                  var5 = var3;
                  if (this.mRecyclerView.canScrollHorizontally(1)) {
                     var5 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                     break label37;
                  }
               }

               byte var6 = 0;
               var3 = var5;
               var5 = var6;
            }

            if (var3 == 0 && var5 == 0) {
               return false;
            } else {
               this.mRecyclerView.smoothScrollBy(var5, var3);
               return true;
            }
         }
      }

      boolean performAccessibilityActionForItem(View var1, int var2, Bundle var3) {
         RecyclerView var4 = this.mRecyclerView;
         return this.performAccessibilityActionForItem(var4.mRecycler, var4.mState, var1, var2, var3);
      }

      public boolean performAccessibilityActionForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, int var4, Bundle var5) {
         return false;
      }

      public void postOnAnimation(Runnable var1) {
         RecyclerView var2 = this.mRecyclerView;
         if (var2 != null) {
            ViewCompat.postOnAnimation(var2, var1);
         }

      }

      public void removeAllViews() {
         for(int var1 = this.getChildCount() - 1; var1 >= 0; --var1) {
            this.mChildHelper.removeViewAt(var1);
         }

      }

      public void removeAndRecycleAllViews(RecyclerView.Recycler var1) {
         for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
            if (!RecyclerView.getChildViewHolderInt(this.getChildAt(var2)).shouldIgnore()) {
               this.removeAndRecycleViewAt(var2, var1);
            }
         }

      }

      void removeAndRecycleScrapInt(RecyclerView.Recycler var1) {
         int var2 = var1.getScrapCount();

         for(int var3 = var2 - 1; var3 >= 0; --var3) {
            View var4 = var1.getScrapViewAt(var3);
            RecyclerView.ViewHolder var5 = RecyclerView.getChildViewHolderInt(var4);
            if (!var5.shouldIgnore()) {
               var5.setIsRecyclable(false);
               if (var5.isTmpDetached()) {
                  this.mRecyclerView.removeDetachedView(var4, false);
               }

               RecyclerView.ItemAnimator var6 = this.mRecyclerView.mItemAnimator;
               if (var6 != null) {
                  var6.endAnimation(var5);
               }

               var5.setIsRecyclable(true);
               var1.quickRecycleScrapView(var4);
            }
         }

         var1.clearScrap();
         if (var2 > 0) {
            this.mRecyclerView.invalidate();
         }

      }

      public void removeAndRecycleView(View var1, RecyclerView.Recycler var2) {
         this.removeView(var1);
         var2.recycleView(var1);
      }

      public void removeAndRecycleViewAt(int var1, RecyclerView.Recycler var2) {
         View var3 = this.getChildAt(var1);
         this.removeViewAt(var1);
         var2.recycleView(var3);
      }

      public boolean removeCallbacks(Runnable var1) {
         RecyclerView var2 = this.mRecyclerView;
         return var2 != null ? var2.removeCallbacks(var1) : false;
      }

      public void removeDetachedView(View var1) {
         this.mRecyclerView.removeDetachedView(var1, false);
      }

      public void removeView(View var1) {
         this.mChildHelper.removeView(var1);
      }

      public void removeViewAt(int var1) {
         if (this.getChildAt(var1) != null) {
            this.mChildHelper.removeViewAt(var1);
         }

      }

      public boolean requestChildRectangleOnScreen(RecyclerView var1, View var2, Rect var3, boolean var4) {
         return this.requestChildRectangleOnScreen(var1, var2, var3, var4, false);
      }

      public boolean requestChildRectangleOnScreen(RecyclerView var1, View var2, Rect var3, boolean var4, boolean var5) {
         int[] var8 = this.getChildRectangleOnScreenScrollAmount(var2, var3);
         int var6 = var8[0];
         int var7 = var8[1];
         if ((!var5 || this.isFocusedChildVisibleAfterScrolling(var1, var6, var7)) && (var6 != 0 || var7 != 0)) {
            if (var4) {
               var1.scrollBy(var6, var7);
            } else {
               var1.smoothScrollBy(var6, var7);
            }

            return true;
         } else {
            return false;
         }
      }

      public void requestLayout() {
         RecyclerView var1 = this.mRecyclerView;
         if (var1 != null) {
            var1.requestLayout();
         }

      }

      public void requestSimpleAnimationsInNextLayout() {
         this.mRequestedSimpleAnimations = true;
      }

      public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
         return 0;
      }

      public void scrollToPosition(int var1) {
      }

      public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
         return 0;
      }

      @Deprecated
      public void setAutoMeasureEnabled(boolean var1) {
         this.mAutoMeasure = var1;
      }

      void setExactMeasureSpecsFrom(RecyclerView var1) {
         this.setMeasureSpecs(MeasureSpec.makeMeasureSpec(var1.getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(var1.getHeight(), 1073741824));
      }

      public final void setItemPrefetchEnabled(boolean var1) {
         if (var1 != this.mItemPrefetchEnabled) {
            this.mItemPrefetchEnabled = var1;
            this.mPrefetchMaxCountObserved = 0;
            RecyclerView var2 = this.mRecyclerView;
            if (var2 != null) {
               var2.mRecycler.updateViewCacheSize();
            }
         }

      }

      void setMeasureSpecs(int var1, int var2) {
         this.mWidth = MeasureSpec.getSize(var1);
         this.mWidthMode = MeasureSpec.getMode(var1);
         if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
            this.mWidth = 0;
         }

         this.mHeight = MeasureSpec.getSize(var2);
         this.mHeightMode = MeasureSpec.getMode(var2);
         if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
            this.mHeight = 0;
         }

      }

      public void setMeasuredDimension(int var1, int var2) {
         this.mRecyclerView.setMeasuredDimension(var1, var2);
      }

      public void setMeasuredDimension(Rect var1, int var2, int var3) {
         int var4 = var1.width();
         int var5 = this.getPaddingLeft();
         int var6 = this.getPaddingRight();
         int var7 = var1.height();
         int var8 = this.getPaddingTop();
         int var9 = this.getPaddingBottom();
         this.setMeasuredDimension(chooseSize(var2, var4 + var5 + var6, this.getMinimumWidth()), chooseSize(var3, var7 + var8 + var9, this.getMinimumHeight()));
      }

      void setMeasuredDimensionFromChildren(int var1, int var2) {
         int var3 = this.getChildCount();
         if (var3 == 0) {
            this.mRecyclerView.defaultOnMeasure(var1, var2);
         } else {
            int var4 = 0;
            int var5 = Integer.MAX_VALUE;
            int var6 = Integer.MAX_VALUE;
            int var7 = Integer.MIN_VALUE;

            int var8;
            int var13;
            for(var8 = Integer.MIN_VALUE; var4 < var3; var8 = var13) {
               View var9 = this.getChildAt(var4);
               Rect var10 = this.mRecyclerView.mTempRect;
               this.getDecoratedBoundsWithMargins(var9, var10);
               int var11 = var10.left;
               int var12 = var5;
               if (var11 < var5) {
                  var12 = var11;
               }

               var13 = var10.right;
               var11 = var7;
               if (var13 > var7) {
                  var11 = var13;
               }

               var13 = var10.top;
               var7 = var6;
               if (var13 < var6) {
                  var7 = var13;
               }

               var6 = var10.bottom;
               var13 = var8;
               if (var6 > var8) {
                  var13 = var6;
               }

               ++var4;
               var6 = var7;
               var5 = var12;
               var7 = var11;
            }

            this.mRecyclerView.mTempRect.set(var5, var6, var7, var8);
            this.setMeasuredDimension(this.mRecyclerView.mTempRect, var1, var2);
         }
      }

      public void setMeasurementCacheEnabled(boolean var1) {
         this.mMeasurementCacheEnabled = var1;
      }

      void setRecyclerView(RecyclerView var1) {
         if (var1 == null) {
            this.mRecyclerView = null;
            this.mChildHelper = null;
            this.mWidth = 0;
            this.mHeight = 0;
         } else {
            this.mRecyclerView = var1;
            this.mChildHelper = var1.mChildHelper;
            this.mWidth = var1.getWidth();
            this.mHeight = var1.getHeight();
         }

         this.mWidthMode = 1073741824;
         this.mHeightMode = 1073741824;
      }

      boolean shouldMeasureChild(View var1, int var2, int var3, RecyclerView.LayoutParams var4) {
         boolean var5;
         if (!var1.isLayoutRequested() && this.mMeasurementCacheEnabled && isMeasurementUpToDate(var1.getWidth(), var2, var4.width) && isMeasurementUpToDate(var1.getHeight(), var3, var4.height)) {
            var5 = false;
         } else {
            var5 = true;
         }

         return var5;
      }

      boolean shouldMeasureTwice() {
         return false;
      }

      boolean shouldReMeasureChild(View var1, int var2, int var3, RecyclerView.LayoutParams var4) {
         boolean var5;
         if (this.mMeasurementCacheEnabled && isMeasurementUpToDate(var1.getMeasuredWidth(), var2, var4.width) && isMeasurementUpToDate(var1.getMeasuredHeight(), var3, var4.height)) {
            var5 = false;
         } else {
            var5 = true;
         }

         return var5;
      }

      public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
         Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
      }

      public void startSmoothScroll(RecyclerView.SmoothScroller var1) {
         RecyclerView.SmoothScroller var2 = this.mSmoothScroller;
         if (var2 != null && var1 != var2 && var2.isRunning()) {
            this.mSmoothScroller.stop();
         }

         this.mSmoothScroller = var1;
         this.mSmoothScroller.start(this.mRecyclerView, this);
      }

      public void stopIgnoringView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         var2.stopIgnoring();
         var2.resetInternal();
         var2.addFlags(4);
      }

      void stopSmoothScroller() {
         RecyclerView.SmoothScroller var1 = this.mSmoothScroller;
         if (var1 != null) {
            var1.stop();
         }

      }

      public boolean supportsPredictiveItemAnimations() {
         return false;
      }

      public interface LayoutPrefetchRegistry {
         void addPosition(int var1, int var2);
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      public final Rect mDecorInsets = new Rect();
      boolean mInsetsDirty = true;
      boolean mPendingInvalidate = false;
      RecyclerView.ViewHolder mViewHolder;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(RecyclerView.LayoutParams var1) {
         super(var1);
      }

      public int getViewLayoutPosition() {
         return this.mViewHolder.getLayoutPosition();
      }

      public boolean isItemChanged() {
         return this.mViewHolder.isUpdated();
      }

      public boolean isItemRemoved() {
         return this.mViewHolder.isRemoved();
      }

      public boolean isViewInvalid() {
         return this.mViewHolder.isInvalid();
      }
   }

   public interface OnChildAttachStateChangeListener {
      void onChildViewAttachedToWindow(View var1);

      void onChildViewDetachedFromWindow(View var1);
   }

   public abstract static class OnFlingListener {
      public abstract boolean onFling(int var1, int var2);
   }

   public interface OnItemTouchListener {
      boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2);

      void onRequestDisallowInterceptTouchEvent(boolean var1);

      void onTouchEvent(RecyclerView var1, MotionEvent var2);
   }

   public abstract static class OnScrollListener {
      public void onScrollStateChanged(RecyclerView var1, int var2) {
      }

      public void onScrolled(RecyclerView var1, int var2, int var3) {
      }
   }

   public static class RecycledViewPool {
      private int mAttachCount = 0;
      SparseArray mScrap = new SparseArray();

      private RecyclerView.RecycledViewPool.ScrapData getScrapDataForType(int var1) {
         RecyclerView.RecycledViewPool.ScrapData var2 = (RecyclerView.RecycledViewPool.ScrapData)this.mScrap.get(var1);
         RecyclerView.RecycledViewPool.ScrapData var3 = var2;
         if (var2 == null) {
            var3 = new RecyclerView.RecycledViewPool.ScrapData();
            this.mScrap.put(var1, var3);
         }

         return var3;
      }

      void attach() {
         ++this.mAttachCount;
      }

      public void clear() {
         for(int var1 = 0; var1 < this.mScrap.size(); ++var1) {
            ((RecyclerView.RecycledViewPool.ScrapData)this.mScrap.valueAt(var1)).mScrapHeap.clear();
         }

      }

      void detach() {
         --this.mAttachCount;
      }

      void factorInBindTime(int var1, long var2) {
         RecyclerView.RecycledViewPool.ScrapData var4 = this.getScrapDataForType(var1);
         var4.mBindRunningAverageNs = this.runningAverage(var4.mBindRunningAverageNs, var2);
      }

      void factorInCreateTime(int var1, long var2) {
         RecyclerView.RecycledViewPool.ScrapData var4 = this.getScrapDataForType(var1);
         var4.mCreateRunningAverageNs = this.runningAverage(var4.mCreateRunningAverageNs, var2);
      }

      public RecyclerView.ViewHolder getRecycledView(int var1) {
         RecyclerView.RecycledViewPool.ScrapData var2 = (RecyclerView.RecycledViewPool.ScrapData)this.mScrap.get(var1);
         if (var2 != null && !var2.mScrapHeap.isEmpty()) {
            ArrayList var3 = var2.mScrapHeap;

            for(var1 = var3.size() - 1; var1 >= 0; --var1) {
               if (!((RecyclerView.ViewHolder)var3.get(var1)).isAttachedToTransitionOverlay()) {
                  return (RecyclerView.ViewHolder)var3.remove(var1);
               }
            }
         }

         return null;
      }

      void onAdapterChanged(RecyclerView.Adapter var1, RecyclerView.Adapter var2, boolean var3) {
         if (var1 != null) {
            this.detach();
         }

         if (!var3 && this.mAttachCount == 0) {
            this.clear();
         }

         if (var2 != null) {
            this.attach();
         }

      }

      public void putRecycledView(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         ArrayList var3 = this.getScrapDataForType(var2).mScrapHeap;
         if (((RecyclerView.RecycledViewPool.ScrapData)this.mScrap.get(var2)).mMaxScrap > var3.size()) {
            var1.resetInternal();
            var3.add(var1);
         }
      }

      long runningAverage(long var1, long var3) {
         return var1 == 0L ? var3 : var1 / 4L * 3L + var3 / 4L;
      }

      boolean willBindInTime(int var1, long var2, long var4) {
         long var6 = this.getScrapDataForType(var1).mBindRunningAverageNs;
         boolean var8;
         if (var6 != 0L && var2 + var6 >= var4) {
            var8 = false;
         } else {
            var8 = true;
         }

         return var8;
      }

      boolean willCreateInTime(int var1, long var2, long var4) {
         long var6 = this.getScrapDataForType(var1).mCreateRunningAverageNs;
         boolean var8;
         if (var6 != 0L && var2 + var6 >= var4) {
            var8 = false;
         } else {
            var8 = true;
         }

         return var8;
      }

      static class ScrapData {
         long mBindRunningAverageNs = 0L;
         long mCreateRunningAverageNs = 0L;
         int mMaxScrap = 20;
         final ArrayList mScrapHeap = new ArrayList();
      }
   }

   public final class Recycler {
      final ArrayList mAttachedScrap = new ArrayList();
      final ArrayList mCachedViews = new ArrayList();
      ArrayList mChangedScrap = null;
      RecyclerView.RecycledViewPool mRecyclerPool;
      private int mRequestedCacheMax;
      private final List mUnmodifiableAttachedScrap;
      private RecyclerView.ViewCacheExtension mViewCacheExtension;
      int mViewCacheMax;

      public Recycler() {
         this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
         this.mRequestedCacheMax = 2;
         this.mViewCacheMax = 2;
      }

      private void attachAccessibilityDelegateOnBind(RecyclerView.ViewHolder var1) {
         if (RecyclerView.this.isAccessibilityEnabled()) {
            View var2 = var1.itemView;
            if (ViewCompat.getImportantForAccessibility(var2) == 0) {
               ViewCompat.setImportantForAccessibility(var2, 1);
            }

            AccessibilityDelegateCompat var3 = ViewCompat.getAccessibilityDelegate(var2);
            if (var3 == null || var3.getClass().equals(AccessibilityDelegateCompat.class)) {
               var1.addFlags(16384);
               ViewCompat.setAccessibilityDelegate(var2, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
            }
         }

      }

      private void invalidateDisplayListInt(ViewGroup var1, boolean var2) {
         int var3;
         for(var3 = var1.getChildCount() - 1; var3 >= 0; --var3) {
            View var4 = var1.getChildAt(var3);
            if (var4 instanceof ViewGroup) {
               this.invalidateDisplayListInt((ViewGroup)var4, true);
            }
         }

         if (var2) {
            if (var1.getVisibility() == 4) {
               var1.setVisibility(0);
               var1.setVisibility(4);
            } else {
               var3 = var1.getVisibility();
               var1.setVisibility(4);
               var1.setVisibility(var3);
            }

         }
      }

      private void invalidateDisplayListInt(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof ViewGroup) {
            this.invalidateDisplayListInt((ViewGroup)var2, false);
         }

      }

      private boolean tryBindViewHolderByDeadline(RecyclerView.ViewHolder var1, int var2, int var3, long var4) {
         var1.mOwnerRecyclerView = RecyclerView.this;
         int var6 = var1.getItemViewType();
         long var7 = RecyclerView.this.getNanoTime();
         if (var4 != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(var6, var7, var4)) {
            return false;
         } else {
            RecyclerView.this.mAdapter.bindViewHolder(var1, var2);
            var4 = RecyclerView.this.getNanoTime();
            this.mRecyclerPool.factorInBindTime(var1.getItemViewType(), var4 - var7);
            this.attachAccessibilityDelegateOnBind(var1);
            if (RecyclerView.this.mState.isPreLayout()) {
               var1.mPreLayoutPosition = var3;
            }

            return true;
         }
      }

      void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder var1, boolean var2) {
         RecyclerView.clearNestedRecyclerViewIfNotNested(var1);
         if (var1.hasAnyOfTheFlags(16384)) {
            var1.setFlags(0, 16384);
            ViewCompat.setAccessibilityDelegate(var1.itemView, (AccessibilityDelegateCompat)null);
         }

         if (var2) {
            this.dispatchViewRecycled(var1);
         }

         var1.mOwnerRecyclerView = null;
         this.getRecycledViewPool().putRecycledView(var1);
      }

      public void clear() {
         this.mAttachedScrap.clear();
         this.recycleAndClearCachedViews();
      }

      void clearOldPositions() {
         int var1 = this.mCachedViews.size();
         byte var2 = 0;

         int var3;
         for(var3 = 0; var3 < var1; ++var3) {
            ((RecyclerView.ViewHolder)this.mCachedViews.get(var3)).clearOldPosition();
         }

         var1 = this.mAttachedScrap.size();

         for(var3 = 0; var3 < var1; ++var3) {
            ((RecyclerView.ViewHolder)this.mAttachedScrap.get(var3)).clearOldPosition();
         }

         ArrayList var4 = this.mChangedScrap;
         if (var4 != null) {
            var1 = var4.size();

            for(var3 = var2; var3 < var1; ++var3) {
               ((RecyclerView.ViewHolder)this.mChangedScrap.get(var3)).clearOldPosition();
            }
         }

      }

      void clearScrap() {
         this.mAttachedScrap.clear();
         ArrayList var1 = this.mChangedScrap;
         if (var1 != null) {
            var1.clear();
         }

      }

      public int convertPreLayoutPositionToPostLayout(int var1) {
         if (var1 >= 0 && var1 < RecyclerView.this.mState.getItemCount()) {
            return !RecyclerView.this.mState.isPreLayout() ? var1 : RecyclerView.this.mAdapterHelper.findPositionOffset(var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("invalid position ");
            var2.append(var1);
            var2.append(". State item count is ");
            var2.append(RecyclerView.this.mState.getItemCount());
            var2.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(var2.toString());
         }
      }

      void dispatchViewRecycled(RecyclerView.ViewHolder var1) {
         RecyclerView.RecyclerListener var2 = RecyclerView.this.mRecyclerListener;
         if (var2 != null) {
            var2.onViewRecycled(var1);
         }

         RecyclerView.Adapter var3 = RecyclerView.this.mAdapter;
         if (var3 != null) {
            var3.onViewRecycled(var1);
         }

         RecyclerView var4 = RecyclerView.this;
         if (var4.mState != null) {
            var4.mViewInfoStore.removeViewHolder(var1);
         }

      }

      RecyclerView.ViewHolder getChangedScrapViewForPosition(int var1) {
         ArrayList var2 = this.mChangedScrap;
         if (var2 != null) {
            int var3 = var2.size();
            if (var3 != 0) {
               byte var4 = 0;

               RecyclerView.ViewHolder var8;
               for(int var5 = 0; var5 < var3; ++var5) {
                  var8 = (RecyclerView.ViewHolder)this.mChangedScrap.get(var5);
                  if (!var8.wasReturnedFromScrap() && var8.getLayoutPosition() == var1) {
                     var8.addFlags(32);
                     return var8;
                  }
               }

               if (RecyclerView.this.mAdapter.hasStableIds()) {
                  var1 = RecyclerView.this.mAdapterHelper.findPositionOffset(var1);
                  if (var1 > 0 && var1 < RecyclerView.this.mAdapter.getItemCount()) {
                     long var6 = RecyclerView.this.mAdapter.getItemId(var1);

                     for(var1 = var4; var1 < var3; ++var1) {
                        var8 = (RecyclerView.ViewHolder)this.mChangedScrap.get(var1);
                        if (!var8.wasReturnedFromScrap() && var8.getItemId() == var6) {
                           var8.addFlags(32);
                           return var8;
                        }
                     }
                  }
               }
            }
         }

         return null;
      }

      RecyclerView.RecycledViewPool getRecycledViewPool() {
         if (this.mRecyclerPool == null) {
            this.mRecyclerPool = new RecyclerView.RecycledViewPool();
         }

         return this.mRecyclerPool;
      }

      int getScrapCount() {
         return this.mAttachedScrap.size();
      }

      public List getScrapList() {
         return this.mUnmodifiableAttachedScrap;
      }

      RecyclerView.ViewHolder getScrapOrCachedViewForId(long var1, int var3, boolean var4) {
         int var5;
         RecyclerView.ViewHolder var6;
         for(var5 = this.mAttachedScrap.size() - 1; var5 >= 0; --var5) {
            var6 = (RecyclerView.ViewHolder)this.mAttachedScrap.get(var5);
            if (var6.getItemId() == var1 && !var6.wasReturnedFromScrap()) {
               if (var3 == var6.getItemViewType()) {
                  var6.addFlags(32);
                  if (var6.isRemoved() && !RecyclerView.this.mState.isPreLayout()) {
                     var6.setFlags(2, 14);
                  }

                  return var6;
               }

               if (!var4) {
                  this.mAttachedScrap.remove(var5);
                  RecyclerView.this.removeDetachedView(var6.itemView, false);
                  this.quickRecycleScrapView(var6.itemView);
               }
            }
         }

         for(var5 = this.mCachedViews.size() - 1; var5 >= 0; --var5) {
            var6 = (RecyclerView.ViewHolder)this.mCachedViews.get(var5);
            if (var6.getItemId() == var1 && !var6.isAttachedToTransitionOverlay()) {
               if (var3 == var6.getItemViewType()) {
                  if (!var4) {
                     this.mCachedViews.remove(var5);
                  }

                  return var6;
               }

               if (!var4) {
                  this.recycleCachedViewAt(var5);
                  return null;
               }
            }
         }

         return null;
      }

      RecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int var1, boolean var2) {
         int var3 = this.mAttachedScrap.size();
         byte var4 = 0;

         int var5;
         RecyclerView.ViewHolder var6;
         for(var5 = 0; var5 < var3; ++var5) {
            var6 = (RecyclerView.ViewHolder)this.mAttachedScrap.get(var5);
            if (!var6.wasReturnedFromScrap() && var6.getLayoutPosition() == var1 && !var6.isInvalid() && (RecyclerView.this.mState.mInPreLayout || !var6.isRemoved())) {
               var6.addFlags(32);
               return var6;
            }
         }

         if (!var2) {
            View var7 = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(var1);
            if (var7 != null) {
               var6 = RecyclerView.getChildViewHolderInt(var7);
               RecyclerView.this.mChildHelper.unhide(var7);
               var1 = RecyclerView.this.mChildHelper.indexOfChild(var7);
               if (var1 != -1) {
                  RecyclerView.this.mChildHelper.detachViewFromParent(var1);
                  this.scrapView(var7);
                  var6.addFlags(8224);
                  return var6;
               }

               StringBuilder var8 = new StringBuilder();
               var8.append("layout index should not be -1 after unhiding a view:");
               var8.append(var6);
               var8.append(RecyclerView.this.exceptionLabel());
               throw new IllegalStateException(var8.toString());
            }
         }

         var3 = this.mCachedViews.size();

         for(var5 = var4; var5 < var3; ++var5) {
            var6 = (RecyclerView.ViewHolder)this.mCachedViews.get(var5);
            if (!var6.isInvalid() && var6.getLayoutPosition() == var1 && !var6.isAttachedToTransitionOverlay()) {
               if (!var2) {
                  this.mCachedViews.remove(var5);
               }

               return var6;
            }
         }

         return null;
      }

      View getScrapViewAt(int var1) {
         return ((RecyclerView.ViewHolder)this.mAttachedScrap.get(var1)).itemView;
      }

      public View getViewForPosition(int var1) {
         return this.getViewForPosition(var1, false);
      }

      View getViewForPosition(int var1, boolean var2) {
         return this.tryGetViewHolderForPositionByDeadline(var1, var2, Long.MAX_VALUE).itemView;
      }

      void markItemDecorInsetsDirty() {
         int var1 = this.mCachedViews.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            RecyclerView.LayoutParams var3 = (RecyclerView.LayoutParams)((RecyclerView.ViewHolder)this.mCachedViews.get(var2)).itemView.getLayoutParams();
            if (var3 != null) {
               var3.mInsetsDirty = true;
            }
         }

      }

      void markKnownViewsInvalid() {
         int var1 = this.mCachedViews.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            RecyclerView.ViewHolder var3 = (RecyclerView.ViewHolder)this.mCachedViews.get(var2);
            if (var3 != null) {
               var3.addFlags(6);
               var3.addChangePayload((Object)null);
            }
         }

         RecyclerView.Adapter var4 = RecyclerView.this.mAdapter;
         if (var4 == null || !var4.hasStableIds()) {
            this.recycleAndClearCachedViews();
         }

      }

      void offsetPositionRecordsForInsert(int var1, int var2) {
         int var3 = this.mCachedViews.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            RecyclerView.ViewHolder var5 = (RecyclerView.ViewHolder)this.mCachedViews.get(var4);
            if (var5 != null && var5.mPosition >= var1) {
               var5.offsetPosition(var2, true);
            }
         }

      }

      void offsetPositionRecordsForMove(int var1, int var2) {
         int var3;
         int var4;
         byte var5;
         if (var1 < var2) {
            var3 = var1;
            var4 = var2;
            var5 = -1;
         } else {
            var4 = var1;
            var3 = var2;
            var5 = 1;
         }

         int var6 = this.mCachedViews.size();

         for(int var7 = 0; var7 < var6; ++var7) {
            RecyclerView.ViewHolder var8 = (RecyclerView.ViewHolder)this.mCachedViews.get(var7);
            if (var8 != null) {
               int var9 = var8.mPosition;
               if (var9 >= var3 && var9 <= var4) {
                  if (var9 == var1) {
                     var8.offsetPosition(var2 - var1, false);
                  } else {
                     var8.offsetPosition(var5, false);
                  }
               }
            }
         }

      }

      void offsetPositionRecordsForRemove(int var1, int var2, boolean var3) {
         for(int var4 = this.mCachedViews.size() - 1; var4 >= 0; --var4) {
            RecyclerView.ViewHolder var5 = (RecyclerView.ViewHolder)this.mCachedViews.get(var4);
            if (var5 != null) {
               int var6 = var5.mPosition;
               if (var6 >= var1 + var2) {
                  var5.offsetPosition(-var2, var3);
               } else if (var6 >= var1) {
                  var5.addFlags(8);
                  this.recycleCachedViewAt(var4);
               }
            }
         }

      }

      void onAdapterChanged(RecyclerView.Adapter var1, RecyclerView.Adapter var2, boolean var3) {
         this.clear();
         this.getRecycledViewPool().onAdapterChanged(var1, var2, var3);
      }

      void quickRecycleScrapView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         var2.mScrapContainer = null;
         var2.mInChangeScrap = false;
         var2.clearReturnedFromScrapFlag();
         this.recycleViewHolderInternal(var2);
      }

      void recycleAndClearCachedViews() {
         for(int var1 = this.mCachedViews.size() - 1; var1 >= 0; --var1) {
            this.recycleCachedViewAt(var1);
         }

         this.mCachedViews.clear();
         if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
         }

      }

      void recycleCachedViewAt(int var1) {
         this.addViewHolderToRecycledViewPool((RecyclerView.ViewHolder)this.mCachedViews.get(var1), true);
         this.mCachedViews.remove(var1);
      }

      public void recycleView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         if (var2.isTmpDetached()) {
            RecyclerView.this.removeDetachedView(var1, false);
         }

         if (var2.isScrap()) {
            var2.unScrap();
         } else if (var2.wasReturnedFromScrap()) {
            var2.clearReturnedFromScrapFlag();
         }

         this.recycleViewHolderInternal(var2);
         if (RecyclerView.this.mItemAnimator != null && !var2.isRecyclable()) {
            RecyclerView.this.mItemAnimator.endAnimation(var2);
         }

      }

      void recycleViewHolderInternal(RecyclerView.ViewHolder var1) {
         boolean var2 = var1.isScrap();
         boolean var3 = false;
         boolean var4 = false;
         StringBuilder var5;
         if (!var2 && var1.itemView.getParent() == null) {
            if (!var1.isTmpDetached()) {
               if (var1.shouldIgnore()) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
                  var8.append(RecyclerView.this.exceptionLabel());
                  throw new IllegalArgumentException(var8.toString());
               } else {
                  var3 = var1.doesTransientStatePreventRecycling();
                  RecyclerView.Adapter var9 = RecyclerView.this.mAdapter;
                  boolean var6;
                  if (var9 != null && var3 && var9.onFailedToRecycleView(var1)) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  boolean var11;
                  if (!var6 && !var1.isRecyclable()) {
                     var11 = false;
                  } else {
                     if (this.mViewCacheMax > 0 && !var1.hasAnyOfTheFlags(526)) {
                        int var7 = this.mCachedViews.size();
                        int var10 = var7;
                        if (var7 >= this.mViewCacheMax) {
                           var10 = var7;
                           if (var7 > 0) {
                              this.recycleCachedViewAt(0);
                              var10 = var7 - 1;
                           }
                        }

                        var7 = var10;
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                           var7 = var10;
                           if (var10 > 0) {
                              var7 = var10;
                              if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(var1.mPosition)) {
                                 --var10;

                                 while(var10 >= 0) {
                                    var7 = ((RecyclerView.ViewHolder)this.mCachedViews.get(var10)).mPosition;
                                    if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(var7)) {
                                       break;
                                    }

                                    --var10;
                                 }

                                 var7 = var10 + 1;
                              }
                           }
                        }

                        this.mCachedViews.add(var7, var1);
                        var6 = true;
                     } else {
                        var6 = false;
                     }

                     var11 = var6;
                     if (!var6) {
                        this.addViewHolderToRecycledViewPool(var1, true);
                        var4 = true;
                        var11 = var6;
                     }
                  }

                  RecyclerView.this.mViewInfoStore.removeViewHolder(var1);
                  if (!var11 && !var4 && var3) {
                     var1.mOwnerRecyclerView = null;
                  }

               }
            } else {
               var5 = new StringBuilder();
               var5.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
               var5.append(var1);
               var5.append(RecyclerView.this.exceptionLabel());
               throw new IllegalArgumentException(var5.toString());
            }
         } else {
            var5 = new StringBuilder();
            var5.append("Scrapped or attached views may not be recycled. isScrap:");
            var5.append(var1.isScrap());
            var5.append(" isAttached:");
            if (var1.itemView.getParent() != null) {
               var3 = true;
            }

            var5.append(var3);
            var5.append(RecyclerView.this.exceptionLabel());
            throw new IllegalArgumentException(var5.toString());
         }
      }

      void scrapView(View var1) {
         RecyclerView.ViewHolder var2 = RecyclerView.getChildViewHolderInt(var1);
         if (!var2.hasAnyOfTheFlags(12) && var2.isUpdated() && !RecyclerView.this.canReuseUpdatedViewHolder(var2)) {
            if (this.mChangedScrap == null) {
               this.mChangedScrap = new ArrayList();
            }

            var2.setScrapContainer(this, true);
            this.mChangedScrap.add(var2);
         } else {
            if (var2.isInvalid() && !var2.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
               var3.append(RecyclerView.this.exceptionLabel());
               throw new IllegalArgumentException(var3.toString());
            }

            var2.setScrapContainer(this, false);
            this.mAttachedScrap.add(var2);
         }

      }

      void setRecycledViewPool(RecyclerView.RecycledViewPool var1) {
         RecyclerView.RecycledViewPool var2 = this.mRecyclerPool;
         if (var2 != null) {
            var2.detach();
         }

         this.mRecyclerPool = var1;
         if (this.mRecyclerPool != null && RecyclerView.this.getAdapter() != null) {
            this.mRecyclerPool.attach();
         }

      }

      void setViewCacheExtension(RecyclerView.ViewCacheExtension var1) {
         this.mViewCacheExtension = var1;
      }

      public void setViewCacheSize(int var1) {
         this.mRequestedCacheMax = var1;
         this.updateViewCacheSize();
      }

      RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int var1, boolean var2, long var3) {
         StringBuilder var7;
         if (var1 >= 0 && var1 < RecyclerView.this.mState.getItemCount()) {
            boolean var6;
            RecyclerView.ViewHolder var8;
            boolean var9;
            RecyclerView.ViewHolder var18;
            label143: {
               boolean var5 = RecyclerView.this.mState.isPreLayout();
               var6 = true;
               if (var5) {
                  var18 = this.getChangedScrapViewForPosition(var1);
                  var8 = var18;
                  if (var18 != null) {
                     var9 = true;
                     var8 = var18;
                     break label143;
                  }
               } else {
                  var8 = null;
               }

               var9 = false;
            }

            var18 = var8;
            boolean var10 = var9;
            if (var8 == null) {
               var8 = this.getScrapOrHiddenOrCachedHolderForPosition(var1, var2);
               var18 = var8;
               var10 = var9;
               if (var8 != null) {
                  if (!this.validateViewHolderForOffsetPosition(var8)) {
                     if (!var2) {
                        var8.addFlags(4);
                        if (var8.isScrap()) {
                           RecyclerView.this.removeDetachedView(var8.itemView, false);
                           var8.unScrap();
                        } else if (var8.wasReturnedFromScrap()) {
                           var8.clearReturnedFromScrapFlag();
                        }

                        this.recycleViewHolderInternal(var8);
                     }

                     var18 = null;
                     var10 = var9;
                  } else {
                     var10 = true;
                     var18 = var8;
                  }
               }
            }

            RecyclerView var21;
            label136: {
               var8 = var18;
               boolean var11 = var10;
               if (var18 == null) {
                  int var19 = RecyclerView.this.mAdapterHelper.findPositionOffset(var1);
                  if (var19 < 0 || var19 >= RecyclerView.this.mAdapter.getItemCount()) {
                     var7 = new StringBuilder();
                     var7.append("Inconsistency detected. Invalid item position ");
                     var7.append(var1);
                     var7.append("(offset:");
                     var7.append(var19);
                     var7.append(").state:");
                     var7.append(RecyclerView.this.mState.getItemCount());
                     var7.append(RecyclerView.this.exceptionLabel());
                     throw new IndexOutOfBoundsException(var7.toString());
                  }

                  int var12 = RecyclerView.this.mAdapter.getItemViewType(var19);
                  var9 = var10;
                  if (RecyclerView.this.mAdapter.hasStableIds()) {
                     var8 = this.getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(var19), var12, var2);
                     var18 = var8;
                     var9 = var10;
                     if (var8 != null) {
                        var8.mPosition = var19;
                        var9 = true;
                        var18 = var8;
                     }
                  }

                  var8 = var18;
                  if (var18 == null) {
                     RecyclerView.ViewCacheExtension var13 = this.mViewCacheExtension;
                     var8 = var18;
                     if (var13 != null) {
                        View var24 = var13.getViewForPositionAndType(this, var1, var12);
                        var8 = var18;
                        if (var24 != null) {
                           var8 = RecyclerView.this.getChildViewHolder(var24);
                           if (var8 == null) {
                              var7 = new StringBuilder();
                              var7.append("getViewForPositionAndType returned a view which does not have a ViewHolder");
                              var7.append(RecyclerView.this.exceptionLabel());
                              throw new IllegalArgumentException(var7.toString());
                           }

                           if (var8.shouldIgnore()) {
                              var7 = new StringBuilder();
                              var7.append("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                              var7.append(RecyclerView.this.exceptionLabel());
                              throw new IllegalArgumentException(var7.toString());
                           }
                        }
                     }
                  }

                  var18 = var8;
                  if (var8 == null) {
                     var8 = this.getRecycledViewPool().getRecycledView(var12);
                     var18 = var8;
                     if (var8 != null) {
                        var8.resetInternal();
                        var18 = var8;
                        if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                           this.invalidateDisplayListInt(var8);
                           var18 = var8;
                        }
                     }
                  }

                  var8 = var18;
                  var11 = var9;
                  if (var18 == null) {
                     long var14 = RecyclerView.this.getNanoTime();
                     if (var3 != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(var12, var14, var3)) {
                        return null;
                     }

                     var21 = RecyclerView.this;
                     var8 = var21.mAdapter.createViewHolder(var21, var12);
                     if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        var21 = RecyclerView.findNestedRecyclerView(var8.itemView);
                        if (var21 != null) {
                           var8.mNestedRecyclerView = new WeakReference(var21);
                        }
                     }

                     long var16 = RecyclerView.this.getNanoTime();
                     this.mRecyclerPool.factorInCreateTime(var12, var16 - var14);
                     break label136;
                  }
               }

               var9 = var11;
            }

            if (var9 && !RecyclerView.this.mState.isPreLayout() && var8.hasAnyOfTheFlags(8192)) {
               var8.setFlags(0, 8192);
               if (RecyclerView.this.mState.mRunSimpleAnimations) {
                  int var20 = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations(var8);
                  var21 = RecyclerView.this;
                  RecyclerView.ItemAnimator.ItemHolderInfo var22 = var21.mItemAnimator.recordPreLayoutInformation(var21.mState, var8, var20 | 4096, var8.getUnmodifiedPayloads());
                  RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(var8, var22);
               }
            }

            label154: {
               if (RecyclerView.this.mState.isPreLayout() && var8.isBound()) {
                  var8.mPreLayoutPosition = var1;
               } else if (!var8.isBound() || var8.needsUpdate() || var8.isInvalid()) {
                  var2 = this.tryBindViewHolderByDeadline(var8, RecyclerView.this.mAdapterHelper.findPositionOffset(var1), var1, var3);
                  break label154;
               }

               var2 = false;
            }

            android.view.ViewGroup.LayoutParams var23 = var8.itemView.getLayoutParams();
            RecyclerView.LayoutParams var25;
            if (var23 == null) {
               var25 = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
               var8.itemView.setLayoutParams(var25);
            } else if (!RecyclerView.this.checkLayoutParams(var23)) {
               var25 = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams(var23);
               var8.itemView.setLayoutParams(var25);
            } else {
               var25 = (RecyclerView.LayoutParams)var23;
            }

            var25.mViewHolder = var8;
            if (var9 && var2) {
               var2 = var6;
            } else {
               var2 = false;
            }

            var25.mPendingInvalidate = var2;
            return var8;
         } else {
            var7 = new StringBuilder();
            var7.append("Invalid item position ");
            var7.append(var1);
            var7.append("(");
            var7.append(var1);
            var7.append("). Item count:");
            var7.append(RecyclerView.this.mState.getItemCount());
            var7.append(RecyclerView.this.exceptionLabel());
            throw new IndexOutOfBoundsException(var7.toString());
         }
      }

      void unscrapView(RecyclerView.ViewHolder var1) {
         if (var1.mInChangeScrap) {
            this.mChangedScrap.remove(var1);
         } else {
            this.mAttachedScrap.remove(var1);
         }

         var1.mScrapContainer = null;
         var1.mInChangeScrap = false;
         var1.clearReturnedFromScrapFlag();
      }

      void updateViewCacheSize() {
         RecyclerView.LayoutManager var1 = RecyclerView.this.mLayout;
         int var2;
         if (var1 != null) {
            var2 = var1.mPrefetchMaxCountObserved;
         } else {
            var2 = 0;
         }

         this.mViewCacheMax = this.mRequestedCacheMax + var2;

         for(var2 = this.mCachedViews.size() - 1; var2 >= 0 && this.mCachedViews.size() > this.mViewCacheMax; --var2) {
            this.recycleCachedViewAt(var2);
         }

      }

      boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder var1) {
         if (var1.isRemoved()) {
            return RecyclerView.this.mState.isPreLayout();
         } else {
            int var2 = var1.mPosition;
            if (var2 >= 0 && var2 < RecyclerView.this.mAdapter.getItemCount()) {
               boolean var3 = RecyclerView.this.mState.isPreLayout();
               boolean var4 = false;
               if (!var3 && RecyclerView.this.mAdapter.getItemViewType(var1.mPosition) != var1.getItemViewType()) {
                  return false;
               } else if (RecyclerView.this.mAdapter.hasStableIds()) {
                  if (var1.getItemId() == RecyclerView.this.mAdapter.getItemId(var1.mPosition)) {
                     var4 = true;
                  }

                  return var4;
               } else {
                  return true;
               }
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append("Inconsistency detected. Invalid view holder adapter position");
               var5.append(var1);
               var5.append(RecyclerView.this.exceptionLabel());
               throw new IndexOutOfBoundsException(var5.toString());
            }
         }
      }

      void viewRangeUpdate(int var1, int var2) {
         for(int var3 = this.mCachedViews.size() - 1; var3 >= 0; --var3) {
            RecyclerView.ViewHolder var4 = (RecyclerView.ViewHolder)this.mCachedViews.get(var3);
            if (var4 != null) {
               int var5 = var4.mPosition;
               if (var5 >= var1 && var5 < var2 + var1) {
                  var4.addFlags(2);
                  this.recycleCachedViewAt(var3);
               }
            }
         }

      }
   }

   public interface RecyclerListener {
      void onViewRecycled(RecyclerView.ViewHolder var1);
   }

   private class RecyclerViewDataObserver extends RecyclerView.AdapterDataObserver {
      RecyclerViewDataObserver() {
      }

      public void onChanged() {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         RecyclerView var1 = RecyclerView.this;
         var1.mState.mStructureChanged = true;
         var1.processDataSetCompletelyChanged(true);
         if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
            RecyclerView.this.requestLayout();
         }

      }

      public void onItemRangeChanged(int var1, int var2, Object var3) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(var1, var2, var3)) {
            this.triggerUpdateProcessor();
         }

      }

      public void onItemRangeInserted(int var1, int var2) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(var1, var2)) {
            this.triggerUpdateProcessor();
         }

      }

      public void onItemRangeMoved(int var1, int var2, int var3) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(var1, var2, var3)) {
            this.triggerUpdateProcessor();
         }

      }

      public void onItemRangeRemoved(int var1, int var2) {
         RecyclerView.this.assertNotInLayoutOrScroll((String)null);
         if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(var1, var2)) {
            this.triggerUpdateProcessor();
         }

      }

      void triggerUpdateProcessor() {
         RecyclerView var1;
         if (RecyclerView.POST_UPDATES_ON_ANIMATION) {
            var1 = RecyclerView.this;
            if (var1.mHasFixedSize && var1.mIsAttached) {
               ViewCompat.postOnAnimation(var1, var1.mUpdateChildViewsRunnable);
               return;
            }
         }

         var1 = RecyclerView.this;
         var1.mAdapterUpdateDuringMeasure = true;
         var1.requestLayout();
      }
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public RecyclerView.SavedState createFromParcel(Parcel var1) {
            return new RecyclerView.SavedState(var1, (ClassLoader)null);
         }

         public RecyclerView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new RecyclerView.SavedState(var1, var2);
         }

         public RecyclerView.SavedState[] newArray(int var1) {
            return new RecyclerView.SavedState[var1];
         }
      };
      Parcelable mLayoutState;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         if (var2 == null) {
            var2 = RecyclerView.LayoutManager.class.getClassLoader();
         }

         this.mLayoutState = var1.readParcelable(var2);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      void copyFrom(RecyclerView.SavedState var1) {
         this.mLayoutState = var1.mLayoutState;
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeParcelable(this.mLayoutState, 0);
      }
   }

   public abstract static class SmoothScroller {
      private RecyclerView.LayoutManager mLayoutManager;
      private boolean mPendingInitialRun;
      private RecyclerView mRecyclerView;
      private final RecyclerView.SmoothScroller.Action mRecyclingAction = new RecyclerView.SmoothScroller.Action(0, 0);
      private boolean mRunning;
      private boolean mStarted;
      private int mTargetPosition = -1;
      private View mTargetView;

      public PointF computeScrollVectorForPosition(int var1) {
         RecyclerView.LayoutManager var2 = this.getLayoutManager();
         if (var2 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return ((RecyclerView.SmoothScroller.ScrollVectorProvider)var2).computeScrollVectorForPosition(var1);
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("You should override computeScrollVectorForPosition when the LayoutManager does not implement ");
            var3.append(RecyclerView.SmoothScroller.ScrollVectorProvider.class.getCanonicalName());
            Log.w("RecyclerView", var3.toString());
            return null;
         }
      }

      public View findViewByPosition(int var1) {
         return this.mRecyclerView.mLayout.findViewByPosition(var1);
      }

      public int getChildCount() {
         return this.mRecyclerView.mLayout.getChildCount();
      }

      public int getChildPosition(View var1) {
         return this.mRecyclerView.getChildLayoutPosition(var1);
      }

      public RecyclerView.LayoutManager getLayoutManager() {
         return this.mLayoutManager;
      }

      public int getTargetPosition() {
         return this.mTargetPosition;
      }

      public boolean isPendingInitialRun() {
         return this.mPendingInitialRun;
      }

      public boolean isRunning() {
         return this.mRunning;
      }

      protected void normalize(PointF var1) {
         float var2 = var1.x;
         float var3 = var1.y;
         var3 = (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
         var1.x /= var3;
         var1.y /= var3;
      }

      void onAnimation(int var1, int var2) {
         RecyclerView var3 = this.mRecyclerView;
         if (this.mTargetPosition == -1 || var3 == null) {
            this.stop();
         }

         if (this.mPendingInitialRun && this.mTargetView == null && this.mLayoutManager != null) {
            PointF var4 = this.computeScrollVectorForPosition(this.mTargetPosition);
            if (var4 != null && (var4.x != 0.0F || var4.y != 0.0F)) {
               var3.scrollStep((int)Math.signum(var4.x), (int)Math.signum(var4.y), (int[])null);
            }
         }

         this.mPendingInitialRun = false;
         View var6 = this.mTargetView;
         if (var6 != null) {
            if (this.getChildPosition(var6) == this.mTargetPosition) {
               this.onTargetFound(this.mTargetView, var3.mState, this.mRecyclingAction);
               this.mRecyclingAction.runIfNecessary(var3);
               this.stop();
            } else {
               Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
               this.mTargetView = null;
            }
         }

         if (this.mRunning) {
            this.onSeekTargetStep(var1, var2, var3.mState, this.mRecyclingAction);
            boolean var5 = this.mRecyclingAction.hasJumpTarget();
            this.mRecyclingAction.runIfNecessary(var3);
            if (var5 && this.mRunning) {
               this.mPendingInitialRun = true;
               var3.mViewFlinger.postOnAnimation();
            }
         }

      }

      protected void onChildAttachedToWindow(View var1) {
         if (this.getChildPosition(var1) == this.getTargetPosition()) {
            this.mTargetView = var1;
         }

      }

      protected abstract void onSeekTargetStep(int var1, int var2, RecyclerView.State var3, RecyclerView.SmoothScroller.Action var4);

      protected abstract void onStart();

      protected abstract void onStop();

      protected abstract void onTargetFound(View var1, RecyclerView.State var2, RecyclerView.SmoothScroller.Action var3);

      public void setTargetPosition(int var1) {
         this.mTargetPosition = var1;
      }

      void start(RecyclerView var1, RecyclerView.LayoutManager var2) {
         var1.mViewFlinger.stop();
         if (this.mStarted) {
            StringBuilder var3 = new StringBuilder();
            var3.append("An instance of ");
            var3.append(this.getClass().getSimpleName());
            var3.append(" was started more than once. Each instance of");
            var3.append(this.getClass().getSimpleName());
            var3.append(" is intended to only be used once. You should create a new instance for each use.");
            Log.w("RecyclerView", var3.toString());
         }

         this.mRecyclerView = var1;
         this.mLayoutManager = var2;
         int var4 = this.mTargetPosition;
         if (var4 != -1) {
            this.mRecyclerView.mState.mTargetPosition = var4;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = this.findViewByPosition(this.getTargetPosition());
            this.onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
            this.mStarted = true;
         } else {
            throw new IllegalArgumentException("Invalid target position");
         }
      }

      protected final void stop() {
         if (this.mRunning) {
            this.mRunning = false;
            this.onStop();
            this.mRecyclerView.mState.mTargetPosition = -1;
            this.mTargetView = null;
            this.mTargetPosition = -1;
            this.mPendingInitialRun = false;
            this.mLayoutManager.onSmoothScrollerStopped(this);
            this.mLayoutManager = null;
            this.mRecyclerView = null;
         }
      }

      public static class Action {
         private boolean mChanged;
         private int mConsecutiveUpdates;
         private int mDuration;
         private int mDx;
         private int mDy;
         private Interpolator mInterpolator;
         private int mJumpToPosition;

         public Action(int var1, int var2) {
            this(var1, var2, Integer.MIN_VALUE, (Interpolator)null);
         }

         public Action(int var1, int var2, int var3, Interpolator var4) {
            this.mJumpToPosition = -1;
            this.mChanged = false;
            this.mConsecutiveUpdates = 0;
            this.mDx = var1;
            this.mDy = var2;
            this.mDuration = var3;
            this.mInterpolator = var4;
         }

         private void validate() {
            if (this.mInterpolator != null && this.mDuration < 1) {
               throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
            } else if (this.mDuration < 1) {
               throw new IllegalStateException("Scroll duration must be a positive number");
            }
         }

         boolean hasJumpTarget() {
            boolean var1;
            if (this.mJumpToPosition >= 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            return var1;
         }

         public void jumpTo(int var1) {
            this.mJumpToPosition = var1;
         }

         void runIfNecessary(RecyclerView var1) {
            int var2 = this.mJumpToPosition;
            if (var2 >= 0) {
               this.mJumpToPosition = -1;
               var1.jumpToPositionForSmoothScroller(var2);
               this.mChanged = false;
            } else {
               if (this.mChanged) {
                  this.validate();
                  var1.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                  ++this.mConsecutiveUpdates;
                  if (this.mConsecutiveUpdates > 10) {
                     Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                  }

                  this.mChanged = false;
               } else {
                  this.mConsecutiveUpdates = 0;
               }

            }
         }

         public void update(int var1, int var2, int var3, Interpolator var4) {
            this.mDx = var1;
            this.mDy = var2;
            this.mDuration = var3;
            this.mInterpolator = var4;
            this.mChanged = true;
         }
      }

      public interface ScrollVectorProvider {
         PointF computeScrollVectorForPosition(int var1);
      }
   }

   public static class State {
      private SparseArray mData;
      int mDeletedInvisibleItemCountSincePreviousLayout = 0;
      long mFocusedItemId;
      int mFocusedItemPosition;
      int mFocusedSubChildId;
      boolean mInPreLayout = false;
      boolean mIsMeasuring = false;
      int mItemCount = 0;
      int mLayoutStep = 1;
      int mPreviousLayoutItemCount = 0;
      int mRemainingScrollHorizontal;
      int mRemainingScrollVertical;
      boolean mRunPredictiveAnimations = false;
      boolean mRunSimpleAnimations = false;
      boolean mStructureChanged = false;
      int mTargetPosition = -1;
      boolean mTrackOldChangeHolders = false;

      void assertLayoutStep(int var1) {
         if ((this.mLayoutStep & var1) == 0) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Layout state should be one of ");
            var2.append(Integer.toBinaryString(var1));
            var2.append(" but it is ");
            var2.append(Integer.toBinaryString(this.mLayoutStep));
            throw new IllegalStateException(var2.toString());
         }
      }

      public int getItemCount() {
         int var1;
         if (this.mInPreLayout) {
            var1 = this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
         } else {
            var1 = this.mItemCount;
         }

         return var1;
      }

      public boolean hasTargetScrollPosition() {
         boolean var1;
         if (this.mTargetPosition != -1) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean isPreLayout() {
         return this.mInPreLayout;
      }

      void prepareForNestedPrefetch(RecyclerView.Adapter var1) {
         this.mLayoutStep = 1;
         this.mItemCount = var1.getItemCount();
         this.mInPreLayout = false;
         this.mTrackOldChangeHolders = false;
         this.mIsMeasuring = false;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("State{mTargetPosition=");
         var1.append(this.mTargetPosition);
         var1.append(", mData=");
         var1.append(this.mData);
         var1.append(", mItemCount=");
         var1.append(this.mItemCount);
         var1.append(", mIsMeasuring=");
         var1.append(this.mIsMeasuring);
         var1.append(", mPreviousLayoutItemCount=");
         var1.append(this.mPreviousLayoutItemCount);
         var1.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
         var1.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
         var1.append(", mStructureChanged=");
         var1.append(this.mStructureChanged);
         var1.append(", mInPreLayout=");
         var1.append(this.mInPreLayout);
         var1.append(", mRunSimpleAnimations=");
         var1.append(this.mRunSimpleAnimations);
         var1.append(", mRunPredictiveAnimations=");
         var1.append(this.mRunPredictiveAnimations);
         var1.append('}');
         return var1.toString();
      }

      public boolean willRunPredictiveAnimations() {
         return this.mRunPredictiveAnimations;
      }
   }

   public abstract static class ViewCacheExtension {
      public abstract View getViewForPositionAndType(RecyclerView.Recycler var1, int var2, int var3);
   }

   class ViewFlinger implements Runnable {
      private boolean mEatRunOnAnimationRequest;
      Interpolator mInterpolator;
      private int mLastFlingX;
      private int mLastFlingY;
      OverScroller mOverScroller;
      private boolean mReSchedulePostAnimationCallback;

      ViewFlinger() {
         this.mInterpolator = RecyclerView.sQuinticInterpolator;
         this.mEatRunOnAnimationRequest = false;
         this.mReSchedulePostAnimationCallback = false;
         this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
      }

      private int computeScrollDuration(int var1, int var2, int var3, int var4) {
         int var5 = Math.abs(var1);
         int var6 = Math.abs(var2);
         boolean var7;
         if (var5 > var6) {
            var7 = true;
         } else {
            var7 = false;
         }

         var3 = (int)Math.sqrt((double)(var3 * var3 + var4 * var4));
         var2 = (int)Math.sqrt((double)(var1 * var1 + var2 * var2));
         if (var7) {
            var1 = RecyclerView.this.getWidth();
         } else {
            var1 = RecyclerView.this.getHeight();
         }

         var4 = var1 / 2;
         float var8 = (float)var2;
         float var9 = (float)var1;
         float var10 = Math.min(1.0F, var8 * 1.0F / var9);
         var8 = (float)var4;
         var10 = this.distanceInfluenceForSnapDuration(var10);
         if (var3 > 0) {
            var1 = Math.round(Math.abs((var8 + var10 * var8) / (float)var3) * 1000.0F) * 4;
         } else {
            if (var7) {
               var1 = var5;
            } else {
               var1 = var6;
            }

            var1 = (int)(((float)var1 / var9 + 1.0F) * 300.0F);
         }

         return Math.min(var1, 2000);
      }

      private float distanceInfluenceForSnapDuration(float var1) {
         return (float)Math.sin((double)((var1 - 0.5F) * 0.47123894F));
      }

      private void internalPostOnAnimation() {
         RecyclerView.this.removeCallbacks(this);
         ViewCompat.postOnAnimation(RecyclerView.this, this);
      }

      public void fling(int var1, int var2) {
         RecyclerView.this.setScrollState(2);
         this.mLastFlingY = 0;
         this.mLastFlingX = 0;
         Interpolator var3 = this.mInterpolator;
         Interpolator var4 = RecyclerView.sQuinticInterpolator;
         if (var3 != var4) {
            this.mInterpolator = var4;
            this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
         }

         this.mOverScroller.fling(0, 0, var1, var2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
         this.postOnAnimation();
      }

      void postOnAnimation() {
         if (this.mEatRunOnAnimationRequest) {
            this.mReSchedulePostAnimationCallback = true;
         } else {
            this.internalPostOnAnimation();
         }

      }

      public void run() {
         RecyclerView var1 = RecyclerView.this;
         if (var1.mLayout == null) {
            this.stop();
         } else {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
            var1.consumePendingUpdateOperations();
            OverScroller var12 = this.mOverScroller;
            if (var12.computeScrollOffset()) {
               int var2 = var12.getCurrX();
               int var3 = var12.getCurrY();
               int var4 = var2 - this.mLastFlingX;
               int var5 = var3 - this.mLastFlingY;
               this.mLastFlingX = var2;
               this.mLastFlingY = var3;
               RecyclerView var6 = RecyclerView.this;
               int[] var7 = var6.mReusableIntPair;
               var7[0] = 0;
               var7[1] = 0;
               var3 = var4;
               var2 = var5;
               int[] var15;
               if (var6.dispatchNestedPreScroll(var4, var5, var7, (int[])null, 1)) {
                  var15 = RecyclerView.this.mReusableIntPair;
                  var3 = var4 - var15[0];
                  var2 = var5 - var15[1];
               }

               if (RecyclerView.this.getOverScrollMode() != 2) {
                  RecyclerView.this.considerReleasingGlowsOnScroll(var3, var2);
               }

               var6 = RecyclerView.this;
               int var8;
               int var9;
               RecyclerView.SmoothScroller var16;
               RecyclerView var18;
               if (var6.mAdapter != null) {
                  var7 = var6.mReusableIntPair;
                  var7[0] = 0;
                  var7[1] = 0;
                  var6.scrollStep(var3, var2, var7);
                  var18 = RecyclerView.this;
                  var15 = var18.mReusableIntPair;
                  var8 = var15[0];
                  var9 = var15[1];
                  int var10 = var3 - var8;
                  int var11 = var2 - var9;
                  var16 = var18.mLayout.mSmoothScroller;
                  var3 = var10;
                  var4 = var9;
                  var5 = var8;
                  var2 = var11;
                  if (var16 != null) {
                     var3 = var10;
                     var4 = var9;
                     var5 = var8;
                     var2 = var11;
                     if (!var16.isPendingInitialRun()) {
                        var3 = var10;
                        var4 = var9;
                        var5 = var8;
                        var2 = var11;
                        if (var16.isRunning()) {
                           var3 = RecyclerView.this.mState.getItemCount();
                           if (var3 == 0) {
                              var16.stop();
                              var3 = var10;
                              var4 = var9;
                              var5 = var8;
                              var2 = var11;
                           } else if (var16.getTargetPosition() >= var3) {
                              var16.setTargetPosition(var3 - 1);
                              var16.onAnimation(var8, var9);
                              var3 = var10;
                              var4 = var9;
                              var5 = var8;
                              var2 = var11;
                           } else {
                              var16.onAnimation(var8, var9);
                              var3 = var10;
                              var4 = var9;
                              var5 = var8;
                              var2 = var11;
                           }
                        }
                     }
                  }
               } else {
                  var4 = 0;
                  var5 = 0;
               }

               if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                  RecyclerView.this.invalidate();
               }

               var18 = RecyclerView.this;
               var15 = var18.mReusableIntPair;
               var15[0] = 0;
               var15[1] = 0;
               var18.dispatchNestedScroll(var5, var4, var3, var2, (int[])null, 1, var15);
               var15 = RecyclerView.this.mReusableIntPair;
               var8 = var3 - var15[0];
               var9 = var2 - var15[1];
               if (var5 != 0 || var4 != 0) {
                  RecyclerView.this.dispatchOnScrolled(var5, var4);
               }

               if (!RecyclerView.this.awakenScrollBars()) {
                  RecyclerView.this.invalidate();
               }

               boolean var17;
               if (var12.getCurrX() == var12.getFinalX()) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               boolean var14;
               if (var12.getCurrY() == var12.getFinalY()) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               if (var12.isFinished() || (var17 || var8 != 0) && (var14 || var9 != 0)) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               var16 = RecyclerView.this.mLayout.mSmoothScroller;
               if (var16 != null && var16.isPendingInitialRun()) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               if (!var14 && var17) {
                  if (RecyclerView.this.getOverScrollMode() != 2) {
                     var2 = (int)var12.getCurrVelocity();
                     if (var8 < 0) {
                        var3 = -var2;
                     } else if (var8 > 0) {
                        var3 = var2;
                     } else {
                        var3 = 0;
                     }

                     if (var9 < 0) {
                        var2 = -var2;
                     } else if (var9 <= 0) {
                        var2 = 0;
                     }

                     RecyclerView.this.absorbGlows(var3, var2);
                  }

                  if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                     RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                  }
               } else {
                  this.postOnAnimation();
                  var1 = RecyclerView.this;
                  GapWorker var19 = var1.mGapWorker;
                  if (var19 != null) {
                     var19.postFromTraversal(var1, var8, var9);
                  }
               }
            }

            RecyclerView.SmoothScroller var13 = RecyclerView.this.mLayout.mSmoothScroller;
            if (var13 != null && var13.isPendingInitialRun()) {
               var13.onAnimation(0, 0);
            }

            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
               this.internalPostOnAnimation();
            } else {
               RecyclerView.this.setScrollState(0);
               RecyclerView.this.stopNestedScroll(1);
            }

         }
      }

      public void smoothScrollBy(int var1, int var2, int var3, Interpolator var4) {
         int var5 = var3;
         if (var3 == Integer.MIN_VALUE) {
            var5 = this.computeScrollDuration(var1, var2, 0, 0);
         }

         Interpolator var6 = var4;
         if (var4 == null) {
            var6 = RecyclerView.sQuinticInterpolator;
         }

         if (this.mInterpolator != var6) {
            this.mInterpolator = var6;
            this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), var6);
         }

         this.mLastFlingY = 0;
         this.mLastFlingX = 0;
         RecyclerView.this.setScrollState(2);
         this.mOverScroller.startScroll(0, 0, var1, var2, var5);
         if (VERSION.SDK_INT < 23) {
            this.mOverScroller.computeScrollOffset();
         }

         this.postOnAnimation();
      }

      public void stop() {
         RecyclerView.this.removeCallbacks(this);
         this.mOverScroller.abortAnimation();
      }
   }

   public abstract static class ViewHolder {
      static final int FLAG_ADAPTER_FULLUPDATE = 1024;
      static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
      static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
      static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
      static final int FLAG_BOUND = 1;
      static final int FLAG_IGNORE = 128;
      static final int FLAG_INVALID = 4;
      static final int FLAG_MOVED = 2048;
      static final int FLAG_NOT_RECYCLABLE = 16;
      static final int FLAG_REMOVED = 8;
      static final int FLAG_RETURNED_FROM_SCRAP = 32;
      static final int FLAG_SET_A11Y_ITEM_DELEGATE = 16384;
      static final int FLAG_TMP_DETACHED = 256;
      static final int FLAG_UPDATE = 2;
      private static final List FULLUPDATE_PAYLOADS = Collections.emptyList();
      static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
      public final View itemView;
      int mFlags;
      boolean mInChangeScrap = false;
      private int mIsRecyclableCount = 0;
      long mItemId = -1L;
      int mItemViewType = -1;
      WeakReference mNestedRecyclerView;
      int mOldPosition = -1;
      RecyclerView mOwnerRecyclerView;
      List mPayloads = null;
      int mPendingAccessibilityState = -1;
      int mPosition = -1;
      int mPreLayoutPosition = -1;
      RecyclerView.Recycler mScrapContainer = null;
      RecyclerView.ViewHolder mShadowedHolder = null;
      RecyclerView.ViewHolder mShadowingHolder = null;
      List mUnmodifiedPayloads = null;
      private int mWasImportantForAccessibilityBeforeHidden = 0;

      public ViewHolder(View var1) {
         if (var1 != null) {
            this.itemView = var1;
         } else {
            throw new IllegalArgumentException("itemView may not be null");
         }
      }

      private void createPayloadsIfNeeded() {
         if (this.mPayloads == null) {
            this.mPayloads = new ArrayList();
            this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
         }

      }

      void addChangePayload(Object var1) {
         if (var1 == null) {
            this.addFlags(1024);
         } else if ((1024 & this.mFlags) == 0) {
            this.createPayloadsIfNeeded();
            this.mPayloads.add(var1);
         }

      }

      void addFlags(int var1) {
         this.mFlags |= var1;
      }

      void clearOldPosition() {
         this.mOldPosition = -1;
         this.mPreLayoutPosition = -1;
      }

      void clearPayload() {
         List var1 = this.mPayloads;
         if (var1 != null) {
            var1.clear();
         }

         this.mFlags &= -1025;
      }

      void clearReturnedFromScrapFlag() {
         this.mFlags &= -33;
      }

      void clearTmpDetachFlag() {
         this.mFlags &= -257;
      }

      boolean doesTransientStatePreventRecycling() {
         boolean var1;
         if ((this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      void flagRemovedAndOffsetPosition(int var1, int var2, boolean var3) {
         this.addFlags(8);
         this.offsetPosition(var2, var3);
         this.mPosition = var1;
      }

      public final int getAdapterPosition() {
         RecyclerView var1 = this.mOwnerRecyclerView;
         return var1 == null ? -1 : var1.getAdapterPositionFor(this);
      }

      public final long getItemId() {
         return this.mItemId;
      }

      public final int getItemViewType() {
         return this.mItemViewType;
      }

      public final int getLayoutPosition() {
         int var1 = this.mPreLayoutPosition;
         int var2 = var1;
         if (var1 == -1) {
            var2 = this.mPosition;
         }

         return var2;
      }

      public final int getOldPosition() {
         return this.mOldPosition;
      }

      @Deprecated
      public final int getPosition() {
         int var1 = this.mPreLayoutPosition;
         int var2 = var1;
         if (var1 == -1) {
            var2 = this.mPosition;
         }

         return var2;
      }

      List getUnmodifiedPayloads() {
         if ((this.mFlags & 1024) == 0) {
            List var1 = this.mPayloads;
            return var1 != null && var1.size() != 0 ? this.mUnmodifiedPayloads : FULLUPDATE_PAYLOADS;
         } else {
            return FULLUPDATE_PAYLOADS;
         }
      }

      boolean hasAnyOfTheFlags(int var1) {
         boolean var2;
         if ((var1 & this.mFlags) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      boolean isAdapterPositionUnknown() {
         boolean var1;
         if ((this.mFlags & 512) == 0 && !this.isInvalid()) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      boolean isAttachedToTransitionOverlay() {
         boolean var1;
         if (this.itemView.getParent() != null && this.itemView.getParent() != this.mOwnerRecyclerView) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean isBound() {
         int var1 = this.mFlags;
         boolean var2 = true;
         if ((var1 & 1) == 0) {
            var2 = false;
         }

         return var2;
      }

      boolean isInvalid() {
         boolean var1;
         if ((this.mFlags & 4) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public final boolean isRecyclable() {
         boolean var1;
         if ((this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean isRemoved() {
         boolean var1;
         if ((this.mFlags & 8) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean isScrap() {
         boolean var1;
         if (this.mScrapContainer != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean isTmpDetached() {
         boolean var1;
         if ((this.mFlags & 256) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean isUpdated() {
         boolean var1;
         if ((this.mFlags & 2) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean needsUpdate() {
         boolean var1;
         if ((this.mFlags & 2) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      void offsetPosition(int var1, boolean var2) {
         if (this.mOldPosition == -1) {
            this.mOldPosition = this.mPosition;
         }

         if (this.mPreLayoutPosition == -1) {
            this.mPreLayoutPosition = this.mPosition;
         }

         if (var2) {
            this.mPreLayoutPosition += var1;
         }

         this.mPosition += var1;
         if (this.itemView.getLayoutParams() != null) {
            ((RecyclerView.LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
         }

      }

      void onEnteredHiddenState(RecyclerView var1) {
         int var2 = this.mPendingAccessibilityState;
         if (var2 != -1) {
            this.mWasImportantForAccessibilityBeforeHidden = var2;
         } else {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
         }

         var1.setChildImportantForAccessibilityInternal(this, 4);
      }

      void onLeftHiddenState(RecyclerView var1) {
         var1.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
         this.mWasImportantForAccessibilityBeforeHidden = 0;
      }

      void resetInternal() {
         this.mFlags = 0;
         this.mPosition = -1;
         this.mOldPosition = -1;
         this.mItemId = -1L;
         this.mPreLayoutPosition = -1;
         this.mIsRecyclableCount = 0;
         this.mShadowedHolder = null;
         this.mShadowingHolder = null;
         this.clearPayload();
         this.mWasImportantForAccessibilityBeforeHidden = 0;
         this.mPendingAccessibilityState = -1;
         RecyclerView.clearNestedRecyclerViewIfNotNested(this);
      }

      void saveOldPosition() {
         if (this.mOldPosition == -1) {
            this.mOldPosition = this.mPosition;
         }

      }

      void setFlags(int var1, int var2) {
         this.mFlags = var1 & var2 | this.mFlags & ~var2;
      }

      public final void setIsRecyclable(boolean var1) {
         int var2;
         if (var1) {
            var2 = this.mIsRecyclableCount - 1;
         } else {
            var2 = this.mIsRecyclableCount + 1;
         }

         this.mIsRecyclableCount = var2;
         var2 = this.mIsRecyclableCount;
         if (var2 < 0) {
            this.mIsRecyclableCount = 0;
            StringBuilder var3 = new StringBuilder();
            var3.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
            var3.append(this);
            Log.e("View", var3.toString());
         } else if (!var1 && var2 == 1) {
            this.mFlags |= 16;
         } else if (var1 && this.mIsRecyclableCount == 0) {
            this.mFlags &= -17;
         }

      }

      void setScrapContainer(RecyclerView.Recycler var1, boolean var2) {
         this.mScrapContainer = var1;
         this.mInChangeScrap = var2;
      }

      boolean shouldBeKeptAsChild() {
         boolean var1;
         if ((this.mFlags & 16) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean shouldIgnore() {
         boolean var1;
         if ((this.mFlags & 128) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      void stopIgnoring() {
         this.mFlags &= -129;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("ViewHolder{");
         var1.append(Integer.toHexString(this.hashCode()));
         var1.append(" position=");
         var1.append(this.mPosition);
         var1.append(" id=");
         var1.append(this.mItemId);
         var1.append(", oldPos=");
         var1.append(this.mOldPosition);
         var1.append(", pLpos:");
         var1.append(this.mPreLayoutPosition);
         StringBuilder var2 = new StringBuilder(var1.toString());
         if (this.isScrap()) {
            var2.append(" scrap ");
            String var3;
            if (this.mInChangeScrap) {
               var3 = "[changeScrap]";
            } else {
               var3 = "[attachedScrap]";
            }

            var2.append(var3);
         }

         if (this.isInvalid()) {
            var2.append(" invalid");
         }

         if (!this.isBound()) {
            var2.append(" unbound");
         }

         if (this.needsUpdate()) {
            var2.append(" update");
         }

         if (this.isRemoved()) {
            var2.append(" removed");
         }

         if (this.shouldIgnore()) {
            var2.append(" ignored");
         }

         if (this.isTmpDetached()) {
            var2.append(" tmpDetached");
         }

         if (!this.isRecyclable()) {
            var1 = new StringBuilder();
            var1.append(" not recyclable(");
            var1.append(this.mIsRecyclableCount);
            var1.append(")");
            var2.append(var1.toString());
         }

         if (this.isAdapterPositionUnknown()) {
            var2.append(" undefined adapter position");
         }

         if (this.itemView.getParent() == null) {
            var2.append(" no parent");
         }

         var2.append("}");
         return var2.toString();
      }

      void unScrap() {
         this.mScrapContainer.unscrapView(this);
      }

      boolean wasReturnedFromScrap() {
         boolean var1;
         if ((this.mFlags & 32) != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }
}
