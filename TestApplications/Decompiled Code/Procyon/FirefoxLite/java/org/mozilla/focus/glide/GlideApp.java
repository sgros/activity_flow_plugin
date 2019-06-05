// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.glide;

import com.bumptech.glide.Glide;
import android.app.Activity;

public final class GlideApp
{
    public static GlideRequests with(final Activity activity) {
        return (GlideRequests)Glide.with(activity);
    }
}
