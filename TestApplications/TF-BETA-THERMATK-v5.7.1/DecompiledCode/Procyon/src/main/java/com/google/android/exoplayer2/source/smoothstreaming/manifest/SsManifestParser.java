// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collection;
import java.util.Collections;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Util;
import android.text.TextUtils;
import java.util.ArrayList;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import android.util.Base64;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import java.util.UUID;
import java.util.LinkedList;
import android.util.Pair;
import java.util.List;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import com.google.android.exoplayer2.ParserException;
import java.io.InputStream;
import android.net.Uri;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.google.android.exoplayer2.upstream.ParsingLoadable;

public class SsManifestParser implements Parser<SsManifest>
{
    private final XmlPullParserFactory xmlParserFactory;
    
    public SsManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException cause) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", (Throwable)cause);
        }
    }
    
    public SsManifest parse(final Uri uri, final InputStream inputStream) throws IOException {
        try {
            final XmlPullParser pullParser = this.xmlParserFactory.newPullParser();
            pullParser.setInput(inputStream, (String)null);
            return (SsManifest)((ElementParser)new SmoothStreamingMediaParser(null, uri.toString())).parse(pullParser);
        }
        catch (XmlPullParserException ex) {
            throw new ParserException((Throwable)ex);
        }
    }
    
    private abstract static class ElementParser
    {
        private final String baseUri;
        private final List<Pair<String, Object>> normalizedAttributes;
        private final ElementParser parent;
        private final String tag;
        
        public ElementParser(final ElementParser parent, final String baseUri, final String tag) {
            this.parent = parent;
            this.baseUri = baseUri;
            this.tag = tag;
            this.normalizedAttributes = new LinkedList<Pair<String, Object>>();
        }
        
        private ElementParser newChildParser(final ElementParser elementParser, final String anObject, final String s) {
            if ("QualityLevel".equals(anObject)) {
                return (ElementParser)new QualityLevelParser(elementParser, s);
            }
            if ("Protection".equals(anObject)) {
                return (ElementParser)new ProtectionParser(elementParser, s);
            }
            if ("StreamIndex".equals(anObject)) {
                return (ElementParser)new StreamIndexParser(elementParser, s);
            }
            return null;
        }
        
        protected void addChild(final Object o) {
        }
        
        protected abstract Object build();
        
        protected final Object getNormalizedAttribute(final String anObject) {
            for (int i = 0; i < this.normalizedAttributes.size(); ++i) {
                final Pair<String, Object> pair = this.normalizedAttributes.get(i);
                if (((String)pair.first).equals(anObject)) {
                    return pair.second;
                }
            }
            final ElementParser parent = this.parent;
            Object normalizedAttribute;
            if (parent == null) {
                normalizedAttribute = null;
            }
            else {
                normalizedAttribute = parent.getNormalizedAttribute(anObject);
            }
            return normalizedAttribute;
        }
        
        protected boolean handleChildInline(final String s) {
            return false;
        }
        
        public final Object parse(final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
            int n = 0;
            int n2 = 0;
            while (true) {
                final int eventType = xmlPullParser.getEventType();
                if (eventType == 1) {
                    return null;
                }
                int n3;
                int n4;
                if (eventType != 2) {
                    if (eventType != 3) {
                        if (eventType != 4) {
                            n3 = n;
                            n4 = n2;
                        }
                        else {
                            n3 = n;
                            n4 = n2;
                            if (n != 0) {
                                n3 = n;
                                if ((n4 = n2) == 0) {
                                    this.parseText(xmlPullParser);
                                    n3 = n;
                                    n4 = n2;
                                }
                            }
                        }
                    }
                    else {
                        n3 = n;
                        n4 = n2;
                        if (n != 0) {
                            if (n2 > 0) {
                                n4 = n2 - 1;
                                n3 = n;
                            }
                            else {
                                final String name = xmlPullParser.getName();
                                this.parseEndTag(xmlPullParser);
                                n3 = n;
                                n4 = n2;
                                if (!this.handleChildInline(name)) {
                                    return this.build();
                                }
                            }
                        }
                    }
                }
                else {
                    final String name2 = xmlPullParser.getName();
                    if (this.tag.equals(name2)) {
                        this.parseStartTag(xmlPullParser);
                        n3 = 1;
                        n4 = n2;
                    }
                    else {
                        n3 = n;
                        n4 = n2;
                        if (n != 0) {
                            if (n2 > 0) {
                                n4 = n2 + 1;
                                n3 = n;
                            }
                            else if (this.handleChildInline(name2)) {
                                this.parseStartTag(xmlPullParser);
                                n3 = n;
                                n4 = n2;
                            }
                            else {
                                final ElementParser childParser = this.newChildParser(this, name2, this.baseUri);
                                if (childParser == null) {
                                    n4 = 1;
                                    n3 = n;
                                }
                                else {
                                    this.addChild(childParser.parse(xmlPullParser));
                                    n4 = n2;
                                    n3 = n;
                                }
                            }
                        }
                    }
                }
                xmlPullParser.next();
                n = n3;
                n2 = n4;
            }
        }
        
        protected final boolean parseBoolean(final XmlPullParser xmlPullParser, final String s, final boolean b) {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, s);
            if (attributeValue != null) {
                return Boolean.parseBoolean(attributeValue);
            }
            return b;
        }
        
        protected void parseEndTag(final XmlPullParser xmlPullParser) {
        }
        
        protected final int parseInt(final XmlPullParser xmlPullParser, final String s, int int1) throws ParserException {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, s);
            if (attributeValue != null) {
                try {
                    int1 = Integer.parseInt(attributeValue);
                    return int1;
                }
                catch (NumberFormatException ex) {
                    throw new ParserException(ex);
                }
            }
            return int1;
        }
        
        protected final long parseLong(final XmlPullParser xmlPullParser, final String s, long long1) throws ParserException {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, s);
            if (attributeValue != null) {
                try {
                    long1 = Long.parseLong(attributeValue);
                    return long1;
                }
                catch (NumberFormatException ex) {
                    throw new ParserException(ex);
                }
            }
            return long1;
        }
        
        protected final int parseRequiredInt(final XmlPullParser xmlPullParser, final String s) throws ParserException {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, s);
            if (attributeValue != null) {
                try {
                    return Integer.parseInt(attributeValue);
                }
                catch (NumberFormatException ex) {
                    throw new ParserException(ex);
                }
            }
            throw new MissingFieldException(s);
        }
        
        protected final long parseRequiredLong(final XmlPullParser xmlPullParser, final String s) throws ParserException {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, s);
            if (attributeValue != null) {
                try {
                    return Long.parseLong(attributeValue);
                }
                catch (NumberFormatException ex) {
                    throw new ParserException(ex);
                }
            }
            throw new MissingFieldException(s);
        }
        
        protected final String parseRequiredString(final XmlPullParser xmlPullParser, final String s) throws MissingFieldException {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, s);
            if (attributeValue != null) {
                return attributeValue;
            }
            throw new MissingFieldException(s);
        }
        
        protected abstract void parseStartTag(final XmlPullParser p0) throws ParserException;
        
        protected void parseText(final XmlPullParser xmlPullParser) {
        }
        
        protected final void putNormalizedAttribute(final String s, final Object o) {
            this.normalizedAttributes.add((Pair<String, Object>)Pair.create((Object)s, o));
        }
    }
    
    public static class MissingFieldException extends ParserException
    {
        public MissingFieldException(final String str) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Missing required field: ");
            sb.append(str);
            super(sb.toString());
        }
    }
    
    private static class ProtectionParser extends ElementParser
    {
        private boolean inProtectionHeader;
        private byte[] initData;
        private UUID uuid;
        
        public ProtectionParser(final ElementParser elementParser, final String s) {
            super(elementParser, s, "Protection");
        }
        
        private static TrackEncryptionBox[] buildTrackEncryptionBoxes(final byte[] array) {
            return new TrackEncryptionBox[] { new TrackEncryptionBox(true, null, 8, getProtectionElementKeyId(array), 0, 0, null) };
        }
        
        private static byte[] getProtectionElementKeyId(byte[] decode) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < decode.length; i += 2) {
                sb.append((char)decode[i]);
            }
            final String string = sb.toString();
            decode = Base64.decode(string.substring(string.indexOf("<KID>") + 5, string.indexOf("</KID>")), 0);
            swap(decode, 0, 3);
            swap(decode, 1, 2);
            swap(decode, 4, 5);
            swap(decode, 6, 7);
            return decode;
        }
        
        private static String stripCurlyBraces(final String s) {
            String substring = s;
            if (s.charAt(0) == '{') {
                substring = s;
                if (s.charAt(s.length() - 1) == '}') {
                    substring = s.substring(1, s.length() - 1);
                }
            }
            return substring;
        }
        
        private static void swap(final byte[] array, final int n, final int n2) {
            final byte b = array[n];
            array[n] = array[n2];
            array[n2] = b;
        }
        
        public Object build() {
            final UUID uuid = this.uuid;
            return new SsManifest.ProtectionElement(uuid, PsshAtomUtil.buildPsshAtom(uuid, this.initData), buildTrackEncryptionBoxes(this.initData));
        }
        
        public boolean handleChildInline(final String anObject) {
            return "ProtectionHeader".equals(anObject);
        }
        
        public void parseEndTag(final XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.inProtectionHeader = false;
            }
        }
        
        public void parseStartTag(final XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.inProtectionHeader = true;
                this.uuid = UUID.fromString(stripCurlyBraces(xmlPullParser.getAttributeValue((String)null, "SystemID")));
            }
        }
        
        public void parseText(final XmlPullParser xmlPullParser) {
            if (this.inProtectionHeader) {
                this.initData = Base64.decode(xmlPullParser.getText(), 0);
            }
        }
    }
    
    private static class QualityLevelParser extends ElementParser
    {
        private Format format;
        
        public QualityLevelParser(final ElementParser elementParser, final String s) {
            super(elementParser, s, "QualityLevel");
        }
        
        private static List<byte[]> buildCodecSpecificData(final String s) {
            final ArrayList<Object> c = (ArrayList<Object>)new ArrayList<byte[]>();
            if (!TextUtils.isEmpty((CharSequence)s)) {
                final byte[] bytesFromHexString = Util.getBytesFromHexString(s);
                final byte[][] splitNalUnits = CodecSpecificDataUtil.splitNalUnits(bytesFromHexString);
                if (splitNalUnits == null) {
                    c.add(bytesFromHexString);
                }
                else {
                    Collections.addAll(c, splitNalUnits);
                }
            }
            return (List<byte[]>)c;
        }
        
        private static String fourCCToMimeType(final String s) {
            if (s.equalsIgnoreCase("H264") || s.equalsIgnoreCase("X264") || s.equalsIgnoreCase("AVC1") || s.equalsIgnoreCase("DAVC")) {
                return "video/avc";
            }
            if (s.equalsIgnoreCase("AAC") || s.equalsIgnoreCase("AACL") || s.equalsIgnoreCase("AACH") || s.equalsIgnoreCase("AACP")) {
                return "audio/mp4a-latm";
            }
            if (s.equalsIgnoreCase("TTML") || s.equalsIgnoreCase("DFXP")) {
                return "application/ttml+xml";
            }
            if (s.equalsIgnoreCase("ac-3") || s.equalsIgnoreCase("dac3")) {
                return "audio/ac3";
            }
            if (s.equalsIgnoreCase("ec-3") || s.equalsIgnoreCase("dec3")) {
                return "audio/eac3";
            }
            if (s.equalsIgnoreCase("dtsc")) {
                return "audio/vnd.dts";
            }
            if (s.equalsIgnoreCase("dtsh") || s.equalsIgnoreCase("dtsl")) {
                return "audio/vnd.dts.hd";
            }
            if (s.equalsIgnoreCase("dtse")) {
                return "audio/vnd.dts.hd;profile=lbr";
            }
            if (s.equalsIgnoreCase("opus")) {
                return "audio/opus";
            }
            return null;
        }
        
        public Object build() {
            return this.format;
        }
        
        public void parseStartTag(final XmlPullParser xmlPullParser) throws ParserException {
            final int intValue = (int)((ElementParser)this).getNormalizedAttribute("Type");
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, "Index");
            final String s = (String)((ElementParser)this).getNormalizedAttribute("Name");
            final int requiredInt = ((ElementParser)this).parseRequiredInt(xmlPullParser, "Bitrate");
            final String fourCCToMimeType = fourCCToMimeType(((ElementParser)this).parseRequiredString(xmlPullParser, "FourCC"));
            if (intValue == 2) {
                this.format = Format.createVideoContainerFormat(attributeValue, s, "video/mp4", fourCCToMimeType, null, requiredInt, ((ElementParser)this).parseRequiredInt(xmlPullParser, "MaxWidth"), ((ElementParser)this).parseRequiredInt(xmlPullParser, "MaxHeight"), -1.0f, buildCodecSpecificData(xmlPullParser.getAttributeValue((String)null, "CodecPrivateData")), 0);
            }
            else if (intValue == 1) {
                String anObject;
                if ((anObject = fourCCToMimeType) == null) {
                    anObject = "audio/mp4a-latm";
                }
                final int requiredInt2 = ((ElementParser)this).parseRequiredInt(xmlPullParser, "Channels");
                final int requiredInt3 = ((ElementParser)this).parseRequiredInt(xmlPullParser, "SamplingRate");
                List<byte[]> list2;
                final List<byte[]> list = list2 = buildCodecSpecificData(xmlPullParser.getAttributeValue((String)null, "CodecPrivateData"));
                if (list.isEmpty()) {
                    list2 = list;
                    if ("audio/mp4a-latm".equals(anObject)) {
                        list2 = Collections.singletonList(CodecSpecificDataUtil.buildAacLcAudioSpecificConfig(requiredInt3, requiredInt2));
                    }
                }
                this.format = Format.createAudioContainerFormat(attributeValue, s, "audio/mp4", anObject, null, requiredInt, requiredInt2, requiredInt3, list2, 0, (String)((ElementParser)this).getNormalizedAttribute("Language"));
            }
            else if (intValue == 3) {
                this.format = Format.createTextContainerFormat(attributeValue, s, "application/mp4", fourCCToMimeType, null, requiredInt, 0, (String)((ElementParser)this).getNormalizedAttribute("Language"));
            }
            else {
                this.format = Format.createContainerFormat(attributeValue, s, "application/mp4", fourCCToMimeType, null, requiredInt, 0, null);
            }
        }
    }
    
    private static class SmoothStreamingMediaParser extends ElementParser
    {
        private long duration;
        private long dvrWindowLength;
        private boolean isLive;
        private int lookAheadCount;
        private int majorVersion;
        private int minorVersion;
        private SsManifest.ProtectionElement protectionElement;
        private final List<SsManifest.StreamElement> streamElements;
        private long timescale;
        
        public SmoothStreamingMediaParser(final ElementParser elementParser, final String s) {
            super(elementParser, s, "SmoothStreamingMedia");
            this.lookAheadCount = -1;
            this.protectionElement = null;
            this.streamElements = new LinkedList<SsManifest.StreamElement>();
        }
        
        public void addChild(final Object o) {
            if (o instanceof SsManifest.StreamElement) {
                this.streamElements.add((SsManifest.StreamElement)o);
            }
            else if (o instanceof SsManifest.ProtectionElement) {
                Assertions.checkState(this.protectionElement == null);
                this.protectionElement = (SsManifest.ProtectionElement)o;
            }
        }
        
        public Object build() {
            final SsManifest.StreamElement[] array = new SsManifest.StreamElement[this.streamElements.size()];
            this.streamElements.toArray(array);
            final SsManifest.ProtectionElement protectionElement = this.protectionElement;
            if (protectionElement != null) {
                final DrmInitData drmInitData = new DrmInitData(new DrmInitData.SchemeData[] { new DrmInitData.SchemeData(protectionElement.uuid, "video/mp4", protectionElement.data) });
                for (final SsManifest.StreamElement streamElement : array) {
                    final int type = streamElement.type;
                    if (type == 2 || type == 1) {
                        final Format[] formats = streamElement.formats;
                        for (int j = 0; j < formats.length; ++j) {
                            formats[j] = formats[j].copyWithDrmInitData(drmInitData);
                        }
                    }
                }
            }
            return new SsManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, array);
        }
        
        public void parseStartTag(final XmlPullParser xmlPullParser) throws ParserException {
            this.majorVersion = ((ElementParser)this).parseRequiredInt(xmlPullParser, "MajorVersion");
            this.minorVersion = ((ElementParser)this).parseRequiredInt(xmlPullParser, "MinorVersion");
            this.timescale = ((ElementParser)this).parseLong(xmlPullParser, "TimeScale", 10000000L);
            this.duration = ((ElementParser)this).parseRequiredLong(xmlPullParser, "Duration");
            this.dvrWindowLength = ((ElementParser)this).parseLong(xmlPullParser, "DVRWindowLength", 0L);
            this.lookAheadCount = ((ElementParser)this).parseInt(xmlPullParser, "LookaheadCount", -1);
            this.isLive = ((ElementParser)this).parseBoolean(xmlPullParser, "IsLive", false);
            ((ElementParser)this).putNormalizedAttribute("TimeScale", this.timescale);
        }
    }
    
    private static class StreamIndexParser extends ElementParser
    {
        private final String baseUri;
        private int displayHeight;
        private int displayWidth;
        private final List<Format> formats;
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
        
        public StreamIndexParser(final ElementParser elementParser, final String baseUri) {
            super(elementParser, baseUri, "StreamIndex");
            this.baseUri = baseUri;
            this.formats = new LinkedList<Format>();
        }
        
        private void parseStreamElementStartTag(final XmlPullParser xmlPullParser) throws ParserException {
            this.type = this.parseType(xmlPullParser);
            ((ElementParser)this).putNormalizedAttribute("Type", this.type);
            if (this.type == 3) {
                this.subType = ((ElementParser)this).parseRequiredString(xmlPullParser, "Subtype");
            }
            else {
                this.subType = xmlPullParser.getAttributeValue((String)null, "Subtype");
            }
            this.name = xmlPullParser.getAttributeValue((String)null, "Name");
            this.url = ((ElementParser)this).parseRequiredString(xmlPullParser, "Url");
            this.maxWidth = ((ElementParser)this).parseInt(xmlPullParser, "MaxWidth", -1);
            this.maxHeight = ((ElementParser)this).parseInt(xmlPullParser, "MaxHeight", -1);
            this.displayWidth = ((ElementParser)this).parseInt(xmlPullParser, "DisplayWidth", -1);
            this.displayHeight = ((ElementParser)this).parseInt(xmlPullParser, "DisplayHeight", -1);
            ((ElementParser)this).putNormalizedAttribute("Language", this.language = xmlPullParser.getAttributeValue((String)null, "Language"));
            this.timescale = ((ElementParser)this).parseInt(xmlPullParser, "TimeScale", -1);
            if (this.timescale == -1L) {
                this.timescale = (long)((ElementParser)this).getNormalizedAttribute("TimeScale");
            }
            this.startTimes = new ArrayList<Long>();
        }
        
        private void parseStreamFragmentStartTag(final XmlPullParser xmlPullParser) throws ParserException {
            final int size = this.startTimes.size();
            final long long1 = ((ElementParser)this).parseLong(xmlPullParser, "t", -9223372036854775807L);
            final int n = 1;
            long l = long1;
            if (long1 == -9223372036854775807L) {
                if (size == 0) {
                    l = 0L;
                }
                else {
                    if (this.lastChunkDuration == -1L) {
                        throw new ParserException("Unable to infer start time");
                    }
                    l = this.startTimes.get(size - 1) + this.lastChunkDuration;
                }
            }
            this.startTimes.add(l);
            this.lastChunkDuration = ((ElementParser)this).parseLong(xmlPullParser, "d", -9223372036854775807L);
            final long long2 = ((ElementParser)this).parseLong(xmlPullParser, "r", 1L);
            int n2 = n;
            if (long2 > 1L) {
                if (this.lastChunkDuration == -9223372036854775807L) {
                    throw new ParserException("Repeated chunk with unspecified duration");
                }
                n2 = n;
            }
            while (true) {
                final long n3 = n2;
                if (n3 >= long2) {
                    break;
                }
                this.startTimes.add(this.lastChunkDuration * n3 + l);
                ++n2;
            }
        }
        
        private int parseType(final XmlPullParser xmlPullParser) throws ParserException {
            final String attributeValue = xmlPullParser.getAttributeValue((String)null, "Type");
            if (attributeValue == null) {
                throw new MissingFieldException("Type");
            }
            if ("audio".equalsIgnoreCase(attributeValue)) {
                return 1;
            }
            if ("video".equalsIgnoreCase(attributeValue)) {
                return 2;
            }
            if ("text".equalsIgnoreCase(attributeValue)) {
                return 3;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid key value[");
            sb.append(attributeValue);
            sb.append("]");
            throw new ParserException(sb.toString());
        }
        
        public void addChild(final Object o) {
            if (o instanceof Format) {
                this.formats.add((Format)o);
            }
        }
        
        public Object build() {
            final Format[] array = new Format[this.formats.size()];
            this.formats.toArray(array);
            return new SsManifest.StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, array, this.startTimes, this.lastChunkDuration);
        }
        
        public boolean handleChildInline(final String anObject) {
            return "c".equals(anObject);
        }
        
        public void parseStartTag(final XmlPullParser xmlPullParser) throws ParserException {
            if ("c".equals(xmlPullParser.getName())) {
                this.parseStreamFragmentStartTag(xmlPullParser);
            }
            else {
                this.parseStreamElementStartTag(xmlPullParser);
            }
        }
    }
}
