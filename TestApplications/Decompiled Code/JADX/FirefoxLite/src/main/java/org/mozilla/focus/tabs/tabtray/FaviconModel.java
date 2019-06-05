package org.mozilla.focus.tabs.tabtray;

import android.graphics.Bitmap;

public class FaviconModel {
    Bitmap originalIcon;
    int type;
    String url;

    FaviconModel(String str, int i, Bitmap bitmap) {
        this.url = str;
        this.type = i;
        this.originalIcon = bitmap;
    }
}
