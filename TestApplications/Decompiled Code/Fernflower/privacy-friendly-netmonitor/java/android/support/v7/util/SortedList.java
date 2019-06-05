package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList {
   private static final int CAPACITY_GROWTH = 10;
   private static final int DELETION = 2;
   private static final int INSERTION = 1;
   public static final int INVALID_POSITION = -1;
   private static final int LOOKUP = 4;
   private static final int MIN_CAPACITY = 10;
   private SortedList.BatchedCallback mBatchedCallback;
   private SortedList.Callback mCallback;
   Object[] mData;
   private int mMergedSize;
   private Object[] mOldData;
   private int mOldDataSize;
   private int mOldDataStart;
   private int mSize;
   private final Class mTClass;

   public SortedList(Class var1, SortedList.Callback var2) {
      this(var1, var2, 10);
   }

   public SortedList(Class var1, SortedList.Callback var2, int var3) {
      this.mTClass = var1;
      this.mData = (Object[])Array.newInstance(var1, var3);
      this.mCallback = var2;
      this.mSize = 0;
   }

   private int add(Object var1, boolean var2) {
      int var3 = this.findIndexOf(var1, this.mData, 0, this.mSize, 1);
      int var4;
      if (var3 == -1) {
         var4 = 0;
      } else {
         var4 = var3;
         if (var3 < this.mSize) {
            Object var5 = this.mData[var3];
            var4 = var3;
            if (this.mCallback.areItemsTheSame(var5, var1)) {
               if (this.mCallback.areContentsTheSame(var5, var1)) {
                  this.mData[var3] = var1;
                  return var3;
               }

               this.mData[var3] = var1;
               this.mCallback.onChanged(var3, 1);
               return var3;
            }
         }
      }

      this.addToData(var4, var1);
      if (var2) {
         this.mCallback.onInserted(var4, 1);
      }

      return var4;
   }

   private void addAllInternal(Object[] var1) {
      boolean var2;
      if (!(this.mCallback instanceof SortedList.BatchedCallback)) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         this.beginBatchedUpdates();
      }

      this.mOldData = this.mData;
      this.mOldDataStart = 0;
      this.mOldDataSize = this.mSize;
      Arrays.sort(var1, this.mCallback);
      int var3 = this.deduplicate(var1);
      if (this.mSize == 0) {
         this.mData = var1;
         this.mSize = var3;
         this.mMergedSize = var3;
         this.mCallback.onInserted(0, var3);
      } else {
         this.merge(var1, var3);
      }

      this.mOldData = null;
      if (var2) {
         this.endBatchedUpdates();
      }

   }

   private void addToData(int var1, Object var2) {
      if (var1 > this.mSize) {
         StringBuilder var4 = new StringBuilder();
         var4.append("cannot add item to ");
         var4.append(var1);
         var4.append(" because size is ");
         var4.append(this.mSize);
         throw new IndexOutOfBoundsException(var4.toString());
      } else {
         if (this.mSize == this.mData.length) {
            Object[] var3 = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
            System.arraycopy(this.mData, 0, var3, 0, var1);
            var3[var1] = var2;
            System.arraycopy(this.mData, var1, var3, var1 + 1, this.mSize - var1);
            this.mData = var3;
         } else {
            System.arraycopy(this.mData, var1, this.mData, var1 + 1, this.mSize - var1);
            this.mData[var1] = var2;
         }

         ++this.mSize;
      }
   }

   private int deduplicate(Object[] var1) {
      if (var1.length == 0) {
         throw new IllegalArgumentException("Input array must be non-empty");
      } else {
         int var2 = 0;
         int var3 = 1;

         int var4;
         for(var4 = 1; var3 < var1.length; ++var3) {
            Object var5 = var1[var3];
            int var6 = this.mCallback.compare(var1[var2], var5);
            if (var6 > 0) {
               throw new IllegalArgumentException("Input must be sorted in ascending order.");
            }

            if (var6 == 0) {
               var6 = this.findSameItem(var5, var1, var2, var4);
               if (var6 != -1) {
                  var1[var6] = var5;
               } else {
                  if (var4 != var3) {
                     var1[var4] = var5;
                  }

                  ++var4;
               }
            } else {
               if (var4 != var3) {
                  var1[var4] = var5;
               }

               var6 = var4 + 1;
               var2 = var4;
               var4 = var6;
            }
         }

         return var4;
      }
   }

   private int findIndexOf(Object var1, Object[] var2, int var3, int var4, int var5) {
      while(var3 < var4) {
         int var6 = (var3 + var4) / 2;
         Object var7 = var2[var6];
         int var8 = this.mCallback.compare(var7, var1);
         if (var8 < 0) {
            var3 = var6 + 1;
         } else {
            if (var8 == 0) {
               if (this.mCallback.areItemsTheSame(var7, var1)) {
                  return var6;
               }

               var4 = this.linearEqualitySearch(var1, var6, var3, var4);
               if (var5 == 1) {
                  var3 = var4;
                  if (var4 == -1) {
                     var3 = var6;
                  }

                  return var3;
               }

               return var4;
            }

            var4 = var6;
         }
      }

      if (var5 != 1) {
         var3 = -1;
      }

      return var3;
   }

   private int findSameItem(Object var1, Object[] var2, int var3, int var4) {
      while(var3 < var4) {
         if (this.mCallback.areItemsTheSame(var2[var3], var1)) {
            return var3;
         }

         ++var3;
      }

      return -1;
   }

   private int linearEqualitySearch(Object var1, int var2, int var3, int var4) {
      int var5 = var2 - 1;

      int var6;
      Object var7;
      while(true) {
         var6 = var2;
         if (var5 < var3) {
            break;
         }

         var7 = this.mData[var5];
         if (this.mCallback.compare(var7, var1) != 0) {
            var6 = var2;
            break;
         }

         if (this.mCallback.areItemsTheSame(var7, var1)) {
            return var5;
         }

         --var5;
      }

      while(true) {
         var2 = var6 + 1;
         if (var2 >= var4) {
            break;
         }

         var7 = this.mData[var2];
         if (this.mCallback.compare(var7, var1) != 0) {
            break;
         }

         var6 = var2;
         if (this.mCallback.areItemsTheSame(var7, var1)) {
            return var2;
         }
      }

      return -1;
   }

   private void merge(Object[] var1, int var2) {
      int var3 = this.mSize;
      this.mData = (Object[])Array.newInstance(this.mTClass, var3 + var2 + 10);
      var3 = 0;
      this.mMergedSize = 0;

      while(this.mOldDataStart < this.mOldDataSize || var3 < var2) {
         if (this.mOldDataStart == this.mOldDataSize) {
            var2 -= var3;
            System.arraycopy(var1, var3, this.mData, this.mMergedSize, var2);
            this.mMergedSize += var2;
            this.mSize += var2;
            this.mCallback.onInserted(this.mMergedSize - var2, var2);
            break;
         }

         if (var3 == var2) {
            var2 = this.mOldDataSize - this.mOldDataStart;
            System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mMergedSize, var2);
            this.mMergedSize += var2;
            break;
         }

         Object var4 = this.mOldData[this.mOldDataStart];
         Object var5 = var1[var3];
         int var6 = this.mCallback.compare(var4, var5);
         if (var6 > 0) {
            Object[] var8 = this.mData;
            var6 = this.mMergedSize++;
            var8[var6] = var5;
            ++this.mSize;
            ++var3;
            this.mCallback.onInserted(this.mMergedSize - 1, 1);
         } else if (var6 == 0 && this.mCallback.areItemsTheSame(var4, var5)) {
            Object[] var7 = this.mData;
            var6 = this.mMergedSize++;
            var7[var6] = var5;
            var6 = var3 + 1;
            ++this.mOldDataStart;
            var3 = var6;
            if (!this.mCallback.areContentsTheSame(var4, var5)) {
               this.mCallback.onChanged(this.mMergedSize - 1, 1);
               var3 = var6;
            }
         } else {
            Object[] var9 = this.mData;
            var6 = this.mMergedSize++;
            var9[var6] = var4;
            ++this.mOldDataStart;
         }
      }

   }

   private boolean remove(Object var1, boolean var2) {
      int var3 = this.findIndexOf(var1, this.mData, 0, this.mSize, 2);
      if (var3 == -1) {
         return false;
      } else {
         this.removeItemAtIndex(var3, var2);
         return true;
      }
   }

   private void removeItemAtIndex(int var1, boolean var2) {
      System.arraycopy(this.mData, var1 + 1, this.mData, var1, this.mSize - var1 - 1);
      --this.mSize;
      this.mData[this.mSize] = null;
      if (var2) {
         this.mCallback.onRemoved(var1, 1);
      }

   }

   private void throwIfMerging() {
      if (this.mOldData != null) {
         throw new IllegalStateException("Cannot call this method from within addAll");
      }
   }

   public int add(Object var1) {
      this.throwIfMerging();
      return this.add(var1, true);
   }

   public void addAll(Collection var1) {
      this.addAll(var1.toArray((Object[])Array.newInstance(this.mTClass, var1.size())), true);
   }

   public void addAll(Object... var1) {
      this.addAll(var1, false);
   }

   public void addAll(Object[] var1, boolean var2) {
      this.throwIfMerging();
      if (var1.length != 0) {
         if (var2) {
            this.addAllInternal(var1);
         } else {
            Object[] var3 = (Object[])Array.newInstance(this.mTClass, var1.length);
            System.arraycopy(var1, 0, var3, 0, var1.length);
            this.addAllInternal(var3);
         }

      }
   }

   public void beginBatchedUpdates() {
      this.throwIfMerging();
      if (!(this.mCallback instanceof SortedList.BatchedCallback)) {
         if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new SortedList.BatchedCallback(this.mCallback);
         }

         this.mCallback = this.mBatchedCallback;
      }
   }

   public void clear() {
      this.throwIfMerging();
      if (this.mSize != 0) {
         int var1 = this.mSize;
         Arrays.fill(this.mData, 0, var1, (Object)null);
         this.mSize = 0;
         this.mCallback.onRemoved(0, var1);
      }
   }

   public void endBatchedUpdates() {
      this.throwIfMerging();
      if (this.mCallback instanceof SortedList.BatchedCallback) {
         ((SortedList.BatchedCallback)this.mCallback).dispatchLastEvent();
      }

      if (this.mCallback == this.mBatchedCallback) {
         this.mCallback = this.mBatchedCallback.mWrappedCallback;
      }

   }

   public Object get(int var1) throws IndexOutOfBoundsException {
      if (var1 < this.mSize && var1 >= 0) {
         return this.mOldData != null && var1 >= this.mMergedSize ? this.mOldData[var1 - this.mMergedSize + this.mOldDataStart] : this.mData[var1];
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Asked to get item at ");
         var2.append(var1);
         var2.append(" but size is ");
         var2.append(this.mSize);
         throw new IndexOutOfBoundsException(var2.toString());
      }
   }

   public int indexOf(Object var1) {
      if (this.mOldData != null) {
         int var2 = this.findIndexOf(var1, this.mData, 0, this.mMergedSize, 4);
         if (var2 != -1) {
            return var2;
         } else {
            var2 = this.findIndexOf(var1, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            return var2 != -1 ? var2 - this.mOldDataStart + this.mMergedSize : -1;
         }
      } else {
         return this.findIndexOf(var1, this.mData, 0, this.mSize, 4);
      }
   }

   public void recalculatePositionOfItemAt(int var1) {
      this.throwIfMerging();
      Object var2 = this.get(var1);
      this.removeItemAtIndex(var1, false);
      int var3 = this.add(var2, false);
      if (var1 != var3) {
         this.mCallback.onMoved(var1, var3);
      }

   }

   public boolean remove(Object var1) {
      this.throwIfMerging();
      return this.remove(var1, true);
   }

   public Object removeItemAt(int var1) {
      this.throwIfMerging();
      Object var2 = this.get(var1);
      this.removeItemAtIndex(var1, true);
      return var2;
   }

   public int size() {
      return this.mSize;
   }

   public void updateItemAt(int var1, Object var2) {
      this.throwIfMerging();
      Object var3 = this.get(var1);
      boolean var4;
      if (var3 != var2 && this.mCallback.areContentsTheSame(var3, var2)) {
         var4 = false;
      } else {
         var4 = true;
      }

      if (var3 != var2 && this.mCallback.compare(var3, var2) == 0) {
         this.mData[var1] = var2;
         if (var4) {
            this.mCallback.onChanged(var1, 1);
         }

      } else {
         if (var4) {
            this.mCallback.onChanged(var1, 1);
         }

         this.removeItemAtIndex(var1, false);
         int var5 = this.add(var2, false);
         if (var1 != var5) {
            this.mCallback.onMoved(var1, var5);
         }

      }
   }

   public static class BatchedCallback extends SortedList.Callback {
      private final BatchingListUpdateCallback mBatchingListUpdateCallback;
      final SortedList.Callback mWrappedCallback;

      public BatchedCallback(SortedList.Callback var1) {
         this.mWrappedCallback = var1;
         this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
      }

      public boolean areContentsTheSame(Object var1, Object var2) {
         return this.mWrappedCallback.areContentsTheSame(var1, var2);
      }

      public boolean areItemsTheSame(Object var1, Object var2) {
         return this.mWrappedCallback.areItemsTheSame(var1, var2);
      }

      public int compare(Object var1, Object var2) {
         return this.mWrappedCallback.compare(var1, var2);
      }

      public void dispatchLastEvent() {
         this.mBatchingListUpdateCallback.dispatchLastEvent();
      }

      public void onChanged(int var1, int var2) {
         this.mBatchingListUpdateCallback.onChanged(var1, var2, (Object)null);
      }

      public void onInserted(int var1, int var2) {
         this.mBatchingListUpdateCallback.onInserted(var1, var2);
      }

      public void onMoved(int var1, int var2) {
         this.mBatchingListUpdateCallback.onMoved(var1, var2);
      }

      public void onRemoved(int var1, int var2) {
         this.mBatchingListUpdateCallback.onRemoved(var1, var2);
      }
   }

   public abstract static class Callback implements Comparator, ListUpdateCallback {
      public abstract boolean areContentsTheSame(Object var1, Object var2);

      public abstract boolean areItemsTheSame(Object var1, Object var2);

      public abstract int compare(Object var1, Object var2);

      public abstract void onChanged(int var1, int var2);

      public void onChanged(int var1, int var2, Object var3) {
         this.onChanged(var1, var2);
      }
   }
}
