package android.support.v7.widget;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;
import java.util.List;

class ChildHelper {
   final ChildHelper.Bucket mBucket;
   final ChildHelper.Callback mCallback;
   final List mHiddenViews;

   ChildHelper(ChildHelper.Callback var1) {
      this.mCallback = var1;
      this.mBucket = new ChildHelper.Bucket();
      this.mHiddenViews = new ArrayList();
   }

   private int getOffset(int var1) {
      if (var1 < 0) {
         return -1;
      } else {
         int var2 = this.mCallback.getChildCount();

         int var4;
         for(int var3 = var1; var3 < var2; var3 += var4) {
            var4 = var1 - (var3 - this.mBucket.countOnesBefore(var3));
            if (var4 == 0) {
               while(this.mBucket.get(var3)) {
                  ++var3;
               }

               return var3;
            }
         }

         return -1;
      }
   }

   private void hideViewInternal(View var1) {
      this.mHiddenViews.add(var1);
      this.mCallback.onEnteredHiddenState(var1);
   }

   private boolean unhideViewInternal(View var1) {
      if (this.mHiddenViews.remove(var1)) {
         this.mCallback.onLeftHiddenState(var1);
         return true;
      } else {
         return false;
      }
   }

   void addView(View var1, int var2, boolean var3) {
      if (var2 < 0) {
         var2 = this.mCallback.getChildCount();
      } else {
         var2 = this.getOffset(var2);
      }

      this.mBucket.insert(var2, var3);
      if (var3) {
         this.hideViewInternal(var1);
      }

      this.mCallback.addView(var1, var2);
   }

   void addView(View var1, boolean var2) {
      this.addView(var1, -1, var2);
   }

   void attachViewToParent(View var1, int var2, LayoutParams var3, boolean var4) {
      if (var2 < 0) {
         var2 = this.mCallback.getChildCount();
      } else {
         var2 = this.getOffset(var2);
      }

      this.mBucket.insert(var2, var4);
      if (var4) {
         this.hideViewInternal(var1);
      }

      this.mCallback.attachViewToParent(var1, var2, var3);
   }

   void detachViewFromParent(int var1) {
      var1 = this.getOffset(var1);
      this.mBucket.remove(var1);
      this.mCallback.detachViewFromParent(var1);
   }

   View findHiddenNonRemovedView(int var1) {
      int var2 = this.mHiddenViews.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var4 = (View)this.mHiddenViews.get(var3);
         RecyclerView.ViewHolder var5 = this.mCallback.getChildViewHolder(var4);
         if (var5.getLayoutPosition() == var1 && !var5.isInvalid() && !var5.isRemoved()) {
            return var4;
         }
      }

