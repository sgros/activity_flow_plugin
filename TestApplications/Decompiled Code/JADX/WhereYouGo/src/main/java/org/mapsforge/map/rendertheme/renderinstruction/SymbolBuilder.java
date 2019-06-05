package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SymbolBuilder {
    static final String SRC = "src";
    Bitmap bitmap;

    public SymbolBuilder(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, String relativePathPrefix) throws IOException, SAXException {
        extractValues(graphicAdapter, elementName, attributes, relativePathPrefix);
    }

    public Symbol build() {
        return new Symbol(this);
    }

    private void extractValues(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, String relativePathPrefix) throws IOException, SAXException {
        int i = 0;
        while (i < attributes.getLength()) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (SRC.equals(name)) {
                this.bitmap = XmlUtils.createBitmap(graphicAdapter, relativePathPrefix, value);
                i++;
            } else {
                throw XmlUtils.createSAXException(elementName, name, value, i);
            }
        }
        XmlUtils.checkMandatoryAttribute(elementName, SRC, this.bitmap);
    }
}
