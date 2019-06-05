// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.network;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import java.util.LinkedHashSet;
import okhttp3.Cookie;
import java.util.Set;
import okhttp3.CookieJar;

class NonPersistentCookieJar implements CookieJar
{
    private final Set<Cookie> cookieStore;
    
    NonPersistentCookieJar() {
        this.cookieStore = new LinkedHashSet<Cookie>();
    }
    
    @Override
    public List<Cookie> loadForRequest(final HttpUrl httpUrl) {
        final ArrayList<Cookie> list = new ArrayList<Cookie>();
        final Iterator<Cookie> iterator = this.cookieStore.iterator();
        while (iterator.hasNext()) {
            final Cookie cookie = iterator.next();
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                iterator.remove();
            }
            else {
                if (!cookie.matches(httpUrl)) {
                    continue;
                }
                list.add(cookie);
            }
        }
        return list;
    }
    
    @Override
    public void saveFromResponse(final HttpUrl httpUrl, final List<Cookie> list) {
        this.cookieStore.addAll(list);
    }
}
