// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig.platform;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public interface FileHandle
{
    void create() throws IOException;
    
    void delete() throws IOException;
    
    boolean exists() throws IOException;
    
    DataInputStream openDataInputStream() throws IOException;
    
    DataOutputStream openDataOutputStream() throws IOException;
    
    void truncate(final long p0) throws IOException;
}
