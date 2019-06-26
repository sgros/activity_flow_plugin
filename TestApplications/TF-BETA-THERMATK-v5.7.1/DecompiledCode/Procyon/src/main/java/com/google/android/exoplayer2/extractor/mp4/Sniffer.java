// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Util;

final class Sniffer
{
    private static final int[] COMPATIBLE_BRANDS;
    
    static {
        COMPATIBLE_BRANDS = new int[] { Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("iso3"), Util.getIntegerCodeForString("iso4"), Util.getIntegerCodeForString("iso5"), Util.getIntegerCodeForString("iso6"), Util.getIntegerCodeForString("avc1"), Util.getIntegerCodeForString("hvc1"), Util.getIntegerCodeForString("hev1"), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV") };
    }
    
    private static boolean isCompatibleBrand(final int n) {
        if (n >>> 8 == Util.getIntegerCodeForString("3gp")) {
            return true;
        }
        final int[] compatible_BRANDS = Sniffer.COMPATIBLE_BRANDS;
        for (int length = compatible_BRANDS.length, i = 0; i < length; ++i) {
            if (compatible_BRANDS[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean sniffFragmented(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return sniffInternal(extractorInput, true);
    }
    
    private static boolean sniffInternal(final ExtractorInput extractorInput, final boolean b) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        long n = 4096L;
        if (length != -1L) {
            if (length > 4096L) {
                n = n;
            }
            else {
                n = length;
            }
        }
        int n2 = (int)n;
        final ParsableByteArray parsableByteArray = new ParsableByteArray(64);
        int i = 0;
        int n3 = 0;
        while (true) {
            while (i < n2) {
                parsableByteArray.reset(8);
                extractorInput.peekFully(parsableByteArray.data, 0, 8);
                final long unsignedInt = parsableByteArray.readUnsignedInt();
                final int int1 = parsableByteArray.readInt();
                int n4 = 16;
                long long1;
                if (unsignedInt == 1L) {
                    extractorInput.peekFully(parsableByteArray.data, 8, 8);
                    parsableByteArray.setLimit(16);
                    long1 = parsableByteArray.readLong();
                }
                else {
                    long1 = unsignedInt;
                    if (unsignedInt == 0L) {
                        final long length2 = extractorInput.getLength();
                        long1 = unsignedInt;
                        if (length2 != -1L) {
                            long1 = 8 + (length2 - extractorInput.getPeekPosition());
                        }
                    }
                    n4 = 8;
                }
                if (length != -1L && i + long1 > length) {
                    return false;
                }
                final long n5 = n4;
                if (long1 < n5) {
                    return false;
                }
                i += n4;
                if (int1 == Atom.TYPE_moov) {
                    final int n6 = n2 += (int)long1;
                    if (length == -1L) {
                        continue;
                    }
                    n2 = n6;
                    if (n6 <= length) {
                        continue;
                    }
                    n2 = (int)length;
                }
                else {
                    if (int1 == Atom.TYPE_moof || int1 == Atom.TYPE_mvex) {
                        final boolean b2 = true;
                        boolean b3 = false;
                        if (n3 != 0) {
                            b3 = b3;
                            if (b == b2) {
                                b3 = true;
                            }
                        }
                        return b3;
                    }
                    if (i + long1 - n5 >= n2) {
                        break;
                    }
                    final int n7 = (int)(long1 - n5);
                    final int n8 = i + n7;
                    int n11;
                    if (int1 == Atom.TYPE_ftyp) {
                        if (n7 < 8) {
                            return false;
                        }
                        parsableByteArray.reset(n7);
                        extractorInput.peekFully(parsableByteArray.data, 0, n7);
                        for (int n9 = n7 / 4, j = 0; j < n9; ++j) {
                            final int n10 = 1;
                            if (j == 1) {
                                parsableByteArray.skipBytes(4);
                            }
                            else if (isCompatibleBrand(parsableByteArray.readInt())) {
                                n3 = n10;
                                break;
                            }
                        }
                        if (n3 == 0) {
                            return false;
                        }
                        n11 = n3;
                    }
                    else {
                        n11 = n3;
                        if (n7 != 0) {
                            extractorInput.advancePeekPosition(n7);
                            n11 = n3;
                        }
                    }
                    i = n8;
                    n3 = n11;
                }
            }
            final boolean b2 = false;
            continue;
        }
    }
    
    public static boolean sniffUnfragmented(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        return sniffInternal(extractorInput, false);
    }
}
