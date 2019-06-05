package org.mozilla.rocket.partner;

import android.util.MalformedJsonException;
import org.json.JSONException;
import org.json.JSONObject;

class Activation {
    final long duration;
    /* renamed from: id */
    final String f54id;
    final String owner;
    final String url;
    final long version;

    private Activation(JSONObject jSONObject) throws JSONException {
        this.owner = jSONObject.getString("owner");
        this.version = (long) jSONObject.getInt("version");
        this.f54id = jSONObject.getString("id");
        this.duration = jSONObject.getLong("duration");
        this.url = jSONObject.getString("url");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean matchKeys(String[] strArr) {
        int i = 0;
        if (strArr == null || strArr.length != 3) {
            return false;
        }
        int i2 = (strArr[0] == null || !strArr[0].equals(this.owner)) ? 0 : 1;
        int i3 = (strArr[1] == null || !strArr[1].equals(String.valueOf(this.version))) ? 0 : 1;
        i2 &= i3;
        if (strArr[2] != null && strArr[2].equals(this.f54id)) {
            i = 1;
        }
        return i2 & i;
    }

    static Activation from(JSONObject jSONObject) throws MalformedJsonException {
        try {
            return new Activation(jSONObject);
        } catch (JSONException unused) {
            throw new MalformedJsonException("Activation information invalid");
        }
    }
}
