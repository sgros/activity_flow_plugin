package kotlinx.coroutines.experimental;

import kotlin.jvm.functions.Function1;

public abstract class CancelHandlerBase implements Function1 {
   public abstract void invoke(Throwable var1);
}
