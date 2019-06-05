// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

public class VersionMeasurement extends StaticMeasurement
{
    public VersionMeasurement(final int i) {
        super("v", i);
        if (i > 0) {
            return;
        }
        throw new IllegalArgumentException("Version should be a positive integer > 0");
    }
}
