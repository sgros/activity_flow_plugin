package org.mozilla.focus.search;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class SearchEngineParser {
    public static SearchEngine load(AssetManager assetManager, String str, String str2) throws IOException {
        InputStream open;
        try {
            open = assetManager.open(str2);
            SearchEngine load = load(str, open);
            if (open != null) {
                open.close();
            }
            return load;
        } catch (XmlPullParserException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Parser exception while reading ");
            stringBuilder.append(str2);
            throw new AssertionError(stringBuilder.toString(), e);
        } catch (Throwable th) {
            r0.addSuppressed(th);
        }
    }

    static SearchEngine load(String str, InputStream inputStream) throws IOException, XmlPullParserException {
        SearchEngine searchEngine = new SearchEngine(str);
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        newPullParser.next();
        readSearchPlugin(newPullParser, searchEngine);
        return searchEngine;
    }

    private static void readSearchPlugin(XmlPullParser xmlPullParser, SearchEngine searchEngine) throws XmlPullParserException, IOException {
        StringBuilder stringBuilder;
        if (2 == xmlPullParser.getEventType()) {
            String name = xmlPullParser.getName();
            if ("SearchPlugin".equals(name) || "OpenSearchDescription".equals(name)) {
                while (xmlPullParser.next() != 3) {
                    if (xmlPullParser.getEventType() == 2) {
                        name = xmlPullParser.getName();
                        if (name.equals("ShortName")) {
                            readShortName(xmlPullParser, searchEngine);
                        } else if (name.equals("Url")) {
                            readUrl(xmlPullParser, searchEngine);
                        } else if (name.equals("Image")) {
                            readImage(xmlPullParser, searchEngine);
                        } else {
                            skip(xmlPullParser);
                        }
                    }
                }
                return;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Expected <SearchPlugin> or <OpenSearchDescription> as root tag: ");
            stringBuilder.append(xmlPullParser.getPositionDescription());
            throw new XmlPullParserException(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Expected start tag: ");
        stringBuilder.append(xmlPullParser.getPositionDescription());
        throw new XmlPullParserException(stringBuilder.toString());
    }

    private static void readUrl(XmlPullParser xmlPullParser, SearchEngine searchEngine) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "Url");
        String attributeValue = xmlPullParser.getAttributeValue(null, "type");
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "template");
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "rel");
        Uri parse = Uri.parse(attributeValue2);
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("Param")) {
                    parse = parse.buildUpon().appendQueryParameter(xmlPullParser.getAttributeValue(null, "name"), xmlPullParser.getAttributeValue(null, "value")).build();
                    xmlPullParser.nextTag();
                } else {
                    skip(xmlPullParser);
                }
            }
        }
        if (attributeValue.equals("text/html")) {
            if (attributeValue3 == null || !attributeValue3.equals("mobile")) {
                searchEngine.resultsUris.add(parse);
            } else {
                searchEngine.resultsUris.add(0, parse);
            }
        } else if (attributeValue.equals("application/x-suggestions+json")) {
            searchEngine.suggestUri = parse;
        }
    }

    private static void skip(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                switch (xmlPullParser.next()) {
                    case 2:
                        i++;
                        break;
                    case 3:
                        i--;
                        break;
                    default:
                        break;
                }
            }
            return;
        }
        throw new IllegalStateException();
    }

    private static void readShortName(XmlPullParser xmlPullParser, SearchEngine searchEngine) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, "ShortName");
        if (xmlPullParser.next() == 4) {
            searchEngine.name = xmlPullParser.getText();
            xmlPullParser.nextTag();
        }
    }

    private static void readImage(XmlPullParser xmlPullParser, SearchEngine searchEngine) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, "Image");
        if (xmlPullParser.next() == 4) {
            String text = xmlPullParser.getText();
            if (text.startsWith("data:image/png;base64,")) {
                byte[] decode = Base64.decode(text.substring("data:image/png;base64,".length()), 0);
                searchEngine.icon = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                xmlPullParser.nextTag();
            }
        }
    }
}
