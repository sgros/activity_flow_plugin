package org.mapsforge.map.rendertheme.rule;

import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RenderThemeBuilder {
    private static final String BASE_STROKE_WIDTH = "base-stroke-width";
    private static final String BASE_TEXT_SIZE = "base-text-size";
    private static final String MAP_BACKGROUND = "map-background";
    private static final int RENDER_THEME_VERSION = 2;
    private static final String VERSION = "version";
    private static final String XMLNS = "xmlns";
    private static final String XMLNS_XSI = "xmlns:xsi";
    private static final String XSI_SCHEMALOCATION = "xsi:schemaLocation";
    float baseStrokeWidth = 1.0f;
    float baseTextSize = 1.0f;
    int mapBackground;
    private Integer version;

    public RenderThemeBuilder(GraphicAdapter graphicAdapter, String elementName, Attributes attributes) throws SAXException {
        this.mapBackground = graphicAdapter.getColor(Color.WHITE);
        extractValues(graphicAdapter, elementName, attributes);
    }

    public RenderTheme build() {
        return new RenderTheme(this);
    }

    private void extractValues(GraphicAdapter graphicAdapter, String elementName, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (!(XMLNS.equals(name) || XMLNS_XSI.equals(name) || XSI_SCHEMALOCATION.equals(name))) {
                if (VERSION.equals(name)) {
                    this.version = Integer.valueOf(XmlUtils.parseNonNegativeInteger(name, value));
                } else if (MAP_BACKGROUND.equals(name)) {
                    this.mapBackground = graphicAdapter.parseColor(value);
                } else if (BASE_STROKE_WIDTH.equals(name)) {
                    this.baseStrokeWidth = XmlUtils.parseNonNegativeFloat(name, value);
                } else if (BASE_TEXT_SIZE.equals(name)) {
                    this.baseTextSize = XmlUtils.parseNonNegativeFloat(name, value);
                } else {
                    throw XmlUtils.createSAXException(elementName, name, value, i);
                }
            }
        }
        validate(elementName);
    }

    private void validate(String elementName) throws SAXException {
        XmlUtils.checkMandatoryAttribute(elementName, VERSION, this.version);
        if (this.version.intValue() != 2) {
            throw new SAXException("unsupported render theme version: " + this.version);
        }
    }
}
