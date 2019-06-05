package com.davemorrissey.labs.subscaleview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import com.adjust.sdk.Constants;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class ImageSource {
    private final Bitmap bitmap;
    private boolean cached;
    private final Integer resource;
    private int sHeight;
    private Rect sRegion;
    private int sWidth;
    private boolean tile;
    private final Uri uri;

    private ImageSource(Uri uri) {
        String uri2 = uri.toString();
        if (uri2.startsWith("file:///") && !new File(uri2.substring("file:///".length() - 1)).exists()) {
            try {
                uri = Uri.parse(URLDecoder.decode(uri2, Constants.ENCODING));
            } catch (UnsupportedEncodingException unused) {
            }
        }
        this.bitmap = null;
        this.uri = uri;
        this.resource = null;
        this.tile = true;
    }

    private ImageSource(int i) {
        this.bitmap = null;
        this.uri = null;
        this.resource = Integer.valueOf(i);
        this.tile = true;
    }

    public static ImageSource resource(int i) {
        return new ImageSource(i);
    }

    public static ImageSource asset(String str) {
        if (str != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file:///android_asset/");
            stringBuilder.append(str);
            return uri(stringBuilder.toString());
        }
        throw new NullPointerException("Asset name must not be null");
    }

    public static ImageSource uri(String str) {
        if (str != null) {
            if (!str.contains("://")) {
                if (str.startsWith("/")) {
                    str = str.substring(1);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("file:///");
                stringBuilder.append(str);
                str = stringBuilder.toString();
            }
            return new ImageSource(Uri.parse(str));
        }
        throw new NullPointerException("Uri must not be null");
    }

    public static ImageSource uri(Uri uri) {
        if (uri != null) {
            return new ImageSource(uri);
        }
        throw new NullPointerException("Uri must not be null");
    }

    public ImageSource tilingEnabled() {
        return tiling(true);
    }

    public ImageSource tiling(boolean z) {
        this.tile = z;
        return this;
    }

    /* Access modifiers changed, original: protected|final */
    public final Uri getUri() {
        return this.uri;
    }

    /* Access modifiers changed, original: protected|final */
    public final Bitmap getBitmap() {
        return this.bitmap;
    }

    /* Access modifiers changed, original: protected|final */
    public final Integer getResource() {
        return this.resource;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean getTile() {
        return this.tile;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getSWidth() {
        return this.sWidth;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getSHeight() {
        return this.sHeight;
    }

    /* Access modifiers changed, original: protected|final */
    public final Rect getSRegion() {
        return this.sRegion;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isCached() {
        return this.cached;
    }
}
