// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.location;

public abstract class Point2D
{
    Point2D() {
    }
    
    public static double distance(final double n, final double n2, final double n3, final double n4) {
        return Math.sqrt(distanceSq(n, n2, n3, n4));
    }
    
    public static double distanceSq(double n, double n2, final double n3, final double n4) {
        n = n3 - n;
        n2 = n4 - n2;
        return n * n + n2 * n2;
    }
    
    public double distance(final double n, final double n2) {
        return distance(this.getX(), this.getY(), n, n2);
    }
    
    public double distance(final Point2D point2D) {
        return distance(this.getX(), this.getY(), point2D.getX(), point2D.getY());
    }
    
    public double distanceSq(final double n, final double n2) {
        return distanceSq(this.getX(), this.getY(), n, n2);
    }
    
    public double distanceSq(final Point2D point2D) {
        return distanceSq(this.getX(), this.getY(), point2D.getX(), point2D.getY());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof Point2D)) {
            b2 = b;
        }
        else {
            final Point2D point2D = (Point2D)o;
            b2 = b;
            if (this.getX() == point2D.getX()) {
                b2 = b;
                if (this.getY() == point2D.getY()) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public abstract double getX();
    
    public abstract double getY();
    
    @Override
    public int hashCode() {
        final long n = 31L * Double.doubleToLongBits(this.getY()) ^ Double.doubleToLongBits(this.getX());
        return (int)(n >> 32 ^ n);
    }
    
    public abstract void setLocation(final double p0, final double p1);
    
    public void setLocation(final Point2D point2D) {
        this.setLocation(point2D.getX(), point2D.getY());
    }
    
    @Override
    public String toString() {
        return "[ X: " + this.getX() + " Y: " + this.getY() + " ]";
    }
    
    public static class Int extends Point2D
    {
        public int x;
        public int y;
        
        public Int() {
        }
        
        public Int(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public double getX() {
            return this.x;
        }
        
        @Override
        public double getY() {
            return this.y;
        }
        
        @Override
        public void setLocation(final double n, final double n2) {
            this.x = (int)n;
            this.y = (int)n2;
        }
        
        public void setLocation(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return "Point2D.int[" + this.x + ", " + this.y + ']';
        }
    }
}
