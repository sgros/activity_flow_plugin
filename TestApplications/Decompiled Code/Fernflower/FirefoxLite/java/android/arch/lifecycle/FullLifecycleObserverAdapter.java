package android.arch.lifecycle;

class FullLifecycleObserverAdapter implements GenericLifecycleObserver {
   private final FullLifecycleObserver mObserver;

   FullLifecycleObserverAdapter(FullLifecycleObserver var1) {
      this.mObserver = var1;
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      switch(var2) {
      case ON_CREATE:
         this.mObserver.onCreate(var1);
         break;
      case ON_START:
         this.mObserver.onStart(var1);
         break;
      case ON_RESUME:
         this.mObserver.onResume(var1);
         break;
      case ON_PAUSE:
         this.mObserver.onPause(var1);
         break;
      case ON_STOP:
         this.mObserver.onStop(var1);
         break;
      case ON_DESTROY:
         this.mObserver.onDestroy(var1);
         break;
      case ON_ANY:
         throw new IllegalArgumentException("ON_ANY must not been send by anybody");
      }

   }
}
