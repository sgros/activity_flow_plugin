// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.video.HevcConfig;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Log;
import java.util.ArrayList;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.Serializable;
import com.google.android.exoplayer2.ParserException;
import android.util.Pair;
import java.util.Collections;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import java.util.List;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

final class AtomParsers
{
    private static final int TYPE_clcp;
    private static final int TYPE_mdta;
    private static final int TYPE_meta;
    private static final int TYPE_sbtl;
    private static final int TYPE_soun;
    private static final int TYPE_subt;
    private static final int TYPE_text;
    private static final int TYPE_vide;
    private static final byte[] opusMagic;
    
    static {
        TYPE_vide = Util.getIntegerCodeForString("vide");
        TYPE_soun = Util.getIntegerCodeForString("soun");
        TYPE_text = Util.getIntegerCodeForString("text");
        TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
        TYPE_subt = Util.getIntegerCodeForString("subt");
        TYPE_clcp = Util.getIntegerCodeForString("clcp");
        TYPE_meta = Util.getIntegerCodeForString("meta");
        TYPE_mdta = Util.getIntegerCodeForString("mdta");
        opusMagic = Util.getUtf8Bytes("OpusHead");
    }
    
    private static boolean canApplyEditWithGaplessInfo(final long[] array, final long n, final long n2, final long n3) {
        final int length = array.length;
        boolean b = true;
        final int n4 = length - 1;
        final int constrainValue = Util.constrainValue(3, 0, n4);
        final int constrainValue2 = Util.constrainValue(array.length - 3, 0, n4);
        if (array[0] > n2 || n2 >= array[constrainValue] || array[constrainValue2] >= n3 || n3 > n) {
            b = false;
        }
        return b;
    }
    
    private static int findEsdsPosition(final ParsableByteArray parsableByteArray, final int n, final int n2) {
        int int1;
        for (int position = parsableByteArray.getPosition(); position - n < n2; position += int1) {
            parsableByteArray.setPosition(position);
            int1 = parsableByteArray.readInt();
            Assertions.checkArgument(int1 > 0, "childAtomSize should be positive");
            if (parsableByteArray.readInt() == Atom.TYPE_esds) {
                return position;
            }
        }
        return -1;
    }
    
    private static int getTrackTypeForHdlr(final int n) {
        if (n == AtomParsers.TYPE_soun) {
            return 1;
        }
        if (n == AtomParsers.TYPE_vide) {
            return 2;
        }
        if (n == AtomParsers.TYPE_text || n == AtomParsers.TYPE_sbtl || n == AtomParsers.TYPE_subt || n == AtomParsers.TYPE_clcp) {
            return 3;
        }
        if (n == AtomParsers.TYPE_meta) {
            return 4;
        }
        return -1;
    }
    
