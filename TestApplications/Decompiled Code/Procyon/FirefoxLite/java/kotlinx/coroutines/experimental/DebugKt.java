// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

public final class DebugKt
{
    public static final String getClassSimpleName(final Object o) {
        Intrinsics.checkParameterIsNotNull(o, "$receiver");
        final String simpleName = o.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }
    
    public static final String getHexAddress(final Object o) {
        Intrinsics.checkParameterIsNotNull(o, "$receiver");
        final String hexString = Integer.toHexString(System.identityHashCode(o));
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(System.identityHashCode(this))");
        return hexString;
    }
    
    public static final String toDebugString(final Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        String s;
        if (continuation instanceof DispatchedContinuation) {
            s = continuation.toString();
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append(continuation.getClass().getName());
            sb.append('@');
            sb.append(getHexAddress(continuation));
            s = sb.toString();
        }
        return s;
    }
}
