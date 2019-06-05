package org.mozilla.rocket.home.pinsite;

import java.util.List;
import org.mozilla.focus.history.model.Site;

public interface PinSiteDelegate {
   List getPinSites();

   boolean isEnabled();

   boolean isFirstTimeEnable();

   boolean isPinned(Site var1);

   void pin(Site var1);

   void unpinned(Site var1);
}
