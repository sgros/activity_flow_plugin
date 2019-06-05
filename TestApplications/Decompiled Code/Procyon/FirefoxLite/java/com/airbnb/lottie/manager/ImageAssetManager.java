// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.manager;

import java.util.Iterator;
import android.content.res.AssetManager;
import java.io.IOException;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.graphics.BitmapFactory$Options;
import android.graphics.Bitmap;
import java.util.HashMap;
import android.util.Log;
import android.view.View;
import android.text.TextUtils;
import android.graphics.drawable.Drawable$Callback;
import com.airbnb.lottie.LottieImageAsset;
import java.util.Map;
import com.airbnb.lottie.ImageAssetDelegate;
import android.content.Context;

public class ImageAssetManager
{
    private static final Object bitmapHashLock;
    private final Context context;
    private ImageAssetDelegate delegate;
    private final Map<String, LottieImageAsset> imageAssets;
    private String imagesFolder;
    
    static {
        bitmapHashLock = new Object();
    }
    
    public ImageAssetManager(final Drawable$Callback drawable$Callback, final String imagesFolder, final ImageAssetDelegate delegate, final Map<String, LottieImageAsset> imageAssets) {
        this.imagesFolder = imagesFolder;
        if (!TextUtils.isEmpty((CharSequence)imagesFolder) && this.imagesFolder.charAt(this.imagesFolder.length() - 1) != '/') {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.imagesFolder);
            sb.append('/');
            this.imagesFolder = sb.toString();
        }
        if (!(drawable$Callback instanceof View)) {
            Log.w("LOTTIE", "LottieDrawable must be inside of a view for images to work.");
            this.imageAssets = new HashMap<String, LottieImageAsset>();
            this.context = null;
            return;
        }
        this.context = ((View)drawable$Callback).getContext();
        this.imageAssets = imageAssets;
        this.setDelegate(delegate);
    }
    
    private Bitmap putBitmap(final String s, final Bitmap bitmap) {
        synchronized (ImageAssetManager.bitmapHashLock) {
            this.imageAssets.get(s).setBitmap(bitmap);
            return bitmap;
        }
    }
    
    public Bitmap bitmapForId(final String s) {
        final LottieImageAsset lottieImageAsset = this.imageAssets.get(s);
        if (lottieImageAsset == null) {
            return null;
        }
        final Bitmap bitmap = lottieImageAsset.getBitmap();
        if (bitmap != null) {
            return bitmap;
        }
        if (this.delegate != null) {
            final Bitmap fetchBitmap = this.delegate.fetchBitmap(lottieImageAsset);
            if (fetchBitmap != null) {
                this.putBitmap(s, fetchBitmap);
            }
            return fetchBitmap;
        }
        final String fileName = lottieImageAsset.getFileName();
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inScaled = true;
        bitmapFactory$Options.inDensity = 160;
        if (fileName.startsWith("data:") && fileName.indexOf("base64,") > 0) {
            try {
                final byte[] decode = Base64.decode(fileName.substring(fileName.indexOf(44) + 1), 0);
                return this.putBitmap(s, BitmapFactory.decodeByteArray(decode, 0, decode.length, bitmapFactory$Options));
            }
            catch (IllegalArgumentException ex) {
                Log.w("LOTTIE", "data URL did not have correct base64 format.", (Throwable)ex);
                return null;
            }
        }
        try {
            if (!TextUtils.isEmpty((CharSequence)this.imagesFolder)) {
                final AssetManager assets = this.context.getAssets();
                final StringBuilder sb = new StringBuilder();
                sb.append(this.imagesFolder);
                sb.append(fileName);
                return this.putBitmap(s, BitmapFactory.decodeStream(assets.open(sb.toString()), (Rect)null, bitmapFactory$Options));
            }
            throw new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
        }
        catch (IOException ex2) {
            Log.w("LOTTIE", "Unable to open asset.", (Throwable)ex2);
            return null;
        }
    }
    
    public boolean hasSameContext(final Context obj) {
        return (obj == null && this.context == null) || this.context.equals(obj);
    }
    
    public void recycleBitmaps() {
        synchronized (ImageAssetManager.bitmapHashLock) {
            final Iterator<Map.Entry<String, LottieImageAsset>> iterator = this.imageAssets.entrySet().iterator();
            while (iterator.hasNext()) {
                final LottieImageAsset lottieImageAsset = iterator.next().getValue();
                final Bitmap bitmap = lottieImageAsset.getBitmap();
                if (bitmap != null) {
                    bitmap.recycle();
                    lottieImageAsset.setBitmap(null);
                }
            }
        }
    }
    
    public void setDelegate(final ImageAssetDelegate delegate) {
        this.delegate = delegate;
    }
}
