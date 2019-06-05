package org.mozilla.rocket.home.pinsite;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.history.model.Site;

public final class PinSiteManager implements PinSiteDelegate {
   private final PinSiteDelegate pinSiteDelegate;

   public PinSiteManager(PinSiteDelegate var1) {
      Intrinsics.checkParameterIsNotNull(var1, "pinSiteDelegate");
      super();
      this.pinSiteDelegate = var1;
   }

   public List getPinSites() {
      return this.pinSiteDelegate.getPinSites();
   }

   public boolean isEnabled() {
      return this.pinSiteDelegate.isEnabled();
   }

   public boolean isFirstTimeEnable() {
      return this.pinSiteDelegate.isFirstTimeEnable();
   }

   public boolean isPinned(Site var1) {
      Intrinsics.checkParameterIsNotNull(var1, "site");
      return this.pinSiteDelegate.isPinned(var1);
   }

   public void pin(Site var1) {
      Intrinsics.checkParameterIsNotNull(var1, "site");
      this.pinSiteDelegate.pin(var1);
   }

   public void unpinned(Site var1) {
      Intrinsics.checkParameterIsNotNull(var1, "site");
      this.pinSiteDelegate.unpinned(var1);
   }
}
