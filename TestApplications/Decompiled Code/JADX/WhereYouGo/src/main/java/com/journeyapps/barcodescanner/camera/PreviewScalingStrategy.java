package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import android.util.Log;
import com.journeyapps.barcodescanner.Size;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class PreviewScalingStrategy {
    private static final String TAG = PreviewScalingStrategy.class.getSimpleName();

    public abstract Rect scalePreview(Size size, Size size2);

    public Size getBestPreviewSize(List<Size> sizes, Size desired) {
        List<Size> ordered = getBestPreviewOrder(sizes, desired);
        Log.i(TAG, "Viewfinder size: " + desired);
        Log.i(TAG, "Preview in order of preference: " + ordered);
        return (Size) ordered.get(0);
    }

    public List<Size> getBestPreviewOrder(List<Size> sizes, final Size desired) {
        if (desired != null) {
            Collections.sort(sizes, new Comparator<Size>() {
                public int compare(Size a, Size b) {
                    return Float.compare(PreviewScalingStrategy.this.getScore(b, desired), PreviewScalingStrategy.this.getScore(a, desired));
                }
            });
        }
        return sizes;
    }

    /* Access modifiers changed, original: protected */
    public float getScore(Size size, Size desired) {
        return 0.5f;
    }
}
