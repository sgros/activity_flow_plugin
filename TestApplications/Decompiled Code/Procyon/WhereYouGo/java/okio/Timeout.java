// 
// Decompiled by Procyon v0.5.34
// 

package okio;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class Timeout
{
    public static final Timeout NONE;
    private long deadlineNanoTime;
    private boolean hasDeadline;
    private long timeoutNanos;
    
    static {
        NONE = new Timeout() {
            @Override
            public Timeout deadlineNanoTime(final long n) {
                return this;
            }
            
            @Override
            public void throwIfReached() throws IOException {
            }
            
            @Override
            public Timeout timeout(final long n, final TimeUnit timeUnit) {
                return this;
            }
        };
    }
    
    public Timeout clearDeadline() {
        this.hasDeadline = false;
        return this;
    }
    
    public Timeout clearTimeout() {
        this.timeoutNanos = 0L;
        return this;
    }
    
    public final Timeout deadline(final long n, final TimeUnit timeUnit) {
        if (n <= 0L) {
            throw new IllegalArgumentException("duration <= 0: " + n);
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        return this.deadlineNanoTime(System.nanoTime() + timeUnit.toNanos(n));
    }
    
    public long deadlineNanoTime() {
        if (!this.hasDeadline) {
            throw new IllegalStateException("No deadline");
        }
        return this.deadlineNanoTime;
    }
    
    public Timeout deadlineNanoTime(final long deadlineNanoTime) {
        this.hasDeadline = true;
        this.deadlineNanoTime = deadlineNanoTime;
        return this;
    }
    
    public boolean hasDeadline() {
        return this.hasDeadline;
    }
    
    public void throwIfReached() throws IOException {
        if (Thread.interrupted()) {
            throw new InterruptedIOException("thread interrupted");
        }
        if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0L) {
            throw new InterruptedIOException("deadline reached");
        }
    }
    
    public Timeout timeout(final long n, final TimeUnit timeUnit) {
        if (n < 0L) {
            throw new IllegalArgumentException("timeout < 0: " + n);
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        this.timeoutNanos = timeUnit.toNanos(n);
        return this;
    }
    
    public long timeoutNanos() {
        return this.timeoutNanos;
    }
    
    public final void waitUntilNotified(Object o) throws InterruptedIOException {
        while (true) {
            while (true) {
                boolean hasDeadline = false;
                long nanoTime = 0L;
                Label_0121: {
                    try {
                        hasDeadline = this.hasDeadline();
                        long a = this.timeoutNanos();
                        if (!hasDeadline && a == 0L) {
                            o.wait();
                        }
                        else {
                            nanoTime = System.nanoTime();
                            if (!hasDeadline || a == 0L) {
                                break Label_0121;
                            }
                            a = Math.min(a, this.deadlineNanoTime() - nanoTime);
                            long n = 0L;
                            if (a > 0L) {
                                final long timeout = a / 1000000L;
                                o.wait(timeout, (int)(a - 1000000L * timeout));
                                n = System.nanoTime() - nanoTime;
                            }
                            if (n >= a) {
                                o = new InterruptedIOException("timeout");
                                throw o;
                            }
                        }
                        return;
                    }
                    catch (InterruptedException ex) {
                        throw new InterruptedIOException("interrupted");
                    }
                }
                if (hasDeadline) {
                    final long a = this.deadlineNanoTime() - nanoTime;
                    continue;
                }
                continue;
            }
        }
    }
}
