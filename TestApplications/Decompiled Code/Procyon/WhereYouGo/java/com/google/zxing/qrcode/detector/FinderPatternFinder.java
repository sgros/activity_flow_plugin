// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.detector;

import java.io.Serializable;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import com.google.zxing.NotFoundException;
import java.util.Iterator;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import com.google.zxing.ResultPointCallback;
import java.util.List;
import com.google.zxing.common.BitMatrix;

public class FinderPatternFinder
{
    private static final int CENTER_QUORUM = 2;
    protected static final int MAX_MODULES = 57;
    protected static final int MIN_SKIP = 3;
    private final int[] crossCheckStateCount;
    private boolean hasSkipped;
    private final BitMatrix image;
    private final List<FinderPattern> possibleCenters;
    private final ResultPointCallback resultPointCallback;
    
    public FinderPatternFinder(final BitMatrix bitMatrix) {
        this(bitMatrix, null);
    }
    
    public FinderPatternFinder(final BitMatrix image, final ResultPointCallback resultPointCallback) {
        this.image = image;
        this.possibleCenters = new ArrayList<FinderPattern>();
        this.crossCheckStateCount = new int[5];
        this.resultPointCallback = resultPointCallback;
    }
    
    private static float centerFromEnd(final int[] array, final int n) {
        return n - array[4] - array[3] - array[2] / 2.0f;
    }
    
    private boolean crossCheckDiagonal(final int n, final int n2, final int n3, final int n4) {
        final int[] crossCheckStateCount = this.getCrossCheckStateCount();
        int n5;
        for (n5 = 0; n >= n5 && n2 >= n5 && this.image.get(n2 - n5, n - n5); ++n5) {
            ++crossCheckStateCount[2];
        }
        int n6;
        boolean b;
        if (n < n5 || n2 < (n6 = n5)) {
            b = false;
        }
        else {
            while (n >= n6 && n2 >= n6 && !this.image.get(n2 - n6, n - n6) && crossCheckStateCount[1] <= n3) {
                ++crossCheckStateCount[1];
                ++n6;
            }
            if (n < n6 || n2 < n6 || crossCheckStateCount[1] > n3) {
                b = false;
            }
            else {
                while (n >= n6 && n2 >= n6 && this.image.get(n2 - n6, n - n6) && crossCheckStateCount[0] <= n3) {
                    ++crossCheckStateCount[0];
                    ++n6;
                }
                if (crossCheckStateCount[0] > n3) {
                    b = false;
                }
                else {
                    int height;
                    int width;
                    int n7;
                    for (height = this.image.getHeight(), width = this.image.getWidth(), n7 = 1; n + n7 < height && n2 + n7 < width && this.image.get(n2 + n7, n + n7); ++n7) {
                        ++crossCheckStateCount[2];
                    }
                    if (n + n7 < height) {
                        int n8 = n7;
                        if (n2 + n7 < width) {
                            while (n + n8 < height && n2 + n8 < width && !this.image.get(n2 + n8, n + n8) && crossCheckStateCount[3] < n3) {
                                ++crossCheckStateCount[3];
                                ++n8;
                            }
                            if (n + n8 >= height || n2 + n8 >= width || crossCheckStateCount[3] >= n3) {
                                b = false;
                                return b;
                            }
                            while (n + n8 < height && n2 + n8 < width && this.image.get(n2 + n8, n + n8) && crossCheckStateCount[4] < n3) {
                                ++crossCheckStateCount[4];
                                ++n8;
                            }
                            b = (crossCheckStateCount[4] < n3 && (Math.abs(crossCheckStateCount[0] + crossCheckStateCount[1] + crossCheckStateCount[2] + crossCheckStateCount[3] + crossCheckStateCount[4] - n4) < n4 * 2 && foundPatternCross(crossCheckStateCount)));
                            return b;
                        }
                    }
                    b = false;
                }
            }
        }
        return b;
    }
    
