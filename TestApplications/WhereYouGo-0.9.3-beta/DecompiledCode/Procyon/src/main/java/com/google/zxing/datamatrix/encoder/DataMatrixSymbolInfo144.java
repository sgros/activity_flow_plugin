// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix.encoder;

final class DataMatrixSymbolInfo144 extends SymbolInfo
{
    DataMatrixSymbolInfo144() {
        super(false, 1558, 620, 22, 22, 36, -1, 62);
    }
    
    @Override
    public int getDataLengthForInterleavedBlock(int n) {
        if (n <= 8) {
            n = 156;
        }
        else {
            n = 155;
        }
        return n;
    }
    
    @Override
    public int getInterleavedBlockCount() {
        return 10;
    }
}
