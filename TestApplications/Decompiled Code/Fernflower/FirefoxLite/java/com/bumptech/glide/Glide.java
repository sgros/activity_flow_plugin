package com.bumptech.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.stream.HttpUriLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TargetApi(14)
public class Glide implements ComponentCallbacks2 {
   private static volatile Glide glide;
   private static volatile boolean isInitializing;
   private final ArrayPool arrayPool;
   private final BitmapPool bitmapPool;
   private final BitmapPreFiller bitmapPreFiller;
   private final ConnectivityMonitorFactory connectivityMonitorFactory;
   private final Engine engine;
   private final GlideContext glideContext;
   private final List managers = new ArrayList();
   private final MemoryCache memoryCache;
   private MemoryCategory memoryCategory;
   private final Registry registry;
   private final RequestManagerRetriever requestManagerRetriever;

   @TargetApi(14)
   Glide(Context var1, Engine var2, MemoryCache var3, BitmapPool var4, ArrayPool var5, RequestManagerRetriever var6, ConnectivityMonitorFactory var7, int var8, RequestOptions var9, Map var10) {
      this.memoryCategory = MemoryCategory.NORMAL;
      this.engine = var2;
      this.bitmapPool = var4;
      this.arrayPool = var5;
      this.memoryCache = var3;
      this.requestManagerRetriever = var6;
      this.connectivityMonitorFactory = var7;
      this.bitmapPreFiller = new BitmapPreFiller(var3, var4, (DecodeFormat)var9.getOptions().get(Downsampler.DECODE_FORMAT));
      Resources var11 = var1.getResources();
      this.registry = new Registry();
      this.registry.register((ImageHeaderParser)(new DefaultImageHeaderParser()));
      Downsampler var13 = new Downsampler(this.registry.getImageHeaderParsers(), var11.getDisplayMetrics(), var4, var5);
      this.registry.append(ByteBuffer.class, (Encoder)(new ByteBufferEncoder())).append(InputStream.class, (Encoder)(new StreamEncoder(var5))).append("Bitmap", ByteBuffer.class, Bitmap.class, new ByteBufferBitmapDecoder(var13)).append("Bitmap", InputStream.class, Bitmap.class, new StreamBitmapDecoder(var13, var5)).append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, new VideoBitmapDecoder(var4)).append(Bitmap.class, (ResourceEncoder)(new BitmapEncoder())).append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder(var11, var4, new ByteBufferBitmapDecoder(var13))).append("BitmapDrawable", InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder(var11, var4, new StreamBitmapDecoder(var13, var5))).append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder(var11, var4, new VideoBitmapDecoder(var4))).append(BitmapDrawable.class, (ResourceEncoder)(new BitmapDrawableEncoder(var4, new BitmapEncoder()))).register((DataRewinder.Factory)(new ByteBufferRewinder.Factory())).append(File.class, ByteBuffer.class, (ModelLoaderFactory)(new ByteBufferFileLoader.Factory())).append(File.class, InputStream.class, (ModelLoaderFactory)(new FileLoader.StreamFactory())).append(File.class, File.class, (ResourceDecoder)(new FileDecoder())).append(File.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new FileLoader.FileDescriptorFactory())).append(File.class, File.class, (ModelLoaderFactory)(new UnitModelLoader.Factory())).register((DataRewinder.Factory)(new InputStreamRewinder.Factory(var5))).append(Integer.TYPE, InputStream.class, (ModelLoaderFactory)(new ResourceLoader.StreamFactory(var11))).append(Integer.TYPE, ParcelFileDescriptor.class, (ModelLoaderFactory)(new ResourceLoader.FileDescriptorFactory(var11))).append(Integer.class, InputStream.class, (ModelLoaderFactory)(new ResourceLoader.StreamFactory(var11))).append(Integer.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new ResourceLoader.FileDescriptorFactory(var11))).append(String.class, InputStream.class, (ModelLoaderFactory)(new DataUrlLoader.StreamFactory())).append(String.class, InputStream.class, (ModelLoaderFactory)(new StringLoader.StreamFactory())).append(String.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new StringLoader.FileDescriptorFactory())).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new HttpUriLoader.Factory())).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new AssetUriLoader.StreamFactory(var1.getAssets()))).append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new AssetUriLoader.FileDescriptorFactory(var1.getAssets()))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new MediaStoreImageThumbLoader.Factory(var1))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new MediaStoreVideoThumbLoader.Factory(var1))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new UriLoader.StreamFactory(var1.getContentResolver()))).append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new UriLoader.FileDescriptorFactory(var1.getContentResolver()))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new UrlUriLoader.StreamFactory())).append(URL.class, InputStream.class, (ModelLoaderFactory)(new UrlLoader.StreamFactory())).append(Uri.class, File.class, (ModelLoaderFactory)(new MediaStoreFileLoader.Factory(var1))).append(GlideUrl.class, InputStream.class, (ModelLoaderFactory)(new HttpGlideUrlLoader.Factory())).append(byte[].class, ByteBuffer.class, (ModelLoaderFactory)(new ByteArrayLoader.ByteBufferFactory())).append(byte[].class, InputStream.class, (ModelLoaderFactory)(new ByteArrayLoader.StreamFactory())).register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(var11, var4)).register(Bitmap.class, byte[].class, new BitmapBytesTranscoder());
      ImageViewTargetFactory var12 = new ImageViewTargetFactory();
      this.glideContext = new GlideContext(var1, this.registry, var12, var9, var10, var2, var8);
   }

   private static void checkAndInitializeGlide(Context var0) {
      if (!isInitializing) {
         isInitializing = true;
         initializeGlide(var0);
         isInitializing = false;
      } else {
         throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
      }
   }

   public static Glide get(Context var0) {
      if (glide == null) {
         synchronized(Glide.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (glide == null) {
                  checkAndInitializeGlide(var0);
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return glide;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      } else {
         return glide;
      }
   }

   private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules() {
      GeneratedAppGlideModule var0;
      try {
         var0 = (GeneratedAppGlideModule)Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").newInstance();
      } catch (ClassNotFoundException var1) {
         if (Log.isLoggable("Glide", 5)) {
            Log.w("Glide", "Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
         }

         var0 = null;
      } catch (InstantiationException var2) {
         throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", var2);
      } catch (IllegalAccessException var3) {
         throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", var3);
      }

      return var0;
   }

   private static RequestManagerRetriever getRetriever(Context var0) {
      Preconditions.checkNotNull(var0, "You cannot start a load on a not yet attached View or a  Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
      return get(var0).getRequestManagerRetriever();
   }

   private static void initializeGlide(Context var0) {
      Context var1 = var0.getApplicationContext();
      GeneratedAppGlideModule var2 = getAnnotationGeneratedGlideModules();
      List var3 = Collections.emptyList();
      if (var2 == null || var2.isManifestParsingEnabled()) {
         var3 = (new ManifestParser(var1)).parse();
      }

      Iterator var5;
      if (var2 != null && !var2.getExcludedModuleClasses().isEmpty()) {
         Set var4 = var2.getExcludedModuleClasses();
         var5 = var3.iterator();

         while(var5.hasNext()) {
            GlideModule var6 = (GlideModule)var5.next();
            if (var4.contains(var6.getClass())) {
               if (Log.isLoggable("Glide", 3)) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("AppGlideModule excludes manifest GlideModule: ");
                  var7.append(var6);
                  Log.d("Glide", var7.toString());
               }

               var5.remove();
            }
         }
      }

      if (Log.isLoggable("Glide", 3)) {
         Iterator var12 = var3.iterator();

         while(var12.hasNext()) {
            GlideModule var8 = (GlideModule)var12.next();
            StringBuilder var10 = new StringBuilder();
            var10.append("Discovered GlideModule from manifest: ");
            var10.append(var8.getClass());
            Log.d("Glide", var10.toString());
         }
      }

      RequestManagerRetriever.RequestManagerFactory var11;
      if (var2 != null) {
         var11 = var2.getRequestManagerFactory();
      } else {
         var11 = null;
      }

      GlideBuilder var13 = (new GlideBuilder()).setRequestManagerFactory(var11);
      var5 = var3.iterator();

      while(var5.hasNext()) {
         ((GlideModule)var5.next()).applyOptions(var1, var13);
      }

      if (var2 != null) {
         var2.applyOptions(var1, var13);
      }

      Glide var14 = var13.build(var1);
      Iterator var9 = var3.iterator();

      while(var9.hasNext()) {
         ((GlideModule)var9.next()).registerComponents(var1, var14, var14.registry);
      }

      if (var2 != null) {
         var2.registerComponents(var1, var14, var14.registry);
      }

      var0.getApplicationContext().registerComponentCallbacks(var14);
      glide = var14;
   }

   public static RequestManager with(Activity var0) {
      return getRetriever(var0).get(var0);
   }

   public static RequestManager with(Context var0) {
      return getRetriever(var0).get(var0);
   }

   public static RequestManager with(Fragment var0) {
      return getRetriever(var0.getActivity()).get(var0);
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

   public void onConfigurationChanged(Configuration var1) {
   }

   public void onLowMemory() {
      this.clearMemory();
   }

   public void onTrimMemory(int var1) {
      this.trimMemory(var1);
   }

   void registerRequestManager(RequestManager var1) {
      List var2 = this.managers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!this.managers.contains(var1)) {
               this.managers.add(var1);
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            IllegalStateException var16 = new IllegalStateException("Cannot register already registered manager");
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   void removeFromManagers(Target var1) {
      List var2 = this.managers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label216: {
         Iterator var3;
         try {
            var3 = this.managers.iterator();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label216;
         }

         try {
            while(var3.hasNext()) {
               if (((RequestManager)var3.next()).untrack(var1)) {
                  return;
               }
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label216;
         }

         label204:
         try {
            throw new IllegalStateException("Failed to remove target from managers");
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label204;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public void trimMemory(int var1) {
      Util.assertMainThread();
      this.memoryCache.trimMemory(var1);
      this.bitmapPool.trimMemory(var1);
      this.arrayPool.trimMemory(var1);
   }

   void unregisterRequestManager(RequestManager var1) {
      List var2 = this.managers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.managers.contains(var1)) {
               this.managers.remove(var1);
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            IllegalStateException var16 = new IllegalStateException("Cannot register not yet registered manager");
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }
}
