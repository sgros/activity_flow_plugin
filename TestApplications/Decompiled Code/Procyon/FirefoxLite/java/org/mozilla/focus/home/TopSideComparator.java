// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import org.mozilla.focus.history.model.Site;
import java.util.Comparator;
import java.io.Serializable;

public class TopSideComparator implements Serializable, Comparator<Site>
{
    @Override
    public int compare(final Site site, final Site site2) {
        final long n = lcmp(site.getViewCount(), site2.getViewCount());
        if (n > 0) {
            return -1;
        }
        if (n < 0) {
            return 1;
        }
        final long n2 = lcmp(site.getLastViewTimestamp(), site2.getLastViewTimestamp());
        if (n2 > 0) {
            return -1;
        }
        if (n2 < 0) {
            return 1;
        }
        return 0;
    }
}
