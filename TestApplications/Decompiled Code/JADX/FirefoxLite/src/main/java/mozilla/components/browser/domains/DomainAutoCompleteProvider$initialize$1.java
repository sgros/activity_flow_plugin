package mozilla.components.browser.domains;

import android.content.Context;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CommonPool;
import kotlinx.coroutines.experimental.CoroutineScope;
import kotlinx.coroutines.experimental.Deferred;
import kotlinx.coroutines.experimental.DeferredKt;

/* compiled from: DomainAutoCompleteProvider.kt */
final class DomainAutoCompleteProvider$initialize$1 extends CoroutineImpl implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Context $context;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    /* renamed from: p$ */
    private CoroutineScope f79p$;
    final /* synthetic */ DomainAutoCompleteProvider this$0;

    DomainAutoCompleteProvider$initialize$1(DomainAutoCompleteProvider domainAutoCompleteProvider, Context context, Continuation continuation) {
        this.this$0 = domainAutoCompleteProvider;
        this.$context = context;
        super(2, continuation);
    }

    public final Continuation<Unit> create(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        DomainAutoCompleteProvider$initialize$1 domainAutoCompleteProvider$initialize$1 = new DomainAutoCompleteProvider$initialize$1(this.this$0, this.$context, continuation);
        domainAutoCompleteProvider$initialize$1.f79p$ = coroutineScope;
        return domainAutoCompleteProvider$initialize$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return ((DomainAutoCompleteProvider$initialize$1) create(coroutineScope, (Continuation) continuation)).doResume(Unit.INSTANCE, null);
    }

    public final Object doResume(Object obj, Throwable th) {
        Object async$default;
        Object async$default2;
        Object await;
        DomainAutoCompleteProvider domainAutoCompleteProvider;
        List list;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                if (th == null) {
                    CoroutineScope coroutineScope = this.f79p$;
                    async$default = DeferredKt.async$default(CommonPool.INSTANCE, null, null, null, new DomainAutoCompleteProvider$initialize$1$domains$1(this, null), 14, null);
                    async$default2 = DeferredKt.async$default(CommonPool.INSTANCE, null, null, null, new DomainAutoCompleteProvider$initialize$1$customDomains$1(this, null), 14, null);
                    DomainAutoCompleteProvider domainAutoCompleteProvider2 = this.this$0;
                    this.L$0 = async$default;
                    this.L$1 = async$default2;
                    this.L$2 = domainAutoCompleteProvider2;
                    this.label = 1;
                    await = async$default.await(this);
                    if (await != coroutine_suspended) {
                        domainAutoCompleteProvider = domainAutoCompleteProvider2;
                        obj = await;
                        break;
                    }
                    return coroutine_suspended;
                }
                throw th;
            case 1:
                domainAutoCompleteProvider = (DomainAutoCompleteProvider) this.L$2;
                async$default2 = (Deferred) this.L$1;
                async$default = (Deferred) this.L$0;
                if (th != null) {
                    throw th;
                }
                break;
            case 2:
                list = (List) this.L$3;
                domainAutoCompleteProvider = (DomainAutoCompleteProvider) this.L$2;
                Deferred deferred = (Deferred) this.L$1;
                deferred = (Deferred) this.L$0;
                if (th != null) {
                    throw th;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        List list2 = (List) obj;
        this.L$0 = async$default;
        this.L$1 = async$default2;
        this.L$2 = domainAutoCompleteProvider;
        this.L$3 = list2;
        this.label = 2;
        await = async$default2.await(this);
        if (await == coroutine_suspended) {
            return coroutine_suspended;
        }
        list = list2;
        obj = await;
        domainAutoCompleteProvider.onDomainsLoaded$domains_release(list, (List) obj);
        return Unit.INSTANCE;
    }
}
