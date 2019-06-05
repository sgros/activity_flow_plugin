package android.support.p001v4.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ReportFragment;
import android.os.Bundle;
import android.support.p001v4.util.SimpleArrayMap;
import android.support.p001v4.view.KeyEventDispatcher;
import android.support.p001v4.view.KeyEventDispatcher.Component;
import android.view.KeyEvent;
import android.view.View;

/* compiled from: ComponentActivity */
/* renamed from: android.support.v4.app.SupportActivity */
public class SupportActivity extends Activity implements LifecycleOwner, Component {
    private SimpleArrayMap<Class<? extends Object>, Object> mExtraDataMap = new SimpleArrayMap();
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ReportFragment.injectIfNeededIn(this);
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle bundle) {
        this.mLifecycleRegistry.markState(State.CREATED);
        super.onSaveInstanceState(bundle);
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        View decorView = getWindow().getDecorView();
        if (decorView == null || !KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return super.dispatchKeyShortcutEvent(keyEvent);
        }
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        View decorView = getWindow().getDecorView();
        if (decorView == null || !KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return KeyEventDispatcher.dispatchKeyEvent(this, decorView, this, keyEvent);
        }
        return true;
    }
}
