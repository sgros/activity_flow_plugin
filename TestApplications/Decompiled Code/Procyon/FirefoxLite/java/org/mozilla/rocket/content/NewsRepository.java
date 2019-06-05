// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

import org.mozilla.lite.newspoint.RepositoryNewsPoint;
import org.mozilla.rocket.bhaskar.RepositoryBhaskar;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.annotation.SuppressLint;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.lite.partner.Repository;

public final class NewsRepository
{
    public static final Companion Companion;
    @SuppressLint({ "StaticFieldLeak" })
    private static volatile Repository<? extends NewsItem> INSTANCE;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final /* synthetic */ Repository access$getINSTANCE$cp() {
        return NewsRepository.INSTANCE;
    }
    
    public static final /* synthetic */ void access$setINSTANCE$cp(final Repository instance) {
        NewsRepository.INSTANCE = (Repository<? extends NewsItem>)instance;
    }
    
    public static final void reset() {
        NewsRepository.Companion.reset();
    }
    
    public static final void resetSubscriptionUrl(final String s) {
        NewsRepository.Companion.resetSubscriptionUrl(s);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        private final Repository<? extends NewsItem> buildRepository(final Context context) {
            final NewsSourceManager instance = NewsSourceManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(instance, "NewsSourceManager.getInstance()");
            Object o;
            if (Intrinsics.areEqual(instance.getNewsSource(), "DainikBhaskar.com")) {
                final NewsSourceManager instance2 = NewsSourceManager.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance2, "NewsSourceManager.getInstance()");
                o = new RepositoryBhaskar(context, instance2.getNewsSourceUrl());
            }
            else {
                final NewsSourceManager instance3 = NewsSourceManager.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance3, "NewsSourceManager.getInstance()");
                o = new RepositoryNewsPoint(context, instance3.getNewsSourceUrl());
            }
            return (Repository<? extends NewsItem>)o;
        }
        
        public final Repository<? extends NewsItem> getInstance(Context applicationContext) {
            final Repository access$getINSTANCE$cp = NewsRepository.access$getINSTANCE$cp();
            if (access$getINSTANCE$cp != null) {
                return (Repository<? extends NewsItem>)access$getINSTANCE$cp;
            }
            // monitorenter(this)
            Label_0061: {
                if (applicationContext == null) {
                    break Label_0061;
                }
                try {
                    final Repository access$getINSTANCE$cp2 = NewsRepository.access$getINSTANCE$cp();
                    Repository<? extends NewsItem> buildRepository;
                    if (access$getINSTANCE$cp2 != null) {
                        buildRepository = (Repository<? extends NewsItem>)access$getINSTANCE$cp2;
                    }
                    else {
                        final Companion companion = NewsRepository.Companion;
                        applicationContext = applicationContext.getApplicationContext();
                        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "context.applicationContext");
                        buildRepository = companion.buildRepository(applicationContext);
                        NewsRepository.access$setINSTANCE$cp(buildRepository);
                    }
                    return buildRepository;
                    throw new IllegalStateException("can't create Content Repository with null context");
                }
                finally {
                }
                // monitorexit(this)
            }
        }
        
        public final boolean isEmpty() {
            return NewsRepository.access$getINSTANCE$cp() == null;
        }
        
        public final void reset() {
            final Repository access$getINSTANCE$cp = NewsRepository.access$getINSTANCE$cp();
            if (access$getINSTANCE$cp != null) {
                access$getINSTANCE$cp.reset();
            }
            NewsRepository.access$setINSTANCE$cp(null);
        }
        
        public final void resetSubscriptionUrl(final String subscriptionUrl) {
            Intrinsics.checkParameterIsNotNull(subscriptionUrl, "subscriptionUrl");
            final Repository access$getINSTANCE$cp = NewsRepository.access$getINSTANCE$cp();
            if (access$getINSTANCE$cp != null) {
                access$getINSTANCE$cp.setSubscriptionUrl(subscriptionUrl);
            }
        }
    }
}
