// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.decoder;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.FormatException;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import com.google.zxing.ChecksumException;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;

public final class Decoder
{
    private final ReedSolomonDecoder rsDecoder;
    
    public Decoder() {
        this.rsDecoder = new ReedSolomonDecoder(GenericGF.QR_CODE_FIELD_256);
    }
    
    private void correctErrors(final byte[] array, final int n) throws ChecksumException {
        final int length = array.length;
        final int[] array2 = new int[length];
        for (int i = 0; i < length; ++i) {
            array2[i] = (array[i] & 0xFF);
        }
        try {
            this.rsDecoder.decode(array2, array.length - n);
            for (int j = 0; j < n; ++j) {
                array[j] = (byte)array2[j];
            }
        }
        catch (ReedSolomonException ex) {
            throw ChecksumException.getChecksumInstance();
        }
    }
    
    private DecoderResult decode(final BitMatrixParser bitMatrixParser, final Map<DecodeHintType, ?> map) throws FormatException, ChecksumException {
        final Version version = bitMatrixParser.readVersion();
        final ErrorCorrectionLevel errorCorrectionLevel = bitMatrixParser.readFormatInformation().getErrorCorrectionLevel();
        final DataBlock[] dataBlocks = DataBlock.getDataBlocks(bitMatrixParser.readCodewords(), version, errorCorrectionLevel);
        int n = 0;
        for (int length = dataBlocks.length, i = 0; i < length; ++i) {
            n += dataBlocks[i].getNumDataCodewords();
        }
        final byte[] array = new byte[n];
        int n2 = 0;
        for (final DataBlock dataBlock : dataBlocks) {
            final byte[] codewords = dataBlock.getCodewords();
            final int numDataCodewords = dataBlock.getNumDataCodewords();
            this.correctErrors(codewords, numDataCodewords);
            for (int k = 0; k < numDataCodewords; ++k, ++n2) {
                array[n2] = codewords[k];
            }
        }
        return DecodedBitStreamParser.decode(array, version, errorCorrectionLevel, map);
    }
    
    public DecoderResult decode(final BitMatrix bitMatrix) throws ChecksumException, FormatException {
        return this.decode(bitMatrix, null);
    }
    
    public DecoderResult decode(BitMatrix decode, Map<DecodeHintType, ?> decode2) throws FormatException, ChecksumException {
        while (true) {
            final BitMatrixParser bitMatrixParser = new BitMatrixParser((BitMatrix)decode);
            decode = null;
            try {
                decode = (ChecksumException)this.decode(bitMatrixParser, (Map<DecodeHintType, ?>)decode2);
                return (DecoderResult)decode;
            }
            catch (FormatException ex) {}
            catch (ChecksumException decode) {}
            try {
                bitMatrixParser.remask();
                bitMatrixParser.setMirror(true);
                bitMatrixParser.readVersion();
                bitMatrixParser.readFormatInformation();
                bitMatrixParser.mirror();
                decode2 = this.decode(bitMatrixParser, (Map<DecodeHintType, ?>)decode2);
                ((DecoderResult)decode2).setOther(new QRCodeDecoderMetaData(true));
                decode = (ChecksumException)decode2;
                return (DecoderResult)decode;
            }
            catch (FormatException ex2) {}
            catch (ChecksumException decode2) {
                goto Label_0082;
            }
        }
    }
    
    public DecoderResult decode(final boolean[][] array) throws ChecksumException, FormatException {
        return this.decode(array, null);
    }
    
    public DecoderResult decode(final boolean[][] array, final Map<DecodeHintType, ?> map) throws ChecksumException, FormatException {
        final int length = array.length;
        final BitMatrix bitMatrix = new BitMatrix(length);
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (array[i][j]) {
                    bitMatrix.set(j, i);
                }
            }
        }
        return this.decode(bitMatrix, map);
    }
}
