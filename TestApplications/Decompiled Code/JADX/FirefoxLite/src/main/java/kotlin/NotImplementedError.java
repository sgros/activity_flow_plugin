package kotlin;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: Standard.kt */
public final class NotImplementedError extends Error {
    public NotImplementedError() {
        this(null, 1, null);
    }

    public NotImplementedError(String str) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        super(str);
    }

    public /* synthetic */ NotImplementedError(String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = "An operation is not implemented.";
        }
        this(str);
    }
}
