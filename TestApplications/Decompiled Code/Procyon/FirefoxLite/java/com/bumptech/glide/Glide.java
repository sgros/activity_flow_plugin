// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import com.bumptech.glide.request.target.Target;
import android.content.res.Configuration;
import com.bumptech.glide.util.Util;
import android.support.v4.app.Fragment;
import android.app.Activity;
import java.util.Iterator;
import java.util.Set;
import android.content.ComponentCallbacks;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import java.util.Collections;
import com.bumptech.glide.util.Preconditions;
import android.util.Log;
import android.content.res.Resources;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import java.net.URL;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.stream.HttpUriLoader;
import android.net.Uri;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import java.io.File;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import android.graphics.Bitmap;
import com.bumptech.glide.load.model.StreamEncoder;
import java.io.InputStream;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import java.nio.ByteBuffer;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.DecodeFormat;
import java.util.ArrayList;
import java.util.Map;
import com.bumptech.glide.request.RequestOptions;
import android.content.Context;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import java.util.List;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;

@TargetApi(14)
public class Glide implements ComponentCallbacks2
{
    private static volatile Glide glide;
    private static volatile boolean isInitializing;
    private final ArrayPool arrayPool;
    private final BitmapPool bitmapPool;
    private final BitmapPreFiller bitmapPreFiller;
    private final ConnectivityMonitorFactory connectivityMonitorFactory;
    private final Engine engine;
    private final GlideContext glideContext;
    private final List<RequestManager> managers;
    private final MemoryCache memoryCache;
    private MemoryCategory memoryCategory;
    private final Registry registry;
    private final RequestManagerRetriever requestManagerRetriever;
    
