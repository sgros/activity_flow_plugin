package com.bumptech.glide;

import android.support.p001v4.util.Pools.Pool;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinder.Factory;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Registry {
    private final DataRewinderRegistry dataRewinderRegistry = new DataRewinderRegistry();
    private final ResourceDecoderRegistry decoderRegistry = new ResourceDecoderRegistry();
    private final EncoderRegistry encoderRegistry = new EncoderRegistry();
    private final Pool<List<Exception>> exceptionListPool = FactoryPools.threadSafeList();
    private final ImageHeaderParserRegistry imageHeaderParserRegistry = new ImageHeaderParserRegistry();
    private final LoadPathCache loadPathCache = new LoadPathCache();
    private final ModelLoaderRegistry modelLoaderRegistry = new ModelLoaderRegistry(this.exceptionListPool);
    private final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
    private final ResourceEncoderRegistry resourceEncoderRegistry = new ResourceEncoderRegistry();
    private final TranscoderRegistry transcoderRegistry = new TranscoderRegistry();

    public static class MissingComponentException extends RuntimeException {
        public MissingComponentException(String str) {
            super(str);
        }
    }

    public static final class NoImageHeaderParserException extends MissingComponentException {
        public NoImageHeaderParserException() {
            super("Failed to find image header parser.");
        }
    }

    public static class NoModelLoaderAvailableException extends MissingComponentException {
        public NoModelLoaderAvailableException(Object obj) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find any ModelLoaders for model: ");
            stringBuilder.append(obj);
            super(stringBuilder.toString());
        }

        public NoModelLoaderAvailableException(Class<?> cls, Class<?> cls2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find any ModelLoaders for model: ");
            stringBuilder.append(cls);
            stringBuilder.append(" and data: ");
            stringBuilder.append(cls2);
            super(stringBuilder.toString());
        }
    }

    public static class NoResultEncoderAvailableException extends MissingComponentException {
        public NoResultEncoderAvailableException(Class<?> cls) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find result encoder for resource class: ");
            stringBuilder.append(cls);
            super(stringBuilder.toString());
        }
    }

    public static class NoSourceEncoderAvailableException extends MissingComponentException {
        public NoSourceEncoderAvailableException(Class<?> cls) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find source encoder for data class: ");
            stringBuilder.append(cls);
            super(stringBuilder.toString());
        }
    }

    public Registry() {
        setResourceDecoderBucketPriorityList(Arrays.asList(new String[]{"Gif", "Bitmap", "BitmapDrawable"}));
    }

    public <Data> Registry append(Class<Data> cls, Encoder<Data> encoder) {
        this.encoderRegistry.append(cls, encoder);
        return this;
    }

    public <Data, TResource> Registry append(Class<Data> cls, Class<TResource> cls2, ResourceDecoder<Data, TResource> resourceDecoder) {
        append("legacy_append", cls, cls2, resourceDecoder);
        return this;
    }

    public <Data, TResource> Registry append(String str, Class<Data> cls, Class<TResource> cls2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.append(str, resourceDecoder, cls, cls2);
        return this;
    }

    public <Data, TResource> Registry prepend(Class<Data> cls, Class<TResource> cls2, ResourceDecoder<Data, TResource> resourceDecoder) {
        prepend("legacy_prepend_all", cls, cls2, resourceDecoder);
        return this;
    }

    public <Data, TResource> Registry prepend(String str, Class<Data> cls, Class<TResource> cls2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.prepend(str, resourceDecoder, cls, cls2);
        return this;
    }

    public final Registry setResourceDecoderBucketPriorityList(List<String> list) {
        ArrayList arrayList = new ArrayList(list);
        arrayList.add(0, "legacy_prepend_all");
        arrayList.add("legacy_append");
        this.decoderRegistry.setBucketPriorityList(arrayList);
        return this;
    }

    public <TResource> Registry append(Class<TResource> cls, ResourceEncoder<TResource> resourceEncoder) {
        this.resourceEncoderRegistry.append(cls, resourceEncoder);
        return this;
    }

    public Registry register(Factory factory) {
        this.dataRewinderRegistry.register(factory);
        return this;
    }

    public <TResource, Transcode> Registry register(Class<TResource> cls, Class<Transcode> cls2, ResourceTranscoder<TResource, Transcode> resourceTranscoder) {
        this.transcoderRegistry.register(cls, cls2, resourceTranscoder);
        return this;
    }

    public Registry register(ImageHeaderParser imageHeaderParser) {
        this.imageHeaderParserRegistry.add(imageHeaderParser);
        return this;
    }

    public <Model, Data> Registry append(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.append(cls, cls2, modelLoaderFactory);
        return this;
    }

    public <Model, Data> Registry prepend(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.prepend(cls, cls2, modelLoaderFactory);
        return this;
    }

    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(Class<Data> cls, Class<TResource> cls2, Class<Transcode> cls3) {
        LoadPath<Data, TResource, Transcode> loadPath = this.loadPathCache.get(cls, cls2, cls3);
        if (loadPath == null && !this.loadPathCache.contains(cls, cls2, cls3)) {
            List decodePaths = getDecodePaths(cls, cls2, cls3);
            if (decodePaths.isEmpty()) {
                loadPath = null;
            } else {
                LoadPath<Data, TResource, Transcode> loadPath2 = new LoadPath(cls, cls2, cls3, decodePaths, this.exceptionListPool);
            }
            this.loadPathCache.put(cls, cls2, cls3, loadPath);
        }
        return loadPath;
    }

    private <Data, TResource, Transcode> List<DecodePath<Data, TResource, Transcode>> getDecodePaths(Class<Data> cls, Class<TResource> cls2, Class<Transcode> cls3) {
        ArrayList arrayList = new ArrayList();
        for (Class cls4 : this.decoderRegistry.getResourceClasses(cls, cls2)) {
            for (Class cls5 : this.transcoderRegistry.getTranscodeClasses(cls4, cls3)) {
                arrayList.add(new DecodePath(cls, cls4, cls5, this.decoderRegistry.getDecoders(cls, cls4), this.transcoderRegistry.get(cls4, cls5), this.exceptionListPool));
            }
        }
        return arrayList;
    }

    public <Model, TResource, Transcode> List<Class<?>> getRegisteredResourceClasses(Class<Model> cls, Class<TResource> cls2, Class<Transcode> cls3) {
        List<Class<?>> list = this.modelToResourceClassCache.get(cls, cls2);
        if (list == null) {
            list = new ArrayList();
            for (Class resourceClasses : this.modelLoaderRegistry.getDataClasses(cls)) {
                for (Class cls4 : this.decoderRegistry.getResourceClasses(resourceClasses, cls2)) {
                    if (!(this.transcoderRegistry.getTranscodeClasses(cls4, cls3).isEmpty() || list.contains(cls4))) {
                        list.add(cls4);
                    }
                }
            }
            this.modelToResourceClassCache.put(cls, cls2, Collections.unmodifiableList(list));
        }
        return list;
    }

    public boolean isResourceEncoderAvailable(Resource<?> resource) {
        return this.resourceEncoderRegistry.get(resource.getResourceClass()) != null;
    }

    public <X> ResourceEncoder<X> getResultEncoder(Resource<X> resource) throws NoResultEncoderAvailableException {
        ResourceEncoder resourceEncoder = this.resourceEncoderRegistry.get(resource.getResourceClass());
        if (resourceEncoder != null) {
            return resourceEncoder;
        }
        throw new NoResultEncoderAvailableException(resource.getResourceClass());
    }

    public <X> Encoder<X> getSourceEncoder(X x) throws NoSourceEncoderAvailableException {
        Encoder encoder = this.encoderRegistry.getEncoder(x.getClass());
        if (encoder != null) {
            return encoder;
        }
        throw new NoSourceEncoderAvailableException(x.getClass());
    }

    public <X> DataRewinder<X> getRewinder(X x) {
        return this.dataRewinderRegistry.build(x);
    }

    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Model model) {
        List modelLoaders = this.modelLoaderRegistry.getModelLoaders(model);
        if (!modelLoaders.isEmpty()) {
            return modelLoaders;
        }
        throw new NoModelLoaderAvailableException(model);
    }

    public List<ImageHeaderParser> getImageHeaderParsers() {
        List parsers = this.imageHeaderParserRegistry.getParsers();
        if (!parsers.isEmpty()) {
            return parsers;
        }
        throw new NoImageHeaderParserException();
    }
}
