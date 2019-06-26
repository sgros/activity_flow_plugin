package org.mapsforge.android.maps.mapgenerator;

import java.io.Serializable;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

public class JobParameters implements Serializable {
    private static final long serialVersionUID = 1;
    private final int hashCodeValue = calculateHashCode();
    public final XmlRenderTheme jobTheme;
    public final float textScale;

    public JobParameters(XmlRenderTheme jobTheme, float textScale) {
        this.jobTheme = jobTheme;
        this.textScale = textScale;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JobParameters)) {
            return false;
        }
        JobParameters other = (JobParameters) obj;
        if (this.jobTheme == null) {
            if (other.jobTheme != null) {
                return false;
            }
        } else if (!this.jobTheme.equals(other.jobTheme)) {
            return false;
        }
        if (Float.floatToIntBits(this.textScale) != Float.floatToIntBits(other.textScale)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        return (((this.jobTheme == null ? 0 : this.jobTheme.hashCode()) + 217) * 31) + Float.floatToIntBits(this.textScale);
    }
}
