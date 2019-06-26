// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.network;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import com.airbnb.lottie.utils.Logger;
import java.io.FileInputStream;
import java.io.InputStream;
import androidx.core.util.Pair;
import java.io.FileNotFoundException;
import java.io.File;
import android.content.Context;

class NetworkCache
{
    private final Context appContext;
    private final String url;
    
    NetworkCache(final Context context, final String url) {
        this.appContext = context.getApplicationContext();
        this.url = url;
    }
    
    private static String filenameForUrl(String str, final FileExtension fileExtension, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("lottie_cache_");
        sb.append(str.replaceAll("\\W+", ""));
        if (b) {
            str = fileExtension.tempExtension();
        }
        else {
            str = fileExtension.extension;
        }
        sb.append(str);
        return sb.toString();
    }
    
    private File getCachedFile(final String s) throws FileNotFoundException {
        final File file = new File(this.appContext.getCacheDir(), filenameForUrl(s, FileExtension.JSON, false));
        if (file.exists()) {
            return file;
        }
        final File file2 = new File(this.appContext.getCacheDir(), filenameForUrl(s, FileExtension.ZIP, false));
        if (file2.exists()) {
            return file2;
        }
        return null;
    }
    
    Pair<FileExtension, InputStream> fetch() {
        try {
            final File cachedFile = this.getCachedFile(this.url);
            if (cachedFile == null) {
                return null;
            }
            final FileInputStream fileInputStream = new FileInputStream(cachedFile);
            FileExtension fileExtension;
            if (cachedFile.getAbsolutePath().endsWith(".zip")) {
                fileExtension = FileExtension.ZIP;
            }
            else {
                fileExtension = FileExtension.JSON;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Cache hit for ");
            sb.append(this.url);
            sb.append(" at ");
            sb.append(cachedFile.getAbsolutePath());
            Logger.debug(sb.toString());
            return new Pair<FileExtension, InputStream>(fileExtension, fileInputStream);
        }
        catch (FileNotFoundException ex) {
            return null;
        }
    }
    
    void renameTempFile(final FileExtension fileExtension) {
        final File file = new File(this.appContext.getCacheDir(), filenameForUrl(this.url, fileExtension, true));
        final File file2 = new File(file.getAbsolutePath().replace(".temp", ""));
        final boolean renameTo = file.renameTo(file2);
        final StringBuilder sb = new StringBuilder();
        sb.append("Copying temp file to real file (");
        sb.append(file2);
        sb.append(")");
        Logger.debug(sb.toString());
        if (!renameTo) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to rename cache file ");
            sb2.append(file.getAbsolutePath());
            sb2.append(" to ");
            sb2.append(file2.getAbsolutePath());
            sb2.append(".");
            Logger.warning(sb2.toString());
        }
    }
    
    File writeTempCacheFile(final InputStream inputStream, FileExtension fileExtension) throws IOException {
        final File file = new File(this.appContext.getCacheDir(), filenameForUrl(this.url, fileExtension, true));
        try {
            fileExtension = (FileExtension)new FileOutputStream(file);
            try {
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    ((FileOutputStream)fileExtension).write(array, 0, read);
                }
                ((OutputStream)fileExtension).flush();
                return file;
            }
            finally {
                ((FileOutputStream)fileExtension).close();
            }
        }
        finally {
            inputStream.close();
        }
    }
}
