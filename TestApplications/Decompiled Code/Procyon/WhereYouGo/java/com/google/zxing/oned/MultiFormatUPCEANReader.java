// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import com.google.zxing.BarcodeFormat;
import java.util.ArrayList;
import java.util.Collection;
import com.google.zxing.DecodeHintType;
import java.util.Map;

public final class MultiFormatUPCEANReader extends OneDReader
{
    private final UPCEANReader[] readers;
    
    public MultiFormatUPCEANReader(final Map<DecodeHintType, ?> map) {
        Collection collection;
        if (map == null) {
            collection = null;
        }
        else {
            collection = (Collection)map.get(DecodeHintType.POSSIBLE_FORMATS);
        }
        final ArrayList<EAN13Reader> list = new ArrayList<EAN13Reader>();
        if (collection != null) {
            if (collection.contains(BarcodeFormat.EAN_13)) {
                list.add(new EAN13Reader());
            }
            else if (collection.contains(BarcodeFormat.UPC_A)) {
                list.add((EAN13Reader)new UPCAReader());
            }
            if (collection.contains(BarcodeFormat.EAN_8)) {
                list.add((EAN13Reader)new EAN8Reader());
            }
            if (collection.contains(BarcodeFormat.UPC_E)) {
                list.add((EAN13Reader)new UPCEReader());
            }
        }
        if (list.isEmpty()) {
            list.add(new EAN13Reader());
            list.add((EAN13Reader)new EAN8Reader());
            list.add((EAN13Reader)new UPCEReader());
        }
        this.readers = list.toArray(new UPCEANReader[list.size()]);
    }
    
    @Override
    public Result decodeRow(int n, BitArray bitArray, final Map<DecodeHintType, ?> map) throws NotFoundException {
        final int n2 = 0;
        final int[] startGuardPattern = UPCEANReader.findStartGuardPattern(bitArray);
        final UPCEANReader[] readers = this.readers;
        final int length = readers.length;
        int i = 0;
        while (i < length) {
            final UPCEANReader upceanReader = readers[i];
            try {
                final Result decodeRow = upceanReader.decodeRow(n, bitArray, startGuardPattern, map);
                if (decodeRow.getBarcodeFormat() == BarcodeFormat.EAN_13 && decodeRow.getText().charAt(0) == '0') {
                    n = 1;
                }
                else {
                    n = 0;
                }
                if (map == null) {
                    bitArray = null;
                }
                else {
                    bitArray = (BitArray)map.get(DecodeHintType.POSSIBLE_FORMATS);
                }
                Label_0105: {
                    if (bitArray != null) {
                        i = n2;
                        if (!((Collection)bitArray).contains(BarcodeFormat.UPC_A)) {
                            break Label_0105;
                        }
                    }
                    i = 1;
                }
                if (n != 0 && i != 0) {
                    bitArray = (BitArray)new Result(decodeRow.getText().substring(1), decodeRow.getRawBytes(), decodeRow.getResultPoints(), BarcodeFormat.UPC_A);
                    ((Result)bitArray).putAllMetadata(decodeRow.getResultMetadata());
                }
                else {
                    bitArray = (BitArray)decodeRow;
                }
                return (Result)bitArray;
            }
            catch (ReaderException ex) {
                ++i;
                continue;
            }
            break;
        }
        throw NotFoundException.getNotFoundInstance();
    }
    
    @Override
    public void reset() {
        final UPCEANReader[] readers = this.readers;
        for (int length = readers.length, i = 0; i < length; ++i) {
            readers[i].reset();
        }
    }
}
