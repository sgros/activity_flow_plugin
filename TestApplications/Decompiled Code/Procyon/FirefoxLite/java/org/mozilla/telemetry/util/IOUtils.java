// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.util;

import java.io.IOException;
import java.io.Closeable;

public class IOUtils
{
    public static void safeClose(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
}
