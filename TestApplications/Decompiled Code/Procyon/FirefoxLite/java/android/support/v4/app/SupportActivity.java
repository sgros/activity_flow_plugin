// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.arch.lifecycle.ReportFragment;
import android.os.Bundle;
import android.arch.lifecycle.Lifecycle;
import android.view.View;
import android.view.Window$Callback;
import android.view.KeyEvent;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.KeyEventDispatcher;
import android.arch.lifecycle.LifecycleOwner;
import android.app.Activity;

public class SupportActivity extends Activity implements LifecycleOwner, Component
{
    private SimpleArrayMap<Class<?>, Object> mExtraDataMap;
    private LifecycleRegistry mLifecycleRegistry;
    
    public SupportActivity() {
        this.mExtraDataMap = new SimpleArrayMap<Class<?>, Object>();
        this.mLifecycleRegistry = new LifecycleRegistry(this);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || KeyEventDispatcher.dispatchKeyEvent((KeyEventDispatcher.Component)this, decorView, (Window$Callback)this, keyEvent);
    }
    
    public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || super.dispatchKeyShortcutEvent(keyEvent);
    }
    
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        ReportFragment.injectIfNeededIn(this);
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(bundle);
    }
    
    public boolean superDispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
}
