// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.partner;

import android.util.MalformedJsonException;
import org.json.JSONException;
import org.json.JSONObject;

class Activation
{
    final long duration;
    final String id;
    final String owner;
    final String url;
    final long version;
    
    private Activation(final JSONObject jsonObject) throws JSONException {
        this.owner = jsonObject.getString("owner");
        this.version = jsonObject.getInt("version");
        this.id = jsonObject.getString("id");
        this.duration = jsonObject.getLong("duration");
        this.url = jsonObject.getString("url");
    }
    
    static Activation from(final JSONObject jsonObject) throws MalformedJsonException {
        try {
            return new Activation(jsonObject);
        }
        catch (JSONException ex) {
            throw new MalformedJsonException("Activation information invalid");
        }
    }
    
    boolean matchKeys(final String[] array) {
        final int n = false ? 1 : 0;
        if (array != null && array.length == 3) {
            final boolean b = array[0] != null && array[0].equals(this.owner);
            final boolean b2 = array[1] != null && array[1].equals(String.valueOf(this.version));
            boolean b3 = n != 0;
            if (array[2] != null) {
                b3 = (n != 0);
                if (array[2].equals(this.id)) {
                    b3 = true;
                }
            }
            return b & b2 & b3;
        }
        return false;
    }
}
