// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.model;

import org.mapsforge.core.model.GeoPoint;

public class MyGeoPoint extends GeoPoint
{
    int id;
    
    public MyGeoPoint(final double n, final double n2, final int id) {
        super(n, n2);
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
