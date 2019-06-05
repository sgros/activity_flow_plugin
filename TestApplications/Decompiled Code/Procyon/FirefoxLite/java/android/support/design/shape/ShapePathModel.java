// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.shape;

public class ShapePathModel
{
    private static final CornerTreatment DEFAULT_CORNER_TREATMENT;
    private static final EdgeTreatment DEFAULT_EDGE_TREATMENT;
    private EdgeTreatment bottomEdge;
    private CornerTreatment bottomLeftCorner;
    private CornerTreatment bottomRightCorner;
    private EdgeTreatment leftEdge;
    private EdgeTreatment rightEdge;
    private EdgeTreatment topEdge;
    private CornerTreatment topLeftCorner;
    private CornerTreatment topRightCorner;
    
    static {
        DEFAULT_CORNER_TREATMENT = new CornerTreatment();
        DEFAULT_EDGE_TREATMENT = new EdgeTreatment();
    }
    
    public EdgeTreatment getBottomEdge() {
        return this.bottomEdge;
    }
    
    public CornerTreatment getBottomLeftCorner() {
        return this.bottomLeftCorner;
    }
    
    public CornerTreatment getBottomRightCorner() {
        return this.bottomRightCorner;
    }
    
    public EdgeTreatment getLeftEdge() {
        return this.leftEdge;
    }
    
    public EdgeTreatment getRightEdge() {
        return this.rightEdge;
    }
    
    public EdgeTreatment getTopEdge() {
        return this.topEdge;
    }
    
    public CornerTreatment getTopLeftCorner() {
        return this.topLeftCorner;
    }
    
    public CornerTreatment getTopRightCorner() {
        return this.topRightCorner;
    }
}
