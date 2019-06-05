package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LineSymbolBuilder {
    static final String ALIGN_CENTER = "align-center";
    static final String REPEAT = "repeat";
    static final String SRC = "src";
    boolean alignCenter;
    Bitmap bitmap;
    boolean repeat;

    public LineSymbolBuilder(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, String relativePathPrefix) throws IOException, SAXException {
        extractValues(graphicAdapter, elementName, attributes, relativePathPrefix);
    }

    public LineSymbol build() {
        return new LineSymbol(this);
    }

    private void extractValues(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, String relativePathPrefix) throws IOException, SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (SRC.equals(name)) {
                this.bitmap = XmlUtils.createBitmap(graphicAdapter, relativePathPrefix, value);
            } else if (ALIGN_CENTER.equals(name)) {
                this.alignCenter = Boolean.parseBoolean(value);
            } else if (REPEAT.equals(name)) {
                this.repeat = Boolean.parseBoolean(value);
            } else {
                throw XmlUtils.createSAXException(elementName, name, value, i);
            }
        }
        XmlUtils.checkMandatoryAttribute(elementName, SRC, this.bitmap);
    }
}
