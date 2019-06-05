// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.content.ContentResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import android.graphics.Typeface;
import android.support.v4.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.Context;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.Os;
import java.io.File;
import android.os.ParcelFileDescriptor;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl
{
    private File getFile(final ParcelFileDescriptor parcelFileDescriptor) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            final String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        }
        catch (ErrnoException ex) {
            return null;
        }
    }
    
    @Override
    public Typeface createFromFontInfo(Context t, CancellationSignal t2, FontsContractCompat.FontInfo[] openFileDescriptor, final int n) {
        if (openFileDescriptor.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(openFileDescriptor, n);
        final ContentResolver contentResolver = ((Context)t).getContentResolver();
        try {
            openFileDescriptor = (FontsContractCompat.FontInfo[])(Object)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", (CancellationSignal)t2);
            try {
                final File file = this.getFile((ParcelFileDescriptor)(Object)openFileDescriptor);
                if (file != null && file.canRead()) {
                    final Typeface fromFile = Typeface.createFromFile(file);
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return fromFile;
                }
                final FileInputStream fileInputStream = new FileInputStream(((ParcelFileDescriptor)(Object)openFileDescriptor).getFileDescriptor());
                try {
                    final Typeface fromInputStream = super.createFromInputStream((Context)t, fileInputStream);
                    fileInputStream.close();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return fromInputStream;
                }
                catch (Throwable t2) {
                    try {
                        throw t2;
                    }
                    finally {}
                }
                finally {
                    t2 = null;
                }
                if (t2 != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (Throwable exception) {
                        t2.addSuppressed(exception);
                    }
                }
                else {
                    fileInputStream.close();
                }
                throw t;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {}
            }
            finally {
                t = null;
            }
            if (openFileDescriptor != null) {
                if (t != null) {
                    try {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    catch (Throwable exception2) {
                        t.addSuppressed(exception2);
                    }
                }
                else {
                    ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                }
            }
            throw t2;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
