// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import java.util.Collection;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.Resource;
import java.util.Collections;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Encoder;
import java.util.Iterator;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import com.bumptech.glide.load.engine.DecodePath;
import java.util.Arrays;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import java.util.List;
import android.support.v4.util.Pools;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.load.data.DataRewinderRegistry;

public class Registry
{
    private final DataRewinderRegistry dataRewinderRegistry;
    private final ResourceDecoderRegistry decoderRegistry;
    private final EncoderRegistry encoderRegistry;
    private final Pools.Pool<List<Exception>> exceptionListPool;
    private final ImageHeaderParserRegistry imageHeaderParserRegistry;
    private final LoadPathCache loadPathCache;
    private final ModelLoaderRegistry modelLoaderRegistry;
    private final ModelToResourceClassCache modelToResourceClassCache;
    private final ResourceEncoderRegistry resourceEncoderRegistry;
    private final TranscoderRegistry transcoderRegistry;
    
    public Registry() {
        this.modelToResourceClassCache = new ModelToResourceClassCache();
        this.loadPathCache = new LoadPathCache();
        this.exceptionListPool = FactoryPools.threadSafeList();
        this.modelLoaderRegistry = new ModelLoaderRegistry(this.exceptionListPool);
        this.encoderRegistry = new EncoderRegistry();
        this.decoderRegistry = new ResourceDecoderRegistry();
        this.resourceEncoderRegistry = new ResourceEncoderRegistry();
        this.dataRewinderRegistry = new DataRewinderRegistry();
        this.transcoderRegistry = new TranscoderRegistry();
        this.imageHeaderParserRegistry = new ImageHeaderParserRegistry();
        this.setResourceDecoderBucketPriorityList(Arrays.asList("Gif", "Bitmap", "BitmapDrawable"));
    }
    
    private <Data, TResource, Transcode> List<DecodePath<Data, TResource, Transcode>> getDecodePaths(final Class<Data> clazz, final Class<TResource> clazz2, final Class<Transcode> clazz3) {
        final ArrayList<DecodePath<Data, Z, Object>> list = (ArrayList<DecodePath<Data, Z, Object>>)new ArrayList<DecodePath<Data, TResource, Transcode>>();
        for (final Class<TResource> clazz4 : this.decoderRegistry.getResourceClasses(clazz, clazz2)) {
            for (final Class<Transcode> clazz5 : this.transcoderRegistry.getTranscodeClasses((Class<R>)clazz4, clazz3)) {
                list.add(new DecodePath<Data, TResource, Transcode>((Class<Object>)clazz, (Class<Object>)clazz4, (Class<Object>)clazz5, (List<? extends ResourceDecoder<Object, Object>>)this.decoderRegistry.getDecoders(clazz, (Class<Z>)clazz4), (ResourceTranscoder<Object, Object>)this.transcoderRegistry.get((Class<Z>)clazz4, clazz5), this.exceptionListPool));
            }
        }
        return (List<DecodePath<Data, TResource, Transcode>>)list;
    }
    
    public <Data> Registry append(final Class<Data> clazz, final Encoder<Data> encoder) {
        this.encoderRegistry.append(clazz, encoder);
        return this;
    }
    
    public <TResource> Registry append(final Class<TResource> clazz, final ResourceEncoder<TResource> resourceEncoder) {
        this.resourceEncoderRegistry.append(clazz, resourceEncoder);
        return this;
    }
    
    public <Data, TResource> Registry append(final Class<Data> clazz, final Class<TResource> clazz2, final ResourceDecoder<Data, TResource> resourceDecoder) {
        this.append("legacy_append", clazz, clazz2, resourceDecoder);
        return this;
    }
    
    public <Model, Data> Registry append(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.append(clazz, clazz2, modelLoaderFactory);
        return this;
    }
    
    public <Data, TResource> Registry append(final String s, final Class<Data> clazz, final Class<TResource> clazz2, final ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.append(s, resourceDecoder, clazz, clazz2);
        return this;
    }
    
    public List<ImageHeaderParser> getImageHeaderParsers() {
        final List<ImageHeaderParser> parsers = this.imageHeaderParserRegistry.getParsers();
        if (!parsers.isEmpty()) {
            return parsers;
        }
        throw new NoImageHeaderParserException();
    }
    
    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(final Class<Data> clazz, final Class<TResource> clazz2, final Class<Transcode> clazz3) {
        LoadPath<?, ?, ?> value;
        final LoadPath<Data, TResource, Transcode> loadPath = (LoadPath<Data, TResource, Transcode>)(value = this.loadPathCache.get(clazz, clazz2, clazz3));
        if (loadPath == null) {
            value = loadPath;
            if (!this.loadPathCache.contains(clazz, clazz2, clazz3)) {
                final List<DecodePath<Data, TResource, Transcode>> decodePaths = this.getDecodePaths(clazz, clazz2, clazz3);
                if (decodePaths.isEmpty()) {
                    value = null;
                }
                else {
                    value = new LoadPath<Data, TResource, Transcode>((Class<Object>)clazz, (Class<Object>)clazz2, (Class<Object>)clazz3, (List<DecodePath<Object, Object, Object>>)decodePaths, this.exceptionListPool);
                }
                this.loadPathCache.put(clazz, clazz2, clazz3, value);
            }
        }
        return (LoadPath<Data, TResource, Transcode>)value;
    }
    
    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(final Model model) {
        final List<ModelLoader<Model, ?>> modelLoaders = this.modelLoaderRegistry.getModelLoaders(model);
        if (!modelLoaders.isEmpty()) {
            return modelLoaders;
        }
        throw new NoModelLoaderAvailableException(model);
    }
    
