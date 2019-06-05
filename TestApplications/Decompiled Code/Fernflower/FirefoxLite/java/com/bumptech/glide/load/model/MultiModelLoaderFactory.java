package com.bumptech.glide.load.model;

import android.support.v4.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
   private static final MultiModelLoaderFactory.Factory DEFAULT_FACTORY = new MultiModelLoaderFactory.Factory();
   private static final ModelLoader EMPTY_MODEL_LOADER = new MultiModelLoaderFactory.EmptyModelLoader();
   private final Set alreadyUsedEntries;
   private final List entries;
   private final Pools.Pool exceptionListPool;
   private final MultiModelLoaderFactory.Factory factory;

   public MultiModelLoaderFactory(Pools.Pool var1) {
      this(var1, DEFAULT_FACTORY);
   }

   MultiModelLoaderFactory(Pools.Pool var1, MultiModelLoaderFactory.Factory var2) {
      this.entries = new ArrayList();
      this.alreadyUsedEntries = new HashSet();
      this.exceptionListPool = var1;
      this.factory = var2;
   }

   private void add(Class var1, Class var2, ModelLoaderFactory var3, boolean var4) {
      MultiModelLoaderFactory.Entry var6 = new MultiModelLoaderFactory.Entry(var1, var2, var3);
      List var7 = this.entries;
      int var5;
      if (var4) {
         var5 = this.entries.size();
      } else {
         var5 = 0;
      }

      var7.add(var5, var6);
   }

   private ModelLoader build(MultiModelLoaderFactory.Entry var1) {
      return (ModelLoader)Preconditions.checkNotNull(var1.factory.build(this));
   }

   private static ModelLoader emptyModelLoader() {
      return EMPTY_MODEL_LOADER;
   }

   void append(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.add(var1, var2, var3, true);
      } finally {
         ;
      }

   }

   public ModelLoader build(Class param1, Class param2) {
      // $FF: Couldn't be decompiled
   }

   List build(Class param1) {
      // $FF: Couldn't be decompiled
   }

   List getDataClasses(Class var1) {
      synchronized(this){}

      Throwable var10000;
      label107: {
         boolean var10001;
         ArrayList var2;
         Iterator var3;
         try {
            var2 = new ArrayList();
            var3 = this.entries.iterator();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label107;
         }

         while(true) {
            try {
               MultiModelLoaderFactory.Entry var4;
               do {
                  do {
                     if (!var3.hasNext()) {
                        return var2;
                     }

                     var4 = (MultiModelLoaderFactory.Entry)var3.next();
                  } while(var2.contains(var4.dataClass));
               } while(!var4.handles(var1));

               var2.add(var4.dataClass);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var11 = var10000;
      throw var11;
   }

   void prepend(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.add(var1, var2, var3, false);
      } finally {
         ;
      }

   }

   private static class EmptyModelLoader implements ModelLoader {
      EmptyModelLoader() {
      }

      public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
         return null;
      }

      public boolean handles(Object var1) {
         return false;
      }
   }

   private static class Entry {
      final Class dataClass;
      final ModelLoaderFactory factory;
      private final Class modelClass;

      public Entry(Class var1, Class var2, ModelLoaderFactory var3) {
         this.modelClass = var1;
         this.dataClass = var2;
         this.factory = var3;
      }

      public boolean handles(Class var1) {
         return this.modelClass.isAssignableFrom(var1);
      }

      public boolean handles(Class var1, Class var2) {
         boolean var3;
         if (this.handles(var1) && this.dataClass.isAssignableFrom(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   static class Factory {
      public MultiModelLoader build(List var1, Pools.Pool var2) {
         return new MultiModelLoader(var1, var2);
      }
   }
}
