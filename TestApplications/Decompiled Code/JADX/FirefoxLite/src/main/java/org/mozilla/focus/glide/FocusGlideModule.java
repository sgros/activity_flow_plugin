package org.mozilla.focus.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream;
import org.mozilla.focus.screenshot.RegionFileDecoder;
import org.mozilla.focus.tabs.tabtray.FaviconDecoder;
import org.mozilla.focus.tabs.tabtray.FaviconModel;
import org.mozilla.focus.tabs.tabtray.FaviconModelLoaderFactory;

public class FocusGlideModule extends AppGlideModule {
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.prepend(InputStream.class, Bitmap.class, new RegionFileDecoder(glide, getScreenWidth(context)));
        registry.prepend(FaviconModel.class, FaviconModel.class, new FaviconModelLoaderFactory());
        registry.prepend(FaviconModel.class, Bitmap.class, new FaviconDecoder(context, glide));
    }

    private int getScreenWidth(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x;
    }
}
