package android.arch.lifecycle;

public abstract class Lifecycle {
   public abstract void addObserver(LifecycleObserver var1);

   public abstract Lifecycle.State getCurrentState();

   public abstract void removeObserver(LifecycleObserver var1);

   public static enum Event {
      ON_ANY,
      ON_CREATE,
      ON_DESTROY,
      ON_PAUSE,
      ON_RESUME,
      ON_START,
      ON_STOP;
   }

   public static enum State {
      CREATED,
      DESTROYED,
      INITIALIZED,
      RESUMED,
      STARTED;

      public boolean isAtLeast(Lifecycle.State var1) {
         boolean var2;
         if (this.compareTo(var1) >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }
}
