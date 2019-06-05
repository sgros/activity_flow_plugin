// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.InputStream;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import android.content.res.Resources$NotFoundException;
import android.util.Log;
import android.net.Uri;
import android.content.res.Resources;

public class ResourceLoader<Data> implements ModelLoader<Integer, Data>
{
    private final Resources resources;
    private final ModelLoader<Uri, Data> uriLoader;
    
    public ResourceLoader(final Resources resources, final ModelLoader<Uri, Data> uriLoader) {
        this.resources = resources;
        this.uriLoader = uriLoader;
    }
    
    private Uri getResourceUri(final Integer obj) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("android.resource://");
            sb.append(this.resources.getResourcePackageName((int)obj));
            sb.append('/');
            sb.append(this.resources.getResourceTypeName((int)obj));
            sb.append('/');
            sb.append(this.resources.getResourceEntryName((int)obj));
            return Uri.parse(sb.toString());
        }
        catch (Resources$NotFoundException ex) {
            if (Log.isLoggable("ResourceLoader", 5)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Received invalid resource id: ");
                sb2.append(obj);
                Log.w("ResourceLoader", sb2.toString(), (Throwable)ex);
            }
            return null;
        }
    }
    
    public LoadData<Data> buildLoadData(final Integer n, final int n2, final int n3, final Options options) {
        final Uri resourceUri = this.getResourceUri(n);
        Object buildLoadData;
        if (resourceUri == null) {
            buildLoadData = null;
        }
        else {
            buildLoadData = this.uriLoader.buildLoadData(resourceUri, n2, n3, options);
        }
        return (LoadData<Data>)buildLoadData;
    }
    
    @Override
    public boolean handles(final Integer n) {
        return true;
    }
    
    public static class FileDescriptorFactory implements ModelLoaderFactory<Integer, ParcelFileDescriptor>
    {
        private final Resources resources;
        
        public FileDescriptorFactory(final Resources resources) {
            this.resources = resources;
        }
        
        @Override
        public ModelLoader<Integer, ParcelFileDescriptor> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader<ParcelFileDescriptor>(this.resources, (ModelLoader<Uri, ParcelFileDescriptor>)multiModelLoaderFactory.build(Uri.class, (Class<Data>)ParcelFileDescriptor.class));
        }
    }
    
    public static class StreamFactory implements ModelLoaderFactory<Integer, InputStream>
    {
        private final Resources resources;
        
        public StreamFactory(final Resources resources) {
            this.resources = resources;
        }
        
        @Override
        public ModelLoader<Integer, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader<InputStream>(this.resources, (ModelLoader<Uri, InputStream>)multiModelLoaderFactory.build(Uri.class, (Class<Data>)InputStream.class));
        }
    }
}
