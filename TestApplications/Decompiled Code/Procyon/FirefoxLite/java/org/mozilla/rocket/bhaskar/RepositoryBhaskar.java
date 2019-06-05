// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.bhaskar;

import java.util.Locale;
import org.json.JSONException;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import org.mozilla.lite.partner.Repository;

public class RepositoryBhaskar extends Repository<BhaskarItem>
{
    static Parser<BhaskarItem> PARSER;
    
    static {
        RepositoryBhaskar.PARSER = (Parser<BhaskarItem>)_$$Lambda$RepositoryBhaskar$6W7SyoJU81_IckXVButtHQnV0FA.INSTANCE;
    }
    
    public RepositoryBhaskar(final Context context, final String s) {
        super(context, null, 3, null, null, "bhaskar", s, 1, RepositoryBhaskar.PARSER, true);
    }
    
    @Override
    protected String getSubscriptionUrl(final int i) {
        return String.format(Locale.US, this.subscriptionUrl, 30, 521, i);
    }
}
