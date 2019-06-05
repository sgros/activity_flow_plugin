package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl extends LoaderManager {
   static boolean DEBUG = false;
   static final String TAG = "LoaderManager";
   boolean mCreatingLoader;
   FragmentHostCallback mHost;
   final SparseArrayCompat mInactiveLoaders = new SparseArrayCompat();
   final SparseArrayCompat mLoaders = new SparseArrayCompat();
   boolean mRetaining;
   boolean mRetainingStarted;
   boolean mStarted;
   final String mWho;

   LoaderManagerImpl(String var1, FragmentHostCallback var2, boolean var3) {
      this.mWho = var1;
      this.mHost = var2;
      this.mStarted = var3;
   }

   private LoaderManagerImpl.LoaderInfo createAndInstallLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3) {
      LoaderManagerImpl.LoaderInfo var6;
      try {
         this.mCreatingLoader = true;
         var6 = this.createLoader(var1, var2, var3);
         this.installLoader(var6);
      } finally {
         this.mCreatingLoader = false;
      }

      return var6;
   }

   private LoaderManagerImpl.LoaderInfo createLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3) {
      LoaderManagerImpl.LoaderInfo var4 = new LoaderManagerImpl.LoaderInfo(var1, var2, var3);
      var4.mLoader = var3.onCreateLoader(var1, var2);
      return var4;
   }

   public void destroyLoader(int var1) {
      if (this.mCreatingLoader) {
         throw new IllegalStateException("Called while creating a loader");
      } else {
         if (DEBUG) {
            Log.v("LoaderManager", "destroyLoader in " + this + " of " + var1);
         }

         int var2 = this.mLoaders.indexOfKey(var1);
         LoaderManagerImpl.LoaderInfo var3;
         if (var2 >= 0) {
            var3 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2);
            this.mLoaders.removeAt(var2);
            var3.destroy();
         }

         var1 = this.mInactiveLoaders.indexOfKey(var1);
         if (var1 >= 0) {
            var3 = (LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.valueAt(var1);
            this.mInactiveLoaders.removeAt(var1);
            var3.destroy();
         }

         if (this.mHost != null && !this.hasRunningLoaders()) {
            this.mHost.mFragmentManager.startPendingDeferredFragments();
         }

      }
   }

   void doDestroy() {
      int var1;
      if (!this.mRetaining) {
         if (DEBUG) {
            Log.v("LoaderManager", "Destroying Active in " + this);
         }

         for(var1 = this.mLoaders.size() - 1; var1 >= 0; --var1) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var1)).destroy();
         }

         this.mLoaders.clear();
      }

      if (DEBUG) {
         Log.v("LoaderManager", "Destroying Inactive in " + this);
      }

      for(var1 = this.mInactiveLoaders.size() - 1; var1 >= 0; --var1) {
         ((LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.valueAt(var1)).destroy();
      }

      this.mInactiveLoaders.clear();
      this.mHost = null;
   }

   void doReportNextStart() {
      for(int var1 = this.mLoaders.size() - 1; var1 >= 0; --var1) {
         ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var1)).mReportNextStart = true;
      }

   }

   void doReportStart() {
      for(int var1 = this.mLoaders.size() - 1; var1 >= 0; --var1) {
         ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var1)).reportStart();
      }

   }

   void doRetain() {
      if (DEBUG) {
         Log.v("LoaderManager", "Retaining in " + this);
      }

      if (!this.mStarted) {
         RuntimeException var1 = new RuntimeException("here");
         var1.fillInStackTrace();
         Log.w("LoaderManager", "Called doRetain when not started: " + this, var1);
      } else {
         this.mRetaining = true;
         this.mStarted = false;

         for(int var2 = this.mLoaders.size() - 1; var2 >= 0; --var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).retain();
         }
      }

   }

   void doStart() {
      if (DEBUG) {
         Log.v("LoaderManager", "Starting in " + this);
      }

      if (this.mStarted) {
         RuntimeException var1 = new RuntimeException("here");
         var1.fillInStackTrace();
         Log.w("LoaderManager", "Called doStart when already started: " + this, var1);
      } else {
         this.mStarted = true;

         for(int var2 = this.mLoaders.size() - 1; var2 >= 0; --var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).start();
         }
      }

   }

   void doStop() {
      if (DEBUG) {
         Log.v("LoaderManager", "Stopping in " + this);
      }

      if (!this.mStarted) {
         RuntimeException var1 = new RuntimeException("here");
         var1.fillInStackTrace();
         Log.w("LoaderManager", "Called doStop when not started: " + this, var1);
      } else {
         for(int var2 = this.mLoaders.size() - 1; var2 >= 0; --var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).stop();
         }

         this.mStarted = false;
      }

   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      int var6;
      if (this.mLoaders.size() > 0) {
         var3.print(var1);
         var3.println("Active Loaders:");
         String var5 = var1 + "    ";

         for(var6 = 0; var6 < this.mLoaders.size(); ++var6) {
            LoaderManagerImpl.LoaderInfo var7 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var6);
            var3.print(var1);
            var3.print("  #");
            var3.print(this.mLoaders.keyAt(var6));
            var3.print(": ");
            var3.println(var7.toString());
            var7.dump(var5, var2, var3, var4);
         }
      }

      if (this.mInactiveLoaders.size() > 0) {
         var3.print(var1);
         var3.println("Inactive Loaders:");
         String var9 = var1 + "    ";

         for(var6 = 0; var6 < this.mInactiveLoaders.size(); ++var6) {
            LoaderManagerImpl.LoaderInfo var8 = (LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.valueAt(var6);
            var3.print(var1);
            var3.print("  #");
            var3.print(this.mInactiveLoaders.keyAt(var6));
            var3.print(": ");
            var3.println(var8.toString());
            var8.dump(var9, var2, var3, var4);
         }
      }

   }

   void finishRetain() {
      if (this.mRetaining) {
         if (DEBUG) {
            Log.v("LoaderManager", "Finished Retaining in " + this);
         }

         this.mRetaining = false;

         for(int var1 = this.mLoaders.size() - 1; var1 >= 0; --var1) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var1)).finishRetain();
         }
      }

   }

   public Loader getLoader(int var1) {
      if (this.mCreatingLoader) {
         throw new IllegalStateException("Called while creating a loader");
      } else {
         LoaderManagerImpl.LoaderInfo var2 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.get(var1);
         Loader var3;
         if (var2 != null) {
            if (var2.mPendingLoader != null) {
               var3 = var2.mPendingLoader.mLoader;
            } else {
               var3 = var2.mLoader;
            }
         } else {
            var3 = null;
         }

         return var3;
      }
   }

   public boolean hasRunningLoaders() {
      boolean var1 = false;
      int var2 = this.mLoaders.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         LoaderManagerImpl.LoaderInfo var4 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var3);
         boolean var5;
         if (var4.mStarted && !var4.mDeliveredData) {
            var5 = true;
         } else {
            var5 = false;
         }

         var1 |= var5;
      }

      return var1;
   }

   public Loader initLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3) {
      if (this.mCreatingLoader) {
         throw new IllegalStateException("Called while creating a loader");
      } else {
         LoaderManagerImpl.LoaderInfo var4 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.get(var1);
         if (DEBUG) {
            Log.v("LoaderManager", "initLoader in " + this + ": args=" + var2);
         }

         LoaderManagerImpl.LoaderInfo var5;
         if (var4 == null) {
            LoaderManagerImpl.LoaderInfo var6 = this.createAndInstallLoader(var1, var2, var3);
            var5 = var6;
            if (DEBUG) {
               Log.v("LoaderManager", "  Created new loader " + var6);
               var5 = var6;
            }
         } else {
            if (DEBUG) {
               Log.v("LoaderManager", "  Re-using existing loader " + var4);
            }

            var4.mCallbacks = var3;
            var5 = var4;
         }

         if (var5.mHaveData && this.mStarted) {
            var5.callOnLoadFinished(var5.mLoader, var5.mData);
         }

         return var5.mLoader;
      }
   }

   void installLoader(LoaderManagerImpl.LoaderInfo var1) {
      this.mLoaders.put(var1.mId, var1);
      if (this.mStarted) {
         var1.start();
      }

   }

   public Loader restartLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3) {
      if (this.mCreatingLoader) {
         throw new IllegalStateException("Called while creating a loader");
      } else {
         LoaderManagerImpl.LoaderInfo var4 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.get(var1);
         if (DEBUG) {
            Log.v("LoaderManager", "restartLoader in " + this + ": args=" + var2);
         }

         Loader var6;
         if (var4 != null) {
            LoaderManagerImpl.LoaderInfo var5 = (LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.get(var1);
            if (var5 != null) {
               if (var4.mHaveData) {
                  if (DEBUG) {
                     Log.v("LoaderManager", "  Removing last inactive loader: " + var4);
                  }

                  var5.mDeliveredData = false;
                  var5.destroy();
                  var4.mLoader.abandon();
                  this.mInactiveLoaders.put(var1, var4);
               } else {
                  if (var4.cancel()) {
                     if (DEBUG) {
                        Log.v("LoaderManager", "  Current loader is running; configuring pending loader");
                     }

                     if (var4.mPendingLoader != null) {
                        if (DEBUG) {
                           Log.v("LoaderManager", "  Removing pending loader: " + var4.mPendingLoader);
                        }

                        var4.mPendingLoader.destroy();
                        var4.mPendingLoader = null;
                     }

                     if (DEBUG) {
                        Log.v("LoaderManager", "  Enqueuing as new pending loader");
                     }

                     var4.mPendingLoader = this.createLoader(var1, var2, var3);
                     var6 = var4.mPendingLoader.mLoader;
                     return var6;
                  }

                  if (DEBUG) {
                     Log.v("LoaderManager", "  Current loader is stopped; replacing");
                  }

                  this.mLoaders.put(var1, (Object)null);
                  var4.destroy();
               }
            } else {
               if (DEBUG) {
                  Log.v("LoaderManager", "  Making last loader inactive: " + var4);
               }

               var4.mLoader.abandon();
               this.mInactiveLoaders.put(var1, var4);
            }
         }

         var6 = this.createAndInstallLoader(var1, var2, var3).mLoader;
         return var6;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("LoaderManager{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" in ");
      DebugUtils.buildShortClassTag(this.mHost, var1);
      var1.append("}}");
      return var1.toString();
   }

   void updateHostController(FragmentHostCallback var1) {
      this.mHost = var1;
   }

   final class LoaderInfo implements Loader.OnLoadCompleteListener, Loader.OnLoadCanceledListener {
      final Bundle mArgs;
      LoaderManager.LoaderCallbacks mCallbacks;
      Object mData;
      boolean mDeliveredData;
      boolean mDestroyed;
      boolean mHaveData;
      final int mId;
      boolean mListenerRegistered;
      Loader mLoader;
      LoaderManagerImpl.LoaderInfo mPendingLoader;
      boolean mReportNextStart;
      boolean mRetaining;
      boolean mRetainingStarted;
      boolean mStarted;

      public LoaderInfo(int var2, Bundle var3, LoaderManager.LoaderCallbacks var4) {
         this.mId = var2;
         this.mArgs = var3;
         this.mCallbacks = var4;
      }

      void callOnLoadFinished(Loader var1, Object var2) {
         if (this.mCallbacks != null) {
            String var3 = null;
            if (LoaderManagerImpl.this.mHost != null) {
               var3 = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
               LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
            }

            try {
               if (LoaderManagerImpl.DEBUG) {
                  StringBuilder var4 = new StringBuilder();
                  Log.v("LoaderManager", var4.append("  onLoadFinished in ").append(var1).append(": ").append(var1.dataToString(var2)).toString());
               }

               this.mCallbacks.onLoadFinished(var1, var2);
            } finally {
               if (LoaderManagerImpl.this.mHost != null) {
                  LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = var3;
               }

            }

            this.mDeliveredData = true;
         }

      }

      boolean cancel() {
         if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "  Canceling: " + this);
         }

         boolean var2;
         if (this.mStarted && this.mLoader != null && this.mListenerRegistered) {
            boolean var1 = this.mLoader.cancelLoad();
            var2 = var1;
            if (!var1) {
               this.onLoadCanceled(this.mLoader);
               var2 = var1;
            }
         } else {
            var2 = false;
         }

         return var2;
      }

      void destroy() {
         if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "  Destroying: " + this);
         }

         this.mDestroyed = true;
         boolean var1 = this.mDeliveredData;
         this.mDeliveredData = false;
         if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && var1) {
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Resetting: " + this);
            }

            String var2 = null;
            if (LoaderManagerImpl.this.mHost != null) {
               var2 = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
               LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
            }

            try {
               this.mCallbacks.onLoaderReset(this.mLoader);
            } finally {
               if (LoaderManagerImpl.this.mHost != null) {
                  LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = var2;
               }

            }
         }

         this.mCallbacks = null;
         this.mData = null;
         this.mHaveData = false;
         if (this.mLoader != null) {
            if (this.mListenerRegistered) {
               this.mListenerRegistered = false;
               this.mLoader.unregisterListener(this);
               this.mLoader.unregisterOnLoadCanceledListener(this);
            }

            this.mLoader.reset();
         }

         if (this.mPendingLoader != null) {
            this.mPendingLoader.destroy();
         }

      }

      public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         var3.print(var1);
         var3.print("mId=");
         var3.print(this.mId);
         var3.print(" mArgs=");
         var3.println(this.mArgs);
         var3.print(var1);
         var3.print("mCallbacks=");
         var3.println(this.mCallbacks);
         var3.print(var1);
         var3.print("mLoader=");
         var3.println(this.mLoader);
         if (this.mLoader != null) {
            this.mLoader.dump(var1 + "  ", var2, var3, var4);
         }

         if (this.mHaveData || this.mDeliveredData) {
            var3.print(var1);
            var3.print("mHaveData=");
            var3.print(this.mHaveData);
            var3.print("  mDeliveredData=");
            var3.println(this.mDeliveredData);
            var3.print(var1);
            var3.print("mData=");
            var3.println(this.mData);
         }

         var3.print(var1);
         var3.print("mStarted=");
         var3.print(this.mStarted);
         var3.print(" mReportNextStart=");
         var3.print(this.mReportNextStart);
         var3.print(" mDestroyed=");
         var3.println(this.mDestroyed);
         var3.print(var1);
         var3.print("mRetaining=");
         var3.print(this.mRetaining);
         var3.print(" mRetainingStarted=");
         var3.print(this.mRetainingStarted);
         var3.print(" mListenerRegistered=");
         var3.println(this.mListenerRegistered);
         if (this.mPendingLoader != null) {
            var3.print(var1);
            var3.println("Pending Loader ");
            var3.print(this.mPendingLoader);
            var3.println(":");
            this.mPendingLoader.dump(var1 + "  ", var2, var3, var4);
         }

      }

      void finishRetain() {
         if (this.mRetaining) {
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Finished Retaining: " + this);
            }

            this.mRetaining = false;
            if (this.mStarted != this.mRetainingStarted && !this.mStarted) {
               this.stop();
            }
         }

         if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
            this.callOnLoadFinished(this.mLoader, this.mData);
         }

      }

      public void onLoadCanceled(Loader var1) {
         if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "onLoadCanceled: " + this);
         }

         if (this.mDestroyed) {
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Ignoring load canceled -- destroyed");
            }
         } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Ignoring load canceled -- not active");
            }
         } else {
            LoaderManagerImpl.LoaderInfo var2 = this.mPendingLoader;
            if (var2 != null) {
               if (LoaderManagerImpl.DEBUG) {
                  Log.v("LoaderManager", "  Switching to pending loader: " + var2);
               }

               this.mPendingLoader = null;
               LoaderManagerImpl.this.mLoaders.put(this.mId, (Object)null);
               this.destroy();
               LoaderManagerImpl.this.installLoader(var2);
            }
         }

      }

      public void onLoadComplete(Loader var1, Object var2) {
         if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "onLoadComplete: " + this);
         }

         if (this.mDestroyed) {
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Ignoring load complete -- destroyed");
            }
         } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Ignoring load complete -- not active");
            }
         } else {
            LoaderManagerImpl.LoaderInfo var3 = this.mPendingLoader;
            if (var3 != null) {
               if (LoaderManagerImpl.DEBUG) {
                  Log.v("LoaderManager", "  Switching to pending loader: " + var3);
               }

               this.mPendingLoader = null;
               LoaderManagerImpl.this.mLoaders.put(this.mId, (Object)null);
               this.destroy();
               LoaderManagerImpl.this.installLoader(var3);
            } else {
               if (this.mData != var2 || !this.mHaveData) {
                  this.mData = var2;
                  this.mHaveData = true;
                  if (this.mStarted) {
                     this.callOnLoadFinished(var1, var2);
                  }
               }

               LoaderManagerImpl.LoaderInfo var4 = (LoaderManagerImpl.LoaderInfo)LoaderManagerImpl.this.mInactiveLoaders.get(this.mId);
               if (var4 != null && var4 != this) {
                  var4.mDeliveredData = false;
                  var4.destroy();
                  LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
               }

               if (LoaderManagerImpl.this.mHost != null && !LoaderManagerImpl.this.hasRunningLoaders()) {
                  LoaderManagerImpl.this.mHost.mFragmentManager.startPendingDeferredFragments();
               }
            }
         }

      }

      void reportStart() {
         if (this.mStarted && this.mReportNextStart) {
            this.mReportNextStart = false;
            if (this.mHaveData && !this.mRetaining) {
               this.callOnLoadFinished(this.mLoader, this.mData);
            }
         }

      }

      void retain() {
         if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "  Retaining: " + this);
         }

         this.mRetaining = true;
         this.mRetainingStarted = this.mStarted;
         this.mStarted = false;
         this.mCallbacks = null;
      }

      void start() {
         if (this.mRetaining && this.mRetainingStarted) {
            this.mStarted = true;
         } else if (!this.mStarted) {
            this.mStarted = true;
            if (LoaderManagerImpl.DEBUG) {
               Log.v("LoaderManager", "  Starting: " + this);
            }

            if (this.mLoader == null && this.mCallbacks != null) {
               this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
            }

            if (this.mLoader != null) {
               if (this.mLoader.getClass().isMemberClass() && !Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                  throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + this.mLoader);
               }

               if (!this.mListenerRegistered) {
                  this.mLoader.registerListener(this.mId, this);
                  this.mLoader.registerOnLoadCanceledListener(this);
                  this.mListenerRegistered = true;
               }

               this.mLoader.startLoading();
            }
         }

      }

      void stop() {
         if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "  Stopping: " + this);
         }

         this.mStarted = false;
         if (!this.mRetaining && this.mLoader != null && this.mListenerRegistered) {
            this.mListenerRegistered = false;
            this.mLoader.unregisterListener(this);
            this.mLoader.unregisterOnLoadCanceledListener(this);
            this.mLoader.stopLoading();
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(64);
         var1.append("LoaderInfo{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" #");
         var1.append(this.mId);
         var1.append(" : ");
         DebugUtils.buildShortClassTag(this.mLoader, var1);
         var1.append("}}");
         return var1.toString();
      }
   }
}
