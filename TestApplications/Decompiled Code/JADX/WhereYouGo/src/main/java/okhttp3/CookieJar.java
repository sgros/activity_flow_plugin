package okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
    public static final CookieJar NO_COOKIES = new C03311();

    /* renamed from: okhttp3.CookieJar$1 */
    static class C03311 implements CookieJar {
        C03311() {
        }

        public void saveFromResponse(HttpUrl url, List<Cookie> list) {
        }

        public List<Cookie> loadForRequest(HttpUrl url) {
            return Collections.emptyList();
        }
    }

    List<Cookie> loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
}
