package org.osmdroid.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class GarbageCollector {
    private final Runnable mAction;
    private final AtomicBoolean mRunning = new AtomicBoolean(false);

    /* renamed from: org.osmdroid.util.GarbageCollector$1 */
    class C02631 implements Runnable {
        C02631() {
        }

        public void run() {
            try {
                GarbageCollector.this.mAction.run();
            } finally {
                GarbageCollector.this.mRunning.set(false);
            }
        }
    }

    public GarbageCollector(Runnable runnable) {
        this.mAction = runnable;
    }

    /* renamed from: gc */
    public boolean mo4115gc() {
        if (this.mRunning.getAndSet(true)) {
            return false;
        }
        Thread thread = new Thread(new C02631());
        thread.setPriority(1);
        thread.start();
        return true;
    }

    public boolean isRunning() {
        return this.mRunning.get();
    }
}
