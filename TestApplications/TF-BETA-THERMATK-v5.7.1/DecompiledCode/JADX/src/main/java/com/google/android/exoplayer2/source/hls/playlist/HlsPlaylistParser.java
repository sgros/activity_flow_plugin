package com.google.android.exoplayer2.source.hls.playlist;

import android.util.Base64;
import com.google.android.exoplayer2.C0131C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HlsPlaylistParser implements Parser<HlsPlaylist> {
    private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
    private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
    private static final Pattern REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
    private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
    private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
    private static final Pattern REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
    private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
    private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
    private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
    private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final Pattern REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
    private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
    private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
    private static final Pattern REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
    private static final Pattern REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
    private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
    private static final Pattern REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");
    private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private final HlsMasterPlaylist masterPlaylist;

    private static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> queue, BufferedReader bufferedReader) {
            this.extraLines = queue;
            this.reader = bufferedReader;
        }

        public boolean hasNext() throws IOException {
            if (this.next != null) {
                return true;
            }
            if (this.extraLines.isEmpty()) {
                do {
                    String readLine = this.reader.readLine();
                    this.next = readLine;
                    if (readLine == null) {
                        return false;
                    }
                    this.next = this.next.trim();
                } while (this.next.isEmpty());
                return true;
            }
            this.next = (String) this.extraLines.poll();
            return true;
        }

        public String next() throws IOException {
            if (!hasNext()) {
                return null;
            }
            String str = this.next;
            this.next = null;
            return str;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:44:0x00b5 in {8, 13, 31, 32, 35, 37, 40, 43} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist parse(android.net.Uri r4, java.io.InputStream r5) throws java.io.IOException {
        /*
        r3 = this;
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r5);
        r0.<init>(r1);
        r5 = new java.util.ArrayDeque;
        r5.<init>();
        r1 = checkPlaylistHeader(r0);	 Catch:{ all -> 0x00b0 }
        if (r1 == 0) goto L_0x00a8;	 Catch:{ all -> 0x00b0 }
        r1 = r0.readLine();	 Catch:{ all -> 0x00b0 }
        if (r1 == 0) goto L_0x009d;	 Catch:{ all -> 0x00b0 }
        r1 = r1.trim();	 Catch:{ all -> 0x00b0 }
        r2 = r1.isEmpty();	 Catch:{ all -> 0x00b0 }
        if (r2 == 0) goto L_0x0026;	 Catch:{ all -> 0x00b0 }
        goto L_0x0015;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-STREAM-INF";	 Catch:{ all -> 0x00b0 }
        r2 = r1.startsWith(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 == 0) goto L_0x0042;	 Catch:{ all -> 0x00b0 }
        r5.add(r1);	 Catch:{ all -> 0x00b0 }
        r1 = new com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser$LineIterator;	 Catch:{ all -> 0x00b0 }
        r1.<init>(r5, r0);	 Catch:{ all -> 0x00b0 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b0 }
        r4 = parseMasterPlaylist(r1, r4);	 Catch:{ all -> 0x00b0 }
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        return r4;
        r2 = "#EXT-X-TARGETDURATION";	 Catch:{ all -> 0x00b0 }
        r2 = r1.startsWith(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-MEDIA-SEQUENCE";	 Catch:{ all -> 0x00b0 }
        r2 = r1.startsWith(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXTINF";	 Catch:{ all -> 0x00b0 }
        r2 = r1.startsWith(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-KEY";	 Catch:{ all -> 0x00b0 }
        r2 = r1.startsWith(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-BYTERANGE";	 Catch:{ all -> 0x00b0 }
        r2 = r1.startsWith(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-DISCONTINUITY";	 Catch:{ all -> 0x00b0 }
        r2 = r1.equals(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-DISCONTINUITY-SEQUENCE";	 Catch:{ all -> 0x00b0 }
        r2 = r1.equals(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 != 0) goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r2 = "#EXT-X-ENDLIST";	 Catch:{ all -> 0x00b0 }
        r2 = r1.equals(r2);	 Catch:{ all -> 0x00b0 }
        if (r2 == 0) goto L_0x0083;	 Catch:{ all -> 0x00b0 }
        goto L_0x0087;	 Catch:{ all -> 0x00b0 }
        r5.add(r1);	 Catch:{ all -> 0x00b0 }
        goto L_0x0015;	 Catch:{ all -> 0x00b0 }
        r5.add(r1);	 Catch:{ all -> 0x00b0 }
        r1 = r3.masterPlaylist;	 Catch:{ all -> 0x00b0 }
        r2 = new com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser$LineIterator;	 Catch:{ all -> 0x00b0 }
        r2.<init>(r5, r0);	 Catch:{ all -> 0x00b0 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b0 }
        r4 = parseMediaPlaylist(r1, r2, r4);	 Catch:{ all -> 0x00b0 }
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        return r4;
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        r4 = new com.google.android.exoplayer2.ParserException;
        r5 = "Failed to parse the playlist, could not identify any tags.";
        r4.<init>(r5);
        throw r4;
        r5 = new com.google.android.exoplayer2.source.UnrecognizedInputFormatException;	 Catch:{ all -> 0x00b0 }
        r1 = "Input does not start with the #EXTM3U header.";	 Catch:{ all -> 0x00b0 }
        r5.<init>(r1, r4);	 Catch:{ all -> 0x00b0 }
        throw r5;	 Catch:{ all -> 0x00b0 }
        r4 = move-exception;
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser.parse(android.net.Uri, java.io.InputStream):com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist");
    }

    public HlsPlaylistParser() {
        this(HlsMasterPlaylist.EMPTY);
    }

    public HlsPlaylistParser(HlsMasterPlaylist hlsMasterPlaylist) {
        this.masterPlaylist = hlsMasterPlaylist;
    }

    private static boolean checkPlaylistHeader(BufferedReader bufferedReader) throws IOException {
        int read = bufferedReader.read();
        if (read == 239) {
            if (bufferedReader.read() != 187 || bufferedReader.read() != 191) {
                return false;
            }
            read = bufferedReader.read();
        }
        char skipIgnorableWhitespace = skipIgnorableWhitespace(bufferedReader, true, read);
        for (read = 0; read < 7; read++) {
            if (skipIgnorableWhitespace != "#EXTM3U".charAt(read)) {
                return false;
            }
            skipIgnorableWhitespace = bufferedReader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(bufferedReader, false, skipIgnorableWhitespace));
    }

    private static int skipIgnorableWhitespace(BufferedReader bufferedReader, boolean z, int i) throws IOException {
        while (i != -1 && Character.isWhitespace(i) && (z || !Util.isLinebreak(i))) {
            i = bufferedReader.read();
        }
        return i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x0203  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x019e  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x019e  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0203  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0203  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x019e  */
    private static com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist parseMasterPlaylist(com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser.LineIterator r32, java.lang.String r33) throws java.io.IOException {
        /*
        r0 = new java.util.HashSet;
        r0.<init>();
        r1 = new java.util.HashMap;
        r1.<init>();
        r11 = new java.util.HashMap;
        r11.<init>();
        r5 = new java.util.ArrayList;
        r5.<init>();
        r6 = new java.util.ArrayList;
        r6.<init>();
        r7 = new java.util.ArrayList;
        r7.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r4 = new java.util.ArrayList;
        r4.<init>();
        r3 = 0;
        r8 = 1;
        r9 = 0;
        r10 = 0;
    L_0x002c:
        r12 = r32.hasNext();
        if (r12 == 0) goto L_0x011b;
    L_0x0032:
        r12 = r32.next();
        r14 = "#EXT";
        r14 = r12.startsWith(r14);
        if (r14 == 0) goto L_0x0041;
    L_0x003e:
        r4.add(r12);
    L_0x0041:
        r14 = "#EXT-X-DEFINE";
        r14 = r12.startsWith(r14);
        if (r14 == 0) goto L_0x0059;
    L_0x0049:
        r13 = REGEX_NAME;
        r13 = parseStringAttr(r12, r13, r11);
        r14 = REGEX_VALUE;
        r12 = parseStringAttr(r12, r14, r11);
        r11.put(r13, r12);
        goto L_0x002c;
    L_0x0059:
        r14 = "#EXT-X-INDEPENDENT-SEGMENTS";
        r14 = r12.equals(r14);
        if (r14 == 0) goto L_0x0063;
    L_0x0061:
        r10 = 1;
        goto L_0x002c;
    L_0x0063:
        r14 = "#EXT-X-MEDIA";
        r14 = r12.startsWith(r14);
        if (r14 == 0) goto L_0x006f;
    L_0x006b:
        r2.add(r12);
        goto L_0x002c;
    L_0x006f:
        r14 = "#EXT-X-STREAM-INF";
        r14 = r12.startsWith(r14);
        if (r14 == 0) goto L_0x002c;
    L_0x0077:
        r14 = "CLOSED-CAPTIONS=NONE";
        r14 = r12.contains(r14);
        r9 = r9 | r14;
        r14 = REGEX_BANDWIDTH;
        r14 = parseIntAttr(r12, r14);
        r15 = REGEX_AVERAGE_BANDWIDTH;
        r15 = parseOptionalStringAttr(r12, r15, r11);
        if (r15 == 0) goto L_0x0090;
    L_0x008c:
        r14 = java.lang.Integer.parseInt(r15);
    L_0x0090:
        r20 = r14;
        r14 = REGEX_CODECS;
        r14 = parseOptionalStringAttr(r12, r14, r11);
        r15 = REGEX_RESOLUTION;
        r15 = parseOptionalStringAttr(r12, r15, r11);
        if (r15 == 0) goto L_0x00c3;
    L_0x00a0:
        r13 = "x";
        r13 = r15.split(r13);
        r15 = r13[r3];
        r15 = java.lang.Integer.parseInt(r15);
        r13 = r13[r8];
        r13 = java.lang.Integer.parseInt(r13);
        if (r15 <= 0) goto L_0x00bb;
    L_0x00b4:
        if (r13 > 0) goto L_0x00b7;
    L_0x00b6:
        goto L_0x00bb;
    L_0x00b7:
        r16 = r13;
        r13 = r15;
        goto L_0x00be;
    L_0x00bb:
        r13 = -1;
        r16 = -1;
    L_0x00be:
        r21 = r13;
        r22 = r16;
        goto L_0x00c7;
    L_0x00c3:
        r21 = -1;
        r22 = -1;
    L_0x00c7:
        r13 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r15 = REGEX_FRAME_RATE;
        r15 = parseOptionalStringAttr(r12, r15, r11);
        if (r15 == 0) goto L_0x00d8;
    L_0x00d1:
        r13 = java.lang.Float.parseFloat(r15);
        r23 = r13;
        goto L_0x00da;
    L_0x00d8:
        r23 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
    L_0x00da:
        r13 = REGEX_AUDIO;
        r12 = parseOptionalStringAttr(r12, r13, r11);
        if (r12 == 0) goto L_0x00eb;
    L_0x00e2:
        if (r14 == 0) goto L_0x00eb;
    L_0x00e4:
        r13 = com.google.android.exoplayer2.util.Util.getCodecsOfType(r14, r8);
        r1.put(r12, r13);
    L_0x00eb:
        r12 = r32.next();
        r12 = replaceVariableReferences(r12, r11);
        r13 = r0.add(r12);
        if (r13 == 0) goto L_0x002c;
    L_0x00f9:
        r13 = r5.size();
        r15 = java.lang.Integer.toString(r13);
        r16 = 0;
        r18 = 0;
        r24 = 0;
        r25 = 0;
        r17 = "application/x-mpegURL";
        r19 = r14;
        r13 = com.google.android.exoplayer2.Format.createVideoContainerFormat(r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25);
        r14 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r14.<init>(r12, r13);
        r5.add(r14);
        goto L_0x002c;
    L_0x011b:
        r12 = 0;
        r13 = 0;
        r14 = 0;
    L_0x011e:
        r15 = r2.size();
        if (r12 >= r15) goto L_0x0244;
    L_0x0124:
        r15 = r2.get(r12);
        r15 = (java.lang.String) r15;
        r26 = parseSelectionFlags(r15);
        r0 = REGEX_URI;
        r0 = parseOptionalStringAttr(r15, r0, r11);
        r3 = REGEX_NAME;
        r3 = parseStringAttr(r15, r3, r11);
        r8 = REGEX_LANGUAGE;
        r27 = parseOptionalStringAttr(r15, r8, r11);
        r8 = REGEX_GROUP_ID;
        r8 = parseOptionalStringAttr(r15, r8, r11);
        r28 = r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r8);
        r29 = r10;
        r10 = ":";
        r2.append(r10);
        r2.append(r3);
        r17 = r2.toString();
        r2 = REGEX_TYPE;
        r2 = parseStringAttr(r15, r2, r11);
        r10 = r2.hashCode();
        r30 = r13;
        r13 = -959297733; // 0xffffffffc6d2473b float:-26915.615 double:NaN;
        r31 = r4;
        r4 = 2;
        if (r10 == r13) goto L_0x0191;
    L_0x0172:
        r13 = -333210994; // 0xffffffffec239a8e float:-7.911391E26 double:NaN;
        if (r10 == r13) goto L_0x0187;
    L_0x0177:
        r13 = 62628790; // 0x3bba3b6 float:1.1028458E-36 double:3.09427336E-316;
        if (r10 == r13) goto L_0x017d;
    L_0x017c:
        goto L_0x019b;
    L_0x017d:
        r10 = "AUDIO";
        r2 = r2.equals(r10);
        if (r2 == 0) goto L_0x019b;
    L_0x0185:
        r2 = 0;
        goto L_0x019c;
    L_0x0187:
        r10 = "CLOSED-CAPTIONS";
        r2 = r2.equals(r10);
        if (r2 == 0) goto L_0x019b;
    L_0x018f:
        r2 = 2;
        goto L_0x019c;
    L_0x0191:
        r10 = "SUBTITLES";
        r2 = r2.equals(r10);
        if (r2 == 0) goto L_0x019b;
    L_0x0199:
        r2 = 1;
        goto L_0x019c;
    L_0x019b:
        r2 = -1;
    L_0x019c:
        if (r2 == 0) goto L_0x0203;
    L_0x019e:
        r10 = 1;
        if (r2 == r10) goto L_0x01e8;
    L_0x01a1:
        if (r2 == r4) goto L_0x01a5;
    L_0x01a3:
        goto L_0x0236;
    L_0x01a5:
        r0 = REGEX_INSTREAM_ID;
        r0 = parseStringAttr(r15, r0, r11);
        r2 = "CC";
        r2 = r0.startsWith(r2);
        if (r2 == 0) goto L_0x01be;
    L_0x01b3:
        r0 = r0.substring(r4);
        r0 = java.lang.Integer.parseInt(r0);
        r2 = "application/cea-608";
        goto L_0x01c9;
    L_0x01be:
        r2 = 7;
        r0 = r0.substring(r2);
        r0 = java.lang.Integer.parseInt(r0);
        r2 = "application/cea-708";
    L_0x01c9:
        r25 = r0;
        r20 = r2;
        if (r14 != 0) goto L_0x01d4;
    L_0x01cf:
        r14 = new java.util.ArrayList;
        r14.<init>();
    L_0x01d4:
        r19 = 0;
        r21 = 0;
        r22 = -1;
        r18 = r3;
        r23 = r26;
        r24 = r27;
        r0 = com.google.android.exoplayer2.Format.createTextContainerFormat(r17, r18, r19, r20, r21, r22, r23, r24, r25);
        r14.add(r0);
        goto L_0x0236;
    L_0x01e8:
        r21 = 0;
        r22 = -1;
        r19 = "application/x-mpegURL";
        r20 = "text/vtt";
        r18 = r3;
        r23 = r26;
        r24 = r27;
        r2 = com.google.android.exoplayer2.Format.createTextContainerFormat(r17, r18, r19, r20, r21, r22, r23, r24);
        r3 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r3.<init>(r0, r2);
        r7.add(r3);
        goto L_0x0236;
    L_0x0203:
        r10 = 1;
        r2 = r1.get(r8);
        r21 = r2;
        r21 = (java.lang.String) r21;
        if (r21 == 0) goto L_0x0215;
    L_0x020e:
        r2 = com.google.android.exoplayer2.util.MimeTypes.getMediaMimeType(r21);
        r20 = r2;
        goto L_0x0217;
    L_0x0215:
        r20 = 0;
    L_0x0217:
        r22 = -1;
        r23 = -1;
        r24 = -1;
        r25 = 0;
        r19 = "application/x-mpegURL";
        r18 = r3;
        r13 = com.google.android.exoplayer2.Format.createAudioContainerFormat(r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27);
        r2 = isMediaTagMuxed(r5, r0);
        if (r2 == 0) goto L_0x022e;
    L_0x022d:
        goto L_0x0238;
    L_0x022e:
        r2 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r2.<init>(r0, r13);
        r6.add(r2);
    L_0x0236:
        r13 = r30;
    L_0x0238:
        r12 = r12 + 1;
        r2 = r28;
        r10 = r29;
        r4 = r31;
        r3 = 0;
        r8 = 1;
        goto L_0x011e;
    L_0x0244:
        r31 = r4;
        r29 = r10;
        r30 = r13;
        if (r9 == 0) goto L_0x0252;
    L_0x024c:
        r0 = java.util.Collections.emptyList();
        r9 = r0;
        goto L_0x0253;
    L_0x0252:
        r9 = r14;
    L_0x0253:
        r0 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
        r2 = r0;
        r3 = r33;
        r4 = r31;
        r8 = r30;
        r10 = r29;
        r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser.parseMasterPlaylist(com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser$LineIterator, java.lang.String):com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist");
    }

    private static int parseSelectionFlags(String str) {
        int i = parseOptionalBooleanAttribute(str, REGEX_DEFAULT, false) ? 1 : 0;
        if (parseOptionalBooleanAttribute(str, REGEX_FORCED, false)) {
            i |= 2;
        }
        return parseOptionalBooleanAttribute(str, REGEX_AUTOSELECT, false) ? i | 4 : i;
    }

    private static HlsMediaPlaylist parseMediaPlaylist(HlsMasterPlaylist hlsMasterPlaylist, LineIterator lineIterator, String str) throws IOException {
        HlsMasterPlaylist hlsMasterPlaylist2 = hlsMasterPlaylist;
        boolean z = hlsMasterPlaylist2.hasIndependentSegments;
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        TreeMap treeMap = new TreeMap();
        String str2 = "";
        int i = 0;
        int i2 = 1;
        boolean z2 = z;
        long j = -9223372036854775807L;
        long j2 = j;
        String str3 = str2;
        z = false;
        int i3 = 0;
        String str4 = null;
        long j3 = 0;
        int i4 = 0;
        long j4 = 0;
        int i5 = 1;
        boolean z3 = false;
        DrmInitData drmInitData = null;
        long j5 = 0;
        long j6 = 0;
        DrmInitData drmInitData2 = null;
        boolean z4 = false;
        long j7 = -1;
        int i6 = 0;
        long j8 = 0;
        String str5 = null;
        String str6 = null;
        Segment segment = null;
        long j9 = 0;
        while (lineIterator.hasNext()) {
            String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                arrayList2.add(next);
            }
            String parseStringAttr;
            if (next.startsWith("#EXT-X-PLAYLIST-TYPE")) {
                parseStringAttr = parseStringAttr(next, REGEX_PLAYLIST_TYPE, hashMap);
                if ("VOD".equals(parseStringAttr)) {
                    i3 = 1;
                } else if ("EVENT".equals(parseStringAttr)) {
                    i3 = 2;
                }
            } else if (next.startsWith("#EXT-X-START")) {
                j = (long) (parseDoubleAttr(next, REGEX_TIME_OFFSET) * 1000000.0d);
            } else {
                String str7 = "@";
                String[] split;
                if (next.startsWith("#EXT-X-MAP")) {
                    String parseStringAttr2 = parseStringAttr(next, REGEX_URI, hashMap);
                    parseStringAttr = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, hashMap);
                    if (parseStringAttr != null) {
                        split = parseStringAttr.split(str7);
                        j7 = Long.parseLong(split[i]);
                        if (split.length > i2) {
                            j5 = Long.parseLong(split[i2]);
                        }
                    }
                    segment = new Segment(parseStringAttr2, j5, j7);
                    j5 = 0;
                    j7 = -1;
                } else if (next.startsWith("#EXT-X-TARGETDURATION")) {
                    j2 = 1000000 * ((long) parseIntAttr(next, REGEX_TARGET_DURATION));
                } else if (next.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                    j6 = parseLongAttr(next, REGEX_MEDIA_SEQUENCE);
                    j4 = j6;
                } else if (next.startsWith("#EXT-X-VERSION")) {
                    i5 = parseIntAttr(next, REGEX_VERSION);
                } else {
                    long parseDoubleAttr;
                    if (next.startsWith("#EXT-X-DEFINE")) {
                        parseStringAttr = parseOptionalStringAttr(next, REGEX_IMPORT, hashMap);
                        if (parseStringAttr != null) {
                            str7 = (String) hlsMasterPlaylist2.variableDefinitions.get(parseStringAttr);
                            if (str7 != null) {
                                hashMap.put(parseStringAttr, str7);
                            }
                        } else {
                            hashMap.put(parseStringAttr(next, REGEX_NAME, hashMap), parseStringAttr(next, REGEX_VALUE, hashMap));
                        }
                    } else if (next.startsWith("#EXTINF")) {
                        parseDoubleAttr = (long) (parseDoubleAttr(next, REGEX_MEDIA_DURATION) * 1000000.0d);
                        str3 = parseOptionalStringAttr(next, REGEX_MEDIA_TITLE, str2, hashMap);
                        j9 = parseDoubleAttr;
                    } else if (next.startsWith("#EXT-X-KEY")) {
                        parseStringAttr = parseStringAttr(next, REGEX_METHOD, hashMap);
                        String str8 = "identity";
                        str7 = parseOptionalStringAttr(next, REGEX_KEYFORMAT, str8, hashMap);
                        if ("NONE".equals(parseStringAttr)) {
                            treeMap.clear();
                            drmInitData2 = null;
                            str5 = null;
                            str6 = null;
                        } else {
                            String parseOptionalStringAttr = parseOptionalStringAttr(next, REGEX_IV, hashMap);
                            if (!str8.equals(str7)) {
                                Object parsePlayReadySchemeData;
                                if (str4 == null) {
                                    str4 = ("SAMPLE-AES-CENC".equals(parseStringAttr) || "SAMPLE-AES-CTR".equals(parseStringAttr)) ? "cenc" : "cbcs";
                                }
                                if ("com.microsoft.playready".equals(str7)) {
                                    parsePlayReadySchemeData = parsePlayReadySchemeData(next, hashMap);
                                } else {
                                    parsePlayReadySchemeData = parseWidevineSchemeData(next, str7, hashMap);
                                }
                                if (parsePlayReadySchemeData != null) {
                                    treeMap.put(str7, parsePlayReadySchemeData);
                                    str6 = parseOptionalStringAttr;
                                    drmInitData2 = null;
                                    str5 = null;
                                }
                            } else if ("AES-128".equals(parseStringAttr)) {
                                str5 = parseStringAttr(next, REGEX_URI, hashMap);
                                str6 = parseOptionalStringAttr;
                            }
                            str6 = parseOptionalStringAttr;
                            str5 = null;
                        }
                    } else if (next.startsWith("#EXT-X-BYTERANGE")) {
                        split = parseStringAttr(next, REGEX_BYTERANGE, hashMap).split(str7);
                        j7 = Long.parseLong(split[i]);
                        if (split.length > i2) {
                            j5 = Long.parseLong(split[i2]);
                        }
                    } else if (next.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE")) {
                        i4 = Integer.parseInt(next.substring(next.indexOf(58) + i2));
                        z = true;
                    } else if (next.equals("#EXT-X-DISCONTINUITY")) {
                        i6++;
                    } else if (next.startsWith("#EXT-X-PROGRAM-DATE-TIME")) {
                        if (j3 == 0) {
                            j3 = C0131C.msToUs(Util.parseXsDateTime(next.substring(next.indexOf(58) + i2))) - j8;
                        }
                    } else if (next.equals("#EXT-X-GAP")) {
                        z4 = true;
                    } else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                        z2 = true;
                    } else if (next.equals("#EXT-X-ENDLIST")) {
                        z3 = true;
                    } else if (!next.startsWith("#")) {
                        TreeMap treeMap2;
                        DrmInitData drmInitData3;
                        String toHexString = str5 == null ? null : str6 != null ? str6 : Long.toHexString(j6);
                        parseDoubleAttr = j6 + 1;
                        if (j7 == -1) {
                            j5 = 0;
                        }
                        if (drmInitData2 != null || treeMap.isEmpty()) {
                            treeMap2 = treeMap;
                            drmInitData3 = drmInitData2;
                        } else {
                            SchemeData[] schemeDataArr = (SchemeData[]) treeMap.values().toArray(new SchemeData[i]);
                            drmInitData3 = new DrmInitData(str4, schemeDataArr);
                            if (drmInitData == null) {
                                SchemeData[] schemeDataArr2 = new SchemeData[schemeDataArr.length];
                                i2 = 0;
                                while (i2 < schemeDataArr.length) {
                                    treeMap2 = treeMap;
                                    schemeDataArr2[i2] = schemeDataArr[i2].copyWithData(null);
                                    i2++;
                                    treeMap = treeMap2;
                                }
                                treeMap2 = treeMap;
                                drmInitData = new DrmInitData(str4, schemeDataArr2);
                            } else {
                                treeMap2 = treeMap;
                            }
                        }
                        arrayList.add(new Segment(replaceVariableReferences(next, hashMap), segment, str3, j9, i6, j8, drmInitData3, str5, toHexString, j5, j7, z4));
                        j8 += j9;
                        if (j7 != -1) {
                            j5 += j7;
                        }
                        i2 = 1;
                        z4 = false;
                        j9 = 0;
                        hlsMasterPlaylist2 = hlsMasterPlaylist;
                        str3 = str2;
                        j6 = parseDoubleAttr;
                        drmInitData2 = drmInitData3;
                        j7 = -1;
                        treeMap = treeMap2;
                        i = 0;
                    }
                    i = 0;
                    i2 = 1;
                    hlsMasterPlaylist2 = hlsMasterPlaylist;
                    treeMap = treeMap;
                }
            }
        }
        return new HlsMediaPlaylist(i3, str, arrayList2, j, j3, z, i4, j4, i5, j2, z2, z3, j3 != 0, drmInitData, arrayList);
    }

    private static SchemeData parsePlayReadySchemeData(String str, Map<String, String> map) throws ParserException {
        String str2 = "1";
        if (!str2.equals(parseOptionalStringAttr(str, REGEX_KEYFORMATVERSIONS, str2, map))) {
            return null;
        }
        str = parseStringAttr(str, REGEX_URI, map);
        return new SchemeData(C0131C.PLAYREADY_UUID, MimeTypes.VIDEO_MP4, PsshAtomUtil.buildPsshAtom(C0131C.PLAYREADY_UUID, Base64.decode(str.substring(str.indexOf(44)), 0)));
    }

    private static SchemeData parseWidevineSchemeData(String str, String str2, Map<String, String> map) throws ParserException {
        if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(str2)) {
            str = parseStringAttr(str, REGEX_URI, map);
            return new SchemeData(C0131C.WIDEVINE_UUID, MimeTypes.VIDEO_MP4, Base64.decode(str.substring(str.indexOf(44)), 0));
        } else if (!"com.widevine".equals(str2)) {
            return null;
        } else {
            try {
                return new SchemeData(C0131C.WIDEVINE_UUID, "hls", str.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new ParserException(e);
            }
        }
    }

    private static int parseIntAttr(String str, Pattern pattern) throws ParserException {
        return Integer.parseInt(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static long parseLongAttr(String str, Pattern pattern) throws ParserException {
        return Long.parseLong(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static double parseDoubleAttr(String str, Pattern pattern) throws ParserException {
        return Double.parseDouble(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static String parseStringAttr(String str, Pattern pattern, Map<String, String> map) throws ParserException {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, pattern, map);
        if (parseOptionalStringAttr != null) {
            return parseOptionalStringAttr;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Couldn't match ");
        stringBuilder.append(pattern.pattern());
        stringBuilder.append(" in ");
        stringBuilder.append(str);
        throw new ParserException(stringBuilder.toString());
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, Map<String, String> map) {
        return parseOptionalStringAttr(str, pattern, null, map);
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, String str2, Map<String, String> map) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str2 = matcher.group(1);
        }
        return (map.isEmpty() || str2 == null) ? str2 : replaceVariableReferences(str2, map);
    }

    private static String replaceVariableReferences(String str, Map<String, String> map) {
        Matcher matcher = REGEX_VARIABLE_REFERENCE.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (map.containsKey(group)) {
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement((String) map.get(group)));
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private static boolean parseOptionalBooleanAttribute(String str, Pattern pattern, boolean z) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? matcher.group(1).equals("YES") : z;
    }

    private static Pattern compileBooleanAttrPattern(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("=(");
        stringBuilder.append("NO");
        stringBuilder.append("|");
        stringBuilder.append("YES");
        stringBuilder.append(")");
        return Pattern.compile(stringBuilder.toString());
    }

    private static boolean isMediaTagMuxed(List<HlsUrl> list, String str) {
        if (str == null) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(((HlsUrl) list.get(i)).url)) {
                return true;
            }
        }
        return false;
    }
}
