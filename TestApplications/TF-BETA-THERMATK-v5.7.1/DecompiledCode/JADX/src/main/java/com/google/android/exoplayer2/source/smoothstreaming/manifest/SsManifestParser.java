package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.ProtectionElement;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.StreamElement;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SsManifestParser implements Parser<SsManifest> {
    private final XmlPullParserFactory xmlParserFactory;

    private static abstract class ElementParser {
        private final String baseUri;
        private final List<Pair<String, Object>> normalizedAttributes = new LinkedList();
        private final ElementParser parent;
        private final String tag;

        /* Access modifiers changed, original: protected */
        public void addChild(Object obj) {
        }

        public abstract Object build();

        /* Access modifiers changed, original: protected */
        public boolean handleChildInline(String str) {
            return false;
        }

        /* Access modifiers changed, original: protected */
        public void parseEndTag(XmlPullParser xmlPullParser) {
        }

        public abstract void parseStartTag(XmlPullParser xmlPullParser) throws ParserException;

        /* Access modifiers changed, original: protected */
        public void parseText(XmlPullParser xmlPullParser) {
        }

        public ElementParser(ElementParser elementParser, String str, String str2) {
            this.parent = elementParser;
            this.baseUri = str;
            this.tag = str2;
        }

        public final Object parse(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
            Object obj = null;
            int i = 0;
            while (true) {
                int eventType = xmlPullParser.getEventType();
                if (eventType == 1) {
                    return null;
                }
                String name;
                if (eventType == 2) {
                    name = xmlPullParser.getName();
                    if (this.tag.equals(name)) {
                        parseStartTag(xmlPullParser);
                        obj = 1;
                    } else if (obj != null) {
                        if (i > 0) {
                            i++;
                        } else if (handleChildInline(name)) {
                            parseStartTag(xmlPullParser);
                        } else {
                            ElementParser newChildParser = newChildParser(this, name, this.baseUri);
                            if (newChildParser == null) {
                                i = 1;
                            } else {
                                addChild(newChildParser.parse(xmlPullParser));
                            }
                        }
                    }
                } else if (eventType != 3) {
                    if (eventType == 4 && obj != null && i == 0) {
                        parseText(xmlPullParser);
                    }
                } else if (obj == null) {
                    continue;
                } else if (i > 0) {
                    i--;
                } else {
                    name = xmlPullParser.getName();
                    parseEndTag(xmlPullParser);
                    if (!handleChildInline(name)) {
                        return build();
                    }
                }
                xmlPullParser.next();
            }
        }

        private ElementParser newChildParser(ElementParser elementParser, String str, String str2) {
            if ("QualityLevel".equals(str)) {
                return new QualityLevelParser(elementParser, str2);
            }
            if ("Protection".equals(str)) {
                return new ProtectionParser(elementParser, str2);
            }
            return "StreamIndex".equals(str) ? new StreamIndexParser(elementParser, str2) : null;
        }

        /* Access modifiers changed, original: protected|final */
        public final void putNormalizedAttribute(String str, Object obj) {
            this.normalizedAttributes.add(Pair.create(str, obj));
        }

        /* Access modifiers changed, original: protected|final */
        public final Object getNormalizedAttribute(String str) {
            for (int i = 0; i < this.normalizedAttributes.size(); i++) {
                Pair pair = (Pair) this.normalizedAttributes.get(i);
                if (((String) pair.first).equals(str)) {
                    return pair.second;
                }
            }
            ElementParser elementParser = this.parent;
            return elementParser == null ? null : elementParser.getNormalizedAttribute(str);
        }

        /* Access modifiers changed, original: protected|final */
        public final String parseRequiredString(XmlPullParser xmlPullParser, String str) throws MissingFieldException {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                return attributeValue;
            }
            throw new MissingFieldException(str);
        }

        /* Access modifiers changed, original: protected|final */
        public final int parseInt(XmlPullParser xmlPullParser, String str, int i) throws ParserException {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                return i;
            }
            try {
                return Integer.parseInt(attributeValue);
            } catch (NumberFormatException e) {
                throw new ParserException(e);
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final int parseRequiredInt(XmlPullParser xmlPullParser, String str) throws ParserException {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                try {
                    return Integer.parseInt(attributeValue);
                } catch (NumberFormatException e) {
                    throw new ParserException(e);
                }
            }
            throw new MissingFieldException(str);
        }

        /* Access modifiers changed, original: protected|final */
        public final long parseLong(XmlPullParser xmlPullParser, String str, long j) throws ParserException {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                return j;
            }
            try {
                return Long.parseLong(attributeValue);
            } catch (NumberFormatException e) {
                throw new ParserException(e);
            }
        }

        /* Access modifiers changed, original: protected|final */
        public final long parseRequiredLong(XmlPullParser xmlPullParser, String str) throws ParserException {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                try {
                    return Long.parseLong(attributeValue);
                } catch (NumberFormatException e) {
                    throw new ParserException(e);
                }
            }
            throw new MissingFieldException(str);
        }

        /* Access modifiers changed, original: protected|final */
        public final boolean parseBoolean(XmlPullParser xmlPullParser, String str, boolean z) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            return attributeValue != null ? Boolean.parseBoolean(attributeValue) : z;
        }
    }

    public static class MissingFieldException extends ParserException {
        public MissingFieldException(String str) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Missing required field: ");
            stringBuilder.append(str);
            super(stringBuilder.toString());
        }
    }

    private static class ProtectionParser extends ElementParser {
        private boolean inProtectionHeader;
        private byte[] initData;
        private UUID uuid;

        public ProtectionParser(ElementParser elementParser, String str) {
            super(elementParser, str, "Protection");
        }

        public boolean handleChildInline(String str) {
            return "ProtectionHeader".equals(str);
        }

        public void parseStartTag(XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.inProtectionHeader = true;
                this.uuid = UUID.fromString(stripCurlyBraces(xmlPullParser.getAttributeValue(null, "SystemID")));
            }
        }

        public void parseText(XmlPullParser xmlPullParser) {
            if (this.inProtectionHeader) {
                this.initData = Base64.decode(xmlPullParser.getText(), 0);
            }
        }

        public void parseEndTag(XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.inProtectionHeader = false;
            }
        }

        public Object build() {
            UUID uuid = this.uuid;
            return new ProtectionElement(uuid, PsshAtomUtil.buildPsshAtom(uuid, this.initData), buildTrackEncryptionBoxes(this.initData));
        }

        private static TrackEncryptionBox[] buildTrackEncryptionBoxes(byte[] bArr) {
            return new TrackEncryptionBox[]{new TrackEncryptionBox(true, null, 8, getProtectionElementKeyId(bArr), 0, 0, null)};
        }

        private static byte[] getProtectionElementKeyId(byte[] bArr) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bArr.length; i += 2) {
                stringBuilder.append((char) bArr[i]);
            }
            String stringBuilder2 = stringBuilder.toString();
            bArr = Base64.decode(stringBuilder2.substring(stringBuilder2.indexOf("<KID>") + 5, stringBuilder2.indexOf("</KID>")), 0);
            swap(bArr, 0, 3);
            swap(bArr, 1, 2);
            swap(bArr, 4, 5);
            swap(bArr, 6, 7);
            return bArr;
        }

        private static void swap(byte[] bArr, int i, int i2) {
            byte b = bArr[i];
            bArr[i] = bArr[i2];
            bArr[i2] = b;
        }

        private static String stripCurlyBraces(String str) {
            return (str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}') ? str.substring(1, str.length() - 1) : str;
        }
    }

    private static class QualityLevelParser extends ElementParser {
        private Format format;

        public QualityLevelParser(ElementParser elementParser, String str) {
            super(elementParser, str, "QualityLevel");
        }

        public void parseStartTag(XmlPullParser xmlPullParser) throws ParserException {
            int intValue = ((Integer) getNormalizedAttribute("Type")).intValue();
            String attributeValue = xmlPullParser.getAttributeValue(null, "Index");
            String str = (String) getNormalizedAttribute("Name");
            int parseRequiredInt = parseRequiredInt(xmlPullParser, "Bitrate");
            Object fourCCToMimeType = fourCCToMimeType(parseRequiredString(xmlPullParser, "FourCC"));
            String str2 = "CodecPrivateData";
            if (intValue == 2) {
                this.format = Format.createVideoContainerFormat(attributeValue, str, MimeTypes.VIDEO_MP4, fourCCToMimeType, null, parseRequiredInt, parseRequiredInt(xmlPullParser, "MaxWidth"), parseRequiredInt(xmlPullParser, "MaxHeight"), -1.0f, buildCodecSpecificData(xmlPullParser.getAttributeValue(null, str2)), 0);
                return;
            }
            String str3 = "Language";
            if (intValue == 1) {
                String str4 = MimeTypes.AUDIO_AAC;
                if (fourCCToMimeType == null) {
                    fourCCToMimeType = str4;
                }
                int parseRequiredInt2 = parseRequiredInt(xmlPullParser, "Channels");
                int parseRequiredInt3 = parseRequiredInt(xmlPullParser, "SamplingRate");
                List buildCodecSpecificData = buildCodecSpecificData(xmlPullParser.getAttributeValue(null, str2));
                if (buildCodecSpecificData.isEmpty() && str4.equals(fourCCToMimeType)) {
                    buildCodecSpecificData = Collections.singletonList(CodecSpecificDataUtil.buildAacLcAudioSpecificConfig(parseRequiredInt3, parseRequiredInt2));
                }
                this.format = Format.createAudioContainerFormat(attributeValue, str, MimeTypes.AUDIO_MP4, fourCCToMimeType, null, parseRequiredInt, parseRequiredInt2, parseRequiredInt3, buildCodecSpecificData, 0, (String) getNormalizedAttribute(str3));
            } else if (intValue == 3) {
                this.format = Format.createTextContainerFormat(attributeValue, str, MimeTypes.APPLICATION_MP4, fourCCToMimeType, null, parseRequiredInt, 0, (String) getNormalizedAttribute(str3));
            } else {
                this.format = Format.createContainerFormat(attributeValue, str, MimeTypes.APPLICATION_MP4, fourCCToMimeType, null, parseRequiredInt, 0, null);
            }
        }

        public Object build() {
            return this.format;
        }

        private static List<byte[]> buildCodecSpecificData(String str) {
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(str)) {
                byte[] bytesFromHexString = Util.getBytesFromHexString(str);
                byte[][] splitNalUnits = CodecSpecificDataUtil.splitNalUnits(bytesFromHexString);
                if (splitNalUnits == null) {
                    arrayList.add(bytesFromHexString);
                } else {
                    Collections.addAll(arrayList, splitNalUnits);
                }
            }
            return arrayList;
        }

        private static String fourCCToMimeType(String str) {
            if (str.equalsIgnoreCase("H264") || str.equalsIgnoreCase("X264") || str.equalsIgnoreCase("AVC1") || str.equalsIgnoreCase("DAVC")) {
                return "video/avc";
            }
            if (str.equalsIgnoreCase("AAC") || str.equalsIgnoreCase("AACL") || str.equalsIgnoreCase("AACH") || str.equalsIgnoreCase("AACP")) {
                return MimeTypes.AUDIO_AAC;
            }
            if (str.equalsIgnoreCase("TTML") || str.equalsIgnoreCase("DFXP")) {
                return MimeTypes.APPLICATION_TTML;
            }
            if (str.equalsIgnoreCase("ac-3") || str.equalsIgnoreCase("dac3")) {
                return MimeTypes.AUDIO_AC3;
            }
            if (str.equalsIgnoreCase("ec-3") || str.equalsIgnoreCase("dec3")) {
                return MimeTypes.AUDIO_E_AC3;
            }
            if (str.equalsIgnoreCase("dtsc")) {
                return MimeTypes.AUDIO_DTS;
            }
            if (str.equalsIgnoreCase("dtsh") || str.equalsIgnoreCase("dtsl")) {
                return MimeTypes.AUDIO_DTS_HD;
            }
            if (str.equalsIgnoreCase("dtse")) {
                return MimeTypes.AUDIO_DTS_EXPRESS;
            }
            return str.equalsIgnoreCase("opus") ? MimeTypes.AUDIO_OPUS : null;
        }
    }

    private static class SmoothStreamingMediaParser extends ElementParser {
        private long duration;
        private long dvrWindowLength;
        private boolean isLive;
        private int lookAheadCount = -1;
        private int majorVersion;
        private int minorVersion;
        private ProtectionElement protectionElement = null;
        private final List<StreamElement> streamElements = new LinkedList();
        private long timescale;

        public SmoothStreamingMediaParser(ElementParser elementParser, String str) {
            super(elementParser, str, "SmoothStreamingMedia");
        }

        public void parseStartTag(XmlPullParser xmlPullParser) throws ParserException {
            this.majorVersion = parseRequiredInt(xmlPullParser, "MajorVersion");
            this.minorVersion = parseRequiredInt(xmlPullParser, "MinorVersion");
            String str = "TimeScale";
            this.timescale = parseLong(xmlPullParser, str, 10000000);
            this.duration = parseRequiredLong(xmlPullParser, "Duration");
            this.dvrWindowLength = parseLong(xmlPullParser, "DVRWindowLength", 0);
            this.lookAheadCount = parseInt(xmlPullParser, "LookaheadCount", -1);
            this.isLive = parseBoolean(xmlPullParser, "IsLive", false);
            putNormalizedAttribute(str, Long.valueOf(this.timescale));
        }

        public void addChild(Object obj) {
            if (obj instanceof StreamElement) {
                this.streamElements.add((StreamElement) obj);
            } else if (obj instanceof ProtectionElement) {
                Assertions.checkState(this.protectionElement == null);
                this.protectionElement = (ProtectionElement) obj;
            }
        }

        public Object build() {
            StreamElement[] streamElementArr = new StreamElement[this.streamElements.size()];
            this.streamElements.toArray(streamElementArr);
            ProtectionElement protectionElement = this.protectionElement;
            if (protectionElement != null) {
                SchemeData[] schemeDataArr = new SchemeData[1];
                schemeDataArr[0] = new SchemeData(protectionElement.uuid, MimeTypes.VIDEO_MP4, protectionElement.data);
                DrmInitData drmInitData = new DrmInitData(schemeDataArr);
                for (StreamElement streamElement : streamElementArr) {
                    int i = streamElement.type;
                    if (i == 2 || i == 1) {
                        Format[] formatArr = streamElement.formats;
                        for (i = 0; i < formatArr.length; i++) {
                            formatArr[i] = formatArr[i].copyWithDrmInitData(drmInitData);
                        }
                    }
                }
            }
            return new SsManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, streamElementArr);
        }
    }

    private static class StreamIndexParser extends ElementParser {
        private final String baseUri;
        private int displayHeight;
        private int displayWidth;
        private final List<Format> formats = new LinkedList();
        private String language;
        private long lastChunkDuration;
        private int maxHeight;
        private int maxWidth;
        private String name;
        private ArrayList<Long> startTimes;
        private String subType;
        private long timescale;
        private int type;
        private String url;

        public StreamIndexParser(ElementParser elementParser, String str) {
            super(elementParser, str, "StreamIndex");
            this.baseUri = str;
        }

        public boolean handleChildInline(String str) {
            return "c".equals(str);
        }

        public void parseStartTag(XmlPullParser xmlPullParser) throws ParserException {
            if ("c".equals(xmlPullParser.getName())) {
                parseStreamFragmentStartTag(xmlPullParser);
            } else {
                parseStreamElementStartTag(xmlPullParser);
            }
        }

        private void parseStreamFragmentStartTag(XmlPullParser xmlPullParser) throws ParserException {
            int size = this.startTimes.size();
            long parseLong = parseLong(xmlPullParser, "t", -9223372036854775807L);
            int i = 1;
            if (parseLong == -9223372036854775807L) {
                if (size == 0) {
                    parseLong = 0;
                } else if (this.lastChunkDuration != -1) {
                    parseLong = ((Long) this.startTimes.get(size - 1)).longValue() + this.lastChunkDuration;
                } else {
                    throw new ParserException("Unable to infer start time");
                }
            }
            this.startTimes.add(Long.valueOf(parseLong));
            this.lastChunkDuration = parseLong(xmlPullParser, "d", -9223372036854775807L);
            long parseLong2 = parseLong(xmlPullParser, "r", 1);
            if (parseLong2 <= 1 || this.lastChunkDuration != -9223372036854775807L) {
                while (true) {
                    long j = (long) i;
                    if (j < parseLong2) {
                        this.startTimes.add(Long.valueOf((this.lastChunkDuration * j) + parseLong));
                        i++;
                    } else {
                        return;
                    }
                }
            }
            throw new ParserException("Repeated chunk with unspecified duration");
        }

        private void parseStreamElementStartTag(XmlPullParser xmlPullParser) throws ParserException {
            this.type = parseType(xmlPullParser);
            putNormalizedAttribute("Type", Integer.valueOf(this.type));
            String str = "Subtype";
            if (this.type == 3) {
                this.subType = parseRequiredString(xmlPullParser, str);
            } else {
                this.subType = xmlPullParser.getAttributeValue(null, str);
            }
            this.name = xmlPullParser.getAttributeValue(null, "Name");
            this.url = parseRequiredString(xmlPullParser, "Url");
            this.maxWidth = parseInt(xmlPullParser, "MaxWidth", -1);
            this.maxHeight = parseInt(xmlPullParser, "MaxHeight", -1);
            this.displayWidth = parseInt(xmlPullParser, "DisplayWidth", -1);
            this.displayHeight = parseInt(xmlPullParser, "DisplayHeight", -1);
            str = "Language";
            this.language = xmlPullParser.getAttributeValue(null, str);
            putNormalizedAttribute(str, this.language);
            str = "TimeScale";
            this.timescale = (long) parseInt(xmlPullParser, str, -1);
            if (this.timescale == -1) {
                this.timescale = ((Long) getNormalizedAttribute(str)).longValue();
            }
            this.startTimes = new ArrayList();
        }

        private int parseType(XmlPullParser xmlPullParser) throws ParserException {
            String str = "Type";
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                throw new MissingFieldException(str);
            } else if (MimeTypes.BASE_TYPE_AUDIO.equalsIgnoreCase(attributeValue)) {
                return 1;
            } else {
                if (MimeTypes.BASE_TYPE_VIDEO.equalsIgnoreCase(attributeValue)) {
                    return 2;
                }
                if (MimeTypes.BASE_TYPE_TEXT.equalsIgnoreCase(attributeValue)) {
                    return 3;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid key value[");
                stringBuilder.append(attributeValue);
                stringBuilder.append("]");
                throw new ParserException(stringBuilder.toString());
            }
        }

        public void addChild(Object obj) {
            if (obj instanceof Format) {
                this.formats.add((Format) obj);
            }
        }

        public Object build() {
            Format[] formatArr = new Format[this.formats.size()];
            Format[] formatArr2 = formatArr;
            this.formats.toArray(formatArr);
            StreamElement streamElement = r2;
            StreamElement streamElement2 = new StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, formatArr2, this.startTimes, this.lastChunkDuration);
            return streamElement;
        }
    }

    public SsManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public SsManifest parse(Uri uri, InputStream inputStream) throws IOException {
        try {
            XmlPullParser newPullParser = this.xmlParserFactory.newPullParser();
            newPullParser.setInput(inputStream, null);
            return (SsManifest) new SmoothStreamingMediaParser(null, uri.toString()).parse(newPullParser);
        } catch (XmlPullParserException e) {
            throw new ParserException(e);
        }
    }
}
