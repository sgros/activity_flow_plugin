// 
// Decompiled by Procyon v0.5.34
// 

package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaState;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.LuaTable;

public class Zone extends Thing
{
    private static final double DEFAULT_PROXIMITY = 1500.0;
    public static final int DISTANT = 0;
    public static final int INSIDE = 2;
    public static final int NOWHERE = -1;
    public static final int PROXIMITY = 1;
    public static final int S_ALWAYS = 0;
    public static final int S_NEVER = 3;
    public static final int S_ONENTER = 1;
    public static final int S_ONPROXIMITY = 2;
    private boolean active;
    public double bbBottom;
    public ZonePoint bbCenter;
    public double bbLeft;
    public double bbRight;
    public double bbTop;
    public int contain;
    private double diameter;
    public double distance;
    private double distanceRange;
    private double distantTolerance;
    private double insideTolerance;
    private int ncontain;
    public ZonePoint nearestPoint;
    public double pbbBottom;
    public double pbbLeft;
    public double pbbRight;
    public double pbbTop;
    public ZonePoint[] points;
    private double proximityRange;
    private double proximityTolerance;
    private int showObjects;
    
    public Zone() {
        this.active = false;
        this.contain = -1;
        this.ncontain = -1;
        this.showObjects = 1;
        this.distance = Double.MAX_VALUE;
        this.nearestPoint = new ZonePoint(0.0, 0.0, 0.0);
        this.distanceRange = -1.0;
        this.proximityRange = -1.0;
        this.bbCenter = new ZonePoint(0.0, 0.0, 0.0);
        this.insideTolerance = 5.0;
        this.proximityTolerance = 10.0;
        this.distantTolerance = 20.0;
    }
    
