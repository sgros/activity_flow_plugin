// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.view.View;
import android.content.Context;
import android.os.Bundle;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileDescriptor;

public abstract class FragmentManager
{
    public abstract void addOnBackStackChangedListener(final OnBackStackChangedListener p0);
    
    public abstract FragmentTransaction beginTransaction();
    
    public abstract void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    public abstract boolean executePendingTransactions();
    
    public abstract Fragment findFragmentById(final int p0);
    
    public abstract Fragment findFragmentByTag(final String p0);
    
    public abstract BackStackEntry getBackStackEntryAt(final int p0);
    
    public abstract int getBackStackEntryCount();
    
    public abstract List<Fragment> getFragments();
    
    public abstract boolean isDestroyed();
    
    public abstract boolean isStateSaved();
    
    public abstract void popBackStack();
    
    public abstract void popBackStack(final int p0, final int p1);
    
    public abstract boolean popBackStackImmediate();
    
    public abstract void registerFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks p0, final boolean p1);
    
    public abstract void removeOnBackStackChangedListener(final OnBackStackChangedListener p0);
    
    public abstract void unregisterFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks p0);
    
    public interface BackStackEntry
    {
        String getName();
    }
    
    public abstract static class FragmentLifecycleCallbacks
    {
        public void onFragmentActivityCreated(final FragmentManager fragmentManager, final Fragment fragment, final Bundle bundle) {
        }
        
        public void onFragmentAttached(final FragmentManager fragmentManager, final Fragment fragment, final Context context) {
        }
        
        public void onFragmentCreated(final FragmentManager fragmentManager, final Fragment fragment, final Bundle bundle) {
        }
        
        public void onFragmentDestroyed(final FragmentManager fragmentManager, final Fragment fragment) {
        }
        
        public void onFragmentDetached(final FragmentManager fragmentManager, final Fragment fragment) {
        }
        
        public void onFragmentPaused(final FragmentManager fragmentManager, final Fragment fragment) {
        }
        
        public void onFragmentPreAttached(final FragmentManager fragmentManager, final Fragment fragment, final Context context) {
        }
        
        public void onFragmentPreCreated(final FragmentManager fragmentManager, final Fragment fragment, final Bundle bundle) {
        }
        
        public void onFragmentResumed(final FragmentManager fragmentManager, final Fragment fragment) {
        }
        
        public void onFragmentSaveInstanceState(final FragmentManager fragmentManager, final Fragment fragment, final Bundle bundle) {
        }
        
        public void onFragmentStarted(final FragmentManager fragmentManager, final Fragment fragment) {
        }
        
        public void onFragmentStopped(final FragmentManager fragmentManager, final Fragment fragment) {
        }
        
        public void onFragmentViewCreated(final FragmentManager fragmentManager, final Fragment fragment, final View view, final Bundle bundle) {
        }
        
        public void onFragmentViewDestroyed(final FragmentManager fragmentManager, final Fragment fragment) {
        }
    }
    
    public interface OnBackStackChangedListener
    {
        void onBackStackChanged();
    }
}