    public <Model, TResource, Transcode> List<Class<?>> getRegisteredResourceClasses(final Class<Model> clazz, final Class<TResource> clazz2, final Class<Transcode> clazz3) {
        List<Class<?>> value;
        if ((value = this.modelToResourceClassCache.get(clazz, clazz2)) == null) {
            value = (List<Class<?>>)new ArrayList<Class<TResource>>();
            final Iterator<Class<Object>> iterator = this.modelLoaderRegistry.getDataClasses(clazz).iterator();
            while (iterator.hasNext()) {
                for (final Class<TResource> clazz4 : this.decoderRegistry.getResourceClasses(iterator.next(), clazz2)) {
                    if (!this.transcoderRegistry.getTranscodeClasses(clazz4, clazz3).isEmpty() && !value.contains(clazz4)) {
                        value.add(clazz4);
                    }
                }
            }
            this.modelToResourceClassCache.put(clazz, clazz2, (List<Class<?>>)Collections.unmodifiableList((List<?>)value));
        }
        return value;
    }
    
    public <X> ResourceEncoder<X> getResultEncoder(final Resource<X> resource) throws NoResultEncoderAvailableException {
        final ResourceEncoder<X> value = this.resourceEncoderRegistry.get(resource.getResourceClass());
        if (value != null) {
            return value;
        }
        throw new NoResultEncoderAvailableException(resource.getResourceClass());
    }
    
    public <X> DataRewinder<X> getRewinder(final X x) {
        return this.dataRewinderRegistry.build(x);
    }
    
    public <X> Encoder<X> getSourceEncoder(final X x) throws NoSourceEncoderAvailableException {
        final Encoder<?> encoder = this.encoderRegistry.getEncoder(x.getClass());
        if (encoder != null) {
            return (Encoder<X>)encoder;
        }
        throw new NoSourceEncoderAvailableException(x.getClass());
    }
    
    public boolean isResourceEncoderAvailable(final Resource<?> resource) {
        return this.resourceEncoderRegistry.get(resource.getResourceClass()) != null;
    }
    
    public <Data, TResource> Registry prepend(final Class<Data> clazz, final Class<TResource> clazz2, final ResourceDecoder<Data, TResource> resourceDecoder) {
        this.prepend("legacy_prepend_all", clazz, clazz2, resourceDecoder);
        return this;
    }
    
    public <Model, Data> Registry prepend(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.prepend(clazz, clazz2, modelLoaderFactory);
        return this;
    }
    
    public <Data, TResource> Registry prepend(final String s, final Class<Data> clazz, final Class<TResource> clazz2, final ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.prepend(s, resourceDecoder, clazz, clazz2);
        return this;
    }
    
    public Registry register(final ImageHeaderParser imageHeaderParser) {
        this.imageHeaderParserRegistry.add(imageHeaderParser);
        return this;
    }
    
    public Registry register(final DataRewinder.Factory factory) {
        this.dataRewinderRegistry.register(factory);
        return this;
    }
    
    public <TResource, Transcode> Registry register(final Class<TResource> clazz, final Class<Transcode> clazz2, final ResourceTranscoder<TResource, Transcode> resourceTranscoder) {
        this.transcoderRegistry.register(clazz, clazz2, resourceTranscoder);
        return this;
    }
    
    public final Registry setResourceDecoderBucketPriorityList(final List<String> c) {
        final ArrayList<String> bucketPriorityList = new ArrayList<String>(c);
        bucketPriorityList.add(0, "legacy_prepend_all");
        bucketPriorityList.add("legacy_append");
        this.decoderRegistry.setBucketPriorityList(bucketPriorityList);
        return this;
    }
    
    public static class MissingComponentException extends RuntimeException
    {
        public MissingComponentException(final String message) {
            super(message);
        }
    }
    
    public static final class NoImageHeaderParserException extends MissingComponentException
    {
        public NoImageHeaderParserException() {
            super("Failed to find image header parser.");
        }
    }
    
    public static class NoModelLoaderAvailableException extends MissingComponentException
    {
        public NoModelLoaderAvailableException(final Class<?> obj, final Class<?> obj2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to find any ModelLoaders for model: ");
            sb.append(obj);
            sb.append(" and data: ");
            sb.append(obj2);
            super(sb.toString());
        }
        
        public NoModelLoaderAvailableException(final Object obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to find any ModelLoaders for model: ");
            sb.append(obj);
            super(sb.toString());
        }
    }
    
    public static class NoResultEncoderAvailableException extends MissingComponentException
    {
        public NoResultEncoderAvailableException(final Class<?> obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to find result encoder for resource class: ");
            sb.append(obj);
            super(sb.toString());
        }
    }
    
    public static class NoSourceEncoderAvailableException extends MissingComponentException
    {
        public NoSourceEncoderAvailableException(final Class<?> obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to find source encoder for data class: ");
            sb.append(obj);
            super(sb.toString());
        }
    }
}
