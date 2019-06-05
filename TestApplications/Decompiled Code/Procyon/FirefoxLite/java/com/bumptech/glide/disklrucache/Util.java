// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.disklrucache;

import java.io.IOException;
import java.io.File;
import java.io.Closeable;
import java.nio.charset.Charset;

final class Util
{
    static final Charset US_ASCII;
    static final Charset UTF_8;
    
    static {
        US_ASCII = Charset.forName("US-ASCII");
        UTF_8 = Charset.forName("UTF-8");
    }
    
    static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            goto Label_0016;
        }
        try {
            closeable.close();
            goto Label_0016;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            goto Label_0016;
        }
    }
    
    static void deleteContents(File file) throws IOException {
        final File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                file = listFiles[i];
                if (file.isDirectory()) {
                    deleteContents(file);
                }
                if (!file.delete()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to delete file: ");
                    sb.append(file);
                    throw new IOException(sb.toString());
                }
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("not a readable directory: ");
        sb2.append(file);
        throw new IOException(sb2.toString());
    }
}
