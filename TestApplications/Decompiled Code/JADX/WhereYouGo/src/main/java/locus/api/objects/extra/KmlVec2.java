package locus.api.objects.extra;

import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class KmlVec2 {
    /* renamed from: x */
    public double f44x = 0.5d;
    public Units xUnits = Units.FRACTION;
    /* renamed from: y */
    public double f45y = 0.5d;
    public Units yUnits = Units.FRACTION;

    public enum Units {
        FRACTION,
        PIXELS,
        INSET_PIXELS
    }

    public KmlVec2(double x, Units xUnits, double y, Units yUnits) {
        this.f44x = x;
        this.xUnits = xUnits;
        this.f45y = y;
        this.yUnits = yUnits;
    }

    public double[] getCoords(double sourceWidth, double sourceHeight) {
        return getCoords(sourceWidth, sourceHeight, new double[2]);
    }

    public double[] getCoords(double sourceWidth, double sourceHeight, double[] result) {
        if (result == null || result.length != 2) {
            result = new double[2];
        }
        if (this.xUnits == Units.FRACTION) {
            result[0] = this.f44x * sourceWidth;
        } else if (this.xUnits == Units.PIXELS) {
            result[0] = this.f44x;
        } else if (this.xUnits == Units.INSET_PIXELS) {
            result[0] = sourceWidth - this.f44x;
        }
        if (this.yUnits == Units.FRACTION) {
            result[1] = (1.0d - this.f45y) * sourceHeight;
        } else if (this.yUnits == Units.PIXELS) {
            result[1] = sourceHeight - this.f45y;
        } else if (this.yUnits == Units.INSET_PIXELS) {
            result[1] = this.f45y;
        }
        return result;
    }

    public String getAsXmlText() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t<hotSpot x=\"").append(this.f44x).append("\" y=\"").append(this.f45y).append("\"");
        sb.append(" xunits=\"");
        switch (this.xUnits) {
            case FRACTION:
                sb.append("fraction");
                break;
            case PIXELS:
                sb.append("pixels");
                break;
            case INSET_PIXELS:
                sb.append("insetPixels");
                break;
        }
        sb.append("\"");
        sb.append(" yunits=\"");
        switch (this.yUnits) {
            case FRACTION:
                sb.append("fraction");
                break;
            case PIXELS:
                sb.append("pixels");
                break;
            case INSET_PIXELS:
                sb.append("insetPixels");
                break;
        }
        sb.append("\"");
        sb.append(" />");
        return sb.toString();
    }

    public KmlVec2 getCopy() {
        KmlVec2 vec = new KmlVec2();
        vec.f44x = this.f44x;
        vec.xUnits = this.xUnits;
        vec.f45y = this.f45y;
        vec.yUnits = this.yUnits;
        return vec;
    }

    public void write(DataWriterBigEndian dw) throws IOException {
        dw.writeDouble(this.f44x);
        dw.writeInt(this.xUnits.ordinal());
        dw.writeDouble(this.f45y);
        dw.writeInt(this.yUnits.ordinal());
    }

    public static KmlVec2 read(DataReaderBigEndian dr) throws IOException {
        KmlVec2 vec = new KmlVec2();
        vec.f44x = dr.readDouble();
        vec.xUnits = Units.values()[dr.readInt()];
        vec.f45y = dr.readDouble();
        vec.yUnits = Units.values()[dr.readInt()];
        return vec;
    }
}
