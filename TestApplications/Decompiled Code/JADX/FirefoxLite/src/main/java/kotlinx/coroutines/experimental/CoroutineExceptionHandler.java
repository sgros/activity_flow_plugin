package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutineContext.Element;

/* compiled from: CoroutineExceptionHandler.kt */
public interface CoroutineExceptionHandler extends Element {
    public static final Key Key = Key.$$INSTANCE;

    /* compiled from: CoroutineExceptionHandler.kt */
    public static final class Key implements kotlin.coroutines.experimental.CoroutineContext.Key<CoroutineExceptionHandler> {
        static final /* synthetic */ Key $$INSTANCE = new Key();

        private Key() {
        }
    }

    void handleException(CoroutineContext coroutineContext, Throwable th);
}
