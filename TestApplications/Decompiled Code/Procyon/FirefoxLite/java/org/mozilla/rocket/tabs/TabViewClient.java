// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

public class TabViewClient
{
    public boolean handleExternalUrl(final String s) {
        return false;
    }
    
    public void onHttpAuthRequest(final HttpAuthCallback httpAuthCallback, final String s, final String s2) {
    }
    
    public void onPageFinished(final boolean b) {
    }
    
    public void onPageStarted(final String s) {
    }
    
    public void onURLChanged(final String s) {
    }
    
    public void updateFailingUrl(final String s, final boolean b) {
    }
    
    public interface HttpAuthCallback
    {
        void cancel();
        
        void proceed(final String p0, final String p1);
    }
}
