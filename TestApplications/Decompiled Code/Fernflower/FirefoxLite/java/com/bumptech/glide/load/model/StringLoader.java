package com.bumptech.glide.load.model;

import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import java.io.File;
import java.io.InputStream;

public class StringLoader implements ModelLoader {
   private final ModelLoader uriLoader;

   public StringLoader(ModelLoader var1) {
      this.uriLoader = var1;
   }

   private static Uri parseUri(String var0) {
      if (TextUtils.isEmpty(var0)) {
         return null;
      } else {
         Uri var2;
         if (var0.startsWith("/")) {
            var2 = toFileUri(var0);
         } else {
            Uri var1 = Uri.parse(var0);
            if (var1.getScheme() == null) {
               var2 = toFileUri(var0);
            } else {
               var2 = var1;
            }
         }

         return var2;
      }
   }

   private static Uri toFileUri(String var0) {
      return Uri.fromFile(new File(var0));
   }

   public ModelLoader.LoadData buildLoadData(String var1, int var2, int var3, Options var4) {
      Uri var5 = parseUri(var1);
      ModelLoader.LoadData var6;
      if (var5 == null) {
         var6 = null;
      } else {
         var6 = this.uriLoader.buildLoadData(var5, var2, var3, var4);
      }

      return var6;
   }

   public boolean handles(String var1) {
      return true;
   }

   public static class FileDescriptorFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new StringLoader(var1.build(Uri.class, ParcelFileDescriptor.class));
      }
   }

   public static class StreamFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new StringLoader(var1.build(Uri.class, InputStream.class));
      }
   }
}
