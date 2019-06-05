package org.mozilla.focus.tabs.tabtray;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;

public class FaviconDecoder implements ResourceDecoder {
   private Context context;
   private Glide glide;

   public FaviconDecoder(Context var1, Glide var2) {
      this.context = var1;
      this.glide = var2;
   }

   public Resource decode(FaviconModel var1, int var2, int var3, Options var4) {
      if (var1.type == 2) {
         return null;
      } else {
         Bitmap var5 = DimenUtils.getRefinedBitmap(this.context.getResources(), var1.originalIcon, FavIconUtils.getRepresentativeCharacter(var1.url));
         Bitmap var6 = var5;
         if (var5 == var1.originalIcon) {
            var6 = Bitmap.createBitmap(var1.originalIcon);
         }

         return BitmapResource.obtain(var6, this.glide.getBitmapPool());
      }
   }

   public boolean handles(FaviconModel var1, Options var2) {
      return true;
   }
}
