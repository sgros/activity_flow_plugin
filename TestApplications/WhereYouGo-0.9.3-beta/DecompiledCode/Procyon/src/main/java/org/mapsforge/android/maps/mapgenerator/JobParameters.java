// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import org.mapsforge.map.rendertheme.XmlRenderTheme;
import java.io.Serializable;

public class JobParameters implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final int hashCodeValue;
    public final XmlRenderTheme jobTheme;
    public final float textScale;
    
    public JobParameters(final XmlRenderTheme jobTheme, final float textScale) {
        this.jobTheme = jobTheme;
        this.textScale = textScale;
        this.hashCodeValue = this.calculateHashCode();
    }
    
    private int calculateHashCode() {
        int hashCode;
        if (this.jobTheme == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.jobTheme.hashCode();
        }
        return (hashCode + 217) * 31 + Float.floatToIntBits(this.textScale);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof JobParameters)) {
                b = false;
            }
            else {
                final JobParameters jobParameters = (JobParameters)o;
                if (this.jobTheme == null) {
                    if (jobParameters.jobTheme != null) {
                        b = false;
                        return b;
                    }
                }
                else if (!this.jobTheme.equals(jobParameters.jobTheme)) {
                    b = false;
                    return b;
                }
                if (Float.floatToIntBits(this.textScale) != Float.floatToIntBits(jobParameters.textScale)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return this.hashCodeValue;
    }
}
