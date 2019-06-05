package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: CompletionHandler.kt */
public abstract class CancelHandlerBase implements Function1<Throwable, Unit> {
    public abstract void invoke(Throwable th);
}
