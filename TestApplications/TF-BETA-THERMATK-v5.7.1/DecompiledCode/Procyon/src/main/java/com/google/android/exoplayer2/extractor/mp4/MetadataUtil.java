// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Log;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Util;

final class MetadataUtil
{
    private static final int SHORT_TYPE_ALBUM;
    private static final int SHORT_TYPE_ARTIST;
    private static final int SHORT_TYPE_COMMENT;
    private static final int SHORT_TYPE_COMPOSER_1;
    private static final int SHORT_TYPE_COMPOSER_2;
    private static final int SHORT_TYPE_ENCODER;
    private static final int SHORT_TYPE_GENRE;
    private static final int SHORT_TYPE_LYRICS;
    private static final int SHORT_TYPE_NAME_1;
    private static final int SHORT_TYPE_NAME_2;
    private static final int SHORT_TYPE_YEAR;
    private static final String[] STANDARD_GENRES;
    private static final int TYPE_ALBUM_ARTIST;
    private static final int TYPE_COMPILATION;
    private static final int TYPE_COVER_ART;
    private static final int TYPE_DISK_NUMBER;
    private static final int TYPE_GAPLESS_ALBUM;
    private static final int TYPE_GENRE;
    private static final int TYPE_GROUPING;
    private static final int TYPE_INTERNAL;
    private static final int TYPE_RATING;
    private static final int TYPE_SORT_ALBUM;
    private static final int TYPE_SORT_ALBUM_ARTIST;
    private static final int TYPE_SORT_ARTIST;
    private static final int TYPE_SORT_COMPOSER;
    private static final int TYPE_SORT_TRACK_NAME;
    private static final int TYPE_TEMPO;
    private static final int TYPE_TRACK_NUMBER;
    private static final int TYPE_TV_SHOW;
    private static final int TYPE_TV_SORT_SHOW;
    
    static {
        SHORT_TYPE_NAME_1 = Util.getIntegerCodeForString("nam");
        SHORT_TYPE_NAME_2 = Util.getIntegerCodeForString("trk");
        SHORT_TYPE_COMMENT = Util.getIntegerCodeForString("cmt");
        SHORT_TYPE_YEAR = Util.getIntegerCodeForString("day");
        SHORT_TYPE_ARTIST = Util.getIntegerCodeForString("ART");
        SHORT_TYPE_ENCODER = Util.getIntegerCodeForString("too");
        SHORT_TYPE_ALBUM = Util.getIntegerCodeForString("alb");
        SHORT_TYPE_COMPOSER_1 = Util.getIntegerCodeForString("com");
        SHORT_TYPE_COMPOSER_2 = Util.getIntegerCodeForString("wrt");
        SHORT_TYPE_LYRICS = Util.getIntegerCodeForString("lyr");
        SHORT_TYPE_GENRE = Util.getIntegerCodeForString("gen");
        TYPE_COVER_ART = Util.getIntegerCodeForString("covr");
        TYPE_GENRE = Util.getIntegerCodeForString("gnre");
        TYPE_GROUPING = Util.getIntegerCodeForString("grp");
        TYPE_DISK_NUMBER = Util.getIntegerCodeForString("disk");
        TYPE_TRACK_NUMBER = Util.getIntegerCodeForString("trkn");
        TYPE_TEMPO = Util.getIntegerCodeForString("tmpo");
        TYPE_COMPILATION = Util.getIntegerCodeForString("cpil");
        TYPE_ALBUM_ARTIST = Util.getIntegerCodeForString("aART");
        TYPE_SORT_TRACK_NAME = Util.getIntegerCodeForString("sonm");
        TYPE_SORT_ALBUM = Util.getIntegerCodeForString("soal");
        TYPE_SORT_ARTIST = Util.getIntegerCodeForString("soar");
        TYPE_SORT_ALBUM_ARTIST = Util.getIntegerCodeForString("soaa");
        TYPE_SORT_COMPOSER = Util.getIntegerCodeForString("soco");
        TYPE_RATING = Util.getIntegerCodeForString("rtng");
        TYPE_GAPLESS_ALBUM = Util.getIntegerCodeForString("pgap");
        TYPE_TV_SORT_SHOW = Util.getIntegerCodeForString("sosn");
        TYPE_TV_SHOW = Util.getIntegerCodeForString("tvsh");
        TYPE_INTERNAL = Util.getIntegerCodeForString("----");
        STANDARD_GENRES = new String[] { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "Jpop", "Synthpop" };
    }
    
