// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso.boxes;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public interface Box
{
    void getBox(final WritableByteChannel p0) throws IOException;
    
    long getSize();
    
    void setParent(final Container p0);
}
