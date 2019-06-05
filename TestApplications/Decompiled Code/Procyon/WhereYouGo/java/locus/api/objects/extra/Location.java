// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import locus.api.utils.Utils;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.Storable;

public class Location extends Storable
{
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
    
    public Location() {
        this("");
    }
    
    public Location(final String provider) {
        this.setProvider(provider);
    }
    
    public Location(final String provider, final double latitude, final double longitude) {
        this.setProvider(provider);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
    
    public Location(final Location location) {
        this.set(location);
    }
    
    public Location(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
    }
    
    public Location(final byte[] array) throws IOException {
        super(array);
    }
    
    private void checkExtraBasic() {
        if (!this.mExtraBasic.hasData()) {
            this.mExtraBasic = null;
        }
    }
    
    private void checkExtraSensor() {
        if (!this.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
        }
    }
    
    private void readSensorVersion1(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mExtraSensor = new ExtraSensor();
        this.mExtraSensor.hasHr = dataReaderBigEndian.readBoolean();
        this.mExtraSensor.hr = dataReaderBigEndian.readInt();
        this.mExtraSensor.hasCadence = dataReaderBigEndian.readBoolean();
        this.mExtraSensor.cadence = dataReaderBigEndian.readInt();
        this.mExtraSensor.hasSpeed = dataReaderBigEndian.readBoolean();
        this.mExtraSensor.speed = dataReaderBigEndian.readFloat();
        this.mExtraSensor.hasPower = dataReaderBigEndian.readBoolean();
        this.mExtraSensor.power = dataReaderBigEndian.readFloat();
        if (!this.mExtraSensor.hasData()) {
            this.mExtraSensor = null;
        }
    }
    
    public float bearingTo(final Location location) {
        return new LocationCompute(this).bearingTo(location);
    }
    
    public float[] distanceAndBearingTo(final Location location) {
        final LocationCompute locationCompute = new LocationCompute(this);
        return new float[] { locationCompute.distanceTo(location), locationCompute.bearingTo(location) };
    }
    
    public float distanceTo(final Location location) {
        return new LocationCompute(this).distanceTo(location);
    }
    
    public float getAccuracy() {
        float accuracy;
        if (this.hasAccuracy()) {
            accuracy = this.mExtraBasic.accuracy;
        }
        else {
            accuracy = 0.0f;
        }
        return accuracy;
    }
    
    public double getAltitude() {
        double mAltitude;
        if (this.hasAltitude()) {
            mAltitude = this.mAltitude;
        }
        else {
            mAltitude = 0.0;
        }
        return mAltitude;
    }
    
    public float getBearing() {
        float bearing;
        if (this.hasBearing()) {
            bearing = this.mExtraBasic.bearing;
        }
        else {
            bearing = 0.0f;
        }
        return bearing;
    }
    
    public long getId() {
        return this.mId;
    }
    
    public double getLatitude() {
        return this.latitude;
    }
    
    public double getLongitude() {
        return this.longitude;
    }
    
    public String getProvider() {
        return this.provider;
    }
    
    public int getSensorCadence() {
        int cadence;
        if (this.hasSensorCadence()) {
            cadence = this.mExtraSensor.cadence;
        }
        else {
            cadence = 0;
        }
        return cadence;
    }
    
    public int getSensorHeartRate() {
        int hr;
        if (this.hasSensorHeartRate()) {
            hr = this.mExtraSensor.hr;
        }
        else {
            hr = 0;
        }
        return hr;
    }
    
    public float getSensorPower() {
        float power;
        if (this.hasSensorPower()) {
            power = this.mExtraSensor.power;
        }
        else {
            power = 0.0f;
        }
        return power;
    }
    
    public float getSensorSpeed() {
        float speed;
        if (this.hasSensorSpeed()) {
            speed = this.mExtraSensor.speed;
        }
        else {
            speed = 0.0f;
        }
        return speed;
    }
    
    public int getSensorStrides() {
        int strides;
        if (this.hasSensorStrides()) {
            strides = this.mExtraSensor.strides;
        }
        else {
            strides = 0;
        }
        return strides;
    }
    
    public float getSensorTemperature() {
        float temperature;
        if (this.hasSensorTemperature()) {
            temperature = this.mExtraSensor.temperature;
        }
        else {
            temperature = 0.0f;
        }
        return temperature;
    }
    
    public float getSpeed() {
        float speed;
        if (this.hasSpeed()) {
            speed = this.mExtraBasic.speed;
        }
        else {
            speed = 0.0f;
        }
        return speed;
    }
    
    public float getSpeedOptimal() {
        float n;
        if (this.hasSensorSpeed()) {
            n = this.getSensorSpeed();
        }
        else {
            n = this.getSpeed();
        }
        return n;
    }
    
    public long getTime() {
        return this.time;
    }
    
    @Override
    protected int getVersion() {
        return 2;
    }
    
    public boolean hasAccuracy() {
        return this.mExtraBasic != null && this.mExtraBasic.hasAccuracy;
    }
    
    public boolean hasAltitude() {
        return this.mHasAltitude;
    }
    
    public boolean hasBearing() {
        return this.mExtraBasic != null && this.mExtraBasic.hasBearing;
    }
    
    public boolean hasSensorCadence() {
        return this.mExtraSensor != null && this.mExtraSensor.hasCadence;
    }
    
    public boolean hasSensorHeartRate() {
        return this.mExtraSensor != null && this.mExtraSensor.hasHr;
    }
    
    public boolean hasSensorPower() {
        return this.mExtraSensor != null && this.mExtraSensor.hasPower;
    }
    
    public boolean hasSensorSpeed() {
        return this.mExtraSensor != null && this.mExtraSensor.hasSpeed;
    }
    
    public boolean hasSensorStrides() {
        return this.mExtraSensor != null && this.mExtraSensor.hasStrides;
    }
    
    public boolean hasSensorTemperature() {
        return this.mExtraSensor != null && this.mExtraSensor.hasTemperature;
    }
    
    public boolean hasSpeed() {
        return this.mExtraBasic != null && this.mExtraBasic.hasSpeed;
    }
    
    public boolean hasSpeedOptimal() {
        return this.hasSpeed() || this.hasSensorSpeed();
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mId = dataReaderBigEndian.readLong();
        this.provider = dataReaderBigEndian.readString();
        this.time = dataReaderBigEndian.readLong();
        this.latitude = dataReaderBigEndian.readDouble();
        this.longitude = dataReaderBigEndian.readDouble();
        this.mHasAltitude = dataReaderBigEndian.readBoolean();
        this.mAltitude = dataReaderBigEndian.readDouble();
        if (dataReaderBigEndian.readBoolean()) {
            this.mExtraBasic = new ExtraBasic();
            this.mExtraBasic.hasAccuracy = dataReaderBigEndian.readBoolean();
            this.mExtraBasic.accuracy = dataReaderBigEndian.readFloat();
            this.mExtraBasic.hasBearing = dataReaderBigEndian.readBoolean();
            this.mExtraBasic.bearing = dataReaderBigEndian.readFloat();
            this.mExtraBasic.hasSpeed = dataReaderBigEndian.readBoolean();
            this.mExtraBasic.speed = dataReaderBigEndian.readFloat();
            if (!this.mExtraBasic.hasData()) {
                this.mExtraBasic = null;
            }
        }
        if (n >= 1 && dataReaderBigEndian.readBoolean()) {
            if (n == 1) {
                this.readSensorVersion1(dataReaderBigEndian);
            }
            else {
                this.mExtraSensor = new ExtraSensor(dataReaderBigEndian);
            }
        }
    }
    
    public void removeAccuracy() {
        if (this.mExtraBasic != null) {
            this.mExtraBasic.accuracy = 0.0f;
            this.mExtraBasic.hasAccuracy = false;
            this.checkExtraBasic();
        }
    }
    
    public void removeAltitude() {
        this.mAltitude = 0.0;
        this.mHasAltitude = false;
    }
    
    public void removeBearing() {
        if (this.mExtraBasic != null) {
            this.mExtraBasic.bearing = 0.0f;
            this.mExtraBasic.hasBearing = false;
            this.checkExtraBasic();
        }
    }
    
    public void removeSensorAll() {
        this.mExtraSensor = null;
    }
    
    public void removeSensorCadence() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.cadence = 0;
            this.mExtraSensor.hasCadence = false;
            this.checkExtraSensor();
        }
    }
    
    public void removeSensorHeartRate() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.hr = 0;
            this.mExtraSensor.hasHr = false;
            this.checkExtraSensor();
        }
    }
    
    public void removeSensorPower() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.power = 0.0f;
            this.mExtraSensor.hasPower = false;
            this.checkExtraSensor();
        }
    }
    
    public void removeSensorSpeed() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.speed = 0.0f;
            this.mExtraSensor.hasSpeed = false;
            this.checkExtraSensor();
        }
    }
    
    public void removeSensorStrides() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.strides = 0;
            this.mExtraSensor.hasStrides = false;
            this.checkExtraSensor();
        }
    }
    
    public void removeSensorTemperature() {
        if (this.mExtraSensor != null) {
            this.mExtraSensor.temperature = 0.0f;
            this.mExtraSensor.hasTemperature = false;
            this.checkExtraSensor();
        }
    }
    
    public void removeSpeed() {
        if (this.mExtraBasic != null) {
            this.mExtraBasic.speed = 0.0f;
            this.mExtraBasic.hasSpeed = false;
            this.checkExtraBasic();
        }
    }
    
    @Override
    public void reset() {
        this.mId = -1L;
        this.provider = null;
        this.time = 0L;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.mExtraBasic = null;
        this.mExtraSensor = null;
    }
    
    public void set(final Location location) {
        this.mId = location.mId;
        this.provider = location.provider;
        this.time = location.time;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.mHasAltitude = location.hasAltitude();
        this.mAltitude = location.getAltitude();
        if (location.mExtraBasic != null && location.mExtraBasic.hasData()) {
            this.mExtraBasic = location.mExtraBasic.clone();
            if (!this.mExtraBasic.hasData()) {
                this.mExtraBasic = null;
            }
        }
        else {
            this.mExtraBasic = null;
        }
        if (location.mExtraSensor != null && location.mExtraSensor.hasData()) {
            this.mExtraSensor = location.mExtraSensor.clone();
            if (!this.mExtraSensor.hasData()) {
                this.mExtraSensor = null;
            }
        }
        else {
            this.mExtraSensor = null;
        }
    }
    
    public void setAccuracy(final float accuracy) {
        if (this.mExtraBasic == null) {
            this.mExtraBasic = new ExtraBasic();
        }
        this.mExtraBasic.accuracy = accuracy;
        this.mExtraBasic.hasAccuracy = true;
    }
    
    public void setAltitude(final double mAltitude) {
        this.mAltitude = mAltitude;
        this.mHasAltitude = true;
    }
    
    public void setBearing(float n) {
        float bearing;
        while (true) {
            bearing = n;
            if (n >= 0.0f) {
                break;
            }
            n += 360.0f;
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
    
    public void setId(final long mId) {
        this.mId = mId;
    }
    
    public Location setLatitude(final double n) {
        double latitude;
        if (n < -90.0) {
            Logger.logE("Location", "setLatitude(" + n + "), " + "invalid latitude", new Exception(""));
            latitude = -90.0;
        }
        else {
            latitude = n;
            if (n > 90.0) {
                Logger.logE("Location", "setLatitude(" + n + "), " + "invalid latitude", new Exception(""));
                latitude = 90.0;
            }
        }
        this.latitude = latitude;
        return this;
    }
    
    public Location setLongitude(final double n) {
        double longitude;
        if (n < -180.0) {
            longitude = n + 360.0;
        }
        else {
            longitude = n;
            if (n > 180.0) {
                longitude = n - 360.0;
            }
        }
        this.longitude = longitude;
        return this;
    }
    
    public Location setProvider(final String provider) {
        if (provider == null) {
            this.provider = "";
        }
        else {
            this.provider = provider;
        }
        return this;
    }
    
    public void setSensorCadence(final int cadence) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.cadence = cadence;
        this.mExtraSensor.hasCadence = true;
    }
    
    public void setSensorHeartRate(final int hr) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.hr = hr;
        this.mExtraSensor.hasHr = true;
    }
    
    public void setSensorPower(final float power) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.power = power;
        this.mExtraSensor.hasPower = true;
    }
    
    public void setSensorSpeed(final float speed) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.speed = speed;
        this.mExtraSensor.hasSpeed = true;
    }
    
    public void setSensorStrides(final int strides) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.strides = strides;
        this.mExtraSensor.hasStrides = true;
    }
    
    public void setSensorTemperature(final float temperature) {
        if (this.mExtraSensor == null) {
            this.mExtraSensor = new ExtraSensor();
        }
        this.mExtraSensor.temperature = temperature;
        this.mExtraSensor.hasTemperature = true;
    }
    
    public void setSpeed(final float speed) {
        if (this.mExtraBasic == null) {
            this.mExtraBasic = new ExtraBasic();
        }
        this.mExtraBasic.speed = speed;
        this.mExtraBasic.hasSpeed = true;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    @Override
    public String toString() {
        return "Location [tag:" + this.provider + ", " + "lon:" + this.longitude + ", " + "lat:" + this.latitude + ", " + "alt:" + this.mAltitude + "]";
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.mId);
        dataWriterBigEndian.writeString(this.provider);
        dataWriterBigEndian.writeLong(this.time);
        dataWriterBigEndian.writeDouble(this.latitude);
        dataWriterBigEndian.writeDouble(this.longitude);
        dataWriterBigEndian.writeBoolean(this.mHasAltitude);
        dataWriterBigEndian.writeDouble(this.mAltitude);
        if (this.mExtraBasic == null || !this.mExtraBasic.hasData()) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeBoolean(this.mExtraBasic.hasAccuracy);
            dataWriterBigEndian.writeFloat(this.mExtraBasic.accuracy);
            dataWriterBigEndian.writeBoolean(this.mExtraBasic.hasBearing);
            dataWriterBigEndian.writeFloat(this.mExtraBasic.bearing);
            dataWriterBigEndian.writeBoolean(this.mExtraBasic.hasSpeed);
            dataWriterBigEndian.writeFloat(this.mExtraBasic.speed);
        }
        if (this.mExtraSensor == null || !this.mExtraSensor.hasData()) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.mExtraSensor.write(dataWriterBigEndian);
        }
    }
    
    private static class ExtraBasic implements Cloneable
    {
        float accuracy;
        float bearing;
        boolean hasAccuracy;
        boolean hasBearing;
        boolean hasSpeed;
        float speed;
        
        ExtraBasic() {
            this.hasSpeed = false;
            this.speed = 0.0f;
            this.hasBearing = false;
            this.bearing = 0.0f;
            this.hasAccuracy = false;
            this.accuracy = 0.0f;
        }
        
        public ExtraBasic clone() {
            final ExtraBasic extraBasic = new ExtraBasic();
            extraBasic.hasSpeed = this.hasSpeed;
            extraBasic.speed = this.speed;
            extraBasic.hasBearing = this.hasBearing;
            extraBasic.bearing = this.bearing;
            extraBasic.hasAccuracy = this.hasAccuracy;
            extraBasic.accuracy = this.accuracy;
            return extraBasic;
        }
        
        boolean hasData() {
            return this.hasSpeed || this.hasBearing || this.hasAccuracy;
        }
        
        @Override
        public String toString() {
            return Utils.toString(this, "    ");
        }
    }
    
    private static class ExtraSensor extends Storable implements Cloneable
    {
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
        int hr;
        float power;
        float speed;
        int strides;
        float temperature;
        
        public ExtraSensor() {
        }
        
        public ExtraSensor(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            super(dataReaderBigEndian);
        }
        
        public ExtraSensor clone() {
            final ExtraSensor extraSensor = new ExtraSensor();
            extraSensor.hasHr = this.hasHr;
            extraSensor.hr = this.hr;
            extraSensor.hasCadence = this.hasCadence;
            extraSensor.cadence = this.cadence;
            extraSensor.hasSpeed = this.hasSpeed;
            extraSensor.speed = this.speed;
            extraSensor.hasPower = this.hasPower;
            extraSensor.power = this.power;
            extraSensor.hasStrides = this.hasStrides;
            extraSensor.strides = this.strides;
            extraSensor.hasTemperature = this.hasTemperature;
            extraSensor.temperature = this.temperature;
            return extraSensor;
        }
        
        @Override
        protected int getVersion() {
            return 1;
        }
        
        boolean hasData() {
            return this.hasHr || this.hasCadence || this.hasSpeed || this.hasPower || this.hasStrides || this.hasTemperature;
        }
        
        @Override
        protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.hasHr = dataReaderBigEndian.readBoolean();
            this.hr = dataReaderBigEndian.readInt();
            this.hasCadence = dataReaderBigEndian.readBoolean();
            this.cadence = dataReaderBigEndian.readInt();
            this.hasSpeed = dataReaderBigEndian.readBoolean();
            this.speed = dataReaderBigEndian.readFloat();
            this.hasPower = dataReaderBigEndian.readBoolean();
            this.power = dataReaderBigEndian.readFloat();
            this.hasStrides = dataReaderBigEndian.readBoolean();
            this.strides = dataReaderBigEndian.readInt();
            this.hasBattery = dataReaderBigEndian.readBoolean();
            this.battery = dataReaderBigEndian.readInt();
            if (n >= 1) {
                this.hasTemperature = dataReaderBigEndian.readBoolean();
                this.temperature = dataReaderBigEndian.readFloat();
            }
        }
        
        @Override
        public void reset() {
            this.hasHr = false;
            this.hr = 0;
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
        
        @Override
        public String toString() {
            return Utils.toString(this, "    ");
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeBoolean(this.hasHr);
            dataWriterBigEndian.writeInt(this.hr);
            dataWriterBigEndian.writeBoolean(this.hasCadence);
            dataWriterBigEndian.writeInt(this.cadence);
            dataWriterBigEndian.writeBoolean(this.hasSpeed);
            dataWriterBigEndian.writeFloat(this.speed);
            dataWriterBigEndian.writeBoolean(this.hasPower);
            dataWriterBigEndian.writeFloat(this.power);
            dataWriterBigEndian.writeBoolean(this.hasStrides);
            dataWriterBigEndian.writeInt(this.strides);
            dataWriterBigEndian.writeBoolean(this.hasBattery);
            dataWriterBigEndian.writeInt(this.battery);
            dataWriterBigEndian.writeBoolean(this.hasTemperature);
            dataWriterBigEndian.writeFloat(this.temperature);
        }
    }
}
