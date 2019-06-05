package okhttp3;

import java.io.IOException;

public interface Authenticator {
    public static final Authenticator NONE = new C03251();

    /* renamed from: okhttp3.Authenticator$1 */
    static class C03251 implements Authenticator {
        C03251() {
        }

        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    Request authenticate(Route route, Response response) throws IOException;
}
