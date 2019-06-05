// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.support.v4.content.Loader;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.arch.lifecycle.ViewModelStoreOwner;

public abstract class LoaderManager
{
    public static <T extends LifecycleOwner> LoaderManager getInstance(final T t) {
        return new LoaderManagerImpl((LifecycleOwner)t, ((ViewModelStoreOwner)t).getViewModelStore());
    }
    
    @Deprecated
    public abstract void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    public abstract void markForRedelivery();
    
    public interface LoaderCallbacks<D>
    {
        void onLoadFinished(final Loader<D> p0, final D p1);
        
        void onLoaderReset(final Loader<D> p0);
    }
}
