package com.adjust.sdk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Locale;
import org.json.JSONObject;

public class AdjustAttribution implements Serializable {
   private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("trackerToken", String.class), new ObjectStreamField("trackerName", String.class), new ObjectStreamField("network", String.class), new ObjectStreamField("campaign", String.class), new ObjectStreamField("adgroup", String.class), new ObjectStreamField("creative", String.class), new ObjectStreamField("clickLabel", String.class), new ObjectStreamField("adid", String.class)};
   private static final long serialVersionUID = 1L;
   public String adgroup;
   public String adid;
   public String campaign;
   public String clickLabel;
   public String creative;
   public String network;
   public String trackerName;
   public String trackerToken;

   public static AdjustAttribution fromJson(JSONObject var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         AdjustAttribution var2 = new AdjustAttribution();
         var2.trackerToken = var0.optString("tracker_token", (String)null);
         var2.trackerName = var0.optString("tracker_name", (String)null);
         var2.network = var0.optString("network", (String)null);
         var2.campaign = var0.optString("campaign", (String)null);
         var2.adgroup = var0.optString("adgroup", (String)null);
         var2.creative = var0.optString("creative", (String)null);
         var2.clickLabel = var0.optString("click_label", (String)null);
         var2.adid = var1;
         return var2;
      }
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      var1.defaultReadObject();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AdjustAttribution var2 = (AdjustAttribution)var1;
         if (!Util.equalString(this.trackerToken, var2.trackerToken)) {
            return false;
         } else if (!Util.equalString(this.trackerName, var2.trackerName)) {
            return false;
         } else if (!Util.equalString(this.network, var2.network)) {
            return false;
         } else if (!Util.equalString(this.campaign, var2.campaign)) {
            return false;
         } else if (!Util.equalString(this.adgroup, var2.adgroup)) {
            return false;
         } else if (!Util.equalString(this.creative, var2.creative)) {
            return false;
         } else if (!Util.equalString(this.clickLabel, var2.clickLabel)) {
            return false;
         } else {
            return Util.equalString(this.adid, var2.adid);
         }
      }
   }

   public int hashCode() {
      return (((((((629 + Util.hashString(this.trackerToken)) * 37 + Util.hashString(this.trackerName)) * 37 + Util.hashString(this.network)) * 37 + Util.hashString(this.campaign)) * 37 + Util.hashString(this.adgroup)) * 37 + Util.hashString(this.creative)) * 37 + Util.hashString(this.clickLabel)) * 37 + Util.hashString(this.adid);
   }

   public String toString() {
      return String.format(Locale.US, "tt:%s tn:%s net:%s cam:%s adg:%s cre:%s cl:%s adid:%s", this.trackerToken, this.trackerName, this.network, this.campaign, this.adgroup, this.creative, this.clickLabel, this.adid);
   }
}
