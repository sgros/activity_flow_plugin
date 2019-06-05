package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ManifestParser {
   private final Context context;

   public ManifestParser(Context var1) {
      this.context = var1;
   }

   private static GlideModule parseModule(String var0) {
      Class var6;
      try {
         var6 = Class.forName(var0);
      } catch (ClassNotFoundException var5) {
         throw new IllegalArgumentException("Unable to find GlideModule implementation", var5);
      }

      StringBuilder var1;
      Object var8;
      try {
         var8 = var6.newInstance();
      } catch (InstantiationException var3) {
         var1 = new StringBuilder();
         var1.append("Unable to instantiate GlideModule implementation for ");
         var1.append(var6);
         throw new RuntimeException(var1.toString(), var3);
      } catch (IllegalAccessException var4) {
         var1 = new StringBuilder();
         var1.append("Unable to instantiate GlideModule implementation for ");
         var1.append(var6);
         throw new RuntimeException(var1.toString(), var4);
      }

      if (var8 instanceof GlideModule) {
         return (GlideModule)var8;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("Expected instanceof GlideModule, but found: ");
         var7.append(var8);
         throw new RuntimeException(var7.toString());
      }
   }

   public List parse() {
      if (Log.isLoggable("ManifestParser", 3)) {
         Log.d("ManifestParser", "Loading Glide modules");
      }

      ArrayList var1 = new ArrayList();

      label64: {
         NameNotFoundException var10000;
         label70: {
            ApplicationInfo var2;
            boolean var10001;
            try {
               var2 = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
               if (var2.metaData == null) {
                  if (Log.isLoggable("ManifestParser", 3)) {
                     Log.d("ManifestParser", "Got null app info metadata");
                  }

                  return var1;
               }
            } catch (NameNotFoundException var9) {
               var10000 = var9;
               var10001 = false;
               break label70;
            }

            StringBuilder var3;
            try {
               if (Log.isLoggable("ManifestParser", 2)) {
                  var3 = new StringBuilder();
                  var3.append("Got app info metadata: ");
                  var3.append(var2.metaData);
                  Log.v("ManifestParser", var3.toString());
               }
            } catch (NameNotFoundException var8) {
               var10000 = var8;
               var10001 = false;
               break label70;
            }

            Iterator var4;
            try {
               var4 = var2.metaData.keySet().iterator();
            } catch (NameNotFoundException var7) {
               var10000 = var7;
               var10001 = false;
               break label70;
            }

            while(true) {
               try {
                  String var5;
                  do {
                     do {
                        if (!var4.hasNext()) {
                           break label64;
                        }

                        var5 = (String)var4.next();
                     } while(!"GlideModule".equals(var2.metaData.get(var5)));

                     var1.add(parseModule(var5));
                  } while(!Log.isLoggable("ManifestParser", 3));

                  var3 = new StringBuilder();
                  var3.append("Loaded Glide module: ");
                  var3.append(var5);
                  Log.d("ManifestParser", var3.toString());
               } catch (NameNotFoundException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }
            }
         }

         NameNotFoundException var10 = var10000;
         throw new RuntimeException("Unable to find metadata to parse GlideModules", var10);
      }

      if (Log.isLoggable("ManifestParser", 3)) {
         Log.d("ManifestParser", "Finished loading Glide modules");
      }

      return var1;
   }
}
