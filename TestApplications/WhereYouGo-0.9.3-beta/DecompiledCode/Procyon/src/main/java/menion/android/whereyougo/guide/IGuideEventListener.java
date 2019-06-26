// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.guide;

public interface IGuideEventListener
{
    void guideStart();
    
    void guideStop();
    
    void receiveGuideEvent(final IGuide p0, final String p1, final float p2, final double p3);
    
    void trackGuideCallRecalculate();
}
