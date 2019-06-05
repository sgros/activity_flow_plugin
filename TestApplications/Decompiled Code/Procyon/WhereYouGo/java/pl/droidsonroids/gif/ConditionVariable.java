// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

class ConditionVariable
{
    private volatile boolean mCondition;
    
    void block() throws InterruptedException {
        synchronized (this) {
            while (!this.mCondition) {
                this.wait();
            }
        }
    }
    // monitorexit(this)
    
    void close() {
        synchronized (this) {
            this.mCondition = false;
        }
    }
    
    void open() {
        synchronized (this) {
            final boolean mCondition = this.mCondition;
            this.mCondition = true;
            if (!mCondition) {
                this.notify();
            }
        }
    }
    
    void set(final boolean b) {
        // monitorenter(this)
        Label_0013: {
            if (!b) {
                break Label_0013;
            }
            try {
                this.open();
                return;
                this.close();
            }
            finally {
            }
            // monitorexit(this)
        }
    }
}
