package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;

public interface TileCache {
    boolean containsKey(MapGeneratorJob mapGeneratorJob);

    void destroy();

    Bitmap get(MapGeneratorJob mapGeneratorJob);

    int getCapacity();

    boolean isPersistent();

    void put(MapGeneratorJob mapGeneratorJob, Bitmap bitmap);

    void setCapacity(int i);

    void setPersistent(boolean z);
}
