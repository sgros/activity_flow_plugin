// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.location;

public class SatellitePosition
{
    float azimuth;
    float elevation;
    boolean fixed;
    Integer prn;
    int snr;
    
    public SatellitePosition() {
        this.prn = 0;
        this.azimuth = 0.0f;
        this.elevation = 0.0f;
        this.snr = 0;
    }
    
    public float getAzimuth() {
        return this.azimuth;
    }
    
    public float getElevation() {
        return this.elevation;
    }
    
    public Integer getPrn() {
        return this.prn;
    }
    
    public int getSnr() {
        return this.snr;
    }
    
    public boolean isFixed() {
        return this.fixed;
    }
}
