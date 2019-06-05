package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public final class ImageHeaderParserUtils {
   public static int getOrientation(List var0, InputStream var1, ArrayPool var2) throws IOException {
      if (var1 == null) {
         return -1;
      } else {
         Object var3 = var1;
         if (!var1.markSupported()) {
            var3 = new RecyclableBufferedInputStream(var1, var2);
         }

         ((InputStream)var3).mark(5242880);
         Iterator var8 = var0.iterator();

         while(var8.hasNext()) {
            ImageHeaderParser var9 = (ImageHeaderParser)var8.next();
            boolean var6 = false;

            int var4;
            try {
               var6 = true;
               var4 = var9.getOrientation((InputStream)var3, var2);
               var6 = false;
            } finally {
               if (var6) {
                  ((InputStream)var3).reset();
               }
            }

            if (var4 != -1) {
               ((InputStream)var3).reset();
               return var4;
            }

            ((InputStream)var3).reset();
         }

         return -1;
      }
   }

   public static ImageHeaderParser.ImageType getType(List var0, InputStream var1, ArrayPool var2) throws IOException {
      if (var1 == null) {
         return ImageHeaderParser.ImageType.UNKNOWN;
      } else {
         Object var3 = var1;
         if (!var1.markSupported()) {
            var3 = new RecyclableBufferedInputStream(var1, var2);
         }

         ((InputStream)var3).mark(5242880);
         Iterator var7 = var0.iterator();

         while(var7.hasNext()) {
            ImageHeaderParser var8 = (ImageHeaderParser)var7.next();
            boolean var5 = false;

            ImageHeaderParser.ImageType var9;
            ImageHeaderParser.ImageType var10;
            try {
               var5 = true;
               var10 = var8.getType((InputStream)var3);
               var9 = ImageHeaderParser.ImageType.UNKNOWN;
               var5 = false;
            } finally {
               if (var5) {
                  ((InputStream)var3).reset();
               }
            }

            if (var10 != var9) {
               ((InputStream)var3).reset();
               return var10;
            }

            ((InputStream)var3).reset();
         }

         return ImageHeaderParser.ImageType.UNKNOWN;
      }
   }
}
