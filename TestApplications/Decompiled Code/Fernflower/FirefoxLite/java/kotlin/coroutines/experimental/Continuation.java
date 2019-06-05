package kotlin.coroutines.experimental;

public interface Continuation {
   CoroutineContext getContext();

   void resume(Object var1);

   void resumeWithException(Throwable var1);
}
