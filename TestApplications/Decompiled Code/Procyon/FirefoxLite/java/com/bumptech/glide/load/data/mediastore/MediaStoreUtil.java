// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data.mediastore;

import android.net.Uri;

public final class MediaStoreUtil
{
    public static boolean isMediaStoreImageUri(final Uri uri) {
        return isMediaStoreUri(uri) && !isVideoUri(uri);
    }
    
    public static boolean isMediaStoreUri(final Uri uri) {
        return uri != null && "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }
    
    public static boolean isMediaStoreVideoUri(final Uri uri) {
        return isMediaStoreUri(uri) && isVideoUri(uri);
    }
    
    public static boolean isThumbnailSize(final int n, final int n2) {
        return n <= 512 && n2 <= 384;
    }
    
    private static boolean isVideoUri(final Uri uri) {
        return uri.getPathSegments().contains("video");
    }
}
