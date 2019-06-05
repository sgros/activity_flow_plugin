// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import org.mozilla.lite.partner.NewsItem;
import java.util.List;
import android.arch.lifecycle.LifecycleOwner;

public interface NewsViewContract
{
    LifecycleOwner getViewLifecycleOwner();
    
    void updateNews(final List<? extends NewsItem> p0);
}
