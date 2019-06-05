// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;

final class BitMatrixParser
{
    private final BitMatrix bitMatrix;
    private boolean mirror;
    private FormatInformation parsedFormatInfo;
    private Version parsedVersion;
    
    BitMatrixParser(final BitMatrix bitMatrix) throws FormatException {
        final int height = bitMatrix.getHeight();
        if (height < 21 || (height & 0x3) != 0x1) {
            throw FormatException.getFormatInstance();
        }
        this.bitMatrix = bitMatrix;
    }
    
    private int copyBit(int n, final int n2, final int n3) {
        boolean b;
        if (this.mirror) {
            b = this.bitMatrix.get(n2, n);
        }
        else {
            b = this.bitMatrix.get(n, n2);
        }
        if (b) {
            n = (n3 << 1 | 0x1);
        }
        else {
            n = n3 << 1;
        }
        return n;
    }
    
    void mirror() {
        for (int i = 0; i < this.bitMatrix.getWidth(); ++i) {
            for (int j = i + 1; j < this.bitMatrix.getHeight(); ++j) {
                if (this.bitMatrix.get(i, j) != this.bitMatrix.get(j, i)) {
                    this.bitMatrix.flip(j, i);
                    this.bitMatrix.flip(i, j);
                }
            }
        }
    }
    
    byte[] readCodewords() throws FormatException {
        final FormatInformation formatInformation = this.readFormatInformation();
        final Version version = this.readVersion();
        final DataMask dataMask = DataMask.values()[formatInformation.getDataMask()];
        final int height = this.bitMatrix.getHeight();
        dataMask.unmaskBitMatrix(this.bitMatrix, height);
        final BitMatrix buildFunctionPattern = version.buildFunctionPattern();
        int n = 1;
        final byte[] array = new byte[version.getTotalCodewords()];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5;
        for (int i = height - 1; i > 0; i = n5 - 2) {
            if ((n5 = i) == 6) {
                n5 = i - 1;
            }
            for (int j = 0; j < height; ++j) {
                int n6;
                if (n != 0) {
                    n6 = height - 1 - j;
                }
                else {
                    n6 = j;
                }
                int k = 0;
            Label_0217_Outer:
                while (k < 2) {
                    int n7 = n4;
                    int n8 = n3;
                    while (true) {
                        Label_0266: {
                            if (buildFunctionPattern.get(n5 - k, n6)) {
                                break Label_0266;
                            }
                            ++n4;
                            int n10;
                            final int n9 = n10 = n3 << 1;
                            if (this.bitMatrix.get(n5 - k, n6)) {
                                n10 = (n9 | 0x1);
                            }
                            n7 = n4;
                            n8 = n10;
                            if (n4 != 8) {
                                break Label_0266;
                            }
                            final int n11 = n2 + 1;
                            array[n2] = (byte)n10;
                            n4 = 0;
                            n3 = 0;
                            n2 = n11;
                            ++k;
                            continue Label_0217_Outer;
                        }
                        n4 = n7;
                        n3 = n8;
                        continue;
                    }
                }
            }
            n ^= 0x1;
        }
        if (n2 != version.getTotalCodewords()) {
            throw FormatException.getFormatInstance();
        }
        return array;
    }
    
    FormatInformation readFormatInformation() throws FormatException {
        FormatInformation formatInformation;
        if (this.parsedFormatInfo != null) {
            formatInformation = this.parsedFormatInfo;
        }
        else {
            int copyBit = 0;
            for (int i = 0; i < 6; ++i) {
                copyBit = this.copyBit(i, 8, copyBit);
            }
            int n = this.copyBit(8, 7, this.copyBit(8, 8, this.copyBit(7, 8, copyBit)));
            for (int j = 5; j >= 0; --j) {
                n = this.copyBit(8, j, n);
            }
            final int height = this.bitMatrix.getHeight();
            int n2 = 0;
            for (int k = height - 1; k >= height - 7; --k) {
                n2 = this.copyBit(8, k, n2);
            }
            for (int l = height - 8; l < height; ++l) {
                n2 = this.copyBit(l, 8, n2);
            }
            this.parsedFormatInfo = FormatInformation.decodeFormatInformation(n, n2);
            if (this.parsedFormatInfo == null) {
                throw FormatException.getFormatInstance();
            }
            formatInformation = this.parsedFormatInfo;
        }
        return formatInformation;
    }
    
    Version readVersion() throws FormatException {
        Version version;
        if (this.parsedVersion != null) {
            version = this.parsedVersion;
        }
        else {
            final int height = this.bitMatrix.getHeight();
            final int n = (height - 17) / 4;
            if (n <= 6) {
                version = Version.getVersionForNumber(n);
            }
            else {
                int copyBit = 0;
                final int n2 = height - 11;
                for (int i = 5; i >= 0; --i) {
                    for (int j = height - 9; j >= n2; --j) {
                        copyBit = this.copyBit(j, i, copyBit);
                    }
                }
                version = Version.decodeVersionInformation(copyBit);
                if (version != null && version.getDimensionForVersion() == height) {
                    this.parsedVersion = version;
                }
                else {
                    int copyBit2 = 0;
                    for (int k = 5; k >= 0; --k) {
                        for (int l = height - 9; l >= n2; --l) {
                            copyBit2 = this.copyBit(k, l, copyBit2);
                        }
                    }
                    version = Version.decodeVersionInformation(copyBit2);
                    if (version == null || version.getDimensionForVersion() != height) {
                        throw FormatException.getFormatInstance();
                    }
                    this.parsedVersion = version;
                }
            }
        }
        return version;
    }
    
    void remask() {
        if (this.parsedFormatInfo != null) {
            DataMask.values()[this.parsedFormatInfo.getDataMask()].unmaskBitMatrix(this.bitMatrix, this.bitMatrix.getHeight());
        }
    }
    
    void setMirror(final boolean mirror) {
        this.parsedVersion = null;
        this.parsedFormatInfo = null;
        this.mirror = mirror;
    }
}
