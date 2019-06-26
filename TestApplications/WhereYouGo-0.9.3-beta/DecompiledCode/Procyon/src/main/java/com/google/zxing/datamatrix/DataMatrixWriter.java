// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix;

import com.google.zxing.datamatrix.encoder.ErrorCorrection;
import com.google.zxing.datamatrix.encoder.HighLevelEncoder;
import com.google.zxing.Dimension;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.datamatrix.encoder.SymbolInfo;
import com.google.zxing.datamatrix.encoder.DefaultPlacement;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.Writer;

public final class DataMatrixWriter implements Writer
{
    private static BitMatrix convertByteMatrixToBitMatrix(final ByteMatrix byteMatrix) {
        final int width = byteMatrix.getWidth();
        final int height = byteMatrix.getHeight();
        final BitMatrix bitMatrix = new BitMatrix(width, height);
        bitMatrix.clear();
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (byteMatrix.get(i, j) == 1) {
                    bitMatrix.set(i, j);
                }
            }
        }
        return bitMatrix;
    }
    
    private static BitMatrix encodeLowLevel(final DefaultPlacement defaultPlacement, final SymbolInfo symbolInfo) {
        final int symbolDataWidth = symbolInfo.getSymbolDataWidth();
        final int symbolDataHeight = symbolInfo.getSymbolDataHeight();
        final ByteMatrix byteMatrix = new ByteMatrix(symbolInfo.getSymbolWidth(), symbolInfo.getSymbolHeight());
        int n = 0;
        for (int i = 0; i < symbolDataHeight; ++i) {
            int n2 = n;
            if (i % symbolInfo.matrixHeight == 0) {
                int n3 = 0;
                for (int j = 0; j < symbolInfo.getSymbolWidth(); ++j) {
                    byteMatrix.set(n3, n, j % 2 == 0);
                    ++n3;
                }
                n2 = n + 1;
            }
            int n4 = 0;
            for (int k = 0; k < symbolDataWidth; ++k) {
                int n5 = n4;
                if (k % symbolInfo.matrixWidth == 0) {
                    byteMatrix.set(n4, n2, true);
                    n5 = n4 + 1;
                }
                byteMatrix.set(n5, n2, defaultPlacement.getBit(k, i));
                n4 = ++n5;
                if (k % symbolInfo.matrixWidth == symbolInfo.matrixWidth - 1) {
                    byteMatrix.set(n5, n2, i % 2 == 0);
                    n4 = n5 + 1;
                }
            }
            final int n6 = n = n2 + 1;
            if (i % symbolInfo.matrixHeight == symbolInfo.matrixHeight - 1) {
                int n7 = 0;
                for (int l = 0; l < symbolInfo.getSymbolWidth(); ++l) {
                    byteMatrix.set(n7, n6, true);
                    ++n7;
                }
                n = n6 + 1;
            }
        }
        return convertByteMatrixToBitMatrix(byteMatrix);
    }
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat barcodeFormat, final int n, final int n2) {
        return this.encode(s, barcodeFormat, n, n2, null);
    }
    
    @Override
    public BitMatrix encode(String encodeHighLevel, final BarcodeFormat obj, final int i, final int j, final Map<EncodeHintType, ?> map) {
        if (encodeHighLevel.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (obj != BarcodeFormat.DATA_MATRIX) {
            throw new IllegalArgumentException("Can only encode DATA_MATRIX, but got " + obj);
        }
        if (i < 0 || j < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + i + 'x' + j);
        }
        SymbolShapeHint force_NONE = SymbolShapeHint.FORCE_NONE;
        Dimension dimension = null;
        Dimension dimension2 = null;
        Dimension dimension4;
        final Dimension dimension3 = dimension4 = null;
        SymbolShapeHint symbolShapeHint = force_NONE;
        if (map != null) {
            final SymbolShapeHint symbolShapeHint2 = (SymbolShapeHint)map.get(EncodeHintType.DATA_MATRIX_SHAPE);
            if (symbolShapeHint2 != null) {
                force_NONE = symbolShapeHint2;
            }
            final Dimension dimension5 = (Dimension)map.get(EncodeHintType.MIN_SIZE);
            if (dimension5 != null) {
                dimension2 = dimension5;
            }
            final Dimension dimension6 = (Dimension)map.get(EncodeHintType.MAX_SIZE);
            dimension4 = dimension3;
            dimension = dimension2;
            symbolShapeHint = force_NONE;
            if (dimension6 != null) {
                dimension4 = dimension6;
                symbolShapeHint = force_NONE;
                dimension = dimension2;
            }
        }
        encodeHighLevel = HighLevelEncoder.encodeHighLevel(encodeHighLevel, symbolShapeHint, dimension, dimension4);
        final SymbolInfo lookup = SymbolInfo.lookup(encodeHighLevel.length(), symbolShapeHint, dimension, dimension4, true);
        final DefaultPlacement defaultPlacement = new DefaultPlacement(ErrorCorrection.encodeECC200(encodeHighLevel, lookup), lookup.getSymbolDataWidth(), lookup.getSymbolDataHeight());
        defaultPlacement.place();
        return encodeLowLevel(defaultPlacement, lookup);
    }
}
