package org.mozilla.rocket.home.pinsite;

import java.util.List;
import org.mozilla.focus.history.model.Site;

/* compiled from: PinSiteManager.kt */
public interface PinSiteDelegate {
    List<Site> getPinSites();

    boolean isEnabled();

    boolean isFirstTimeEnable();

    boolean isPinned(Site site);

    void pin(Site site);

    void unpinned(Site site);
}
