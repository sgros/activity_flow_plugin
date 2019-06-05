package kotlinx.coroutines.experimental;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CommonPool.kt */
public final class CommonPool extends CoroutineDispatcher {
    public static final CommonPool INSTANCE = new CommonPool();
    private static final int parallelism;
    private static volatile Executor pool;
    private static boolean usePrivatePool;

    public String toString() {
        return "CommonPool";
    }

    static {
        String property;
        int coerceAtLeast;
        CommonPool commonPool = INSTANCE;
        try {
            property = System.getProperty("kotlinx.coroutines.default.parallelism");
        } catch (Throwable unused) {
            property = null;
        }
        if (property == null) {
            coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
        } else {
            Integer toIntOrNull = StringsKt__StringNumberConversionsKt.toIntOrNull(property);
            if (toIntOrNull == null || toIntOrNull.intValue() < 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected positive number in kotlinx.coroutines.default.parallelism, but has ");
                stringBuilder.append(property);
                throw new IllegalStateException(stringBuilder.toString().toString());
            }
            coerceAtLeast = toIntOrNull.intValue();
        }
        parallelism = coerceAtLeast;
    }

    private CommonPool() {
    }

    private final ExecutorService createPool() {
        if (System.getSecurityManager() != null) {
            return createPlainPool();
        }
        Class cls;
        ExecutorService executorService = null;
        try {
            cls = Class.forName("java.util.concurrent.ForkJoinPool");
        } catch (Throwable unused) {
            cls = null;
        }
        if (cls == null) {
            return createPlainPool();
        }
        if (!usePrivatePool) {
            ExecutorService executorService2;
            try {
                Method method = cls.getMethod("commonPool", new Class[0]);
                Object invoke = method != null ? method.invoke(null, new Object[0]) : null;
                if (!(invoke instanceof ExecutorService)) {
                    invoke = null;
                }
                executorService2 = (ExecutorService) invoke;
            } catch (Throwable unused2) {
                executorService2 = null;
            }
            if (executorService2 != null) {
                return executorService2;
            }
        }
        try {
            Object newInstance = cls.getConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(parallelism)});
            if (!(newInstance instanceof ExecutorService)) {
                newInstance = null;
            }
            executorService = (ExecutorService) newInstance;
        } catch (Throwable unused3) {
        }
        if (executorService != null) {
            return executorService;
        }
        return createPlainPool();
    }

    private final ExecutorService createPlainPool() {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(parallelism, new CommonPool$createPlainPool$1(new AtomicInteger()));
        Intrinsics.checkExpressionValueIsNotNull(newFixedThreadPool, "Executors.newFixedThread…Daemon = true }\n        }");
        return newFixedThreadPool;
    }

    private final synchronized Executor getOrCreatePoolSync() {
        Executor executor;
        executor = pool;
        if (executor == null) {
            ExecutorService createPool = createPool();
            pool = createPool;
            executor = createPool;
        }
        return executor;
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        try {
            Executor executor = pool;
            if (executor == null) {
                executor = getOrCreatePoolSync();
            }
            executor.execute(TimeSourceKt.getTimeSource().trackTask(runnable));
        } catch (RejectedExecutionException unused) {
            TimeSourceKt.getTimeSource().unTrackTask();
            DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(runnable);
        }
    }
}
