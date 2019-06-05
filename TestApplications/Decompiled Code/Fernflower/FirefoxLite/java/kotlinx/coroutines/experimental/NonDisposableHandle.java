package kotlinx.coroutines.experimental;

public final class NonDisposableHandle implements DisposableHandle {
   public static final NonDisposableHandle INSTANCE = new NonDisposableHandle();

   private NonDisposableHandle() {
   }

   public void dispose() {
   }

   public String toString() {
      return "NonDisposableHandle";
   }
}
