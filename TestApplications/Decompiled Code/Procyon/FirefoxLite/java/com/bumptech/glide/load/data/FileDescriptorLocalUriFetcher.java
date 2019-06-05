// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import android.content.res.AssetFileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.net.Uri;
import android.content.ContentResolver;
import android.os.ParcelFileDescriptor;

public class FileDescriptorLocalUriFetcher extends LocalUriFetcher<ParcelFileDescriptor>
{
    public FileDescriptorLocalUriFetcher(final ContentResolver contentResolver, final Uri uri) {
        super(contentResolver, uri);
    }
    
    @Override
    protected void close(final ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        parcelFileDescriptor.close();
    }
    
    @Override
    public Class<ParcelFileDescriptor> getDataClass() {
        return ParcelFileDescriptor.class;
    }
    
    @Override
    protected ParcelFileDescriptor loadResource(final Uri obj, final ContentResolver contentResolver) throws FileNotFoundException {
        final AssetFileDescriptor openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(obj, "r");
        if (openAssetFileDescriptor != null) {
            return openAssetFileDescriptor.getParcelFileDescriptor();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("FileDescriptor is null for: ");
        sb.append(obj);
        throw new FileNotFoundException(sb.toString());
    }
}
