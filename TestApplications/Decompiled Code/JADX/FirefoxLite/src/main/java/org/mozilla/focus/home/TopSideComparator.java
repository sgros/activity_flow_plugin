package org.mozilla.focus.home;

import java.io.Serializable;
import java.util.Comparator;
import org.mozilla.focus.history.model.Site;

public class TopSideComparator implements Serializable, Comparator<Site> {
    public int compare(Site site, Site site2) {
        int i = (site.getViewCount() > site2.getViewCount() ? 1 : (site.getViewCount() == site2.getViewCount() ? 0 : -1));
        if (i > 0) {
            return -1;
        }
        if (i < 0) {
            return 1;
        }
        int i2 = (site.getLastViewTimestamp() > site2.getLastViewTimestamp() ? 1 : (site.getLastViewTimestamp() == site2.getLastViewTimestamp() ? 0 : -1));
        if (i2 > 0) {
            return -1;
        }
        if (i2 < 0) {
            return 1;
        }
        return 0;
    }
}
