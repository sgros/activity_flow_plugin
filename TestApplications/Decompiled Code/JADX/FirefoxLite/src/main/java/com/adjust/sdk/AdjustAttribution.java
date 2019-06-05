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
    private static final long serialVersionUID = 1;
    public String adgroup;
    public String adid;
    public String campaign;
    public String clickLabel;
    public String creative;
    public String network;
    public String trackerName;
    public String trackerToken;

    public static AdjustAttribution fromJson(JSONObject jSONObject, String str) {
        if (jSONObject == null) {
            return null;
        }
        AdjustAttribution adjustAttribution = new AdjustAttribution();
        adjustAttribution.trackerToken = jSONObject.optString("tracker_token", null);
        adjustAttribution.trackerName = jSONObject.optString("tracker_name", null);
        adjustAttribution.network = jSONObject.optString("network", null);
        adjustAttribution.campaign = jSONObject.optString("campaign", null);
        adjustAttribution.adgroup = jSONObject.optString("adgroup", null);
        adjustAttribution.creative = jSONObject.optString("creative", null);
        adjustAttribution.clickLabel = jSONObject.optString("click_label", null);
        adjustAttribution.adid = str;
        return adjustAttribution;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdjustAttribution adjustAttribution = (AdjustAttribution) obj;
        return Util.equalString(this.trackerToken, adjustAttribution.trackerToken) && Util.equalString(this.trackerName, adjustAttribution.trackerName) && Util.equalString(this.network, adjustAttribution.network) && Util.equalString(this.campaign, adjustAttribution.campaign) && Util.equalString(this.adgroup, adjustAttribution.adgroup) && Util.equalString(this.creative, adjustAttribution.creative) && Util.equalString(this.clickLabel, adjustAttribution.clickLabel) && Util.equalString(this.adid, adjustAttribution.adid);
    }

    public int hashCode() {
        return ((((((((((((((629 + Util.hashString(this.trackerToken)) * 37) + Util.hashString(this.trackerName)) * 37) + Util.hashString(this.network)) * 37) + Util.hashString(this.campaign)) * 37) + Util.hashString(this.adgroup)) * 37) + Util.hashString(this.creative)) * 37) + Util.hashString(this.clickLabel)) * 37) + Util.hashString(this.adid);
    }

    public String toString() {
        return String.format(Locale.US, "tt:%s tn:%s net:%s cam:%s adg:%s cre:%s cl:%s adid:%s", new Object[]{this.trackerToken, this.trackerName, this.network, this.campaign, this.adgroup, this.creative, this.clickLabel, this.adid});
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
    }
}
