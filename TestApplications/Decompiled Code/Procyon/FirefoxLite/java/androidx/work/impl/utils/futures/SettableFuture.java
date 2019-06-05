// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils.futures;

import com.google.common.util.concurrent.ListenableFuture;

public final class SettableFuture<V> extends AbstractFuture<V>
{
    private SettableFuture() {
    }
    
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }
    
    public boolean set(final V v) {
        return super.set(v);
    }
    
    public boolean setException(final Throwable exception) {
        return super.setException(exception);
    }
    
    public boolean setFuture(final ListenableFuture<? extends V> future) {
        return super.setFuture(future);
    }
}
