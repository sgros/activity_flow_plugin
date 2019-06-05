// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Context;

public interface ImageDecoder
{
    Bitmap decode(final Context p0, final Uri p1) throws Exception;
}
