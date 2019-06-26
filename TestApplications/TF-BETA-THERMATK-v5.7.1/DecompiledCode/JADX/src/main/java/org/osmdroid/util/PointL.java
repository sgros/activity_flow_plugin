package org.osmdroid.util;

public class PointL {
    /* renamed from: x */
    public long f44x;
    /* renamed from: y */
    public long f45y;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PointL(");
        stringBuilder.append(this.f44x);
        stringBuilder.append(", ");
        stringBuilder.append(this.f45y);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PointL)) {
            return false;
        }
        PointL pointL = (PointL) obj;
        if (!(this.f44x == pointL.f44x && this.f45y == pointL.f45y)) {
            z = false;
        }
        return z;
    }
}
