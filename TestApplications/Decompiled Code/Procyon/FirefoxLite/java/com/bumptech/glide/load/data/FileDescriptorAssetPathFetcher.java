// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.io.IOException;
import android.content.res.AssetManager;
import android.os.ParcelFileDescriptor;

public class FileDescriptorAssetPathFetcher extends AssetPathFetcher<ParcelFileDescriptor>
{
    public FileDescriptorAssetPathFetcher(final AssetManager assetManager, final String s) {
        super(assetManager, s);
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
    protected ParcelFileDescriptor loadResource(final AssetManager assetManager, final String s) throws IOException {
        return assetManager.openFd(s).getParcelFileDescriptor();
    }
}
