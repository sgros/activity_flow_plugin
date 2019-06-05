// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.multi.qrcode.detector;

import java.util.List;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import com.google.zxing.ReaderException;
import java.util.ArrayList;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.detector.Detector;

public final class MultiDetector extends Detector
{
    private static final DetectorResult[] EMPTY_DETECTOR_RESULTS;
    
    static {
        EMPTY_DETECTOR_RESULTS = new DetectorResult[0];
    }
    
    public MultiDetector(final BitMatrix bitMatrix) {
        super(bitMatrix);
    }
    
    public DetectorResult[] detectMulti(Map<DecodeHintType, ?> empty_DETECTOR_RESULTS) throws NotFoundException {
        final BitMatrix image = this.getImage();
        ResultPointCallback resultPointCallback;
        if (empty_DETECTOR_RESULTS == null) {
            resultPointCallback = null;
        }
        else {
            resultPointCallback = ((Map<DecodeHintType, ResultPointCallback>)empty_DETECTOR_RESULTS).get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
        }
        final FinderPatternInfo[] multi = new MultiFinderPatternFinder(image, resultPointCallback).findMulti((Map<DecodeHintType, ?>)empty_DETECTOR_RESULTS);
        if (multi.length == 0) {
            throw NotFoundException.getNotFoundInstance();
        }
        empty_DETECTOR_RESULTS = new ArrayList();
        final int length = multi.length;
        int n = 0;
    Label_0089_Outer:
        while (true) {
            Label_0095: {
                if (n >= length) {
                    break Label_0095;
                }
                final FinderPatternInfo finderPatternInfo = multi[n];
                while (true) {
                    try {
                        ((List<DetectorResult>)empty_DETECTOR_RESULTS).add(this.processFinderPatternInfo(finderPatternInfo));
                        ++n;
                        continue Label_0089_Outer;
                        Label_0110: {
                            empty_DETECTOR_RESULTS = ((List<DetectorResult>)empty_DETECTOR_RESULTS).toArray(new DetectorResult[((List)empty_DETECTOR_RESULTS).size()]);
                        }
                        return (DetectorResult[])empty_DETECTOR_RESULTS;
                        empty_DETECTOR_RESULTS = MultiDetector.EMPTY_DETECTOR_RESULTS;
                        return (DetectorResult[])empty_DETECTOR_RESULTS;
                        // iftrue(Label_0110:, !empty_DETECTOR_RESULTS.isEmpty())
                        return MultiDetector.EMPTY_DETECTOR_RESULTS;
                    }
                    catch (ReaderException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
}
