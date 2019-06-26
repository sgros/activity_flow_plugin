package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class Sniffer {
    private static final int[] COMPATIBLE_BRANDS = new int[]{Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("iso3"), Util.getIntegerCodeForString("iso4"), Util.getIntegerCodeForString("iso5"), Util.getIntegerCodeForString("iso6"), Util.getIntegerCodeForString("avc1"), Util.getIntegerCodeForString("hvc1"), Util.getIntegerCodeForString("hev1"), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV")};

    public static boolean sniffFragmented(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return sniffInternal(extractorInput, true);
    }

    public static boolean sniffUnfragmented(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return sniffInternal(extractorInput, false);
    }

    private static boolean sniffInternal(ExtractorInput extractorInput, boolean z) throws IOException, InterruptedException {
        boolean z2;
        boolean z3;
        ExtractorInput extractorInput2 = extractorInput;
        long length = extractorInput.getLength();
        long j = 4096;
        long j2 = -1;
        if (length != -1 && length <= 4096) {
            j = length;
        }
        int i = (int) j;
        ParsableByteArray parsableByteArray = new ParsableByteArray(64);
        int i2 = 0;
        int i3 = i;
        i = 0;
        Object obj = null;
        while (i < i3) {
            parsableByteArray.reset(8);
            extractorInput2.peekFully(parsableByteArray.data, i2, 8);
            long readUnsignedInt = parsableByteArray.readUnsignedInt();
            int readInt = parsableByteArray.readInt();
            int i4 = 16;
            if (readUnsignedInt == 1) {
                extractorInput2.peekFully(parsableByteArray.data, 8, 8);
                parsableByteArray.setLimit(16);
                readUnsignedInt = parsableByteArray.readLong();
            } else {
                if (readUnsignedInt == 0) {
                    long length2 = extractorInput.getLength();
                    if (length2 != j2) {
                        readUnsignedInt = ((long) 8) + (length2 - extractorInput.getPeekPosition());
                    }
                }
                i4 = 8;
            }
            if (length != j2 && ((long) i) + readUnsignedInt > length) {
                return i2;
            }
            j2 = (long) i4;
            if (readUnsignedInt < j2) {
                return i2;
            }
            i += i4;
            if (readInt == Atom.TYPE_moov) {
                i3 += (int) readUnsignedInt;
                if (length != -1 && ((long) i3) > length) {
                    i3 = (int) length;
                }
                j2 = -1;
            } else if (readInt == Atom.TYPE_moof || readInt == Atom.TYPE_mvex) {
                z2 = false;
                z3 = true;
                break;
            } else {
                long j3 = readUnsignedInt;
                i4 = i3;
                if ((((long) i) + readUnsignedInt) - j2 >= ((long) i4)) {
                    break;
                }
                int i5 = (int) (j3 - j2);
                i += i5;
                if (readInt == Atom.TYPE_ftyp) {
                    if (i5 < 8) {
                        return false;
                    }
                    Object obj2;
                    parsableByteArray.reset(i5);
                    extractorInput2.peekFully(parsableByteArray.data, 0, i5);
                    i5 /= 4;
                    for (i2 = 0; i2 < i5; i2++) {
                        obj2 = 1;
                        if (i2 == 1) {
                            parsableByteArray.skipBytes(4);
                        } else if (isCompatibleBrand(parsableByteArray.readInt())) {
                            break;
                        }
                    }
                    obj2 = obj;
                    if (obj2 == null) {
                        return false;
                    }
                    obj = obj2;
                } else if (i5 != 0) {
                    extractorInput2.advancePeekPosition(i5);
                }
                i3 = i4;
                j2 = -1;
                i2 = 0;
            }
        }
        z2 = false;
        z3 = false;
        if (obj != null && z == z3) {
            z2 = true;
        }
        return z2;
    }

    private static boolean isCompatibleBrand(int i) {
        if ((i >>> 8) == Util.getIntegerCodeForString("3gp")) {
            return true;
        }
        for (int i2 : COMPATIBLE_BRANDS) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }
}
