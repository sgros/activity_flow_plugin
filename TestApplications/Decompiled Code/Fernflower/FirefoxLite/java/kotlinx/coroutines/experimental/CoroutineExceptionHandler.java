package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;

public interface CoroutineExceptionHandler extends CoroutineContext.Element {
   CoroutineExceptionHandler.Key Key = CoroutineExceptionHandler.Key.$$INSTANCE;

   void handleException(CoroutineContext var1, Throwable var2);

   public static final class Key implements CoroutineContext.Key {
      // $FF: synthetic field
      static final CoroutineExceptionHandler.Key $$INSTANCE = new CoroutineExceptionHandler.Key();

      private Key() {
      }
   }
}
