package org.mozilla.focus.screenshot;

import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import java.io.IOException;
import java.io.InputStream;

public class RegionFileDecoder implements ResourceDecoder {
   private final BitmapPool bitmapPool;
   private final int defaultWidth;

   public RegionFileDecoder(Glide var1, int var2) {
      this(var1.getBitmapPool(), var2);
   }

   public RegionFileDecoder(BitmapPool var1, int var2) {
      this.bitmapPool = var1;
      this.defaultWidth = var2;
   }

   private BitmapRegionDecoder createDecoder(InputStream var1, int var2, int var3) throws IOException {
      return BitmapRegionDecoder.newInstance(var1, false);
   }

   public Resource decode(InputStream var1, int var2, int var3, Options var4) throws IOException {
      int var5 = this.defaultWidth;
      if (var1.markSupported()) {
         android.graphics.BitmapFactory.Options var7 = new android.graphics.BitmapFactory.Options();
         var7.inJustDecodeBounds = true;
         var1.mark(var1.available());
         BitmapFactory.decodeStream(var1, new Rect(), var7);
         var1.reset();
         var5 = var7.outWidth;
      }

      BitmapRegionDecoder var8 = this.createDecoder(var1, var2, var3);
      android.graphics.BitmapFactory.Options var6 = new android.graphics.BitmapFactory.Options();
      var2 = (int)Math.ceil((double)var5 / (double)var2);
      if (var2 == 0) {
         var2 = 0;
      } else {
         var2 = Integer.highestOneBit(var2);
      }

      var6.inSampleSize = Math.max(1, var2);
      return BitmapResource.obtain(var8.decodeRegion(new Rect(0, 0, var5, var5), var6), this.bitmapPool);
   }

   public boolean handles(InputStream var1, Options var2) {
      return true;
   }
}
