// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;

public interface TileCache
{
    boolean containsKey(final MapGeneratorJob p0);
    
    void destroy();
    
    Bitmap get(final MapGeneratorJob p0);
    
    int getCapacity();
    
    boolean isPersistent();
    
    void put(final MapGeneratorJob p0, final Bitmap p1);
    
    void setCapacity(final int p0);
    
    void setPersistent(final boolean p0);
}
