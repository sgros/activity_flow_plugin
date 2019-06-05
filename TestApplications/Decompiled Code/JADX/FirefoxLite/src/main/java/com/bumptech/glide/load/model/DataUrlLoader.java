package com.bumptech.glide.load.model;

import android.util.Base64;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DataUrlLoader<Data> implements ModelLoader<String, Data> {
    private final DataDecoder<Data> dataDecoder;

    public interface DataDecoder<Data> {
        void close(Data data) throws IOException;

        Data decode(String str) throws IllegalArgumentException;

        Class<Data> getDataClass();
    }

    private static final class DataUriFetcher<Data> implements DataFetcher<Data> {
        private Data data;
        private final String dataUri;
        private final DataDecoder<Data> reader;

        public void cancel() {
        }

        public DataUriFetcher(String str, DataDecoder<Data> dataDecoder) {
            this.dataUri = str;
            this.reader = dataDecoder;
        }

        public void loadData(Priority priority, DataCallback<? super Data> dataCallback) {
            try {
                this.data = this.reader.decode(this.dataUri);
                dataCallback.onDataReady(this.data);
            } catch (IllegalArgumentException e) {
                dataCallback.onLoadFailed(e);
            }
        }

        public void cleanup() {
            try {
                this.reader.close(this.data);
            } catch (IOException unused) {
            }
        }

        public Class<Data> getDataClass() {
            return this.reader.getDataClass();
        }

        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public static final class StreamFactory implements ModelLoaderFactory<String, InputStream> {
        private final DataDecoder<InputStream> opener = new C06751();

        /* renamed from: com.bumptech.glide.load.model.DataUrlLoader$StreamFactory$1 */
        class C06751 implements DataDecoder<InputStream> {
            C06751() {
            }

            public InputStream decode(String str) {
                if (str.startsWith("data:image")) {
                    int indexOf = str.indexOf(44);
                    if (indexOf == -1) {
                        throw new IllegalArgumentException("Missing comma in data URL.");
                    } else if (str.substring(0, indexOf).endsWith(";base64")) {
                        return new ByteArrayInputStream(Base64.decode(str.substring(indexOf + 1), 0));
                    } else {
                        throw new IllegalArgumentException("Not a base64 image data URL.");
                    }
                }
                throw new IllegalArgumentException("Not a valid image data URL.");
            }

            public void close(InputStream inputStream) throws IOException {
                inputStream.close();
            }

            public Class<InputStream> getDataClass() {
                return InputStream.class;
            }
        }

        public final ModelLoader<String, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new DataUrlLoader(this.opener);
        }
    }

    public DataUrlLoader(DataDecoder<Data> dataDecoder) {
        this.dataDecoder = dataDecoder;
    }

    public LoadData<Data> buildLoadData(String str, int i, int i2, Options options) {
        return new LoadData(new ObjectKey(str), new DataUriFetcher(str, this.dataDecoder));
    }

    public boolean handles(String str) {
        return str.startsWith("data:image");
    }
}
