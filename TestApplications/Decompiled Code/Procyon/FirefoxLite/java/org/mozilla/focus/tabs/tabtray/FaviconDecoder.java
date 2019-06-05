// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;
import java.io.IOException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.Glide;
import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.ResourceDecoder;

public class FaviconDecoder implements ResourceDecoder<FaviconModel, Bitmap>
{
    private Context context;
    private Glide glide;
    
    public FaviconDecoder(final Context context, final Glide glide) {
        this.context = context;
        this.glide = glide;
    }
    
    @Override
    public Resource<Bitmap> decode(final FaviconModel faviconModel, final int n, final int n2, final Options options) {
        if (faviconModel.type == 2) {
            return null;
        }
        Bitmap bitmap;
        if ((bitmap = DimenUtils.getRefinedBitmap(this.context.getResources(), faviconModel.originalIcon, FavIconUtils.getRepresentativeCharacter(faviconModel.url))) == faviconModel.originalIcon) {
            bitmap = Bitmap.createBitmap(faviconModel.originalIcon);
        }
        return BitmapResource.obtain(bitmap, this.glide.getBitmapPool());
    }
    
    @Override
    public boolean handles(final FaviconModel faviconModel, final Options options) {
        return true;
    }
}