    public static Format getFormatWithMetadata(int i, Format o, Metadata metadata, final Metadata metadata2, final GaplessInfoHolder gaplessInfoHolder) {
        Object copyWithMetadata;
        if (i == 1) {
            Object copyWithGaplessInfo = o;
            if (gaplessInfoHolder.hasGaplessInfo()) {
                copyWithGaplessInfo = ((Format)o).copyWithGaplessInfo(gaplessInfoHolder.encoderDelay, gaplessInfoHolder.encoderPadding);
            }
            copyWithMetadata = copyWithGaplessInfo;
            if (metadata != null) {
                copyWithMetadata = ((Format)copyWithGaplessInfo).copyWithMetadata(metadata);
            }
        }
        else {
            copyWithMetadata = o;
            if (i == 2) {
                copyWithMetadata = o;
                if (metadata2 != null) {
                    Metadata.Entry value;
                    MdtaMetadataEntry mdtaMetadataEntry;
                    Metadata metadata3;
                    for (i = 0; i < metadata2.length(); ++i, o = metadata) {
                        value = metadata2.get(i);
                        metadata = (Metadata)o;
                        if (value instanceof MdtaMetadataEntry) {
                            mdtaMetadataEntry = (MdtaMetadataEntry)value;
                            metadata = (Metadata)o;
                            if ("com.android.capture.fps".equals(mdtaMetadataEntry.key)) {
                                metadata = (Metadata)o;
                                if (mdtaMetadataEntry.typeIndicator == 23) {
                                    metadata = (Metadata)o;
                                    try {
                                        o = (metadata = (metadata = (Metadata)((Format)o).copyWithFrameRate(ByteBuffer.wrap(mdtaMetadataEntry.value).asFloatBuffer().get())));
                                        metadata3 = new Metadata(new Metadata.Entry[] { mdtaMetadataEntry });
                                        metadata = (Metadata)o;
                                        o = (metadata = (Metadata)((Format)o).copyWithMetadata(metadata3));
                                    }
                                    catch (NumberFormatException ex) {
                                        Log.w("MetadataUtil", "Ignoring invalid framerate");
                                    }
                                }
                            }
                        }
                    }
                    copyWithMetadata = o;
                }
            }
        }
        return (Format)copyWithMetadata;
    }
    
    private static CommentFrame parseCommentAttribute(final int n, final ParsableByteArray parsableByteArray) {
        final int int1 = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            parsableByteArray.skipBytes(8);
            final String nullTerminatedString = parsableByteArray.readNullTerminatedString(int1 - 16);
            return new CommentFrame("und", nullTerminatedString, nullTerminatedString);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to parse comment attribute: ");
        sb.append(Atom.getAtomTypeString(n));
        Log.w("MetadataUtil", sb.toString());
        return null;
    }
    
    private static ApicFrame parseCoverArt(final ParsableByteArray parsableByteArray) {
        final int int1 = parsableByteArray.readInt();
        if (parsableByteArray.readInt() != Atom.TYPE_data) {
            Log.w("MetadataUtil", "Failed to parse cover art attribute");
            return null;
        }
        final int fullAtomFlags = Atom.parseFullAtomFlags(parsableByteArray.readInt());
        String s;
        if (fullAtomFlags == 13) {
            s = "image/jpeg";
        }
        else if (fullAtomFlags == 14) {
            s = "image/png";
        }
        else {
            s = null;
        }
        if (s == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized cover art flags: ");
            sb.append(fullAtomFlags);
            Log.w("MetadataUtil", sb.toString());
            return null;
        }
        parsableByteArray.skipBytes(4);
        final byte[] array = new byte[int1 - 16];
        parsableByteArray.readBytes(array, 0, array.length);
        return new ApicFrame(s, null, 3, array);
    }
    
