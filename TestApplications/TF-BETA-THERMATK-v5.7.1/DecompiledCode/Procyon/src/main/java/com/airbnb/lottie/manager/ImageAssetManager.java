// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.manager;

import android.content.res.AssetManager;
import java.io.IOException;
import com.airbnb.lottie.utils.Utils;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.graphics.BitmapFactory$Options;
import android.graphics.Bitmap;
import java.util.HashMap;
import com.airbnb.lottie.utils.Logger;
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
    
    public ImageAssetManager(final Drawable$Callback drawable$Callback, String imagesFolder, final ImageAssetDelegate delegate, final Map<String, LottieImageAsset> imageAssets) {
        this.imagesFolder = imagesFolder;
        if (!TextUtils.isEmpty((CharSequence)imagesFolder)) {
            imagesFolder = this.imagesFolder;
            if (imagesFolder.charAt(imagesFolder.length() - 1) != '/') {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.imagesFolder);
                sb.append('/');
                this.imagesFolder = sb.toString();
            }
        }
        if (!(drawable$Callback instanceof View)) {
            Logger.warning("LottieDrawable must be inside of a view for images to work.");
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
        final ImageAssetDelegate delegate = this.delegate;
        if (delegate != null) {
            final Bitmap fetchBitmap = delegate.fetchBitmap(lottieImageAsset);
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
                final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length, bitmapFactory$Options);
                this.putBitmap(s, decodeByteArray);
                return decodeByteArray;
            }
            catch (IllegalArgumentException ex) {
                Logger.warning("data URL did not have correct base64 format.", ex);
                return null;
            }
        }
        try {
            if (!TextUtils.isEmpty((CharSequence)this.imagesFolder)) {
                final AssetManager assets = this.context.getAssets();
                final StringBuilder sb = new StringBuilder();
                sb.append(this.imagesFolder);
                sb.append(fileName);
                final Bitmap resizeBitmapIfNeeded = Utils.resizeBitmapIfNeeded(BitmapFactory.decodeStream(assets.open(sb.toString()), (Rect)null, bitmapFactory$Options), lottieImageAsset.getWidth(), lottieImageAsset.getHeight());
                this.putBitmap(s, resizeBitmapIfNeeded);
                return resizeBitmapIfNeeded;
            }
            throw new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
        }
        catch (IOException ex2) {
            Logger.warning("Unable to open asset.", ex2);
            return null;
        }
    }
    
    public boolean hasSameContext(final Context obj) {
        return (obj == null && this.context == null) || this.context.equals(obj);
    }
    
    public void setDelegate(final ImageAssetDelegate delegate) {
        this.delegate = delegate;
    }
}