    @TargetApi(14)
    Glide(final Context context, final Engine engine, final MemoryCache memoryCache, final BitmapPool bitmapPool, final ArrayPool arrayPool, final RequestManagerRetriever requestManagerRetriever, final ConnectivityMonitorFactory connectivityMonitorFactory, final int n, final RequestOptions requestOptions, final Map<Class<?>, TransitionOptions<?, ?>> map) {
        this.managers = new ArrayList<RequestManager>();
        this.memoryCategory = MemoryCategory.NORMAL;
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
        this.memoryCache = memoryCache;
        this.requestManagerRetriever = requestManagerRetriever;
        this.connectivityMonitorFactory = connectivityMonitorFactory;
        this.bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, requestOptions.getOptions().get(Downsampler.DECODE_FORMAT));
        final Resources resources = context.getResources();
        (this.registry = new Registry()).register(new DefaultImageHeaderParser());
        final Downsampler downsampler = new Downsampler(this.registry.getImageHeaderParsers(), resources.getDisplayMetrics(), bitmapPool, arrayPool);
        this.registry.append(ByteBuffer.class, new ByteBufferEncoder()).append(InputStream.class, new StreamEncoder(arrayPool)).append("Bitmap", ByteBuffer.class, Bitmap.class, new ByteBufferBitmapDecoder(downsampler)).append("Bitmap", InputStream.class, Bitmap.class, new StreamBitmapDecoder(downsampler, arrayPool)).append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, new VideoBitmapDecoder(bitmapPool)).append(Bitmap.class, new BitmapEncoder()).append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<ByteBuffer>(resources, bitmapPool, (ResourceDecoder<Object, Bitmap>)new ByteBufferBitmapDecoder(downsampler))).append("BitmapDrawable", InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder<InputStream>(resources, bitmapPool, (ResourceDecoder<Object, Bitmap>)new StreamBitmapDecoder(downsampler, arrayPool))).append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder<ParcelFileDescriptor>(resources, bitmapPool, new VideoBitmapDecoder(bitmapPool))).append(BitmapDrawable.class, new BitmapDrawableEncoder(bitmapPool, new BitmapEncoder())).register(new ByteBufferRewinder.Factory()).append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory()).append(File.class, InputStream.class, new FileLoader.StreamFactory()).append(File.class, File.class, new FileDecoder()).append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory()).append(File.class, File.class, new UnitModelLoader.Factory<File>()).register(new InputStreamRewinder.Factory(arrayPool)).append(Integer.TYPE, InputStream.class, new ResourceLoader.StreamFactory(resources)).append(Integer.TYPE, ParcelFileDescriptor.class, new ResourceLoader.FileDescriptorFactory(resources)).append(Integer.class, InputStream.class, new ResourceLoader.StreamFactory(resources)).append(Integer.class, ParcelFileDescriptor.class, new ResourceLoader.FileDescriptorFactory(resources)).append(String.class, InputStream.class, new DataUrlLoader.StreamFactory()).append(String.class, InputStream.class, new StringLoader.StreamFactory()).append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory()).append(Uri.class, InputStream.class, new HttpUriLoader.Factory()).append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets())).append(Uri.class, ParcelFileDescriptor.class, new AssetUriLoader.FileDescriptorFactory(context.getAssets())).append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context)).append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context)).append(Uri.class, InputStream.class, new UriLoader.StreamFactory(context.getContentResolver())).append(Uri.class, ParcelFileDescriptor.class, new UriLoader.FileDescriptorFactory(context.getContentResolver())).append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory()).append(URL.class, InputStream.class, new UrlLoader.StreamFactory()).append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context)).append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory()).append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory()).append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory()).register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources, bitmapPool)).register(Bitmap.class, byte[].class, new BitmapBytesTranscoder());
        this.glideContext = new GlideContext(context, this.registry, new ImageViewTargetFactory(), requestOptions, map, engine, n);
    }
    
    private static void checkAndInitializeGlide(final Context context) {
        if (!Glide.isInitializing) {
            Glide.isInitializing = true;
            initializeGlide(context);
            Glide.isInitializing = false;
            return;
        }
        throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
    }
    
    public static Glide get(final Context context) {
        if (Glide.glide == null) {
            synchronized (Glide.class) {
                if (Glide.glide == null) {
                    checkAndInitializeGlide(context);
                }
            }
        }
        return Glide.glide;
    }
    
    private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules() {
        GeneratedAppGlideModule generatedAppGlideModule;
        try {
            generatedAppGlideModule = (GeneratedAppGlideModule)Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").newInstance();
        }
        catch (IllegalAccessException cause) {
            throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", cause);
        }
        catch (InstantiationException cause2) {
            throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", cause2);
        }
        catch (ClassNotFoundException ex) {
            if (Log.isLoggable("Glide", 5)) {
                Log.w("Glide", "Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
            }
            generatedAppGlideModule = null;
        }
        return generatedAppGlideModule;
    }
    
    private static RequestManagerRetriever getRetriever(final Context context) {
        Preconditions.checkNotNull(context, "You cannot start a load on a not yet attached View or a  Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
        return get(context).getRequestManagerRetriever();
    }
    
    private static void initializeGlide(final Context context) {
        final Context applicationContext = context.getApplicationContext();
        final GeneratedAppGlideModule annotationGeneratedGlideModules = getAnnotationGeneratedGlideModules();
        List<GlideModule> list = Collections.emptyList();
        if (annotationGeneratedGlideModules == null || annotationGeneratedGlideModules.isManifestParsingEnabled()) {
            list = new ManifestParser(applicationContext).parse();
        }
        if (annotationGeneratedGlideModules != null && !annotationGeneratedGlideModules.getExcludedModuleClasses().isEmpty()) {
            final Set<Class<?>> excludedModuleClasses = annotationGeneratedGlideModules.getExcludedModuleClasses();
            final Iterator<GlideModule> iterator = list.iterator();
            while (iterator.hasNext()) {
                final GlideModule obj = iterator.next();
                if (!excludedModuleClasses.contains(obj.getClass())) {
                    continue;
                }
                if (Log.isLoggable("Glide", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("AppGlideModule excludes manifest GlideModule: ");
                    sb.append(obj);
                    Log.d("Glide", sb.toString());
                }
                iterator.remove();
            }
        }
        if (Log.isLoggable("Glide", 3)) {
            for (final GlideModule glideModule : list) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Discovered GlideModule from manifest: ");
                sb2.append(glideModule.getClass());
                Log.d("Glide", sb2.toString());
            }
        }
        RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
        if (annotationGeneratedGlideModules != null) {
            requestManagerFactory = annotationGeneratedGlideModules.getRequestManagerFactory();
        }
        else {
            requestManagerFactory = null;
        }
        final GlideBuilder setRequestManagerFactory = new GlideBuilder().setRequestManagerFactory(requestManagerFactory);
        final Iterator<GlideModule> iterator3 = list.iterator();
        while (iterator3.hasNext()) {
            iterator3.next().applyOptions(applicationContext, setRequestManagerFactory);
        }
        if (annotationGeneratedGlideModules != null) {
            annotationGeneratedGlideModules.applyOptions(applicationContext, setRequestManagerFactory);
        }
        final Glide build = setRequestManagerFactory.build(applicationContext);
        final Iterator<GlideModule> iterator4 = list.iterator();
        while (iterator4.hasNext()) {
            iterator4.next().registerComponents(applicationContext, build, build.registry);
        }
        if (annotationGeneratedGlideModules != null) {
            annotationGeneratedGlideModules.registerComponents(applicationContext, build, build.registry);
        }
        context.getApplicationContext().registerComponentCallbacks((ComponentCallbacks)build);
        Glide.glide = build;
    }
    
    public static RequestManager with(final Activity activity) {
        return getRetriever((Context)activity).get(activity);
    }
    
    public static RequestManager with(final Context context) {
        return getRetriever(context).get(context);
    }
    
    public static RequestManager with(final Fragment fragment) {
        return getRetriever((Context)fragment.getActivity()).get(fragment);
    }
    
    public void clearMemory() {
        Util.assertMainThread();
        this.memoryCache.clearMemory();
        this.bitmapPool.clearMemory();
        this.arrayPool.clearMemory();
    }
    
    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }
    
    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }
    
    ConnectivityMonitorFactory getConnectivityMonitorFactory() {
        return this.connectivityMonitorFactory;
    }
    
    GlideContext getGlideContext() {
        return this.glideContext;
    }
    
    public Registry getRegistry() {
        return this.registry;
    }
    
    public RequestManagerRetriever getRequestManagerRetriever() {
        return this.requestManagerRetriever;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
    }
    
    public void onLowMemory() {
        this.clearMemory();
    }
    
    public void onTrimMemory(final int n) {
        this.trimMemory(n);
    }
    
    void registerRequestManager(final RequestManager requestManager) {
        synchronized (this.managers) {
            if (!this.managers.contains(requestManager)) {
                this.managers.add(requestManager);
                return;
            }
            throw new IllegalStateException("Cannot register already registered manager");
        }
    }
    
    void removeFromManagers(final Target<?> target) {
        synchronized (this.managers) {
            final Iterator<RequestManager> iterator = this.managers.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().untrack(target)) {
                    return;
                }
            }
            // monitorexit(this.managers)
            throw new IllegalStateException("Failed to remove target from managers");
        }
    }
    
    public void trimMemory(final int n) {
        Util.assertMainThread();
        this.memoryCache.trimMemory(n);
        this.bitmapPool.trimMemory(n);
        this.arrayPool.trimMemory(n);
    }
    
    void unregisterRequestManager(final RequestManager requestManager) {
        synchronized (this.managers) {
            if (this.managers.contains(requestManager)) {
                this.managers.remove(requestManager);
                return;
            }
            throw new IllegalStateException("Cannot register not yet registered manager");
        }
    }
}
