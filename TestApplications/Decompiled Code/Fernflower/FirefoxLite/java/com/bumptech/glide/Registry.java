package com.bumptech.glide;

import android.support.v4.util.Pools;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
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
import java.util.Iterator;
import java.util.List;

public class Registry {
   private final DataRewinderRegistry dataRewinderRegistry;
   private final ResourceDecoderRegistry decoderRegistry;
   private final EncoderRegistry encoderRegistry;
   private final Pools.Pool exceptionListPool = FactoryPools.threadSafeList();
   private final ImageHeaderParserRegistry imageHeaderParserRegistry;
   private final LoadPathCache loadPathCache = new LoadPathCache();
   private final ModelLoaderRegistry modelLoaderRegistry;
   private final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
   private final ResourceEncoderRegistry resourceEncoderRegistry;
   private final TranscoderRegistry transcoderRegistry;

   public Registry() {
      this.modelLoaderRegistry = new ModelLoaderRegistry(this.exceptionListPool);
      this.encoderRegistry = new EncoderRegistry();
      this.decoderRegistry = new ResourceDecoderRegistry();
      this.resourceEncoderRegistry = new ResourceEncoderRegistry();
      this.dataRewinderRegistry = new DataRewinderRegistry();
      this.transcoderRegistry = new TranscoderRegistry();
      this.imageHeaderParserRegistry = new ImageHeaderParserRegistry();
      this.setResourceDecoderBucketPriorityList(Arrays.asList("Gif", "Bitmap", "BitmapDrawable"));
   }

   private List getDecodePaths(Class var1, Class var2, Class var3) {
      ArrayList var4 = new ArrayList();
      Iterator var5 = this.decoderRegistry.getResourceClasses(var1, var2).iterator();

      while(var5.hasNext()) {
         Class var6 = (Class)var5.next();
         Iterator var7 = this.transcoderRegistry.getTranscodeClasses(var6, var3).iterator();

         while(var7.hasNext()) {
            var2 = (Class)var7.next();
            var4.add(new DecodePath(var1, var6, var2, this.decoderRegistry.getDecoders(var1, var6), this.transcoderRegistry.get(var6, var2), this.exceptionListPool));
         }
      }

      return var4;
   }

   public Registry append(Class var1, Encoder var2) {
      this.encoderRegistry.append(var1, var2);
      return this;
   }

   public Registry append(Class var1, ResourceEncoder var2) {
      this.resourceEncoderRegistry.append(var1, var2);
      return this;
   }

   public Registry append(Class var1, Class var2, ResourceDecoder var3) {
      this.append("legacy_append", var1, var2, var3);
      return this;
   }

   public Registry append(Class var1, Class var2, ModelLoaderFactory var3) {
      this.modelLoaderRegistry.append(var1, var2, var3);
      return this;
   }

   public Registry append(String var1, Class var2, Class var3, ResourceDecoder var4) {
      this.decoderRegistry.append(var1, var4, var2, var3);
      return this;
   }

   public List getImageHeaderParsers() {
      List var1 = this.imageHeaderParserRegistry.getParsers();
      if (!var1.isEmpty()) {
         return var1;
      } else {
         throw new Registry.NoImageHeaderParserException();
      }
   }

   public LoadPath getLoadPath(Class var1, Class var2, Class var3) {
      LoadPath var4 = this.loadPathCache.get(var1, var2, var3);
      LoadPath var5 = var4;
      if (var4 == null) {
         var5 = var4;
         if (!this.loadPathCache.contains(var1, var2, var3)) {
            List var6 = this.getDecodePaths(var1, var2, var3);
            if (var6.isEmpty()) {
               var5 = null;
            } else {
               var5 = new LoadPath(var1, var2, var3, var6, this.exceptionListPool);
            }

            this.loadPathCache.put(var1, var2, var3, var5);
         }
      }

      return var5;
   }

   public List getModelLoaders(Object var1) {
      List var2 = this.modelLoaderRegistry.getModelLoaders(var1);
      if (!var2.isEmpty()) {
         return var2;
      } else {
         throw new Registry.NoModelLoaderAvailableException(var1);
      }
   }

