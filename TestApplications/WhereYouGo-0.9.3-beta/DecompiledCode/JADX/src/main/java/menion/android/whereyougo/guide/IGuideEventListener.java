package menion.android.whereyougo.guide;

public interface IGuideEventListener {
    void guideStart();

    void guideStop();

    void receiveGuideEvent(IGuide iGuide, String str, float f, double d);

    void trackGuideCallRecalculate();
}
