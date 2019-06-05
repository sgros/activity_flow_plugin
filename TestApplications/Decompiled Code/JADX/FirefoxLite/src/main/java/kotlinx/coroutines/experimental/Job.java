package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Job.kt */
public interface Job extends Element {
    public static final Key Key = Key.$$INSTANCE;

    /* compiled from: Job.kt */
    public static final class DefaultImpls {
        public static <R> R fold(Job job, R r, Function2<? super R, ? super Element, ? extends R> function2) {
            Intrinsics.checkParameterIsNotNull(function2, "operation");
            return kotlin.coroutines.experimental.CoroutineContext.Element.DefaultImpls.fold(job, r, function2);
        }

        public static <E extends Element> E get(Job job, kotlin.coroutines.experimental.CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return kotlin.coroutines.experimental.CoroutineContext.Element.DefaultImpls.get(job, key);
        }

        public static CoroutineContext minusKey(Job job, kotlin.coroutines.experimental.CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return kotlin.coroutines.experimental.CoroutineContext.Element.DefaultImpls.minusKey(job, key);
        }

        public static CoroutineContext plus(Job job, CoroutineContext coroutineContext) {
            Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
            return kotlin.coroutines.experimental.CoroutineContext.Element.DefaultImpls.plus(job, coroutineContext);
        }

        public static /* bridge */ /* synthetic */ DisposableHandle invokeOnCompletion$default(Job job, boolean z, boolean z2, Function1 function1, int i, Object obj) {
            if (obj == null) {
                if ((i & 1) != 0) {
                    z = false;
                }
                if ((i & 2) != 0) {
                    z2 = true;
                }
                return job.invokeOnCompletion(z, z2, function1);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: invokeOnCompletion");
        }
    }

    /* compiled from: Job.kt */
    public static final class Key implements kotlin.coroutines.experimental.CoroutineContext.Key<Job> {
        static final /* synthetic */ Key $$INSTANCE = new Key();

        static {
            kotlinx.coroutines.experimental.CoroutineExceptionHandler.Key key = CoroutineExceptionHandler.Key;
        }

        private Key() {
        }
    }

    DisposableHandle attachChild(Job job);

    boolean cancel(Throwable th);

    CancellationException getCancellationException();

    DisposableHandle invokeOnCompletion(boolean z, boolean z2, Function1<? super Throwable, Unit> function1);

    boolean isActive();

    boolean start();
}
