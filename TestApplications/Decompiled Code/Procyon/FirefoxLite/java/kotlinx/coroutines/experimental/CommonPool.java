// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.concurrent.RejectedExecutionException;
import kotlin.coroutines.experimental.CoroutineContext;
import java.lang.reflect.Method;
import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;

public final class CommonPool extends CoroutineDispatcher
{
    public static final CommonPool INSTANCE;
    private static final int parallelism;
    private static volatile Executor pool;
    private static boolean usePrivatePool;
    
    static {
        INSTANCE = new CommonPool();
        final CommonPool instance = CommonPool.INSTANCE;
        String property;
        try {
            property = System.getProperty("kotlinx.coroutines.default.parallelism");
        }
        catch (Throwable t) {
            property = null;
        }
        int parallelism2;
        if (property == null) {
            parallelism2 = RangesKt___RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
        }
        else {
            final Integer intOrNull = StringsKt__StringNumberConversionsKt.toIntOrNull(property);
            if (intOrNull == null || intOrNull < 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected positive number in kotlinx.coroutines.default.parallelism, but has ");
                sb.append(property);
                throw new IllegalStateException(sb.toString().toString());
            }
            parallelism2 = intOrNull;
        }
        parallelism = parallelism2;
    }
    
    private CommonPool() {
    }
    
    private final ExecutorService createPlainPool() {
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(CommonPool.parallelism, (ThreadFactory)new CommonPool$createPlainPool.CommonPool$createPlainPool$1(new AtomicInteger()));
        Intrinsics.checkExpressionValueIsNotNull(fixedThreadPool, "Executors.newFixedThread\u2026Daemon = true }\n        }");
        return fixedThreadPool;
    }
    
    private final ExecutorService createPool() {
        if (System.getSecurityManager() != null) {
            return this.createPlainPool();
        }
        final ExecutorService executorService = null;
        Class<?> forName;
        try {
            forName = Class.forName("java.util.concurrent.ForkJoinPool");
        }
        catch (Throwable t) {
            forName = null;
        }
        Label_0154: {
            if (forName == null) {
                break Label_0154;
            }
            if (!CommonPool.usePrivatePool) {
                ExecutorService executorService2;
                try {
                    final Method method = forName.getMethod("commonPool", (Class<?>[])new Class[0]);
                    Object invoke;
                    if (method != null) {
                        invoke = method.invoke(null, new Object[0]);
                    }
                    else {
                        invoke = null;
                    }
                    Object o = invoke;
                    if (!(invoke instanceof ExecutorService)) {
                        o = null;
                    }
                    executorService2 = (ExecutorService)o;
                }
                catch (Throwable t2) {
                    executorService2 = null;
                }
                if (executorService2 != null) {
                    return executorService2;
                }
            }
            while (true) {
                try {
                    Object instance;
                    if (!((instance = forName.getConstructor(Integer.TYPE).newInstance(CommonPool.parallelism)) instanceof ExecutorService)) {
                        instance = null;
                    }
                    final ExecutorService executorService3 = (ExecutorService)instance;
                    if (executorService3 != null) {
                        return executorService3;
                    }
                    return this.createPlainPool();
                    return this.createPlainPool();
                }
                catch (Throwable t3) {
                    final ExecutorService executorService3 = executorService;
                    continue;
                }
                break;
            }
        }
    }
    
    private final Executor getOrCreatePoolSync() {
        synchronized (this) {
            Executor pool = CommonPool.pool;
            if (pool == null) {
                final ExecutorService pool2 = this.createPool();
                CommonPool.pool = pool2;
                pool = pool2;
            }
            return pool;
        }
    }
    
    @Override
    public void dispatch(final CoroutineContext coroutineContext, final Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        try {
            Executor executor = CommonPool.pool;
            if (executor == null) {
                executor = this.getOrCreatePoolSync();
            }
            executor.execute(TimeSourceKt.getTimeSource().trackTask(runnable));
        }
        catch (RejectedExecutionException ex) {
            TimeSourceKt.getTimeSource().unTrackTask();
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(runnable);
        }
    }
    
    @Override
    public String toString() {
        return "CommonPool";
    }
}
