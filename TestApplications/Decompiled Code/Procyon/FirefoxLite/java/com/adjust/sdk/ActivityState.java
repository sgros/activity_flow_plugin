// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Calendar;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.io.ObjectStreamField;
import java.io.Serializable;

public class ActivityState implements Serializable, Cloneable
{
    private static int ORDER_ID_MAXCOUNT = 10;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long serialVersionUID = 9039439291143138148L;
    protected String adid;
    protected boolean askingAttribution;
    protected boolean enabled;
    protected int eventCount;
    protected long lastActivity;
    protected long lastInterval;
    private transient ILogger logger;
    protected LinkedList<String> orderIds;
    protected String pushToken;
    protected int sessionCount;
    protected long sessionLength;
    protected int subsessionCount;
    protected long timeSpent;
    protected boolean updatePackages;
    protected String uuid;
    
    static {
        serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("uuid", String.class), new ObjectStreamField("enabled", Boolean.TYPE), new ObjectStreamField("askingAttribution", Boolean.TYPE), new ObjectStreamField("eventCount", Integer.TYPE), new ObjectStreamField("sessionCount", Integer.TYPE), new ObjectStreamField("subsessionCount", Integer.TYPE), new ObjectStreamField("sessionLength", Long.TYPE), new ObjectStreamField("timeSpent", Long.TYPE), new ObjectStreamField("lastActivity", Long.TYPE), new ObjectStreamField("lastInterval", Long.TYPE), new ObjectStreamField("updatePackages", Boolean.TYPE), new ObjectStreamField("orderIds", LinkedList.class), new ObjectStreamField("pushToken", String.class), new ObjectStreamField("adid", String.class) };
    }
    
    protected ActivityState() {
        this.logger = AdjustFactory.getLogger();
        this.uuid = Util.createUuid();
        this.enabled = true;
        this.askingAttribution = false;
        this.eventCount = 0;
        this.sessionCount = 0;
        this.subsessionCount = -1;
        this.sessionLength = -1L;
        this.timeSpent = -1L;
        this.lastActivity = -1L;
        this.lastInterval = -1L;
        this.updatePackages = false;
        this.orderIds = null;
        this.pushToken = null;
        this.adid = null;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        final ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.eventCount = Util.readIntField(fields, "eventCount", 0);
        this.sessionCount = Util.readIntField(fields, "sessionCount", 0);
        this.subsessionCount = Util.readIntField(fields, "subsessionCount", -1);
        this.sessionLength = Util.readLongField(fields, "sessionLength", -1L);
        this.timeSpent = Util.readLongField(fields, "timeSpent", -1L);
        this.lastActivity = Util.readLongField(fields, "lastActivity", -1L);
        this.lastInterval = Util.readLongField(fields, "lastInterval", -1L);
        this.uuid = Util.readStringField(fields, "uuid", null);
        this.enabled = Util.readBooleanField(fields, "enabled", true);
        this.askingAttribution = Util.readBooleanField(fields, "askingAttribution", false);
        this.updatePackages = Util.readBooleanField(fields, "updatePackages", false);
        this.orderIds = Util.readObjectField(fields, "orderIds", (LinkedList<String>)null);
        this.pushToken = Util.readStringField(fields, "pushToken", null);
        this.adid = Util.readStringField(fields, "adid", null);
        if (this.uuid == null) {
            this.uuid = Util.createUuid();
        }
    }
    
    private static String stamp(final long timeInMillis) {
        Calendar.getInstance().setTimeInMillis(timeInMillis);
        return String.format(Locale.US, "%02d:%02d:%02d", 11, 12, 13);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }
    
    protected void addOrderId(final String e) {
        if (this.orderIds == null) {
            this.orderIds = new LinkedList<String>();
        }
        if (this.orderIds.size() >= ActivityState.ORDER_ID_MAXCOUNT) {
            this.orderIds.removeLast();
        }
        this.orderIds.addFirst(e);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final ActivityState activityState = (ActivityState)o;
        return Util.equalString(this.uuid, activityState.uuid) && Util.equalBoolean(this.enabled, activityState.enabled) && Util.equalBoolean(this.askingAttribution, activityState.askingAttribution) && Util.equalInt(this.eventCount, activityState.eventCount) && Util.equalInt(this.sessionCount, activityState.sessionCount) && Util.equalInt(this.subsessionCount, activityState.subsessionCount) && Util.equalLong(this.sessionLength, activityState.sessionLength) && Util.equalLong(this.timeSpent, activityState.timeSpent) && Util.equalLong(this.lastInterval, activityState.lastInterval) && Util.equalBoolean(this.updatePackages, activityState.updatePackages) && Util.equalObject(this.orderIds, activityState.orderIds) && Util.equalString(this.pushToken, activityState.pushToken) && Util.equalString(this.adid, activityState.adid);
    }
    
    protected boolean findOrderId(final String o) {
        return this.orderIds != null && this.orderIds.contains(o);
    }
    
    @Override
    public int hashCode() {
        return ((((((((((((629 + Util.hashString(this.uuid)) * 37 + Util.hashBoolean(this.enabled)) * 37 + Util.hashBoolean(this.askingAttribution)) * 37 + this.eventCount) * 37 + this.sessionCount) * 37 + this.subsessionCount) * 37 + Util.hashLong(this.sessionLength)) * 37 + Util.hashLong(this.timeSpent)) * 37 + Util.hashLong(this.lastInterval)) * 37 + Util.hashBoolean(this.updatePackages)) * 37 + Util.hashObject(this.orderIds)) * 37 + Util.hashString(this.pushToken)) * 37 + Util.hashString(this.adid);
    }
    
    protected void resetSessionAttributes(final long lastActivity) {
        this.subsessionCount = 1;
        this.sessionLength = 0L;
        this.timeSpent = 0L;
        this.lastActivity = lastActivity;
        this.lastInterval = -1L;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "ec:%d sc:%d ssc:%d sl:%.1f ts:%.1f la:%s uuid:%s", this.eventCount, this.sessionCount, this.subsessionCount, this.sessionLength / 1000.0, this.timeSpent / 1000.0, stamp(this.lastActivity), this.uuid);
    }
}
