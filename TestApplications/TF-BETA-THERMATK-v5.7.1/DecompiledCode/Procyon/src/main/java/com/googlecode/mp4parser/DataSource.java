// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser;

import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.Closeable;

public interface DataSource extends Closeable
{
    void close() throws IOException;
    
    ByteBuffer map(final long p0, final long p1) throws IOException;
    
    long position() throws IOException;
    
    void position(final long p0) throws IOException;
    
    long transferTo(final long p0, final long p1, final WritableByteChannel p2) throws IOException;
}
