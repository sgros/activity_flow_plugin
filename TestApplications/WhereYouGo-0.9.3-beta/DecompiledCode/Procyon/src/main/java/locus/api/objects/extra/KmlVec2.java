// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;

public class KmlVec2
{
    public double x;
    public Units xUnits;
    public double y;
    public Units yUnits;
    
    public KmlVec2() {
        this.x = 0.5;
        this.xUnits = Units.FRACTION;
        this.y = 0.5;
        this.yUnits = Units.FRACTION;
    }
    
    public KmlVec2(final double x, final Units xUnits, final double y, final Units yUnits) {
        this.x = 0.5;
        this.xUnits = Units.FRACTION;
        this.y = 0.5;
        this.yUnits = Units.FRACTION;
        this.x = x;
        this.xUnits = xUnits;
        this.y = y;
        this.yUnits = yUnits;
    }
    
    public static KmlVec2 read(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        final KmlVec2 kmlVec2 = new KmlVec2();
        kmlVec2.x = dataReaderBigEndian.readDouble();
        kmlVec2.xUnits = Units.values()[dataReaderBigEndian.readInt()];
        kmlVec2.y = dataReaderBigEndian.readDouble();
        kmlVec2.yUnits = Units.values()[dataReaderBigEndian.readInt()];
        return kmlVec2;
    }
    
    public String getAsXmlText() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t<hotSpot x=\"").append(this.x).append("\" y=\"").append(this.y).append("\"");
        sb.append(" xunits=\"");
        switch (this.xUnits) {
            case FRACTION: {
                sb.append("fraction");
                break;
            }
            case PIXELS: {
                sb.append("pixels");
                break;
            }
            case INSET_PIXELS: {
                sb.append("insetPixels");
                break;
            }
        }
        sb.append("\"");
        sb.append(" yunits=\"");
        switch (this.yUnits) {
            case FRACTION: {
                sb.append("fraction");
                break;
            }
            case PIXELS: {
                sb.append("pixels");
                break;
            }
            case INSET_PIXELS: {
                sb.append("insetPixels");
                break;
            }
        }
        sb.append("\"");
        sb.append(" />");
        return sb.toString();
    }
    
    public double[] getCoords(final double n, final double n2) {
        return this.getCoords(n, n2, new double[2]);
    }
    
    public double[] getCoords(final double n, final double n2, final double[] array) {
        double[] array2 = null;
        Label_0021: {
            if (array != null) {
                array2 = array;
                if (array.length == 2) {
                    break Label_0021;
                }
            }
            array2 = new double[2];
        }
        if (this.xUnits == Units.FRACTION) {
            array2[0] = this.x * n;
        }
        else if (this.xUnits == Units.PIXELS) {
            array2[0] = this.x;
        }
        else if (this.xUnits == Units.INSET_PIXELS) {
            array2[0] = n - this.x;
        }
        if (this.yUnits == Units.FRACTION) {
            array2[1] = (1.0 - this.y) * n2;
        }
        else if (this.yUnits == Units.PIXELS) {
            array2[1] = n2 - this.y;
        }
        else if (this.yUnits == Units.INSET_PIXELS) {
            array2[1] = this.y;
        }
        return array2;
    }
    
    public KmlVec2 getCopy() {
        final KmlVec2 kmlVec2 = new KmlVec2();
        kmlVec2.x = this.x;
        kmlVec2.xUnits = this.xUnits;
        kmlVec2.y = this.y;
        kmlVec2.yUnits = this.yUnits;
        return kmlVec2;
    }
    
    public void write(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeDouble(this.x);
        dataWriterBigEndian.writeInt(this.xUnits.ordinal());
        dataWriterBigEndian.writeDouble(this.y);
        dataWriterBigEndian.writeInt(this.yUnits.ordinal());
    }
    
    public enum Units
    {
        FRACTION, 
        INSET_PIXELS, 
        PIXELS;
    }
}
