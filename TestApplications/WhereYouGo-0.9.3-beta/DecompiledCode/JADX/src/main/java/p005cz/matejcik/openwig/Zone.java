package p005cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import p009se.krka.kahlua.p010vm.LuaState;
import p009se.krka.kahlua.p010vm.LuaTable;
import p009se.krka.kahlua.stdlib.TableLib;

/* renamed from: cz.matejcik.openwig.Zone */
public class Zone extends Thing {
    private static final double DEFAULT_PROXIMITY = 1500.0d;
    public static final int DISTANT = 0;
    public static final int INSIDE = 2;
    public static final int NOWHERE = -1;
    public static final int PROXIMITY = 1;
    public static final int S_ALWAYS = 0;
    public static final int S_NEVER = 3;
    public static final int S_ONENTER = 1;
    public static final int S_ONPROXIMITY = 2;
    private boolean active = false;
    public double bbBottom;
    public ZonePoint bbCenter = new ZonePoint(0.0d, 0.0d, 0.0d);
    public double bbLeft;
    public double bbRight;
    public double bbTop;
    public int contain = -1;
    private double diameter;
    public double distance = Double.MAX_VALUE;
    private double distanceRange = -1.0d;
    private double distantTolerance = 20.0d;
    private double insideTolerance = 5.0d;
    private int ncontain = -1;
    public ZonePoint nearestPoint = new ZonePoint(0.0d, 0.0d, 0.0d);
    public double pbbBottom;
    public double pbbLeft;
    public double pbbRight;
    public double pbbTop;
    public ZonePoint[] points;
    private double proximityRange = -1.0d;
    private double proximityTolerance = 10.0d;
    private int showObjects = 1;

    /* Access modifiers changed, original: protected */
    public String luaTostring() {
        return "a Zone instance";
    }

    public boolean isVisible() {
        return this.active && this.visible && this.contain > -1;
    }

    public boolean visibleToPlayer() {
        return isVisible();
    }

