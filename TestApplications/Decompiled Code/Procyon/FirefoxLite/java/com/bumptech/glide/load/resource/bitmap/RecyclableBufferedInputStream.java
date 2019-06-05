// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.io.IOException;
import java.io.InputStream;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;

public class RecyclableBufferedInputStream extends FilterInputStream
{
    private volatile byte[] buf;
    private final ArrayPool byteArrayPool;
    private int count;
    private int marklimit;
    private int markpos;
    private int pos;
    
    public RecyclableBufferedInputStream(final InputStream inputStream, final ArrayPool arrayPool) {
        this(inputStream, arrayPool, 65536);
    }
    
    RecyclableBufferedInputStream(final InputStream in, final ArrayPool byteArrayPool, final int n) {
        super(in);
        this.markpos = -1;
        this.byteArrayPool = byteArrayPool;
        this.buf = byteArrayPool.get(n, byte[].class);
    }
    
    private int fillbuf(final InputStream inputStream, final byte[] b) throws IOException {
        if (this.markpos != -1 && this.pos - this.markpos < this.marklimit) {
            byte[] array;
            if (this.markpos == 0 && this.marklimit > b.length && this.count == b.length) {
                int marklimit;
                if ((marklimit = b.length * 2) > this.marklimit) {
                    marklimit = this.marklimit;
                }
                array = this.byteArrayPool.get(marklimit, byte[].class);
                System.arraycopy(b, 0, array, 0, b.length);
                this.buf = array;
                this.byteArrayPool.put(b, byte[].class);
            }
            else {
                array = b;
                if (this.markpos > 0) {
                    System.arraycopy(b, this.markpos, b, 0, b.length - this.markpos);
                    array = b;
                }
            }
            this.pos -= this.markpos;
            this.markpos = 0;
            this.count = 0;
            final int read = inputStream.read(array, this.pos, array.length - this.pos);
            int pos;
            if (read <= 0) {
                pos = this.pos;
            }
            else {
                pos = this.pos + read;
            }
            this.count = pos;
            return read;
        }
        final int read2 = inputStream.read(b);
        if (read2 > 0) {
            this.markpos = -1;
            this.pos = 0;
            this.count = read2;
        }
        return read2;
    }
    
    private static IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }
    
    @Override
    public int available() throws IOException {
        synchronized (this) {
            final InputStream in = this.in;
            if (this.buf != null && in != null) {
                final int count = this.count;
                final int pos = this.pos;
                final int available = in.available();
                // monitorexit(this)
                return count - pos + available;
            }
            throw streamClosed();
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf, byte[].class);
            this.buf = null;
        }
        final InputStream in = this.in;
        this.in = null;
        if (in != null) {
            in.close();
        }
    }
    
    public void fixMarkLimit() {
        synchronized (this) {
            this.marklimit = this.buf.length;
        }
    }
    
    @Override
    public void mark(final int b) {
        synchronized (this) {
            this.marklimit = Math.max(this.marklimit, b);
            this.markpos = this.pos;
        }
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public int read() throws IOException {
        synchronized (this) {
            final byte[] buf = this.buf;
            final InputStream in = this.in;
            if (buf == null || in == null) {
                throw streamClosed();
            }
            if (this.pos >= this.count && this.fillbuf(in, buf) == -1) {
                return -1;
            }
            byte[] buf2;
            if ((buf2 = buf) != this.buf) {
                buf2 = this.buf;
                if (buf2 == null) {
                    throw streamClosed();
                }
            }
            if (this.count - this.pos > 0) {
                final byte b = buf2[this.pos++];
                // monitorexit(this)
                return b & 0xFF;
            }
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] b, int len, final int n) throws IOException {
        synchronized (this) {
            byte[] buf = this.buf;
            if (buf == null) {
                throw streamClosed();
            }
            if (n == 0) {
                return 0;
            }
            final InputStream in = this.in;
            if (in == null) {
                throw streamClosed();
            }
            int off;
            if (this.pos < this.count) {
                int n2;
                if (this.count - this.pos >= n) {
                    n2 = n;
                }
                else {
                    n2 = this.count - this.pos;
                }
                System.arraycopy(buf, this.pos, b, len, n2);
                this.pos += n2;
                if (n2 == n || in.available() == 0) {
                    return n2;
                }
                final int n3 = len + n2;
                len = n - n2;
                off = n3;
            }
            else {
                off = len;
                len = n;
            }
            while (true) {
                final int markpos = this.markpos;
                int n4 = -1;
                int read;
                if (markpos == -1 && len >= buf.length) {
                    if ((read = in.read(b, off, len)) == -1) {
                        if (len != n) {
                            n4 = n - len;
                        }
                        return n4;
                    }
                }
                else {
                    if (this.fillbuf(in, buf) == -1) {
                        if (len != n) {
                            n4 = n - len;
                        }
                        return n4;
                    }
                    byte[] buf2;
                    if ((buf2 = buf) != this.buf) {
                        buf2 = this.buf;
                        if (buf2 == null) {
                            throw streamClosed();
                        }
                    }
                    int n5;
                    if (this.count - this.pos >= len) {
                        n5 = len;
                    }
                    else {
                        n5 = this.count - this.pos;
                    }
                    System.arraycopy(buf2, this.pos, b, off, n5);
                    this.pos += n5;
                    read = n5;
                    buf = buf2;
                }
                len -= read;
                if (len == 0) {
                    return n;
                }
                if (in.available() == 0) {
                    // monitorexit(this)
                    return n - len;
                }
                off += read;
            }
        }
    }
    
    public void release() {
        synchronized (this) {
            if (this.buf != null) {
                this.byteArrayPool.put(this.buf, byte[].class);
                this.buf = null;
            }
        }
    }
    
    @Override
    public void reset() throws IOException {
        synchronized (this) {
            if (this.buf == null) {
                throw new IOException("Stream is closed");
            }
            if (-1 != this.markpos) {
                this.pos = this.markpos;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Mark has been invalidated, pos: ");
            sb.append(this.pos);
            sb.append(" markLimit: ");
            sb.append(this.marklimit);
            throw new InvalidMarkException(sb.toString());
        }
    }
    
    @Override
    public long skip(long skip) throws IOException {
        synchronized (this) {
            final byte[] buf = this.buf;
            final InputStream in = this.in;
            if (buf == null) {
                throw streamClosed();
            }
            if (skip < 1L) {
                return 0L;
            }
            if (in == null) {
                throw streamClosed();
            }
            if (this.count - this.pos >= skip) {
                this.pos += (int)skip;
                return skip;
            }
            final long n = this.count - this.pos;
            this.pos = this.count;
            if (this.markpos == -1 || skip > this.marklimit) {
                skip = in.skip(skip - n);
                // monitorexit(this)
                return n + skip;
            }
            if (this.fillbuf(in, buf) == -1) {
                return n;
            }
            final long n2 = this.count - this.pos;
            final long n3 = skip - n;
            if (n2 >= n3) {
                this.pos += (int)n3;
                return skip;
            }
            skip = this.count;
            final long n4 = this.pos;
            this.pos = this.count;
            // monitorexit(this)
            return n + skip - n4;
        }
    }
    
    public static class InvalidMarkException extends IOException
    {
        public InvalidMarkException(final String message) {
            super(message);
        }
    }
}
