package org.mapsforge.android.maps;

public abstract class PausableThread extends Thread {
    private boolean pausing;
    private boolean shouldPause;

    protected enum ThreadPriority {
        ABOVE_NORMAL(7),
        BELOW_NORMAL(3),
        HIGHEST(10),
        LOWEST(1),
        NORMAL(5);
        
        final int priority;

        private ThreadPriority(int priority) {
            if (priority < 1 || priority > 10) {
                throw new IllegalArgumentException("invalid priority: " + priority);
            }
            this.priority = priority;
        }
    }

    public abstract void doWork() throws InterruptedException;

    public abstract String getThreadName();

    public abstract ThreadPriority getThreadPriority();

    public abstract boolean hasWork();

    public final void awaitPausing() {
        synchronized (this) {
            while (!isInterrupted() && !isPausing()) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void interrupt() {
        synchronized (this) {
            super.interrupt();
        }
    }

    public final synchronized boolean isPausing() {
        return this.pausing;
    }

    public final synchronized void pause() {
        if (!this.shouldPause) {
            this.shouldPause = true;
            notify();
        }
    }

    public final synchronized void proceed() {
        if (this.shouldPause) {
            this.shouldPause = false;
            this.pausing = false;
            afterPause();
            notify();
        }
    }

    public final void run() {
        setName(getThreadName());
        setPriority(getThreadPriority().priority);
        while (!isInterrupted()) {
            synchronized (this) {
                while (!isInterrupted() && (this.shouldPause || !hasWork())) {
                    try {
                        if (this.shouldPause) {
                            this.pausing = true;
                        }
                        wait();
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            }
            if (isInterrupted()) {
                break;
            }
            try {
                doWork();
            } catch (InterruptedException e2) {
                interrupt();
            }
        }
        afterRun();
    }

    /* Access modifiers changed, original: protected */
    public void afterPause() {
    }

    /* Access modifiers changed, original: protected */
    public void afterRun() {
    }
}