    public boolean isLocated() {
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void setItem(String key, Object value) {
        int i = 0;
        boolean a;
        if ("Points".equals(key) && value != null) {
            LuaTable lt = (LuaTable) value;
            int n = lt.len();
            this.points = new ZonePoint[n];
            for (int i2 = 1; i2 <= n; i2++) {
                this.points[i2 - 1] = (ZonePoint) lt.rawget(new Double((double) i2));
            }
            if (this.active) {
                preprocess();
                walk(Engine.instance.player.position);
            }
        } else if ("Active".equals(key)) {
            a = LuaState.boolEval(value);
            if (a != this.active) {
                callEvent("OnZoneState", null);
            }
            this.active = a;
            if (a) {
                preprocess();
            }
            if (this.active) {
                walk(Engine.instance.player.position);
                return;
            }
            if (this.distanceRange >= 0.0d) {
                i = -1;
            }
            this.ncontain = i;
            this.contain = i;
            Engine.instance.player.leaveZone(this);
        } else if ("Visible".equals(key)) {
            a = LuaState.boolEval(value);
            if (a != this.visible) {
                callEvent("OnZoneState", null);
            }
            this.visible = a;
        } else if ("DistanceRange".equals(key) && (value instanceof Double)) {
            this.distanceRange = LuaState.fromDouble(value);
            preprocess();
            if (this.distanceRange < 0.0d && this.contain == -1) {
                this.ncontain = 0;
                this.contain = 0;
            }
        } else if ("ProximityRange".equals(key) && (value instanceof Double)) {
            preprocess();
            this.proximityRange = LuaState.fromDouble(value);
        } else if ("ShowObjects".equals(key)) {
            String v = (String) value;
            if ("Always".equals(v)) {
                this.showObjects = 0;
            } else if ("OnProximity".equals(v)) {
                this.showObjects = 2;
            } else if ("OnEnter".equals(v)) {
                this.showObjects = 1;
            } else if ("Never".equals(v)) {
                this.showObjects = 3;
            }
        } else if ("OriginalPoint".equals(key)) {
            this.position = (ZonePoint) value;
        } else {
            super.setItem(key, value);
        }
    }

    public void tick() {
        if (this.active && this.contain != this.ncontain) {
            setcontain();
        }
    }

    private void setcontain() {
        if (this.contain != this.ncontain) {
            if (this.contain == 2) {
                Engine.instance.player.leaveZone(this);
                Engine.callEvent(this, "OnExit", null);
            }
            this.contain = this.ncontain;
            if (this.contain == 2) {
                Engine.instance.player.enterZone(this);
            }
            switch (this.contain) {
                case -1:
                    Engine.log("ZONE: out-of-range " + this.name, 0);
                    Engine.callEvent(this, "OnNotInRange", null);
                    break;
                case 0:
                    Engine.log("ZONE: distant " + this.name, 0);
                    Engine.callEvent(this, "OnDistant", null);
                    break;
                case 1:
                    Engine.log("ZONE: proximity " + this.name, 0);
                    Engine.callEvent(this, "OnProximity", null);
                    break;
                case 2:
                    Engine.log("ZONE: inside " + this.name, 0);
                    Engine.callEvent(this, "OnEnter", null);
                    break;
                default:
                    return;
            }
            Engine.refreshUI();
        }
    }

    private void preprocess() {
        if (this.points != null && this.points.length != 0) {
            int i;
            double d;
            this.bbTop = Double.NEGATIVE_INFINITY;
            this.bbBottom = Double.POSITIVE_INFINITY;
            this.bbLeft = Double.POSITIVE_INFINITY;
            this.bbRight = Double.NEGATIVE_INFINITY;
            for (i = 0; i < this.points.length; i++) {
                this.bbTop = Math.max(this.bbTop, this.points[i].latitude);
                this.bbBottom = Math.min(this.bbBottom, this.points[i].latitude);
                this.bbLeft = Math.min(this.bbLeft, this.points[i].longitude);
                this.bbRight = Math.max(this.bbRight, this.points[i].longitude);
            }
            this.bbCenter.latitude = this.bbBottom + ((this.bbTop - this.bbBottom) / 2.0d);
            this.bbCenter.longitude = this.bbLeft + ((this.bbRight - this.bbLeft) / 2.0d);
            double proximityX = ZonePoint.m2lat(this.proximityRange < DEFAULT_PROXIMITY ? DEFAULT_PROXIMITY : this.proximityRange);
            double d2 = this.bbCenter.latitude;
            if (this.proximityRange < DEFAULT_PROXIMITY) {
                d = DEFAULT_PROXIMITY;
            } else {
                d = this.proximityRange;
            }
            double proximityY = ZonePoint.m2lon(d2, d);
            this.pbbTop = this.bbTop + proximityX;
            this.pbbBottom = this.bbBottom - proximityX;
            this.pbbLeft = this.bbLeft - proximityY;
            this.pbbRight = this.bbRight + proximityY;
            double dist = 0.0d;
            double xx = 0.0d;
            double yy = 0.0d;
            for (i = 0; i < this.points.length; i++) {
                double x = this.points[i].latitude - this.bbCenter.latitude;
                double y = this.points[i].longitude - this.bbCenter.longitude;
                double dd = (x * x) + (y * y);
                if (dd > dist) {
                    xx = this.points[i].latitude;
                    yy = this.points[i].longitude;
                    dist = dd;
                }
            }
            this.diameter = this.bbCenter.distance(xx, yy);
        }
    }

    public void walk(ZonePoint z) {
        if (this.active && this.points != null && this.points.length != 0 && z != null) {
            double dist = 0.0d;
            if (z.latitude <= this.pbbBottom || z.latitude >= this.pbbTop || z.longitude <= this.pbbLeft || z.longitude >= this.pbbRight) {
                this.distance = this.bbCenter.distance(z);
                dist = this.distance - this.diameter;
                this.nearestPoint.sync(this.bbCenter);
                if (dist < this.distanceRange || this.distanceRange < 0.0d) {
                    this.ncontain = 0;
                } else {
                    this.ncontain = -1;
                }
            } else {
                double ax;
                double ay;
                int i;
                double bx;
                double by;
                this.ncontain = 1;
                if (z.latitude > this.bbBottom && z.latitude < this.bbTop && z.longitude > this.bbLeft && z.longitude < this.bbRight && this.points.length > 2) {
                    double xt = z.latitude;
                    double yt = z.longitude;
                    ax = this.points[this.points.length - 1].latitude;
                    ay = this.points[this.points.length - 1].longitude;
                    boolean inside = false;
                    for (i = 0; i < this.points.length; i++) {
                        double x1;
                        bx = this.points[i].latitude;
                        by = this.points[i].longitude;
                        double y1;
                        double x2;
                        double y2;
                        if (bx > ax) {
                            x1 = ax;
                            y1 = ay;
                            x2 = bx;
                            y2 = by;
                        } else {
                            x1 = bx;
                            y1 = by;
                            x2 = ax;
                            y2 = ay;
                        }
                        if (x1 < xt && xt <= x2) {
                            if (ay > yt && by > yt) {
                                inside = !inside;
                            } else if ((ay >= yt || by >= yt) && (yt - y1) * (x2 - x1) < (y2 - y1) * (xt - x1)) {
                                inside = !inside;
                            }
                        }
                        ax = bx;
                        ay = by;
                    }
                    if (inside) {
                        this.ncontain = 2;
                        dist = 0.0d;
                        this.distance = 0.0d;
                        this.nearestPoint.sync(z);
                    }
                }
                if (this.ncontain != 2) {
                    ax = this.points[this.points.length - 1].latitude;
                    ay = this.points[this.points.length - 1].longitude;
                    double nx = ax;
                    double ny = ay;
                    double ndist = Double.POSITIVE_INFINITY;
                    for (i = 0; i < this.points.length; i++) {
                        double x;
                        double y;
                        bx = this.points[i].latitude;
                        by = this.points[i].longitude;
                        double dot_ta = ((z.latitude - ax) * (bx - ax)) + ((z.longitude - ay) * (by - ay));
                        if (dot_ta <= 0.0d) {
                            x = ax;
                            y = ay;
                        } else {
                            double dot_tb = ((z.latitude - bx) * (ax - bx)) + ((z.longitude - by) * (ay - by));
                            if (dot_tb <= 0.0d) {
                                x = bx;
                                y = by;
                            } else {
                                x = ax + (((bx - ax) * dot_ta) / (dot_ta + dot_tb));
                                y = ay + (((by - ay) * dot_ta) / (dot_ta + dot_tb));
                            }
                        }
                        double dd = ((x - z.latitude) * (x - z.latitude)) + ((y - z.longitude) * (y - z.longitude));
                        if (dd < ndist) {
                            nx = x;
                            ny = y;
                            ndist = dd;
                        }
                        ax = bx;
                        ay = by;
                    }
                    this.nearestPoint.latitude = nx;
                    this.nearestPoint.longitude = ny;
                    dist = z.distance(nx, ny);
                    this.distance = dist;
                    if (this.distance < this.proximityRange || this.proximityRange < 0.0d) {
                        this.ncontain = 1;
                    } else if (this.distance < this.distanceRange || this.distanceRange < 0.0d) {
                        this.ncontain = 0;
                    } else {
                        this.ncontain = -1;
                    }
                }
            }
            if (this.ncontain < this.contain) {
                switch (this.contain) {
                    case 0:
                        if (dist - this.distantTolerance < this.distanceRange) {
                            this.ncontain = 0;
                            break;
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        return;
                }
                if (dist - this.proximityTolerance < this.proximityRange) {
                    this.ncontain = 1;
                }
                if (dist - this.insideTolerance < 0.0d) {
                    this.ncontain = 2;
                }
            }
        }
    }

    public boolean showThings() {
        boolean z = true;
        if (!this.active) {
            return false;
        }
        switch (this.showObjects) {
            case 0:
                return true;
            case 1:
                if (this.contain != 2) {
                    z = false;
                }
                return z;
            case 2:
                if (this.contain < 1) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    public int visibleThings() {
        if (!showThings()) {
            return 0;
        }
        int count = 0;
        Object key = null;
        while (true) {
            key = this.inventory.next(key);
            if (key == null) {
                return count;
            }
            Object o = this.inventory.rawget(key);
            if (!(o instanceof Player) && (o instanceof Thing) && ((Thing) o).isVisible()) {
                count++;
            }
        }
    }

    public void collectThings(LuaTable c) {
        if (showThings()) {
            Object key = null;
            while (true) {
                key = this.inventory.next(key);
                if (key != null) {
                    Object z = this.inventory.rawget(key);
                    if ((z instanceof Thing) && ((Thing) z).isVisible()) {
                        TableLib.rawappend(c, z);
                    }
                } else {
                    return;
                }
            }
        }
    }

    public boolean contains(Thing t) {
        if (t == Engine.instance.player) {
            return this.contain == 2;
        } else {
            return super.contains(t);
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.contain);
        out.writeInt(this.ncontain);
        super.serialize(out);
    }

    public void deserialize(DataInputStream in) throws IOException {
        this.contain = in.readInt();
        this.ncontain = in.readInt();
        super.deserialize(in);
    }
}
