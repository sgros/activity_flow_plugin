// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso;

import java.io.IOException;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import com.googlecode.mp4parser.BasicContainer;

public class IsoFile extends BasicContainer implements Closeable
{
    private static Logger LOG;
    
    static {
        IsoFile.LOG = Logger.getLogger(IsoFile.class);
    }
    
    public static byte[] fourCCtoBytes(final String s) {
        final byte[] array = new byte[4];
        if (s != null) {
            for (int i = 0; i < Math.min(4, s.length()); ++i) {
                array[i] = (byte)s.charAt(i);
            }
        }
        return array;
    }
    
    @Override
    public void close() throws IOException {
        super.dataSource.close();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("model(");
        sb.append(super.dataSource.toString());
        sb.append(")");
        return sb.toString();
    }
}
