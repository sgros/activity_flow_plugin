package com.airbnb.lottie.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable.Callback;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageAssetManager {
    private static final Object bitmapHashLock = new Object();
    private final Context context;
    private ImageAssetDelegate delegate;
    private final Map<String, LottieImageAsset> imageAssets;
    private String imagesFolder;

    public ImageAssetManager(Callback callback, String str, ImageAssetDelegate imageAssetDelegate, Map<String, LottieImageAsset> map) {
        this.imagesFolder = str;
        if (!TextUtils.isEmpty(str)) {
            str = this.imagesFolder;
            if (str.charAt(str.length() - 1) != '/') {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.imagesFolder);
                stringBuilder.append('/');
                this.imagesFolder = stringBuilder.toString();
            }
        }
        if (callback instanceof View) {
            this.context = ((View) callback).getContext();
            this.imageAssets = map;
            setDelegate(imageAssetDelegate);
            return;
        }
        Logger.warning("LottieDrawable must be inside of a view for images to work.");
        this.imageAssets = new HashMap();
        this.context = null;
    }

    public void setDelegate(ImageAssetDelegate imageAssetDelegate) {
        this.delegate = imageAssetDelegate;
    }

    public Bitmap bitmapForId(String str) {
        LottieImageAsset lottieImageAsset = (LottieImageAsset) this.imageAssets.get(str);
        if (lottieImageAsset == null) {
            return null;
        }
        Bitmap bitmap = lottieImageAsset.getBitmap();
        if (bitmap != null) {
            return bitmap;
        }
        ImageAssetDelegate imageAssetDelegate = this.delegate;
        Bitmap fetchBitmap;
        if (imageAssetDelegate != null) {
            fetchBitmap = imageAssetDelegate.fetchBitmap(lottieImageAsset);
            if (fetchBitmap != null) {
                putBitmap(str, fetchBitmap);
            }
            return fetchBitmap;
        }
        String fileName = lottieImageAsset.getFileName();
        Options options = new Options();
        options.inScaled = true;
        options.inDensity = 160;
        if (!fileName.startsWith("data:") || fileName.indexOf("base64,") <= 0) {
            try {
                if (TextUtils.isEmpty(this.imagesFolder)) {
                    throw new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
                }
                AssetManager assets = this.context.getAssets();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.imagesFolder);
                stringBuilder.append(fileName);
                fetchBitmap = Utils.resizeBitmapIfNeeded(BitmapFactory.decodeStream(assets.open(stringBuilder.toString()), null, options), lottieImageAsset.getWidth(), lottieImageAsset.getHeight());
                putBitmap(str, fetchBitmap);
                return fetchBitmap;
            } catch (IOException e) {
                Logger.warning("Unable to open asset.", e);
                return null;
            }
        }
        try {
            byte[] decode = Base64.decode(fileName.substring(fileName.indexOf(44) + 1), 0);
            fetchBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
            putBitmap(str, fetchBitmap);
            return fetchBitmap;
        } catch (IllegalArgumentException e2) {
            Logger.warning("data URL did not have correct base64 format.", e2);
            return null;
        }
    }

    public boolean hasSameContext(Context context) {
        return (context == null && this.context == null) || this.context.equals(context);
    }

    private Bitmap putBitmap(String str, Bitmap bitmap) {
        synchronized (bitmapHashLock) {
            ((LottieImageAsset) this.imageAssets.get(str)).setBitmap(bitmap);
        }
        return bitmap;
    }
}
