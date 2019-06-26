// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ttml;

import java.util.ArrayDeque;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.ColorParser;
import android.text.Layout$Alignment;
import java.io.IOException;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.util.Map;
import com.google.android.exoplayer2.util.Util;
import java.util.regex.Matcher;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class TtmlDecoder extends SimpleSubtitleDecoder
{
    private static final Pattern CELL_RESOLUTION;
    private static final Pattern CLOCK_TIME;
    private static final CellResolution DEFAULT_CELL_RESOLUTION;
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE;
    private static final Pattern FONT_SIZE;
    private static final Pattern OFFSET_TIME;
    private static final Pattern PERCENTAGE_COORDINATES;
    private static final Pattern PIXEL_COORDINATES;
    private final XmlPullParserFactory xmlParserFactory;
    
    static {
        CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
        OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
        FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
        PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
        PIXEL_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)px (\\d+\\.?\\d*?)px$");
        CELL_RESOLUTION = Pattern.compile("^(\\d+) (\\d+)$");
        DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
        DEFAULT_CELL_RESOLUTION = new CellResolution(32, 15);
    }
    
    public TtmlDecoder() {
        super("TtmlDecoder");
        try {
            (this.xmlParserFactory = XmlPullParserFactory.newInstance()).setNamespaceAware(true);
        }
        catch (XmlPullParserException cause) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", (Throwable)cause);
        }
    }
    
    private TtmlStyle createIfNull(final TtmlStyle ttmlStyle) {
        TtmlStyle ttmlStyle2 = ttmlStyle;
        if (ttmlStyle == null) {
            ttmlStyle2 = new TtmlStyle();
        }
        return ttmlStyle2;
    }
    
    private static boolean isSupportedTag(final String s) {
        return s.equals("tt") || s.equals("head") || s.equals("body") || s.equals("div") || s.equals("p") || s.equals("span") || s.equals("br") || s.equals("style") || s.equals("styling") || s.equals("layout") || s.equals("region") || s.equals("metadata") || s.equals("image") || s.equals("data") || s.equals("information");
    }
    
    private CellResolution parseCellResolution(XmlPullParser attributeValue, final CellResolution cellResolution) throws SubtitleDecoderException {
        attributeValue = (XmlPullParser)attributeValue.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "cellResolution");
        if (attributeValue == null) {
            return cellResolution;
        }
        final Matcher matcher = TtmlDecoder.CELL_RESOLUTION.matcher((CharSequence)attributeValue);
        if (!matcher.matches()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring malformed cell resolution: ");
            sb.append((String)attributeValue);
            Log.w("TtmlDecoder", sb.toString());
            return cellResolution;
        }
        try {
            final int int1 = Integer.parseInt(matcher.group(1));
            final int int2 = Integer.parseInt(matcher.group(2));
            if (int1 != 0 && int2 != 0) {
                return new CellResolution(int1, int2);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid cell resolution ");
            sb2.append(int1);
            sb2.append(" ");
            sb2.append(int2);
            throw new SubtitleDecoderException(sb2.toString());
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Ignoring malformed cell resolution: ");
            sb3.append((String)attributeValue);
            Log.w("TtmlDecoder", sb3.toString());
            return cellResolution;
        }
    }
    
    private static void parseFontSize(String group, final TtmlStyle ttmlStyle) throws SubtitleDecoderException {
        final String[] split = Util.split(group, "\\s+");
        Matcher matcher;
        if (split.length == 1) {
            matcher = TtmlDecoder.FONT_SIZE.matcher(group);
        }
        else {
            if (split.length != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid number of entries for fontSize: ");
                sb.append(split.length);
                sb.append(".");
                throw new SubtitleDecoderException(sb.toString());
            }
            matcher = TtmlDecoder.FONT_SIZE.matcher(split[1]);
            Log.w("TtmlDecoder", "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        }
        if (matcher.matches()) {
            group = matcher.group(3);
            int n = -1;
            final int hashCode = group.hashCode();
            if (hashCode != 37) {
                if (hashCode != 3240) {
                    if (hashCode == 3592) {
                        if (group.equals("px")) {
                            n = 0;
                        }
                    }
                }
                else if (group.equals("em")) {
                    n = 1;
                }
            }
            else if (group.equals("%")) {
                n = 2;
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Invalid unit for fontSize: '");
                        sb2.append(group);
                        sb2.append("'.");
                        throw new SubtitleDecoderException(sb2.toString());
                    }
                    ttmlStyle.setFontSizeUnit(3);
                }
                else {
                    ttmlStyle.setFontSizeUnit(2);
                }
            }
            else {
                ttmlStyle.setFontSizeUnit(1);
            }
            ttmlStyle.setFontSize(Float.valueOf(matcher.group(1)));
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Invalid expression for fontSize: '");
        sb3.append(group);
        sb3.append("'.");
        throw new SubtitleDecoderException(sb3.toString());
    }
    
    private FrameAndTickRate parseFrameAndTickRates(final XmlPullParser xmlPullParser) throws SubtitleDecoderException {
        final String attributeValue = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRate");
        int int1;
        if (attributeValue != null) {
            int1 = Integer.parseInt(attributeValue);
        }
        else {
            int1 = 30;
        }
        float n = 1.0f;
        final String attributeValue2 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "frameRateMultiplier");
        if (attributeValue2 != null) {
            final String[] split = Util.split(attributeValue2, " ");
            if (split.length != 2) {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
            n = Integer.parseInt(split[0]) / (float)Integer.parseInt(split[1]);
        }
        int n2 = TtmlDecoder.DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
        final String attributeValue3 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "subFrameRate");
        if (attributeValue3 != null) {
            n2 = Integer.parseInt(attributeValue3);
        }
        int n3 = TtmlDecoder.DEFAULT_FRAME_AND_TICK_RATE.tickRate;
        final String attributeValue4 = xmlPullParser.getAttributeValue("http://www.w3.org/ns/ttml#parameter", "tickRate");
        if (attributeValue4 != null) {
            n3 = Integer.parseInt(attributeValue4);
        }
        return new FrameAndTickRate(int1 * n, n2, n3);
    }
    
    private Map<String, TtmlStyle> parseHeader(final XmlPullParser xmlPullParser, final Map<String, TtmlStyle> map, final CellResolution cellResolution, final TtsExtent ttsExtent, final Map<String, TtmlRegion> map2, final Map<String, String> map3) throws IOException, XmlPullParserException {
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "style")) {
                final String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "style");
                final TtmlStyle styleAttributes = this.parseStyleAttributes(xmlPullParser, new TtmlStyle());
                if (attributeValue != null) {
                    final String[] styleIds = this.parseStyleIds(attributeValue);
                    for (int length = styleIds.length, i = 0; i < length; ++i) {
                        styleAttributes.chain(map.get(styleIds[i]));
                    }
                }
                if (styleAttributes.getId() == null) {
                    continue;
                }
                map.put(styleAttributes.getId(), styleAttributes);
            }
            else if (XmlPullParserUtil.isStartTag(xmlPullParser, "region")) {
                final TtmlRegion regionAttributes = this.parseRegionAttributes(xmlPullParser, cellResolution, ttsExtent);
                if (regionAttributes == null) {
                    continue;
                }
                map2.put(regionAttributes.id, regionAttributes);
            }
            else {
                if (!XmlPullParserUtil.isStartTag(xmlPullParser, "metadata")) {
                    continue;
                }
                this.parseMetadata(xmlPullParser, map3);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "head"));
        return map;
    }
    
    private void parseMetadata(final XmlPullParser xmlPullParser, final Map<String, String> map) throws IOException, XmlPullParserException {
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "image")) {
                final String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "id");
                if (attributeValue == null) {
                    continue;
                }
                map.put(attributeValue, xmlPullParser.nextText());
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "metadata"));
    }
    
    private TtmlNode parseNode(final XmlPullParser xmlPullParser, final TtmlNode ttmlNode, final Map<String, TtmlRegion> map, final FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException {
        final int attributeCount = xmlPullParser.getAttributeCount();
        final TtmlStyle styleAttributes = this.parseStyleAttributes(xmlPullParser, null);
        String[] array;
        Object substring = array = null;
        String s = "";
        int i = 0;
        long n = -9223372036854775807L;
        long n2 = -9223372036854775807L;
        long n3 = -9223372036854775807L;
        while (i < attributeCount) {
            final String attributeName = xmlPullParser.getAttributeName(i);
            final String attributeValue = xmlPullParser.getAttributeValue(i);
            int n4 = 0;
            Label_0242: {
                switch (attributeName.hashCode()) {
                    case 1292595405: {
                        if (attributeName.equals("backgroundImage")) {
                            n4 = 5;
                            break Label_0242;
                        }
                        break;
                    }
                    case 109780401: {
                        if (attributeName.equals("style")) {
                            n4 = 3;
                            break Label_0242;
                        }
                        break;
                    }
                    case 93616297: {
                        if (attributeName.equals("begin")) {
                            n4 = 0;
                            break Label_0242;
                        }
                        break;
                    }
                    case 100571: {
                        if (attributeName.equals("end")) {
                            n4 = 1;
                            break Label_0242;
                        }
                        break;
                    }
                    case 99841: {
                        if (attributeName.equals("dur")) {
                            n4 = 2;
                            break Label_0242;
                        }
                        break;
                    }
                    case -934795532: {
                        if (attributeName.equals("region")) {
                            n4 = 4;
                            break Label_0242;
                        }
                        break;
                    }
                }
                n4 = -1;
            }
            long timeExpression;
            long timeExpression2;
            long timeExpression3;
            String s2;
            String s3;
            String[] array2;
            if (n4 != 0) {
                if (n4 != 1) {
                    if (n4 != 2) {
                        if (n4 != 3) {
                            if (n4 != 4) {
                                if (n4 == 5) {
                                    if (attributeValue.startsWith("#")) {
                                        substring = attributeValue.substring(1);
                                    }
                                }
                                timeExpression = n;
                                timeExpression2 = n2;
                                timeExpression3 = n3;
                                s2 = s;
                                s3 = (String)substring;
                                array2 = array;
                            }
                            else {
                                timeExpression = n;
                                timeExpression2 = n2;
                                timeExpression3 = n3;
                                s2 = s;
                                s3 = (String)substring;
                                array2 = array;
                                if (map.containsKey(attributeValue)) {
                                    s2 = attributeValue;
                                    timeExpression = n;
                                    timeExpression2 = n2;
                                    timeExpression3 = n3;
                                    s3 = (String)substring;
                                    array2 = array;
                                }
                            }
                        }
                        else {
                            final String[] styleIds = this.parseStyleIds(attributeValue);
                            timeExpression = n;
                            timeExpression2 = n2;
                            timeExpression3 = n3;
                            s2 = s;
                            s3 = (String)substring;
                            array2 = array;
                            if (styleIds.length > 0) {
                                array2 = styleIds;
                                timeExpression = n;
                                timeExpression2 = n2;
                                timeExpression3 = n3;
                                s2 = s;
                                s3 = (String)substring;
                            }
                        }
                    }
                    else {
                        timeExpression3 = parseTimeExpression(attributeValue, frameAndTickRate);
                        timeExpression = n;
                        timeExpression2 = n2;
                        s2 = s;
                        s3 = (String)substring;
                        array2 = array;
                    }
                }
                else {
                    timeExpression2 = parseTimeExpression(attributeValue, frameAndTickRate);
                    timeExpression = n;
                    timeExpression3 = n3;
                    s2 = s;
                    s3 = (String)substring;
                    array2 = array;
                }
            }
            else {
                timeExpression = parseTimeExpression(attributeValue, frameAndTickRate);
                array2 = array;
                s3 = (String)substring;
                s2 = s;
                timeExpression3 = n3;
                timeExpression2 = n2;
            }
            ++i;
            n = timeExpression;
            n2 = timeExpression2;
            n3 = timeExpression3;
            s = s2;
            substring = s3;
            array = array2;
        }
        long n5;
        long n6;
        if (ttmlNode != null) {
            final long startTimeUs = ttmlNode.startTimeUs;
            n5 = n;
            n6 = n2;
            if (startTimeUs != -9223372036854775807L) {
                long n7 = n;
                if (n != -9223372036854775807L) {
                    n7 = n + startTimeUs;
                }
                n5 = n7;
                n6 = n2;
                if (n2 != -9223372036854775807L) {
                    n6 = n2 + ttmlNode.startTimeUs;
                    n5 = n7;
                }
            }
        }
        else {
            n6 = n2;
            n5 = n;
        }
        if (n6 == -9223372036854775807L) {
            if (n3 != -9223372036854775807L) {
                final long endTimeUs = n3 + n5;
                return TtmlNode.buildNode(xmlPullParser.getName(), n5, endTimeUs, styleAttributes, array, s, (String)substring);
            }
            if (ttmlNode != null) {
                final long endTimeUs = ttmlNode.endTimeUs;
                if (endTimeUs != -9223372036854775807L) {
                    return TtmlNode.buildNode(xmlPullParser.getName(), n5, endTimeUs, styleAttributes, array, s, (String)substring);
                }
            }
        }
        final long endTimeUs = n6;
        return TtmlNode.buildNode(xmlPullParser.getName(), n5, endTimeUs, styleAttributes, array, s, (String)substring);
    }
    
    private TtmlRegion parseRegionAttributes(final XmlPullParser xmlPullParser, final CellResolution cellResolution, final TtsExtent ttsExtent) {
        final String attributeValue = XmlPullParserUtil.getAttributeValue(xmlPullParser, "id");
        if (attributeValue == null) {
            return null;
        }
        final String attributeValue2 = XmlPullParserUtil.getAttributeValue(xmlPullParser, "origin");
        if (attributeValue2 != null) {
            final Matcher matcher = TtmlDecoder.PERCENTAGE_COORDINATES.matcher(attributeValue2);
            final Matcher matcher2 = TtmlDecoder.PIXEL_COORDINATES.matcher(attributeValue2);
            Label_0228: {
                if (matcher.matches()) {
                    try {
                        final float n = Float.parseFloat(matcher.group(1)) / 100.0f;
                        final float n2 = Float.parseFloat(matcher.group(2)) / 100.0f;
                        break Label_0228;
                    }
                    catch (NumberFormatException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Ignoring region with malformed origin: ");
                        sb.append(attributeValue2);
                        Log.w("TtmlDecoder", sb.toString());
                        return null;
                    }
                }
                if (!matcher2.matches()) {
                    break Label_0228;
                }
                if (ttsExtent == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Ignoring region with missing tts:extent: ");
                    sb2.append(attributeValue2);
                    Log.w("TtmlDecoder", sb2.toString());
                    return null;
                }
                try {
                    final int int1 = Integer.parseInt(matcher2.group(1));
                    final int int2 = Integer.parseInt(matcher2.group(2));
                    final float n = int1 / (float)ttsExtent.width;
                    float n2 = int2 / (float)ttsExtent.height;
                    final String attributeValue3 = XmlPullParserUtil.getAttributeValue(xmlPullParser, "extent");
                    if (attributeValue3 != null) {
                        final Matcher matcher3 = TtmlDecoder.PERCENTAGE_COORDINATES.matcher(attributeValue3);
                        final Matcher matcher4 = TtmlDecoder.PIXEL_COORDINATES.matcher(attributeValue3);
                        Label_0440: {
                            if (matcher3.matches()) {
                                try {
                                    final float n3 = Float.parseFloat(matcher3.group(1)) / 100.0f;
                                    final float n4 = Float.parseFloat(matcher3.group(2)) / 100.0f;
                                    break Label_0440;
                                }
                                catch (NumberFormatException ex2) {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Ignoring region with malformed extent: ");
                                    sb3.append(attributeValue2);
                                    Log.w("TtmlDecoder", sb3.toString());
                                    return null;
                                }
                            }
                            if (!matcher4.matches()) {
                                break Label_0440;
                            }
                            if (ttsExtent == null) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("Ignoring region with missing tts:extent: ");
                                sb4.append(attributeValue2);
                                Log.w("TtmlDecoder", sb4.toString());
                                return null;
                            }
                            try {
                                final int int3 = Integer.parseInt(matcher4.group(1));
                                final int int4 = Integer.parseInt(matcher4.group(2));
                                final float n3 = int3 / (float)ttsExtent.width;
                                final float n4 = int4 / (float)ttsExtent.height;
                                final String attributeValue4 = XmlPullParserUtil.getAttributeValue(xmlPullParser, "displayAlign");
                                if (attributeValue4 != null) {
                                    final String lowerInvariant = Util.toLowerInvariant(attributeValue4);
                                    int n5 = -1;
                                    final int hashCode = lowerInvariant.hashCode();
                                    if (hashCode != -1364013995) {
                                        if (hashCode == 92734940) {
                                            if (lowerInvariant.equals("after")) {
                                                n5 = 1;
                                            }
                                        }
                                    }
                                    else if (lowerInvariant.equals("center")) {
                                        n5 = 0;
                                    }
                                    if (n5 == 0) {
                                        n2 += n4 / 2.0f;
                                        final int n6 = 1;
                                        return new TtmlRegion(attributeValue, n, n2, 0, n6, n3, 1, 1.0f / cellResolution.rows);
                                    }
                                    if (n5 == 1) {
                                        n2 += n4;
                                        final int n6 = 2;
                                        return new TtmlRegion(attributeValue, n, n2, 0, n6, n3, 1, 1.0f / cellResolution.rows);
                                    }
                                }
                                final int n6 = 0;
                                return new TtmlRegion(attributeValue, n, n2, 0, n6, n3, 1, 1.0f / cellResolution.rows);
                            }
                            catch (NumberFormatException ex3) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("Ignoring region with malformed extent: ");
                                sb5.append(attributeValue2);
                                Log.w("TtmlDecoder", sb5.toString());
                                return null;
                            }
                        }
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("Ignoring region with unsupported extent: ");
                        sb6.append(attributeValue2);
                        Log.w("TtmlDecoder", sb6.toString());
                        return null;
                    }
                    Log.w("TtmlDecoder", "Ignoring region without an extent");
                    return null;
                }
                catch (NumberFormatException ex4) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("Ignoring region with malformed origin: ");
                    sb7.append(attributeValue2);
                    Log.w("TtmlDecoder", sb7.toString());
                    return null;
                }
            }
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Ignoring region with unsupported origin: ");
            sb8.append(attributeValue2);
            Log.w("TtmlDecoder", sb8.toString());
            return null;
        }
        Log.w("TtmlDecoder", "Ignoring region without an origin");
        return null;
    }
    
    private TtmlStyle parseStyleAttributes(final XmlPullParser xmlPullParser, TtmlStyle ttmlStyle) {
        final int attributeCount = xmlPullParser.getAttributeCount();
        int i = 0;
        TtmlStyle ttmlStyle2 = ttmlStyle;
        while (i < attributeCount) {
            final String attributeValue = xmlPullParser.getAttributeValue(i);
            final String attributeName = xmlPullParser.getAttributeName(i);
            final int hashCode = attributeName.hashCode();
            final int n = -1;
            int n2 = 0;
            Label_0285: {
                switch (hashCode) {
                    case 1287124693: {
                        if (attributeName.equals("backgroundColor")) {
                            n2 = 1;
                            break Label_0285;
                        }
                        break;
                    }
                    case 365601008: {
                        if (attributeName.equals("fontSize")) {
                            n2 = 4;
                            break Label_0285;
                        }
                        break;
                    }
                    case 94842723: {
                        if (attributeName.equals("color")) {
                            n2 = 2;
                            break Label_0285;
                        }
                        break;
                    }
                    case 3355: {
                        if (attributeName.equals("id")) {
                            n2 = 0;
                            break Label_0285;
                        }
                        break;
                    }
                    case -734428249: {
                        if (attributeName.equals("fontWeight")) {
                            n2 = 5;
                            break Label_0285;
                        }
                        break;
                    }
                    case -879295043: {
                        if (attributeName.equals("textDecoration")) {
                            n2 = 8;
                            break Label_0285;
                        }
                        break;
                    }
                    case -1065511464: {
                        if (attributeName.equals("textAlign")) {
                            n2 = 7;
                            break Label_0285;
                        }
                        break;
                    }
                    case -1224696685: {
                        if (attributeName.equals("fontFamily")) {
                            n2 = 3;
                            break Label_0285;
                        }
                        break;
                    }
                    case -1550943582: {
                        if (attributeName.equals("fontStyle")) {
                            n2 = 6;
                            break Label_0285;
                        }
                        break;
                    }
                }
                n2 = -1;
            }
            switch (n2) {
                default: {
                    ttmlStyle = ttmlStyle2;
                    break;
                }
                case 8: {
                    final String lowerInvariant = Util.toLowerInvariant(attributeValue);
                    int n3 = 0;
                    switch (lowerInvariant.hashCode()) {
                        default: {
                            n3 = n;
                            break;
                        }
                        case 1679736913: {
                            n3 = n;
                            if (lowerInvariant.equals("linethrough")) {
                                n3 = 0;
                                break;
                            }
                            break;
                        }
                        case 913457136: {
                            n3 = n;
                            if (lowerInvariant.equals("nolinethrough")) {
                                n3 = 1;
                                break;
                            }
                            break;
                        }
                        case -1026963764: {
                            n3 = n;
                            if (lowerInvariant.equals("underline")) {
                                n3 = 2;
                                break;
                            }
                            break;
                        }
                        case -1461280213: {
                            n3 = n;
                            if (lowerInvariant.equals("nounderline")) {
                                n3 = 3;
                                break;
                            }
                            break;
                        }
                    }
                    if (n3 == 0) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setLinethrough(true);
                        break;
                    }
                    if (n3 == 1) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setLinethrough(false);
                        break;
                    }
                    if (n3 == 2) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setUnderline(true);
                        break;
                    }
                    if (n3 != 3) {
                        ttmlStyle = ttmlStyle2;
                        break;
                    }
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    ttmlStyle.setUnderline(false);
                    break;
                }
                case 7: {
                    final String lowerInvariant2 = Util.toLowerInvariant(attributeValue);
                    int n4 = 0;
                    switch (lowerInvariant2.hashCode()) {
                        default: {
                            n4 = n;
                            break;
                        }
                        case 109757538: {
                            n4 = n;
                            if (lowerInvariant2.equals("start")) {
                                n4 = 1;
                                break;
                            }
                            break;
                        }
                        case 108511772: {
                            n4 = n;
                            if (lowerInvariant2.equals("right")) {
                                n4 = 2;
                                break;
                            }
                            break;
                        }
                        case 3317767: {
                            n4 = n;
                            if (lowerInvariant2.equals("left")) {
                                n4 = 0;
                                break;
                            }
                            break;
                        }
                        case 100571: {
                            n4 = n;
                            if (lowerInvariant2.equals("end")) {
                                n4 = 3;
                                break;
                            }
                            break;
                        }
                        case -1364013995: {
                            n4 = n;
                            if (lowerInvariant2.equals("center")) {
                                n4 = 4;
                                break;
                            }
                            break;
                        }
                    }
                    if (n4 == 0) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setTextAlign(Layout$Alignment.ALIGN_NORMAL);
                        break;
                    }
                    if (n4 == 1) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setTextAlign(Layout$Alignment.ALIGN_NORMAL);
                        break;
                    }
                    if (n4 == 2) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setTextAlign(Layout$Alignment.ALIGN_OPPOSITE);
                        break;
                    }
                    if (n4 == 3) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setTextAlign(Layout$Alignment.ALIGN_OPPOSITE);
                        break;
                    }
                    if (n4 != 4) {
                        ttmlStyle = ttmlStyle2;
                        break;
                    }
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    ttmlStyle.setTextAlign(Layout$Alignment.ALIGN_CENTER);
                    break;
                }
                case 6: {
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    ttmlStyle.setItalic("italic".equalsIgnoreCase(attributeValue));
                    break;
                }
                case 5: {
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    ttmlStyle.setBold("bold".equalsIgnoreCase(attributeValue));
                    break;
                }
                case 4: {
                    ttmlStyle = ttmlStyle2;
                    try {
                        final TtmlStyle ttmlStyle3 = ttmlStyle = this.createIfNull(ttmlStyle2);
                        parseFontSize(attributeValue, ttmlStyle3);
                        ttmlStyle = ttmlStyle3;
                    }
                    catch (SubtitleDecoderException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed parsing fontSize value: ");
                        sb.append(attributeValue);
                        Log.w("TtmlDecoder", sb.toString());
                    }
                    break;
                }
                case 3: {
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    ttmlStyle.setFontFamily(attributeValue);
                    break;
                }
                case 2: {
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    try {
                        ttmlStyle.setFontColor(ColorParser.parseTtmlColor(attributeValue));
                    }
                    catch (IllegalArgumentException ex2) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed parsing color value: ");
                        sb2.append(attributeValue);
                        Log.w("TtmlDecoder", sb2.toString());
                    }
                    break;
                }
                case 1: {
                    ttmlStyle = this.createIfNull(ttmlStyle2);
                    try {
                        ttmlStyle.setBackgroundColor(ColorParser.parseTtmlColor(attributeValue));
                    }
                    catch (IllegalArgumentException ex3) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Failed parsing background value: ");
                        sb3.append(attributeValue);
                        Log.w("TtmlDecoder", sb3.toString());
                    }
                    break;
                }
                case 0: {
                    ttmlStyle = ttmlStyle2;
                    if ("style".equals(xmlPullParser.getName())) {
                        ttmlStyle = this.createIfNull(ttmlStyle2);
                        ttmlStyle.setId(attributeValue);
                        break;
                    }
                    break;
                }
            }
            ++i;
            ttmlStyle2 = ttmlStyle;
        }
        return ttmlStyle2;
    }
    
    private String[] parseStyleIds(String trim) {
        trim = trim.trim();
        String[] split;
        if (trim.isEmpty()) {
            split = new String[0];
        }
        else {
            split = Util.split(trim, "\\s+");
        }
        return split;
    }
    
    private static long parseTimeExpression(String s, final FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException {
        final Matcher matcher = TtmlDecoder.CLOCK_TIME.matcher(s);
        if (matcher.matches()) {
            final double v = (double)(Long.parseLong(matcher.group(1)) * 3600L);
            final double v2 = (double)(Long.parseLong(matcher.group(2)) * 60L);
            Double.isNaN(v);
            Double.isNaN(v2);
            final double v3 = (double)Long.parseLong(matcher.group(3));
            Double.isNaN(v3);
            s = matcher.group(4);
            double n = 0.0;
            double double1;
            if (s != null) {
                double1 = Double.parseDouble(s);
            }
            else {
                double1 = 0.0;
            }
            s = matcher.group(5);
            double n2;
            if (s != null) {
                n2 = Long.parseLong(s) / frameAndTickRate.effectiveFrameRate;
            }
            else {
                n2 = 0.0;
            }
            s = matcher.group(6);
            if (s != null) {
                final double v4 = (double)Long.parseLong(s);
                final double v5 = frameAndTickRate.subFrameRate;
                Double.isNaN(v4);
                Double.isNaN(v5);
                final double n3 = v4 / v5;
                final double v6 = frameAndTickRate.effectiveFrameRate;
                Double.isNaN(v6);
                n = n3 / v6;
            }
            return (long)((v + v2 + v3 + double1 + n2 + n) * 1000000.0);
        }
        final Matcher matcher2 = TtmlDecoder.OFFSET_TIME.matcher(s);
        if (matcher2.matches()) {
            final double double2 = Double.parseDouble(matcher2.group(1));
            s = matcher2.group(2);
            final int hashCode = s.hashCode();
            int n4 = 0;
            Label_0394: {
                if (hashCode != 102) {
                    if (hashCode != 104) {
                        if (hashCode != 109) {
                            if (hashCode != 3494) {
                                if (hashCode != 115) {
                                    if (hashCode == 116) {
                                        if (s.equals("t")) {
                                            n4 = 5;
                                            break Label_0394;
                                        }
                                    }
                                }
                                else if (s.equals("s")) {
                                    n4 = 2;
                                    break Label_0394;
                                }
                            }
                            else if (s.equals("ms")) {
                                n4 = 3;
                                break Label_0394;
                            }
                        }
                        else if (s.equals("m")) {
                            n4 = 1;
                            break Label_0394;
                        }
                    }
                    else if (s.equals("h")) {
                        n4 = 0;
                        break Label_0394;
                    }
                }
                else if (s.equals("f")) {
                    n4 = 4;
                    break Label_0394;
                }
                n4 = -1;
            }
            double n7;
            if (n4 != 0) {
                if (n4 != 1) {
                    double n5 = double2;
                    if (n4 != 2) {
                        double n6;
                        if (n4 != 3) {
                            if (n4 != 4) {
                                if (n4 != 5) {
                                    n5 = double2;
                                    return (long)(n5 * 1000000.0);
                                }
                                n6 = frameAndTickRate.tickRate;
                                Double.isNaN(n6);
                            }
                            else {
                                n6 = frameAndTickRate.effectiveFrameRate;
                                Double.isNaN(n6);
                            }
                        }
                        else {
                            n6 = 1000.0;
                        }
                        n5 = double2 / n6;
                        return (long)(n5 * 1000000.0);
                    }
                    return (long)(n5 * 1000000.0);
                }
                else {
                    n7 = 60.0;
                }
            }
            else {
                n7 = 3600.0;
            }
            double n5 = double2 * n7;
            return (long)(n5 * 1000000.0);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Malformed time expression: ");
        sb.append(s);
        throw new SubtitleDecoderException(sb.toString());
    }
    
    private TtsExtent parseTtsExtent(XmlPullParser attributeValue) {
        attributeValue = (XmlPullParser)XmlPullParserUtil.getAttributeValue(attributeValue, "extent");
        if (attributeValue == null) {
            return null;
        }
        final Matcher matcher = TtmlDecoder.PIXEL_COORDINATES.matcher((CharSequence)attributeValue);
        if (!matcher.matches()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring non-pixel tts extent: ");
            sb.append((String)attributeValue);
            Log.w("TtmlDecoder", sb.toString());
            return null;
        }
        try {
            return new TtsExtent(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Ignoring malformed tts extent: ");
            sb2.append((String)attributeValue);
            Log.w("TtmlDecoder", sb2.toString());
            return null;
        }
    }
    
    @Override
    protected TtmlSubtitle decode(byte[] buf, int length, final boolean b) throws SubtitleDecoderException {
        try {
            final XmlPullParser pullParser = this.xmlParserFactory.newPullParser();
            final HashMap<String, TtmlStyle> hashMap = new HashMap<String, TtmlStyle>();
            final HashMap<String, TtmlRegion> hashMap2 = new HashMap<String, TtmlRegion>();
            final HashMap<String, String> hashMap3 = new HashMap<String, String>();
            TtsExtent ttsExtent = null;
            hashMap2.put("", new TtmlRegion(null));
            pullParser.setInput((InputStream)new ByteArrayInputStream(buf, 0, length), (String)null);
            final ArrayDeque<TtmlNode> arrayDeque = new ArrayDeque<TtmlNode>();
            int i = pullParser.getEventType();
            FrameAndTickRate frameAndTickRate = TtmlDecoder.DEFAULT_FRAME_AND_TICK_RATE;
            CellResolution default_CELL_RESOLUTION = TtmlDecoder.DEFAULT_CELL_RESOLUTION;
            buf = null;
            length = 0;
            while (i != 1) {
                final TtmlNode ttmlNode = arrayDeque.peek();
                TtsExtent ttsExtent2 = null;
                CellResolution cellResolution4;
                if (length == 0) {
                    final String name = pullParser.getName();
                    CellResolution cellResolution = null;
                    Label_0456: {
                        if (i == 2) {
                            if ("tt".equals(name)) {
                                frameAndTickRate = this.parseFrameAndTickRates(pullParser);
                                cellResolution = this.parseCellResolution(pullParser, TtmlDecoder.DEFAULT_CELL_RESOLUTION);
                                ttsExtent2 = this.parseTtsExtent(pullParser);
                            }
                            else {
                                final CellResolution cellResolution2 = default_CELL_RESOLUTION;
                                ttsExtent2 = ttsExtent;
                                cellResolution = cellResolution2;
                            }
                            if (!isSupportedTag(name)) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Ignoring unsupported tag: ");
                                sb.append(pullParser.getName());
                                Log.i("TtmlDecoder", sb.toString());
                                ++length;
                                break Label_0456;
                            }
                            while (true) {
                                if ("head".equals(name)) {
                                    this.parseHeader(pullParser, hashMap, cellResolution, ttsExtent2, hashMap2, hashMap3);
                                    final int n = length;
                                    break Label_0339;
                                }
                                try {
                                    final TtmlNode node = this.parseNode(pullParser, ttmlNode, hashMap2, frameAndTickRate);
                                    arrayDeque.push(node);
                                    int n = length;
                                    if (ttmlNode != null) {
                                        ttmlNode.addChild(node);
                                        n = length;
                                    }
                                    length = n;
                                    break Label_0456;
                                }
                                catch (SubtitleDecoderException ex) {
                                    Log.w("TtmlDecoder", "Suppressing parser error", ex);
                                    final int n = length + 1;
                                    continue;
                                }
                                break;
                            }
                        }
                        if (i == 4) {
                            ttmlNode.addChild(TtmlNode.buildTextNode(pullParser.getText()));
                        }
                        else if (i == 3) {
                            if (pullParser.getName().equals("tt")) {
                                buf = (byte[])(Object)new TtmlSubtitle(arrayDeque.peek(), hashMap, hashMap2, hashMap3);
                            }
                            arrayDeque.pop();
                        }
                        final CellResolution cellResolution3 = default_CELL_RESOLUTION;
                        ttsExtent2 = ttsExtent;
                        cellResolution = cellResolution3;
                    }
                    cellResolution4 = cellResolution;
                }
                else {
                    int n2;
                    if (i == 2) {
                        n2 = length + 1;
                    }
                    else {
                        n2 = length;
                        if (i == 3) {
                            n2 = length - 1;
                        }
                    }
                    length = n2;
                    cellResolution4 = default_CELL_RESOLUTION;
                    ttsExtent2 = ttsExtent;
                }
                pullParser.next();
                i = pullParser.getEventType();
                ttsExtent = ttsExtent2;
                default_CELL_RESOLUTION = cellResolution4;
            }
            return (TtmlSubtitle)(Object)buf;
        }
        catch (IOException cause) {
            throw new IllegalStateException("Unexpected error when reading input.", cause);
        }
        catch (XmlPullParserException ex2) {
            throw new SubtitleDecoderException("Unable to decode source", (Throwable)ex2);
        }
    }
    
    private static final class CellResolution
    {
        final int columns;
        final int rows;
        
        CellResolution(final int columns, final int rows) {
            this.columns = columns;
            this.rows = rows;
        }
    }
    
    private static final class FrameAndTickRate
    {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;
        
        FrameAndTickRate(final float effectiveFrameRate, final int subFrameRate, final int tickRate) {
            this.effectiveFrameRate = effectiveFrameRate;
            this.subFrameRate = subFrameRate;
            this.tickRate = tickRate;
        }
    }
    
    private static final class TtsExtent
    {
        final int height;
        final int width;
        
        TtsExtent(final int width, final int height) {
            this.width = width;
            this.height = height;
        }
    }
}
