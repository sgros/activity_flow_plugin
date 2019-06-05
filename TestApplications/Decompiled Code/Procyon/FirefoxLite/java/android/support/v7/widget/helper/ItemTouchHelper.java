// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget.helper;

import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import android.animation.Animator$AnimatorListener;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.view.animation.Interpolator;
import android.view.ViewParent;
import android.animation.Animator;
import android.graphics.Canvas;
import android.content.res.Resources;
import android.support.v7.recyclerview.R;
import android.view.GestureDetector$OnGestureListener;
import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.support.v4.view.ViewCompat;
import java.util.ArrayList;
import android.view.VelocityTracker;
import android.graphics.Rect;
import android.view.View;
import android.support.v4.view.GestureDetectorCompat;
import java.util.List;
import android.support.v7.widget.RecyclerView;

public class ItemTouchHelper extends ItemDecoration implements OnChildAttachStateChangeListener
{
    private int mActionState;
    int mActivePointerId;
    Callback mCallback;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    private ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
    private float mMaxSwipeVelocity;
    private final OnItemTouchListener mOnItemTouchListener;
    View mOverdrawChild;
    int mOverdrawChildPosition;
    final List<View> mPendingCleanup;
    List<RecoverAnimation> mRecoverAnimations;
    RecyclerView mRecyclerView;
    final Runnable mScrollRunnable;
    ViewHolder mSelected;
    int mSelectedFlags;
    private float mSelectedStartX;
    private float mSelectedStartY;
    private int mSlop;
    private List<ViewHolder> mSwapTargets;
    private float mSwipeEscapeVelocity;
    private final float[] mTmpPosition;
    private Rect mTmpRect;
    VelocityTracker mVelocityTracker;
    
