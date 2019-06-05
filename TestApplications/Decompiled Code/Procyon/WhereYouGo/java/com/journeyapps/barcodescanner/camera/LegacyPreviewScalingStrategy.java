// 
// Decompiled by Procyon v0.5.34
// 

package com.journeyapps.barcodescanner.camera;

import android.graphics.Rect;
import android.util.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.journeyapps.barcodescanner.Size;

public class LegacyPreviewScalingStrategy extends PreviewScalingStrategy
{
    private static final String TAG;
    
    static {
        TAG = LegacyPreviewScalingStrategy.class.getSimpleName();
    }
    
    public static Size scale(Size scale, final Size size) {
        Size size2 = scale = scale;
        if (!size.fitsIn(size2)) {
            while (true) {
                final Size scale2 = size2.scale(3, 2);
                scale = size2.scale(2, 1);
                if (size.fitsIn(scale2)) {
                    scale = scale2;
                    break;
                }
                if (size.fitsIn(scale)) {
                    break;
                }
                size2 = scale;
            }
        }
        else {
            Size scale4 = null;
            Label_0056: {
                break Label_0056;
                Size scale3 = null;
                do {
                    scale = scale3;
                    scale4 = scale.scale(2, 3);
                    scale3 = scale.scale(1, 2);
                } while (size.fitsIn(scale3));
            }
            if (size.fitsIn(scale4)) {
                scale = scale4;
            }
        }
        return scale;
    }
    
    @Override
    public Size getBestPreviewSize(final List<Size> list, final Size obj) {
        Size size;
        if (obj == null) {
            size = list.get(0);
        }
        else {
            Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<Size>() {
                @Override
                public int compare(final Size size, final Size size2) {
                    final int n = -1;
                    final int n2 = LegacyPreviewScalingStrategy.scale(size, obj).width - size.width;
                    final int n3 = LegacyPreviewScalingStrategy.scale(size2, obj).width - size2.width;
                    int n4;
                    if (n2 == 0 && n3 == 0) {
                        n4 = size.compareTo(size2);
                    }
                    else {
                        n4 = n;
                        if (n2 != 0) {
                            if (n3 == 0) {
                                n4 = 1;
                            }
                            else if (n2 < 0 && n3 < 0) {
                                n4 = size.compareTo(size2);
                            }
                            else if (n2 > 0 && n3 > 0) {
                                n4 = -size.compareTo(size2);
                            }
                            else {
                                n4 = n;
                                if (n2 >= 0) {
                                    n4 = 1;
                                }
                            }
                        }
                    }
                    return n4;
                }
            });
            Log.i(LegacyPreviewScalingStrategy.TAG, "Viewfinder size: " + obj);
            Log.i(LegacyPreviewScalingStrategy.TAG, "Preview in order of preference: " + list);
            size = list.get(0);
        }
        return size;
    }
    
    @Override
    public Rect scalePreview(final Size obj, final Size obj2) {
        final Size scale = scale(obj, obj2);
        Log.i(LegacyPreviewScalingStrategy.TAG, "Preview: " + obj + "; Scaled: " + scale + "; Want: " + obj2);
        final int n = (scale.width - obj2.width) / 2;
        final int n2 = (scale.height - obj2.height) / 2;
        return new Rect(-n, -n2, scale.width - n, scale.height - n2);
    }
}
