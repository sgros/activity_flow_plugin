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

public class FaviconDecoder implements ResourceDecoder<FaviconModel, Bitmap> {
    private Context context;
    private Glide glide;

    public boolean handles(FaviconModel faviconModel, Options options) {
        return true;
    }

    public FaviconDecoder(Context context, Glide glide) {
        this.context = context;
        this.glide = glide;
    }

    public Resource<Bitmap> decode(FaviconModel faviconModel, int i, int i2, Options options) {
        if (faviconModel.type == 2) {
            return null;
        }
        Bitmap refinedBitmap = DimenUtils.getRefinedBitmap(this.context.getResources(), faviconModel.originalIcon, FavIconUtils.getRepresentativeCharacter(faviconModel.url));
        if (refinedBitmap == faviconModel.originalIcon) {
            refinedBitmap = Bitmap.createBitmap(faviconModel.originalIcon);
        }
        return BitmapResource.obtain(refinedBitmap, this.glide.getBitmapPool());
    }
}