    private void preprocess() {
        if (this.points != null && this.points.length != 0) {
            this.bbTop = Double.NEGATIVE_INFINITY;
            this.bbBottom = Double.POSITIVE_INFINITY;
            this.bbLeft = Double.POSITIVE_INFINITY;
            this.bbRight = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < this.points.length; ++i) {
                this.bbTop = Math.max(this.bbTop, this.points[i].latitude);
                this.bbBottom = Math.min(this.bbBottom, this.points[i].latitude);
                this.bbLeft = Math.min(this.bbLeft, this.points[i].longitude);
                this.bbRight = Math.max(this.bbRight, this.points[i].longitude);
            }
            this.bbCenter.latitude = this.bbBottom + (this.bbTop - this.bbBottom) / 2.0;
            this.bbCenter.longitude = this.bbLeft + (this.bbRight - this.bbLeft) / 2.0;
            double proximityRange;
            if (this.proximityRange < 1500.0) {
                proximityRange = 1500.0;
            }
            else {
                proximityRange = this.proximityRange;
            }
            final double m2lat = ZonePoint.m2lat(proximityRange);
            final double latitude = this.bbCenter.latitude;
            double proximityRange2;
            if (this.proximityRange < 1500.0) {
                proximityRange2 = 1500.0;
            }
            else {
                proximityRange2 = this.proximityRange;
            }
            final double m2lon = ZonePoint.m2lon(latitude, proximityRange2);
            this.pbbTop = this.bbTop + m2lat;
            this.pbbBottom = this.bbBottom - m2lat;
            this.pbbLeft = this.bbLeft - m2lon;
            this.pbbRight = this.bbRight + m2lon;
            double n = 0.0;
            double latitude2 = 0.0;
            double longitude = 0.0;
            double n5;
            for (int j = 0; j < this.points.length; ++j, n = n5) {
                final double n2 = this.points[j].latitude - this.bbCenter.latitude;
                final double n3 = this.points[j].longitude - this.bbCenter.longitude;
                final double n4 = n2 * n2 + n3 * n3;
                n5 = n;
                if (n4 > n) {
                    latitude2 = this.points[j].latitude;
                    longitude = this.points[j].longitude;
                    n5 = n4;
                }
            }
            this.diameter = this.bbCenter.distance(latitude2, longitude);
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
                default: {
                    return;
                }
                case -1: {
                    Engine.log("ZONE: out-of-range " + this.name, 0);
                    Engine.callEvent(this, "OnNotInRange", null);
                    break;
                }
                case 2: {
                    Engine.log("ZONE: inside " + this.name, 0);
                    Engine.callEvent(this, "OnEnter", null);
                    break;
                }
                case 1: {
                    Engine.log("ZONE: proximity " + this.name, 0);
                    Engine.callEvent(this, "OnProximity", null);
                    break;
                }
                case 0: {
                    Engine.log("ZONE: distant " + this.name, 0);
                    Engine.callEvent(this, "OnDistant", null);
                    break;
                }
            }
            Engine.refreshUI();
        }
    }
    
    public void collectThings(final LuaTable luaTable) {
        if (this.showThings()) {
            Object o = null;
            while (true) {
                final Object next = this.inventory.next(o);
                if (next == null) {
                    break;
                }
                final Object rawget = this.inventory.rawget(next);
                o = next;
                if (!(rawget instanceof Thing)) {
                    continue;
                }
                o = next;
                if (!((Thing)rawget).isVisible()) {
                    continue;
                }
                TableLib.rawappend(luaTable, rawget);
                o = next;
            }
        }
    }
    
    @Override
    public boolean contains(final Thing thing) {
        boolean contains;
        if (thing == Engine.instance.player) {
            contains = (this.contain == 2);
        }
        else {
            contains = super.contains(thing);
        }
        return contains;
    }
    
    @Override
    public void deserialize(final DataInputStream dataInputStream) throws IOException {
        this.contain = dataInputStream.readInt();
        this.ncontain = dataInputStream.readInt();
        super.deserialize(dataInputStream);
    }
    
    @Override
    public boolean isLocated() {
        return true;
    }
    
    @Override
    public boolean isVisible() {
        return this.active && this.visible && this.contain > -1;
    }
    
    @Override
    protected String luaTostring() {
        return "a Zone instance";
    }
    
    @Override
    public void serialize(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.contain);
        dataOutputStream.writeInt(this.ncontain);
        super.serialize(dataOutputStream);
    }
    
    @Override
    protected void setItem(String anObject, final Object o) {
        int n = 0;
        if ("Points".equals(anObject) && o != null) {
            final LuaTable luaTable = (LuaTable)o;
            final int len = luaTable.len();
            this.points = new ZonePoint[len];
            for (int i = 1; i <= len; ++i) {
                this.points[i - 1] = (ZonePoint)luaTable.rawget(new Double(i));
            }
            if (this.active) {
                this.preprocess();
                this.walk(Engine.instance.player.position);
            }
        }
        else if ("Active".equals(anObject)) {
            final boolean boolEval = LuaState.boolEval(o);
            if (boolEval != this.active) {
                this.callEvent("OnZoneState", null);
            }
            this.active = boolEval;
            if (boolEval) {
                this.preprocess();
            }
            if (this.active) {
                this.walk(Engine.instance.player.position);
            }
            else {
                if (this.distanceRange >= 0.0) {
                    n = -1;
                }
                this.ncontain = n;
                this.contain = n;
                Engine.instance.player.leaveZone(this);
            }
        }
        else if ("Visible".equals(anObject)) {
            final boolean boolEval2 = LuaState.boolEval(o);
            if (boolEval2 != this.visible) {
                this.callEvent("OnZoneState", null);
            }
            this.visible = boolEval2;
        }
        else if ("DistanceRange".equals(anObject) && o instanceof Double) {
            this.distanceRange = LuaState.fromDouble(o);
            this.preprocess();
            if (this.distanceRange < 0.0 && this.contain == -1) {
                this.ncontain = 0;
                this.contain = 0;
            }
        }
        else if ("ProximityRange".equals(anObject) && o instanceof Double) {
            this.preprocess();
            this.proximityRange = LuaState.fromDouble(o);
        }
        else if ("ShowObjects".equals(anObject)) {
            anObject = (String)o;
            if ("Always".equals(anObject)) {
                this.showObjects = 0;
            }
            else if ("OnProximity".equals(anObject)) {
                this.showObjects = 2;
            }
            else if ("OnEnter".equals(anObject)) {
                this.showObjects = 1;
            }
            else if ("Never".equals(anObject)) {
                this.showObjects = 3;
            }
        }
        else if ("OriginalPoint".equals(anObject)) {
            this.position = (ZonePoint)o;
        }
        else {
            super.setItem(anObject, o);
        }
    }
    
    public boolean showThings() {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = false;
        boolean b4;
        if (!this.active) {
            b4 = b3;
        }
        else {
            b4 = b3;
            switch (this.showObjects) {
                case 1: {
                    b4 = (this.contain == 2 && b);
                    break;
                }
                case 2: {
                    b4 = (this.contain >= 1 && b2);
                    break;
                }
                case 0: {
                    b4 = true;
                }
                case 3: {
                    break;
                }
                default: {
                    b4 = b3;
                    break;
                }
            }
        }
        return b4;
    }
    
    public void tick() {
        if (this.active && this.contain != this.ncontain) {
            this.setcontain();
        }
    }
    
    public int visibleThings() {
        int n;
        if (!this.showThings()) {
            n = 0;
        }
        else {
            int n2 = 0;
            Object o = null;
            while (true) {
                final Object next = this.inventory.next(o);
                n = n2;
                if (next == null) {
                    break;
                }
                final Object rawget = this.inventory.rawget(next);
                o = next;
                if (rawget instanceof Player) {
                    continue;
                }
                o = next;
                if (!(rawget instanceof Thing)) {
                    continue;
                }
                o = next;
                if (!((Thing)rawget).isVisible()) {
                    continue;
                }
                ++n2;
                o = next;
            }
        }
        return n;
    }
    
    @Override
    public boolean visibleToPlayer() {
        return this.isVisible();
    }
    
    public void walk(final ZonePoint zonePoint) {
        if (this.active && this.points != null && this.points.length != 0 && zonePoint != null) {
            double distance;
            if (zonePoint.latitude > this.pbbBottom && zonePoint.latitude < this.pbbTop && zonePoint.longitude > this.pbbLeft && zonePoint.longitude < this.pbbRight) {
                this.ncontain = 1;
                if (zonePoint.latitude > this.bbBottom && zonePoint.latitude < this.bbTop && zonePoint.longitude > this.bbLeft && zonePoint.longitude < this.bbRight && this.points.length > 2) {
                    final double latitude = zonePoint.latitude;
                    final double longitude = zonePoint.longitude;
                    double latitude2 = this.points[this.points.length - 1].latitude;
                    double longitude2 = this.points[this.points.length - 1].longitude;
                    int n = 0;
                    int n7;
                    for (int i = 0; i < this.points.length; ++i, n = n7) {
                        final double latitude3 = this.points[i].latitude;
                        final double longitude3 = this.points[i].longitude;
                        double n2;
                        double n3;
                        double n4;
                        if (latitude3 > latitude2) {
                            n2 = latitude2;
                            n3 = longitude2;
                            latitude2 = latitude3;
                            n4 = longitude3;
                        }
                        else {
                            n2 = latitude3;
                            final double n5 = longitude3;
                            final double n6 = longitude2;
                            n3 = n5;
                            n4 = n6;
                        }
                        n7 = n;
                        Label_0292: {
                            if (n2 < latitude) {
                                n7 = n;
                                if (latitude <= latitude2) {
                                    if (longitude2 > longitude && longitude3 > longitude) {
                                        if (n == 0) {
                                            n7 = 1;
                                        }
                                        else {
                                            n7 = 0;
                                        }
                                    }
                                    else {
                                        if (longitude2 < longitude) {
                                            n7 = n;
                                            if (longitude3 < longitude) {
                                                break Label_0292;
                                            }
                                        }
                                        n7 = n;
                                        if ((longitude - n3) * (latitude2 - n2) < (n4 - n3) * (latitude - n2)) {
                                            if (n == 0) {
                                                n7 = 1;
                                            }
                                            else {
                                                n7 = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        latitude2 = latitude3;
                        longitude2 = longitude3;
                    }
                    if (n != 0) {
                        this.ncontain = 2;
                        this.distance = 0.0;
                        this.nearestPoint.sync(zonePoint);
                    }
                }
                distance = 0.0;
                if (this.ncontain != 2) {
                    double latitude4 = this.points[this.points.length - 1].latitude;
                    double longitude4 = this.points[this.points.length - 1].longitude;
                    double latitude5 = latitude4;
                    double longitude5 = longitude4;
                    double n8 = Double.POSITIVE_INFINITY;
                    double n12;
                    for (int j = 0; j < this.points.length; ++j, n8 = n12) {
                        final double latitude6 = this.points[j].latitude;
                        final double longitude6 = this.points[j].longitude;
                        final double n9 = (zonePoint.latitude - latitude4) * (latitude6 - latitude4) + (zonePoint.longitude - longitude4) * (longitude6 - longitude4);
                        if (n9 > 0.0) {
                            final double n10 = (zonePoint.latitude - latitude6) * (latitude4 - latitude6) + (zonePoint.longitude - longitude6) * (longitude4 - longitude6);
                            if (n10 <= 0.0) {
                                latitude4 = latitude6;
                                longitude4 = longitude6;
                            }
                            else {
                                latitude4 += (latitude6 - latitude4) * n9 / (n9 + n10);
                                longitude4 += (longitude6 - longitude4) * n9 / (n9 + n10);
                            }
                        }
                        final double n11 = (latitude4 - zonePoint.latitude) * (latitude4 - zonePoint.latitude) + (longitude4 - zonePoint.longitude) * (longitude4 - zonePoint.longitude);
                        n12 = n8;
                        if (n11 < n8) {
                            n12 = n11;
                            longitude5 = longitude4;
                            latitude5 = latitude4;
                        }
                        latitude4 = latitude6;
                        longitude4 = longitude6;
                    }
                    this.nearestPoint.latitude = latitude5;
                    this.nearestPoint.longitude = longitude5;
                    distance = zonePoint.distance(latitude5, longitude5);
                    this.distance = distance;
                    if (this.distance < this.proximityRange || this.proximityRange < 0.0) {
                        this.ncontain = 1;
                    }
                    else if (this.distance < this.distanceRange || this.distanceRange < 0.0) {
                        this.ncontain = 0;
                    }
                    else {
                        this.ncontain = -1;
                    }
                }
            }
            else {
                this.distance = this.bbCenter.distance(zonePoint);
                distance = this.distance - this.diameter;
                this.nearestPoint.sync(this.bbCenter);
                if (distance < this.distanceRange || this.distanceRange < 0.0) {
                    this.ncontain = 0;
                }
                else {
                    this.ncontain = -1;
                }
            }
            if (this.ncontain < this.contain) {
                switch (this.contain) {
                    case 0: {
                        if (distance - this.distantTolerance < this.distanceRange) {
                            this.ncontain = 0;
                        }
                    }
                    case 1: {
                        if (distance - this.proximityTolerance < this.proximityRange) {
                            this.ncontain = 1;
                        }
                    }
                    case 2: {
                        if (distance - this.insideTolerance < 0.0) {
                            this.ncontain = 2;
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
}
