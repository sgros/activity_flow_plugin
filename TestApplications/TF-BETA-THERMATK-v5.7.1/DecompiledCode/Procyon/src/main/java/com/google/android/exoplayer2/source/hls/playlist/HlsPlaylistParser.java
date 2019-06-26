// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls.playlist;

import java.util.Queue;
import java.io.Closeable;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import java.util.ArrayDeque;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import android.net.Uri;
import java.io.UnsupportedEncodingException;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import android.util.Base64;
import java.util.regex.Matcher;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.TreeMap;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.Format;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.google.android.exoplayer2.ParserException;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.upstream.ParsingLoadable;

public final class HlsPlaylistParser implements Parser<HlsPlaylist>
{
    private static final Pattern REGEX_ATTR_BYTERANGE;
    private static final Pattern REGEX_AUDIO;
    private static final Pattern REGEX_AUTOSELECT;
    private static final Pattern REGEX_AVERAGE_BANDWIDTH;
    private static final Pattern REGEX_BANDWIDTH;
    private static final Pattern REGEX_BYTERANGE;
    private static final Pattern REGEX_CODECS;
    private static final Pattern REGEX_DEFAULT;
    private static final Pattern REGEX_FORCED;
    private static final Pattern REGEX_FRAME_RATE;
    private static final Pattern REGEX_GROUP_ID;
    private static final Pattern REGEX_IMPORT;
    private static final Pattern REGEX_INSTREAM_ID;
    private static final Pattern REGEX_IV;
    private static final Pattern REGEX_KEYFORMAT;
    private static final Pattern REGEX_KEYFORMATVERSIONS;
    private static final Pattern REGEX_LANGUAGE;
    private static final Pattern REGEX_MEDIA_DURATION;
    private static final Pattern REGEX_MEDIA_SEQUENCE;
    private static final Pattern REGEX_MEDIA_TITLE;
    private static final Pattern REGEX_METHOD;
    private static final Pattern REGEX_NAME;
    private static final Pattern REGEX_PLAYLIST_TYPE;
    private static final Pattern REGEX_RESOLUTION;
    private static final Pattern REGEX_TARGET_DURATION;
    private static final Pattern REGEX_TIME_OFFSET;
    private static final Pattern REGEX_TYPE;
    private static final Pattern REGEX_URI;
    private static final Pattern REGEX_VALUE;
    private static final Pattern REGEX_VARIABLE_REFERENCE;
    private static final Pattern REGEX_VERSION;
    private final HlsMasterPlaylist masterPlaylist;
    
    static {
        REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
        REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
        REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
        REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
        REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
        REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
        REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
        REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
        REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
        REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
        REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
        REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
        REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
        REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
        REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
        REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
        REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
        REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
        REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
        REGEX_IV = Pattern.compile("IV=([^,.*]+)");
        REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
        REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
        REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
        REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
        REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
        REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
        REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
        REGEX_FORCED = compileBooleanAttrPattern("FORCED");
        REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
        REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
        REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");
    }
    
    public HlsPlaylistParser() {
        this(HlsMasterPlaylist.EMPTY);
    }
    
    public HlsPlaylistParser(final HlsMasterPlaylist masterPlaylist) {
        this.masterPlaylist = masterPlaylist;
    }
    
    private static boolean checkPlaylistHeader(final BufferedReader bufferedReader) throws IOException {
        int n;
        if ((n = bufferedReader.read()) == 239) {
            if (bufferedReader.read() != 187 || bufferedReader.read() != 191) {
                return false;
            }
            n = bufferedReader.read();
        }
        int n2 = skipIgnorableWhitespace(bufferedReader, true, n);
        for (int i = 0; i < 7; ++i) {
            if (n2 != "#EXTM3U".charAt(i)) {
                return false;
            }
            n2 = bufferedReader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(bufferedReader, false, n2));
    }
    
