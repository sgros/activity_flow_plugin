// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.multi;

import com.google.zxing.FormatException;
import com.google.zxing.ChecksumException;
import com.google.zxing.NotFoundException;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.Result;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ResultPoint;
import com.google.zxing.Reader;

public final class ByQuadrantReader implements Reader
{
    private final Reader delegate;
    
    public ByQuadrantReader(final Reader delegate) {
        this.delegate = delegate;
    }
    
    private static void makeAbsolute(final ResultPoint[] array, final int n, final int n2) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                final ResultPoint resultPoint = array[i];
                array[i] = new ResultPoint(resultPoint.getX() + n, resultPoint.getY() + n2);
            }
        }
    }
    
    @Override
    public Result decode(final BinaryBitmap binaryBitmap) throws NotFoundException, ChecksumException, FormatException {
        return this.decode(binaryBitmap, null);
    }
    
    @Override
    public Result decode(BinaryBitmap o, final Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        final int width = ((BinaryBitmap)o).getWidth();
        final int height = ((BinaryBitmap)o).getHeight();
        final int n = width / 2;
        final int n2 = height / 2;
        try {
            o = this.delegate.decode(((BinaryBitmap)o).crop(0, 0, n, n2), map);
            return (Result)o;
        }
        catch (NotFoundException ex) {
            try {
                final Result decode = this.delegate.decode(((BinaryBitmap)o).crop(n, 0, n, n2), map);
                makeAbsolute(decode.getResultPoints(), n, 0);
                o = decode;
            }
            catch (NotFoundException ex2) {
                try {
                    final Result decode2 = this.delegate.decode(((BinaryBitmap)o).crop(0, n2, n, n2), map);
                    makeAbsolute(decode2.getResultPoints(), 0, n2);
                    o = decode2;
                }
                catch (NotFoundException ex3) {
                    try {
                        final Result decode3 = this.delegate.decode(((BinaryBitmap)o).crop(n, n2, n, n2), map);
                        makeAbsolute(decode3.getResultPoints(), n, n2);
                        o = decode3;
                    }
                    catch (NotFoundException ex4) {
                        final int n3 = n / 2;
                        final int n4 = n2 / 2;
                        o = ((BinaryBitmap)o).crop(n3, n4, n, n2);
                        o = this.delegate.decode((BinaryBitmap)o, map);
                        makeAbsolute(((Result)o).getResultPoints(), n3, n4);
                    }
                }
            }
        }
    }
    
    @Override
    public void reset() {
        this.delegate.reset();
    }
}
