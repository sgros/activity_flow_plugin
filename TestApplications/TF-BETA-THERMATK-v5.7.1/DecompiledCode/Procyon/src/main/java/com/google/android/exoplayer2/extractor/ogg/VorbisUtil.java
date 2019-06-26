// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import java.util.Arrays;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.ParserException;

final class VorbisUtil
{
    public static int iLog(int i) {
        int n = 0;
        while (i > 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    private static long mapType1QuantValues(final long n, final long n2) {
        final double a = (double)n;
        final double v = (double)n2;
        Double.isNaN(v);
        return (long)Math.floor(Math.pow(a, 1.0 / v));
    }
    
    private static CodeBook readBook(final VorbisBitArray vorbisBitArray) throws ParserException {
        if (vorbisBitArray.readBits(24) != 5653314) {
            final StringBuilder sb = new StringBuilder();
            sb.append("expected code book to start with [0x56, 0x43, 0x42] at ");
            sb.append(vorbisBitArray.getPosition());
            throw new ParserException(sb.toString());
        }
        final int bits = vorbisBitArray.readBits(16);
        final int bits2 = vorbisBitArray.readBits(24);
        final long[] array = new long[bits2];
        final boolean bit = vorbisBitArray.readBit();
        long mapType1QuantValues = 0L;
        int i = 0;
        if (!bit) {
            final boolean bit2 = vorbisBitArray.readBit();
            while (i < array.length) {
                if (bit2) {
                    if (vorbisBitArray.readBit()) {
                        array[i] = vorbisBitArray.readBits(5) + 1;
                    }
                    else {
                        array[i] = 0L;
                    }
                }
                else {
                    array[i] = vorbisBitArray.readBits(5) + 1;
                }
                ++i;
            }
        }
        else {
            int n = vorbisBitArray.readBits(5) + 1;
            int j = 0;
            while (j < array.length) {
                for (int bits3 = vorbisBitArray.readBits(iLog(bits2 - j)), n2 = 0; n2 < bits3 && j < array.length; ++j, ++n2) {
                    array[j] = n;
                }
                ++n;
            }
        }
        final int bits4 = vorbisBitArray.readBits(4);
        if (bits4 <= 2) {
            if (bits4 == 1 || bits4 == 2) {
                vorbisBitArray.skipBits(32);
                vorbisBitArray.skipBits(32);
                final int bits5 = vorbisBitArray.readBits(4);
                vorbisBitArray.skipBits(1);
                if (bits4 == 1) {
                    if (bits != 0) {
                        mapType1QuantValues = mapType1QuantValues(bits2, bits);
                    }
                }
                else {
                    mapType1QuantValues = bits2 * (long)bits;
                }
                vorbisBitArray.skipBits((int)(mapType1QuantValues * (bits5 + 1)));
            }
            return new CodeBook(bits, bits2, array, bits4, bit);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("lookup type greater than 2 not decodable: ");
        sb2.append(bits4);
        throw new ParserException(sb2.toString());
    }
    
    private static void readFloors(final VorbisBitArray vorbisBitArray) throws ParserException {
        for (int bits = vorbisBitArray.readBits(6), i = 0; i < bits + 1; ++i) {
            final int bits2 = vorbisBitArray.readBits(16);
            if (bits2 != 0) {
                if (bits2 != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("floor type greater than 1 not decodable: ");
                    sb.append(bits2);
                    throw new ParserException(sb.toString());
                }
                final int bits3 = vorbisBitArray.readBits(5);
                final int[] array = new int[bits3];
                int j = 0;
                int n = -1;
                while (j < bits3) {
                    array[j] = vorbisBitArray.readBits(4);
                    int n2;
                    if (array[j] > (n2 = n)) {
                        n2 = array[j];
                    }
                    ++j;
                    n = n2;
                }
                final int[] array2 = new int[n + 1];
                for (int k = 0; k < array2.length; ++k) {
                    array2[k] = vorbisBitArray.readBits(3) + 1;
                    final int bits4 = vorbisBitArray.readBits(2);
                    if (bits4 > 0) {
                        vorbisBitArray.skipBits(8);
                    }
                    for (int l = 0; l < 1 << bits4; ++l) {
                        vorbisBitArray.skipBits(8);
                    }
                }
                vorbisBitArray.skipBits(2);
                final int bits5 = vorbisBitArray.readBits(4);
                int n3 = 0;
                int n4 = 0;
                int n5 = 0;
                while (n3 < bits3) {
                    for (n4 += array2[array[n3]]; n5 < n4; ++n5) {
                        vorbisBitArray.skipBits(bits5);
                    }
                    ++n3;
                }
            }
            else {
                vorbisBitArray.skipBits(8);
                vorbisBitArray.skipBits(16);
                vorbisBitArray.skipBits(16);
                vorbisBitArray.skipBits(6);
                vorbisBitArray.skipBits(8);
                for (int bits6 = vorbisBitArray.readBits(4), n6 = 0; n6 < bits6 + 1; ++n6) {
                    vorbisBitArray.skipBits(8);
                }
            }
        }
    }
    
    private static void readMappings(final int n, final VorbisBitArray vorbisBitArray) throws ParserException {
        for (int bits = vorbisBitArray.readBits(6), i = 0; i < bits + 1; ++i) {
            final int bits2 = vorbisBitArray.readBits(16);
            if (bits2 != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("mapping type other than 0 not supported: ");
                sb.append(bits2);
                Log.e("VorbisUtil", sb.toString());
            }
            else {
                int n2;
                if (vorbisBitArray.readBit()) {
                    n2 = vorbisBitArray.readBits(4) + 1;
                }
                else {
                    n2 = 1;
                }
                if (vorbisBitArray.readBit()) {
                    for (int bits3 = vorbisBitArray.readBits(8), j = 0; j < bits3 + 1; ++j) {
                        final int n3 = n - 1;
                        vorbisBitArray.skipBits(iLog(n3));
                        vorbisBitArray.skipBits(iLog(n3));
                    }
                }
                if (vorbisBitArray.readBits(2) != 0) {
                    throw new ParserException("to reserved bits must be zero after mapping coupling steps");
                }
                if (n2 > 1) {
                    for (int k = 0; k < n; ++k) {
                        vorbisBitArray.skipBits(4);
                    }
                }
                for (int l = 0; l < n2; ++l) {
                    vorbisBitArray.skipBits(8);
                    vorbisBitArray.skipBits(8);
                    vorbisBitArray.skipBits(8);
                }
            }
        }
    }
    
    private static Mode[] readModes(final VorbisBitArray vorbisBitArray) {
        final int n = vorbisBitArray.readBits(6) + 1;
        final Mode[] array = new Mode[n];
        for (int i = 0; i < n; ++i) {
            array[i] = new Mode(vorbisBitArray.readBit(), vorbisBitArray.readBits(16), vorbisBitArray.readBits(16), vorbisBitArray.readBits(8));
        }
        return array;
    }
    
    private static void readResidues(final VorbisBitArray vorbisBitArray) throws ParserException {
        for (int bits = vorbisBitArray.readBits(6), i = 0; i < bits + 1; ++i) {
            if (vorbisBitArray.readBits(16) > 2) {
                throw new ParserException("residueType greater than 2 is not decodable");
            }
            vorbisBitArray.skipBits(24);
            vorbisBitArray.skipBits(24);
            vorbisBitArray.skipBits(24);
            final int n = vorbisBitArray.readBits(6) + 1;
            vorbisBitArray.skipBits(8);
            final int[] array = new int[n];
            for (int j = 0; j < n; ++j) {
                final int bits2 = vorbisBitArray.readBits(3);
                int bits3;
                if (vorbisBitArray.readBit()) {
                    bits3 = vorbisBitArray.readBits(5);
                }
                else {
                    bits3 = 0;
                }
                array[j] = bits3 * 8 + bits2;
            }
            for (int k = 0; k < n; ++k) {
                for (int l = 0; l < 8; ++l) {
                    if ((array[k] & 1 << l) != 0x0) {
                        vorbisBitArray.skipBits(8);
                    }
                }
            }
        }
    }
    
    public static CommentHeader readVorbisCommentHeader(final ParsableByteArray parsableByteArray) throws ParserException {
        int n = 0;
        verifyVorbisHeaderCapturePattern(3, parsableByteArray, false);
        final String string = parsableByteArray.readString((int)parsableByteArray.readLittleEndianUnsignedInt());
        final int length = string.length();
        final long littleEndianUnsignedInt = parsableByteArray.readLittleEndianUnsignedInt();
        final String[] array = new String[(int)littleEndianUnsignedInt];
        int n2 = 11 + length + 4;
        while (n < littleEndianUnsignedInt) {
            array[n] = parsableByteArray.readString((int)parsableByteArray.readLittleEndianUnsignedInt());
            n2 = n2 + 4 + array[n].length();
            ++n;
        }
        if ((parsableByteArray.readUnsignedByte() & 0x1) != 0x0) {
            return new CommentHeader(string, array, n2 + 1);
        }
        throw new ParserException("framing bit expected to be set");
    }
    
    public static VorbisIdHeader readVorbisIdentificationHeader(final ParsableByteArray parsableByteArray) throws ParserException {
        verifyVorbisHeaderCapturePattern(1, parsableByteArray, false);
        final long littleEndianUnsignedInt = parsableByteArray.readLittleEndianUnsignedInt();
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final long littleEndianUnsignedInt2 = parsableByteArray.readLittleEndianUnsignedInt();
        final int littleEndianInt = parsableByteArray.readLittleEndianInt();
        final int littleEndianInt2 = parsableByteArray.readLittleEndianInt();
        final int littleEndianInt3 = parsableByteArray.readLittleEndianInt();
        final int unsignedByte2 = parsableByteArray.readUnsignedByte();
        return new VorbisIdHeader(littleEndianUnsignedInt, unsignedByte, littleEndianUnsignedInt2, littleEndianInt, littleEndianInt2, littleEndianInt3, (int)Math.pow(2.0, unsignedByte2 & 0xF), (int)Math.pow(2.0, (unsignedByte2 & 0xF0) >> 4), (parsableByteArray.readUnsignedByte() & 0x1) > 0, Arrays.copyOf(parsableByteArray.data, parsableByteArray.limit()));
    }
    
    public static Mode[] readVorbisModes(final ParsableByteArray parsableByteArray, final int n) throws ParserException {
        final int n2 = 0;
        verifyVorbisHeaderCapturePattern(5, parsableByteArray, false);
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final VorbisBitArray vorbisBitArray = new VorbisBitArray(parsableByteArray.data);
        vorbisBitArray.skipBits(parsableByteArray.getPosition() * 8);
        for (int i = 0; i < unsignedByte + 1; ++i) {
            readBook(vorbisBitArray);
        }
        for (int bits = vorbisBitArray.readBits(6), j = n2; j < bits + 1; ++j) {
            if (vorbisBitArray.readBits(16) != 0) {
                throw new ParserException("placeholder of time domain transforms not zeroed out");
            }
        }
        readFloors(vorbisBitArray);
        readResidues(vorbisBitArray);
        readMappings(n, vorbisBitArray);
        final Mode[] modes = readModes(vorbisBitArray);
        if (vorbisBitArray.readBit()) {
            return modes;
        }
        throw new ParserException("framing bit after modes not set as expected");
    }
    
    public static boolean verifyVorbisHeaderCapturePattern(final int i, final ParsableByteArray parsableByteArray, final boolean b) throws ParserException {
        if (parsableByteArray.bytesLeft() < 7) {
            if (b) {
                return false;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("too short header: ");
            sb.append(parsableByteArray.bytesLeft());
            throw new ParserException(sb.toString());
        }
        else if (parsableByteArray.readUnsignedByte() != i) {
            if (b) {
                return false;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("expected header type ");
            sb2.append(Integer.toHexString(i));
            throw new ParserException(sb2.toString());
        }
        else {
            if (parsableByteArray.readUnsignedByte() == 118 && parsableByteArray.readUnsignedByte() == 111 && parsableByteArray.readUnsignedByte() == 114 && parsableByteArray.readUnsignedByte() == 98 && parsableByteArray.readUnsignedByte() == 105 && parsableByteArray.readUnsignedByte() == 115) {
                return true;
            }
            if (b) {
                return false;
            }
            throw new ParserException("expected characters 'vorbis'");
        }
    }
    
    public static final class CodeBook
    {
        public final int dimensions;
        public final int entries;
        public final boolean isOrdered;
        public final long[] lengthMap;
        public final int lookupType;
        
        public CodeBook(final int dimensions, final int entries, final long[] lengthMap, final int lookupType, final boolean isOrdered) {
            this.dimensions = dimensions;
            this.entries = entries;
            this.lengthMap = lengthMap;
            this.lookupType = lookupType;
            this.isOrdered = isOrdered;
        }
    }
    
    public static final class CommentHeader
    {
        public final String[] comments;
        public final int length;
        public final String vendor;
        
        public CommentHeader(final String vendor, final String[] comments, final int length) {
            this.vendor = vendor;
            this.comments = comments;
            this.length = length;
        }
    }
    
    public static final class Mode
    {
        public final boolean blockFlag;
        public final int mapping;
        public final int transformType;
        public final int windowType;
        
        public Mode(final boolean blockFlag, final int windowType, final int transformType, final int mapping) {
            this.blockFlag = blockFlag;
            this.windowType = windowType;
            this.transformType = transformType;
            this.mapping = mapping;
        }
    }
    
    public static final class VorbisIdHeader
    {
        public final int bitrateMax;
        public final int bitrateMin;
        public final int bitrateNominal;
        public final int blockSize0;
        public final int blockSize1;
        public final int channels;
        public final byte[] data;
        public final boolean framingFlag;
        public final long sampleRate;
        public final long version;
        
        public VorbisIdHeader(final long version, final int channels, final long sampleRate, final int bitrateMax, final int bitrateNominal, final int bitrateMin, final int blockSize0, final int blockSize2, final boolean framingFlag, final byte[] data) {
            this.version = version;
            this.channels = channels;
            this.sampleRate = sampleRate;
            this.bitrateMax = bitrateMax;
            this.bitrateNominal = bitrateNominal;
            this.bitrateMin = bitrateMin;
            this.blockSize0 = blockSize0;
            this.blockSize1 = blockSize2;
            this.framingFlag = framingFlag;
            this.data = data;
        }
    }
}
