// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.Registry;
import java.io.File;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.util.ArrayList;
import com.bumptech.glide.load.Transformation;
import java.util.Map;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.load.Key;
import java.util.List;

final class DecodeHelper<Transcode>
{
    private final List<Key> cacheKeys;
    private DecodeJob.DiskCacheProvider diskCacheProvider;
    private DiskCacheStrategy diskCacheStrategy;
    private GlideContext glideContext;
    private int height;
    private boolean isCacheKeysSet;
    private boolean isLoadDataSet;
    private boolean isScaleOnlyOrNoTransform;
    private boolean isTransformationRequired;
    private final List<ModelLoader.LoadData<?>> loadData;
    private Object model;
    private Options options;
    private Priority priority;
    private Class<?> resourceClass;
    private Key signature;
    private Class<Transcode> transcodeClass;
    private Map<Class<?>, Transformation<?>> transformations;
    private int width;
    
    DecodeHelper() {
        this.loadData = new ArrayList<ModelLoader.LoadData<?>>();
        this.cacheKeys = new ArrayList<Key>();
    }
    
    void clear() {
        this.glideContext = null;
        this.model = null;
        this.signature = null;
        this.resourceClass = null;
        this.transcodeClass = null;
        this.options = null;
        this.priority = null;
        this.transformations = null;
        this.diskCacheStrategy = null;
        this.loadData.clear();
        this.isLoadDataSet = false;
        this.cacheKeys.clear();
        this.isCacheKeysSet = false;
    }
    
    List<Key> getCacheKeys() {
        if (!this.isCacheKeysSet) {
            this.isCacheKeysSet = true;
            this.cacheKeys.clear();
            final List<ModelLoader.LoadData<?>> loadData = this.getLoadData();
            for (int size = loadData.size(), i = 0; i < size; ++i) {
                final ModelLoader.LoadData loadData2 = (ModelLoader.LoadData)loadData.get(i);
                if (!this.cacheKeys.contains(((ModelLoader.LoadData)loadData2).sourceKey)) {
                    this.cacheKeys.add(((ModelLoader.LoadData)loadData2).sourceKey);
                }
                for (int j = 0; j < ((ModelLoader.LoadData)loadData2).alternateKeys.size(); ++j) {
                    if (!this.cacheKeys.contains(((ModelLoader.LoadData)loadData2).alternateKeys.get(j))) {
                        this.cacheKeys.add(((ModelLoader.LoadData)loadData2).alternateKeys.get(j));
                    }
                }
            }
        }
        return this.cacheKeys;
    }
    
    DiskCache getDiskCache() {
        return this.diskCacheProvider.getDiskCache();
    }
    
    DiskCacheStrategy getDiskCacheStrategy() {
        return this.diskCacheStrategy;
    }
    
    int getHeight() {
        return this.height;
    }
    
    List<ModelLoader.LoadData<?>> getLoadData() {
        if (!this.isLoadDataSet) {
            this.isLoadDataSet = true;
            this.loadData.clear();
            final List<ModelLoader<Object, ?>> modelLoaders = this.glideContext.getRegistry().getModelLoaders(this.model);
            for (int size = modelLoaders.size(), i = 0; i < size; ++i) {
                final ModelLoader.LoadData<Object> buildLoadData = modelLoaders.get(i).buildLoadData(this.model, this.width, this.height, this.options);
                if (buildLoadData != null) {
                    this.loadData.add((ModelLoader.LoadData<?>)buildLoadData);
                }
            }
        }
        return this.loadData;
    }
    
     <Data> LoadPath<Data, ?, Transcode> getLoadPath(final Class<Data> clazz) {
        return this.glideContext.getRegistry().getLoadPath(clazz, this.resourceClass, this.transcodeClass);
    }
    
    List<ModelLoader<File, ?>> getModelLoaders(final File file) throws Registry.NoModelLoaderAvailableException {
        return this.glideContext.getRegistry().getModelLoaders(file);
    }
    
    Options getOptions() {
        return this.options;
    }
    
    Priority getPriority() {
        return this.priority;
    }
    
    List<Class<?>> getRegisteredResourceClasses() {
        return this.glideContext.getRegistry().getRegisteredResourceClasses(this.model.getClass(), this.resourceClass, this.transcodeClass);
    }
    
     <Z> ResourceEncoder<Z> getResultEncoder(final Resource<Z> resource) {
        return this.glideContext.getRegistry().getResultEncoder(resource);
    }
    
    Key getSignature() {
        return this.signature;
    }
    
     <X> Encoder<X> getSourceEncoder(final X x) throws Registry.NoSourceEncoderAvailableException {
        return this.glideContext.getRegistry().getSourceEncoder(x);
    }
    
     <Z> Transformation<Z> getTransformation(final Class<Z> obj) {
        final Transformation<?> transformation = this.transformations.get(obj);
        if (transformation != null) {
            return (Transformation<Z>)transformation;
        }
        if (this.transformations.isEmpty() && this.isTransformationRequired) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Missing transformation for ");
            sb.append(obj);
            sb.append(". If you wish to ignore unknown resource types, use the optional transformation methods.");
            throw new IllegalArgumentException(sb.toString());
        }
        return (Transformation<Z>)UnitTransformation.get();
    }
    
    int getWidth() {
        return this.width;
    }
    
    boolean hasLoadPath(final Class<?> clazz) {
        return this.getLoadPath(clazz) != null;
    }
    
     <R> DecodeHelper<R> init(final GlideContext glideContext, final Object model, final Key signature, final int width, final int height, final DiskCacheStrategy diskCacheStrategy, final Class<?> resourceClass, final Class<R> transcodeClass, final Priority priority, final Options options, final Map<Class<?>, Transformation<?>> transformations, final boolean isTransformationRequired, final boolean isScaleOnlyOrNoTransform, final DecodeJob.DiskCacheProvider diskCacheProvider) {
        this.glideContext = glideContext;
        this.model = model;
        this.signature = signature;
        this.width = width;
        this.height = height;
        this.diskCacheStrategy = diskCacheStrategy;
        this.resourceClass = resourceClass;
        this.diskCacheProvider = diskCacheProvider;
        this.transcodeClass = (Class<Transcode>)transcodeClass;
        this.priority = priority;
        this.options = options;
        this.transformations = transformations;
        this.isTransformationRequired = isTransformationRequired;
        this.isScaleOnlyOrNoTransform = isScaleOnlyOrNoTransform;
        return (DecodeHelper<R>)this;
    }
    
    boolean isResourceEncoderAvailable(final Resource<?> resource) {
        return this.glideContext.getRegistry().isResourceEncoderAvailable(resource);
    }
    
    boolean isScaleOnlyOrNoTransform() {
        return this.isScaleOnlyOrNoTransform;
    }
    
    boolean isSourceKey(final Key key) {
        final List<ModelLoader.LoadData<?>> loadData = this.getLoadData();
        for (int size = loadData.size(), i = 0; i < size; ++i) {
            if (((ModelLoader.LoadData)(ModelLoader.LoadData)loadData.get(i)).sourceKey.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
