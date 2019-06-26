// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.database.Cursor;
import java.io.IOException;
import java.io.FileNotFoundException;
import android.os.ParcelFileDescriptor$AutoCloseOutputStream;
import org.telegram.messenger.ApplicationLoader;
import android.os.ParcelFileDescriptor;
import android.content.ContentValues;
import android.net.Uri;
import android.content.ContentProvider;

public class CallNotificationSoundProvider extends ContentProvider
{
    public int delete(final Uri uri, final String s, final String[] array) {
        return 0;
    }
    
    public String getType(final Uri uri) {
        return null;
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        return null;
    }
    
    public boolean onCreate() {
        return true;
    }
    
    public ParcelFileDescriptor openFile(final Uri uri, final String s) throws FileNotFoundException {
        if ("r".equals(s)) {
            if (ApplicationLoader.applicationContext != null) {
                final VoIPBaseService sharedInstance = VoIPBaseService.getSharedInstance();
                if (sharedInstance != null) {
                    sharedInstance.startRingtoneAndVibration();
                }
                try {
                    final ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                    final ParcelFileDescriptor$AutoCloseOutputStream parcelFileDescriptor$AutoCloseOutputStream = new ParcelFileDescriptor$AutoCloseOutputStream(pipe[1]);
                    parcelFileDescriptor$AutoCloseOutputStream.write(new byte[] { 82, 73, 70, 70, 41, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, 1, 0, 68, -84, 0, 0, 16, -79, 2, 0, 2, 0, 16, 0, 100, 97, 116, 97, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
                    parcelFileDescriptor$AutoCloseOutputStream.close();
                    return pipe[0];
                }
                catch (IOException ex) {
                    throw new FileNotFoundException(ex.getMessage());
                }
            }
            throw new FileNotFoundException("Unexpected application state");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected file mode ");
        sb.append(s);
        throw new SecurityException(sb.toString());
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        return null;
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        return 0;
    }
}
