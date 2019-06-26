package locus.api.objects.extra;

import java.io.IOException;
import locus.api.objects.GeoData;
import locus.api.objects.GeoData.ReadWriteMode;
import locus.api.objects.geocaching.GeocachingData;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class Waypoint extends GeoData {
    private static final String TAG = "Waypoint";
    public static final String TAG_EXTRA_CALLBACK = "TAG_EXTRA_CALLBACK";
    public static final String TAG_EXTRA_ON_DISPLAY = "TAG_EXTRA_ON_DISPLAY";
    public GeocachingData gcData;
    private Location loc;

    public Waypoint(String name, Location loc) {
        setName(name);
        this.loc = loc;
    }

    public Waypoint() {
        this("", new Location(""));
    }

    public Waypoint(DataReaderBigEndian dr) throws IOException {
        super(dr);
    }

    public Waypoint(byte[] data) throws IOException {
        super(data);
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 2;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.f80id = dr.readLong();
        this.name = dr.readString();
        this.loc = new Location(dr);
        readExtraData(dr);
        readStyles(dr);
        this.gcData = readGeocachingData(dr);
        if (version >= 1) {
            this.timeCreated = dr.readLong();
        }
        if (version >= 2) {
            setReadWriteMode(ReadWriteMode.values()[dr.readInt()]);
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.f80id);
        dw.writeString(this.name);
        this.loc.write(dw);
        writeExtraData(dw);
        writeStyles(dw);
        writeGeocachingData(dw);
        dw.writeLong(this.timeCreated);
        dw.writeInt(getReadWriteMode().ordinal());
    }

    public void reset() {
        this.f80id = -1;
        this.name = "";
        this.loc = null;
        this.extraData = null;
        this.styleNormal = null;
        this.styleHighlight = null;
        this.gcData = null;
        this.timeCreated = System.currentTimeMillis();
        setReadWriteMode(ReadWriteMode.READ_WRITE);
    }

    public static GeocachingData readGeocachingData(DataReaderBigEndian dr) throws IOException {
        if (dr.readBoolean()) {
            return new GeocachingData(dr);
        }
        return null;
    }

    private void writeGeocachingData(DataWriterBigEndian dw) throws IOException {
        if (this.gcData != null) {
            dw.writeBoolean(true);
            this.gcData.write(dw);
            return;
        }
        dw.writeBoolean(false);
    }

    public Location getLocation() {
        return this.loc;
    }

    public void setLocation(Location loc) {
        if (loc == null) {
            Logger.logW(TAG, "setLocation(null), unable to set invalid Location object");
        } else {
            this.loc = loc;
        }
    }

    public String getExtraCallback() {
        if (this.extraData != null) {
            return this.extraData.getParameter(20);
        }
        return null;
    }

    public void setExtraCallback(String btnName, String packageName, String className, String returnDataName, String returnDataValue) {
        String callBack = ExtraData.generateCallbackString(btnName, packageName, className, returnDataName, returnDataValue);
        if (callBack.length() != 0) {
            StringBuilder b = new StringBuilder();
            b.append(TAG_EXTRA_CALLBACK).append(";");
            b.append(callBack).append(";");
            addParameter(20, b.toString());
        }
    }

    public void removeExtraCallback() {
        addParameter(20, "clear");
    }

    public String getExtraOnDisplay() {
        if (this.extraData != null) {
            return this.extraData.getParameter(21);
        }
        return null;
    }

    public void setExtraOnDisplay(String packageName, String className, String returnDataName, String returnDataValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG_EXTRA_ON_DISPLAY).append(";");
        sb.append(packageName).append(";");
        sb.append(className).append(";");
        sb.append(returnDataName).append(";");
        sb.append(returnDataValue).append(";");
        addParameter(21, sb.toString());
    }

    public void removeExtraOnDisplay() {
        addParameter(21, "clear");
    }

    public byte[] getGeocachingData() {
        try {
            DataWriterBigEndian dw = new DataWriterBigEndian();
            writeGeocachingData(dw);
            return dw.toByteArray();
        } catch (IOException e) {
            Logger.logE(TAG, "getGeocachingData()", e);
            return null;
        }
    }

    public void setGeocachingData(byte[] data) {
        try {
            this.gcData = readGeocachingData(new DataReaderBigEndian(data));
        } catch (Exception e) {
            Logger.logE(TAG, "setGeocachingData(" + data + ")", e);
            this.gcData = null;
        }
    }
}
