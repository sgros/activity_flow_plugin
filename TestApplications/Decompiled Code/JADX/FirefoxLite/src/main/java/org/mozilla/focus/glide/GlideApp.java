package org.mozilla.focus.glide;

import android.app.Activity;
import com.bumptech.glide.Glide;

public final class GlideApp {
    public static GlideRequests with(Activity activity) {
        return (GlideRequests) Glide.with(activity);
    }
}
