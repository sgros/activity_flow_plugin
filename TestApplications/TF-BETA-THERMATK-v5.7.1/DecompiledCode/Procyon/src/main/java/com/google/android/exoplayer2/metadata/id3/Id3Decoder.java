// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.id3;

import java.util.List;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import java.util.Locale;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.Log;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.metadata.MetadataDecoder;

public final class Id3Decoder implements MetadataDecoder
{
    public static final int ID3_TAG;
    public static final FramePredicate NO_FRAMES_PREDICATE;
    private final FramePredicate framePredicate;
    
    static {
        NO_FRAMES_PREDICATE = (FramePredicate)_$$Lambda$Id3Decoder$7M0gB_IGKaTbyTVX_WCb62bIHyc.INSTANCE;
        ID3_TAG = Util.getIntegerCodeForString("ID3");
    }
    
    public Id3Decoder() {
        this(null);
    }
    
    public Id3Decoder(final FramePredicate framePredicate) {
        this.framePredicate = framePredicate;
    }
    
    private static byte[] copyOfRangeIfValid(final byte[] original, final int from, final int to) {
        if (to <= from) {
            return Util.EMPTY_BYTE_ARRAY;
        }
        return Arrays.copyOfRange(original, from, to);
    }
    
    private static ApicFrame decodeApicFrame(final ParsableByteArray parsableByteArray, int indexOfZeroByte, int n) throws UnsupportedEncodingException {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final String charsetName = getCharsetName(unsignedByte);
        final byte[] bytes = new byte[--indexOfZeroByte];
        parsableByteArray.readBytes(bytes, 0, indexOfZeroByte);
        String str;
        if (n == 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("image/");
            sb.append(Util.toLowerInvariant(new String(bytes, 0, 3, "ISO-8859-1")));
            if ("image/jpg".equals(str = sb.toString())) {
                str = "image/jpeg";
            }
            indexOfZeroByte = 2;
        }
        else {
            indexOfZeroByte = indexOfZeroByte(bytes, 0);
            str = Util.toLowerInvariant(new String(bytes, 0, indexOfZeroByte, "ISO-8859-1"));
            if (str.indexOf(47) == -1) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("image/");
                sb2.append(str);
                str = sb2.toString();
            }
        }
        n = bytes[indexOfZeroByte + 1];
        indexOfZeroByte += 2;
        final int indexOfEos = indexOfEos(bytes, indexOfZeroByte, unsignedByte);
        return new ApicFrame(str, new String(bytes, indexOfZeroByte, indexOfEos - indexOfZeroByte, charsetName), n & 0xFF, copyOfRangeIfValid(bytes, indexOfEos + delimiterLength(unsignedByte), bytes.length));
    }
    
    private static BinaryFrame decodeBinaryFrame(final ParsableByteArray parsableByteArray, final int n, final String s) {
        final byte[] array = new byte[n];
        parsableByteArray.readBytes(array, 0, n);
        return new BinaryFrame(s, array);
    }
    
    private static ChapterFrame decodeChapterFrame(final ParsableByteArray parsableByteArray, final int n, final int n2, final boolean b, final int n3, final FramePredicate framePredicate) throws UnsupportedEncodingException {
        final int position = parsableByteArray.getPosition();
        final int indexOfZeroByte = indexOfZeroByte(parsableByteArray.data, position);
        final String s = new String(parsableByteArray.data, position, indexOfZeroByte - position, "ISO-8859-1");
        parsableByteArray.setPosition(indexOfZeroByte + 1);
        final int int1 = parsableByteArray.readInt();
        final int int2 = parsableByteArray.readInt();
        long unsignedInt = parsableByteArray.readUnsignedInt();
        if (unsignedInt == 4294967295L) {
            unsignedInt = -1L;
        }
        long unsignedInt2 = parsableByteArray.readUnsignedInt();
        if (unsignedInt2 == 4294967295L) {
            unsignedInt2 = -1L;
        }
        final ArrayList<Id3Frame> list = new ArrayList<Id3Frame>();
        while (parsableByteArray.getPosition() < position + n) {
            final Id3Frame decodeFrame = decodeFrame(n2, parsableByteArray, b, n3, framePredicate);
            if (decodeFrame != null) {
                list.add(decodeFrame);
            }
        }
        final Id3Frame[] a = new Id3Frame[list.size()];
        list.toArray(a);
        return new ChapterFrame(s, int1, int2, unsignedInt, unsignedInt2, a);
    }
    
    private static ChapterTocFrame decodeChapterTOCFrame(final ParsableByteArray parsableByteArray, final int n, final int n2, final boolean b, final int n3, final FramePredicate framePredicate) throws UnsupportedEncodingException {
        final int position = parsableByteArray.getPosition();
        final int indexOfZeroByte = indexOfZeroByte(parsableByteArray.data, position);
        final String s = new String(parsableByteArray.data, position, indexOfZeroByte - position, "ISO-8859-1");
        parsableByteArray.setPosition(indexOfZeroByte + 1);
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        int i = 0;
        final boolean b2 = (unsignedByte & 0x2) != 0x0;
        final boolean b3 = (unsignedByte & 0x1) != 0x0;
        final int unsignedByte2 = parsableByteArray.readUnsignedByte();
        final String[] array = new String[unsignedByte2];
        while (i < unsignedByte2) {
            final int position2 = parsableByteArray.getPosition();
            final int indexOfZeroByte2 = indexOfZeroByte(parsableByteArray.data, position2);
            array[i] = new String(parsableByteArray.data, position2, indexOfZeroByte2 - position2, "ISO-8859-1");
            parsableByteArray.setPosition(indexOfZeroByte2 + 1);
            ++i;
        }
        final ArrayList<Id3Frame> list = new ArrayList<Id3Frame>();
        while (parsableByteArray.getPosition() < position + n) {
            final Id3Frame decodeFrame = decodeFrame(n2, parsableByteArray, b, n3, framePredicate);
            if (decodeFrame != null) {
                list.add(decodeFrame);
            }
        }
        final Id3Frame[] a = new Id3Frame[list.size()];
        list.toArray(a);
        return new ChapterTocFrame(s, b2, b3, array, a);
    }
    
    private static CommentFrame decodeCommentFrame(final ParsableByteArray parsableByteArray, int indexOfEos) throws UnsupportedEncodingException {
        if (indexOfEos < 4) {
            return null;
        }
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final String charsetName = getCharsetName(unsignedByte);
        final byte[] bytes = new byte[3];
        parsableByteArray.readBytes(bytes, 0, 3);
        final String s = new String(bytes, 0, 3);
        indexOfEos -= 4;
        final byte[] bytes2 = new byte[indexOfEos];
        parsableByteArray.readBytes(bytes2, 0, indexOfEos);
        indexOfEos = indexOfEos(bytes2, 0, unsignedByte);
        final String s2 = new String(bytes2, 0, indexOfEos, charsetName);
        indexOfEos += delimiterLength(unsignedByte);
        return new CommentFrame(s, s2, decodeStringIfValid(bytes2, indexOfEos, indexOfEos(bytes2, indexOfEos, unsignedByte), charsetName));
    }
    
    private static Id3Frame decodeFrame(final int n, final ParsableByteArray parsableByteArray, final boolean b, final int n2, final FramePredicate framePredicate) {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final int unsignedByte2 = parsableByteArray.readUnsignedByte();
        final int unsignedByte3 = parsableByteArray.readUnsignedByte();
        int unsignedByte4;
        if (n >= 3) {
            unsignedByte4 = parsableByteArray.readUnsignedByte();
        }
        else {
            unsignedByte4 = 0;
        }
        int n4;
        if (n == 4) {
            final int n3 = n4 = parsableByteArray.readUnsignedIntToInt();
            if (!b) {
                n4 = ((n3 >> 24 & 0xFF) << 21 | ((n3 & 0xFF) | (n3 >> 8 & 0xFF) << 7 | (n3 >> 16 & 0xFF) << 14));
            }
        }
        else if (n == 3) {
            n4 = parsableByteArray.readUnsignedIntToInt();
        }
        else {
            n4 = parsableByteArray.readUnsignedInt24();
        }
        final int n5 = n4;
        int unsignedShort;
        if (n >= 3) {
            unsignedShort = parsableByteArray.readUnsignedShort();
        }
        else {
            unsignedShort = 0;
        }
        if (unsignedByte == 0 && unsignedByte2 == 0 && unsignedByte3 == 0 && unsignedByte4 == 0 && n5 == 0 && unsignedShort == 0) {
            parsableByteArray.setPosition(parsableByteArray.limit());
            return null;
        }
        final int position = parsableByteArray.getPosition() + n5;
        if (position > parsableByteArray.limit()) {
            Log.w("Id3Decoder", "Frame size exceeds remaining tag data");
            parsableByteArray.setPosition(parsableByteArray.limit());
            return null;
        }
        if (framePredicate != null && !framePredicate.evaluate(n, unsignedByte, unsignedByte2, unsignedByte3, unsignedByte4)) {
            parsableByteArray.setPosition(position);
            return null;
        }
        final int n6 = unsignedShort;
        boolean b3;
        int n7;
        int n8;
        int n10;
        int n11;
        if (n == 3) {
            final boolean b2 = (n6 & 0x80) != 0x0;
            b3 = ((n6 & 0x40) != 0x0);
            if ((n6 & 0x20) != 0x0) {
                n7 = 1;
            }
            else {
                n7 = 0;
            }
            n8 = (b2 ? 1 : 0);
            final int n9 = 0;
            n10 = (b2 ? 1 : 0);
            n11 = n9;
        }
        else if (n == 4) {
            final boolean b4 = (n6 & 0x40) != 0x0;
            final boolean b5 = (n6 & 0x8) != 0x0;
            b3 = ((n6 & 0x4) != 0x0);
            if ((n6 & 0x2) != 0x0) {
                n11 = 1;
            }
            else {
                n11 = 0;
            }
            final boolean b6 = (n6 & 0x1) != 0x0;
            n8 = (b5 ? 1 : 0);
            n7 = (b4 ? 1 : 0);
            n10 = (b6 ? 1 : 0);
        }
        else {
            n7 = 0;
            n10 = 0;
            b3 = false;
            n11 = 0;
            n8 = 0;
        }
        if (n8 != 0 || b3) {
            Log.w("Id3Decoder", "Skipping unsupported compressed or encrypted frame");
            parsableByteArray.setPosition(position);
            return null;
        }
        int n12 = n5;
        if (n7 != 0) {
            n12 = n5 - 1;
            parsableByteArray.skipBytes(1);
        }
        int n13 = n12;
        if (n10 != 0) {
            n13 = n12 - 4;
            parsableByteArray.skipBytes(4);
        }
        int removeUnsynchronization = n13;
        if (n11 != 0) {
            removeUnsynchronization = removeUnsynchronization(parsableByteArray, n13);
        }
        Label_0569: {
            if (unsignedByte != 84 || unsignedByte2 != 88 || unsignedByte3 != 88) {
                break Label_0569;
            }
            if (n != 2) {
                if (unsignedByte4 != 88) {
                    break Label_0569;
                }
            }
            try {
                Id3Frame id3Frame = null;
                Label_1021: {
                    try {
                        decodeTxxxFrame(parsableByteArray, removeUnsynchronization);
                        break Label_1021;
                        while (true) {
                            decodeTextInformationFrame(parsableByteArray, removeUnsynchronization, getFrameId(n, unsignedByte, unsignedByte2, unsignedByte3, unsignedByte4));
                            break Label_1021;
                            continue;
                        }
                    }
                    // iftrue(Label_0604:, unsignedByte != 84)
                    finally {
                        break Label_0569;
                    }
                    Label_0604: {
                        if (unsignedByte == 87 && unsignedByte2 == 88 && unsignedByte3 == 88 && (n == 2 || unsignedByte4 == 88)) {
                            id3Frame = decodeWxxxFrame(parsableByteArray, removeUnsynchronization);
                        }
                        else if (unsignedByte == 87) {
                            id3Frame = decodeUrlLinkFrame(parsableByteArray, removeUnsynchronization, getFrameId(n, unsignedByte, unsignedByte2, unsignedByte3, unsignedByte4));
                        }
                        else if (unsignedByte == 80 && unsignedByte2 == 82 && unsignedByte3 == 73 && unsignedByte4 == 86) {
                            id3Frame = decodePrivFrame(parsableByteArray, removeUnsynchronization);
                        }
                        else if (unsignedByte == 71 && unsignedByte2 == 69 && unsignedByte3 == 79 && (unsignedByte4 == 66 || n == 2)) {
                            id3Frame = decodeGeobFrame(parsableByteArray, removeUnsynchronization);
                        }
                        else {
                            Label_0830: {
                                if (n == 2) {
                                    if (unsignedByte != 80 || unsignedByte2 != 73 || unsignedByte3 != 67) {
                                        break Label_0830;
                                    }
                                }
                                else if (unsignedByte != 65 || unsignedByte2 != 80 || unsignedByte3 != 73 || unsignedByte4 != 67) {
                                    break Label_0830;
                                }
                                id3Frame = decodeApicFrame(parsableByteArray, removeUnsynchronization, n);
                                break Label_1021;
                            }
                            if (unsignedByte == 67 && unsignedByte2 == 79 && unsignedByte3 == 77 && (unsignedByte4 == 77 || n == 2)) {
                                id3Frame = decodeCommentFrame(parsableByteArray, removeUnsynchronization);
                            }
                            else if (unsignedByte == 67 && unsignedByte2 == 72 && unsignedByte3 == 65 && unsignedByte4 == 80) {
                                id3Frame = decodeChapterFrame(parsableByteArray, removeUnsynchronization, n, b, n2, (FramePredicate)id3Frame);
                            }
                            else if (unsignedByte == 67 && unsignedByte2 == 84 && unsignedByte3 == 79 && unsignedByte4 == 67) {
                                id3Frame = decodeChapterTOCFrame(parsableByteArray, removeUnsynchronization, n, b, n2, (FramePredicate)id3Frame);
                            }
                            else if (unsignedByte == 77 && unsignedByte2 == 76 && unsignedByte3 == 76 && unsignedByte4 == 84) {
                                id3Frame = decodeMlltFrame(parsableByteArray, removeUnsynchronization);
                            }
                            else {
                                id3Frame = decodeBinaryFrame(parsableByteArray, removeUnsynchronization, getFrameId(n, unsignedByte, unsignedByte2, unsignedByte3, unsignedByte4));
                            }
                        }
                    }
                }
                if (id3Frame == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to decode frame: id=");
                    sb.append(getFrameId(n, unsignedByte, unsignedByte2, unsignedByte3, unsignedByte4));
                    sb.append(", frameSize=");
                    sb.append(removeUnsynchronization);
                    Log.w("Id3Decoder", sb.toString());
                }
                parsableByteArray.setPosition(position);
                return id3Frame;
            }
            catch (UnsupportedEncodingException ex) {
                Log.w("Id3Decoder", "Unsupported character encoding");
                parsableByteArray.setPosition(position);
                return null;
            }
        }
        parsableByteArray.setPosition(position);
    }
    
    private static GeobFrame decodeGeobFrame(final ParsableByteArray parsableByteArray, int length) throws UnsupportedEncodingException {
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final String charsetName = getCharsetName(unsignedByte);
        final byte[] bytes = new byte[--length];
        parsableByteArray.readBytes(bytes, 0, length);
        length = indexOfZeroByte(bytes, 0);
        final String s = new String(bytes, 0, length, "ISO-8859-1");
        final int n = length + 1;
        length = indexOfEos(bytes, n, unsignedByte);
        final String decodeStringIfValid = decodeStringIfValid(bytes, n, length, charsetName);
        length += delimiterLength(unsignedByte);
        final int indexOfEos = indexOfEos(bytes, length, unsignedByte);
        return new GeobFrame(s, decodeStringIfValid, decodeStringIfValid(bytes, length, indexOfEos, charsetName), copyOfRangeIfValid(bytes, indexOfEos + delimiterLength(unsignedByte), bytes.length));
    }
    
    private static Id3Header decodeHeader(final ParsableByteArray parsableByteArray) {
        if (parsableByteArray.bytesLeft() < 10) {
            Log.w("Id3Decoder", "Data too short to be an ID3 tag");
            return null;
        }
        final int unsignedInt24 = parsableByteArray.readUnsignedInt24();
        if (unsignedInt24 != Id3Decoder.ID3_TAG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected first three bytes of ID3 tag header: ");
            sb.append(unsignedInt24);
            Log.w("Id3Decoder", sb.toString());
            return null;
        }
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        boolean b = true;
        parsableByteArray.skipBytes(1);
        final int unsignedByte2 = parsableByteArray.readUnsignedByte();
        final int synchSafeInt = parsableByteArray.readSynchSafeInt();
        int n;
        if (unsignedByte == 2) {
            final boolean b2 = (unsignedByte2 & 0x40) != 0x0;
            n = synchSafeInt;
            if (b2) {
                Log.w("Id3Decoder", "Skipped ID3 tag with majorVersion=2 and undefined compression scheme");
                return null;
            }
        }
        else if (unsignedByte == 3) {
            final boolean b3 = (unsignedByte2 & 0x40) != 0x0;
            n = synchSafeInt;
            if (b3) {
                final int int1 = parsableByteArray.readInt();
                parsableByteArray.skipBytes(int1);
                n = synchSafeInt - (int1 + 4);
            }
        }
        else {
            if (unsignedByte != 4) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Skipped ID3 tag with unsupported majorVersion=");
                sb2.append(unsignedByte);
                Log.w("Id3Decoder", sb2.toString());
                return null;
            }
            final boolean b4 = (unsignedByte2 & 0x40) != 0x0;
            int n2 = synchSafeInt;
            if (b4) {
                final int synchSafeInt2 = parsableByteArray.readSynchSafeInt();
                parsableByteArray.skipBytes(synchSafeInt2 - 4);
                n2 = synchSafeInt - synchSafeInt2;
            }
            final boolean b5 = (unsignedByte2 & 0x10) != 0x0;
            n = n2;
            if (b5) {
                n = n2 - 10;
            }
        }
        if (unsignedByte >= 4 || (unsignedByte2 & 0x80) == 0x0) {
            b = false;
        }
        return new Id3Header(unsignedByte, b, n);
    }
    
    private static MlltFrame decodeMlltFrame(final ParsableByteArray parsableByteArray, int i) {
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int unsignedInt24 = parsableByteArray.readUnsignedInt24();
        final int unsignedInt25 = parsableByteArray.readUnsignedInt24();
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final int unsignedByte2 = parsableByteArray.readUnsignedByte();
        final ParsableBitArray parsableBitArray = new ParsableBitArray();
        parsableBitArray.reset(parsableByteArray);
        final int n = (i - 10) * 8 / (unsignedByte + unsignedByte2);
        final int[] array = new int[n];
        final int[] array2 = new int[n];
        int bits;
        int bits2;
        for (i = 0; i < n; ++i) {
            bits = parsableBitArray.readBits(unsignedByte);
            bits2 = parsableBitArray.readBits(unsignedByte2);
            array[i] = bits;
            array2[i] = bits2;
        }
        return new MlltFrame(unsignedShort, unsignedInt24, unsignedInt25, array, array2);
    }
    
    private static PrivFrame decodePrivFrame(final ParsableByteArray parsableByteArray, int indexOfZeroByte) throws UnsupportedEncodingException {
        final byte[] bytes = new byte[indexOfZeroByte];
        parsableByteArray.readBytes(bytes, 0, indexOfZeroByte);
        indexOfZeroByte = indexOfZeroByte(bytes, 0);
        return new PrivFrame(new String(bytes, 0, indexOfZeroByte, "ISO-8859-1"), copyOfRangeIfValid(bytes, indexOfZeroByte + 1, bytes.length));
    }
    
    private static String decodeStringIfValid(final byte[] bytes, final int offset, final int n, final String charsetName) throws UnsupportedEncodingException {
        if (n > offset && n <= bytes.length) {
            return new String(bytes, offset, n - offset, charsetName);
        }
        return "";
    }
    
    private static TextInformationFrame decodeTextInformationFrame(final ParsableByteArray parsableByteArray, int n, final String s) throws UnsupportedEncodingException {
        if (n < 1) {
            return null;
        }
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final String charsetName = getCharsetName(unsignedByte);
        final byte[] bytes = new byte[--n];
        parsableByteArray.readBytes(bytes, 0, n);
        return new TextInformationFrame(s, null, new String(bytes, 0, indexOfEos(bytes, 0, unsignedByte), charsetName));
    }
    
    private static TextInformationFrame decodeTxxxFrame(final ParsableByteArray parsableByteArray, int indexOfEos) throws UnsupportedEncodingException {
        if (indexOfEos < 1) {
            return null;
        }
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final String charsetName = getCharsetName(unsignedByte);
        final byte[] bytes = new byte[--indexOfEos];
        parsableByteArray.readBytes(bytes, 0, indexOfEos);
        indexOfEos = indexOfEos(bytes, 0, unsignedByte);
        final String s = new String(bytes, 0, indexOfEos, charsetName);
        indexOfEos += delimiterLength(unsignedByte);
        return new TextInformationFrame("TXXX", s, decodeStringIfValid(bytes, indexOfEos, indexOfEos(bytes, indexOfEos, unsignedByte), charsetName));
    }
    
    private static UrlLinkFrame decodeUrlLinkFrame(final ParsableByteArray parsableByteArray, final int n, final String s) throws UnsupportedEncodingException {
        final byte[] bytes = new byte[n];
        parsableByteArray.readBytes(bytes, 0, n);
        return new UrlLinkFrame(s, null, new String(bytes, 0, indexOfZeroByte(bytes, 0), "ISO-8859-1"));
    }
    
    private static UrlLinkFrame decodeWxxxFrame(final ParsableByteArray parsableByteArray, int indexOfEos) throws UnsupportedEncodingException {
        if (indexOfEos < 1) {
            return null;
        }
        final int unsignedByte = parsableByteArray.readUnsignedByte();
        final String charsetName = getCharsetName(unsignedByte);
        final byte[] bytes = new byte[--indexOfEos];
        parsableByteArray.readBytes(bytes, 0, indexOfEos);
        indexOfEos = indexOfEos(bytes, 0, unsignedByte);
        final String s = new String(bytes, 0, indexOfEos, charsetName);
        indexOfEos += delimiterLength(unsignedByte);
        return new UrlLinkFrame("WXXX", s, decodeStringIfValid(bytes, indexOfEos, indexOfZeroByte(bytes, indexOfEos), "ISO-8859-1"));
    }
    
    private static int delimiterLength(int n) {
        if (n != 0 && n != 3) {
            n = 2;
        }
        else {
            n = 1;
        }
        return n;
    }
    
    private static String getCharsetName(final int n) {
        if (n == 1) {
            return "UTF-16";
        }
        if (n == 2) {
            return "UTF-16BE";
        }
        if (n != 3) {
            return "ISO-8859-1";
        }
        return "UTF-8";
    }
    
    private static String getFrameId(final int n, final int n2, final int n3, final int n4, final int i) {
        String s;
        if (n == 2) {
            s = String.format(Locale.US, "%c%c%c", n2, n3, n4);
        }
        else {
            s = String.format(Locale.US, "%c%c%c%c", n2, n3, n4, i);
        }
        return s;
    }
    
    private static int indexOfEos(final byte[] array, int i, final int n) {
        final int indexOfZeroByte = indexOfZeroByte(array, i);
        if (n != 0) {
            i = indexOfZeroByte;
            if (n != 3) {
                while (i < array.length - 1) {
                    if (i % 2 == 0 && array[i + 1] == 0) {
                        return i;
                    }
                    i = indexOfZeroByte(array, i + 1);
                }
                return array.length;
            }
        }
        return indexOfZeroByte;
    }
    
    private static int indexOfZeroByte(final byte[] array, int i) {
        while (i < array.length) {
            if (array[i] == 0) {
                return i;
            }
            ++i;
        }
        return array.length;
    }
    
    private static int removeUnsynchronization(final ParsableByteArray parsableByteArray, int n) {
        final byte[] data = parsableByteArray.data;
        int position = parsableByteArray.getPosition();
        int n2 = n;
        while (true) {
            final int n3 = position + 1;
            if (n3 >= n2) {
                break;
            }
            n = n2;
            if ((data[position] & 0xFF) == 0xFF) {
                n = n2;
                if (data[n3] == 0) {
                    System.arraycopy(data, position + 2, data, n3, n2 - position - 2);
                    n = n2 - 1;
                }
            }
            position = n3;
            n2 = n;
        }
        return n2;
    }
    
    private static boolean validateFrames(final ParsableByteArray parsableByteArray, final int n, final int n2, final boolean b) {
        final int position = parsableByteArray.getPosition();
        try {
            while (true) {
                final int bytesLeft = parsableByteArray.bytesLeft();
                final int n3 = 1;
                if (bytesLeft < n2) {
                    return true;
                }
                int n4;
                long unsignedInt;
                int unsignedShort;
                if (n >= 3) {
                    n4 = parsableByteArray.readInt();
                    unsignedInt = parsableByteArray.readUnsignedInt();
                    unsignedShort = parsableByteArray.readUnsignedShort();
                }
                else {
                    n4 = parsableByteArray.readUnsignedInt24();
                    unsignedInt = parsableByteArray.readUnsignedInt24();
                    unsignedShort = 0;
                }
                if (n4 == 0 && unsignedInt == 0L && unsignedShort == 0) {
                    return true;
                }
                long n5 = unsignedInt;
                if (n == 4) {
                    n5 = unsignedInt;
                    if (!b) {
                        if ((0x808080L & unsignedInt) != 0x0L) {
                            return false;
                        }
                        n5 = ((unsignedInt >> 24 & 0xFFL) << 21 | ((unsignedInt & 0xFFL) | (unsignedInt >> 8 & 0xFFL) << 7 | (unsignedInt >> 16 & 0xFFL) << 14));
                    }
                }
                int n7 = 0;
                int n9 = 0;
                Label_0266: {
                    Label_0263: {
                        int n6;
                        if (n == 4) {
                            if ((unsignedShort & 0x40) != 0x0) {
                                n6 = 1;
                            }
                            else {
                                n6 = 0;
                            }
                            n7 = n6;
                            if ((unsignedShort & 0x1) == 0x0) {
                                break Label_0263;
                            }
                        }
                        else {
                            if (n != 3) {
                                n7 = 0;
                                break Label_0263;
                            }
                            if ((unsignedShort & 0x20) != 0x0) {
                                n6 = 1;
                            }
                            else {
                                n6 = 0;
                            }
                            n7 = n6;
                            if ((unsignedShort & 0x80) == 0x0) {
                                break Label_0263;
                            }
                        }
                        final int n8 = 1;
                        n7 = n6;
                        n9 = n8;
                        break Label_0266;
                    }
                    n9 = 0;
                }
                int n10;
                if (n7 != 0) {
                    n10 = n3;
                }
                else {
                    n10 = 0;
                }
                int n11 = n10;
                if (n9 != 0) {
                    n11 = n10 + 4;
                }
                if (n5 < n11) {
                    return false;
                }
                if (parsableByteArray.bytesLeft() < n5) {
                    return false;
                }
                parsableByteArray.skipBytes((int)n5);
            }
        }
        finally {
            parsableByteArray.setPosition(position);
        }
    }
    
    @Override
    public Metadata decode(final MetadataInputBuffer metadataInputBuffer) {
        final ByteBuffer data = metadataInputBuffer.data;
        return this.decode(data.array(), data.limit());
    }
    
    public Metadata decode(final byte[] array, int n) {
        final ArrayList<Metadata.Entry> list = new ArrayList<Metadata.Entry>();
        final ParsableByteArray parsableByteArray = new ParsableByteArray(array, n);
        final Id3Header decodeHeader = decodeHeader(parsableByteArray);
        if (decodeHeader == null) {
            return null;
        }
        final int position = parsableByteArray.getPosition();
        if (decodeHeader.majorVersion == 2) {
            n = 6;
        }
        else {
            n = 10;
        }
        int n2 = decodeHeader.framesSize;
        if (decodeHeader.isUnsynchronized) {
            n2 = removeUnsynchronization(parsableByteArray, decodeHeader.framesSize);
        }
        parsableByteArray.setLimit(position + n2);
        final int access$000 = decodeHeader.majorVersion;
        boolean b = false;
        if (!validateFrames(parsableByteArray, access$000, n, false)) {
            if (decodeHeader.majorVersion != 4 || !validateFrames(parsableByteArray, 4, n, true)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to validate ID3 tag with majorVersion=");
                sb.append(decodeHeader.majorVersion);
                Log.w("Id3Decoder", sb.toString());
                return null;
            }
            b = true;
        }
        while (parsableByteArray.bytesLeft() >= n) {
            final Id3Frame decodeFrame = decodeFrame(decodeHeader.majorVersion, parsableByteArray, b, n, this.framePredicate);
            if (decodeFrame != null) {
                list.add(decodeFrame);
            }
        }
        return new Metadata(list);
    }
    
    public interface FramePredicate
    {
        boolean evaluate(final int p0, final int p1, final int p2, final int p3, final int p4);
    }
    
    private static final class Id3Header
    {
        private final int framesSize;
        private final boolean isUnsynchronized;
        private final int majorVersion;
        
        public Id3Header(final int majorVersion, final boolean isUnsynchronized, final int framesSize) {
            this.majorVersion = majorVersion;
            this.isUnsynchronized = isUnsynchronized;
            this.framesSize = framesSize;
        }
    }
}
