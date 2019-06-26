// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.DataReaderBigEndian;
import java.io.InvalidObjectException;
import java.io.IOException;
import locus.api.objects.GeoData;

public class Circle extends GeoData
{
    private boolean drawPrecise;
    private Location loc;
    private float radius;
    
    public Circle() {
    }
    
    public Circle(final Location location, final float n) throws IOException {
        this(location, n, false);
    }
    
    public Circle(final Location loc, final float radius, final boolean drawPrecise) throws IOException {
        this.loc = loc;
        this.radius = radius;
        this.drawPrecise = drawPrecise;
        this.checkData();
    }
    
    public Circle(final byte[] array) throws IOException {
        super(array);
        this.checkData();
    }
    
    private void checkData() throws InvalidObjectException {
        if (this.loc == null) {
            throw new InvalidObjectException("Location cannot be 'null'");
        }
        if (this.radius <= 0.0f) {
            throw new InvalidObjectException("radius have to be bigger then 0");
        }
    }
    
    public Location getLocation() {
        return this.loc;
    }
    
    public float getRadius() {
        return this.radius;
    }
    
    @Override
    protected int getVersion() {
        return 1;
    }
    
    public boolean isDrawPrecise() {
        return this.drawPrecise;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.id = dataReaderBigEndian.readLong();
        this.name = dataReaderBigEndian.readString();
        this.readExtraData(dataReaderBigEndian);
        this.readStyles(dataReaderBigEndian);
        this.loc = new Location(dataReaderBigEndian);
        this.radius = dataReaderBigEndian.readFloat();
        this.drawPrecise = dataReaderBigEndian.readBoolean();
        if (n >= 1) {
            this.timeCreated = dataReaderBigEndian.readLong();
        }
    }
    
    @Override
    public void reset() {
        this.loc = null;
        this.radius = 0.0f;
        this.drawPrecise = false;
        this.timeCreated = System.currentTimeMillis();
    }
    
    public void setDrawPrecise(final boolean drawPrecise) {
        this.drawPrecise = drawPrecise;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.id);
        dataWriterBigEndian.writeString(this.name);
        this.writeExtraData(dataWriterBigEndian);
        this.writeStyles(dataWriterBigEndian);
        this.loc.write(dataWriterBigEndian);
        dataWriterBigEndian.writeFloat(this.radius);
        dataWriterBigEndian.writeBoolean(this.drawPrecise);
        dataWriterBigEndian.writeLong(this.timeCreated);
    }
}
