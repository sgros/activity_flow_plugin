package com.journeyapps.barcodescanner;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.common.HybridBinarizer;

public class InvertedDecoder extends Decoder {
    public InvertedDecoder(Reader reader) {
        super(reader);
    }

    /* Access modifiers changed, original: protected */
    public BinaryBitmap toBitmap(LuminanceSource source) {
        return new BinaryBitmap(new HybridBinarizer(source.invert()));
    }
}
