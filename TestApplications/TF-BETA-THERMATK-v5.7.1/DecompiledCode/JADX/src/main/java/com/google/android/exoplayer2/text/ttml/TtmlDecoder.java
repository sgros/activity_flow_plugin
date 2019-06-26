package com.google.android.exoplayer2.text.ttml;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class TtmlDecoder extends SimpleSubtitleDecoder {
    private static final Pattern CELL_RESOLUTION = Pattern.compile("^(\\d+) (\\d+)$");
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final CellResolution DEFAULT_CELL_RESOLUTION = new CellResolution(32, 15);
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
    private static final Pattern PIXEL_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)px (\\d+\\.?\\d*?)px$");
    private final XmlPullParserFactory xmlParserFactory;

    private static final class CellResolution {
        final int columns;
        final int rows;

        CellResolution(int i, int i2) {
            this.columns = i;
            this.rows = i2;
        }
    }

    private static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float f, int i, int i2) {
            this.effectiveFrameRate = f;
            this.subFrameRate = i;
            this.tickRate = i2;
        }
    }

    private static final class TtsExtent {
        final int height;
        final int width;

        TtsExtent(int i, int i2) {
            this.width = i;
            this.height = i2;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:63:0x0133 in {13, 14, 19, 23, 28, 29, 32, 35, 40, 41, 42, 43, 44, 45, 48, 51, 52, 54, 56, 59, 62} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    protected com.google.android.exoplayer2.text.ttml.TtmlSubtitle decode(byte[] r19, int r20, boolean r21) throws com.google.android.exoplayer2.text.SubtitleDecoderException {
        /*
        r18 = this;
        r8 = r18;
        r0 = r8.xmlParserFactory;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r9 = r0.newPullParser();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r10 = new java.util.HashMap;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r10.<init>();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r11 = new java.util.HashMap;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r11.<init>();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r12 = new java.util.HashMap;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r12.<init>();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = "";	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = new com.google.android.exoplayer2.text.ttml.TtmlRegion;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r2 = 0;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1.<init>(r2);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r11.put(r0, r1);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = new java.io.ByteArrayInputStream;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = 0;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r3 = r19;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r4 = r20;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0.<init>(r3, r1, r4);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r9.setInput(r0, r2);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r13 = new java.util.ArrayDeque;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r13.<init>();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r9.getEventType();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r3 = DEFAULT_FRAME_AND_TICK_RATE;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r4 = DEFAULT_CELL_RESOLUTION;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14 = r2;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r15 = 0;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = 1;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 == r1) goto L_0x011e;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = r13.peek();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = (com.google.android.exoplayer2.text.ttml.TtmlNode) r1;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r6 = 2;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r15 != 0) goto L_0x0107;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r7 = r9.getName();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r5 = "tt";
        if (r0 != r6) goto L_0x00cc;
        r0 = r5.equals(r7);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 == 0) goto L_0x006c;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r3 = r8.parseFrameAndTickRates(r9);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = DEFAULT_CELL_RESOLUTION;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r8.parseCellResolution(r9, r0);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r2 = r8.parseTtsExtent(r9);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r16 = r0;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r17 = r2;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r6 = r3;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x0071;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r17 = r2;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r6 = r3;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r16 = r4;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = isSupportedTag(r7);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r2 = "TtmlDecoder";
        if (r0 != 0) goto L_0x0096;
        r0 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0.<init>();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = "Ignoring unsupported tag: ";	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0.append(r1);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = r9.getName();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0.append(r1);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r0.toString();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        com.google.android.exoplayer2.util.Log.m16i(r2, r0);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r15 = r15 + 1;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r3 = r6;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x0102;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = "head";	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r0.equals(r7);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 == 0) goto L_0x00b0;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = r18;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r2 = r9;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r3 = r10;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r4 = r16;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r5 = r17;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r7 = r6;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r6 = r11;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r20 = r14;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14 = r7;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r7 = r12;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1.parseHeader(r2, r3, r4, r5, r6, r7);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x00bf;
        r20 = r14;
        r14 = r6;
        r0 = r8.parseNode(r9, r1, r11, r14);	 Catch:{ SubtitleDecoderException -> 0x00c3 }
        r13.push(r0);	 Catch:{ SubtitleDecoderException -> 0x00c3 }
        if (r1 == 0) goto L_0x00bf;	 Catch:{ SubtitleDecoderException -> 0x00c3 }
        r1.addChild(r0);	 Catch:{ SubtitleDecoderException -> 0x00c3 }
        r3 = r14;
        r14 = r20;
        goto L_0x0102;
        r0 = move-exception;
        r1 = "Suppressing parser error";	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        com.google.android.exoplayer2.util.Log.m19w(r2, r1, r0);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r15 = r15 + 1;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x00bf;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r20 = r14;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r6 = 4;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 != r6) goto L_0x00dd;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r9.getText();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = com.google.android.exoplayer2.text.ttml.TtmlNode.buildTextNode(r0);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1.addChild(r0);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x00fc;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = 3;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 != r1) goto L_0x00fc;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r9.getName();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r0.equals(r5);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 == 0) goto L_0x00f6;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14 = new com.google.android.exoplayer2.text.ttml.TtmlSubtitle;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r13.peek();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = (com.google.android.exoplayer2.text.ttml.TtmlNode) r0;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14.<init>(r0, r10, r11, r12);	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x00f8;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14 = r20;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r13.pop();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x00fe;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14 = r20;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r17 = r2;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r16 = r4;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r4 = r16;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r2 = r17;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x0115;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r20 = r14;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 != r6) goto L_0x010e;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r15 = r15 + 1;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x0113;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r1 = 3;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        if (r0 != r1) goto L_0x0113;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r15 = r15 + -1;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r14 = r20;	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r9.next();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        r0 = r9.getEventType();	 Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0121 }
        goto L_0x003e;
        r20 = r14;
        return r20;
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r2 = "Unexpected error when reading input.";
        r1.<init>(r2, r0);
        throw r1;
        r0 = move-exception;
        r1 = new com.google.android.exoplayer2.text.SubtitleDecoderException;
        r2 = "Unable to decode source";
        r1.<init>(r2, r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.ttml.TtmlDecoder.decode(byte[], int, boolean):com.google.android.exoplayer2.text.ttml.TtmlSubtitle");
    }

    public TtmlDecoder() {
        super("TtmlDecoder");
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
            this.xmlParserFactory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    private FrameAndTickRate parseFrameAndTickRates(XmlPullParser xmlPullParser) throws SubtitleDecoderException {
        String str = "http://www.w3.org/ns/ttml#parameter";
        String attributeValue = xmlPullParser.getAttributeValue(str, "frameRate");
        int parseInt = attributeValue != null ? Integer.parseInt(attributeValue) : 30;
        float f = 1.0f;
        String attributeValue2 = xmlPullParser.getAttributeValue(str, "frameRateMultiplier");
        if (attributeValue2 != null) {
            String[] split = Util.split(attributeValue2, " ");
            if (split.length == 2) {
                f = ((float) Integer.parseInt(split[0])) / ((float) Integer.parseInt(split[1]));
            } else {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
        }
        int i = DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
        String attributeValue3 = xmlPullParser.getAttributeValue(str, "subFrameRate");
        if (attributeValue3 != null) {
            i = Integer.parseInt(attributeValue3);
        }
        int i2 = DEFAULT_FRAME_AND_TICK_RATE.tickRate;
        String attributeValue4 = xmlPullParser.getAttributeValue(str, "tickRate");
        if (attributeValue4 != null) {
            i2 = Integer.parseInt(attributeValue4);
        }
        return new FrameAndTickRate(((float) parseInt) * f, i, i2);
    }

    private CellResolution parseCellResolution(XmlPullParser xmlPullParser, CellResolution cellResolution) throws SubtitleDecoderException {
        StringBuilder stringBuilder;
        String attributeValue = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "cellResolution");
        if (attributeValue == null) {
            return cellResolution;
        }
        Matcher matcher = CELL_RESOLUTION.matcher(attributeValue);
        String str = "Ignoring malformed cell resolution: ";
        String str2 = "TtmlDecoder";
        if (matcher.matches()) {
            try {
                int parseInt = Integer.parseInt(matcher.group(1));
                int parseInt2 = Integer.parseInt(matcher.group(2));
                if (parseInt != 0 && parseInt2 != 0) {
                    return new CellResolution(parseInt, parseInt2);
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Invalid cell resolution ");
                stringBuilder2.append(parseInt);
                stringBuilder2.append(" ");
                stringBuilder2.append(parseInt2);
                throw new SubtitleDecoderException(stringBuilder2.toString());
            } catch (NumberFormatException unused) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(attributeValue);
                Log.m18w(str2, stringBuilder.toString());
                return cellResolution;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(attributeValue);
        Log.m18w(str2, stringBuilder.toString());
        return cellResolution;
    }

    private TtsExtent parseTtsExtent(XmlPullParser xmlPullParser) {
        StringBuilder stringBuilder;
        String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "extent");
        if (attributeValue == null) {
            return null;
        }
        Matcher matcher = PIXEL_COORDINATES.matcher(attributeValue);
        String str = "TtmlDecoder";
        if (matcher.matches()) {
            try {
                return new TtsExtent(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } catch (NumberFormatException unused) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring malformed tts extent: ");
                stringBuilder.append(attributeValue);
                Log.m18w(str, stringBuilder.toString());
                return null;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Ignoring non-pixel tts extent: ");
        stringBuilder.append(attributeValue);
        Log.m18w(str, stringBuilder.toString());
        return null;
    }

    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlPullParser, Map<String, TtmlStyle> map, CellResolution cellResolution, TtsExtent ttsExtent, Map<String, TtmlRegion> map2, Map<String, String> map3) throws IOException, XmlPullParserException {
        do {
            xmlPullParser.next();
            String str = "style";
            if (XmlPullParserUtil.isStartTag(xmlPullParser, str)) {
                str = XmlPullParserUtil.getAttributeValue(xmlPullParser, str);
                TtmlStyle parseStyleAttributes = parseStyleAttributes(xmlPullParser, new TtmlStyle());
                if (str != null) {
                    for (Object obj : parseStyleIds(str)) {
                        parseStyleAttributes.chain((TtmlStyle) map.get(obj));
                    }
                }
                if (parseStyleAttributes.getId() != null) {
                    map.put(parseStyleAttributes.getId(), parseStyleAttributes);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "region")) {
                TtmlRegion parseRegionAttributes = parseRegionAttributes(xmlPullParser, cellResolution, ttsExtent);
                if (parseRegionAttributes != null) {
                    map2.put(parseRegionAttributes.f29id, parseRegionAttributes);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "metadata")) {
                parseMetadata(xmlPullParser, map3);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "head"));
        return map;
    }

    private void parseMetadata(XmlPullParser xmlPullParser, Map<String, String> map) throws IOException, XmlPullParserException {
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "image")) {
                String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "id");
                if (attributeValue != null) {
                    map.put(attributeValue, xmlPullParser.nextText());
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "metadata"));
    }

    private TtmlRegion parseRegionAttributes(XmlPullParser xmlPullParser, CellResolution cellResolution, TtsExtent ttsExtent) {
        StringBuilder stringBuilder;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        TtsExtent ttsExtent2 = ttsExtent;
        String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser2, "id");
        TtmlRegion ttmlRegion = null;
        if (attributeValue == null) {
            return null;
        }
        String attributeValue2 = XmlPullParserUtil.getAttributeValue(xmlPullParser2, "origin");
        String str = "TtmlDecoder";
        if (attributeValue2 != null) {
            float parseFloat;
            float f;
            Matcher matcher = PERCENTAGE_COORDINATES.matcher(attributeValue2);
            Matcher matcher2 = PIXEL_COORDINATES.matcher(attributeValue2);
            String str2 = "Ignoring region with malformed origin: ";
            String str3 = "Ignoring region with missing tts:extent: ";
            if (matcher.matches()) {
                try {
                    float parseFloat2 = Float.parseFloat(matcher.group(1)) / 100.0f;
                    parseFloat = Float.parseFloat(matcher.group(2)) / 100.0f;
                    f = parseFloat2;
                } catch (NumberFormatException unused) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(attributeValue2);
                    Log.m18w(str, stringBuilder.toString());
                    return null;
                }
            } else if (!matcher2.matches()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring region with unsupported origin: ");
                stringBuilder.append(attributeValue2);
                Log.m18w(str, stringBuilder.toString());
                return null;
            } else if (ttsExtent2 == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(attributeValue2);
                Log.m18w(str, stringBuilder.toString());
                return null;
            } else {
                try {
                    int parseInt = Integer.parseInt(matcher2.group(1));
                    f = ((float) parseInt) / ((float) ttsExtent2.width);
                    parseFloat = ((float) Integer.parseInt(matcher2.group(2))) / ((float) ttsExtent2.height);
                } catch (NumberFormatException unused2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(attributeValue2);
                    Log.m18w(str, stringBuilder.toString());
                    return null;
                }
            }
            String attributeValue3 = XmlPullParserUtil.getAttributeValue(xmlPullParser2, "extent");
            if (attributeValue3 != null) {
                float f2;
                float f3;
                int i;
                Matcher matcher3 = PERCENTAGE_COORDINATES.matcher(attributeValue3);
                Matcher matcher4 = PIXEL_COORDINATES.matcher(attributeValue3);
                String str4 = "Ignoring region with malformed extent: ";
                if (matcher3.matches()) {
                    try {
                        float parseFloat3 = Float.parseFloat(matcher3.group(1)) / 100.0f;
                        ttmlRegion = Float.parseFloat(matcher3.group(2));
                        f2 = ttmlRegion / 100.0f;
                        f3 = parseFloat3;
                    } catch (NumberFormatException unused3) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(attributeValue2);
                        Log.m18w(str, stringBuilder.toString());
                        return ttmlRegion;
                    }
                } else if (!matcher4.matches()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Ignoring region with unsupported extent: ");
                    stringBuilder.append(attributeValue2);
                    Log.m18w(str, stringBuilder.toString());
                    return null;
                } else if (ttsExtent2 == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(attributeValue2);
                    Log.m18w(str, stringBuilder.toString());
                    return null;
                } else {
                    try {
                        int parseInt2 = Integer.parseInt(matcher4.group(1));
                        f3 = ((float) parseInt2) / ((float) ttsExtent2.width);
                        f2 = ((float) Integer.parseInt(matcher4.group(2))) / ((float) ttsExtent2.height);
                    } catch (NumberFormatException unused4) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(attributeValue2);
                        Log.m18w(str, stringBuilder.toString());
                        return null;
                    }
                }
                String attributeValue4 = XmlPullParserUtil.getAttributeValue(xmlPullParser2, "displayAlign");
                if (attributeValue4 != null) {
                    attributeValue4 = Util.toLowerInvariant(attributeValue4);
                    Object obj = -1;
                    int hashCode = attributeValue4.hashCode();
                    if (hashCode != -1364013995) {
                        if (hashCode == 92734940 && attributeValue4.equals("after")) {
                            obj = 1;
                        }
                    } else if (attributeValue4.equals("center")) {
                        obj = null;
                    }
                    if (obj == null) {
                        parseFloat += f2 / 2.0f;
                        i = 1;
                    } else if (obj == 1) {
                        parseFloat += f2;
                        i = 2;
                    }
                    return new TtmlRegion(attributeValue, f, parseFloat, 0, i, f3, 1, 1.0f / ((float) cellResolution.rows));
                }
                i = 0;
                return new TtmlRegion(attributeValue, f, parseFloat, 0, i, f3, 1, 1.0f / ((float) cellResolution.rows));
            }
            Log.m18w(str, "Ignoring region without an extent");
            return null;
        }
        Log.m18w(str, "Ignoring region without an origin");
        return null;
    }

    private String[] parseStyleIds(String str) {
        str = str.trim();
        return str.isEmpty() ? new String[0] : Util.split(str, "\\s+");
    }

    private com.google.android.exoplayer2.text.ttml.TtmlStyle parseStyleAttributes(org.xmlpull.v1.XmlPullParser r12, com.google.android.exoplayer2.text.ttml.TtmlStyle r13) {
        /*
        r11 = this;
        r0 = r12.getAttributeCount();
        r1 = 0;
        r2 = r13;
        r13 = 0;
    L_0x0007:
        if (r13 >= r0) goto L_0x01ff;
    L_0x0009:
        r3 = r12.getAttributeValue(r13);
        r4 = r12.getAttributeName(r13);
        r5 = r4.hashCode();
        r6 = 4;
        r7 = -1;
        r8 = 2;
        r9 = 3;
        r10 = 1;
        switch(r5) {
            case -1550943582: goto L_0x006f;
            case -1224696685: goto L_0x0065;
            case -1065511464: goto L_0x005b;
            case -879295043: goto L_0x0050;
            case -734428249: goto L_0x0046;
            case 3355: goto L_0x003c;
            case 94842723: goto L_0x0032;
            case 365601008: goto L_0x0028;
            case 1287124693: goto L_0x001e;
            default: goto L_0x001d;
        };
    L_0x001d:
        goto L_0x0079;
    L_0x001e:
        r5 = "backgroundColor";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x0026:
        r4 = 1;
        goto L_0x007a;
    L_0x0028:
        r5 = "fontSize";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x0030:
        r4 = 4;
        goto L_0x007a;
    L_0x0032:
        r5 = "color";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x003a:
        r4 = 2;
        goto L_0x007a;
    L_0x003c:
        r5 = "id";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x0044:
        r4 = 0;
        goto L_0x007a;
    L_0x0046:
        r5 = "fontWeight";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x004e:
        r4 = 5;
        goto L_0x007a;
    L_0x0050:
        r5 = "textDecoration";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x0058:
        r4 = 8;
        goto L_0x007a;
    L_0x005b:
        r5 = "textAlign";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x0063:
        r4 = 7;
        goto L_0x007a;
    L_0x0065:
        r5 = "fontFamily";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x006d:
        r4 = 3;
        goto L_0x007a;
    L_0x006f:
        r5 = "fontStyle";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0079;
    L_0x0077:
        r4 = 6;
        goto L_0x007a;
    L_0x0079:
        r4 = -1;
    L_0x007a:
        r5 = "TtmlDecoder";
        switch(r4) {
            case 0: goto L_0x01e8;
            case 1: goto L_0x01c7;
            case 2: goto L_0x01a6;
            case 3: goto L_0x019e;
            case 4: goto L_0x0180;
            case 5: goto L_0x0171;
            case 6: goto L_0x0162;
            case 7: goto L_0x00e2;
            case 8: goto L_0x0081;
            default: goto L_0x007f;
        };
    L_0x007f:
        goto L_0x01fb;
    L_0x0081:
        r3 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r3);
        r4 = r3.hashCode();
        switch(r4) {
            case -1461280213: goto L_0x00ab;
            case -1026963764: goto L_0x00a1;
            case 913457136: goto L_0x0097;
            case 1679736913: goto L_0x008d;
            default: goto L_0x008c;
        };
    L_0x008c:
        goto L_0x00b4;
    L_0x008d:
        r4 = "linethrough";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x00b4;
    L_0x0095:
        r7 = 0;
        goto L_0x00b4;
    L_0x0097:
        r4 = "nolinethrough";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x00b4;
    L_0x009f:
        r7 = 1;
        goto L_0x00b4;
    L_0x00a1:
        r4 = "underline";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x00b4;
    L_0x00a9:
        r7 = 2;
        goto L_0x00b4;
    L_0x00ab:
        r4 = "nounderline";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x00b4;
    L_0x00b3:
        r7 = 3;
    L_0x00b4:
        if (r7 == 0) goto L_0x00d9;
    L_0x00b6:
        if (r7 == r10) goto L_0x00d0;
    L_0x00b8:
        if (r7 == r8) goto L_0x00c7;
    L_0x00ba:
        if (r7 == r9) goto L_0x00be;
    L_0x00bc:
        goto L_0x01fb;
    L_0x00be:
        r2 = r11.createIfNull(r2);
        r2.setUnderline(r1);
        goto L_0x01fb;
    L_0x00c7:
        r2 = r11.createIfNull(r2);
        r2.setUnderline(r10);
        goto L_0x01fb;
    L_0x00d0:
        r2 = r11.createIfNull(r2);
        r2.setLinethrough(r1);
        goto L_0x01fb;
    L_0x00d9:
        r2 = r11.createIfNull(r2);
        r2.setLinethrough(r10);
        goto L_0x01fb;
    L_0x00e2:
        r3 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r3);
        r4 = r3.hashCode();
        switch(r4) {
            case -1364013995: goto L_0x0116;
            case 100571: goto L_0x010c;
            case 3317767: goto L_0x0102;
            case 108511772: goto L_0x00f8;
            case 109757538: goto L_0x00ee;
            default: goto L_0x00ed;
        };
    L_0x00ed:
        goto L_0x011f;
    L_0x00ee:
        r4 = "start";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x011f;
    L_0x00f6:
        r7 = 1;
        goto L_0x011f;
    L_0x00f8:
        r4 = "right";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x011f;
    L_0x0100:
        r7 = 2;
        goto L_0x011f;
    L_0x0102:
        r4 = "left";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x011f;
    L_0x010a:
        r7 = 0;
        goto L_0x011f;
    L_0x010c:
        r4 = "end";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x011f;
    L_0x0114:
        r7 = 3;
        goto L_0x011f;
    L_0x0116:
        r4 = "center";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x011f;
    L_0x011e:
        r7 = 4;
    L_0x011f:
        if (r7 == 0) goto L_0x0157;
    L_0x0121:
        if (r7 == r10) goto L_0x014c;
    L_0x0123:
        if (r7 == r8) goto L_0x0141;
    L_0x0125:
        if (r7 == r9) goto L_0x0136;
    L_0x0127:
        if (r7 == r6) goto L_0x012b;
    L_0x0129:
        goto L_0x01fb;
    L_0x012b:
        r2 = r11.createIfNull(r2);
        r3 = android.text.Layout.Alignment.ALIGN_CENTER;
        r2.setTextAlign(r3);
        goto L_0x01fb;
    L_0x0136:
        r2 = r11.createIfNull(r2);
        r3 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        r2.setTextAlign(r3);
        goto L_0x01fb;
    L_0x0141:
        r2 = r11.createIfNull(r2);
        r3 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        r2.setTextAlign(r3);
        goto L_0x01fb;
    L_0x014c:
        r2 = r11.createIfNull(r2);
        r3 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r2.setTextAlign(r3);
        goto L_0x01fb;
    L_0x0157:
        r2 = r11.createIfNull(r2);
        r3 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r2.setTextAlign(r3);
        goto L_0x01fb;
    L_0x0162:
        r2 = r11.createIfNull(r2);
        r4 = "italic";
        r3 = r4.equalsIgnoreCase(r3);
        r2.setItalic(r3);
        goto L_0x01fb;
    L_0x0171:
        r2 = r11.createIfNull(r2);
        r4 = "bold";
        r3 = r4.equalsIgnoreCase(r3);
        r2.setBold(r3);
        goto L_0x01fb;
    L_0x0180:
        r2 = r11.createIfNull(r2);	 Catch:{ SubtitleDecoderException -> 0x0189 }
        parseFontSize(r3, r2);	 Catch:{ SubtitleDecoderException -> 0x0189 }
        goto L_0x01fb;
    L_0x0189:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "Failed parsing fontSize value: ";
        r4.append(r6);
        r4.append(r3);
        r3 = r4.toString();
        com.google.android.exoplayer2.util.Log.m18w(r5, r3);
        goto L_0x01fb;
    L_0x019e:
        r2 = r11.createIfNull(r2);
        r2.setFontFamily(r3);
        goto L_0x01fb;
    L_0x01a6:
        r2 = r11.createIfNull(r2);
        r4 = com.google.android.exoplayer2.util.ColorParser.parseTtmlColor(r3);	 Catch:{ IllegalArgumentException -> 0x01b2 }
        r2.setFontColor(r4);	 Catch:{ IllegalArgumentException -> 0x01b2 }
        goto L_0x01fb;
    L_0x01b2:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "Failed parsing color value: ";
        r4.append(r6);
        r4.append(r3);
        r3 = r4.toString();
        com.google.android.exoplayer2.util.Log.m18w(r5, r3);
        goto L_0x01fb;
    L_0x01c7:
        r2 = r11.createIfNull(r2);
        r4 = com.google.android.exoplayer2.util.ColorParser.parseTtmlColor(r3);	 Catch:{ IllegalArgumentException -> 0x01d3 }
        r2.setBackgroundColor(r4);	 Catch:{ IllegalArgumentException -> 0x01d3 }
        goto L_0x01fb;
    L_0x01d3:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "Failed parsing background value: ";
        r4.append(r6);
        r4.append(r3);
        r3 = r4.toString();
        com.google.android.exoplayer2.util.Log.m18w(r5, r3);
        goto L_0x01fb;
    L_0x01e8:
        r4 = r12.getName();
        r5 = "style";
        r4 = r5.equals(r4);
        if (r4 == 0) goto L_0x01fb;
    L_0x01f4:
        r2 = r11.createIfNull(r2);
        r2.setId(r3);
    L_0x01fb:
        r13 = r13 + 1;
        goto L_0x0007;
    L_0x01ff:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.ttml.TtmlDecoder.parseStyleAttributes(org.xmlpull.v1.XmlPullParser, com.google.android.exoplayer2.text.ttml.TtmlStyle):com.google.android.exoplayer2.text.ttml.TtmlStyle");
    }

    private TtmlStyle createIfNull(TtmlStyle ttmlStyle) {
        return ttmlStyle == null ? new TtmlStyle() : ttmlStyle;
    }

    private com.google.android.exoplayer2.text.ttml.TtmlNode parseNode(org.xmlpull.v1.XmlPullParser r21, com.google.android.exoplayer2.text.ttml.TtmlNode r22, java.util.Map<java.lang.String, com.google.android.exoplayer2.text.ttml.TtmlRegion> r23, com.google.android.exoplayer2.text.ttml.TtmlDecoder.FrameAndTickRate r24) throws com.google.android.exoplayer2.text.SubtitleDecoderException {
        /*
        r20 = this;
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r24;
        r4 = r21.getAttributeCount();
        r5 = 0;
        r11 = r0.parseStyleAttributes(r1, r5);
        r9 = "";
        r17 = r5;
        r18 = r17;
        r16 = r9;
        r5 = 0;
        r9 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r12 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r14 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
    L_0x0029:
        if (r5 >= r4) goto L_0x00ce;
    L_0x002b:
        r6 = r1.getAttributeName(r5);
        r7 = r1.getAttributeValue(r5);
        r19 = r6.hashCode();
        switch(r19) {
            case -934795532: goto L_0x006d;
            case 99841: goto L_0x0063;
            case 100571: goto L_0x0059;
            case 93616297: goto L_0x004f;
            case 109780401: goto L_0x0045;
            case 1292595405: goto L_0x003b;
            default: goto L_0x003a;
        };
    L_0x003a:
        goto L_0x0077;
    L_0x003b:
        r8 = "backgroundImage";
        r6 = r6.equals(r8);
        if (r6 == 0) goto L_0x0077;
    L_0x0043:
        r6 = 5;
        goto L_0x0078;
    L_0x0045:
        r8 = "style";
        r6 = r6.equals(r8);
        if (r6 == 0) goto L_0x0077;
    L_0x004d:
        r6 = 3;
        goto L_0x0078;
    L_0x004f:
        r8 = "begin";
        r6 = r6.equals(r8);
        if (r6 == 0) goto L_0x0077;
    L_0x0057:
        r6 = 0;
        goto L_0x0078;
    L_0x0059:
        r8 = "end";
        r6 = r6.equals(r8);
        if (r6 == 0) goto L_0x0077;
    L_0x0061:
        r6 = 1;
        goto L_0x0078;
    L_0x0063:
        r8 = "dur";
        r6 = r6.equals(r8);
        if (r6 == 0) goto L_0x0077;
    L_0x006b:
        r6 = 2;
        goto L_0x0078;
    L_0x006d:
        r8 = "region";
        r6 = r6.equals(r8);
        if (r6 == 0) goto L_0x0077;
    L_0x0075:
        r6 = 4;
        goto L_0x0078;
    L_0x0077:
        r6 = -1;
    L_0x0078:
        if (r6 == 0) goto L_0x00c3;
    L_0x007a:
        r8 = 1;
        if (r6 == r8) goto L_0x00bb;
    L_0x007d:
        r8 = 2;
        if (r6 == r8) goto L_0x00b3;
    L_0x0080:
        r8 = 3;
        if (r6 == r8) goto L_0x00a7;
    L_0x0083:
        r8 = 4;
        if (r6 == r8) goto L_0x009c;
    L_0x0086:
        r8 = 5;
        if (r6 == r8) goto L_0x008a;
    L_0x0089:
        goto L_0x0099;
    L_0x008a:
        r6 = "#";
        r6 = r7.startsWith(r6);
        if (r6 == 0) goto L_0x0099;
    L_0x0092:
        r6 = 1;
        r6 = r7.substring(r6);
        r17 = r6;
    L_0x0099:
        r6 = r23;
        goto L_0x00ca;
    L_0x009c:
        r6 = r23;
        r8 = r6.containsKey(r7);
        if (r8 == 0) goto L_0x00ca;
    L_0x00a4:
        r16 = r7;
        goto L_0x00ca;
    L_0x00a7:
        r6 = r23;
        r7 = r0.parseStyleIds(r7);
        r8 = r7.length;
        if (r8 <= 0) goto L_0x00ca;
    L_0x00b0:
        r18 = r7;
        goto L_0x00ca;
    L_0x00b3:
        r6 = r23;
        r7 = parseTimeExpression(r7, r3);
        r14 = r7;
        goto L_0x00ca;
    L_0x00bb:
        r6 = r23;
        r7 = parseTimeExpression(r7, r3);
        r12 = r7;
        goto L_0x00ca;
    L_0x00c3:
        r6 = r23;
        r7 = parseTimeExpression(r7, r3);
        r9 = r7;
    L_0x00ca:
        r5 = r5 + 1;
        goto L_0x0029;
    L_0x00ce:
        if (r2 == 0) goto L_0x00e8;
    L_0x00d0:
        r3 = r2.startTimeUs;
        r5 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 == 0) goto L_0x00ed;
    L_0x00db:
        r7 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1));
        if (r7 == 0) goto L_0x00e0;
    L_0x00df:
        r9 = r9 + r3;
    L_0x00e0:
        r3 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1));
        if (r3 == 0) goto L_0x00ed;
    L_0x00e4:
        r3 = r2.startTimeUs;
        r12 = r12 + r3;
        goto L_0x00ed;
    L_0x00e8:
        r5 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
    L_0x00ed:
        r7 = r9;
        r3 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x0103;
    L_0x00f2:
        r3 = (r14 > r5 ? 1 : (r14 == r5 ? 0 : -1));
        if (r3 == 0) goto L_0x00f9;
    L_0x00f6:
        r14 = r14 + r7;
        r9 = r14;
        goto L_0x0104;
    L_0x00f9:
        if (r2 == 0) goto L_0x0103;
    L_0x00fb:
        r2 = r2.endTimeUs;
        r4 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x0103;
    L_0x0101:
        r9 = r2;
        goto L_0x0104;
    L_0x0103:
        r9 = r12;
    L_0x0104:
        r6 = r21.getName();
        r12 = r18;
        r13 = r16;
        r14 = r17;
        r1 = com.google.android.exoplayer2.text.ttml.TtmlNode.buildNode(r6, r7, r9, r11, r12, r13, r14);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.ttml.TtmlDecoder.parseNode(org.xmlpull.v1.XmlPullParser, com.google.android.exoplayer2.text.ttml.TtmlNode, java.util.Map, com.google.android.exoplayer2.text.ttml.TtmlDecoder$FrameAndTickRate):com.google.android.exoplayer2.text.ttml.TtmlNode");
    }

    private static boolean isSupportedTag(String str) {
        return str.equals("tt") || str.equals("head") || str.equals("body") || str.equals("div") || str.equals("p") || str.equals("span") || str.equals("br") || str.equals("style") || str.equals("styling") || str.equals("layout") || str.equals("region") || str.equals("metadata") || str.equals("image") || str.equals("data") || str.equals("information");
    }

    private static void parseFontSize(String str, TtmlStyle ttmlStyle) throws SubtitleDecoderException {
        Matcher matcher;
        StringBuilder stringBuilder;
        String[] split = Util.split(str, "\\s+");
        if (split.length == 1) {
            matcher = FONT_SIZE.matcher(str);
        } else if (split.length == 2) {
            matcher = FONT_SIZE.matcher(split[1]);
            Log.m18w("TtmlDecoder", "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid number of entries for fontSize: ");
            stringBuilder.append(split.length);
            stringBuilder.append(".");
            throw new SubtitleDecoderException(stringBuilder.toString());
        }
        String str2 = "'.";
        if (matcher.matches()) {
            String group = matcher.group(3);
            Object obj = -1;
            int hashCode = group.hashCode();
            if (hashCode != 37) {
                if (hashCode != 3240) {
                    if (hashCode == 3592 && group.equals("px")) {
                        obj = null;
                    }
                } else if (group.equals("em")) {
                    obj = 1;
                }
            } else if (group.equals("%")) {
                obj = 2;
            }
            if (obj == null) {
                ttmlStyle.setFontSizeUnit(1);
            } else if (obj == 1) {
                ttmlStyle.setFontSizeUnit(2);
            } else if (obj == 2) {
                ttmlStyle.setFontSizeUnit(3);
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid unit for fontSize: '");
                stringBuilder.append(group);
                stringBuilder.append(str2);
                throw new SubtitleDecoderException(stringBuilder.toString());
            }
            ttmlStyle.setFontSize(Float.valueOf(matcher.group(1)).floatValue());
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid expression for fontSize: '");
        stringBuilder2.append(str);
        stringBuilder2.append(str2);
        throw new SubtitleDecoderException(stringBuilder2.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0120  */
    private static long parseTimeExpression(java.lang.String r14, com.google.android.exoplayer2.text.ttml.TtmlDecoder.FrameAndTickRate r15) throws com.google.android.exoplayer2.text.SubtitleDecoderException {
        /*
        r0 = CLOCK_TIME;
        r0 = r0.matcher(r14);
        r1 = r0.matches();
        r2 = 4696837146684686336; // 0x412e848000000000 float:0.0 double:1000000.0;
        r4 = 5;
        r5 = 4;
        r6 = 3;
        r7 = 2;
        r8 = 1;
        if (r1 == 0) goto L_0x0088;
    L_0x0016:
        r14 = r0.group(r8);
        r8 = java.lang.Long.parseLong(r14);
        r10 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
        r8 = r8 * r10;
        r8 = (double) r8;
        r14 = r0.group(r7);
        r10 = java.lang.Long.parseLong(r14);
        r12 = 60;
        r10 = r10 * r12;
        r10 = (double) r10;
        java.lang.Double.isNaN(r8);
        java.lang.Double.isNaN(r10);
        r8 = r8 + r10;
        r14 = r0.group(r6);
        r6 = java.lang.Long.parseLong(r14);
        r6 = (double) r6;
        java.lang.Double.isNaN(r6);
        r8 = r8 + r6;
        r14 = r0.group(r5);
        r5 = 0;
        if (r14 == 0) goto L_0x0051;
    L_0x004c:
        r10 = java.lang.Double.parseDouble(r14);
        goto L_0x0052;
    L_0x0051:
        r10 = r5;
    L_0x0052:
        r8 = r8 + r10;
        r14 = r0.group(r4);
        if (r14 == 0) goto L_0x0063;
    L_0x0059:
        r10 = java.lang.Long.parseLong(r14);
        r14 = (float) r10;
        r1 = r15.effectiveFrameRate;
        r14 = r14 / r1;
        r10 = (double) r14;
        goto L_0x0064;
    L_0x0063:
        r10 = r5;
    L_0x0064:
        r8 = r8 + r10;
        r14 = 6;
        r14 = r0.group(r14);
        if (r14 == 0) goto L_0x0083;
    L_0x006c:
        r0 = java.lang.Long.parseLong(r14);
        r0 = (double) r0;
        r14 = r15.subFrameRate;
        r4 = (double) r14;
        java.lang.Double.isNaN(r0);
        java.lang.Double.isNaN(r4);
        r0 = r0 / r4;
        r14 = r15.effectiveFrameRate;
        r14 = (double) r14;
        java.lang.Double.isNaN(r14);
        r5 = r0 / r14;
    L_0x0083:
        r8 = r8 + r5;
        r8 = r8 * r2;
        r14 = (long) r8;
        return r14;
    L_0x0088:
        r0 = OFFSET_TIME;
        r0 = r0.matcher(r14);
        r1 = r0.matches();
        if (r1 == 0) goto L_0x012b;
    L_0x0094:
        r14 = r0.group(r8);
        r9 = java.lang.Double.parseDouble(r14);
        r14 = r0.group(r7);
        r0 = -1;
        r1 = r14.hashCode();
        r11 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r1 == r11) goto L_0x00f0;
    L_0x00a9:
        r11 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        if (r1 == r11) goto L_0x00e6;
    L_0x00ad:
        r11 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        if (r1 == r11) goto L_0x00dc;
    L_0x00b1:
        r11 = 3494; // 0xda6 float:4.896E-42 double:1.7263E-320;
        if (r1 == r11) goto L_0x00d2;
    L_0x00b5:
        r11 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r1 == r11) goto L_0x00c8;
    L_0x00b9:
        r11 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r1 == r11) goto L_0x00be;
    L_0x00bd:
        goto L_0x00fa;
    L_0x00be:
        r1 = "t";
        r14 = r14.equals(r1);
        if (r14 == 0) goto L_0x00fa;
    L_0x00c6:
        r14 = 5;
        goto L_0x00fb;
    L_0x00c8:
        r1 = "s";
        r14 = r14.equals(r1);
        if (r14 == 0) goto L_0x00fa;
    L_0x00d0:
        r14 = 2;
        goto L_0x00fb;
    L_0x00d2:
        r1 = "ms";
        r14 = r14.equals(r1);
        if (r14 == 0) goto L_0x00fa;
    L_0x00da:
        r14 = 3;
        goto L_0x00fb;
    L_0x00dc:
        r1 = "m";
        r14 = r14.equals(r1);
        if (r14 == 0) goto L_0x00fa;
    L_0x00e4:
        r14 = 1;
        goto L_0x00fb;
    L_0x00e6:
        r1 = "h";
        r14 = r14.equals(r1);
        if (r14 == 0) goto L_0x00fa;
    L_0x00ee:
        r14 = 0;
        goto L_0x00fb;
    L_0x00f0:
        r1 = "f";
        r14 = r14.equals(r1);
        if (r14 == 0) goto L_0x00fa;
    L_0x00f8:
        r14 = 4;
        goto L_0x00fb;
    L_0x00fa:
        r14 = -1;
    L_0x00fb:
        if (r14 == 0) goto L_0x0120;
    L_0x00fd:
        if (r14 == r8) goto L_0x011d;
    L_0x00ff:
        if (r14 == r7) goto L_0x0127;
    L_0x0101:
        if (r14 == r6) goto L_0x0116;
    L_0x0103:
        if (r14 == r5) goto L_0x010f;
    L_0x0105:
        if (r14 == r4) goto L_0x0108;
    L_0x0107:
        goto L_0x0127;
    L_0x0108:
        r14 = r15.tickRate;
        r14 = (double) r14;
        java.lang.Double.isNaN(r14);
        goto L_0x011b;
    L_0x010f:
        r14 = r15.effectiveFrameRate;
        r14 = (double) r14;
        java.lang.Double.isNaN(r14);
        goto L_0x011b;
    L_0x0116:
        r14 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
    L_0x011b:
        r9 = r9 / r14;
        goto L_0x0127;
    L_0x011d:
        r14 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
        goto L_0x0125;
    L_0x0120:
        r14 = 4660134898793709568; // 0x40ac200000000000 float:0.0 double:3600.0;
    L_0x0125:
        r9 = r9 * r14;
    L_0x0127:
        r9 = r9 * r2;
        r14 = (long) r9;
        return r14;
    L_0x012b:
        r15 = new com.google.android.exoplayer2.text.SubtitleDecoderException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Malformed time expression: ";
        r0.append(r1);
        r0.append(r14);
        r14 = r0.toString();
        r15.<init>(r14);
        throw r15;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.ttml.TtmlDecoder.parseTimeExpression(java.lang.String, com.google.android.exoplayer2.text.ttml.TtmlDecoder$FrameAndTickRate):long");
    }
}
