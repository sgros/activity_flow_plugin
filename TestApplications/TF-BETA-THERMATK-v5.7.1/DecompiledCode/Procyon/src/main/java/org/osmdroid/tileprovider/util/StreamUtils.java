// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.util;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Closeable;

public class StreamUtils
{
    public static void closeStream(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static long copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[8192];
        long n = 0L;
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
            n += read;
        }
        return n;
    }
}
