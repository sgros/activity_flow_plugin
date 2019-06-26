package locus.api.objects.extra;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public class Location extends Storable {
    private static final String TAG = "Location";
    public double latitude;
    public double longitude;
    private double mAltitude;
    private ExtraBasic mExtraBasic;
    private ExtraSensor mExtraSensor;
    private boolean mHasAltitude;
    private long mId;
    private String provider;
    private long time;

    private static class ExtraBasic implements Cloneable {
        float accuracy = 0.0f;
        float bearing = 0.0f;
        boolean hasAccuracy = false;
        boolean hasBearing = false;
        boolean hasSpeed = false;
        float speed = 0.0f;

        ExtraBasic() {
        }

        public ExtraBasic clone() {
            ExtraBasic newExtra = new ExtraBasic();
            newExtra.hasSpeed = this.hasSpeed;
            newExtra.speed = this.speed;
            newExtra.hasBearing = this.hasBearing;
            newExtra.bearing = this.bearing;
            newExtra.hasAccuracy = this.hasAccuracy;
            newExtra.accuracy = this.accuracy;
            return newExtra;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasData() {
            return this.hasSpeed || this.hasBearing || this.hasAccuracy;
        }

        public String toString() {
            return Utils.toString(this, "    ");
        }
    }

    private static class ExtraSensor extends Storable implements Cloneable {
        @Deprecated
        private int battery;
        int cadence;
        @Deprecated
        private boolean hasBattery;
        boolean hasCadence;
        boolean hasHr;
        boolean hasPower;
        boolean hasSpeed;
        boolean hasStrides;
        boolean hasTemperature;
        /* renamed from: hr */
        int f81hr;
        float power;
        float speed;
        int strides;
        float temperature;

        public ExtraSensor(DataReaderBigEndian dr) throws IOException {
            super(dr);
        }

        public ExtraSensor clone() {
            ExtraSensor newExtra = new ExtraSensor();
            newExtra.hasHr = this.hasHr;
            newExtra.f81hr = this.f81hr;
            newExtra.hasCadence = this.hasCadence;
            newExtra.cadence = this.cadence;
            newExtra.hasSpeed = this.hasSpeed;
            newExtra.speed = this.speed;
            newExtra.hasPower = this.hasPower;
            newExtra.power = this.power;
            newExtra.hasStrides = this.hasStrides;
            newExtra.strides = this.strides;
            newExtra.hasTemperature = this.hasTemperature;
            newExtra.temperature = this.temperature;
            return newExtra;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasData() {
            return this.hasHr || this.hasCadence || this.hasSpeed || this.hasPower || this.hasStrides || this.hasTemperature;
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 1;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            this.hasHr = dr.readBoolean();
            this.f81hr = dr.readInt();
            this.hasCadence = dr.readBoolean();
            this.cadence = dr.readInt();
            this.hasSpeed = dr.readBoolean();
            this.speed = dr.readFloat();
            this.hasPower = dr.readBoolean();
            this.power = dr.readFloat();
            this.hasStrides = dr.readBoolean();
            this.strides = dr.readInt();
            this.hasBattery = dr.readBoolean();
            this.battery = dr.readInt();
            if (version >= 1) {
                this.hasTemperature = dr.readBoolean();
                this.temperature = dr.readFloat();
            }
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeBoolean(this.hasHr);
            dw.writeInt(this.f81hr);
            dw.writeBoolean(this.hasCadence);
            dw.writeInt(this.cadence);
            dw.writeBoolean(this.hasSpeed);
            dw.writeFloat(this.speed);
            dw.writeBoolean(this.hasPower);
            dw.writeFloat(this.power);
            dw.writeBoolean(this.hasStrides);
            dw.writeInt(this.strides);
            dw.writeBoolean(this.hasBattery);
            dw.writeInt(this.battery);
            dw.writeBoolean(this.hasTemperature);
            dw.writeFloat(this.temperature);
        }

        public void reset() {
            this.hasHr = false;
            this.f81hr = 0;
            this.hasCadence = false;
            this.cadence = 0;
            this.hasSpeed = false;
            this.speed = 0.0f;
            this.hasPower = false;
            this.power = 0.0f;
            this.hasStrides = false;
            this.strides = 0;
            this.hasBattery = false;
            this.battery = 0;
            this.hasTemperature = false;
            this.temperature = 0.0f;
        }

        public String toString() {
            return Utils.toString(this, "    ");
        }
    }

    public Location(String provider) {
        setProvider(provider);
    }

    public Location(String provider, double lat, double lon) {
        setProvider(provider);
        setLatitude(lat);
        setLongitude(lon);
    }

    public Location() {
        this("");
    }

    public Location(DataReaderBigEndian dr) throws IOException {
        super(dr);
    }

    public Location(Location loc) {
        set(loc);
    }

    public Location(byte[] data) throws IOException {
        super(data);
    }

    public void set(Location loc) {
        this.mId = loc.mId;
        this.provider = loc.provider;
        this.time = loc.time;
        this.latitude = loc.latitude;
        this.longitude = loc.longitude;
        this.mHasAltitude = loc.hasAltitude();
        this.mAltitude = loc.getAltitude();
        if (loc.mExtraBasic == null || !loc.mExtraBasic.hasData()) {
            this.mExtraBasic = null;
        } else {
            this.mExtraBasic = loc.mExtraBasic.clone();
            if (!this.mExtraBasic.hasData()) {
                this.mExtraBasic = null;
            }
        }
        if (loc.mExtraSensor == null || !loc.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
            return;
        }
        this.mExtraSensor = loc.mExtraSensor.clone();
        if (!this.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 2;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readLong();
        this.provider = dr.readString();
        this.time = dr.readLong();
        this.latitude = dr.readDouble();
        this.longitude = dr.readDouble();
        this.mHasAltitude = dr.readBoolean();
        this.mAltitude = dr.readDouble();
        if (dr.readBoolean()) {
            this.mExtraBasic = new ExtraBasic();
            this.mExtraBasic.hasAccuracy = dr.readBoolean();
            this.mExtraBasic.accuracy = dr.readFloat();
            this.mExtraBasic.hasBearing = dr.readBoolean();
            this.mExtraBasic.bearing = dr.readFloat();
            this.mExtraBasic.hasSpeed = dr.readBoolean();
            this.mExtraBasic.speed = dr.readFloat();
            if (!this.mExtraBasic.hasData()) {
                this.mExtraBasic = null;
            }
        }
        if (version < 1 || !dr.readBoolean()) {
            return;
        }
        if (version == 1) {
            readSensorVersion1(dr);
        } else {
            this.mExtraSensor = new ExtraSensor(dr);
        }
    }

    private void readSensorVersion1(DataReaderBigEndian dr) throws IOException {
        this.mExtraSensor = new ExtraSensor();
        this.mExtraSensor.hasHr = dr.readBoolean();
        this.mExtraSensor.f81hr = dr.readInt();
        this.mExtraSensor.hasCadence = dr.readBoolean();
        this.mExtraSensor.cadence = dr.readInt();
        this.mExtraSensor.hasSpeed = dr.readBoolean();
        this.mExtraSensor.speed = dr.readFloat();
        this.mExtraSensor.hasPower = dr.readBoolean();
        this.mExtraSensor.power = dr.readFloat();
        if (!this.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.mId);
        dw.writeString(this.provider);
        dw.writeLong(this.time);
        dw.writeDouble(this.latitude);
        dw.writeDouble(this.longitude);
        dw.writeBoolean(this.mHasAltitude);
        dw.writeDouble(this.mAltitude);
        if (this.mExtraBasic == null || !this.mExtraBasic.hasData()) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            dw.writeBoolean(this.mExtraBasic.hasAccuracy);
            dw.writeFloat(this.mExtraBasic.accuracy);
            dw.writeBoolean(this.mExtraBasic.hasBearing);
            dw.writeFloat(this.mExtraBasic.bearing);
            dw.writeBoolean(this.mExtraBasic.hasSpeed);
            dw.writeFloat(this.mExtraBasic.speed);
        }
        if (this.mExtraSensor == null || !this.mExtraSensor.hasData()) {
            dw.writeBoolean(false);
            return;
        }
        dw.writeBoolean(true);
        this.mExtraSensor.write(dw);
    }

    public void reset() {
        this.mId = -1;
        this.provider = null;
        this.time = 0;
        this.latitude = 0.0d;
        this.longitude = 0.0d;
        this.mExtraBasic = null;
        this.mExtraSensor = null;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getProvider() {
        return this.provider;
    }

    public Location setProvider(String provider) {
        if (provider == null) {
            this.provider = "";
        } else {
            this.provider = provider;
        }
        return this;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public Location setLatitude(double lat) {
        if (lat < -90.0d) {
            Logger.logE(TAG, "setLatitude(" + lat + "), " + "invalid latitude", new Exception(""));
            lat = -90.0d;
        } else if (lat > 90.0d) {
            Logger.logE(TAG, "setLatitude(" + lat + "), " + "invalid latitude", new Exception(""));
            lat = 90.0d;
        }
        this.latitude = lat;
        return this;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public Location setLongitude(double lon) {
        if (lon < -180.0d) {
            lon += 360.0d;
        } else if (lon > 180.0d) {
            lon -= 360.0d;
        }
        this.longitude = lon;
        return this;
    }

    public boolean hasAltitude() {
        return this.mHasAltitude;
    }

    public double getAltitude() {
        if (hasAltitude()) {
            return this.mAltitude;
        }
        return 0.0d;
    }

    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
        this.mHasAltitude = true;
    }

    public void removeAltitude() {
        this.mAltitude = 0.0d;
        this.mHasAltitude = false;
    }

    public boolean hasSpeed() {
        return this.mExtraBasic != null && this.mExtraBasic.hasSpeed;
    }

    public float getSpeed() {
        if (hasSpeed()) {
            return this.mExtraBasic.speed;
        }
        return 0.0f;
    }

    public void setSpeed(float speed) {
        if (this.mExtraBasic == null) {
            this.mExtraBasic = new ExtraBasic();
        }
        this.mExtraBasic.speed = speed;
        this.mExtraBasic.hasSpeed = true;
    }

    public void removeSpeed() {
        if (this.mExtraBasic != null) {
            this.mExtraBasic.speed = 0.0f;
            this.mExtraBasic.hasSpeed = false;
            checkExtraBasic();
        }
    }

    public boolean hasBearing() {
        return this.mExtraBasic != null && this.mExtraBasic.hasBearing;
    }

    public float getBearing() {
        if (hasBearing()) {
            return this.mExtraBasic.bearing;
        }
        return 0.0f;
    }

    public void setBearing(float bearing) {
        while (bearing < 0.0f) {
            bearing += 360.0f;
        }
        while (bearing >= 360.0f) {
            bearing -= 360.0f;
        }
        if (this.mExtraBasic == null) {
            this.mExtraBasic = new ExtraBasic();
        }
        this.mExtraBasic.bearing = bearing;
        this.mExtraBasic.hasBearing = true;
    }

    public void removeBearing() {
        if (this.mExtraBasic != null) {
            this.mExtraBasic.bearing = 0.0f;
            this.mExtraBasic.hasBearing = false;
            checkExtraBasic();
        }
    }

    public boolean hasAccuracy() {
        return this.mExtraBasic != null && this.mExtraBasic.hasAccuracy;
    }

    public float getAccuracy() {
        if (hasAccuracy()) {
            return this.mExtraBasic.accuracy;
        }
        return 0.0f;
    }

    public void setAccuracy(float accuracy) {
        if (this.mExtraBasic == null) {
            this.mExtraBasic = new ExtraBasic();
        }
        this.mExtraBasic.accuracy = accuracy;
        this.mExtraBasic.hasAccuracy = true;
    }

    public void removeAccuracy() {
        if (this.mExtraBasic != null) {
            this.mExtraBasic.accuracy = 0.0f;
            this.mExtraBasic.hasAccuracy = false;
            checkExtraBasic();
        }
    }

    private void checkExtraBasic() {
        if (!this.mExtraBasic.hasData()) {
            this.mExtraBasic = null;
        }
    }

    public boolean hasSensorHeartRate() {
        return this.mExtraSensor != null && this.mExtraSensor.hasHr;
    }

    public int getSensorHeartRate() {
        if (hasSensorHeartRate()) {
            return this.mExtraSensor.f81hr;
        }
        return 0;
    }

    public void setSensorHeartRate(int heartRate) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.f81hr = heartRate;
        this.mExtraSensor.hasHr = true;
    }

    public void removeSensorHeartRate() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.f81hr = 0;
            this.mExtraSensor.hasHr = false;
            checkExtraSensor();
        }
    }

    public boolean hasSensorCadence() {
        return this.mExtraSensor != null && this.mExtraSensor.hasCadence;
    }

    public int getSensorCadence() {
        if (hasSensorCadence()) {
            return this.mExtraSensor.cadence;
        }
        return 0;
    }

    public void setSensorCadence(int cadence) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.cadence = cadence;
        this.mExtraSensor.hasCadence = true;
    }

    public void removeSensorCadence() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.cadence = 0;
            this.mExtraSensor.hasCadence = false;
            checkExtraSensor();
        }
    }

    public boolean hasSensorSpeed() {
        return this.mExtraSensor != null && this.mExtraSensor.hasSpeed;
    }

    public float getSensorSpeed() {
        if (hasSensorSpeed()) {
            return this.mExtraSensor.speed;
        }
        return 0.0f;
    }

    public void setSensorSpeed(float speed) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.speed = speed;
        this.mExtraSensor.hasSpeed = true;
    }

    public void removeSensorSpeed() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.speed = 0.0f;
            this.mExtraSensor.hasSpeed = false;
            checkExtraSensor();
        }
    }

    public boolean hasSensorPower() {
        return this.mExtraSensor != null && this.mExtraSensor.hasPower;
    }

    public float getSensorPower() {
        if (hasSensorPower()) {
            return this.mExtraSensor.power;
        }
        return 0.0f;
    }

    public void setSensorPower(float power) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.power = power;
        this.mExtraSensor.hasPower = true;
    }

    public void removeSensorPower() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.power = 0.0f;
            this.mExtraSensor.hasPower = false;
            checkExtraSensor();
        }
    }

    public boolean hasSensorStrides() {
        return this.mExtraSensor != null && this.mExtraSensor.hasStrides;
    }

    public int getSensorStrides() {
        if (hasSensorStrides()) {
            return this.mExtraSensor.strides;
        }
        return 0;
    }

    public void setSensorStrides(int strides) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.strides = strides;
        this.mExtraSensor.hasStrides = true;
    }

    public void removeSensorStrides() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.strides = 0;
            this.mExtraSensor.hasStrides = false;
            checkExtraSensor();
        }
    }

    public boolean hasSensorTemperature() {
        return this.mExtraSensor != null && this.mExtraSensor.hasTemperature;
    }

    public float getSensorTemperature() {
        if (hasSensorTemperature()) {
            return this.mExtraSensor.temperature;
        }
        return 0.0f;
    }

    public void setSensorTemperature(float temperature) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.temperature = temperature;
        this.mExtraSensor.hasTemperature = true;
    }

    public void removeSensorTemperature() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.temperature = 0.0f;
            this.mExtraSensor.hasTemperature = false;
            checkExtraSensor();
        }
    }

    private void checkExtraSensor() {
        if (!this.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
        }
    }

    public String toString() {
        return "Location [tag:" + this.provider + ", " + "lon:" + this.longitude + ", " + "lat:" + this.latitude + ", " + "alt:" + this.mAltitude + "]";
    }

    public float distanceTo(Location dest) {
        return new LocationCompute(this).distanceTo(dest);
    }

    public float bearingTo(Location dest) {
        return new LocationCompute(this).bearingTo(dest);
    }

    public float[] distanceAndBearingTo(Location dest) {
        LocationCompute com = new LocationCompute(this);
        return new float[]{com.distanceTo(dest), com.bearingTo(dest)};
    }

    public boolean hasSpeedOptimal() {
        return hasSpeed() || hasSensorSpeed();
    }

    public float getSpeedOptimal() {
        if (hasSensorSpeed()) {
            return getSensorSpeed();
        }
        return getSpeed();
    }

    public void removeSensorAll() {
        this.mExtraSensor = null;
    }
}
