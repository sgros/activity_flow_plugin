package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.signature.EmptySignature;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteArrayLoader<Data> implements ModelLoader<byte[], Data> {
    private final Converter<Data> converter;

    public interface Converter<Data> {
        Data convert(byte[] bArr);

        Class<Data> getDataClass();
    }

    public static class ByteBufferFactory implements ModelLoaderFactory<byte[], ByteBuffer> {

        /* renamed from: com.bumptech.glide.load.model.ByteArrayLoader$ByteBufferFactory$1 */
        class C06731 implements Converter<ByteBuffer> {
            C06731() {
            }

            public ByteBuffer convert(byte[] bArr) {
                return ByteBuffer.wrap(bArr);
            }

            public Class<ByteBuffer> getDataClass() {
                return ByteBuffer.class;
            }
        }

        public ModelLoader<byte[], ByteBuffer> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader(new C06731());
        }
    }

    private static class Fetcher<Data> implements DataFetcher<Data> {
        private final Converter<Data> converter;
        private final byte[] model;

        public void cancel() {
        }

        public void cleanup() {
        }

        public Fetcher(byte[] bArr, Converter<Data> converter) {
            this.model = bArr;
            this.converter = converter;
        }

        public void loadData(Priority priority, DataCallback<? super Data> dataCallback) {
            dataCallback.onDataReady(this.converter.convert(this.model));
        }

        public Class<Data> getDataClass() {
            return this.converter.getDataClass();
        }

        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public static class StreamFactory implements ModelLoaderFactory<byte[], InputStream> {

        /* renamed from: com.bumptech.glide.load.model.ByteArrayLoader$StreamFactory$1 */
        class C06741 implements Converter<InputStream> {
            C06741() {
            }

            public InputStream convert(byte[] bArr) {
                return new ByteArrayInputStream(bArr);
            }

            public Class<InputStream> getDataClass() {
                return InputStream.class;
            }
        }

        public ModelLoader<byte[], InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader(new C06741());
        }
    }

    public boolean handles(byte[] bArr) {
        return true;
    }

    public ByteArrayLoader(Converter<Data> converter) {
        this.converter = converter;
    }

    public LoadData<Data> buildLoadData(byte[] bArr, int i, int i2, Options options) {
        return new LoadData(EmptySignature.obtain(), new Fetcher(bArr, this.converter));
    }
}
