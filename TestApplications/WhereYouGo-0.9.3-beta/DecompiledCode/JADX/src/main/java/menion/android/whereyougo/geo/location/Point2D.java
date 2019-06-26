package menion.android.whereyougo.geo.location;

public abstract class Point2D {

    public static class Int extends Point2D {
        /* renamed from: x */
        public int f52x;
        /* renamed from: y */
        public int f53y;

        public Int(int x, int y) {
            this.f52x = x;
            this.f53y = y;
        }

        public double getX() {
            return (double) this.f52x;
        }

        public double getY() {
            return (double) this.f53y;
        }

        public void setLocation(double x, double y) {
            this.f52x = (int) x;
            this.f53y = (int) y;
        }

        public void setLocation(int x, int y) {
            this.f52x = x;
            this.f53y = y;
        }

        public String toString() {
            return "Point2D.int[" + this.f52x + ", " + this.f53y + ']';
        }
    }

    public abstract double getX();

    public abstract double getY();

    public abstract void setLocation(double d, double d2);

    Point2D() {
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSq(x1, y1, x2, y2));
    }

    public static double distanceSq(double x1, double y1, double x2, double y2) {
        x2 -= x1;
        y2 -= y1;
        return (x2 * x2) + (y2 * y2);
    }

    public double distance(double x, double y) {
        return distance(getX(), getY(), x, y);
    }

    public double distance(Point2D p) {
        return distance(getX(), getY(), p.getX(), p.getY());
    }

    public double distanceSq(double x, double y) {
        return distanceSq(getX(), getY(), x, y);
    }

    public double distanceSq(Point2D p) {
        return distanceSq(getX(), getY(), p.getX(), p.getY());
    }

    public boolean equals(Object o) {
        if (!(o instanceof Point2D)) {
            return false;
        }
        Point2D p = (Point2D) o;
        if (getX() == p.getX() && getY() == p.getY()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long l = (31 * Double.doubleToLongBits(getY())) ^ Double.doubleToLongBits(getX());
        return (int) ((l >> 32) ^ l);
    }

    public void setLocation(Point2D p) {
        setLocation(p.getX(), p.getY());
    }

    public String toString() {
        return "[ X: " + getX() + " Y: " + getY() + " ]";
    }
}
