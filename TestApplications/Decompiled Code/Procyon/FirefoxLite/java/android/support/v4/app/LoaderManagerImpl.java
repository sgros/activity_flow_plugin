// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.support.v4.util.SparseArrayCompat;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.Observer;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.util.DebugUtils;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.LifecycleOwner;

class LoaderManagerImpl extends LoaderManager
{
    static boolean DEBUG = false;
    private final LifecycleOwner mLifecycleOwner;
    private final LoaderViewModel mLoaderViewModel;
    
    LoaderManagerImpl(final LifecycleOwner mLifecycleOwner, final ViewModelStore viewModelStore) {
        this.mLifecycleOwner = mLifecycleOwner;
        this.mLoaderViewModel = LoaderViewModel.getInstance(viewModelStore);
    }
    
    @Deprecated
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.mLoaderViewModel.dump(s, fileDescriptor, printWriter, array);
    }
    
    @Override
    public void markForRedelivery() {
        this.mLoaderViewModel.markForRedelivery();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mLifecycleOwner, sb);
        sb.append("}}");
        return sb.toString();
    }
    
    public static class LoaderInfo<D> extends MutableLiveData<D> implements OnLoadCompleteListener<D>
    {
        private final Bundle mArgs;
        private final int mId;
        private LifecycleOwner mLifecycleOwner;
        private final Loader<D> mLoader;
        private LoaderObserver<D> mObserver;
        private Loader<D> mPriorLoader;
        
        Loader<D> destroy(final boolean b) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Destroying: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mLoader.cancelLoad();
            this.mLoader.abandon();
            final LoaderObserver<D> mObserver = this.mObserver;
            if (mObserver != null) {
                this.removeObserver(mObserver);
                if (b) {
                    mObserver.reset();
                }
            }
            this.mLoader.unregisterListener((Loader.OnLoadCompleteListener<D>)this);
            if ((mObserver != null && !mObserver.hasDeliveredData()) || b) {
                this.mLoader.reset();
                return this.mPriorLoader;
            }
            return this.mLoader;
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            printWriter.print(s);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println(this.mArgs);
            printWriter.print(s);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            final Loader<D> mLoader = this.mLoader;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("  ");
            mLoader.dump(sb.toString(), fileDescriptor, printWriter, array);
            if (this.mObserver != null) {
                printWriter.print(s);
                printWriter.print("mCallbacks=");
                printWriter.println(this.mObserver);
                final LoaderObserver<D> mObserver = this.mObserver;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("  ");
                mObserver.dump(sb2.toString(), printWriter);
            }
            printWriter.print(s);
            printWriter.print("mData=");
            printWriter.println(this.getLoader().dataToString(this.getValue()));
            printWriter.print(s);
            printWriter.print("mStarted=");
            printWriter.println(this.hasActiveObservers());
        }
        
        Loader<D> getLoader() {
            return this.mLoader;
        }
        
        void markForRedelivery() {
            final LifecycleOwner mLifecycleOwner = this.mLifecycleOwner;
            final LoaderObserver<D> mObserver = this.mObserver;
            if (mLifecycleOwner != null && mObserver != null) {
                super.removeObserver(mObserver);
                this.observe(mLifecycleOwner, mObserver);
            }
        }
        
        @Override
        protected void onActive() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Starting: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mLoader.startLoading();
        }
        
        @Override
        protected void onInactive() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Stopping: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mLoader.stopLoading();
        }
        
        @Override
        public void removeObserver(final Observer<? super D> observer) {
            super.removeObserver((Observer<D>)observer);
            this.mLifecycleOwner = null;
            this.mObserver = null;
        }
        
        @Override
        public void setValue(final D value) {
            super.setValue(value);
            if (this.mPriorLoader != null) {
                this.mPriorLoader.reset();
                this.mPriorLoader = null;
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }
    }
    
    static class LoaderObserver<D> implements Observer<D>
    {
        private final LoaderCallbacks<D> mCallback;
        private boolean mDeliveredData;
        private final Loader<D> mLoader;
        
        public void dump(final String s, final PrintWriter printWriter) {
            printWriter.print(s);
            printWriter.print("mDeliveredData=");
            printWriter.println(this.mDeliveredData);
        }
        
        boolean hasDeliveredData() {
            return this.mDeliveredData;
        }
        
        @Override
        public void onChanged(final D n) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  onLoadFinished in ");
                sb.append(this.mLoader);
                sb.append(": ");
                sb.append(this.mLoader.dataToString(n));
                Log.v("LoaderManager", sb.toString());
            }
            this.mCallback.onLoadFinished(this.mLoader, n);
            this.mDeliveredData = true;
        }
        
        void reset() {
            if (this.mDeliveredData) {
                if (LoaderManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("  Resetting: ");
                    sb.append(this.mLoader);
                    Log.v("LoaderManager", sb.toString());
                }
                this.mCallback.onLoaderReset(this.mLoader);
            }
        }
        
        @Override
        public String toString() {
            return this.mCallback.toString();
        }
    }
    
    static class LoaderViewModel extends ViewModel
    {
        private static final ViewModelProvider.Factory FACTORY;
        private boolean mCreatingLoader;
        private SparseArrayCompat<LoaderInfo> mLoaders;
        
        static {
            FACTORY = new ViewModelProvider.Factory() {
                @Override
                public <T extends ViewModel> T create(final Class<T> clazz) {
                    return (T)new LoaderViewModel();
                }
            };
        }
        
        LoaderViewModel() {
            this.mLoaders = new SparseArrayCompat<LoaderInfo>();
            this.mCreatingLoader = false;
        }
        
        static LoaderViewModel getInstance(final ViewModelStore viewModelStore) {
            return new ViewModelProvider(viewModelStore, LoaderViewModel.FACTORY).get(LoaderViewModel.class);
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            if (this.mLoaders.size() > 0) {
                printWriter.print(s);
                printWriter.println("Loaders:");
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("    ");
                final String string = sb.toString();
                for (int i = 0; i < this.mLoaders.size(); ++i) {
                    final LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(this.mLoaders.keyAt(i));
                    printWriter.print(": ");
                    printWriter.println(loaderInfo.toString());
                    loaderInfo.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        
        void markForRedelivery() {
            for (int size = this.mLoaders.size(), i = 0; i < size; ++i) {
                this.mLoaders.valueAt(i).markForRedelivery();
            }
        }
        
        @Override
        protected void onCleared() {
            super.onCleared();
            for (int size = this.mLoaders.size(), i = 0; i < size; ++i) {
                this.mLoaders.valueAt(i).destroy(true);
            }
            this.mLoaders.clear();
        }
    }
}
