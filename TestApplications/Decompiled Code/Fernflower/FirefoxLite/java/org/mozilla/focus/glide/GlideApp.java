package org.mozilla.focus.glide;

import android.app.Activity;
import com.bumptech.glide.Glide;

public final class GlideApp {
   public static GlideRequests with(Activity var0) {
      return (GlideRequests)Glide.with(var0);
   }
}
