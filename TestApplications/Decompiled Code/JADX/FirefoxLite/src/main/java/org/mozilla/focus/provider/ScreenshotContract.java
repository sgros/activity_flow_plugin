package org.mozilla.focus.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ScreenshotContract {
    public static final Uri AUTHORITY_URI = Uri.parse("content://org.mozilla.rocket.provider.screenshotprovider");

    public static final class Screenshot implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ScreenshotContract.AUTHORITY_URI, "screenshot");
    }
}
