// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.provider.BaseColumns;
import android.net.Uri;

public class DownloadContract
{
    public static final Uri AUTHORITY_URI;
    
    static {
        AUTHORITY_URI = Uri.parse("content://org.mozilla.rocket.provider.downloadprovider");
    }
    
    public static final class Download implements BaseColumns
    {
        public static final Uri CONTENT_URI;
        
        static {
            CONTENT_URI = Uri.withAppendedPath(DownloadContract.AUTHORITY_URI, "download_info");
        }
    }
}
