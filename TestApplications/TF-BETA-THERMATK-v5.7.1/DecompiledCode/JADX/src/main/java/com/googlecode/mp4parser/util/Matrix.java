package com.googlecode.mp4parser.util;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class Matrix {
    public static final Matrix ROTATE_0 = new Matrix(1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_180 = new Matrix(-1.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_270 = new Matrix(0.0d, -1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_90 = new Matrix(0.0d, 1.0d, -1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    /* renamed from: a */
    double f31a;
    /* renamed from: b */
    double f32b;
    /* renamed from: c */
    double f33c;
    /* renamed from: d */
    double f34d;
    /* renamed from: tx */
    double f35tx;
    /* renamed from: ty */
    double f36ty;
    /* renamed from: u */
    double f37u;
    /* renamed from: v */
    double f38v;
    /* renamed from: w */
    double f39w;

    public Matrix(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        this.f37u = d5;
        this.f38v = d6;
        this.f39w = d7;
        this.f31a = d;
        this.f32b = d2;
        this.f33c = d3;
        this.f34d = d4;
        this.f35tx = d8;
        this.f36ty = d9;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Matrix.class != obj.getClass()) {
            return false;
        }
        Matrix matrix = (Matrix) obj;
        return Double.compare(matrix.f31a, this.f31a) == 0 && Double.compare(matrix.f32b, this.f32b) == 0 && Double.compare(matrix.f33c, this.f33c) == 0 && Double.compare(matrix.f34d, this.f34d) == 0 && Double.compare(matrix.f35tx, this.f35tx) == 0 && Double.compare(matrix.f36ty, this.f36ty) == 0 && Double.compare(matrix.f37u, this.f37u) == 0 && Double.compare(matrix.f38v, this.f38v) == 0 && Double.compare(matrix.f39w, this.f39w) == 0;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.f37u);
        int i = (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
        long doubleToLongBits2 = Double.doubleToLongBits(this.f38v);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f39w);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f31a);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f32b);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f33c);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f34d);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f35tx);
        i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        doubleToLongBits2 = Double.doubleToLongBits(this.f36ty);
        return (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }

    public String toString() {
        if (equals(ROTATE_0)) {
            return "Rotate 0°";
        }
        if (equals(ROTATE_90)) {
            return "Rotate 90°";
        }
        if (equals(ROTATE_180)) {
            return "Rotate 180°";
        }
        if (equals(ROTATE_270)) {
            return "Rotate 270°";
        }
        StringBuilder stringBuilder = new StringBuilder("Matrix{u=");
        stringBuilder.append(this.f37u);
        stringBuilder.append(", v=");
        stringBuilder.append(this.f38v);
        stringBuilder.append(", w=");
        stringBuilder.append(this.f39w);
        stringBuilder.append(", a=");
        stringBuilder.append(this.f31a);
        stringBuilder.append(", b=");
        stringBuilder.append(this.f32b);
        stringBuilder.append(", c=");
        stringBuilder.append(this.f33c);
        stringBuilder.append(", d=");
        stringBuilder.append(this.f34d);
        stringBuilder.append(", tx=");
        stringBuilder.append(this.f35tx);
        stringBuilder.append(", ty=");
        stringBuilder.append(this.f36ty);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static Matrix fromFileOrder(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        return new Matrix(d, d2, d4, d5, d3, d6, d9, d7, d8);
    }

    public static Matrix fromByteBuffer(ByteBuffer byteBuffer) {
        return fromFileOrder(IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer));
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f31a);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f32b);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f37u);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f33c);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f34d);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f38v);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f35tx);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f36ty);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f39w);
    }
}
