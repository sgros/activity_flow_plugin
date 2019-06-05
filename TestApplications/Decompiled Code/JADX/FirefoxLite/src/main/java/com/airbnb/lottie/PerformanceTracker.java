package com.airbnb.lottie;

import android.support.p001v4.util.ArraySet;
import android.support.p001v4.util.Pair;
import com.airbnb.lottie.utils.MeanCalculator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PerformanceTracker {
    private boolean enabled = false;
    private final Comparator<Pair<String, Float>> floatComparator = new C03721();
    private final Set<FrameListener> frameListeners = new ArraySet();
    private final Map<String, MeanCalculator> layerRenderTimes = new HashMap();

    /* renamed from: com.airbnb.lottie.PerformanceTracker$1 */
    class C03721 implements Comparator<Pair<String, Float>> {
        C03721() {
        }

        public int compare(Pair<String, Float> pair, Pair<String, Float> pair2) {
            float floatValue = ((Float) pair.second).floatValue();
            float floatValue2 = ((Float) pair2.second).floatValue();
            if (floatValue2 > floatValue) {
                return 1;
            }
            return floatValue > floatValue2 ? -1 : 0;
        }
    }

    public interface FrameListener {
        void onFrameRendered(float f);
    }

    /* Access modifiers changed, original: 0000 */
    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void recordRenderTime(String str, float f) {
        if (this.enabled) {
            MeanCalculator meanCalculator = (MeanCalculator) this.layerRenderTimes.get(str);
            if (meanCalculator == null) {
                meanCalculator = new MeanCalculator();
                this.layerRenderTimes.put(str, meanCalculator);
            }
            meanCalculator.add(f);
            if (str.equals("__container")) {
                for (FrameListener onFrameRendered : this.frameListeners) {
                    onFrameRendered.onFrameRendered(f);
                }
            }
        }
    }
}
