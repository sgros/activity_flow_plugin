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
   private final Set cookieStore = new LinkedHashSet();

   public List loadForRequest(HttpUrl var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.cookieStore.iterator();

      while(var3.hasNext()) {
         Cookie var4 = (Cookie)var3.next();
         if (var4.expiresAt() < System.currentTimeMillis()) {
            var3.remove();
         } else if (var4.matches(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public void saveFromResponse(HttpUrl var1, List var2) {
      this.cookieStore.addAll(var2);
   }
}
