// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.config;

public class Configuration
{
    private static IConfigurationProvider ref;
    
    public static IConfigurationProvider getInstance() {
        synchronized (Configuration.class) {
            if (Configuration.ref == null) {
                Configuration.ref = new DefaultConfigurationProvider();
            }
            return Configuration.ref;
        }
    }
}
