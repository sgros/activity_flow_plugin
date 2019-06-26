// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.detector;

import com.google.zxing.NotFoundException;
import java.util.Iterator;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import com.google.zxing.ResultPointCallback;
import java.util.List;
import com.google.zxing.common.BitMatrix;

final class AlignmentPatternFinder
{
    private final int[] crossCheckStateCount;
    private final int height;
    private final BitMatrix image;
    private final float moduleSize;
    private final List<AlignmentPattern> possibleCenters;
    private final ResultPointCallback resultPointCallback;
    private final int startX;
    private final int startY;
    private final int width;
    
    AlignmentPatternFinder(final BitMatrix image, final int startX, final int startY, final int width, final int height, final float moduleSize, final ResultPointCallback resultPointCallback) {
        this.image = image;
        this.possibleCenters = new ArrayList<AlignmentPattern>(5);
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.moduleSize = moduleSize;
        this.crossCheckStateCount = new int[3];
        this.resultPointCallback = resultPointCallback;
    }
    
    private static float centerFromEnd(final int[] array, final int n) {
        return n - array[2] - array[1] / 2.0f;
    }
    
    private float crossCheckVertical(int n, final int n2, final int n3, final int n4) {
        final float n5 = Float.NaN;
        final BitMatrix image = this.image;
        final int height = image.getHeight();
        final int[] crossCheckStateCount = this.crossCheckStateCount;
        crossCheckStateCount[0] = 0;
        crossCheckStateCount[2] = (crossCheckStateCount[1] = 0);
        int n6;
        for (n6 = n; n6 >= 0 && image.get(n2, n6) && crossCheckStateCount[1] <= n3; --n6) {
            ++crossCheckStateCount[1];
        }
        float centerFromEnd = n5;
        if (n6 >= 0) {
            if (crossCheckStateCount[1] > n3) {
                centerFromEnd = n5;
            }
            else {
                while (n6 >= 0 && !image.get(n2, n6) && crossCheckStateCount[0] <= n3) {
                    ++crossCheckStateCount[0];
                    --n6;
                }
                centerFromEnd = n5;
                if (crossCheckStateCount[0] <= n3) {
                    ++n;
                    while (n < height && image.get(n2, n) && crossCheckStateCount[1] <= n3) {
                        ++crossCheckStateCount[1];
                        ++n;
                    }
                    centerFromEnd = n5;
                    if (n != height) {
                        centerFromEnd = n5;
                        if (crossCheckStateCount[1] <= n3) {
                            while (n < height && !image.get(n2, n) && crossCheckStateCount[2] <= n3) {
                                ++crossCheckStateCount[2];
                                ++n;
                            }
                            centerFromEnd = n5;
                            if (crossCheckStateCount[2] <= n3) {
                                centerFromEnd = n5;
                                if (Math.abs(crossCheckStateCount[0] + crossCheckStateCount[1] + crossCheckStateCount[2] - n4) * 5 < n4 * 2) {
                                    centerFromEnd = n5;
                                    if (this.foundPatternCross(crossCheckStateCount)) {
                                        centerFromEnd = centerFromEnd(crossCheckStateCount, n);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return centerFromEnd;
    }
    
    private boolean foundPatternCross(final int[] array) {
        final float moduleSize = this.moduleSize;
        final float n = moduleSize / 2.0f;
        for (int i = 0; i < 3; ++i) {
            if (Math.abs(moduleSize - array[i]) >= n) {
                return false;
            }
        }
        return true;
    }
    
    private AlignmentPattern handlePossibleCenter(final int[] array, final int n, final int n2) {
        final int n3 = array[0];
        final int n4 = array[1];
        final int n5 = array[2];
        final float centerFromEnd = centerFromEnd(array, n2);
        final float crossCheckVertical = this.crossCheckVertical(n, (int)centerFromEnd, array[1] * 2, n3 + n4 + n5);
        if (Float.isNaN(crossCheckVertical)) {
            return null;
        }
        final float n6 = (array[0] + array[1] + array[2]) / 3.0f;
        for (final AlignmentPattern alignmentPattern : this.possibleCenters) {
            if (alignmentPattern.aboutEquals(n6, crossCheckVertical, centerFromEnd)) {
                return alignmentPattern.combineEstimate(crossCheckVertical, centerFromEnd, n6);
            }
        }
        final AlignmentPattern alignmentPattern2 = new AlignmentPattern(centerFromEnd, crossCheckVertical, n6);
        this.possibleCenters.add(alignmentPattern2);
        if (this.resultPointCallback != null) {
            this.resultPointCallback.foundPossibleResultPoint(alignmentPattern2);
            return null;
        }
        return null;
        return null;
    }
    
    AlignmentPattern find() throws NotFoundException {
        final int startX = this.startX;
        final int height = this.height;
        final int n = startX + this.width;
        final int startY = this.startY;
        final int n2 = height / 2;
        final int[] array = new int[3];
        int i = 0;
        while (i < height) {
            int n3;
            if ((i & 0x1) == 0x0) {
                n3 = (i + 1) / 2;
            }
            else {
                n3 = -((i + 1) / 2);
            }
            final int n4 = startY + n2 + n3;
            array[0] = 0;
            array[2] = (array[1] = 0);
            int n5;
            for (n5 = startX; n5 < n && !this.image.get(n5, n4); ++n5) {}
            final int n6 = 0;
            int j = n5;
            int n7 = n6;
            while (j < n) {
                if (this.image.get(j, n4)) {
                    if (n7 == 1) {
                        ++array[1];
                    }
                    else if (n7 == 2) {
                        if (this.foundPatternCross(array)) {
                            final AlignmentPattern alignmentPattern = this.handlePossibleCenter(array, n4, j);
                            if (alignmentPattern != null) {
                                return alignmentPattern;
                            }
                        }
                        array[0] = array[2];
                        array[1] = 1;
                        array[2] = 0;
                        n7 = 1;
                    }
                    else {
                        ++n7;
                        ++array[n7];
                    }
                }
                else {
                    int n8;
                    if ((n8 = n7) == 1) {
                        n8 = n7 + 1;
                    }
                    ++array[n8];
                    n7 = n8;
                }
                ++j;
            }
            AlignmentPattern alignmentPattern;
            if (!this.foundPatternCross(array) || (alignmentPattern = this.handlePossibleCenter(array, n4, n)) == null) {
                ++i;
                continue;
            }
            return alignmentPattern;
        }
        if (!this.possibleCenters.isEmpty()) {
            return this.possibleCenters.get(0);
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
