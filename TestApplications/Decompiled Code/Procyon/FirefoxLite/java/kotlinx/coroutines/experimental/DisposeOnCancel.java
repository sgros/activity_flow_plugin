// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

final class DisposeOnCancel extends CancelHandler
{
    private final DisposableHandle handle;
    
    public DisposeOnCancel(final DisposableHandle handle) {
        Intrinsics.checkParameterIsNotNull(handle, "handle");
        this.handle = handle;
    }
    
    @Override
    public void invoke(final Throwable t) {
        this.handle.dispose();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DisposeOnCancel[");
        sb.append(this.handle);
        sb.append(']');
        return sb.toString();
    }
}
