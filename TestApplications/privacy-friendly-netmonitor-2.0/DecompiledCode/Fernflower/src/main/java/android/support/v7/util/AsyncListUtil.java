package android.support.v7.util;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

public class AsyncListUtil {
   static final boolean DEBUG = false;
   static final String TAG = "AsyncListUtil";
   boolean mAllowScrollHints;
   private final ThreadUtil.BackgroundCallback mBackgroundCallback;
   final ThreadUtil.BackgroundCallback mBackgroundProxy;
   final AsyncListUtil.DataCallback mDataCallback;
   int mDisplayedGeneration = 0;
   int mItemCount = 0;
   private final ThreadUtil.MainThreadCallback mMainThreadCallback;
   final ThreadUtil.MainThreadCallback mMainThreadProxy;
   final SparseIntArray mMissingPositions;
   final int[] mPrevRange = new int[2];
   int mRequestedGeneration;
   private int mScrollHint = 0;
   final Class mTClass;
   final TileList mTileList;
   final int mTileSize;
   final int[] mTmpRange = new int[2];
   final int[] mTmpRangeExtended = new int[2];
   final AsyncListUtil.ViewCallback mViewCallback;

   public AsyncListUtil(Class var1, int var2, AsyncListUtil.DataCallback var3, AsyncListUtil.ViewCallback var4) {
      this.mRequestedGeneration = this.mDisplayedGeneration;
      this.mMissingPositions = new SparseIntArray();
      this.mMainThreadCallback = new ThreadUtil.MainThreadCallback() {
         private boolean isRequestedGeneration(int var1) {
            boolean var2;
            if (var1 == AsyncListUtil.this.mRequestedGeneration) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         private void recycleAllTiles() {
            for(int var1 = 0; var1 < AsyncListUtil.this.mTileList.size(); ++var1) {
               AsyncListUtil.this.mBackgroundProxy.recycleTile(AsyncListUtil.this.mTileList.getAtIndex(var1));
            }

            AsyncListUtil.this.mTileList.clear();
         }

         public void addTile(int var1, TileList.Tile var2) {
            if (!this.isRequestedGeneration(var1)) {
               AsyncListUtil.this.mBackgroundProxy.recycleTile(var2);
            } else {
               TileList.Tile var3 = AsyncListUtil.this.mTileList.addOrReplace(var2);
               if (var3 != null) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("duplicate tile @");
                  var4.append(var3.mStartPosition);
                  Log.e("AsyncListUtil", var4.toString());
                  AsyncListUtil.this.mBackgroundProxy.recycleTile(var3);
               }

               int var5 = var2.mStartPosition;
               int var6 = var2.mItemCount;
               var1 = 0;

               while(true) {
                  while(var1 < AsyncListUtil.this.mMissingPositions.size()) {
                     int var7 = AsyncListUtil.this.mMissingPositions.keyAt(var1);
                     if (var2.mStartPosition <= var7 && var7 < var5 + var6) {
                        AsyncListUtil.this.mMissingPositions.removeAt(var1);
                        AsyncListUtil.this.mViewCallback.onItemLoaded(var7);
                     } else {
                        ++var1;
                     }
                  }

                  return;
               }
            }
         }

         public void removeTile(int var1, int var2) {
            if (this.isRequestedGeneration(var1)) {
               TileList.Tile var3 = AsyncListUtil.this.mTileList.removeAtPos(var2);
               if (var3 == null) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("tile not found @");
                  var4.append(var2);
                  Log.e("AsyncListUtil", var4.toString());
               } else {
                  AsyncListUtil.this.mBackgroundProxy.recycleTile(var3);
               }
            }
         }

         public void updateItemCount(int var1, int var2) {
            if (this.isRequestedGeneration(var1)) {
               AsyncListUtil.this.mItemCount = var2;
               AsyncListUtil.this.mViewCallback.onDataRefresh();
               AsyncListUtil.this.mDisplayedGeneration = AsyncListUtil.this.mRequestedGeneration;
               this.recycleAllTiles();
               AsyncListUtil.this.mAllowScrollHints = false;
               AsyncListUtil.this.updateRange();
            }
         }
      };
      this.mBackgroundCallback = new ThreadUtil.BackgroundCallback() {
         private int mFirstRequiredTileStart;
         private int mGeneration;
         private int mItemCount;
         private int mLastRequiredTileStart;
         final SparseBooleanArray mLoadedTiles = new SparseBooleanArray();
         private TileList.Tile mRecycledRoot;

         private TileList.Tile acquireTile() {
            if (this.mRecycledRoot != null) {
               TileList.Tile var1 = this.mRecycledRoot;
               this.mRecycledRoot = this.mRecycledRoot.mNext;
               return var1;
            } else {
               return new TileList.Tile(AsyncListUtil.this.mTClass, AsyncListUtil.this.mTileSize);
            }
         }

         private void addTile(TileList.Tile var1) {
            this.mLoadedTiles.put(var1.mStartPosition, true);
            AsyncListUtil.this.mMainThreadProxy.addTile(this.mGeneration, var1);
         }

         private void flushTileCache(int var1) {
            int var2 = AsyncListUtil.this.mDataCallback.getMaxCachedTiles();

            while(true) {
               while(this.mLoadedTiles.size() >= var2) {
                  int var3 = this.mLoadedTiles.keyAt(0);
                  int var4 = this.mLoadedTiles.keyAt(this.mLoadedTiles.size() - 1);
                  int var5 = this.mFirstRequiredTileStart - var3;
                  int var6 = var4 - this.mLastRequiredTileStart;
                  if (var5 <= 0 || var5 < var6 && var1 != 2) {
                     if (var6 <= 0 || var5 >= var6 && var1 != 1) {
                        return;
                     }

                     this.removeTile(var4);
                  } else {
                     this.removeTile(var3);
                  }
               }

               return;
            }
         }

         private int getTileStart(int var1) {
            return var1 - var1 % AsyncListUtil.this.mTileSize;
         }

         private boolean isTileLoaded(int var1) {
            return this.mLoadedTiles.get(var1);
         }

         private void log(String var1, Object... var2) {
            StringBuilder var3 = new StringBuilder();
            var3.append("[BKGR] ");
            var3.append(String.format(var1, var2));
            Log.d("AsyncListUtil", var3.toString());
         }

         private void removeTile(int var1) {
            this.mLoadedTiles.delete(var1);
            AsyncListUtil.this.mMainThreadProxy.removeTile(this.mGeneration, var1);
         }

         private void requestTiles(int var1, int var2, int var3, boolean var4) {
            for(int var5 = var1; var5 <= var2; var5 += AsyncListUtil.this.mTileSize) {
               int var6;
               if (var4) {
                  var6 = var2 + var1 - var5;
               } else {
                  var6 = var5;
               }

               AsyncListUtil.this.mBackgroundProxy.loadTile(var6, var3);
            }

         }

         public void loadTile(int var1, int var2) {
            if (!this.isTileLoaded(var1)) {
               TileList.Tile var3 = this.acquireTile();
               var3.mStartPosition = var1;
               var3.mItemCount = Math.min(AsyncListUtil.this.mTileSize, this.mItemCount - var3.mStartPosition);
               AsyncListUtil.this.mDataCallback.fillData(var3.mItems, var3.mStartPosition, var3.mItemCount);
               this.flushTileCache(var2);
               this.addTile(var3);
            }
         }

         public void recycleTile(TileList.Tile var1) {
            AsyncListUtil.this.mDataCallback.recycleData(var1.mItems, var1.mItemCount);
            var1.mNext = this.mRecycledRoot;
            this.mRecycledRoot = var1;
         }

         public void refresh(int var1) {
            this.mGeneration = var1;
            this.mLoadedTiles.clear();
            this.mItemCount = AsyncListUtil.this.mDataCallback.refreshData();
            AsyncListUtil.this.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
         }

         public void updateRange(int var1, int var2, int var3, int var4, int var5) {
            if (var1 <= var2) {
               var1 = this.getTileStart(var1);
               var2 = this.getTileStart(var2);
               this.mFirstRequiredTileStart = this.getTileStart(var3);
               this.mLastRequiredTileStart = this.getTileStart(var4);
               if (var5 == 1) {
                  this.requestTiles(this.mFirstRequiredTileStart, var2, var5, true);
                  this.requestTiles(var2 + AsyncListUtil.this.mTileSize, this.mLastRequiredTileStart, var5, false);
               } else {
                  this.requestTiles(var1, this.mLastRequiredTileStart, var5, false);
                  this.requestTiles(this.mFirstRequiredTileStart, var1 - AsyncListUtil.this.mTileSize, var5, true);
               }

            }
         }
      };
      this.mTClass = var1;
      this.mTileSize = var2;
      this.mDataCallback = var3;
      this.mViewCallback = var4;
      this.mTileList = new TileList(this.mTileSize);
      MessageThreadUtil var5 = new MessageThreadUtil();
      this.mMainThreadProxy = var5.getMainThreadProxy(this.mMainThreadCallback);
      this.mBackgroundProxy = var5.getBackgroundProxy(this.mBackgroundCallback);
      this.refresh();
   }

   private boolean isRefreshPending() {
      boolean var1;
      if (this.mRequestedGeneration != this.mDisplayedGeneration) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public Object getItem(int var1) {
      if (var1 >= 0 && var1 < this.mItemCount) {
         Object var3 = this.mTileList.getItemAt(var1);
         if (var3 == null && !this.isRefreshPending()) {
            this.mMissingPositions.put(var1, 0);
         }

         return var3;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append(" is not within 0 and ");
         var2.append(this.mItemCount);
         throw new IndexOutOfBoundsException(var2.toString());
      }
   }

   public int getItemCount() {
      return this.mItemCount;
   }

   void log(String var1, Object... var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("[MAIN] ");
      var3.append(String.format(var1, var2));
      Log.d("AsyncListUtil", var3.toString());
   }

   public void onRangeChanged() {
      if (!this.isRefreshPending()) {
         this.updateRange();
         this.mAllowScrollHints = true;
      }
   }

   public void refresh() {
      this.mMissingPositions.clear();
      ThreadUtil.BackgroundCallback var1 = this.mBackgroundProxy;
      int var2 = this.mRequestedGeneration + 1;
      this.mRequestedGeneration = var2;
      var1.refresh(var2);
   }

   void updateRange() {
      this.mViewCallback.getItemRangeInto(this.mTmpRange);
      if (this.mTmpRange[0] <= this.mTmpRange[1] && this.mTmpRange[0] >= 0) {
         if (this.mTmpRange[1] < this.mItemCount) {
            if (!this.mAllowScrollHints) {
               this.mScrollHint = 0;
            } else if (this.mTmpRange[0] <= this.mPrevRange[1] && this.mPrevRange[0] <= this.mTmpRange[1]) {
               if (this.mTmpRange[0] < this.mPrevRange[0]) {
                  this.mScrollHint = 1;
               } else if (this.mTmpRange[0] > this.mPrevRange[0]) {
                  this.mScrollHint = 2;
               }
            } else {
               this.mScrollHint = 0;
            }

            this.mPrevRange[0] = this.mTmpRange[0];
            this.mPrevRange[1] = this.mTmpRange[1];
            this.mViewCallback.extendRangeInto(this.mTmpRange, this.mTmpRangeExtended, this.mScrollHint);
            this.mTmpRangeExtended[0] = Math.min(this.mTmpRange[0], Math.max(this.mTmpRangeExtended[0], 0));
            this.mTmpRangeExtended[1] = Math.max(this.mTmpRange[1], Math.min(this.mTmpRangeExtended[1], this.mItemCount - 1));
            this.mBackgroundProxy.updateRange(this.mTmpRange[0], this.mTmpRange[1], this.mTmpRangeExtended[0], this.mTmpRangeExtended[1], this.mScrollHint);
         }
      }
   }

   public abstract static class DataCallback {
      @WorkerThread
      public abstract void fillData(Object[] var1, int var2, int var3);

      @WorkerThread
      public int getMaxCachedTiles() {
         return 10;
      }

      @WorkerThread
      public void recycleData(Object[] var1, int var2) {
      }

      @WorkerThread
      public abstract int refreshData();
   }

   public abstract static class ViewCallback {
      public static final int HINT_SCROLL_ASC = 2;
      public static final int HINT_SCROLL_DESC = 1;
      public static final int HINT_SCROLL_NONE = 0;

      @UiThread
      public void extendRangeInto(int[] var1, int[] var2, int var3) {
         int var4 = var1[1] - var1[0] + 1;
         int var5 = var4 / 2;
         int var6 = var1[0];
         int var7;
         if (var3 == 1) {
            var7 = var4;
         } else {
            var7 = var5;
         }

         var2[0] = var6 - var7;
         var7 = var1[1];
         if (var3 != 2) {
            var4 = var5;
         }

         var2[1] = var7 + var4;
      }

      @UiThread
      public abstract void getItemRangeInto(int[] var1);

      @UiThread
      public abstract void onDataRefresh();

      @UiThread
      public abstract void onItemLoaded(int var1);
   }
}
