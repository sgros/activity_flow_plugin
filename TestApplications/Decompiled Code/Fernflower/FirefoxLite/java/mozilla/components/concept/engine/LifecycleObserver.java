package mozilla.components.concept.engine;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

public final class LifecycleObserver implements android.arch.lifecycle.LifecycleObserver {
   private final EngineView engineView;

   @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
   public final void onCreate() {
      this.engineView.onCreate();
   }

   @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
   public final void onDestroy() {
      this.engineView.onDestroy();
   }

   @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
   public final void onPause() {
      this.engineView.onPause();
   }

   @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
   public final void onResume() {
      this.engineView.onResume();
   }

   @OnLifecycleEvent(Lifecycle.Event.ON_START)
   public final void onStart() {
      this.engineView.onStart();
   }

   @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
   public final void onStop() {
      this.engineView.onStop();
   }
}
