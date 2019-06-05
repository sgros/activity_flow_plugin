// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.FileInputStream;
import java.io.InputStream;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;
import java.io.File;

public class FileLoader<Data> implements ModelLoader<File, Data>
{
    private final FileOpener<Data> fileOpener;
    
    public FileLoader(final FileOpener<Data> fileOpener) {
        this.fileOpener = fileOpener;
    }
    
    public LoadData<Data> buildLoadData(final File file, final int n, final int n2, final Options options) {
        return (LoadData<Data>)new LoadData(new ObjectKey(file), new FileFetcher<Object>(file, (FileOpener<Object>)this.fileOpener));
    }
    
    @Override
    public boolean handles(final File file) {
        return true;
    }
    
    public static class Factory<Data> implements ModelLoaderFactory<File, Data>
    {
        private final FileOpener<Data> opener;
        
        public Factory(final FileOpener<Data> opener) {
            this.opener = opener;
        }
        
        @Override
        public final ModelLoader<File, Data> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new FileLoader<Data>(this.opener);
        }
    }
    
    public static class FileDescriptorFactory extends Factory<ParcelFileDescriptor>
    {
        public FileDescriptorFactory() {
            super(new FileOpener<ParcelFileDescriptor>() {
                public void close(final ParcelFileDescriptor parcelFileDescriptor) throws IOException {
                    parcelFileDescriptor.close();
                }
                
                @Override
                public Class<ParcelFileDescriptor> getDataClass() {
                    return ParcelFileDescriptor.class;
                }
                
                public ParcelFileDescriptor open(final File file) throws FileNotFoundException {
                    return ParcelFileDescriptor.open(file, 268435456);
                }
            });
        }
    }
    
    private static class FileFetcher<Data> implements DataFetcher<Data>
    {
        private Data data;
        private final File file;
        private final FileOpener<Data> opener;
        
        public FileFetcher(final File file, final FileOpener<Data> opener) {
            this.file = file;
            this.opener = opener;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
            if (this.data == null) {
                return;
            }
            try {
                this.opener.close(this.data);
            }
            catch (IOException ex) {}
        }
        
        @Override
        public Class<Data> getDataClass() {
            return this.opener.getDataClass();
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super Data> dataCallback) {
            try {
                dataCallback.onDataReady(this.data = this.opener.open(this.file));
            }
            catch (FileNotFoundException ex) {
                if (Log.isLoggable("FileLoader", 3)) {
                    Log.d("FileLoader", "Failed to open file", (Throwable)ex);
                }
                dataCallback.onLoadFailed(ex);
            }
        }
    }
    
    public interface FileOpener<Data>
    {
        void close(final Data p0) throws IOException;
        
        Class<Data> getDataClass();
        
        Data open(final File p0) throws FileNotFoundException;
    }
    
    public static class StreamFactory extends Factory<InputStream>
    {
        public StreamFactory() {
            super(new FileOpener<InputStream>() {
                public void close(final InputStream inputStream) throws IOException {
                    inputStream.close();
                }
                
                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }
                
                public InputStream open(final File file) throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        }
    }
}
