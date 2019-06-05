// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import locus.api.utils.Logger;
import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.geocaching.GeocachingData;
import locus.api.objects.GeoData;

public class Waypoint extends GeoData
{
    private static final String TAG = "Waypoint";
    public static final String TAG_EXTRA_CALLBACK = "TAG_EXTRA_CALLBACK";
    public static final String TAG_EXTRA_ON_DISPLAY = "TAG_EXTRA_ON_DISPLAY";
    public GeocachingData gcData;
    private Location loc;
    
    public Waypoint() {
        this("", new Location(""));
    }
    
    public Waypoint(final String name, final Location loc) {
        this.setName(name);
        this.loc = loc;
    }
    
    public Waypoint(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
    }
    
    public Waypoint(final byte[] array) throws IOException {
        super(array);
    }
    
    public static GeocachingData readGeocachingData(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        GeocachingData geocachingData;
        if (dataReaderBigEndian.readBoolean()) {
            geocachingData = new GeocachingData(dataReaderBigEndian);
        }
        else {
            geocachingData = null;
        }
        return geocachingData;
    }
    
    private void writeGeocachingData(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        if (this.gcData != null) {
            dataWriterBigEndian.writeBoolean(true);
            this.gcData.write(dataWriterBigEndian);
        }
        else {
            dataWriterBigEndian.writeBoolean(false);
        }
    }
    
    public String getExtraCallback() {
        String parameter;
        if (this.extraData != null) {
            parameter = this.extraData.getParameter(20);
        }
        else {
            parameter = null;
        }
        return parameter;
    }
    
    public String getExtraOnDisplay() {
        String parameter;
        if (this.extraData != null) {
            parameter = this.extraData.getParameter(21);
        }
        else {
            parameter = null;
        }
        return parameter;
    }
    
    public byte[] getGeocachingData() {
        try {
            final DataWriterBigEndian dataWriterBigEndian = new DataWriterBigEndian();
            this.writeGeocachingData(dataWriterBigEndian);
            return dataWriterBigEndian.toByteArray();
        }
        catch (IOException ex) {
            Logger.logE("Waypoint", "getGeocachingData()", ex);
            return null;
        }
    }
    
    public Location getLocation() {
        return this.loc;
    }
    
    @Override
    protected int getVersion() {
        return 2;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.id = dataReaderBigEndian.readLong();
        this.name = dataReaderBigEndian.readString();
        this.loc = new Location(dataReaderBigEndian);
        this.readExtraData(dataReaderBigEndian);
        this.readStyles(dataReaderBigEndian);
        this.gcData = readGeocachingData(dataReaderBigEndian);
        if (n >= 1) {
            this.timeCreated = dataReaderBigEndian.readLong();
        }
        if (n >= 2) {
            this.setReadWriteMode(ReadWriteMode.values()[dataReaderBigEndian.readInt()]);
        }
    }
    
    public void removeExtraCallback() {
        this.addParameter(20, "clear");
    }
    
    public void removeExtraOnDisplay() {
        this.addParameter(21, "clear");
    }
    
    @Override
    public void reset() {
        this.id = -1L;
        this.name = "";
        this.loc = null;
        this.extraData = null;
        this.styleNormal = null;
        this.styleHighlight = null;
        this.gcData = null;
        this.timeCreated = System.currentTimeMillis();
        this.setReadWriteMode(ReadWriteMode.READ_WRITE);
    }
    
    public void setExtraCallback(final String s, String generateCallbackString, final String s2, final String s3, final String s4) {
        generateCallbackString = ExtraData.generateCallbackString(s, generateCallbackString, s2, s3, s4);
        if (generateCallbackString.length() != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("TAG_EXTRA_CALLBACK").append(";");
            sb.append(generateCallbackString).append(";");
            this.addParameter(20, sb.toString());
        }
    }
    
    public void setExtraOnDisplay(final String str, final String str2, final String str3, final String str4) {
        final StringBuilder sb = new StringBuilder();
        sb.append("TAG_EXTRA_ON_DISPLAY").append(";");
        sb.append(str).append(";");
        sb.append(str2).append(";");
        sb.append(str3).append(";");
        sb.append(str4).append(";");
        this.addParameter(21, sb.toString());
    }
    
    public void setGeocachingData(final byte[] obj) {
        try {
            this.gcData = readGeocachingData(new DataReaderBigEndian(obj));
        }
        catch (Exception ex) {
            Logger.logE("Waypoint", "setGeocachingData(" + obj + ")", ex);
            this.gcData = null;
        }
    }
    
    public void setLocation(final Location loc) {
        if (loc == null) {
            Logger.logW("Waypoint", "setLocation(null), unable to set invalid Location object");
        }
        else {
            this.loc = loc;
        }
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.id);
        dataWriterBigEndian.writeString(this.name);
        this.loc.write(dataWriterBigEndian);
        this.writeExtraData(dataWriterBigEndian);
        this.writeStyles(dataWriterBigEndian);
        this.writeGeocachingData(dataWriterBigEndian);
        dataWriterBigEndian.writeLong(this.timeCreated);
        dataWriterBigEndian.writeInt(this.getReadWriteMode().ordinal());
    }
}
