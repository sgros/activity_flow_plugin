// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;

public interface CoroutineExceptionHandler extends Element
{
    public static final Key Key = CoroutineExceptionHandler.Key.$$INSTANCE;
    
    void handleException(final CoroutineContext p0, final Throwable p1);
    
    public static final class Key implements CoroutineContext.Key<CoroutineExceptionHandler>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        static {
            $$INSTANCE = new Key();
        }
        
        private Key() {
        }
    }
}
