// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import com.google.android.exoplayer2.util.Util;
import android.net.Uri;

public final class SsUtil
{
    public static Uri fixManifestUri(final Uri uri) {
        final String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment != null && Util.toLowerInvariant(lastPathSegment).matches("manifest(\\(.+\\))?")) {
            return uri;
        }
        return Uri.withAppendedPath(uri, "Manifest");
    }
}
