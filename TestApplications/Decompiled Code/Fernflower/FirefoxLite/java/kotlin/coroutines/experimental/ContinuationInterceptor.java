package kotlin.coroutines.experimental;

public interface ContinuationInterceptor extends CoroutineContext.Element {
   ContinuationInterceptor.Key Key = ContinuationInterceptor.Key.$$INSTANCE;

   Continuation interceptContinuation(Continuation var1);

   public static final class Key implements CoroutineContext.Key {
      // $FF: synthetic field
      static final ContinuationInterceptor.Key $$INSTANCE = new ContinuationInterceptor.Key();

      private Key() {
      }
   }
}