   public List getRegisteredResourceClasses(Class var1, Class var2, Class var3) {
      List var4 = this.modelToResourceClassCache.get(var1, var2);
      Object var5 = var4;
      if (var4 == null) {
         var5 = new ArrayList();
         Iterator var8 = this.modelLoaderRegistry.getDataClasses(var1).iterator();

         while(var8.hasNext()) {
            Class var6 = (Class)var8.next();
            Iterator var9 = this.decoderRegistry.getResourceClasses(var6, var2).iterator();

            while(var9.hasNext()) {
               Class var7 = (Class)var9.next();
               if (!this.transcoderRegistry.getTranscodeClasses(var7, var3).isEmpty() && !((List)var5).contains(var7)) {
                  ((List)var5).add(var7);
               }
            }
         }

         this.modelToResourceClassCache.put(var1, var2, Collections.unmodifiableList((List)var5));
      }

      return (List)var5;
   }

   public ResourceEncoder getResultEncoder(Resource var1) throws Registry.NoResultEncoderAvailableException {
      ResourceEncoder var2 = this.resourceEncoderRegistry.get(var1.getResourceClass());
      if (var2 != null) {
         return var2;
      } else {
         throw new Registry.NoResultEncoderAvailableException(var1.getResourceClass());
      }
   }

   public DataRewinder getRewinder(Object var1) {
      return this.dataRewinderRegistry.build(var1);
   }

   public Encoder getSourceEncoder(Object var1) throws Registry.NoSourceEncoderAvailableException {
      Encoder var2 = this.encoderRegistry.getEncoder(var1.getClass());
      if (var2 != null) {
         return var2;
      } else {
         throw new Registry.NoSourceEncoderAvailableException(var1.getClass());
      }
   }

   public boolean isResourceEncoderAvailable(Resource var1) {
      boolean var2;
      if (this.resourceEncoderRegistry.get(var1.getResourceClass()) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Registry prepend(Class var1, Class var2, ResourceDecoder var3) {
      this.prepend("legacy_prepend_all", var1, var2, var3);
      return this;
   }

   public Registry prepend(Class var1, Class var2, ModelLoaderFactory var3) {
      this.modelLoaderRegistry.prepend(var1, var2, var3);
      return this;
   }

   public Registry prepend(String var1, Class var2, Class var3, ResourceDecoder var4) {
      this.decoderRegistry.prepend(var1, var4, var2, var3);
      return this;
   }

   public Registry register(ImageHeaderParser var1) {
      this.imageHeaderParserRegistry.add(var1);
      return this;
   }

   public Registry register(DataRewinder.Factory var1) {
      this.dataRewinderRegistry.register(var1);
      return this;
   }

   public Registry register(Class var1, Class var2, ResourceTranscoder var3) {
      this.transcoderRegistry.register(var1, var2, var3);
      return this;
   }

   public final Registry setResourceDecoderBucketPriorityList(List var1) {
      ArrayList var2 = new ArrayList(var1);
      var2.add(0, "legacy_prepend_all");
      var2.add("legacy_append");
      this.decoderRegistry.setBucketPriorityList(var2);
      return this;
   }

   public static class MissingComponentException extends RuntimeException {
      public MissingComponentException(String var1) {
         super(var1);
      }
   }

   public static final class NoImageHeaderParserException extends Registry.MissingComponentException {
      public NoImageHeaderParserException() {
         super("Failed to find image header parser.");
      }
   }

   public static class NoModelLoaderAvailableException extends Registry.MissingComponentException {
      public NoModelLoaderAvailableException(Class var1, Class var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Failed to find any ModelLoaders for model: ");
         var3.append(var1);
         var3.append(" and data: ");
         var3.append(var2);
         super(var3.toString());
      }

      public NoModelLoaderAvailableException(Object var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to find any ModelLoaders for model: ");
         var2.append(var1);
         super(var2.toString());
      }
   }

   public static class NoResultEncoderAvailableException extends Registry.MissingComponentException {
      public NoResultEncoderAvailableException(Class var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to find result encoder for resource class: ");
         var2.append(var1);
         super(var2.toString());
      }
   }

   public static class NoSourceEncoderAvailableException extends Registry.MissingComponentException {
      public NoSourceEncoderAvailableException(Class var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to find source encoder for data class: ");
         var2.append(var1);
         super(var2.toString());
      }
   }
}
