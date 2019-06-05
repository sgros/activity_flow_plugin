// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.glide;

import org.mozilla.focus.tabs.tabtray.FaviconDecoder;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import org.mozilla.focus.tabs.tabtray.FaviconModelLoaderFactory;
import org.mozilla.focus.tabs.tabtray.FaviconModel;
import com.bumptech.glide.load.ResourceDecoder;
import org.mozilla.focus.screenshot.RegionFileDecoder;
import android.graphics.Bitmap;
import java.io.InputStream;
import com.bumptech.glide.Registry;
import com.bumptech.glide.Glide;
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;
import android.content.Context;
import com.bumptech.glide.module.AppGlideModule;

public class FocusGlideModule extends AppGlideModule
{
    private int getScreenWidth(final Context context) {
        final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x;
    }
    
    @Override
    public void registerComponents(final Context context, final Glide glide, final Registry registry) {
        registry.prepend(InputStream.class, Bitmap.class, new RegionFileDecoder(glide, this.getScreenWidth(context)));
        registry.prepend(FaviconModel.class, FaviconModel.class, new FaviconModelLoaderFactory());
        registry.prepend(FaviconModel.class, Bitmap.class, new FaviconDecoder(context, glide));
    }
}
