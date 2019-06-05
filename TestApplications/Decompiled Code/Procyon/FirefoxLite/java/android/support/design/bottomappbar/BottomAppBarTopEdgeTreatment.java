// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.bottomappbar;

import android.support.design.shape.ShapePath;
import android.support.design.shape.EdgeTreatment;

public class BottomAppBarTopEdgeTreatment extends EdgeTreatment
{
    private float cradleVerticalOffset;
    private float fabDiameter;
    private float fabMargin;
    private float horizontalOffset;
    private float roundedCornerRadius;
    
    float getCradleVerticalOffset() {
        return this.cradleVerticalOffset;
    }
    
    @Override
    public void getEdgePath(final float n, float n2, final ShapePath shapePath) {
        if (this.fabDiameter == 0.0f) {
            shapePath.lineTo(n, 0.0f);
            return;
        }
        final float n3 = (this.fabMargin * 2.0f + this.fabDiameter) / 2.0f;
        final float n4 = n2 * this.roundedCornerRadius;
        final float n5 = n / 2.0f + this.horizontalOffset;
        n2 = this.cradleVerticalOffset * n2 + (1.0f - n2) * n3;
        if (n2 / n3 >= 1.0f) {
            shapePath.lineTo(n, 0.0f);
            return;
        }
        final float n6 = n3 + n4;
        final float n7 = n2 + n4;
        final float n8 = (float)Math.sqrt(n6 * n6 - n7 * n7);
        final float n9 = n5 - n8;
        final float n10 = n5 + n8;
        final float n11 = (float)Math.toDegrees(Math.atan(n8 / n7));
        final float n12 = 90.0f - n11;
        final float n13 = n9 - n4;
        shapePath.lineTo(n13, 0.0f);
        final float n14 = n4 * 2.0f;
        shapePath.addArc(n13, 0.0f, n9 + n4, n14, 270.0f, n11);
        shapePath.addArc(n5 - n3, -n3 - n2, n5 + n3, n3 - n2, 180.0f - n12, n12 * 2.0f - 180.0f);
        shapePath.addArc(n10 - n4, 0.0f, n10 + n4, n14, 270.0f - n11, n11);
        shapePath.lineTo(n, 0.0f);
    }
    
    float getFabCradleMargin() {
        return this.fabMargin;
    }
    
    float getFabCradleRoundedCornerRadius() {
        return this.roundedCornerRadius;
    }
    
    float getFabDiameter() {
        return this.fabDiameter;
    }
    
    float getHorizontalOffset() {
        return this.horizontalOffset;
    }
    
    void setCradleVerticalOffset(final float cradleVerticalOffset) {
        this.cradleVerticalOffset = cradleVerticalOffset;
    }
    
    void setFabCradleMargin(final float fabMargin) {
        this.fabMargin = fabMargin;
    }
    
    void setFabCradleRoundedCornerRadius(final float roundedCornerRadius) {
        this.roundedCornerRadius = roundedCornerRadius;
    }
    
    void setFabDiameter(final float fabDiameter) {
        this.fabDiameter = fabDiameter;
    }
    
    void setHorizontalOffset(final float horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }
}
