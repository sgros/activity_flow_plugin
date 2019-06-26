// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.location;

import android.os.Bundle;
import java.util.ArrayList;

public interface ILocationEventListener
{
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    
    String getName();
    
    int getPriority();
    
    boolean isRequired();
    
    void onGpsStatusChanged(final int p0, final ArrayList<SatellitePosition> p1);
    
    void onLocationChanged(final Location p0);
    
    void onStatusChanged(final String p0, final int p1, final Bundle p2);
}