    private float crossCheckHorizontal(int n, final int n2, final int n3, final int n4) {
        final BitMatrix image = this.image;
        final int width = image.getWidth();
        final int[] crossCheckStateCount = this.getCrossCheckStateCount();
        int n5;
        for (n5 = n; n5 >= 0 && image.get(n5, n2); --n5) {
            ++crossCheckStateCount[2];
        }
        int n6;
        float centerFromEnd;
        if ((n6 = n5) < 0) {
            centerFromEnd = Float.NaN;
        }
        else {
            while (n6 >= 0 && !image.get(n6, n2) && crossCheckStateCount[1] <= n3) {
                ++crossCheckStateCount[1];
                --n6;
            }
            if (n6 < 0 || crossCheckStateCount[1] > n3) {
                centerFromEnd = Float.NaN;
            }
            else {
                while (n6 >= 0 && image.get(n6, n2) && crossCheckStateCount[0] <= n3) {
                    ++crossCheckStateCount[0];
                    --n6;
                }
                if (crossCheckStateCount[0] > n3) {
                    centerFromEnd = Float.NaN;
                }
                else {
                    int n7;
                    for (n7 = n + 1; n7 < width && image.get(n7, n2); ++n7) {
                        ++crossCheckStateCount[2];
                    }
                    if ((n = n7) == width) {
                        centerFromEnd = Float.NaN;
                    }
                    else {
                        while (n < width && !image.get(n, n2) && crossCheckStateCount[3] < n3) {
                            ++crossCheckStateCount[3];
                            ++n;
                        }
                        if (n == width || crossCheckStateCount[3] >= n3) {
                            centerFromEnd = Float.NaN;
                        }
                        else {
                            while (n < width && image.get(n, n2) && crossCheckStateCount[4] < n3) {
                                ++crossCheckStateCount[4];
                                ++n;
                            }
                            if (crossCheckStateCount[4] >= n3) {
                                centerFromEnd = Float.NaN;
                            }
                            else if (Math.abs(crossCheckStateCount[0] + crossCheckStateCount[1] + crossCheckStateCount[2] + crossCheckStateCount[3] + crossCheckStateCount[4] - n4) * 5 >= n4) {
                                centerFromEnd = Float.NaN;
                            }
                            else if (foundPatternCross(crossCheckStateCount)) {
                                centerFromEnd = centerFromEnd(crossCheckStateCount, n);
                            }
                            else {
                                centerFromEnd = Float.NaN;
                            }
                        }
                    }
                }
            }
        }
        return centerFromEnd;
    }
    
    private float crossCheckVertical(int n, final int n2, final int n3, final int n4) {
        final BitMatrix image = this.image;
        final int height = image.getHeight();
        final int[] crossCheckStateCount = this.getCrossCheckStateCount();
        int n5;
        for (n5 = n; n5 >= 0 && image.get(n2, n5); --n5) {
            ++crossCheckStateCount[2];
        }
        int n6;
        float centerFromEnd;
        if ((n6 = n5) < 0) {
            centerFromEnd = Float.NaN;
        }
        else {
            while (n6 >= 0 && !image.get(n2, n6) && crossCheckStateCount[1] <= n3) {
                ++crossCheckStateCount[1];
                --n6;
            }
            if (n6 < 0 || crossCheckStateCount[1] > n3) {
                centerFromEnd = Float.NaN;
            }
            else {
                while (n6 >= 0 && image.get(n2, n6) && crossCheckStateCount[0] <= n3) {
                    ++crossCheckStateCount[0];
                    --n6;
                }
                if (crossCheckStateCount[0] > n3) {
                    centerFromEnd = Float.NaN;
                }
                else {
                    int n7;
                    for (n7 = n + 1; n7 < height && image.get(n2, n7); ++n7) {
                        ++crossCheckStateCount[2];
                    }
                    if ((n = n7) == height) {
                        centerFromEnd = Float.NaN;
                    }
                    else {
                        while (n < height && !image.get(n2, n) && crossCheckStateCount[3] < n3) {
                            ++crossCheckStateCount[3];
                            ++n;
                        }
                        if (n == height || crossCheckStateCount[3] >= n3) {
                            centerFromEnd = Float.NaN;
                        }
                        else {
                            while (n < height && image.get(n2, n) && crossCheckStateCount[4] < n3) {
                                ++crossCheckStateCount[4];
                                ++n;
                            }
                            if (crossCheckStateCount[4] >= n3) {
                                centerFromEnd = Float.NaN;
                            }
                            else if (Math.abs(crossCheckStateCount[0] + crossCheckStateCount[1] + crossCheckStateCount[2] + crossCheckStateCount[3] + crossCheckStateCount[4] - n4) * 5 >= n4 * 2) {
                                centerFromEnd = Float.NaN;
                            }
                            else if (foundPatternCross(crossCheckStateCount)) {
                                centerFromEnd = centerFromEnd(crossCheckStateCount, n);
                            }
                            else {
                                centerFromEnd = Float.NaN;
                            }
                        }
                    }
                }
            }
        }
        return centerFromEnd;
    }
    
