// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.os.Build$VERSION;

public class OperatingSystemVersionMeasurement extends StaticMeasurement
{
    public OperatingSystemVersionMeasurement() {
        super("osversion", Integer.toString(Build$VERSION.SDK_INT));
    }
}
