package mozilla.components.concept.engine;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.OnLifecycleEvent;

/* compiled from: EngineView.kt */
public final class LifecycleObserver implements android.arch.lifecycle.LifecycleObserver {
    private final EngineView engineView;

    @OnLifecycleEvent(Event.ON_PAUSE)
    public final void onPause() {
        this.engineView.onPause();
    }

    @OnLifecycleEvent(Event.ON_RESUME)
    public final void onResume() {
        this.engineView.onResume();
    }

    @OnLifecycleEvent(Event.ON_START)
    public final void onStart() {
        this.engineView.onStart();
    }

    @OnLifecycleEvent(Event.ON_STOP)
    public final void onStop() {
        this.engineView.onStop();
    }

    @OnLifecycleEvent(Event.ON_CREATE)
    public final void onCreate() {
        this.engineView.onCreate();
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    public final void onDestroy() {
        this.engineView.onDestroy();
    }
}
