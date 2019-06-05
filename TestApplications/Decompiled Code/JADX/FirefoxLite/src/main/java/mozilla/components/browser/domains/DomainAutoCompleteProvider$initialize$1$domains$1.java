package mozilla.components.browser.domains;

import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineScope;

/* compiled from: DomainAutoCompleteProvider.kt */
final class DomainAutoCompleteProvider$initialize$1$domains$1 extends CoroutineImpl implements Function2<CoroutineScope, Continuation<? super List<? extends String>>, Object> {
    /* renamed from: p$ */
    private CoroutineScope f78p$;
    final /* synthetic */ DomainAutoCompleteProvider$initialize$1 this$0;

    DomainAutoCompleteProvider$initialize$1$domains$1(DomainAutoCompleteProvider$initialize$1 domainAutoCompleteProvider$initialize$1, Continuation continuation) {
        this.this$0 = domainAutoCompleteProvider$initialize$1;
        super(2, continuation);
    }

    public final Continuation<Unit> create(CoroutineScope coroutineScope, Continuation<? super List<String>> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        DomainAutoCompleteProvider$initialize$1$domains$1 domainAutoCompleteProvider$initialize$1$domains$1 = new DomainAutoCompleteProvider$initialize$1$domains$1(this.this$0, continuation);
        domainAutoCompleteProvider$initialize$1$domains$1.f78p$ = coroutineScope;
        return domainAutoCompleteProvider$initialize$1$domains$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super List<String>> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return ((DomainAutoCompleteProvider$initialize$1$domains$1) create(coroutineScope, (Continuation) continuation)).doResume(Unit.INSTANCE, null);
    }

    public final Object doResume(Object obj, Throwable th) {
        IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else if (th == null) {
            CoroutineScope coroutineScope = this.f78p$;
            return Domains.INSTANCE.load(this.this$0.$context);
        } else {
            throw th;
        }
    }
}
