package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Xml;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentList;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTimelineElement;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class DashManifestParser extends DefaultHandler implements Parser<DashManifest> {
    private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
    private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
    private final XmlPullParserFactory xmlParserFactory;

    protected static final class RepresentationInfo {
        public final String baseUrl;
        public final ArrayList<SchemeData> drmSchemeDatas;
        public final String drmSchemeType;
        public final Format format;
        public final ArrayList<Descriptor> inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;

        public RepresentationInfo(Format format, String str, SegmentBase segmentBase, String str2, ArrayList<SchemeData> arrayList, ArrayList<Descriptor> arrayList2, long j) {
            this.format = format;
            this.baseUrl = str;
            this.segmentBase = segmentBase;
            this.drmSchemeType = str2;
            this.drmSchemeDatas = arrayList;
            this.inbandEventStreams = arrayList2;
            this.revisionId = j;
        }
    }

    public DashManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public DashManifest parse(Uri uri, InputStream inputStream) throws IOException {
        try {
            XmlPullParser newPullParser = this.xmlParserFactory.newPullParser();
            newPullParser.setInput(inputStream, null);
            if (newPullParser.next() == 2 && "MPD".equals(newPullParser.getName())) {
                return parseMediaPresentationDescription(newPullParser, uri.toString());
            }
            throw new ParserException("inputStream does not contain a valid media presentation description");
        } catch (XmlPullParserException e) {
            throw new ParserException(e);
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0171  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0158  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0179 A:{LOOP_END, LOOP:0: B:20:0x006c->B:67:0x0179} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0138 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0138 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0179 A:{LOOP_END, LOOP:0: B:20:0x006c->B:67:0x0179} */
    public com.google.android.exoplayer2.source.dash.manifest.DashManifest parseMediaPresentationDescription(org.xmlpull.v1.XmlPullParser r33, java.lang.String r34) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r32 = this;
        r0 = r33;
        r1 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r3 = "availabilityStartTime";
        r5 = parseDateTime(r0, r3, r1);
        r3 = "mediaPresentationDuration";
        r3 = parseDuration(r0, r3, r1);
        r7 = "minBufferTime";
        r9 = parseDuration(r0, r7, r1);
        r7 = 0;
        r8 = "type";
        r8 = r0.getAttributeValue(r7, r8);
        r12 = 0;
        if (r8 == 0) goto L_0x002d;
    L_0x0023:
        r13 = "dynamic";
        r8 = r13.equals(r8);
        if (r8 == 0) goto L_0x002d;
    L_0x002b:
        r13 = 1;
        goto L_0x002e;
    L_0x002d:
        r13 = 0;
    L_0x002e:
        if (r13 == 0) goto L_0x0037;
    L_0x0030:
        r8 = "minimumUpdatePeriod";
        r14 = parseDuration(r0, r8, r1);
        goto L_0x0038;
    L_0x0037:
        r14 = r1;
    L_0x0038:
        if (r13 == 0) goto L_0x0041;
    L_0x003a:
        r8 = "timeShiftBufferDepth";
        r16 = parseDuration(r0, r8, r1);
        goto L_0x0043;
    L_0x0041:
        r16 = r1;
    L_0x0043:
        if (r13 == 0) goto L_0x004c;
    L_0x0045:
        r8 = "suggestedPresentationDelay";
        r18 = parseDuration(r0, r8, r1);
        goto L_0x004e;
    L_0x004c:
        r18 = r1;
    L_0x004e:
        r8 = "publishTime";
        r20 = parseDateTime(r0, r8, r1);
        r8 = new java.util.ArrayList;
        r8.<init>();
        if (r13 == 0) goto L_0x005e;
    L_0x005b:
        r22 = r1;
        goto L_0x0060;
    L_0x005e:
        r22 = 0;
    L_0x0060:
        r25 = r7;
        r26 = r25;
        r27 = r26;
        r1 = r22;
        r22 = 0;
        r7 = r34;
    L_0x006c:
        r33.next();
        r11 = "BaseURL";
        r11 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r11);
        if (r11 == 0) goto L_0x008a;
    L_0x0077:
        if (r12 != 0) goto L_0x0082;
    L_0x0079:
        r7 = parseBaseUrl(r0, r7);
        r30 = r14;
        r12 = 1;
        goto L_0x0130;
    L_0x0082:
        r28 = r1;
        r34 = r12;
        r30 = r14;
        goto L_0x012c;
    L_0x008a:
        r11 = "ProgramInformation";
        r11 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r11);
        if (r11 == 0) goto L_0x009c;
    L_0x0092:
        r11 = r32.parseProgramInformation(r33);
        r25 = r11;
    L_0x0098:
        r30 = r14;
        goto L_0x0130;
    L_0x009c:
        r11 = "UTCTiming";
        r11 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r11);
        if (r11 == 0) goto L_0x00ab;
    L_0x00a4:
        r11 = r32.parseUtcTiming(r33);
        r26 = r11;
        goto L_0x0098;
    L_0x00ab:
        r11 = "Location";
        r11 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r11);
        if (r11 == 0) goto L_0x00be;
    L_0x00b3:
        r11 = r33.nextText();
        r11 = android.net.Uri.parse(r11);
        r27 = r11;
        goto L_0x0098;
    L_0x00be:
        r11 = "Period";
        r11 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r11);
        if (r11 == 0) goto L_0x0123;
    L_0x00c6:
        if (r22 != 0) goto L_0x0123;
    L_0x00c8:
        r11 = r32;
        r34 = r12;
        r12 = r11.parsePeriod(r0, r7, r1);
        r28 = r1;
        r1 = r12.first;
        r1 = (com.google.android.exoplayer2.source.dash.manifest.Period) r1;
        r30 = r14;
        r14 = r1.startMs;
        r23 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r2 = (r14 > r23 ? 1 : (r14 == r23 ? 0 : -1));
        if (r2 != 0) goto L_0x0103;
    L_0x00e3:
        if (r13 == 0) goto L_0x00e8;
    L_0x00e5:
        r22 = 1;
        goto L_0x012c;
    L_0x00e8:
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to determine start of period ";
        r1.append(r2);
        r2 = r8.size();
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0103:
        r2 = r12.second;
        r2 = (java.lang.Long) r2;
        r14 = r2.longValue();
        r23 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r2 = (r14 > r23 ? 1 : (r14 == r23 ? 0 : -1));
        if (r2 != 0) goto L_0x011a;
    L_0x0114:
        r11 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        goto L_0x011d;
    L_0x011a:
        r11 = r1.startMs;
        r11 = r11 + r14;
    L_0x011d:
        r8.add(r1);
        r28 = r11;
        goto L_0x012c;
    L_0x0123:
        r28 = r1;
        r34 = r12;
        r30 = r14;
        maybeSkipTag(r33);
    L_0x012c:
        r12 = r34;
        r1 = r28;
    L_0x0130:
        r11 = "MPD";
        r11 = com.google.android.exoplayer2.util.XmlPullParserUtil.isEndTag(r0, r11);
        if (r11 == 0) goto L_0x0179;
    L_0x0138:
        r14 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r0 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1));
        if (r0 != 0) goto L_0x0151;
    L_0x0141:
        r0 = (r1 > r14 ? 1 : (r1 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x0146;
    L_0x0145:
        goto L_0x0152;
    L_0x0146:
        if (r13 == 0) goto L_0x0149;
    L_0x0148:
        goto L_0x0151;
    L_0x0149:
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = "Unable to determine duration of static manifest.";
        r0.<init>(r1);
        throw r0;
    L_0x0151:
        r1 = r3;
    L_0x0152:
        r0 = r8.isEmpty();
        if (r0 != 0) goto L_0x0171;
    L_0x0158:
        r4 = r32;
        r23 = r8;
        r7 = r1;
        r11 = r13;
        r12 = r30;
        r14 = r16;
        r16 = r18;
        r18 = r20;
        r20 = r25;
        r21 = r26;
        r22 = r27;
        r0 = r4.buildMediaPresentationDescription(r5, r7, r9, r11, r12, r14, r16, r18, r20, r21, r22, r23);
        return r0;
    L_0x0171:
        r0 = new com.google.android.exoplayer2.ParserException;
        r1 = "No periods found.";
        r0.<init>(r1);
        throw r0;
    L_0x0179:
        r14 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r14 = r30;
        goto L_0x006c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseMediaPresentationDescription(org.xmlpull.v1.XmlPullParser, java.lang.String):com.google.android.exoplayer2.source.dash.manifest.DashManifest");
    }

    /* Access modifiers changed, original: protected */
    public DashManifest buildMediaPresentationDescription(long j, long j2, long j3, boolean z, long j4, long j5, long j6, long j7, ProgramInformation programInformation, UtcTimingElement utcTimingElement, Uri uri, List<Period> list) {
        return new DashManifest(j, j2, j3, z, j4, j5, j6, j7, programInformation, utcTimingElement, uri, list);
    }

    /* Access modifiers changed, original: protected */
    public UtcTimingElement parseUtcTiming(XmlPullParser xmlPullParser) {
        return buildUtcTimingElement(xmlPullParser.getAttributeValue(null, "schemeIdUri"), xmlPullParser.getAttributeValue(null, "value"));
    }

    /* Access modifiers changed, original: protected */
    public UtcTimingElement buildUtcTimingElement(String str, String str2) {
        return new UtcTimingElement(str, str2);
    }

    /* Access modifiers changed, original: protected */
    public Pair<Period, Long> parsePeriod(XmlPullParser xmlPullParser, String str, long j) throws XmlPullParserException, IOException {
        String attributeValue = xmlPullParser.getAttributeValue(null, "id");
        long parseDuration = parseDuration(xmlPullParser, "start", j);
        j = parseDuration(xmlPullParser, "duration", -9223372036854775807L);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Object obj = null;
        SegmentBase segmentBase = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (obj == null) {
                    str = parseBaseUrl(xmlPullParser, str);
                    obj = 1;
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AdaptationSet")) {
                arrayList.add(parseAdaptationSet(xmlPullParser, str, segmentBase));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "EventStream")) {
                arrayList2.add(parseEventStream(xmlPullParser));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                segmentBase = parseSegmentBase(xmlPullParser, null);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                segmentBase = parseSegmentList(xmlPullParser, null);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                segmentBase = parseSegmentTemplate(xmlPullParser, null);
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Period"));
        return Pair.create(buildPeriod(attributeValue, parseDuration, arrayList, arrayList2), Long.valueOf(j));
    }

    /* Access modifiers changed, original: protected */
    public Period buildPeriod(String str, long j, List<AdaptationSet> list, List<EventStream> list2) {
        return new Period(str, j, list, list2);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0230 A:{LOOP_END, LOOP:0: B:1:0x0069->B:59:0x0230} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0200 A:{SYNTHETIC, EDGE_INSN: B:60:0x0200->B:53:0x0200 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0200 A:{SYNTHETIC, EDGE_INSN: B:60:0x0200->B:53:0x0200 ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0230 A:{LOOP_END, LOOP:0: B:1:0x0069->B:59:0x0230} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0230 A:{LOOP_END, LOOP:0: B:1:0x0069->B:59:0x0230} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0200 A:{SYNTHETIC, EDGE_INSN: B:60:0x0200->B:53:0x0200 ?: BREAK  } */
    public com.google.android.exoplayer2.source.dash.manifest.AdaptationSet parseAdaptationSet(org.xmlpull.v1.XmlPullParser r41, java.lang.String r42, com.google.android.exoplayer2.source.dash.manifest.SegmentBase r43) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r40 = this;
        r15 = r40;
        r14 = r41;
        r0 = -1;
        r1 = "id";
        r16 = parseInt(r14, r1, r0);
        r1 = r40.parseContentType(r41);
        r13 = 0;
        r2 = "mimeType";
        r17 = r14.getAttributeValue(r13, r2);
        r2 = "codecs";
        r18 = r14.getAttributeValue(r13, r2);
        r2 = "width";
        r19 = parseInt(r14, r2, r0);
        r2 = "height";
        r20 = parseInt(r14, r2, r0);
        r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r21 = parseFrameRate(r14, r2);
        r2 = "audioSamplingRate";
        r22 = parseInt(r14, r2, r0);
        r12 = "lang";
        r2 = r14.getAttributeValue(r13, r12);
        r3 = "label";
        r23 = r14.getAttributeValue(r13, r3);
        r11 = new java.util.ArrayList;
        r11.<init>();
        r10 = new java.util.ArrayList;
        r10.<init>();
        r9 = new java.util.ArrayList;
        r9.<init>();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r7 = new java.util.ArrayList;
        r7.<init>();
        r24 = 0;
        r6 = r42;
        r28 = r43;
        r4 = r1;
        r5 = r2;
        r29 = r13;
        r25 = 0;
        r26 = 0;
        r27 = -1;
    L_0x0069:
        r41.next();
        r0 = "BaseURL";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r0 == 0) goto L_0x00a4;
    L_0x0074:
        if (r25 != 0) goto L_0x0090;
    L_0x0076:
        r0 = parseBaseUrl(r14, r6);
        r1 = 1;
        r6 = r0;
        r2 = r4;
        r3 = r7;
        r34 = r8;
        r35 = r9;
        r4 = r10;
        r37 = r11;
        r38 = r12;
        r39 = r13;
        r1 = r14;
        r0 = r29;
        r25 = 1;
        goto L_0x01f8;
    L_0x0090:
        r2 = r4;
        r31 = r5;
        r32 = r6;
        r3 = r7;
        r34 = r8;
        r35 = r9;
        r4 = r10;
        r37 = r11;
        r38 = r12;
        r39 = r13;
        r1 = r14;
        goto L_0x01f2;
    L_0x00a4:
        r0 = "ContentProtection";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r0 == 0) goto L_0x00c0;
    L_0x00ac:
        r0 = r40.parseContentProtection(r41);
        r1 = r0.first;
        if (r1 == 0) goto L_0x00b8;
    L_0x00b4:
        r29 = r1;
        r29 = (java.lang.String) r29;
    L_0x00b8:
        r0 = r0.second;
        if (r0 == 0) goto L_0x00f8;
    L_0x00bc:
        r11.add(r0);
        goto L_0x00f8;
    L_0x00c0:
        r0 = "ContentComponent";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r0 == 0) goto L_0x00ea;
    L_0x00c8:
        r0 = r14.getAttributeValue(r13, r12);
        r5 = checkLanguageConsistency(r5, r0);
        r0 = r40.parseContentType(r41);
        r0 = checkContentTypeConsistency(r4, r0);
        r2 = r0;
    L_0x00d9:
        r3 = r7;
        r34 = r8;
        r35 = r9;
        r4 = r10;
        r37 = r11;
        r38 = r12;
        r39 = r13;
        r1 = r14;
        r0 = r29;
        goto L_0x01f8;
    L_0x00ea:
        r0 = "Role";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r0 == 0) goto L_0x00fa;
    L_0x00f2:
        r0 = r40.parseRole(r41);
        r26 = r26 | r0;
    L_0x00f8:
        r2 = r4;
        goto L_0x00d9;
    L_0x00fa:
        r0 = "AudioChannelConfiguration";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r0 == 0) goto L_0x0107;
    L_0x0102:
        r27 = r40.parseAudioChannelConfiguration(r41);
        goto L_0x00f8;
    L_0x0107:
        r0 = "Accessibility";
        r1 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r1 == 0) goto L_0x0118;
    L_0x010f:
        r0 = parseDescriptor(r14, r0);
        r9.add(r0);
        goto L_0x0090;
    L_0x0118:
        r0 = "SupplementalProperty";
        r1 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r1 == 0) goto L_0x0129;
    L_0x0120:
        r0 = parseDescriptor(r14, r0);
        r8.add(r0);
        goto L_0x0090;
    L_0x0129:
        r0 = "Representation";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r14, r0);
        if (r0 == 0) goto L_0x0184;
    L_0x0131:
        r0 = r40;
        r1 = r41;
        r2 = r6;
        r3 = r23;
        r30 = r4;
        r4 = r17;
        r31 = r5;
        r5 = r18;
        r32 = r6;
        r6 = r19;
        r33 = r7;
        r7 = r20;
        r34 = r8;
        r8 = r21;
        r35 = r9;
        r9 = r27;
        r36 = r10;
        r10 = r22;
        r37 = r11;
        r11 = r31;
        r38 = r12;
        r12 = r26;
        r39 = r13;
        r13 = r35;
        r14 = r28;
        r0 = r0.parseRepresentation(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14);
        r1 = r0.format;
        r1 = r15.getContentType(r1);
        r2 = r30;
        r1 = checkContentTypeConsistency(r2, r1);
        r3 = r33;
        r3.add(r0);
        r2 = r1;
        r0 = r29;
        r5 = r31;
        r6 = r32;
        r4 = r36;
        r1 = r41;
        goto L_0x01f8;
    L_0x0184:
        r2 = r4;
        r31 = r5;
        r32 = r6;
        r3 = r7;
        r34 = r8;
        r35 = r9;
        r36 = r10;
        r37 = r11;
        r38 = r12;
        r39 = r13;
        r0 = "SegmentBase";
        r1 = r41;
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r1, r0);
        if (r0 == 0) goto L_0x01b3;
    L_0x01a0:
        r0 = r28;
        r0 = (com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase) r0;
        r0 = r15.parseSegmentBase(r1, r0);
    L_0x01a8:
        r28 = r0;
        r0 = r29;
        r5 = r31;
        r6 = r32;
        r4 = r36;
        goto L_0x01f8;
    L_0x01b3:
        r0 = "SegmentList";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r1, r0);
        if (r0 == 0) goto L_0x01c4;
    L_0x01bb:
        r0 = r28;
        r0 = (com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentList) r0;
        r0 = r15.parseSegmentList(r1, r0);
        goto L_0x01a8;
    L_0x01c4:
        r0 = "SegmentTemplate";
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r1, r0);
        if (r0 == 0) goto L_0x01d5;
    L_0x01cc:
        r0 = r28;
        r0 = (com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate) r0;
        r0 = r15.parseSegmentTemplate(r1, r0);
        goto L_0x01a8;
    L_0x01d5:
        r0 = "InbandEventStream";
        r4 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r1, r0);
        if (r4 == 0) goto L_0x01e7;
    L_0x01dd:
        r0 = parseDescriptor(r1, r0);
        r4 = r36;
        r4.add(r0);
        goto L_0x01f2;
    L_0x01e7:
        r4 = r36;
        r0 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r41);
        if (r0 == 0) goto L_0x01f2;
    L_0x01ef:
        r40.parseAdaptationSetChild(r41);
    L_0x01f2:
        r0 = r29;
        r5 = r31;
        r6 = r32;
    L_0x01f8:
        r7 = "AdaptationSet";
        r7 = com.google.android.exoplayer2.util.XmlPullParserUtil.isEndTag(r1, r7);
        if (r7 == 0) goto L_0x0230;
    L_0x0200:
        r5 = new java.util.ArrayList;
        r1 = r3.size();
        r5.<init>(r1);
        r1 = 0;
    L_0x020a:
        r6 = r3.size();
        if (r1 >= r6) goto L_0x0222;
    L_0x0210:
        r6 = r3.get(r1);
        r6 = (com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.RepresentationInfo) r6;
        r7 = r37;
        r6 = r15.buildRepresentation(r6, r0, r7, r4);
        r5.add(r6);
        r1 = r1 + 1;
        goto L_0x020a;
    L_0x0222:
        r0 = r40;
        r1 = r16;
        r3 = r5;
        r4 = r35;
        r5 = r34;
        r0 = r0.buildAdaptationSet(r1, r2, r3, r4, r5);
        return r0;
    L_0x0230:
        r29 = r0;
        r14 = r1;
        r7 = r3;
        r10 = r4;
        r8 = r34;
        r9 = r35;
        r11 = r37;
        r12 = r38;
        r13 = r39;
        r4 = r2;
        goto L_0x0069;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseAdaptationSet(org.xmlpull.v1.XmlPullParser, java.lang.String, com.google.android.exoplayer2.source.dash.manifest.SegmentBase):com.google.android.exoplayer2.source.dash.manifest.AdaptationSet");
    }

    /* Access modifiers changed, original: protected */
    public AdaptationSet buildAdaptationSet(int i, int i2, List<Representation> list, List<Descriptor> list2, List<Descriptor> list3) {
        return new AdaptationSet(i, i2, list, list2, list3);
    }

    /* Access modifiers changed, original: protected */
    public int parseContentType(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(attributeValue)) {
            return -1;
        }
        if (MimeTypes.BASE_TYPE_AUDIO.equals(attributeValue)) {
            return 1;
        }
        if (MimeTypes.BASE_TYPE_VIDEO.equals(attributeValue)) {
            return 2;
        }
        if (MimeTypes.BASE_TYPE_TEXT.equals(attributeValue)) {
            return 3;
        }
        return -1;
    }

    /* Access modifiers changed, original: protected */
    public int getContentType(Format format) {
        String str = format.sampleMimeType;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (MimeTypes.isVideo(str)) {
            return 2;
        }
        if (MimeTypes.isAudio(str)) {
            return 1;
        }
        if (mimeTypeIsRawText(str)) {
            return 3;
        }
        return -1;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x013f A:{LOOP_END, LOOP:1: B:35:0x009d->B:75:0x013f} */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x012e A:{SYNTHETIC, EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x012e A:{SYNTHETIC, EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  , EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x013f A:{LOOP_END, LOOP:1: B:35:0x009d->B:75:0x013f} */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x013f A:{LOOP_END, LOOP:1: B:35:0x009d->B:75:0x013f} */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x012e A:{SYNTHETIC, EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  , EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  , EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x012e A:{SYNTHETIC, EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  , EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  , EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  , EDGE_INSN: B:77:0x012e->B:70:0x012e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x013f A:{LOOP_END, LOOP:1: B:35:0x009d->B:75:0x013f} */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0130  */
    public android.util.Pair<java.lang.String, com.google.android.exoplayer2.drm.DrmInitData.SchemeData> parseContentProtection(org.xmlpull.v1.XmlPullParser r17) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r16 = this;
        r0 = r17;
        r1 = 0;
        r2 = "schemeIdUri";
        r2 = r0.getAttributeValue(r1, r2);
        r3 = 1;
        r4 = 0;
        if (r2 == 0) goto L_0x0098;
    L_0x000d:
        r2 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r2);
        r5 = -1;
        r6 = r2.hashCode();
        r7 = 489446379; // 0x1d2c5beb float:2.281153E-21 double:2.418186413E-315;
        r8 = 2;
        if (r6 == r7) goto L_0x003b;
    L_0x001c:
        r7 = 755418770; // 0x2d06c692 float:7.66111E-12 double:3.732264625E-315;
        if (r6 == r7) goto L_0x0031;
    L_0x0021:
        r7 = 1812765994; // 0x6c0c9d2a float:6.799672E26 double:8.956254016E-315;
        if (r6 == r7) goto L_0x0027;
    L_0x0026:
        goto L_0x0044;
    L_0x0027:
        r6 = "urn:mpeg:dash:mp4protection:2011";
        r2 = r2.equals(r6);
        if (r2 == 0) goto L_0x0044;
    L_0x002f:
        r5 = 0;
        goto L_0x0044;
    L_0x0031:
        r6 = "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed";
        r2 = r2.equals(r6);
        if (r2 == 0) goto L_0x0044;
    L_0x0039:
        r5 = 2;
        goto L_0x0044;
    L_0x003b:
        r6 = "urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95";
        r2 = r2.equals(r6);
        if (r2 == 0) goto L_0x0044;
    L_0x0043:
        r5 = 1;
    L_0x0044:
        if (r5 == 0) goto L_0x0052;
    L_0x0046:
        if (r5 == r3) goto L_0x004e;
    L_0x0048:
        if (r5 == r8) goto L_0x004b;
    L_0x004a:
        goto L_0x0098;
    L_0x004b:
        r2 = com.google.android.exoplayer2.C0131C.WIDEVINE_UUID;
        goto L_0x0050;
    L_0x004e:
        r2 = com.google.android.exoplayer2.C0131C.PLAYREADY_UUID;
    L_0x0050:
        r5 = r1;
        goto L_0x009a;
    L_0x0052:
        r2 = "value";
        r2 = r0.getAttributeValue(r1, r2);
        r5 = "default_KID";
        r5 = com.google.android.exoplayer2.util.XmlPullParserUtil.getAttributeValueIgnorePrefix(r0, r5);
        r6 = android.text.TextUtils.isEmpty(r5);
        if (r6 != 0) goto L_0x0092;
    L_0x0064:
        r6 = "00000000-0000-0000-0000-000000000000";
        r6 = r6.equals(r5);
        if (r6 != 0) goto L_0x0092;
    L_0x006c:
        r6 = "\\s+";
        r5 = r5.split(r6);
        r6 = r5.length;
        r6 = new java.util.UUID[r6];
        r7 = 0;
    L_0x0076:
        r8 = r5.length;
        if (r7 >= r8) goto L_0x0084;
    L_0x0079:
        r8 = r5[r7];
        r8 = java.util.UUID.fromString(r8);
        r6[r7] = r8;
        r7 = r7 + 1;
        goto L_0x0076;
    L_0x0084:
        r5 = com.google.android.exoplayer2.C0131C.COMMON_PSSH_UUID;
        r5 = com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil.buildPsshAtom(r5, r6, r1);
        r6 = com.google.android.exoplayer2.C0131C.COMMON_PSSH_UUID;
        r7 = r1;
        r8 = 0;
        r15 = r6;
        r6 = r2;
        r2 = r15;
        goto L_0x009d;
    L_0x0092:
        r5 = r1;
        r7 = r5;
        r6 = r2;
        r8 = 0;
        r2 = r7;
        goto L_0x009d;
    L_0x0098:
        r2 = r1;
        r5 = r2;
    L_0x009a:
        r6 = r5;
        r7 = r6;
        r8 = 0;
    L_0x009d:
        r17.next();
        r9 = "ms:laurl";
        r9 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r9);
        if (r9 == 0) goto L_0x00b4;
    L_0x00a8:
        r7 = "licenseUrl";
        r7 = r0.getAttributeValue(r1, r7);
    L_0x00ae:
        r10 = r2;
        r13 = r5;
    L_0x00b0:
        r11 = r7;
        r14 = r8;
        goto L_0x0126;
    L_0x00b4:
        r9 = "widevine:license";
        r9 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r9);
        if (r9 == 0) goto L_0x00d0;
    L_0x00bc:
        r8 = "robustness_level";
        r8 = r0.getAttributeValue(r1, r8);
        if (r8 == 0) goto L_0x00ce;
    L_0x00c4:
        r9 = "HW";
        r8 = r8.startsWith(r9);
        if (r8 == 0) goto L_0x00ce;
    L_0x00cc:
        r8 = 1;
        goto L_0x00ae;
    L_0x00ce:
        r8 = 0;
        goto L_0x00ae;
    L_0x00d0:
        r9 = 4;
        if (r5 != 0) goto L_0x00fb;
    L_0x00d3:
        r10 = "pssh";
        r10 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTagIgnorePrefix(r0, r10);
        if (r10 == 0) goto L_0x00fb;
    L_0x00db:
        r10 = r17.next();
        if (r10 != r9) goto L_0x00fb;
    L_0x00e1:
        r2 = r17.getText();
        r2 = android.util.Base64.decode(r2, r4);
        r5 = com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil.parseUuid(r2);
        if (r5 != 0) goto L_0x00f8;
    L_0x00ef:
        r2 = "MpdParser";
        r9 = "Skipping malformed cenc:pssh data";
        com.google.android.exoplayer2.util.Log.m18w(r2, r9);
        r13 = r1;
        goto L_0x00f9;
    L_0x00f8:
        r13 = r2;
    L_0x00f9:
        r10 = r5;
        goto L_0x00b0;
    L_0x00fb:
        if (r5 != 0) goto L_0x0122;
    L_0x00fd:
        r10 = com.google.android.exoplayer2.C0131C.PLAYREADY_UUID;
        r10 = r10.equals(r2);
        if (r10 == 0) goto L_0x0122;
    L_0x0105:
        r10 = "mspr:pro";
        r10 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r10);
        if (r10 == 0) goto L_0x0122;
    L_0x010d:
        r10 = r17.next();
        if (r10 != r9) goto L_0x0122;
    L_0x0113:
        r5 = com.google.android.exoplayer2.C0131C.PLAYREADY_UUID;
        r9 = r17.getText();
        r9 = android.util.Base64.decode(r9, r4);
        r5 = com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil.buildPsshAtom(r5, r9);
        goto L_0x00ae;
    L_0x0122:
        maybeSkipTag(r17);
        goto L_0x00ae;
    L_0x0126:
        r2 = "ContentProtection";
        r2 = com.google.android.exoplayer2.util.XmlPullParserUtil.isEndTag(r0, r2);
        if (r2 == 0) goto L_0x013f;
    L_0x012e:
        if (r10 == 0) goto L_0x0139;
    L_0x0130:
        r0 = new com.google.android.exoplayer2.drm.DrmInitData$SchemeData;
        r12 = "video/mp4";
        r9 = r0;
        r9.<init>(r10, r11, r12, r13, r14);
        goto L_0x013a;
    L_0x0139:
        r0 = r1;
    L_0x013a:
        r0 = android.util.Pair.create(r6, r0);
        return r0;
    L_0x013f:
        r2 = r10;
        r7 = r11;
        r5 = r13;
        r8 = r14;
        goto L_0x009d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseContentProtection(org.xmlpull.v1.XmlPullParser):android.util.Pair");
    }

    /* Access modifiers changed, original: protected */
    public int parseRole(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", null);
        String parseString2 = parseString(xmlPullParser, "value", null);
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Role"));
        return ("urn:mpeg:dash:role:2011".equals(parseString) && "main".equals(parseString2)) ? 1 : 0;
    }

    /* Access modifiers changed, original: protected */
    public void parseAdaptationSetChild(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        maybeSkipTag(xmlPullParser);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0146 A:{LOOP_END, LOOP:0: B:1:0x0058->B:43:0x0146} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x010a A:{SYNTHETIC, EDGE_INSN: B:44:0x010a->B:37:0x010a ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x010a A:{SYNTHETIC, EDGE_INSN: B:44:0x010a->B:37:0x010a ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0146 A:{LOOP_END, LOOP:0: B:1:0x0058->B:43:0x0146} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0146 A:{LOOP_END, LOOP:0: B:1:0x0058->B:43:0x0146} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x010a A:{SYNTHETIC, EDGE_INSN: B:44:0x010a->B:37:0x010a ?: BREAK  } */
    public com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.RepresentationInfo parseRepresentation(org.xmlpull.v1.XmlPullParser r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, int r28, int r29, float r30, int r31, int r32, java.lang.String r33, int r34, java.util.List<com.google.android.exoplayer2.source.dash.manifest.Descriptor> r35, com.google.android.exoplayer2.source.dash.manifest.SegmentBase r36) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r22 = this;
        r15 = r22;
        r0 = r23;
        r1 = 0;
        r2 = "id";
        r2 = r0.getAttributeValue(r1, r2);
        r3 = "bandwidth";
        r4 = -1;
        r9 = parseInt(r0, r3, r4);
        r3 = "mimeType";
        r4 = r26;
        r3 = parseString(r0, r3, r4);
        r4 = "codecs";
        r5 = r27;
        r13 = parseString(r0, r4, r5);
        r4 = "width";
        r5 = r28;
        r4 = parseInt(r0, r4, r5);
        r5 = "height";
        r6 = r29;
        r5 = parseInt(r0, r5, r6);
        r6 = r30;
        r6 = parseFrameRate(r0, r6);
        r7 = "audioSamplingRate";
        r8 = r32;
        r8 = parseInt(r0, r7, r8);
        r14 = new java.util.ArrayList;
        r14.<init>();
        r12 = new java.util.ArrayList;
        r12.<init>();
        r11 = new java.util.ArrayList;
        r11.<init>();
        r7 = 0;
        r16 = r31;
        r10 = r36;
        r17 = r1;
        r1 = r24;
    L_0x0058:
        r23.next();
        r26 = r13;
        r13 = "BaseURL";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r13);
        if (r13 == 0) goto L_0x0078;
    L_0x0065:
        if (r7 != 0) goto L_0x0074;
    L_0x0067:
        r1 = parseBaseUrl(r0, r1);
        r7 = 1;
    L_0x006c:
        r13 = r16;
        r18 = r17;
        r16 = r1;
        goto L_0x0100;
    L_0x0074:
        r24 = r1;
        goto L_0x00fa;
    L_0x0078:
        r13 = "AudioChannelConfiguration";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r13);
        if (r13 == 0) goto L_0x008a;
    L_0x0080:
        r13 = r22.parseAudioChannelConfiguration(r23);
        r16 = r1;
        r18 = r17;
        goto L_0x0100;
    L_0x008a:
        r13 = "SegmentBase";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r13);
        if (r13 == 0) goto L_0x0099;
    L_0x0092:
        r10 = (com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase) r10;
        r10 = r15.parseSegmentBase(r0, r10);
        goto L_0x006c;
    L_0x0099:
        r13 = "SegmentList";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r13);
        if (r13 == 0) goto L_0x00a8;
    L_0x00a1:
        r10 = (com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentList) r10;
        r10 = r15.parseSegmentList(r0, r10);
        goto L_0x006c;
    L_0x00a8:
        r13 = "SegmentTemplate";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r13);
        if (r13 == 0) goto L_0x00b7;
    L_0x00b0:
        r10 = (com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate) r10;
        r10 = r15.parseSegmentTemplate(r0, r10);
        goto L_0x006c;
    L_0x00b7:
        r13 = "ContentProtection";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r13);
        if (r13 == 0) goto L_0x00d5;
    L_0x00bf:
        r13 = r22.parseContentProtection(r23);
        r24 = r1;
        r1 = r13.first;
        if (r1 == 0) goto L_0x00cd;
    L_0x00c9:
        r17 = r1;
        r17 = (java.lang.String) r17;
    L_0x00cd:
        r1 = r13.second;
        if (r1 == 0) goto L_0x00fa;
    L_0x00d1:
        r14.add(r1);
        goto L_0x00fa;
    L_0x00d5:
        r24 = r1;
        r1 = "InbandEventStream";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r1);
        if (r13 == 0) goto L_0x00e7;
    L_0x00df:
        r1 = parseDescriptor(r0, r1);
        r12.add(r1);
        goto L_0x00fa;
    L_0x00e7:
        r1 = "SupplementalProperty";
        r13 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r0, r1);
        if (r13 == 0) goto L_0x00f7;
    L_0x00ef:
        r1 = parseDescriptor(r0, r1);
        r11.add(r1);
        goto L_0x00fa;
    L_0x00f7:
        maybeSkipTag(r23);
    L_0x00fa:
        r13 = r16;
        r18 = r17;
        r16 = r24;
    L_0x0100:
        r17 = r10;
        r1 = "Representation";
        r1 = com.google.android.exoplayer2.util.XmlPullParserUtil.isEndTag(r0, r1);
        if (r1 == 0) goto L_0x0146;
    L_0x010a:
        r0 = r22;
        r1 = r2;
        r2 = r25;
        r7 = r13;
        r10 = r33;
        r19 = r11;
        r11 = r34;
        r20 = r12;
        r12 = r35;
        r13 = r26;
        r21 = r14;
        r14 = r19;
        r0 = r0.buildFormat(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14);
        if (r17 == 0) goto L_0x0129;
    L_0x0126:
        r1 = r17;
        goto L_0x012e;
    L_0x0129:
        r1 = new com.google.android.exoplayer2.source.dash.manifest.SegmentBase$SingleSegmentBase;
        r1.<init>();
    L_0x012e:
        r2 = new com.google.android.exoplayer2.source.dash.manifest.DashManifestParser$RepresentationInfo;
        r3 = -1;
        r23 = r2;
        r24 = r0;
        r25 = r16;
        r26 = r1;
        r27 = r18;
        r28 = r21;
        r29 = r20;
        r30 = r3;
        r23.<init>(r24, r25, r26, r27, r28, r29, r30);
        return r2;
    L_0x0146:
        r1 = r16;
        r10 = r17;
        r17 = r18;
        r16 = r13;
        r13 = r26;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseRepresentation(org.xmlpull.v1.XmlPullParser, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, float, int, int, java.lang.String, int, java.util.List, com.google.android.exoplayer2.source.dash.manifest.SegmentBase):com.google.android.exoplayer2.source.dash.manifest.DashManifestParser$RepresentationInfo");
    }

    /* Access modifiers changed, original: protected */
    public Format buildFormat(String str, String str2, String str3, int i, int i2, float f, int i3, int i4, int i5, String str4, int i6, List<Descriptor> list, String str5, List<Descriptor> list2) {
        String str6;
        String str7 = str3;
        String sampleMimeType = getSampleMimeType(str3, str5);
        if (sampleMimeType != null) {
            if (MimeTypes.AUDIO_E_AC3.equals(sampleMimeType)) {
                sampleMimeType = parseEac3SupplementalProperties(list2);
            }
            str6 = sampleMimeType;
            if (MimeTypes.isVideo(str6)) {
                return Format.createVideoContainerFormat(str, str2, str3, str6, str5, i5, i, i2, f, null, i6);
            }
            if (MimeTypes.isAudio(str6)) {
                return Format.createAudioContainerFormat(str, str2, str3, str6, str5, i5, i3, i4, null, i6, str4);
            }
            if (mimeTypeIsRawText(str6)) {
                int parseCea608AccessibilityChannel;
                int i7;
                if (MimeTypes.APPLICATION_CEA608.equals(str6)) {
                    parseCea608AccessibilityChannel = parseCea608AccessibilityChannel(list);
                } else if (MimeTypes.APPLICATION_CEA708.equals(str6)) {
                    parseCea608AccessibilityChannel = parseCea708AccessibilityChannel(list);
                } else {
                    i7 = -1;
                    return Format.createTextContainerFormat(str, str2, str3, str6, str5, i5, i6, str4, i7);
                }
                i7 = parseCea608AccessibilityChannel;
                return Format.createTextContainerFormat(str, str2, str3, str6, str5, i5, i6, str4, i7);
            }
        }
        str6 = sampleMimeType;
        return Format.createContainerFormat(str, str2, str3, str6, str5, i5, i6, str4);
    }

    /* Access modifiers changed, original: protected */
    public Representation buildRepresentation(RepresentationInfo representationInfo, String str, ArrayList<SchemeData> arrayList, ArrayList<Descriptor> arrayList2) {
        Format format = representationInfo.format;
        String str2 = representationInfo.drmSchemeType;
        if (str2 != null) {
            str = str2;
        }
        List list = representationInfo.drmSchemeDatas;
        list.addAll(arrayList);
        if (!list.isEmpty()) {
            filterRedundantIncompleteSchemeDatas(list);
            format = format.copyWithDrmInitData(new DrmInitData(str, list));
        }
        Format format2 = format;
        ArrayList arrayList3 = representationInfo.inbandEventStreams;
        arrayList3.addAll(arrayList2);
        return Representation.newInstance(representationInfo.revisionId, format2, representationInfo.baseUrl, representationInfo.segmentBase, arrayList3);
    }

    /* Access modifiers changed, original: protected */
    public SingleSegmentBase parseSegmentBase(XmlPullParser xmlPullParser, SingleSegmentBase singleSegmentBase) throws XmlPullParserException, IOException {
        long parseLong;
        long j;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        SegmentBase segmentBase = singleSegmentBase;
        long parseLong2 = parseLong(xmlPullParser2, "timescale", segmentBase != null ? segmentBase.timescale : 1);
        long j2 = 0;
        long parseLong3 = parseLong(xmlPullParser2, "presentationTimeOffset", segmentBase != null ? segmentBase.presentationTimeOffset : 0);
        long j3 = segmentBase != null ? segmentBase.indexStart : 0;
        if (segmentBase != null) {
            j2 = segmentBase.indexLength;
        }
        String str = null;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "indexRange");
        if (attributeValue != null) {
            String[] split = attributeValue.split("-");
            long parseLong4 = Long.parseLong(split[0]);
            parseLong = (Long.parseLong(split[1]) - parseLong4) + 1;
            j = parseLong4;
        } else {
            parseLong = j2;
            j = j3;
        }
        if (segmentBase != null) {
            str = segmentBase.initialization;
        }
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Initialization")) {
                str = parseInitialization(xmlPullParser);
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser2, "SegmentBase"));
        return buildSingleSegmentBase(str, parseLong2, parseLong3, j, parseLong);
    }

    /* Access modifiers changed, original: protected */
    public SingleSegmentBase buildSingleSegmentBase(RangedUri rangedUri, long j, long j2, long j3, long j4) {
        return new SingleSegmentBase(rangedUri, j, j2, j3, j4);
    }

    /* Access modifiers changed, original: protected */
    public SegmentList parseSegmentList(XmlPullParser xmlPullParser, SegmentList segmentList) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        SegmentBase segmentBase = segmentList;
        long j = 1;
        long parseLong = parseLong(xmlPullParser2, "timescale", segmentBase != null ? segmentBase.timescale : 1);
        long parseLong2 = parseLong(xmlPullParser2, "presentationTimeOffset", segmentBase != null ? segmentBase.presentationTimeOffset : 0);
        long parseLong3 = parseLong(xmlPullParser2, "duration", segmentBase != null ? segmentBase.duration : -9223372036854775807L);
        if (segmentBase != null) {
            j = segmentBase.startNumber;
        }
        long parseLong4 = parseLong(xmlPullParser2, "startNumber", j);
        List list = null;
        RangedUri rangedUri = null;
        List list2 = rangedUri;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTimeline")) {
                list2 = parseSegmentTimeline(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentURL")) {
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(parseSegmentUrl(xmlPullParser));
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser2, "SegmentList"));
        if (segmentBase != null) {
            if (rangedUri == null) {
                rangedUri = segmentBase.initialization;
            }
            if (list2 == null) {
                list2 = segmentBase.segmentTimeline;
            }
            if (list == null) {
                list = segmentBase.mediaSegments;
            }
        }
        return buildSegmentList(rangedUri, parseLong, parseLong2, parseLong4, parseLong3, list2, list);
    }

    /* Access modifiers changed, original: protected */
    public SegmentList buildSegmentList(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list, List<RangedUri> list2) {
        return new SegmentList(rangedUri, j, j2, j3, j4, list, list2);
    }

    /* Access modifiers changed, original: protected */
    public SegmentTemplate parseSegmentTemplate(XmlPullParser xmlPullParser, SegmentTemplate segmentTemplate) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        SegmentBase segmentBase = segmentTemplate;
        long j = 1;
        long parseLong = parseLong(xmlPullParser2, "timescale", segmentBase != null ? segmentBase.timescale : 1);
        long parseLong2 = parseLong(xmlPullParser2, "presentationTimeOffset", segmentBase != null ? segmentBase.presentationTimeOffset : 0);
        long parseLong3 = parseLong(xmlPullParser2, "duration", segmentBase != null ? segmentBase.duration : -9223372036854775807L);
        if (segmentBase != null) {
            j = segmentBase.startNumber;
        }
        long parseLong4 = parseLong(xmlPullParser2, "startNumber", j);
        RangedUri rangedUri = null;
        UrlTemplate parseUrlTemplate = parseUrlTemplate(xmlPullParser2, "media", segmentBase != null ? segmentBase.mediaTemplate : null);
        UrlTemplate parseUrlTemplate2 = parseUrlTemplate(xmlPullParser2, "initialization", segmentBase != null ? segmentBase.initializationTemplate : null);
        List list = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTimeline")) {
                list = parseSegmentTimeline(xmlPullParser);
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser2, "SegmentTemplate"));
        if (segmentBase != null) {
            if (rangedUri == null) {
                rangedUri = segmentBase.initialization;
            }
            if (list == null) {
                list = segmentBase.segmentTimeline;
            }
        }
        return buildSegmentTemplate(rangedUri, parseLong, parseLong2, parseLong4, parseLong3, list, parseUrlTemplate2, parseUrlTemplate);
    }

    /* Access modifiers changed, original: protected */
    public SegmentTemplate buildSegmentTemplate(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list, UrlTemplate urlTemplate, UrlTemplate urlTemplate2) {
        return new SegmentTemplate(rangedUri, j, j2, j3, j4, list, urlTemplate, urlTemplate2);
    }

    /* Access modifiers changed, original: protected */
    public EventStream parseEventStream(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String str = "";
        String parseString = parseString(xmlPullParser, "schemeIdUri", str);
        str = parseString(xmlPullParser, "value", str);
        long parseLong = parseLong(xmlPullParser, "timescale", 1);
        ArrayList arrayList = new ArrayList();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Event")) {
                arrayList.add(parseEvent(xmlPullParser, parseString, str, parseLong, byteArrayOutputStream));
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "EventStream"));
        long[] jArr = new long[arrayList.size()];
        EventMessage[] eventMessageArr = new EventMessage[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            EventMessage eventMessage = (EventMessage) arrayList.get(i);
            jArr[i] = eventMessage.presentationTimeUs;
            eventMessageArr[i] = eventMessage;
        }
        return buildEventStream(parseString, str, parseLong, jArr, eventMessageArr);
    }

    /* Access modifiers changed, original: protected */
    public EventStream buildEventStream(String str, String str2, long j, long[] jArr, EventMessage[] eventMessageArr) {
        return new EventStream(str, str2, j, jArr, eventMessageArr);
    }

    /* Access modifiers changed, original: protected */
    public EventMessage parseEvent(XmlPullParser xmlPullParser, String str, String str2, long j, ByteArrayOutputStream byteArrayOutputStream) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        long parseLong = parseLong(xmlPullParser2, "id", 0);
        long parseLong2 = parseLong(xmlPullParser2, "duration", -9223372036854775807L);
        long parseLong3 = parseLong(xmlPullParser2, "presentationTime", 0);
        parseLong2 = Util.scaleLargeTimestamp(parseLong2, 1000, j);
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(parseLong3, 1000000, j);
        String parseString = parseString(xmlPullParser2, "messageData", null);
        byte[] parseEventObject = parseEventObject(xmlPullParser2, byteArrayOutputStream);
        if (parseString != null) {
            parseEventObject = Util.getUtf8Bytes(parseString);
        }
        return buildEvent(str, str2, parseLong, parseLong2, parseEventObject, scaleLargeTimestamp);
    }

    /* Access modifiers changed, original: protected */
    public byte[] parseEventObject(XmlPullParser xmlPullParser, ByteArrayOutputStream byteArrayOutputStream) throws XmlPullParserException, IOException {
        byteArrayOutputStream.reset();
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(byteArrayOutputStream, "UTF-8");
        xmlPullParser.nextToken();
        while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Event")) {
            int i = 0;
            switch (xmlPullParser.getEventType()) {
                case 0:
                    newSerializer.startDocument(null, Boolean.valueOf(false));
                    break;
                case 1:
                    newSerializer.endDocument();
                    break;
                case 2:
                    newSerializer.startTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    while (i < xmlPullParser.getAttributeCount()) {
                        newSerializer.attribute(xmlPullParser.getAttributeNamespace(i), xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
                        i++;
                    }
                    break;
                case 3:
                    newSerializer.endTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    break;
                case 4:
                    newSerializer.text(xmlPullParser.getText());
                    break;
                case 5:
                    newSerializer.cdsect(xmlPullParser.getText());
                    break;
                case 6:
                    newSerializer.entityRef(xmlPullParser.getText());
                    break;
                case 7:
                    newSerializer.ignorableWhitespace(xmlPullParser.getText());
                    break;
                case 8:
                    newSerializer.processingInstruction(xmlPullParser.getText());
                    break;
                case 9:
                    newSerializer.comment(xmlPullParser.getText());
                    break;
                case 10:
                    newSerializer.docdecl(xmlPullParser.getText());
                    break;
                default:
                    break;
            }
            xmlPullParser.nextToken();
        }
        newSerializer.flush();
        return byteArrayOutputStream.toByteArray();
    }

    /* Access modifiers changed, original: protected */
    public EventMessage buildEvent(String str, String str2, long j, long j2, byte[] bArr, long j3) {
        return new EventMessage(str, str2, j2, j, bArr, j3);
    }

    /* Access modifiers changed, original: protected */
    public List<SegmentTimelineElement> parseSegmentTimeline(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        long j = 0;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "S")) {
                j = parseLong(xmlPullParser, "t", j);
                long parseLong = parseLong(xmlPullParser, "d", -9223372036854775807L);
                int i = 0;
                int parseInt = parseInt(xmlPullParser, "r", 0) + 1;
                while (i < parseInt) {
                    arrayList.add(buildSegmentTimelineElement(j, parseLong));
                    j += parseLong;
                    i++;
                }
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTimeline"));
        return arrayList;
    }

    /* Access modifiers changed, original: protected */
    public SegmentTimelineElement buildSegmentTimelineElement(long j, long j2) {
        return new SegmentTimelineElement(j, j2);
    }

    /* Access modifiers changed, original: protected */
    public UrlTemplate parseUrlTemplate(XmlPullParser xmlPullParser, String str, UrlTemplate urlTemplate) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue != null ? UrlTemplate.compile(attributeValue) : urlTemplate;
    }

    /* Access modifiers changed, original: protected */
    public RangedUri parseInitialization(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "sourceURL", "range");
    }

    /* Access modifiers changed, original: protected */
    public RangedUri parseSegmentUrl(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "media", "mediaRange");
    }

    /* Access modifiers changed, original: protected */
    public RangedUri parseRangedUrl(XmlPullParser xmlPullParser, String str, String str2) {
        long parseLong;
        long parseLong2;
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        String attributeValue2 = xmlPullParser.getAttributeValue(null, str2);
        if (attributeValue2 != null) {
            String[] split = attributeValue2.split("-");
            parseLong = Long.parseLong(split[0]);
            if (split.length == 2) {
                parseLong2 = (Long.parseLong(split[1]) - parseLong) + 1;
                return buildRangedUri(attributeValue, parseLong, parseLong2);
            }
        }
        parseLong = 0;
        parseLong2 = -1;
        return buildRangedUri(attributeValue, parseLong, parseLong2);
    }

    /* Access modifiers changed, original: protected */
    public RangedUri buildRangedUri(String str, long j, long j2) {
        return new RangedUri(str, j, j2);
    }

    /* Access modifiers changed, original: protected */
    public ProgramInformation parseProgramInformation(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String str = null;
        String parseString = parseString(xmlPullParser, "moreInformationURL", null);
        String parseString2 = parseString(xmlPullParser, "lang", null);
        String str2 = null;
        String str3 = str2;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Title")) {
                str = xmlPullParser.nextText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Source")) {
                str2 = xmlPullParser.nextText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Copyright")) {
                str3 = xmlPullParser.nextText();
            } else {
                maybeSkipTag(xmlPullParser);
            }
            String str4 = str3;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "ProgramInformation")) {
                return new ProgramInformation(str, str2, str4, parseString, parseString2);
            }
            str3 = str4;
        }
    }

    /* Access modifiers changed, original: protected */
    public int parseAudioChannelConfiguration(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", null);
        int i = -1;
        if ("urn:mpeg:dash:23003:3:audio_channel_configuration:2011".equals(parseString)) {
            i = parseInt(xmlPullParser, "value", -1);
        } else if ("tag:dolby.com,2014:dash:audio_channel_configuration:2011".equals(parseString)) {
            i = parseDolbyChannelConfiguration(xmlPullParser);
        }
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "AudioChannelConfiguration"));
        return i;
    }

    public static void maybeSkipTag(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
            int i = 1;
            while (i != 0) {
                xmlPullParser.next();
                if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
                    i++;
                } else if (XmlPullParserUtil.isEndTag(xmlPullParser)) {
                    i--;
                }
            }
        }
    }

    private static void filterRedundantIncompleteSchemeDatas(ArrayList<SchemeData> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            SchemeData schemeData = (SchemeData) arrayList.get(size);
            if (!schemeData.hasData()) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (((SchemeData) arrayList.get(i)).canReplace(schemeData)) {
                        arrayList.remove(size);
                        break;
                    }
                }
            }
        }
    }

    private static String getSampleMimeType(String str, String str2) {
        if (MimeTypes.isAudio(str)) {
            return MimeTypes.getAudioMediaMimeType(str2);
        }
        if (MimeTypes.isVideo(str)) {
            return MimeTypes.getVideoMediaMimeType(str2);
        }
        if (mimeTypeIsRawText(str)) {
            return str;
        }
        if (MimeTypes.APPLICATION_MP4.equals(str)) {
            if (str2 != null) {
                if (str2.startsWith("stpp")) {
                    return MimeTypes.APPLICATION_TTML;
                }
                if (str2.startsWith("wvtt")) {
                    return MimeTypes.APPLICATION_MP4VTT;
                }
            }
        } else if (MimeTypes.APPLICATION_RAWCC.equals(str) && str2 != null) {
            if (str2.contains("cea708")) {
                return MimeTypes.APPLICATION_CEA708;
            }
            if (str2.contains("eia608") || str2.contains("cea608")) {
                return MimeTypes.APPLICATION_CEA608;
            }
        }
        return null;
    }

    private static boolean mimeTypeIsRawText(String str) {
        return MimeTypes.isText(str) || MimeTypes.APPLICATION_TTML.equals(str) || MimeTypes.APPLICATION_MP4VTT.equals(str) || MimeTypes.APPLICATION_CEA708.equals(str) || MimeTypes.APPLICATION_CEA608.equals(str);
    }

    private static String checkLanguageConsistency(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        Assertions.checkState(str.equals(str2));
        return str;
    }

    private static int checkContentTypeConsistency(int i, int i2) {
        if (i == -1) {
            return i2;
        }
        if (i2 == -1) {
            return i;
        }
        Assertions.checkState(i == i2);
        return i;
    }

    protected static Descriptor parseDescriptor(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", "");
        String parseString2 = parseString(xmlPullParser, "value", null);
        String parseString3 = parseString(xmlPullParser, "id", null);
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, str));
        return new Descriptor(parseString, parseString2, parseString3);
    }

    protected static int parseCea608AccessibilityChannel(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("urn:scte:dash:cc:cea-608:2015".equals(descriptor.schemeIdUri)) {
                String str = descriptor.value;
                if (str != null) {
                    Matcher matcher = CEA_608_ACCESSIBILITY_PATTERN.matcher(str);
                    if (matcher.matches()) {
                        return Integer.parseInt(matcher.group(1));
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to parse CEA-608 channel number from: ");
                    stringBuilder.append(descriptor.value);
                    Log.m18w("MpdParser", stringBuilder.toString());
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    protected static int parseCea708AccessibilityChannel(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("urn:scte:dash:cc:cea-708:2015".equals(descriptor.schemeIdUri)) {
                String str = descriptor.value;
                if (str != null) {
                    Matcher matcher = CEA_708_ACCESSIBILITY_PATTERN.matcher(str);
                    if (matcher.matches()) {
                        return Integer.parseInt(matcher.group(1));
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to parse CEA-708 service block number from: ");
                    stringBuilder.append(descriptor.value);
                    Log.m18w("MpdParser", stringBuilder.toString());
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    protected static String parseEac3SupplementalProperties(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(descriptor.schemeIdUri)) {
                if ("ec+3".equals(descriptor.value)) {
                    return MimeTypes.AUDIO_E_AC3_JOC;
                }
            }
        }
        return MimeTypes.AUDIO_E_AC3;
    }

    protected static float parseFrameRate(XmlPullParser xmlPullParser, float f) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "frameRate");
        if (attributeValue == null) {
            return f;
        }
        Matcher matcher = FRAME_RATE_PATTERN.matcher(attributeValue);
        if (!matcher.matches()) {
            return f;
        }
        int parseInt = Integer.parseInt(matcher.group(1));
        attributeValue = matcher.group(2);
        return !TextUtils.isEmpty(attributeValue) ? ((float) parseInt) / ((float) Integer.parseInt(attributeValue)) : (float) parseInt;
    }

    protected static long parseDuration(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        if (attributeValue == null) {
            return j;
        }
        return Util.parseXsDuration(attributeValue);
    }

    protected static long parseDateTime(XmlPullParser xmlPullParser, String str, long j) throws ParserException {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        if (attributeValue == null) {
            return j;
        }
        return Util.parseXsDateTime(attributeValue);
    }

    protected static String parseBaseUrl(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        xmlPullParser.next();
        return UriUtil.resolve(str, xmlPullParser.getText());
    }

    protected static int parseInt(XmlPullParser xmlPullParser, String str, int i) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? i : Integer.parseInt(attributeValue);
    }

    protected static long parseLong(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Long.parseLong(attributeValue);
    }

    protected static String parseString(XmlPullParser xmlPullParser, String str, String str2) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    protected static int parseDolbyChannelConfiguration(org.xmlpull.v1.XmlPullParser r5) {
        /*
        r0 = 0;
        r1 = "value";
        r5 = r5.getAttributeValue(r0, r1);
        r5 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r5);
        r0 = -1;
        if (r5 != 0) goto L_0x000f;
    L_0x000e:
        return r0;
    L_0x000f:
        r1 = r5.hashCode();
        r2 = 3;
        r3 = 2;
        r4 = 1;
        switch(r1) {
            case 1596796: goto L_0x0038;
            case 2937391: goto L_0x002e;
            case 3094035: goto L_0x0024;
            case 3133436: goto L_0x001a;
            default: goto L_0x0019;
        };
    L_0x0019:
        goto L_0x0042;
    L_0x001a:
        r1 = "fa01";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x0042;
    L_0x0022:
        r5 = 3;
        goto L_0x0043;
    L_0x0024:
        r1 = "f801";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x0042;
    L_0x002c:
        r5 = 2;
        goto L_0x0043;
    L_0x002e:
        r1 = "a000";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x0042;
    L_0x0036:
        r5 = 1;
        goto L_0x0043;
    L_0x0038:
        r1 = "4000";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x0042;
    L_0x0040:
        r5 = 0;
        goto L_0x0043;
    L_0x0042:
        r5 = -1;
    L_0x0043:
        if (r5 == 0) goto L_0x0052;
    L_0x0045:
        if (r5 == r4) goto L_0x0051;
    L_0x0047:
        if (r5 == r3) goto L_0x004f;
    L_0x0049:
        if (r5 == r2) goto L_0x004c;
    L_0x004b:
        return r0;
    L_0x004c:
        r5 = 8;
        return r5;
    L_0x004f:
        r5 = 6;
        return r5;
    L_0x0051:
        return r3;
    L_0x0052:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseDolbyChannelConfiguration(org.xmlpull.v1.XmlPullParser):int");
    }
}
