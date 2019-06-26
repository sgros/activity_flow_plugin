package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import org.mapsforge.map.graphics.Cap;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AreaBuilder {
    static final String FILL = "fill";
    static final String SRC = "src";
    static final String STROKE = "stroke";
    static final String STROKE_WIDTH = "stroke-width";
    final Paint fill;
    final int level;
    final Paint stroke;
    float strokeWidth;

    public AreaBuilder(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, int level, String relativePathPrefix) throws IOException, SAXException {
        this.level = level;
        this.fill = graphicAdapter.getPaint();
        this.fill.setColor(graphicAdapter.getColor(Color.BLACK));
        this.fill.setStyle(Style.FILL);
        this.fill.setStrokeCap(Cap.ROUND);
        this.stroke = graphicAdapter.getPaint();
        this.stroke.setColor(graphicAdapter.getColor(Color.TRANSPARENT));
        this.stroke.setStyle(Style.STROKE);
        this.stroke.setStrokeCap(Cap.ROUND);
        extractValues(graphicAdapter, elementName, attributes, relativePathPrefix);
    }

    public Area build() {
        return new Area(this);
    }

    private void extractValues(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, String relativePathPrefix) throws IOException, SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (SRC.equals(name)) {
                this.fill.setBitmapShader(XmlUtils.createBitmap(graphicAdapter, relativePathPrefix, value));
            } else if (FILL.equals(name)) {
                this.fill.setColor(graphicAdapter.parseColor(value));
            } else if (STROKE.equals(name)) {
                this.stroke.setColor(graphicAdapter.parseColor(value));
            } else if (STROKE_WIDTH.equals(name)) {
                this.strokeWidth = XmlUtils.parseNonNegativeFloat(name, value);
            } else {
                throw XmlUtils.createSAXException(elementName, name, value, i);
            }
        }
    }
}
