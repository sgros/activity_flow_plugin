// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class TileSystemWebMercator extends TileSystem
{
    @Override
    public double getLatitudeFromY01(final double n) {
        return 90.0 - Math.atan(Math.exp((n - 0.5) * 2.0 * 3.141592653589793)) * 360.0 / 3.141592653589793;
    }
    
    @Override
    public double getLongitudeFromX01(final double n) {
        return this.getMinLongitude() + (this.getMaxLongitude() - this.getMinLongitude()) * n;
    }
    
    @Override
    public double getMaxLatitude() {
        return 85.05112877980658;
    }
    
    @Override
    public double getMaxLongitude() {
        return 180.0;
    }
    
    @Override
    public double getMinLatitude() {
        return -85.05112877980658;
    }
    
    @Override
    public double getMinLongitude() {
        return -180.0;
    }
    
    @Override
    public double getX01FromLongitude(final double n) {
        return (n - this.getMinLongitude()) / (this.getMaxLongitude() - this.getMinLongitude());
    }
    
    @Override
    public double getY01FromLatitude(double sin) {
        sin = Math.sin(sin * 3.141592653589793 / 180.0);
        return 0.5 - Math.log((sin + 1.0) / (1.0 - sin)) / 12.566370614359172;
    }
}
