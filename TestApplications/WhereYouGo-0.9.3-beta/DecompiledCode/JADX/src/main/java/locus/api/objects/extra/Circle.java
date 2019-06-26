package locus.api.objects.extra;

import java.io.IOException;
import java.io.InvalidObjectException;
import locus.api.objects.GeoData;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class Circle extends GeoData {
    private boolean drawPrecise;
    private Location loc;
    private float radius;

    public Circle(Location loc, float radius) throws IOException {
        this(loc, radius, false);
    }

    public Circle(Location loc, float radius, boolean drawPrecise) throws IOException {
        this.loc = loc;
        this.radius = radius;
        this.drawPrecise = drawPrecise;
        checkData();
    }

    public Circle(byte[] data) throws IOException {
        super(data);
        checkData();
    }

    private void checkData() throws InvalidObjectException {
        if (this.loc == null) {
            throw new InvalidObjectException("Location cannot be 'null'");
        } else if (this.radius <= 0.0f) {
            throw new InvalidObjectException("radius have to be bigger then 0");
        }
    }

    public Location getLocation() {
        return this.loc;
    }

    public float getRadius() {
        return this.radius;
    }

    public boolean isDrawPrecise() {
        return this.drawPrecise;
    }

    public void setDrawPrecise(boolean drawPrecise) {
        this.drawPrecise = drawPrecise;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 1;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.f80id = dr.readLong();
        this.name = dr.readString();
        readExtraData(dr);
        readStyles(dr);
        this.loc = new Location(dr);
        this.radius = dr.readFloat();
        this.drawPrecise = dr.readBoolean();
        if (version >= 1) {
            this.timeCreated = dr.readLong();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.f80id);
        dw.writeString(this.name);
        writeExtraData(dw);
        writeStyles(dw);
        this.loc.write(dw);
        dw.writeFloat(this.radius);
        dw.writeBoolean(this.drawPrecise);
        dw.writeLong(this.timeCreated);
    }

    public void reset() {
        this.loc = null;
        this.radius = 0.0f;
        this.drawPrecise = false;
        this.timeCreated = System.currentTimeMillis();
    }
}
