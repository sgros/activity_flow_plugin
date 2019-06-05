package android.support.v4.app;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;

class LoaderManagerImpl extends LoaderManager {
   static boolean DEBUG;
   private final LifecycleOwner mLifecycleOwner;
   private final LoaderManagerImpl.LoaderViewModel mLoaderViewModel;

   LoaderManagerImpl(LifecycleOwner var1, ViewModelStore var2) {
      this.mLifecycleOwner = var1;
      this.mLoaderViewModel = LoaderManagerImpl.LoaderViewModel.getInstance(var2);
   }

   @Deprecated
   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      this.mLoaderViewModel.dump(var1, var2, var3, var4);
   }

   public void markForRedelivery() {
      this.mLoaderViewModel.markForRedelivery();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("LoaderManager{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" in ");
      DebugUtils.buildShortClassTag(this.mLifecycleOwner, var1);
      var1.append("}}");
      return var1.toString();
   }

   public static class LoaderInfo extends MutableLiveData implements Loader.OnLoadCompleteListener {
      private final Bundle mArgs;
      private final int mId;
      private LifecycleOwner mLifecycleOwner;
      private final Loader mLoader;
      private LoaderManagerImpl.LoaderObserver mObserver;
      private Loader mPriorLoader;

      Loader destroy(boolean var1) {
         if (LoaderManagerImpl.DEBUG) {
            StringBuilder var2 = new StringBuilder();
            var2.append("  Destroying: ");
            var2.append(this);
            Log.v("LoaderManager", var2.toString());
         }

         this.mLoader.cancelLoad();
         this.mLoader.abandon();
         LoaderManagerImpl.LoaderObserver var3 = this.mObserver;
         if (var3 != null) {
            this.removeObserver(var3);
            if (var1) {
               var3.reset();
            }
         }

         this.mLoader.unregisterListener(this);
         if ((var3 == null || var3.hasDeliveredData()) && !var1) {
            return this.mLoader;
         } else {
            this.mLoader.reset();
            return this.mPriorLoader;
         }
      }

      public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         var3.print(var1);
         var3.print("mId=");
         var3.print(this.mId);
         var3.print(" mArgs=");
         var3.println(this.mArgs);
         var3.print(var1);
         var3.print("mLoader=");
         var3.println(this.mLoader);
         Loader var5 = this.mLoader;
         StringBuilder var6 = new StringBuilder();
         var6.append(var1);
         var6.append("  ");
         var5.dump(var6.toString(), var2, var3, var4);
         if (this.mObserver != null) {
            var3.print(var1);
            var3.print("mCallbacks=");
            var3.println(this.mObserver);
            LoaderManagerImpl.LoaderObserver var7 = this.mObserver;
            StringBuilder var8 = new StringBuilder();
            var8.append(var1);
            var8.append("  ");
            var7.dump(var8.toString(), var3);
         }

         var3.print(var1);
         var3.print("mData=");
         var3.println(this.getLoader().dataToString(this.getValue()));
         var3.print(var1);
         var3.print("mStarted=");
         var3.println(this.hasActiveObservers());
      }

      Loader getLoader() {
         return this.mLoader;
      }

      void markForRedelivery() {
         LifecycleOwner var1 = this.mLifecycleOwner;
         LoaderManagerImpl.LoaderObserver var2 = this.mObserver;
         if (var1 != null && var2 != null) {
            super.removeObserver(var2);
            this.observe(var1, var2);
         }

      }

      protected void onActive() {
         if (LoaderManagerImpl.DEBUG) {
            StringBuilder var1 = new StringBuilder();
            var1.append("  Starting: ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
         }

         this.mLoader.startLoading();
      }

      protected void onInactive() {
         if (LoaderManagerImpl.DEBUG) {
            StringBuilder var1 = new StringBuilder();
            var1.append("  Stopping: ");
            var1.append(this);
            Log.v("LoaderManager", var1.toString());
         }

         this.mLoader.stopLoading();
      }

      public void removeObserver(Observer var1) {
         super.removeObserver(var1);
         this.mLifecycleOwner = null;
         this.mObserver = null;
      }

      public void setValue(Object var1) {
         super.setValue(var1);
         if (this.mPriorLoader != null) {
            this.mPriorLoader.reset();
            this.mPriorLoader = null;
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

   static class LoaderObserver implements Observer {
      private final LoaderManager.LoaderCallbacks mCallback;
      private boolean mDeliveredData;
      private final Loader mLoader;

      public void dump(String var1, PrintWriter var2) {
         var2.print(var1);
         var2.print("mDeliveredData=");
         var2.println(this.mDeliveredData);
      }

      boolean hasDeliveredData() {
         return this.mDeliveredData;
      }

      public void onChanged(Object var1) {
         if (LoaderManagerImpl.DEBUG) {
            StringBuilder var2 = new StringBuilder();
            var2.append("  onLoadFinished in ");
            var2.append(this.mLoader);
            var2.append(": ");
            var2.append(this.mLoader.dataToString(var1));
            Log.v("LoaderManager", var2.toString());
         }

         this.mCallback.onLoadFinished(this.mLoader, var1);
         this.mDeliveredData = true;
      }

      void reset() {
         if (this.mDeliveredData) {
            if (LoaderManagerImpl.DEBUG) {
               StringBuilder var1 = new StringBuilder();
               var1.append("  Resetting: ");
               var1.append(this.mLoader);
               Log.v("LoaderManager", var1.toString());
            }

            this.mCallback.onLoaderReset(this.mLoader);
         }

      }

      public String toString() {
         return this.mCallback.toString();
      }
   }

   static class LoaderViewModel extends ViewModel {
      private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {
         public ViewModel create(Class var1) {
            return new LoaderManagerImpl.LoaderViewModel();
         }
      };
      private boolean mCreatingLoader = false;
      private SparseArrayCompat mLoaders = new SparseArrayCompat();

      static LoaderManagerImpl.LoaderViewModel getInstance(ViewModelStore var0) {
         return (LoaderManagerImpl.LoaderViewModel)(new ViewModelProvider(var0, FACTORY)).get(LoaderManagerImpl.LoaderViewModel.class);
      }

      public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         if (this.mLoaders.size() > 0) {
            var3.print(var1);
            var3.println("Loaders:");
            StringBuilder var5 = new StringBuilder();
            var5.append(var1);
            var5.append("    ");
            String var8 = var5.toString();

            for(int var6 = 0; var6 < this.mLoaders.size(); ++var6) {
               LoaderManagerImpl.LoaderInfo var7 = (LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var6);
               var3.print(var1);
               var3.print("  #");
               var3.print(this.mLoaders.keyAt(var6));
               var3.print(": ");
               var3.println(var7.toString());
               var7.dump(var8, var2, var3, var4);
            }
         }

      }

      void markForRedelivery() {
         int var1 = this.mLoaders.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).markForRedelivery();
         }

      }

      protected void onCleared() {
         super.onCleared();
         int var1 = this.mLoaders.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ((LoaderManagerImpl.LoaderInfo)this.mLoaders.valueAt(var2)).destroy(true);
         }

         this.mLoaders.clear();
      }
   }
}
