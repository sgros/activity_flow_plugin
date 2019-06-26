// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.guide;

import menion.android.whereyougo.geo.location.Location;
import cz.matejcik.openwig.Zone;

public class ZoneGuide extends Guide
{
    private boolean mAlreadyEntered;
    private final Zone mZone;
    
    public ZoneGuide(final Zone mZone) {
        super(mZone.name, new Location("Guidance: " + mZone.name, mZone.bbCenter.latitude, mZone.bbCenter.longitude));
        this.mAlreadyEntered = false;
        this.mZone = mZone;
        this.mAlreadyEntered = false;
    }
}