      return null;
   }

   View getChildAt(int var1) {
      var1 = this.getOffset(var1);
      return this.mCallback.getChildAt(var1);
   }

   int getChildCount() {
      return this.mCallback.getChildCount() - this.mHiddenViews.size();
   }

   View getUnfilteredChildAt(int var1) {
      return this.mCallback.getChildAt(var1);
   }

   int getUnfilteredChildCount() {
      return this.mCallback.getChildCount();
   }

   void hide(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 >= 0) {
         this.mBucket.set(var2);
         this.hideViewInternal(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("view is not a child, cannot hide ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   int indexOfChild(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 == -1) {
         return -1;
      } else {
         return this.mBucket.get(var2) ? -1 : var2 - this.mBucket.countOnesBefore(var2);
      }
   }

   boolean isHidden(View var1) {
      return this.mHiddenViews.contains(var1);
   }

   void removeAllViewsUnfiltered() {
      this.mBucket.reset();

      for(int var1 = this.mHiddenViews.size() - 1; var1 >= 0; --var1) {
         this.mCallback.onLeftHiddenState((View)this.mHiddenViews.get(var1));
         this.mHiddenViews.remove(var1);
      }

      this.mCallback.removeAllViews();
   }

   void removeView(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 >= 0) {
         if (this.mBucket.remove(var2)) {
            this.unhideViewInternal(var1);
         }

         this.mCallback.removeViewAt(var2);
      }
   }

   void removeViewAt(int var1) {
      var1 = this.getOffset(var1);
      View var2 = this.mCallback.getChildAt(var1);
      if (var2 != null) {
         if (this.mBucket.remove(var1)) {
            this.unhideViewInternal(var2);
         }

         this.mCallback.removeViewAt(var1);
      }
   }

   boolean removeViewIfHidden(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 == -1) {
         this.unhideViewInternal(var1);
         return true;
      } else if (this.mBucket.get(var2)) {
         this.mBucket.remove(var2);
         this.unhideViewInternal(var1);
         this.mCallback.removeViewAt(var2);
         return true;
      } else {
         return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mBucket.toString());
      var1.append(", hidden list:");
      var1.append(this.mHiddenViews.size());
      return var1.toString();
   }

   void unhide(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      StringBuilder var3;
      if (var2 >= 0) {
         if (this.mBucket.get(var2)) {
            this.mBucket.clear(var2);
            this.unhideViewInternal(var1);
         } else {
            var3 = new StringBuilder();
            var3.append("trying to unhide a view that was not hidden");
            var3.append(var1);
            throw new RuntimeException(var3.toString());
         }
      } else {
         var3 = new StringBuilder();
         var3.append("view is not a child, cannot hide ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   static class Bucket {
      long mData = 0L;
      ChildHelper.Bucket mNext;

      private void ensureNext() {
         if (this.mNext == null) {
            this.mNext = new ChildHelper.Bucket();
         }

      }

      void clear(int var1) {
         if (var1 >= 64) {
            if (this.mNext != null) {
               this.mNext.clear(var1 - 64);
            }
         } else {
            this.mData &= 1L << var1;
         }

      }

      int countOnesBefore(int var1) {
         if (this.mNext == null) {
            return var1 >= 64 ? Long.bitCount(this.mData) : Long.bitCount(this.mData & (1L << var1) - 1L);
         } else {
            return var1 < 64 ? Long.bitCount(this.mData & (1L << var1) - 1L) : this.mNext.countOnesBefore(var1 - 64) + Long.bitCount(this.mData);
         }
      }

      boolean get(int var1) {
         if (var1 >= 64) {
            this.ensureNext();
            return this.mNext.get(var1 - 64);
         } else {
            boolean var2;
            if ((this.mData & 1L << var1) != 0L) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      }

      void insert(int var1, boolean var2) {
         if (var1 >= 64) {
            this.ensureNext();
            this.mNext.insert(var1 - 64, var2);
         } else {
            boolean var3;
            if ((this.mData & Long.MIN_VALUE) != 0L) {
               var3 = true;
            } else {
               var3 = false;
            }

            long var4 = (1L << var1) - 1L;
            this.mData = this.mData & var4 | (var4 & this.mData) << 1;
            if (var2) {
               this.set(var1);
            } else {
               this.clear(var1);
            }

            if (var3 || this.mNext != null) {
               this.ensureNext();
               this.mNext.insert(0, var3);
            }
         }

      }

      boolean remove(int var1) {
         if (var1 >= 64) {
            this.ensureNext();
            return this.mNext.remove(var1 - 64);
         } else {
            long var2 = 1L << var1;
            boolean var4;
            if ((this.mData & var2) != 0L) {
               var4 = true;
            } else {
               var4 = false;
            }

            this.mData &= var2;
            --var2;
            this.mData = this.mData & var2 | Long.rotateRight(var2 & this.mData, 1);
            if (this.mNext != null) {
               if (this.mNext.get(0)) {
                  this.set(63);
               }

               this.mNext.remove(0);
            }

            return var4;
         }
      }

      void reset() {
         this.mData = 0L;
         if (this.mNext != null) {
            this.mNext.reset();
         }

      }

      void set(int var1) {
         if (var1 >= 64) {
            this.ensureNext();
            this.mNext.set(var1 - 64);
         } else {
            this.mData |= 1L << var1;
         }

      }

      public String toString() {
         String var1;
         if (this.mNext == null) {
            var1 = Long.toBinaryString(this.mData);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.mNext.toString());
            var2.append("xx");
            var2.append(Long.toBinaryString(this.mData));
            var1 = var2.toString();
         }

         return var1;
      }
   }

   interface Callback {
      void addView(View var1, int var2);

      void attachViewToParent(View var1, int var2, LayoutParams var3);

      void detachViewFromParent(int var1);

      View getChildAt(int var1);

      int getChildCount();

      RecyclerView.ViewHolder getChildViewHolder(View var1);

      int indexOfChild(View var1);

      void onEnteredHiddenState(View var1);

      void onLeftHiddenState(View var1);

      void removeAllViews();

      void removeViewAt(int var1);
   }
}
