// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;

public interface IMapTileProviderCallback
{
    void mapTileRequestCompleted(final MapTileRequestState p0, final Drawable p1);
    
    void mapTileRequestExpiredTile(final MapTileRequestState p0, final Drawable p1);
    
    void mapTileRequestFailed(final MapTileRequestState p0);
    
    void mapTileRequestFailedExceedsMaxQueueSize(final MapTileRequestState p0);
}