    public static Metadata.Entry parseIlstElement(final ParsableByteArray parsableByteArray) {
        final int position = parsableByteArray.getPosition() + parsableByteArray.readInt();
        final int int1 = parsableByteArray.readInt();
        final int n = int1 >> 24 & 0xFF;
        Label_0463: {
            if (n == 169 || n == 253) {
                break Label_0463;
            }
            try {
                if (int1 == MetadataUtil.TYPE_GENRE) {
                    return parseStandardGenreAttribute(parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_DISK_NUMBER) {
                    return parseIndexAndCountAttribute(int1, "TPOS", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_TRACK_NUMBER) {
                    return parseIndexAndCountAttribute(int1, "TRCK", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_TEMPO) {
                    return parseUint8Attribute(int1, "TBPM", parsableByteArray, true, false);
                }
                if (int1 == MetadataUtil.TYPE_COMPILATION) {
                    return parseUint8Attribute(int1, "TCMP", parsableByteArray, true, true);
                }
                if (int1 == MetadataUtil.TYPE_COVER_ART) {
                    return parseCoverArt(parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_ALBUM_ARTIST) {
                    return parseTextAttribute(int1, "TPE2", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_SORT_TRACK_NAME) {
                    return parseTextAttribute(int1, "TSOT", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_SORT_ALBUM) {
                    return parseTextAttribute(int1, "TSO2", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_SORT_ARTIST) {
                    return parseTextAttribute(int1, "TSOA", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_SORT_ALBUM_ARTIST) {
                    return parseTextAttribute(int1, "TSOP", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_SORT_COMPOSER) {
                    return parseTextAttribute(int1, "TSOC", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_RATING) {
                    return parseUint8Attribute(int1, "ITUNESADVISORY", parsableByteArray, false, false);
                }
                if (int1 == MetadataUtil.TYPE_GAPLESS_ALBUM) {
                    return parseUint8Attribute(int1, "ITUNESGAPLESS", parsableByteArray, false, true);
                }
                if (int1 == MetadataUtil.TYPE_TV_SORT_SHOW) {
                    return parseTextAttribute(int1, "TVSHOWSORT", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_TV_SHOW) {
                    return parseTextAttribute(int1, "TVSHOW", parsableByteArray);
                }
                if (int1 == MetadataUtil.TYPE_INTERNAL) {
                    return parseInternalAttribute(parsableByteArray, position);
                }
                final StringBuilder sb;
                Label_0700: {
                    sb = new StringBuilder();
                }
                sb.append("Skipped unknown metadata entry: ");
                sb.append(Atom.getAtomTypeString(int1));
                Log.d("MetadataUtil", sb.toString());
                return null;
                Label_0600:
                // iftrue(Label_0625:, n2 != MetadataUtil.SHORT_TYPE_ALBUM)
                return parseTextAttribute(int1, "TALB", parsableByteArray);
                Label_0675:
                // iftrue(Label_0700:, n2 != MetadataUtil.TYPE_GROUPING)
                return parseTextAttribute(int1, "TIT1", parsableByteArray);
                Label_0765:
                return parseTextAttribute(int1, "TIT2", parsableByteArray);
                Label_0625:
                // iftrue(Label_0650:, n2 != MetadataUtil.SHORT_TYPE_LYRICS)
                return parseTextAttribute(int1, "USLT", parsableByteArray);
                Label_0747:
                return parseTextAttribute(int1, "TCOM", parsableByteArray);
                // iftrue(Label_0550:, n2 != MetadataUtil.SHORT_TYPE_YEAR)
                // iftrue(Label_0675:, n2 != MetadataUtil.SHORT_TYPE_GENRE)
                // iftrue(Label_0600:, n2 != MetadataUtil.SHORT_TYPE_ENCODER)
                // iftrue(Label_0575:, n2 != MetadataUtil.SHORT_TYPE_ARTIST)
                // iftrue(Label_0747:, n2 == MetadataUtil.SHORT_TYPE_COMPOSER_1 || n2 == MetadataUtil.SHORT_TYPE_COMPOSER_2)
                // iftrue(Label_0765:, n2 == MetadataUtil.SHORT_TYPE_NAME_1 || n2 == MetadataUtil.SHORT_TYPE_NAME_2)
                while (true) {
                    while (true) {
                        return parseTextAttribute(int1, "TDRC", parsableByteArray);
                        Label_0650:
                        return parseTextAttribute(int1, "TCON", parsableByteArray);
                        Label_0575:
                        return parseTextAttribute(int1, "TSSE", parsableByteArray);
                        Label_0550:
                        return parseTextAttribute(int1, "TPE1", parsableByteArray);
                        continue;
                    }
                    Label_0491:
                    continue;
                }
                final int n2 = 0xFFFFFF & int1;
                // iftrue(Label_0491:, n2 != MetadataUtil.SHORT_TYPE_COMMENT)
                return parseCommentAttribute(int1, parsableByteArray);
            }
            finally {
                parsableByteArray.setPosition(position);
            }
        }
    }
    
    private static TextInformationFrame parseIndexAndCountAttribute(int unsignedShort, final String s, final ParsableByteArray parsableByteArray) {
        final int int1 = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == Atom.TYPE_data && int1 >= 22) {
            parsableByteArray.skipBytes(10);
            final int unsignedShort2 = parsableByteArray.readUnsignedShort();
            if (unsignedShort2 > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(unsignedShort2);
                final String string = sb.toString();
                unsignedShort = parsableByteArray.readUnsignedShort();
                String string2 = string;
                if (unsignedShort > 0) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(string);
                    sb2.append("/");
                    sb2.append(unsignedShort);
                    string2 = sb2.toString();
                }
                return new TextInformationFrame(s, null, string2);
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Failed to parse index/count attribute: ");
        sb3.append(Atom.getAtomTypeString(unsignedShort));
        Log.w("MetadataUtil", sb3.toString());
        return null;
    }
    
    private static Id3Frame parseInternalAttribute(final ParsableByteArray parsableByteArray, final int n) {
        String nullTerminatedString2;
        String nullTerminatedString = nullTerminatedString2 = null;
        int position = -1;
        int n2 = -1;
        while (parsableByteArray.getPosition() < n) {
            final int position2 = parsableByteArray.getPosition();
            final int int1 = parsableByteArray.readInt();
            final int int2 = parsableByteArray.readInt();
            parsableByteArray.skipBytes(4);
            if (int2 == Atom.TYPE_mean) {
                nullTerminatedString = parsableByteArray.readNullTerminatedString(int1 - 12);
            }
            else if (int2 == Atom.TYPE_name) {
                nullTerminatedString2 = parsableByteArray.readNullTerminatedString(int1 - 12);
            }
            else {
                if (int2 == Atom.TYPE_data) {
                    position = position2;
                    n2 = int1;
                }
                parsableByteArray.skipBytes(int1 - 12);
            }
        }
        if (nullTerminatedString != null && nullTerminatedString2 != null && position != -1) {
            parsableByteArray.setPosition(position);
            parsableByteArray.skipBytes(16);
            return new InternalFrame(nullTerminatedString, nullTerminatedString2, parsableByteArray.readNullTerminatedString(n2 - 16));
        }
        return null;
    }
    
    public static MdtaMetadataEntry parseMdtaMetadataEntryFromIlst(final ParsableByteArray parsableByteArray, int int1, final String s) {
        while (true) {
            final int position = parsableByteArray.getPosition();
            if (position >= int1) {
                return null;
            }
            int int2 = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_data) {
                int1 = parsableByteArray.readInt();
                final int int3 = parsableByteArray.readInt();
                int2 -= 16;
                final byte[] array = new byte[int2];
                parsableByteArray.readBytes(array, 0, int2);
                return new MdtaMetadataEntry(s, array, int3, int1);
            }
            parsableByteArray.setPosition(position + int2);
        }
    }
    
    private static TextInformationFrame parseStandardGenreAttribute(final ParsableByteArray parsableByteArray) {
        final int uint8AttributeValue = parseUint8AttributeValue(parsableByteArray);
        String s = null;
        Label_0030: {
            if (uint8AttributeValue > 0) {
                final String[] standard_GENRES = MetadataUtil.STANDARD_GENRES;
                if (uint8AttributeValue <= standard_GENRES.length) {
                    s = standard_GENRES[uint8AttributeValue - 1];
                    break Label_0030;
                }
            }
            s = null;
        }
        if (s != null) {
            return new TextInformationFrame("TCON", null, s);
        }
        Log.w("MetadataUtil", "Failed to parse standard genre code");
        return null;
    }
    
    private static TextInformationFrame parseTextAttribute(final int n, final String s, final ParsableByteArray parsableByteArray) {
        final int int1 = parsableByteArray.readInt();
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            parsableByteArray.skipBytes(8);
            return new TextInformationFrame(s, null, parsableByteArray.readNullTerminatedString(int1 - 16));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to parse text attribute: ");
        sb.append(Atom.getAtomTypeString(n));
        Log.w("MetadataUtil", sb.toString());
        return null;
    }
    
    private static Id3Frame parseUint8Attribute(final int n, final String s, final ParsableByteArray parsableByteArray, final boolean b, final boolean b2) {
        int n2;
        final int b3 = n2 = parseUint8AttributeValue(parsableByteArray);
        if (b2) {
            n2 = Math.min(1, b3);
        }
        if (n2 >= 0) {
            Id3Frame id3Frame;
            if (b) {
                id3Frame = new TextInformationFrame(s, null, Integer.toString(n2));
            }
            else {
                id3Frame = new CommentFrame("und", s, Integer.toString(n2));
            }
            return id3Frame;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to parse uint8 attribute: ");
        sb.append(Atom.getAtomTypeString(n));
        Log.w("MetadataUtil", sb.toString());
        return null;
    }
    
    private static int parseUint8AttributeValue(final ParsableByteArray parsableByteArray) {
        parsableByteArray.skipBytes(4);
        if (parsableByteArray.readInt() == Atom.TYPE_data) {
            parsableByteArray.skipBytes(8);
            return parsableByteArray.readUnsignedByte();
        }
        Log.w("MetadataUtil", "Failed to parse uint8 attribute value");
        return -1;
    }
}
