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
   static boolean DEBUG;
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
            StringBuilder var2 = new StringBuilder();
            var2.append("destroyLoader in ");
            var2.append(this);
            var2.append(" of ");
            var2.append(var1);
            Log.v("LoaderManager", var2.toString());
         }

         int var3 = this.mLoaders.indexOfKey(var1);
         LoaderManagerImpl.LoaderInfo var4;
         if (var3 >= 0) {
            var4 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var3);
            this.mLoaders.removeAt(var3);
            var4.destroy();
         }

         var1 = this.mInactiveLoaders.indexOfKey(var1);
         if (var1 >= 0) {
            var4 = (LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.valueAt(var1);
            this.mInactiveLoaders.removeAt(var1);
            var4.destroy();
         }

         if (this.mHost != null && !this.hasRunningLoaders()) {
            this.mHost.mFragmentManager.startPendingDeferredFragments();
         }

      }
   }

   void doDestroy() {
      StringBuilder var1;
      int var2;
      if (!this.mRetaining) {
         if (DEBUG) {
            var1 = new StringBuilder();
            var1.append("Destroying Active in ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
         }

         for(var2 = this.mLoaders.size() - 1; var2 >= 0; --var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).destroy();
         }

         this.mLoaders.clear();
      }

      if (DEBUG) {
         var1 = new StringBuilder();
         var1.append("Destroying Inactive in ");
         var1.append(this);
         Log.v("LoaderManager", var1.toString());
      }

      for(var2 = this.mInactiveLoaders.size() - 1; var2 >= 0; --var2) {
         ((LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.valueAt(var2)).destroy();
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
      StringBuilder var1;
      if (DEBUG) {
         var1 = new StringBuilder();
         var1.append("Retaining in ");
         var1.append(this);
         Log.v("LoaderManager", var1.toString());
      }

      if (!this.mStarted) {
         RuntimeException var2 = new RuntimeException("here");
         var2.fillInStackTrace();
         var1 = new StringBuilder();
         var1.append("Called doRetain when not started: ");
         var1.append(this);
         Log.w("LoaderManager", var1.toString(), var2);
      } else {
         this.mRetaining = true;
         this.mStarted = false;

         for(int var3 = this.mLoaders.size() - 1; var3 >= 0; --var3) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var3)).retain();
         }

      }
   }

   void doStart() {
      StringBuilder var1;
      if (DEBUG) {
         var1 = new StringBuilder();
         var1.append("Starting in ");
         var1.append(this);
         Log.v("LoaderManager", var1.toString());
      }

      if (this.mStarted) {
         RuntimeException var2 = new RuntimeException("here");
         var2.fillInStackTrace();
         var1 = new StringBuilder();
         var1.append("Called doStart when already started: ");
         var1.append(this);
         Log.w("LoaderManager", var1.toString(), var2);
      } else {
         this.mStarted = true;

         for(int var3 = this.mLoaders.size() - 1; var3 >= 0; --var3) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var3)).start();
         }

      }
   }

   void doStop() {
      StringBuilder var1;
      if (DEBUG) {
         var1 = new StringBuilder();
         var1.append("Stopping in ");
         var1.append(this);
         Log.v("LoaderManager", var1.toString());
      }

      if (!this.mStarted) {
         RuntimeException var2 = new RuntimeException("here");
         var2.fillInStackTrace();
         var1 = new StringBuilder();
         var1.append("Called doStop when not started: ");
         var1.append(this);
         Log.w("LoaderManager", var1.toString(), var2);
      } else {
         for(int var3 = this.mLoaders.size() - 1; var3 >= 0; --var3) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var3)).stop();
         }

         this.mStarted = false;
      }
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      int var5 = this.mLoaders.size();
      byte var6 = 0;
      StringBuilder var7;
      if (var5 > 0) {
         var3.print(var1);
         var3.println("Active Loaders:");
         var7 = new StringBuilder();
         var7.append(var1);
         var7.append("    ");
         String var8 = var7.toString();

         for(var5 = 0; var5 < this.mLoaders.size(); ++var5) {
            LoaderManagerImpl.LoaderInfo var9 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var5);
            var3.print(var1);
            var3.print("  #");
            var3.print(this.mLoaders.keyAt(var5));
            var3.print(": ");
            var3.println(var9.toString());
            var9.dump(var8, var2, var3, var4);
         }
      }

      if (this.mInactiveLoaders.size() > 0) {
         var3.print(var1);
         var3.println("Inactive Loaders:");
         var7 = new StringBuilder();
         var7.append(var1);
         var7.append("    ");
         String var10 = var7.toString();

         for(var5 = var6; var5 < this.mInactiveLoaders.size(); ++var5) {
            LoaderManagerImpl.LoaderInfo var11 = (LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.valueAt(var5);
            var3.print(var1);
            var3.print("  #");
            var3.print(this.mInactiveLoaders.keyAt(var5));
            var3.print(": ");
            var3.println(var11.toString());
            var11.dump(var10, var2, var3, var4);
         }
      }

   }

   void finishRetain() {
      if (this.mRetaining) {
         if (DEBUG) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Finished Retaining in ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
         }

         this.mRetaining = false;

         for(int var2 = this.mLoaders.size() - 1; var2 >= 0; --var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).finishRetain();
         }
      }

   }

   public Loader getLoader(int var1) {
      if (this.mCreatingLoader) {
         throw new IllegalStateException("Called while creating a loader");
      } else {
         LoaderManagerImpl.LoaderInfo var2 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.get(var1);
         if (var2 != null) {
            return var2.mPendingLoader != null ? var2.mPendingLoader.mLoader : var2.mLoader;
         } else {
            return null;
         }
      }
   }

   public boolean hasRunningLoaders() {
      int var1 = this.mLoaders.size();
      int var2 = 0;

      int var3;
      for(var3 = var2; var2 < var1; ++var2) {
         LoaderManagerImpl.LoaderInfo var4 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2);
         byte var5;
         if (var4.mStarted && !var4.mDeliveredData) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var3 |= var5;
      }

      return (boolean)var3;
   }

   public Loader initLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3) {
      if (this.mCreatingLoader) {
         throw new IllegalStateException("Called while creating a loader");
      } else {
         LoaderManagerImpl.LoaderInfo var4 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.get(var1);
         if (DEBUG) {
            StringBuilder var5 = new StringBuilder();
            var5.append("initLoader in ");
            var5.append(this);
            var5.append(": args=");
            var5.append(var2);
            Log.v("LoaderManager", var5.toString());
         }

         LoaderManagerImpl.LoaderInfo var6;
         StringBuilder var7;
         if (var4 == null) {
            LoaderManagerImpl.LoaderInfo var8 = this.createAndInstallLoader(var1, var2, var3);
            var6 = var8;
            if (DEBUG) {
               var7 = new StringBuilder();
               var7.append("  Created new loader ");
               var7.append(var8);
               Log.v("LoaderManager", var7.toString());
               var6 = var8;
            }
         } else {
            if (DEBUG) {
               var7 = new StringBuilder();
               var7.append("  Re-using existing loader ");
               var7.append(var4);
               Log.v("LoaderManager", var7.toString());
            }

            var4.mCallbacks = var3;
            var6 = var4;
         }

         if (var6.mHaveData && this.mStarted) {
            var6.callOnLoadFinished(var6.mLoader, var6.mData);
         }

         return var6.mLoader;
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
         StringBuilder var5;
         if (DEBUG) {
            var5 = new StringBuilder();
            var5.append("restartLoader in ");
            var5.append(this);
            var5.append(": args=");
            var5.append(var2);
            Log.v("LoaderManager", var5.toString());
         }

         if (var4 != null) {
            LoaderManagerImpl.LoaderInfo var7 = (LoaderManagerImpl.LoaderInfo)this.mInactiveLoaders.get(var1);
            if (var7 != null) {
               if (var4.mHaveData) {
                  if (DEBUG) {
                     StringBuilder var6 = new StringBuilder();
                     var6.append("  Removing last inactive loader: ");
                     var6.append(var4);
                     Log.v("LoaderManager", var6.toString());
                  }

                  var7.mDeliveredData = false;
                  var7.destroy();
                  var4.mLoader.abandon();
                  this.mInactiveLoaders.put(var1, var4);
               } else {
                  if (var4.cancel()) {
                     if (DEBUG) {
                        Log.v("LoaderManager", "  Current loader is running; configuring pending loader");
                     }

                     if (var4.mPendingLoader != null) {
                        if (DEBUG) {
                           var5 = new StringBuilder();
                           var5.append("  Removing pending loader: ");
                           var5.append(var4.mPendingLoader);
                           Log.v("LoaderManager", var5.toString());
                        }

                        var4.mPendingLoader.destroy();
                        var4.mPendingLoader = null;
                     }

                     if (DEBUG) {
                        Log.v("LoaderManager", "  Enqueuing as new pending loader");
                     }

                     var4.mPendingLoader = this.createLoader(var1, var2, var3);
                     return var4.mPendingLoader.mLoader;
                  }

                  if (DEBUG) {
                     Log.v("LoaderManager", "  Current loader is stopped; replacing");
                  }

                  this.mLoaders.put(var1, (Object)null);
                  var4.destroy();
               }
            } else {
               if (DEBUG) {
                  var5 = new StringBuilder();
                  var5.append("  Making last loader inactive: ");
                  var5.append(var4);
                  Log.v("LoaderManager", var5.toString());
               }

               var4.mLoader.abandon();
               this.mInactiveLoaders.put(var1, var4);
            }
         }

         return this.createAndInstallLoader(var1, var2, var3).mLoader;
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
                  var4.append("  onLoadFinished in ");
                  var4.append(var1);
                  var4.append(": ");
                  var4.append(var1.dataToString(var2));
                  Log.v("LoaderManager", var4.toString());
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
            StringBuilder var1 = new StringBuilder();
            var1.append("  Canceling: ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
         }

         if (this.mStarted && this.mLoader != null && this.mListenerRegistered) {
            boolean var2 = this.mLoader.cancelLoad();
            if (!var2) {
               this.onLoadCanceled(this.mLoader);
            }

            return var2;
         } else {
            return false;
         }
      }

      void destroy() {
         StringBuilder var1;
         if (LoaderManagerImpl.DEBUG) {
            var1 = new StringBuilder();
            var1.append("  Destroying: ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
         }

         this.mDestroyed = true;
         boolean var2 = this.mDeliveredData;
         this.mDeliveredData = false;
         if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && var2) {
            if (LoaderManagerImpl.DEBUG) {
               var1 = new StringBuilder();
               var1.append("  Resetting: ");
               var1.append(this);
               Log.v("LoaderManager", var1.toString());
            }

            String var6;
            if (LoaderManagerImpl.this.mHost != null) {
               var6 = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
               LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
            } else {
               var6 = null;
            }

            try {
               this.mCallbacks.onLoaderReset(this.mLoader);
            } finally {
               if (LoaderManagerImpl.this.mHost != null) {
                  LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = var6;
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
         StringBuilder var6;
         if (this.mLoader != null) {
            Loader var5 = this.mLoader;
            var6 = new StringBuilder();
            var6.append(var1);
            var6.append("  ");
            var5.dump(var6.toString(), var2, var3, var4);
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
            LoaderManagerImpl.LoaderInfo var7 = this.mPendingLoader;
            var6 = new StringBuilder();
            var6.append(var1);
            var6.append("  ");
            var7.dump(var6.toString(), var2, var3, var4);
         }

      }

      void finishRetain() {
         if (this.mRetaining) {
            if (LoaderManagerImpl.DEBUG) {
               StringBuilder var1 = new StringBuilder();
               var1.append("  Finished Retaining: ");
               var1.append(this);
               Log.v("LoaderManager", var1.toString());
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
            StringBuilder var3 = new StringBuilder();
            var3.append("onLoadCanceled: ");
            var3.append(this);
            Log.v("LoaderManager", var3.toString());
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
            LoaderManagerImpl.LoaderInfo var4 = this.mPendingLoader;
            if (var4 != null) {
               if (LoaderManagerImpl.DEBUG) {
                  StringBuilder var2 = new StringBuilder();
                  var2.append("  Switching to pending loader: ");
                  var2.append(var4);
                  Log.v("LoaderManager", var2.toString());
               }

               this.mPendingLoader = null;
               LoaderManagerImpl.this.mLoaders.put(this.mId, (Object)null);
               this.destroy();
               LoaderManagerImpl.this.installLoader(var4);
            }

         }
      }

      public void onLoadComplete(Loader var1, Object var2) {
         if (LoaderManagerImpl.DEBUG) {
            StringBuilder var3 = new StringBuilder();
            var3.append("onLoadComplete: ");
            var3.append(this);
            Log.v("LoaderManager", var3.toString());
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
            LoaderManagerImpl.LoaderInfo var6 = this.mPendingLoader;
            if (var6 != null) {
               if (LoaderManagerImpl.DEBUG) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("  Switching to pending loader: ");
                  var5.append(var6);
                  Log.v("LoaderManager", var5.toString());
               }

               this.mPendingLoader = null;
               LoaderManagerImpl.this.mLoaders.put(this.mId, (Object)null);
               this.destroy();
               LoaderManagerImpl.this.installLoader(var6);
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
            StringBuilder var1 = new StringBuilder();
            var1.append("  Retaining: ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
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
            StringBuilder var1;
            if (LoaderManagerImpl.DEBUG) {
               var1 = new StringBuilder();
               var1.append("  Starting: ");
               var1.append(this);
               Log.v("LoaderManager", var1.toString());
            }

            if (this.mLoader == null && this.mCallbacks != null) {
               this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
            }

            if (this.mLoader != null) {
               if (this.mLoader.getClass().isMemberClass() && !Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                  var1 = new StringBuilder();
                  var1.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                  var1.append(this.mLoader);
                  throw new IllegalArgumentException(var1.toString());
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
            StringBuilder var1 = new StringBuilder();
            var1.append("  Stopping: ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
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
