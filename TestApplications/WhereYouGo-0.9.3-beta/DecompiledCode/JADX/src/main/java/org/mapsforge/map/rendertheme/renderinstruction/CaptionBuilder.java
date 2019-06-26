package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.Locale;
import org.mapsforge.map.graphics.Align;
import org.mapsforge.map.graphics.FontFamily;
import org.mapsforge.map.graphics.FontStyle;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CaptionBuilder {
    /* renamed from: DY */
    static final String f70DY = "dy";
    static final String FILL = "fill";
    static final String FONT_FAMILY = "font-family";
    static final String FONT_SIZE = "font-size";
    static final String FONT_STYLE = "font-style";
    /* renamed from: K */
    static final String f71K = "k";
    static final String STROKE = "stroke";
    static final String STROKE_WIDTH = "stroke-width";
    /* renamed from: dy */
    float f72dy;
    final Paint fill;
    float fontSize;
    final Paint stroke;
    TextKey textKey;

    public CaptionBuilder(GraphicAdapter graphicAdapter, String elementName, Attributes attributes) throws SAXException {
        this.fill = graphicAdapter.getPaint();
        this.fill.setColor(graphicAdapter.getColor(Color.BLACK));
        this.fill.setStyle(Style.FILL);
        this.fill.setTextAlign(Align.LEFT);
        this.stroke = graphicAdapter.getPaint();
        this.stroke.setColor(graphicAdapter.getColor(Color.BLACK));
        this.stroke.setStyle(Style.STROKE);
        this.stroke.setTextAlign(Align.LEFT);
        extractValues(graphicAdapter, elementName, attributes);
    }

    public Caption build() {
        return new Caption(this);
    }

    private void extractValues(GraphicAdapter graphicAdapter, String elementName, Attributes attributes) throws SAXException {
        FontFamily fontFamily = FontFamily.DEFAULT;
        FontStyle fontStyle = FontStyle.NORMAL;
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (f71K.equals(name)) {
                this.textKey = TextKey.getInstance(value);
            } else if (f70DY.equals(name)) {
                this.f72dy = Float.parseFloat(value);
            } else if (FONT_FAMILY.equals(name)) {
                fontFamily = FontFamily.valueOf(value.toUpperCase(Locale.ENGLISH));
            } else if (FONT_STYLE.equals(name)) {
                fontStyle = FontStyle.valueOf(value.toUpperCase(Locale.ENGLISH));
            } else if (FONT_SIZE.equals(name)) {
                this.fontSize = XmlUtils.parseNonNegativeFloat(name, value);
            } else if (FILL.equals(name)) {
                this.fill.setColor(graphicAdapter.parseColor(value));
            } else if (STROKE.equals(name)) {
                this.stroke.setColor(graphicAdapter.parseColor(value));
            } else if (STROKE_WIDTH.equals(name)) {
                this.stroke.setStrokeWidth(XmlUtils.parseNonNegativeFloat(name, value));
            } else {
                throw XmlUtils.createSAXException(elementName, name, value, i);
            }
        }
        this.fill.setTypeface(fontFamily, fontStyle);
        this.stroke.setTypeface(fontFamily, fontStyle);
        XmlUtils.checkMandatoryAttribute(elementName, f71K, this.textKey);
    }
}
