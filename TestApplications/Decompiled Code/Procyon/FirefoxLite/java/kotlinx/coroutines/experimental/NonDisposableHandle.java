// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

public final class NonDisposableHandle implements DisposableHandle
{
    public static final NonDisposableHandle INSTANCE;
    
    static {
        INSTANCE = new NonDisposableHandle();
    }
    
    private NonDisposableHandle() {
    }
    
    @Override
    public void dispose() {
    }
    
    @Override
    public String toString() {
        return "NonDisposableHandle";
    }
}