    public ItemTouchHelper(final Callback mCallback) {
        this.mPendingCleanup = new ArrayList<View>();
        this.mTmpPosition = new float[2];
        this.mSelected = null;
        this.mActivePointerId = -1;
        this.mActionState = 0;
        this.mRecoverAnimations = new ArrayList<RecoverAnimation>();
        this.mScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
                    if (ItemTouchHelper.this.mSelected != null) {
                        ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected);
                    }
                    ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                    ViewCompat.postOnAnimation((View)ItemTouchHelper.this.mRecyclerView, this);
                }
            }
        };
        this.mChildDrawingOrderCallback = null;
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.mOnItemTouchListener = new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
                ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
                final int actionMasked = motionEvent.getActionMasked();
                boolean b = true;
                if (actionMasked == 0) {
                    ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(0);
                    ItemTouchHelper.this.mInitialTouchX = motionEvent.getX();
                    ItemTouchHelper.this.mInitialTouchY = motionEvent.getY();
                    ItemTouchHelper.this.obtainVelocityTracker();
                    if (ItemTouchHelper.this.mSelected == null) {
                        final RecoverAnimation animation = ItemTouchHelper.this.findAnimation(motionEvent);
                        if (animation != null) {
                            final ItemTouchHelper this$0 = ItemTouchHelper.this;
                            this$0.mInitialTouchX -= animation.mX;
                            final ItemTouchHelper this$2 = ItemTouchHelper.this;
                            this$2.mInitialTouchY -= animation.mY;
                            ItemTouchHelper.this.endRecoverAnimation(animation.mViewHolder, true);
                            if (ItemTouchHelper.this.mPendingCleanup.remove(animation.mViewHolder.itemView)) {
                                ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, animation.mViewHolder);
                            }
                            ItemTouchHelper.this.select(animation.mViewHolder, animation.mActionState);
                            ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, 0);
                        }
                    }
                }
                else if (actionMasked != 3 && actionMasked != 1) {
                    if (ItemTouchHelper.this.mActivePointerId != -1) {
                        final int pointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                        if (pointerIndex >= 0) {
                            ItemTouchHelper.this.checkSelectForSwipe(actionMasked, motionEvent, pointerIndex);
                        }
                    }
                }
                else {
                    ItemTouchHelper.this.mActivePointerId = -1;
                    ItemTouchHelper.this.select(null, 0);
                }
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
                }
                if (ItemTouchHelper.this.mSelected == null) {
                    b = false;
                }
                return b;
            }
            
            @Override
            public void onRequestDisallowInterceptTouchEvent(final boolean b) {
                if (!b) {
                    return;
                }
                ItemTouchHelper.this.select(null, 0);
            }
            
            @Override
            public void onTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
                ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
                }
                if (ItemTouchHelper.this.mActivePointerId == -1) {
                    return;
                }
                final int actionMasked = motionEvent.getActionMasked();
                final int pointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                if (pointerIndex >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(actionMasked, motionEvent, pointerIndex);
                }
                final ViewHolder mSelected = ItemTouchHelper.this.mSelected;
                if (mSelected == null) {
                    return;
                }
                int n = 0;
                if (actionMasked != 6) {
                    switch (actionMasked) {
                        case 2: {
                            if (pointerIndex >= 0) {
                                ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, pointerIndex);
                                ItemTouchHelper.this.moveIfNecessary(mSelected);
                                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                                ItemTouchHelper.this.mScrollRunnable.run();
                                ItemTouchHelper.this.mRecyclerView.invalidate();
                                break;
                            }
                            break;
                        }
                        case 3: {
                            if (ItemTouchHelper.this.mVelocityTracker != null) {
                                ItemTouchHelper.this.mVelocityTracker.clear();
                            }
                        }
                        case 1: {
                            ItemTouchHelper.this.select(null, 0);
                            ItemTouchHelper.this.mActivePointerId = -1;
                            break;
                        }
                    }
                }
                else {
                    final int actionIndex = motionEvent.getActionIndex();
                    if (motionEvent.getPointerId(actionIndex) == ItemTouchHelper.this.mActivePointerId) {
                        if (actionIndex == 0) {
                            n = 1;
                        }
                        ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(n);
                        ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, actionIndex);
                    }
                }
            }
        };
        this.mCallback = mCallback;
    }
    
    private void addChildDrawingOrderCallback() {
        if (Build$VERSION.SDK_INT >= 21) {
            return;
        }
        if (this.mChildDrawingOrderCallback == null) {
            this.mChildDrawingOrderCallback = new ChildDrawingOrderCallback() {
                @Override
                public int onGetChildDrawingOrder(final int n, int n2) {
                    if (ItemTouchHelper.this.mOverdrawChild == null) {
                        return n2;
                    }
                    int mOverdrawChildPosition;
                    if ((mOverdrawChildPosition = ItemTouchHelper.this.mOverdrawChildPosition) == -1) {
                        mOverdrawChildPosition = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                        ItemTouchHelper.this.mOverdrawChildPosition = mOverdrawChildPosition;
                    }
                    if (n2 == n - 1) {
                        return mOverdrawChildPosition;
                    }
                    if (n2 >= mOverdrawChildPosition) {
                        ++n2;
                    }
                    return n2;
                }
            };
        }
        this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
    }
    
    private int checkHorizontalSwipe(final ViewHolder viewHolder, final int n) {
        if ((n & 0xC) != 0x0) {
            final float mDx = this.mDx;
            int n2 = 4;
            int n3;
            if (mDx > 0.0f) {
                n3 = 8;
            }
            else {
                n3 = 4;
            }
            if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
                final float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
                final float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                if (xVelocity > 0.0f) {
                    n2 = 8;
                }
                final float abs = Math.abs(xVelocity);
                if ((n2 & n) != 0x0 && n3 == n2 && abs >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && abs > Math.abs(yVelocity)) {
                    return n2;
                }
            }
            final float n4 = (float)this.mRecyclerView.getWidth();
            final float swipeThreshold = this.mCallback.getSwipeThreshold(viewHolder);
            if ((n & n3) != 0x0 && Math.abs(this.mDx) > n4 * swipeThreshold) {
                return n3;
            }
        }
        return 0;
    }
    
    private int checkVerticalSwipe(final ViewHolder viewHolder, final int n) {
        if ((n & 0x3) != 0x0) {
            final float mDy = this.mDy;
            int n2 = 1;
            int n3;
            if (mDy > 0.0f) {
                n3 = 2;
            }
            else {
                n3 = 1;
            }
            if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
                final float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
                final float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                if (yVelocity > 0.0f) {
                    n2 = 2;
                }
                final float abs = Math.abs(yVelocity);
                if ((n2 & n) != 0x0 && n2 == n3 && abs >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && abs > Math.abs(xVelocity)) {
                    return n2;
                }
            }
            final float n4 = (float)this.mRecyclerView.getHeight();
            final float swipeThreshold = this.mCallback.getSwipeThreshold(viewHolder);
            if ((n & n3) != 0x0 && Math.abs(this.mDy) > n4 * swipeThreshold) {
                return n3;
            }
        }
        return 0;
    }
    
    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration((RecyclerView.ItemDecoration)this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener((RecyclerView.OnChildAttachStateChangeListener)this);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            this.mCallback.clearView(this.mRecyclerView, this.mRecoverAnimations.get(0).mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.releaseVelocityTracker();
        this.stopGestureDetection();
    }
    
    private List<ViewHolder> findSwapTargets(final ViewHolder viewHolder) {
        if (this.mSwapTargets == null) {
            this.mSwapTargets = new ArrayList<ViewHolder>();
            this.mDistances = new ArrayList<Integer>();
        }
        else {
            this.mSwapTargets.clear();
            this.mDistances.clear();
        }
        final int boundingBoxMargin = this.mCallback.getBoundingBoxMargin();
        final int n = Math.round(this.mSelectedStartX + this.mDx) - boundingBoxMargin;
        final int n2 = Math.round(this.mSelectedStartY + this.mDy) - boundingBoxMargin;
        final int width = viewHolder.itemView.getWidth();
        final int n3 = boundingBoxMargin * 2;
        final int n4 = width + n + n3;
        final int n5 = viewHolder.itemView.getHeight() + n2 + n3;
        final int n6 = (n + n4) / 2;
        final int n7 = (n2 + n5) / 2;
        final RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        for (int childCount = layoutManager.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = layoutManager.getChildAt(i);
            if (child != viewHolder.itemView) {
                if (child.getBottom() >= n2 && child.getTop() <= n5 && child.getRight() >= n) {
                    if (child.getLeft() <= n4) {
                        final RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(child);
                        if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, childViewHolder)) {
                            final int abs = Math.abs(n6 - (child.getLeft() + child.getRight()) / 2);
                            final int abs2 = Math.abs(n7 - (child.getTop() + child.getBottom()) / 2);
                            final int j = abs * abs + abs2 * abs2;
                            final int size = this.mSwapTargets.size();
                            int n8 = 0;
                            int n9 = 0;
                            while (n8 < size && j > this.mDistances.get(n8)) {
                                ++n9;
                                ++n8;
                            }
                            this.mSwapTargets.add(n9, childViewHolder);
                            this.mDistances.add(n9, j);
                        }
                    }
                }
            }
        }
        return this.mSwapTargets;
    }
    
    private ViewHolder findSwipedView(final MotionEvent motionEvent) {
        final RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mActivePointerId == -1) {
            return null;
        }
        final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
        final float x = motionEvent.getX(pointerIndex);
        final float mInitialTouchX = this.mInitialTouchX;
        final float y = motionEvent.getY(pointerIndex);
        final float mInitialTouchY = this.mInitialTouchY;
        final float abs = Math.abs(x - mInitialTouchX);
        final float abs2 = Math.abs(y - mInitialTouchY);
        if (abs < this.mSlop && abs2 < this.mSlop) {
            return null;
        }
        if (abs > abs2 && layoutManager.canScrollHorizontally()) {
            return null;
        }
        if (abs2 > abs && layoutManager.canScrollVertically()) {
            return null;
        }
        final View childView = this.findChildView(motionEvent);
        if (childView == null) {
            return null;
        }
        return this.mRecyclerView.getChildViewHolder(childView);
    }
    
    private void getSelectedDxDy(final float[] array) {
        if ((this.mSelectedFlags & 0xC) != 0x0) {
            array[0] = this.mSelectedStartX + this.mDx - this.mSelected.itemView.getLeft();
        }
        else {
            array[0] = this.mSelected.itemView.getTranslationX();
        }
        if ((this.mSelectedFlags & 0x3) != 0x0) {
            array[1] = this.mSelectedStartY + this.mDy - this.mSelected.itemView.getTop();
        }
        else {
            array[1] = this.mSelected.itemView.getTranslationY();
        }
    }
    
    private static boolean hitTest(final View view, final float n, final float n2, final float n3, final float n4) {
        return n >= n3 && n <= n3 + view.getWidth() && n2 >= n4 && n2 <= n4 + view.getHeight();
    }
    
    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener((RecyclerView.OnChildAttachStateChangeListener)this);
        this.startGestureDetection();
    }
    
    private void startGestureDetection() {
        this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener();
        this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), (GestureDetector$OnGestureListener)this.mItemTouchHelperGestureListener);
    }
    
    private void stopGestureDetection() {
        if (this.mItemTouchHelperGestureListener != null) {
            this.mItemTouchHelperGestureListener.doNotReactToLongPress();
            this.mItemTouchHelperGestureListener = null;
        }
        if (this.mGestureDetector != null) {
            this.mGestureDetector = null;
        }
    }
    
    private int swipeIfNecessary(final ViewHolder viewHolder) {
        if (this.mActionState == 2) {
            return 0;
        }
        final int movementFlags = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        final int n = (this.mCallback.convertToAbsoluteDirection(movementFlags, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) & 0xFF00) >> 8;
        if (n == 0) {
            return 0;
        }
        final int n2 = (movementFlags & 0xFF00) >> 8;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            final int checkHorizontalSwipe = this.checkHorizontalSwipe(viewHolder, n);
            if (checkHorizontalSwipe > 0) {
                if ((n2 & checkHorizontalSwipe) == 0x0) {
                    return Callback.convertToRelativeDirection(checkHorizontalSwipe, ViewCompat.getLayoutDirection((View)this.mRecyclerView));
                }
                return checkHorizontalSwipe;
            }
            else {
                final int checkVerticalSwipe = this.checkVerticalSwipe(viewHolder, n);
                if (checkVerticalSwipe > 0) {
                    return checkVerticalSwipe;
                }
            }
        }
        else {
            final int checkVerticalSwipe2 = this.checkVerticalSwipe(viewHolder, n);
            if (checkVerticalSwipe2 > 0) {
                return checkVerticalSwipe2;
            }
            final int checkHorizontalSwipe2 = this.checkHorizontalSwipe(viewHolder, n);
            if (checkHorizontalSwipe2 > 0) {
                if ((n2 & checkHorizontalSwipe2) == 0x0) {
                    return Callback.convertToRelativeDirection(checkHorizontalSwipe2, ViewCompat.getLayoutDirection((View)this.mRecyclerView));
                }
                return checkHorizontalSwipe2;
            }
        }
        return 0;
    }
    
    public void attachToRecyclerView(final RecyclerView mRecyclerView) {
        if (this.mRecyclerView == mRecyclerView) {
            return;
        }
        if (this.mRecyclerView != null) {
            this.destroyCallbacks();
        }
        if ((this.mRecyclerView = mRecyclerView) != null) {
            final Resources resources = mRecyclerView.getResources();
            this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
            this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
            this.setupCallbacks();
        }
    }
    
    void checkSelectForSwipe(int n, final MotionEvent motionEvent, final int n2) {
        if (this.mSelected != null || n != 2 || this.mActionState == 2 || !this.mCallback.isItemViewSwipeEnabled()) {
            return;
        }
        if (this.mRecyclerView.getScrollState() == 1) {
            return;
        }
        final ViewHolder swipedView = this.findSwipedView(motionEvent);
        if (swipedView == null) {
            return;
        }
        n = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, swipedView) & 0xFF00) >> 8;
        if (n == 0) {
            return;
        }
        final float x = motionEvent.getX(n2);
        final float y = motionEvent.getY(n2);
        final float a = x - this.mInitialTouchX;
        final float a2 = y - this.mInitialTouchY;
        final float abs = Math.abs(a);
        final float abs2 = Math.abs(a2);
        if (abs < this.mSlop && abs2 < this.mSlop) {
            return;
        }
        if (abs > abs2) {
            if (a < 0.0f && (n & 0x4) == 0x0) {
                return;
            }
            if (a > 0.0f && (n & 0x8) == 0x0) {
                return;
            }
        }
        else {
            if (a2 < 0.0f && (n & 0x1) == 0x0) {
                return;
            }
            if (a2 > 0.0f && (n & 0x2) == 0x0) {
                return;
            }
        }
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.mActivePointerId = motionEvent.getPointerId(0);
        this.select(swipedView, 1);
    }
    
    void endRecoverAnimation(final ViewHolder viewHolder, final boolean b) {
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            final RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
            if (recoverAnimation.mViewHolder == viewHolder) {
                recoverAnimation.mOverridden |= b;
                if (!recoverAnimation.mEnded) {
                    recoverAnimation.cancel();
                }
                this.mRecoverAnimations.remove(i);
                return;
            }
        }
    }
    
    RecoverAnimation findAnimation(final MotionEvent motionEvent) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        final View childView = this.findChildView(motionEvent);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            final RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
            if (recoverAnimation.mViewHolder.itemView == childView) {
                return recoverAnimation;
            }
        }
        return null;
    }
    
    View findChildView(final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        if (this.mSelected != null) {
            final View itemView = this.mSelected.itemView;
            if (hitTest(itemView, x, y, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
                return itemView;
            }
        }
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            final RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
            final View itemView2 = recoverAnimation.mViewHolder.itemView;
            if (hitTest(itemView2, x, y, recoverAnimation.mX, recoverAnimation.mY)) {
                return itemView2;
            }
        }
        return this.mRecyclerView.findChildViewUnder(x, y);
    }
    
    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
        rect.setEmpty();
    }
    
    boolean hasRunningRecoverAnim() {
        for (int size = this.mRecoverAnimations.size(), i = 0; i < size; ++i) {
            if (!this.mRecoverAnimations.get(i).mEnded) {
                return true;
            }
        }
        return false;
    }
    
    void moveIfNecessary(final ViewHolder viewHolder) {
        if (this.mRecyclerView.isLayoutRequested()) {
            return;
        }
        if (this.mActionState != 2) {
            return;
        }
        final float moveThreshold = this.mCallback.getMoveThreshold(viewHolder);
        final int n = (int)(this.mSelectedStartX + this.mDx);
        final int n2 = (int)(this.mSelectedStartY + this.mDy);
        if (Math.abs(n2 - viewHolder.itemView.getTop()) < viewHolder.itemView.getHeight() * moveThreshold && Math.abs(n - viewHolder.itemView.getLeft()) < viewHolder.itemView.getWidth() * moveThreshold) {
            return;
        }
        final List<ViewHolder> swapTargets = this.findSwapTargets(viewHolder);
        if (swapTargets.size() == 0) {
            return;
        }
        final ViewHolder chooseDropTarget = this.mCallback.chooseDropTarget(viewHolder, swapTargets, n, n2);
        if (chooseDropTarget == null) {
            this.mSwapTargets.clear();
            this.mDistances.clear();
            return;
        }
        final int adapterPosition = chooseDropTarget.getAdapterPosition();
        final int adapterPosition2 = viewHolder.getAdapterPosition();
        if (this.mCallback.onMove(this.mRecyclerView, viewHolder, chooseDropTarget)) {
            this.mCallback.onMoved(this.mRecyclerView, viewHolder, adapterPosition2, chooseDropTarget, adapterPosition, n, n2);
        }
    }
    
    void obtainVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }
    
    @Override
    public void onChildViewAttachedToWindow(final View view) {
    }
    
    @Override
    public void onChildViewDetachedFromWindow(final View view) {
        this.removeChildDrawingOrderCallbackIfNecessary(view);
        final RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (childViewHolder == null) {
            return;
        }
        if (this.mSelected != null && childViewHolder == this.mSelected) {
            this.select(null, 0);
        }
        else {
            this.endRecoverAnimation(childViewHolder, false);
            if (this.mPendingCleanup.remove(childViewHolder.itemView)) {
                this.mCallback.clearView(this.mRecyclerView, childViewHolder);
            }
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final RecyclerView recyclerView, final State state) {
        this.mOverdrawChildPosition = -1;
        float n;
        float n2;
        if (this.mSelected != null) {
            this.getSelectedDxDy(this.mTmpPosition);
            n = this.mTmpPosition[0];
            n2 = this.mTmpPosition[1];
        }
        else {
            n = 0.0f;
            n2 = 0.0f;
        }
        this.mCallback.onDraw(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, n, n2);
    }
    
    @Override
    public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final State state) {
        float n;
        float n2;
        if (this.mSelected != null) {
            this.getSelectedDxDy(this.mTmpPosition);
            n = this.mTmpPosition[0];
            n2 = this.mTmpPosition[1];
        }
        else {
            n = 0.0f;
            n2 = 0.0f;
        }
        this.mCallback.onDrawOver(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, n, n2);
    }
    
    void postDispatchSwipe(final RecoverAnimation recoverAnimation, final int n) {
        this.mRecyclerView.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !recoverAnimation.mOverridden && recoverAnimation.mViewHolder.getAdapterPosition() != -1) {
                    final RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                    if ((itemAnimator == null || !itemAnimator.isRunning(null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                        ItemTouchHelper.this.mCallback.onSwiped(recoverAnimation.mViewHolder, n);
                    }
                    else {
                        ItemTouchHelper.this.mRecyclerView.post((Runnable)this);
                    }
                }
            }
        });
    }
    
    void removeChildDrawingOrderCallbackIfNecessary(final View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback(null);
            }
        }
    }
    
    boolean scrollIfNecessary() {
        if (this.mSelected == null) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        long n;
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            n = 0L;
        }
        else {
            n = currentTimeMillis - this.mDragScrollStartTimeInMs;
        }
        final RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
        }
        layoutManager.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        int n3 = 0;
        Label_0202: {
            if (layoutManager.canScrollHorizontally()) {
                final int n2 = (int)(this.mSelectedStartX + this.mDx);
                n3 = n2 - this.mTmpRect.left - this.mRecyclerView.getPaddingLeft();
                if (this.mDx < 0.0f && n3 < 0) {
                    break Label_0202;
                }
                if (this.mDx > 0.0f) {
                    n3 = n2 + this.mSelected.itemView.getWidth() + this.mTmpRect.right - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight());
                    if (n3 > 0) {
                        break Label_0202;
                    }
                }
            }
            n3 = 0;
        }
        int interpolateOutOfBoundsScroll = 0;
        Label_0318: {
            if (layoutManager.canScrollVertically()) {
                final int n4 = (int)(this.mSelectedStartY + this.mDy);
                interpolateOutOfBoundsScroll = n4 - this.mTmpRect.top - this.mRecyclerView.getPaddingTop();
                if (this.mDy < 0.0f && interpolateOutOfBoundsScroll < 0) {
                    break Label_0318;
                }
                if (this.mDy > 0.0f) {
                    interpolateOutOfBoundsScroll = n4 + this.mSelected.itemView.getHeight() + this.mTmpRect.bottom - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom());
                    if (interpolateOutOfBoundsScroll > 0) {
                        break Label_0318;
                    }
                }
            }
            interpolateOutOfBoundsScroll = 0;
        }
        int interpolateOutOfBoundsScroll2 = n3;
        if (n3 != 0) {
            interpolateOutOfBoundsScroll2 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), n3, this.mRecyclerView.getWidth(), n);
        }
        if (interpolateOutOfBoundsScroll != 0) {
            interpolateOutOfBoundsScroll = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), interpolateOutOfBoundsScroll, this.mRecyclerView.getHeight(), n);
        }
        if (interpolateOutOfBoundsScroll2 == 0 && interpolateOutOfBoundsScroll == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            this.mDragScrollStartTimeInMs = currentTimeMillis;
        }
        this.mRecyclerView.scrollBy(interpolateOutOfBoundsScroll2, interpolateOutOfBoundsScroll);
        return true;
    }
    
    void select(final ViewHolder mSelected, final int mActionState) {
        if (mSelected == this.mSelected && mActionState == this.mActionState) {
            return;
        }
        this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
        final int mActionState2 = this.mActionState;
        this.endRecoverAnimation(mSelected, true);
        if ((this.mActionState = mActionState) == 2) {
            if (mSelected == null) {
                throw new IllegalArgumentException("Must pass a ViewHolder when dragging");
            }
            this.mOverdrawChild = mSelected.itemView;
            this.addChildDrawingOrderCallback();
        }
        boolean b;
        if (this.mSelected != null) {
            final ViewHolder mSelected2 = this.mSelected;
            if (mSelected2.itemView.getParent() != null) {
                int swipeIfNecessary;
                if (mActionState2 == 2) {
                    swipeIfNecessary = 0;
                }
                else {
                    swipeIfNecessary = this.swipeIfNecessary(mSelected2);
                }
                this.releaseVelocityTracker();
                float n = 0.0f;
                float n2 = 0.0f;
                Label_0226: {
                    if (swipeIfNecessary != 4 && swipeIfNecessary != 8 && swipeIfNecessary != 16 && swipeIfNecessary != 32) {
                        switch (swipeIfNecessary) {
                            default: {
                                n = 0.0f;
                                break;
                            }
                            case 1:
                            case 2: {
                                n2 = Math.signum(this.mDy) * this.mRecyclerView.getHeight();
                                n = 0.0f;
                                break Label_0226;
                            }
                        }
                    }
                    else {
                        n = Math.signum(this.mDx) * this.mRecyclerView.getWidth();
                    }
                    n2 = 0.0f;
                }
                int n3;
                if (mActionState2 == 2) {
                    n3 = 8;
                }
                else if (swipeIfNecessary > 0) {
                    n3 = 2;
                }
                else {
                    n3 = 4;
                }
                this.getSelectedDxDy(this.mTmpPosition);
                final float n4 = this.mTmpPosition[0];
                final float n5 = this.mTmpPosition[1];
                final RecoverAnimation recoverAnimation = new RecoverAnimation(mSelected2, n3, mActionState2, n4, n5, n, n2) {
                    @Override
                    public void onAnimationEnd(final Animator animator) {
                        super.onAnimationEnd(animator);
                        if (this.mOverridden) {
                            return;
                        }
                        if (swipeIfNecessary <= 0) {
                            ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, mSelected2);
                        }
                        else {
                            ItemTouchHelper.this.mPendingCleanup.add(mSelected2.itemView);
                            this.mIsPendingCleanup = true;
                            if (swipeIfNecessary > 0) {
                                ItemTouchHelper.this.postDispatchSwipe((RecoverAnimation)this, swipeIfNecessary);
                            }
                        }
                        if (ItemTouchHelper.this.mOverdrawChild == mSelected2.itemView) {
                            ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(mSelected2.itemView);
                        }
                    }
                };
                ((RecoverAnimation)recoverAnimation).setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, n3, n - n4, n2 - n5));
                this.mRecoverAnimations.add((RecoverAnimation)recoverAnimation);
                ((RecoverAnimation)recoverAnimation).start();
                b = true;
            }
            else {
                this.removeChildDrawingOrderCallbackIfNecessary(mSelected2.itemView);
                this.mCallback.clearView(this.mRecyclerView, mSelected2);
                b = false;
            }
            this.mSelected = null;
        }
        else {
            b = false;
        }
        if (mSelected != null) {
            this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, mSelected) & (1 << mActionState * 8 + 8) - 1) >> this.mActionState * 8;
            this.mSelectedStartX = (float)mSelected.itemView.getLeft();
            this.mSelectedStartY = (float)mSelected.itemView.getTop();
            this.mSelected = mSelected;
            if (mActionState == 2) {
                this.mSelected.itemView.performHapticFeedback(0);
            }
        }
        boolean b2 = false;
        final ViewParent parent = this.mRecyclerView.getParent();
        if (parent != null) {
            if (this.mSelected != null) {
                b2 = true;
            }
            parent.requestDisallowInterceptTouchEvent(b2);
        }
        if (!b) {
            this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
        }
        this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
        this.mRecyclerView.invalidate();
    }
    
    void updateDxDy(final MotionEvent motionEvent, final int n, final int n2) {
        final float x = motionEvent.getX(n2);
        final float y = motionEvent.getY(n2);
        this.mDx = x - this.mInitialTouchX;
        this.mDy = y - this.mInitialTouchY;
        if ((n & 0x4) == 0x0) {
            this.mDx = Math.max(0.0f, this.mDx);
        }
        if ((n & 0x8) == 0x0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((n & 0x1) == 0x0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((n & 0x2) == 0x0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }
    
    public abstract static class Callback
    {
        private static final Interpolator sDragScrollInterpolator;
        private static final Interpolator sDragViewScrollCapInterpolator;
        private int mCachedMaxScrollSpeed;
        
        static {
            sDragScrollInterpolator = (Interpolator)new Interpolator() {
                public float getInterpolation(final float n) {
                    return n * n * n * n * n;
                }
            };
            sDragViewScrollCapInterpolator = (Interpolator)new Interpolator() {
                public float getInterpolation(float n) {
                    --n;
                    return n * n * n * n * n + 1.0f;
                }
            };
        }
        
        public Callback() {
            this.mCachedMaxScrollSpeed = -1;
        }
        
        public static int convertToRelativeDirection(int n, int n2) {
            final int n3 = n & 0xC0C0C;
            if (n3 == 0) {
                return n;
            }
            n &= n3;
            if (n2 == 0) {
                return n | n3 << 2;
            }
            n2 = n3 << 1;
            return n | (0xFFF3F3F3 & n2) | (n2 & 0xC0C0C) << 2;
        }
        
        private int getMaxDragScroll(final RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }
        
        public static int makeFlag(final int n, final int n2) {
            return n2 << n * 8;
        }
        
        public static int makeMovementFlags(final int n, int flag) {
            final int flag2 = makeFlag(0, flag | n);
            flag = makeFlag(1, flag);
            return makeFlag(2, n) | (flag | flag2);
        }
        
        public boolean canDropOver(final RecyclerView recyclerView, final ViewHolder viewHolder, final ViewHolder viewHolder2) {
            return true;
        }
        
        public ViewHolder chooseDropTarget(final ViewHolder viewHolder, final List<ViewHolder> list, final int n, final int n2) {
            final int width = viewHolder.itemView.getWidth();
            final int height = viewHolder.itemView.getHeight();
            final int n3 = n - viewHolder.itemView.getLeft();
            final int n4 = n2 - viewHolder.itemView.getTop();
            final int size = list.size();
            ViewHolder viewHolder2 = null;
            int abs = -1;
            for (int i = 0; i < size; ++i) {
                final ViewHolder viewHolder3 = list.get(i);
                int abs2 = 0;
                Label_0146: {
                    if (n3 > 0) {
                        final int a = viewHolder3.itemView.getRight() - (n + width);
                        if (a < 0 && viewHolder3.itemView.getRight() > viewHolder.itemView.getRight()) {
                            abs2 = Math.abs(a);
                            if (abs2 > abs) {
                                viewHolder2 = viewHolder3;
                                break Label_0146;
                            }
                        }
                    }
                    abs2 = abs;
                }
                ViewHolder viewHolder4 = viewHolder2;
                int n5 = abs2;
                if (n3 < 0) {
                    final int a2 = viewHolder3.itemView.getLeft() - n;
                    viewHolder4 = viewHolder2;
                    n5 = abs2;
                    if (a2 > 0) {
                        viewHolder4 = viewHolder2;
                        n5 = abs2;
                        if (viewHolder3.itemView.getLeft() < viewHolder.itemView.getLeft()) {
                            final int abs3 = Math.abs(a2);
                            viewHolder4 = viewHolder2;
                            if (abs3 > (n5 = abs2)) {
                                n5 = abs3;
                                viewHolder4 = viewHolder3;
                            }
                        }
                    }
                }
                viewHolder2 = viewHolder4;
                int n6 = n5;
                if (n4 < 0) {
                    final int a3 = viewHolder3.itemView.getTop() - n2;
                    viewHolder2 = viewHolder4;
                    n6 = n5;
                    if (a3 > 0) {
                        viewHolder2 = viewHolder4;
                        n6 = n5;
                        if (viewHolder3.itemView.getTop() < viewHolder.itemView.getTop()) {
                            final int abs4 = Math.abs(a3);
                            viewHolder2 = viewHolder4;
                            if (abs4 > (n6 = n5)) {
                                n6 = abs4;
                                viewHolder2 = viewHolder3;
                            }
                        }
                    }
                }
                if (n4 > 0) {
                    final int a4 = viewHolder3.itemView.getBottom() - (n2 + height);
                    if (a4 < 0 && viewHolder3.itemView.getBottom() > viewHolder.itemView.getBottom()) {
                        abs = Math.abs(a4);
                        if (abs > n6) {
                            viewHolder2 = viewHolder3;
                            continue;
                        }
                    }
                }
                abs = n6;
            }
            return viewHolder2;
        }
        
        public void clearView(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            ItemTouchUIUtilImpl.INSTANCE.clearView(viewHolder.itemView);
        }
        
        public int convertToAbsoluteDirection(int n, int n2) {
            final int n3 = n & 0x303030;
            if (n3 == 0) {
                return n;
            }
            n &= n3;
            if (n2 == 0) {
                return n | n3 >> 2;
            }
            n2 = n3 >> 1;
            return n | (0xFFCFCFCF & n2) | (n2 & 0x303030) >> 2;
        }
        
        final int getAbsoluteMovementFlags(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            return this.convertToAbsoluteDirection(this.getMovementFlags(recyclerView, viewHolder), ViewCompat.getLayoutDirection((View)recyclerView));
        }
        
        public long getAnimationDuration(final RecyclerView recyclerView, final int n, final float n2, final float n3) {
            final RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            if (itemAnimator == null) {
                long n4;
                if (n == 8) {
                    n4 = 200L;
                }
                else {
                    n4 = 250L;
                }
                return n4;
            }
            long n5;
            if (n == 8) {
                n5 = itemAnimator.getMoveDuration();
            }
            else {
                n5 = itemAnimator.getRemoveDuration();
            }
            return n5;
        }
        
        public int getBoundingBoxMargin() {
            return 0;
        }
        
        public float getMoveThreshold(final ViewHolder viewHolder) {
            return 0.5f;
        }
        
        public abstract int getMovementFlags(final RecyclerView p0, final ViewHolder p1);
        
        public float getSwipeEscapeVelocity(final float n) {
            return n;
        }
        
        public float getSwipeThreshold(final ViewHolder viewHolder) {
            return 0.5f;
        }
        
        public float getSwipeVelocityThreshold(final float n) {
            return n;
        }
        
        boolean hasDragFlag(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            return (this.getAbsoluteMovementFlags(recyclerView, viewHolder) & 0xFF0000) != 0x0;
        }
        
        public int interpolateOutOfBoundsScroll(final RecyclerView recyclerView, int n, final int a, int abs, final long n2) {
            final int maxDragScroll = this.getMaxDragScroll(recyclerView);
            abs = Math.abs(a);
            final int n3 = (int)Math.signum((float)a);
            final float n4 = (float)abs;
            float n5 = 1.0f;
            n = (int)(n3 * maxDragScroll * Callback.sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, n4 * 1.0f / n)));
            if (n2 <= 2000L) {
                n5 = n2 / 2000.0f;
            }
            n *= (int)Callback.sDragScrollInterpolator.getInterpolation(n5);
            if (n == 0) {
                if (a > 0) {
                    n = 1;
                }
                else {
                    n = -1;
                }
                return n;
            }
            return n;
        }
        
        public boolean isItemViewSwipeEnabled() {
            return true;
        }
        
        public boolean isLongPressDragEnabled() {
            return true;
        }
        
        public void onChildDraw(final Canvas canvas, final RecyclerView recyclerView, final ViewHolder viewHolder, final float n, final float n2, final int n3, final boolean b) {
            ItemTouchUIUtilImpl.INSTANCE.onDraw(canvas, recyclerView, viewHolder.itemView, n, n2, n3, b);
        }
        
        public void onChildDrawOver(final Canvas canvas, final RecyclerView recyclerView, final ViewHolder viewHolder, final float n, final float n2, final int n3, final boolean b) {
            ItemTouchUIUtilImpl.INSTANCE.onDrawOver(canvas, recyclerView, viewHolder.itemView, n, n2, n3, b);
        }
        
        void onDraw(final Canvas canvas, final RecyclerView recyclerView, final ViewHolder viewHolder, final List<RecoverAnimation> list, final int n, final float n2, final float n3) {
            for (int size = list.size(), i = 0; i < size; ++i) {
                final RecoverAnimation recoverAnimation = list.get(i);
                recoverAnimation.update();
                final int save = canvas.save();
                this.onChildDraw(canvas, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                canvas.restoreToCount(save);
            }
            if (viewHolder != null) {
                final int save2 = canvas.save();
                this.onChildDraw(canvas, recyclerView, viewHolder, n2, n3, n, true);
                canvas.restoreToCount(save2);
            }
        }
        
        void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final ViewHolder viewHolder, final List<RecoverAnimation> list, int i, final float n, final float n2) {
            final int size = list.size();
            final int n3 = 0;
            for (int j = 0; j < size; ++j) {
                final RecoverAnimation recoverAnimation = list.get(j);
                final int save = canvas.save();
                this.onChildDrawOver(canvas, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                canvas.restoreToCount(save);
            }
            if (viewHolder != null) {
                final int save2 = canvas.save();
                this.onChildDrawOver(canvas, recyclerView, viewHolder, n, n2, i, true);
                canvas.restoreToCount(save2);
            }
            i = size - 1;
            int n4 = n3;
            while (i >= 0) {
                final RecoverAnimation recoverAnimation2 = list.get(i);
                if (recoverAnimation2.mEnded && !recoverAnimation2.mIsPendingCleanup) {
                    list.remove(i);
                }
                else if (!recoverAnimation2.mEnded) {
                    n4 = 1;
                }
                --i;
            }
            if (n4 != 0) {
                recyclerView.invalidate();
            }
        }
        
        public abstract boolean onMove(final RecyclerView p0, final ViewHolder p1, final ViewHolder p2);
        
        public void onMoved(final RecyclerView recyclerView, final ViewHolder viewHolder, final int n, final ViewHolder viewHolder2, final int n2, final int n3, final int n4) {
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler)layoutManager).prepareForDrop(viewHolder.itemView, viewHolder2.itemView, n3, n4);
                return;
            }
            if (layoutManager.canScrollHorizontally()) {
                if (layoutManager.getDecoratedLeft(viewHolder2.itemView) <= recyclerView.getPaddingLeft()) {
                    recyclerView.scrollToPosition(n2);
                }
                if (layoutManager.getDecoratedRight(viewHolder2.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.scrollToPosition(n2);
                }
            }
            if (layoutManager.canScrollVertically()) {
                if (layoutManager.getDecoratedTop(viewHolder2.itemView) <= recyclerView.getPaddingTop()) {
                    recyclerView.scrollToPosition(n2);
                }
                if (layoutManager.getDecoratedBottom(viewHolder2.itemView) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.scrollToPosition(n2);
                }
            }
        }
        
        public void onSelectedChanged(final ViewHolder viewHolder, final int n) {
            if (viewHolder != null) {
                ItemTouchUIUtilImpl.INSTANCE.onSelected(viewHolder.itemView);
            }
        }
        
        public abstract void onSwiped(final ViewHolder p0, final int p1);
    }
    
    private class ItemTouchHelperGestureListener extends GestureDetector$SimpleOnGestureListener
    {
        private boolean mShouldReactToLongPress;
        
        ItemTouchHelperGestureListener() {
            this.mShouldReactToLongPress = true;
        }
        
        void doNotReactToLongPress() {
            this.mShouldReactToLongPress = false;
        }
        
        public boolean onDown(final MotionEvent motionEvent) {
            return true;
        }
        
        public void onLongPress(final MotionEvent motionEvent) {
            if (!this.mShouldReactToLongPress) {
                return;
            }
            final View childView = ItemTouchHelper.this.findChildView(motionEvent);
            if (childView != null) {
                final RecyclerView.ViewHolder childViewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(childView);
                if (childViewHolder != null) {
                    if (!ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, childViewHolder)) {
                        return;
                    }
                    if (motionEvent.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
                        final int pointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                        final float x = motionEvent.getX(pointerIndex);
                        final float y = motionEvent.getY(pointerIndex);
                        ItemTouchHelper.this.mInitialTouchX = x;
                        ItemTouchHelper.this.mInitialTouchY = y;
                        final ItemTouchHelper this$0 = ItemTouchHelper.this;
                        ItemTouchHelper.this.mDy = 0.0f;
                        this$0.mDx = 0.0f;
                        if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) {
                            ItemTouchHelper.this.select(childViewHolder, 2);
                        }
                    }
                }
            }
        }
    }
    
    private static class RecoverAnimation implements Animator$AnimatorListener
    {
        final int mActionState;
        final int mAnimationType;
        boolean mEnded;
        private float mFraction;
        boolean mIsPendingCleanup;
        boolean mOverridden;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        private final ValueAnimator mValueAnimator;
        final ViewHolder mViewHolder;
        float mX;
        float mY;
        
        RecoverAnimation(final ViewHolder mViewHolder, final int mAnimationType, final int mActionState, final float mStartDx, final float mStartDy, final float mTargetX, final float mTargetY) {
            this.mOverridden = false;
            this.mEnded = false;
            this.mActionState = mActionState;
            this.mAnimationType = mAnimationType;
            this.mViewHolder = mViewHolder;
            this.mStartDx = mStartDx;
            this.mStartDy = mStartDy;
            this.mTargetX = mTargetX;
            this.mTargetY = mTargetY;
            (this.mValueAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f })).addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    RecoverAnimation.this.setFraction(valueAnimator.getAnimatedFraction());
                }
            });
            this.mValueAnimator.setTarget((Object)mViewHolder.itemView);
            this.mValueAnimator.addListener((Animator$AnimatorListener)this);
            this.setFraction(0.0f);
        }
        
        public void cancel() {
            this.mValueAnimator.cancel();
        }
        
        public void onAnimationCancel(final Animator animator) {
            this.setFraction(1.0f);
        }
        
        public void onAnimationEnd(final Animator animator) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }
        
        public void onAnimationRepeat(final Animator animator) {
        }
        
        public void onAnimationStart(final Animator animator) {
        }
        
        public void setDuration(final long duration) {
            this.mValueAnimator.setDuration(duration);
        }
        
        public void setFraction(final float mFraction) {
            this.mFraction = mFraction;
        }
        
        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }
        
        public void update() {
            if (this.mStartDx == this.mTargetX) {
                this.mX = this.mViewHolder.itemView.getTranslationX();
            }
            else {
                this.mX = this.mStartDx + this.mFraction * (this.mTargetX - this.mStartDx);
            }
            if (this.mStartDy == this.mTargetY) {
                this.mY = this.mViewHolder.itemView.getTranslationY();
            }
            else {
                this.mY = this.mStartDy + this.mFraction * (this.mTargetY - this.mStartDy);
            }
        }
    }
    
    public abstract static class SimpleCallback extends Callback
    {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;
        
        public SimpleCallback(final int mDefaultDragDirs, final int mDefaultSwipeDirs) {
            this.mDefaultSwipeDirs = mDefaultSwipeDirs;
            this.mDefaultDragDirs = mDefaultDragDirs;
        }
        
        public int getDragDirs(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }
        
        @Override
        public int getMovementFlags(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            return Callback.makeMovementFlags(this.getDragDirs(recyclerView, viewHolder), this.getSwipeDirs(recyclerView, viewHolder));
        }
        
        public int getSwipeDirs(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }
    }
    
    public interface ViewDropHandler
    {
        void prepareForDrop(final View p0, final View p1, final int p2, final int p3);
    }
}