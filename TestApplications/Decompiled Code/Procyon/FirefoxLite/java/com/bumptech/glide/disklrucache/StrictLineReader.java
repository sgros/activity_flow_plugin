// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.disklrucache;

import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.io.Closeable;

class StrictLineReader implements Closeable
{
    private byte[] buf;
    private final Charset charset;
    private int end;
    private final InputStream in;
    private int pos;
    
    public StrictLineReader(final InputStream in, final int n, final Charset charset) {
        if (in == null || charset == null) {
            throw new NullPointerException();
        }
        if (n < 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        if (charset.equals(Util.US_ASCII)) {
            this.in = in;
            this.charset = charset;
            this.buf = new byte[n];
            return;
        }
        throw new IllegalArgumentException("Unsupported encoding");
    }
    
    public StrictLineReader(final InputStream inputStream, final Charset charset) {
        this(inputStream, 8192, charset);
    }
    
    private void fillBuf() throws IOException {
        final int read = this.in.read(this.buf, 0, this.buf.length);
        if (read != -1) {
            this.pos = 0;
            this.end = read;
            return;
        }
        throw new EOFException();
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this.in) {
            if (this.buf != null) {
                this.buf = null;
                this.in.close();
            }
        }
    }
    
    public boolean hasUnterminatedLine() {
        return this.end == -1;
    }
    
    public String readLine() throws IOException {
        synchronized (this.in) {
            if (this.buf != null) {
                if (this.pos >= this.end) {
                    this.fillBuf();
                }
                for (int i = this.pos; i != this.end; ++i) {
                    if (this.buf[i] == 10) {
                        int n = 0;
                        Label_0086: {
                            if (i != this.pos) {
                                final byte[] buf = this.buf;
                                n = i - 1;
                                if (buf[n] == 13) {
                                    break Label_0086;
                                }
                            }
                            n = i;
                        }
                        final String s = new String(this.buf, this.pos, n - this.pos, this.charset.name());
                        this.pos = i + 1;
                        return s;
                    }
                }
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(this.end - this.pos + 80) {
                    @Override
                    public String toString() {
                        int count;
                        if (this.count > 0 && this.buf[this.count - 1] == 13) {
                            count = this.count - 1;
                        }
                        else {
                            count = this.count;
                        }
                        try {
                            return new String(this.buf, 0, count, StrictLineReader.this.charset.name());
                        }
                        catch (UnsupportedEncodingException detailMessage) {
                            throw new AssertionError((Object)detailMessage);
                        }
                    }
                };
                int j = 0;
            Block_10:
                while (true) {
                    byteArrayOutputStream.write(this.buf, this.pos, this.end - this.pos);
                    this.end = -1;
                    this.fillBuf();
                    for (j = this.pos; j != this.end; ++j) {
                        if (this.buf[j] == 10) {
                            break Block_10;
                        }
                    }
                }
                if (j != this.pos) {
                    byteArrayOutputStream.write(this.buf, this.pos, j - this.pos);
                }
                this.pos = j + 1;
                return byteArrayOutputStream.toString();
            }
            throw new IOException("LineReader is closed");
        }
    }
}
