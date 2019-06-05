package menion.android.whereyougo.geo.location;

import android.os.Bundle;
import java.util.ArrayList;

public interface ILocationEventListener {
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;

    String getName();

    int getPriority();

    boolean isRequired();

    void onGpsStatusChanged(int i, ArrayList<SatellitePosition> arrayList);

    void onLocationChanged(Location location);

    void onStatusChanged(String str, int i, Bundle bundle);
}
