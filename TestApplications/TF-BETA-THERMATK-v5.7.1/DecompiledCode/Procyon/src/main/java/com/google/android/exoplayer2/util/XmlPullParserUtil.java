// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;

public final class XmlPullParserUtil
{
    private XmlPullParserUtil() {
    }
    
    public static String getAttributeValue(final XmlPullParser xmlPullParser, final String anObject) {
        for (int attributeCount = xmlPullParser.getAttributeCount(), i = 0; i < attributeCount; ++i) {
            if (xmlPullParser.getAttributeName(i).equals(anObject)) {
                return xmlPullParser.getAttributeValue(i);
            }
        }
        return null;
    }
    
    public static String getAttributeValueIgnorePrefix(final XmlPullParser xmlPullParser, final String anObject) {
        for (int attributeCount = xmlPullParser.getAttributeCount(), i = 0; i < attributeCount; ++i) {
            if (stripPrefix(xmlPullParser.getAttributeName(i)).equals(anObject)) {
                return xmlPullParser.getAttributeValue(i);
            }
        }
        return null;
    }
    
    public static boolean isEndTag(final XmlPullParser xmlPullParser) throws XmlPullParserException {
        return xmlPullParser.getEventType() == 3;
    }
    
    public static boolean isEndTag(final XmlPullParser xmlPullParser, final String anObject) throws XmlPullParserException {
        return isEndTag(xmlPullParser) && xmlPullParser.getName().equals(anObject);
    }
    
    public static boolean isStartTag(final XmlPullParser xmlPullParser) throws XmlPullParserException {
        return xmlPullParser.getEventType() == 2;
    }
    
    public static boolean isStartTag(final XmlPullParser xmlPullParser, final String anObject) throws XmlPullParserException {
        return isStartTag(xmlPullParser) && xmlPullParser.getName().equals(anObject);
    }
    
    public static boolean isStartTagIgnorePrefix(final XmlPullParser xmlPullParser, final String anObject) throws XmlPullParserException {
        return isStartTag(xmlPullParser) && stripPrefix(xmlPullParser.getName()).equals(anObject);
    }
    
    private static String stripPrefix(String substring) {
        final int index = substring.indexOf(58);
        if (index != -1) {
            substring = substring.substring(index + 1);
        }
        return substring;
    }
}
