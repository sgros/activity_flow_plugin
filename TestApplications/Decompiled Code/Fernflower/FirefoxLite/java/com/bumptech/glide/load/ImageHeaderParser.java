package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.InputStream;

public interface ImageHeaderParser {
   int getOrientation(InputStream var1, ArrayPool var2) throws IOException;

   ImageHeaderParser.ImageType getType(InputStream var1) throws IOException;

   public static enum ImageType {
      GIF(true),
      JPEG(false),
      PNG(false),
      PNG_A(true),
      RAW(false),
      UNKNOWN(false),
      WEBP(false),
      WEBP_A(true);

      private final boolean hasAlpha;

      private ImageType(boolean var3) {
         this.hasAlpha = var3;
      }

      public boolean hasAlpha() {
         return this.hasAlpha;
      }
   }
}
