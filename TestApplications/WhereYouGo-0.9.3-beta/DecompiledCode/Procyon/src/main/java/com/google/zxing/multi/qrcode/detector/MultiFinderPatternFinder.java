// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.multi.qrcode.detector;

import java.io.Serializable;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import com.google.zxing.NotFoundException;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import com.google.zxing.qrcode.detector.FinderPatternFinder;

final class MultiFinderPatternFinder extends FinderPatternFinder
{
    private static final float DIFF_MODSIZE_CUTOFF = 0.5f;
    private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05f;
    private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY;
    private static final float MAX_MODULE_COUNT_PER_EDGE = 180.0f;
    private static final float MIN_MODULE_COUNT_PER_EDGE = 9.0f;
    
    static {
        EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];
    }
    
    MultiFinderPatternFinder(final BitMatrix bitMatrix) {
        super(bitMatrix);
    }
    
    MultiFinderPatternFinder(final BitMatrix bitMatrix, final ResultPointCallback resultPointCallback) {
        super(bitMatrix, resultPointCallback);
    }
    
    private FinderPattern[][] selectMutipleBestPatterns() throws NotFoundException {
        final List<FinderPattern> possibleCenters = this.getPossibleCenters();
        final int size = possibleCenters.size();
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        FinderPattern[][] array;
        if (size == 3) {
            array = new FinderPattern[][] { { possibleCenters.get(0), possibleCenters.get(1), possibleCenters.get(2) } };
        }
        else {
            Collections.sort((List<Object>)possibleCenters, (Comparator<? super Object>)new ModuleSizeComparator());
            final ArrayList<FinderPattern[]> list = new ArrayList<FinderPattern[]>();
            for (int i = 0; i < size - 2; ++i) {
                final FinderPattern finderPattern = possibleCenters.get(i);
                if (finderPattern != null) {
                    for (int j = i + 1; j < size - 1; ++j) {
                        final FinderPattern finderPattern2 = possibleCenters.get(j);
                        if (finderPattern2 != null) {
                            final float n = (finderPattern.getEstimatedModuleSize() - finderPattern2.getEstimatedModuleSize()) / Math.min(finderPattern.getEstimatedModuleSize(), finderPattern2.getEstimatedModuleSize());
                            if (Math.abs(finderPattern.getEstimatedModuleSize() - finderPattern2.getEstimatedModuleSize()) > 0.5f && n >= 0.05f) {
                                break;
                            }
                            for (int k = j + 1; k < size; ++k) {
                                final FinderPattern finderPattern3 = possibleCenters.get(k);
                                if (finderPattern3 != null) {
                                    final float n2 = (finderPattern2.getEstimatedModuleSize() - finderPattern3.getEstimatedModuleSize()) / Math.min(finderPattern2.getEstimatedModuleSize(), finderPattern3.getEstimatedModuleSize());
                                    if (Math.abs(finderPattern2.getEstimatedModuleSize() - finderPattern3.getEstimatedModuleSize()) > 0.5f && n2 >= 0.05f) {
                                        break;
                                    }
                                    final FinderPattern[] array2 = { finderPattern, finderPattern2, finderPattern3 };
                                    ResultPoint.orderBestPatterns(array2);
                                    final FinderPatternInfo finderPatternInfo = new FinderPatternInfo(array2);
                                    final float distance = ResultPoint.distance(finderPatternInfo.getTopLeft(), finderPatternInfo.getBottomLeft());
                                    final float distance2 = ResultPoint.distance(finderPatternInfo.getTopRight(), finderPatternInfo.getBottomLeft());
                                    final float distance3 = ResultPoint.distance(finderPatternInfo.getTopLeft(), finderPatternInfo.getTopRight());
                                    final float n3 = (distance + distance3) / (finderPattern.getEstimatedModuleSize() * 2.0f);
                                    if (n3 <= 180.0f && n3 >= 9.0f && Math.abs((distance - distance3) / Math.min(distance, distance3)) < 0.1f) {
                                        final float b = (float)Math.sqrt(distance * distance + distance3 * distance3);
                                        if (Math.abs((distance2 - b) / Math.min(distance2, b)) < 0.1f) {
                                            list.add(array2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (list.isEmpty()) {
                throw NotFoundException.getNotFoundInstance();
            }
            array = list.toArray(new FinderPattern[list.size()][]);
        }
        return array;
    }
    
    public FinderPatternInfo[] findMulti(final Map<DecodeHintType, ?> map) throws NotFoundException {
        boolean b;
        if (map != null && map.containsKey(DecodeHintType.TRY_HARDER)) {
            b = true;
        }
        else {
            b = false;
        }
        final boolean b2 = map != null && map.containsKey(DecodeHintType.PURE_BARCODE);
        final BitMatrix image = this.getImage();
        final int height = image.getHeight();
        final int width = image.getWidth();
        int n = (int)(height / 228.0f * 3.0f);
        if (n < 3 || b) {
            n = 3;
        }
        final int[] array = new int[5];
        for (int i = n - 1; i < height; i += n) {
            array[0] = 0;
            array[2] = (array[1] = 0);
            array[4] = (array[3] = 0);
            int n2 = 0;
            for (int j = 0; j < width; ++j) {
                if (image.get(j, i)) {
                    int n3 = n2;
                    if ((n2 & 0x1) == 0x1) {
                        n3 = n2 + 1;
                    }
                    ++array[n3];
                    n2 = n3;
                }
                else if ((n2 & 0x1) == 0x0) {
                    if (n2 == 4) {
                        if (FinderPatternFinder.foundPatternCross(array) && this.handlePossibleCenter(array, i, j, b2)) {
                            n2 = 0;
                            array[0] = 0;
                            array[2] = (array[1] = 0);
                            array[4] = (array[3] = 0);
                        }
                        else {
                            array[0] = array[2];
                            array[1] = array[3];
                            array[2] = array[4];
                            array[3] = 1;
                            array[4] = 0;
                            n2 = 3;
                        }
                    }
                    else {
                        ++n2;
                        ++array[n2];
                    }
                }
                else {
                    ++array[n2];
                }
            }
            if (FinderPatternFinder.foundPatternCross(array)) {
                this.handlePossibleCenter(array, i, width, b2);
            }
        }
        final FinderPattern[][] selectMutipleBestPatterns = this.selectMutipleBestPatterns();
        final ArrayList<FinderPatternInfo> list = new ArrayList<FinderPatternInfo>();
        for (final FinderPattern[] array2 : selectMutipleBestPatterns) {
            ResultPoint.orderBestPatterns(array2);
            list.add(new FinderPatternInfo(array2));
        }
        FinderPatternInfo[] empty_RESULT_ARRAY;
        if (list.isEmpty()) {
            empty_RESULT_ARRAY = MultiFinderPatternFinder.EMPTY_RESULT_ARRAY;
        }
        else {
            empty_RESULT_ARRAY = list.toArray(new FinderPatternInfo[list.size()]);
        }
        return empty_RESULT_ARRAY;
    }
    
    private static final class ModuleSizeComparator implements Serializable, Comparator<FinderPattern>
    {
        @Override
        public int compare(final FinderPattern finderPattern, final FinderPattern finderPattern2) {
            final float n = finderPattern2.getEstimatedModuleSize() - finderPattern.getEstimatedModuleSize();
            int n2;
            if (n < 0.0) {
                n2 = -1;
            }
            else if (n > 0.0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            return n2;
        }
    }
}
