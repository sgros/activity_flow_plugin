// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.experimental.CoroutineContext;

public interface Job extends Element
{
    public static final Key Key = Job.Key.$$INSTANCE;
    
    DisposableHandle attachChild(final Job p0);
    
    boolean cancel(final Throwable p0);
    
    CancellationException getCancellationException();
    
    DisposableHandle invokeOnCompletion(final boolean p0, final boolean p1, final Function1<? super Throwable, Unit> p2);
    
    boolean isActive();
    
    boolean start();
    
    public static final class DefaultImpls
    {
        public static <R> R fold(final Job job, final R r, final Function2<? super R, ? super Element, ? extends R> function2) {
            Intrinsics.checkParameterIsNotNull(function2, "operation");
            return Element.DefaultImpls.fold((CoroutineContext.Element)job, r, function2);
        }
        
        public static <E extends Element> E get(final Job job, final CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return Element.DefaultImpls.get((CoroutineContext.Element)job, key);
        }
        
        public static CoroutineContext minusKey(final Job job, final CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return Element.DefaultImpls.minusKey((CoroutineContext.Element)job, key);
        }
        
        public static CoroutineContext plus(final Job job, final CoroutineContext coroutineContext) {
            Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
            return Element.DefaultImpls.plus((CoroutineContext.Element)job, coroutineContext);
        }
    }
    
    public static final class Key implements CoroutineContext.Key<Job>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        static {
            $$INSTANCE = new Key();
            final CoroutineExceptionHandler.Key key = CoroutineExceptionHandler.Key;
        }
        
        private Key() {
        }
    }
}
