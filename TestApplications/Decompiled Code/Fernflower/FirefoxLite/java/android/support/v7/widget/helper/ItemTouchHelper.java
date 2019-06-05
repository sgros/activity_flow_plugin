package android.support.v7.widget.helper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
   private int mActionState = 0;
   int mActivePointerId = -1;
   ItemTouchHelper.Callback mCallback;
   private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
   private List mDistances;
   private long mDragScrollStartTimeInMs;
   float mDx;
   float mDy;
   GestureDetectorCompat mGestureDetector;
   float mInitialTouchX;
   float mInitialTouchY;
   private ItemTouchHelper.ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
   private float mMaxSwipeVelocity;
   private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
      public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2) {
         ItemTouchHelper.this.mGestureDetector.onTouchEvent(var2);
         int var3 = var2.getActionMasked();
         boolean var4 = true;
         if (var3 == 0) {
            ItemTouchHelper.this.mActivePointerId = var2.getPointerId(0);
            ItemTouchHelper.this.mInitialTouchX = var2.getX();
            ItemTouchHelper.this.mInitialTouchY = var2.getY();
            ItemTouchHelper.this.obtainVelocityTracker();
            if (ItemTouchHelper.this.mSelected == null) {
               ItemTouchHelper.RecoverAnimation var7 = ItemTouchHelper.this.findAnimation(var2);
               if (var7 != null) {
                  ItemTouchHelper var5 = ItemTouchHelper.this;
                  var5.mInitialTouchX -= var7.mX;
                  var5 = ItemTouchHelper.this;
                  var5.mInitialTouchY -= var7.mY;
                  ItemTouchHelper.this.endRecoverAnimation(var7.mViewHolder, true);
                  if (ItemTouchHelper.this.mPendingCleanup.remove(var7.mViewHolder.itemView)) {
                     ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, var7.mViewHolder);
                  }

                  ItemTouchHelper.this.select(var7.mViewHolder, var7.mActionState);
                  ItemTouchHelper.this.updateDxDy(var2, ItemTouchHelper.this.mSelectedFlags, 0);
               }
            }
         } else if (var3 != 3 && var3 != 1) {
            if (ItemTouchHelper.this.mActivePointerId != -1) {
               int var6 = var2.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
               if (var6 >= 0) {
                  ItemTouchHelper.this.checkSelectForSwipe(var3, var2, var6);
               }
            }
         } else {
            ItemTouchHelper.this.mActivePointerId = -1;
            ItemTouchHelper.this.select((RecyclerView.ViewHolder)null, 0);
         }

         if (ItemTouchHelper.this.mVelocityTracker != null) {
            ItemTouchHelper.this.mVelocityTracker.addMovement(var2);
         }

         if (ItemTouchHelper.this.mSelected == null) {
            var4 = false;
         }

         return var4;
      }

      public void onRequestDisallowInterceptTouchEvent(boolean var1) {
         if (var1) {
            ItemTouchHelper.this.select((RecyclerView.ViewHolder)null, 0);
         }
      }

      public void onTouchEvent(RecyclerView var1, MotionEvent var2) {
         ItemTouchHelper.this.mGestureDetector.onTouchEvent(var2);
         if (ItemTouchHelper.this.mVelocityTracker != null) {
            ItemTouchHelper.this.mVelocityTracker.addMovement(var2);
         }

         if (ItemTouchHelper.this.mActivePointerId != -1) {
            int var3 = var2.getActionMasked();
            int var4 = var2.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
            if (var4 >= 0) {
               ItemTouchHelper.this.checkSelectForSwipe(var3, var2, var4);
            }

            RecyclerView.ViewHolder var6 = ItemTouchHelper.this.mSelected;
            if (var6 != null) {
               byte var5 = 0;
               if (var3 != 6) {
                  switch(var3) {
                  case 2:
                     if (var4 >= 0) {
                        ItemTouchHelper.this.updateDxDy(var2, ItemTouchHelper.this.mSelectedFlags, var4);
                        ItemTouchHelper.this.moveIfNecessary(var6);
                        ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                        ItemTouchHelper.this.mScrollRunnable.run();
                        ItemTouchHelper.this.mRecyclerView.invalidate();
                     }
                     break;
                  case 3:
                     if (ItemTouchHelper.this.mVelocityTracker != null) {
                        ItemTouchHelper.this.mVelocityTracker.clear();
                     }
                  case 1:
                     ItemTouchHelper.this.select((RecyclerView.ViewHolder)null, 0);
                     ItemTouchHelper.this.mActivePointerId = -1;
                  }
               } else {
                  var4 = var2.getActionIndex();
                  if (var2.getPointerId(var4) == ItemTouchHelper.this.mActivePointerId) {
                     if (var4 == 0) {
                        var5 = 1;
                     }

                     ItemTouchHelper.this.mActivePointerId = var2.getPointerId(var5);
                     ItemTouchHelper.this.updateDxDy(var2, ItemTouchHelper.this.mSelectedFlags, var4);
                  }
               }

            }
         }
      }
   };
   View mOverdrawChild = null;
   int mOverdrawChildPosition = -1;
   final List mPendingCleanup = new ArrayList();
   List mRecoverAnimations = new ArrayList();
   RecyclerView mRecyclerView;
   final Runnable mScrollRunnable = new Runnable() {
      public void run() {
         if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
            if (ItemTouchHelper.this.mSelected != null) {
               ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected);
            }

            ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
            ViewCompat.postOnAnimation(ItemTouchHelper.this.mRecyclerView, this);
         }

      }
   };
   RecyclerView.ViewHolder mSelected = null;
   int mSelectedFlags;
   private float mSelectedStartX;
   private float mSelectedStartY;
   private int mSlop;
   private List mSwapTargets;
   private float mSwipeEscapeVelocity;
   private final float[] mTmpPosition = new float[2];
   private Rect mTmpRect;
   VelocityTracker mVelocityTracker;

   public ItemTouchHelper(ItemTouchHelper.Callback var1) {
      this.mCallback = var1;
   }

   private void addChildDrawingOrderCallback() {
      if (VERSION.SDK_INT < 21) {
         if (this.mChildDrawingOrderCallback == null) {
            this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
               public int onGetChildDrawingOrder(int var1, int var2) {
                  if (ItemTouchHelper.this.mOverdrawChild == null) {
                     return var2;
                  } else {
                     int var3 = ItemTouchHelper.this.mOverdrawChildPosition;
                     int var4 = var3;
                     if (var3 == -1) {
                        var4 = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                        ItemTouchHelper.this.mOverdrawChildPosition = var4;
                     }

                     if (var2 == var1 - 1) {
                        return var4;
                     } else {
                        if (var2 >= var4) {
                           ++var2;
                        }

                        return var2;
                     }
                  }
               }
            };
         }

         this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
      }
   }

   private int checkHorizontalSwipe(RecyclerView.ViewHolder var1, int var2) {
      if ((var2 & 12) != 0) {
         float var3 = this.mDx;
         byte var4 = 4;
         byte var5;
         if (var3 > 0.0F) {
            var5 = 8;
         } else {
            var5 = 4;
         }

         float var6;
         if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            var6 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            var3 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (var6 > 0.0F) {
               var4 = 8;
            }

            var6 = Math.abs(var6);
            if ((var4 & var2) != 0 && var5 == var4 && var6 >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && var6 > Math.abs(var3)) {
               return var4;
            }
         }

         var3 = (float)this.mRecyclerView.getWidth();
         var6 = this.mCallback.getSwipeThreshold(var1);
         if ((var2 & var5) != 0 && Math.abs(this.mDx) > var3 * var6) {
            return var5;
         }
      }

      return 0;
   }

   private int checkVerticalSwipe(RecyclerView.ViewHolder var1, int var2) {
      if ((var2 & 3) != 0) {
         float var3 = this.mDy;
         byte var4 = 1;
         byte var5;
         if (var3 > 0.0F) {
            var5 = 2;
         } else {
            var5 = 1;
         }

         float var6;
         if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            var3 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            var6 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (var6 > 0.0F) {
               var4 = 2;
            }

            var6 = Math.abs(var6);
            if ((var4 & var2) != 0 && var4 == var5 && var6 >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && var6 > Math.abs(var3)) {
               return var4;
            }
         }

         var3 = (float)this.mRecyclerView.getHeight();
         var6 = this.mCallback.getSwipeThreshold(var1);
         if ((var2 & var5) != 0 && Math.abs(this.mDy) > var3 * var6) {
            return var5;
         }
      }

      return 0;
   }

   private void destroyCallbacks() {
      this.mRecyclerView.removeItemDecoration(this);
      this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
      this.mRecyclerView.removeOnChildAttachStateChangeListener(this);

      for(int var1 = this.mRecoverAnimations.size() - 1; var1 >= 0; --var1) {
         ItemTouchHelper.RecoverAnimation var2 = (ItemTouchHelper.RecoverAnimation)this.mRecoverAnimations.get(0);
         this.mCallback.clearView(this.mRecyclerView, var2.mViewHolder);
      }

      this.mRecoverAnimations.clear();
      this.mOverdrawChild = null;
      this.mOverdrawChildPosition = -1;
      this.releaseVelocityTracker();
      this.stopGestureDetection();
   }

   private List findSwapTargets(RecyclerView.ViewHolder var1) {
      if (this.mSwapTargets == null) {
         this.mSwapTargets = new ArrayList();
         this.mDistances = new ArrayList();
      } else {
         this.mSwapTargets.clear();
         this.mDistances.clear();
      }

      int var3 = this.mCallback.getBoundingBoxMargin();
      int var4 = Math.round(this.mSelectedStartX + this.mDx) - var3;
      int var5 = Math.round(this.mSelectedStartY + this.mDy) - var3;
      int var6 = var1.itemView.getWidth();
      var3 *= 2;
      int var7 = var6 + var4 + var3;
      int var8 = var1.itemView.getHeight() + var5 + var3;
      int var9 = (var4 + var7) / 2;
      int var10 = (var5 + var8) / 2;
      RecyclerView.LayoutManager var11 = this.mRecyclerView.getLayoutManager();
      int var12 = var11.getChildCount();

      for(var6 = 0; var6 < var12; ++var6) {
         View var13 = var11.getChildAt(var6);
         if (var13 != var1.itemView && var13.getBottom() >= var5 && var13.getTop() <= var8 && var13.getRight() >= var4 && var13.getLeft() <= var7) {
            RecyclerView.ViewHolder var2 = this.mRecyclerView.getChildViewHolder(var13);
            if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, var2)) {
               int var14 = Math.abs(var9 - (var13.getLeft() + var13.getRight()) / 2);
               var3 = Math.abs(var10 - (var13.getTop() + var13.getBottom()) / 2);
               int var15 = var14 * var14 + var3 * var3;
               int var16 = this.mSwapTargets.size();
               var3 = 0;

               for(var14 = 0; var3 < var16 && var15 > (Integer)this.mDistances.get(var3); ++var3) {
                  ++var14;
               }

               this.mSwapTargets.add(var14, var2);
               this.mDistances.add(var14, var15);
            }
         }
      }

      return this.mSwapTargets;
   }

   private RecyclerView.ViewHolder findSwipedView(MotionEvent var1) {
      RecyclerView.LayoutManager var2 = this.mRecyclerView.getLayoutManager();
      if (this.mActivePointerId == -1) {
         return null;
      } else {
         int var3 = var1.findPointerIndex(this.mActivePointerId);
         float var4 = var1.getX(var3);
         float var5 = this.mInitialTouchX;
         float var6 = var1.getY(var3);
         float var7 = this.mInitialTouchY;
         var5 = Math.abs(var4 - var5);
         var7 = Math.abs(var6 - var7);
         if (var5 < (float)this.mSlop && var7 < (float)this.mSlop) {
            return null;
         } else if (var5 > var7 && var2.canScrollHorizontally()) {
            return null;
         } else if (var7 > var5 && var2.canScrollVertically()) {
            return null;
         } else {
            View var8 = this.findChildView(var1);
            return var8 == null ? null : this.mRecyclerView.getChildViewHolder(var8);
         }
      }
   }

   private void getSelectedDxDy(float[] var1) {
      if ((this.mSelectedFlags & 12) != 0) {
         var1[0] = this.mSelectedStartX + this.mDx - (float)this.mSelected.itemView.getLeft();
      } else {
         var1[0] = this.mSelected.itemView.getTranslationX();
      }

      if ((this.mSelectedFlags & 3) != 0) {
         var1[1] = this.mSelectedStartY + this.mDy - (float)this.mSelected.itemView.getTop();
      } else {
         var1[1] = this.mSelected.itemView.getTranslationY();
      }

   }

   private static boolean hitTest(View var0, float var1, float var2, float var3, float var4) {
      boolean var5;
      if (var1 >= var3 && var1 <= var3 + (float)var0.getWidth() && var2 >= var4 && var2 <= var4 + (float)var0.getHeight()) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private void releaseVelocityTracker() {
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }

   private void setupCallbacks() {
      this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
      this.mRecyclerView.addItemDecoration(this);
      this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
      this.mRecyclerView.addOnChildAttachStateChangeListener(this);
      this.startGestureDetection();
   }

   private void startGestureDetection() {
      this.mItemTouchHelperGestureListener = new ItemTouchHelper.ItemTouchHelperGestureListener();
      this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), this.mItemTouchHelperGestureListener);
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

   private int swipeIfNecessary(RecyclerView.ViewHolder var1) {
      if (this.mActionState == 2) {
         return 0;
      } else {
         int var2 = this.mCallback.getMovementFlags(this.mRecyclerView, var1);
         int var3 = (this.mCallback.convertToAbsoluteDirection(var2, ViewCompat.getLayoutDirection(this.mRecyclerView)) & '\uff00') >> 8;
         if (var3 == 0) {
            return 0;
         } else {
            var2 = (var2 & '\uff00') >> 8;
            int var4;
            if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
               var4 = this.checkHorizontalSwipe(var1, var3);
               if (var4 > 0) {
                  if ((var2 & var4) == 0) {
                     return ItemTouchHelper.Callback.convertToRelativeDirection(var4, ViewCompat.getLayoutDirection(this.mRecyclerView));
                  }

                  return var4;
               }

               var3 = this.checkVerticalSwipe(var1, var3);
               if (var3 > 0) {
                  return var3;
               }
            } else {
               var4 = this.checkVerticalSwipe(var1, var3);
               if (var4 > 0) {
                  return var4;
               }

               var3 = this.checkHorizontalSwipe(var1, var3);
               if (var3 > 0) {
                  if ((var2 & var3) == 0) {
                     return ItemTouchHelper.Callback.convertToRelativeDirection(var3, ViewCompat.getLayoutDirection(this.mRecyclerView));
                  }

                  return var3;
               }
            }

            return 0;
         }
      }
   }

   public void attachToRecyclerView(RecyclerView var1) {
      if (this.mRecyclerView != var1) {
         if (this.mRecyclerView != null) {
            this.destroyCallbacks();
         }

         this.mRecyclerView = var1;
         if (var1 != null) {
            Resources var2 = var1.getResources();
            this.mSwipeEscapeVelocity = var2.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
            this.mMaxSwipeVelocity = var2.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
            this.setupCallbacks();
         }

      }
   }

   void checkSelectForSwipe(int var1, MotionEvent var2, int var3) {
      if (this.mSelected == null && var1 == 2 && this.mActionState != 2 && this.mCallback.isItemViewSwipeEnabled()) {
         if (this.mRecyclerView.getScrollState() != 1) {
            RecyclerView.ViewHolder var4 = this.findSwipedView(var2);
            if (var4 != null) {
               var1 = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, var4) & '\uff00') >> 8;
               if (var1 != 0) {
                  float var5 = var2.getX(var3);
                  float var6 = var2.getY(var3);
                  var5 -= this.mInitialTouchX;
                  float var7 = var6 - this.mInitialTouchY;
                  var6 = Math.abs(var5);
                  float var8 = Math.abs(var7);
                  if (var6 >= (float)this.mSlop || var8 >= (float)this.mSlop) {
                     if (var6 > var8) {
                        if (var5 < 0.0F && (var1 & 4) == 0) {
                           return;
                        }

                        if (var5 > 0.0F && (var1 & 8) == 0) {
                           return;
                        }
                     } else {
                        if (var7 < 0.0F && (var1 & 1) == 0) {
                           return;
                        }

                        if (var7 > 0.0F && (var1 & 2) == 0) {
                           return;
                        }
                     }

                     this.mDy = 0.0F;
                     this.mDx = 0.0F;
                     this.mActivePointerId = var2.getPointerId(0);
                     this.select(var4, 1);
                  }
               }
            }
         }
      }
   }

   void endRecoverAnimation(RecyclerView.ViewHolder var1, boolean var2) {
      for(int var3 = this.mRecoverAnimations.size() - 1; var3 >= 0; --var3) {
         ItemTouchHelper.RecoverAnimation var4 = (ItemTouchHelper.RecoverAnimation)this.mRecoverAnimations.get(var3);
         if (var4.mViewHolder == var1) {
            var4.mOverridden |= var2;
            if (!var4.mEnded) {
               var4.cancel();
            }

            this.mRecoverAnimations.remove(var3);
            return;
         }
      }

   }

   ItemTouchHelper.RecoverAnimation findAnimation(MotionEvent var1) {
      if (this.mRecoverAnimations.isEmpty()) {
         return null;
      } else {
         View var4 = this.findChildView(var1);

         for(int var2 = this.mRecoverAnimations.size() - 1; var2 >= 0; --var2) {
            ItemTouchHelper.RecoverAnimation var3 = (ItemTouchHelper.RecoverAnimation)this.mRecoverAnimations.get(var2);
            if (var3.mViewHolder.itemView == var4) {
               return var3;
            }
         }

         return null;
      }
   }

   View findChildView(MotionEvent var1) {
      float var2 = var1.getX();
      float var3 = var1.getY();
      if (this.mSelected != null) {
         View var6 = this.mSelected.itemView;
         if (hitTest(var6, var2, var3, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
            return var6;
         }
      }

      for(int var4 = this.mRecoverAnimations.size() - 1; var4 >= 0; --var4) {
         ItemTouchHelper.RecoverAnimation var7 = (ItemTouchHelper.RecoverAnimation)this.mRecoverAnimations.get(var4);
         View var5 = var7.mViewHolder.itemView;
         if (hitTest(var5, var2, var3, var7.mX, var7.mY)) {
            return var5;
         }
      }

      return this.mRecyclerView.findChildViewUnder(var2, var3);
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
      var1.setEmpty();
   }

   boolean hasRunningRecoverAnim() {
      int var1 = this.mRecoverAnimations.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (!((ItemTouchHelper.RecoverAnimation)this.mRecoverAnimations.get(var2)).mEnded) {
            return true;
         }
      }

      return false;
   }

   void moveIfNecessary(RecyclerView.ViewHolder var1) {
      if (!this.mRecyclerView.isLayoutRequested()) {
         if (this.mActionState == 2) {
            float var2 = this.mCallback.getMoveThreshold(var1);
            int var3 = (int)(this.mSelectedStartX + this.mDx);
            int var4 = (int)(this.mSelectedStartY + this.mDy);
            if ((float)Math.abs(var4 - var1.itemView.getTop()) >= (float)var1.itemView.getHeight() * var2 || (float)Math.abs(var3 - var1.itemView.getLeft()) >= (float)var1.itemView.getWidth() * var2) {
               List var5 = this.findSwapTargets(var1);
               if (var5.size() != 0) {
                  RecyclerView.ViewHolder var8 = this.mCallback.chooseDropTarget(var1, var5, var3, var4);
                  if (var8 == null) {
                     this.mSwapTargets.clear();
                     this.mDistances.clear();
                  } else {
                     int var6 = var8.getAdapterPosition();
                     int var7 = var1.getAdapterPosition();
                     if (this.mCallback.onMove(this.mRecyclerView, var1, var8)) {
                        this.mCallback.onMoved(this.mRecyclerView, var1, var7, var8, var6, var3, var4);
                     }

                  }
               }
            }
         }
      }
   }

   void obtainVelocityTracker() {
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
      }

      this.mVelocityTracker = VelocityTracker.obtain();
   }

   public void onChildViewAttachedToWindow(View var1) {
   }

   public void onChildViewDetachedFromWindow(View var1) {
      this.removeChildDrawingOrderCallbackIfNecessary(var1);
      RecyclerView.ViewHolder var2 = this.mRecyclerView.getChildViewHolder(var1);
      if (var2 != null) {
         if (this.mSelected != null && var2 == this.mSelected) {
            this.select((RecyclerView.ViewHolder)null, 0);
         } else {
            this.endRecoverAnimation(var2, false);
            if (this.mPendingCleanup.remove(var2.itemView)) {
               this.mCallback.clearView(this.mRecyclerView, var2);
            }
         }

      }
   }

   public void onDraw(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
      this.mOverdrawChildPosition = -1;
      float var4;
      float var5;
      if (this.mSelected != null) {
         this.getSelectedDxDy(this.mTmpPosition);
         var4 = this.mTmpPosition[0];
         var5 = this.mTmpPosition[1];
      } else {
         var4 = 0.0F;
         var5 = 0.0F;
      }

      this.mCallback.onDraw(var1, var2, this.mSelected, this.mRecoverAnimations, this.mActionState, var4, var5);
   }

   public void onDrawOver(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
      float var4;
      float var5;
      if (this.mSelected != null) {
         this.getSelectedDxDy(this.mTmpPosition);
         var4 = this.mTmpPosition[0];
         var5 = this.mTmpPosition[1];
      } else {
         var4 = 0.0F;
         var5 = 0.0F;
      }

      this.mCallback.onDrawOver(var1, var2, this.mSelected, this.mRecoverAnimations, this.mActionState, var4, var5);
   }

   void postDispatchSwipe(final ItemTouchHelper.RecoverAnimation var1, final int var2) {
      this.mRecyclerView.post(new Runnable() {
         public void run() {
            if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !var1.mOverridden && var1.mViewHolder.getAdapterPosition() != -1) {
               RecyclerView.ItemAnimator var1x = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
               if ((var1x == null || !var1x.isRunning((RecyclerView.ItemAnimator.ItemAnimatorFinishedListener)null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                  ItemTouchHelper.this.mCallback.onSwiped(var1.mViewHolder, var2);
               } else {
                  ItemTouchHelper.this.mRecyclerView.post(this);
               }
            }

         }
      });
   }

   void removeChildDrawingOrderCallbackIfNecessary(View var1) {
      if (var1 == this.mOverdrawChild) {
         this.mOverdrawChild = null;
         if (this.mChildDrawingOrderCallback != null) {
            this.mRecyclerView.setChildDrawingOrderCallback((RecyclerView.ChildDrawingOrderCallback)null);
         }
      }

   }

   boolean scrollIfNecessary() {
      if (this.mSelected == null) {
         this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
         return false;
      } else {
         long var1 = System.currentTimeMillis();
         long var3;
         if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            var3 = 0L;
         } else {
            var3 = var1 - this.mDragScrollStartTimeInMs;
         }

         RecyclerView.LayoutManager var5 = this.mRecyclerView.getLayoutManager();
         if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
         }

         int var6;
         int var7;
         label66: {
            var5.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
            if (var5.canScrollHorizontally()) {
               var6 = (int)(this.mSelectedStartX + this.mDx);
               var7 = var6 - this.mTmpRect.left - this.mRecyclerView.getPaddingLeft();
               if (this.mDx < 0.0F && var7 < 0) {
                  break label66;
               }

               if (this.mDx > 0.0F) {
                  var7 = var6 + this.mSelected.itemView.getWidth() + this.mTmpRect.right - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight());
                  if (var7 > 0) {
                     break label66;
                  }
               }
            }

            var7 = 0;
         }

         int var8;
         label57: {
            if (var5.canScrollVertically()) {
               var8 = (int)(this.mSelectedStartY + this.mDy);
               var6 = var8 - this.mTmpRect.top - this.mRecyclerView.getPaddingTop();
               if (this.mDy < 0.0F && var6 < 0) {
                  break label57;
               }

               if (this.mDy > 0.0F) {
                  var6 = var8 + this.mSelected.itemView.getHeight() + this.mTmpRect.bottom - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom());
                  if (var6 > 0) {
                     break label57;
                  }
               }
            }

            var6 = 0;
         }

         var8 = var7;
         if (var7 != 0) {
            var8 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), var7, this.mRecyclerView.getWidth(), var3);
         }

         if (var6 != 0) {
            var6 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), var6, this.mRecyclerView.getHeight(), var3);
         }

         if (var8 == 0 && var6 == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
         } else {
            if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
               this.mDragScrollStartTimeInMs = var1;
            }

            this.mRecyclerView.scrollBy(var8, var6);
            return true;
         }
      }
   }

   void select(RecyclerView.ViewHolder var1, int var2) {
      if (var1 != this.mSelected || var2 != this.mActionState) {
         this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
         int var3 = this.mActionState;
         this.endRecoverAnimation(var1, true);
         this.mActionState = var2;
         if (var2 == 2) {
            if (var1 == null) {
               throw new IllegalArgumentException("Must pass a ViewHolder when dragging");
            }

            this.mOverdrawChild = var1.itemView;
            this.addChildDrawingOrderCallback();
         }

         boolean var8;
         if (this.mSelected != null) {
            final RecyclerView.ViewHolder var4 = this.mSelected;
            if (var4.itemView.getParent() != null) {
               final int var5;
               if (var3 == 2) {
                  var5 = 0;
               } else {
                  var5 = this.swipeIfNecessary(var4);
               }

               float var6;
               float var7;
               label85: {
                  this.releaseVelocityTracker();
                  if (var5 != 4 && var5 != 8 && var5 != 16 && var5 != 32) {
                     switch(var5) {
                     case 1:
                     case 2:
                        var7 = Math.signum(this.mDy) * (float)this.mRecyclerView.getHeight();
                        var6 = 0.0F;
                        break label85;
                     default:
                        var6 = 0.0F;
                     }
                  } else {
                     var6 = Math.signum(this.mDx) * (float)this.mRecyclerView.getWidth();
                  }

                  var7 = 0.0F;
               }

               byte var14;
               if (var3 == 2) {
                  var14 = 8;
               } else if (var5 > 0) {
                  var14 = 2;
               } else {
                  var14 = 4;
               }

               this.getSelectedDxDy(this.mTmpPosition);
               float var9 = this.mTmpPosition[0];
               float var10 = this.mTmpPosition[1];
               ItemTouchHelper.RecoverAnimation var13 = new ItemTouchHelper.RecoverAnimation(var4, var14, var3, var9, var10, var6, var7) {
                  public void onAnimationEnd(Animator var1) {
                     super.onAnimationEnd(var1);
                     if (!this.mOverridden) {
                        if (var5 <= 0) {
                           ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, var4);
                        } else {
                           ItemTouchHelper.this.mPendingCleanup.add(var4.itemView);
                           this.mIsPendingCleanup = true;
                           if (var5 > 0) {
                              ItemTouchHelper.this.postDispatchSwipe(this, var5);
                           }
                        }

                        if (ItemTouchHelper.this.mOverdrawChild == var4.itemView) {
                           ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(var4.itemView);
                        }

                     }
                  }
               };
               var13.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, var14, var6 - var9, var7 - var10));
               this.mRecoverAnimations.add(var13);
               var13.start();
               var8 = true;
            } else {
               this.removeChildDrawingOrderCallbackIfNecessary(var4.itemView);
               this.mCallback.clearView(this.mRecyclerView, var4);
               var8 = false;
            }

            this.mSelected = null;
         } else {
            var8 = false;
         }

         if (var1 != null) {
            this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, var1) & (1 << var2 * 8 + 8) - 1) >> this.mActionState * 8;
            this.mSelectedStartX = (float)var1.itemView.getLeft();
            this.mSelectedStartY = (float)var1.itemView.getTop();
            this.mSelected = var1;
            if (var2 == 2) {
               this.mSelected.itemView.performHapticFeedback(0);
            }
         }

         boolean var11 = false;
         ViewParent var12 = this.mRecyclerView.getParent();
         if (var12 != null) {
            if (this.mSelected != null) {
               var11 = true;
            }

            var12.requestDisallowInterceptTouchEvent(var11);
         }

         if (!var8) {
            this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
         }

         this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
         this.mRecyclerView.invalidate();
      }
   }

   void updateDxDy(MotionEvent var1, int var2, int var3) {
      float var4 = var1.getX(var3);
      float var5 = var1.getY(var3);
      this.mDx = var4 - this.mInitialTouchX;
      this.mDy = var5 - this.mInitialTouchY;
      if ((var2 & 4) == 0) {
         this.mDx = Math.max(0.0F, this.mDx);
      }

      if ((var2 & 8) == 0) {
         this.mDx = Math.min(0.0F, this.mDx);
      }

      if ((var2 & 1) == 0) {
         this.mDy = Math.max(0.0F, this.mDy);
      }

      if ((var2 & 2) == 0) {
         this.mDy = Math.min(0.0F, this.mDy);
      }

   }

   public abstract static class Callback {
      private static final Interpolator sDragScrollInterpolator = new Interpolator() {
         public float getInterpolation(float var1) {
            return var1 * var1 * var1 * var1 * var1;
         }
      };
      private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
         public float getInterpolation(float var1) {
            --var1;
            return var1 * var1 * var1 * var1 * var1 + 1.0F;
         }
      };
      private int mCachedMaxScrollSpeed = -1;

      public static int convertToRelativeDirection(int var0, int var1) {
         int var2 = var0 & 789516;
         if (var2 == 0) {
            return var0;
         } else {
            var0 &= var2;
            if (var1 == 0) {
               return var0 | var2 << 2;
            } else {
               var1 = var2 << 1;
               return var0 | -789517 & var1 | (var1 & 789516) << 2;
            }
         }
      }

      private int getMaxDragScroll(RecyclerView var1) {
         if (this.mCachedMaxScrollSpeed == -1) {
            this.mCachedMaxScrollSpeed = var1.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
         }

         return this.mCachedMaxScrollSpeed;
      }

      public static int makeFlag(int var0, int var1) {
         return var1 << var0 * 8;
      }

      public static int makeMovementFlags(int var0, int var1) {
         int var2 = makeFlag(0, var1 | var0);
         var1 = makeFlag(1, var1);
         return makeFlag(2, var0) | var1 | var2;
      }

      public boolean canDropOver(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3) {
         return true;
      }

      public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder var1, List var2, int var3, int var4) {
         int var5 = var1.itemView.getWidth();
         int var6 = var1.itemView.getHeight();
         int var7 = var3 - var1.itemView.getLeft();
         int var8 = var4 - var1.itemView.getTop();
         int var9 = var2.size();
         RecyclerView.ViewHolder var10 = null;
         int var11 = -1;

         for(int var12 = 0; var12 < var9; ++var12) {
            RecyclerView.ViewHolder var13;
            int var14;
            label43: {
               var13 = (RecyclerView.ViewHolder)var2.get(var12);
               if (var7 > 0) {
                  var14 = var13.itemView.getRight() - (var3 + var5);
                  if (var14 < 0 && var13.itemView.getRight() > var1.itemView.getRight()) {
                     var14 = Math.abs(var14);
                     if (var14 > var11) {
                        var10 = var13;
                        break label43;
                     }
                  }
               }

               var14 = var11;
            }

            RecyclerView.ViewHolder var15 = var10;
            var11 = var14;
            int var16;
            if (var7 < 0) {
               var16 = var13.itemView.getLeft() - var3;
               var15 = var10;
               var11 = var14;
               if (var16 > 0) {
                  var15 = var10;
                  var11 = var14;
                  if (var13.itemView.getLeft() < var1.itemView.getLeft()) {
                     var16 = Math.abs(var16);
                     var15 = var10;
                     var11 = var14;
                     if (var16 > var14) {
                        var11 = var16;
                        var15 = var13;
                     }
                  }
               }
            }

            var10 = var15;
            var14 = var11;
            if (var8 < 0) {
               var16 = var13.itemView.getTop() - var4;
               var10 = var15;
               var14 = var11;
               if (var16 > 0) {
                  var10 = var15;
                  var14 = var11;
                  if (var13.itemView.getTop() < var1.itemView.getTop()) {
                     var16 = Math.abs(var16);
                     var10 = var15;
                     var14 = var11;
                     if (var16 > var11) {
                        var14 = var16;
                        var10 = var13;
                     }
                  }
               }
            }

            if (var8 > 0) {
               var11 = var13.itemView.getBottom() - (var4 + var6);
               if (var11 < 0 && var13.itemView.getBottom() > var1.itemView.getBottom()) {
                  var11 = Math.abs(var11);
                  if (var11 > var14) {
                     var10 = var13;
                     continue;
                  }
               }
            }

            var11 = var14;
         }

         return var10;
      }

      public void clearView(RecyclerView var1, RecyclerView.ViewHolder var2) {
         ItemTouchUIUtilImpl.INSTANCE.clearView(var2.itemView);
      }

      public int convertToAbsoluteDirection(int var1, int var2) {
         int var3 = var1 & 3158064;
         if (var3 == 0) {
            return var1;
         } else {
            var1 &= var3;
            if (var2 == 0) {
               return var1 | var3 >> 2;
            } else {
               var2 = var3 >> 1;
               return var1 | -3158065 & var2 | (var2 & 3158064) >> 2;
            }
         }
      }

      final int getAbsoluteMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2) {
         return this.convertToAbsoluteDirection(this.getMovementFlags(var1, var2), ViewCompat.getLayoutDirection(var1));
      }

      public long getAnimationDuration(RecyclerView var1, int var2, float var3, float var4) {
         RecyclerView.ItemAnimator var7 = var1.getItemAnimator();
         long var5;
         if (var7 == null) {
            if (var2 == 8) {
               var5 = 200L;
            } else {
               var5 = 250L;
            }

            return var5;
         } else {
            if (var2 == 8) {
               var5 = var7.getMoveDuration();
            } else {
               var5 = var7.getRemoveDuration();
            }

            return var5;
         }
      }

      public int getBoundingBoxMargin() {
         return 0;
      }

      public float getMoveThreshold(RecyclerView.ViewHolder var1) {
         return 0.5F;
      }

      public abstract int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2);

      public float getSwipeEscapeVelocity(float var1) {
         return var1;
      }

      public float getSwipeThreshold(RecyclerView.ViewHolder var1) {
         return 0.5F;
      }

      public float getSwipeVelocityThreshold(float var1) {
         return var1;
      }

      boolean hasDragFlag(RecyclerView var1, RecyclerView.ViewHolder var2) {
         boolean var3;
         if ((this.getAbsoluteMovementFlags(var1, var2) & 16711680) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public int interpolateOutOfBoundsScroll(RecyclerView var1, int var2, int var3, int var4, long var5) {
         int var7 = this.getMaxDragScroll(var1);
         var4 = Math.abs(var3);
         int var8 = (int)Math.signum((float)var3);
         float var9 = (float)var4;
         float var10 = 1.0F;
         var9 = Math.min(1.0F, var9 * 1.0F / (float)var2);
         var2 = (int)((float)(var8 * var7) * sDragViewScrollCapInterpolator.getInterpolation(var9));
         if (var5 <= 2000L) {
            var10 = (float)var5 / 2000.0F;
         }

         var2 = (int)((float)var2 * sDragScrollInterpolator.getInterpolation(var10));
         if (var2 == 0) {
            byte var11;
            if (var3 > 0) {
               var11 = 1;
            } else {
               var11 = -1;
            }

            return var11;
         } else {
            return var2;
         }
      }

      public boolean isItemViewSwipeEnabled() {
         return true;
      }

      public boolean isLongPressDragEnabled() {
         return true;
      }

      public void onChildDraw(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, float var4, float var5, int var6, boolean var7) {
         ItemTouchUIUtilImpl.INSTANCE.onDraw(var1, var2, var3.itemView, var4, var5, var6, var7);
      }

      public void onChildDrawOver(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, float var4, float var5, int var6, boolean var7) {
         ItemTouchUIUtilImpl.INSTANCE.onDrawOver(var1, var2, var3.itemView, var4, var5, var6, var7);
      }

      void onDraw(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, List var4, int var5, float var6, float var7) {
         int var8 = var4.size();

         int var9;
         for(var9 = 0; var9 < var8; ++var9) {
            ItemTouchHelper.RecoverAnimation var10 = (ItemTouchHelper.RecoverAnimation)var4.get(var9);
            var10.update();
            int var11 = var1.save();
            this.onChildDraw(var1, var2, var10.mViewHolder, var10.mX, var10.mY, var10.mActionState, false);
            var1.restoreToCount(var11);
         }

         if (var3 != null) {
            var9 = var1.save();
            this.onChildDraw(var1, var2, var3, var6, var7, var5, true);
            var1.restoreToCount(var9);
         }

      }

      void onDrawOver(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, List var4, int var5, float var6, float var7) {
         int var8 = var4.size();
         boolean var9 = false;

         int var10;
         for(var10 = 0; var10 < var8; ++var10) {
            ItemTouchHelper.RecoverAnimation var11 = (ItemTouchHelper.RecoverAnimation)var4.get(var10);
            int var12 = var1.save();
            this.onChildDrawOver(var1, var2, var11.mViewHolder, var11.mX, var11.mY, var11.mActionState, false);
            var1.restoreToCount(var12);
         }

         if (var3 != null) {
            var10 = var1.save();
            this.onChildDrawOver(var1, var2, var3, var6, var7, var5, true);
            var1.restoreToCount(var10);
         }

         var5 = var8 - 1;

         boolean var14;
         for(var14 = var9; var5 >= 0; --var5) {
            ItemTouchHelper.RecoverAnimation var13 = (ItemTouchHelper.RecoverAnimation)var4.get(var5);
            if (var13.mEnded && !var13.mIsPendingCleanup) {
               var4.remove(var5);
            } else if (!var13.mEnded) {
               var14 = true;
            }
         }

         if (var14) {
            var2.invalidate();
         }

      }

      public abstract boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3);

      public void onMoved(RecyclerView var1, RecyclerView.ViewHolder var2, int var3, RecyclerView.ViewHolder var4, int var5, int var6, int var7) {
         RecyclerView.LayoutManager var8 = var1.getLayoutManager();
         if (var8 instanceof ItemTouchHelper.ViewDropHandler) {
            ((ItemTouchHelper.ViewDropHandler)var8).prepareForDrop(var2.itemView, var4.itemView, var6, var7);
         } else {
            if (var8.canScrollHorizontally()) {
               if (var8.getDecoratedLeft(var4.itemView) <= var1.getPaddingLeft()) {
                  var1.scrollToPosition(var5);
               }

               if (var8.getDecoratedRight(var4.itemView) >= var1.getWidth() - var1.getPaddingRight()) {
                  var1.scrollToPosition(var5);
               }
            }

            if (var8.canScrollVertically()) {
               if (var8.getDecoratedTop(var4.itemView) <= var1.getPaddingTop()) {
                  var1.scrollToPosition(var5);
               }

               if (var8.getDecoratedBottom(var4.itemView) >= var1.getHeight() - var1.getPaddingBottom()) {
                  var1.scrollToPosition(var5);
               }
            }

         }
      }

      public void onSelectedChanged(RecyclerView.ViewHolder var1, int var2) {
         if (var1 != null) {
            ItemTouchUIUtilImpl.INSTANCE.onSelected(var1.itemView);
         }

      }

      public abstract void onSwiped(RecyclerView.ViewHolder var1, int var2);
   }

   private class ItemTouchHelperGestureListener extends SimpleOnGestureListener {
      private boolean mShouldReactToLongPress = true;

      ItemTouchHelperGestureListener() {
      }

      void doNotReactToLongPress() {
         this.mShouldReactToLongPress = false;
      }

      public boolean onDown(MotionEvent var1) {
         return true;
      }

      public void onLongPress(MotionEvent var1) {
         if (this.mShouldReactToLongPress) {
            View var2 = ItemTouchHelper.this.findChildView(var1);
            if (var2 != null) {
               RecyclerView.ViewHolder var7 = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(var2);
               if (var7 != null) {
                  if (!ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, var7)) {
                     return;
                  }

                  if (var1.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
                     int var3 = var1.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                     float var4 = var1.getX(var3);
                     float var5 = var1.getY(var3);
                     ItemTouchHelper.this.mInitialTouchX = var4;
                     ItemTouchHelper.this.mInitialTouchY = var5;
                     ItemTouchHelper var6 = ItemTouchHelper.this;
                     ItemTouchHelper.this.mDy = 0.0F;
                     var6.mDx = 0.0F;
                     if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) {
                        ItemTouchHelper.this.select(var7, 2);
                     }
                  }
               }
            }

         }
      }
   }

   private static class RecoverAnimation implements AnimatorListener {
      final int mActionState;
      final int mAnimationType;
      boolean mEnded = false;
      private float mFraction;
      boolean mIsPendingCleanup;
      boolean mOverridden = false;
      final float mStartDx;
      final float mStartDy;
      final float mTargetX;
      final float mTargetY;
      private final ValueAnimator mValueAnimator;
      final RecyclerView.ViewHolder mViewHolder;
      float mX;
      float mY;

      RecoverAnimation(RecyclerView.ViewHolder var1, int var2, int var3, float var4, float var5, float var6, float var7) {
         this.mActionState = var3;
         this.mAnimationType = var2;
         this.mViewHolder = var1;
         this.mStartDx = var4;
         this.mStartDy = var5;
         this.mTargetX = var6;
         this.mTargetY = var7;
         this.mValueAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
         this.mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               RecoverAnimation.this.setFraction(var1.getAnimatedFraction());
            }
         });
         this.mValueAnimator.setTarget(var1.itemView);
         this.mValueAnimator.addListener(this);
         this.setFraction(0.0F);
      }

      public void cancel() {
         this.mValueAnimator.cancel();
      }

      public void onAnimationCancel(Animator var1) {
         this.setFraction(1.0F);
      }

      public void onAnimationEnd(Animator var1) {
         if (!this.mEnded) {
            this.mViewHolder.setIsRecyclable(true);
         }

         this.mEnded = true;
      }

      public void onAnimationRepeat(Animator var1) {
      }

      public void onAnimationStart(Animator var1) {
      }

      public void setDuration(long var1) {
         this.mValueAnimator.setDuration(var1);
      }

      public void setFraction(float var1) {
         this.mFraction = var1;
      }

      public void start() {
         this.mViewHolder.setIsRecyclable(false);
         this.mValueAnimator.start();
      }

      public void update() {
         if (this.mStartDx == this.mTargetX) {
            this.mX = this.mViewHolder.itemView.getTranslationX();
         } else {
            this.mX = this.mStartDx + this.mFraction * (this.mTargetX - this.mStartDx);
         }

         if (this.mStartDy == this.mTargetY) {
            this.mY = this.mViewHolder.itemView.getTranslationY();
         } else {
            this.mY = this.mStartDy + this.mFraction * (this.mTargetY - this.mStartDy);
         }

      }
   }

   public abstract static class SimpleCallback extends ItemTouchHelper.Callback {
      private int mDefaultDragDirs;
      private int mDefaultSwipeDirs;

      public SimpleCallback(int var1, int var2) {
         this.mDefaultSwipeDirs = var2;
         this.mDefaultDragDirs = var1;
      }

      public int getDragDirs(RecyclerView var1, RecyclerView.ViewHolder var2) {
         return this.mDefaultDragDirs;
      }

      public int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2) {
         return makeMovementFlags(this.getDragDirs(var1, var2), this.getSwipeDirs(var1, var2));
      }

      public int getSwipeDirs(RecyclerView var1, RecyclerView.ViewHolder var2) {
         return this.mDefaultSwipeDirs;
      }
   }

   public interface ViewDropHandler {
      void prepareForDrop(View var1, View var2, int var3, int var4);
   }
}
