// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import java.util.Iterator;
import java.util.HashMap;
import androidx.collection.ArraySet;
import com.airbnb.lottie.utils.MeanCalculator;
import java.util.Map;
import java.util.Set;
import androidx.core.util.Pair;
import java.util.Comparator;

public class PerformanceTracker
{
    private boolean enabled;
    private final Comparator<Pair<String, Float>> floatComparator;
    private final Set<FrameListener> frameListeners;
    private final Map<String, MeanCalculator> layerRenderTimes;
    
    public PerformanceTracker() {
        this.enabled = false;
        this.frameListeners = new ArraySet<FrameListener>();
        this.layerRenderTimes = new HashMap<String, MeanCalculator>();
        this.floatComparator = new Comparator<Pair<String, Float>>() {
            @Override
            public int compare(final Pair<String, Float> pair, final Pair<String, Float> pair2) {
                final float floatValue = pair.second;
                final float floatValue2 = pair2.second;
                if (floatValue2 > floatValue) {
                    return 1;
                }
                if (floatValue > floatValue2) {
                    return -1;
                }
                return 0;
            }
        };
    }
    
    public void recordRenderTime(final String s, final float n) {
        if (!this.enabled) {
            return;
        }
        MeanCalculator meanCalculator;
        if ((meanCalculator = this.layerRenderTimes.get(s)) == null) {
            meanCalculator = new MeanCalculator();
            this.layerRenderTimes.put(s, meanCalculator);
        }
        meanCalculator.add(n);
        if (s.equals("__container")) {
            final Iterator<FrameListener> iterator = this.frameListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onFrameRendered(n);
            }
        }
    }
    
    void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public interface FrameListener
    {
        void onFrameRendered(final float p0);
    }
}
