package org.mozilla.focus.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class DownloadContract {
    public static final Uri AUTHORITY_URI = Uri.parse("content://org.mozilla.rocket.provider.downloadprovider");

    public static final class Download implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(DownloadContract.AUTHORITY_URI, "download_info");
    }
}
