// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig.platform;

import java.io.IOException;

public interface SeekableFile
{
    long position() throws IOException;
    
    int read() throws IOException;
    
    double readDouble() throws IOException;
    
    void readFully(final byte[] p0) throws IOException;
    
    int readInt() throws IOException;
    
    long readLong() throws IOException;
    
    short readShort() throws IOException;
    
    String readString() throws IOException;
    
    void seek(final long p0) throws IOException;
    
    long skip(final long p0) throws IOException;
}
