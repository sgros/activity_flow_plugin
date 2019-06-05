// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.provider.BaseColumns;
import android.net.Uri;

public class HistoryContract
{
    public static final Uri AUTHORITY_URI;
    
    static {
        AUTHORITY_URI = Uri.parse("content://org.mozilla.rocket.provider.historyprovider");
    }
    
    public static final class BrowsingHistory implements BaseColumns
    {
        public static final Uri CONTENT_URI;
        
        static {
            CONTENT_URI = Uri.withAppendedPath(HistoryContract.AUTHORITY_URI, "browsing_history");
        }
    }
}
