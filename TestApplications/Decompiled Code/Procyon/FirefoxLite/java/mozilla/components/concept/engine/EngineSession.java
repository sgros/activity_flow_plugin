// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.concept.engine;

import mozilla.components.support.base.observer.Observable;

public abstract class EngineSession implements Observable<Observer>
{
    public interface Observer
    {
        void onExternalResource(final String p0, final String p1, final Long p2, final String p3, final String p4, final String p5);
        
        void onFindResult(final int p0, final int p1, final boolean p2);
        
        void onLoadingStateChange(final boolean p0);
        
        void onLocationChange(final String p0);
        
        void onNavigationStateChange(final Boolean p0, final Boolean p1);
        
        void onProgress(final int p0);
        
        void onSecurityChange(final boolean p0, final String p1, final String p2);
        
        void onTitleChange(final String p0);
        
        public static final class DefaultImpls
        {
        }
    }
}