    private int findRowSkip() {
        final int n = 0;
        int n2;
        if (this.possibleCenters.size() <= 1) {
            n2 = n;
        }
        else {
            ResultPoint resultPoint = null;
            final Iterator<FinderPattern> iterator = this.possibleCenters.iterator();
            FinderPattern finderPattern;
            while (true) {
                n2 = n;
                if (!iterator.hasNext()) {
                    return n2;
                }
                finderPattern = iterator.next();
                if (finderPattern.getCount() < 2) {
                    continue;
                }
                if (resultPoint != null) {
                    break;
                }
                resultPoint = finderPattern;
            }
            this.hasSkipped = true;
            n2 = (int)(Math.abs(resultPoint.getX() - finderPattern.getX()) - Math.abs(resultPoint.getY() - finderPattern.getY())) / 2;
        }
        return n2;
    }
    
    protected static boolean foundPatternCross(final int[] array) {
        final boolean b = false;
        int n = 0;
        for (int i = 0; i < 5; ++i) {
            final int n2 = array[i];
            if (n2 == 0) {
                return b;
            }
            n += n2;
        }
        boolean b2 = b;
        if (n < 7) {
            return b2;
        }
        final float n3 = n / 7.0f;
        final float n4 = n3 / 2.0f;
        b2 = b;
        if (Math.abs(n3 - array[0]) >= n4) {
            return b2;
        }
        b2 = b;
        if (Math.abs(n3 - array[1]) >= n4) {
            return b2;
        }
        b2 = b;
        if (Math.abs(3.0f * n3 - array[2]) >= 3.0f * n4) {
            return b2;
        }
        b2 = b;
        if (Math.abs(n3 - array[3]) >= n4) {
            return b2;
        }
        b2 = b;
        if (Math.abs(n3 - array[4]) < n4) {
            b2 = true;
            return b2;
        }
        return b2;
    }
    
    private int[] getCrossCheckStateCount() {
        this.crossCheckStateCount[0] = 0;
        this.crossCheckStateCount[1] = 0;
        this.crossCheckStateCount[2] = 0;
        this.crossCheckStateCount[3] = 0;
        this.crossCheckStateCount[4] = 0;
        return this.crossCheckStateCount;
    }
    
    private boolean haveMultiplyConfirmedCenters() {
        boolean b = false;
        int n = 0;
        float n2 = 0.0f;
        final int size = this.possibleCenters.size();
        for (final FinderPattern finderPattern : this.possibleCenters) {
            if (finderPattern.getCount() >= 2) {
                ++n;
                n2 += finderPattern.getEstimatedModuleSize();
            }
        }
        if (n >= 3) {
            final float n3 = n2 / size;
            float n4 = 0.0f;
            final Iterator<FinderPattern> iterator2 = this.possibleCenters.iterator();
            while (iterator2.hasNext()) {
                n4 += Math.abs(iterator2.next().getEstimatedModuleSize() - n3);
            }
            if (n4 <= 0.05f * n2) {
                b = true;
            }
        }
        return b;
    }
    
