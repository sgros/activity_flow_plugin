package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileLoader<Data> implements ModelLoader<File, Data> {
    private final FileOpener<Data> fileOpener;

    public interface FileOpener<Data> {
        void close(Data data) throws IOException;

        Class<Data> getDataClass();

        Data open(File file) throws FileNotFoundException;
    }

    public static class Factory<Data> implements ModelLoaderFactory<File, Data> {
        private final FileOpener<Data> opener;

        public Factory(FileOpener<Data> fileOpener) {
            this.opener = fileOpener;
        }

        public final ModelLoader<File, Data> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new FileLoader(this.opener);
        }
    }

    private static class FileFetcher<Data> implements DataFetcher<Data> {
        private Data data;
        private final File file;
        private final FileOpener<Data> opener;

        public void cancel() {
        }

        public FileFetcher(File file, FileOpener<Data> fileOpener) {
            this.file = file;
            this.opener = fileOpener;
        }

        public void loadData(Priority priority, DataCallback<? super Data> dataCallback) {
            try {
                this.data = this.opener.open(this.file);
                dataCallback.onDataReady(this.data);
            } catch (FileNotFoundException e) {
                if (Log.isLoggable("FileLoader", 3)) {
                    Log.d("FileLoader", "Failed to open file", e);
                }
                dataCallback.onLoadFailed(e);
            }
        }

        public void cleanup() {
            if (this.data != null) {
                try {
                    this.opener.close(this.data);
                } catch (IOException unused) {
                }
            }
        }

        public Class<Data> getDataClass() {
            return this.opener.getDataClass();
        }

        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public static class FileDescriptorFactory extends Factory<ParcelFileDescriptor> {

        /* renamed from: com.bumptech.glide.load.model.FileLoader$FileDescriptorFactory$1 */
        class C06761 implements FileOpener<ParcelFileDescriptor> {
            C06761() {
            }

            public ParcelFileDescriptor open(File file) throws FileNotFoundException {
                return ParcelFileDescriptor.open(file, 268435456);
            }

            public void close(ParcelFileDescriptor parcelFileDescriptor) throws IOException {
                parcelFileDescriptor.close();
            }

            public Class<ParcelFileDescriptor> getDataClass() {
                return ParcelFileDescriptor.class;
            }
        }

        public FileDescriptorFactory() {
            super(new C06761());
        }
    }

    public static class StreamFactory extends Factory<InputStream> {

        /* renamed from: com.bumptech.glide.load.model.FileLoader$StreamFactory$1 */
        class C06771 implements FileOpener<InputStream> {
            C06771() {
            }

            public InputStream open(File file) throws FileNotFoundException {
                return new FileInputStream(file);
            }

            public void close(InputStream inputStream) throws IOException {
                inputStream.close();
            }

            public Class<InputStream> getDataClass() {
                return InputStream.class;
            }
        }

        public StreamFactory() {
            super(new C06771());
        }
    }

    public boolean handles(File file) {
        return true;
    }

    public FileLoader(FileOpener<Data> fileOpener) {
        this.fileOpener = fileOpener;
    }

    public LoadData<Data> buildLoadData(File file, int i, int i2, Options options) {
        return new LoadData(new ObjectKey(file), new FileFetcher(file, this.fileOpener));
    }
}
