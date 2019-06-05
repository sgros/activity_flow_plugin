package kotlinx.coroutines.experimental;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: CommonPool.kt */
final class CommonPool$createPlainPool$1 implements ThreadFactory {
    final /* synthetic */ AtomicInteger $threadId;

    CommonPool$createPlainPool$1(AtomicInteger atomicInteger) {
        this.$threadId = atomicInteger;
    }

    public final Thread newThread(Runnable runnable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CommonPool-worker-");
        stringBuilder.append(this.$threadId.incrementAndGet());
        Thread thread = new Thread(runnable, stringBuilder.toString());
        thread.setDaemon(true);
        return thread;
    }
}