    private static Pattern compileBooleanAttrPattern(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("=(");
        sb.append("NO");
        sb.append("|");
        sb.append("YES");
        sb.append(")");
        return Pattern.compile(sb.toString());
    }
    
    private static boolean isMediaTagMuxed(final List<HlsMasterPlaylist.HlsUrl> list, final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (s.equals(list.get(i).url)) {
                return true;
            }
        }
        return false;
    }
    
    private static double parseDoubleAttr(final String s, final Pattern pattern) throws ParserException {
        return Double.parseDouble(parseStringAttr(s, pattern, Collections.emptyMap()));
    }
    
    private static int parseIntAttr(final String s, final Pattern pattern) throws ParserException {
        return Integer.parseInt(parseStringAttr(s, pattern, Collections.emptyMap()));
    }
    
    private static long parseLongAttr(final String s, final Pattern pattern) throws ParserException {
        return Long.parseLong(parseStringAttr(s, pattern, Collections.emptyMap()));
    }
    
    private static HlsMasterPlaylist parseMasterPlaylist(final LineIterator lineIterator, final String s) throws IOException {
        final HashSet<String> set = new HashSet<String>();
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final HashMap<String, String> hashMap2 = new HashMap<String, String>();
        final ArrayList<HlsMasterPlaylist.HlsUrl> list = new ArrayList<HlsMasterPlaylist.HlsUrl>();
        final ArrayList<HlsMasterPlaylist.HlsUrl> list2 = new ArrayList<HlsMasterPlaylist.HlsUrl>();
        final ArrayList<HlsMasterPlaylist.HlsUrl> list3 = new ArrayList<HlsMasterPlaylist.HlsUrl>();
        final ArrayList<String> list4 = new ArrayList<String>();
        final ArrayList<String> list5 = new ArrayList<String>();
        boolean b = false;
        boolean b2 = false;
        while (lineIterator.hasNext()) {
            final String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                list5.add(next);
            }
            if (next.startsWith("#EXT-X-DEFINE")) {
                hashMap2.put(parseStringAttr(next, HlsPlaylistParser.REGEX_NAME, hashMap2), parseStringAttr(next, HlsPlaylistParser.REGEX_VALUE, hashMap2));
            }
            else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                b2 = true;
            }
            else if (next.startsWith("#EXT-X-MEDIA")) {
                list4.add(next);
            }
            else {
                if (!next.startsWith("#EXT-X-STREAM-INF")) {
                    continue;
                }
                final boolean b3 = b | next.contains("CLOSED-CAPTIONS=NONE");
                int n = parseIntAttr(next, HlsPlaylistParser.REGEX_BANDWIDTH);
                final String optionalStringAttr = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_AVERAGE_BANDWIDTH, hashMap2);
                if (optionalStringAttr != null) {
                    n = Integer.parseInt(optionalStringAttr);
                }
                final String optionalStringAttr2 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_CODECS, hashMap2);
                final String optionalStringAttr3 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_RESOLUTION, hashMap2);
                int n2;
                int n3;
                if (optionalStringAttr3 != null) {
                    final String[] split = optionalStringAttr3.split("x");
                    int int1 = Integer.parseInt(split[0]);
                    int int2 = Integer.parseInt(split[1]);
                    if (int1 <= 0 || int2 <= 0) {
                        int1 = -1;
                        int2 = -1;
                    }
                    n2 = int2;
                    n3 = int1;
                }
                else {
                    n3 = -1;
                    n2 = -1;
                }
                final String optionalStringAttr4 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_FRAME_RATE, hashMap2);
                float float1;
                if (optionalStringAttr4 != null) {
                    float1 = Float.parseFloat(optionalStringAttr4);
                }
                else {
                    float1 = -1.0f;
                }
                final String optionalStringAttr5 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_AUDIO, hashMap2);
                if (optionalStringAttr5 != null && optionalStringAttr2 != null) {
                    hashMap.put(optionalStringAttr5, Util.getCodecsOfType(optionalStringAttr2, 1));
                }
                final String replaceVariableReferences = replaceVariableReferences(lineIterator.next(), hashMap2);
                b = b3;
                if (!set.add(replaceVariableReferences)) {
                    continue;
                }
                list.add(new HlsMasterPlaylist.HlsUrl(replaceVariableReferences, Format.createVideoContainerFormat(Integer.toString(list.size()), null, "application/x-mpegURL", null, optionalStringAttr2, n, n3, n2, float1, null, 0)));
                b = b3;
            }
        }
        int i = 0;
        Format format = null;
        List<Format> emptyList = null;
        while (i < list4.size()) {
            final String s2 = list4.get(i);
            final int selectionFlags = parseSelectionFlags(s2);
            final String optionalStringAttr6 = parseOptionalStringAttr(s2, HlsPlaylistParser.REGEX_URI, hashMap2);
            final String stringAttr = parseStringAttr(s2, HlsPlaylistParser.REGEX_NAME, hashMap2);
            final String optionalStringAttr7 = parseOptionalStringAttr(s2, HlsPlaylistParser.REGEX_LANGUAGE, hashMap2);
            final String optionalStringAttr8 = parseOptionalStringAttr(s2, HlsPlaylistParser.REGEX_GROUP_ID, hashMap2);
            final StringBuilder sb = new StringBuilder();
            sb.append(optionalStringAttr8);
            sb.append(":");
            sb.append(stringAttr);
            final String string = sb.toString();
            final String stringAttr2 = parseStringAttr(s2, HlsPlaylistParser.REGEX_TYPE, hashMap2);
            final int hashCode = stringAttr2.hashCode();
            int n4 = 0;
            Label_0707: {
                if (hashCode != -959297733) {
                    if (hashCode != -333210994) {
                        if (hashCode == 62628790) {
                            if (stringAttr2.equals("AUDIO")) {
                                n4 = 0;
                                break Label_0707;
                            }
                        }
                    }
                    else if (stringAttr2.equals("CLOSED-CAPTIONS")) {
                        n4 = 2;
                        break Label_0707;
                    }
                }
                else if (stringAttr2.equals("SUBTITLES")) {
                    n4 = 1;
                    break Label_0707;
                }
                n4 = -1;
            }
            if (n4 != 0) {
                if (n4 != 1) {
                    if (n4 == 2) {
                        final String stringAttr3 = parseStringAttr(s2, HlsPlaylistParser.REGEX_INSTREAM_ID, hashMap2);
                        int n5;
                        String s3;
                        if (stringAttr3.startsWith("CC")) {
                            n5 = Integer.parseInt(stringAttr3.substring(2));
                            s3 = "application/cea-608";
                        }
                        else {
                            n5 = Integer.parseInt(stringAttr3.substring(7));
                            s3 = "application/cea-708";
                        }
                        List<Format> list6 = emptyList;
                        if (emptyList == null) {
                            list6 = new ArrayList<Format>();
                        }
                        list6.add(Format.createTextContainerFormat(string, stringAttr, null, s3, null, -1, selectionFlags, optionalStringAttr7, n5));
                        emptyList = list6;
                    }
                }
                else {
                    list3.add(new HlsMasterPlaylist.HlsUrl(optionalStringAttr6, Format.createTextContainerFormat(string, stringAttr, "application/x-mpegURL", "text/vtt", null, -1, selectionFlags, optionalStringAttr7)));
                }
            }
            else {
                final String s4 = hashMap.get(optionalStringAttr8);
                String mediaMimeType;
                if (s4 != null) {
                    mediaMimeType = MimeTypes.getMediaMimeType(s4);
                }
                else {
                    mediaMimeType = null;
                }
                final Format audioContainerFormat = Format.createAudioContainerFormat(string, stringAttr, "application/x-mpegURL", mediaMimeType, s4, -1, -1, -1, null, selectionFlags, optionalStringAttr7);
                if (isMediaTagMuxed(list, optionalStringAttr6)) {
                    format = audioContainerFormat;
                }
                else {
                    list2.add(new HlsMasterPlaylist.HlsUrl(optionalStringAttr6, audioContainerFormat));
                }
            }
            ++i;
        }
        if (b) {
            emptyList = Collections.emptyList();
        }
        return new HlsMasterPlaylist(s, list5, list, list2, list3, format, emptyList, b2, hashMap2);
    }
    
    private static HlsMediaPlaylist parseMediaPlaylist(final HlsMasterPlaylist hlsMasterPlaylist, final LineIterator lineIterator, final String s) throws IOException {
        boolean hasIndependentSegments = hlsMasterPlaylist.hasIndependentSegments;
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final ArrayList<HlsMediaPlaylist.Segment> list = new ArrayList<HlsMediaPlaylist.Segment>();
        final ArrayList<String> list2 = new ArrayList<String>();
        final TreeMap<String, DrmInitData.SchemeData> treeMap = new TreeMap<String, DrmInitData.SchemeData>();
        long n2;
        long n = n2 = -9223372036854775807L;
        String optionalStringAttr = "";
        boolean b = false;
        int n3 = 0;
        String s2 = null;
        long n4 = 0L;
        int int1 = 0;
        long longAttr = 0L;
        int intAttr = 1;
        boolean b2 = false;
        DrmInitData drmInitData = null;
        long long1 = 0L;
        long i = 0L;
        DrmInitData drmInitData2 = null;
        boolean b3 = false;
        long long2 = -1L;
        int n5 = 0;
        long n6 = 0L;
        String stringAttr = null;
        String s3 = null;
        HlsMediaPlaylist.Segment segment = null;
        long n7 = 0L;
        while (lineIterator.hasNext()) {
            final String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                list2.add(next);
            }
            if (next.startsWith("#EXT-X-PLAYLIST-TYPE")) {
                final String stringAttr2 = parseStringAttr(next, HlsPlaylistParser.REGEX_PLAYLIST_TYPE, hashMap);
                if ("VOD".equals(stringAttr2)) {
                    n3 = 1;
                }
                else {
                    if (!"EVENT".equals(stringAttr2)) {
                        continue;
                    }
                    n3 = 2;
                }
            }
            else if (next.startsWith("#EXT-X-START")) {
                n = (long)(parseDoubleAttr(next, HlsPlaylistParser.REGEX_TIME_OFFSET) * 1000000.0);
            }
            else if (next.startsWith("#EXT-X-MAP")) {
                final String stringAttr3 = parseStringAttr(next, HlsPlaylistParser.REGEX_URI, hashMap);
                final String optionalStringAttr2 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_ATTR_BYTERANGE, hashMap);
                long long3 = long1;
                if (optionalStringAttr2 != null) {
                    final String[] split = optionalStringAttr2.split("@");
                    final long long4 = Long.parseLong(split[0]);
                    long3 = long1;
                    long2 = long4;
                    if (split.length > 1) {
                        long3 = Long.parseLong(split[1]);
                        long2 = long4;
                    }
                }
                segment = new HlsMediaPlaylist.Segment(stringAttr3, long3, long2);
                long1 = 0L;
                long2 = -1L;
            }
            else if (next.startsWith("#EXT-X-TARGETDURATION")) {
                n2 = 1000000L * parseIntAttr(next, HlsPlaylistParser.REGEX_TARGET_DURATION);
            }
            else if (next.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                i = (longAttr = parseLongAttr(next, HlsPlaylistParser.REGEX_MEDIA_SEQUENCE));
            }
            else if (next.startsWith("#EXT-X-VERSION")) {
                intAttr = parseIntAttr(next, HlsPlaylistParser.REGEX_VERSION);
            }
            else if (next.startsWith("#EXT-X-DEFINE")) {
                final String optionalStringAttr3 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_IMPORT, hashMap);
                if (optionalStringAttr3 != null) {
                    final String value = hlsMasterPlaylist.variableDefinitions.get(optionalStringAttr3);
                    if (value == null) {
                        continue;
                    }
                    hashMap.put(optionalStringAttr3, value);
                }
                else {
                    hashMap.put(parseStringAttr(next, HlsPlaylistParser.REGEX_NAME, hashMap), parseStringAttr(next, HlsPlaylistParser.REGEX_VALUE, hashMap));
                }
            }
            else if (next.startsWith("#EXTINF")) {
                n7 = (long)(parseDoubleAttr(next, HlsPlaylistParser.REGEX_MEDIA_DURATION) * 1000000.0);
                optionalStringAttr = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_MEDIA_TITLE, "", hashMap);
            }
            else if (next.startsWith("#EXT-X-KEY")) {
                final String stringAttr4 = parseStringAttr(next, HlsPlaylistParser.REGEX_METHOD, hashMap);
                final String optionalStringAttr4 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_KEYFORMAT, "identity", hashMap);
                if ("NONE".equals(stringAttr4)) {
                    treeMap.clear();
                    drmInitData2 = null;
                    stringAttr = null;
                    s3 = null;
                }
                else {
                    final String optionalStringAttr5 = parseOptionalStringAttr(next, HlsPlaylistParser.REGEX_IV, hashMap);
                    String s5 = null;
                    Label_0697: {
                        String s4;
                        if ("identity".equals(optionalStringAttr4)) {
                            s4 = s2;
                            if ("AES-128".equals(stringAttr4)) {
                                stringAttr = parseStringAttr(next, HlsPlaylistParser.REGEX_URI, hashMap);
                                s3 = optionalStringAttr5;
                                continue;
                            }
                        }
                        else {
                            if ((s5 = s2) == null) {
                                if (!"SAMPLE-AES-CENC".equals(stringAttr4) && !"SAMPLE-AES-CTR".equals(stringAttr4)) {
                                    s5 = "cbcs";
                                }
                                else {
                                    s5 = "cenc";
                                }
                            }
                            DrmInitData.SchemeData value2;
                            if ("com.microsoft.playready".equals(optionalStringAttr4)) {
                                value2 = parsePlayReadySchemeData(next, hashMap);
                            }
                            else {
                                value2 = parseWidevineSchemeData(next, optionalStringAttr4, hashMap);
                            }
                            s4 = s5;
                            if (value2 != null) {
                                treeMap.put(optionalStringAttr4, value2);
                                drmInitData2 = null;
                                break Label_0697;
                            }
                        }
                        s5 = s4;
                    }
                    stringAttr = null;
                    s2 = s5;
                    s3 = optionalStringAttr5;
                }
            }
            else if (next.startsWith("#EXT-X-BYTERANGE")) {
                final String[] split2 = parseStringAttr(next, HlsPlaylistParser.REGEX_BYTERANGE, hashMap).split("@");
                final long n8 = long2 = Long.parseLong(split2[0]);
                if (split2.length <= 1) {
                    continue;
                }
                long1 = Long.parseLong(split2[1]);
                long2 = n8;
            }
            else if (next.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE")) {
                int1 = Integer.parseInt(next.substring(next.indexOf(58) + 1));
                b = true;
            }
            else if (next.equals("#EXT-X-DISCONTINUITY")) {
                ++n5;
            }
            else if (next.startsWith("#EXT-X-PROGRAM-DATE-TIME")) {
                if (n4 != 0L) {
                    continue;
                }
                n4 = C.msToUs(Util.parseXsDateTime(next.substring(next.indexOf(58) + 1))) - n6;
            }
            else if (next.equals("#EXT-X-GAP")) {
                b3 = true;
            }
            else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                hasIndependentSegments = true;
            }
            else if (next.equals("#EXT-X-ENDLIST")) {
                b2 = true;
            }
            else {
                if (next.startsWith("#")) {
                    continue;
                }
                String hexString;
                if (stringAttr == null) {
                    hexString = null;
                }
                else if (s3 != null) {
                    hexString = s3;
                }
                else {
                    hexString = Long.toHexString(i);
                }
                long n9 = long1;
                if (long2 == -1L) {
                    n9 = 0L;
                }
                if (drmInitData2 == null && !treeMap.isEmpty()) {
                    final DrmInitData.SchemeData[] array = treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                    drmInitData2 = new DrmInitData(s2, array);
                    if (drmInitData == null) {
                        final DrmInitData.SchemeData[] array2 = new DrmInitData.SchemeData[array.length];
                        for (int j = 0; j < array.length; ++j) {
                            array2[j] = array[j].copyWithData(null);
                        }
                        drmInitData = new DrmInitData(s2, array2);
                    }
                }
                list.add(new HlsMediaPlaylist.Segment(replaceVariableReferences(next, hashMap), segment, optionalStringAttr, n7, n5, n6, drmInitData2, stringAttr, hexString, n9, long2, b3));
                n6 += n7;
                long1 = n9;
                if (long2 != -1L) {
                    long1 = n9 + long2;
                }
                b3 = false;
                n7 = 0L;
                optionalStringAttr = "";
                ++i;
                long2 = -1L;
            }
        }
        return new HlsMediaPlaylist(n3, s, list2, n, n4, b, int1, longAttr, intAttr, n2, hasIndependentSegments, b2, n4 != 0L, drmInitData, list);
    }
    
    private static boolean parseOptionalBooleanAttribute(final String input, final Pattern pattern, final boolean b) {
        final Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).equals("YES");
        }
        return b;
    }
    
    private static String parseOptionalStringAttr(String replaceVariableReferences, final Pattern pattern, String group, final Map<String, String> map) {
        final Matcher matcher = pattern.matcher(replaceVariableReferences);
        if (matcher.find()) {
            group = matcher.group(1);
        }
        replaceVariableReferences = group;
        if (!map.isEmpty()) {
            if (group == null) {
                replaceVariableReferences = group;
            }
            else {
                replaceVariableReferences = replaceVariableReferences(group, map);
            }
        }
        return replaceVariableReferences;
    }
    
    private static String parseOptionalStringAttr(final String s, final Pattern pattern, final Map<String, String> map) {
        return parseOptionalStringAttr(s, pattern, null, map);
    }
    
    private static DrmInitData.SchemeData parsePlayReadySchemeData(String stringAttr, final Map<String, String> map) throws ParserException {
        if (!"1".equals(parseOptionalStringAttr(stringAttr, HlsPlaylistParser.REGEX_KEYFORMATVERSIONS, "1", map))) {
            return null;
        }
        stringAttr = parseStringAttr(stringAttr, HlsPlaylistParser.REGEX_URI, map);
        return new DrmInitData.SchemeData(C.PLAYREADY_UUID, "video/mp4", PsshAtomUtil.buildPsshAtom(C.PLAYREADY_UUID, Base64.decode(stringAttr.substring(stringAttr.indexOf(44)), 0)));
    }
    
    private static int parseSelectionFlags(final String s) {
        int optionalBooleanAttribute;
        final boolean b = (optionalBooleanAttribute = (parseOptionalBooleanAttribute(s, HlsPlaylistParser.REGEX_DEFAULT, (boolean)(0 != 0)) ? 1 : 0)) != 0;
        if (parseOptionalBooleanAttribute(s, HlsPlaylistParser.REGEX_FORCED, false)) {
            optionalBooleanAttribute = ((b ? 1 : 0) | 0x2);
        }
        int n = optionalBooleanAttribute;
        if (parseOptionalBooleanAttribute(s, HlsPlaylistParser.REGEX_AUTOSELECT, false)) {
            n = (optionalBooleanAttribute | 0x4);
        }
        return n;
    }
    
    private static String parseStringAttr(final String str, final Pattern pattern, final Map<String, String> map) throws ParserException {
        final String optionalStringAttr = parseOptionalStringAttr(str, pattern, map);
        if (optionalStringAttr != null) {
            return optionalStringAttr;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Couldn't match ");
        sb.append(pattern.pattern());
        sb.append(" in ");
        sb.append(str);
        throw new ParserException(sb.toString());
    }
    
    private static DrmInitData.SchemeData parseWidevineSchemeData(String stringAttr, final String s, final Map<String, String> map) throws ParserException {
        if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(s)) {
            stringAttr = parseStringAttr(stringAttr, HlsPlaylistParser.REGEX_URI, map);
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "video/mp4", Base64.decode(stringAttr.substring(stringAttr.indexOf(44)), 0));
        }
        if ("com.widevine".equals(s)) {
            try {
                return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "hls", stringAttr.getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException ex) {
                throw new ParserException(ex);
            }
        }
        return null;
    }
    
    private static String replaceVariableReferences(String group, final Map<String, String> map) {
        final Matcher matcher = HlsPlaylistParser.REGEX_VARIABLE_REFERENCE.matcher(group);
        final StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            group = matcher.group(1);
            if (map.containsKey(group)) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(map.get(group)));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    private static int skipIgnorableWhitespace(final BufferedReader bufferedReader, final boolean b, int read) throws IOException {
        while (read != -1 && Character.isWhitespace(read) && (b || !Util.isLinebreak(read))) {
            read = bufferedReader.read();
        }
        return read;
    }
    
    public HlsPlaylist parse(final Uri uri, InputStream in) throws IOException {
        in = (InputStream)new BufferedReader(new InputStreamReader(in));
        final ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
        try {
            if (!checkPlaylistHeader((BufferedReader)in)) {
                throw new UnrecognizedInputFormatException("Input does not start with the #EXTM3U header.", uri);
            }
            while (true) {
                final String line = ((BufferedReader)in).readLine();
                if (line == null) {
                    Util.closeQuietly(in);
                    throw new ParserException("Failed to parse the playlist, could not identify any tags.");
                }
                final String trim = line.trim();
                if (trim.isEmpty()) {
                    continue;
                }
                if (trim.startsWith("#EXT-X-STREAM-INF")) {
                    arrayDeque.add(trim);
                    return parseMasterPlaylist(new LineIterator(arrayDeque, (BufferedReader)in), uri.toString());
                }
                if (trim.startsWith("#EXT-X-TARGETDURATION") || trim.startsWith("#EXT-X-MEDIA-SEQUENCE") || trim.startsWith("#EXTINF") || trim.startsWith("#EXT-X-KEY") || trim.startsWith("#EXT-X-BYTERANGE") || trim.equals("#EXT-X-DISCONTINUITY") || trim.equals("#EXT-X-DISCONTINUITY-SEQUENCE") || trim.equals("#EXT-X-ENDLIST")) {
                    arrayDeque.add(trim);
                    return parseMediaPlaylist(this.masterPlaylist, new LineIterator(arrayDeque, (BufferedReader)in), uri.toString());
                }
                arrayDeque.add(trim);
            }
        }
        finally {
            Util.closeQuietly(in);
        }
    }
    
    private static class LineIterator
    {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;
        
        public LineIterator(final Queue<String> extraLines, final BufferedReader reader) {
            this.extraLines = extraLines;
            this.reader = reader;
        }
        
        public boolean hasNext() throws IOException {
            if (this.next != null) {
                return true;
            }
            if (!this.extraLines.isEmpty()) {
                this.next = this.extraLines.poll();
                return true;
            }
            while ((this.next = this.reader.readLine()) != null) {
                this.next = this.next.trim();
                if (!this.next.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        
        public String next() throws IOException {
            String next;
            if (this.hasNext()) {
                next = this.next;
                this.next = null;
            }
            else {
                next = null;
            }
            return next;
        }
    }
}
