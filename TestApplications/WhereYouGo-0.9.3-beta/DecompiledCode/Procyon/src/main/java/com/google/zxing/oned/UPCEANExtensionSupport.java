// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;

final class UPCEANExtensionSupport
{
    private static final int[] EXTENSION_START_PATTERN;
    private final UPCEANExtension5Support fiveSupport;
    private final UPCEANExtension2Support twoSupport;
    
    static {
        EXTENSION_START_PATTERN = new int[] { 1, 1, 2 };
    }
    
    UPCEANExtensionSupport() {
        this.twoSupport = new UPCEANExtension2Support();
        this.fiveSupport = new UPCEANExtension5Support();
    }
    
    Result decodeRow(final int n, BitArray o, final int n2) throws NotFoundException {
        final int[] guardPattern = UPCEANReader.findGuardPattern((BitArray)o, n2, false, UPCEANExtensionSupport.EXTENSION_START_PATTERN);
        try {
            o = this.fiveSupport.decodeRow(n, (BitArray)o, guardPattern);
            return (Result)o;
        }
        catch (ReaderException ex) {
            o = this.twoSupport.decodeRow(n, (BitArray)o, guardPattern);
            return (Result)o;
        }
    }
}
