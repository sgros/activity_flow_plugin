package p008pl.droidsonroids.gif;

/* renamed from: pl.droidsonroids.gif.ConditionVariable */
class ConditionVariable {
    private volatile boolean mCondition;

    ConditionVariable() {
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void set(boolean state) {
        if (state) {
            open();
        } else {
            close();
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void open() {
        boolean old = this.mCondition;
        this.mCondition = true;
        if (!old) {
            notify();
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void close() {
        this.mCondition = false;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void block() throws InterruptedException {
        while (!this.mCondition) {
            wait();
        }
    }
}