    private FinderPattern[] selectBestPatterns() throws NotFoundException {
        final int size = this.possibleCenters.size();
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (size > 3) {
            float n = 0.0f;
            float n2 = 0.0f;
            final Iterator<FinderPattern> iterator = this.possibleCenters.iterator();
            while (iterator.hasNext()) {
                final float estimatedModuleSize = iterator.next().getEstimatedModuleSize();
                n += estimatedModuleSize;
                n2 += estimatedModuleSize * estimatedModuleSize;
            }
            final float n3 = n / size;
            final float b = (float)Math.sqrt(n2 / size - n3 * n3);
            Collections.sort(this.possibleCenters, new FurthestFromAverageComparator(n3));
            final float max = Math.max(0.2f * n3, b);
            int n5;
            for (int n4 = 0; n4 < this.possibleCenters.size() && this.possibleCenters.size() > 3; n4 = n5 + 1) {
                n5 = n4;
                if (Math.abs(this.possibleCenters.get(n4).getEstimatedModuleSize() - n3) > max) {
                    this.possibleCenters.remove(n4);
                    n5 = n4 - 1;
                }
            }
        }
        if (this.possibleCenters.size() > 3) {
            float n6 = 0.0f;
            final Iterator<FinderPattern> iterator2 = this.possibleCenters.iterator();
            while (iterator2.hasNext()) {
                n6 += iterator2.next().getEstimatedModuleSize();
            }
            Collections.sort(this.possibleCenters, new CenterComparator(n6 / this.possibleCenters.size()));
            this.possibleCenters.subList(3, this.possibleCenters.size()).clear();
        }
        return new FinderPattern[] { this.possibleCenters.get(0), this.possibleCenters.get(1), this.possibleCenters.get(2) };
    }
    
    final FinderPatternInfo find(final Map<DecodeHintType, ?> map) throws NotFoundException {
        boolean b;
        if (map != null && map.containsKey(DecodeHintType.TRY_HARDER)) {
            b = true;
        }
        else {
            b = false;
        }
        final boolean b2 = map != null && map.containsKey(DecodeHintType.PURE_BARCODE);
        final int height = this.image.getHeight();
        final int width = this.image.getWidth();
        int n = height * 3 / 228;
        if (n < 3 || b) {
            n = 3;
        }
        int n2 = 0;
        final int[] array = new int[5];
        int haveMultiplyConfirmedCenters2;
        for (int n3 = n - 1; n3 < height && n2 == 0; n3 += n, n2 = haveMultiplyConfirmedCenters2) {
            array[0] = 0;
            array[2] = (array[1] = 0);
            array[4] = (array[3] = 0);
            int n4 = 0;
            int i = 0;
            int n5 = n;
            while (i < width) {
                int n6;
                if (this.image.get(i, n3)) {
                    n6 = n4;
                    if ((n4 & 0x1) == 0x1) {
                        n6 = n4 + 1;
                    }
                    ++array[n6];
                }
                else if ((n4 & 0x1) == 0x0) {
                    if (n4 == 4) {
                        if (foundPatternCross(array)) {
                            if (this.handlePossibleCenter(array, n3, i, b2)) {
                                final int n7 = 2;
                                int haveMultiplyConfirmedCenters;
                                int n8;
                                if (this.hasSkipped) {
                                    haveMultiplyConfirmedCenters = (this.haveMultiplyConfirmedCenters() ? 1 : 0);
                                    n8 = n3;
                                }
                                else {
                                    final int rowSkip = this.findRowSkip();
                                    haveMultiplyConfirmedCenters = n2;
                                    n8 = n3;
                                    if (rowSkip > array[2]) {
                                        n8 = n3 + (rowSkip - array[2] - 2);
                                        i = width - 1;
                                        haveMultiplyConfirmedCenters = n2;
                                    }
                                }
                                n6 = 0;
                                array[0] = 0;
                                array[2] = (array[1] = 0);
                                array[4] = (array[3] = 0);
                                n2 = haveMultiplyConfirmedCenters;
                                n3 = n8;
                                n5 = n7;
                            }
                            else {
                                array[0] = array[2];
                                array[1] = array[3];
                                array[2] = array[4];
                                array[3] = 1;
                                array[4] = 0;
                                n6 = 3;
                            }
                        }
                        else {
                            array[0] = array[2];
                            array[1] = array[3];
                            array[2] = array[4];
                            array[3] = 1;
                            array[4] = 0;
                            n6 = 3;
                        }
                    }
                    else {
                        n6 = n4 + 1;
                        ++array[n6];
                    }
                }
                else {
                    ++array[n4];
                    n6 = n4;
                }
                ++i;
                n4 = n6;
            }
            haveMultiplyConfirmedCenters2 = n2;
            n = n5;
            if (foundPatternCross(array)) {
                haveMultiplyConfirmedCenters2 = n2;
                n = n5;
                if (this.handlePossibleCenter(array, n3, width, b2)) {
                    final int n9 = array[0];
                    haveMultiplyConfirmedCenters2 = n2;
                    n = n9;
                    if (this.hasSkipped) {
                        haveMultiplyConfirmedCenters2 = (this.haveMultiplyConfirmedCenters() ? 1 : 0);
                        n = n9;
                    }
                }
            }
        }
        final FinderPattern[] selectBestPatterns = this.selectBestPatterns();
        ResultPoint.orderBestPatterns(selectBestPatterns);
        return new FinderPatternInfo(selectBestPatterns);
    }
    
