package menion.android.whereyougo.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

class NonPersistentCookieJar implements CookieJar {
    private final Set<Cookie> cookieStore = new LinkedHashSet();

    NonPersistentCookieJar() {
    }

    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookieStore.addAll(cookies);
    }

    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> matchingCookies = new ArrayList();
        Iterator<Cookie> it = this.cookieStore.iterator();
        while (it.hasNext()) {
            Cookie cookie = (Cookie) it.next();
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                it.remove();
            } else if (cookie.matches(url)) {
                matchingCookies.add(cookie);
            }
        }
        return matchingCookies;
    }
}
