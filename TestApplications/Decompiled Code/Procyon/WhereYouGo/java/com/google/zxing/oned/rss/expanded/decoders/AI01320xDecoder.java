// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;

final class AI01320xDecoder extends AI013x0xDecoder
{
    AI01320xDecoder(final BitArray bitArray) {
        super(bitArray);
    }
    
    @Override
    protected void addWeightCode(final StringBuilder sb, final int n) {
        if (n < 10000) {
            sb.append("(3202)");
        }
        else {
            sb.append("(3203)");
        }
    }
    
    @Override
    protected int checkWeight(int n) {
        if (n >= 10000) {
            n -= 10000;
        }
        return n;
    }
}
