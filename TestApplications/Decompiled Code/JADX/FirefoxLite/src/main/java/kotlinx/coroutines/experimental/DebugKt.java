package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Debug.kt */
public final class DebugKt {
    public static final String getHexAddress(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "$receiver");
        String toHexString = Integer.toHexString(System.identityHashCode(obj));
        Intrinsics.checkExpressionValueIsNotNull(toHexString, "Integer.toHexString(System.identityHashCode(this))");
        return toHexString;
    }

    public static final String toDebugString(Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        if (continuation instanceof DispatchedContinuation) {
            return continuation.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(continuation.getClass().getName());
        stringBuilder.append('@');
        stringBuilder.append(getHexAddress(continuation));
        return stringBuilder.toString();
    }

    public static final String getClassSimpleName(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "$receiver");
        String simpleName = obj.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }
}
