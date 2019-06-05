// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.graphics.Bitmap;

public class FaviconModel
{
    Bitmap originalIcon;
    int type;
    String url;
    
    FaviconModel(final String url, final int type, final Bitmap originalIcon) {
        this.url = url;
        this.type = type;
        this.originalIcon = originalIcon;
    }
}
