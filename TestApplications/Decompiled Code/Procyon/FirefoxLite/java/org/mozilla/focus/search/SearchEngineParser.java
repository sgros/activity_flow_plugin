// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.search;

import android.net.Uri;
import android.graphics.BitmapFactory;
import android.util.Base64;
import org.xmlpull.v1.XmlPullParser;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.AssetManager;

class SearchEngineParser
{
    public static SearchEngine load(AssetManager assetManager, final String s, final String str) throws IOException {
        try {
            final InputStream open = assetManager.open(str);
            assetManager = null;
            try {
                try {
                    final SearchEngine load = load(s, open);
                    if (open != null) {
                        open.close();
                    }
                    return load;
                }
                finally {
                    if (open != null) {
                        if (assetManager != null) {
                            final InputStream inputStream = open;
                            inputStream.close();
                        }
                        else {
                            open.close();
                        }
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final InputStream inputStream = open;
                inputStream.close();
            }
            catch (Throwable t2) {}
        }
        catch (XmlPullParserException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Parser exception while reading ");
            sb.append(str);
            throw new AssertionError(sb.toString(), (Throwable)cause);
        }
    }
    
    static SearchEngine load(final String s, final InputStream in) throws IOException, XmlPullParserException {
        final SearchEngine searchEngine = new SearchEngine(s);
        final XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
        pullParser.setInput((Reader)new InputStreamReader(in, StandardCharsets.UTF_8));
        pullParser.next();
        readSearchPlugin(pullParser, searchEngine);
        return searchEngine;
    }
    
    private static void readImage(final XmlPullParser xmlPullParser, final SearchEngine searchEngine) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, (String)null, "Image");
        if (xmlPullParser.next() != 4) {
            return;
        }
        final String text = xmlPullParser.getText();
        if (!text.startsWith("data:image/png;base64,")) {
            return;
        }
        final byte[] decode = Base64.decode(text.substring("data:image/png;base64,".length()), 0);
        searchEngine.icon = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        xmlPullParser.nextTag();
    }
    
    private static void readSearchPlugin(final XmlPullParser xmlPullParser, final SearchEngine searchEngine) throws XmlPullParserException, IOException {
        if (2 == xmlPullParser.getEventType()) {
            final String name = xmlPullParser.getName();
            if (!"SearchPlugin".equals(name)) {
                if (!"OpenSearchDescription".equals(name)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Expected <SearchPlugin> or <OpenSearchDescription> as root tag: ");
                    sb.append(xmlPullParser.getPositionDescription());
                    throw new XmlPullParserException(sb.toString());
                }
            }
            while (xmlPullParser.next() != 3) {
                if (xmlPullParser.getEventType() != 2) {
                    continue;
                }
                final String name2 = xmlPullParser.getName();
                if (name2.equals("ShortName")) {
                    readShortName(xmlPullParser, searchEngine);
                }
                else if (name2.equals("Url")) {
                    readUrl(xmlPullParser, searchEngine);
                }
                else if (name2.equals("Image")) {
                    readImage(xmlPullParser, searchEngine);
                }
                else {
                    skip(xmlPullParser);
                }
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected start tag: ");
        sb2.append(xmlPullParser.getPositionDescription());
        throw new XmlPullParserException(sb2.toString());
    }
    
    private static void readShortName(final XmlPullParser xmlPullParser, final SearchEngine searchEngine) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, (String)null, "ShortName");
        if (xmlPullParser.next() == 4) {
            searchEngine.name = xmlPullParser.getText();
            xmlPullParser.nextTag();
        }
    }
    
    private static void readUrl(final XmlPullParser xmlPullParser, final SearchEngine searchEngine) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, (String)null, "Url");
        final String attributeValue = xmlPullParser.getAttributeValue((String)null, "type");
        final String attributeValue2 = xmlPullParser.getAttributeValue((String)null, "template");
        final String attributeValue3 = xmlPullParser.getAttributeValue((String)null, "rel");
        Uri suggestUri = Uri.parse(attributeValue2);
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() != 2) {
                continue;
            }
            if (xmlPullParser.getName().equals("Param")) {
                suggestUri = suggestUri.buildUpon().appendQueryParameter(xmlPullParser.getAttributeValue((String)null, "name"), xmlPullParser.getAttributeValue((String)null, "value")).build();
                xmlPullParser.nextTag();
            }
            else {
                skip(xmlPullParser);
            }
        }
        if (attributeValue.equals("text/html")) {
            if (attributeValue3 != null && attributeValue3.equals("mobile")) {
                searchEngine.resultsUris.add(0, suggestUri);
            }
            else {
                searchEngine.resultsUris.add(suggestUri);
            }
        }
        else if (attributeValue.equals("application/x-suggestions+json")) {
            searchEngine.suggestUri = suggestUri;
        }
    }
    
    private static void skip(final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                switch (xmlPullParser.next()) {
                    default: {
                        continue;
                    }
                    case 3: {
                        --i;
                        continue;
                    }
                    case 2: {
                        ++i;
                        continue;
                    }
                }
            }
            return;
        }
        throw new IllegalStateException();
    }
}
