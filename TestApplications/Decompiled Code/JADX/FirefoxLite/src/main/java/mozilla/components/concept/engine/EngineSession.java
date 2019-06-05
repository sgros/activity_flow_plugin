package mozilla.components.concept.engine;

import mozilla.components.support.base.observer.Observable;

/* compiled from: EngineSession.kt */
public abstract class EngineSession implements Observable<Observer> {

    /* compiled from: EngineSession.kt */
    public interface Observer {

        /* compiled from: EngineSession.kt */
        public static final class DefaultImpls {
            public static /* bridge */ /* synthetic */ void onSecurityChange$default(Observer observer, boolean z, String str, String str2, int i, Object obj) {
                if (obj == null) {
                    if ((i & 2) != 0) {
                        str = (String) null;
                    }
                    if ((i & 4) != 0) {
                        str2 = (String) null;
                    }
                    observer.onSecurityChange(z, str, str2);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onSecurityChange");
            }
        }

        void onExternalResource(String str, String str2, Long l, String str3, String str4, String str5);

        void onFindResult(int i, int i2, boolean z);

        void onLoadingStateChange(boolean z);

        void onLocationChange(String str);

        void onNavigationStateChange(Boolean bool, Boolean bool2);

        void onProgress(int i);

        void onSecurityChange(boolean z, String str, String str2);

        void onTitleChange(String str);
    }
}
