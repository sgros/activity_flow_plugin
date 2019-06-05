package org.mapsforge.map.rendertheme.renderinstruction;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;
import org.mapsforge.map.graphics.Cap;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LineBuilder {
    private static final Pattern SPLIT_PATTERN = Pattern.compile(",");
    static final String SRC = "src";
    static final String STROKE = "stroke";
    static final String STROKE_DASHARRAY = "stroke-dasharray";
    static final String STROKE_LINECAP = "stroke-linecap";
    static final String STROKE_WIDTH = "stroke-width";
    final int level;
    final Paint stroke;
    float strokeWidth;

    private static float[] parseFloatArray(String name, String dashString) throws SAXException {
        String[] dashEntries = SPLIT_PATTERN.split(dashString);
        float[] dashIntervals = new float[dashEntries.length];
        for (int i = 0; i < dashEntries.length; i++) {
            dashIntervals[i] = XmlUtils.parseNonNegativeFloat(name, dashEntries[i]);
        }
        return dashIntervals;
    }

    public LineBuilder(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, int level, String relativePathPrefix) throws IOException, SAXException {
        this.level = level;
        this.stroke = graphicAdapter.getPaint();
        this.stroke.setColor(graphicAdapter.getColor(Color.BLACK));
        this.stroke.setStyle(Style.STROKE);
        this.stroke.setStrokeCap(Cap.ROUND);
        extractValues(graphicAdapter, elementName, attributes, relativePathPrefix);
    }

    public Line build() {
        return new Line(this);
    }

    private void extractValues(GraphicAdapter graphicAdapter, String elementName, Attributes attributes, String relativePathPrefix) throws IOException, SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            if (SRC.equals(name)) {
                this.stroke.setBitmapShader(XmlUtils.createBitmap(graphicAdapter, relativePathPrefix, value));
            } else if (STROKE.equals(name)) {
                this.stroke.setColor(graphicAdapter.parseColor(value));
            } else if (STROKE_WIDTH.equals(name)) {
                this.strokeWidth = XmlUtils.parseNonNegativeFloat(name, value);
            } else if (STROKE_DASHARRAY.equals(name)) {
                this.stroke.setDashPathEffect(parseFloatArray(name, value));
            } else if (STROKE_LINECAP.equals(name)) {
                this.stroke.setStrokeCap(Cap.valueOf(value.toUpperCase(Locale.ENGLISH)));
            } else {
                throw XmlUtils.createSAXException(elementName, name, value, i);
            }
        }
    }
}
