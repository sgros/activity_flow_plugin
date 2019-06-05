package com.davemorrissey.labs.subscaleview.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;

public interface ImageRegionDecoder {
   Bitmap decodeRegion(Rect var1, int var2);

   Point init(Context var1, Uri var2) throws Exception;

   boolean isReady();

   void recycle();
}
