// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayDeque;

public abstract class SimpleDecoder<I extends DecoderInputBuffer, O extends OutputBuffer, E extends Exception> implements Decoder<I, O, E>
{
    private int availableInputBufferCount;
    private final I[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final O[] availableOutputBuffers;
    private final Thread decodeThread;
    private I dequeuedInputBuffer;
    private E exception;
    private boolean flushed;
    private final Object lock;
    private final ArrayDeque<I> queuedInputBuffers;
    private final ArrayDeque<O> queuedOutputBuffers;
    private boolean released;
    private int skippedOutputBufferCount;
    
    protected SimpleDecoder(final I[] availableInputBuffers, final O[] availableOutputBuffers) {
        this.lock = new Object();
        this.queuedInputBuffers = new ArrayDeque<I>();
        this.queuedOutputBuffers = new ArrayDeque<O>();
        this.availableInputBuffers = availableInputBuffers;
        this.availableInputBufferCount = availableInputBuffers.length;
        final int n = 0;
        for (int i = 0; i < this.availableInputBufferCount; ++i) {
            this.availableInputBuffers[i] = this.createInputBuffer();
        }
        this.availableOutputBuffers = availableOutputBuffers;
        this.availableOutputBufferCount = availableOutputBuffers.length;
        for (int j = n; j < this.availableOutputBufferCount; ++j) {
            this.availableOutputBuffers[j] = this.createOutputBuffer();
        }
        (this.decodeThread = new Thread() {
            @Override
            public void run() {
                SimpleDecoder.this.run();
            }
        }).start();
    }
    
    private boolean canDecodeBuffer() {
        return !this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0;
    }
    
    private boolean decode() throws InterruptedException {
        final Object lock = this.lock;
        synchronized (lock) {
            while (!this.released && !this.canDecodeBuffer()) {
                this.lock.wait();
            }
            if (this.released) {
                return false;
            }
            final DecoderInputBuffer decoderInputBuffer = this.queuedInputBuffers.removeFirst();
            final O[] availableOutputBuffers = this.availableOutputBuffers;
            final int availableOutputBufferCount = this.availableOutputBufferCount - 1;
            this.availableOutputBufferCount = availableOutputBufferCount;
            final OutputBuffer e = availableOutputBuffers[availableOutputBufferCount];
            final boolean flushed = this.flushed;
            this.flushed = false;
            // monitorexit(lock)
            if (decoderInputBuffer.isEndOfStream()) {
                e.addFlag(4);
            }
            else {
                if (decoderInputBuffer.isDecodeOnly()) {
                    e.addFlag(Integer.MIN_VALUE);
                }
                try {
                    this.exception = this.decode((I)decoderInputBuffer, (O)e, flushed);
                }
                catch (OutOfMemoryError lock) {
                    this.exception = this.createUnexpectedDecodeException((Throwable)lock);
                }
                catch (RuntimeException lock) {
                    this.exception = this.createUnexpectedDecodeException((Throwable)lock);
                }
                if (this.exception != null) {
                    synchronized (this.lock) {
                        return false;
                    }
                }
            }
            synchronized (this.lock) {
                if (this.flushed) {
                    e.release();
                }
                else if (e.isDecodeOnly()) {
                    ++this.skippedOutputBufferCount;
                    e.release();
                }
                else {
                    e.skippedOutputBufferCount = this.skippedOutputBufferCount;
                    this.skippedOutputBufferCount = 0;
                    this.queuedOutputBuffers.addLast((O)e);
                }
                this.releaseInputBufferInternal((I)decoderInputBuffer);
                return true;
            }
        }
    }
    
    private void maybeNotifyDecodeLoop() {
        if (this.canDecodeBuffer()) {
            this.lock.notify();
        }
    }
    
    private void maybeThrowException() throws E, Exception {
        final Exception exception = this.exception;
        if (exception == null) {
            return;
        }
        throw exception;
    }
    
    private void releaseInputBufferInternal(final I n) {
        n.clear();
        this.availableInputBuffers[this.availableInputBufferCount++] = n;
    }
    
    private void releaseOutputBufferInternal(final O o) {
        o.clear();
        this.availableOutputBuffers[this.availableOutputBufferCount++] = o;
    }
    
    private void run() {
        try {
            while (this.decode()) {}
        }
        catch (InterruptedException cause) {
            throw new IllegalStateException(cause);
        }
    }
    
    protected abstract I createInputBuffer();
    
    protected abstract O createOutputBuffer();
    
    protected abstract E createUnexpectedDecodeException(final Throwable p0);
    
    protected abstract E decode(final I p0, final O p1, final boolean p2);
    
    @Override
    public final I dequeueInputBuffer() throws E, Exception {
        synchronized (this.lock) {
            this.maybeThrowException();
            Assertions.checkState(this.dequeuedInputBuffer == null);
            DecoderInputBuffer dequeuedInputBuffer;
            if (this.availableInputBufferCount == 0) {
                dequeuedInputBuffer = null;
            }
            else {
                final I[] availableInputBuffers = this.availableInputBuffers;
                final int availableInputBufferCount = this.availableInputBufferCount - 1;
                this.availableInputBufferCount = availableInputBufferCount;
                dequeuedInputBuffer = availableInputBuffers[availableInputBufferCount];
            }
            return this.dequeuedInputBuffer = (I)dequeuedInputBuffer;
        }
    }
    
    @Override
    public final O dequeueOutputBuffer() throws E, Exception {
        synchronized (this.lock) {
            this.maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
                return null;
            }
            return this.queuedOutputBuffers.removeFirst();
        }
    }
    
    @Override
    public final void flush() {
        synchronized (this.lock) {
            this.flushed = true;
            this.skippedOutputBufferCount = 0;
            if (this.dequeuedInputBuffer != null) {
                this.releaseInputBufferInternal(this.dequeuedInputBuffer);
                this.dequeuedInputBuffer = null;
            }
            while (!this.queuedInputBuffers.isEmpty()) {
                this.releaseInputBufferInternal(this.queuedInputBuffers.removeFirst());
            }
            while (!this.queuedOutputBuffers.isEmpty()) {
                this.queuedOutputBuffers.removeFirst().release();
            }
        }
    }
    
    @Override
    public final void queueInputBuffer(final I e) throws E, Exception {
        synchronized (this.lock) {
            this.maybeThrowException();
            Assertions.checkArgument(e == this.dequeuedInputBuffer);
            this.queuedInputBuffers.addLast(e);
            this.maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
        }
    }
    
    @Override
    public void release() {
        synchronized (this.lock) {
            this.released = true;
            this.lock.notify();
            // monitorexit(this.lock)
            try {
                this.decodeThread.join();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    protected void releaseOutputBuffer(final O o) {
        synchronized (this.lock) {
            this.releaseOutputBufferInternal(o);
            this.maybeNotifyDecodeLoop();
        }
    }
    
    protected final void setInitialInputBufferSize(final int n) {
        final int availableInputBufferCount = this.availableInputBufferCount;
        final int length = this.availableInputBuffers.length;
        int i = 0;
        Assertions.checkState(availableInputBufferCount == length);
        for (I[] availableInputBuffers = this.availableInputBuffers; i < availableInputBuffers.length; ++i) {
            availableInputBuffers[i].ensureSpaceForWrite(n);
        }
    }
}
