package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

final class MetadataUtil {
    private static final int SHORT_TYPE_ALBUM = Util.getIntegerCodeForString("alb");
    private static final int SHORT_TYPE_ARTIST = Util.getIntegerCodeForString("ART");
    private static final int SHORT_TYPE_COMMENT = Util.getIntegerCodeForString("cmt");
    private static final int SHORT_TYPE_COMPOSER_1 = Util.getIntegerCodeForString("com");
    private static final int SHORT_TYPE_COMPOSER_2 = Util.getIntegerCodeForString("wrt");
    private static final int SHORT_TYPE_ENCODER = Util.getIntegerCodeForString("too");
    private static final int SHORT_TYPE_GENRE = Util.getIntegerCodeForString("gen");
    private static final int SHORT_TYPE_LYRICS = Util.getIntegerCodeForString("lyr");
    private static final int SHORT_TYPE_NAME_1 = Util.getIntegerCodeForString("nam");
    private static final int SHORT_TYPE_NAME_2 = Util.getIntegerCodeForString("trk");
    private static final int SHORT_TYPE_YEAR = Util.getIntegerCodeForString("day");
    private static final String[] STANDARD_GENRES = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "Jpop", "Synthpop"};
    private static final int TYPE_ALBUM_ARTIST = Util.getIntegerCodeForString("aART");
    private static final int TYPE_COMPILATION = Util.getIntegerCodeForString("cpil");
    private static final int TYPE_COVER_ART = Util.getIntegerCodeForString("covr");
    private static final int TYPE_DISK_NUMBER = Util.getIntegerCodeForString("disk");
    private static final int TYPE_GAPLESS_ALBUM = Util.getIntegerCodeForString("pgap");
    private static final int TYPE_GENRE = Util.getIntegerCodeForString("gnre");
    private static final int TYPE_GROUPING = Util.getIntegerCodeForString("grp");
    private static final int TYPE_INTERNAL = Util.getIntegerCodeForString("----");
    private static final int TYPE_RATING = Util.getIntegerCodeForString("rtng");
    private static final int TYPE_SORT_ALBUM = Util.getIntegerCodeForString("soal");
    private static final int TYPE_SORT_ALBUM_ARTIST = Util.getIntegerCodeForString("soaa");
    private static final int TYPE_SORT_ARTIST = Util.getIntegerCodeForString("soar");
    private static final int TYPE_SORT_COMPOSER = Util.getIntegerCodeForString("soco");
    private static final int TYPE_SORT_TRACK_NAME = Util.getIntegerCodeForString("sonm");
    private static final int TYPE_TEMPO = Util.getIntegerCodeForString("tmpo");
    private static final int TYPE_TRACK_NUMBER = Util.getIntegerCodeForString("trkn");
    private static final int TYPE_TV_SHOW = Util.getIntegerCodeForString("tvsh");
    private static final int TYPE_TV_SORT_SHOW = Util.getIntegerCodeForString("sosn");

    public static Format getFormatWithMetadata(int i, Format format, Metadata metadata, Metadata metadata2, GaplessInfoHolder gaplessInfoHolder) {
        if (i == 1) {
            if (gaplessInfoHolder.hasGaplessInfo()) {
                format = format.copyWithGaplessInfo(gaplessInfoHolder.encoderDelay, gaplessInfoHolder.encoderPadding);
            }
            if (metadata != null) {
                return format.copyWithMetadata(metadata);
            }
            return format;
        } else if (i != 2 || metadata2 == null) {
            return format;
        } else {
            Format format2 = format;
            for (int i2 = 0; i2 < metadata2.length(); i2++) {
                Entry entry = metadata2.get(i2);
                if (entry instanceof MdtaMetadataEntry) {
                    MdtaMetadataEntry mdtaMetadataEntry = (MdtaMetadataEntry) entry;
                    if ("com.android.capture.fps".equals(mdtaMetadataEntry.key) && mdtaMetadataEntry.typeIndicator == 23) {
                        try {
                            format2 = format2.copyWithFrameRate(ByteBuffer.wrap(mdtaMetadataEntry.value).asFloatBuffer().get()).copyWithMetadata(new Metadata(mdtaMetadataEntry));
                        } catch (NumberFormatException unused) {
                            Log.m18w("MetadataUtil", "Ignoring invalid framerate");
                        }
                    }
                }
            }
            return format2;
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:172:0x01b4=Splitter:B:172:0x01b4, B:168:0x01aa=Splitter:B:168:0x01aa, B:164:0x018b=Splitter:B:164:0x018b} */
    public static com.google.android.exoplayer2.metadata.Metadata.Entry parseIlstElement(com.google.android.exoplayer2.util.ParsableByteArray r5) {
        /*
        r0 = r5.getPosition();
        r1 = r5.readInt();
        r0 = r0 + r1;
        r1 = r5.readInt();
        r2 = r1 >> 24;
        r2 = r2 & 255;
        r3 = 169; // 0xa9 float:2.37E-43 double:8.35E-322;
        if (r2 == r3) goto L_0x0105;
    L_0x0015:
        r3 = 253; // 0xfd float:3.55E-43 double:1.25E-321;
        if (r2 != r3) goto L_0x001b;
    L_0x0019:
        goto L_0x0105;
    L_0x001b:
        r2 = TYPE_GENRE;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x0027;
    L_0x001f:
        r1 = parseStandardGenreAttribute(r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0027:
        r2 = TYPE_DISK_NUMBER;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x0035;
    L_0x002b:
        r2 = "TPOS";
        r1 = parseIndexAndCountAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0035:
        r2 = TYPE_TRACK_NUMBER;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x0043;
    L_0x0039:
        r2 = "TRCK";
        r1 = parseIndexAndCountAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0043:
        r2 = TYPE_TEMPO;	 Catch:{ all -> 0x01be }
        r3 = 0;
        r4 = 1;
        if (r1 != r2) goto L_0x0053;
    L_0x0049:
        r2 = "TBPM";
        r1 = parseUint8Attribute(r1, r2, r5, r4, r3);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0053:
        r2 = TYPE_COMPILATION;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x0061;
    L_0x0057:
        r2 = "TCMP";
        r1 = parseUint8Attribute(r1, r2, r5, r4, r4);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0061:
        r2 = TYPE_COVER_ART;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x006d;
    L_0x0065:
        r1 = parseCoverArt(r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x006d:
        r2 = TYPE_ALBUM_ARTIST;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x007b;
    L_0x0071:
        r2 = "TPE2";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x007b:
        r2 = TYPE_SORT_TRACK_NAME;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x0089;
    L_0x007f:
        r2 = "TSOT";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0089:
        r2 = TYPE_SORT_ALBUM;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x0097;
    L_0x008d:
        r2 = "TSO2";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0097:
        r2 = TYPE_SORT_ARTIST;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00a5;
    L_0x009b:
        r2 = "TSOA";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00a5:
        r2 = TYPE_SORT_ALBUM_ARTIST;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00b3;
    L_0x00a9:
        r2 = "TSOP";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00b3:
        r2 = TYPE_SORT_COMPOSER;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00c1;
    L_0x00b7:
        r2 = "TSOC";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00c1:
        r2 = TYPE_RATING;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00cf;
    L_0x00c5:
        r2 = "ITUNESADVISORY";
        r1 = parseUint8Attribute(r1, r2, r5, r3, r3);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00cf:
        r2 = TYPE_GAPLESS_ALBUM;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00dd;
    L_0x00d3:
        r2 = "ITUNESGAPLESS";
        r1 = parseUint8Attribute(r1, r2, r5, r3, r4);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00dd:
        r2 = TYPE_TV_SORT_SHOW;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00eb;
    L_0x00e1:
        r2 = "TVSHOWSORT";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00eb:
        r2 = TYPE_TV_SHOW;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x00f9;
    L_0x00ef:
        r2 = "TVSHOW";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x00f9:
        r2 = TYPE_INTERNAL;	 Catch:{ all -> 0x01be }
        if (r1 != r2) goto L_0x018b;
    L_0x00fd:
        r1 = parseInternalAttribute(r5, r0);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0105:
        r2 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        r2 = r2 & r1;
        r3 = SHORT_TYPE_COMMENT;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x0115;
    L_0x010d:
        r1 = parseCommentAttribute(r1, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0115:
        r3 = SHORT_TYPE_NAME_1;	 Catch:{ all -> 0x01be }
        if (r2 == r3) goto L_0x01b4;
    L_0x0119:
        r3 = SHORT_TYPE_NAME_2;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x011f;
    L_0x011d:
        goto L_0x01b4;
    L_0x011f:
        r3 = SHORT_TYPE_COMPOSER_1;	 Catch:{ all -> 0x01be }
        if (r2 == r3) goto L_0x01aa;
    L_0x0123:
        r3 = SHORT_TYPE_COMPOSER_2;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x0129;
    L_0x0127:
        goto L_0x01aa;
    L_0x0129:
        r3 = SHORT_TYPE_YEAR;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x0137;
    L_0x012d:
        r2 = "TDRC";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0137:
        r3 = SHORT_TYPE_ARTIST;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x0145;
    L_0x013b:
        r2 = "TPE1";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0145:
        r3 = SHORT_TYPE_ENCODER;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x0153;
    L_0x0149:
        r2 = "TSSE";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0153:
        r3 = SHORT_TYPE_ALBUM;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x0161;
    L_0x0157:
        r2 = "TALB";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x0161:
        r3 = SHORT_TYPE_LYRICS;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x016f;
    L_0x0165:
        r2 = "USLT";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x016f:
        r3 = SHORT_TYPE_GENRE;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x017d;
    L_0x0173:
        r2 = "TCON";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x017d:
        r3 = TYPE_GROUPING;	 Catch:{ all -> 0x01be }
        if (r2 != r3) goto L_0x018b;
    L_0x0181:
        r2 = "TIT1";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x018b:
        r2 = "MetadataUtil";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01be }
        r3.<init>();	 Catch:{ all -> 0x01be }
        r4 = "Skipped unknown metadata entry: ";
        r3.append(r4);	 Catch:{ all -> 0x01be }
        r1 = com.google.android.exoplayer2.extractor.mp4.Atom.getAtomTypeString(r1);	 Catch:{ all -> 0x01be }
        r3.append(r1);	 Catch:{ all -> 0x01be }
        r1 = r3.toString();	 Catch:{ all -> 0x01be }
        com.google.android.exoplayer2.util.Log.m12d(r2, r1);	 Catch:{ all -> 0x01be }
        r1 = 0;
        r5.setPosition(r0);
        return r1;
    L_0x01aa:
        r2 = "TCOM";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x01b4:
        r2 = "TIT2";
        r1 = parseTextAttribute(r1, r2, r5);	 Catch:{ all -> 0x01be }
        r5.setPosition(r0);
        return r1;
    L_0x01be:
        r1 = move-exception;
        r5.setPosition(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.MetadataUtil.parseIlstElement(com.google.android.exoplayer2.util.ParsableByteArray):com.google.android.exoplayer2.metadata.Metadata$Entry");
    }

    public static MdtaMetadataEntry parseMdtaMetadataEntryFromIlst(ParsableByteArray parsableByteArray, int i, String str) {
        while (true) {
            int position = parsableByteArray.getPosition();
            if (position >= i) {
                return null;
            }
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_data) {
                i = parsableByteArray.readInt();
                position = parsableByteArray.readInt();
                readInt -= 16;
                byte[] bArr = new byte[readInt];
                parsableByteArray.readBytes(bArr, 0, readInt);
                return new MdtaMetadataEntry(str, bArr, position, i);
            }
            parsableByteArray.setPosition(position + readInt);
        }
    }

    private static TextInformationFrame parseTextAttribute(int i, String str, ParsableByteArray parsableByteArray) {
        int readInt = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            parsableByteArray.skipBytes(8);
            return new TextInformationFrame(str, null, parsableByteArray.readNullTerminatedString(readInt - 16));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to parse text attribute: ");
        stringBuilder.append(Atom.getAtomTypeString(i));
        Log.m18w("MetadataUtil", stringBuilder.toString());
        return null;
    }

    private static CommentFrame parseCommentAttribute(int i, ParsableByteArray parsableByteArray) {
        int readInt = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            parsableByteArray.skipBytes(8);
            String readNullTerminatedString = parsableByteArray.readNullTerminatedString(readInt - 16);
            return new CommentFrame("und", readNullTerminatedString, readNullTerminatedString);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to parse comment attribute: ");
        stringBuilder.append(Atom.getAtomTypeString(i));
        Log.m18w("MetadataUtil", stringBuilder.toString());
        return null;
    }

    private static Id3Frame parseUint8Attribute(int i, String str, ParsableByteArray parsableByteArray, boolean z, boolean z2) {
        int parseUint8AttributeValue = parseUint8AttributeValue(parsableByteArray);
        if (z2) {
            parseUint8AttributeValue = Math.min(1, parseUint8AttributeValue);
        }
        if (parseUint8AttributeValue >= 0) {
            Id3Frame textInformationFrame;
            if (z) {
                textInformationFrame = new TextInformationFrame(str, null, Integer.toString(parseUint8AttributeValue));
            } else {
                textInformationFrame = new CommentFrame("und", str, Integer.toString(parseUint8AttributeValue));
            }
            return textInformationFrame;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to parse uint8 attribute: ");
        stringBuilder.append(Atom.getAtomTypeString(i));
        Log.m18w("MetadataUtil", stringBuilder.toString());
        return null;
    }

    private static TextInformationFrame parseIndexAndCountAttribute(int i, String str, ParsableByteArray parsableByteArray) {
        int readInt = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == Atom.TYPE_data && readInt >= 22) {
            parsableByteArray.skipBytes(10);
            readInt = parsableByteArray.readUnsignedShort();
            if (readInt > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(readInt);
                String stringBuilder2 = stringBuilder.toString();
                int readUnsignedShort = parsableByteArray.readUnsignedShort();
                if (readUnsignedShort > 0) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(stringBuilder2);
                    stringBuilder3.append("/");
                    stringBuilder3.append(readUnsignedShort);
                    stringBuilder2 = stringBuilder3.toString();
                }
                return new TextInformationFrame(str, null, stringBuilder2);
            }
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Failed to parse index/count attribute: ");
        stringBuilder4.append(Atom.getAtomTypeString(i));
        Log.m18w("MetadataUtil", stringBuilder4.toString());
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001c  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0014  */
    private static com.google.android.exoplayer2.metadata.id3.TextInformationFrame parseStandardGenreAttribute(com.google.android.exoplayer2.util.ParsableByteArray r3) {
        /*
        r3 = parseUint8AttributeValue(r3);
        r0 = 0;
        if (r3 <= 0) goto L_0x0011;
    L_0x0007:
        r1 = STANDARD_GENRES;
        r2 = r1.length;
        if (r3 > r2) goto L_0x0011;
    L_0x000c:
        r3 = r3 + -1;
        r3 = r1[r3];
        goto L_0x0012;
    L_0x0011:
        r3 = r0;
    L_0x0012:
        if (r3 == 0) goto L_0x001c;
    L_0x0014:
        r1 = new com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
        r2 = "TCON";
        r1.<init>(r2, r0, r3);
        return r1;
    L_0x001c:
        r3 = "MetadataUtil";
        r1 = "Failed to parse standard genre code";
        com.google.android.exoplayer2.util.Log.m18w(r3, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.MetadataUtil.parseStandardGenreAttribute(com.google.android.exoplayer2.util.ParsableByteArray):com.google.android.exoplayer2.metadata.id3.TextInformationFrame");
    }

    private static ApicFrame parseCoverArt(ParsableByteArray parsableByteArray) {
        int readInt = parsableByteArray.readInt();
        String str = "MetadataUtil";
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            int parseFullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
            String str2 = parseFullAtomFlags == 13 ? "image/jpeg" : parseFullAtomFlags == 14 ? "image/png" : null;
            if (str2 == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized cover art flags: ");
                stringBuilder.append(parseFullAtomFlags);
                Log.m18w(str, stringBuilder.toString());
                return null;
            }
            parsableByteArray.skipBytes(4);
            byte[] bArr = new byte[(readInt - 16)];
            parsableByteArray.readBytes(bArr, 0, bArr.length);
            return new ApicFrame(str2, null, 3, bArr);
        }
        Log.m18w(str, "Failed to parse cover art attribute");
        return null;
    }

    private static Id3Frame parseInternalAttribute(ParsableByteArray parsableByteArray, int i) {
        String str = null;
        String str2 = str;
        int i2 = -1;
        int i3 = -1;
        while (parsableByteArray.getPosition() < i) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            int readInt2 = parsableByteArray.readInt();
            parsableByteArray.skipBytes(4);
            if (readInt2 == Atom.TYPE_mean) {
                str = parsableByteArray.readNullTerminatedString(readInt - 12);
            } else if (readInt2 == Atom.TYPE_name) {
                str2 = parsableByteArray.readNullTerminatedString(readInt - 12);
            } else {
                if (readInt2 == Atom.TYPE_data) {
                    i2 = position;
                    i3 = readInt;
                }
                parsableByteArray.skipBytes(readInt - 12);
            }
        }
        if (str == null || str2 == null || i2 == -1) {
            return null;
        }
        parsableByteArray.setPosition(i2);
        parsableByteArray.skipBytes(16);
        return new InternalFrame(str, str2, parsableByteArray.readNullTerminatedString(i3 - 16));
    }

    private static int parseUint8AttributeValue(ParsableByteArray parsableByteArray) {
        parsableByteArray.skipBytes(4);
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            parsableByteArray.skipBytes(8);
            return parsableByteArray.readUnsignedByte();
        }
        Log.m18w("MetadataUtil", "Failed to parse uint8 attribute value");
        return -1;
    }
}
