// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.io.File;
import android.net.Uri;
import android.graphics.Rect;
import android.graphics.Bitmap;

public final class ImageSource
{
    private final Bitmap bitmap;
    private boolean cached;
    private final Integer resource;
    private int sHeight;
    private Rect sRegion;
    private int sWidth;
    private boolean tile;
    private final Uri uri;
    
    private ImageSource(final int i) {
        this.bitmap = null;
        this.uri = null;
        this.resource = i;
        this.tile = true;
    }
    
    private ImageSource(final Uri uri) {
        final String string = uri.toString();
        Uri parse = uri;
        while (true) {
            if (!string.startsWith("file:///")) {
                break Label_0056;
            }
            parse = uri;
            if (new File(string.substring("file:///".length() - 1)).exists()) {
                break Label_0056;
            }
            try {
                parse = Uri.parse(URLDecoder.decode(string, "UTF-8"));
                this.bitmap = null;
                this.uri = parse;
                this.resource = null;
                this.tile = true;
            }
            catch (UnsupportedEncodingException ex) {
                parse = uri;
                continue;
            }
            break;
        }
    }
    
    public static ImageSource asset(final String str) {
        if (str != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("file:///android_asset/");
            sb.append(str);
            return uri(sb.toString());
        }
        throw new NullPointerException("Asset name must not be null");
    }
    
    public static ImageSource resource(final int n) {
        return new ImageSource(n);
    }
    
    public static ImageSource uri(final Uri uri) {
        if (uri != null) {
            return new ImageSource(uri);
        }
        throw new NullPointerException("Uri must not be null");
    }
    
    public static ImageSource uri(final String s) {
        if (s != null) {
            String string = s;
            if (!s.contains("://")) {
                String substring = s;
                if (s.startsWith("/")) {
                    substring = s.substring(1);
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("file:///");
                sb.append(substring);
                string = sb.toString();
            }
            return new ImageSource(Uri.parse(string));
        }
        throw new NullPointerException("Uri must not be null");
    }
    
    protected final Bitmap getBitmap() {
        return this.bitmap;
    }
    
    protected final Integer getResource() {
        return this.resource;
    }
    
    protected final int getSHeight() {
        return this.sHeight;
    }
    
    protected final Rect getSRegion() {
        return this.sRegion;
    }
    
    protected final int getSWidth() {
        return this.sWidth;
    }
    
    protected final boolean getTile() {
        return this.tile;
    }
    
    protected final Uri getUri() {
        return this.uri;
    }
    
    protected final boolean isCached() {
        return this.cached;
    }
    
    public ImageSource tiling(final boolean tile) {
        this.tile = tile;
        return this;
    }
    
    public ImageSource tilingEnabled() {
        return this.tiling(true);
    }
}
