package org.mozilla.focus.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream;
import org.mozilla.focus.screenshot.RegionFileDecoder;
import org.mozilla.focus.tabs.tabtray.FaviconDecoder;
import org.mozilla.focus.tabs.tabtray.FaviconModel;
import org.mozilla.focus.tabs.tabtray.FaviconModelLoaderFactory;

public class FocusGlideModule extends AppGlideModule {
   private int getScreenWidth(Context var1) {
      Display var2 = ((WindowManager)var1.getSystemService("window")).getDefaultDisplay();
      Point var3 = new Point();
      var2.getSize(var3);
      return var3.x;
   }

   public void registerComponents(Context var1, Glide var2, Registry var3) {
      var3.prepend(InputStream.class, Bitmap.class, (ResourceDecoder)(new RegionFileDecoder(var2, this.getScreenWidth(var1))));
      var3.prepend(FaviconModel.class, FaviconModel.class, (ModelLoaderFactory)(new FaviconModelLoaderFactory()));
      var3.prepend(FaviconModel.class, Bitmap.class, (ResourceDecoder)(new FaviconDecoder(var1, var2)));
   }
}
