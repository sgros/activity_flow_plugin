package org.mozilla.rocket.content;

import android.annotation.SuppressLint;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.newspoint.RepositoryNewsPoint;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.lite.partner.Repository;
import org.mozilla.rocket.bhaskar.RepositoryBhaskar;

/* compiled from: NewsRepository.kt */
public final class NewsRepository {
    public static final Companion Companion = new Companion();
    @SuppressLint({"StaticFieldLeak"})
    private static volatile Repository<? extends NewsItem> INSTANCE;

    /* compiled from: NewsRepository.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Repository<? extends NewsItem> getInstance(Context context) {
            Repository<? extends NewsItem> access$getINSTANCE$cp = NewsRepository.INSTANCE;
            if (access$getINSTANCE$cp == null) {
                synchronized (this) {
                    if (context != null) {
                        access$getINSTANCE$cp = NewsRepository.INSTANCE;
                        if (access$getINSTANCE$cp == null) {
                            Companion companion = NewsRepository.Companion;
                            context = context.getApplicationContext();
                            Intrinsics.checkExpressionValueIsNotNull(context, "context.applicationContext");
                            Repository<? extends NewsItem> buildRepository = companion.buildRepository(context);
                            NewsRepository.INSTANCE = buildRepository;
                            access$getINSTANCE$cp = buildRepository;
                        }
                    } else {
                        throw new IllegalStateException("can't create Content Repository with null context");
                    }
                }
            }
            return access$getINSTANCE$cp;
        }

        public final void reset() {
            Repository access$getINSTANCE$cp = NewsRepository.INSTANCE;
            if (access$getINSTANCE$cp != null) {
                access$getINSTANCE$cp.reset();
            }
            NewsRepository.INSTANCE = (Repository) null;
        }

        public final void resetSubscriptionUrl(String str) {
            Intrinsics.checkParameterIsNotNull(str, "subscriptionUrl");
            Repository access$getINSTANCE$cp = NewsRepository.INSTANCE;
            if (access$getINSTANCE$cp != null) {
                access$getINSTANCE$cp.setSubscriptionUrl(str);
            }
        }

        public final boolean isEmpty() {
            return NewsRepository.INSTANCE == null;
        }

        private final Repository<? extends NewsItem> buildRepository(Context context) {
            NewsSourceManager instance = NewsSourceManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(instance, "NewsSourceManager.getInstance()");
            NewsSourceManager instance2;
            if (Intrinsics.areEqual(instance.getNewsSource(), "DainikBhaskar.com")) {
                instance2 = NewsSourceManager.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance2, "NewsSourceManager.getInstance()");
                return new RepositoryBhaskar(context, instance2.getNewsSourceUrl());
            }
            instance2 = NewsSourceManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(instance2, "NewsSourceManager.getInstance()");
            return new RepositoryNewsPoint(context, instance2.getNewsSourceUrl());
        }
    }

    public static final void reset() {
        Companion.reset();
    }

    public static final void resetSubscriptionUrl(String str) {
        Companion.resetSubscriptionUrl(str);
    }
}
