package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;

public interface IMapTileProviderCallback {
   void mapTileRequestCompleted(MapTileRequestState var1, Drawable var2);

   void mapTileRequestExpiredTile(MapTileRequestState var1, Drawable var2);

   void mapTileRequestFailed(MapTileRequestState var1);

   void mapTileRequestFailedExceedsMaxQueueSize(MapTileRequestState var1);
}
