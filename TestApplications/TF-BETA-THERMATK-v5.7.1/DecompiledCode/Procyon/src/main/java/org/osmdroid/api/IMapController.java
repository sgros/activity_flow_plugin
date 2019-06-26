// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.api;

public interface IMapController
{
    void animateTo(final IGeoPoint p0);
    
    void animateTo(final IGeoPoint p0, final Double p1, final Long p2);
    
    void setCenter(final IGeoPoint p0);
    
    double setZoom(final double p0);
    
    void stopAnimation(final boolean p0);
    
    boolean zoomIn();
    
    boolean zoomInFixing(final int p0, final int p1);
    
    boolean zoomOut();
    
    boolean zoomOutFixing(final int p0, final int p1);
}
