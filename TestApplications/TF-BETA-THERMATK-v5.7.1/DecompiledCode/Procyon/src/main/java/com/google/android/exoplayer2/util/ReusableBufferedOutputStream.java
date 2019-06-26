// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

public final class ReusableBufferedOutputStream extends BufferedOutputStream
{
    private boolean closed;
    
    public ReusableBufferedOutputStream(final OutputStream out) {
        super(out);
    }
    
    public ReusableBufferedOutputStream(final OutputStream out, final int size) {
        super(out, size);
    }
    
    @Override
    public void close() throws IOException {
        this.closed = true;
        Throwable t = null;
        try {
            this.flush();
            t = null;
        }
        catch (Throwable t4) {}
        Throwable t2;
        try {
            super.out.close();
            t2 = t;
        }
        catch (Throwable t3) {
            t2 = t;
            if (t == null) {
                t2 = t3;
            }
        }
        if (t2 == null) {
            return;
        }
        Util.sneakyThrow(t2);
        throw null;
    }
    
    public void reset(final OutputStream out) {
        Assertions.checkState(this.closed);
        super.out = out;
        super.count = 0;
        this.closed = false;
    }
}
