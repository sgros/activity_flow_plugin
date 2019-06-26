package org.aspectj.runtime.reflect;

import java.lang.ref.SoftReference;
import java.util.StringTokenizer;
import org.aspectj.lang.Signature;

abstract class SignatureImpl implements Signature {
   static Class[] EMPTY_CLASS_ARRAY = new Class[0];
   static String[] EMPTY_STRING_ARRAY = new String[0];
   private static boolean useCache;
   Class declaringType;
   String declaringTypeName;
   ClassLoader lookupClassLoader = null;
   int modifiers = -1;
   String name;
   SignatureImpl.Cache stringCache;
   private String stringRep;

   SignatureImpl(int var1, String var2, Class var3) {
      this.modifiers = var1;
      this.name = var2;
      this.declaringType = var3;
   }

   private ClassLoader getLookupClassLoader() {
      if (this.lookupClassLoader == null) {
         this.lookupClassLoader = this.getClass().getClassLoader();
      }

      return this.lookupClassLoader;
   }

   protected abstract String createToString(StringMaker var1);

   int extractInt(int var1) {
      return Integer.parseInt(this.extractString(var1), 16);
   }

   String extractString(int var1) {
      int var2 = this.stringRep.indexOf(45);
      int var3 = 0;
      int var4 = var1;

      for(var1 = var2; var4 > 0; --var4) {
         var3 = var1 + 1;
         var1 = this.stringRep.indexOf(45, var3);
      }

      var4 = var1;
      if (var1 == -1) {
         var4 = this.stringRep.length();
      }

      return this.stringRep.substring(var3, var4);
   }

   Class extractType(int var1) {
      return Factory.makeClass(this.extractString(var1), this.getLookupClassLoader());
   }

   Class[] extractTypes(int var1) {
      StringTokenizer var2 = new StringTokenizer(this.extractString(var1), ":");
      int var3 = var2.countTokens();
      Class[] var4 = new Class[var3];

      for(var1 = 0; var1 < var3; ++var1) {
         var4[var1] = Factory.makeClass(var2.nextToken(), this.getLookupClassLoader());
      }

      return var4;
   }

   public Class getDeclaringType() {
      if (this.declaringType == null) {
         this.declaringType = this.extractType(2);
      }

      return this.declaringType;
   }

   public String getDeclaringTypeName() {
      if (this.declaringTypeName == null) {
         this.declaringTypeName = this.getDeclaringType().getName();
      }

      return this.declaringTypeName;
   }

   public int getModifiers() {
      if (this.modifiers == -1) {
         this.modifiers = this.extractInt(0);
      }

      return this.modifiers;
   }

   public String getName() {
      if (this.name == null) {
         this.name = this.extractString(1);
      }

      return this.name;
   }

   public final String toString() {
      return this.toString(StringMaker.middleStringMaker);
   }

   String toString(StringMaker var1) {
      String var6;
      label27: {
         if (useCache) {
            SignatureImpl.Cache var2 = this.stringCache;
            if (var2 != null) {
               var6 = var2.get(var1.cacheOffset);
               break label27;
            }

            try {
               SignatureImpl.CacheImpl var5 = new SignatureImpl.CacheImpl();
               this.stringCache = var5;
            } catch (Throwable var4) {
               useCache = false;
            }
         }

         var6 = null;
      }

      String var3 = var6;
      if (var6 == null) {
         var3 = this.createToString(var1);
      }

      if (useCache) {
         this.stringCache.set(var1.cacheOffset, var3);
      }

      return var3;
   }

   private interface Cache {
      String get(int var1);

      void set(int var1, String var2);
   }

   private static final class CacheImpl implements SignatureImpl.Cache {
      private SoftReference toStringCacheRef;

      public CacheImpl() {
         this.makeCache();
      }

      private String[] array() {
         return (String[])this.toStringCacheRef.get();
      }

      private String[] makeCache() {
         String[] var1 = new String[3];
         this.toStringCacheRef = new SoftReference(var1);
         return var1;
      }

      public String get(int var1) {
         String[] var2 = this.array();
         return var2 == null ? null : var2[var1];
      }

      public void set(int var1, String var2) {
         String[] var3 = this.array();
         String[] var4 = var3;
         if (var3 == null) {
            var4 = this.makeCache();
         }

         var4[var1] = var2;
      }
   }
}
