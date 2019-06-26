package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.util.Util;

public final class SsUtil {
   public static Uri fixManifestUri(Uri var0) {
      String var1 = var0.getLastPathSegment();
      return var1 != null && Util.toLowerInvariant(var1).matches("manifest(\\(.+\\))?") ? var0 : Uri.withAppendedPath(var0, "Manifest");
   }
}
