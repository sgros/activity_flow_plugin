// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

public interface DefaultLifecycleObserver extends FullLifecycleObserver
{
    void onCreate(final LifecycleOwner p0);
    
    void onDestroy(final LifecycleOwner p0);
    
    void onPause(final LifecycleOwner p0);
    
    void onResume(final LifecycleOwner p0);
    
    void onStart(final LifecycleOwner p0);
    
    void onStop(final LifecycleOwner p0);
}
