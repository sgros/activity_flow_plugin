package menion.android.whereyougo.guide;

public interface IGuideEventListener {
   void guideStart();

   void guideStop();

   void receiveGuideEvent(IGuide var1, String var2, float var3, double var4);

   void trackGuideCallRecalculate();
}
