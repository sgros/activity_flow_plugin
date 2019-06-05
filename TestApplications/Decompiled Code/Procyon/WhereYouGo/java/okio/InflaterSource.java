// 
// Decompiled by Procyon v0.5.34
// 

package okio;

import java.util.zip.DataFormatException;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.Inflater;

public final class InflaterSource implements Source
{
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;
    
    InflaterSource(final BufferedSource source, final Inflater inflater) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (inflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.source = source;
        this.inflater = inflater;
    }
    
    public InflaterSource(final Source source, final Inflater inflater) {
        this(Okio.buffer(source), inflater);
    }
    
    private void releaseInflatedBytes() throws IOException {
        if (this.bufferBytesHeldByInflater != 0) {
            final int n = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
            this.bufferBytesHeldByInflater -= n;
            this.source.skip(n);
        }
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.inflater.end();
            this.closed = true;
            this.source.close();
        }
    }
    
    @Override
    public long read(final Buffer buffer, long lng) throws IOException {
        final long n = 0L;
        if (lng < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + lng);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (lng == 0L) {
            lng = n;
        }
        else {
            while (true) {
                final boolean refill = this.refill();
                try {
                    final Segment writableSegment = buffer.writableSegment(1);
                    final int inflate = this.inflater.inflate(writableSegment.data, writableSegment.limit, 8192 - writableSegment.limit);
                    if (inflate > 0) {
                        writableSegment.limit += inflate;
                        buffer.size += inflate;
                        lng = inflate;
                        break;
                    }
                    if (this.inflater.finished() || this.inflater.needsDictionary()) {
                        this.releaseInflatedBytes();
                        if (writableSegment.pos == writableSegment.limit) {
                            buffer.head = writableSegment.pop();
                            SegmentPool.recycle(writableSegment);
                        }
                        lng = -1L;
                        break;
                    }
                    if (refill) {
                        throw new EOFException("source exhausted prematurely");
                    }
                    continue;
                }
                catch (DataFormatException cause) {
                    throw new IOException(cause);
                }
            }
        }
        return lng;
    }
    
    public boolean refill() throws IOException {
        boolean b = false;
        if (this.inflater.needsInput()) {
            this.releaseInflatedBytes();
            if (this.inflater.getRemaining() != 0) {
                throw new IllegalStateException("?");
            }
            if (this.source.exhausted()) {
                b = true;
            }
            else {
                final Segment head = this.source.buffer().head;
                this.bufferBytesHeldByInflater = head.limit - head.pos;
                this.inflater.setInput(head.data, head.pos, this.bufferBytesHeldByInflater);
            }
        }
        return b;
    }
    
    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}
