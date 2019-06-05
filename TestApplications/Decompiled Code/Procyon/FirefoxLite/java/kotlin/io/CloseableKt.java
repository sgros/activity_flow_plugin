// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.io;

import java.io.Closeable;

public final class CloseableKt
{
    public static final void closeFinally(final Closeable closeable, final Throwable t) {
        if (closeable != null) {
            if (t == null) {
                closeable.close();
            }
            else {
                try {
                    closeable.close();
                }
                catch (Throwable t2) {
                    ExceptionsKt__ExceptionsKt.addSuppressed(t, t2);
                }
            }
        }
    }
}