    protected final BitMatrix getImage() {
        return this.image;
    }
    
    protected final List<FinderPattern> getPossibleCenters() {
        return this.possibleCenters;
    }
    
    protected final boolean handlePossibleCenter(final int[] array, int n, int n2, final boolean b) {
        final int n3 = array[0] + array[1] + array[2] + array[3] + array[4];
        final float centerFromEnd = centerFromEnd(array, n2);
        final float crossCheckVertical = this.crossCheckVertical(n, (int)centerFromEnd, array[2], n3);
        if (Float.isNaN(crossCheckVertical)) {
            return false;
        }
        final float crossCheckHorizontal = this.crossCheckHorizontal((int)centerFromEnd, (int)crossCheckVertical, array[2], n3);
        if (Float.isNaN(crossCheckHorizontal) || (b && !this.crossCheckDiagonal((int)crossCheckVertical, (int)crossCheckHorizontal, array[2], n3))) {
            return false;
        }
        final float n4 = n3 / 7.0f;
        final int n5 = 0;
        n2 = 0;
        while (true) {
            n = n5;
            if (n2 >= this.possibleCenters.size()) {
                break;
            }
            final FinderPattern finderPattern = this.possibleCenters.get(n2);
            if (finderPattern.aboutEquals(n4, crossCheckVertical, crossCheckHorizontal)) {
                this.possibleCenters.set(n2, finderPattern.combineEstimate(crossCheckVertical, crossCheckHorizontal, n4));
                n = 1;
                break;
            }
            ++n2;
        }
        if (n == 0) {
            final FinderPattern finderPattern2 = new FinderPattern(crossCheckHorizontal, crossCheckVertical, n4);
            this.possibleCenters.add(finderPattern2);
            if (this.resultPointCallback != null) {
                this.resultPointCallback.foundPossibleResultPoint(finderPattern2);
            }
        }
        return true;
        b2 = false;
        return b2;
    }
    
    private static final class CenterComparator implements Serializable, Comparator<FinderPattern>
    {
        private final float average;
        
        private CenterComparator(final float average) {
            this.average = average;
        }
        
        @Override
        public int compare(final FinderPattern finderPattern, final FinderPattern finderPattern2) {
            int n;
            if (finderPattern2.getCount() == finderPattern.getCount()) {
                final float abs = Math.abs(finderPattern2.getEstimatedModuleSize() - this.average);
                final float abs2 = Math.abs(finderPattern.getEstimatedModuleSize() - this.average);
                if (abs < abs2) {
                    n = 1;
                }
                else if (abs == abs2) {
                    n = 0;
                }
                else {
                    n = -1;
                }
            }
            else {
                n = finderPattern2.getCount() - finderPattern.getCount();
            }
            return n;
        }
    }
    
    private static final class FurthestFromAverageComparator implements Serializable, Comparator<FinderPattern>
    {
        private final float average;
        
        private FurthestFromAverageComparator(final float average) {
            this.average = average;
        }
        
        @Override
        public int compare(final FinderPattern finderPattern, final FinderPattern finderPattern2) {
            final float abs = Math.abs(finderPattern2.getEstimatedModuleSize() - this.average);
            final float abs2 = Math.abs(finderPattern.getEstimatedModuleSize() - this.average);
            int n;
            if (abs < abs2) {
                n = -1;
            }
            else if (abs == abs2) {
                n = 0;
            }
            else {
                n = 1;
            }
            return n;
        }
    }
}
