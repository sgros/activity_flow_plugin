// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

public interface ContinuationInterceptor extends Element
{
    public static final Key Key = ContinuationInterceptor.Key.$$INSTANCE;
    
     <T> Continuation<T> interceptContinuation(final Continuation<? super T> p0);
    
    public static final class Key implements CoroutineContext.Key<ContinuationInterceptor>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        static {
            $$INSTANCE = new Key();
        }
        
        private Key() {
        }
    }
}
