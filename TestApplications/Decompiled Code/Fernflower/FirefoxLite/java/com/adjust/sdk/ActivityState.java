package com.adjust.sdk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.ObjectInputStream.GetField;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

public class ActivityState implements Serializable, Cloneable {
   private static int ORDER_ID_MAXCOUNT;
   private static final ObjectStreamField[] serialPersistentFields;
   private static final long serialVersionUID = 9039439291143138148L;
   protected String adid = null;
   protected boolean askingAttribution = false;
   protected boolean enabled = true;
   protected int eventCount = 0;
   protected long lastActivity = -1L;
   protected long lastInterval = -1L;
   private transient ILogger logger = AdjustFactory.getLogger();
   protected LinkedList orderIds = null;
   protected String pushToken = null;
   protected int sessionCount = 0;
   protected long sessionLength = -1L;
   protected int subsessionCount = -1;
   protected long timeSpent = -1L;
   protected boolean updatePackages = false;
   protected String uuid = Util.createUuid();

   static {
      serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("uuid", String.class), new ObjectStreamField("enabled", Boolean.TYPE), new ObjectStreamField("askingAttribution", Boolean.TYPE), new ObjectStreamField("eventCount", Integer.TYPE), new ObjectStreamField("sessionCount", Integer.TYPE), new ObjectStreamField("subsessionCount", Integer.TYPE), new ObjectStreamField("sessionLength", Long.TYPE), new ObjectStreamField("timeSpent", Long.TYPE), new ObjectStreamField("lastActivity", Long.TYPE), new ObjectStreamField("lastInterval", Long.TYPE), new ObjectStreamField("updatePackages", Boolean.TYPE), new ObjectStreamField("orderIds", LinkedList.class), new ObjectStreamField("pushToken", String.class), new ObjectStreamField("adid", String.class)};
   }

   protected ActivityState() {
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      GetField var2 = var1.readFields();
      this.eventCount = Util.readIntField(var2, "eventCount", 0);
      this.sessionCount = Util.readIntField(var2, "sessionCount", 0);
      this.subsessionCount = Util.readIntField(var2, "subsessionCount", -1);
      this.sessionLength = Util.readLongField(var2, "sessionLength", -1L);
      this.timeSpent = Util.readLongField(var2, "timeSpent", -1L);
      this.lastActivity = Util.readLongField(var2, "lastActivity", -1L);
      this.lastInterval = Util.readLongField(var2, "lastInterval", -1L);
      this.uuid = Util.readStringField(var2, "uuid", (String)null);
      this.enabled = Util.readBooleanField(var2, "enabled", true);
      this.askingAttribution = Util.readBooleanField(var2, "askingAttribution", false);
      this.updatePackages = Util.readBooleanField(var2, "updatePackages", false);
      this.orderIds = (LinkedList)Util.readObjectField(var2, "orderIds", (Object)null);
      this.pushToken = Util.readStringField(var2, "pushToken", (String)null);
      this.adid = Util.readStringField(var2, "adid", (String)null);
      if (this.uuid == null) {
         this.uuid = Util.createUuid();
      }

   }

   private static String stamp(long var0) {
      Calendar.getInstance().setTimeInMillis(var0);
      return String.format(Locale.US, "%02d:%02d:%02d", 11, 12, 13);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
   }

   protected void addOrderId(String var1) {
      if (this.orderIds == null) {
         this.orderIds = new LinkedList();
      }

      if (this.orderIds.size() >= ORDER_ID_MAXCOUNT) {
         this.orderIds.removeLast();
      }

      this.orderIds.addFirst(var1);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         ActivityState var2 = (ActivityState)var1;
         if (!Util.equalString(this.uuid, var2.uuid)) {
            return false;
         } else if (!Util.equalBoolean(this.enabled, var2.enabled)) {
            return false;
         } else if (!Util.equalBoolean(this.askingAttribution, var2.askingAttribution)) {
            return false;
         } else if (!Util.equalInt(this.eventCount, var2.eventCount)) {
            return false;
         } else if (!Util.equalInt(this.sessionCount, var2.sessionCount)) {
            return false;
         } else if (!Util.equalInt(this.subsessionCount, var2.subsessionCount)) {
            return false;
         } else if (!Util.equalLong(this.sessionLength, var2.sessionLength)) {
            return false;
         } else if (!Util.equalLong(this.timeSpent, var2.timeSpent)) {
            return false;
         } else if (!Util.equalLong(this.lastInterval, var2.lastInterval)) {
            return false;
         } else if (!Util.equalBoolean(this.updatePackages, var2.updatePackages)) {
            return false;
         } else if (!Util.equalObject(this.orderIds, var2.orderIds)) {
            return false;
         } else if (!Util.equalString(this.pushToken, var2.pushToken)) {
            return false;
         } else {
            return Util.equalString(this.adid, var2.adid);
         }
      }
   }

   protected boolean findOrderId(String var1) {
      return this.orderIds == null ? false : this.orderIds.contains(var1);
   }

   public int hashCode() {
      return ((((((((((((629 + Util.hashString(this.uuid)) * 37 + Util.hashBoolean(this.enabled)) * 37 + Util.hashBoolean(this.askingAttribution)) * 37 + this.eventCount) * 37 + this.sessionCount) * 37 + this.subsessionCount) * 37 + Util.hashLong(this.sessionLength)) * 37 + Util.hashLong(this.timeSpent)) * 37 + Util.hashLong(this.lastInterval)) * 37 + Util.hashBoolean(this.updatePackages)) * 37 + Util.hashObject(this.orderIds)) * 37 + Util.hashString(this.pushToken)) * 37 + Util.hashString(this.adid);
   }

   protected void resetSessionAttributes(long var1) {
      this.subsessionCount = 1;
      this.sessionLength = 0L;
      this.timeSpent = 0L;
      this.lastActivity = var1;
      this.lastInterval = -1L;
   }

   public String toString() {
      return String.format(Locale.US, "ec:%d sc:%d ssc:%d sl:%.1f ts:%.1f la:%s uuid:%s", this.eventCount, this.sessionCount, this.subsessionCount, (double)this.sessionLength / 1000.0D, (double)this.timeSpent / 1000.0D, stamp(this.lastActivity), this.uuid);
   }
}