    private static void parseAudioSampleEntry(final ParsableByteArray parsableByteArray, int n, final int n2, final int n3, final int n4, final String s, final boolean b, final DrmInitData drmInitData, final StsdData stsdData, int intValue) throws ParserException {
        parsableByteArray.setPosition(n2 + 8 + 8);
        int unsignedShort;
        if (b) {
            unsignedShort = parsableByteArray.readUnsignedShort();
            parsableByteArray.skipBytes(6);
        }
        else {
            parsableByteArray.skipBytes(8);
            unsignedShort = 0;
        }
        int unsignedFixedPoint1616;
        int n5;
        if (unsignedShort != 0 && unsignedShort != 1) {
            if (unsignedShort != 2) {
                return;
            }
            parsableByteArray.skipBytes(16);
            unsignedFixedPoint1616 = (int)Math.round(parsableByteArray.readDouble());
            n5 = parsableByteArray.readUnsignedIntToInt();
            parsableByteArray.skipBytes(20);
        }
        else {
            final int unsignedShort2 = parsableByteArray.readUnsignedShort();
            parsableByteArray.skipBytes(6);
            unsignedFixedPoint1616 = parsableByteArray.readUnsignedFixedPoint1616();
            n5 = unsignedShort2;
            if (unsignedShort == 1) {
                parsableByteArray.skipBytes(16);
                n5 = unsignedShort2;
                unsignedFixedPoint1616 = unsignedFixedPoint1616;
            }
        }
        final int position = parsableByteArray.getPosition();
        final int type_enca = Atom.TYPE_enca;
        DrmInitData copyWithSchemeType = drmInitData;
        int n6;
        if ((n6 = n) == type_enca) {
            final Pair<Integer, TrackEncryptionBox> sampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, n2, n3);
            copyWithSchemeType = drmInitData;
            if (sampleEntryEncryptionData != null) {
                n = (int)sampleEntryEncryptionData.first;
                if (drmInitData == null) {
                    copyWithSchemeType = null;
                }
                else {
                    copyWithSchemeType = drmInitData.copyWithSchemeType(((TrackEncryptionBox)sampleEntryEncryptionData.second).schemeType);
                }
                stsdData.trackEncryptionBoxes[intValue] = (TrackEncryptionBox)sampleEntryEncryptionData.second;
            }
            parsableByteArray.setPosition(position);
            n6 = n;
        }
        n = Atom.TYPE_ac_3;
        final String s2 = "audio/raw";
        String s3;
        if (n6 == n) {
            s3 = "audio/ac3";
        }
        else if (n6 == Atom.TYPE_ec_3) {
            s3 = "audio/eac3";
        }
        else if (n6 == Atom.TYPE_dtsc) {
            s3 = "audio/vnd.dts";
        }
        else if (n6 != Atom.TYPE_dtsh && n6 != Atom.TYPE_dtsl) {
            if (n6 == Atom.TYPE_dtse) {
                s3 = "audio/vnd.dts.hd;profile=lbr";
            }
            else if (n6 == Atom.TYPE_samr) {
                s3 = "audio/3gpp";
            }
            else if (n6 == Atom.TYPE_sawb) {
                s3 = "audio/amr-wb";
            }
            else if (n6 != Atom.TYPE_lpcm && n6 != Atom.TYPE_sowt) {
                if (n6 == Atom.TYPE__mp3) {
                    s3 = "audio/mpeg";
                }
                else if (n6 == Atom.TYPE_alac) {
                    s3 = "audio/alac";
                }
                else if (n6 == Atom.TYPE_alaw) {
                    s3 = "audio/g711-alaw";
                }
                else if (n6 == Atom.TYPE_ulaw) {
                    s3 = "audio/g711-mlaw";
                }
                else if (n6 == Atom.TYPE_Opus) {
                    s3 = "audio/opus";
                }
                else if (n6 == Atom.TYPE_fLaC) {
                    s3 = "audio/flac";
                }
                else {
                    s3 = null;
                }
            }
            else {
                s3 = "audio/raw";
            }
        }
        else {
            s3 = "audio/vnd.dts.hd";
        }
        intValue = unsignedFixedPoint1616;
        n = position;
        final byte[] array = null;
        final String s4 = s3;
        byte[] o = array;
        final DrmInitData drmInitData2 = copyWithSchemeType;
        String anObject = s4;
        while (n - n2 < n3) {
            parsableByteArray.setPosition(n);
            final int int1 = parsableByteArray.readInt();
            Assertions.checkArgument(int1 > 0, "childAtomSize should be positive");
            final int int2 = parsableByteArray.readInt();
            if (int2 != Atom.TYPE_esds && (!b || int2 != Atom.TYPE_wave)) {
                if (int2 == Atom.TYPE_dac3) {
                    parsableByteArray.setPosition(n + 8);
                    stsdData.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray, Integer.toString(n4), s, drmInitData2);
                }
                else if (int2 == Atom.TYPE_dec3) {
                    parsableByteArray.setPosition(n + 8);
                    stsdData.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray, Integer.toString(n4), s, drmInitData2);
                }
                else if (int2 == Atom.TYPE_ddts) {
                    stsdData.format = Format.createAudioSampleFormat(Integer.toString(n4), anObject, null, -1, -1, n5, intValue, null, drmInitData2, 0, s);
                }
                else if (int2 == Atom.TYPE_alac) {
                    final int n7 = int1;
                    o = new byte[n7];
                    parsableByteArray.setPosition(n);
                    parsableByteArray.readBytes(o, 0, n7);
                }
                else {
                    int n8 = int1;
                    final int n9 = n;
                    if (int2 == Atom.TYPE_dOps) {
                        n8 -= 8;
                        final byte[] opusMagic = AtomParsers.opusMagic;
                        o = new byte[opusMagic.length + n8];
                        System.arraycopy(opusMagic, 0, o, 0, opusMagic.length);
                        parsableByteArray.setPosition(n9 + 8);
                        parsableByteArray.readBytes(o, AtomParsers.opusMagic.length, n8);
                    }
                    else if (n8 == Atom.TYPE_dfLa) {
                        n8 -= 12;
                        o = new byte[n8];
                        parsableByteArray.setPosition(n9 + 12);
                        parsableByteArray.readBytes(o, 0, n8);
                    }
                }
            }
            else {
                int esdsPosition = n;
                if (int2 != Atom.TYPE_esds) {
                    esdsPosition = findEsdsPosition(parsableByteArray, esdsPosition, int1);
                }
                if (esdsPosition != -1) {
                    final Pair<String, byte[]> esdsFromParent = parseEsdsFromParent(parsableByteArray, esdsPosition);
                    final String anObject2 = (String)esdsFromParent.first;
                    final byte[] array2 = (byte[])esdsFromParent.second;
                    anObject = anObject2;
                    o = array2;
                    if ("audio/mp4a-latm".equals(anObject2)) {
                        final Pair<Integer, Integer> aacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(array2);
                        intValue = (int)aacAudioSpecificConfig.first;
                        n5 = (int)aacAudioSpecificConfig.second;
                        o = array2;
                        anObject = anObject2;
                    }
                }
            }
            n += int1;
        }
        n = 2;
        if (stsdData.format == null && anObject != null) {
            if (!s2.equals(anObject)) {
                n = -1;
            }
            final String string = Integer.toString(n4);
            List<byte[]> singletonList;
            if (o == null) {
                singletonList = null;
            }
            else {
                singletonList = Collections.singletonList(o);
            }
            stsdData.format = Format.createAudioSampleFormat(string, anObject, null, -1, -1, n5, intValue, n, singletonList, drmInitData2, 0, s);
        }
    }
    
    static Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent(final ParsableByteArray parsableByteArray, final int n, final int n2) {
        int position = n + 8;
        Integer n3;
        Serializable s = n3 = null;
        int n4 = -1;
        int n5 = 0;
        while (position - n < n2) {
            parsableByteArray.setPosition(position);
            final int int1 = parsableByteArray.readInt();
            final int int2 = parsableByteArray.readInt();
            Integer value;
            String string;
            if (int2 == Atom.TYPE_frma) {
                value = parsableByteArray.readInt();
                string = (String)s;
            }
            else if (int2 == Atom.TYPE_schm) {
                parsableByteArray.skipBytes(4);
                string = parsableByteArray.readString(4);
                value = n3;
            }
            else {
                string = (String)s;
                value = n3;
                if (int2 == Atom.TYPE_schi) {
                    n4 = position;
                    n5 = int1;
                    value = n3;
                    string = (String)s;
                }
            }
            position += int1;
            s = string;
            n3 = value;
        }
        if (!"cenc".equals(s) && !"cbc1".equals(s) && !"cens".equals(s) && !"cbcs".equals(s)) {
            return null;
        }
        final boolean b = true;
        Assertions.checkArgument(n3 != null, "frma atom is mandatory");
        Assertions.checkArgument(n4 != -1, "schi atom is mandatory");
        final TrackEncryptionBox schiFromParent = parseSchiFromParent(parsableByteArray, n4, n5, (String)s);
        Assertions.checkArgument(schiFromParent != null && b, "tenc atom is mandatory");
        return (Pair<Integer, TrackEncryptionBox>)Pair.create((Object)n3, (Object)schiFromParent);
    }
    
    private static Pair<long[], long[]> parseEdts(final Atom.ContainerAtom containerAtom) {
        if (containerAtom != null) {
            final Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_elst);
            if (leafAtomOfType != null) {
                final ParsableByteArray data = leafAtomOfType.data;
                data.setPosition(8);
                final int fullAtomVersion = Atom.parseFullAtomVersion(data.readInt());
                final int unsignedIntToInt = data.readUnsignedIntToInt();
                final long[] array = new long[unsignedIntToInt];
                final long[] array2 = new long[unsignedIntToInt];
                for (int i = 0; i < unsignedIntToInt; ++i) {
                    long n;
                    if (fullAtomVersion == 1) {
                        n = data.readUnsignedLongToLong();
                    }
                    else {
                        n = data.readUnsignedInt();
                    }
                    array[i] = n;
                    long long1;
                    if (fullAtomVersion == 1) {
                        long1 = data.readLong();
                    }
                    else {
                        long1 = data.readInt();
                    }
                    array2[i] = long1;
                    if (data.readShort() != 1) {
                        throw new IllegalArgumentException("Unsupported media rate.");
                    }
                    data.skipBytes(2);
                }
                return (Pair<long[], long[]>)Pair.create((Object)array, (Object)array2);
            }
        }
        return (Pair<long[], long[]>)Pair.create((Object)null, (Object)null);
    }
    
    private static Pair<String, byte[]> parseEsdsFromParent(final ParsableByteArray parsableByteArray, int n) {
        parsableByteArray.setPosition(n + 8 + 4);
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        parsableByteArray.skipBytes(2);
        n = parsableByteArray.readUnsignedByte();
        if ((n & 0x80) != 0x0) {
            parsableByteArray.skipBytes(2);
        }
        if ((n & 0x40) != 0x0) {
            parsableByteArray.skipBytes(parsableByteArray.readUnsignedShort());
        }
        if ((n & 0x20) != 0x0) {
            parsableByteArray.skipBytes(2);
        }
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        final String mimeTypeFromMp4ObjectType = MimeTypes.getMimeTypeFromMp4ObjectType(parsableByteArray.readUnsignedByte());
        if (!"audio/mpeg".equals(mimeTypeFromMp4ObjectType) && !"audio/vnd.dts".equals(mimeTypeFromMp4ObjectType) && !"audio/vnd.dts.hd".equals(mimeTypeFromMp4ObjectType)) {
            parsableByteArray.skipBytes(12);
            parsableByteArray.skipBytes(1);
            n = parseExpandableClassSize(parsableByteArray);
            final byte[] array = new byte[n];
            parsableByteArray.readBytes(array, 0, n);
            return (Pair<String, byte[]>)Pair.create((Object)mimeTypeFromMp4ObjectType, (Object)array);
        }
        return (Pair<String, byte[]>)Pair.create((Object)mimeTypeFromMp4ObjectType, (Object)null);
    }
    
    private static int parseExpandableClassSize(final ParsableByteArray parsableByteArray) {
        int n;
        int n2;
        for (n = parsableByteArray.readUnsignedByte(), n2 = (n & 0x7F); (n & 0x80) == 0x80; n = parsableByteArray.readUnsignedByte(), n2 = (n2 << 7 | (n & 0x7F))) {}
        return n2;
    }
    
    private static int parseHdlr(final ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(16);
        return parsableByteArray.readInt();
    }
    
    private static Metadata parseIlst(final ParsableByteArray parsableByteArray, final int n) {
        parsableByteArray.skipBytes(8);
        final ArrayList<Metadata.Entry> list = new ArrayList<Metadata.Entry>();
        while (parsableByteArray.getPosition() < n) {
            final Metadata.Entry ilstElement = MetadataUtil.parseIlstElement(parsableByteArray);
            if (ilstElement != null) {
                list.add(ilstElement);
            }
        }
        Metadata metadata;
        if (list.isEmpty()) {
            metadata = null;
        }
        else {
            metadata = new Metadata(list);
        }
        return metadata;
    }
    
    private static Pair<Long, String> parseMdhd(final ParsableByteArray parsableByteArray) {
        final int n = 8;
        parsableByteArray.setPosition(8);
        final int fullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        int n2;
        if (fullAtomVersion == 0) {
            n2 = 8;
        }
        else {
            n2 = 16;
        }
        parsableByteArray.skipBytes(n2);
        final long unsignedInt = parsableByteArray.readUnsignedInt();
        int n3 = n;
        if (fullAtomVersion == 0) {
            n3 = 4;
        }
        parsableByteArray.skipBytes(n3);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append((char)((unsignedShort >> 10 & 0x1F) + 96));
        sb.append((char)((unsignedShort >> 5 & 0x1F) + 96));
        sb.append((char)((unsignedShort & 0x1F) + 96));
        return (Pair<Long, String>)Pair.create((Object)unsignedInt, (Object)sb.toString());
    }
    
    public static Metadata parseMdtaFromMeta(final Atom.ContainerAtom containerAtom) {
        final Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_hdlr);
        final Atom.LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(Atom.TYPE_keys);
        final Atom.LeafAtom leafAtomOfType3 = containerAtom.getLeafAtomOfType(Atom.TYPE_ilst);
        Metadata metadata2;
        final Metadata metadata = metadata2 = null;
        if (leafAtomOfType != null) {
            metadata2 = metadata;
            if (leafAtomOfType2 != null) {
                metadata2 = metadata;
                if (leafAtomOfType3 != null) {
                    if (parseHdlr(leafAtomOfType.data) != AtomParsers.TYPE_mdta) {
                        metadata2 = metadata;
                    }
                    else {
                        final ParsableByteArray data = leafAtomOfType2.data;
                        data.setPosition(12);
                        final int int1 = data.readInt();
                        final String[] array = new String[int1];
                        for (int i = 0; i < int1; ++i) {
                            final int int2 = data.readInt();
                            data.skipBytes(4);
                            array[i] = data.readString(int2 - 8);
                        }
                        final ParsableByteArray data2 = leafAtomOfType3.data;
                        data2.setPosition(8);
                        final ArrayList<Metadata.Entry> list = new ArrayList<Metadata.Entry>();
                        while (data2.bytesLeft() > 8) {
                            final int position = data2.getPosition();
                            final int int3 = data2.readInt();
                            final int j = data2.readInt() - 1;
                            if (j >= 0 && j < array.length) {
                                final MdtaMetadataEntry mdtaMetadataEntryFromIlst = MetadataUtil.parseMdtaMetadataEntryFromIlst(data2, position + int3, array[j]);
                                if (mdtaMetadataEntryFromIlst != null) {
                                    list.add((Metadata.Entry)mdtaMetadataEntryFromIlst);
                                }
                            }
                            else {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Skipped metadata with unknown key index: ");
                                sb.append(j);
                                Log.w("AtomParsers", sb.toString());
                            }
                            data2.setPosition(position + int3);
                        }
                        if (list.isEmpty()) {
                            metadata2 = metadata;
                        }
                        else {
                            metadata2 = new Metadata(list);
                        }
                    }
                }
            }
        }
        return metadata2;
    }
    
    private static long parseMvhd(final ParsableByteArray parsableByteArray) {
        int n = 8;
        parsableByteArray.setPosition(8);
        if (Atom.parseFullAtomVersion(parsableByteArray.readInt()) != 0) {
            n = 16;
        }
        parsableByteArray.skipBytes(n);
        return parsableByteArray.readUnsignedInt();
    }
    
    private static float parsePaspFromParent(final ParsableByteArray parsableByteArray, int unsignedIntToInt) {
        parsableByteArray.setPosition(unsignedIntToInt + 8);
        unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        return unsignedIntToInt / (float)parsableByteArray.readUnsignedIntToInt();
    }
    
    private static byte[] parseProjFromParent(final ParsableByteArray parsableByteArray, final int n, final int n2) {
        int int1;
        for (int n3 = n + 8; n3 - n < n2; n3 += int1) {
            parsableByteArray.setPosition(n3);
            int1 = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_proj) {
                return Arrays.copyOfRange(parsableByteArray.data, n3, int1 + n3);
            }
        }
        return null;
    }
    
    private static Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData(final ParsableByteArray parsableByteArray, final int n, final int n2) {
        int int1;
        for (int position = parsableByteArray.getPosition(); position - n < n2; position += int1) {
            parsableByteArray.setPosition(position);
            int1 = parsableByteArray.readInt();
            Assertions.checkArgument(int1 > 0, "childAtomSize should be positive");
            if (parsableByteArray.readInt() == Atom.TYPE_sinf) {
                final Pair<Integer, TrackEncryptionBox> commonEncryptionSinfFromParent = parseCommonEncryptionSinfFromParent(parsableByteArray, position, int1);
                if (commonEncryptionSinfFromParent != null) {
                    return commonEncryptionSinfFromParent;
                }
            }
        }
        return null;
    }
    
    private static TrackEncryptionBox parseSchiFromParent(final ParsableByteArray parsableByteArray, int fullAtomVersion, int unsignedByte, final String s) {
        int position = fullAtomVersion + 8;
        while (true) {
            final byte[] array = null;
            if (position - fullAtomVersion >= unsignedByte) {
                return null;
            }
            parsableByteArray.setPosition(position);
            final int int1 = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_tenc) {
                fullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
                parsableByteArray.skipBytes(1);
                if (fullAtomVersion == 0) {
                    parsableByteArray.skipBytes(1);
                    unsignedByte = 0;
                    fullAtomVersion = 0;
                }
                else {
                    unsignedByte = parsableByteArray.readUnsignedByte();
                    fullAtomVersion = (unsignedByte & 0xF);
                    unsignedByte = (unsignedByte & 0xF0) >> 4;
                }
                final boolean b = parsableByteArray.readUnsignedByte() == 1;
                final int unsignedByte2 = parsableByteArray.readUnsignedByte();
                final byte[] array2 = new byte[16];
                parsableByteArray.readBytes(array2, 0, array2.length);
                byte[] array3 = array;
                if (b) {
                    array3 = array;
                    if (unsignedByte2 == 0) {
                        final int unsignedByte3 = parsableByteArray.readUnsignedByte();
                        array3 = new byte[unsignedByte3];
                        parsableByteArray.readBytes(array3, 0, unsignedByte3);
                    }
                }
                return new TrackEncryptionBox(b, s, unsignedByte2, array2, unsignedByte, fullAtomVersion, array3);
            }
            position += int1;
        }
    }
    
    public static TrackSampleTable parseStbl(final Track track, final Atom.ContainerAtom containerAtom, final GaplessInfoHolder gaplessInfoHolder) throws ParserException {
        final Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(Atom.TYPE_stsz);
        SampleSizeBox sampleSizeBox;
        if (leafAtomOfType != null) {
            sampleSizeBox = new StszSampleSizeBox(leafAtomOfType);
        }
        else {
            final Atom.LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(Atom.TYPE_stz2);
            if (leafAtomOfType2 == null) {
                throw new ParserException("Track has no sample table size information");
            }
            sampleSizeBox = new Stz2SampleSizeBox(leafAtomOfType2);
        }
        final int sampleCount = sampleSizeBox.getSampleCount();
        if (sampleCount == 0) {
            return new TrackSampleTable(track, new long[0], new int[0], 0, new long[0], new int[0], -9223372036854775807L);
        }
        Atom.LeafAtom leafAtom = containerAtom.getLeafAtomOfType(Atom.TYPE_stco);
        boolean b;
        if (leafAtom == null) {
            leafAtom = containerAtom.getLeafAtomOfType(Atom.TYPE_co64);
            b = true;
        }
        else {
            b = false;
        }
        final ParsableByteArray data = leafAtom.data;
        final ParsableByteArray data2 = containerAtom.getLeafAtomOfType(Atom.TYPE_stsc).data;
        final ParsableByteArray data3 = containerAtom.getLeafAtomOfType(Atom.TYPE_stts).data;
        final Atom.LeafAtom leafAtomOfType3 = containerAtom.getLeafAtomOfType(Atom.TYPE_stss);
        ParsableByteArray data4;
        if (leafAtomOfType3 != null) {
            data4 = leafAtomOfType3.data;
        }
        else {
            data4 = null;
        }
        final Atom.LeafAtom leafAtomOfType4 = containerAtom.getLeafAtomOfType(Atom.TYPE_ctts);
        ParsableByteArray data5;
        if (leafAtomOfType4 != null) {
            data5 = leafAtomOfType4.data;
        }
        else {
            data5 = null;
        }
        final ChunkIterator chunkIterator = new ChunkIterator(data2, data, b);
        data3.setPosition(12);
        int i = data3.readUnsignedIntToInt() - 1;
        int unsignedIntToInt = data3.readUnsignedIntToInt();
        int unsignedIntToInt2 = data3.readUnsignedIntToInt();
        int j;
        if (data5 != null) {
            data5.setPosition(12);
            j = data5.readUnsignedIntToInt();
        }
        else {
            j = 0;
        }
        int n = -1;
        int unsignedIntToInt3;
        if (data4 != null) {
            data4.setPosition(12);
            unsignedIntToInt3 = data4.readUnsignedIntToInt();
            if (unsignedIntToInt3 > 0) {
                n = data4.readUnsignedIntToInt() - 1;
            }
            else {
                data4 = null;
            }
        }
        else {
            unsignedIntToInt3 = 0;
        }
        int n8 = 0;
        int[] flags = null;
        long duration = 0L;
        long[] timestamps = null;
        int maximumSize = 0;
        int[] sizes = null;
        long[] offsets = null;
        Label_1205: {
            if (!sampleSizeBox.isFixedSampleSize() || !"audio/raw".equals(track.format.sampleMimeType) || i != 0 || j != 0 || unsignedIntToInt3 != 0) {
                final long[] original = new long[sampleCount];
                int[] copy = new int[sampleCount];
                final long[] original2 = new long[sampleCount];
                final int[] original3 = new int[sampleCount];
                long offset;
                long n2 = offset = 0L;
                final int n3 = 0;
                int k = 0;
                int unsignedIntToInt4 = 0;
                final int n4 = 0;
                int l = unsignedIntToInt3;
                final int n5 = 0;
                int int1 = n4;
                int numSamples = n5;
                int n6 = n;
                int n7 = n3;
                while (true) {
                    while (k < sampleCount) {
                        boolean moveNext = true;
                        boolean b2;
                        while (true) {
                            b2 = moveNext;
                            if (numSamples != 0) {
                                break;
                            }
                            moveNext = chunkIterator.moveNext();
                            if (!(b2 = moveNext)) {
                                break;
                            }
                            offset = chunkIterator.offset;
                            numSamples = chunkIterator.numSamples;
                        }
                        final int m = unsignedIntToInt;
                        if (!b2) {
                            Log.w("AtomParsers", "Unexpected end of chunk data");
                            final long[] copy2 = Arrays.copyOf(original, k);
                            copy = Arrays.copyOf(copy, k);
                            final long[] copy3 = Arrays.copyOf(original2, k);
                            final int[] copy4 = Arrays.copyOf(original3, k);
                            n8 = k;
                            final long[] array = copy2;
                            flags = copy4;
                            duration = n2 + int1;
                            while (true) {
                                while (j > 0) {
                                    if (data5.readUnsignedIntToInt() != 0) {
                                        final boolean b3 = false;
                                        if (l != 0 || m != 0 || numSamples != 0 || i != 0 || unsignedIntToInt4 != 0 || !b3) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("Inconsistent stbl box for track ");
                                            sb.append(track.id);
                                            sb.append(": remainingSynchronizationSamples ");
                                            sb.append(l);
                                            sb.append(", remainingSamplesAtTimestampDelta ");
                                            sb.append(m);
                                            sb.append(", remainingSamplesInChunk ");
                                            sb.append(numSamples);
                                            sb.append(", remainingTimestampDeltaChanges ");
                                            sb.append(i);
                                            sb.append(", remainingSamplesAtTimestampOffset ");
                                            sb.append(unsignedIntToInt4);
                                            String str;
                                            if (!b3) {
                                                str = ", ctts invalid";
                                            }
                                            else {
                                                str = "";
                                            }
                                            sb.append(str);
                                            Log.w("AtomParsers", sb.toString());
                                        }
                                        timestamps = copy3;
                                        maximumSize = n7;
                                        sizes = copy;
                                        offsets = array;
                                        break Label_1205;
                                    }
                                    data5.readInt();
                                    --j;
                                }
                                final boolean b3 = true;
                                continue;
                            }
                        }
                        if (data5 != null) {
                            while (unsignedIntToInt4 == 0 && j > 0) {
                                unsignedIntToInt4 = data5.readUnsignedIntToInt();
                                int1 = data5.readInt();
                                --j;
                            }
                            --unsignedIntToInt4;
                        }
                        original[k] = offset;
                        copy[k] = sampleSizeBox.readNextSampleSize();
                        int n9;
                        if (copy[k] > (n9 = n7)) {
                            n9 = copy[k];
                        }
                        original2[k] = n2 + int1;
                        int n10;
                        if (data4 == null) {
                            n10 = 1;
                        }
                        else {
                            n10 = 0;
                        }
                        original3[k] = n10;
                        if (k == n6) {
                            original3[k] = 1;
                            if (--l > 0) {
                                n6 = data4.readUnsignedIntToInt() - 1;
                            }
                        }
                        n2 += unsignedIntToInt2;
                        int unsignedIntToInt5;
                        final int n11 = unsignedIntToInt5 = m - 1;
                        int int2 = unsignedIntToInt2;
                        int n12 = i;
                        if (n11 == 0) {
                            unsignedIntToInt5 = n11;
                            int2 = unsignedIntToInt2;
                            if ((n12 = i) > 0) {
                                unsignedIntToInt5 = data3.readUnsignedIntToInt();
                                int2 = data3.readInt();
                                n12 = i - 1;
                            }
                        }
                        final long n13 = copy[k];
                        final int n14 = numSamples - 1;
                        ++k;
                        final int n15 = unsignedIntToInt5;
                        offset += n13;
                        n7 = n9;
                        unsignedIntToInt2 = int2;
                        unsignedIntToInt = n15;
                        numSamples = n14;
                        i = n12;
                    }
                    final int m = unsignedIntToInt;
                    n8 = sampleCount;
                    final long[] copy3 = original2;
                    flags = original3;
                    final long[] array = original;
                    continue;
                }
            }
            n8 = sampleCount;
            final int length = chunkIterator.length;
            final long[] array2 = new long[length];
            final int[] array3 = new int[length];
            while (chunkIterator.moveNext()) {
                final int index = chunkIterator.index;
                array2[index] = chunkIterator.offset;
                array3[index] = chunkIterator.numSamples;
            }
            final Format format = track.format;
            final FixedSampleSizeRechunker.Results rechunk = FixedSampleSizeRechunker.rechunk(Util.getPcmFrameSize(format.pcmEncoding, format.channelCount), array2, array3, unsignedIntToInt2);
            offsets = rechunk.offsets;
            sizes = rechunk.sizes;
            maximumSize = rechunk.maximumSize;
            timestamps = rechunk.timestamps;
            flags = rechunk.flags;
            duration = rechunk.duration;
        }
        final long scaleLargeTimestamp = Util.scaleLargeTimestamp(duration, 1000000L, track.timescale);
        if (track.editListDurations == null || gaplessInfoHolder.hasGaplessInfo()) {
            Util.scaleLargeTimestampsInPlace(timestamps, 1000000L, track.timescale);
            return new TrackSampleTable(track, offsets, sizes, maximumSize, timestamps, flags, scaleLargeTimestamp);
        }
        final long[] editListDurations = track.editListDurations;
        if (editListDurations.length == 1 && track.type == 1 && timestamps.length >= 2) {
            final long n16 = track.editListMediaTimes[0];
            final long n17 = n16 + Util.scaleLargeTimestamp(editListDurations[0], track.timescale, track.movieTimescale);
            if (canApplyEditWithGaplessInfo(timestamps, duration, n16, n17)) {
                final long scaleLargeTimestamp2 = Util.scaleLargeTimestamp(n16 - timestamps[0], track.format.sampleRate, track.timescale);
                final long scaleLargeTimestamp3 = Util.scaleLargeTimestamp(duration - n17, track.format.sampleRate, track.timescale);
                if ((scaleLargeTimestamp2 != 0L || scaleLargeTimestamp3 != 0L) && scaleLargeTimestamp2 <= 2147483647L && scaleLargeTimestamp3 <= 2147483647L) {
                    gaplessInfoHolder.encoderDelay = (int)scaleLargeTimestamp2;
                    gaplessInfoHolder.encoderPadding = (int)scaleLargeTimestamp3;
                    Util.scaleLargeTimestampsInPlace(timestamps, 1000000L, track.timescale);
                    return new TrackSampleTable(track, offsets, sizes, maximumSize, timestamps, flags, Util.scaleLargeTimestamp(track.editListDurations[0], 1000000L, track.movieTimescale));
                }
            }
        }
        final int n18 = maximumSize;
        final int[] array4 = sizes;
        final long[] editListDurations2 = track.editListDurations;
        if (editListDurations2.length == 1 && editListDurations2[0] == 0L) {
            final long n19 = track.editListMediaTimes[0];
            for (int n20 = 0; n20 < timestamps.length; ++n20) {
                timestamps[n20] = Util.scaleLargeTimestamp(timestamps[n20] - n19, 1000000L, track.timescale);
            }
            return new TrackSampleTable(track, offsets, array4, n18, timestamps, flags, Util.scaleLargeTimestamp(duration - n19, 1000000L, track.timescale));
        }
        final boolean b4 = track.type == 1;
        final long[] editListDurations3 = track.editListDurations;
        final int[] array5 = new int[editListDurations3.length];
        final int[] array6 = new int[editListDurations3.length];
        int n21 = 0;
        int n22 = 0;
        int n23 = 0;
        int n24 = 0;
        while (true) {
            final long[] editListDurations4 = track.editListDurations;
            if (n21 >= editListDurations4.length) {
                break;
            }
            final long n25 = track.editListMediaTimes[n21];
            int n26;
            int n27;
            if (n25 != -1L) {
                final long scaleLargeTimestamp4 = Util.scaleLargeTimestamp(editListDurations4[n21], track.timescale, track.movieTimescale);
                array5[n21] = Util.binarySearchCeil(timestamps, n25, true, true);
                array6[n21] = Util.binarySearchCeil(timestamps, n25 + scaleLargeTimestamp4, b4, false);
                while (array5[n21] < array6[n21] && (flags[array5[n21]] & 0x1) == 0x0) {
                    ++array5[n21];
                }
                n23 += array6[n21] - array5[n21];
                n26 = (n22 | ((n24 != array5[n21]) ? 1 : 0));
                n27 = array6[n21];
            }
            else {
                final int n28 = n22;
                n27 = n24;
                n26 = n28;
            }
            ++n21;
            final int n29 = n26;
            n24 = n27;
            n22 = n29;
        }
        final int n30 = 0;
        int n31 = 1;
        if (n23 == n8) {
            n31 = 0;
        }
        final int n32 = n22 | n31;
        long[] array7;
        if (n32 != 0) {
            array7 = new long[n23];
        }
        else {
            array7 = offsets;
        }
        int[] array8;
        if (n32 != 0) {
            array8 = new int[n23];
        }
        else {
            array8 = array4;
        }
        int n33;
        if (n32 != 0) {
            n33 = 0;
        }
        else {
            n33 = n18;
        }
        int[] array9;
        if (n32 != 0) {
            array9 = new int[n23];
        }
        else {
            array9 = flags;
        }
        final long[] array10 = new long[n23];
        long n34 = 0L;
        int n35 = 0;
        final int[] array11 = array4;
        final int[] array12 = array9;
        int n36 = n30;
        final int[] array13 = array5;
        while (n36 < track.editListDurations.length) {
            final long n37 = track.editListMediaTimes[n36];
            int n38 = array13[n36];
            final int n39 = array6[n36];
            if (n32 != 0) {
                final int n40 = n39 - n38;
                System.arraycopy(offsets, n38, array7, n35, n40);
                System.arraycopy(array11, n38, array8, n35, n40);
                System.arraycopy(flags, n38, array12, n35, n40);
            }
            while (n38 < n39) {
                array10[n35] = Util.scaleLargeTimestamp(n34, 1000000L, track.movieTimescale) + Util.scaleLargeTimestamp(timestamps[n38] - n37, 1000000L, track.timescale);
                int n41 = n33;
                if (n32 != 0 && array8[n35] > (n41 = n33)) {
                    n41 = array11[n38];
                }
                ++n35;
                ++n38;
                n33 = n41;
            }
            n34 += track.editListDurations[n36];
            ++n36;
        }
        return new TrackSampleTable(track, array7, array8, n33, array10, array12, Util.scaleLargeTimestamp(n34, 1000000L, track.movieTimescale));
    }
    
    private static StsdData parseStsd(final ParsableByteArray parsableByteArray, final int i, final int n, final String s, final DrmInitData drmInitData, final boolean b) throws ParserException {
        parsableByteArray.setPosition(12);
        final int int1 = parsableByteArray.readInt();
        final StsdData stsdData = new StsdData(int1);
        for (int j = 0; j < int1; ++j) {
            final int position = parsableByteArray.getPosition();
            final int int2 = parsableByteArray.readInt();
            Assertions.checkArgument(int2 > 0, "childAtomSize should be positive");
            final int int3 = parsableByteArray.readInt();
            if (int3 != Atom.TYPE_avc1 && int3 != Atom.TYPE_avc3 && int3 != Atom.TYPE_encv && int3 != Atom.TYPE_mp4v && int3 != Atom.TYPE_hvc1 && int3 != Atom.TYPE_hev1 && int3 != Atom.TYPE_s263 && int3 != Atom.TYPE_vp08 && int3 != Atom.TYPE_vp09) {
                if (int3 != Atom.TYPE_mp4a && int3 != Atom.TYPE_enca && int3 != Atom.TYPE_ac_3 && int3 != Atom.TYPE_ec_3 && int3 != Atom.TYPE_dtsc && int3 != Atom.TYPE_dtse && int3 != Atom.TYPE_dtsh && int3 != Atom.TYPE_dtsl && int3 != Atom.TYPE_samr && int3 != Atom.TYPE_sawb && int3 != Atom.TYPE_lpcm && int3 != Atom.TYPE_sowt && int3 != Atom.TYPE__mp3 && int3 != Atom.TYPE_alac && int3 != Atom.TYPE_alaw && int3 != Atom.TYPE_ulaw && int3 != Atom.TYPE_Opus && int3 != Atom.TYPE_fLaC) {
                    if (int3 != Atom.TYPE_TTML && int3 != Atom.TYPE_tx3g && int3 != Atom.TYPE_wvtt && int3 != Atom.TYPE_stpp && int3 != Atom.TYPE_c608) {
                        if (int3 == Atom.TYPE_camm) {
                            stsdData.format = Format.createSampleFormat(Integer.toString(i), "application/x-camera-motion", null, -1, null);
                        }
                    }
                    else {
                        parseTextSampleEntry(parsableByteArray, int3, position, int2, i, s, stsdData);
                    }
                }
                else {
                    parseAudioSampleEntry(parsableByteArray, int3, position, int2, i, s, b, drmInitData, stsdData, j);
                }
            }
            else {
                parseVideoSampleEntry(parsableByteArray, int3, position, int2, i, n, drmInitData, stsdData, j);
            }
            parsableByteArray.setPosition(position + int2);
        }
        return stsdData;
    }
    
    private static void parseTextSampleEntry(final ParsableByteArray parsableByteArray, int n, int type_TTML, final int n2, final int i, final String s, final StsdData stsdData) throws ParserException {
        parsableByteArray.setPosition(type_TTML + 8 + 8);
        type_TTML = Atom.TYPE_TTML;
        final String s2 = "application/ttml+xml";
        List<byte[]> singletonList = null;
        long n3 = Long.MAX_VALUE;
        String s3;
        if (n == type_TTML) {
            s3 = s2;
        }
        else if (n == Atom.TYPE_tx3g) {
            n = n2 - 8 - 8;
            final byte[] o = new byte[n];
            parsableByteArray.readBytes(o, 0, n);
            singletonList = Collections.singletonList(o);
            s3 = "application/x-quicktime-tx3g";
        }
        else if (n == Atom.TYPE_wvtt) {
            s3 = "application/x-mp4-vtt";
        }
        else if (n == Atom.TYPE_stpp) {
            n3 = 0L;
            s3 = s2;
        }
        else {
            if (n != Atom.TYPE_c608) {
                throw new IllegalStateException();
            }
            stsdData.requiredSampleTransformation = 1;
            s3 = "application/x-mp4-cea-608";
        }
        stsdData.format = Format.createTextSampleFormat(Integer.toString(i), s3, null, -1, 0, s, -1, null, n3, singletonList);
    }
    
    private static TkhdData parseTkhd(final ParsableByteArray parsableByteArray) {
        final int n = 8;
        parsableByteArray.setPosition(8);
        final int fullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        int n2;
        if (fullAtomVersion == 0) {
            n2 = 8;
        }
        else {
            n2 = 16;
        }
        parsableByteArray.skipBytes(n2);
        final int int1 = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        final int position = parsableByteArray.getPosition();
        int n3 = n;
        if (fullAtomVersion == 0) {
            n3 = 4;
        }
        final int n4 = 0;
        while (true) {
            for (int i = 0; i < n3; ++i) {
                if (parsableByteArray.data[position + i] != -1) {
                    final boolean b = false;
                    final long n5 = -9223372036854775807L;
                    long n6;
                    if (b) {
                        parsableByteArray.skipBytes(n3);
                        n6 = n5;
                    }
                    else {
                        if (fullAtomVersion == 0) {
                            n6 = parsableByteArray.readUnsignedInt();
                        }
                        else {
                            n6 = parsableByteArray.readUnsignedLongToLong();
                        }
                        if (n6 == 0L) {
                            n6 = n5;
                        }
                    }
                    parsableByteArray.skipBytes(16);
                    final int int2 = parsableByteArray.readInt();
                    final int int3 = parsableByteArray.readInt();
                    parsableByteArray.skipBytes(4);
                    final int int4 = parsableByteArray.readInt();
                    final int int5 = parsableByteArray.readInt();
                    int n7;
                    if (int2 == 0 && int3 == 65536 && int4 == -65536 && int5 == 0) {
                        n7 = 90;
                    }
                    else if (int2 == 0 && int3 == -65536 && int4 == 65536 && int5 == 0) {
                        n7 = 270;
                    }
                    else {
                        n7 = n4;
                        if (int2 == -65536) {
                            n7 = n4;
                            if (int3 == 0) {
                                n7 = n4;
                                if (int4 == 0) {
                                    n7 = n4;
                                    if (int5 == -65536) {
                                        n7 = 180;
                                    }
                                }
                            }
                        }
                    }
                    return new TkhdData(int1, n6, n7);
                }
            }
            final boolean b = true;
            continue;
        }
    }
    
    public static Track parseTrak(final Atom.ContainerAtom containerAtom, final Atom.LeafAtom leafAtom, long n, final DrmInitData drmInitData, final boolean b, final boolean b2) throws ParserException {
        final Atom.ContainerAtom containerAtomOfType = containerAtom.getContainerAtomOfType(Atom.TYPE_mdia);
        final int trackTypeForHdlr = getTrackTypeForHdlr(parseHdlr(containerAtomOfType.getLeafAtomOfType(Atom.TYPE_hdlr).data));
        if (trackTypeForHdlr == -1) {
            return null;
        }
        final TkhdData tkhd = parseTkhd(containerAtom.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        final long n2 = -9223372036854775807L;
        if (n == -9223372036854775807L) {
            n = tkhd.duration;
        }
        final long mvhd = parseMvhd(leafAtom.data);
        if (n == -9223372036854775807L) {
            n = n2;
        }
        else {
            n = Util.scaleLargeTimestamp(n, 1000000L, mvhd);
        }
        final Atom.ContainerAtom containerAtomOfType2 = containerAtomOfType.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        final Pair<Long, String> mdhd = parseMdhd(containerAtomOfType.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        final StsdData stsd = parseStsd(containerAtomOfType2.getLeafAtomOfType(Atom.TYPE_stsd).data, tkhd.id, tkhd.rotationDegrees, (String)mdhd.second, drmInitData, b2);
        long[] array;
        long[] array2;
        if (!b) {
            final Pair<long[], long[]> edts = parseEdts(containerAtom.getContainerAtomOfType(Atom.TYPE_edts));
            array = (long[])edts.first;
            array2 = (long[])edts.second;
        }
        else {
            array = (array2 = null);
        }
        Track track;
        if (stsd.format == null) {
            track = null;
        }
        else {
            track = new Track(tkhd.id, trackTypeForHdlr, (long)mdhd.first, mvhd, n, stsd.format, stsd.requiredSampleTransformation, stsd.trackEncryptionBoxes, stsd.nalUnitLengthFieldLength, array, array2);
        }
        return track;
    }
    
    public static Metadata parseUdta(final Atom.LeafAtom leafAtom, final boolean b) {
        if (b) {
            return null;
        }
        final ParsableByteArray data = leafAtom.data;
        data.setPosition(8);
        while (data.bytesLeft() >= 8) {
            final int position = data.getPosition();
            final int int1 = data.readInt();
            if (data.readInt() == Atom.TYPE_meta) {
                data.setPosition(position);
                return parseUdtaMeta(data, position + int1);
            }
            data.setPosition(position + int1);
        }
        return null;
    }
    
    private static Metadata parseUdtaMeta(final ParsableByteArray parsableByteArray, final int n) {
        parsableByteArray.skipBytes(12);
        while (parsableByteArray.getPosition() < n) {
            final int position = parsableByteArray.getPosition();
            final int int1 = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_ilst) {
                parsableByteArray.setPosition(position);
                return parseIlst(parsableByteArray, position + int1);
            }
            parsableByteArray.setPosition(position + int1);
        }
        return null;
    }
    
    private static void parseVideoSampleEntry(final ParsableByteArray parsableByteArray, int n, final int n2, final int n3, final int i, final int n4, final DrmInitData drmInitData, final StsdData stsdData, int position) throws ParserException {
        parsableByteArray.setPosition(n2 + 8 + 8);
        parsableByteArray.skipBytes(16);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int unsignedShort2 = parsableByteArray.readUnsignedShort();
        parsableByteArray.skipBytes(50);
        final int position2 = parsableByteArray.getPosition();
        final int type_encv = Atom.TYPE_encv;
        String s = null;
        DrmInitData drmInitData2 = drmInitData;
        int n5 = n;
        if (n == type_encv) {
            final Pair<Integer, TrackEncryptionBox> sampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, n2, n3);
            DrmInitData copyWithSchemeType = drmInitData;
            if (sampleEntryEncryptionData != null) {
                n = (int)sampleEntryEncryptionData.first;
                if (drmInitData == null) {
                    copyWithSchemeType = null;
                }
                else {
                    copyWithSchemeType = drmInitData.copyWithSchemeType(((TrackEncryptionBox)sampleEntryEncryptionData.second).schemeType);
                }
                stsdData.trackEncryptionBoxes[position] = (TrackEncryptionBox)sampleEntryEncryptionData.second;
            }
            parsableByteArray.setPosition(position2);
            n5 = n;
            drmInitData2 = copyWithSchemeType;
        }
        byte[] array;
        Object o = array = null;
        n = 0;
        float n6 = 1.0f;
        int n7 = -1;
        position = position2;
        int n8 = n;
        while (position - n2 < n3) {
            parsableByteArray.setPosition(position);
            n = parsableByteArray.getPosition();
            final int int1 = parsableByteArray.readInt();
            if (int1 == 0 && parsableByteArray.getPosition() - n2 == n3) {
                break;
            }
            Assertions.checkArgument(int1 > 0, "childAtomSize should be positive");
            final int int2 = parsableByteArray.readInt();
            List<byte[]> list;
            float n9;
            String s2;
            int n10;
            byte[] projFromParent;
            if (int2 == Atom.TYPE_avcC) {
                Assertions.checkState(s == null);
                parsableByteArray.setPosition(n + 8);
                final AvcConfig parse = AvcConfig.parse(parsableByteArray);
                list = parse.initializationData;
                stsdData.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
                n9 = n6;
                if (n8 == 0) {
                    n9 = parse.pixelWidthAspectRatio;
                }
                s2 = "video/avc";
                n10 = n8;
                projFromParent = array;
                n = n7;
            }
            else if (int2 == Atom.TYPE_hvcC) {
                Assertions.checkState(s == null);
                parsableByteArray.setPosition(n + 8);
                final HevcConfig parse2 = HevcConfig.parse(parsableByteArray);
                list = parse2.initializationData;
                stsdData.nalUnitLengthFieldLength = parse2.nalUnitLengthFieldLength;
                s2 = "video/hevc";
                n10 = n8;
                n9 = n6;
                projFromParent = array;
                n = n7;
            }
            else if (int2 == Atom.TYPE_vpcC) {
                Assertions.checkState(s == null);
                if (n5 == Atom.TYPE_vp08) {
                    s2 = "video/x-vnd.on2.vp8";
                }
                else {
                    s2 = "video/x-vnd.on2.vp9";
                }
                n10 = n8;
                list = (List<byte[]>)o;
                n9 = n6;
                projFromParent = array;
                n = n7;
            }
            else if (int2 == Atom.TYPE_d263) {
                Assertions.checkState(s == null);
                s2 = "video/3gpp";
                n10 = n8;
                list = (List<byte[]>)o;
                n9 = n6;
                projFromParent = array;
                n = n7;
            }
            else if (int2 == Atom.TYPE_esds) {
                Assertions.checkState(s == null);
                final Pair<String, byte[]> esdsFromParent = parseEsdsFromParent(parsableByteArray, n);
                s2 = (String)esdsFromParent.first;
                list = Collections.singletonList(esdsFromParent.second);
                n10 = n8;
                n9 = n6;
                projFromParent = array;
                n = n7;
            }
            else if (int2 == Atom.TYPE_pasp) {
                n9 = parsePaspFromParent(parsableByteArray, n);
                n10 = 1;
                s2 = s;
                list = (List<byte[]>)o;
                projFromParent = array;
                n = n7;
            }
            else if (int2 == Atom.TYPE_sv3d) {
                projFromParent = parseProjFromParent(parsableByteArray, n, int1);
                n10 = n8;
                s2 = s;
                list = (List<byte[]>)o;
                n9 = n6;
                n = n7;
            }
            else {
                n10 = n8;
                s2 = s;
                list = (List<byte[]>)o;
                n9 = n6;
                projFromParent = array;
                n = n7;
                if (int2 == Atom.TYPE_st3d) {
                    final int unsignedByte = parsableByteArray.readUnsignedByte();
                    parsableByteArray.skipBytes(3);
                    n10 = n8;
                    s2 = s;
                    list = (List<byte[]>)o;
                    n9 = n6;
                    projFromParent = array;
                    n = n7;
                    if (unsignedByte == 0) {
                        n = parsableByteArray.readUnsignedByte();
                        if (n != 0) {
                            if (n != 1) {
                                if (n != 2) {
                                    if (n != 3) {
                                        n10 = n8;
                                        s2 = s;
                                        list = (List<byte[]>)o;
                                        n9 = n6;
                                        projFromParent = array;
                                        n = n7;
                                    }
                                    else {
                                        n = 3;
                                        n10 = n8;
                                        s2 = s;
                                        list = (List<byte[]>)o;
                                        n9 = n6;
                                        projFromParent = array;
                                    }
                                }
                                else {
                                    n = 2;
                                    n10 = n8;
                                    s2 = s;
                                    list = (List<byte[]>)o;
                                    n9 = n6;
                                    projFromParent = array;
                                }
                            }
                            else {
                                n = 1;
                                n10 = n8;
                                s2 = s;
                                list = (List<byte[]>)o;
                                n9 = n6;
                                projFromParent = array;
                            }
                        }
                        else {
                            n = 0;
                            projFromParent = array;
                            n9 = n6;
                            list = (List<byte[]>)o;
                            s2 = s;
                            n10 = n8;
                        }
                    }
                }
            }
            position += int1;
            n8 = n10;
            s = s2;
            o = list;
            n6 = n9;
            array = projFromParent;
            n7 = n;
        }
        if (s == null) {
            return;
        }
        stsdData.format = Format.createVideoSampleFormat(Integer.toString(i), s, null, -1, -1, unsignedShort, unsignedShort2, -1.0f, (List<byte[]>)o, n4, n6, array, n7, null, drmInitData2);
    }
    
    private static final class ChunkIterator
    {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;
        
        public ChunkIterator(final ParsableByteArray stsc, final ParsableByteArray chunkOffsets, final boolean chunkOffsetsAreLongs) {
            this.stsc = stsc;
            this.chunkOffsets = chunkOffsets;
            this.chunkOffsetsAreLongs = chunkOffsetsAreLongs;
            chunkOffsets.setPosition(12);
            this.length = chunkOffsets.readUnsignedIntToInt();
            stsc.setPosition(12);
            this.remainingSamplesPerChunkChanges = stsc.readUnsignedIntToInt();
            final int int1 = stsc.readInt();
            boolean b = true;
            if (int1 != 1) {
                b = false;
            }
            Assertions.checkState(b, "first_chunk must be 1");
            this.index = -1;
        }
        
        public boolean moveNext() {
            final int index = this.index + 1;
            this.index = index;
            if (index == this.length) {
                return false;
            }
            long offset;
            if (this.chunkOffsetsAreLongs) {
                offset = this.chunkOffsets.readUnsignedLongToLong();
            }
            else {
                offset = this.chunkOffsets.readUnsignedInt();
            }
            this.offset = offset;
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
                this.numSamples = this.stsc.readUnsignedIntToInt();
                this.stsc.skipBytes(4);
                int nextSamplesPerChunkChangeIndex;
                if (--this.remainingSamplesPerChunkChanges > 0) {
                    nextSamplesPerChunkChangeIndex = this.stsc.readUnsignedIntToInt() - 1;
                }
                else {
                    nextSamplesPerChunkChangeIndex = -1;
                }
                this.nextSamplesPerChunkChangeIndex = nextSamplesPerChunkChangeIndex;
            }
            return true;
        }
    }
    
    private interface SampleSizeBox
    {
        int getSampleCount();
        
        boolean isFixedSampleSize();
        
        int readNextSampleSize();
    }
    
    private static final class StsdData
    {
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation;
        public final TrackEncryptionBox[] trackEncryptionBoxes;
        
        public StsdData(final int n) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[n];
            this.requiredSampleTransformation = 0;
        }
    }
    
    static final class StszSampleSizeBox implements SampleSizeBox
    {
        private final ParsableByteArray data;
        private final int fixedSampleSize;
        private final int sampleCount;
        
        public StszSampleSizeBox(final Atom.LeafAtom leafAtom) {
            (this.data = leafAtom.data).setPosition(12);
            this.fixedSampleSize = this.data.readUnsignedIntToInt();
            this.sampleCount = this.data.readUnsignedIntToInt();
        }
        
        @Override
        public int getSampleCount() {
            return this.sampleCount;
        }
        
        @Override
        public boolean isFixedSampleSize() {
            return this.fixedSampleSize != 0;
        }
        
        @Override
        public int readNextSampleSize() {
            int n;
            if ((n = this.fixedSampleSize) == 0) {
                n = this.data.readUnsignedIntToInt();
            }
            return n;
        }
    }
    
    static final class Stz2SampleSizeBox implements SampleSizeBox
    {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize;
        private final int sampleCount;
        private int sampleIndex;
        
        public Stz2SampleSizeBox(final Atom.LeafAtom leafAtom) {
            (this.data = leafAtom.data).setPosition(12);
            this.fieldSize = (this.data.readUnsignedIntToInt() & 0xFF);
            this.sampleCount = this.data.readUnsignedIntToInt();
        }
        
        @Override
        public int getSampleCount() {
            return this.sampleCount;
        }
        
        @Override
        public boolean isFixedSampleSize() {
            return false;
        }
        
        @Override
        public int readNextSampleSize() {
            final int fieldSize = this.fieldSize;
            if (fieldSize == 8) {
                return this.data.readUnsignedByte();
            }
            if (fieldSize == 16) {
                return this.data.readUnsignedShort();
            }
            if (this.sampleIndex++ % 2 == 0) {
                this.currentByte = this.data.readUnsignedByte();
                return (this.currentByte & 0xF0) >> 4;
            }
            return this.currentByte & 0xF;
        }
    }
    
    private static final class TkhdData
    {
        private final long duration;
        private final int id;
        private final int rotationDegrees;
        
        public TkhdData(final int id, final long duration, final int rotationDegrees) {
            this.id = id;
            this.duration = duration;
            this.rotationDegrees = rotationDegrees;
        }
    }
}
