package com.bumptech.glide;

import android.content.Context;
import android.util.Log;
import java.util.Collections;
import java.util.Set;
import org.mozilla.focus.glide.FocusGlideModule;

final class GeneratedAppGlideModuleImpl extends GeneratedAppGlideModule {
   private final FocusGlideModule appGlideModule = new FocusGlideModule();

   GeneratedAppGlideModuleImpl() {
      if (Log.isLoggable("Glide", 3)) {
         Log.d("Glide", "Discovered AppGlideModule from annotation: org.mozilla.focus.glide.FocusGlideModule");
      }

   }

   public void applyOptions(Context var1, GlideBuilder var2) {
      this.appGlideModule.applyOptions(var1, var2);
   }

   public Set getExcludedModuleClasses() {
      return Collections.emptySet();
   }

   GeneratedRequestManagerFactory getRequestManagerFactory() {
      return new GeneratedRequestManagerFactory();
   }

   public boolean isManifestParsingEnabled() {
      return this.appGlideModule.isManifestParsingEnabled();
   }

   public void registerComponents(Context var1, Glide var2, Registry var3) {
      this.appGlideModule.registerComponents(var1, var2, var3);
   }
}
