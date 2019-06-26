// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class PointL
{
    public long x;
    public long y;
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointL)) {
            return false;
        }
        final PointL pointL = (PointL)o;
        if (this.x != pointL.x || this.y != pointL.y) {
            b = false;
        }
        return b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PointL(");
        sb.append(this.x);
        sb.append(", ");
        sb.append(this.y);
        sb.append(")");
        return sb.toString();
    }
}
