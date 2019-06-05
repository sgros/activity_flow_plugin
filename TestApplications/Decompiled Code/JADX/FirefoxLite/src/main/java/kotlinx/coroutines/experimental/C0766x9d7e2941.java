package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

/* compiled from: JobSupport.kt */
/* renamed from: kotlinx.coroutines.experimental.JobSupport$awaitSuspend$$inlined$suspendCancellableCoroutine$lambda$1 */
final class C0766x9d7e2941 extends Lambda implements Function1<Throwable, Unit> {
    final /* synthetic */ CancellableContinuation $cont;
    final /* synthetic */ JobSupport this$0;

    C0766x9d7e2941(CancellableContinuation cancellableContinuation, JobSupport jobSupport) {
        this.$cont = cancellableContinuation;
        this.this$0 = jobSupport;
        super(1);
    }

    public final void invoke(Throwable th) {
        Object state$kotlinx_coroutines_core = this.this$0.getState$kotlinx_coroutines_core();
        if ((!(state$kotlinx_coroutines_core instanceof Incomplete) ? 1 : null) == null) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            this.$cont.resumeWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
        } else {
            this.$cont.resume(state$kotlinx_coroutines_core);
        }
    }
}
