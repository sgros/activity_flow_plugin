// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public interface Serializable
{
    void deserialize(final DataInputStream p0) throws IOException;
    
    void serialize(final DataOutputStream p0) throws IOException;
}
