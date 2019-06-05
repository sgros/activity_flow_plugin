package android.support.v4.app;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.support.v4.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class LoaderManager {
   public static LoaderManager getInstance(LifecycleOwner var0) {
      return new LoaderManagerImpl(var0, ((ViewModelStoreOwner)var0).getViewModelStore());
   }

   @Deprecated
   public abstract void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

   public abstract void markForRedelivery();

   public interface LoaderCallbacks {
      void onLoadFinished(Loader var1, Object var2);

      void onLoaderReset(Loader var1);
   }
}
